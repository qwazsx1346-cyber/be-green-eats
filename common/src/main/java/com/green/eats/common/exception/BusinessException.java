package com.green.eats.common.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {//예외터뜨릴때 무조건 쓰는친구
  private final ErrorCode errorCode;

  public BusinessException(ErrorCode errorCode) {
      super(errorCode.getMessage());
      this.errorCode = errorCode;
  }

  public static BusinessException of(ErrorCode errorCode) {
      return new BusinessException(errorCode);
  }
}