import EnvUtil from '@/utils/EnvUtil';
import request from "@/utils/request";

const accessTokenText = 'accessToken'
const idTokenText = 'idToken'
export function getUserInfo(){
    // 
     
    return {
        
        accessToken: localStorage.getItem(accessTokenText)
    }
}
export function setUserAccessToken(accessToken){
    // 
    localStorage.setItem(accessTokenText, accessToken)
}
export function cleanToken(){
    
    localStorage.removeItem(accessTokenText)
}
export function logout(){
    debugger
    const idToken = getUserIdToken()
    const href = `${EnvUtil.AUTH_SERVER_URL}/connect/logout?id_token_hint=${idToken}&post_logout_redirect_uri=http://vue-front-before-gateway.clouddizai.com:${EnvUtil.apiPort}`
    localStorage.removeItem(accessTokenText)
    window.location.href = href
}
export function getCurrentUser(){
    return request.get('/account/sysUser/getCurrentUser');
}
export function setUserIdToken(idToken){
    // 
     
    localStorage.setItem(idTokenText, idToken)
}
export function getUserIdToken(){
    return localStorage.getItem(idTokenText)
}