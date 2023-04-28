package com.example.btquatrinh_04.model;

public class representatives {
    private String name;
    private String office;
    private int officials;

    public representatives(String name, String office) {
        this.name = name;
        this.office = office;
    }

    public int getOfficials() {
        return officials;
    }

    public void setOfficials(int officials) {
        this.officials = officials;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }
}
