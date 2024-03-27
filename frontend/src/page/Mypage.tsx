import axios from "axios";
import {useNavigate} from "react-router-dom";

export const Mypage = () => {

    const navigate = useNavigate()
    const handleCheckAuth = async () => {
        try{
            const res = await axios.get("/api/auth/check-auth")
            const url = new URL(res.request.responseURL);
            if (url.pathname === "/login") navigate("/login")
        }catch (err) {
            throw Error(`error : ${err}`)
        }
    }

    return <>
    <div>Mypage</div>
        <br/>
        <br/>
        <button onClick={handleCheckAuth}>CheckAuth</button>
    </>
}