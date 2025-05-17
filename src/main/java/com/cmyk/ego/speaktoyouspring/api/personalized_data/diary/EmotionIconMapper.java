package com.cmyk.ego.speaktoyouspring.api.personalized_data.diary;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

public class EmotionIconMapper {
    public static final Map<String, String> emotionIconMap = Map.of(
            "행복", "assets/icon/emotion/happiness.svg",
            "평범", "assets/icon/emotion/embarrassment.svg",
            "불안", "assets/icon/emotion/disappointment.svg",
            "화남", "assets/icon/emotion/anger.svg",
            "슬픔", "assets/icon/emotion/sadness.svg"
    );

    public static String getDefaultIcon() {
        return emotionIconMap.get("평범");
    }

    // 문자열에서 첫 단어를 찾아 매핑된 아이콘 반환
    public static Optional<String> getFirstIconFromText(String emotionText) {
        return Arrays.stream(emotionText.split(","))
                .map(String::trim)
                .findFirst()
                .flatMap(emotion -> emotionIconMap.containsKey(emotion)
                        ? Optional.of(emotionIconMap.get(emotion))
                        : Optional.empty()
                );
    }
}

