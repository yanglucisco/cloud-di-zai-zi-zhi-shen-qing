import { createI18n } from "vue-i18n";
import zhCN from "ant-design-vue/es/locale/zh_CN";
import enGB from "ant-design-vue/es/locale/en_GB";
import zh_cn from "./lang/zh-cn.js";
import en from "./lang/en.js";
// import tool from '@/utils/tool'
// import sysConfig from '@/config/index'

export const messages = {
  'zh-cn': {
    lang: zhCN,
    ...zh_cn,
  },
  en: {
    lang: enGB,
    ...en,
  },
};

const i18n = createI18n({
  legacy: false, // 使用 Composition API 模式
  locale: "zh-cn", // 默认语言 tool.data.get('APP_LANG') || sysConfig.LANG,
  fallbackLocale: "zh-cn",
  globalInjection: true,
  messages,
});

export default i18n;
