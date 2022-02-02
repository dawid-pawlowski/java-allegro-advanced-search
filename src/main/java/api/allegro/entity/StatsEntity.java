package api.allegro.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "stats")
public class StatsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    public long updateDate;
    // TODO: count offers in offer table instead (?)
    public long offerCount;

    public StatsEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }

    public long getOfferCount() {
        return offerCount;
    }

    public void setOfferCount(long offerCount) {
        this.offerCount = offerCount;
    }
}
