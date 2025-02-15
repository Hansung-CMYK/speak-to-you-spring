package com.cmyk.ego.speaktoyouspring.api.test;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestService {
    private final TestRepository testRepository;

    public Test access(TestAccess testAccess) {
        // testParam2는 Unique이기 때문에 접근가능
        var testParam2 = testAccess.getTestParam2();

        // 1. id를 통해 해당 엔티티 불러오기
        var test = testRepository.findByTestParam2(testParam2)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        return test;
    }

    public Test create(TestCreate testCreate) {
        var test = Test.builder()
                .testParam1(testCreate.getTestParam1())
                .testParam2(testCreate.getTestParam2())
                .testParam3(testCreate.getTestParam3())
                .build();
        testRepository.save(test);
        return test;
    }
}
