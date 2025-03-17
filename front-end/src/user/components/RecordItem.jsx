import React, { useEffect, useState } from 'react';
import Styles from "./RecordItem.module.css";
import Modal from '../../Modal';
import axios from 'axios';
import RecordVote from './RecordVote';
import GameRecord from './GameRecord';
import { Link } from 'react-router-dom';


const RecordItem = ({userId, gameId, gameTitle, resultTeamA, resultTeamB}) => {

    const [modalOpen,setModalOpen] = useState(false);
    const [readyInput, setReadyInput] = useState(true);
    const [readyVote, setReadyVote] = useState(true);
    const [readyPost,setReadyPost] = useState(true);

    const [isRecordModalOpen, setRecordModalOpen] = useState(false);
    const [isVoteModalOpen, setVoteModalOpen] = useState(false);

    const openRecordModal = () => setRecordModalOpen(true);
    const closeRecordModal = () => setRecordModalOpen(false);

    const openVoteModal = () => setVoteModalOpen(true);
    const closeVoteModal = () => setVoteModalOpen(false);

    useEffect(() => {
        // 경기 종료 상태
        axios.get(`http://localhost:8085/api/check-ready-input/${gameId}`)
        .then(response => {
            if (response.data.GameStatus === "COMPLETED") {
                setReadyInput(false);
            }
        })
        .catch(error => console.error('Error checking input readiness:', error));
    
        // 경기 투표 상태 확인을 위한 API 요청
        axios.get(`http://localhost:8085/api/check-ready-vote/${gameId}`)
        .then(response => {
            if (response.data.GameRecordTempStatus  === "BEFORE_RECORD") {
                setReadyVote(false);
            }
        })
        .catch(error => console.error('Error checking vote readiness:', error));
        
        // 게시물 작성 상태 확인을 위한 API 요청
        axios.get(`http://localhost:8085/api/check-ready-post/${gameId}`)
        .then(response => {
            if (response.data.GameStatus === "RECORDING_COMPLETED") {
                setReadyPost(false);
            } 
        })
        .catch(error => console.error('Error checking post readiness:', error));

    }, []); 

    const inputRecord = () => {
        openRecordModal();
    };

    const voteRecord = () => {
        openVoteModal();
    };

    const postHilight = () => {
        <Link to="/rank" >랭킹</Link>

    };


    return (
    <div className={Styles.record_container}>
        <div className={Styles.game_info}>
            <div className={Styles.game_date}>
                {gameTitle}이게 언제 날짜일까요
            </div>
            <div className={Styles.game_record}>
                `${resultTeamA} : ${resultTeamB}`
            </div>
        </div>
        <div className={Styles.record_btns}>
            <button className={Styles.record_input} onClick={inputRecord} disabled={readyInput}>기록입력</button>
            <button className={Styles.record_votes} onClick={voteRecord} disabled={readyVote}>기록투표</button>
            <button className={Styles.record_post} onClick={postHilight} disabled={readyPost}>게시물작성</button>
        </div>

        <Modal 
            isOpen={isRecordModalOpen} 
            onClose={closeRecordModal} 
            title="경기 기록" 
            >
            <GameRecord 
                gameId={gameId}
                userId={userId}
                setReadyInput={setReadyInput}
                closeRecordModal={closeRecordModal} 
            />
        </Modal>

        <Modal 
            isOpen={isVoteModalOpen} 
            onClose={closeVoteModal} 
            title="기록 투표" 
            >
            <RecordVote 
                gameId={gameId} 
                userId={userId}
                setReadyVote={setReadyVote}
                closeVoteModal={closeVoteModal}
            />
        </Modal>

    </div>
  )
}

export default RecordItem