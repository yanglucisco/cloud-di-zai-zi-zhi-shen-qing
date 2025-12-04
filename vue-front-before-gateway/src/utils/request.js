import axios from 'axios'
import { generateCodeChallenge, generateCodeVerifier } from './pkce-util'

// 创建axios实例，配置默认参数
const service = axios.create({
  baseURL: 'http://vue-front-before-gateway.clouddizai.com:20005/gateway/catalog', // import.meta.env.VITE_API_BASE_URL, // 从环境变量读取API基础地址[3](@ref)[4](@ref)
  timeout: 10000, // 请求超时时间
  headers: {
    'Content-Type': 'application/json;charset=UTF-8',
  },
})

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    // 在发送请求前做些什么：例如注入Token[1](@ref)[6](@ref)
    const token = sessionStorage.getItem('idToken')
    debugger
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    // 对请求错误做些什么
    return Promise.reject(error)
  },
)

// 响应拦截器
service.interceptors.response.use(
  (response) => {
    // 2xx 范围内的状态码都会触发该函数。
    // 可以对响应数据进行处理，例如直接返回后端统一的数据结构[6](@ref)
    return response.data
  },
  async (error) => {
    // 超出 2xx 范围的状态码都会触发该函数。
    // 统一处理错误，例如根据HTTP状态码提示用户[4](@ref)
    if (error.response) {
      switch (error.response.status) {
        case 401:
          debugger
          console.error('未授权，请重新登录')
          let codeChallenge = await generateCodeChallenge() //'hQqHvGROSi0bvuXVAUXnSj1ZN1p1pDTpnKy5HZvvAso' // await generateCodeChallenge()
          // window.open('/api/oauth2/authorization/certification-catalog-oidc', '_self')
          window.open('http://auth-server:20001/oauth2/authorize?response_type=code&client_id=pkce-client&scope=openid pkce' 
                    + '&redirect_uri=http://vue-front-before-gateway.clouddizai.com:20005/home' 
                    + '&code_challenge_method=S256&code_challenge=' + codeChallenge, '_self');
          //跳转到上面的网址，输入用户名和密码，授权服务器会返回到注册的回调地址，也就是下面的地址
          //http://vue-front-before-gateway.clouddizai.com:20005/home?
          // code=iBOnqwJZ7rvWXoX3Ba6N764IVlREs4DHrrmqYH4eWgstgfc8HBWkuaj8hCNxtzy5_BiumZpFipcPvGoQeSUcbrwtnwQwPXvrhlCT5ckMvagW6-0ZGaiewy5siOmGMIO2
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
