<template>
    <div class="not-found-container">
        <a-result
            status="404"
            :title="$t('notFound.title')"
            :sub-title="$t('notFound.subTitle')"
        >
            <template #extra>
                <a-button type="primary" @click="goHome">
                    {{ $t('notFound.backHome') }}
                </a-button>
            </template>
        </a-result>
    </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const goHome = () => {
    router.push('/home')
}
</script>

<style scoped>
.not-found-container {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 100%;
}
</style>
