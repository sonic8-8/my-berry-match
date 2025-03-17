import React, { useEffect, useState } from 'react'
import { Outlet } from 'react-router-dom'
import styles from './PostPage.module.css'
import axios from 'axios';

// 컴포넌트
import PostList from './PostList';



function PostPage() {

  // const [ pageId, setPageId ] = useState(1);
  // let { currentPage } = useParams();

  // useEffect(() => {
  //   setPageId(currentPage ? parseInt(currentPage) : 1);
  // }, [currentPage]);
  

  let totalPage = 5;

  /**
   * 나의 하이라이트만 보기
   */
  function myPost(){
    console.log("내 게시물만보기 클릭");
  }

  /**
   * 인기순으로 정렬
   */
  function sortByLikes(){
    console.log("인기순으로 정렬 클릭");
    
  }

  /**
  * 최신순으로 정렬
  */
  function sortByDate(){
    console.log("최신순으로 정렬 클릭");
  }

  return (
    <div className={styles.container}>
      <div className={styles.highlight_container}>

          <div className={styles.button_group}>    
            <button className={styles.mypost_btn} onClick={ myPost }>내 게시물 보기</button>
            <button className={styles.sort_by_likes_btn} onClick={ sortByLikes }>
              인기순
            </button>
            <button className={styles.sort_by_date_btn} onClick={ sortByDate }>
              최신순
            </button>
            
          </div>  

          {/* PostList 컴포넌트 */}
          {/* <PostList></PostList> */}

          {/* <div className={styles.page_change_btton_group}>
            {
              function PaginationButtons({ totalPages }) {
                return (
                  <div>
                    {Array.from({ length: totalPages }, (_, index) => index + 1).map((page) => (
                      <button key={page}>
                        {page}
                      </button>
                    ))}
                  </div>
                );
              }
            }
          </div> */}
         
      </div>
    </div>
  );
}

export default PostPage