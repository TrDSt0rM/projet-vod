<template>
  <div class="home">
    <h1>Projet VOD - Système de réservation</h1>
    <p>Bienvenue dans l'application de gestion de VOD</p>

    <div class="links">
      <router-link to="/payment" class="card">
        <h2>💳 Paiement</h2>
        <p>Tester l'API de paiement</p>
      </router-link>
    </div>
  </div>
</template>

<style scoped>
.home {
  max-width: 800px;
  margin: 0 auto;
  padding: 2rem;
}

.links {
  display: grid;
  gap: 1rem;
  margin-top: 2rem;
}

.card {
  padding: 1.5rem;
  border: 1px solid #ddd;
  border-radius: 8px;
  text-decoration: none;
  color: inherit;
  transition: all 0.3s;
}

.card:hover {
  border-color: #42b983;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

h1 {
  color: #42b983;
}
</style>