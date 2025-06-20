import { useNavigate } from "react-router-dom";

function Home() {
  const navigate = useNavigate();

  // ✅ 마이페이지 API 호출
  const callApi = () => {
    const token = localStorage.getItem("accessToken");

    fetch("http://localhost:8080/api/mypage", {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
      .then((res) => res.json())
      .then((data) => alert(JSON.stringify(data)))
      .catch(() => alert("API 호출 실패"));
  };

  // ✅ 소셜 로그인 요청
  const handleLogin = (provider) => {
    window.location.href = `http://localhost:8080/oauth2/authorization/${provider}`;
  };

  // ✅ 로그아웃 요청
  const handleLogout = () => {
    console.log("🔴 로그아웃 버튼 클릭됨");

    fetch("http://localhost:8080/member/logout", {
      method: "POST",
      credentials: "include", // ✅ 쿠키 포함
    })
      .then(() => {
        localStorage.removeItem("accessToken");
        alert("로그아웃 되었습니다.");
        navigate("/");
      })
      .catch((err) => {
        console.error("로그아웃 실패:", err);
        alert("로그아웃 실패");
      });
  };

  return (
    <div style={{ textAlign: "center", marginTop: "50px" }}>
      <h1>홈입니다</h1>

      {/* 마이페이지 및 API 테스트 */}
      <button onClick={() => navigate("/mypage")}>마이페이지</button>
      <button onClick={callApi}>마이페이지 API 호출</button>

      <br />
      <br />

      {/* 로그인 */}
      <h2>소셜 로그인</h2>
      <button onClick={() => handleLogin("google")}>구글 로그인</button>
      <button onClick={() => handleLogin("naver")}>네이버 로그인</button>

      <br />
      <br />

      {/* 로그아웃 */}
      <button onClick={handleLogout}>로그아웃</button>
    </div>
  );
}

export default Home;
