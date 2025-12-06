const accessTokenText = 'accessToken'
const idTokenText = 'idToken'
export function getUserInfo(){
    // debugger
    return {
        accessToken: sessionStorage.getItem(accessTokenText)
    }
}
export function setUserAccessToken(accessToken){
    // debugger
    sessionStorage.setItem(accessTokenText, accessToken)
}
export function cleanToken(){
    sessionStorage.removeItem(accessTokenText)
}
export function setUserIdToken(idToken){
    // debugger
    sessionStorage.setItem(idTokenText, idToken)
}
export function getUserIdToken(){
    return sessionStorage.getItem(idTokenText)
}