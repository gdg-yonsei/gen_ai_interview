package com.gen_ai.interview.service;

import com.gen_ai.interview.dto.user.DuplicateEmailRequestDTO;
import com.gen_ai.interview.dto.user.JwtTokenDTO;
import com.gen_ai.interview.dto.user.UserAuthResponseDTO;
import com.gen_ai.interview.dto.user.UserDTO;
import com.gen_ai.interview.dto.user.UserEmailLoginRequestDTO;
import com.gen_ai.interview.dto.user.UserEmailSignUpRequestDTO;
import com.gen_ai.interview.dto.user.UserGoogleInfoDTO;
import com.gen_ai.interview.dto.user.UserGoogleLoginRequestDTO;
import com.gen_ai.interview.error.errorcode.UserErrorCode;
import com.gen_ai.interview.error.exception.BadCredentialsException;
import com.gen_ai.interview.error.exception.DuplicateEmailException;
import com.gen_ai.interview.error.exception.DuplicateIdException;
import com.gen_ai.interview.error.exception.GoogleAccountRetrieveException;
import com.gen_ai.interview.error.exception.JsonParseException;
import com.gen_ai.interview.error.exception.NotExistIdException;
import com.gen_ai.interview.model.Resume;
import com.gen_ai.interview.model.User;
import com.gen_ai.interview.model.eunm.LoginType;
import com.gen_ai.interview.model.eunm.RoleType;
import com.gen_ai.interview.repository.ResumeRepository;
import com.gen_ai.interview.repository.UserRepository;
import com.gen_ai.interview.security.CustomUserDetails;
import com.gen_ai.interview.security.TokenProvider;
import com.gen_ai.interview.util.PasswordEncrypter;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/*
사용자의 회원가입, 로그인을 담당하는 서비스 레이어
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final ResumeRepository resumeRepository;
    private final TokenProvider tokenProvider;
    private static final Integer REMAIN_INTERVIEW = 5;
    private static final String GOOGLE_USERINFO_URL = "https://www.googleapis.com/oauth2/v3/userinfo";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 사용자 회원가입시 사용자 정보와 로그인 토큰도 함께 반환
    public UserAuthResponseDTO emailSignUpAndLogin(UserEmailSignUpRequestDTO userEmailSignUpRequestDTO) {
        UserDTO newUser = signUp(userEmailSignUpRequestDTO, LoginType.EMAIL);
        UserEmailLoginRequestDTO loginRequestDTO = UserEmailLoginRequestDTO.builder()
                .email(newUser.getEmail())
                .password(userEmailSignUpRequestDTO.getPassword())
                .build();
        return emailLogin(loginRequestDTO, LoginType.EMAIL);
    }

    // 이메일 회원가입
    public UserDTO signUp(UserEmailSignUpRequestDTO userEmailSignUpRequestDTO, LoginType loginType) {
        if (userRepository.existsByEmail(userEmailSignUpRequestDTO.getEmail())) {
            throw new DuplicateIdException(UserErrorCode.DUPLICATE_USER);
        }

        // 사용자 비밀번호 암호화
        String encryptedPassword = encryptPassword(userEmailSignUpRequestDTO.getPassword());

        // 사용자 정보 저장
        User user = getUser(userEmailSignUpRequestDTO, encryptedPassword, loginType);

        // 명시적으로 save 유저 변수 추출
        User saveUser = userRepository.save(user);

        // 유저 정보 반환
        return UserDTO.builder().
                userId(saveUser.getId())
                .name(saveUser.getName())
                .email(saveUser.getEmail())
                .loginType(saveUser.getLoginType())
                .remainInterview(REMAIN_INTERVIEW)
                .build();
    }

    // 사용자 로그인
    public UserAuthResponseDTO emailLogin(UserEmailLoginRequestDTO userLoginRequestDTO, LoginType loginType) {
        // 이메일로 데이터베이스에서 사용자 찾기
        User user = userRepository.findByEmail(userLoginRequestDTO.getEmail())
                .orElseThrow(() -> new BadCredentialsException(UserErrorCode.BAD_CREDENTIALS));
        // 비밀번호 일치하는지 검증
        if (!PasswordEncrypter.isMatch(userLoginRequestDTO.getPassword(), user.getPassword())) {
            throw new BadCredentialsException(UserErrorCode.BAD_CREDENTIALS);
        }
        // JWT 토큰 만들기
        JwtTokenDTO loginToken = getJwtTokenDTO(user);

        //UserDTO
        UserDTO loggedInUser = UserDTO.builder().
                userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .loginType(loginType)
                .remainInterview(user.getRemainInterview())
                .build();

        return UserAuthResponseDTO.builder()
                .user(loggedInUser)
                .token(loginToken)
                .build();
    }

    // 구글 회원가입
    public User googleSignUp(UserGoogleInfoDTO userGoogleInfoDTO) {
        // 이미 존재하는 이메일인지 확인
        if (userRepository.existsByEmail(userGoogleInfoDTO.getEmail())) {
            throw new DuplicateIdException(UserErrorCode.DUPLICATE_USER);
        }

        // 비밀번호 암호화
        String encryptPassword = encryptPassword(userGoogleInfoDTO.getAccessToken());
        User user = User.signUpUser(userGoogleInfoDTO.getName(), userGoogleInfoDTO.getEmail(),
                encryptPassword);
        user.setLoginType(LoginType.GOOGLE);

        User saveUser = userRepository.save(user);
        logger.info("{} 가 저장되었습니다. ", user.getEmail());

        return saveUser;
    }

    // 구글 로그인
    public UserAuthResponseDTO googleLogin(UserGoogleLoginRequestDTO userGoogleLoginRequestDTO) {
        UserGoogleInfoDTO userGoogleInfoDTO = getGoogleInfo(userGoogleLoginRequestDTO);
        User currentUser;
        if (!userRepository.existsByEmail(userGoogleInfoDTO.getEmail())) {
            currentUser = googleSignUp(userGoogleInfoDTO);
        } else {
            currentUser = userRepository.findByEmail(userGoogleInfoDTO.getEmail())
                    .orElseThrow(() -> new BadCredentialsException(UserErrorCode.BAD_CREDENTIALS));
            String encryptPassword = encryptPassword(userGoogleInfoDTO.getAccessToken());
            currentUser.setPassword(encryptPassword);
        }

        JwtTokenDTO loginToken = getJwtTokenDTO(currentUser);

        //UserDTO
        UserDTO loggedInUser = UserDTO.builder().
                userId(currentUser.getId())
                .name(currentUser.getName())
                .email(currentUser.getEmail())
                .loginType(currentUser.getLoginType())
                .remainInterview(currentUser.getRemainInterview())
                .build();

        return UserAuthResponseDTO.builder()
                .user(loggedInUser)
                .token(loginToken)
                .build();
    }


    private UserGoogleInfoDTO getGoogleInfo(UserGoogleLoginRequestDTO userGoogleLoginRequestDTO) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization",
                userGoogleLoginRequestDTO.getTokenType() + " " + userGoogleLoginRequestDTO.getAccessToken());

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                GOOGLE_USERINFO_URL,
                HttpMethod.GET,
                entity,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            String userInfo = response.getBody();
            ObjectMapper mapper = new ObjectMapper();
            try {
                UserGoogleInfoDTO googleUserInfo = mapper.readValue(userInfo, UserGoogleInfoDTO.class);
                googleUserInfo.setAccessToken(userGoogleLoginRequestDTO.getAccessToken());
                log.info("GOOGLE LOGIN : Getting {} google account info...", googleUserInfo.getName());
                return googleUserInfo;
            } catch (Exception e) {
                throw new JsonParseException(UserErrorCode.GOOGLE_ACCOUNT_PARSE_ERROR);
            }
        } else {
            throw new GoogleAccountRetrieveException(UserErrorCode.GOOGLE_ACCOUNT_RETRIEVE_ERROR);
        }
    }

    public boolean checkDuplicateEmail(DuplicateEmailRequestDTO requestDTO) {
        return userRepository.existsByEmail(requestDTO.getEmail());
    }

    public UserAuthResponseDTO guestSignUpAndLogin() {
        int length = 10;
        String randomId = RandomStringUtils.randomAlphanumeric(length);
        String guestEmail = "guest_" + randomId + "@example.com";
        String randomPassword = RandomStringUtils.randomAlphanumeric(length);
        UserEmailSignUpRequestDTO guestSignUpRequestDTO = UserEmailSignUpRequestDTO.builder()
                .email(guestEmail)
                .name("guest")
                .password(randomPassword)
                .build();
        UserDTO guestUser = signUp(guestSignUpRequestDTO, LoginType.GUEST);
        UserEmailLoginRequestDTO guestLoginRequestDTO = UserEmailLoginRequestDTO.builder()
                .email(guestUser.getEmail())
                .password(guestSignUpRequestDTO.getPassword())
                .build();
        return emailLogin(guestLoginRequestDTO, LoginType.GUEST);
    }

    // 게스트 일반 유저로 회원가입
    public UserAuthResponseDTO upgradeGuestToRegular(UserEmailSignUpRequestDTO userEmailSignUpRequestDTO) {
        User guestUser = getCurrentUser();

        if (userRepository.existsByEmail(userEmailSignUpRequestDTO.getEmail())) {
            throw new DuplicateEmailException(UserErrorCode.DUPLICATE_USER);
        }

        String hashedPassword = encryptPassword(userEmailSignUpRequestDTO.getPassword());

        guestUser.setName(userEmailSignUpRequestDTO.getName());
        guestUser.setEmail(userEmailSignUpRequestDTO.getEmail());
        guestUser.setPassword(hashedPassword);
        guestUser.setLoginType(LoginType.EMAIL);

        userRepository.save(guestUser);

        JwtTokenDTO loginToken = getJwtTokenDTO(guestUser);

        UserDTO loggedInUser = UserDTO.builder()
                .userId(guestUser.getId())
                .name(guestUser.getName())
                .email(guestUser.getEmail())
                .loginType(guestUser.getLoginType())
                .remainInterview(guestUser.getRemainInterview())
                .build();

        return UserAuthResponseDTO.builder()
                .user(loggedInUser)
                .token(loginToken)
                .build();
    }

    public UserAuthResponseDTO guestGoogleSignUpAndLogin(UserGoogleLoginRequestDTO userGoogleLoginRequestDTO) {
        User guestUser = getCurrentUser();
        UserGoogleInfoDTO userGoogleInfoDTO = getGoogleInfo(userGoogleLoginRequestDTO);

        guestUser.setName(userGoogleInfoDTO.getName());
        guestUser.setEmail(userGoogleInfoDTO.getEmail());
        String encryptedIdToken = PasswordEncrypter.encrypt(userGoogleInfoDTO.getAccessToken());
        guestUser.setPassword(encryptedIdToken);
        guestUser.setLoginType(LoginType.GOOGLE);
        userRepository.save(guestUser);

        JwtTokenDTO loginToken = getJwtTokenDTO(guestUser);

        UserDTO loggedInUser = UserDTO.builder()
                .userId(guestUser.getId())
                .name(guestUser.getName())
                .email(guestUser.getEmail())
                .loginType(guestUser.getLoginType())
                .remainInterview(guestUser.getRemainInterview())
                .build();

        return UserAuthResponseDTO.builder()
                .user(loggedInUser)
                .token(loginToken)
                .build();

    }


    // 사용자 삭제
    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotExistIdException(UserErrorCode.NOT_EXIST_USER));
        userRepository.delete(user);
    }

    // 사용자 비밀번호 변경
    public void changeUserPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotExistIdException(UserErrorCode.NOT_EXIST_USER));
        String newEncryptedPassword = PasswordEncrypter.encrypt(newPassword);
        user.setPassword(newEncryptedPassword);
        userRepository.save(user);
    }


    // 현재 로그인한 유저 정보 반환
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        return userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }


    public UserDTO getCurrentUserDTO() {
        User user = getCurrentUser();
        Resume defaultResume = null;
        List<Resume> resumes = resumeRepository.findByUser(user);
        for (Resume resume : resumes) {
            if (resume.isDefault()) {
                defaultResume = resume;
                break;
            }
        }
        return UserDTO.builder().
                userId(user.getId())
                .name(user.getName())
                .loginType(user.getLoginType())
                .email(user.getEmail())
                .defaultResume(defaultResume != null ? defaultResume.getName() : null)
                .remainInterview(REMAIN_INTERVIEW)
                .build();
    }


    // 사용자 유저 정보 반환
    private User getUser(UserEmailSignUpRequestDTO userEmailSignUpRequestDTO, String encryptedPassword,
                         LoginType loginType) {
        User user = new User();
        user.setPassword(encryptedPassword);
        user.setName(userEmailSignUpRequestDTO.getName());
        user.setEmail(userEmailSignUpRequestDTO.getEmail());
        user.setRole(RoleType.ROLE_USER);
        user.setRemainInterview(REMAIN_INTERVIEW);
        user.setLoginType(loginType);
        return user;
    }

    // 비밀번호 암호화
    private String encryptPassword(String password) {
        return PasswordEncrypter.encrypt(password);
    }

    /**
     * 사용자 정보를 이용하여 토큰 발행하는 메소드
     *
     * @param user 사용자 정보
     * @return JwtTokenDTO
     */
    private JwtTokenDTO getJwtTokenDTO(User user) {
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                user.getPassword(),
                customUserDetails.getAuthorities()
        );

        return tokenProvider.createToken(authenticationToken);
    }
}
