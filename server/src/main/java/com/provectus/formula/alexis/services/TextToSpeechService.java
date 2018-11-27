package com.provectus.formula.alexis.services;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.provectus.formula.alexis.texttospeech.AudioStreamHandler;
import com.provectus.formula.alexis.texttospeech.TextToSpeechEngine;
import com.provectus.formula.alexis.texttospeech.impl.TextToSpeechEngineImpl;
import com.provectus.formula.alexis.utils.S3StreamHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TextToSpeechService {

    private TextToSpeechEngine textToSpeechEngine;

    @Value("${alexis.bucket.name:formula1.alexis}")
    private String bucketName;

    public String convertToSpeech(String textToConvert) {
        textToSpeechEngine =  this.getTextToSpeechEngine();
        //Create related entity to obtain unic fileName
        //Call saveToS3 method with fileName obtained from entity
        String fileName = UUID.randomUUID().toString() + ".mp3";
        textToSpeechEngine.getAudioStreamAsync(textToConvert, new S3StreamHandler(bucketName,fileName));

        return fileName;
    }

    public TextToSpeechEngine getTextToSpeechEngine() {
        if (textToSpeechEngine==null){
            textToSpeechEngine=new TextToSpeechEngineImpl();
        }
        return textToSpeechEngine;
    }

    public void deleteFromS3(String fileNameOnS3, String bucketName) {

        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(Regions.US_EAST_1)
                    .withCredentials(new ProfileCredentialsProvider())
                    .build();
            DeleteObjectRequest request = new DeleteObjectRequest(bucketName, fileNameOnS3);
            s3Client.deleteObject(request);

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
