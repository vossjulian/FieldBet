package de.hsos.swa.project.fieldbet.betmanagement.entity;

import java.time.LocalDateTime;

import javax.enterprise.inject.Vetoed;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import de.hsos.swa.project.fieldbet.matchmanagement.entity.Match;
import de.hsos.swa.project.fieldbet.usermanagement.entity.Profile;

/**
 * Bet
 * 
 * @author Patrick Felschen
 */
@Vetoed
@Entity
@Cacheable
@NamedQueries({
        @NamedQuery(name = "Bet.findAll", query = "SELECT b FROM Bet b ORDER BY b.match.startDateTime"),
        @NamedQuery(name = "Bet.findNotFinishedByProfileId", query = "SELECT b FROM Bet b WHERE b.match.isFinished = false AND b.profile.id = ?1 ORDER BY b.match.startDateTime"),
        @NamedQuery(name = "Bet.findByMatchId", query = "SELECT b FROM Bet b WHERE b.match.id = ?1"),
        @NamedQuery(name = "Bet.findByProfileId", query = "SELECT b FROM Bet b WHERE b.profile.id = ?1"),
})
@Table(name = "Bet", uniqueConstraints = {
        @UniqueConstraint(name = "UniqueMatchAndProfile", columnNames = { "match_id", "profile_id" })
})
public class Bet {
    @Id
    @SequenceGenerator(name = "betsSequence", sequenceName = "bets_id_seq", allocationSize = 1, initialValue = 3100)
    @GeneratedValue(generator = "betsSequence")
    private Long id;

    @Version
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @Column(nullable = false)
    @Min(value = 0)
    @Max(value = 99)
    private Integer goalsTeam1;

    @Column(nullable = false)
    @Min(value = 0)
    @Max(value = 99)
    private Integer goalsTeam2;

    @Column(nullable = false)
    private LocalDateTime creationDateTime;

    @Column(nullable = false)
    private LocalDateTime modificationDateTime;

    public Bet() {
    }

    public Bet(
            Match match,
            Profile profile,
            @Min(0) @Max(99) Integer goalsTeam1,
            @Min(0) @Max(99) Integer goalsTeam2,
            LocalDateTime creationDateTime,
            LocalDateTime modificationDateTime) {
        this.match = match;
        this.profile = profile;
        this.goalsTeam1 = goalsTeam1;
        this.goalsTeam2 = goalsTeam2;
        this.creationDateTime = creationDateTime;
        this.modificationDateTime = modificationDateTime;
    }

    @AssertTrue(message = "match start must be after bet creation")
    private boolean isMatchStartAfterCreation() {
        return match.getStartDateTime().isAfter(creationDateTime);
    }

    @AssertTrue(message = "match start must be after bet modification")
    private boolean isMatchStartAfterModification() {
        return match.getStartDateTime().isAfter(modificationDateTime);
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

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
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

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(LocalDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public LocalDateTime getModificationDateTime() {
        return modificationDateTime;
    }

    public void setModificationDateTime(LocalDateTime modificationDateTime) {
        this.modificationDateTime = modificationDateTime;
    }

    @Override
    public String toString() {
        return "Bet [id=" + id + ", version=" + version + ", goalsTeam1=" + goalsTeam1 + ", goalsTeam2=" + goalsTeam2
                + ", creationDateTime=" + creationDateTime + ", modificationDateTime=" + modificationDateTime + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
        result = prime * result + ((goalsTeam1 == null) ? 0 : goalsTeam1.hashCode());
        result = prime * result + ((goalsTeam2 == null) ? 0 : goalsTeam2.hashCode());
        result = prime * result + ((creationDateTime == null) ? 0 : creationDateTime.hashCode());
        result = prime * result + ((modificationDateTime == null) ? 0 : modificationDateTime.hashCode());
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
        Bet other = (Bet) obj;
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
        if (creationDateTime == null) {
            if (other.creationDateTime != null)
                return false;
        } else if (!creationDateTime.equals(other.creationDateTime))
            return false;
        if (modificationDateTime == null) {
            if (other.modificationDateTime != null)
                return false;
        } else if (!modificationDateTime.equals(other.modificationDateTime))
            return false;
        return true;
    }

}