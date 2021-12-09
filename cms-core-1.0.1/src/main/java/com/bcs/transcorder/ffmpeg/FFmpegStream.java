package com.bcs.transcorder.ffmpeg;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class FFmpegStream {
	
	
	private Process process;
	
	private BufferedReader reader;
	
	
	public FFmpegStream(Process process)
	{
		this.process = process;
	}
	
	
	public boolean initailize()
	{
		boolean ret = false;
		try
		{
			reader = new BufferedReader( new InputStreamReader(process.getInputStream()) );
			ret = true;
		}catch(Exception ex)
		{
			ex.printStackTrace();
			ret = false;
		}
		return ret;
	}
	
	
	public String readLine() throws Exception
	{
		String line = reader.readLine();
		return line;
	}
	

	public void close()
	{
		try{
			reader.close();
		}catch(Exception e){}
		
	}
	
}
