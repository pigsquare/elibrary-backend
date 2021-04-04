package db2.elibrary.util;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AliPayUtil{
    public String  payFee(Double fee, String barCode, String subject) throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(
                "https://openapi.alipaydev.com/gateway.do",
                "2021000119644643",
                "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCmp36sDNSrjsSyDiLuVRFz37JRdBKdWa/dbmWpepZ2Jp4A+mehVSBt9Y80lOWk9CRvbBMqoQNAFzYn07RoUZQgJGSi6c8k2dHzjDI3aMiQ591C01Iv4GmQWZqLfTwDhh39caTQR6G3PUSVhF9gZyINlvjYy0eakNfJOGyjCKYtu3CcG0zxuEpf5T4r0pjNz0v4JCivIBTHsd2/Y9DeL8tLp2jAT470ITvwu8/A/fCJ2IlufBTE8LLAkAqdUdC4rPRhbNuh/OHqI2whoyG1zOX8z6t+PF79wFTgOuZKhT7AFNupHej9ZS8WoV0hQ3epvU4B+mRzdDBHImkNX3wDDCUnAgMBAAECggEBAIQB2MzfvPXHuPZxTCoNKC3o2465qqUJDc0QTWbZu6ULIc0UjFG25GE1SaIU73KiENpReT0jHTf/Kt6RSy32sKZlLXH+Uh6Wiz+npX9OeNCPWSw9x7Q0xmmZEFm9cXQ5IDhJdIumseo7lb/5grMR/l9VXvgazTUZ1lOFZkgd6cP+ldHg6OmJlGgMg7gSfpD8w7L6XhMRKgp67Jb3BFeJuuIRbuJo+0bdhpcFqFpG+gFniRvysTY/zNSrGGu7KfdOThbWdaYgRdPTF+nQM6/InOl+9dvZv8/Bk052a6KWvI/WO1Ub0k7U1EIBfks1TLsktvEWct8H12/GscRzuSzsCAECgYEA1tyLiXXFvc3Zdms4CfGODMWz8E+FuXGsNXgZTf1sqD7p/aqe9SNZ3Lk1oJAVusVSy9kkQywXFKaWOUmtOD0y80gbBvRCdHVc06ZX3B5SqP0UZd470y+uyz3F9fUb0cDxATXA7XxHB05U66hJMvJB98JybvidS2VtAnt6V7mjgqcCgYEAxpARgWQbReewv1Obv5DOskco7Sb+dY2fwk6ceZMXjQlxtNcAlIehyY4tikjbfE4K1k1db1GsvwV2JLF66Cl0sPKihrM446HdOOf7ZScjYfRZtj6tcYXdmsGdZ9OvGeEuxAaZlSeHKOSOj/H/KSNVsKRhtgPXN5z0PTWXWvaTGYECgYB/EuDT43uXze8XQkJaXyqME0FglyeXjcsztFwC7CcfOlZZWFe607WJ/V/iHlU7qQhW143CzIi6BexC5Xg1ErzXdYXGjx/ekKrd4juES8rHdyZhORZYyJKwUVCLLvPNEjd3Z7s6uY2onINM/LKOFwk85xuW1iLxPINEPJMW3wv9cQKBgAzIW4gj5KmUZ8l5XW5wqQ3LDPRvbTQuINnGa8Zb1MlMkVhd+viU3rJ2B7J+TDrtXz86jchLzr4hYOCt2Ea33H5DsHjRctkco3w1WvbBFUMKEvhrgojrYD/FNRfpBb25xGP0NNDoUYgtZowXvSBt2OddPbkU+DbpUMPb/llpxECBAoGAQ1yQ+wowqphcp5rb2TU54aMHc5S817+WG8oWYiaYrT4uQIAdskVd/I1IZoedcB09IXgy3rT3TW3JgJHJbQjBhSL0dnb3s/Zqh80p1xT1or/ikL/y6UNe62LwtreqPIXx6lWKBCt4I/kydJOM7zGLrxSakTBDgh0eKDSM6IFSTm8=",
                "json","utf-8",
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDIgHnOn7LLILlKETd6BFRJ0GqgS2Y3mn1wMQmyh9zEyWlz5p1zrahRahbXAfCfSqshSNfqOmAQzSHRVjCqjsAw1jyqrXaPdKBmr90DIpIxmIyKXv4GGAkPyJ/6FTFY99uhpiq0qadD/uSzQsefWo0aTvP/65zi3eof7TcZ32oWpwIDAQAB",
                "RSA"
                );
        AlipayTradePayRequest request = new AlipayTradePayRequest();
        AlipayTradePayModel model = new AlipayTradePayModel();
        request.setBizModel(model);
        AlipayTradeQueryRequest queryRequest = new AlipayTradeQueryRequest();
        AlipayTradeQueryModel queryModel = new AlipayTradeQueryModel();
        queryRequest.setBizModel(queryModel);
        String outTradeNo = System.currentTimeMillis()+"";

        model.setOutTradeNo(outTradeNo);
        model.setSubject(subject);
        model.setTotalAmount("0"+new java.text.DecimalFormat("#.00").format(fee));
        model.setAuthCode(barCode);//沙箱钱包中的付款码
        model.setScene("bar_code");
        model.setTimeoutExpress("1m");

        AlipayTradePayResponse response = null;
        try {
            response = alipayClient.execute(request);
            System.out.println(response.getBody());
            String resultCode = response.getCode();
            int times=0;
            while (resultCode.equals("10003")&&times<=12){
                times++;
                log.info("交易进行中，5s后第"+times+"次查询。。。");
                Thread.sleep(5000);
                queryModel.setOutTradeNo(outTradeNo);
                queryModel.setTradeNo(response.getTradeNo());
                AlipayTradeQueryResponse queryResponse = alipayClient.execute(queryRequest);
                if(queryResponse.getCode().equals("10000")){
                    log.info(queryResponse.getTradeStatus());
                    if(queryResponse.getTradeStatus().equals("TRADE_SUCCESS")||queryResponse.getTradeStatus().equals("TRADE_FINISHED"))
                        resultCode = queryResponse.getCode();
                    if(queryResponse.getTradeStatus().equals("TRADE_CLOSED")){
                        resultCode = "40004";
                    }
                }

            }
            System.out.println(response.getTradeNo());
            if(resultCode.equals("10000"))
                return response.getTradeNo();
        } catch (AlipayApiException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
