package voting;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.HashMap;
import java.util.Map;

public class HibernateUtil {

    private static SessionFactory factory;

    static {
        try {
            Map<String, String> settings = new HashMap<>();
            settings.put("hibernate.connection.driver_class", "org.h2.Driver");
            settings.put("hibernate.connection.url", "jdbc:h2:./votingdb;MODE=MySQL;DATABASE_TO_UPPER=false");
            settings.put("hibernate.connection.username", "sa");
            settings.put("hibernate.connection.password", "");
            settings.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
            settings.put("hibernate.hbm2ddl.auto", "update");
            settings.put("hibernate.show_sql", "true");
            settings.put("hibernate.format_sql", "true");

            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .applySettings(settings)
                    .build();

            MetadataSources sources = new MetadataSources(registry)
                    .addAnnotatedClass(voting.entity.Voter.class)
                    .addAnnotatedClass(voting.entity.Candidate.class)
                    .addAnnotatedClass(voting.entity.Election.class)
                    .addAnnotatedClass(voting.entity.Vote.class);

            Metadata metadata = sources.getMetadataBuilder().build();
            factory = metadata.getSessionFactoryBuilder().build();

            System.out.println("✅ Hibernate SessionFactory created for Online Voting System");

        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError("❌ Hibernate initialization failed: " + e.getMessage());
        }
    }

    public static SessionFactory getSessionFactory() {
        return factory;
    }
}
