package de.hsos.swa.project.fieldbet.shared.control;

/**
 * ProfilePointsDTO
 * 
 * @author Patrick Felschen
 */
public class ProfilePointsDTO {
    public String profileId;
    public Long points;

    public ProfilePointsDTO(String profileId, Long points) {
        this.profileId = profileId;
        this.points = points;
    }

}
