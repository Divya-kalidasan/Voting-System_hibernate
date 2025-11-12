package voting.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "elections")
public class Election {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=120)
    private String title;

    private LocalDate date;

    public Election(){}
    public Election(String title, LocalDate date){
        this.title=title; this.date=date;
    }

    public Long getId(){ return id; }
    public String getTitle(){ return title; }
    public void setTitle(String title){ this.title = title; }
    public LocalDate getDate(){ return date; }
    public void setDate(LocalDate date){ this.date = date; }

    @Override
    public String toString(){
        return String.format("Election{id=%d, title='%s', date=%s}", id, title, date);
    }
}
