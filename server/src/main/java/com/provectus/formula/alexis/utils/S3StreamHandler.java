package com.provectus.formula.alexis.utils;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.provectus.formula.alexis.texttospeech.AudioStreamHandler;
import java.io.InputStream;


public class S3StreamHandler implements AudioStreamHandler {

    private String bucketName;
    private String fileName;

    public S3StreamHandler(String bucketName, String fileName) {
        this.bucketName = bucketName;
        this.fileName=fileName;
    }


    @Override
    public void processAudioStream(InputStream audio) {

        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(Regions.US_EAST_1)
                    .withCredentials(new ProfileCredentialsProvider())
                    .build();

            PutObjectRequest request = new PutObjectRequest(bucketName, fileName, audio, null);
            s3Client.putObject(request);
        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }
    }
}
