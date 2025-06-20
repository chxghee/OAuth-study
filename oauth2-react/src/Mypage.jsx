import { useEffect, useState } from "react";

function Mypage() {
  const [userInfo, setUserInfo] = useState(null);

  useEffect(() => {
    const token = localStorage.getItem("accessToken");

    fetch("http://localhost:8080/member/mypage", {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
      .then((res) => {
        if (!res.ok) throw new Error("로그인 필요");
        return res.json();
      })
      .then((data) => setUserInfo(data))
      .catch((err) => {
        console.error(err);
        alert("사용자 정보를 불러올 수 없습니다. 로그인 해주세요.");
      });
  }, []);

  if (!userInfo) return <div>로딩 중...</div>;

  return (
    <div style={{ padding: "20px" }}>
      <h2>마이페이지</h2>
      <p>
        <strong>ID:</strong> {userInfo.memberAuthInfo.id}
      </p>
      <p>
        <strong>Email:</strong> {userInfo.memberAuthInfo.email}
      </p>
      <p>
        <strong>Nickname:</strong> {userInfo.memberAuthInfo.nickname}
      </p>
      <p>
        <strong>Role:</strong> {userInfo.memberAuthInfo.role}
      </p>
    </div>
  );
}

export default Mypage;
