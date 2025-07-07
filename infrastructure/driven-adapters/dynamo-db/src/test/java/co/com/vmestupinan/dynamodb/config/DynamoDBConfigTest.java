package co.com.vmestupinan.dynamodb.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

@ExtendWith(MockitoExtension.class)
class DynamoDBConfigTest {

    @Mock
    private DynamoDbAsyncClient dynamoDbAsyncClient;

    private final DynamoDBConfig dynamoDBConfig = new DynamoDBConfig();

    @Test
    void testAmazonDynamoDB() {

        DynamoDbAsyncClient result = dynamoDBConfig.amazonDynamoDB(
                "http://localhost:8000", "us-west-2");

        assertNotNull(result);
    }

    @Test
    void testGetDynamoDbEnhancedAsyncClient() {
        DynamoDbEnhancedAsyncClient result = dynamoDBConfig.getDynamoDbEnhancedAsyncClient(dynamoDbAsyncClient);

        assertNotNull(result);
    }
}
