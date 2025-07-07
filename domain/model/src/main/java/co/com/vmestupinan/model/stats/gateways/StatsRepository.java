package co.com.vmestupinan.model.stats.gateways;

import co.com.vmestupinan.model.stats.Stats;
import reactor.core.publisher.Mono;

public interface StatsRepository {
    Mono<Stats> save(Stats stats);
}
