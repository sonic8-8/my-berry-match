import React from 'react';
import styles from './KakaoLoginButton.module.css';

function KakaoLoginButton() {

    // const handleKakaoLogin = () => {
    //     window.location.href = `https://kauth.kakao.com/oauth/authorize?client_id=${process.env.REACT_APP_KAKAO_OAUTH2_CLIENT_ID}&redirect_uri=${process.env.REACT_APP_OAUTH2_REDIRECT_URI}/login/oauth2/code/kakao&response_type=code&scope=account_email`;
    //   };

      const handleKakaoLogin = () => {
        window.location.href = "http://localhost:8085/oauth2/authorization/kakao";
    };

    return (
        <div className={styles.button} onClick={handleKakaoLogin}>
            <img src="https://d1nuzc1w51n1es.cloudfront.net/c9b51919f15c93b05ae8.png" className={styles.button_img}/>
            <span className={styles.button_title}>Kakao</span>
        </div>
    )
}

export default KakaoLoginButton;