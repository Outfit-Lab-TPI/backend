# API de Suscripciones

## Endpoints

### 1. Crear Preferencia de Pago (MercadoPago)

**POST** `/api/mp/crear-suscripcion`

Genera un link de pago de MercadoPago para actualizar la suscripción.

**Request Body:**
```json
{
  "planId": "pro-monthly",
  "userEmail": "usuario@example.com",
  "price": 24990,
  "currency": "ARS"
}
```

**Response (200):**
```json
{
  "initPoint": "https://www.mercadopago.com.ar/checkout/v1/redirect?pref_id=..."
}
```

---

### 2. Webhook de MercadoPago

**POST** `/api/mp/webhooks?id={paymentId}&topic={topic}`

Procesa notificaciones de pago de MercadoPago. Actualiza automáticamente la suscripción del usuario cuando el pago es aprobado.

**Query Params:**
- `id`: ID del pago (Long)
- `topic`: Tipo de notificación (ej: "payment")

**Response (200):**
```text
Notification processed successfully.
```

---

### 3. Obtener Todos los Planes

**GET** `/api/mp/subscriptions`

Retorna todos los planes de suscripción disponibles.

**Response (200):**
```json
{
  "data": [
    {
      "id": 1,
      "planCode": "free-monthly",
      "name": "Plan Free",
      "price": 0,
      "feature1": "2 Favoritos",
      "feature2": "5 Outfits/día",
      "feature3": "2 Modelos 3D"
    },
    {
      "id": 2,
      "planCode": "pro-monthly",
      "name": "Plan PRO",
      "price": 24990,
      "feature1": "Favoritos ilimitados",
      "feature2": "Outfits ilimitados",
      "feature3": "Modelos 3D ilimitados"
    }
  ]
}
```

---

### 4. Obtener Suscripción del Usuario

**GET** `/api/mp/user-subscription?email={userEmail}`

Retorna el estado actual de la suscripción del usuario, incluyendo uso y límites.

**Query Params:**
- `email`: Email del usuario

**Response (200):**
```json
{
  "planCode": "pro-monthly",
  "status": "ACTIVE",
  "usage": {
    "combinations": {
      "used": 0,
      "max": null
    },
    "favorites": {
      "count": 3,
      "max": null
    },
    "models": {
      "generated": 0,
      "max": null
    }
  }
}
```

**Nota:** `max: null` indica límite ilimitado.

**Response (404):**
```json
{
  "error": "No se encontró suscripción para el usuario: usuario@example.com"
}
```

---

## Modificaciones en Usuarios

### Registro de Usuario

**Cambio:** Al registrarse, el usuario recibe automáticamente el plan **free-monthly**.

**Flujo:**
1. Usuario se registra → `POST /api/user/register`
2. Sistema crea cuenta
3. Sistema asigna plan FREE automáticamente
4. Usuario puede usar la app con límites del plan gratuito

---

## Validación de Límites

### Favoritos

**Endpoint afectado:** `POST /api/combination/add-to-favourite`

**Comportamiento:**
- **Plan Free (2 favoritos):** Bloquea al intentar agregar el 3er favorito
- **Plan PRO (ilimitado):** Permite agregar sin límite

**Response (403) cuando se excede:**
```json
{
  "error": "Has alcanzado el límite de favoritos para tu plan. Actualiza a PRO para acceso ilimitado."
}
```

---

## Códigos de Plan

| Código | Nombre | Precio | Favoritos | Outfits/día | Modelos 3D |
|--------|--------|--------|-----------|-------------|------------|
| `free-monthly` | Plan Free | $0 | 2 | 5 | 2 |
| `pro-monthly` | Plan PRO | $24.990 | ∞ | ∞ | ∞ |
