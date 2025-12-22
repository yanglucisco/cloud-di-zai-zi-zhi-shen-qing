import { defineStore } from "pinia";
import { ref } from "vue";

export const useGlobalStore = defineStore('global', () => {
    const menuIsCollapse = ref(false)
    // 主题
	const theme = ref('dark')
    function setMenuIsCollapse(value){
        menuIsCollapse.value = value
    }
    return { menuIsCollapse, setMenuIsCollapse, theme }
})