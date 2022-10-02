package StagEntities;

import java.util.ArrayList;
import java.util.HashMap;

public class Location extends Entity {
    private HashMap<String, Furniture> localFurniture;
    private HashMap<String, Artefact> localArtefacts;
    private HashMap<String, Character> localCharacters;
    private HashMap<String, String> localPlayers;
    private ArrayList<String> permittedPaths;

    public Location(String name, String description) {
        super(name, description);
        localFurniture = new HashMap<>();
        localArtefacts = new HashMap<>();
        localCharacters = new HashMap<>();
        localPlayers = new HashMap<>();
        permittedPaths = new ArrayList<>();
    }

    public void addFurniture(Furniture furniture) {
        localFurniture.put(furniture.getName(), furniture);
    }

    public void addArtefact(Artefact artefact) {
        localArtefacts.put(artefact.getName(), artefact);
    }

    public void addCharacter(Character character) {
        localCharacters.put(character.getName(), character);
    }

    public void addPath(String locationName) {
        permittedPaths.add(locationName);
    }

    public Artefact removeArtefact(String artefactName) {
        return localArtefacts.remove(artefactName);
    }

    public Furniture removeFurniture(String furnitureName) {
        return localFurniture.remove(furnitureName);
    }

    public Character removeCharacter(String characterName) {
        return localCharacters.remove(characterName);
    }

    public void removePath(String locationName) {
        permittedPaths.remove(locationName);
    }

    public boolean isTravelPossibleTo(String locationName) {
        if(permittedPaths.contains(locationName)) {
            return true;
        }
        return false;
    }

    public boolean hasArtefact(String artefactName) {
        if(localArtefacts.containsKey(artefactName)) {
            return true;
        }
        return false;
    }

    public boolean hasSubject(String subjectName) {
        if (localFurniture.containsKey(subjectName) ||
                localArtefacts.containsKey(subjectName) ||
                localCharacters.containsKey(subjectName) ) {
            return true;
        }
        return false;
    }

    public String getEntityType(String entityName) {
        if(localArtefacts.containsKey(entityName)){
            return "artefact";
        }

        if(localFurniture.containsKey(entityName)){
            return "furniture";
        }

        if(localCharacters.containsKey(entityName)){
            return "character";
        }

        return "";
    }

    public String describeLocation() {
        StringBuilder sb = new StringBuilder();

        sb.append("You are in " + super.getDescription() + " You can see:\n");
        describeArtefacts(sb);
        describeFurniture(sb);
        describeCharacters(sb);
        describePlayers(sb);
        sb.append("You can access from here:\n");
        describeTravelOptions(sb);

        return sb.toString();
    }

    private void describeArtefacts(StringBuilder sb) {
        for (String key: localArtefacts.keySet()) {
            sb.append(localArtefacts.get(key).getDescription() + "\n");
        }
    }

    private void describeFurniture(StringBuilder sb) {
        for (String key : localFurniture.keySet()) {
            sb.append(localFurniture.get(key).getDescription() + "\n");
        }
    }

    private void describeCharacters(StringBuilder sb) {
        for (String key : localCharacters.keySet()) {
            sb.append(localCharacters.get(key).getDescription() + "\n");
        }
    }

    private void describeTravelOptions(StringBuilder sb) {
        for (String s : permittedPaths) {
            sb.append(s + "\n");
        }
    }

    private void describePlayers(StringBuilder sb) {
        for (String s : localPlayers.keySet()) {
            sb.append(s + "\n");
        }
    }
}
