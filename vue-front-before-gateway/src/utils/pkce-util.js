import CryptoJS from 'crypto-js'

export function getCurrentVerifier() {
  const currentVerifier = sessionStorage.getItem('currentVerifier')
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
