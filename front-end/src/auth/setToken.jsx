import axios from 'axios';
import Cookies from 'js-cookie';

export const setToken = async (identifier) => {
    try {
        const loginResponse = await axios.post("http://localhost:8085/api/auth", { identifier }, {
            headers: {
                "Content-Type": "application/json",
            },
            withCredentials: true // 쿠키를 포함하여 전송 (리프레시 토큰)
        });

        const loginApiResponse = loginResponse.data;
        const loginData = loginApiResponse.data;
        const loginCode = loginApiResponse.code;

        if (loginCode === 200) {
            const authorizationHeader = loginResponse.headers['authorization'];
            const accessToken = authorizationHeader.split(' ')[1];

            // 토큰을 쿠키에 저장
            Cookies.set('accessToken', accessToken, { path: '/' });

            return loginData.role; // 사용자 역할을 반환
        } else {
            throw new Error('Login failed');
        }
    } catch (error) {
        console.error('Error during login and token setting:', error);
        throw error;
    }
};