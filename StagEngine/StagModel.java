package StagEngine;

import StagEntities.Location;
import StagEntities.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class StagModel {
    private HashMap<String, ArrayList<StagAction>> actionsMap;
    private HashMap<String, Location> locationsMap;
    private HashMap<String, Player> playerMap;
    private ArrayList<String> triggersList;
    private ArrayList<String> entitiesList;
    private String startingLocation;
    private String currentPlayer;

    public StagModel() {
        locationsMap = new HashMap<>();
        actionsMap = new HashMap<>();
        playerMap = new HashMap<>();
        triggersList = new ArrayList<>();
        entitiesList = new ArrayList<>();
    }

    public void addLocation(String locationName, Location location) {
        locationsMap.put(locationName, location);
    }

    public Location getLocation(String locationName) {
        return locationsMap.get(locationName);
    }

    public void addAction(StagAction action) {
        if (actionsMap.containsKey(action.getTrigger())) {
            actionsMap.get(action.getTrigger()).add(action);
        }
        else {
            ArrayList<StagAction> actionsList = new ArrayList<>();
            actionsList.add(action);
            actionsMap.put(action.getTrigger(), actionsList);
        }
    }

    public void setStartingLocation(String locationName) {
        startingLocation = locationName;
    }

    public String getStartingLocation() {
        return startingLocation;
    }

    public void addTrigger(String trigger) {
        if(!triggersList.contains(trigger)) {
            triggersList.add(trigger);
        }
    }

    public void addSubject(String subject) {
        entitiesList.add(subject);
    }

    public Player getPlayerByName(String playerName) {

        if(!playerMap.containsKey(playerName)) {
            Player playerObject = new Player(playerName, startingLocation);
            playerMap.put(playerName, playerObject);
            return playerMap.get(playerName);
        }
        return playerMap.get(playerName);
    }

    public void setCurrentPlayerName(String playerName) {
        currentPlayer = playerName;
    }

    public String getCurrentPlayerName() {
        return currentPlayer;
    }

    public ArrayList<String> getTriggersList() {
        return triggersList;
    }

    public HashMap<String, Location> getLocationHashMap() {
        return locationsMap;
    }

    public HashMap<String, ArrayList<StagAction>> getActionsHashMap() {
        return actionsMap;
    }

    public HashMap<String, Player> getPlayerMap() {
        return playerMap;
    }
}
