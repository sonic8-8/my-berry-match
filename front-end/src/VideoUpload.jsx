import React, { useState } from 'react';
import axios from 'axios';

const VideoUpload = () => {
  const [videoFile, setVideoFile] = useState(null);
  const [data, setData] = useState(null);
  const [message, setMessage] = useState('');
  const [status, setStatus] = useState(false);
  const [code, setCode] = useState(0);

  const handleFileChange = (event) => {
    setVideoFile(event.target.files[0]);
  };

  const handleUpload = async () => {
    if (!videoFile) {
      alert("? 영상부터 선택하십쇼");
      return;
    }

    const formData = new FormData();
    formData.append('file', videoFile);

    try {
      const response = await axios.post('http://localhost:8085/api/s3/upload', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })
      .then(response => {
                
        const apiResponse = response.data;

        // 각 필드를 추출
        setStatus(apiResponse.status);
        setMessage(apiResponse.message);
        setData(apiResponse.data);
        setCode(apiResponse.code);
      })
      .catch(error => {
          console.error('Error:', error);
      });

      alert("업로드 성공요");
      console.log(message);

    } catch (error) {
      console.error("업로드 실패요:", error);
      alert("업로드 실패요");
    }
  };

  return (
    <div>
      <h2>Upload a Video</h2>
      <input type="file" accept="video/*" onChange={handleFileChange} />
      <button onClick={handleUpload}>Upload</button>
    </div>
  );
};

export default VideoUpload;
