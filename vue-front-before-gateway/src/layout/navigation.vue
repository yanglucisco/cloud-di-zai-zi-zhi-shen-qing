<template>
    <div>
        <a-tabs v-model:activeKey="activeKey" :tab-position="mode" @tabScroll="callback" type="editable-card"
            @edit="onEdit" class="snowy-admin-tabs" @change="handleTabChange" hide-add>
            <a-tab-pane v-for="pane in panes" :key="pane.key" :tab="pane.title" :closable="pane.closable"></a-tab-pane>
        </a-tabs>
    </div>
</template>
<script setup>
import { ref, watch } from 'vue';
import { useRouter } from 'vue-router';

// 存储键名常量
const STORAGE_KEY = 'snowy-admin-tabs';
const STORAGE_KEY_ACTIVE = 'snowy-admin-active-key';

const mode = ref('top');
const router = useRouter();
const emit = defineEmits(['removeItem']);

// 从 localStorage 恢复数据，如果没有则使用默认值
function loadFromStorage() {
    try {
        const savedPanes = localStorage.getItem(STORAGE_KEY);
        const savedActiveKey = localStorage.getItem(STORAGE_KEY_ACTIVE);
        return {
            panes: savedPanes ? JSON.parse(savedPanes) : [
                {
                    title: '首页',
                    content: '首页',
                    key: '/index',
                    closable: false,
                }
            ],
            activeKey: savedActiveKey || '/index'
        };
    } catch (e) {
        console.warn('Failed to load tabs from storage:', e);
        return {
            panes: [
                {
                    title: '首页',
                    content: '首页',
                    key: '/index',
                    closable: false,
                }
            ],
            activeKey: '/index'
        };
    }
}

// 保存到 localStorage
function saveToStorage() {
    try {
        localStorage.setItem(STORAGE_KEY, JSON.stringify(panes.value));
        localStorage.setItem(STORAGE_KEY_ACTIVE, activeKey.value);
    } catch (e) {
        console.warn('Failed to save tabs to storage:', e);
    }
}

const initializedData = loadFromStorage();
const panes = ref(initializedData.panes);
const activeKey = ref(initializedData.activeKey);

// 监听数据变化，自动保存到 localStorage
watch([panes, activeKey], () => {
    saveToStorage();
}, { deep: true });

const testFunc = () => {
    addPaneItem('New Tab', 'New Content', 'new-tab');
};

/**
 * Method to add a new pane item, callable by parent component
 * @param {string} tile - The title of the pane
 * @param {string} content - The content of the pane
 * @param {string} key - The key/route of the pane
 */
const addPaneItem = (tile, content, key) => {
    const panelItem = {
        title: tile,
        content: content,
        key: key,
        closable: true,
    };
    if (!panelItem || !panelItem.key) {
        console.warn('Invalid panel item: key is required');
        return;
    }

    // Check if pane with same key already exists to avoid duplicates
    const exists = panes.value.some((pane) => pane.key === panelItem.key);
    if (exists) {
        console.warn(`Pane with key ${panelItem.key} already exists`);
        // Optional: Switch to the existing tab instead of adding
        activeKey.value = panelItem.key;
        return;
    }

    // Add the new pane
    panes.value.push(panelItem);

    // Optionally switch to the newly added tab
    activeKey.value = panelItem.key;
};

// 监听切换事件，key 为当前点击的 tab-pane 的 key
const handleTabChange = (key) => {
  console.log('当前切换到了标签页:', key)
  // 在这里可以执行你需要的业务逻辑，比如请求对应标签页的数据
  router.push(key);
}

const callback = val => {
    console.log(val);
};

// Handle tab closing/adding
const onEdit = (targetKey, action) => {
    if (action === 'remove') {
        removeTab(targetKey);
    }
};

const removeTab = (targetKey) => {
    // Find the index of the tab to be removed
    const index = panes.value.findIndex((pane) => pane.key === targetKey);
    if (index !== -1) {
        // Remove the tab from the array
        panes.value.splice(index, 1);
        let newActiveKey = '';
        // If the removed tab was the active one, switch to another tab
        if (activeKey.value === targetKey) {
            // Switch to the previous tab, or the next one if it was the first
            newActiveKey = panes.value.length > 0
                ? panes.value[Math.max(0, index - 1)].key
                : ''; // Or handle empty state

            activeKey.value = newActiveKey;
        } else {
            // If the removed tab was NOT active, newActiveKey remains the current activeKey
            // Or you might want to pass null/undefined to indicate no change needed by parent
            newActiveKey = activeKey.value;
        }
        router.push(newActiveKey);
        emit('removeItem', newActiveKey);
    }
};

// Expose the method to the parent component
defineExpose({
    addPaneItem
});
</script>
<style lang="less">
.snowy-admin-tabs {
    &.ant-tabs {
        background: var(--component-background);
        box-shadow: var(--header-light-shadow);
        z-index: 99;

        .ant-tabs-nav {
            margin-bottom: 0;

            .ant-tabs-extra-content {
                display: flex;
            }

            .ant-tabs-nav-wrap {
                .ant-tabs-ink-bar {
                    visibility: visible;
                }

                .ant-tabs-tab-with-remove {
                    padding-right: 4px;
                }

                .ant-tabs-tab {
                    background: none;
                    height: 40px;
                    line-height: 40px;
                    transition:
                        background-color 0.3s,
                        color 0.3s;
                    padding: 0 16px;
                    border-radius: 0;
                    border: none;
                    margin: 0;

                    .ant-tabs-tab-remove {
                        margin: 0;
                        padding: 0 5px;
                    }
                }

                .ant-tabs-tab-active {
                    background: var(--primary-1);
                }
            }
        }

        .snowy-admin-tabs-drop,
        .snowy-admin-tabs-arrow,
        .ant-tabs-nav-operations .ant-tabs-nav-more {
            padding: 0;
            width: 40px;
            height: 40px;
            line-height: 40px;
            text-align: center;
            cursor: pointer;

            .anticon {
                font-size: 12px;
                vertical-align: -1px;
            }
        }
    }
}
</style>