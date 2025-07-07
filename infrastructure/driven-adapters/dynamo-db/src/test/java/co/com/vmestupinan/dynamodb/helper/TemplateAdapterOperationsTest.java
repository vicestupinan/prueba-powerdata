package co.com.vmestupinan.dynamodb.helper;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.reactivecommons.utils.ObjectMapper;

import co.com.vmestupinan.dynamodb.DynamoDBTemplateAdapter;
import co.com.vmestupinan.dynamodb.StatsEntity;
import co.com.vmestupinan.model.stats.Stats;
import reactor.test.StepVerifier;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

class TemplateAdapterOperationsTest {

    @Mock
    private DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private DynamoDbAsyncTable<StatsEntity> customerTable;

    private StatsEntity statsEntity;

    private Stats stats;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(dynamoDbEnhancedAsyncClient.table("stats", TableSchema.fromBean(StatsEntity.class)))
                .thenReturn(customerTable);

        statsEntity = new StatsEntity();
        statsEntity.setTotalContactoClientes(250);
        statsEntity.setMotivoReclamo(25);
        statsEntity.setMotivoGarantia(10);
        statsEntity.setMotivoDuda(100);
        statsEntity.setMotivoCompra(100);
        statsEntity.setMotivoFelicitaciones(7);
        statsEntity.setMotivoCambio(8);
        statsEntity.setHash("5484062a4be1ce5645eb414663e14f59");

        stats = new Stats();
        stats.setTotalContactoClientes(250);
        stats.setMotivoReclamo(25);
        stats.setMotivoGarantia(10);
        stats.setMotivoDuda(100);
        stats.setMotivoCompra(100);
        stats.setMotivoFelicitaciones(7);
        stats.setMotivoCambio(8);
        stats.setHash("5484062a4be1ce5645eb414663e14f59");
    }

    @Test
    void modelEntityPropertiesMustNotBeNull() {
        StatsEntity statsEntityUnderTest = new StatsEntity(250, 25, 10, 100, 100, 7, 8,
                "5484062a4be1ce5645eb414663e14f59");

        assertNotNull(statsEntityUnderTest.getTotalContactoClientes());
        assertNotNull(statsEntityUnderTest.getMotivoReclamo());
        assertNotNull(statsEntityUnderTest.getMotivoGarantia());
        assertNotNull(statsEntityUnderTest.getMotivoDuda());
        assertNotNull(statsEntityUnderTest.getMotivoCompra());
        assertNotNull(statsEntityUnderTest.getMotivoFelicitaciones());
        assertNotNull(statsEntityUnderTest.getMotivoCambio());
        assertNotNull(statsEntityUnderTest.getHash());
    }

    @Test
    void testSave() {
        when(mapper.map(any(Stats.class), eq(StatsEntity.class))).thenReturn(statsEntity);
        when(customerTable.putItem(any(StatsEntity.class)))
                .thenReturn(CompletableFuture.completedFuture(null));

        DynamoDBTemplateAdapter dynamoDBTemplateAdapter = new DynamoDBTemplateAdapter(dynamoDbEnhancedAsyncClient,
                mapper);

        StepVerifier.create(dynamoDBTemplateAdapter.save(stats))
                .expectNext(stats)
                .verifyComplete();
    }
}