package StagEngine;

import StagEntities.Location;
import StagEntities.Player;
import StagExceptions.StagException;
import StagExceptions.StagParseException;
import StagExceptions.StagPlayException;

import java.util.ArrayList;

/**
 * Class: StagController
 * Author: kf13056
 * Date: 01.05.2021
 *
 * Performs STAG's built-in commands and orchestrates the implementation of the custom 'dynamic' commands
 * using the StagNLP, StagTokenizer and StagGameplay classes.
 */

public class StagController {
    private StagModel model;
    private String commandString;
    private StagNLP nlp;

    public StagController(StagModel model) {
        this.model = model;
    }

    public String handleIncomingCommand(String command) throws StagException {
        ArrayList<String> tokens = new ArrayList<>();
        StagTokenizer tokenizer = new StagTokenizer(command);

        String playerName = tokenizer.extractPlayerName();
        Player player = model.getPlayerByName(playerName);
        model.setCurrentPlayerName(playerName);

        tokenizer.splitIntoTokens(tokens);
        commandString = tokenizer.getColonDelimitedCommand();

        nlp = new StagNLP(model, commandString, tokens);

        return interpretCommand(player, tokens);
    }

    /* Interprets the user command using built-in or dynamic triggers */
    private String interpretCommand(Player player, ArrayList<String> tokens) throws StagException {
        String builtInTrigger = nlp.lookForBuiltIns();

        switch (builtInTrigger.toLowerCase()) {
            case "inv":
            case "inventory":
                return performInventoryAction(player);
            case "look":
                return performLookAction(player);
            case "goto":
                return performGotoAction(player, tokens);
            case "get":
                return performGetAction(player, tokens);
            case "drop":
                return performDropAction(player, tokens);
            case "health":
                return performHealthAction(player);
            default:
                return performDynamicAction();
        }
    }

    private String performInventoryAction(Player player) {
        return player.describeInventory();
    }

    private String performGotoAction(Player player, ArrayList<String> tokens) throws StagException {
        isCommandIncomplete(tokens);
        StringBuilder response = new StringBuilder();
        Location location = model.getLocation(player.getCurrentLocation());

        for (String potentialDestination : tokens) {
            if (location.isTravelPossibleTo(potentialDestination)){
                player.setCurrentLocation(potentialDestination);
                response.append(model.getLocation(player.getCurrentLocation()).describeLocation());
                response.append(describePlayersInLocation(potentialDestination));
                return response.toString();
            }
        }

        throw new StagPlayException("Travel to this location is not possible.");
    }

    private String performLookAction(Player player) {
        StringBuilder response = new StringBuilder();
        response.append(model.getLocation(player.getCurrentLocation()).describeLocation());
        response.append(describePlayersInLocation(player.getCurrentLocation()));
        return response.toString();
    }

    private String performGetAction(Player player, ArrayList<String> tokens) throws StagException {
        isCommandIncomplete(tokens);
        StringBuilder response = new StringBuilder();
        Location location = model.getLocation(player.getCurrentLocation());

        for (String artefact : tokens) {
            if (location.hasArtefact(artefact)) {
                player.addArtefactToInventory(location.removeArtefact(artefact));
                response.append("You picked up a: ");
                response.append(artefact);
                return response.toString();
            }
        }

        throw new StagPlayException("This entity cannot be picked up.");
    }

    private String performDropAction(Player player, ArrayList<String> tokens) throws StagException {
        isCommandIncomplete(tokens);
        Location location = model.getLocation(player.getCurrentLocation());
        StringBuilder response = new StringBuilder();

        for (String artefact : tokens) {
            if(player.artefactInInventory(artefact)) {
                location.addArtefact(player.dropArtefact(artefact));
                response.append("You dropped a: ");
                response.append(artefact);
                return response.toString();
            }
        }
        throw new StagPlayException("You do not have this artefact, hence it cannot be dropped.");
    }

    private String performHealthAction(Player player) {
        return String.valueOf(player.getPlayerHealth());
    }

    /* Performs an action based on the actions defined in the JSON file */
    private String performDynamicAction() {
        StringBuilder response = new StringBuilder();

        try {
            StagAction mostSuitableAction = nlp.lookForAction();
            StagGameplay gameplay = new StagGameplay(model, mostSuitableAction);
            response.append(gameplay.playAction());
        }
        catch (StagException se) {
            return se.toString();
        }

        return response.toString();
    }

    /* Checks if a command is one token or less */
    private void isCommandIncomplete(ArrayList<String> tokens) throws StagException {
        if(tokens.size() <= 1) {
            throw new StagException("Your command is incomplete, please try again.");
        }
    }

    /* Provides a summary of the players in a specified location */
    private String describePlayersInLocation(String locationName) {
        StringBuilder playerSummary = new StringBuilder();
        playerSummary.append("Other players in the game:\n");

        for (String playerName : model.getPlayerMap().keySet()) {
            if(!playerName.equals(model.getCurrentPlayerName()) &&
                    model.getPlayerMap().get(playerName).getCurrentLocation().equals(locationName)){
                playerSummary.append(playerName);
                playerSummary.append("\n");
            }
        }
        return playerSummary.toString();
    }
}
