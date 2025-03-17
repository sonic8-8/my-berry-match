import React from 'react';
import './Team.css';
import defaultUserImg from './User.png'; 

const Team = ({ teamLabel, users, teamLogo, onButtonClick, currentUserId, allUsersReady }) => (
    <div className="team">
        <div className="glitch-effect">
            <img 
                src={teamLogo} 
                alt={`${teamLabel} Logo`} 
                className="team-logo" 
            />
        </div>
        <div className="team-label">{teamLabel}</div>

        <div className="user-list">
            {users.length > 0 ? (
                users.map((user) => (
                    <div className="user" key={user.id}>
                        <img 
                            src={user.profileImageUrl || defaultUserImg} 
                            alt={user.nickname} 
                            onError={(e) => e.target.src = defaultUserImg}  
                        />
                        <div className="user-info">
                            <div className="nickname">{user.nickname}</div>
                            <div className={`status ${user.isReady ? 'ready' : 'waiting'}`}>
                                {user.isReady ? '🟢 Ready' : ' 🟡 Not Ready'}
                            </div>
                        </div>
                        {/* 현재 유저만 나가기 버튼을 표시, 모두가 준비 상태일 때는 버튼을 숨김 */}
                        {!allUsersReady && user.id === currentUserId && teamLabel === "B팀" && (
                            <button className="leave-button" onClick={() => onButtonClick(user.id, user.nickname)}>
                                매치 나가기
                            </button>
                        )}
                        {/* A팀에는 숨겨진 버튼 추가 */}
                        {teamLabel === "A팀" && (
                            <button className="leave-button hidden">매치 나가기</button>
                        )}
                    </div>
                ))
            ) : (
                <div>No users available</div>
            )}
        </div>
    </div>
);

export default Team;
