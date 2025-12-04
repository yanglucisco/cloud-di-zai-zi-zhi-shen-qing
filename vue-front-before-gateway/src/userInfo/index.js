const idTokenText = 'idToken'
export function getUserInfo(){
    debugger
    return {
        idToken: sessionStorage.getItem(idTokenText)
    }
}
export function setUserIdToken(idToken){
    debugger
    sessionStorage.setItem(idTokenText, idToken)
}