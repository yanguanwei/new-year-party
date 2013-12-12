<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<input type="button" id="showVote" value="${hasVote?'已投过票':'投上一票'}" class="${hasVote?'ui-disabled':''}" data-theme="b" />
<input type="button" id="showVoteCancel" value="取消" data-theme="c" />

<script type="text/javascript">
$(function() {
	$("#showVote").click(function() {
		var showId = <c:out value="${show.id}" />
		if (showId) {
			$.isst.api.showVote(showId, function() {
				window.location.href = "<%=basePath%>party/shows.html";
			});
		}
	});
	
	$("#showVoteCancel").click(function() {
		$('.ui-dialog').dialog('close');
	});
});
</script>