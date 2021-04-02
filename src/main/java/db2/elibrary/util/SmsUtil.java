package db2.elibrary.util;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;

import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
@Slf4j
public class SmsUtil {

    @Value("${tencent.secret-id}")
    private String secretId;

    @Value("${tencent.secret-key}")
    private String secretKey;

    @Value("${tencent.appid}")
    private String appId;

    private String templateId;
    private String sessionContext;
    private String[] phoneNumberSet1;
    private String[] templateParamSet1;

    public String  sendSms(){
        String res = null;
        try{

            Credential cred = new Credential(secretId, secretKey);

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("sms.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            SmsClient client = new SmsClient(cred, "", clientProfile);

            SendSmsRequest req = new SendSmsRequest();

            req.setPhoneNumberSet(phoneNumberSet1);

            req.setTemplateID(templateId);
            req.setSign("小组作业助手");

            req.setTemplateParamSet(templateParamSet1);

            req.setSessionContext(sessionContext);
            req.setSmsSdkAppid(appId);

            SendSmsResponse resp = client.SendSms(req);
            res = resp.getRequestId();

            log.info(SendSmsResponse.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            log.error(e.toString());
        }
        return res;
    }
}
