import { useState, useEffect } from 'react';
import axios from 'axios';
import Cookies from 'js-cookie';
import { jwtDecode } from 'jwt-decode';
import { useNavigate } from 'react-router-dom';

const useUserInfo = () => {
  const [userInfo, setUserInfo] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchUserInfo = async () => {
      const refreshToken = Cookies.get('refresh');
      const accessToken = Cookies.get('accessToken')

      if (!accessToken) {
        setError(new Error('Access token not found'));
        setLoading(false);
        return;
      }

      try {
        const decodedToken = jwtDecode(accessToken);
        const identifier = decodedToken.identifier;
        const providerInfo = decodedToken.providerInfo;

        if (!identifier) {
          throw new Error('identifier not found in refresh token');
        }

        const response = await axios.get('http://localhost:8085/api/user-info', {
          params: { identifier: identifier, providerInfo: providerInfo },
          headers: {
            'Authorization': `Bearer ${accessToken}` // 여기에 accessToken 추가
          },
          withCredentials: true // 쿠키를 포함하여 전송 (리프레시 토큰)
        });

        const apiResponse = response.data;
        const code = apiResponse.code;
        const status = apiResponse.status;
        const data = apiResponse.data;
        const message = apiResponse.message;

        console.log(data);
        console.log(apiResponse);

        if (code === 200) {
          setUserInfo(data);
        } else {
          setError(new Error(message || 'Error fetching user info'));
        }

        // data 불러오기 실패할 경우
        if (data === undefined) {
          navigate('/logout');
        }

      } catch (err) {
        console.error('Error fetching user info:', err);
        console.log("엑세스 토큰 만료됨");

        navigate('/logout');
        

      } finally {
        setLoading(false);
      }
    };

    fetchUserInfo();
  }, []);

  return { userInfo, loading, error };
};

export default useUserInfo;