export const Login = () => {

    const handleLogin = (provider:string) => {
        window.location.href = `http://localhost:8080/oauth2/authorization/${provider}`
    }

    return <>
        <button onClick={() => {
            handleLogin("line")
        }}>Lineログイン
        </button>
        <br/>
        <br/>
        <button onClick={() => {
            handleLogin("github")
        }}>githubでログイン
        </button>
        <br/>
        <br/>
        <button onClick={() => {
            handleLogin("google")
        }}>googleでログイン
        </button>
    </>
}