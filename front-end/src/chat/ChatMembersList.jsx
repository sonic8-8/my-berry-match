import React, { useEffect, useState } from "react";
import ChatMember from "./ChatMember";
import Styles from "./ChatMembersList.module.css";
import { io } from "socket.io-client";

const ChatMembersList = ({team, roomId}) => {

    const socket = io.connect(`http://localhost:8085/chat/${roomId}`,{
        cors:{
            origin:"*"
        }
    });
    socket.connect();

    const [membersArr, setMembersArr] = useState(team);

    useEffect(()=>{
        socket.on("user ready on",(id,newState)=>{
            // 해당하는 사용자의 준비 상태가 on으로 바뀜
            setMembersArr(team.map(member => member.id === id? {...member,state:newState}:member))            
        });
        socket.on("user ready off",(id, newState)=>{
            // 해당하는 사용자의 준비 상태가 off으로 바뀜
            setMembersArr(team.map(member => member.id === id? {...member,state:newState}:member))
        });

    });

    return (
        <div className={Styles.chat_members_list}>
            {membersArr.map((membersInfo) => {
                return(
                    <div key={membersInfo.id}>
                        <ChatMember
                            memberImg={membersInfo.img}
                            memberId = {membersInfo.id}
                            memberState = {membersInfo.state}
                        />
                    </div>
                )
            })}
        </div>
    );
};

export default ChatMembersList;