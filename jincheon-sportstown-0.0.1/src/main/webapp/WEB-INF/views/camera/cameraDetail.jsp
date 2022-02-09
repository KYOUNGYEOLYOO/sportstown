<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page session="false" %>
<jsp:useBean id="now" class="java.util.Date" />


	<input type="hidden" name="camId" value="${camera.camId}" />
	<dl>
		<dt>카메라명</dt>
		<dd class="full">
			<input type="text" name="name" value="${camera.name}" title="카메라명" class="inputTxt" readonly>
		</dd>
		<dt>카메라유형</dt>
		<dd class="full">
			<c:set var="cameraTypeName"  value="" />
					
			<c:choose>
				<c:when test="${camera.cameraType == 'Static'}">
					<c:set var="cameraTypeName"  value="고정" />
				</c:when>
				<c:when test="${camera.cameraType == 'Shift'}">
					<c:set var="cameraTypeName"  value="유동" />
				</c:when>
			</c:choose>
			<input type="text" name="cameraType" value="${cameraTypeName}" title="카메라유형" class="inputTxt" readonly>
		</dd>
		<dt>카메라위치</dt>
		<dd class="full">
			<input type="text" name="locationCodeName" value="${camera.location.name}" title="카메라위치" class="inputTxt" readonly>
		</dd>
		<dt>스포츠종목</dt>
		<dd class="full">
			<input type="text" name="sportsEventCodeName" value="${camera.sportsEvent.name}" title="스포츠종목" class="inputTxt" readonly>
		</dd>
		<dt>라이브 전용</dt>
		<dd class="full">
			<p>
				<c:choose>
					<c:when test="${camera.isLiveOnly == true}">라이브전용</c:when>
					<c:otherwise>라이브/DVR 사용</c:otherwise>
				</c:choose>
			</p>
		</dd>
	</dl>
	
	
	<c:forEach items="${camera.streamMetaItems}" var="streamMeta" varStatus="st">
		<h3>${streamMeta.metaClass} 정보 입력</h3>
		<input type="hidden" name="streamMetaItems[${st.index}].camId" value="${streamMeta.camId}" />
		<input type="hidden" name="streamMetaItems[${st.index}].metaClass" value="${streamMeta.metaClass}" />
		
		
		<dl>
			<dt>스트리밍서버</dt>
			<dd class="full">
				<input type="text" name="streamMetaItems[${st.index}].applicationCodeName" value="${streamMeta.streamServer.name}" title="스트리밍서버" class="type_2" readonly>
			</dd>
			<dt>Application</dt>
			<dd>
				<input type="text" name="streamMetaItems[${st.index}].applicationCodeName" value="${streamMeta.application.name}" title="Application 서비스 이름" class="type_2" readonly>
			</dd>
			<dt class="ml20">스트림명</dt>
			<dd>
				<input type="text" name="streamMetaItems[${st.index}].streamName" value="${streamMeta.streamName}" title="스트리밍 서비스 이름" class="type_2" readonly>	
			</dd>

			<dt>Source URL</dt>
			<dd class="full"><input type="text" name="streamMetaItems[${st.index}].streamSourceUrl" value="${streamMeta.streamSourceUrl}" title="카메라명" class="type_2" readonly></dd>

			<dt>아이디</dt>
			<dd>
				<input type="text" name="streamMetaItems[${st.index}].streamUserId" value="${streamMeta.streamUserId}" title="스트리밍 소스 아이디" class="type_2" readonly>
			</dd>
			<dt class="ml20">패스워드</dt>
			<dd>
				<input type="text" name="streamMetaItems[${st.index}].streamUserPassword" value="${streamMeta.streamUserPassword}" title="스트리밍 소스 패스워드" class="type_2" readonly>
			</dd>
		</dl>
	</c:forEach>

	
