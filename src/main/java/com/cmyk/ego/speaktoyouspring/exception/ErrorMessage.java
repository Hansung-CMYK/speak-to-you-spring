package com.cmyk.ego.speaktoyouspring.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorMessage {
    int status;
    String message;
}
