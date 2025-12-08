import { defineStore } from 'pinia'
import { ref } from 'vue'

export const usePkceStore = defineStore('pkce', () => {
    const currentVerifier = ref('')
    const setVer = () => {
        currentVerifier.value = new Date() 
        console.log('通过 pina 设置currentVerifier 成功')
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