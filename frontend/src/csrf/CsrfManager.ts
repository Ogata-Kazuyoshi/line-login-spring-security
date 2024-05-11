class CsrfManager {
    private csrf = ""

    setCsrf(token:string){
        this.csrf  =token
    }
    getCsrf(){
        return this.csrf
    }
}

export const csrfManager = new CsrfManager()