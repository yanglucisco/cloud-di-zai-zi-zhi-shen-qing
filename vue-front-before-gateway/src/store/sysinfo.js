import { defineStore } from "pinia";
import { ref } from "vue";

export const sysinfoStore = defineStore('sysinfo', () => {
    const menuList = ref(
        [
        ]
    )
    function setMenuList(menuList) {
        menuList.value = menuList
    }
    return { menuList, setMenuList }
})