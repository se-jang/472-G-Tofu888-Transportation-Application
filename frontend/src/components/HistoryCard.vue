<template>
    <div class="history-card">
        <div class="card-header">
            <h2 class="name">{{ props.name }}</h2>
        </div>
        <div class="card-body">
            <p class="phone">{{ props.phone }}</p>
            <p class="address">{{ props.address }}</p>
            <p class="address">
                Total amount: {{ parseFloat(props.amount.toFixed(2)) }}
            </p>
        </div>
        <div class="card-footer">
            <span class="status rejected">Completed</span>
        </div>
    </div>
</template>

<script setup lang="ts">
interface Props {
    name: string
    phone?: string
    address?: string
    amount: number
}

const props = defineProps<Props>()
</script>

<style scoped>
.history-card {
    border: 1px solid #e0e0e0;
    border-radius: 8px;
    padding: 16px;
    margin: 8px;
    background-color: #ffffff;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    min-height: 6rem;
}

.card-header {
    margin-bottom: 12px;
}

.name {
    font-size: 1.25rem;
    font-weight: bold;
    margin: 0;
}

.email {
    font-size: 0.875rem;
    color: #666666;
    margin: 4px 0 0;
}

.card-body {
    margin-bottom: 12px;
}

.phone,
.address {
    font-size: 0.875rem;
    color: #333333;
    margin: 4px 0;
}

.card-footer {
    text-align: right;
}

.status {
    font-size: 0.875rem;
    font-weight: bold;
    padding: 4px 8px;
    border-radius: 4px;
}

.rejected {
    color: #ffffff;
    background-color: #61be66;
}
</style>
