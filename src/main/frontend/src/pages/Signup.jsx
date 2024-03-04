import React, { useState } from "react";
import axios from "axios";
import { MdOutlineEmail, MdOutlinePhoneIphone } from "react-icons/md";
import { TbLock, TbLockCheck } from "react-icons/tb";
import { GiCharacter } from "react-icons/gi";
import { IconContext } from "react-icons";

function SignUp() {
  const [emailSuccess, setEmailSuccess] = useState(false);
  const [passwordSuccess, setPasswordSuccess] = useState(false);
  const [confirmPasswordSuccess, setConfirmPasswordSuccess] = useState(false);
  const [confirmPassword, setConfirmPassword] = useState("");
  const [authInfo, setAuthInfo] = useState({
    email: "",
    authNumber: "",
    authValid: false,
  });
  const [userInfo, setUserInfo] = useState({
    name: "",
    phone: "",
    email: "",
    password: "",
    role: "USER",
  });

  //input 입력시 값을 userInfo에 저장
  const handleChange = (event) => {
    setUserInfo({
      ...userInfo,
      [event.target.name]: event.target.value,
    });
  };

  //인증번호요청
  const certificationNumberRequest = async () => {
    // const sendEmail = await API.get(
    //   // "/auth/send_email",
    //   "/signupAuthentication"
    // );
    // const certificationNumberCheck = await axios.get(
    //   `https://test-xtcj6il6hq-du.a.run.app/auth/send_email?email=${userInfo.useremail}`
    // );
    const certificationNumberCheck = await axios.get(
      `https://test-xtcj6il6hq-du.a.run.app/auth/send_email?`,
      { params: { email: userInfo.email } }
    );
    //사용자 이메일로 인증번호를 생성해서 가져온다.
    try {
      debugger;
      console.log(certificationNumberCheck.data);
      if (certificationNumberCheck.data["result"] === "OK") {
        setAuthInfo({
          ...authInfo,
          email: userInfo.email,
          authNumber: certificationNumberCheck.data["data"],
        });
      }
    } catch (err) {
      debugger;
      console.log(err.response.data);
      setEmailSuccess(false);
    }
  };
  // 이메일 중복체크
  const emailDuplicateCheck = async () => {
    const duplicateCheck = await axios.get(
      `https://test-xtcj6il6hq-du.a.run.app/auth/check_email`,
      { params: { email: userInfo.email } }
    );
    try {
      debugger;
      if (duplicateCheck.data.data === true) {
        //이메일 중복
        document.getElementById("emailDuplicate").style.display = "block";
      } else {
        //가입가능
        document.getElementById("authFail").style.display = "none";
        document.getElementById("emailFail").style.display = "none";
        document.getElementById("emailDuplicate").style.display = "none";
        certificationNumberRequest();
        setEmailSuccess(true);
      }
    } catch (err) {
      console.log(err.response.data);
      setEmailSuccess(false);
    }
  };
  //인증번호 유효성 체크
  const handleAuthCheck = (event) => {
    let inputValue = event.target.value;
    let authNumber = authInfo.authNumber;
    let i = 0;
    while (i < inputValue.length && i < authNumber.length) {
      if (inputValue[i] !== authNumber[i]) {
        document.getElementById("authFail").style.display = "block";
      } else {
        document.getElementById("authFail").style.display = "none";
        document.getElementById("emailFail").style.display = "none";
        document.getElementById("emailDuplicate").style.display = "none";
      }
      i++;
    }
    if (inputValue === authNumber) {
      setEmailSuccess(true);
      setAuthInfo({
        ...authInfo,
        authValid: true,
      });
      document.getElementById("authSuccess").style.display = "block";
    }
  };

  //이메일 유효성 체크
  const validateEmail = () => {
    //debugger;
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    //이메일 형식이 맞으면 _emailSuccess가 true
    let _emailSuccess = emailRegex.test(userInfo.email);
    // setEmailSuccess(_emailSuccess);
    if (_emailSuccess === false) {
      document.getElementById("emailFail").style.display = "block";
    } else {
      document.getElementById("emailFail").style.display = "none";
      document.getElementById("authFail").style.display = "none";
      document.getElementById("emailDuplicate").style.display = "none";
      //GET  /auth/check_email 여기로 통신을 보내야 한다. 이메일 중복 체크 하는..
      emailDuplicateCheck();
    }
  };
  //비밀번호 유효성 체크
  const validatePassword = () => {
    // 영어 대문자, 소문자, 숫자중에서 8자 이상 12자 이하의 문자열
    const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,12}$/;
    setPasswordSuccess(passwordRegex.test(userInfo.password));
  };
  //비밀번호 확인 유효성 체크
  const validateConfirmPassword = () => {
    debugger;
    setConfirmPasswordSuccess(userInfo.password === confirmPassword);
  };

  //회원 가입 버튼
  const handleSubmit = async (event) => {
    event.preventDefault();

    debugger;
    if (
      emailSuccess === true &&
      authInfo.authValid === true &&
      passwordSuccess === true &&
      confirmPasswordSuccess === true
    ) {
      const res = await axios.post(
        `https://test-xtcj6il6hq-du.a.run.app/auth/signup`,
        userInfo
      );
      try {
        debugger;
        console.log(res.data);
      } catch (err) {
        // console.log(err.response.data);
        console.log("err:", err);
      }
    }
    // const res = await API.post("/users", userInfo);
    // console.log(res.data);
    // success("가입되엇습니다");
  };
  return (
    <IconContext.Provider
      value={{
        className: "signupIcon",
      }}
    >
      <div className="flex justify-center items-center flex-col w-6/12 h-3/5 mx-auto my-12">
        <form onSubmit={handleSubmit} className="w-full h-full">
          <div
            id="section_title"
            className="border-sky-400 h-12 flex justify-center items-center"
          >
            회원정보를 입력해주세요
          </div>
          <div id="join_content" className="w-full h-full">
            <div id="member_wrap" className="mt-4">
              <div id="member_input_field" className="relative">
                <label
                  htmlFor="join_name"
                  className="relative cursor-pointer block h-12 p-0 border border-stone-300 box-border"
                >
                  <span className="relative h-full min-w-11 border-r float-left flex items-center justify-center border-stone-300 box-border">
                    <GiCharacter />
                  </span>
                  <div className="relative m-0 overflow-hidden">
                    <input
                      className="m-0 h-full w-full align-middle box-border pl-2 pt-3 pb-3 text-sm"
                      id="join_name"
                      type="text"
                      name="name"
                      value={userInfo.name}
                      onChange={handleChange}
                      required
                    />
                    <span className="signupInput">이름</span>
                  </div>
                </label>
              </div>
            </div>
            <div id="member_wrap">
              <div id="member_input_field" className="relative">
                <label
                  htmlFor="join_selectInput"
                  className="relative cursor-pointer block h-12 p-0 border border-stone-300 box-border"
                >
                  <span className="relative h-full min-w-11 border-r float-left flex items-center justify-center border-stone-300 box-border">
                    <MdOutlinePhoneIphone />
                  </span>
                  <div className="relative m-0 overflow-hidden">
                    <input
                      id="join_selectInput"
                      className="m-0 h-full w-full align-middle box-border pl-2 pt-3 pb-3 text-sm"
                      type="tel"
                      name="phone"
                      value={userInfo.phone}
                      onChange={handleChange}
                    />
                    <span className="signupInput">선택 입력</span>
                  </div>
                </label>
              </div>
            </div>
            <div id="member_wrap">
              <div id="member_input_field" className="relative">
                <label
                  htmlFor="join_inputEmail"
                  className="relative cursor-pointer block h-12 p-0 border border-stone-300 box-border"
                >
                  <span className="relative h-full min-w-11 border-r float-left flex items-center justify-center border-stone-300 box-border">
                    <MdOutlineEmail />
                  </span>
                  <div className="relative m-0 overflow-hidden">
                    <input
                      className="m-0 h-full w-full align-middle box-border pl-2 pt-3 pb-3 text-sm"
                      id="join_inputEmail"
                      type="text"
                      name="email"
                      value={userInfo.email}
                      onChange={handleChange}
                      // onBlur={validateEmail}
                      required
                    />
                    <span className="signupInput">이메일 입력</span>
                  </div>
                </label>
              </div>
            </div>
            <button id="btnCheck">인증메일 받기</button>
            <div id="member_wrap">
              <div id="member_input_field" className="relative">
                <label
                  htmlFor="join_authNumber"
                  className="relative cursor-pointer block h-12 p-0 border border-stone-300 box-border"
                >
                  <span className="relative h-full min-w-11 border-r float-left flex items-center justify-center border-stone-300 box-border">
                    <MdOutlineEmail />
                  </span>
                  <div className="relative m-0 overflow-hidden">
                    <input
                      id="join_authNumber"
                      className="m-0 h-full w-full align-middle box-border pl-2 pt-3 pb-3 text-sm"
                      type="text"
                      placeholder=""
                      onChange={handleAuthCheck}
                      name=""
                    />
                    <span className="signupInput">인증번호를 입력하세요</span>
                  </div>
                </label>
              </div>
            </div>
            {/* {!emailSuccess && (
              // <IoIosCloseCircle style={{ color: "red", marginLeft: "10px" }} />
              // <span style={{ color: "red", marginLeft: "10px" }}>✖️</span>
              
            )}
            {emailSuccess && userInfo.useremail && (
              // <IoIosCheckmarkCircle
              //   style={{ color: "green", marginLeft: "10px" }}
              // />
              // <span>✅</span>
              
            )} */}
            <span
              id="emailFail"
              style={{ color: "red", marginLeft: "10px", display: "none" }}
            >
              이메일 주소를 다시 확인해주세요.
            </span>
            <span
              id="emailDuplicate"
              style={{ color: "red", marginLeft: "10px", display: "none" }}
            >
              이미 가입된 이메일 주소입니다.
            </span>
            <span
              id="authFail"
              style={{ color: "red", marginLeft: "10px", display: "none" }}
            >
              인증번호가 일치하지 않습니다.
            </span>
            <span
              id="authSuccess"
              style={{ color: "green", marginLeft: "10px", display: "none" }}
            >
              인증번호가 일치
            </span>
            <span
              id="emailSuccess"
              style={{
                display: "none",
                color: "green",
                fontSize: "10px",
              }}
            >
              인증번호를 발송했습니다.(유효시간 30분)
              <br />
              인증번호가 오지 않으면 입력하신 정보가 정확한지 확인하여 주세요.
              <br />
              이미 가입된 이메일은 인증번호를 받을 수 없습니다.
              <br />
            </span>
            <div id="member_wrap">
              <div id="member_input_field" className="relative">
                <label
                  htmlFor="join_password"
                  className="relative cursor-pointer block h-12 p-0 border border-stone-300 box-border"
                >
                  <span className="relative h-full min-w-11 border-r float-left flex items-center justify-center border-stone-300 box-border">
                    <TbLock />
                  </span>
                  <div className="relative m-0 overflow-hidden">
                    <input
                      id="join_password"
                      className="m-0 h-full w-full align-middle box-border pl-2 pt-3 pb-3 text-sm"
                      type="password"
                      name="password"
                      value={userInfo.password}
                      onChange={handleChange}
                      onBlur={validatePassword}
                      required
                      autoComplete="on"
                    />
                    <span className="signupInput">비밀번호 입력</span>
                  </div>
                </label>
              </div>
              {/* {!passwordSuccess && <span style={{ color: "red" }}>✖️</span>}
            {passwordSuccess && userInfo.password && <span>✅</span>} */}
            </div>
            <div id="member_wrap">
              <div id="member_input_field" className="relative">
                <label
                  htmlFor="join_checkPassword"
                  className="relative cursor-pointer block h-12 p-0 border border-stone-300 box-border"
                >
                  <span className="relative h-full min-w-11 border-r float-left flex items-center justify-center border-stone-300 box-border">
                    <TbLockCheck />
                  </span>
                  <div className="relative m-0 overflow-hidden">
                    <input
                      id="join_checkPassword"
                      className="m-0 h-full w-full align-middle box-border pl-2 pt-3 pb-3 text-sm"
                      type="password"
                      name="confirmPassword"
                      value={confirmPassword}
                      onChange={(e) => setConfirmPassword(e.target.value)}
                      onBlur={validateConfirmPassword}
                      required
                      autoComplete="on"
                    />
                    <span className="signupInput">비밀번호 확인</span>
                  </div>
                </label>
              </div>
              {/* {!confirmPasswordSuccess && <span style={{ color: "red" }}>✖️</span>}
            {confirmPasswordSuccess && userInfo.password && confirmPassword && (
              <span>✅</span>
            )} */}
            </div>
            <button
              className="bg-sky-400 h-12 flex justify-center items-center text-white w-full"
              type="submit"
            >
              가입하기
            </button>
          </div>
        </form>
      </div>
    </IconContext.Provider>
  );
}

export default SignUp;
