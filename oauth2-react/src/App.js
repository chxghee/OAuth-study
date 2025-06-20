import { BrowserRouter, Routes, Route } from "react-router-dom";
import OAuth2Redirect from "./OAuth2Redirect";
import Login from "./Login"; // 👈 Login.jsx 추가
import Home from "./Home"; // 👈 Home.jsx도 따로 분리해서 관리하면 좋음
import Mypage from "./Mypage";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} /> // ✔ 5단계 Home
        <Route path="/login" element={<Login />} /> // ✔ 4단계 Login
        <Route path="/oauth2/redirect" element={<OAuth2Redirect />} />
        <Route path="/mypage" element={<Mypage />} />{" "}
        {/* ✅ 마이페이지 라우팅 */}
      </Routes>
    </BrowserRouter>
  );
}

export default App;
