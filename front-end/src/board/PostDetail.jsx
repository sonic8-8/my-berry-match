import React, { useEffect, useState } from 'react'
import styles from './PostDetail.module.css';
import Cookies from 'js-cookie';
import { jwtDecode } from 'jwt-decode';
import PostEdit from './PostEdit.jsx';
import { useNavigate } from 'react-router-dom';
import { ClipLoader } from 'react-spinners';

// 외부에서 좋아요 버튼 디자인 갖고옴
import { AiOutlineLike } from "react-icons/ai";
import { AiFillLike } from "react-icons/ai";

// redux
import { useDispatch, useSelector } from 'react-redux';
import { setModalSwitch, setLikeSwitch } from '../store';
import axios from 'axios';


function PostDetail(props) {

  // const [ postList, setPostList ] = useState();
  const [ loading, setLoading ] = useState(false);

  const nav = useNavigate();
  
  const accessToken = Cookies.get("accessToken");

  const decodedToken = jwtDecode(accessToken);
  const id = decodedToken.id;

  const [ postAuthState, setPostAuthState ] = useState(null);
  const [ likeState, setLikeState ] = useState(null);

  // store.js로 요청 보내주는 함수
  let dispatch = useDispatch();
  // store에 있는 state 가지고 오기
  let storeState = useSelector((state) => {return state});
  // 꺼내서 사용
  let likeSwitch = storeState.likeSwitch;

  const postAuthData = {
    "userId" : id,
    "id" : props.postList.postId
  }

  const postViewsData = {
    "id" : props.postList.postId
  }

  const postLikeCheckData = {
    "userId" : id,
    "postId" : props.postList.postId
  }

  const postLikeUpdateData = {
    "userId" : id,
    "postId" : props.postList.postId
  }

  // useEffect(()=>{
    /**
     * 게시글 모달창에서 사용자의 게시글 편집 권한 확인
     */
    axios.post('http://localhost:8085/api/post/auth', postAuthData, {
      headers: {
        'Authorization': `Bearer ${accessToken}` // 여기에 accessToken 추가
      },
      withCredentials: true // 쿠키를 포함하여 전송 (리프레시 토큰)
    })
    .then(
      response=>{
        console.log("게시글 권한 확인 : ", response.data.data);
        setPostAuthState(response.data.data);
      }
    )
    .catch(
      error=>{
        console.log("권한 에러임", error);
      }
    )

    /**
     * 조회수 추가
     */
    axios.post('http://localhost:8085/api/post/views', postViewsData, {
      headers: {
        'Authorization': `Bearer ${accessToken}`
      },
      withCredentials: true
    })
    .then(
      response=>{
        console.log("조회수 반환값", response)
      }
    )
    .catch(
      error=>{
        console.log("조회수 에러", error);
      }
    )

  // }, [accessToken]);

  /**
   * 모달창 닫는 함수
   */
  function modalClose() {
    // modarSwitch 값 false로 바꿔주기
    dispatch(setModalSwitch());
  }

  useEffect(()=>{
    /**
     * 게시글 접속 시 해당 게시글의 좋아요 버튼 상태
     */
    axios.post('http://localhost:8085/api/postlike/check', postLikeCheckData, {
      headers: {
        'Authorization': `Bearer ${accessToken}`
      },
      withCredentials: true
    })
    .then(
      response=>{
        const data = response.data.data;
        console.log("사용자의 해당 게시물 좋아요 상태 : ", data);
        // 여기에서 좋아요 상태 업데이트
        // dispatch(setModalSwitch());
        setLikeState(data);
      }
    )
    .catch(
      error=>{
        console.log("게시글 접속 시 좋아요 판별 오류남 ", error);
      }
    )
  }, [])
   
    /**
     * "좋아요" 버튼 클릭 시 실행되는 로직
     */
    function handleLikeClick() {
      
      if(likeState){
        setLikeState(false);
      }else{
        setLikeState(true);
      }
      
      console.log("좋아요 값 ?", likeSwitch);
      console.log("좋아요 누른 게시물 아이디 : ", props.postList.postId)
  
      axios.post('http://localhost:8085/api/postlike/update', postLikeUpdateData ,{
        headers: {
          'Authorization': `Bearer ${accessToken}`
        },
        withCredentials: true
      })
      .then(
        response=>{
          console.log("좋아요 응답 데이터 : ", response);
        }
      )
      .catch(
        error=>{
          console.log("좋아요눌렀는데 오류가 왜나냐고", error);
        }
      )
      }
  /**
   * 게시글 수정 버튼 클릭 시
   */
  function handlePostEdit() {

    const editConfirm = window.confirm("게시글 수정 페이지로 이동하시겠습니까?")

    if(editConfirm){
      dispatch(setModalSwitch());
      const postList = props.postList
      nav('/board/post/edit', { state: { postList } });
    }
  }

  /**
   * 게시글 삭제 버튼 클릭 시
   */
  function handlePostDelete() {
    
    const deleteConfirm = window.confirm("해당 게시글을 삭제하시겠습니까?");

    if(deleteConfirm){
      const postDeleteData = {
        "userId" : id,
        "id" : props.postList.postId
      }
  
      axios.post('http://localhost:8085/api/post/delete', postDeleteData, {
        headers: {
          'Authorization': `Bearer ${accessToken}` // 여기에 accessToken 추가
        },
        withCredentials: true // 쿠키를 포함하여 전송 (리프레시 토큰)
      })
      .then(
        response=>{
          console.log("게시글 삭제 성공함 : ", response.data);
          window.alert("게시글 삭제가 완료되었습니다.");
          dispatch(setModalSwitch());
          nav('/board');
        }
      )
      .catch(
        error=>{
          console.log("게시글 삭제중 에러남 : ", error)
        }
      )
    }
  }

  return (
    <div className={styles.modal_container}>
    <div>
      
        <div className={styles.post_headers}>

        
          { postAuthState ? 
            <div className={styles.button_group}>
              <button className={styles.edit_post} onClick={ handlePostEdit }>수정</button>
              <button className={styles.delete_post} onClick={handlePostDelete}>삭제</button>
            </div>
            : <div className={styles.empty_space}></div>
          }

          { 
            loading ?
            <div className={styles.spinner_overlay}>
                <ClipLoader color="#ffffff" size={100} />
            </div> :
            null
          }
            
          <div className={styles.post_header}>
            <div className={styles.post_title}>{props.postList.title}</div>
            <div className={styles.post_createAt}>{props.postList.createAt}</div>
          </div>
          <button className={styles.close_btn} onClick={ modalClose }>X</button>
        </div>
    </div>
      
    <video className={styles.highlight_file} controls autoPlay><source src={props.postList.fileUrl}></source></video>
      


      <div className={styles.post_footer}>
        <div className={styles.post_content}>{props.postList.content}</div>
        <div onClick={ handleLikeClick } className={styles.like_button_container}>
        {
          likeState ? <AiFillLike className={styles.like_button}/> : <AiOutlineLike className={styles.like_button}/>
        }
        </div>
      </div>


    </div>
  )
}

export default PostDetail