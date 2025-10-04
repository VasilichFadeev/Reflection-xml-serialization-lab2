package org.example.models;

import org.example.annotations.XmlField;

public class Company {
    @XmlField(name = "companyName")
    private String companyName;

    @XmlField(name = "mainTeam")
    private Team mainTeam;

    public Company() {}

    public Company(String companyName, Team mainTeam) {
        this.companyName = companyName;
        this.mainTeam = mainTeam;
    }

    public String getCompanyName() { return companyName; }
    public Team getMainTeam() { return mainTeam; }
}
