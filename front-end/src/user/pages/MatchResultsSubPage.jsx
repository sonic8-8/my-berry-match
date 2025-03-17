import React, { useEffect, useState } from 'react'
import axios from "axios";
import Styles from "./MatchResultsSubPage.module.css"
import RecordItem from '../components/RecordItem'
import { jwtDecode } from 'jwt-decode';
import Cookies from 'js-cookie';

const MatchResultsSubPage = () => {
    const [recordList, setRecordList] = useState([]);
    const accessToken = Cookies.get('accessToken');
  
    let userId = null;
    if (accessToken) {
        const decodedToken = jwtDecode(accessToken);
        userId = Number(decodedToken.id);
    }

    const fetchData = async () => {
        const params = { id: userId };

        axios.get('http://localhost:8085/api/matchusers', {
          params: params,
          headers: {
            'Authorization': `Bearer ${accessToken}`,
            'Content-Type': 'application/json',
          },
          withCredentials: true,
        })
        .then(response => {
          console.log(response.data.data);
        })
        .catch(error => {
          console.error('Error sending match request:', error);
        });
    };


 useEffect(() => {
    // 해당하는 유저의 모든 경기 기록을 가져옴
    axios.get(`http://localhost:8085/api/game/${userId}`)
    .then(res => 
        setRecordList(preList => [...preList,res.data.recordList])
    )
    .catch(error => 
        console.error('Error fetching data:', error)
    );

    fetchData();
 });

  return (
    <div className={Styles.recordPage}>
        <div className={Styles.record}>
            {recordList.map((record)=>(
                <RecordItem
                    key={record.gameId}
                    gameTitle={record.gameTitle}
                    resultTeamA={record.resultTeamA}
                    resultTeamB={record.resultTeamB}
                    resultState={record.resultState}
                />
            ))}
        </div>
    </div>
  )
}

export default MatchResultsSubPage