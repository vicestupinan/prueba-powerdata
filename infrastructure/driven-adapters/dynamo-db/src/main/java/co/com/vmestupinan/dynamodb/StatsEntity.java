package co.com.vmestupinan.dynamodb;

import java.time.Instant;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

/* Enhanced DynamoDB annotations are incompatible with Lombok #1932
         https://github.com/aws/aws-sdk-java-v2/issues/1932*/
@DynamoDbBean
public class StatsEntity {

    private String timestamp;
    private Integer totalContactoClientes;
    private Integer motivoReclamo;
    private Integer motivoGarantia;
    private Integer motivoDuda;
    private Integer motivoCompra;
    private Integer motivoFelicitaciones;
    private Integer motivoCambio;
    private String hash;

    public StatsEntity() {
        this.timestamp = Instant.now().toString();
    }

    public StatsEntity(Integer totalContactoClientes, Integer motivoReclamo, Integer motivoGarantia, Integer motivoDuda, Integer motivoCompra, Integer motivoFelicitaciones, Integer motivoCambio, String hash) {
        this.timestamp = Instant.now().toString();
        this.totalContactoClientes = totalContactoClientes;
        this.motivoReclamo = motivoReclamo;
        this.motivoGarantia = motivoGarantia;
        this.motivoDuda = motivoDuda;
        this.motivoCompra = motivoCompra;
        this.motivoFelicitaciones = motivoFelicitaciones;
        this.motivoCambio = motivoCambio;
        this.hash = hash;
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("timestamp")
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @DynamoDbAttribute("totalContactoClientes")
    public Integer getTotalContactoClientes() {
        return totalContactoClientes;
    }

    public void setTotalContactoClientes(Integer totalContactoClientes) {
        this.totalContactoClientes = totalContactoClientes;
    }

    @DynamoDbAttribute("motivoReclamo")
    public Integer getMotivoReclamo() {
        return motivoReclamo;
    }

    public void setMotivoReclamo(Integer motivoReclamo) {
        this.motivoReclamo = motivoReclamo;
    }

    @DynamoDbAttribute("motivoGarantia")
    public Integer getMotivoGarantia() {
        return motivoGarantia;
    }

    public void setMotivoGarantia(Integer motivoGarantia) {
        this.motivoGarantia = motivoGarantia;
    }

    @DynamoDbAttribute("motivoDuda")
    public Integer getMotivoDuda() {
        return motivoDuda;
    }

    public void setMotivoDuda(Integer motivoDuda) {
        this.motivoDuda = motivoDuda;
    }

    @DynamoDbAttribute("motivoCompra")
    public Integer getMotivoCompra() {
        return motivoCompra;
    }

    public void setMotivoCompra(Integer motivoCompra) {
        this.motivoCompra = motivoCompra;
    }

    @DynamoDbAttribute("motivoFelicitaciones")
    public Integer getMotivoFelicitaciones() {
        return motivoFelicitaciones;
    }

    public void setMotivoFelicitaciones(Integer motivoFelicitaciones) {
        this.motivoFelicitaciones = motivoFelicitaciones;
    }

    @DynamoDbAttribute("motivoCambio")
    public Integer getMotivoCambio() {
        return motivoCambio;
    }

    public void setMotivoCambio(Integer motivoCambio) {
        this.motivoCambio = motivoCambio;
    }

    @DynamoDbAttribute("hash")
    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
