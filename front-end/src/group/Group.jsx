import styles from './Group.module.css';

function Group() {

    return (

        <div className={styles.group_container}>
            <div className={styles.group_user}>
                <img className={styles.group_user_image} />
                <span className={styles.group_user_name}></span>
            </div>

            <div className={styles.group_user}>
                <img className={styles.group_user_image} />
                <span className={styles.group_user_name}></span>
            </div>

            <div className={styles.group_user}>
                <img className={styles.group_user_image} />
                <span className={styles.group_user_name}></span>
            </div>

        </div>
    )
}