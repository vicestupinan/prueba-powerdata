package co.com.vmestupinan.usecase.processstats;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import co.com.vmestupinan.model.events.gateways.EventsGateway;
import co.com.vmestupinan.model.exception.InvalidHashException;
import co.com.vmestupinan.model.stats.Stats;
import co.com.vmestupinan.model.stats.gateways.StatsRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class ProcessStatsUseCaseTest {
    @Mock
    private StatsRepository statsRepository;

    @Mock
    private EventsGateway eventsGateway;

    @InjectMocks
    private ProcessStatsUseCase processStatsUseCase;

    private Stats stats;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        stats = Stats.builder()
                .totalContactoClientes(250)
                .motivoReclamo(25)
                .motivoGarantia(10)
                .motivoDuda(100)
                .motivoCompra(100)
                .motivoFelicitaciones(7)
                .motivoCambio(8)
                .hash("5484062a4be1ce5645eb414663e14f59")
                .build();
    }

    @Test
    void processValidStats() {
        when(statsRepository.save(stats)).thenReturn(Mono.just(stats));
        when(eventsGateway.emit(stats)).thenReturn(Mono.empty());

        StepVerifier.create(processStatsUseCase.excecute(stats))
                .expectNext(stats)
                .verifyComplete();

        verify(statsRepository, times(1)).save(stats);
        verify(eventsGateway, times(1)).emit(stats);
    }

    @Test
    void processInvalidStats() {
        Stats invalidStats = stats.toBuilder()
                .hash("invalidhash")
                .build();

        StepVerifier.create(processStatsUseCase.excecute(invalidStats))
                .expectError(InvalidHashException.class)
                .verify();

        verify(statsRepository, never()).save(any());
        verify(eventsGateway, never()).emit(any());
    }
}
