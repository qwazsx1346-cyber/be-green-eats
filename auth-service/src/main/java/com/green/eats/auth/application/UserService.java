package com.green.eats.auth.application;

import com.green.eats.auth.application.model.UserSigninReq;
import com.green.eats.auth.application.model.UserSignupReq;
import com.green.eats.auth.application.model.UserUpdateReq;
import com.green.eats.auth.entity.User;
import com.green.eats.auth.exception.UserErrorCode;
import com.green.eats.common.constants.UserEventType;
import com.green.eats.common.exception.BusinessException;
import com.green.eats.common.model.UserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void signup(UserSignupReq req) {
        //비밀번호 암호화해서 담기
        String hashedPassword = passwordEncoder.encode( req.getPassword() );
        log.info("hashedPassword: {}", hashedPassword);
        req.setPassword(hashedPassword);


        //회원가입
        User newUser = new User();
        newUser.setEmail( req.getEmail() );
        newUser.setPassword( hashedPassword );
        newUser.setName( req.getName() );
        newUser.setAddress( req.getAddress() );
        newUser.setEnumUserRole( req.getUserRole() );
        newUser.setIsDel( false );

        userRepository.save(newUser);

        //kafkaEvent(newUser.getId(), newUser.getName(), UserEventType.CREATE); 이 한줄로 아래작업 끝

        //여기부터 59번라인까지는 내가 너희에게 신호를 쏴줄게~하는 친구
        UserEvent userEvent = UserEvent.builder()
            //아래는 내가 보내고싶은 데이터들
            .userId( newUser.getId() )
            .name( newUser.getName() )
            .eventType( UserEventType.CREATE )
            .build();

        kafkaTemplate.send("user-topic", String.valueOf(newUser.getId()), userEvent)
            .whenComplete((result, ex) -> {
                if (ex == null) {
                    // 성공 시 로그
                    log.info("✅ [Kafka Success] Topic: {}, Offset: {}",
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().offset());
                } else {
                    // 실패 시 로그
                    log.error("❌ [Kafka Failure] 원인: {}", ex.getMessage());
                }
            });
    }

    public User signin(UserSigninReq req) {
        User signedUser = userRepository.findByEmail( req.getEmail() );

        log.info("signedUser: {}", signedUser);
        //!를 붙여줌으로서 양쪽값이 같아야 notFoundUser가 호출됨
        if(signedUser == null || !passwordEncoder.matches( req.getPassword(), signedUser.getPassword())) {
            notFoundUserAndNotMatchedPassword();
        }
        return signedUser;
    }

    @Transactional
    public void updateUser(Long userId, UserUpdateReq req) {
        // 1. 유저 조회
        User user = userRepository.findById(userId)
            .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."));

        //kafkaEvent(user.getId(), user.getName(), UserEventType.UPDATE); 이 한줄로 아래작업 끝

        // 2. 유저 정보 수정
        user.setName( req.getName() );
        user.setAddress( req.getAddress() );
        // userRepository.save() 안 해도 됨 → @Transactional이 자동으로 감지해서 저장

        // 3. Kafka에 UPDATE 이벤트 발행(signup이랑 구조 똑같이)
        UserEvent userEvent = UserEvent.builder()
            .userId( user.getId() )
            .name( user.getName() )
            .eventType( UserEventType.UPDATE ) //회원가입에서 여기 이부분만 다름
            .build();

        kafkaTemplate.send("user-topic", String.valueOf(user.getId()), userEvent)
            .whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("[Kafka Success] Topic: {}, Offset: {}",
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().offset());
                } else  {
                    log.error("[Kafka Failure] 원인: {}", ex.getMessage());
                }
            });
    }

    //@Transactional 이걸 붙이면 userRepository.save(user); 이 작업 안해줘도 됨.
    public void delUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."));

        user.setIsDel(true);
        userRepository.save(user);

        kafkaEvent(userId, user.getName(), UserEventType.DELETE);
    }

    private void kafkaEvent(Long userId, String name, UserEventType eventType) {
        UserEvent userEvent = UserEvent.builder()
            .userId( userId )
            .name( name )
            .eventType( eventType ) //회원가입에서 여기 이부분만 다름
            .build();

        kafkaTemplate.send("user-topic", String.valueOf(userId), userEvent)
            .whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("[Kafka Success] Topic: {}, Offset: {}",
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().offset());
                } else  {
                    log.error("[Kafka Failure] 원인: {}", ex.getMessage());
                }
            });
    }

    private void notFoundUserAndNotMatchedPassword() {
        throw BusinessException.of(UserErrorCode.CHECK_EMAIL_PASSWORD);
    }

}
