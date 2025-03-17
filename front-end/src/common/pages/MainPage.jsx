import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import styles from './MainPage.module.css';
import Dashboard from '../Dashboard';
import useFCMToken from '../useFCMToken';
import DummyDataTest from '../DummyDataTest';

function MainPage() {
  
  return (
    
      <div className={styles.layout}>

        <div className={styles.layout_header}>

        </div>

        <div className={styles.layout_content}>
          
          <Dashboard />
          
        </div>
        
        <div className={styles.layout_footer}>

        </div>

      </div>

  );
};

export default MainPage;