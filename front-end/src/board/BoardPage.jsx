import styles from './BoardPage.module.css';
import BoardDashboard from './components/BoardDashboard';

function BoardPage() {
    return (


      <div className={styles.layout}>
          
        <div className={styles.layout_header}>
          

        </div>

        <div className={styles.layout_content}>

          <BoardDashboard/>

        </div>
        
        <div className={styles.layout_footer}>


        </div>

      </div>
      );
}

export default BoardPage;