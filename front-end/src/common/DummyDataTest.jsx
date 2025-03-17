import React, { useState } from 'react';
import axios from 'axios';
import Cookies from 'js-cookie';
import styles from './DummyDataTest.module.css';

function DummyDataTest() {
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
        <div className={styles.test_container}>

            <div>더미 유저 테스트</div>

            <form onSubmit={handleSubmit} className={styles.test}>
                
                    <div>
                        <label>날짜 선택:</label>
                        <input
                            type="date"
                            value={selectedDate}
                            onChange={(e) => setSelectedDate(e.target.value)}
                            required
                            className={styles.test_input}
                        />
                    </div>
                    <div>
                        <label>경기 시간:</label>
                        <input
                            type="time"
                            value={startAvailableTime}
                            onChange={(e) => setStartAvailableTime(e.target.value)}
                            required
                            className={styles.test_input}
                        />
                    </div>
                    <div>
                        <label>더미 유저 수 선택:</label>
                        <button type="button" onClick={() => handleCountChange(10)} className={styles.test_button_user}>10명</button>
                        <button type="button" onClick={() => handleCountChange(9)} className={styles.test_button_user}>9명</button>
                        <button type="button" onClick={() => handleCountChange(8)} className={styles.test_button_user}>8명</button>
                    </div>

                    <button type="submit" className={styles.test_button}>랜덤 매칭</button>
                </form>    
            


            <div className={styles.test}>
                <div>메인보드 테스트</div>
                <button onClick={() => submitAddRequestsForAllUsers(generateDummyUsers(dummyCount))} className={styles.test_button}>추가</button>
                <button onClick={() => submitCancelRequestsForAllUsers(generateDummyUsers(dummyCount))} className={styles.test_button}>매칭 취소</button>
                <button onClick={() => submitDeleteRequestsForAllUsers(generateDummyUsers(dummyCount))} className={styles.test_button}>삭제</button>
            </div>

            <div className={styles.test}>
                <div>매칭 대시보드 테스트</div>
                <button onClick={() => dummyReadyOn(generateDummyUsers(dummyCount))} className={styles.test_button}>매칭방 준비</button>
                <button onClick={() => dummyReadyOff(generateDummyUsers(dummyCount))} className={styles.test_button}>매칭방 준비 취소</button>
                <button onClick={() => dummyLeave(generateDummyUsers(dummyCount))} className={styles.test_button}>경기 떠나기</button>
            </div>

            <div className={styles.test}>
                <div>게임 대시보드 테스트</div>
                <button onClick={() => dummyVote(generateDummyUsers(dummyCount))} className={styles.test_button}>경기 종료</button>
            </div>
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

// 더미 유저를 준비 상태로 업데이트 하는 메서드
function dummyReadyOn(dummyUsers) {
    const accessToken = Cookies.get('accessToken');
    dummyUsers.forEach(user => {
        const requestData = { 
            'nickname': user.nickname, 
        };
        axios.post('http://localhost:8085/api/dummy-ready', requestData, {
            headers: {
                'Authorization': `Bearer ${accessToken}`,
                'Content-Type': 'application/json',
            },
            withCredentials: true,
        }).then(response => {
            console.log(`더미 유저 준비 완료 (${user.nickname}):`, response.data);
        }).catch(error => {
            console.error(`더미 유저 준비 중 오류 발생 (${user.nickname}):`, error);
        });
    });
}

// 더미 유저를 준비 취소 상태로 업데이트 하는 메서드
function dummyReadyOff(dummyUsers) {
    const accessToken = Cookies.get('accessToken');
    dummyUsers.forEach(user => {
        const requestData = { 
            'nickname': user.nickname, 
        };
        axios.post('http://localhost:8085/api/dummy-waiting', requestData, {
            headers: {
                'Authorization': `Bearer ${accessToken}`,
                'Content-Type': 'application/json',
            },
            withCredentials: true,
        }).then(response => {
            console.log(`더미 유저 준비 취소 완료 (${user.nickname}):`, response.data);
        }).catch(error => {
            console.error(`더미 유저 준비 취소 중 오류 발생 (${user.nickname}):`, error);
        });
    });
}

// 더미 유저가 경기를 떠나도록 하는 메서드
function dummyLeave(dummyUsers) {
    const accessToken = Cookies.get('accessToken');
    dummyUsers.forEach(user => {
        const requestData = { 
            'nickname': user.nickname, 
        };
        axios.post('http://localhost:8085/api/dummy-leave', requestData, {
            headers: {
                'Authorization': `Bearer ${accessToken}`,
                'Content-Type': 'application/json',
            },
            withCredentials: true,
        }).then(response => {
            console.log(`더미 유저 경기 떠나기 완료 (${user.nickname}):`, response.data);
        }).catch(error => {
            console.error(`더미 유저 경기 떠나기 중 오류 발생 (${user.nickname}):`, error);
        });
    });
}

// 더미 유저 경기 종료 상태를 업데이트 하는 메서드
function dummyVote(dummyUsers) {
    const accessToken = Cookies.get('accessToken');
    dummyUsers.forEach(user => {
        const requestData = { 
            'nickname': user.nickname, 
        };
        axios.post('http://localhost:8085/api/dummy-vote', requestData, {
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


export default DummyDataTest;