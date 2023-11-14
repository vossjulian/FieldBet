package de.hsos.swa.project.fieldbet.shared.control;

/**
 * MatchFinishedDTO
 * 
 * @author Patrick Felschen
 */
public class MatchFinishedDTO {
    public Long machtId;
    public Integer goalsTeam1;
    public Integer goalsTeam2;

    public MatchFinishedDTO() {
    }

    public MatchFinishedDTO(Long machtId, Integer goalsTeam1, Integer goalsTeam2) {
        this.machtId = machtId;
        this.goalsTeam1 = goalsTeam1;
        this.goalsTeam2 = goalsTeam2;
    }

}
