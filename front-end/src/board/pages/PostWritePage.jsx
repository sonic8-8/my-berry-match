import React, { useState, useEffect, useRef } from 'react';
import styles from './PostWritePage.module.css';
import axios from 'axios';
import defaultImg from '../img/defaultImg.png';
import Cookies from 'js-cookie';
import { jwtDecode } from 'jwt-decode';
import { useNavigate } from 'react-router-dom';
import { ClipLoader } from 'react-spinners';

function PostWritePage() {

  const nav = useNavigate();

  const accessToken = Cookies.get("accessToken");

  const [ selectedFile, setSelectedFile ] = useState(null);
  const [ selectedThumbnail, setSelectedThumbnail ] = useState(null);
  const [ title, setTitle ] = useState(null);
  const [ content, setContent ] = useState(null);
  const [ inputThumbnail, setInputThumbnail ] = useState(null);
  const [ inputFile, setInputFile ] = useState(null);
  const [ loading, setLoading ] = useState(false);

  /**
   * 썸네일을 선택했을 때 동작하는 함수
   */
  function handleThumbnailChange(event) {
    console.log("선택된 썸네일 : ", event.target.files[0]);
    
    const thumbnailReader = new FileReader();

    console.log("썸네일을 담은 파일리더 : ", thumbnailReader);

    thumbnailReader.readAsDataURL(event.target.files[0]);

    thumbnailReader.onloadend = () => {
      setInputThumbnail(thumbnailReader.result);
    }
    setSelectedThumbnail(event.target.files[0]);
  }

  /**
   * 하이라이트 파일을 선택했을 때 동작하는 함수
   */
  function handleHighlightChange(event) {    
    console.log("선택된 하이라이트 이미지나 동영상 : ", event.target.files[0]);

    const fileReader = new FileReader();

    fileReader.readAsDataURL(event.target.files[0]);

    fileReader.onloadend = () => {
      setInputFile(fileReader.result);
    }

    fileReader.addEventListener = () => {
      console.log("리더오류메시지 : ", fileReader.error.message);
    }

    setSelectedFile(event.target.files[0]);
  }



  function handleTitleBlur(event) {
    setTitle(event.target.value);
  }
  console.log("현재 입력된 제목 : ", title);
  
  function handleContentBlur(event) {
    setContent(event.target.value)  
  }
  console.log("현재 작성된 내용 : ", content);



  
  /**
   * 업로드 버튼 눌렀을 때 동작하는 함수
   */
  async function submitFile(event) {
    // 새로고침 방지
    event.preventDefault();

    const decodedToken = jwtDecode(accessToken);
    const id = decodedToken.id;
    console.log(id);

    let post_id = null;

    // 제목과 내용 데이터 묶음
    const postData = {
      "title" : title,
      "content" : content,
      "userId" : id
    }

    const submitConfirm = window.confirm("게시글을 업로드하시겠습니까?");

    if(submitConfirm){
      if((title == null || title == "") || (content == null || content == "")){
        window.alert("제목 또는 내용을 입력해주세요.");
      }else if(inputThumbnail == null || inputFile == null){
        window.alert("썸네일 또는 하이라이트 파일을 첨부해주세요.");
      }else{
        setLoading(true);
        await axios.post('http://localhost:8085/api/post/upload', postData, {
          headers: {
            'Authorization': `Bearer ${accessToken}` // 여기에 accessToken 추가
          },
          withCredentials: true // 쿠키를 포함하여 전송 (리프레시 토큰)
        })
        .then(
          response=>{
            const apiResponse = response.data;
            const data = apiResponse.data;
            const message = apiResponse.message;
            const code = apiResponse.code;
            const status = apiResponse.status;
    
            post_id = data.id;
            console.log("게시글 업로드 성공! 반환값 ", post_id);
          }
        )
        .catch(
          error=>{
            console.log("게시글 업로드 실패 -> ", error);
          }
        )
    
        console.log("사용자가 보낸 파일 :", selectedFile);
        console.log("사용자가 보낸 썸네일 :", selectedThumbnail);
    
        // 파일 데이터를 담을 수 있는 객체
        const formData = new FormData();
        // 선택된 파일과 썸네일 formdata에 담기
        formData.append('file', selectedFile);
        formData.append('thumbnail', selectedThumbnail);
        formData.append('post_id', post_id)
    
        
        await axios.post('http://localhost:8085/api/s3/upload', formData, {
          headers: {
            'Authorization': `Bearer ${accessToken}`
          },
          withCredentials: true
        })
        .then(
          response=>{
            
            const apiResponse = response.data;
            const code = apiResponse.code;
            const status = apiResponse.status;
            const message = apiResponse.message;
            const data = apiResponse.data;
            setLoading(false);
            window.alert('하이라이트 업로드를 성공하였습니다.');
            nav('/board');
            console.log("파일 업로드 성공 : ", message);
          }
        )
        .catch(
          error=>{
            console.log("파일 업로드 실패", error);
          }
        ) 
      }
    }
  }





  return (
    <div className={styles.container}>
      
      <div className={styles.container_input}>
        <div>
          <h3 className={styles.title_h3}>제목</h3> <br></br>
          <input type='text' className={styles.title_input}  onBlur={ handleTitleBlur }></input>
        </div>

        <div>
          <h3 className={styles.content_h3}>내용</h3><br></br>
          <input className={styles.content_textarea}  onBlur={ handleContentBlur }></input>
        </div>
      </div>

      <br></br>

      { 
        loading ?
        <div className={styles.spinner_overlay}>
            <ClipLoader color="#ffffff" size={100} />
        </div> :
        null
      }
      
      <div className={styles.container_files}>
        <div className={styles.thumbnail_div}>
          <p>썸네일</p>
          <input type='file' className={styles.file_input} onChange={ handleThumbnailChange }></input>
          <br></br>
          <img src={ inputThumbnail ? inputThumbnail : defaultImg } className={styles.size_img}></img>
        </div>
        <div className={styles.highlight_div}>
          <p>하이라이트</p>
          <input type='file' className={styles.file_input}  onChange={ handleHighlightChange }></input>
          <br></br>
          {  inputFile ? 
            <video src={inputFile} className={styles.size_img} controls></video> : <img src={defaultImg} className={styles.size_img}></img>
          }
        </div>
      </div>
      <div className={styles.button_container}>
      <button className={styles.upload_button} onClick={ submitFile }>업로드</button>
      </div>

    </div>
  )
}

export default PostWritePage;