import axios from "axios";
import {useNavigate} from "react-router-dom";
import {getUserInfoRepository, updateUserInfoRepository} from "../repository/UserRepository.ts";
import {useEffect, useState} from "react";

type UserInfo = {
    id : string,
    oid : string,
    name : string,
    remark : string
}

export const Mypage = () => {
    const [user, setUser] = useState<UserInfo | null>(null)
    const [remark, setRemark] = useState("")

    const navigate = useNavigate()


    const updateInfo = async () => {
        try{
            await updateUserInfoRepository(remark)
            setRemark("")
            const res = await getUserInfoRepository()
            setUser(res)
        } catch (err) {
            throw Error(`error : ${err}`)
        }
    }

    useEffect(() => {
        const handleCheckAuth = async () => {
            try{
                const res = await axios.get("/api/auth/check-auth")
                const url = new URL(res.request.responseURL);
                if (url.pathname === "/login") navigate("/front-login")
            }catch (err) {
                throw Error(`error : ${err}`)
            }
        }
        handleCheckAuth()
    }, []);



    useEffect(() => {
        const getUserInfo = async () => {
            const res = await getUserInfoRepository()
            setUser(res)
        }
        getUserInfo()
    }, []);

    return <>
    <div>Mypage</div>
        <br/>
        <br/>
        <ul>
            <li>{` id : ${user?.id}`}</li>
            <li>{` oid : ${user?.oid}`}</li>
            <li>{` name : ${user?.name}`}</li>
            <li>{` remark : ${user?.remark}`}</li>
        </ul>
        <br/>
        <br/>
        <div>
            <label>remarkの編集</label>
            <input type="text" value={remark} onChange={(e)=> {setRemark(e.target.value)}}/>
            <br/>
            <button onClick={updateInfo}>投稿</button>
        </div>
    </>
}