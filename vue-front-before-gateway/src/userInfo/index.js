const accessTokenText = 'accessToken'
const idTokenText = 'idToken'
export function getUserInfo(){
    // debugger
    return {
        accessToken: localStorage.getItem(accessTokenText)
    }
}
export function setUserAccessToken(accessToken){
    debugger
    localStorage.setItem(accessTokenText, accessToken)
}
export function cleanToken(){
    localStorage.removeItem(accessTokenText)
}
export function setUserIdToken(idToken){
    // debugger
    localStorage.setItem(idTokenText, idToken)
}
export function getUserIdToken(){
    return localStorage.getItem(idTokenText)
}