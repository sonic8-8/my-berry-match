package com.gongcha.berrymatch.s3bucket;

import org.springframework.boot.test.context.TestConfiguration;
import org.testcontainers.containers.localstack.LocalStackContainer;

@TestConfiguration
public class LocalStackS3Config {

    public LocalStackContainer localStackContainer() {
        return new LocalStackContainer()
                .withServices(LocalStackContainer.Service.S3);
    }
}
