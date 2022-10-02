package StagEngine;

import java.util.ArrayList;

public class StagAction {
    private String trigger;
    private ArrayList<String> subjects;
    private ArrayList<String> consumed;
    private ArrayList<String> produced;
    private String narration;

    public StagAction(String trigger) {
        this.trigger = trigger;
        subjects = new ArrayList<>();
        consumed = new ArrayList<>();
        produced = new ArrayList<>();
        subjects = new ArrayList<>();
    }

    public String getTrigger() {
        return trigger;
    }

    public void addSubject(String subject) {
        subjects.add(subject);
    }

    public void addConsumed(String consumedArtefact) {
        consumed.add(consumedArtefact);
    }

    public void addProduced(String producedArtefact) {
        produced.add(producedArtefact);
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getNarration() {
        return narration;
    }

    public boolean containsSubject(String subjectName) {
        for (String s : subjects) {
            if (s.equalsIgnoreCase(subjectName)){
                return true;
            }
        }
        return false;
    }

    public ArrayList<String> getSubjects() {
        return subjects;
    }

    public ArrayList<String> getConsumed() {
        return consumed;
    }

    public ArrayList<String> getProduced() {
        return produced;
    }

}
