package de.hsos.swa.project.fieldbet.matchmanagement.entity;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.inject.Vetoed;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;

/**
 * Team
 * 
 * @author Julian Voss, Patrick Felschen
 */
@Vetoed
@Entity
@Cacheable
@NamedQueries({
        @NamedQuery(name = "Team.findAll", query = "SELECT t FROM Team t ORDER BY t.name"),
        @NamedQuery(name = "Team.findById", query = "SELECT t FROM Team t WHERE t.id = ?1"),
})
@Table(name = "team")
public class Team {
    @Id
    @SequenceGenerator(name = "teamSequence", sequenceName = "team_id_seq", allocationSize = 1, initialValue = 1100)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "teamSequence")
    private Long id;

    @Version
    private Long version;

    @Column(nullable = false)
    @Size(min = 1, max = 50)
    private String name;

    @Column(nullable = false)
    @Size(min = 3, max = 3)
    private String alias;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "team1")
    private Set<Match> matches1;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "team2")
    private Set<Match> matches2;

    public Team() {
    }

    public Team(@Size(min = 1, max = 50) String name, @Size(min = 3, max = 3) String alias) {
        this.name = name;
        this.alias = alias;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Set<Match> getMatches() {
        Set<Match> matches = new HashSet<>();
        matches.addAll(matches1);
        matches.addAll(matches2);
        return matches;
    }

    public void setMatches1(Set<Match> matches1) {
        this.matches1 = matches1;
    }

    public void setMatches2(Set<Match> matches2) {
        this.matches2 = matches2;
    }

    @Override
    public String toString() {
        return "Team [id=" + id + ", version=" + version + ", name=" + name + ", alias=" + alias + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((alias == null) ? 0 : alias.hashCode());
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
        Team other = (Team) obj;
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
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (alias == null) {
            if (other.alias != null)
                return false;
        } else if (!alias.equals(other.alias))
            return false;
        return true;
    }

}
