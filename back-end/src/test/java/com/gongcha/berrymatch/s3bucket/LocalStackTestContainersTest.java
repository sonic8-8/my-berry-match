package com.gongcha.berrymatch.s3bucket;

import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

@Testcontainers
public class LocalStackTestContainersTest {
    private static final DockerImageName LOCALSTACK_IMAGE = DockerImageName.parse("localstack/localstack");

    @Container
    LocalStackContainer localStackContainer = new LocalStackContainer(LOCALSTACK_IMAGE)
            .withServices(S3);

//    @Test
//    void test(){
//        AmazonS3 amazonS3 = AmazonS3ClientBuilder
//                .standard()
//                .withEndpointConfiguration(localStackContainer.getEndpointConfiguration(S3))
//                .withCredentials(localStackContainer.getDefaultCredentialsProvider())
//                .build();
//
//        String bucketName = "test-bucket";
//        amazonS3.createBucket(bucketName);
//        System.out.println(bucketName +" 버킷 생성");
//
//        String key = "1234";
//        String content = "test-content";
//        amazonS3.putObject(bucketName, key, content);
//        System.out.println("파일을 업로드하였습니다. key=" + key +", content=" + content);
//
//        S3Object object = amazonS3.getObject(bucketName, key);
//        System.out.println("파일을 가져왔습니다. = " + object.getKey());
//    }
}
