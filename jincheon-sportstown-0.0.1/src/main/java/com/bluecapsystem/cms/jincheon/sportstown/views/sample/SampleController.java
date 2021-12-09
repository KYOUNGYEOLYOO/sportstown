package com.bluecapsystem.cms.jincheon.sportstown.views.sample;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sample")
public class SampleController
{
	@RequestMapping("")
	public String sample()
	{
		return "/sample/sampleList";
	}

	@RequestMapping("/layoutDetail")
	public String layoutBase()
	{
		return "/sample/layoutDetail";
	}

	@RequestMapping("/layoutSearch")
	public String layoutList()
	{
		return "/sample/layoutSearch";
	}


	@RequestMapping("/player")
	public String player()
	{
		return "/sample/player";
	}

	@RequestMapping("/flowplayer")
	public String flowplayer()
	{
		return "/sample/flowplayer";
	}

}
