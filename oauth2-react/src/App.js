import { BrowserRouter, Routes, Route } from "react-router-dom";
import OAuth2Redirect from "./OAuth2Redirect";
import Home from "./Home";
import Mypage from "./Mypage";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/oauth2/redirect" element={<OAuth2Redirect />} />
        <Route path="/mypage" element={<Mypage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
