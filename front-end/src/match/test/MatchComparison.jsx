import React, { useEffect, useState } from 'react';
import Team from './Team';
import VSSection from './Vssection';
import './MatchPage.css';
import Home from './Home.png';
import Away from './Away.png';
import io from 'socket.io-client';
import Cookies from 'js-cookie';
import { jwtDecode } from 'jwt-decode';
import axios from 'axios';

const MatchComparison = () => {
    const [teamAUsers, setTeamAUsers] = useState([]);
    const [teamBUsers, setTeamBUsers] = useState([]);
    const [allUsersReady, setAllUsersReady] = useState(false);  // 모두 준비 완료 여부 상태 추가
    const [matchId, setMatchId] = useState(null);
    const [nickname, setNickname] = useState('');
    const [socket, setSocket] = useState(null);
    const [currentUserReady, setCurrentUserReady] = useState(false);  // 현재 유저의 준비 상태 추가
    const accessToken = Cookies.get('accessToken');

    let id = null;
    if (accessToken) {
        const decodedToken = jwtDecode(accessToken);
        id = Number(decodedToken.id);
    }

    useEffect(() => {
        const requestData = { id: id };

        axios.post('http://localhost:8085/api/chat/room', requestData, {
            headers: {
                'Authorization': `Bearer ${accessToken}`,
                'Content-Type': 'application/json',
                'Cookie': `accessToken=${accessToken};`
            },
            withCredentials: true,
        })
        .then(response => {
            const MatchId = response.data.data?.matchId;
            if (MatchId) {
                setMatchId(MatchId);
                setNickname(response.data.data.nickname);

                const newSocket = io('http://localhost:9000', {
                    query: { MatchId: MatchId },
                    auth: { token: accessToken }
                });
                setSocket(newSocket);

                // 소켓 이벤트 설정
                newSocket.on('userReadyStatusChanged', ({ nickname, isReady }) => {
                    console.log(`${nickname} is now ${isReady ? 'ready' : 'not ready'}`);
                    handleReadyStateChange(nickname, isReady);
                });

                newSocket.on('userLeft', ({ id, nickname }) => {
                    handleUserLeave(id, nickname);
                });

                newSocket.on('connect', () => {
                    console.log('Connected to Node.js server with matchId:', MatchId);
                });

                newSocket.on('matchUserData', (users) => {
                    const teamA = users.filter(user => user.team === 'A_Team').map(user => ({
                        ...user,
                        isReady: user.readyState === 'READY' // 준비 상태를 boolean으로 변환
                    }));
                    const teamB = users.filter(user => user.team === 'B_Team').map(user => ({
                        ...user,
                        isReady: user.readyState === 'READY' // 준비 상태를 boolean으로 변환
                    }));

                    // 현재 유저가 준비 상태인지 확인하여 상태 설정
                    const currentUser = [...teamA, ...teamB].find(user => user.id === id);
                    setCurrentUserReady(currentUser?.isReady || false);

                    setTeamAUsers(teamA);
                    setTeamBUsers(teamB);

                    // 유저 상태 업데이트 후 준비 상태 확인
                    const allReady = [...teamA, ...teamB].every(user => user.isReady);
                    setAllUsersReady(allReady);
                });

                return () => {
                    newSocket.disconnect();
                    console.log('Socket disconnected');
                };
            } else {
                console.error("매치 ID가 없습니다.");
            }
        })
        .catch(error => {
            console.error('Error sending match request:', error);
        });
    }, [id, accessToken]);

    const handleReadyStateChange = (nickname, isReady) => {
        setTeamAUsers((prevUsers) => {
            const updatedTeamA = prevUsers.map(user =>
                user.nickname === nickname ? { ...user, isReady } : user
            );
            checkAllUsersReady(updatedTeamA, teamBUsers);  // 상태 업데이트 후 체크
            return updatedTeamA;
        });

        setTeamBUsers((prevUsers) => {
            const updatedTeamB = prevUsers.map(user =>
                user.nickname === nickname ? { ...user, isReady } : user
            );
            checkAllUsersReady(teamAUsers, updatedTeamB);  // 상태 업데이트 후 체크
            return updatedTeamB;
        });

        // 현재 유저가 변경된 경우, 해당 유저의 준비 상태 업데이트
        if (nickname === nickname) {
            setCurrentUserReady(isReady);
        }
    };

    const checkAllUsersReady = (teamA, teamB) => {
        const allReady = [...teamA, ...teamB].every(user => user.isReady);
        setAllUsersReady(allReady);  // 모두가 준비 상태일 경우 true로 설정
    };

    const handleUserLeave = (userId, userNickname) => {
        setTeamAUsers(prevUsers => {
            const updatedTeamA = prevUsers.map(user =>
                user.id === userId && user.nickname === userNickname
                    ? { ...user, isReady: false, nickname: 'WAITING' }
                    : user
            );
            checkAllUsersReady(updatedTeamA, teamBUsers);  // 유저 나갈 때 준비 상태 체크
            return updatedTeamA;
        });

        setTeamBUsers(prevUsers => {
            const updatedTeamB = prevUsers.map(user =>
                user.id === userId && user.nickname === userNickname
                    ? { ...user, isReady: false, nickname: 'WAITING' }
                    : user
            );
            checkAllUsersReady(teamAUsers, updatedTeamB);  // 유저 나갈 때 준비 상태 체크
            return updatedTeamB;
        });
    };

    const handleBTeamButtonClick = (userId, userNickname) => {
        socket.emit('leaveuser', { id: userId, nickname: userNickname });
    };

    return (
        <div className="match-container">
            {/* A팀 */}
            <Team 
                teamLabel="A팀" 
                users={teamAUsers} 
                teamLogo={Home} 
                showButton={false} 
                currentUserId={id}
                allUsersReady={allUsersReady}  // 모든 유저 준비 상태 전달
            />

            {/* VSSection으로 소켓과 매치 ID, 닉네임 전달 */}
            {socket && matchId && (
                <VSSection
                    socket={socket}
                    MatchId={matchId}
                    nickname={nickname}
                    onReadyStateChange={handleReadyStateChange}
                    allUsersReady={allUsersReady}  // 모든 유저 준비 상태 전달
                    currentUserReady={currentUserReady}  // 현재 유저의 준비 상태 전달
                />
            )}

            {/* B팀 */}
            <Team 
                teamLabel="B팀" 
                users={teamBUsers} 
                teamLogo={Away} 
                showButton={true} 
                currentUserId={id}
                onButtonClick={handleBTeamButtonClick}
                allUsersReady={allUsersReady}  // 모든 유저 준비 상태 전달
            />

        </div>
    );
};

export default MatchComparison;
