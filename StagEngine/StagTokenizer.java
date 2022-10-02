package StagEngine;

import StagExceptions.StagParseException;

import java.util.ArrayList;

/**
 * Class: StagTokenizer
 * Author: kf13056
 * Date: 01.05.2021
 *
 * StagTokenizer takes a command string and splits words and common punctuation into separate tokens.
 * Punctuation comprises of: fullstops, commas, hyphens, exclamation marks, colons, single quotes and double quotes.
 *
 * This assumes that trigger words and entities do not contain these characters.
 */
public class StagTokenizer {
    String command;

    public StagTokenizer(String command) {
        this.command = command;
    }

    /* Returns the player name from command string */
    public String extractPlayerName() throws StagParseException {
        if(!checkForColon(command)) {
            throw new StagParseException("Colon missing in command from player, please check your client.");
        }

        String[] strArray = command.split(":", 2);
        return strArray[0];
    }

    public void splitIntoTokens(ArrayList<String> tokens) throws StagParseException {
        String[] strArray = command.split(":", 2);
        String commandString = strArray[1].trim();

        if (commandString.isEmpty()) {
            throw new StagParseException("The command is empty, please try again.");
        }

        commandString = commandString.replaceAll("([-!.:;,'\"])", " $1 ");
        commandString = commandString.trim().replaceAll("\\s+", " ");


        String[] tokensArray = commandString.split(" ");

        for (String token : tokensArray) {
            tokens.add(token.trim());
        }
    }

    public String getColonDelimitedCommand() throws StagParseException {
        if(!checkForColon(command)) {
            throw new StagParseException("Please ensure there is a colon in the string provided.");
        }

        String[] strArray = command.split(":", 2);
        return strArray[1].trim();
    }

    private boolean checkForColon(String checkThisString) {
        return checkThisString.contains(":");
    }
}
