package com.bcs.transcorder.ffmpeg;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class FFProbeProcess {
	
	
	private File ffprobe;
	
	public FFProbeProcess(File ffprobe)
	{
		this.ffprobe = ffprobe;
	}
	
	
	
	public String getInfoString(File source)
	{
		
		return runFFProbe(ffprobe, source);
		
	}
	
	
	
	private String runFFProbe(File ffprobe, File source)
	{
		String cmd = String.format("\"%s\" -v quiet -show_format -show_streams -print_format json \"%s\"", ffprobe.getPath(), source.getPath());
		ProcessBuilder builder = new ProcessBuilder(cmd);
		builder.redirectErrorStream(true);
		Process p = null;
		StringBuilder jsonString = new StringBuilder();
		int exitVal = -1;
		try
		{
			p = builder.start();
			BufferedReader stdOut = new BufferedReader( new InputStreamReader(p.getInputStream()) );
			try
			{
				String buffer = null;
				while((buffer = stdOut.readLine()) != null)
				{
					jsonString.append(buffer);
				}
			}catch(Exception ex)
			{
				if(stdOut != null) stdOut.close();
				throw new IOException(String.format("FFProbe 결과 저장중 오류 발생 [command=%s]", cmd), ex) ;
			}finally{
				if(stdOut != null)
					stdOut.close();
			}
			
			p.waitFor();
			exitVal = p.exitValue();
		}catch(Exception ex)
		{
			if(p!= null) p.destroy();
			throw new IllegalStateException(String.format("FFProbe 실행중 오류 발생 [command=%s]", cmd), ex) ;
		}finally
		{
			if(p!= null) p.destroy();
		}
		
		if(exitVal < 0)
		{
			throw new IllegalArgumentException(String.format("FFProbe 실행 결과 오류 [exitValue=%s], [command=%s]", exitVal, cmd));
		}
		
		return jsonString.toString();
		
	}

}
