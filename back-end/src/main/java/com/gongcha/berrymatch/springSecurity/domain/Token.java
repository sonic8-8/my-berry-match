package com.gongcha.berrymatch.springSecurity.domain;

import com.gongcha.berrymatch.springSecurity.constants.ProviderInfo;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "Token")
@NoArgsConstructor
public class Token {
    @Id
    private String identifier;

    private String token;

    private ProviderInfo providerInfo;

    @Builder
    public Token(String identifier, String token, ProviderInfo providerInfo) {
        this.identifier = identifier;
        this.token = token;
        this.providerInfo = providerInfo;
    }
}
