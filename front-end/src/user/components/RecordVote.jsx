import axios from 'axios';
import React, { useEffect, useState } from 'react'
import Styles from "./RecordVote.module.css"

const RecordVote = ({gameId, userId, setReadyVote, closeVoteModal}) => {

    const [recordList, setRecordList] = useState([]);
    const [selectedRecord, setSelectedRecord] = useState(null);

    useEffect(() => {
        axios.get(`http://localhost:8085/api/record/${gameId}`)
        .then(res =>{
            setRecordList(res.data)
        })
        .catch(error=>{
            console.log("경기 결과 기록 호출 오류");
        })
    
    }, [gameId]);

    const radioChange = (e) =>{
        setSelectedRecord(e.target.value)
    };

    // 투표 제출 함수
    const submitVote = (e) => {
        e.preventDefault();

        if (!selectedRecord) {
            alert('투표할 항목을 선택해 주세요.');
            return;
        }

        axios.post('http://localhost:8085/api/submit-vote', 
            {
                gameId: gameId,
                userId: userId,
                gameResultTemp:selectedRecord
            }

        ) // 'vote'는 input 상태에서 가져오도록 추가 필요
        .then(response => {
            console.log('투표 제출 완료:', response.data);
            setReadyVote(false); // 제출 후 상태 업데이트
            closeVoteModal();
        })
        .catch(error => 
            console.error('Error submitting vote:', error));
    };

  return (
    <div className={Styles.record_vote_container}>
        <h3>A팀 : B팀</h3>
        <div className={Styles.record_vote_items}>
            {recordList.map((record) => (
                <input
                    key={record.id}
                    type="radio"
                    name="vote"
                    value={record.id}
                    onChange={radioChange}
                >
                {record.resultTeamA}:{record.resultTeamB}
                </input>
            ))}
        </div>
        <div className={Styles.record_vote_btn}>
            <button onClick={submitVote}>투표 제출</button>
        </div>
    </div>
  )
};

export default RecordVote;