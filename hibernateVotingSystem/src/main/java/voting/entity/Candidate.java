package voting.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "candidates")
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=100)
    private String name;

    @Column(length=100)
    private String party;

    public Candidate() {}
    public Candidate(String name, String party) {
        this.name = name; this.party = party;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getParty() { return party; }
    public void setParty(String party) { this.party = party; }

    @Override
    public String toString() {
        return String.format("Candidate{id=%d, name='%s', party='%s'}", id, name, party);
    }
}
