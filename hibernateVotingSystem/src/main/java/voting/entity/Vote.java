package voting.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "votes",
    uniqueConstraints = @UniqueConstraint(columnNames = {"election_id", "voter_id"}))
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false)
    @JoinColumn(name="election_id")
    private Election election;

    @ManyToOne(optional=false)
    @JoinColumn(name="voter_id")
    private Voter voter;

    @ManyToOne(optional=false)
    @JoinColumn(name="candidate_id")
    private Candidate candidate;

    public Vote(){}
    public Vote(Election e, Voter v, Candidate c){
        this.election=e; this.voter=v; this.candidate=c;
    }

    public Long getId(){ return id; }
    public Election getElection(){ return election; }
    public void setElection(Election e){ this.election=e; }
    public Voter getVoter(){ return voter; }
    public void setVoter(Voter v){ this.voter=v; }
    public Candidate getCandidate(){ return candidate; }
    public void setCandidate(Candidate c){ this.candidate=c; }
}
