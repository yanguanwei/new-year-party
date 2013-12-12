<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<base href="<%=basePath%>" />
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title><c:out value="${title}" /></title>
	<link rel="stylesheet"  href="<%=basePath%>resources/jquery.mobile/themes/default/jquery.mobile-1.3.2.min.css" />
	<link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Open+Sans:300,400,700">
	<script src="<%=basePath%>resources/js/jquery-2.0.3.min.js"></script>
	<script src="<%=basePath%>resources/jquery.mobile/jquery.mobile-1.3.2.min.js"></script>
	<script type="text/javascript">
		$.isst = {
			url: '<%=basePath%>',
			userId: <c:out value="${user.id}" />
		};
		
		$.isst.party = {
			url: '<%=basePath%>party',
			createUrl: function(path) {
				if (path.substr(0, 1) != '/') {
					path = $.isst.party.url + '/' + path;
				}
				return path;
			},
			activeNav: function(id) {
				var $nav = $("#nav-" + id);
				if ($nav.length !=0) {
					$nav.addClass("ui-btn-active ui-state-persist");
				}
			}
		};
		
		$.isst.api = {
			url: '<%=basePath%>api',
			get: function(path, data, callback) {
				$.isst.api._ajax(path, data, callback, 'get');
			},
			post: function(path, data, callback) {
				$.isst.api._ajax(path, data, callback, 'post');
			},
			put: function(path, data, callback) {
				data['_method'] = 'PUT';
				$.isst.api._ajax(path, data, callback, 'post');
			},
			_ajax: function(path, data, callback, type) {
				$.ajax({
					url: $.isst.api.url + path,
					data: data || {},
					type: type,
					dataType: 'json',
					success: callback
				});
			},
			login: function(name, password, callback) {
				$.isst.api.post('/users/validation', {name: name, password: password}, callback);
			},
			postSpittle: function(content, callback) {
				$.isst.api.post('/users/'+ $.isst.userId +'/spittles', {content: content}, callback);
			},
			updateNickname: function(nickname, callback) {
				$.isst.api.put('/users/'+ $.isst.userId, {nickname: nickname}, callback);
			},
			getShows: function(callback) {
				$.isst.api.get('/users/'+$.isst.userId+'/shows', {}, callback);
			},
			showVote: function(showId, callback) {
				$.isst.api.post('/users/'+ $.isst.userId +'/shows/'+ showId +'/votes', {}, callback);
			}
		};
	</script>