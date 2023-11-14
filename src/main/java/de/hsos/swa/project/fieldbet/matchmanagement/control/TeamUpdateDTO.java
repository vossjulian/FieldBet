package de.hsos.swa.project.fieldbet.matchmanagement.control;

/**
 * TeamUpdateDTO
 * 
 * @author Julian Voss
 */
public class TeamUpdateDTO {
    public String name;
    public String alias;

    public TeamUpdateDTO() {
    }

    public TeamUpdateDTO(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }

}
