package co.com.vmestupinan.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import co.com.vmestupinan.model.stats.Stats;
import co.com.vmestupinan.usecase.processstats.ProcessStatsUseCase;
import reactor.core.publisher.Mono;

@ContextConfiguration(classes = { RouterRest.class, Handler.class })
@WebFluxTest
class RouterRestTest {
    @MockBean
    private ProcessStatsUseCase processStatsUseCase;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testListenPOSTProcessStats() {
        String validJson = """
                {
                  "totalContactoClientes": 250,
                  "motivoReclamo": 25,
                  "motivoGarantia": 10,
                  "motivoDuda": 100,
                  "motivoCompra": 100,
                  "motivoFelicitaciones": 7,
                  "motivoCambio": 8,
                  "hash": "5484062a4be1ce5645eb414663e14f56"
                }
                """;
        Stats expectedStats = new Stats(250, 25, 10, 100, 100, 7, 8, "5484062a4be1ce5645eb414663e14f56");

        when(processStatsUseCase.excecute(any()))
                .thenReturn(Mono.just(expectedStats));

        webTestClient.post()
                .uri("/stats")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(validJson)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo("SUCCESS")
                .jsonPath("$.message").isEqualTo("Statistics processed successfully")
                .jsonPath("$.data.hash").isEqualTo("5484062a4be1ce5645eb414663e14f56");
    }
}
