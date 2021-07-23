package StagEngine;

import StagEntities.Artefact;
import StagEntities.Character;
import StagEntities.Furniture;
import StagEntities.Location;
import org.json.simple.*;
import org.json.simple.parser.*;

import com.alexmerz.graphviz.*;
import com.alexmerz.graphviz.objects.*;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;


public class StagParser
{
    String entityFilename;
    String actionsFilename;
    StagModel model;

    public StagParser(StagModel model, String entityFilename, String actionsFilename) {
        this.entityFilename = entityFilename;
        this.actionsFilename = actionsFilename;
        this.model = model;
    }

    // Create and import entities into locations from DOT file
    public void importEntities() {
        try {
            Parser parser = new Parser();
            Reader reader = new FileReader(entityFilename);
            parser.parse(reader);

            ArrayList<Graph> graphs = parser.getGraphs();
            ArrayList<Graph> subgraphs = graphs.get(0).getSubgraphs();
            ArrayList<Graph> locationGraphs = subgraphs.get(0).getSubgraphs();

            for (Graph location : locationGraphs) {
                ArrayList<Node> locationNodes = location.getNodes(false);
                Node theLocationNode = locationNodes.get(0);

                Location newLocation = new Location(theLocationNode.getId().getId(),
                        theLocationNode.getAttribute("description"));
                model.addLocation(theLocationNode.getId().getId(), newLocation);


                ArrayList<Graph> entityGraphs = location.getSubgraphs();
                for (Graph entity : entityGraphs) {
                    populateEntitiesInLocation(newLocation, entity);
                }
            }

            importPaths(subgraphs.get(1));
            updateStartingLocation(locationGraphs);

        } catch(FileNotFoundException fnfe){
            System.out.println(fnfe);
        } catch(com.alexmerz.graphviz.ParseException pe){
            System.out.println(pe);
        }
    }

    private void updateStartingLocation(ArrayList<Graph> locationGraphs) {
        Graph startLocation = locationGraphs.get(0);
        ArrayList<Node> locationNodes = startLocation.getNodes(false);
        Node startLocationNode = locationNodes.get(0);
        model.setStartingLocation(startLocationNode.getId().getId());
    }

    private void populateEntitiesInLocation(Location loc, Graph entity) {
        String entityID = entity.getId().getId();
        ArrayList<Node> entityNodes = entity.getNodes(false);

        switch (entityID) {
            case "artefacts":
                addArtefactsToLocation(loc, entityNodes);
                break;
            case "furniture":
                addFurnitureToLocation(loc, entityNodes);
                break;
            case "characters":
                addCharactersToLocation(loc, entityNodes);
        }
    }

    private void addArtefactsToLocation(Location loc, ArrayList<Node> entityNodes) {
        for (Node node : entityNodes) {
            model.getLocation(loc.getName()).addArtefact(new Artefact(node.getId().getId(),
                    node.getAttribute("description")));
        }
    }

    private void addFurnitureToLocation(Location loc, ArrayList<Node> entityNodes) {
        for (Node node : entityNodes) {
            model.getLocation(loc.getName()).addFurniture(new Furniture(node.getId().getId(),
                    node.getAttribute("description")));
        }
    }

    private void addCharactersToLocation(Location loc, ArrayList<Node> entityNodes) {
        for (Node node : entityNodes) {
            model.getLocation(loc.getName()).addCharacter(new Character(node.getId().getId(),
                    node.getAttribute("description")));
        }
    }

    private void importPaths(Graph pathsList) {
        ArrayList<Edge> paths = pathsList.getEdges();
            for (Edge path : paths){
                String source = path.getSource().getNode().getId().getId();
                String target = path.getTarget().getNode().getId().getId();
                checkIfValidPath(source, target);
                addPathToLocations(source,target);
            }
    }

    private void checkIfValidPath(String source, String target) {
        if (!model.getLocationHashMap().containsKey(source) || !model.getLocationHashMap().containsKey(target)) {
            System.out.printf("There was an error creating a path from %s to %s", source, target);
        }
    }

    private void addPathToLocations(String source, String target) {
        model.getLocation(source).addPath(target);
    }

    // Import permissible actions in the game defined in a JSON file
    public void importActions() {
        JSONParser parser = new JSONParser();

        try {
            Reader reader = new FileReader(actionsFilename);
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            JSONArray actionsArray = (JSONArray) jsonObject.get("actions");

            for (Object object : actionsArray) {
                if (object instanceof JSONObject) {
                    JSONObject actionObject = (JSONObject) object;
                    JSONArray triggers = (JSONArray) actionObject.get("triggers");
                    addActionToMap(actionObject, triggers);
                }
            }
        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe);
        } catch (IOException | ParseException e) {
            System.out.println(e);
        }
    }

    private void addActionToMap(JSONObject actionObject, JSONArray triggers) {
        for (Object trigger : triggers) {
            String triggerWord = trigger.toString();
            model.addTrigger(triggerWord);
            StagAction action = new StagAction(triggerWord);

            JSONArray subjects = (JSONArray) actionObject.get("subjects");
            for (Object subject : subjects) {
                action.addSubject(subject.toString());
                model.addSubject(subject.toString());
            }

            JSONArray consumed = (JSONArray) actionObject.get("consumed");
            for (Object consume : consumed) {
                action.addConsumed(consume.toString());
            }

            JSONArray produced = (JSONArray) actionObject.get("produced");
            for (Object produce : produced) {
                action.addProduced(produce.toString());
            }

            String narration = (String) actionObject.get("narration");
            action.setNarration(narration);

            model.addAction(action);
        }
    }
}
