package StagEngine;

import StagExceptions.StagTriggerException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.StringTokenizer;


/* Returns the most appropriate action object depending on the command from the user */
public class StagNLP {
    ArrayList<String> tokens;
    StagModel model;
    String commandString;
    private final String[] builtInCommands = {"inv", "inventory", "look", "goto", "get", "drop", "health"};

    public StagNLP(StagModel model, String commandString, ArrayList<String> tokens) {
        this.model = model;
        this.commandString = commandString;
        this.tokens = tokens;
    }

    /* Checks if the user want to execute a built-in game command */
    public String lookForBuiltIns() {
        for (String s : tokens) {
            if(Arrays.asList(builtInCommands).contains(s.toLowerCase())) {
                return s;
            }
        }
        return "";
    }

    /* Returns the closest match of action that can be inferred from the user command */
    public StagAction lookForAction() throws StagTriggerException {
        ArrayList<ArrayList<StagAction>> potentialActions = new ArrayList<>();
        StagAction theAction;

        matchTriggerToAction(potentialActions);

        checkForNoMatches(potentialActions);
        checkForAmbiguity(potentialActions);

        theAction = matchSubjectToAction(potentialActions);

        return theAction;
    }

    private void matchTriggerToAction(ArrayList<ArrayList<StagAction>> potentialActions) {
        ArrayList<String> triggersList = model.getTriggersList();
        HashMap<String, ArrayList<StagAction>> actionsHashMap =  model.getActionsHashMap();

        for (String s : tokens) {
            for (String trigger : triggersList) {
                if (trigger.equalsIgnoreCase(s)) {
                    potentialActions.add(actionsHashMap.get(trigger));
                }
            }
        }

        /* If there are no single word trigger matches, look for two word triggers */
        if (potentialActions.size() == 0) {
            matchMultiWordTriggers(potentialActions);
        }
    }

    private void checkForNoMatches(ArrayList<ArrayList<StagAction>> potentialActions) throws StagTriggerException {
        if(potentialActions.size() == 0) {
            throw new StagTriggerException("Could not find a trigger. " +
                    "Hint: Have a look at your inventory, and what is around you, and please try again.");
        }
    }

    private void checkForAmbiguity(ArrayList<ArrayList<StagAction>> potentialActions) throws StagTriggerException {
        if(potentialActions.size() > 1) {
            throw new StagTriggerException(potentialActions.size() + "The command is ambiguous, please try again.");
        }
    }

    private StagAction matchSubjectToAction(ArrayList<ArrayList<StagAction>> potentialActions) throws StagTriggerException {
        /*
            It was mentioned that every action will be tested with at least one subject, if not it makes sense
            to make this a built in command of the game
         */
        for (String potentialSubject : tokens) {
            for (StagAction action : potentialActions.get(0)) {
                if(action.containsSubject(potentialSubject)) {
                    return action;
                }
            }
        }

        throw new StagTriggerException("Not able to match a subject to the valid trigger, please try again.");
    }

    /* If trigger consist of multiple words, this is checked for here */
    private void matchMultiWordTriggers(ArrayList<ArrayList<StagAction>> potentialActions) {
        for (String trigger : model.getTriggersList()) {
            if(trigger.contains(" ")) {
                if(commandString.contains(trigger)) {
                    potentialActions.add(model.getActionsHashMap().get(trigger));
                }
            }
        }
    }

}
