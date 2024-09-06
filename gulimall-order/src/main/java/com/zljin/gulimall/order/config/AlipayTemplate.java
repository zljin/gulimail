package com.zljin.gulimall.order.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.zljin.gulimall.order.vo.PayVo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Data
public class AlipayTemplate {
    //在支付宝创建的应用的id
    private String APP_ID = "2016092200568607";

    // 商户私钥，您的PKCS8格式RSA2私钥
    private String MERCHANT_PRIVATE_KEY = "";
    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    private String ALIPAY_PUBLIC_KEY = "";
    // 服务器[异步通知]页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 (需要内网穿透)
    // 支付宝会悄悄的给我们发送一个请求，告诉我们支付成功的信息
    private String NOTIFY_URL = "http://**.natappfree.cc/payed/notify";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 (需要内网穿透)
    //同步通知，支付成功，一般跳转到成功页
    private String RETURN_URL = "http://member.gulimall.com/memberOrder.html";

    // 签名方式
    private String SIGN_TYPE = "RSA2";

    // 字符编码格式
    private String CHARSET = "utf-8";

    private String TIME_OUT = "30m";

    // 支付宝网关； https://openapi.alipaydev.com/gateway.do
    private String GATEWAY_URL = "https://openapi.alipaydev.com/gateway.do";

    public String pay(PayVo vo) throws AlipayApiException {

        //1、根据支付宝的配置生成一个支付客户端
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL,
                APP_ID, MERCHANT_PRIVATE_KEY, "json",
                CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);

        //2、创建一个支付请求 //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(RETURN_URL);
        alipayRequest.setNotifyUrl(NOTIFY_URL);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = vo.getOut_trade_no();
        //付款金额，必填
        String total_amount = vo.getTotal_amount();
        //订单名称，必填
        String subject = vo.getSubject();
        //商品描述，可空
        String body = vo.getBody();

        alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\","
                + "\"total_amount\":\"" + total_amount + "\","
                + "\"subject\":\"" + subject + "\","
                + "\"body\":\"" + body + "\","
                + "\"timeout_express\":\"" + TIME_OUT + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        String result = alipayClient.pageExecute(alipayRequest).getBody();

        //会收到支付宝的响应，响应的是一个页面，只要浏览器显示这个页面，就会自动来到支付宝的收银台页面
        log.info("支付宝的响应：{}", result);
        return result;
    }
}
