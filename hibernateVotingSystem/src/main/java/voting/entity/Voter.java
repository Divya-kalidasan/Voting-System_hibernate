package voting.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "voters", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Voter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=100)
    private String name;

    @Column(nullable=false, length=120)
    private String email;

    public Voter() {}
    public Voter(String name, String email) {
        this.name = name; this.email = email;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return String.format("Voter{id=%d, name='%s', email='%s'}", id, name, email);
    }
}
