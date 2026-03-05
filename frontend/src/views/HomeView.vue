<template>
  <div class="home">
    <!-- Cinematic background -->
    <div class="bg-layer">
      <div class="film-grain"></div>
      <div class="spotlight spotlight-1"></div>
      <div class="spotlight spotlight-2"></div>
      <div class="scanlines"></div>
    </div>

    <!-- Header -->
    <header class="hero">
      <div class="logo-block">
        <span class="logo-icon">▶</span>
        <span class="logo-text">VÖDEX</span>
      </div>
      <div class="tagline">Plateforme de gestion VOD</div>
      <h1 class="headline">
        <span class="line-1">Vos services.</span>
        <span class="line-2">En un seul endroit.</span>
      </h1>
      <p class="subline">Accédez à l'ensemble des APIs du système de réservation vidéo à la demande.</p>
      <div class="status-bar">
        <span class="dot"></span> Système opérationnel — 4 services actifs
      </div>
    </header>

    <!-- API Cards Grid -->
    <section class="api-grid">

      <router-link to="/artists" class="api-card card-artist">
        <div class="card-number">01</div>
        <div class="card-icon">🎭</div>
        <div class="card-body">
          <h2>Artiste</h2>
          <p>Gérez les profils d'artistes, filmographies, et métadonnées associées au catalogue VOD.</p>
        </div>
        <div class="card-tag">GET · POST · PUT</div>
        <div class="card-glow"></div>
      </router-link>

      <router-link to="/auth" class="api-card card-auth">
        <div class="card-number">02</div>
        <div class="card-icon">🔐</div>
        <div class="card-body">
          <h2>Authentification</h2>
          <p>Inscription, connexion et gestion des sessions utilisateurs via JWT sécurisé.</p>
        </div>
        <div class="card-tag">OAuth · JWT</div>
        <div class="card-glow"></div>
      </router-link>

      <router-link to="/payment" class="api-card card-payment">
        <div class="card-number">03</div>
        <div class="card-icon">💳</div>
        <div class="card-body">
          <h2>Paiement</h2>
          <p>Traitez les transactions, abonnements et remboursements via les passerelles intégrées.</p>
        </div>
        <div class="card-tag">Stripe · Webhook</div>
        <div class="card-glow"></div>
      </router-link>

      <router-link to="/scraping" class="api-card card-scraping">
        <div class="card-number">04</div>
        <div class="card-icon">🕷️</div>
        <div class="card-body">
          <h2>Scrapping</h2>
          <p>Collectez et enrichissez les données de contenus depuis des sources externes automatiquement.</p>
        </div>
        <div class="card-tag">CRON · Parser</div>
        <div class="card-glow"></div>
      </router-link>

    </section>

    <!-- Footer strip -->
    <footer class="footer-strip">
      <span>VÖDEX © 2025</span>
      <span class="sep">·</span>
      <span>Système de réservation VOD</span>
      <span class="sep">·</span>
      <span>API v2.0</span>
    </footer>
  </div>
</template>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Bebas+Neue&family=DM+Sans:wght@300;400;500&family=JetBrains+Mono:wght@400;700&display=swap');

/* ── Base ── */
.home {
  min-height: 100vh;
  background: #080a0f;
  color: #e8e0d0;
  font-family: 'DM Sans', sans-serif;
  position: relative;
  overflow-x: hidden;
}

/* ── Cinematic BG ── */
.bg-layer {
  position: fixed;
  inset: 0;
  pointer-events: none;
  z-index: 0;
}

.film-grain {
  position: absolute;
  inset: 0;
  background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 200 200' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='n'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.9' numOctaves='4' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23n)' opacity='0.04'/%3E%3C/svg%3E");
  opacity: 0.35;
  animation: grain 0.4s steps(1) infinite;
}

@keyframes grain {
  0%  { transform: translate(0, 0); }
  25% { transform: translate(-3px, 2px); }
  50% { transform: translate(2px, -3px); }
  75% { transform: translate(-2px, -1px); }
}

.spotlight-1 {
  position: absolute;
  top: -20%;
  left: -10%;
  width: 60vw;
  height: 60vw;
  background: radial-gradient(circle, rgba(255,180,50,0.06) 0%, transparent 70%);
  animation: drift1 12s ease-in-out infinite alternate;
}

.spotlight-2 {
  position: absolute;
  bottom: -10%;
  right: -10%;
  width: 50vw;
  height: 50vw;
  background: radial-gradient(circle, rgba(200,100,255,0.05) 0%, transparent 70%);
  animation: drift2 15s ease-in-out infinite alternate;
}

@keyframes drift1 {
  from { transform: translate(0, 0) scale(1); }
  to   { transform: translate(8vw, 5vh) scale(1.15); }
}
@keyframes drift2 {
  from { transform: translate(0, 0) scale(1); }
  to   { transform: translate(-6vw, -4vh) scale(1.1); }
}

.scanlines {
  position: absolute;
  inset: 0;
  background: repeating-linear-gradient(
    0deg,
    transparent,
    transparent 2px,
    rgba(0,0,0,0.07) 2px,
    rgba(0,0,0,0.07) 4px
  );
}

/* ── Hero ── */
.hero {
  position: relative;
  z-index: 1;
  max-width: 900px;
  margin: 0 auto;
  padding: 6rem 2rem 3rem;
  text-align: center;
}

.logo-block {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  background: rgba(255,180,50,0.08);
  border: 1px solid rgba(255,180,50,0.25);
  padding: 0.35rem 1rem;
  border-radius: 2px;
  margin-bottom: 2rem;
  animation: fadeUp 0.6s ease both;
}

.logo-icon {
  color: #ffb432;
  font-size: 0.9rem;
}

.logo-text {
  font-family: 'Bebas Neue', sans-serif;
  font-size: 1.2rem;
  letter-spacing: 0.3em;
  color: #ffb432;
}

.tagline {
  font-family: 'JetBrains Mono', monospace;
  font-size: 0.7rem;
  letter-spacing: 0.2em;
  text-transform: uppercase;
  color: rgba(232,224,208,0.4);
  margin-bottom: 1.5rem;
  animation: fadeUp 0.6s 0.1s ease both;
}

.headline {
  font-family: 'Bebas Neue', sans-serif;
  font-size: clamp(3.5rem, 9vw, 7rem);
  line-height: 0.95;
  letter-spacing: 0.02em;
  margin: 0 0 1.5rem;
}

.line-1 {
  display: block;
  color: #e8e0d0;
  animation: fadeUp 0.7s 0.2s ease both;
}

.line-2 {
  display: block;
  color: #ffb432;
  animation: fadeUp 0.7s 0.35s ease both;
}

.subline {
  font-size: 1rem;
  color: rgba(232,224,208,0.55);
  font-weight: 300;
  max-width: 480px;
  margin: 0 auto 2rem;
  line-height: 1.7;
  animation: fadeUp 0.7s 0.45s ease both;
}

.status-bar {
  display: inline-flex;
  align-items: center;
  gap: 0.6rem;
  font-family: 'JetBrains Mono', monospace;
  font-size: 0.72rem;
  color: rgba(232,224,208,0.45);
  border-top: 1px solid rgba(232,224,208,0.1);
  padding-top: 1.5rem;
  animation: fadeUp 0.7s 0.55s ease both;
}

.dot {
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background: #4ade80;
  box-shadow: 0 0 8px #4ade80;
  animation: pulse 2s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 1; transform: scale(1); }
  50%       { opacity: 0.5; transform: scale(0.85); }
}

/* ── API Grid ── */
.api-grid {
  position: relative;
  z-index: 1;
  max-width: 1000px;
  margin: 0 auto;
  padding: 2rem 2rem 5rem;
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 1.5rem;
}

@media (max-width: 640px) {
  .api-grid { grid-template-columns: 1fr; }
}

/* ── Cards ── */
.api-card {
  position: relative;
  display: flex;
  flex-direction: column;
  gap: 1rem;
  padding: 2rem;
  border: 1px solid rgba(232,224,208,0.08);
  border-radius: 4px;
  background: rgba(232,224,208,0.025);
  text-decoration: none;
  color: inherit;
  overflow: hidden;
  transition: transform 0.35s cubic-bezier(0.22,1,0.36,1), border-color 0.3s, background 0.3s;
  animation: fadeUp 0.7s ease both;
  backdrop-filter: blur(6px);
}

.api-card:nth-child(1) { animation-delay: 0.65s; }
.api-card:nth-child(2) { animation-delay: 0.75s; }
.api-card:nth-child(3) { animation-delay: 0.85s; }
.api-card:nth-child(4) { animation-delay: 0.95s; }

.api-card:hover {
  transform: translateY(-6px);
  background: rgba(232,224,208,0.05);
}

.api-card:hover .card-glow {
  opacity: 1;
}

.card-glow {
  position: absolute;
  inset: 0;
  opacity: 0;
  transition: opacity 0.4s;
  pointer-events: none;
  border-radius: 4px;
}

/* per-card accent colors */
.card-artist   { --accent: #ffb432; }
.card-auth     { --accent: #60a5fa; }
.card-payment  { --accent: #4ade80; }
.card-scraping { --accent: #e879f9; }

.card-artist:hover   { border-color: rgba(255,180,50,0.35); }
.card-auth:hover     { border-color: rgba(96,165,250,0.35); }
.card-payment:hover  { border-color: rgba(74,222,128,0.35); }
.card-scraping:hover { border-color: rgba(232,121,249,0.35); }

.card-artist   .card-glow { background: radial-gradient(circle at 30% 0%, rgba(255,180,50,0.08), transparent 60%); }
.card-auth     .card-glow { background: radial-gradient(circle at 30% 0%, rgba(96,165,250,0.08), transparent 60%); }
.card-payment  .card-glow { background: radial-gradient(circle at 30% 0%, rgba(74,222,128,0.08), transparent 60%); }
.card-scraping .card-glow { background: radial-gradient(circle at 30% 0%, rgba(232,121,249,0.08), transparent 60%); }

.card-number {
  font-family: 'Bebas Neue', sans-serif;
  font-size: 0.85rem;
  letter-spacing: 0.15em;
  color: var(--accent);
  opacity: 0.6;
}

.card-icon {
  font-size: 2rem;
  line-height: 1;
}

.card-body h2 {
  font-family: 'Bebas Neue', sans-serif;
  font-size: 1.8rem;
  letter-spacing: 0.04em;
  color: #e8e0d0;
  margin: 0 0 0.4rem;
}

.card-body p {
  font-size: 0.875rem;
  font-weight: 300;
  color: rgba(232,224,208,0.5);
  line-height: 1.65;
  margin: 0;
}

.card-tag {
  font-family: 'JetBrains Mono', monospace;
  font-size: 0.65rem;
  letter-spacing: 0.12em;
  color: var(--accent);
  opacity: 0.7;
  border-top: 1px solid rgba(232,224,208,0.06);
  padding-top: 0.75rem;
  margin-top: auto;
}

/* ── Footer ── */
.footer-strip {
  position: relative;
  z-index: 1;
  text-align: center;
  padding: 1.5rem;
  font-family: 'JetBrains Mono', monospace;
  font-size: 0.65rem;
  letter-spacing: 0.15em;
  color: rgba(232,224,208,0.2);
  border-top: 1px solid rgba(232,224,208,0.06);
}

.sep { margin: 0 0.75rem; }

/* ── Animation ── */
@keyframes fadeUp {
  from { opacity: 0; transform: translateY(24px); }
  to   { opacity: 1; transform: translateY(0); }
}
</style>
