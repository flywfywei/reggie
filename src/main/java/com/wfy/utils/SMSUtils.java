package com.wfy.utils;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.google.gson.Gson;
import darabonba.core.client.ClientOverrideConfiguration;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 短信发送工具类
 */
public class SMSUtils {

    /**
     * 发送短信
     * @param param 参数
     */
    public static void sendMessage (String phoneNumbers, int param) throws ExecutionException, InterruptedException {
        // 配置凭证认证信息，包括 ak、secret、token
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId("LTAI5tD9UHtnehrpU1oyhow8")
                .accessKeySecret("EthFfcdkCRVo0dBVX8ObynpeRc4rEw")
                .build());
        AsyncClient client = AsyncClient.builder()
                .region("cn-hangzhou") // 电信区域代码
                .credentialsProvider(provider)
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                .setEndpointOverride("dysmsapi.aliyuncs.com")
                )
                .build();
        // API请求参数设置，此处除 phoneNumbers、 code 外全部写死
        SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                .signName("阿里云短信测试")
                .templateCode("SMS_154950909")
                .phoneNumbers(phoneNumbers)
                .templateParam("{\"code\":\"" + param + "\"}")
                .build();
        // 异步获取API请求的返回值
        CompletableFuture<SendSmsResponse> response = client.sendSms(sendSmsRequest);
        // 同步获取API请求的返回值
        SendSmsResponse resp = response.get();
        System.out.println(new Gson().toJson(resp));

        // 最后，关闭客户端
        client.close();
    }

}
