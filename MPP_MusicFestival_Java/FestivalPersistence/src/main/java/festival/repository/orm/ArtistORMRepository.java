package festival.repository.orm;

import festival.domain.Artist;
import festival.repository.ArtistRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;


import java.util.ArrayList;
import java.util.List;

public class ArtistORMRepository implements ArtistRepository {

    private static SessionFactory sessionFactory;

    public ArtistORMRepository() {

    }

    static void initialize() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            System.err.println("Exceptie " + e);
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    static void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }


    @Override
    public void save(Artist entity) {
        initialize();
        /*try {
            findOne(entity.getId());
            throw new LoginException("artist already exists");
        } catch (LoginException e) {
            e.printStackTrace();
        }*/
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.save(entity);
                //session.save(new Artist("Anuel","AA",30,"Cuba"));
                transaction.commit();

            } catch (RuntimeException exception) {
                if (transaction != null)
                    transaction.rollback();
            }
        }finally {
            close();
        }

    }

    @Override
    public void delete(Long aLong) {
        initialize();
        Artist artist = findOne(aLong);
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.delete(artist);
                transaction.commit();
            } catch (RuntimeException exception) {
                if (transaction != null)
                    transaction.rollback();
            }
        }
        finally {
            close();
        }

    }

    @Override
    public void update(Long aLong, Artist entity) {
        initialize();
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.update(entity);
                transaction.commit();
            } catch (RuntimeException exception) {
                if (transaction != null)
                    transaction.rollback();
            }
        }
        finally {
            close();
        }

    }


    @Override
    public Artist findOne(Long aLong) {
        initialize();
        Artist artist = null;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();

                artist = session.createQuery("from Artist where id=:idx" , Artist.class)
                        .setParameter("idx", aLong)
                        .getSingleResult();

                transaction.commit();

                return artist;
            } catch (RuntimeException exception) {
                if (transaction != null)
                    transaction.rollback();
            }
        }
        finally {
            close();
        }
        return null;
    }

    @Override
    public List<Artist> findAll() {

        initialize();
        List<Artist> artists = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                artists = session.createQuery("select a from Artist a", Artist.class).getResultList();

                transaction.commit();

            } catch (RuntimeException exception) {
                if (transaction != null)
                    transaction.rollback();
            }
        }
        finally {
            close();
        }
        return artists;


    }


}
