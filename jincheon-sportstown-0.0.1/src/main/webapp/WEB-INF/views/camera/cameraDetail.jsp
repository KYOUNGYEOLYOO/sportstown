<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page session="false" %>
<jsp:useBean id="now" class="java.util.Date" />


	<input type="hidden" name="camId" value="${camera.camId}" />
	<table class="write_type1 mgb20" summary="">
		<caption></caption>
		<colgroup>
			<col width="150">
			<col width="*">
		</colgroup>
		<tbody>
			<tr>
				<th>카메라명</th>
				<td><input type="text" name="name" value="${camera.name}" title="카메라명" class="type_2" readonly></td>
			</tr>
			<tr>
				<th>카메라유형</th>
				<td>
					<c:set var="cameraTypeName"  value="" />
					
					<c:choose>
						<c:when test="${camera.cameraType == 'Static'}">
							<c:set var="cameraTypeName"  value="고정" />
						</c:when>
						<c:when test="${camera.cameraType == 'Shift'}">
							<c:set var="cameraTypeName"  value="유동" />
						</c:when>
					</c:choose>
					<input type="text" name="cameraType" value="${cameraTypeName}" title="카메라유형" class="type_2" readonly>
				</td>
			</tr>
			<tr>
				<th>카메라위치</th>
				<td><input type="text" name="locationCodeName" value="${camera.location.name}" title="카메라위치" class="type_2" readonly></td>
			</tr>
			<tr>
				<th>스포츠종목</th>
				<td><input type="text" name="sportsEventCodeName" value="${camera.sportsEvent.name}" title="스포츠종목" class="type_2" readonly></td>
			</tr>
			<tr>
				<th>라이브 전용</th>
				<td>
					<c:choose>
						<c:when test="${camera.isLiveOnly == true}">라이브전용</c:when>
						<c:otherwise>라이브/DVR 사용</c:otherwise>
					</c:choose>
				</td>
			</tr>
		</tbody>
	</table>
	
	
	<c:forEach items="${camera.streamMetaItems}" var="streamMeta" varStatus="st">
		<h1>${streamMeta.metaClass} 정보 입력</h1>
		<input type="hidden" name="streamMetaItems[${st.index}].camId" value="${streamMeta.camId}" />
		<input type="hidden" name="streamMetaItems[${st.index}].metaClass" value="${streamMeta.metaClass}" />
		
		<table class="write_type1 mgb20" summary="">
			<caption></caption>
			<colgroup>
				<col width="150">
				<col width="*">
				<col width="150">
				<col width="*">
			</colgroup>
			<tbody>
				<tr>
					<th>스트리밍서버</th>
					<td colspan="3">
						<input type="text" name="streamMetaItems[${st.index}].applicationCodeName" value="${streamMeta.streamServer.name}" title="스트리밍서버" class="type_2" readonly>
					</td>
				</tr>
				<tr>
					<th>Application</th>
					<td>
						<input type="text" name="streamMetaItems[${st.index}].applicationCodeName" value="${streamMeta.application.name}" title="Application 서비스 이름" class="type_2" readonly>
					</td>
					<th>스트림명</th>
					<td>
						<input type="text" name="streamMetaItems[${st.index}].streamName" value="${streamMeta.streamName}" title="스트리밍 서비스 이름" class="type_2" readonly>	
					</td>
				</tr>
				<tr>
					<th>Source URL</th>
					<td colspan="3"><input type="text" name="streamMetaItems[${st.index}].streamSourceUrl" value="${streamMeta.streamSourceUrl}" title="카메라명" class="type_2" readonly></td>
				</tr>
				
				<tr>
					<th>아이디</th>
					<td>
						<input type="text" name="streamMetaItems[${st.index}].streamUserId" value="${streamMeta.streamUserId}" title="스트리밍 소스 아이디" class="type_2" readonly>
					</td>
					<th>패스워드</th>
					<td>
						<input type="text" name="streamMetaItems[${st.index}].streamUserPassword" value="${streamMeta.streamUserPassword}" title="스트리밍 소스 패스워드" class="type_2" readonly>
					</td>
				</tr>
				
			</tbody>
		</table>
	</c:forEach>

	
