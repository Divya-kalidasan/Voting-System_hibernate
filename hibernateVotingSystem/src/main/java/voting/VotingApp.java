package voting;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import voting.entity.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class VotingApp {

    public static void main(String[] args) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== ONLINE VOTING SYSTEM =====");
            System.out.println("1. Register voter");
            System.out.println("2. Add candidate");
            System.out.println("3. Create election");
            System.out.println("4. List voters");
            System.out.println("5. List candidates");
            System.out.println("6. List elections");
            System.out.println("7. Cast vote");
            System.out.println("8. Show results");
            System.out.println("9. Exit");
            System.out.print("Choose: ");
            int choice = safeInt(sc);

            switch (choice) {
                case 1 -> registerVoter(factory, sc);
                case 2 -> addCandidate(factory, sc);
                case 3 -> createElection(factory, sc);
                case 4 -> listAll(factory, Voter.class);
                case 5 -> listAll(factory, Candidate.class);
                case 6 -> listAll(factory, Election.class);
                case 7 -> castVote(factory, sc);
                case 8 -> showResults(factory, sc);
                case 9 -> { System.out.println("Bye!"); return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static int safeInt(Scanner sc){
        while(!sc.hasNextInt()){ sc.next(); System.out.print("Enter a number: "); }
        return sc.nextInt();
    }

    private static void registerVoter(SessionFactory factory, Scanner sc){
        sc.nextLine();
        System.out.print("Name: "); String name = sc.nextLine();
        System.out.print("Email: "); String email = sc.nextLine();
        try(Session session = factory.openSession()){
            Transaction tx = session.beginTransaction();
            session.persist(new Voter(name, email));
            tx.commit();
            System.out.println("✅ Voter registered.");
        }
    }

    private static void addCandidate(SessionFactory factory, Scanner sc){
        sc.nextLine();
        System.out.print("Name: "); String name = sc.nextLine();
        System.out.print("Party (optional): "); String party = sc.nextLine();
        try(Session session = factory.openSession()){
            Transaction tx = session.beginTransaction();
            session.persist(new Candidate(name, party));
            tx.commit();
            System.out.println("✅ Candidate added.");
        }
    }

    private static void createElection(SessionFactory factory, Scanner sc){
        sc.nextLine();
        System.out.print("Title: "); String title = sc.nextLine();
        System.out.print("Date (YYYY-MM-DD): "); String d = sc.nextLine();
        LocalDate date = d.isBlank() ? LocalDate.now() : LocalDate.parse(d);
        try(Session session = factory.openSession()){
            Transaction tx = session.beginTransaction();
            session.persist(new Election(title, date));
            tx.commit();
            System.out.println("✅ Election created.");
        }
    }

    private static <T> void listAll(SessionFactory factory, Class<T> cls){
        try(Session session = factory.openSession()){
            List<T> list = session.createQuery("from " + cls.getSimpleName(), cls).list();
            list.forEach(System.out::println);
            if(list.isEmpty()) System.out.println("(none)");
        }
    }

    private static void castVote(SessionFactory factory, Scanner sc){
        System.out.print("Election ID: "); long eid = safeInt(sc);
        System.out.print("Voter ID: "); long vid = safeInt(sc);
        System.out.print("Candidate ID: "); long cid = safeInt(sc);

        try(Session session = factory.openSession()){
            Transaction tx = session.beginTransaction();
            Election e = session.get(Election.class, eid);
            Voter v = session.get(Voter.class, vid);
            Candidate c = session.get(Candidate.class, cid);
            if(e==null||v==null||c==null){ System.out.println("❌ Invalid IDs."); tx.rollback(); return; }

            // Enforce 1 vote per voter per election
            Long count = session.createQuery(
                "select count(vt) from Vote vt where vt.election=:e and vt.voter=:v", Long.class)
                .setParameter("e", e).setParameter("v", v).uniqueResult();
            if(count!=null && count>0){ System.out.println("⚠️ Voter already voted in this election."); tx.rollback(); return; }

            session.persist(new Vote(e, v, c));
            tx.commit();
            System.out.println("🗳️ Vote recorded.");
        }
    }

    private static void showResults(SessionFactory factory, Scanner sc){
        System.out.print("Election ID: "); long eid = safeInt(sc);
        try(Session session = factory.openSession()){
            Election e = session.get(Election.class, eid);
            if(e==null){ System.out.println("❌ Invalid election."); return; }
            List<Object[]> rows = session.createQuery(
                "select vt.candidate.id, vt.candidate.name, count(vt.id) " +
                "from Vote vt where vt.election=:e group by vt.candidate.id, vt.candidate.name " +
                "order by count(vt.id) desc", Object[].class)
                .setParameter("e", e).list();

            System.out.println("\nResults for: " + e.getTitle() + " (" + e.getDate() + ")");
            if(rows.isEmpty()){ System.out.println("No votes yet."); return; }
            for(Object[] r : rows){
                System.out.printf("Candidate #%s %-20s -> %d votes\n", r[0], r[1], ((Long)r[2]));
            }
        }
    }
}
