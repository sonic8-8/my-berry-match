import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import Cookies from 'js-cookie';
import useUserInfo from '../../user/useUserInfo'; // useUserInfo 훅 import

const LogoutButton = () => {
    const { userInfo, loading, error } = useUserInfo();
    const navigate = useNavigate(); // 페이지 리다이렉트를 위한 훅
    const [loggingOut, setLoggingOut] = useState(false); // 로그아웃 상태 관리

    const handleLogout = async () => {
        setLoggingOut(true);
        const accessToken = Cookies.get('accessToken');
        // 리프레시 토큰은 쿠키에 저장되어 자동으로 포함됨

        try {
            const response = await axios.post('http://localhost:8085/api/logout', {}, {
                headers: {
                    'Authorization': `Bearer ${accessToken}`
                },
                withCredentials: true // 쿠키를 포함하여 전송
            });

            const { message, code } = response.data;

            // 로그아웃 성공 시 처리
            if (code === 200) {
                console.log('Logout successful:', message);
                Cookies.remove('accessToken');
                Cookies.remove('refresh');
                navigate('/login'); // 로그인 페이지로 리다이렉트
            } else {
                console.error('Logout failed:', message);
            }
        } catch (error) {
            console.error('Logout error:', error);
        } finally {
            setLoggingOut(false);
        }
    };

    if (loading || loggingOut) {
        return <div>로딩중...</div>;
    }

    if (error) {
        return <div>Error: {error.message}</div>;
    }

    return (
        <button onClick={handleLogout}>Logout</button>
    );
};

export default LogoutButton;

// const { userInfo, loading, error } = useUserInfo();

// const navigate = useNavigate(); // 페이지 리다이렉트를 위한 훅
// const [loggingOut, setLoggingOut] = useState(false); // 로그아웃 상태 관리

// const handleLogout = async () => {
//     setLoggingOut(true);
//     const accessToken = Cookies.get('accessToken');

//     console.log(accessToken);
    
//     try {
//         const response = await axios.post('http://localhost:8085/api/logout', {}, {
//             headers: {
//                 'Authorization': `Bearer ${accessToken}`
//             }
//         });

//         const apiResponse = response.data;
//         const { message, code, status } = apiResponse;

//         // 로그아웃 성공 시 처리
//         if (code === 200) {
//             console.log('Logout successful:', message);
//             Cookies.remove('accessToken');
//             Cookies.remove('refresh');
//             navigate('/login'); // 로그인 페이지로 리다이렉트
//         } else {
//             console.error('Logout failed:', message);
//         }
//     } catch (error) {
//         console.error('Logout error:', error);
//     } finally {
//         setLoggingOut(false);
//     }
// };

// if ('') {
//   return <div>로딩중</div>;
// }

// if (error) {
//   return <div>Error: {error.message}</div>;
// }