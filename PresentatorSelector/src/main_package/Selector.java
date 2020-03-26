package main_package;

import java.util.List;

public class Selector{
    private final Candidates _candidates;        
    private final Log _log;
    
    public Selector(String candidatesPath, String logPath) {
        _candidates = new Candidates(candidatesPath);
        _log = new Log(logPath);
    }
    
    public String select() {
        Candidate speaker = _candidates.getRandomSpeaker();
        if (speaker != null) {
            return speaker.printCandidate();
        } else {
            return "No speaker avaiable!";
        }
    }
    
    public void remove(String speaker){
        _candidates.removeSpeakers(speaker);  
    }
    
    public void add(String fname, String surname){
        _candidates.addSpeaker(fname, surname);
    }
    
    public List<String> getSpeakers() {
        return _candidates.printCandidates();
    }

    public String printLog() {
        return _log.printLog();
    }
    
    public void setAbsent(String speaker) {
        _candidates.setAbsent(speaker);
        _log.saveEntry(speaker,_candidates.checkAbsent(speaker));
    }

    public boolean checkAbsent(String speaker) {
        return _candidates.checkAbsent(speaker);
    }
    
    public  void clearLog() {
        _log.clearLog();
    }
}