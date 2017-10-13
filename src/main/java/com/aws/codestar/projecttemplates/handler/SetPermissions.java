package com.aws.codestar.projecttemplates.handler;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.aws.codestar.projecttemplates.GatewayResponse;

/**
 * Handler for requests to Lambda function.
 */
public class SetPermissions implements RequestHandler<SNSEvent, Object> {

    private static final AmazonS3 s3 = new AmazonS3Client(new DefaultAWSCredentialsProviderChain());
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
