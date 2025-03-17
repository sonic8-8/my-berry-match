package com.gongcha.berrymatch.springSecurity.service;


import com.gongcha.berrymatch.exception.BusinessException;
import com.gongcha.berrymatch.exception.ErrorCode;
import com.gongcha.berrymatch.springSecurity.constants.ProviderInfo;
import com.gongcha.berrymatch.springSecurity.domain.Token;
import com.gongcha.berrymatch.springSecurity.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;

    @Transactional
    public String save(Token token) {
        Token savedToken = tokenRepository.save(token);
        return savedToken.getIdentifier();
    }

    public Token findByIdentifierAndProviderInfo(String identifier, ProviderInfo providerInfo) {
        return tokenRepository.findByIdentifierAndProviderInfo(identifier, providerInfo)
                .orElseThrow(() -> new BusinessException(ErrorCode.JWT_NOT_FOUND_IN_DB));
    }

    public List<Token> findAllByIdentifierAndProviderInfo(String identifier, ProviderInfo providerInfo) {
        return tokenRepository.findAllByIdentifierAndProviderInfo(identifier, providerInfo);
    }

    public boolean isRefreshHijacked(String identifier, String refreshToken, ProviderInfo providerInfo) {
        Token token = findByIdentifierAndProviderInfo(identifier, providerInfo);
        return !token.getToken().equals(refreshToken);
    }

    public void deleteAllByIdentifierAndProviderInfo(String identifier, ProviderInfo providerInfo) {
        tokenRepository.deleteAllByIdentifierAndProviderInfo(identifier, providerInfo);
    }

    public boolean isRefreshDuplicate(String identifier, ProviderInfo providerInfo) {
        List<Token> tokens = tokenRepository.findAllByIdentifierAndProviderInfo(identifier, providerInfo);
        return (tokens.size() >= 2);
    }

}
