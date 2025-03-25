<template>
    <nav class="header">
        <Header />
    </nav>
    <main class="container">
        <!-- Display HistoryCards for the current page -->
        <div
            v-if="data && data.length"
            v-for="item in paginatedCards"
            :key="item.name"
        >
            <HistoryCard
                :name="item.name"
                :phone="item.phone"
                :address="item.address"
                :amount="item.amount"
            />
        </div>

        <!-- Show a loading message or no data message -->
        <div v-else>
            <p v-if="loading">Loading...</p>
            <p v-else>No data available.</p>
        </div>

        <!-- Pagination Controls -->
        <div class="pagination">
            <button @click="prevPage" :disabled="currentPage === 1">
                Previous
            </button>
            <span>Page {{ currentPage }}/{{ totalPages }}</span>
            <button @click="nextPage" :disabled="currentPage === totalPages">
                Next
            </button>
        </div>
    </main>
</template>

<script setup lang="ts">
import { ref, computed, watch, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import Header from '@/components/Header.vue'
import HistoryCard from '@/components/HistoryCard.vue'
import axios from 'axios'

interface Props {
    name: string
    phone: string
    address: string
    amount: number
}

const data = ref<Props[]>([])
const loading = ref(false)

const getHistories = async () => {
    loading.value = true
    try {
        const res = await axios.get(`http://localhost:8080/payments`)
        data.value = res.data
        console.log(data.value)
    } catch (e) {
        console.error(e)
    } finally {
        loading.value = false
    }
}

onMounted(async () => {
    await getHistories()
})

const itemsPerPage = 5
const currentPage = ref(1)

const route = useRoute()
const router = useRouter()

// Calculate total pages, ensure data is loaded first
const totalPages = computed(() => {
    return data.value.length ? Math.ceil(data.value.length / itemsPerPage) : 1
})

// Read the `page` query parameter from the URL
const pageQuery = parseInt(route.query.page as string, 10)
if (!isNaN(pageQuery) && pageQuery >= 1 && pageQuery <= totalPages.value) {
    currentPage.value = pageQuery
}

const paginatedCards = computed(() => {
    const start = (currentPage.value - 1) * itemsPerPage
    const end = start + itemsPerPage
    return data.value.slice(start, end)
})

function nextPage() {
    if (currentPage.value < totalPages.value) {
        currentPage.value++
        updateUrl()
        scrollToTop()
    }
}

function prevPage() {
    if (currentPage.value > 1) {
        currentPage.value--
        updateUrl()
        scrollToTop()
    }
}

function updateUrl() {
    router.push({ query: { page: currentPage.value.toString() } })
}

function scrollToTop() {
    window.scrollTo({
        top: 0,
        behavior: 'smooth',
    })
}

// Watch for changes in the `page` query parameter
watch(
    () => route.query.page,
    (newPage) => {
        const page = parseInt(newPage as string, 10)
        if (!isNaN(page) && page >= 1 && page <= totalPages.value) {
            currentPage.value = page
        }
    }
)
</script>

<style scoped>
.container {
    z-index: 0;
    width: 70%;
    padding: 2rem;
    margin: 2rem auto;
    background-color: #e6e6e6;
    min-width: 80%;
    height: 100%;
    border-radius: 10px;
    position: relative;
    justify-content: center;
    padding-bottom: 5rem;
}

.header {
    z-index: 1;
    position: sticky;
    top: 0;
}

.pagination {
    z-index: 10;
    width: 80%;
    margin: 1rem auto;
    position: absolute;
    bottom: 0;
    display: flex;
    gap: 1rem;
    justify-content: center;
    left: 50%;
    transform: translateX(-50%);
}

.pagination button {
    padding: 0.5rem 1rem;
    border: none;
    background-color: #3b82f6;
    color: white;
    border-radius: 4px;
    cursor: pointer;
}

.pagination button:disabled {
    background-color: #cccccc;
    cursor: not-allowed;
}
</style>
