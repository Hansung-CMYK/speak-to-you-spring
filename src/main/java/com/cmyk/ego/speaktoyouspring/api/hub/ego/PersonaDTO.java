package com.cmyk.ego.speaktoyouspring.api.hub.ego;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class PersonaDTO {
    @Schema(example = "5")
    @NotBlank(message = "ego_id은 필수입니다.")
    Long egoId;

    @Schema(example = "카리나")
    @NotBlank(message = "name은 필수입니다.")
    String name;

    @Schema(example = "26")
    @NotBlank(message = "age는 필수입니다.")
    int age;

    @Schema(example = "INTJ")
    @NotBlank(message = "mbti는 필수입니다.")
    String mbti;

    @Schema(example = """
                [ 
                    [
                        '1) 가장 좋아하는 음식은 무엇인가요?',
                        '2) 평소 즐겨 하는 취미나 여가 활동은 무엇인가요?',
                        '3) 하루 중 가장 행복하다고 느끼는 순간이 언제인가요?',
                        '4) 최근에 본 영화나 드라마 중 가장 인상 깊었던 장면은?',
                        '5) 스트레스를 받을 때 듣는 노래가 있나요? 있다면 어떤 곡인가요?',
                        '6) 친구들과의 모임에서 주로 맡는 역할은 무엇인가요?',
                        '7) 최근에 구매한 물건 중 가장 만족스러웠던 것은?',
                        '8) 집에서 가장 오래 머무르는 공간은 어디인가요?',
                        '9) 외출할 때 빠뜨리지 않는 필수 아이템은 무엇인가요?',
                        '10) 좋아하는 계절과 그 이유는 무엇인가요?',
                    ],[
                        '달콤한 디저트를 좋아해서 브라우니나 마카롱을 자주 먹어요.',
                        '요가를 하거나 퍼즐 맞추기를 즐겨 해요.',
                        '따뜻한 차 한 잔 마실 때가 가장 행복해요.',
                        '“기생충”에서 가족이 다 함께 식탁에 모이는 장면이 인상 깊었어요.',
                        '스트레스를 받을 땐 잔잔한 재즈 플레이리스트를 듣습니다.',
                        '모임에서 분위기 메이커 역할을 맡아요.',
                        '최근에 산 블루투스 스피커가 음질이 좋아서 만족스러웠어요.',
                        '거실 소파에서 책 읽는 시간을 가장 오래 보내요.',
                        '밖에 나갈 때는 반드시 보조 배터리를 챙겨요.',
                        '가을을 좋아해요. 선선한 날씨와 단풍이 아름다워서요.',
                    ]
                ]
            """)
    List<List<String>> interview;
}
