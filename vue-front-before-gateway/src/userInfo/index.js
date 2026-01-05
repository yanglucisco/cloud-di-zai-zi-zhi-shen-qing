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
export function setUserIdToken(idToken){
    // 
    localStorage.setItem(idTokenText, idToken)
}
export function getUserIdToken(){
    return localStorage.getItem(idTokenText)
}