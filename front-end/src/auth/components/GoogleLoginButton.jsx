import React from 'react';
import styles from './GoogleLoginButton.module.css';

function GoogleLoginButton() {

    // const handleGoogleLogin = () => {
    //     window.location.href = `https://accounts.google.com/o/oauth2/auth?client_id=${process.env.REACT_APP_GOOGLE_OAUTH2_CLIENT_ID}&redirect_uri=${process.env.REACT_APP_OAUTH2_REDIRECT_URI}/login/oauth2/code/google&response_type=code&scope=email%20profile`;
    // };

    const handleGoogleLogin = () => {
        window.location.href = "http://localhost:8085/oauth2/authorization/google";
    };

    return (
        <div className={styles.button} onClick={handleGoogleLogin}>
            <img src="https://d1nuzc1w51n1es.cloudfront.net/d99d8628713bb69bd142.png" className={styles.button_img} alt="Google Login"/>
            <span className={styles.button_title}>Google</span>
        </div>
    );
}


export default GoogleLoginButton;

// try {
//     // 액세스 토큰에서 identifier 추출
//     const accessToken = Cookies.get('accessToken');
//     const decodedToken = accessToken ? jwtDecode(accessToken) : null;
//     const identifier = decodedToken ? decodedToken.Identifier : null;
//     const providerInfo = decodedToken ? decodedToken.ProviderInfo : null;


//     console.log(identifier);

//     // 새 액세스 토큰 발급 요청
//     const response = await axios.post('http://localhost:8085/api/auth', { identifier: identifier, providerInfo: providerInfo }, {
//       headers: {
//         'Authorization': `Bearer ${accessToken}`,
//         'Content-Type': 'application/json'
//       },
//       withCredentials: true // 쿠키를 자동으로 포함하도록 설정
//     });

//     console.log("성공");

    
//     const apiResponse = response.data;
//     const authorizationHeader = apiResponse.headers['authorization'];
//     const newAccessToken = authorizationHeader.split(' ')[1]; // 응답 헤더에서 새 액세스 토큰 추출
    
//     // 새로운 액세스 토큰과 리프레시 토큰을 쿠키와 axios에 설정
//     Cookies.set('accessToken', newAccessToken, { path: '/' });

//     console.log("ㄹㅇ 성공함");

//     const userInfoResponse = await axios.get('http://localhost:8085/api/user-info', {
//       params: { identifier: identifier },
//       headers: {
//         'Authorization': `Bearer ${accessToken}` // 여기에 accessToken 추가
//       },
//       withCredentials: true // 쿠키를 포함하여 전송 (리프레시 토큰)
//     });

//     const { data, message, code, status } = userInfoResponse.data;

//     console.log(data);

//     if (code === 200) {
//       setUserInfo(data);
//     } else {
//       setError(new Error(message || 'Error fetching user info'));
//     }

    

//   } catch (refreshError) {
//     console.error('Refresh token failed', refreshError);
//     // 로그아웃 처리 또는 로그인 페이지로 리다이렉트
//     // window.location.href = 'http://localhost:3000/login';
//   }
