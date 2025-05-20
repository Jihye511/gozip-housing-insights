<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 정보 수정</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
<script>
	function validatePassword(event) {
		var password = document.getElementById("password").value;
		var confirmPassword = document.getElementById("confirmPassword").value;

		// 비밀번호 일치 여부 체크
		if (password !== confirmPassword) {
			// alert 대신 서버 메시지 영역 사용
			const alertDiv = document.getElementById("alert-message");
			alertDiv.classList.remove("d-none");
			alertDiv.textContent = "비밀번호가 일치하지 않습니다.";
			return false; // 폼 제출 막기
		}

		return true; // 폼 제출 허용
	}
</script>
</head>
<body>
	<jsp:include page="/WEB-INF/views/include/header.jsp" />

	<div class="container d-flex justify-content-center align-items-center"
		style="min-height: 80vh;">
		<div class="card p-4 shadow w-100" style="max-width: 500px;">
			<h3 class="mb-3 text-center">회원 정보 수정</h3>

			<!-- 서버에서 온 메시지 표시 -->
			<c:if test="${not empty error}">
				<div id="alert-message" class="alert alert-danger text-center">${error}</div>
			</c:if>
			<c:if test="${not empty success}">
				<div id="success-message" class="alert alert-success text-center">${success}</div>
			</c:if>

			<!-- JS에서 동적 에러 표시용 (초기 상태는 숨김) -->
			<div id="alert-message" class="alert alert-danger text-center d-none"></div>

			<form id="updateForm"
				action="${root}/user/update"
				method="post" onsubmit="return validatePassword()">
				
				<div class="mb-3">
					<label class="form-label">ID</label> <input type="text" name="user_id"
						class="form-control" value="${user.user_id}" readonly="readonly" />
				</div>
				<div class="mb-3">
					<label class="form-label">이름</label> <input type="text" name="name"
						class="form-control" value="${user.name}" required />
				</div>

				<div class="mb-3">
					<label class="form-label">전화번호</label> <input type="tel"
						name="phone" class="form-control" value="${user.phone}" required />
				</div>

				<div class="mb-3">
					<label class="form-label">이메일</label> <input type="email"
						name="email" class="form-control" value="${user.email}" required />
				</div>

				<div class="mb-3">
					<label class="form-label">새 비밀번호</label> <input type="password"
						name="pw" id="password" class="form-control" />
				</div>

				<div class="mb-3">
					<label class="form-label">새 비밀번호 확인</label> <input type="password"
						name="confirmPassword" id="confirmPassword" class="form-control" />
				</div>

				<button type="submit" class="btn btn-primary w-100">수정 완료</button>
			</form>
		</div>
	</div>
</body>
</html>
