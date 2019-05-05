package cn.springcloud.bamboo.ribbon;

import java.io.IOException;
import java.net.URI;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import cn.springcloud.bamboo.BambooAppContext;
import cn.springcloud.bamboo.BambooRequest;
import cn.springcloud.bamboo.ConnectPointContext;
import cn.springcloud.bamboo.autoconfig.properties.BambooProperties;
import cn.springcloud.bamboo.utils.WebUtils;
import cn.springcloud.bamboo.web.RequestIpKeeper;

/**
 * 用于@LoadBalance 标记的 RestTemplate，主要作用是用来获取request的相关信息，为后面的路由提供数据基础。
 */
public class BambooClientHttpRequestIntercptor implements ClientHttpRequestInterceptor {

	private BambooProperties bambooProperties;

	public BambooClientHttpRequestIntercptor(BambooProperties bambooProperties) {
		this.bambooProperties = bambooProperties;
	}

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {

		URI uri = request.getURI();
		BambooRequest.Builder bambooReqBuilder = BambooRequest.builder().serviceId(uri.getHost()).uri(uri.getPath())
				.ip(RequestIpKeeper.getRequestIp()).addMultiHeaders(request.getHeaders())
				.addMultiParams(WebUtils.getQueryParams(uri.getQuery()));
		// InheritableThreadLocal
		if (bambooProperties.getBambooRequest().isLoadBody()) {
			bambooReqBuilder.requestBody(body);
		}

		BambooRequest bambooRequest = bambooReqBuilder.build();

		ConnectPointContext connectPointContext = ConnectPointContext.builder().bambooRequest(bambooRequest).build();
		try {
			BambooAppContext.getBambooRibbonConnectionPoint().executeConnectPoint(connectPointContext);
			return execution.execute(request, body);
		} finally {
			BambooAppContext.getBambooRibbonConnectionPoint().shutdownconnectPoint();
		}
	}
}
