import React from "react";
import Styles from "./ChatMember.module.css";

const ChatMember = ({memberImg, memberNick, memberState}) => {

    return (
        <div className={Styles.chat_member}>
            <img className={Styles.chat_member_img} src={memberImg} onError={(e)=>{e.target.src="/public/defaultProfileImg.png";}}/>
            <div className={Styles.chat_member_info}>
                <div className={Styles.chat_member_info_upper}>
                    <div className={Styles.chat_member_nick}>
                        {memberNick}
                    </div>
                    <div className={Styles.chat_member_status}>
                        {memberState}
                    </div>                    
                </div>
            </div>
        </div>
    );
};

export default ChatMember;