package com.bluecapsystem.cms.jincheon.sportstown.views.sample;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bluecapsystem.cms.jincheon.sportstown.json.wowza.WowzaCURLApi;

@Controller
@RequestMapping("/test")
public class TestController 
{
	@ResponseBody
	@RequestMapping("/hello")
	public String hello()
	{
		return "hello jinsheon-sportstown Contents Management System";
	}
	
	
	@ResponseBody
	@RequestMapping("/wowza/recordStream")
	public String recordStream()
	{
		WowzaCURLApi wowzaApi = new WowzaCURLApi();
		return wowzaApi.recordStream("http://223.26.218.116:8087",
				"live", "cam1.stream", "/cmstorage/ingest", "cam1.mp4" );
	}
	
	
	@ResponseBody
	@RequestMapping("/wowza/recordStop")
	public String recordStrop()
	{
		WowzaCURLApi wowzaApi = new WowzaCURLApi();
		return wowzaApi.actionStream("http://223.26.218.116:8087",
				"live", "cam1.stream", "stopRecording" );
	}
	
}
