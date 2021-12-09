package com.bcs.transcorder.ffmpeg;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class FFMpegProcess 
{
	
	private File ffmpeg;
	private File source;
	private String options;
	private String formatOptions;
	
	private File output;
	private ProcessBuilder builder;
	
	
	
	/**
	 * 영상을 변환할때 사용한다
	 * @param ffmpeg
	 * @param source
	 * @param options
	 * @param output
	 */
	public FFMpegProcess(File ffmpeg, File source, String options, File output)
	{
		this(ffmpeg, source, null, options, output);
	}
	
	public FFMpegProcess(File ffmpeg, File source, String formatOptions, String options, File output)
	{
		this.ffmpeg = ffmpeg;
		this.source = source;
		this.options = options;
		this.output = output;
		this.formatOptions = formatOptions;
	}
	
	
	
	/**
	 * ffmpeg 를 실행할 command 를 가져온다
	 * @return
	 */
	public String commandString()
	{
		StringBuilder cmd = new StringBuilder();
		
		// ffmpeg
		cmd.append("\"");
		cmd.append(ffmpeg.getPath());
		cmd.append("\"");
		
		if(formatOptions != null && formatOptions.trim().isEmpty() == false)
		{
			cmd.append(" ");
			cmd.append(formatOptions);
		}
		
		
		if(output == null)
			throw new NullPointerException("output 정보가 없습니다");
		
		// source file
		cmd.append(" -i ");
		cmd.append("\"");
		cmd.append(source.getPath());
		cmd.append("\"");
		
		if(options != null && options.trim().isEmpty() == false)
		{
			cmd.append(" ");
			cmd.append(options);
		}
		
		
		cmd.append(" ");
		cmd.append("\"");
		cmd.append(output.getPath());
		cmd.append("\"");
		
		return cmd.toString();
	}
	
	/**
	 * ffmpeg 를 실행 시킨다
	 * @return
	 * @throws IOException
	 */
	public Process createProcess() throws IOException
	{
		String cmd = commandString();
		
		builder = new ProcessBuilder(cmd); 
		builder.redirectErrorStream(true);
		
		return builder.start();
	}
	
	/**
	 * ffmpeg 출력을 가져온다
	 * @param process
	 * @return
	 */
	public BufferedReader getReader(Process process)
	{
		BufferedReader stdOut = new BufferedReader( new InputStreamReader(process.getInputStream()) );
		return stdOut;
	}
	
	
	/**
	 * 작업중인 라인을 가져온다
	 * @param stdOut
	 * @return
	 * @throws Exception
	 */
	public String readLine(BufferedReader stdOut) throws Exception
	{
		return stdOut.readLine();
		
	}
	
	/**
	 * 작업중인 ffmpeg 의 진행 데이터를 가져온다
	 * @param strPrint
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> parseWoringData(String strPrint) throws Exception
	{
		if(strPrint.startsWith("frame=") == false)
			return null;
		HashMap<String, Object> values = new HashMap<String, Object>();
		
		int idxFrame 	= strPrint.indexOf("frame=");
		int idxFps 		= strPrint.indexOf("fps=");
		int idxSize 	= strPrint.indexOf("size=");
		int idxTime 	= strPrint.indexOf("time=");
		int idxBit 		= strPrint.indexOf("bitrate=");
		int idxSpeed 	= strPrint.indexOf("speed=");
		
		String [] strList = new String[]{
			strPrint.substring(idxFrame, idxFps),
			strPrint.substring(idxFps, idxSize),
			strPrint.substring(idxSize, idxTime),
			strPrint.substring(idxTime, idxBit),
			strPrint.substring(idxBit, idxSpeed),
			strPrint.substring(idxSpeed)
		} ;
		
		for(int i = 0; i < strList.length; i++)
		{
			
			String [] temp = strList[i].split("=");
			
			if(temp[0].equals("frame"))
			{
				values.put(temp[0].trim(), Long.parseLong(temp[1].trim()));
			}else if(temp[0].equals("fps"))
			{
				// values.put(temp[0].trim(), Double.parseDouble(temp[1].trim()));
			}else if(temp[0].equals("time"))
			{
				values.put(temp[0].trim(), FFmpegDateUtil.parse(temp[1].trim()));
			}else if(temp[0].equals("speed") || temp[0].equals("size") || temp[0].equals("bitrate"))
			{
				values.put(temp[0].trim(), temp[1].trim());
			}
		
		}
		return values;
	}
	
	/**
	 * process 를 닫아준다
	 * @param process
	 */
	public void close(Process process)
	{
		
		try
		{
			try{
				process.getInputStream().close();
			}catch(Exception e){
				e.printStackTrace();
			}finally
			{
				process.destroy();
			}
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
}
