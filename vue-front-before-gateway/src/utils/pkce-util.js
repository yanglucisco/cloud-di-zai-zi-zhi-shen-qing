import CryptoJS from 'crypto-js'
import { usePkceStore } from '@/store/pkce'

// const pkceStore = usePkceStore()

export function getCurrentVerifier() {
  const currentVerifier =  sessionStorage.getItem('currentVerifier')
  if (currentVerifier && currentVerifier.trim().length > 0) {
    return currentVerifier
  }
  const r = generateCodeVerifier()
  sessionStorage.setItem('currentVerifier', r)
  return r
}
export function base64URL(str) {
    return str
        .toString(CryptoJS.enc.Base64)
        .replace(/=/g, '')
        .replace(/\+/g, '-')
        .replace(/\//g, '_')
}
export function generateCodeVerifier() {
  let text = ''
  const possible = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789'
  for (let i = 0; i < 64; i++) {
    text += possible.charAt(Math.floor(Math.random() * possible.length))
  }
  return text
}
export async function generateCodeChallenge() {
  
  const f = getCurrentVerifier()
  console.log('f: ' + f)
  var r1 = CryptoJS.SHA256(f)
  const r = base64URL(r1)
  console.log('r: ' + r)
  return r
}
export function gotoLoginPage(codeChallenge){
  // 
  window.open('http://auth-server:20001/oauth2/authorize?response_type=code&client_id=pkce-client&scope=openid' 
                    + '&redirect_uri=http://vue-front-before-gateway.clouddizai.com:20005/home' 
                    + '&code_challenge_method=S256&code_challenge=' + codeChallenge, '_self')
}
