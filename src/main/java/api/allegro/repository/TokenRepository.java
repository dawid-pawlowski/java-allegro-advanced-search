package api.allegro.repository;

import api.allegro.entity.TokenEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

public class TokenRepository {
    private final EntityManager manager;
    private final EntityManagerFactory factory;

    public TokenRepository(String pu) {
        factory = Persistence.createEntityManagerFactory(pu);
        manager = factory.createEntityManager();
    }

    public TokenEntity add(TokenEntity token) {
        manager.getTransaction().begin();
        manager.persist(token);
        manager.getTransaction().commit();
        return token;
    }

    public TokenEntity update(TokenEntity token) {
        manager.getTransaction().begin();
        manager.merge(token);
        manager.getTransaction().commit();
        return token;
    }

    public TokenEntity findFirst() {
        Query query = manager.createQuery("SELECT t FROM TokenEntity t");
        return (TokenEntity) query.getResultStream().findFirst().orElse(null);
    }

    public TokenEntity find(long id) {
        return manager.find(TokenEntity.class, id);
    }

    public void clear() {
        manager.getTransaction().begin();
        Query query = manager.createQuery("DELETE FROM TokenEntity t");
        query.executeUpdate();
        manager.getTransaction().commit();
    }

    public void close() {
        manager.close();
        factory.close();
    }
}
