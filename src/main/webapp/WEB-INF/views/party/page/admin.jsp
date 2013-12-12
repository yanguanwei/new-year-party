<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<c:set var="activeNav" value="admin" scope="request" />

<div data-role="collapsible" data-collapsed-icon="arrow-r" data-expanded-icon="arrow-d" data-collapsed="true" data-content-theme="c">
	<h3>队列</h3>
	<p>
		<label>普通队列中条数：</label>
		<label class="ui-li-count" id="admin-queue-count">0</label>
	</p>
	<input type="button" id="initSpittleQueue" value="初始化队列" data-theme="c" data-inline="true" />
	<input type="button" id="updateSpittleQueueCount" value="更新队列" data-theme="c" data-inline="true" />
</div>

<div data-role="collapsible" data-collapsed-icon="arrow-r" data-expanded-icon="arrow-d" data-collapsed="true" data-content-theme="c">
	<h3>节目单</h3>
	<ul data-role="listview" data-count-theme="c" data-inset="true" id="show-list">
		<c:forEach var="show" items="${shows}">
		<li>
			<a data-rel="dialog" href="<%=basePath%>party/showForm.html?showId=${show.id}">
				<h2>${show.sortNum}.${show.name}</h2>
				<p>${show.organization}&nbsp;&nbsp;|&nbsp;&nbsp;${show.category}</p>
				<span class="ui-li-count">${show.status==0?'未开始':(show.status==1?'进行中':'已结束')}</span>
			</a>
		</li>
		</c:forEach>
	</ul>
<c:if test="${user.id==1}">
<a href="<%=basePath%>party/showForm.html" data-rel="dialog" data-role="button" data-theme="b">添加节目</a>
</c:if>
</div>

<script type="text/javascript">
$(function() {
	$('#initSpittleQueue').click(function() {
		$.getJSON($.isst.party.createUrl("admin/pushAllSpittles"), function(response) {
			$('#admin-queue-count').text(response.size);
		});
	});
	
	$('#updateSpittleQueueCount').click(function() {
		$.getJSON($.isst.party.createUrl("admin/spittleQueueSize"), function(response) {
			$('#admin-queue-count').text(response.size);
		});
	}).click();
});
</script>