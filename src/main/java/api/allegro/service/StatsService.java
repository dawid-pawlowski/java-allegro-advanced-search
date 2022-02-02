package api.allegro.service;

import api.allegro.entity.StatsEntity;
import api.allegro.exception.AllegroBadRequestException;
import api.allegro.exception.AllegroUnauthorizedException;
import api.allegro.repository.StatsRepository;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class StatsService {

    private final StatsRepository repository;
    private final OfferService offerSrv;

    public StatsService(String pu, String accessToken) {
        repository = new StatsRepository(pu);
        offerSrv = new OfferService(pu, accessToken);
    }

    public void synchronize() throws IOException, InterruptedException, AllegroBadRequestException, AllegroUnauthorizedException {
        // TODO: should go into OfferService
        offerSrv.synchronize();
        repository.clear();

        StatsEntity stats = new StatsEntity();
        stats.setOfferCount(offerSrv.getOfferCount());
        stats.setUpdateDate(System.currentTimeMillis());
        repository.add(stats);
    }

    public String getUpdateDate() {
        StatsEntity stats = repository.findFirst();

        if (stats == null) {
            return "Unknown";
        }

        Instant timestamp = Instant.ofEpochMilli(stats.getUpdateDate());
        ZonedDateTime warsawTime = timestamp.atZone(ZoneId.of("Europe/Warsaw"));

        return warsawTime.toString();
    }

    public long getOfferCount() {
        StatsEntity stats = repository.findFirst();

        if (stats == null) {
            return 0;
        }

        return stats.getOfferCount();
    }
}
