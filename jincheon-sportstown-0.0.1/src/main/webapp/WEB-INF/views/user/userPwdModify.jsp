<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page session="false" %>
<jsp:useBean id="now" class="java.util.Date" />
<jsp:include page="/include/head"/>


<script type="text/javascript">


</script>
<div class="popupContainer">
<form>
	<header>사용자 정보</header>
	<table summary="">
		<caption></caption>
		<colgroup>
			<col width="160">
			<col width="*">
		</colgroup>
		<tbody>
			<tr>
				<th><label for="id">이름</label></th>
				<td><input type="text" title="이름" id="id" class="inputTxt" value="bcs" /></td>
			</tr>
			<tr>
				<th><label for="pw">비밀번호</label></th>
				<td><input type="password" title="비밀번호" id="pw" class="inputTxt" placeholder="비밀번호" /></td>
			</tr>
			<tr>
				<th><label for="pw_change">변경 비밀번호</label></th>
				<td><input type="password" title="변경 비밀번호" id="pw_change" class="inputTxt" placeholder="변경 비밀번호" /></td>
			</tr>
			<tr>
				<th><label for="pw_change_confirm">변경 비밀번호 확인</label></th>
				<td><input type="password" title="변경 비밀번호 확인" id="pw_change_confirm" class="inputTxt" placeholder="변경 비밀번호 확인" /></td>
			</tr>
		</tbody>
	</table>
	<p>
		<input type="checkbox" id="pwview" /><label for="pwview">비밀번호 문자보이기</label>
	</p>
	<div class="infoContainer">
		<p>비밀번호를 변경하시려면 변경 비밀번호를 입력하세요. 아래 내용은 변경 비밀번호 체크 사항입니다.</p>
		<ul>
			<li>연속된 문자 또는 숫자 사용 불가</li>
			<li>영문 대문자, 영문 소문자, 숫자, 특수문자 중 3개 이상</li>
			<li>8자리 ~ 20자리 이내 혼합</li>
		</ul>
	</div>
	<div class="btnWrap">
		<a class="btn reset">초기화</a>
		<a class="btn save">저장</a>
	</div>
</form>
</div>