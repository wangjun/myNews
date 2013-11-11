<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div id="header">
 <!-- Navbar  ================================================== -->
    <div class="navbar navbar-inverse navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container">
          <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="brand" href="${ctx}/">My RSS Reader</a>
          <div class="nav-collapse collapse">
            <ul class="nav">
               <li class="">
                <a href="${ctx}/news">新闻</a>
               </li>
               <li class="">
                <a href="${ctx}/news">关于</a>
               </li>
               <li class="">
                <a href="${ctx}/news">资源</a>
               </li>
              </ul>
              <shiro:guest>
              	 <form class="navbar-form pull-right" action="${ctx}/login" method="post">
		              <input class="span2" type="text" placeholder="username" name="username">
		              <input class="span2" type="password" placeholder="password" name="password">
		              <button type="submit" class="btn">Sign in</button>
	            </form>
			  </shiro:guest>
          </div>
          <div id="title">
		    <shiro:user>
				<div class="btn-group pull-right">
					    
					<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
						<i class="icon-user"></i> <shiro:principal property="name"/>
						<span class="caret"></span>
					</a>
				
					<ul class="dropdown-menu">
						<shiro:hasRole name="admin">
							<li><a href="${ctx}/admin/user">Admin Users</a></li>
							<li class="divider"></li>
						</shiro:hasRole>
						<li><a href="${ctx}/profile">Edit Profile</a></li>
						<li><a href="${ctx}/logout">Logout</a></li>
					</ul>
				</div>
			</shiro:user>
		   </div>
        </div>
      </div>
    </div>
</div>