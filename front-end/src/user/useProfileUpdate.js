import { useState } from 'react';
import axios from 'axios';
import Cookies from 'js-cookie';
import { jwtDecode } from 'jwt-decode';
import { useNavigate } from 'react-router-dom';

const useProfileUpdate = (initialUserInfo) => {
    const [profileImage, setProfileImage] = useState(initialUserInfo.profileImageUrl || '');
    const [file, setFile] = useState(null);
    const [status, setStatus] = useState(null);
    const [message, setMessage] = useState(null);
    const [data, setData] = useState(null);
    const [code, setCode] = useState(null);
    const [introduction, setIntroduction] = useState('');
    const navigate = useNavigate();

    const handleProfileImageChange = (e) => {
        const selectedFile = e.target.files[0];
        if (selectedFile) {
            const reader = new FileReader();
            reader.onloadend = () => {
                setProfileImage(reader.result);
            };
            reader.readAsDataURL(selectedFile);
            setFile(selectedFile);
        }
    };

    const handleUpdate = async () => {
        if (!file) {
            alert("파일을 선택하세요");
            return;
        }

        const accessToken = Cookies.get('accessToken');

        try {
            const decodedToken = jwtDecode(accessToken);
            const identifier = decodedToken.identifier;
            const providerInfo = decodedToken.providerInfo;
    
            if (!identifier) {
              throw new Error('identifier not found in refresh token');
            }
        

            const formData = new FormData();
            formData.append('file', file);
            formData.append('introduction', introduction);
            formData.append('identifier', identifier);
            formData.append('providerInfo', providerInfo);

            const response = await axios.post('http://localhost:8085/api/profile/update', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                    'Authorization': `Bearer ${accessToken}`
                },
                withCredentials: true
            });
            
            const apiResponse = response.data;
            setStatus(apiResponse.status);
            setMessage(apiResponse.message);
            setData(apiResponse.data);
            setCode(apiResponse.code);
            
            alert("업로드 성공요");
            console.log(data);
            console.log(status);
            console.log(code);
            
            navigate('/mypage/profile-edit');
        } catch (error) {
            console.error("업로드 실패요:", error);
            alert("업로드 실패요");
            navigate('/mypage/profile-edit');
        }
    };

    return {
        profileImage,
        handleProfileImageChange,
        handleUpdate,
        status,
        message,
        data,
        code,
        introduction,
        setIntroduction
    };
};

export default useProfileUpdate;
