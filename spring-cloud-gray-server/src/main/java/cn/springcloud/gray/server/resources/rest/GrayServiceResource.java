package cn.springcloud.gray.server.resources.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import cn.springcloud.gray.core.GrayInstance;
import cn.springcloud.gray.core.GrayPolicy;
import cn.springcloud.gray.core.GrayPolicyGroup;
import cn.springcloud.gray.core.GrayService;
import cn.springcloud.gray.core.GrayServiceManager;
import cn.springcloud.gray.server.api.GrayServiceApi;

@RestController
public class GrayServiceResource implements GrayServiceApi {
	@Autowired
	private GrayServiceManager grayServiceManager;

	@Override
	public List<GrayService> services() {
		return new ArrayList<>(grayServiceManager.allGrayService());
	}

	@Override
	public List<GrayService> enableServices() {
		Collection<GrayService> grayServices = grayServiceManager.allGrayService();
		List<GrayService> serviceList = new ArrayList<>(grayServices.size());
		for (GrayService grayService : grayServices) {
			if (grayService.isOpenGray()) {
				serviceList.add(grayService.takeNewOpenGrayService());
			}
		}

		return serviceList;
	}

	@Override
	public GrayService service(@PathVariable("serviceId") String serviceId) {
		return grayServiceManager.getGrayService(serviceId);
	}

	@Override
	public List<GrayInstance> instances(@PathVariable("serviceId") String serviceId) {
		return grayServiceManager.getGrayService(serviceId).getGrayInstances();
	}

	@Override
	public GrayInstance getInstance(@PathVariable("serviceId") String serviceId, String instanceId) {
		return grayServiceManager.getGrayInstane(serviceId, instanceId);
	}

	@Override
	public ResponseEntity<Void> delInstance(@PathVariable("serviceId") String serviceId, String instanceId) {
		grayServiceManager.deleteGrayInstance(serviceId, instanceId);
		return ResponseEntity.ok().build();
	}

	@Override
	public ResponseEntity<Void> instance(@PathVariable("serviceId") String serviceId, @RequestBody GrayInstance instance) {
		instance.setServiceId(serviceId);
		grayServiceManager.addGrayInstance(instance);
		return ResponseEntity.ok().build();
	}

	@Override
	public List<GrayPolicyGroup> policyGroups(@PathVariable("serviceId") String serviceId, String instanceId) {
		System.out.println(1111);
		return grayServiceManager.getGrayInstane(serviceId, instanceId).getGrayPolicyGroups();
	}

	@Override
	public GrayPolicyGroup policyGroup(@PathVariable("serviceId") String serviceId, String instanceId,
			@PathVariable("groupId") String groupId) {
		return grayServiceManager.getGrayInstane(serviceId, instanceId).getGrayPolicyGroup(groupId);
	}

	@Override
	public List<GrayPolicy> policies(@PathVariable("serviceId") String serviceId, String instanceId,
			@PathVariable("groupId") String groupId) {
		return grayServiceManager.getGrayInstane(serviceId, instanceId).getGrayPolicyGroup(groupId).getList();
	}

	@Override
	public GrayPolicy policy(@PathVariable("serviceId") String serviceId, String instanceId,
			@PathVariable("groupId") String groupId, @PathVariable("policyId") String policyId) {
		return grayServiceManager.getGrayInstane(serviceId, instanceId).getGrayPolicyGroup(groupId)
				.getGrayPolicy(policyId);
	}
}
