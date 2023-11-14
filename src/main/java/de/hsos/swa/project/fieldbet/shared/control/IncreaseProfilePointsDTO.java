package de.hsos.swa.project.fieldbet.shared.control;

import java.util.Collection;

/**
 * IncreaseProfilePointsDTO
 * 
 * @author Patrick Felschen
 */
public class IncreaseProfilePointsDTO {
    public Collection<ProfilePointsDTO> profilePoints;

    public IncreaseProfilePointsDTO(Collection<ProfilePointsDTO> profilePoints) {
        this.profilePoints = profilePoints;
    }

}
