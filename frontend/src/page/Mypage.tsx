import axios from "axios";
import {useNavigate} from "react-router-dom";
import {
    csrfHandlerRepository,
    getUserInfoRepository,
    logoutRepository,
    testPostHandlerRepository,
    updateUserInfoRepository
} from "../repository/UserRepository.ts";
import {useEffect, useState} from "react";
import {csrfManager} from "../csrf/CsrfManager.ts";

type UserInfo = {
    id : string,
    oid : string,
    name : string,
    remark : string
}

export const Mypage = () => {
    const [user, setUser] = useState<UserInfo | null>(null)
    const [remark, setRemark] = useState("")
    const [bodyParam1, setBodyParam1] = useState("")
    const [bodyParam2, setBodyParam2] = useState("")
    const [queryParam, setQueryParam] = useState("")
    const [pathParam, setPathParam] = useState("")

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

    const testPostHandler = async () => {
        const res =await testPostHandlerRepository({bodyParam1: bodyParam1, bodyParam2: bodyParam2})
        console.log(`testPostConsole`, res)
    }

    const csrfHandler = async () => {
        const res = await csrfHandlerRepository()
        console.log("csrf : ", res)
        console.log("token : ",res.token)
        csrfManager.setCsrf(res.token)
    }

    const logoutHandler = async () => {
        try {
            const res = await logoutRepository()
            console.log(res)
        } catch (err) {
           navigate("/front-login")
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
        <button onClick={csrfHandler}>GetCsrf</button>
        <div>
            <label>remarkの編集</label>
            <input type="text" value={remark} onChange={(e)=> {setRemark(e.target.value)}}/>
            <br/>
            <button onClick={updateInfo}>投稿</button>
        </div>
        <br/>
        <div>
            <label>bodyパラメーター1</label>
            <input type="text" value={bodyParam1} onChange={(e)=>{setBodyParam1(e.target.value)}}/>
            <br/>
            <label>bodyパラメーター2</label>
            <input type="text" value={bodyParam2} onChange={(e)=>{setBodyParam2(e.target.value)}}/>
            <br/>
            <label>クエリパラメーター1</label>
            <input type="text" value={queryParam} onChange={(e)=>{setQueryParam(e.target.value)}}/>
            <br/>
            <label>パスパラメーター1</label>
            <input type="text" value={pathParam} onChange={(e)=>{setPathParam(e.target.value)}}/>
            <br/>
            <button onClick={testPostHandler}>投稿２</button>
        </div>
        <br/>
        <button onClick={logoutHandler}>ログアウト</button>
    </>
}