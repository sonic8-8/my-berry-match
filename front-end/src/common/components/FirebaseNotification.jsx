import React, { useEffect } from "react";
import useFCMToken from "../useFCMToken";

function FirebaseNotification() {
  const { token, error, isRegistered } = useFCMToken();

  return (
    <div className="App">
      {/* <h1>FCM TEST</h1>
      <h1>React Firebase Push Notification</h1>
      {token && <p>FCM Token: {token}</p>}
      {isRegistered && <p>FCM Token 등록 성공</p>}
      {error && <p style={{ color: 'red' }}>Error: {error}</p>} */}
    </div>
  );
}

export default FirebaseNotification;
