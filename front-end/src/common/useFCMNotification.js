import { useEffect, useState } from 'react';
import axios from 'axios';
import { messaging } from '../firebaseConfig';
import Cookies from 'js-cookie';
import { jwtDecode } from 'jwt-decode';

const useFCMNotification = () => {
  const [error, setError] = useState(null);
  const [messageId, setMessageId ] = useState('');

  useEffect(() => {

    const accessToken = Cookies.get('accessToken');
    const decodedToken = jwtDecode(accessToken);
    const userId = decodedToken.id;

    const sendNotification = async () => {
      try {

        const title = '테스트용';
        const body = '시발아';


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

            setMessageId(data);
        
        
      } catch (err) {
        console.error('알림 보내기 실패:', err);
      }
    };

    sendNotification();

  }, []);

  return { messageId, error };
};

export default useFCMNotification;


