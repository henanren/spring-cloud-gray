package cn.springcloud.gray.zuul;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ImmutableMap;

/**
 * Created by saleson on 2017/10/18.
 */
@RestController
@RequestMapping("/api/test2")
public class TestResource {

	@RequestMapping(value = "/testGet", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> testGet(HttpServletRequest request) {
		return ImmutableMap.of("TestResource", "success.", "service-b-result", "");
	}

}
