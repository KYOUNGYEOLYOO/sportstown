package com.bluecapsystem.cms.core.properties;

import java.io.File;

public class FFMpegProperties {

	
	
	private static String homeDirectory;
	private static String ffmpegPath;
	
	private static String ffprobePath;

	
	public static String getFfmpegPath() {
		return ffmpegPath;
	}

	public void setFfmpegPath(String ffmpegPath) {
		FFMpegProperties.ffmpegPath = ffmpegPath;
	}

	public static String getHomeDirectory() {
		return homeDirectory;
	}

	public void setHomeDirectory(String homeDirectory) {
		FFMpegProperties.homeDirectory = homeDirectory;
	}

	public static String getFfprobePath() {
		return ffprobePath;
	}

	public void setFfprobePath(String ffprobePath) {
		FFMpegProperties.ffprobePath = ffprobePath;
	}
	
	
	public static File getHome()
	{
		return new File(homeDirectory);
	}
	
	public static File getFFfmpeg()
	{
		return new File(getHome(), ffmpegPath);
	}
	
	public static File getFFProbe()
	{
		return new File(getHome(), ffprobePath);
	}

}
