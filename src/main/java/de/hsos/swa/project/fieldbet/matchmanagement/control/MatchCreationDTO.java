package de.hsos.swa.project.fieldbet.matchmanagement.control;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * MatchCreationDTO
 * 
 * @author Julian Voss, Patrick Felschen
 */
public class MatchCreationDTO {
    public Long team1Id;
    public Long team2Id;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SS", shape = JsonFormat.Shape.STRING)
    public LocalDateTime startDateTime;

    public MatchCreationDTO() {
    }

    public MatchCreationDTO(Long team1Id, Long team2Id, LocalDateTime startDateTime) {
        this.team1Id = team1Id;
        this.team2Id = team2Id;
        this.startDateTime = startDateTime;
    }

}
