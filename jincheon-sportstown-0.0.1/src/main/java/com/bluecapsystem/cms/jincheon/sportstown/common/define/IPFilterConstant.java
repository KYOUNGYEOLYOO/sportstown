package com.bluecapsystem.cms.jincheon.sportstown.common.define;

import java.util.HashMap;
import java.util.Map;

import com.bluecapsystem.cms.jincheon.sportstown.data.entity.User.ConnectLocation;

/**
 * 외부 IP 접속을 filter 해야 하는 정보
 *
 * @author matin
 *
 */
public class IPFilterConstant {

	/**
	 * 외부 IP -> 내부 IP 정보
	 */
	private Map<String, String> externalAddressMap;

	/**
	 * 내부 IP -> 외부 IP 정보
	 */
	private Map<String, String> internalAddressMap;

	/**
	 * singleton
	 */
	private static IPFilterConstant instance = new IPFilterConstant();

	public IPFilterConstant() {

		// 외부 IP -> 내부 IP Map
		externalAddressMap = new HashMap<String, String>();

//		// WOWZA Server
//		externalAddressMap.put("119.65.245.234", "192.168.240.41");
//		externalAddressMap.put("119.65.245.235", "192.168.240.42");
//		// WAS Server
//		externalAddressMap.put("119.65.245.233", "192.168.240.40");
		
		// WOWZA Server
		externalAddressMap.put("192.168.0.107", "192.168.0.107");
		

		// test
		//externalAddressMap.put("localhost", "127.0.0.1");

		internalAddressMap = new HashMap<String, String>();
		// 외부 IP 의 key, value 를 바꾸면 됨.
//		externalAddressMap.forEach((extAddr, interAddr) -> {
//			internalAddressMap.put(interAddr, extAddr);
//		});
	}

	/**
	 * 내부 IP 를 외부 IP로 변경 한다
	 * @param addr
	 * @return
	 */
	public String convertToExternalAddress(String addr) {
		String retAddr = addr;

		for(String interAddr : internalAddressMap.keySet()) {
			if(retAddr.indexOf(interAddr) >= 0) {
				retAddr = retAddr.replaceFirst(interAddr, internalAddressMap.get(interAddr));
				break;
			}
		}
		return retAddr;
	}

	/**
	 * 외부 IP를 내부 IP로 변경 한다
	 * @param addr
	 * @return
	 */
	public String convertToInternalAddress(String addr) {
		String retAddr = addr;

		System.out.println("InternalAddr1>>"+addr);
		for(String extAddr : externalAddressMap.keySet()) {
			
			System.out.println("InternalAddr2>>"+extAddr);
			System.out.println("InternalAddr3>>"+retAddr.indexOf(extAddr));
			if(retAddr.indexOf(extAddr) >= 0) {
				retAddr = retAddr.replaceFirst(extAddr, externalAddressMap.get(extAddr));
				break;
			}
		}
		return retAddr;
	}


	/**
	 * 접속한 사용자가 내부이면 내부 IP를, 외부이면 외부 IP를 제공한다
	 * @param cl
	 * @param addr
	 * @return
	 */
	public String filterAddress(ConnectLocation cl, String addr) {
		String retAddr = addr;

		if(cl == ConnectLocation.External) {
			retAddr = convertToExternalAddress(retAddr);
		}else {
			retAddr = convertToInternalAddress(retAddr);
		}

		return retAddr;
	}



	/**
	 * 이미 생성된 instance 를 가져온다
	 * @return
	 */
	public static IPFilterConstant getInstance() {
		return instance;
	}

	/**
	 * 외부 / 내부 접속 여부를 판단 한다
	 * @param addr
	 * @return
	 */
	public static ConnectLocation checkConnectLocation(String addr) {
		ConnectLocation cl = ConnectLocation.Internal;

		for(String extAddr : instance.externalAddressMap.keySet()) {
			if(addr.indexOf(extAddr) >= 0) {
				System.out.println("외부, 내부판단 : " + addr + " / " + extAddr);
				cl = ConnectLocation.External;
				break;
			}
		}

		return cl;
	}
}
