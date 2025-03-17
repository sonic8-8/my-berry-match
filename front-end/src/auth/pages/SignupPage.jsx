import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import axios from "axios";
import styles from './SignupPage.module.css';
import { useParams } from "react-router-dom";
import Cookies from 'js-cookie';
import { setToken } from "../setToken";

function SignupPage() {
    const location = useLocation();
    const [formData, setFormData] = useState({
        identifier: '',
        providerInfo: '',
        nickname: '',
        city: '',
        district: '',
        gender: '',
        birthdate: '',
        phoneNumber: '',
        profileImageUrl: '',
        introduction: ''
    });

    const [districtOptions, setDistrictOptions] = useState([]);

    
    useEffect(() => {
        // URL의 쿼리 파라미터에서 identifier 추출
        const params = new URLSearchParams(location.search);
        const identifier = params.get('identifier');
        const providerInfo = params.get('providerInfo');

        setFormData((prevData) => ({
            ...prevData,
            identifier: identifier || '',
            providerInfo: providerInfo || '',
        }));
    }, [location]);


    /**
     * City에 따른 District를 업데이트하기 위한 useEffect 훅
     */
    useEffect(() => {
        if (formData.city) {
            setDistrictOptions(Districts[formData.city] || []);
        } else {
            setDistrictOptions([]);
        }
    }, [formData.city]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        // 변환된 값을 기반으로 데이터 준비
        const cityEnum = CityMapping[formData.city] || '';
        const districtEnum = DistrictMapping[cityEnum]?.[formData.district] || '';
        

        const requestData = {
            ...formData,
            city: cityEnum,
            district: districtEnum,
           
        };

        try {
            // 1. 회원가입 요청
            const signupResponse = await axios.post("http://localhost:8085/api/auth/signup", formData, {
                headers: {
                    "Content-Type": "application/json"
                }
            });

            const signupApiResponse = signupResponse.data;
            const signupData = signupApiResponse.data;
            const signupMessage = signupApiResponse.message;
            const signupCode = signupApiResponse.code;
            const signupStatus = signupApiResponse.status;

            console.log(signupData);
            alert(signupData.providerInfo);
            

            if (signupCode === 200) {
                console.log('회원가입 성공');

                const accessToken = Cookies.get('accessToken');

                // 2. 로그인 요청 및 액세스 토큰, 리프레시 토큰 처리
                const loginResponse = await axios.post("http://localhost:8085/api/auth", { identifier: signupData.identifier, providerInfo: signupData.providerInfo }, {
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": accessToken ? `Bearer ${accessToken}` : '', // 현재 액세스 토큰 (없을 수도 있음)
                        "token-reissued": "False"
                    },
                    withCredentials: true // 쿠키를 포함하여 전송 (리프레시 토큰)
                });

                const loginApiResponse = loginResponse.data;
                const loginData = loginApiResponse.data;
                const loginMessage = loginApiResponse.message;
                const loginCode = loginApiResponse.code;
                const loginStatus = loginApiResponse.status;

                if (loginCode === 200) {
                    // 3. 액세스 토큰 저장

                    const authorizationHeader = loginResponse.headers['authorization'];

                    const accessToken = authorizationHeader.split(' ')[1];
                    
                    Cookies.set('accessToken', accessToken, { path: '/' });

                    console.log(loginData.role);
                    console.log(accessToken);

                    // 4. 사용자 역할에 따라 리다이렉션 처리
                    if (loginData.role == "USER") {
                        window.location.href = "http://localhost:3000/";
                    } else {
                        window.location.href = "http://localhost:3000/login";
                    }
                } else {
                    console.error('로그인 실패:', loginMessage);
                    window.location.href = "http://localhost:3000/login";
                }
            } else {
                console.error('회원가입 실패:', signupMessage);
                window.location.href = "http://localhost:3000/login";
            }
        } catch (error) {
            console.error("Error:", error);
        }
    };

    return (
        <div className={styles.layout}>
            <div className={styles.layout_header}>
                <h1>{formData.identifier}님 환영합니다!</h1>
            </div>
    
            <div className={styles.layout_content}>

                <form onSubmit={handleSubmit} className={styles.signupForm}>
                    <label className={styles.signup_input_container_none}>
                        <input className={styles.signup_input}
                            type="hidden" 
                            name="identifier"
                            value={formData.identifier}
                        />
                    </label>

                    <label className={styles.signup_input_container_none}>
                        <input className={styles.signup_input}
                            type="hidden" 
                            name="providerInfo"
                            value={formData.providerInfo}
                        />
                    </label>

                    <label className={styles.signup_input_container}>
                        <div className={styles.signup_input_title}>닉네임</div>
                        <input className={styles.signup_input}
                            type="text" 
                            name="nickname" 
                            value={formData.nickname} 
                            onChange={handleChange} 
                            required 
                        />
                    </label>

                    <label className={styles.signup_input_container}>
                        <div className={styles.signup_input_title}>광역시</div>
                        <select className={styles.signup_input}
                            name="city" 
                            value={formData.city} 
                            onChange={handleChange} 
                            required
                        >
                            <option value="">Select City</option>
                            {Object.entries(CityMapping).map(([key, value]) => (
                                <option key={key} value={value}>{key}</option>
                            ))}
                        </select>
                    </label>

                    <label className={styles.signup_input_container}>
                        <div className={styles.signup_input_title}>시/군/구</div>
                        <select className={styles.signup_input}
                            name="district" 
                            value={formData.district} 
                            onChange={handleChange} 
                            required
                        >
                            <option value="">Select District</option>
                            {districtOptions.map((district, index) => (
                                <option key={index} value={district}>{district}</option>
                            ))}
                        </select>
                    </label>

                    <label className={styles.signup_input_container}>
                        <div className={styles.signup_input_title}>성별</div>
                        <select className={styles.signup_input}
                            name="gender" 
                            value={formData.gender} 
                            onChange={handleChange} 
                            required
                        >
                            <option value="">Select Gender</option>
                            <option value="MALE">MALE</option>
                            <option value="FEMALE">FEMALE</option>
                        </select>
                    </label>

                    <label className={styles.signup_input_container}>
                        <div className={styles.signup_input_title}>생년월일</div>
                        <input className={styles.signup_input}
                            type="date" 
                            name="birthdate" 
                            value={formData.birthdate} 
                            onChange={handleChange} 
                            required 
                        />
                    </label>

                    <label className={styles.signup_input_container}>
                        <div className={styles.signup_input_title}>전화번호</div>
                        <input className={styles.signup_input}
                            type="tel" 
                            name="phoneNumber" 
                            value={formData.phoneNumber} 
                            onChange={handleChange} 
                            required 
                        />
                    </label>
                    <div className={styles.signup_input_container}>
                        <button className={styles.signupForm_button} type="submit">가입</button>
                    </div>
                </form>
            </div>
            
        </div>
    );
}

export default SignupPage;


const City = {
    SEOUL: "서울",
    BUSAN: "부산",
    DAEGU: "대구",
    INCHEON: "인천",
    GWANGJU: "광주",
    DAEJEON: "대전",
    ULSAN: "울산",
    SEJONG: "세종"
};

const Districts = {
    SEOUL: [
        "종로구", "중구", "용산구", "성동구", "광진구", "동대문구", "중랑구", "성북구", 
        "강북구", "도봉구", "노원구", "은평구", "서대문구", "마포구", "양천구", "강서구", 
        "구로구", "금천구", "영등포구", "동작구", "관악구", "서초구", "강남구", "송파구", 
        "강동구"
    ],
    BUSAN: [
        "중구", "서구", "동구", "영도구", "부산진구", "동래구", "남구", "북구", 
        "해운대구", "사상구", "수영구", "사하구", "금정구", "강서구", "연제구", "기장군"
    ],
    DAEGU: [
        "중구", "동구", "서구", "남구", "북구", "수성구", "달서구", "달성군"
    ],
    INCHEON: [
        "중구", "동구", "미추홀구", "연수구", "남동구", "부평구", "계양구", "서구", 
        "강화군", "옹진군"
    ],
    GWANGJU: [
        "동구", "서구", "남구", "북구", "광산구"
    ],
    DAEJEON: [
        "동구", "중구", "서구", "유성구", "대덕구"
    ],
    ULSAN: [
        "중구", "남구", "동구", "북구", "울주군"
    ],
    SEJONG: [
        "세종특별자치시"
    ]
};

const providerInfo = {
    KAKAO: "카카오",
    NAVER: "네이버",
    GOOGLE: "구글"
}

const CityMapping = {
    "서울": "SEOUL",
    "부산": "BUSAN",
    "대구": "DAEGU",
    "인천": "INCHEON",
    "광주": "GWANGJU",
    "대전": "DAEJEON",
    "울산": "ULSAN",
    "세종": "SEJONG"
};

const DistrictMapping = {
    SEOUL: {
        "종로구": "SEOUL_JONGNO_GU",
        "중구": "SEOUL_JUNG_GU",
        "용산구": "SEOUL_YONGSAN_GU",
        "성동구": "SEOUL_SEONGDONG_GU",
        "광진구": "SEOUL_GWANGJIN_GU",
        "동대문구": "SEOUL_DONGDAEMUN_GU",
        "중랑구": "SEOUL_JUNGNANG_GU",
        "성북구": "SEOUL_SEONGBUK_GU",
        "강북구": "SEOUL_GANGBUK_GU",
        "도봉구": "SEOUL_DOBONG_GU",
        "노원구": "SEOUL_NOWON_GU",
        "은평구": "SEOUL_EUNPYEONG_GU",
        "서대문구": "SEOUL_SEODAEMUN_GU",
        "마포구": "SEOUL_MAPO_GU",
        "양천구": "SEOUL_YANGCHEON_GU",
        "강서구": "SEOUL_GANGSEO_GU",
        "구로구": "SEOUL_GURO_GU",
        "금천구": "SEOUL_GEUMCHEON_GU",
        "영등포구": "SEOUL_YEONGDEUNGPO_GU",
        "동작구": "SEOUL_DONGJAK_GU",
        "관악구": "SEOUL_GWANAK_GU",
        "서초구": "SEOUL_SEOCHO_GU",
        "강남구": "SEOUL_GANGNAM_GU",
        "송파구": "SEOUL_SONGPA_GU",
        "강동구": "SEOUL_GANGDONG_GU"
    },
    BUSAN: {
        "중구": "BUSAN_JUNG_GU",
        "서구": "BUSAN_SEO_GU",
        "동구": "BUSAN_DONG_GU",
        "영도구": "BUSAN_YEONGDO_GU",
        "부산진구": "BUSAN_BUSANJIN_GU",
        "동래구": "BUSAN_DONGNAE_GU",
        "남구": "BUSAN_NAM_GU",
        "북구": "BUSAN_BUK_GU",
        "해운대구": "BUSAN_HAEUNDAE_GU",
        "사상구": "BUSAN_SASANG_GU",
        "수영구": "BUSAN_SUYEONG_GU",
        "사하구": "BUSAN_SAHA_GU",
        "금정구": "BUSAN_GEUMJEONG_GU",
        "강서구": "BUSAN_GANGSEO_GU",
        "연제구": "BUSAN_YEONJE_GU",
        "기장군": "BUSAN_GIJANG_GUN"
    },
    DAEGU: {
        "중구": "DAEGU_JUNG_GU",
        "동구": "DAEGU_DONG_GU",
        "서구": "DAEGU_SEO_GU",
        "남구": "DAEGU_NAM_GU",
        "북구": "DAEGU_BUK_GU",
        "수성구": "DAEGU_SUSEONG_GU",
        "달서구": "DAEGU_DALSEO_GU",
        "달성군": "DAEGU_DALSEONG_GUN"
    },
    INCHEON: {
        "중구": "INCHEON_JUNG_GU",
        "동구": "INCHEON_DONG_GU",
        "미추홀구": "INCHEON_MI_GU",
        "연수구": "INCHEON_YEONSU_GU",
        "남동구": "INCHEON_NAMDONG_GU",
        "부평구": "INCHEON_BUPEONG_GU",
        "계양구": "INCHEON_GYELYANG_GU",
        "서구": "INCHEON_SEO_GU",
        "강화군": "INCHEON_GANGHWA_GUN",
        "옹진군": "INCHEON_ONGJIN_GUN"
    },
    GWANGJU: {
        "동구": "GWANGJU_DONG_GU",
        "서구": "GWANGJU_SEO_GU",
        "남구": "GWANGJU_NAM_GU",
        "북구": "GWANGJU_BUK_GU",
        "광산구": "GWANGJU_GWANGSAN_GU"
    },
    DAEJEON: {
        "동구": "DAEJEON_DONG_GU",
        "중구": "DAEJEON_JUNG_GU",
        "서구": "DAEJEON_SEO_GU",
        "유성구": "DAEJEON_YUSEONG_GU",
        "대덕구": "DAEJEON_DAEDEOK_GU"
    },
    ULSAN: {
        "중구": "ULSAN_JUNG_GU",
        "남구": "ULSAN_NAM_GU",
        "동구": "ULSAN_DONG_GU",
        "북구": "ULSAN_BUK_GU",
        "울주군": "ULSAN_ULJU_GUN"
    },
    SEJONG: {
        "세종특별자치시": "SEJONG"
    }
};

const ProviderInfoMapping = {
    "카카오": "KAKAO",
    "네이버": "NAVER",
    "구글": "GOOGLE"
};