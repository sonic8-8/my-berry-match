import React from 'react';
import styles from './NaverLoginButton.module.css';

function NaverLoginButton() {

    // const handleNaverLogin = () => {
    //     window.location.href = `https://nid.naver.com/oauth2.0/authorize?client_id=${process.env.REACT_APP_NAVER_OAUTH2_CLIENT_ID}&redirect_uri=${process.env.REACT_APP_OAUTH2_REDIRECT_URI}/login/oauth2/code/naver&response_type=code&scope=email`;
    //   };

      const handleNaverLogin = () => {
        window.location.href = "http://localhost:8085/oauth2/authorization/naver";
    };

    return (
        <div className={styles.button} onClick={handleNaverLogin}>
            <img src="https://d1nuzc1w51n1es.cloudfront.net/6e4f331986317290b3ee.png" className={styles.button_img}/>
            <span className={styles.button_title}>Naver</span>
        </div>
    )
}

export default NaverLoginButton;