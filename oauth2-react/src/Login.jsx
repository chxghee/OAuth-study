function Login() {
  const handleLogin = (provider) => {
    window.location.href = `http://localhost:8080/oauth2/authorization/${provider}`;
  };

  return (
    <div style={{ textAlign: "center", marginTop: "100px" }}>
      <h2>소셜 로그인</h2>
      <button onClick={() => handleLogin("google")}>구글 로그인</button>
      <button onClick={() => handleLogin("naver")}>네이버 로그인</button>
    </div>
  );
}

export default Login;
