<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>

<div id="footerWrap">
	<div id="footer">			
		<p class="copyright">Copyrightⓒ 2016 Korean Sport & Olympic Committee. all rights reserved.</p>
	</div>
</div>
<div class="loading"></div>

<script type="text/javascript">
$(document).ready(function(){	
	$(document).on('click', '.loading', function() {	 
		$(this).hide();
	});		
});
</script>
