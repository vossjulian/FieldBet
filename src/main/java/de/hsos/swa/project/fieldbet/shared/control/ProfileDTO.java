package de.hsos.swa.project.fieldbet.shared.control;

import java.util.Collection;
import java.util.stream.Collectors;

import de.hsos.swa.project.fieldbet.usermanagement.entity.Profile;

/**
 * ProfileDTO
 * 
 * @author Patrick Felschen
 */
public class ProfileDTO {
    public String id;
    public String firstname;
    public String lastname;
    public Long points;

    public ProfileDTO() {
    }

    public ProfileDTO(String id, String firstname, String lastname, Long points) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.points = points;
    }

    public static class Converter {
        public static ProfileDTO toDTO(Profile profile) {
            if (profile == null)
                return null;
            return new ProfileDTO(profile.getId(), profile.getFirstname(), profile.getLastname(), profile.getPoints());
        }

        public static Collection<ProfileDTO> toDTOList(Collection<Profile> bets) {
            return bets.stream().map(ProfileDTO.Converter::toDTO).collect(Collectors.toList());
        }
    }
}
