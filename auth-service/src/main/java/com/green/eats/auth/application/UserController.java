package com.green.eats.auth.application;

import com.green.eats.auth.application.model.UserSigninReq;
import com.green.eats.auth.application.model.UserSigninRes;
import com.green.eats.auth.application.model.UserSignupReq;
import com.green.eats.auth.application.model.UserUpdateReq;
import com.green.eats.auth.entity.User;
import com.green.eats.common.auth.UserContext;
import com.green.eats.common.model.JwtUser;
import com.green.eats.common.model.ResultResponse;
import com.green.eats.common.model.UserDto;
import com.green.eats.common.security.JwtTokenManager;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
//@RequestMapping("/api/user") CommonWebConfiguration에서 22번 관련
public class UserController {
    private final UserService userService;
    private final JwtTokenManager jwtTokenManager;

    @PostMapping("/signup")
    public ResultResponse<?> signup(@RequestBody UserSignupReq req) {
        log.info("req: {}", req);
        userService.signup(req);
        return ResultResponse.builder()
                            .resultMessage("회원가입 성공")
                            .resultData(1)
                            .build();
    }

    @PostMapping("/signin")
    public ResultResponse<?> signin(HttpServletResponse res, @RequestBody UserSigninReq req ) {
        log.info("req: {}", req);

        User signedUser = userService.signin( req );

        //인증 쿠키
        JwtUser jwtUser = new JwtUser( signedUser.getId()
                                    , signedUser.getName()
                                    , signedUser.getEnumUserRole() );
        jwtTokenManager.issue(res, jwtUser);

        //리턴은 무엇을 해야하나?
        UserSigninRes resultData = UserSigninRes.builder()
                .id( signedUser.getId() )
                .name( signedUser.getName() )
                .build();

        return ResultResponse.builder()
            .resultMessage("로그인 성공")
            .resultData(resultData)
            .build();
    }

    @PostMapping("/signout")
    public ResultResponse<?> signout(HttpServletResponse res) {
        jwtTokenManager.signOut(res);

        return ResultResponse.builder()
            .resultMessage("로그아웃 성공")
            .build();
    }

    @PutMapping
    public ResultResponse<?> updateUser(@RequestBody UserUpdateReq req) {
        //PathVariable 대신 JWT에서 userId 꺼내기
        UserDto userDto = UserContext.get(); //로그인한 유저 정보
        userService.updateUser(userDto.id(), req);
        return ResultResponse.builder()
            .resultMessage("success")
            .build();
    }

    @DeleteMapping
    public ResultResponse delUser() {
        UserDto userDto = UserContext.get();
        userService.delUser(userDto.id());
        return ResultResponse.builder()
            .resultMessage("회원탈퇴 성공")
            .build();
    }

}
