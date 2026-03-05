<template>
  <div class="payment">
    <h1>Paiement</h1>

    <form @submit.prevent="processPayment" class="payment-form">
      <div class="form-group">
        <label>Numéro de carte</label>
        <input
            v-model="paymentData.cardNumber"
            type="text"
            placeholder="1234567890123456"
            maxlength="16"
            required
        />
      </div>

      <div class="form-group">
        <label>Titulaire de la carte</label>
        <input
            v-model="paymentData.cardHolder"
            type="text"
            placeholder="John Doe"
            required
        />
      </div>

      <div class="form-row">
        <div class="form-group">
          <label>Date d'expiration</label>
          <input
              v-model="paymentData.expirationDate"
              type="text"
              placeholder="12/26"
              maxlength="5"
              required
          />
        </div>

        <div class="form-group">
          <label>CVC</label>
          <input
              v-model="paymentData.cvc"
              type="text"
              placeholder="123"
              maxlength="3"
              required
          />
        </div>
      </div>

      <div class="form-group">
        <label>Montant (€)</label>
        <input
            v-model.number="paymentData.amount"
            type="number"
            step="0.01"
            placeholder="50.00"
            required
        />
      </div>

      <button type="submit" :disabled="loading" class="btn-submit">
        {{ loading ? 'Traitement...' : 'Payer' }}
      </button>
    </form>

    <div v-if="result" :class="['result', result.success ? 'success' : 'error']">
      <h3>{{ result.success ? 'Paiement accepté' : 'Paiement refusé' }}</h3>
      <p>{{ result.message }}</p>
      <p v-if="result.transactionId"><strong>ID Transaction:</strong> {{ result.transactionId }}</p>
    </div>

    <router-link to="/" class="back-link">← Retour à l'accueil</router-link>
  </div>
</template>

<script>
export default {
  data() {
    return {
      paymentData: {
        cardNumber: '',
        cardHolder: '',
        expirationDate: '',
        cvc: '',
        amount: null
      },
      loading: false,
      result: null
    }
  },
  methods: {
    async processPayment() {
      this.loading = true
      this.result = null

      try {
        const response = await fetch('http://localhost:8080/payment', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(this.paymentData)
        })

        this.result = await response.json()
      } catch (error) {
        this.result = {
          success: false,
          message: 'Erreur de connexion à l\'API: ' + error.message
        }
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.payment {
  max-width: 500px;
  margin: 0 auto;
  padding: 2rem;
  color: #e8e0d0;
}

h1 {
  color: #4ade80;
  margin-bottom: 0;
}

.payment-form {
  background: rgba(232, 224, 208, 0.04);
  border: 1px solid rgba(232, 224, 208, 0.1);
  padding: 2rem;
  border-radius: 8px;
  margin: 2rem 0;
}

.form-group {
  margin-bottom: 1rem;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
}

label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
  color: rgba(232, 224, 208, 0.6);
  font-size: 0.875rem;
}

input {
  width: 100%;
  padding: 0.75rem;
  background: rgba(232, 224, 208, 0.06);
  border: 1px solid rgba(232, 224, 208, 0.12);
  border-radius: 4px;
  font-size: 1rem;
  box-sizing: border-box;
  color: #e8e0d0;
  transition: border-color 0.2s;
}

input::placeholder {
  color: rgba(232, 224, 208, 0.25);
}

input:focus {
  outline: none;
  border-color: rgba(74, 222, 128, 0.45);
  background: rgba(232, 224, 208, 0.08);
}

.btn-submit {
  width: 100%;
  padding: 1rem;
  background: #4ade80;
  color: #080a0f;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.2s, opacity 0.2s;
  margin-top: 0.5rem;
}

.btn-submit:hover:not(:disabled) {
  background: #22c55e;
}

.btn-submit:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.result {
  padding: 1.5rem;
  border-radius: 8px;
  margin: 2rem 0;
}

.result.success {
  background: rgba(74, 222, 128, 0.08);
  border: 1px solid rgba(74, 222, 128, 0.3);
  color: #4ade80;
}

.result.error {
  background: rgba(248, 113, 113, 0.08);
  border: 1px solid rgba(248, 113, 113, 0.3);
  color: #f87171;
}

.result h3 {
  margin: 0 0 0.5rem;
}

.result p {
  margin: 0.25rem 0;
  font-size: 0.9rem;
  opacity: 0.85;
}

.back-link {
  display: inline-block;
  margin-top: 1rem;
  color: rgba(232, 224, 208, 0.4);
  text-decoration: none;
  font-size: 0.875rem;
  transition: color 0.2s;
}

.back-link:hover {
  color: #e8e0d0;
}
</style>