package com.wangku.miaodan.web;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jpay.ext.kit.IpKit;
import com.jpay.ext.kit.PaymentKit;
import com.jpay.ext.kit.StrKit;
import com.jpay.weixin.api.WxPayApi;
import com.jpay.weixin.api.WxPayApi.TradeType;
import com.jpay.weixin.api.WxPayApiConfig;
import com.wangku.miaodan.core.interceptor.LoginInterceptor;
import com.wangku.miaodan.core.model.PaymentOrder;
import com.wangku.miaodan.core.model.Recharge;
import com.wangku.miaodan.core.service.IPaymentOrderService;
import com.wangku.miaodan.core.service.IRechargeService;
import com.wangku.miaodan.core.service.IUserService;
import com.wangku.miaodan.utils.FileUtil;
import com.wangku.miaodan.utils.XmlUtil;

@Controller
@RequestMapping("/pay")
public class PayMentController {
	
	private static final Logger LOG = Logger.getLogger(PayMentController.class);
	
	//H5支付参数
	private static final String APP_ID = "wx48d18bd5531afd7f";
	private static final String MCH_ID = "1513645561";		//商户号
	private static final String API_SECRET =   "23c6b37ac44929d4b3a1fb20cca8ac62";	//支付秘钥
	
	@Autowired
	private IRechargeService rechargeService;
	
	@Autowired
	private IPaymentOrderService paymentService;
	
	@Autowired
	private IUserService userService;
	
	@RequestMapping("")
	@ResponseBody
	public Map<String, Object> pay(Long orderId, HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		// 验证用户
		if (LoginInterceptor.getMobile(request) == null) {
			result.put("code", 602);
			result.put("msg", "用户未登录，请刷新页面");
			return result;
		}
		
		Recharge recharge = rechargeService.getDetailByIdAndMobile(orderId, LoginInterceptor.getMobile(request));
		
		// 验证订单是否存在
		if (recharge == null) {
			result.put("code", 601);
			result.put("msg", "订单不存在");
			return result;
		}
		
		// 验证订单是否可以支付
/*		if (recharge.getStatus() != 0) {
			result.put("code", 602);
			result.put("msg", "不可支付订单");
		}*/
		
		Map<String, String> params = createUOrder(recharge, request);
		LOG.info("微信统一下单接口:" + params);	
		String restXml = WxPayApi.pushOrder(false, params);
		LOG.info("微信统一下单接口返回值:" + restXml);
		Map<String, String> restMap = PaymentKit.xmlToMap(restXml);
		String returnCode = restMap.get("return_code");
		String returnMsg = restMap.get("return_msg");
		if (!PaymentKit.codeIsOK(returnCode)) {
			LOG.error("订单创建失败:" +  returnMsg);
			result.put("code", 605);
			result.put("msg", "订单创建失败:" + returnMsg);		
			return result;
		}
		
		String resultCode = restMap.get("result_code");
		if (!PaymentKit.codeIsOK(resultCode)) {
			LOG.error("订单创建失败:" +  returnMsg);
			result.put("code", 605);
			result.put("msg", "订单创建失败:" + returnMsg);	
			return result;
		}
		
		String prepayId = restMap.get("prepay_id");
		String mwebUrl = restMap.get("mweb_url");
		LOG.info("微信统一订单创建成功,prepay_id:" + prepayId + ",mweb_url:" + mwebUrl);
		
		// 微信支付订单创建成功
		PaymentOrder order = new PaymentOrder();
		order.setAppid(restMap.get("appid"));
		order.setMchId(restMap.get("mch_id"));
		order.setNonceStr(restMap.get("nonce_str"));
		order.setSign(restMap.get("sign"));
		order.setNumber(recharge.getNumber());
		order.setTradeType(restMap.get("trade_type"));
		int id = paymentService.add(order);
		result.put("code", 200);
		result.put("msg", "订单创建成功");
		result.put("mweb_url", restMap.get("mweb_url"));	
		return result;
	}
	
	@RequestMapping("/order/notify")
	@ResponseBody
	public String notify(HttpServletRequest request, HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/xml");
		Map resultMap = new HashMap();
		try {
			String resxml = FileUtil.readInputStream2String(request.getInputStream());
			Map<String, String> restmap = XmlUtil.xmlParse(resxml);
			LOG.info("支付结果通知：" + restmap);
			String out_trade_no = restmap.get("out_trade_no");
			if ("SUCCESS".equals(restmap.get("return_code"))) {
				PaymentOrder order = new PaymentOrder();
				order.setNumber(out_trade_no);
				order.setStatus(1);
				paymentService.notifyResult(order);
			} else {
				PaymentOrder order = new PaymentOrder();
				order.setNumber(out_trade_no);
				order.setStatus(2);
				order.setFailReason(restmap.get("err_code_des"));
				paymentService.notifyResult(order);
				LOG.error("订单支付通知：支付失败，" + restmap.get("err_code") + ":" + restmap.get("err_code_des"));
				resultMap.put("return_code","FAIL");
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			resultMap.put("return_code","FAIL");
		}
		return XmlUtil.xmlFormat(resultMap,true);
	}	
	
	private Map<String, String> createUOrder(Recharge recharge, HttpServletRequest request) {
		String ip = IpKit.getRealIp(request);
		if (StrKit.isBlank(ip)) {
			ip = "127.0.0.1";
		}
		Map<String, Object> sceneInfo = new HashMap<String, Object>();
		Map<String, String> h5Info = new HashMap<String, String>();
		h5Info.put("type", "Wap");
		h5Info.put("wap_url", "http://wwww.99114.com");
		h5Info.put("wap_name", "今日推单");
		sceneInfo.put("h5_info", h5Info);
		WxPayApiConfig apiConfig = WxPayApiConfig.New()
				.setAppId(APP_ID)// 公众号ID
				.setMchId(MCH_ID)// 商户号ID
				.setPaternerKey(API_SECRET)
				.setTradeType(TradeType.MWEB)// 交易类型()
				.setNonceStr(recharge.getNumber())// 随机字符串(订单ID)
				.setBody("今日推单充值")//商品简单描述
				.setAttach("jrtdPay")// 附加数据
				.setOutTradeNo(recharge.getNumber())// 商户侧订单ID
				.setTotalFee(String.valueOf(recharge.getSum().multiply(new BigDecimal(100)).intValue()))// 交易总额
				.setSpbillCreateIp("10.8.0.18")// 用户端IP
				.setNotifyUrl("http://www.99114.com/pay/order/notify")// 支付成功异步通知地址
				.setSceneInfo("{\"h5_info\":{\"wap_name\":\"今日推单\",\"wap_url\":\"http://wwww.99114.com\",\"type\":\"Wap\"}}");// 场景信息
		Map<String, String> build = apiConfig.build();
		//String getsignkey = WxPayApi.getsignkey(MCH_ID, API_SECRET);
		return build;
	}
	
}
