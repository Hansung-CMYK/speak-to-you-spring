package com.cmyk.ego.speaktoyouspring.api.test;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class TestCreate {
    private String testParam1;
    private String testParam2;
    private String testParam3;
}
