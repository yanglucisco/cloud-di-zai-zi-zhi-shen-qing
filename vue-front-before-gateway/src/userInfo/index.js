const idTokenText = 'accessToken'
export function getUserInfo(){
    // debugger
    return {
        idToken: sessionStorage.getItem(idTokenText)
    }
}
export function setUserIdToken(idToken){
    // debugger
    sessionStorage.setItem(idTokenText, idToken)
}
export function cleanToken(){
    sessionStorage.removeItem(idTokenText)
}