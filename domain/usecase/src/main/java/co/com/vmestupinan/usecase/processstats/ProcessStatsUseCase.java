package co.com.vmestupinan.usecase.processstats;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;

import co.com.vmestupinan.model.events.gateways.EventsGateway;
import co.com.vmestupinan.model.exception.InvalidHashException;
import co.com.vmestupinan.model.stats.Stats;
import co.com.vmestupinan.model.stats.gateways.StatsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;

@Log
@RequiredArgsConstructor
public class ProcessStatsUseCase {
    private final StatsRepository statsRepository;
    private final EventsGateway eventsGateway;

    public Mono<Stats> excecute(Stats stats) {
        log.log(Level.INFO, "Processing stats validation");

        if (!isHashValid(stats)) {
            log.log(Level.SEVERE, "Invalid hash provided for stats validation");
            return Mono.error(new InvalidHashException("Invalid hash provided for stats validation"));
        }

        log.log(Level.INFO, "Stats validation successful, proceeding to save");
        return statsRepository.save(stats)
                .flatMap(savedStats -> eventsGateway.emit(savedStats)
                        .thenReturn(savedStats))
                .doOnSuccess(result -> log.log(Level.INFO, "Stats processed and saved successfully"))
                .doOnError(error -> log.log(Level.SEVERE, "Error processing stats: {}", error.getMessage()));
    }

    private boolean isHashValid(Stats stats) {
        String concatenated = String.join(",",
                String.valueOf(stats.getTotalContactoClientes()),
                String.valueOf(stats.getMotivoReclamo()),
                String.valueOf(stats.getMotivoGarantia()),
                String.valueOf(stats.getMotivoDuda()),
                String.valueOf(stats.getMotivoCompra()),
                String.valueOf(stats.getMotivoFelicitaciones()),
                String.valueOf(stats.getMotivoCambio()));

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(concatenated.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b & 0xff));
            }
            String calculatedHash = sb.toString();
            boolean isValid = calculatedHash.equalsIgnoreCase(stats.getHash());

            log.log(Level.INFO, "Hash validation result: {}", isValid ? "valid" : "invalid");

            return isValid;
        } catch (NoSuchAlgorithmException e) {
            log.log(Level.SEVERE, "MD5 algorithm not available", e);
            throw new RuntimeException("MD5 algorithm not available", e);
        }
    }
}