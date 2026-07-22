package com.mypay.identity.services;

import com.mypay.identity.Exception.AppException;
import com.mypay.identity.Exception.ErrorCode;
import com.mypay.identity.dtos.Request.*;
import com.mypay.identity.dtos.Response.TokenResponse;
import com.mypay.identity.dtos.Response.UserResponse;
import com.mypay.identity.entities.*;
import com.mypay.identity.enums.AccountStatus;
import com.mypay.identity.repositories.*;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class AccountService {
    private final UserRepository _userRepository;
    private final RoleRepository _roleRepository;
    private final RefreshTokenRepository _refreshTokenRepository;
    private final LoginLogRepository _loginLogRepository;
    private final StringRedisTemplate _redisTemplate;
    private final JavaMailSender _mailSender;
    private final InvalidatedTokenRepository _invalidatedTokenRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Value("${jwt.signKey}")
    private String SIGNER_KEY;

    @Value("${jwt.valid-duration}")
    private long VALID_DURATION;

    public AccountService(UserRepository userRepository, RoleRepository roleRepository,
                          RefreshTokenRepository refreshTokenRepository, LoginLogRepository loginLogRepository,
                          InvalidatedTokenRepository invalidatedTokenRepository,StringRedisTemplate redisTemplate, JavaMailSender mailSender) {
        _userRepository = userRepository;
        _roleRepository = roleRepository;
        _refreshTokenRepository = refreshTokenRepository;
        _loginLogRepository = loginLogRepository;
        _redisTemplate = redisTemplate;
        _invalidatedTokenRepository = invalidatedTokenRepository;
        _mailSender = mailSender;
    }
    public String register (UserRegisterRequest request){
        if(_userRepository.existsByUsername(request.getUsername())){
            throw new AppException(ErrorCode.USERNAME_ALREADY_EXISTS);
        }
        var userRole = _roleRepository.findById("USER")
                .orElseThrow(() -> new AppException(ErrorCode.DEFAULT_ROLE_NOT_FOUND));

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .isActive(false)
                .email(request.getEmail())
                .status(AccountStatus.PENDING)
                .roles(roles)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        _userRepository.save(user);

        String otpCode = String.format("%06d", new Random().nextInt(999999));
        String redisKey = "OTP:" + request.getEmail();
        _redisTemplate.opsForValue().set(redisKey, otpCode, 3, TimeUnit.MINUTES);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(request.getEmail());
            message.setSubject("Mã xác thực tài khoản MyPay");
            message.setText("Chào " + request.getFullName() + ",\nMã OTP kích hoạt ví MyPay của bạn là: " + otpCode + "\nMã này có hiệu lực trong vòng 3 phút.");
            _mailSender.send(message);
        } catch (Exception e) {
            System.out.println("Lỗi gửi email: " + e.getMessage() + ". Nhớ kiểm tra lại cấu hình tài khoản SMTP Gmail!");
        }
        return "create account success";
    }

    public String verifyOtp(VerifyOtpRequest request) {
        String redisKey = "OTP:" + request.getEmail();

        String savedOtp = _redisTemplate.opsForValue().get(redisKey);

        if (savedOtp == null) {
            throw new AppException(ErrorCode.OTP_EXPIRED_OR_NOT_FOUND);
        }

        if (!savedOtp.equals(request.getOtpCode())) {
            throw new AppException(ErrorCode.OTP_INVALID);
        }


        User user = _userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND));

        user.setStatus(AccountStatus.ACTIVE);
        user.setActive(true);
        user.setUpdatedAt(LocalDateTime.now());
        _userRepository.save(user);
        _redisTemplate.delete(redisKey);

        return "Verify success, you can login now!";
    }

    public String resendOtp(ResendOtpRequest request){
        var user =  _userRepository.findByEmail(request.getEmail()).orElseThrow(()-> new AppException(ErrorCode.EMAIL_NOT_FOUND));
        if(user.getStatus() == AccountStatus.ACTIVE && user.isActive() == true){
            throw new AppException(ErrorCode.ACCOUNT_ACTIVED);
        }
        String otpCode = String.format("%06d", new Random().nextInt(999999));
        String redisKey = "OTP:" + request.getEmail();
        _redisTemplate.opsForValue().set(redisKey, otpCode, 3, TimeUnit.MINUTES);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(request.getEmail());
            message.setSubject("Gửi lại mã xác thực MyPay");
            message.setText("Mã OTP mới của bạn là: " + otpCode + ". Mã có hiệu lực trong 3 phút.");
            _mailSender.send(message);
        } catch (Exception e) {
            throw new AppException(ErrorCode.ERROR_SEND_EMAIL);
        }
        return "Đã gửi lại mã OTP. Vui lòng kiểm tra email!";
    }
    public TokenResponse login(LoginRequest request, String ipAddress, String deviceInfo) {
        User user = _userRepository.findByUsername(request.getUsername()).orElse(null);


        if (user == null) {
            saveLoginLog(request.getUsername(), ipAddress, deviceInfo, false, "Tài khoản không tồn tại");
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }

        if (user.isLocked() && user.getLockoutEnd() != null && user.getLockoutEnd().isAfter(LocalDateTime.now())) {
            saveLoginLog(user.getUsername(), ipAddress, deviceInfo, false, "Tài khoản đang bị khóa tạm thời");
            throw new AppException(ErrorCode.ACCOUNT_LOCKED);
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
            if (user.getFailedLoginAttempts() >= 5) {
                user.setLocked(true);
                user.setLockoutEnd(LocalDateTime.now().plusMinutes(15));
            }
            _userRepository.save(user);
            saveLoginLog(user.getUsername(), ipAddress, deviceInfo, false, "Sai mật khẩu");
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }


        if (user.getStatus() == AccountStatus.PENDING) {
            saveLoginLog(user.getUsername(), ipAddress, deviceInfo, false, "Chưa xác thực OTP");
            throw new AppException(ErrorCode.ACCOUNT_NOT_VERIFIED);
        }
        if (user.getStatus() == AccountStatus.BANNED) {
            saveLoginLog(user.getUsername(), ipAddress, deviceInfo, false, "Tài khoản bị cấm");
            throw new AppException(ErrorCode.ACCOUNT_BANNED);
        }


        user.setFailedLoginAttempts(0);
        user.setLocked(false);
        user.setLockoutEnd(null);
        _userRepository.save(user);


        String accessToken = generateToken(user);
        String refreshTokenString = UUID.randomUUID().toString();

        RefreshToken refreshToken = RefreshToken.builder()
                .token(refreshTokenString)
                .expiryDate(Instant.now().plus(7, ChronoUnit.DAYS))
                .user(user)
                .revoked(false)
                .build();
        _refreshTokenRepository.save(refreshToken);

        saveLoginLog(user.getUsername(), ipAddress, deviceInfo, true, "Thành công");

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenString)
                .build();
    }
    public TokenResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = _refreshTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_REFRESH_TOKEN));

        if (refreshToken.isRevoked() || refreshToken.getExpiryDate().isBefore(Instant.now())) {
            throw new AppException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }


        _refreshTokenRepository.delete(refreshToken);

        String newAccessToken = generateToken(refreshToken.getUser());
        String newRefreshTokenString = UUID.randomUUID().toString();

        RefreshToken newRefreshToken = RefreshToken.builder()
                .token(newRefreshTokenString)
                .expiryDate(Instant.now().plus(7, ChronoUnit.DAYS))
                .user(refreshToken.getUser())
                .revoked(false)
                .build();
        _refreshTokenRepository.save(newRefreshToken);

        return TokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshTokenString)
                .build();
    }


    public String changePassword(ChangePasswordRequest request) {
        User user = _userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.OLD_PASSWORD_INCORRECT);
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        _userRepository.save(user);

        return "Đổi mật khẩu thành công!";
    }

    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        String roles = user.getRoles().stream()
                .map(Role::getRoleId)
                .collect(Collectors.joining(" "));

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("mypay.com")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("roles", roles)
                .claim("accountId", user.getId())
                .build();

        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new AppException(ErrorCode.JWT_GENERATION_FAILED);
        }
    }


    private void saveLoginLog(String username, String ipAddress, String deviceInfo, boolean isSuccess, String reason) {
        LoginLog log = LoginLog.builder()
                .username(username)
                .ipAddress(ipAddress)
                .deviceInfo(deviceInfo)
                .isSuccess(isSuccess)
                .failureReason(reason)
                .loginTime(LocalDateTime.now())
                .build();
        _loginLogRepository.save(log);
    }
    public String logout(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            String jwtId = claims.getJWTID();
            Date expiryTime = claims.getExpirationTime();
            String username = claims.getSubject();

            if (expiryTime.after(new Date())) {
                InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                        .id(jwtId)
                        .expiryTime(expiryTime)
                        .build();
                _invalidatedTokenRepository.save(invalidatedToken);
            }
            User user = _userRepository.findByUsername(username).orElse(null);
            if (user != null) {
                _refreshTokenRepository.findAll().stream()
                        .filter(rt -> rt.getUser() != null &&
                                rt.getUser().getId().equals(user.getId()))
                        .forEach(_refreshTokenRepository::delete);
            }

            return "Đăng xuất thành công!";
        } catch (java.text.ParseException e) {
            throw new AppException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
    }
    public List<UserResponse> getAll(){
        List<User> users = _userRepository.findAll();
        List<UserResponse> userResponses = new ArrayList<>();
        for(User user : users){
            UserResponse response = new UserResponse();
            response.setId(user.getId());
            response.setUsername(user.getUsername());
            response.setFullName(user.getFullName());
            userResponses.add(response);
        }

        return userResponses;

    }
    public UserResponse getUserByToken(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
        String username =  claims.getSubject();
        User user = _userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setFullName(user.getFullName());
        return response;

    }
}
