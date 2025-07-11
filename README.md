# PowerData - Microservicio de Procesamiento de Estadísticas

Este proyecto es un microservicio desarrollado con **Spring WebFlux**, basado en el **scaffold de Clean Architecture** de Bancolombia, y persiste los datos en **DynamoDB**. Expone un endpoint `/stats` que procesa estadísticas enviadas como JSON.

## Prerrequisitos

- Java 21
- Gradle
- Docker y Docker Compose
- AWS CLI configurado (para local con DynamoDB)

## Levantar entorno Docker Compose
El archivo docker-compose.yml levanta los siguientes servicios:

DynamoDB local

RabbitMQ

docker-compose up --build

## Creacion de tablas en dynamo
```bash
aws dynamodb create-table `
--table-name stats `
--attribute-definitions AttributeName=timestamp,AttributeType=S `
--key-schema AttributeName=timestamp,KeyType=HASH `
--provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 `
--endpoint-url http://localhost:8000 `
--region us-west-2
```

## Ejecutar el Servicio

Para ejecutar el microservicio localmente:

gradle clean build

gradle bootRun

## Ejecutar el Servicio **/stats**

Ejemplo entrada:
```bash
curl -X POST http://localhost:8080/stats \
  -H "Content-Type: application/json" \
  -d '{
    "totalContactoClientes": 250,
    "motivoReclamo": 25,
    "motivoGarantia": 10,
    "motivoDuda": 100,
    "motivoCompra": 100,
    "motivoFelicitaciones": 7,
    "motivoCambio": 8,
    "hash": "5484062a4be1ce5645eb414663e14f56"
}'
```

Resultado esperado:
```bash
{
  "status": "SUCCESS",
  "message": "Statistics processed successfully",
  "timestamp": "2025-07-07T01:23:45Z",
  "data": {
    ...
  }
}
```

## Ejecutar pruebas
gradle test
