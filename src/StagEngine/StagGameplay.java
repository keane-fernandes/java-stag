package StagEngine;

import StagEntities.Artefact;
import StagEntities.Location;
import StagEntities.Player;
import StagExceptions.StagPlayException;

import java.util.ArrayList;

/* Performs a STAG's custom 'dynamic' command after StagNLP matches a suitable action */
public class StagGameplay {
    StagAction action;
    StagModel model;
    ArrayList<String> subjects;

    public StagGameplay(StagModel model, StagAction action) {
        this.model = model;
        this.action = action;
        subjects = action.getSubjects();
    }

    public String playAction() throws StagPlayException {
        ArrayList<Boolean> entityChecklist = new ArrayList<>();
        StringBuilder response = new StringBuilder();

        checkEntitiesPresent(entityChecklist);
        verifyChecklist(entityChecklist);
        updateGameState();
        response.append(action.getNarration() + "\n");
        movePlayerIfDead(response);
        return response.toString();
    }

    private void checkEntitiesPresent(ArrayList<Boolean> entityChecklist) {
        for(String subject : subjects) {
            Boolean exists = false;

            if(checkInInventory(subject) || checkInLocation(subject)) {
                exists = true;
            }
            entityChecklist.add(exists);
        }
    }

    private boolean checkInInventory(String subjectName) {
        Player currentPlayer = model.getPlayerByName(model.getCurrentPlayerName());

        if(currentPlayer.artefactInInventory(subjectName)) {
            return true;
        }
        return false;
    }

    private boolean checkInLocation(String subjectName) {
        Player currentPlayer = model.getPlayerByName(model.getCurrentPlayerName());
        Location currentLocation = model.getLocation(currentPlayer.getCurrentLocation());

        if(currentLocation.hasSubject(subjectName)) {
            return true;
        }
        return false;
    }

    private void verifyChecklist(ArrayList<Boolean> entityChecklist) throws StagPlayException {

        for(Boolean exists : entityChecklist) {
            if (exists == false) {
                throw new StagPlayException("An entity is missing for the action, please try again.");
            }
        }
    }

    private void updateGameState() {
        for (String consumedEntity : action.getConsumed()) {
            consumeFromGame(consumedEntity);
        }

        for (String producedEntity : action.getProduced()) {
            produceToGame(producedEntity);
        }
    }

    private void consumeFromGame(String consumedEntity) {
        Player currentPlayer = model.getPlayerByName(model.getCurrentPlayerName());
        Location currentLocation = model.getLocation(currentPlayer.getCurrentLocation());

        consumeHealth(currentPlayer, consumedEntity);
        consumePath(currentLocation, consumedEntity);
        consumeFromPlayer(currentPlayer, consumedEntity);
        consumeFromLocation(currentLocation, consumedEntity);
    }

    private void consumeHealth(Player currentPlayer, String consumedEntity) {
        if(consumedEntity.equalsIgnoreCase("health")) {
            currentPlayer.decreasePlayerHealth();
        }
    }

    private void consumeFromPlayer(Player currentPlayer, String consumedEntity) {
        if(currentPlayer.artefactInInventory(consumedEntity)) {
            currentPlayer.dropArtefact(consumedEntity);
        }
    }

    private void consumeFromLocation(Location currentLocation, String consumedEntity) {
        String entityType = currentLocation.getEntityType(consumedEntity);

        switch (entityType) {
            case "artefact":
                currentLocation.removeArtefact(consumedEntity);
                break;
            case "furniture":
                currentLocation.removeFurniture(consumedEntity);
                break;
            case "character":
                currentLocation.removeCharacter(consumedEntity);
                break;
        }
    }

    private void consumePath(Location currentLocation, String consumedEntity) {
        if(currentLocation.isTravelPossibleTo(consumedEntity)) {
            currentLocation.removePath(consumedEntity);
        }
    }

    private void produceToGame(String producedEntity) {
        Player currentPlayer = model.getPlayerByName(model.getCurrentPlayerName());
        Location currentLocation = model.getLocation(currentPlayer.getCurrentLocation());

        produceHealth(currentPlayer, producedEntity);
        producePath(currentLocation, producedEntity);
        produceInLocation(currentLocation, producedEntity);
    }

    private void produceHealth(Player currentPlayer, String producedEntity) {
        if(producedEntity.equalsIgnoreCase("health")) {
            currentPlayer.increasePlayerHealth();
        }
    }

    private void producePath(Location currentLocation, String producedEntity) {
        if(model.getLocationHashMap().containsKey(producedEntity)) {
            currentLocation.addPath(producedEntity);
        }
    }

    private void produceInLocation(Location currentLocation, String producedEntity) {
        for (String source : model.getLocationHashMap().keySet()) {
            if(model.getLocation(source).hasSubject(producedEntity)) {
                transferEntity(currentLocation, model.getLocation(source), producedEntity);
            }
        }
    }

    private void transferEntity(Location destination, Location source, String entityName) {
        String entityType = source.getEntityType(entityName);

        switch (entityType) {
            case "artefact":
                Artefact a = source.removeArtefact(entityName);
                destination.addArtefact(a);
                break;
            case "furniture":
                destination.addFurniture(source.removeFurniture(entityName));
                break;
            case "character":
                destination.addCharacter(source.removeCharacter(entityName));
                break;
            case "":
        }
    }

    private void movePlayerIfDead(StringBuilder response) {
        Player currentPlayer = model.getPlayerByName(model.getCurrentPlayerName());
        Location currentLocation = model.getLocation(currentPlayer.getCurrentLocation());
        String placeOfRespawn = model.getStartingLocation();
        String placeOfDeath = currentPlayer.getCurrentLocation();

        int health = currentPlayer.getPlayerHealth();

        if(health == 0) {
            currentPlayer.resetPlayerHealth();
            for(String artefactName : currentPlayer.getInventory().keySet()) {
                Artefact droppedArtefact = currentPlayer.getArtefact(artefactName);
                currentLocation.addArtefact(droppedArtefact);
            }

            currentPlayer.emptyInventory();
            currentPlayer.setCurrentLocation(placeOfRespawn);

            response.append("Your player has been killed. Items in your inventory have been dropped in "
                    + placeOfDeath
                    + ". Your health has been fully restored and you have been moved to "
                    + placeOfRespawn + ".");
        }
    }

}
