const accessTokenText = 'accessToken'
const idTokenText = 'idToken'
export function getUserInfo(){
    // 
    return {
        accessToken: sessionStorage.getItem(accessTokenText)
    }
}
export function setUserAccessToken(accessToken){
    // 
    sessionStorage.setItem(accessTokenText, accessToken)
}
export function cleanToken(){
    sessionStorage.removeItem(accessTokenText)
}
export function setUserIdToken(idToken){
    // 
    sessionStorage.setItem(idTokenText, idToken)
}
export function getUserIdToken(){
    return sessionStorage.getItem(idTokenText)
}