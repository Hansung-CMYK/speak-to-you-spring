package com.cmyk.ego.speaktoyouspring.api.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminChatListDTO {
    @Schema(example = "[{\"uid\":\"user_id_006\",\"egoId\":5,\"content\":[{\"content\":\"카리나 씨 혹시 한성대학교 캡스톤 디자인을 어디서 진행하는지 아시나요?\",\"type\":\"u\"},{\"content\":\"아니요 캡스톤 디자인을 어디서 진행하는 지 몰라요! 무슨 일인가요?\",\"type\":\"e\"},{\"content\":\"캡스톤 디자인을 상상관 지하에서 한다고 하는데, 1학년이라서 거기가 어딘지 모르겠어요\",\"type\":\"u\"},{\"content\":\"그렇군요 제 친구 윈터가 작년에 캡스톤 디자인을 했는데, 상상관은 한성대에서 가장 높은 건물이라고 하네요!\",\"type\":\"e\"},{\"content\":\"고맙습니다. 제가 선배 졸업작품 구경가기로 꼭 약속했는데, 지하철에서 넘어져서 병원 갔다오느라 늦었거든요. 못봤으면 미안해서 울 뻔 했어요.\",\"type\":\"u\"},{\"content\":\"그래도 제 시간에 맞춰 도착해서 다행이네요\",\"type\":\"e\"}]},{\"uid\":\"user_id_006\",\"egoId\":7,\"content\":[{\"content\":\"오늘 퇴근하고 탁구장 갈래? 지난번에 내가 3:2로 졌잖아.\",\"type\":\"u\"},{\"content\":\"콜! 오늘은 새 러버 끼웠으니까 회전 맛 좀 보여 줄게.\",\"type\":\"e\"},{\"content\":\"야, 스핀 핑계 대기 없기! 그럼 7시에 체육관에서 만나는 거다.\",\"type\":\"u\"},{\"content\":\"오케이, 라켓이랑 공은 챙겼고, 땀 닦을 수건도 가져갈게.\",\"type\":\"e\"},{\"content\":\"도착하면 5분 정도 드라이브 랠리로 몸 풀자.\",\"type\":\"u\"},{\"content\":\"좋아, 몸 풀고 바로 11점제 세트 들어가자.\",\"type\":\"e\"},{\"content\":\"첫 서브는 내가 넣는다, 백핸드 쪽 조심해.\",\"type\":\"u\"},{\"content\":\"받아칠 준비 완료! 스매시 하나면 바로 득점이지.\",\"type\":\"e\"},{\"content\":\"방금 커트 너무 낮았어, 네트 맞고 아웃이야. 5:3로 내가 앞선다!\",\"type\":\"u\"},{\"content\":\"아직 초반이야! 탑스핀 드라이브 각 보인다, 이 점수 금방 뒤집는다!\",\"type\":\"e\"}]}]")
    private List<AdminChatDTO> chatList;
}
