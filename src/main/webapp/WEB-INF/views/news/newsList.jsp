<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>任务管理</title>
	<script type="text/javascript">
		function fill_modalBody(id){
			$.ajax({ 
				url: './news/'+id, 
				context: document.body, 
				success: function(data){
		        	$("#myModal .modal-body p").empty().html(data.description);
		        	$("#myModal .modal-header #myModalLabel").empty().html(data.title);
		      	}
	      	});
			$('#myModal').modal({
				  keyboard: true,
				  backdrop:true
			})
		}
		function sort(th,type,param){
			console.log(th);
			console.log(type);	
			console.log(param);
			console.log($(th).attr("class"));
			if($(th).attr("class") == "icon-arrow-down"){
				$(th).attr("class","icon-arrow-up");
			} else {
				$(th).attr("class","icon-arrow-down");
			}
			var hrf = "?sortType="+type+"&order=asc&"+param;
			console.log(hrf);
			window.location.href = hrf;
		}
	</script>
</head>

<body>
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	<div class="row">
		<div class="span4 offset7">
			<form class="form-search" action="#">
				<label>名称：</label> <input type="text" name="search_LIKE_title" class="input-medium" value="${param.search_LIKE_title}"> 
				<button type="submit" class="btn" id="search_btn">Search</button>
		    </form>
	    </div>
	    <tags:sort/>
	</div>

	<table id="contentTable" class="table table-striped table-bordered table-condensed table table-hover">
		<thead>
			<tr>
				<th>标题<a class="icon-arrow-down" href="javascript:;" onclick="sort(this,'title','${searchParams}');"></a></th>
				<th>作者<a class="icon-arrow-down" href="javascript:;" onclick="sort(this,'author','${searchParams}');"></a></th>
				<th>发布时间<a class="icon-arrow-down" href="javascript:;" onclick="sort(this,'pubDate','${searchParams}');"></a></th>
				<th>分类</th>
				<td>来源</td>
				<th>管理</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${newsList.content}" var="news">
			<tr>
				<td><a href="javascript:void(0)" data-toggle="modal" onclick="fill_modalBody('${news.id}')">${news.title}</a></td>
				<td>${news.author}</td>
				<td>${news.pubDate}</td>
				<td>${news.catalog}</td>
				<td>${news.source}</td>
				<td><a href="${news.link}" target="blank">原文</a>  |  <a href="${ctx}/news/delete/${news.id}">删除</a></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	 
	<!-- Modal -->
	<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="width:1200px;left: 40%;">
	  <div class="modal-header">
	    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
	    <h3 id="myModalLabel">Modal header</h3>
	  </div>
	  <div class="modal-body">
	    <p>One fine body…</p>
	  </div>
	  <div class="modal-footer">
	    <button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
	  </div>
	</div>
	<!-- Modal end  -->
	
	<div align="center">
		<tags:pagination page="${newsList}" paginationSize="5"/>
	</div>
</body>
</html>
