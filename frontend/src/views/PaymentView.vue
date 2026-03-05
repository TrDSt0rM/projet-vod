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
}

.payment-form {
  background: #f9f9f9;
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
}

input {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
  box-sizing: border-box;
}

.btn-submit {
  width: 100%;
  padding: 1rem;
  background: #42b983;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1.1rem;
  cursor: pointer;
  transition: background 0.3s;
}

.btn-submit:hover:not(:disabled) {
  background: #35a372;
}

.btn-submit:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.result {
  padding: 1.5rem;
  border-radius: 8px;
  margin: 2rem 0;
}

.result.success {
  background: #d4edda;
  border: 1px solid #c3e6cb;
  color: #155724;
}

.result.error {
  background: #f8d7da;
  border: 1px solid #f5c6cb;
  color: #721c24;
}

.back-link {
  display: inline-block;
  margin-top: 2rem;
  color: #42b983;
  text-decoration: none;
}

.back-link:hover {
  text-decoration: underline;
}
</style>