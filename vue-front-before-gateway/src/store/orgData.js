import { defineStore } from "pinia";
import { ref } from "vue";

export const orgDataStore = defineStore("orgData", () => {
  const treeData = ref([]);
  const currentNodeValue = ref(0);
  function setTreeData(data) {
    treeData.value = data;
  }
  function setCurrentNodeValue(value) {
    currentNodeValue.value = value;
  }
  return { treeData, setTreeData, currentNodeValue, setCurrentNodeValue };
});
