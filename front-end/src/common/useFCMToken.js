import { useEffect, useState } from 'react';
import axios from 'axios';
import { messaging } from '../firebaseConfig';
import { getMessaging, getToken, onMessage } from 'firebase/messaging';
import Cookies from 'js-cookie';
import { jwtDecode } from 'jwt-decode';

const useFCMToken = () => {
  const [token, setToken] = useState(null);
  const [error, setError] = useState(null);
  const [isRegistered, setIsRegistered] = useState(false);

  useEffect(() => {

    const accessToken = Cookies.get('accessToken');
    const decodedToken = jwtDecode(accessToken);
    const userId = decodedToken.id;

    const registerToken = async () => {
      try {
        const messagingInstance = messaging; // Firebase messaging instance 가져오기

        // FCM 토큰 요청 및 발급
        const fcmToken = await getToken(messagingInstance, { vapidKey: process.env.REACT_APP_VAPID_KEY });

        setToken(fcmToken);


        // 토큰을 서버로 전송
        const response = await axios.post('http://localhost:8085/api/fcm/register',
            {
              'fcmToken': fcmToken, 
              'userId': userId
            }, 
            {
              headers: {
                  'Authorization': `Bearer ${accessToken}`
              },
              withCredentials: true
            });

            console.log(response);

            const apiResponse = response.data;
            const code = apiResponse.code;
            const status = apiResponse.status;
            const data = apiResponse.data;
            const message = apiResponse.message;
    
            console.log(data);
            console.log(apiResponse);


      setIsRegistered(true);
        
        
      } catch (err) {
        setError('FCM 토큰 등록 실패');
        console.error('Error getting permission or token:', err);
      }
    };

    registerToken();

    // 메시지 수신 대기
    const messagingInstance = messaging; // firebase 메세징 인스턴스
    onMessage(messagingInstance, (payload) => {
      console.log('Message received: ', payload);
      const notificationTitle = payload.notification.title;
      const notificationOptions = {
        body: payload.notification.body,
        icon: '/https://thank-you-berrymatch-bucket-0.s3.ap-northeast-2.amazonaws.com/design/logo.png' // 알림에 표시할 아이콘
      };
    
      // 브라우저에서 푸시 알림을 띄움
      if (Notification.permission === 'granted') {
        new Notification(notificationTitle, notificationOptions);
      }
    });

  }, []);

  return { token, error, isRegistered };
};

export default useFCMToken;



          