package com.cmyk.ego.speaktoyouspring.api.personalized_data.topic;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/topic")
@RequiredArgsConstructor
@Validated
public class TopicController {
    private final TopicService topicService;
}
