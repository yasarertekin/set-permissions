package com.aws.codestar.projecttemplates.handler;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Handler for requests to Lambda function.
 */
public class SetPermissions implements RequestHandler<SNSEvent, Object> {

    private static final AmazonS3 s3 = new AmazonS3Client(new ProfileCredentialsProvider());
    public String handleRequest(final SNSEvent event, final Context context) {
        LambdaLogger logger = context.getLogger();

        String message = event.getRecords().get(0).getSNS().getMessage();
        String sourceKey = "";
        try {
            sourceKey = URLDecoder.decode(message.replace("/\\+/g", " "),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        logger.log("Source key: " + sourceKey);
        String bUcketName = "serverless-video-transcoded-ps";

        PutObjectRequest putObjectRequest = new PutObjectRequest(bUcketName,sourceKey,CannedAccessControlList.PublicRead.toString());
        s3.putObject(putObjectRequest);
        return "ok";
    }
}
