package com.gongcha.berrymatch.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum District {

    // 서울특별시
    SEOUL_JONGNO_GU(City.SEOUL,"종로구"),
    SEOUL_JUNG_GU(City.SEOUL,"중구"),
    SEOUL_YONGSAN_GU(City.SEOUL,"용산구"),
    SEOUL_SEONGDONG_GU(City.SEOUL,"성동구"),
    SEOUL_GWANGJIN_GU(City.SEOUL,"광진구"),
    SEOUL_DONGDAEMUN_GU(City.SEOUL,"동대문구"),
    SEOUL_JUNGNANG_GU(City.SEOUL,"중랑구"),
    SEOUL_SEONGBUK_GU(City.SEOUL,"성북구"),
    SEOUL_GANGBUK_GU(City.SEOUL,"강북구"),
    SEOUL_DOBONG_GU(City.SEOUL,"도봉구"),
    SEOUL_NOWON_GU(City.SEOUL,"노원구"),
    SEOUL_EUNPYEONG_GU(City.SEOUL,"은평구"),
    SEOUL_SEODAEMUN_GU(City.SEOUL,"서대문구"),
    SEOUL_MAPO_GU(City.SEOUL,"마포구"),
    SEOUL_YANGCHEON_GU(City.SEOUL,"양천구"),
    SEOUL_GANGSEO_GU(City.SEOUL,"강서구"),
    SEOUL_GURO_GU(City.SEOUL,"구로구"),
    SEOUL_GEUMCHEON_GU(City.SEOUL,"금천구"),
    SEOUL_YEONGDEUNGPO_GU(City.SEOUL,"영등포구"),
    SEOUL_DONGJAK_GU(City.SEOUL,"동작구"),
    SEOUL_GWANAK_GU(City.SEOUL,"관악구"),
    SEOUL_SEOCHO_GU(City.SEOUL,"서초구"),
    SEOUL_GANGNAM_GU(City.SEOUL,"강남구"),
    SEOUL_SONGPA_GU(City.SEOUL,"송파구"),
    SEOUL_GANGDONG_GU(City.SEOUL,"강동구"),

    // 부산광역시
    BUSAN_JUNG_GU(City.BUSAN,"중구"),
    BUSAN_SEO_GU(City.BUSAN,"서구"),
    BUSAN_DONG_GU(City.BUSAN,"동구"),
    BUSAN_YEONGDO_GU(City.BUSAN,"영도구"),
    BUSAN_BUSANJIN_GU(City.BUSAN,"부산진구"),
    BUSAN_DONGNAE_GU(City.BUSAN,"동래구"),
    BUSAN_NAM_GU(City.BUSAN,"남구"),
    BUSAN_BUK_GU(City.BUSAN,"북구"),
    BUSAN_HAEUNDAE_GU(City.BUSAN,"해운대구"),
    BUSAN_SASANG_GU(City.BUSAN,"사상구"),
    BUSAN_SUYEONG_GU(City.BUSAN,"수영구"),
    BUSAN_SAHA_GU(City.BUSAN,"사하구"),
    BUSAN_GEUMJEONG_GU(City.BUSAN,"금정구"),
    BUSAN_GANGSEO_GU(City.BUSAN,"강서구"),
    BUSAN_YEONJE_GU(City.BUSAN,"연제구"),
    BUSAN_GIJANG_GUN(City.BUSAN,"기장군"),

    // 대구광역시
    DAEGU_JUNG_GU(City.DAEGU, "중구"),
    DAEGU_DONG_GU(City.DAEGU, "동구"),
    DAEGU_SEO_GU(City.DAEGU, "서구"),
    DAEGU_NAM_GU(City.DAEGU, "남구"),
    DAEGU_BUK_GU(City.DAEGU, "북구"),
    DAEGU_SUSEONG_GU(City.DAEGU, "수성구"),
    DAEGU_DALSEO_GU(City.DAEGU, "달서구"),
    DAEGU_DALSEONG_GUN(City.DAEGU, "달성군"),

    // 인천광역시
    INCHEON_JUNG_GU(City.INCHEON, "중구"),
    INCHEON_DONG_GU(City.INCHEON, "동구"),
    INCHEON_MI_GU(City.INCHEON, "미추홀구"),
    INCHEON_YEONSU_GU(City.INCHEON, "연수구"),
    INCHEON_NAMDONG_GU(City.INCHEON, "남동구"),
    INCHEON_BUPEONG_GU(City.INCHEON, "부평구"),
    INCHEON_GYELYANG_GU(City.INCHEON, "계양구"),
    INCHEON_SEO_GU(City.INCHEON, "서구"),
    INCHEON_GANGHWA_GUN(City.INCHEON, "강화군"),
    INCHEON_ONGJIN_GUN(City.INCHEON, "옹진군"),

    // 광주광역시
    GWANGJU_DONG_GU(City.GWANGJU, "동구"),
    GWANGJU_SEO_GU(City.GWANGJU, "서구"),
    GWANGJU_NAM_GU(City.GWANGJU, "남구"),
    GWANGJU_BUK_GU(City.GWANGJU, "북구"),
    GWANGJU_GWANGSAN_GU(City.GWANGJU, "광산구"),

    // 대전광역시
    DAEJEON_DONG_GU(City.DAEJEON, "동구"),
    DAEJEON_JUNG_GU(City.DAEJEON, "중구"),
    DAEJEON_SEO_GU(City.DAEJEON, "서구"),
    DAEJEON_YUSEONG_GU(City.DAEJEON, "유성구"),
    DAEJEON_DAEDEOK_GU(City.DAEJEON, "대덕구"),

    // 울산광역시
    ULSAN_JUNG_GU(City.ULSAN, "중구"),
    ULSAN_NAM_GU(City.ULSAN, "남구"),
    ULSAN_DONG_GU(City.ULSAN, "동구"),
    ULSAN_BUK_GU(City.ULSAN, "북구"),
    ULSAN_ULJU_GUN(City.ULSAN, "울주군"),

    // 세종특별자치시
    SEJONG(City.SEJONG, "세종특별자치시");

    private final City city;
    private final String text;

    @JsonCreator
    public static District fromString(String value) {
        for (District district : District.values()) {
            if (district.text.equals(value)) {
                return district;
            }
        }
        throw new IllegalArgumentException("Unknown enum type " + value);
    }

    @JsonValue
    public String getText() {
        return text;
    }

}
