package api.allegro.repository;

import api.allegro.entity.TokenEntity;
import jakarta.persistence.*;
import java.util.*;

import api.allegro.entity.StatsEntity;

public class StatsRepository {
    private final EntityManager manager;
    private final EntityManagerFactory factory;

    public StatsRepository(String pu) {
        factory = Persistence.createEntityManagerFactory(pu);
        manager = factory.createEntityManager();
    }

    public StatsEntity add(StatsEntity stats) {
        manager.getTransaction().begin();
        manager.persist(stats);
        manager.getTransaction().commit();
        return stats;
    }

    public StatsEntity update(StatsEntity stats) {
        manager.getTransaction().begin();
        manager.merge(stats);
        manager.getTransaction().commit();
        return stats;
    }

    public StatsEntity findFirst() {
        Query query = manager.createQuery("SELECT s FROM StatsEntity s");
        return (StatsEntity) query.getResultStream().findFirst().orElse(null);
    }

    public StatsEntity find(long id) {
        return manager.find(StatsEntity.class, id);
    }

    public void clear() {
        manager.getTransaction().begin();
        Query query = manager.createNativeQuery("DELETE FROM StatsEntity s");
        query.executeUpdate();
        manager.getTransaction().commit();
    }

    public void close() {
        manager.close();
        factory.close();
    }
}
