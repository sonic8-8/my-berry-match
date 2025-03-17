import styles from './MyPage.module.css';
import UserDashboard from '../components/UserDashboard';

function Mypage() {
    return (


      <div className={styles.layout}>
        
        <div className={styles.layout_header}>
          
        </div>

        <div className={styles.layout_content}>
          
          <UserDashboard/>
          
          
        </div>
        
        <div className={styles.layout_footer}>

        </div>

      </div>
      );
}

export default Mypage;
