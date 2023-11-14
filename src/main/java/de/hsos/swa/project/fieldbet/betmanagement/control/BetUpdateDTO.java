package de.hsos.swa.project.fieldbet.betmanagement.control;

/**
 * BetUpdateDTO
 * 
 * @author Patrick Felschen
 */
public class BetUpdateDTO {
    public Integer goalsTeam1;
    public Integer goalsTeam2;

    public BetUpdateDTO() {
    }

    public BetUpdateDTO(Integer goalsTeam1, Integer goalsTeam2) {
        this.goalsTeam1 = goalsTeam1;
        this.goalsTeam2 = goalsTeam2;
    }
}
