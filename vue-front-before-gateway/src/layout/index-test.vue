<template>
    <div class="container">
        <!-- 左列 -->
        <div class="left-column">
            <div class="left-top">
                左列 - 上部 (固定高度内容区)
            </div>
            <div class="left-bottom">
                <div class="scroll-content">
                    <div><span>测试</span></div>
                    <div><span>测试</span></div>
                    <div><span>测试</span></div>
                    <div><span>测试</span></div>
                    <div><span>测试</span></div>
                    <div><span>测试</span></div>
                    <div><span>测试</span></div>
                    <div><span>测试</span></div>
                    <div><span>测试</span></div>
                    <div><span>测试</span></div>
                    <div><span>测试</span></div>
                    <div><span>测试</span></div>
                    <div><span>测试</span></div>
                    <div><span>测试</span></div>
                    <div><span>测试</span></div>
                    <div><span>测试</span></div>
                    <div><span>测试</span></div>
                    <div><span>测试</span></div>
                    <div><span>测试</span></div>
                    <div><span>测试</span></div>
                    <div><span>测试</span></div>
                    <div><span>测试</span></div>
                    <div><span>测试</span></div>
                    <div><span>测试</span></div>
                    <div><span>测试</span></div>
                    <div><span>测试</span></div>
                    <div><span>测试</span></div>
                    <div><span>测试</span></div>
                    <div><span>测试</span></div>
                    <div><span>测试</span></div>
                    <div><span>测试</span></div>
                    <div><span>测试</span></div>
                    <div><span>测试</span></div>
                    <div><span>测试</span></div>
                    <div><span>测试</span></div>
                    <div><span>测试</span></div>
                    <div><span>测试</span></div>
                    <div><span>测试</span></div>
                    <div><span>测试</span></div>
                    <div><span>测试</span></div>
                    <div><span>测试</span></div>
                    <div><span>测试</span></div>
                    <div><span>测试</span></div>
                    <div><span>测试</span></div>
                </div>
            </div>
        </div>
        <!-- 右列 -->
        <div class="right-column">
            右列内容
        </div>
    </div>
</template>
<style scoped>
/* 基本样式重置，确保布局准确 */
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}

body {
    height: 100vh;
    /* 让布局充满整个视口高度 */
}

/* 主容器：左右两列布局 */
.container {
    display: flex;
    height: 100vh;
    /* 充满整个视口高度 */
}

/* 左列：设置为垂直方向的Flex容器 */
.left-column {
    display: flex;
    flex-direction: column;
    width: 30%;
    /* 左列宽度，可调整 */
    border-right: 2px solid #ccc;
    /* 可选：分隔线 */
}

/* 左列上部 - 固定高度 */
.left-top {
    height: 200px;
    /* 设定一个固定高度 */
    background-color: #f0f0f0;
    /* 可选：背景色 */
    padding: 15px;
    /* 防止内容过多时意外撑开或产生滚动 */
    overflow: hidden;
}

/* 左列下部 - 可滚动区域 (关键！) */
.left-bottom {
    flex: 1;
    /* 占据左列剩余的所有垂直空间 */
    overflow-y: auto;
    /* 垂直方向内容溢出时显示滚动条 */
    background-color: #fafafa;
    /* 可选：背景色 */
}

/* 可滚动区域的内容块 */
.scroll-content {
    padding: 15px;
    min-height: 100%;
    /* 确保内容区域至少充满其容器 */
}

/* 右列 - 自适应宽度 */
.right-column {
    flex: 1;
    /* 占据容器剩余宽度 */
    padding: 20px;
    background-color: #e9e9e9;
    /* 可选：背景色 */
}
</style>