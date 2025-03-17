import React from 'react';
import io from "socket.io-clinet";
// import {socket_url} from "config";

export const socket = io('localhost:8085');
export const SocketContext = React.createContext();
