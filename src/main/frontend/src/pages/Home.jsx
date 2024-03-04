import React from "react";
import axios from "axios";

export default function Home() {
  const onClick = async () => {
    //api 통신 테스트
    axios
      .get("http://158.247.197.212:9090/test")
      .then((reponse) => {
        console.log(reponse);
      })
      .catch(function (error) {
        console.log(error);
      });
  };
  return (
    <div>
      <div>
        <h3>Axios 테스트해보기</h3>
        <button onClick={onClick}>불러오기</button>
      </div>
    </div>
  );
}
