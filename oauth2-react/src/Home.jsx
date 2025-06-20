function Home() {
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

  // ✅ 로그아웃 처리 함수
  const handleLogout = () => {
    localStorage.removeItem("accessToken"); // 2. accessToken 제거
    alert("로그아웃 되었습니다.");
    navigate("/login"); // 3. 로그인 페이지로 이동
  };

  return (
    <div>
      <h1>홈입니다</h1>
      <button onClick={() => navigate("/mypage")}>마이페이지</button>
      <button onClick={handleLogout}>로그아웃</button> {/* ✅ 추가 */}
    </div>
  );
}

export default Home;
