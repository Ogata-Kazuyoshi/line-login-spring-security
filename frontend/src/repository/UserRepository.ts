
import axios from "axios";
import {csrfManager} from "../csrf/CsrfManager.ts";
export const getUserInfoRepository = async () => {
    try {
        const res = await axios.get("/api/users")
        return res.data
    } catch (err) {
        throw Error(` error : ${err}`)
    }
}

export const updateUserInfoRepository = async (remark:string) => {
    try {
        await axios.post("/api/users",{remark})
    } catch (err) {
        throw Error(` error : ${err}`)
    }
}

export const logoutRepository = async () => {
    try {
        const res = await axios.get("http://localhost:8080/logout",{withCredentials:true})
        return res.data
    } catch (err) {
        throw Error(` error : ${err}`)
    }
}

type TestPost = {
    bodyParam1:string,
    bodyParam2:string
}

type ResponseTestPost = {
    message:string
}

export const testPostHandlerRepository = async (reqBody:TestPost):Promise<ResponseTestPost> => {
    try {
        const res = await axios.post("/api/users/test",reqBody,
            {
                headers:{
                    "X-CSRF-TOKEN": csrfManager.getCsrf()
                }
            })
        return res.data
    } catch (err){
        throw Error(`Error : ${err}`)
    }
}


type ResponseCsrf = {
    headerName:string,
    parameterName:string,
    token:string
}

export const csrfHandlerRepository = async ():Promise<ResponseCsrf> =>{
    try {
        const res = await axios.get("/api/csrf")
        return res.data
    } catch (err) {
        throw Error(`Error : ${err}`)
    }
}