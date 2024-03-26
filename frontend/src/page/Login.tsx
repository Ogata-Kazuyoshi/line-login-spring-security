export const Login = () => {

    const handleLogin = () => {
        window.location.href = "http://localhost:8080/login/oauth2/code/line"
    }

    return <>
    <button onClick={handleLogin}>ログイン</button>
    </>
}