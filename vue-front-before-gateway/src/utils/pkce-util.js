import CryptoJS from 'crypto-js'

// let currentVerifier = ''
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
    //         FDUIak3h7E0eQEORelHuIA0wJjVUpbEuJH7xRJ60eOI
    // return '8SkwXEJUZJVQLScWYs8nV9bhv4GfvnHmc9iuApguEwY'
}
export async function generateCodeChallenge() {
  const f = getCurrentVerifier()
  console.log('f: ' + f)
  var r1 = CryptoJS.SHA256(f)
  const r = base64URL(r1)
  console.log('r: ' + r)
  return r// 'hQqHvGROSi0bvuXVAUXnSj1ZN1p1pDTpnKy5HZvvAso'
}
