import React, { useEffect, useState } from 'react';
import { jwtDecode } from 'jwt-decode'; // 명명된 내보내기로 수정
import Cookies from 'js-cookie';
import { EventSourcePolyfill } from 'event-source-polyfill'; // 올바르게 임포트
import axios from 'axios';
import { Link } from 'react-router-dom';
import styles from './Notification.module.css';

const Notification = () => {

    const [notifications, setNotifications] = useState([]);
    const [testNotifications, setTestNotifications] = useState([]);
    const [matchStatus, setMatchStatus] = useState('');
    const accessToken = Cookies.get('accessToken');

    useEffect(() => {

        const decodedToken = jwtDecode(accessToken);
        const userId = decodedToken.id;

        console.log(userId);

        const url = `http://localhost:8085/api/stream?userId=${userId}`

        // 1번째 HTTP 통신에서만 인증이 필요함. 인증 후에는 연결이 유지됨.
        const eventSource = new EventSourcePolyfill(url , {
            headers: {
                'Authorization': `Bearer ${accessToken}`,
                'Accept': 'text/event-stream',
            },
            withCredentials: true
        });

        console.log(eventSource);

        /**
         * 이벤트 이름을 설정하지 않았을 경우 여기서 처리합니다.
         */
        eventSource.onmessage = (event) => {
            setNotifications((prevNotifications) => [...prevNotifications, event.data]);

            console.log('이벤트 이름이 설정되지 않은 것들----------------------------')
            console.log(event);
        }
        

        /**
         * 이벤트 이름이 notification일 경우 여기서 처리합니다.
         */
        eventSource.addEventListener('notification', (event) => {

            console.log('이벤트 이름이 notification인 것들----------------------------')
            console.log(event);

            setNotifications((prevNotifications) => [...prevNotifications, event.data]);

        });

        /**
         * 이벤트 이름이 matchStatus 경우 여기서 처리합니다.
         */
        eventSource.addEventListener('matchStatus', (event) => {

            console.log('이벤트 이름이 matchStatus인 것들----------------------------')
            console.log('상태 변경 알림 받음')
            console.log("matchStatus 알림" + event.data);

            setMatchStatus(event.data);


        });

        // 에러 발생 시 연결 끊기
        eventSource.onerror = (error) => {
            console.error('SSE 오류:', error);
            eventSource.close();
        };

        return () => {
            eventSource.close();
        };
    }, [accessToken]);

    /**
     * MatchStatus를 수동으로 요청하는 테스트용 메서드
     */
    const sendMatchStatusNotification = async () => {

        const decodedToken = jwtDecode(accessToken);
        const userId = decodedToken.id;

        try {
            const response = await axios.post('http://localhost:8085/api/stream/matchStatus', { 'userId': userId }, {
                headers: {
                    'Authorization': `Bearer ${accessToken}`,
                    'Accept': 'application/json',
                }
            });

            const apiResponse = response.data;
            const code = apiResponse.code;
            const message = apiResponse.message;
            const data = apiResponse.data;
            const status = apiResponse.status;

            console.log(data);

        } catch (error) {
            console.error('matchStatus 요청 중 에러 발생: ', error);
        }
    };



    return (
        <div className={styles.matchStatus_notification_container}>
            {/* <h2>알림</h2>
            <button onClick={sendMatchStatusNotification}>알림 테스트</button>
            <ul>
                {notifications.map((notification, index) => (
                    <li key={index}>{notification}</li>
                ))}
            </ul> */}
            <div className={styles.matchStatus_notification}>
                {matchStatus == '"MATCHED"' ? 
                <Link to='/match' className={styles.matchStatus_notification_complete}>
                    {matchStatus}<br/>
                    매칭방으로 이동하기
                </Link> 
                : 
                matchStatus}</div>
        </div>
    );
};

export default Notification;