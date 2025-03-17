import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import Cookies from 'js-cookie';

const LogoutPage = () => {
    const navigate = useNavigate();
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const handleLogout = async () => {

            try {
                const accessToken = Cookies.get('accessToken');
                const refreshToken = Cookies.get('refresh');

                console.log(Cookies.get('Refresh'));

                console.log(accessToken);
                console.log(refreshToken);

                const response = await axios.post('http://localhost:8085/api/logout', {}, {
                    headers: {
                        'Authorization': `Bearer ${accessToken}`,
                        'Content-Type': 'application/json'
                    },
                    withCredentials: true // 쿠키를 포함하여 전송
                });

                Cookies.remove('accessToken');
                Cookies.remove('refresh');

                navigate('/login');
            } catch (err) {
                Cookies.remove('accessToken');
                Cookies.remove('refresh');

                setError('로그아웃 중 오류가 발생했습니다.');
                console.error('로그아웃 에러요:', err);


                navigate('/login');

            } finally {
                setLoading(false);
            }
        };

        handleLogout();
    }, [navigate]);

    if (loading) {
        return <div>로그아웃 중...</div>;
    }

    if (error) {
        return <div>Error: {error}</div>;
    }

    return null; // 로그아웃 완료 후 페이지 내용 없음
};

export default LogoutPage;