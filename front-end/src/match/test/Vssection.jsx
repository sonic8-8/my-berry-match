import React, { useState, useEffect, useRef } from 'react';
import './VSSection.css';
import Cookies from 'js-cookie';
import axios from 'axios';
import { jwtDecode } from 'jwt-decode';
import { useNavigate } from 'react-router-dom';

const VSSection = ({ socket, MatchId, nickname, onReadyStateChange, allUsersReady, currentUserReady }) => {
    const [messages, setMessages] = useState([]);
    const [inputValue, setInputValue] = useState('');
    const [headerClicked, setHeaderClicked] = useState(currentUserReady);  // 초기 준비 상태 반영
    const accessToken = Cookies.get('accessToken');
    const chatboxRef = useRef(null); // chatbox에 대한 참조 생성
    const navigate = useNavigate();

    let userId = null;
    if (accessToken) {
        const decodedToken = jwtDecode(accessToken);
        userId = Number(decodedToken.id);
    }

    // 과거 메시지 불러오기
    useEffect(() => {
        if (MatchId) {
            axios.get(`http://localhost:8085/api/chat/${MatchId}`, {
                headers: {
                    'Authorization': `Bearer ${accessToken}`,
                    'Content-Type': 'application/json'
                },
                withCredentials: true,
            })
            .then(response => {
                const pastMessages = response.data.data || [];
                const formattedMessages = pastMessages.map(msg => ({
                    sender: msg.id === userId ? 'me' : 'other',  // 내 메시지와 상대방 메시지 구분
                    text: msg.message[0],
                    nickname: msg.nickname
                }));

                setMessages(formattedMessages);
            })
            .catch(error => {
                console.error('Error fetching past messages:', error);
            });
        }
    }, [MatchId, accessToken, userId]);

    // 스크롤을 최하단으로 이동
    useEffect(() => {
        if (chatboxRef.current) {
            chatboxRef.current.scrollTop = chatboxRef.current.scrollHeight;
        }
    }, [messages]);

    // 실시간으로 메시지 수신
    useEffect(() => {
        if (socket) {
            const handleMessage = (data) => {
                // 새로운 메시지를 채팅에 추가
                setMessages(prevMessages => [...prevMessages, data]);
            };

            socket.on('message', handleMessage);

            return () => {
                socket.off('message', handleMessage);
            };
        }
    }, [socket]);

    // 페이지가 처음 로드될 때 allUsersReady와 currentUserReady에 따라 버튼 상태 변경
    useEffect(() => {
        if (allUsersReady || currentUserReady) {
            setHeaderClicked(true);  // 모든 유저가 준비 상태일 때 버튼을 '게임 중'으로 변경
        }
    }, [allUsersReady, currentUserReady]);

    const handleSendMessage = () => {
        if (inputValue.trim() !== '') {
            const messageData = {
                text: inputValue,
                nickname: nickname,
                id: userId
            };

            // 메시지 리스트에 추가 (자기 메시지)
            setMessages(prevMessages => [...prevMessages, { sender: 'me', text: inputValue, nickname }]);
            setInputValue('');

            // 서버로 메시지 전송
            if (socket) {
                socket.emit('sendMessage', messageData);
            }
        }
    };

    const handleInputChange = (e) => {
        setInputValue(e.target.value);
    };

    const handleKeyPress = (e) => {
        if (e.key === 'Enter') {
            handleSendMessage();
        }
    };

    const handleHeaderClick = () => {
        if (!allUsersReady && socket) {  // 모든 유저가 준비되었을 때는 클릭할 수 없게 처리
            const newIsReady = !headerClicked;

            // 준비 상태에 따라 서버에 알림
            socket.emit(newIsReady ? 'ready' : 'notReady', { id: userId, nickname, matchId: MatchId });
            console.log(`Sent ${newIsReady ? 'ready' : 'notReady'} event: ${nickname} is ${newIsReady ? 'ready' : 'not ready'}`);

            // 부모 컴포넌트로 준비 상태 알림
            onReadyStateChange(nickname, newIsReady);
            setHeaderClicked(newIsReady);  // 클릭 상태 업데이트
        }
    };

    const handleEndGameClick = () => {
        // console.log("경기 종료 투표");
        // socket.emit('endGameVote', { matchId: MatchId, id: userId, nickname });

        const accessToken = Cookies.get('accessToken');
        const decodedToken = jwtDecode(accessToken);
        const id = decodedToken.id;

        console.log(id);
        
        axios.post('http://localhost:8085/api/boom', { 'id': id } ,{
                headers: {
                    'Authorization': `Bearer ${accessToken}`,
                    'Content-Type': 'application/json'
                },
                withCredentials: true
            }).then(response => {
    
                const apiResponse = response.data;
                const code = apiResponse.code;
                const status = apiResponse.status;
                const data = apiResponse.data;
                const message = apiResponse.message;
        
 
                console.log(apiResponse);
                console.log(data);
                console.log('박살 완료', data);

                navigate('/')
                

              }).catch(error => {
                  console.error('박살 중 오류 발생' + error);
              });

    



    };

    return (
        <div className="vs">
            {allUsersReady ? (
                <div>
                    {/* 게임 중 상태 표시 */}
                    <button className="vs-header game-in-progress" disabled>
                        게임 중
                    </button>
                    <button className="end-game-button" onClick={handleEndGameClick}>
                        경기 종료
                    </button>
                </div>
            ) : (
                <button
                    className={`vs-header ${headerClicked ? 'clicked' : ''}`}
                    onClick={handleHeaderClick}
                    aria-label="Ready Button"
                    disabled={allUsersReady}  // 모든 유저가 준비되었을 때 버튼 비활성화
                >
                    {headerClicked ? 'Ready' : 'Not Ready'}
                </button>
            )}

            {/* 채팅 메시지 */}
            <div className="chatbox" ref={chatboxRef}>
                {messages.map((msg, index) => (
                    <div
                        key={index}
                        className={`chat-message-container ${msg.sender === 'me' ? 'me-container' : 'other-container'}`}
                    >
                        <div className={`chat-nickname ${msg.sender}`}>
                            {msg.nickname}
                        </div>
                        <div className={`chat-message ${msg.sender}`}>
                            {msg.text}
                        </div>
                    </div>
                ))}
            </div>

            {/* 채팅 입력 부분 */}
            <div className="input-container">
                <input
                    type="text"
                    placeholder="메시지를 입력하세요..."
                    value={inputValue}
                    onChange={handleInputChange}
                    onKeyPress={handleKeyPress}
                    className="chatbox-input"
                />
                <button className="send-button" onClick={handleSendMessage}>
                    전송
                </button>
            </div>
        </div>
    );
};

export default VSSection;
