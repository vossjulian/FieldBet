package de.hsos.swa.project.fieldbet.matchmanagement.control;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * MatchUpdateDTO
 * 
 * @author Julian Voss, Patrick Felschen
 */
public class MatchUpdateDTO {
    public Integer goalsTeam1;
    public Integer goalsTeam2;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SS", shape = JsonFormat.Shape.STRING)
    public LocalDateTime startDateTime;
    public Boolean isFinished;

    public MatchUpdateDTO() {
    }

    public MatchUpdateDTO(Integer goalsTeam1, Integer goalsTeam2, LocalDateTime startDateTime,
            boolean isFinished) {
        this.goalsTeam1 = goalsTeam1;
        this.goalsTeam2 = goalsTeam2;
        this.startDateTime = startDateTime;
        this.isFinished = isFinished;
    }

    @Override
    public String toString() {
        return "MatchUpdateDTO [goalsTeam1=" + goalsTeam1 + ", goalsTeam2=" + goalsTeam2 + ", startDateTime="
                + startDateTime + ", isFinished=" + isFinished + "]";
    }

}
