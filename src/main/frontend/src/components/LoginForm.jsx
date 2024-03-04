import React, { useState } from "react";
import axios from "axios";
import { Link, useNavigate } from "react-router-dom";

export default function LoginForm() {
  const navigate = useNavigate();
  const [loginInfo, setLoginInfo] = useState({
    id: "",
    password: "",
  });
  const [loginFail, setLoginFail] = useState(false);
  const handleChange = (event) => {
    setLoginInfo({
      ...loginInfo,
      [event.target.name]: event.target.value,
    });
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    await axios
      .post(`http://localhost:4000/login`, loginInfo)
      .then(function (response) {
        if (response.status === 201) {
          setLoginFail(false);
          navigate("/");
        } else {
          setLoginFail(true);
        }
      })
      .catch(function (error) {
        console.log(error);
      });
  };
  return (
    <div className="flex justify-center items-center flex-col w-96 h-3/5 border rounded px-4 mx-auto mt-12">
      <form onSubmit={handleSubmit} className="w-full h-full">
        <div>
          <div className="relative m-auto overflow-hidden pb-1.5 pt-7">
            <input
              className="m-0 h-6 w-full align-middle box-border rounded-md border-solid border border-neutral-500 py-3.5 px-4 text-sm"
              id="user_id"
              type="text"
              name="id"
              placeholder="아이디"
              value={loginInfo.id}
              onChange={handleChange}
              required
            />
          </div>
          <div className="relative m-auto overflow-hidden py-1.5">
            <input
              className="m-0 h-6 w-full align-middle box-border border-solid border border-neutral-500 rounded-md py-3.5 px-4 text-sm"
              id="user_password"
              type="password"
              name="password"
              placeholder="비밀번호"
              autoComplete="on"
              value={loginInfo.password}
              onChange={handleChange}
              required
            />
          </div>
          {loginFail && (
            <label style={{ color: "red" }}>
              아이디 혹은 비밀번호가 틀렸습니다.
            </label>
          )}
        </div>
        <div className="relative m-auto overflow-hidden pt-1.5 text-center">
          <button
            className="text-white bg-lime-900 px-16 border-0 rounded-md w-full"
            id="loginBtn"
            name="login"
            type="submit"
          >
            로그인
          </button>
        </div>
        <div className="mt-4 text-center">
          <Link className="text-blue-700 text-sm" to="/passwordsearch">
            비밀번호 찾기
          </Link>
        </div>
        <div className="border-solid border-b border-neutral-500 my-5"></div>
        <div className="pt-1.5 pb-5 text-center">
          <Link
            className="border-0 px-4 bg-lime-600 text-white rounded-md"
            to="/signup"
          >
            새 계정 만들기
          </Link>
        </div>
      </form>
    </div>
  );
}
