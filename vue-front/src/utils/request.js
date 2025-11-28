import axios from 'axios'

// 创建axios实例，配置默认参数
const service = axios.create({
  baseURL: 'http://127.0.0.1:20003/catalog', // import.meta.env.VITE_API_BASE_URL, // 从环境变量读取API基础地址[3](@ref)[4](@ref)
  timeout: 10000, // 请求超时时间
  headers: {
    'Content-Type': 'application/json;charset=UTF-8',
  },
})

// 请求拦截器
// service.interceptors.request.use(
//   (config) => {
//     // 在发送请求前做些什么：例如注入Token[1](@ref)[6](@ref)
//     const token = localStorage.getItem('auth_token')
//     if (token) {
//       config.headers.Authorization = `Bearer ${token}`
//     }
//     return config
//   },
//   (error) => {
//     // 对请求错误做些什么
//     return Promise.reject(error)
//   },
// )

// 响应拦截器
service.interceptors.response.use(
  (response) => {
    // 2xx 范围内的状态码都会触发该函数。
    // 可以对响应数据进行处理，例如直接返回后端统一的数据结构[6](@ref)
    return response.data
  },
  (error) => {
    // 超出 2xx 范围的状态码都会触发该函数。
    // 统一处理错误，例如根据HTTP状态码提示用户[4](@ref)
    if (error.response) {
      switch (error.response.status) {
        case 401:
          console.error('未授权，请重新登录')
          window.open('/oauth2/authorization/certification-catalog-oidc', '_self')
          // 可在此处跳转到登录页[4](@ref)[5](@ref)
          break
        case 403:
          console.error('拒绝访问')
          break
        case 500:
          console.error('服务器内部错误')
          break
        default:
          console.error('请求失败', error.message)
      }
    } else {
      console.error('网络错误，请检查连接')
    }
    return Promise.reject(error)
  },
)

export default service
