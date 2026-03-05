import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import PaymentView from '../views/PaymentView.vue'
import ArtistView from '../views/ArtistView.vue'
import AuthView from '../views/AuthView.vue'
import ScrapingView from '../views/ScrapingView.vue'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        { path: '/', name: 'home', component: HomeView },
        { path: '/payment', name: 'payment', component: PaymentView },
        { path: '/artists', name: 'artists', component: ArtistView },
        { path: '/auth', name: 'auth', component: AuthView },
        { path: '/scraping', name: 'scraping', component: ScrapingView },
    ]
})

export default router