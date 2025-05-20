<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
	<jsp:include page="/WEB-INF/views/include/header.jsp" />

	<div class="container d-flex justify-content-center align-items-center" style="min-height: 80vh;">
	    <div class="card p-4 shadow w-100" style="max-width: 400px;">
	      <h3 class="mb-3 text-center">회원가입</h3>
	      
		  <!-- 에러 메시지 표시 영역 -->
		  <c:if test="${not empty error}">
		      <div class="alert alert-danger text-center" role="alert">
		          ${error}
		      </div>
		  </c:if>

		  <form action="${root}/user/signup" method="post">      
	        <div class="mb-3">
	          <label class="form-label">아이디</label>
	          <input type="text" name="user_id" class="form-control"
	                 value="${user_id}" required />
	        </div>

	        <div class="mb-3">
	          <label class="form-label">비밀번호</label>
	          <input type="password" name="pw" class="form-control" required />
	        </div>

	        <hr class="my-4" style="border: none; height: 2px; background: #0d6efd;" /> 

	        <div class="mb-3">
	          <label class="form-label">이메일</label>
	          <input type="email" name="email" class="form-control"
	                 value="${email}" required />
	        </div>

	        <div class="mb-3">
	          <label class="form-label">이름</label>
	          <input type="text" name="name" class="form-control"
	                 value="${name}" required />
	        </div>

	        <div class="mb-3">
	          <label class="form-label">전화번호</label>
	          <input type="tel" name="phone" class="form-control"
	                 value="${phone}" required />
	        </div>
	        
	        <button type="submit" class="btn btn-primary w-100">회원가입</button>
	      </form>
	    </div>
 	</div>
</body>
</html>
