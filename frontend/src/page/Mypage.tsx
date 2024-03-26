import axios from "axios";
import {useNavigate} from "react-router-dom";

export const Mypage = () => {

    const navigate = useNavigate()
    const handleCheckAuth = async () => {
        try{
            const res = await axios.get("/api/auth/check-auth")
            console.log("res : ",res)
        }catch (err) {
            console.log("err : ",err)
            navigate("/login")
        }
    }

    return <>
    <div>Mypage</div>
        <br/>
        <br/>
        <button onClick={handleCheckAuth}>CheckAuth</button>
    </>
}