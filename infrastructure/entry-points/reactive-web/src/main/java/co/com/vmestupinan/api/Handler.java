package co.com.vmestupinan.api;

import java.time.Instant;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.vmestupinan.model.exception.InvalidHashException;
import co.com.vmestupinan.model.stats.Stats;
import co.com.vmestupinan.usecase.processstats.ProcessStatsUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

    private final ProcessStatsUseCase useCase;

    public Mono<ServerResponse> listenGETHelloWorld(ServerRequest serverRequest) {
        return ServerResponse.ok().bodyValue("Hola mundo");
    }

    public Mono<ServerResponse> listenPOSTProcessStats(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Stats.class)
                .flatMap(stats -> useCase.excecute(stats))
                .flatMap(this::createSuccessResponse)
                .onErrorResume(this::handleError);
    }

    private Mono<ServerResponse> createSuccessResponse(Stats stats) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of(
                        "status", "SUCCESS",
                        "message", "Statistics processed successfully",
                        "timestamp", String.valueOf(Instant.now()),
                        "data", stats));
    }

    private Mono<ServerResponse> handleError(Throwable error) {
        if (error instanceof InvalidHashException) {
            return createErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    "VALIDATION_ERROR",
                    error.getMessage());
        }

        return createErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_ERROR",
                "An unexpected error occurred while processing the request");
    }

    private Mono<ServerResponse> createErrorResponse(HttpStatus status, String errorCode, String message) {
        return ServerResponse.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of(
                        "status", "ERROR",
                        "errorCode", errorCode,
                        "message", message,
                        "timestamp", String.valueOf(Instant.now())));
    }
}
