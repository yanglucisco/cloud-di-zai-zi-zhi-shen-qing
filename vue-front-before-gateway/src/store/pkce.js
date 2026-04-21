import { defineStore } from 'pinia'
import { ref } from 'vue'

export const usePkceStore = defineStore('pkce', () => {
    const currentVerifier = ref('')
    const setVer = () => {
        currentVerifier.value = new Date() 
    }
    return {
        currentVerifier,
        setVer
    }
//   getters: {
//     currentVerifier: (state) => state.currentVerifier,
//   },
//   actions: {
//     increment() {
//       this.currentVerifier = ''
//     },
//   },
})