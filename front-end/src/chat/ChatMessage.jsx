import React from "react";
import Styles from './ChatMessage.module.css';

const ChatMessage = ({ message, time, sender }) => {
    return (
      <div className={Styles.chat_message_container}>
        <div className={Styles.chat_message_header}>{time} / {sender} :</div>
        <div className={Styles.chat_message_content}>{message}</div> 
      </div>
    );
};

export default ChatMessage;