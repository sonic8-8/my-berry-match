import React, { useState } from 'react';
import axios from 'axios';
import Cookies from 'js-cookie';
import styles from './MatchDummyData.module.css';

function MatchDummyData() {
    const [selectedDate, setSelectedDate] = useState('');
    const [startAvailableTime, setStartAvailableTime] = useState('');
    const [dummyCount, setDummyCount] = useState(10); // 기본 10명

    const handleSubmit = (e) => {
        e.preventDefault();
        const dummyUsers = generateDummyUsers(dummyCount);
        submitMatchRequestsForAllUsers(dummyUsers, selectedDate, startAvailableTime);
    };

    const handleCountChange = (count) => {
        setDummyCount(count);
    };

    return (
        <div>
            <div>테스트 더미 데이터</div>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>날짜 선택:</label>
                    <input
                        type="date"
                        value={selectedDate}
                        onChange={(e) => setSelectedDate(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>경기 시간:</label>
                    <input
                        type="time"
                        value={startAvailableTime}
                        onChange={(e) => setStartAvailableTime(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>더미 유저 수 선택:</label>
                    <button 
                        type="button" 
                        className={`${styles.dummyButton} ${dummyCount === 10 ? styles.selected : ''}`}
                        onClick={() => handleCountChange(10)}
                        >10명
                    </button>
                    <button 
                        type="button"
                        className={`${styles.dummyButton} ${dummyCount === 9 ? styles.selected : ''}`}
                        onClick={() => handleCountChange(9)}
                        >9명
                    </button>
                    <button 
                        type="button" 
                        className={`${styles.dummyButton} ${dummyCount === 8 ? styles.selected : ''}`}
                        onClick={() => handleCountChange(8)}
                        >8명
                    </button>
                </div>
                <button type="submit">랜덤 매칭</button>
            </form>
            <button onClick={() => submitAddRequestsForAllUsers(generateDummyUsers(dummyCount))}>추가</button>
            <button onClick={() => submitCancelRequestsForAllUsers(generateDummyUsers(dummyCount))}>매칭 취소</button>
            <button onClick={() => submitDeleteRequestsForAllUsers(generateDummyUsers(dummyCount))}>삭제</button>
        </div>
    );
}

// 더미 유저 생성 메서드
function generateDummyUsers(count) {
    const dummyUsers = [];
    for (let i = 1; i <= count; i++) {
        dummyUsers.push({
            id: i * 100,  // id가 100씩 증가
            nickname: `User${i}`,
            availableSport: 'Football',
            userMatchStatus: 'NOT_MATCHED',
            city: 'SEOUL',
            district: 'SEOUL_JONGNO_GU'
        });
    }
    return dummyUsers;
}

// 더미 유저 추가 요청을 보내는 메서드
function submitAddRequestsForAllUsers(dummyUsers) {
    const accessToken = Cookies.get('accessToken');
    dummyUsers.forEach(user => {
        const requestData = { 
            'id': user.id, 
            'nickname': user.nickname, 
            'userMatchStatus': user.userMatchStatus,
            'city': user.city,
            'district': user.district 
        };
        axios.post('http://localhost:8085/api/add-dummy', requestData, {
            headers: {
                'Authorization': `Bearer ${accessToken}`,
                'Content-Type': 'application/json',
            },
            withCredentials: true,
        }).then(response => {
            console.log(`더미 유저 추가 완료 (${user.nickname}):`, response.data);
        }).catch(error => {
            console.error(`더미 유저 추가 중 오류 발생 (${user.nickname}):`, error);
        });
    });
}

// 더미 유저 매칭 요청을 보내는 메서드
function submitMatchRequestsForAllUsers(dummyUsers, selectedDate, startAvailableTime) {
    const accessToken = Cookies.get('accessToken');
    const selectedSport = 'Football';  // 스포츠 종목은 고정됨

    dummyUsers.forEach(user => {
        const requestData = {
            'nickname': user.nickname,
            'date': selectedDate,
            'sport': selectedSport,
            'time': startAvailableTime,
            'groupCode': null
        };
        axios.post('http://localhost:8085/api/dummy-matching', requestData, {
            headers: {
                'Authorization': `Bearer ${accessToken}`,
                'Content-Type': 'application/json',
            },
            withCredentials: true,
        }).then(response => {
            console.log(`매칭 완료 (${user.nickname}):`, response.data);
        }).catch(error => {
            console.error(`매칭 중 오류 발생 (${user.nickname}):`, error);
        });
    });
}

// 더미 유저 매칭 취소 요청을 보내는 메서드
function submitCancelRequestsForAllUsers(dummyUsers) {
    const accessToken = Cookies.get('accessToken');
    dummyUsers.forEach(user => {
        const requestData = { 'nickname': user.nickname, 'message': '매칭 취소' };
        axios.post('http://localhost:8085/api/dummy-cancel', requestData, {
            headers: {
                'Authorization': `Bearer ${accessToken}`,
                'Content-Type': 'application/json',
            },
            withCredentials: true,
        }).then(response => {
            console.log(`매칭 취소 완료 (${user.nickname}):`, response.data);
        }).catch(error => {
            console.error(`매칭 취소 중 오류 발생 (${user.nickname}):`, error);
        });
    });
}

// 더미 유저 삭제 요청을 보내는 메서드
function submitDeleteRequestsForAllUsers(dummyUsers) {
    const accessToken = Cookies.get('accessToken');
    dummyUsers.forEach(user => {
        const requestData = { 'nickname': user.nickname };
        axios.post('http://localhost:8085/api/delete-dummy', requestData, {
            headers: {
                'Authorization': `Bearer ${accessToken}`,
                'Content-Type': 'application/json',
            },
            withCredentials: true,
        }).then(response => {
            console.log(`더미 유저 삭제 완료 (${user.nickname}):`, response.data);
        }).catch(error => {
            console.error(`더미 유저 삭제 중 오류 발생 (${user.nickname}):`, error);
        });
    });
}

export default MatchDummyData;
