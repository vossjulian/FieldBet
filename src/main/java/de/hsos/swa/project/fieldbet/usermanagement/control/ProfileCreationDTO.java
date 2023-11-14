package de.hsos.swa.project.fieldbet.usermanagement.control;

/**
 * ProfileCreationDTO
 * 
 * @author Patrick Felschen
 */
public class ProfileCreationDTO {
    public String firstname;
    public String lastname;

    public ProfileCreationDTO() {
    }

    public ProfileCreationDTO(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }

}
