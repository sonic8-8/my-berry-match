import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import axios from 'axios';
import Cookies from 'js-cookie';

const TokenPage = () => {
  const location = useLocation();
  const [identifier, setIdentifier] = useState('');
  const [providerInfo, setProviderInfo] = useState('');


  useEffect(() => {
      // URL의 쿼리 파라미터에서 identifier 추출
      const params = new URLSearchParams(location.search);
      const identifierValue = params.get('identifier'); // 임시 변수로 identifier 값을 가져옵니다.
      const providerInfoValue = params.get('providerInfo');

      if (identifierValue && providerInfoValue) {
          setIdentifier(identifierValue); // 상태를 업데이트합니다.
          setProviderInfo(providerInfoValue);
      }

  }, [location]);


  useEffect(() => {
  
    if (identifier && providerInfo) {
      const accessToken = Cookies.get('accessToken');

      axios.post("http://localhost:8085/api/auth", { identifier: identifier, providerInfo: providerInfo }, {
        headers: {
          "Content-Type": "application/json",
          "Authorization": accessToken ? `Bearer ${accessToken}` : '', // 현재 액세스 토큰 (없을 수도 있음)
          "token-reissued": "False"
      },
      withCredentials: true // 쿠키를 포함하여 전송 (리프레시 토큰)
      })
      .then((loginResponse) => {
        const loginApiResponse = loginResponse.data;
        const loginData = loginApiResponse.data;
        const loginMessage = loginApiResponse.message;
        const loginCode = loginApiResponse.code;
        const loginStatus = loginApiResponse.status;

        if (loginCode === 200) {
          // 액세스 토큰 저장
          const authorizationHeader = loginResponse.headers['authorization'];
          const accessToken = authorizationHeader.split(' ')[1];
          
          Cookies.set('accessToken', accessToken, { path: '/' });

          console.log(loginData.role);
          console.log(accessToken);

          // 액세스 토큰이 없거나 잘못된 경우 로그인 페이지로 리다이렉트
          if (!accessToken || loginData.role === "NOT_REGISTERED") {
            window.location.href = "http://localhost:3000/login";
          } else {
            window.location.href = "http://localhost:3000/";
          }
        }
      })
      .catch((error) => {
        console.error("로그인 요청 중 오류 발생:", error);
        window.location.href = "http://localhost:3000/login"; // 오류 발생 시 로그인 페이지로 리다이렉트
      });
    }
    
  }, [identifier]);

    return (
        <div>
            {/* 인증 과정 중 로딩 스피너 등을 보여줄 수 있습니다. */}
            <p>토큰 발급 중...</p>
        </div>
    );
};

export default TokenPage;