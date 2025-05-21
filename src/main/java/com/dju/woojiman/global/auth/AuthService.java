package com.dju.woojiman.global.auth;

import com.dju.woojiman.domain.user.OAuth2Provider;
import com.dju.woojiman.domain.user.OAuthAccount;
import com.dju.woojiman.domain.user.UserRepository;
import com.dju.woojiman.domain.user.Users;
import com.dju.woojiman.global.security.JwtTokenProvider;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class AuthService {

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final RedisTemplate<Object, Object> redisTemplate;

    public AuthService(JwtTokenProvider jwtTokenProvider, UserRepository userRepository, RedisTemplate<Object, Object> redisTemplate) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
    }

    @Value("${client-id}")
    private String CLIENT_ID;

    public TokenResult processingGoogleUser(IdTokenDto idToken) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY)
                    .setAudience(Collections.singletonList(CLIENT_ID))
                    .build();

            GoogleIdToken token = verifier.verify(idToken.getIdToken());
            if (token == null) {
                throw new RuntimeException("Invalid token");
            }

            GoogleIdToken.Payload payload = token.getPayload();
            String email = payload.getEmail();
            String nickname = (String) payload.get("name");
            String profileImgUrl = (String) payload.get("picture");
            String subjectId = payload.getSubject();

            Optional<Users> userOptional = userRepository.findByEmail(email);
            if (userOptional.isEmpty()) {
                Set<OAuthAccount> accounts = Set.of(new OAuthAccount(OAuth2Provider.GOOGLE, subjectId));
                Users newUser = Users.builder()
                        .email(email)
                        .nickname(nickname)
                        .oauthAccounts(accounts)
                        .profileImageUrl(profileImgUrl)
                        .build();
                userRepository.save(newUser);

                return new TokenResult(
                        jwtTokenProvider.createAccessToken(newUser.getId()),
                        jwtTokenProvider.createRefreshToken(newUser.getId()),
                        null,
                        false
                );
            }

            Users existingUser = userOptional.get();
            boolean hasOtherProvider = existingUser.getOauthAccounts().stream()
                    .anyMatch(account -> account.getProvider() != OAuth2Provider.GOOGLE);

            if (hasOtherProvider) {
                String linkToken = UUID.randomUUID().toString();
                redisTemplate.opsForValue().set(
                        "linkToken:" + linkToken,
                        new LinkInfo(email, "GOOGLE", subjectId),
                        Duration.ofMinutes(5)
                );
                return new TokenResult(null, null, linkToken, existingUser.isProfileCompleted());
            }

            return new TokenResult(
                    jwtTokenProvider.createAccessToken(existingUser.getId()),
                    jwtTokenProvider.createRefreshToken(existingUser.getId()),
                    null,
                    existingUser.isProfileCompleted()
            );

        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException("Google token processing error", e);
        }
    }
}
