import { defineStore } from "pinia";
import { ref } from "vue";

export const orgDataStore = defineStore("orgData", () => {
  const treeData = ref([]);
  function setTreeData(data) {
    treeData.value = data;
  }
  return { treeData, setTreeData };
});
