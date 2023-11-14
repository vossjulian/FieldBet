package de.hsos.swa.project.fieldbet.usermanagement.control;

/**
 * UserCreationDTO
 * 
 * @author Julian Voss
 */
public class UserCreationDTO {
    public String username;
    public String password;
    public String firstname;
    public String lastname;

    public UserCreationDTO() {
    }

    public UserCreationDTO(String username, String password, String firstname, String lastname) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
    }

}
