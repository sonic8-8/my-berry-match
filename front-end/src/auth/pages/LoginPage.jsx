import React from 'react';
import styles from './LoginPage.module.css'
import GoogleLoginButton from '../components/GoogleLoginButton';
import KakaoLoginButton from '../components/KakaoLoginButton';
import NaverLoginButton from '../components/NaverLoginButton';
import TypingEffect from 'react-typing-effect';

const LoginPage = () => {

  return (

    <div className={styles.layout}>
      <div className={styles.layout_content}>
        <div className={styles.introduction_container}>
          {/* <div className={styles.introduction_text}> */}
            <TypingEffect
              text={['혼자라서 더\n특별한 도전', 'Berry Match','랜덤 매칭으로\n당신의 실력을\n증명하세요']}
              speed={100}
              eraseSpeed={50}
              eraseDelay={2000}
              typingDelay={500}
              className={styles.introduction_text}
            />
            {/*             
            <br/>
            
            <br/>
            <span className={styles.introduction_text_berrymatch}>BerryMatch</span>
            <br/>
            
            <br/>
            당신의 실력을 증명하세요
          </div> */}
        </div>
        <div className={styles.login_container}>
          <div className={styles.login_text_container}>
            <div className={styles.login_text}>
              소셜 로그인으로
              <br/>
              빠르게
              <br/>
              시작해보세요
            </div>
          </div>
          <div className={styles.login_button_container}>
            <KakaoLoginButton />
            <NaverLoginButton />
            <GoogleLoginButton />
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;