package api.allegro.repository;

import api.allegro.entity.OfferEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

import java.util.List;

public class OfferRepository {
    private final EntityManager manager;
    private final EntityManagerFactory factory;

    public OfferRepository(String pu) {
        factory = Persistence.createEntityManagerFactory(pu);
        manager = factory.createEntityManager();
    }

    public OfferEntity add(OfferEntity offer) {
        manager.getTransaction().begin();
        manager.persist(offer);
        manager.getTransaction().commit();
        return offer;
    }

    public long count() {
        Query query = manager.createNativeQuery("SELECT COUNT(*) FROM Offer");
        return (Long) query.getSingleResult();
    }

    public void clear() {
        manager.getTransaction().begin();
        manager.clear();
        Query query = manager.createQuery("DELETE FROM OfferEntity o");
        query.executeUpdate();
        manager.getTransaction().commit();
    }

    public List<OfferEntity> getAll() {
        return (List<OfferEntity>) manager.createQuery("SELECT o FROM OfferEntity o").getResultList();
    }

    public void addAll(List<OfferEntity> offers) {
        manager.getTransaction().begin();

        for (int i = 0; i < offers.size(); i++) {
            manager.persist(offers.get(i));

            if (i % 20 == 0) {
                manager.flush();
                manager.clear();
            }
        }

        manager.getTransaction().commit();
    }

    public void updateAll(List<OfferEntity> offers) {
        manager.getTransaction().begin();

        for (int i = 0; i < offers.size(); i++) {
            manager.merge(offers.get(i));

            if (i % 20 == 0) {
                manager.flush();
                manager.clear();
            }
        }

        manager.getTransaction().commit();
    }

    public OfferEntity update(OfferEntity offer) {
        manager.getTransaction().begin();
        manager.merge(offer);
        manager.getTransaction().commit();
        return offer;
    }

    public OfferEntity getById(String offerId) {
        Query q = manager.createQuery("SELECT o FROM OfferEntity o WHERE o.id = :id");
        q.setParameter("id", offerId);
        return (OfferEntity) q.getResultStream().findFirst().orElse(null);
    }

    public void close() {
        manager.close();
        factory.close();
    }
}
