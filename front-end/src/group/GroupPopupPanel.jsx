import React, { useState, useEffect } from 'react';
import styles from './GroupPopupPanel.module.css';
import Cookies from 'js-cookie';
import axios from 'axios';
import {jwtDecode} from 'jwt-decode';  // 수정된 부분

const PopupPanel = () => {
  const [isOpen, setIsOpen] = useState(false);
  const [groupCode, setGroupCode] = useState('');
  const [groupCreated, setGroupCreated] = useState(false);
  const [currentGroup, setCurrentGroup] = useState(null);
  const [members, setMembers] = useState([]);

  const accessToken = Cookies.get('accessToken');
  let id = null;
  if (accessToken) {
    const decodedToken = jwtDecode(accessToken);
    id = Number(decodedToken.id);
  }

  const togglePopup = () => {
    setIsOpen(!isOpen);
  };

  const handleGroupCodeChange = (e) => {
    setGroupCode(e.target.value);
  };

  const fetchGroupInfo = async () => {
    try {
      const response = await axios.get(`http://localhost:8085/api/group/user/${id}`, {
        headers: {
          'Authorization': `Bearer ${accessToken}`,
          'Content-Type': 'application/json',
        },
      });
      if (response.status === 200 && response.data.data) {
        console.log(response.data.data)
        setGroupCreated(true);
        setCurrentGroup(response.data.data);
        setMembers(response.data.data.members);
      } else {
        setGroupCreated(false);
      }
    } catch (error) {
      console.error('그룹 정보 조회 에러:', error);
    }
  };

  useEffect(() => {
    const fetchData = async () => {
      if (isOpen && id && accessToken) {
        await fetchGroupInfo();
      }
    };
    fetchData();
  }, [isOpen, id, accessToken]);

  useEffect(() => {
    if (groupCreated) {
      const intervalId = setInterval(async () => {
        try {
          const response = await axios.get(`http://localhost:8085/api/group/user/${id}/live`, {
            headers: {
              'Authorization': `Bearer ${accessToken}`,
              'Content-Type': 'application/json',
            },
          });
          if (response.status === 200 && response.data.data) {
            console.log(response.data.data)
            setGroupCreated(true);
            setCurrentGroup(response.data.data);
            setMembers(response.data.data.members);
          } else {
            setGroupCreated(false);
          }
        } catch (error) {
          console.error('멤버 목록 업데이트 에러:', error);
        }
      }, 5000);

      return () => clearInterval(intervalId);
    }
  }, [groupCreated, id, accessToken]);

  const joinGroup = async (e) => {
    e.preventDefault();
    if (!groupCode) {
      console.error("그룹 코드가 입력되지 않았습니다!");
      return;
    }

    const requestData = {
      id: id,
      groupCode: groupCode
    };

    try {
      const response = await axios.post('http://localhost:8085/api/joingroup', requestData, {
        headers: {
          'Authorization': `Bearer ${accessToken}`,
          'Content-Type': 'application/json',
        },
      });
      if (response.status === 200) {
        setGroupCreated(true);
        setCurrentGroup(response.data.data);
        setMembers(response.data.data.members);
      }
    } catch (error) {
      console.error('그룹 참가 에러:', error);
    }
  };

  const createGroup = async (e) => {
    e.preventDefault();
    const requestData = {
      id: id,
    };

    try {
      const response = await axios.post('http://localhost:8085/api/creategroup', requestData, {
        headers: {
          'Authorization': `Bearer ${accessToken}`,
          'Content-Type': 'application/json',
        },
        withCredentials: true,
      });

      if (response.status === 200) {
        setGroupCreated(true);
        setCurrentGroup(response.data.data);
        setMembers(response.data.data.members);
      }
    } catch (error) {
      console.error('그룹 생성 에러:', error);
    }
  };



//그룹 나가기 
const GroupLeave = async (e) => {
  e.preventDefault();
  const requestData = {
    id: id,
  };

  try {
    const response = await axios.post(`http://localhost:8085/api/${id}/groupleave`, requestData, {
      headers: {
        'Authorization': `Bearer ${accessToken}`,
        'Content-Type': 'application/json',
      },
      withCredentials: true,
    });

    if (response.status === 200) {
      // 그룹 나가기 성공 후, 그룹 참가/생성 UI로 돌아감
      setGroupCreated(false);
      setCurrentGroup(null); // 현재 그룹 정보를 초기화
      setMembers([]); // 멤버 리스트 초기화
    }
  } catch (error) {
    console.error('그룹 나가기 에러:', error);
  }
};


















 
    return (
        <div>
          <div onClick={togglePopup} className={styles.openButton}>
            그룹찾기/생성
          </div>
      
          {/* 창이 열리면 패널이 나타남 */}
          <div className={`${styles.popupPanel} ${isOpen ? styles.open : ''}`}>
            <div className={styles.panelHeader}>
              <h2>그룹 찾기 / 생성</h2>
              <button onClick={togglePopup} className={styles.closeButton}>
                닫기
              </button>
            </div>
      
            <div className={styles.panelContent}>
              {!groupCreated ? (
                <>
                  {/* 그룹 참가 섹션 */}
                  <div className={styles.joinSection}>
                    <h3>그룹에 참가</h3>
                    <form onSubmit={joinGroup}>
                      <input
                        type="text"
                        placeholder="그룹 코드를 입력하세요"
                        value={groupCode}
                        onChange={handleGroupCodeChange}
                        className={styles.input}
                      />
                      <button type="submit" className={styles.joinButton}>
                        그룹 참가
                      </button>
                    </form>
                  </div>
      
                  <hr className={styles.divider} />
      
                  {/* 그룹 생성 섹션 */}
                  <div className={styles.createSection}>
                    <h3>새 그룹 생성</h3>
                    <button onClick={createGroup} className={styles.createButton}>
                      그룹 생성
                    </button>
                  </div>
                </>
              ) : (
                <div className={styles.groupDetails}>
                  {/* 그룹 코드 표시 */}
                  <div className={styles.groupCode}>
                    <h3>그룹 코드: {currentGroup.groupCode}</h3>
                  </div>
      
                  {/* 그룹 멤버 정보 표시 */}
                  <div className={styles.groupMembers}>
                    <h3>현재 그룹 멤버</h3>
                    <ul>
                      {members && members.length > 0 ? (
                        members.map((member) => (
                          <li key={member.id} className={styles.memberItem}>
                            {/* 프로필 이미지 */}
                            <img 
                              src={member.profileImageUrl || 'default_profile_image_url'} 
                              alt={`${member.nickname}의 프로필 이미지`} 
                              className={styles.profileImage} 
                            />
                            {/* 닉네임 */}
                            <span className={styles.nickname}>{member.nickname}</span>
                          </li>
                        ))
                      ) : (
                        <p>그룹에 멤버가 없습니다.</p>
                      )}
                    </ul>
                    <button 
                    onClick={GroupLeave}
                    >그룹 나가기</button>
                  </div>
                </div>
              )}
            </div>
          </div>
        </div>
      );
    }
export default PopupPanel;
