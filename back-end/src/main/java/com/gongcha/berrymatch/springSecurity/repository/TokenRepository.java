package com.gongcha.berrymatch.springSecurity.repository;

import com.gongcha.berrymatch.springSecurity.constants.ProviderInfo;
import com.gongcha.berrymatch.springSecurity.domain.Token;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends MongoRepository<Token, String> {
    Optional<Token> findByIdentifierAndProviderInfo(String identifier, ProviderInfo providerInfo);

    List<Token> findAllByIdentifierAndProviderInfo(String identifier, ProviderInfo providerInfo);

    void deleteByIdentifierAndProviderInfo(String identifier, ProviderInfo providerInfo);

    void deleteAllByIdentifierAndProviderInfo(String identifier, ProviderInfo providerInfo);
}
