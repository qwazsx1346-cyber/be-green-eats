package com.green.eats.auth.application;

import com.green.eats.auth.application.model.UserSignupReq;
import com.green.eats.common.model.ResultResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResultResponse<?> signup(@RequestBody UserSignupReq req) {
        log.info("req: {}", req);
        userService.signup(req);
        return ResultResponse.builder()
                            .resultMessage("회원가입 성공")
                            .resultData(1)
                            .build();
    }
}
