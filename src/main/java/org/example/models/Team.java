package org.example.models;

import org.example.annotations.XmlField;

public class Team {
    @XmlField(name = "teamName")
    private String teamName;

    @XmlField(name = "members")
    private Person[] members;

    public Team() {}

    public Team(String teamName, Person[] members) {
        this.teamName = teamName;
        this.members = members;
    }

    public String getTeamName() { return teamName; }
    public Person[] getMembers() { return members; }
}
