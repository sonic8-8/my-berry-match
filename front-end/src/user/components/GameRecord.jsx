import axios from 'axios';
import React, { useState } from 'react';
import Styles from "./GameRecord.module.css";

const GameRecord = (gameId, userId, setReadyInput, closeRecordModal) => {

    const [resultTeamA, setResultTeamA] = useState("")
    const [resultTeamB, setResultTeamB] = useState("")

    // 경기 기록 제출 함수
    const submitRecord = (e) => {
        e.preventDefault();
        axios.post('http://localhost:8085/api/submit-record', 
            {
                gameId: gameId,
                userId: userId,
                resultTeamA: resultTeamA,
                resultTeamB: resultTeamB
            }
        )
            .then(response => {
                console.log('경기 기록 제출 완료:', response.data);
                setReadyInput(false); // 제출 후 상태 업데이트
                closeRecordModal();
            })
            .catch(error => console.error('경기 기록 제출 오류:', error));
    };


    return (
        <div className={Styles.game_record_container}>
            <label>
                A팀
                <input type="number" onChange={(e) => {
                    setResultTeamA(e.target.value)
                }}/>
            </label>
            <label>
                B팀
                <input type="number" onChange={(e) => {
                    setResultTeamB(e.target.value)
                }}/>
            </label>
            <button onClick={submitRecord}>기록 제출</button>
        </div>
  )
}

export default GameRecord