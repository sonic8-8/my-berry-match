import React, { useState } from 'react';
import axios from 'axios';
import { BrowserRouter as Router, Route, Routes, useLocation } from 'react-router-dom';

import styles from './App.module.css';
import VideoUpload from './VideoUpload';
import LoginPage from './auth/pages/LoginPage';
import MainPage from './common/pages/MainPage';
import SignupPage from './auth/pages/SignupPage';
import MyPage from './user/pages/MyPage';
import RankPage from './rank/RankPage';
import GroupCreatePage from './group/GroupCreatePage';
import GroupSearchPage from './group/GroupSearchPage';
import BoardPage from './board/BoardPage';
import LogoutPage from './auth/pages/LogoutPage'
import PrivateRoute from './auth/components/PrivateRoute';
import TokenPage from './auth/pages/TokenPage';
import BackgroundMusic from './common/components/BackgroundMusic';
import MatchPage from './match/pages/MatchPage';
import MatchLobbyPage from './match/pages/MatchLobbyPage';
import ProfileEditSubPage from './user/pages/ProfileEditSubPage';
import MatchResultsSubPage from './user/pages/MatchResultsSubPage';
import AccountDeletionSubPage from './user/pages/AccountDeletionSubPage';
import MatchComparison from './match/test/MatchComparison';
import Map from './common/Map';

import PostList from './board/PostList';
import PostWritePage from './board/pages/PostWritePage';
import MyPostList from './board/MyPostList.jsx';
import PostEdit from './board/PostEdit.jsx';
import { Navigate } from 'react-router-dom';
import DummyDataTest from './common/DummyDataTest';


function App() {

  const location = useLocation();
  const showBackground = !['/login', '/signup', '/token', '/logout'].includes(location.pathname);

  return (
    <div className={styles.layout}>
      {/* Conditional Background Video */}
      {showBackground && (
        <video autoPlay muted loop className={styles.backgroundVideo}>
          <source src="https://thank-you-berrymatch-bucket-0.s3.ap-northeast-2.amazonaws.com/design/main_background_video.mp4" type="video/mp4" />
        </video>
      )}
      
      {/* Conditional Background Music */}
      {showBackground && <BackgroundMusic className={styles.backgroundMusic} />}


      {/* 더미 데이터 테스트용 */}
      <div className={styles.dummyDataTest}>
        <DummyDataTest/>
      </div>           
      
      
      <Routes>
        <Route path="/" element={<PrivateRoute><MainPage /></PrivateRoute>} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/logout" element={<LogoutPage />} />
        <Route path="/token" element={<TokenPage/>} />
        <Route path="/signup" element={<SignupPage />} />
        <Route path="/rank" element={<PrivateRoute><RankPage /></PrivateRoute>} />
        <Route path="/mypage" element={<PrivateRoute><MyPage /></PrivateRoute>} >
          <Route path="profile-edit" element={<PrivateRoute><ProfileEditSubPage /></PrivateRoute>} />
          <Route path="match-results" element={<PrivateRoute><MatchResultsSubPage /></PrivateRoute>} />
          <Route path="account-deletion" element={<PrivateRoute><AccountDeletionSubPage /></PrivateRoute>} />
        </Route>
        <Route path="/match" element={<PrivateRoute><MatchPage /></PrivateRoute>} />
        <Route path="/match/lobby" element={<PrivateRoute><MatchComparison /></PrivateRoute>} />

        <Route path="/board" element={<PrivateRoute><BoardPage /></PrivateRoute>}>
          <Route path="" element={<Navigate to="/board/1" replace />} />
          <Route path=':currentPage' element={<PrivateRoute><PostList/></PrivateRoute>} />
          <Route path='post/write' element={<PrivateRoute><PostWritePage/></PrivateRoute>} />
          <Route path='mypost' element={<PrivateRoute><MyPostList/></PrivateRoute>}>
            <Route path="" element={<Navigate to="/1" replace />} />
            <Route path=':currentPage' element={<PrivateRoute><PostList/></PrivateRoute>} />
          </Route>
          <Route path="post/edit" element={<PrivateRoute><PostEdit/></PrivateRoute>}></Route>
        </Route>

        <Route path="/group/create" element={<PrivateRoute><GroupCreatePage /></PrivateRoute>} />
        <Route path="/group/search" element={<PrivateRoute><GroupSearchPage /></PrivateRoute>} />
        <Route path="/alert" element={<PrivateRoute><MainPage /></PrivateRoute>} />
        <Route path="/map" element={<PrivateRoute><Map /></PrivateRoute>} />
      </Routes>
    </div>
  );
}

export default function AppWrapper() {
  return (
      <Router>
        <App />
      </Router>
  );
}