package com.bluecapsystem.cms.jincheon.sportstown.json.wowza;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WowzaCURLApi {

	private static final Logger logger = LoggerFactory.getLogger(WowzaCURLApi.class);

	/**
	 * stream 의 상태를 조회 한다
	 *
	 * @param baseUrl
	 * @param server
	 * @param vhost
	 * @param application
	 * @param instance
	 * @param streamName
	 * @return
	 */
	public String incomingStreams(String baseUrl, String server, String vhost, String application, String instance, String streamName) {
		String url = String.format("%s/v2/servers/%s/vhosts/%s/applications/%s/instances/%s/incomingstreams/%s", //
				baseUrl, server, vhost, application, instance, streamName);

		String resp = "Error";
		try {
			logger.debug("request incomingStreams [url = {}]", url);
			resp = sendGet(url);
			logger.debug("response incomingStreams [url = {}] => {}", url, resp);
		} catch (Exception ex) {
			throw new RuntimeException(String.format("incomingStreams 오류 발생 [url = %s] => %s", url, ex.getMessage()), ex);
		}

		return resp;
	}

	public String incomingStreams(String baseUrl, String application, String streamName) {
		return this.incomingStreams(baseUrl, "_defaultServer_", "_defaultVHost_", application, "_definst_", streamName);
	}
	
	/**
	 * 카메라 등록을 할 때 서버에도 등록
	 * 
	 * @param baseUrl
	 * @param application
	 * 
	 * 20211221
	 */
	public String addStream(String baseUrl, String application, String streamName, String streamSourceUrl) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		String url = String.format("%s/v2/servers/_defaultServer_/vhosts/_defaultVHost_/applications/%s/streamfiles",
				baseUrl, application);
		// sendPut은 뭔지 몰라서 일단 패스
		
		parameters.put("name", streamName);
		parameters.put("serverName", "_defaultServer_");
		parameters.put("uri", streamSourceUrl);
		
		String resp = "Error";
		try {
			JSONObject json = new JSONObject();
			json.putAll(parameters);
			String strParam = json.toJSONString();
			resp = sendPost(url, strParam);
		}catch(Exception ex) {
			throw new RuntimeException(String.format("addStream Error [url = %s] => %s ", url, ex.getMessage()), ex);
		}
		return resp;
	}
	/**
	 * 카메라 삭제할 때 서버에서도 삭제
	 * 
	 *  @param baseUrl
	 *  @param application
	 *  @param name
	 */
	public String deleteStream(String baseUrl, String application, String streamName) {
		Map<String, Object> parameters =new HashMap<String, Object>();
		String url = String.format("%s/v2/servers/_defaultServer_/vhosts/_defaultVHost_/applications/%s/streamfiles/%s",
				baseUrl, application, streamName);
		
		try {
			sendDelete(url);
		}catch(Exception ex) {
			throw new RuntimeException(String.format("deleteStream Error [url = %s] => %s ", url, ex.getMessage()), ex); 
		}
		return url;
	}
	
	/**
	 * 카메라 수정할 때 서버에서도 업데이트
	 * 
	 *  @param baseUrl
	 *  @param application
	 *  @param name
	 * 
	 */
	public String updateStream(String baseUrl, String application, String streamName, String streamSourceUrl) {
		Map<String, Object> parameters =new HashMap<String, Object>();
		String url = String.format("%s/v2/servers/_defaultServer_/vhosts/_defaultVHost_/applications/%s/streamfiles/%s",
				baseUrl, application, streamName);
		
		///////////////////////////////////////////// TODO 
		parameters.put("name", streamName);
		parameters.put("serverName", "_defaultServer_");
		parameters.put("uri", streamSourceUrl);
		
		String resp = "Error";
		try {
			JSONObject json = new JSONObject();
			json.putAll(parameters);
			String strParam = json.toJSONString();
			resp = sendUpdate(url, strParam);
		}catch(Exception ex) {
			throw new RuntimeException(String.format("addStream Error [url = %s] => %s ", url, ex.getMessage()), ex);
		}
		return resp;
		
	}
	
	/**
	 * streamFile에 올라간 데이터가 incomingStream 으로 넘어가게 하는 함수
	 * 
	 */
	public String connectStream(String baseUrl, String application, String streamName) {
		Map<String, Object> parameters =new HashMap<String, Object>();
		String url = String.format("%s/v2/servers/_defaultServer_/vhosts/_defaultVHost_/applications/%s/streamfiles/%s"+
		"/actions/connect?connectAppName=%s&appInstance=_definst_&mediaCasterType=rtp",
				baseUrl, application, streamName, application);
		
		try {
			sendPut(url);
		}catch(Exception ex) {
			throw new RuntimeException(String.format("connectStream Error [url = %s] => %s ", url, ex.getMessage()), ex); 
		}
		return url;
	}
	
	/**
	 * streamFile에 올라간 데이터가 incomingStream 으로 넘어가게 하는 함수
	 * 
	 */
	public String disconnectStream(String baseUrl, String application, String streamName) {
		Map<String, Object> parameters =new HashMap<String, Object>();
		String url = String.format("%s/v2/servers/_defaultServer_/vhosts/_defaultVHost_/applications/%s/instances/_definst_/incomingstreams/%s"+".stream"+
		"/actions/disconnectStream",
				baseUrl, application, streamName);
		
		try {
			sendPut(url);
		}catch(Exception ex) {
			throw new RuntimeException(String.format("disconStream Error [url = %s] => %s ", url, ex.getMessage()), ex); 
		}
		return url;
	}

	/**
	 * 녹화 중지 등 Stream 에 액션을 보내준다
	 *
	 * @param baseUrl
	 * @param application
	 * @param streamFile
	 * @param action
	 * @return
	 */
	public String actionStream(String baseUrl, String application, String streamFile, String action) {
		// stopRecording
		String url = String.format("%s/v2/servers/_defaultServer_/vhosts/_defaultVHost_/applications/%s/instances/_definst_/streamrecorders/%s/actions/%s", //
				baseUrl, application, streamFile, action);

		String resp = "Error";
		try {
			logger.debug("request actionStream [url = {}]", url);
			resp = sendPut(url);
			logger.debug("response actionStream [url = {}] => {}", url, resp);
		} catch (Exception ex) {
			throw new RuntimeException(String.format("actionStream 오류 발생 [url = %s] => %s", url, ex.getMessage()), ex);
		}
		
		return resp;
	}

	/**
	 * 녹화를 시작한다
	 *
	 * @param baseUrl
	 * @param application
	 * @param streamFile
	 * @param outputPath
	 * @param outputFile
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String recordStream(String baseUrl, String application, String streamFile, String outputPath, String outputFile) {
		Map<String, Object> parameters = new HashMap<String, Object>();

		String url = String.format("%s/v2/servers/_defaultServer_/vhosts/_defaultVHost_/applications/%s/instances/_definst_/streamrecorders/%s", //
				baseUrl, application, streamFile);

		parameters.put("restURI", url);
		parameters.put("recorderName", streamFile);
		parameters.put("instanceName", "_definst_");
		parameters.put("recorderState", "Waiting for stream");
		parameters.put("defaultRecorder", true);
		parameters.put("segmentationType", "None");
		parameters.put("outputPath", outputPath);
		parameters.put("baseFile", outputFile);
		parameters.put("fileFormat", "MP4");
		parameters.put("fileVersionDelegateName", "com.wowza.wms.livestreamrecord.manager.StreamRecorderFileVersionDelegate");
//		parameters.put("fileTemplate", "${BaseFileName}_${RecordingStartTime}_${SegmentNumber}");
		parameters.put("fileTemplate", "${SourceStreamName}_${RecordingStartTime}_${SegmentNumber}");
		//SourceStreamName
		parameters.put("segmentDuration", 0);
		parameters.put("segmentSize", 0);
		parameters.put("segmentSchedule", "0 * * * * *");
		parameters.put("recordData", true);
		parameters.put("startOnKeyFrame", true);
		parameters.put("splitOnTcDiscontinuity", false);
		parameters.put("backBufferTime", 3000);
		parameters.put("option", "Version existing file");
		parameters.put("moveFirstVideoFrameToZero", true);
		parameters.put("currentSize", 0);
		parameters.put("currentDuration", 0);
		parameters.put("recordingStartTime", "");

		String resp = "Error";
		try {
			JSONObject json = new JSONObject();
			json.putAll(parameters);
			String strParam = json.toJSONString();

			logger.debug("request recordStream [url = {}], [parameters={}]", //
					url, ToStringBuilder.reflectionToString(parameters));

			resp = sendPost(url, strParam);
			logger.debug("response recordStream [url = {}] => {}", url, resp);
		} catch (Exception ex) {
			throw new RuntimeException(String.format("recordStream 오류 발생 [url = %s] => %s", url, ex.getMessage()), ex);
		}
		return resp;
	}

	private String sendGet(String url) throws Exception {
		URL requestUrl = new URL(url);

		HttpURLConnection conn = (HttpURLConnection) requestUrl.openConnection();

		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json; charset=utf-8");

		conn.setUseCaches(false);
		conn.setDoOutput(false);
		conn.setDoInput(true);

		// read the response
		BufferedReader in = null;
		StringBuffer buffer = new StringBuffer();
		try {
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = null;
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
			in.close();
		} catch (Exception ex) {
			if (in != null)
				in.close();
			throw ex;
		}

		return buffer.toString().trim();

	}

	private String sendPut(String url) throws Exception {
		URL requestUrl = new URL(url);

		HttpURLConnection conn = (HttpURLConnection) requestUrl.openConnection();

		conn.setRequestMethod("PUT");
		conn.setRequestProperty("Accept", "application/json; charset=utf-8");

		conn.setUseCaches(false);
		conn.setDoOutput(false);
		conn.setDoInput(true);

		// read the response
		BufferedReader in = null;
		StringBuffer buffer = new StringBuffer();
		try {
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = null;

			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
			in.close();
		} catch (Exception ex) {
			if (in != null)
				in.close();
			throw new RuntimeException(ex);
		}

		return buffer.toString().trim();

	}
	
	private void sendDelete(String url) throws Exception {
		
		// url 객체 생성
		URL requestUrl = new URL(url);
		
		// 해당 url로 연결 생성
		HttpURLConnection conn = (HttpURLConnection) requestUrl.openConnection();
		conn.setDoOutput(true);
		
		conn.setRequestMethod("DELETE");
		conn.setRequestProperty("Accept", "application/json; charset=utf-8");
		
		conn.connect();

	}

	private String sendPost(String url, String parameter) throws Exception {
		
		// url 객체 생성
		URL requestUrl = new URL(url);

		// 해당 url로 연결 생성
		HttpURLConnection conn = (HttpURLConnection) requestUrl.openConnection();

		logger.info("sendpost return conn : {}", conn);
		conn.setRequestMethod("POST");												// get은 url을통해서, post는 스트림을 통해서 데이터 전달
		conn.setRequestProperty("Accept", "application/json; charset=utf-8");
		conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

		// conn.setRequestProperty("Content-Length", Integer.toString(urlParameter.getBytes().length));

		// 출력/입력스트림 사용설정. post는 데이터 전송시 스트림을 통해서 데이터를 전송함 inputstream에는 헤더와 메세지를, outputstream에는 post데이터를 넘겨주겟다고 정의
		conn.setDoOutput(true);
		conn.setDoInput(true);

		logger.info("//////////////////////////// 1.    sendPost             //////////////////////////");
		OutputStream os = null;
		try {
			os = conn.getOutputStream();											// url에 전송할 outputstream 생성
			os.write(parameter.getBytes("UTF-8"));									// parameter값 utf-8로 인코딩해서 저장
			logger.info("getoutputstream : {}", conn.getOutputStream() );
			os.flush();
			os.close();
		} catch (Exception ex) {
			if (os != null)
				os.close();
			throw ex;
		}


		logger.info("//////////////////////////// 2.    sendPost             //////////////////////////");
		// read the response
		BufferedReader in = null;
		StringBuffer buffer = new StringBuffer();
		try {
			logger.info("getInputStream : {}", conn.getInputStream() );				
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));	// url로부터 데이터 읽어와서 변환후 stringbuffer에 저장

			String line = null;

			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}

			in.close();
		} catch (Exception ex) {
			if (in != null)
				in.close();
			throw ex;
		}

		return buffer.toString().trim();

	}
	
private String sendUpdate(String url, String parameter) throws Exception {
		
		// url 객체 생성
		URL requestUrl = new URL(url);

		// 해당 url로 연결 생성
		HttpURLConnection conn = (HttpURLConnection) requestUrl.openConnection();

		logger.info("sendpost return conn : {}", conn);
		conn.setRequestMethod("PUT");												// get은 url을통해서, post는 스트림을 통해서 데이터 전달
		conn.setRequestProperty("Accept", "application/json; charset=utf-8");
		conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

		// conn.setRequestProperty("Content-Length", Integer.toString(urlParameter.getBytes().length));

		// 출력/입력스트림 사용설정. post는 데이터 전송시 스트림을 통해서 데이터를 전송함 inputstream에는 헤더와 메세지를, outputstream에는 post데이터를 넘겨주겟다고 정의
		conn.setDoOutput(true);
		conn.setDoInput(true);

		logger.info("//////////////////////////// 1.    sendPost             //////////////////////////");
		OutputStream os = null;
		try {
			os = conn.getOutputStream();											// url에 전송할 outputstream 생성
			os.write(parameter.getBytes("UTF-8"));									// parameter값 utf-8로 인코딩해서 저장
			logger.info("getoutputstream : {}", conn.getOutputStream() );
			os.flush();
			os.close();
		} catch (Exception ex) {
			if (os != null)
				os.close();
			throw ex;
		}


		logger.info("//////////////////////////// 2.    sendPost             //////////////////////////");
		// read the response
		BufferedReader in = null;
		StringBuffer buffer = new StringBuffer();
		try {
			logger.info("getInputStream : {}", conn.getInputStream() );				
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));	// url로부터 데이터 읽어와서 변환후 stringbuffer에 저장

			String line = null;

			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}

			in.close();
		} catch (Exception ex) {
			if (in != null)
				in.close();
			throw ex;
		}

		return buffer.toString().trim();

	}
}
