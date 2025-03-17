import React, { useState } from 'react';
import styles from './MatchPage.module.css';
import MatchDashboard from '../components/MatchDashboard';
import DummyDataTest from '../../common/DummyDataTest';

function MatchPage() {
    return (

        <div className={styles.layout}>
          
            <div className={styles.layout_header}>
            

            </div>
    
            <div className={styles.layout_content}>

                <MatchDashboard/>

            </div>
            
            <div className={styles.layout_footer}>
                

            </div>
  
      </div>

    )
}

export default MatchPage;