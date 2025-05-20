<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>비밀번호 재설정</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<jsp:include page="/WEB-INF/views/include/header.jsp" />

<!-- 메시지 표시 -->
<c:if test="${not empty message}">
    <div class="alert alert-${messageType} text-center" role="alert">
        ${message}
    </div>
</c:if>

<div class="container d-flex justify-content-center align-items-center" style="min-height: 80vh;">
    <div class="card p-4 shadow w-100" style="max-width: 450px;">
        <h3 class="mb-3 text-center">비밀번호 재설정</h3>

        <form action="${root}/user/resetpw" method="post">
            <!-- 이메일 전달 -->
            <input type="hidden" name="email" value="${email}" />

            <div class="mb-3">
                <label class="form-label">인증번호</label>
                <input type="text" name="code" class="form-control" required />
            </div>

            <div class="mb-3">
                <label class="form-label">새 비밀번호</label>
                <input type="password" name="newPw" class="form-control" required />
            </div>

            <div class="mb-3">
                <label class="form-label">비밀번호 확인</label>
                <input type="password" name="confirmPw" class="form-control" required />
            </div>

            <button type="submit" class="btn btn-primary w-100">비밀번호 변경</button>
        </form>
    </div>
</div>
</body>
</html>
