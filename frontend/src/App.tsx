import './App.css'
import {Route, Routes} from "react-router-dom";
import {Login} from "./page/Login.tsx";
import {Mypage} from "./page/Mypage.tsx";

function App() {

  return (
    <>
     <Routes>
         <Route path="/login" element={<Login />}/>
         <Route path="/mypage" element={<Mypage />}/>
     </Routes>
    </>
  )
}

export default App
