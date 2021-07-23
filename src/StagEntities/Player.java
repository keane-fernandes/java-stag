package StagEntities;

import StagEntities.Artefact;

import java.util.HashMap;

/* A player in STAG */
public class Player extends Entity {
    private HashMap<String, Artefact> inventory;
    private String currentLocation;
    private int health;

    public Player(String name, String startingLocation) {
        super(name, "");
        health = 3;
        inventory = new HashMap<>();
        currentLocation = startingLocation;
    }

    public void setCurrentLocation(String locationName) {
        currentLocation = locationName;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public String describeInventory() {
        StringBuilder response = new StringBuilder();
        response.append("You have the following items:\n");

        for (String key : inventory.keySet()) {
            response.append(inventory.get(key).getName() + " - " + inventory.get(key).getDescription() + "\n");
        }
        return response.toString();
    }

    public int getPlayerHealth() {
        return health;
    }

    public void increasePlayerHealth() {
        health++;
    }

    public void decreasePlayerHealth() {
        health--;
    }

    public void resetPlayerHealth() {
        health = 3;
    }

    public void addArtefactToInventory(Artefact artefact) {
        inventory.put(artefact.getName(), artefact);
    }

    public boolean artefactInInventory(String artefactName) {
        if(inventory.containsKey(artefactName)){
            return true;
        }
        return false;
    }

    public Artefact dropArtefact(String artefactName) {
        return inventory.remove(artefactName);
    }

    public HashMap<String, Artefact> getInventory() {
        return inventory;
    }

    public Artefact getArtefact(String artefactName) {
        return inventory.get(artefactName);
    }

    public void emptyInventory() {
        inventory.clear();
    }
}
