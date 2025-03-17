import useFCMNotification from "../useFCMNotification"
import { useEffect, useState } from 'react';
import axios from 'axios';
import Cookies from 'js-cookie';
import { jwtDecode } from 'jwt-decode';

function FCMNotificationTestButton() {

    const getFCMNotification = () => {


            const accessToken = Cookies.get('accessToken');
            const decodedToken = jwtDecode(accessToken);
            const userId = decodedToken.id;
        
            const sendNotification = async () => {
              try {
        
                const title = '테스트용';
                const body = '테스트 알림이 전송되었습니다.';
        
        
                // 토큰을 서버로 전송
                const response = await axios.post('http://localhost:8085/api/fcm/notification',
                    {
                      'title': title, 
                      'body': body,
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
            
                    console.log(apiResponse);
                    console.log(data);
                
              } catch (err) {
                console.error('알림 보내기 실패:', err);
              }
            };
        
            sendNotification();
    }

    return (
        <button onClick={getFCMNotification}>
            FCM 알림 보내기
        </button>
    )
}

export default FCMNotificationTestButton;