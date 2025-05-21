package com.dju.woojiman.global.config;

import com.dju.woojiman.global.security.JwtTokenProvider;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
public class StompHandler implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public StompHandler(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        final StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT == accessor.getCommand()) {
            String bearerToken = accessor.getFirstNativeHeader("Authorization");

            if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
                jwtTokenProvider.validateToken(bearerToken.substring(7));
                return message;
            }

            jwtTokenProvider.validateToken(bearerToken);
        }

        return message;
    }

}
