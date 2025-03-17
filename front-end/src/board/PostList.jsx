import React from 'react'
import styles from './PostList.module.css'
import { useEffect,useState } from 'react';
import PostDetail from './PostDetail';
import { setModalSwitch, setLikeSwitch} from '../store';
import { useDispatch, useSelector } from 'react-redux';
import axios from 'axios';
import Cookies from 'js-cookie';
import Modal from 'react-modal';

// url 파라미터 가지고올 수 있음 (useParams)
import { useNavigate, useParams } from 'react-router-dom';

function PostList() {


  const accessToken = Cookies.get("accessToken");

  let nav = useNavigate();

  const { currentPage: currentUrlParam } = useParams(); // 기존 URL에서 가져온 값
  const [currentPage, setCurrentPage] = useState(currentUrlParam); // 상태로 관리


  const [ postList, setPostList ] = useState([""]);
  const [ totalPages, setTotalPages] = useState(0);
  const [ postDetailData, setPostDetailData] = useState([""]);
  const [ sortByLikes, setSortByLikes ] = useState(false);
  
  const sortData = {
    "sortByLikes" : sortByLikes,
  }

  console.log("현재 몇번째 페이지? : ", currentPage);


   /**
   * 하이라이트 페이지를 들어왔을 때 DB에 저장되어 있는 게시글들 보여주기
   */
  let data;
  useEffect(() => {
    console.log("인기순으로 버튼 클릭 후 axios 통신");
    axios.post(`http://localhost:8085/api/postpage/${currentPage}`, sortData,{
      headers: {
        'Authorization': `Bearer ${accessToken}`
      },
      withCredentials: true
    })
    .then(
      response=>{
        const apiResponse = response.data;
        data = apiResponse.data;
        const message = apiResponse.message;
        const code = apiResponse.code;
        const status = apiResponse.status;
        console.log("게시글 및 총 페이지 수 값 : ", data);
        console.log(apiResponse);
        setPostList(data.postDataList);
        setTotalPages(data.totalPages);
        nav(`/board/${currentPage}`);
      })
    .catch(
      error=>{
        console.log("오류남 -> ", error);
      }
    )
  }, [currentPage, sortByLikes]);    


  // store.js로 요청 보내주는 함수
  let dispatch = useDispatch();

  // store에 있는 state 가지고 오기
  let storeState = useSelector((state) => {return state});

  // 꺼내서 사용하기
  let modalSwitch = storeState.modalSwitch;
  let likeSwitch = storeState.likeSwitch;

 



/**
 * 게시글 클릭할 시 state 로직 실행
 */
function handleModalSwitch(i) {

  console.log("현재 스위치 상태 : ", modalSwitch);
  
  // modarSwitch 값 true 바꿔주기
  dispatch(setModalSwitch());
  setPostDetailData(postList[i]);

}

function handleLikeClick(){

  dispatch(setLikeSwitch());

}

/**
 * totalPages의 값 만큼 버튼 생성
 */
function createButton(totalPages) {
  // 빈 배열 생성
  const array = [];

  // 입력된 숫자만큼 반복하며 배열에 요소 추가
  for (let i = 1; i <= totalPages; i++) {
    array.push(i);
  }


  return array;
}

function handlePageChange(event) {
setCurrentPage(parseInt(event.target.value))
console.log("내가 클릭한 페이지 : ", parseInt(event.target.value));
}

function handleSortByLikes() {
  console.log("현재 인기순 값 : ", sortByLikes);
  if(sortByLikes){
    setSortByLikes(false);
  }else{
    setSortByLikes(true);
  }

}

  return (
    <div className={styles.postpage_container}>
      
      <button onClick={ handleSortByLikes }>인기순</button>
      <div className={styles.posts_container}>
        {
         postList.map(function(_, i){
            return (
                <div className={styles.post_container} key={i} onClick={ () => handleModalSwitch(i) }>
                  <div className={styles.post_detail_container}>
                    <img src={postList[i].thumbnailUrl} className={styles.post_thumbnail}></img>
                    <div className={styles.post_title}>{postList[i].title}</div>
                    <div className={styles.post_date}>{postList[i].createAt}</div>
                    <div className={styles.post_writer}>{postList[i].nickname}</div>
                  </div>
                </div>
              )
          })
        }
      </div>

      {
        modalSwitch ? (
          <Modal isOpen={true} ariaHideApp={false} onRequestClose={() => 
            dispatch(setModalSwitch())
          }
          overlayClassName={styles.modal_overlay}
          className={styles.modal}
          >
            <PostDetail postList={postDetailData}></PostDetail>
          </Modal>
        ) : null
      }
      
      <div className={styles.page_change_button_container}>
        {
          createButton(totalPages).map((data)=>(
            <button className={styles.page_change_button} onClick={ handlePageChange } key={data} value={data}>
              {data}
            </button>
          ))
        }
      </div>

    </div>
  );
}

export default PostList