// utils/logger.ts - 推荐这个方案
import { ref } from 'vue';

export class Logger {
  private static instance: Logger;
  private logs = ref<any[]>([]);
  private isDev = import.meta.env.DEV;
  private isProd = import.meta.env.PROD;
  
  private constructor() {}
  
  static getInstance(): Logger {
    if (!Logger.instance) {
      Logger.instance = new Logger();
    }
    return Logger.instance;
  }
  
  // 使用 console 的不同方法，带有颜色标记
  private logWithColor(level: 'log' | 'info' | 'warn' | 'error' | 'debug', 
                      message: string, 
                      data?: any,
                      component?: string) {
    if (this.isProd && level === 'debug') return;
    
    const timestamp = new Date().toLocaleTimeString();
    const prefix = `[${timestamp}] ${component ? `[${component}]` : ''}`;
    
    const styles = {
      log: 'color: #666',
      info: 'color: #2196F3',
      warn: 'color: #FF9800',
      error: 'color: #f44336; font-weight: bold',
      debug: 'color: #9C27B0'
    };
    
    const style = styles[level];
    const consoleMethod = level === 'debug' ? console.log : console[level];
    
    consoleMethod(`%c${prefix} ${message}`, style, data || '');
  }
  
  debug(message: string, data?: any, component?: string) {
    this.logWithColor('debug', message, data, component);
  }
  
  info(message: string, data?: any, component?: string) {
    this.logWithColor('info', message, data, component);
  }
  
  warn(message: string, data?: any, component?: string) {
    this.logWithColor('warn', message, data, component);
  }
  
  error(message: string, error?: any, component?: string) {
    this.logWithColor('error', message, error, component);
    
    // 生产环境错误上报
    if (this.isProd && window.Sentry && error) {
      window.Sentry.captureException(error instanceof Error ? error : new Error(message));
    }
  }
  
  // 创建组件专用日志器
  createComponentLogger(componentName: string) {
    return {
      debug: (msg: string, data?: any) => this.debug(msg, data, componentName),
      info: (msg: string, data?: any) => this.info(msg, data, componentName),
      warn: (msg: string, data?: any) => this.warn(msg, data, componentName),
      error: (msg: string, err?: any) => this.error(msg, err, componentName)
    };
  }
}

// 单例导出
export const logger = Logger.getInstance();

// Composition API hook
export function useLogger(componentName?: string) {
  const componentLogger = logger.createComponentLogger(componentName || 'Unknown');
  
  return {
    ...componentLogger,
    // 添加响应式日志记录
    logs: logger.logs
  };
}