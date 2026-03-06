<template>
  <div class="scraping">

    <!-- Header -->
    <div class="page-header">
      <router-link to="/" class="back-link">← Retour</router-link>
      <div class="header-top">
        <span class="page-number">04</span>
        <h1>Scraping</h1>
      </div>
      <p class="page-desc">Recherchez et collectez des données de films depuis des sources externes.</p>
    </div>

    <!-- Recherche synchrone -->
    <section class="section">
      <h2 class="section-title">Recherche directe</h2>
      <div class="create-form">
        <div class="form-group">
          <label>Titre du film</label>
          <input v-model="searchTitle" type="text" placeholder="Avatar, Titanic…" />
        </div>
        <button @click="searchMovie" :disabled="searching" class="btn-primary">
          <span v-if="searching">Recherche…</span>
          <span v-else>🔍 Rechercher</span>
        </button>
      </div>

      <!-- Résultat film -->
      <div v-if="movie" class="movie-card">
        <div class="movie-header">
          <img v-if="movie.posterUrl" :src="movie.posterUrl" class="movie-poster" alt="Poster" />
          <div class="movie-info">
            <h3 class="movie-title">{{ movie.title }}</h3>
            <div class="movie-meta">
              <span v-if="movie.realisationYear" class="tag">{{ movie.realisationYear }}</span>
              <span v-if="movie.minimalYear" class="tag">{{ movie.minimalYear }}+</span>
              <span :class="['tag', movie.isRentable ? 'tag-green' : 'tag-red']">
                {{ movie.isRentable ? 'Disponible' : 'Indisponible' }}
              </span>
            </div>
            <div v-if="movie.genres && movie.genres.length" class="movie-genres">
              <span v-for="genre in movie.genres" :key="genre.name" class="tag tag-blue">
                {{ genre.name }}
              </span>
            </div>
          </div>
        </div>
        <div v-if="movie.artists && movie.artists.length" class="artist-list">
          <p class="list-label">Artistes</p>
          <div class="artist-chips">
            <span v-for="artist in movie.artists" :key="artist.lastName" class="artist-chip">
              {{ artist.firstName }} {{ artist.lastName }}
              <span v-if="artist.role" class="chip-role">{{ artist.role }}</span>
            </span>
          </div>
        </div>
        <a v-if="movie.sourceUrl" :href="movie.sourceUrl" target="_blank" class="source-link">
          Voir la source ↗
        </a>
      </div>
    </section>

    <!-- Tâche asynchrone -->
    <section class="section">
      <div class="list-header">
        <h2 class="section-title">Tâche asynchrone</h2>
      </div>
      <div class="create-form">
        <div class="form-group">
          <label>Titre du film</label>
          <input v-model="taskTitle" type="text" placeholder="Inception, Matrix…" />
        </div>
        <button @click="startTask" :disabled="taskLoading" class="btn-primary">
          <span v-if="taskLoading">Lancement…</span>
          <span v-else>▶ Lancer la tâche</span>
        </button>
      </div>

      <!-- Suivi de tâche -->
      <div v-if="task" class="task-card">
        <div class="task-header">
          <span class="task-id">ID : {{ task.id }}</span>
          <span :class="['task-status', statusClass(task.status)]">{{ task.status }}</span>
        </div>
        <button @click="refreshTask" :disabled="refreshing" class="btn-secondary">
          {{ refreshing ? 'Actualisation…' : '↻ Actualiser le statut' }}
        </button>
        <div v-if="task.result" class="task-result">
          <p class="list-label">Résultat</p>
          <p class="movie-title-sm">{{ task.result.title }} ({{ task.result.realisationYear }})</p>
        </div>
      </div>
    </section>

    <!-- Toast -->
    <transition name="toast">
      <div v-if="toast" :class="['toast', toast.type]">{{ toast.message }}</div>
    </transition>

  </div>
</template>

<script>
export default {
  data() {
    return {
      searchTitle: '',
      movie: null,
      searching: false,
      taskTitle: '',
      task: null,
      taskLoading: false,
      refreshing: false,
      toast: null,
    }
  },
  methods: {
    async searchMovie() {
      if (!this.searchTitle) {
        this.showToast('Veuillez entrer un titre', 'error')
        return
      }
      this.searching = true
      this.movie = null
      try {
        const res = await fetch(`/api/scraping/movie?title=${encodeURIComponent(this.searchTitle)}`)
        if (res.status === 404) {
          this.showToast('Film non trouvé', 'error')
          return
        }
        this.movie = await res.json()
        this.showToast('Film trouvé', 'success')
      } catch (e) {
        this.showToast('Erreur de connexion à l\'API', 'error')
      } finally {
        this.searching = false
      }
    },

    async startTask() {
      if (!this.taskTitle) {
        this.showToast('Veuillez entrer un titre', 'error')
        return
      }
      this.taskLoading = true
      this.task = null
      try {
        const res = await fetch('/api/scraping/tasks', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ title: this.taskTitle })
        })
        this.task = await res.json()
        this.showToast('Tâche lancée', 'success')
      } catch (e) {
        this.showToast('Erreur lors du lancement de la tâche', 'error')
      } finally {
        this.taskLoading = false
      }
    },

    async refreshTask() {
      if (!this.task?.id) return
      this.refreshing = true
      try {
        const res = await fetch(`/api/scraping/tasks/${this.task.id}`)
        this.task = await res.json()
        this.showToast('Statut mis à jour', 'success')
      } catch (e) {
        this.showToast('Erreur lors de la récupération du statut', 'error')
      } finally {
        this.refreshing = false
      }
    },

    statusClass(status) {
      if (!status) return ''
      const s = status.toString().toUpperCase()
      if (s === 'DONE' || s === 'SUCCESS') return 'status-done'
      if (s === 'PENDING' || s === 'RUNNING') return 'status-pending'
      if (s === 'ERROR' || s === 'FAILED') return 'status-error'
      return ''
    },

    showToast(message, type = 'success') {
      this.toast = { message, type }
      setTimeout(() => { this.toast = null }, 3000)
    }
  }
}
</script>

<style scoped>
.scraping {
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
  color: #e879f9;
  opacity: 0.8;
}

h1 {
  font-family: 'Bebas Neue', sans-serif;
  font-size: 3.5rem;
  letter-spacing: 0.04em;
  color: #e879f9;
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
.section { margin-bottom: 2.5rem; }

.section-title {
  font-family: 'Bebas Neue', sans-serif;
  font-size: 1.1rem;
  letter-spacing: 0.12em;
  color: #4b5563;
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
  background: #f9fafb;
  border: 1px solid #e5e7eb;
  border-radius: 6px;
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;
  margin-bottom: 1.5rem;
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
  border-color: #e879f9;
  box-shadow: 0 0 0 3px rgba(232, 121, 249, 0.1);
}

/* ── Buttons ── */
.btn-primary {
  align-self: flex-start;
  padding: 0.65rem 1.5rem;
  background: #e879f9;
  color: #ffffff;
  border: none;
  border-radius: 4px;
  font-size: 0.875rem;
  font-weight: 600;
  font-family: inherit;
  cursor: pointer;
  transition: background 0.2s, opacity 0.2s;
}
.btn-primary:hover:not(:disabled) { background: #d946ef; }
.btn-primary:disabled { opacity: 0.45; cursor: not-allowed; }

.btn-secondary {
  padding: 0.5rem 1rem;
  background: transparent;
  color: #6b7280;
  border: 1px solid #d1d5db;
  border-radius: 4px;
  font-size: 0.8rem;
  font-family: inherit;
  cursor: pointer;
  transition: color 0.2s, border-color 0.2s;
}
.btn-secondary:hover:not(:disabled) { color: #1a1a1a; border-color: #9ca3af; }
.btn-secondary:disabled { opacity: 0.4; cursor: not-allowed; }

/* ── Tags ── */
.tag {
  font-size: 0.7rem;
  padding: 0.15rem 0.55rem;
  border-radius: 2px;
  background: #f3f4f6;
  border: 1px solid #e5e7eb;
  color: #4b5563;
  font-family: 'JetBrains Mono', monospace;
  letter-spacing: 0.05em;
}

.tag-green { background: #d1fae5; border-color: #6ee7b7; color: #047857; }
.tag-red   { background: #fee2e2; border-color: #fca5a5; color: #dc2626; }
.tag-blue  { background: #dbeafe; border-color: #93c5fd; color: #1d4ed8; }

/* ── Movie card ── */
.movie-card {
  background: #f9fafb;
  border: 1px solid #e5e7eb;
  border-radius: 6px;
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.movie-header {
  display: flex;
  gap: 1.25rem;
  align-items: flex-start;
}

.movie-poster {
  width: 80px;
  border-radius: 4px;
  object-fit: cover;
  flex-shrink: 0;
}

.movie-info {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.movie-title {
  font-family: 'Bebas Neue', sans-serif;
  font-size: 1.6rem;
  letter-spacing: 0.04em;
  color: #1a1a1a;
  margin: 0;
}

.movie-meta, .movie-genres {
  display: flex;
  flex-wrap: wrap;
  gap: 0.4rem;
}

/* ── Task card ── */
.task-card {
  background: #f9fafb;
  border: 1px solid #e5e7eb;
  border-radius: 6px;
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.task-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.task-id {
  font-family: 'JetBrains Mono', monospace;
  font-size: 0.75rem;
  color: #6b7280;
}

.task-status {
  font-family: 'JetBrains Mono', monospace;
  font-size: 0.75rem;
  padding: 0.2rem 0.65rem;
  border-radius: 2px;
  font-weight: 600;
}

.status-done    { background: #d1fae5; color: #047857; border: 1px solid #6ee7b7; }
.status-pending { background: #fef3c7; color: #92400e; border: 1px solid #fbbf24; }
.status-error   { background: #fee2e2; color: #dc2626; border: 1px solid #fca5a5; }

.list-label {
  font-size: 0.75rem;
  color: #4b5563;
  font-weight: 500;
  letter-spacing: 0.05em;
  text-transform: uppercase;
  margin: 0 0 0.5rem;
}

.artist-chips {
  display: flex;
  flex-wrap: wrap;
  gap: 0.4rem;
}

.artist-chip {
  font-size: 0.8rem;
  padding: 0.25rem 0.65rem;
  border-radius: 4px;
  background: #f3f4f6;
  border: 1px solid #e5e7eb;
  color: #1a1a1a;
  display: flex;
  align-items: center;
  gap: 0.4rem;
}

.chip-role {
  font-size: 0.65rem;
  color: #9ca3af;
  font-style: italic;
}

.movie-title-sm {
  font-family: 'Bebas Neue', sans-serif;
  font-size: 1.2rem;
  color: #1a1a1a;
  margin: 0;
}

.source-link {
  font-size: 0.8rem;
  color: #e879f9;
  text-decoration: none;
  align-self: flex-start;
}
.source-link:hover { text-decoration: underline; }

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