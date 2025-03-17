// axiosConfig.js
import axios from 'axios';
import Cookies from 'js-cookie';

const apiClient = axios.create({
    baseURL: 'http://localhost:3000',
    withCredentials: true, // 쿠키를 포함하여 요청
});

// 요청 인터셉터
apiClient.interceptors.request.use(
    (config) => {
        const token = Cookies.get('accessToken');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        console.log(config);
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// 응답 인터셉터
apiClient.interceptors.response.use(
    (response) => {
        return response;
    },
    async (error) => {
        const originalRequest = error.config;
        
        if (!originalRequest._retry) {
            originalRequest._retry = true;

            try {
                const response = await axios.post('http://localhost:8085/api/auth/refresh', {}, { withCredentials: true });
                
                const authorizationHeader = response.headers['authorization'];
                const newAccessToken = authorizationHeader.split(' ')[1];
                
                Cookies.set('accessToken', newAccessToken, { path: '/' });

                // 원래의 요청을 새로운 토큰으로 재시도
                apiClient.defaults.headers.Authorization = `Bearer ${accessToken}`;
                return apiClient(originalRequest);
            } catch (refreshError) {
                console.error('Refresh token is invalid', refreshError);
                // 리다이렉트 또는 로그아웃 처리
                window.location.href = "http://localhost:3000/logout";
            }
        }
        return Promise.reject(error);
    }
);

export default apiClient;
