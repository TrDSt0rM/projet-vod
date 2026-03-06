<template>
  <div class="auth">

    <!-- Header -->
    <div class="page-header">
      <router-link to="/" class="back-link">← Retour</router-link>
      <div class="header-top">
        <span class="page-number">02</span>
        <h1>Authentification</h1>
      </div>
      <p class="page-desc">Inscription et connexion au système VOD.</p>
    </div>

    <!-- Toggle -->
    <div class="toggle-bar">
      <button :class="['toggle-btn', !isRegister ? 'active' : '']" @click="isRegister = false">
        Connexion
      </button>
      <button :class="['toggle-btn', isRegister ? 'active' : '']" @click="isRegister = true">
        Créer un compte
      </button>
    </div>

    <!-- Form -->
    <section class="section">
      <h2 class="section-title">{{ isRegister ? 'Nouveau compte' : 'Se connecter' }}</h2>
      <div class="create-form">

        <div class="form-group">
          <label>Pseudo</label>
          <input v-model="pseudo" type="text" placeholder="monpseudo" required />
        </div>

        <template v-if="isRegister">
          <div class="form-row">
            <div class="form-group">
              <label>Nom</label>
              <input v-model="nom" type="text" placeholder="Dupont" required />
            </div>
            <div class="form-group">
              <label>Prénom</label>
              <input v-model="prenom" type="text" placeholder="Marie" required />
            </div>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label>Âge</label>
              <input v-model="age" type="number" placeholder="25" required />
            </div>
            <div class="form-group">
              <label>Adresse</label>
              <input v-model="adresse" type="text" placeholder="1 rue de la Paix" required />
            </div>
          </div>
        </template>

        <div class="form-group">
          <label>Mot de passe</label>
          <input v-model="password" type="password" placeholder="••••••••" required />
        </div>

        <button @click="submit" :disabled="loading" class="btn-primary">
          <span v-if="loading">Chargement…</span>
          <span v-else>{{ isRegister ? 'Créer le compte' : 'Se connecter' }}</span>
        </button>

      </div>
    </section>

    <!-- Toast -->
    <transition name="toast">
      <div v-if="toast" :class="['toast', toast.type]">{{ toast.message }}</div>
    </transition>

  </div>
</template>

<script>
import axios from 'axios'

export default {
  data() {
    return {
      isRegister: false,
      pseudo: '',
      nom: '',
      prenom: '',
      age: '',
      adresse: '',
      password: '',
      loading: false,
      toast: null,
    }
  },
  mounted() {
    if (localStorage.getItem('token')) this.$router.push('/')
  },
  watch: {
    isRegister() {
      this.pseudo = ''
      this.nom = ''
      this.prenom = ''
      this.age = ''
      this.adresse = ''
      this.password = ''
    }
  },
  methods: {
    async submit() {
      this.loading = true
      try {
        if (this.isRegister) {
          await axios.post('/api/auth/register', {
            pseudo: this.pseudo,
            nom: this.nom,
            prenom: this.prenom,
            age: Number(this.age),
            adresse: this.adresse,
            password: this.password
          })
          this.showToast('Compte créé avec succès', 'success')
          this.isRegister = false
        } else {
          const response = await axios.post('/api/auth/login', {
            pseudo: this.pseudo,
            password: this.password
          })
          localStorage.setItem('token', response.data.token)
          this.$router.push('/')
        }
      } catch (e) {
        this.showToast(e.response?.data?.message || 'Impossible de contacter l\'API', 'error')
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
.auth {
  max-width: 680px;
  margin: 0 auto;
  padding: 2rem;
  color: #1a1a1a;
  font-family: 'DM Sans', sans-serif;
  background-color: #ffffff;
  min-height: 100vh;
}

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
  color: #60a5fa;
  opacity: 0.8;
}

h1 {
  font-family: 'Bebas Neue', sans-serif;
  font-size: 3.5rem;
  letter-spacing: 0.04em;
  color: #60a5fa;
  margin: 0;
  line-height: 1;
}

.page-desc {
  color: #6b7280;
  font-size: 0.875rem;
  margin: 0.5rem 0 2.5rem;
  font-weight: 300;
}

/* ── Toggle ── */
.toggle-bar {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 2rem;
}

.toggle-btn {
  padding: 0.5rem 1.25rem;
  border-radius: 4px;
  font-size: 0.85rem;
  font-family: inherit;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
  background: transparent;
  border: 1px solid #d1d5db;
  color: #6b7280;
}

.toggle-btn.active {
  background: #60a5fa;
  border-color: #60a5fa;
  color: #ffffff;
}

.toggle-btn:not(.active):hover {
  border-color: #60a5fa;
  color: #60a5fa;
}

/* ── Section ── */
.section { margin-bottom: 2.5rem; }

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
  border-color: #60a5fa;
  box-shadow: 0 0 0 3px rgba(96, 165, 250, 0.1);
}

.btn-primary {
  align-self: flex-start;
  padding: 0.65rem 1.5rem;
  background: #60a5fa;
  color: #ffffff;
  border: none;
  border-radius: 4px;
  font-size: 0.875rem;
  font-weight: 600;
  font-family: inherit;
  cursor: pointer;
  transition: background 0.2s, opacity 0.2s;
}
.btn-primary:hover:not(:disabled) { background: #3b82f6; }
.btn-primary:disabled { opacity: 0.45; cursor: not-allowed; }

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