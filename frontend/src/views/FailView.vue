<template>
    <Header />
    <div class="container">
        <main class="main-content">
            <div class="order-details">
                <h1 class="title">Payment Failed</h1>
                <div>
                    <img
                        src="@/assets/cancel_icon.png"
                        alt="Cancel Icon"
                        class="icon"
                    />
                </div>
                <p class="description">
                    Unfortunately, your payment could not be processed. Please
                    try again!
                </p>
                <router-link to="/cart" class="retry-button">
                    Try again!
                </router-link>
            </div>
        </main>
    </div>
</template>

<script setup>
import Header from '@/components/Header.vue'
</script>

<style scoped>
.container {
    display: flex;
}

.main-content {
    width: 100%;
    padding: 2rem;
    display: flex;
    flex-direction: column;
    gap: 1.5rem;
    background-color: #f7fafc;
    height: 100%;
    min-height: calc(100vh - 62px);
    align-items: center;
    justify-content: center;
}

.order-details {
    display: flex;
    flex-direction: column;
    align-items: center;
    background-color: #fff;
    padding: 2rem;
    border-radius: 0.5rem;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    gap: 1.5rem;
}

.title {
    color: #f56565;
    font-size: 2rem;
    font-weight: bold;
}

.icon {
    width: 5rem;
    height: 5rem;
}

.description {
    font-size: 1.125rem;
    color: #4a5568;
    text-align: center;
}

.retry-button {
    margin-top: 1rem;
    background-color: #f56565;
    color: #fff;
    padding: 0.5rem 1rem;
    border-radius: 0.375rem;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    transition: background-color 0.2s ease;
}

.retry-button:hover {
    background-color: #e53e3e;
}
</style>
