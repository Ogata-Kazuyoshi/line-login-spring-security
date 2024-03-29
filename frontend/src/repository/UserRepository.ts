
import axios from "axios";
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
        const res = await axios.post("http://localhost:8080/logout")
        return res.data
    } catch (err) {
        throw Error(` error : ${err}`)
    }
}