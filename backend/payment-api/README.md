# API de Paiement - Payment API

## Démarrage
```bash
docker-compose up -d
./gradlew bootRun
```

L'API tourne sur `http://localhost:8080`

## Endpoint

**POST /payment**

Simule un paiement par carte bancaire.

### Requête - Succès
```json
{
  "cardNumber": "1234567890123456",
  "cardHolder": "Julien Georgel",
  "expirationDate": "12/26",
  "cvc": "123",
  "amount": 50.0
}
```

### Réponse - Succès (200)
```json
{
  "success": true,
  "transactionId": "uuid-generated",
  "message": "Payment accepted"
}
```

### Requête - Échec 
```json
{
  "cardNumber": "",
  "cardHolder": "Julien Georgel",
  "expirationDate": "12/26",
  "cvc": "123",
  "amount": 50.0
}
```

### Réponse - Échec (402)
```json
{
  "success": false,
  "transactionId": null,
  "message": "Invalid card number"
}
```

## Documentation interactive

Swagger UI : http://localhost:8080/swagger-ui/index.html