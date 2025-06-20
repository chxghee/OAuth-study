import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

function OAuth2Redirect() {
  const navigate = useNavigate();

  useEffect(() => {
    console.log("리다이렉트 페이지 진입");

    // 리디렉트된 후, 백엔드에 accessToken 요청
    fetch("http://localhost:8080/reissue", {
      method: "GET",
      credentials: "include", // ✅ 쿠키 포함 필수
    })
      .then((res) => res.json())
      .then((data) => {
        // 응답에서 accessToken 저장
        localStorage.setItem("accessToken", data.accessToken);
        alert("로그인 완료!");
        navigate("/"); // 홈으로 이동
      })
      .catch((err) => {
        console.error(err);
        alert("로그인 실패");
        navigate("/login");
      });
  }, [navigate]);

  return <div>로그인 처리 중입니다...</div>;
}

export default OAuth2Redirect;
