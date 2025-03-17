import React, { useEffect, useState, useRef } from "react";
import io from "socket.io-client";
import axios from "axios";
import Cookies from 'js-cookie';
import { jwtDecode } from 'jwt-decode'; 
import ChatMessage from "./ChatMessage";
import ChatMembersList from "./ChatMembersList";
import Styles from "./ChatRoom.module.css";

const ChatRoom = () => {
    const socket = useRef(null);

    const [gameTitle, setGameTitle] = useState("");
    const [currentRoom, setCurrentRoom] = useState("");
    const [userInputMsg, setUserInputMsg] = useState("");
    const [gameReady, setGameReady] = useState(false);
    const [chatMessages, setChatMessages] = useState([]);
    const [readyDisabled, setReadyDisabled] = useState(false);
    const [endDisabled, setEndDisabled] = useState(true);
    const [teamAUsers, setTeamAUsers] = useState([]);
    const [teamBUsers, setTeamBUsers] = useState([]);

    const accessToken = Cookies.get('accessToken');
    let userId = null;
    if (accessToken) {
        const decodedToken = jwtDecode(accessToken);
        userId = Number(decodedToken.id);
    }

    useEffect(() => {
        // 서버와의 소켓 연결
        socket.current = io.connect("http://localhost:8085/ws/chatRoom", {
            cors: { origin: "*" },
        });

        // 매칭된 경기 일자 불러오기 및 방 ID 설정
        socket.current.on("create new room", (res) => {
            setGameTitle(res.data.date);
            setCurrentRoom(res.roomId);

            // 사용자가 방에 입장하는 이벤트
            socket.current.emit("join Room", {
                roomId: res.roomId,
                userId: userId,
            });
        });

        // 유저 정보 불러오기
        socket.current.on("load user info", (res) => {
            setTeamAUsers(res.members.teamA);
            setTeamBUsers(res.members.teamB);
        });

        // 룸 정보 불러오기
        socket.current.on("room Joined", (res) => {
            setCurrentRoom(res.roomId);
        });

        // 과거 채팅 내역 불러오기
        socket.current.on("load all msgs", (res) => {
            setChatMessages(res);
        });

        // 실시간 채팅 내역 띄우기
        socket.current.on("receive msg", (res) => {
            setChatMessages((prevChatMessages) => [...prevChatMessages, res]);
        });

        // 모든 유저가 준비 완료 시 준비버튼 비활성화
        socket.current.on("all users ready", () => {
            setReadyDisabled(true);
        });

        // 경기 시간이 되면 종료 버튼 활성화
        socket.current.on("game time", () => {
            setEndDisabled(false);
        });

        // 방 폭파 이벤트 처리
        socket.current.on("room disbanded", () => {
            alert("모든 사용자가 준비되지 않아 방이 폭파되었습니다.");
            setReadyDisabled(true);
            setEndDisabled(true);
        });

        // 컴포넌트 언마운트 시 소켓 연결 해제
        return () => {
            socket.current.disconnect();
        };
    }, [userId, currentRoom]); // currentRoom이 변경될 때마다 useEffect 재실행

    const sendReadyStatus = async (isReady) => {
        try {
            const response = await axios.post("http://localhost:8085/api/chatRoom/ready", {
                user: userId,
                isReady,
            });
            console.log("유저 준비 상태 전달 성공:", response.data);
        } catch (error) {
            console.error("유저 준비 상태 전달 실패:", error);
        }
    };

    const readyGame = (e) => {
        const newReady = !gameReady;
        sendReadyStatus(newReady);
        setGameReady(newReady);
        e.target.value = newReady ? "준비 완료" : "준비 취소";
    };

    const endGame = (e) => {
        axios.post("http://localhost:8085/api/game/end", {
            user: userId,
            chatRoom: currentRoom,
        })
            .then(res => {
                console.log("메인 서버로 경기 종료 확인 및 데이터 처리 요청 완료");
            })
            .catch(error => {
                console.log("메인 서버로 경기 종료 확인 및 데이터 처리 요청 실패");
            });
    };

    const sendMsgHandler = () => {
        if (userInputMsg.trim()) {
            try {
                socket.current.emit("send msg", {
                    user: userId,
                    msg: userInputMsg,
                    time: new Date().toLocaleTimeString(),
                    roomId: currentRoom,
                });
                setUserInputMsg("");
            } catch (error) {
                console.error("메시지 전송 실패:", error);
            }
        }
    };

    const changeMsg = (e) => {
        setUserInputMsg(e.target.value);
    };

    return (
        <div className={Styles.chat_page}>
            <ChatMembersList team={teamAUsers} roomId={currentRoom} />
            <div className={Styles.chat_container}>
                <div className={Styles.chat_header}>
                    <div className={Styles.chat_info}>
                        {gameTitle}
                    </div>
                    <div className={Styles.chat_options}>
                        <button
                            className={Styles.game_ready}
                            onClick={readyGame}
                            disabled={readyDisabled}
                        >
                            준비
                        </button>
                        <button
                            className={Styles.game_end}
                            onClick={endGame}
                            disabled={endDisabled}
                        >
                            경기 종료
                        </button>
                    </div>
                </div>
                <div className={Styles.chat_content}>
                    {chatMessages.map((msgContent, index) => (
                        <ChatMessage
                            key={index}
                            time={msgContent.time}
                            user={msgContent.user}
                            msg={msgContent.msg}
                        />
                    ))}
                </div>
                <div className={Styles.chat_input}>
                    <input
                        type="text"
                        placeholder="메세지를 입력하세요"
                        value={userInputMsg}
                        onChange={changeMsg}
                        onKeyDown={(e) => {
                            if (e.key === "Enter") sendMsgHandler();
                        }}
                    />
                    <button onClick={sendMsgHandler}>전송</button>
                </div>
            </div>
            <ChatMembersList team={teamBUsers} roomId={currentRoom} />
        </div>
    );
};

export default ChatRoom;
