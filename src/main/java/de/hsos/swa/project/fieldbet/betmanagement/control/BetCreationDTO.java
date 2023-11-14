package de.hsos.swa.project.fieldbet.betmanagement.control;

/**
 * BetCreationDTO
 * 
 * @author Patrick Felschen
 */
public class BetCreationDTO {
    public Long matchId;
    public Integer goalsTeam1;
    public Integer goalsTeam2;

    public BetCreationDTO() {
    }

    public BetCreationDTO(Long matchId, Integer goalsTeam1, Integer goalsTeam2) {
        this.matchId = matchId;
        this.goalsTeam1 = goalsTeam1;
        this.goalsTeam2 = goalsTeam2;
    }

}
