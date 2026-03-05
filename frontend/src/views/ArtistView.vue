<template>
  <div class="artists">

    <!-- Header -->
    <div class="page-header">
      <router-link to="/" class="back-link">← Retour</router-link>
      <div class="header-top">
        <span class="page-number">01</span>
        <h1>Artistes</h1>
      </div>
      <p class="page-desc">Gérez les profils d'artistes du catalogue VOD.</p>
    </div>

    <!-- Create form -->
    <section class="section">
      <h2 class="section-title">Ajouter un artiste</h2>
      <div class="create-form">
        <div class="form-row">
          <div class="form-group">
            <label>Nom</label>
            <input v-model="newArtist.nom" type="text" placeholder="Dupont" />
          </div>
          <div class="form-group">
            <label>Prénom</label>
            <input v-model="newArtist.prenom" type="text" placeholder="Marie" />
          </div>
        </div>
        <div class="form-row">
          <div class="form-group">
            <label>Âge</label>
            <input v-model="newArtist.age" type="number" placeholder="34" />
          </div>
          <div class="form-group">
            <label>Type</label>
            <input v-model="newArtist.type" type="text" placeholder="Acteur, Réalisateur…" />
          </div>
        </div>
        <button @click="createArtist" :disabled="creating" class="btn-primary">
          <span v-if="creating">Création…</span>
          <span v-else>+ Créer l'artiste</span>
        </button>
      </div>
    </section>

    <!-- Artist list -->
    <section class="section">
      <div class="list-header">
        <h2 class="section-title">Liste des artistes</h2>
        <button @click="loadArtists" :disabled="loading" class="btn-secondary">
          {{ loading ? 'Chargement…' : '↻ Actualiser' }}
        </button>
      </div>

      <div v-if="loading" class="state-msg">Chargement des artistes…</div>

      <div v-else-if="artists.length === 0" class="state-msg">
        Aucun artiste trouvé. Commencez par en créer un.
      </div>

      <ul v-else class="artist-list">
        <li
          v-for="artist in artists"
          :key="artist.id"
          class="artist-card"
          :class="{ 'deleting': deletingId === artist.id }"
        >
          <div class="artist-avatar">
            {{ initials(artist) }}
          </div>
          <div class="artist-info">
            <span class="artist-name">{{ artist.nom }} {{ artist.prenom }}</span>
            <span class="artist-meta">
              <span class="tag">{{ artist.type }}</span>
              <span v-if="artist.age" class="age">{{ artist.age }} ans</span>
            </span>
          </div>
          <button @click="deleteArtist(artist.id)" class="btn-delete" title="Supprimer">
            ✕
          </button>
        </li>
      </ul>
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
      artists: [],
      newArtist: { nom: '', prenom: '', age: '', type: '' },
      loading: false,
      creating: false,
      deletingId: null,
      toast: null,
    }
  },
  mounted() {
    this.loadArtists()
  },
  methods: {
    async loadArtists() {
      this.loading = true
      try {
        const res = await fetch('http://localhost:8080/api/artists')
        this.artists = await res.json()
      } catch (e) {
        this.showToast('Erreur de connexion à l\'API', 'error')
      } finally {
        this.loading = false
      }
    },

    async createArtist() {
      if (!this.newArtist.nom || !this.newArtist.prenom) {
        this.showToast('Nom et prénom requis', 'error')
        return
      }
      this.creating = true
      try {
        await fetch('http://localhost:8080/api/artists', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(this.newArtist)
        })
        this.newArtist = { nom: '', prenom: '', age: '', type: '' }
        await this.loadArtists()
        this.showToast('Artiste créé avec succès', 'success')
      } catch (e) {
        this.showToast('Erreur lors de la création', 'error')
      } finally {
        this.creating = false
      }
    },

    async deleteArtist(id) {
      this.deletingId = id
      try {
        await fetch(`http://localhost:8080/api/artists/${id}`, { method: 'DELETE' })
        await this.loadArtists()
        this.showToast('Artiste supprimé', 'success')
      } catch (e) {
        this.showToast('Erreur lors de la suppression', 'error')
      } finally {
        this.deletingId = null
      }
    },

    initials(artist) {
      return ((artist.prenom?.[0] ?? '') + (artist.nom?.[0] ?? '')).toUpperCase() || '?'
    },

    showToast(message, type = 'success') {
      this.toast = { message, type }
      setTimeout(() => { this.toast = null }, 3000)
    }
  }
}
</script>

<style scoped>
.artists {
  max-width: 680px;
  margin: 0 auto;
  padding: 2rem;
  color: #e8e0d0;
  font-family: 'DM Sans', sans-serif;
}

/* ── Header ── */
.back-link {
  display: inline-block;
  color: rgba(232,224,208,0.35);
  text-decoration: none;
  font-size: 0.8rem;
  margin-bottom: 1.5rem;
  transition: color 0.2s;
}
.back-link:hover { color: #e8e0d0; }

.header-top {
  display: flex;
  align-items: baseline;
  gap: 1rem;
}

.page-number {
  font-family: 'Bebas Neue', sans-serif;
  font-size: 1rem;
  letter-spacing: 0.15em;
  color: #ffb432;
  opacity: 0.6;
}

h1 {
  font-family: 'Bebas Neue', sans-serif;
  font-size: 3.5rem;
  letter-spacing: 0.04em;
  color: #ffb432;
  margin: 0;
  line-height: 1;
}

.page-desc {
  color: rgba(232,224,208,0.4);
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
  color: rgba(232,224,208,0.5);
  margin: 0 0 1rem;
  text-transform: uppercase;
}

.list-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 1rem;
}
.list-header .section-title { margin-bottom: 0; }

/* ── Form ── */
.create-form {
  background: rgba(232,224,208,0.03);
  border: 1px solid rgba(232,224,208,0.08);
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
  color: rgba(232,224,208,0.5);
  font-weight: 500;
  letter-spacing: 0.05em;
}

input {
  background: rgba(232,224,208,0.06);
  border: 1px solid rgba(232,224,208,0.1);
  border-radius: 4px;
  padding: 0.65rem 0.85rem;
  color: #e8e0d0;
  font-size: 0.9rem;
  font-family: inherit;
  transition: border-color 0.2s, background 0.2s;
  box-sizing: border-box;
  width: 100%;
}

input::placeholder { color: rgba(232,224,208,0.2); }

input:focus {
  outline: none;
  border-color: rgba(255,180,50,0.4);
  background: rgba(232,224,208,0.08);
}

/* ── Buttons ── */
.btn-primary {
  align-self: flex-start;
  padding: 0.65rem 1.5rem;
  background: #ffb432;
  color: #080a0f;
  border: none;
  border-radius: 4px;
  font-size: 0.875rem;
  font-weight: 600;
  font-family: inherit;
  cursor: pointer;
  transition: background 0.2s, opacity 0.2s;
}
.btn-primary:hover:not(:disabled) { background: #ffa010; }
.btn-primary:disabled { opacity: 0.45; cursor: not-allowed; }

.btn-secondary {
  padding: 0.5rem 1rem;
  background: transparent;
  color: rgba(232,224,208,0.5);
  border: 1px solid rgba(232,224,208,0.15);
  border-radius: 4px;
  font-size: 0.8rem;
  font-family: inherit;
  cursor: pointer;
  transition: color 0.2s, border-color 0.2s;
}
.btn-secondary:hover:not(:disabled) {
  color: #e8e0d0;
  border-color: rgba(232,224,208,0.35);
}
.btn-secondary:disabled { opacity: 0.4; cursor: not-allowed; }

/* ── Artist list ── */
.artist-list {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: 0.6rem;
}

.artist-card {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 0.9rem 1.1rem;
  background: rgba(232,224,208,0.03);
  border: 1px solid rgba(232,224,208,0.08);
  border-radius: 6px;
  transition: background 0.2s, border-color 0.2s, opacity 0.3s;
}

.artist-card:hover {
  background: rgba(232,224,208,0.06);
  border-color: rgba(255,180,50,0.2);
}

.artist-card.deleting { opacity: 0.4; }

.artist-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: rgba(255,180,50,0.12);
  border: 1px solid rgba(255,180,50,0.25);
  color: #ffb432;
  font-family: 'Bebas Neue', sans-serif;
  font-size: 1rem;
  letter-spacing: 0.05em;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.artist-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.artist-name {
  font-size: 0.95rem;
  font-weight: 500;
  color: #e8e0d0;
}

.artist-meta {
  display: flex;
  align-items: center;
  gap: 0.6rem;
}

.tag {
  font-size: 0.7rem;
  padding: 0.15rem 0.55rem;
  border-radius: 2px;
  background: rgba(255,180,50,0.1);
  border: 1px solid rgba(255,180,50,0.2);
  color: #ffb432;
  font-family: 'JetBrains Mono', monospace;
  letter-spacing: 0.05em;
}

.age {
  font-size: 0.75rem;
  color: rgba(232,224,208,0.35);
}

.btn-delete {
  background: transparent;
  border: 1px solid transparent;
  color: rgba(232,224,208,0.2);
  font-size: 0.75rem;
  width: 30px;
  height: 30px;
  border-radius: 4px;
  cursor: pointer;
  transition: color 0.2s, border-color 0.2s, background 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.btn-delete:hover {
  color: #f87171;
  border-color: rgba(248,113,113,0.3);
  background: rgba(248,113,113,0.08);
}

/* ── States ── */
.state-msg {
  text-align: center;
  padding: 2.5rem;
  color: rgba(232,224,208,0.25);
  font-size: 0.875rem;
  border: 1px dashed rgba(232,224,208,0.08);
  border-radius: 6px;
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
}

.toast.success {
  background: rgba(74,222,128,0.1);
  border: 1px solid rgba(74,222,128,0.3);
  color: #4ade80;
}

.toast.error {
  background: rgba(248,113,113,0.1);
  border: 1px solid rgba(248,113,113,0.3);
  color: #f87171;
}

.toast-enter-active, .toast-leave-active { transition: all 0.3s ease; }
.toast-enter-from, .toast-leave-to { opacity: 0; transform: translateY(10px); }
</style>