import styles from './ProfileEditSubPage.module.css';
import React, { useState, useEffect } from 'react';
import useProfileUpdate from '../useProfileUpdate';
import useUserInfo from '../useUserInfo';
import Modal from 'react-modal';

Modal.setAppElement('#root');

function ProfileEditSubPage() {
    const { userInfo, loading, error } = useUserInfo();
    const {
        profileImage,
        handleProfileImageChange,
        handleUpdate,
        status,
        message,
        data,
        code,
        introduction,
        setIntroduction
    } = useProfileUpdate(userInfo || {});

    const [isModalOpen, setIsModalOpen] = useState(false);
    const [isEditingProfileImage, setIsEditingProfileImage] = useState(false);

    // 모달 열기
    const openModal = () => {
        setIsEditingProfileImage();
        setIsModalOpen(true);
    };

    // 모달 닫기
    const closeModal = () => {
        setIsModalOpen(false);
    };

    if (loading) {
        return <div>로딩중...</div>;
    }

    if (error) {
        return <div>Error: {error.message}</div>;
    }

    return (
        <div className={styles.profileEdit_container}>
            <div className={styles.profileEdit_image_container}>
                <div className={styles.profileEdit_image_title}>프로필 사진</div>
                {
                    profileImage == null ? 
                    '프로필 사진이 없습니다.' : 
                    <img src={userInfo.profileImageUrl} className={styles.profileEdit_image} />
                }
                
            </div>
            <br />
            <div className={styles.profileEdit_introduction_container}>
                <div className={styles.profileEdit_introduction_title}>
                    자기소개 : {userInfo.introduction == '' ? '자기소개가 없습니다.' : userInfo.introduction}
                </div>
                
                {/* 연필 모양 버튼 */}
                <button className={styles.profileEdit_edit_button} onClick={() => openModal(false)}>
                    ✏️
                </button>
            </div>

            {/* 모달 */}
            <Modal
                isOpen={isModalOpen}
                onRequestClose={closeModal}
                className={styles.modal}
                overlayClassName={styles.overlay}
            >
                <div className={styles.modal_profileEdit_title}>프로필 수정</div>
                <div className={styles.modal_profileEdit_image_container}>
                    <input type="file" onChange={handleProfileImageChange} className={styles.profileEdit_image_input} />
                </div>
                    
                <div className={styles.modal_profileEdit_introduction_container}>
                    <input className={styles.profileEdit_introduction_input}
                            type="text"
                            value={introduction}
                            onChange={(e) => setIntroduction(e.target.value)}
                    />
                </div>

                <div className={styles.modal_profileEdit_button_container}>
                    <button className={styles.modal_profileEdit_update_button} onClick={() => { handleUpdate(); closeModal();}}>수정하기</button>
                    <button className={styles.modal_close_button} onClick={closeModal}>닫기</button>
                </div>
            </Modal>
        </div>
    );
}

export default ProfileEditSubPage;
