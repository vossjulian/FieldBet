package de.hsos.swa.project.fieldbet.usermanagement.entity;

import java.util.Set;

import javax.enterprise.inject.Vetoed;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import de.hsos.swa.project.fieldbet.betmanagement.entity.Bet;

/**
 * Profile
 * 
 * @author Patrick Felschen
 */
@Vetoed
@Entity
@Cacheable
@NamedQueries({
        @NamedQuery(name = "Profile.findAll", query = "SELECT p FROM Profile p ORDER BY p.points DESC"),
})
public class Profile {
    @Id
    @Column(nullable = false, unique = true)
    private String id;

    @Version
    private Long version;

    @Column(nullable = false)
    @Size(min = 1, max = 50)
    private String firstname;

    @Column(nullable = false)
    @Size(min = 1, max = 50)
    private String lastname;

    @Column(nullable = false)
    @Min(value = 0)
    private Long points;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profile", orphanRemoval = true)
    private Set<Bet> bets;

    public Profile() {
    }

    public Profile(
            String id,
            @Size(min = 1, max = 50) String firstname,
            @Size(min = 1, max = 50) String lastname,
            @Min(0) Long points) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.points = points;
    }

    public String getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Set<Bet> getBets() {
        return bets;
    }

    public void setBets(Set<Bet> bets) {
        this.bets = bets;
    }

    @Override
    public String toString() {
        return "Profile [id=" + id + ", version=" + version + ", firstname=" + firstname + ", lastname=" + lastname
                + ", points=" + points + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
        result = prime * result + ((firstname == null) ? 0 : firstname.hashCode());
        result = prime * result + ((lastname == null) ? 0 : lastname.hashCode());
        result = prime * result + ((points == null) ? 0 : points.hashCode());
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
        Profile other = (Profile) obj;
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
        if (firstname == null) {
            if (other.firstname != null)
                return false;
        } else if (!firstname.equals(other.firstname))
            return false;
        if (lastname == null) {
            if (other.lastname != null)
                return false;
        } else if (!lastname.equals(other.lastname))
            return false;
        if (points == null) {
            if (other.points != null)
                return false;
        } else if (!points.equals(other.points))
            return false;
        return true;
    }

}