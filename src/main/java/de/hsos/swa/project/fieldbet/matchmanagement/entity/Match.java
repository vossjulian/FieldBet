package de.hsos.swa.project.fieldbet.matchmanagement.entity;

import java.time.LocalDateTime;

import javax.enterprise.inject.Vetoed;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Match
 * 
 * @author Julian Voss, Patrick Felschen
 */
@Vetoed
@Entity
@Cacheable
@NamedQueries({
        @NamedQuery(name = "Match.findAll", query = "SELECT m FROM Match m ORDER BY m.startDateTime"),
        @NamedQuery(name = "Match.findNotFinished", query = "SELECT m FROM Match m WHERE m.isFinished = false ORDER BY m.startDateTime"),
})
public class Match {
    @Id
    @SequenceGenerator(name = "matchSequence", sequenceName = "match_id_seq", allocationSize = 1, initialValue = 2100)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "matchSequence")
    private Long id;

    @Version
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team1_id")
    private Team team1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team2_id")
    private Team team2;

    @Column(nullable = false)
    @Min(value = 0)
    @Max(value = 99)
    private Integer goalsTeam1;

    @Column(nullable = false)
    @Min(value = 0)
    @Max(value = 99)
    private Integer goalsTeam2;

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @Column(nullable = false)
    private boolean isFinished;

    public Match() {
    }

    public Match(
            Team team1,
            Team team2,
            @Min(0) @Max(99) Integer goalsTeam1,
            @Min(0) @Max(99) Integer goalsTeam2,
            LocalDateTime startDateTime,
            boolean isFinished) {
        this.team1 = team1;
        this.team2 = team2;
        this.goalsTeam1 = goalsTeam1;
        this.goalsTeam2 = goalsTeam2;
        this.startDateTime = startDateTime;
        this.isFinished = isFinished;
    }

    @AssertTrue(message = "match teams must be different")
    private boolean isDifferentTeams() {
        return !team1.equals(team2);
    }

    public Long getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Team getTeam1() {
        return team1;
    }

    public void setTeam1(Team team1) {
        this.team1 = team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public void setTeam2(Team team2) {
        this.team2 = team2;
    }

    public Integer getGoalsTeam1() {
        return goalsTeam1;
    }

    public void setGoalsTeam1(Integer goalsTeam1) {
        this.goalsTeam1 = goalsTeam1;
    }

    public Integer getGoalsTeam2() {
        return goalsTeam2;
    }

    public void setGoalsTeam2(Integer goalsTeam2) {
        this.goalsTeam2 = goalsTeam2;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    @Override
    public String toString() {
        return "Match [id=" + id + ", version=" + version + ", goalsTeam1=" + goalsTeam1 + ", goalsTeam2=" + goalsTeam2
                + ", startDateTime=" + startDateTime + ", isFinished=" + isFinished + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
        result = prime * result + ((goalsTeam1 == null) ? 0 : goalsTeam1.hashCode());
        result = prime * result + ((goalsTeam2 == null) ? 0 : goalsTeam2.hashCode());
        result = prime * result + ((startDateTime == null) ? 0 : startDateTime.hashCode());
        result = prime * result + (isFinished ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Match other = (Match) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (version == null) {
            if (other.version != null)
                return false;
        } else if (!version.equals(other.version))
            return false;
        if (goalsTeam1 == null) {
            if (other.goalsTeam1 != null)
                return false;
        } else if (!goalsTeam1.equals(other.goalsTeam1))
            return false;
        if (goalsTeam2 == null) {
            if (other.goalsTeam2 != null)
                return false;
        } else if (!goalsTeam2.equals(other.goalsTeam2))
            return false;
        if (startDateTime == null) {
            if (other.startDateTime != null)
                return false;
        } else if (!startDateTime.equals(other.startDateTime))
            return false;
        if (isFinished != other.isFinished)
            return false;
        return true;
    }

}