class Singleton {
  constructor() {
    if (Singleton.instance) {
      return Singleton.instance;
    }
    
    // 私有属性
    this._data = {};
    this._config = {};
    
    // 初始化操作
    this._initialize();
    
    Singleton.instance = this;
  }
  
  _initialize() {
  }
  
  // 公共方法
  setData(key, value) {
    localStorage.setItem(key, JSON.stringify(value))
    // this._data[key] = value;
  }
  
  getData(key) {
    return JSON.parse(localStorage.getItem(key))
    // return this._data[key];
  }
  
  getAllData() {
    return { ...this._data };
  }
  
  // 静态方法获取实例
  static getInstance() {
    if (!Singleton.instance) {
      Singleton.instance = new Singleton();
    }
    return Singleton.instance;
  }
  
  // 清空实例（用于测试）
  static clearInstance() {
    Singleton.instance = null;
  }
}
// 创建并导出单例实例
const appConfig = Singleton.getInstance()
// 导出类本身和实例
export { Singleton, appConfig }

// 默认导出实例
export default appConfig;