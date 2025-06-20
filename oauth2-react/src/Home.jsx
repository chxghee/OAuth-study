import { useNavigate } from "react-router-dom";

function Home() {
  const navigate = useNavigate();

  const callApi = () => {
    const token = localStorage.getItem("accessToken");

    fetch("http://localhost:8080/api/mypage", {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
      .then((res) => res.json())
      .then((data) => alert(JSON.stringify(data)))
      .catch((err) => alert("API 호출 실패"));
  };

  const handleLogout = () => {
    localStorage.removeItem("accessToken");
    alert("로그아웃 되었습니다.");
    navigate("/login"); //  로그인 페이지로 이동
  };

  return (
    <div>
      <h1>홈입니다</h1>
      <button onClick={() => navigate("/mypage")}>마이페이지</button>
      <button onClick={handleLogout}>로그아웃</button>
    </div>
  );
}

export default Home;
