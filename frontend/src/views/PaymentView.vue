<template>
  <div class="payment">

    <!-- Header -->
    <div class="page-header">
      <router-link to="/" class="back-link">← Retour</router-link>
      <div class="header-top">
        <span class="page-number">03</span>
        <h1>Paiement</h1>
      </div>
      <p class="page-desc">Traitez vos transactions via les passerelles intégrées.</p>
    </div>

    <!-- Payment form -->
    <section class="section">
      <h2 class="section-title">Informations de paiement</h2>
      <div class="create-form">

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

        <button @click="processPayment" :disabled="loading" class="btn-primary">
          <span v-if="loading">Traitement…</span>
          <span v-else>Payer</span>
        </button>

      </div>
    </section>

    <!-- Result -->
    <section v-if="result" class="section">
      <div :class="['result', result.success ? 'success' : 'error']">
        <h3>{{ result.success ? '✔ Paiement accepté' : '✕ Paiement refusé' }}</h3>
        <p>{{ result.message }}</p>
        <p v-if="result.transactionId" class="transaction-id">
          <strong>ID Transaction :</strong> {{ result.transactionId }}
        </p>
      </div>
    </section>

    <!-- Toast notification -->
    <transition name="toast">
      <div v-if="toast" :class="['toast', toast.type]">{{ toast.message }}</div>
    </transition>

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
      result: null,
      toast: null,
    }
  },
  methods: {
    async processPayment() {
      if (!this.paymentData.cardNumber || !this.paymentData.cardHolder) {
        this.showToast('Veuillez remplir tous les champs', 'error')
        return
      }
      this.loading = true
      this.result = null
      try {
        const response = await fetch('/api/payment', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(this.paymentData)
        })
        const text = await response.text()
        if (!text) throw new Error('Réponse vide du serveur')
        this.result = JSON.parse(text)
        this.showToast(
            this.result.success ? 'Paiement accepté' : 'Paiement refusé',
            this.result.success ? 'success' : 'error'
        )
      } catch (error) {
        this.result = { success: false, message: 'Erreur de connexion à l\'API : ' + error.message }
        this.showToast('Erreur de connexion à l\'API', 'error')
      } finally {
        this.loading = false
      }
    },

    showToast(message, type = 'success') {
      this.toast = { message, type }
      setTimeout(() => { this.toast = null }, 3000)
    }
  }
}
</script>

<style scoped>
.payment {
  max-width: 680px;
  margin: 0 auto;
  padding: 2rem;
  color: #1a1a1a;
  font-family: 'DM Sans', sans-serif;
  background-color: #ffffff;
  min-height: 100vh;
}

/* ── Header ── */
.back-link {
  display: inline-block;
  color: #6b7280;
  text-decoration: none;
  font-size: 0.8rem;
  margin-bottom: 1.5rem;
  transition: color 0.2s;
}
.back-link:hover { color: #1a1a1a; }

.header-top {
  display: flex;
  align-items: baseline;
  gap: 1rem;
}

.page-number {
  font-family: 'Bebas Neue', sans-serif;
  font-size: 1rem;
  letter-spacing: 0.15em;
  color: #4ade80;
  opacity: 0.8;
}

h1 {
  font-family: 'Bebas Neue', sans-serif;
  font-size: 3.5rem;
  letter-spacing: 0.04em;
  color: #4ade80;
  margin: 0;
  line-height: 1;
}

.page-desc {
  color: #6b7280;
  font-size: 0.875rem;
  margin: 0.5rem 0 2.5rem;
  font-weight: 300;
}

/* ── Sections ── */
.section {
  margin-bottom: 2.5rem;
}

.section-title {
  font-family: 'Bebas Neue', sans-serif;
  font-size: 1.1rem;
  letter-spacing: 0.12em;
  color: #4b5563;
  margin: 0 0 1rem;
  text-transform: uppercase;
}

/* ── Form ── */
.create-form {
  background: #f9fafb;
  border: 1px solid #e5e7eb;
  border-radius: 6px;
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
}

@media (max-width: 480px) {
  .form-row { grid-template-columns: 1fr; }
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.4rem;
}

label {
  font-size: 0.75rem;
  color: #4b5563;
  font-weight: 500;
  letter-spacing: 0.05em;
}

input {
  background: #ffffff;
  border: 1px solid #d1d5db;
  border-radius: 4px;
  padding: 0.65rem 0.85rem;
  color: #1a1a1a;
  font-size: 0.9rem;
  font-family: inherit;
  transition: border-color 0.2s, box-shadow 0.2s;
  box-sizing: border-box;
  width: 100%;
}

input::placeholder { color: #9ca3af; }
input:focus {
  outline: none;
  border-color: #4ade80;
  box-shadow: 0 0 0 3px rgba(74, 222, 128, 0.1);
}

/* ── Button ── */
.btn-primary {
  align-self: flex-start;
  padding: 0.65rem 1.5rem;
  background: #4ade80;
  color: #ffffff;
  border: none;
  border-radius: 4px;
  font-size: 0.875rem;
  font-weight: 600;
  font-family: inherit;
  cursor: pointer;
  transition: background 0.2s, opacity 0.2s;
}
.btn-primary:hover:not(:disabled) { background: #22c55e; }
.btn-primary:disabled { opacity: 0.45; cursor: not-allowed; }

/* ── Result ── */
.result {
  padding: 1.25rem 1.5rem;
  border-radius: 6px;
}

.result h3 {
  font-family: 'Bebas Neue', sans-serif;
  font-size: 1.3rem;
  letter-spacing: 0.05em;
  margin: 0 0 0.4rem;
}

.result p { font-size: 0.9rem; margin: 0.2rem 0; }

.result.success {
  background: #d1fae5;
  border: 1px solid #6ee7b7;
  color: #047857;
}

.result.error {
  background: #fee2e2;
  border: 1px solid #fca5a5;
  color: #dc2626;
}

.transaction-id {
  font-family: 'JetBrains Mono', monospace;
  font-size: 0.78rem !important;
  margin-top: 0.4rem !important;
  opacity: 0.8;
}

/* ── Toast ── */
.toast {
  position: fixed;
  bottom: 2rem;
  right: 2rem;
  padding: 0.75rem 1.25rem;
  border-radius: 4px;
  font-size: 0.85rem;
  font-family: 'JetBrains Mono', monospace;
  z-index: 100;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.toast.success {
  background: #d1fae5;
  border: 1px solid #6ee7b7;
  color: #047857;
}

.toast.error {
  background: #fee2e2;
  border: 1px solid #fca5a5;
  color: #dc2626;
}

.toast-enter-active, .toast-leave-active { transition: all 0.3s ease; }
.toast-enter-from, .toast-leave-to { opacity: 0; transform: translateY(10px); }
</style>