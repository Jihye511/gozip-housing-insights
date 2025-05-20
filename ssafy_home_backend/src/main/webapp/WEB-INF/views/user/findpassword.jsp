<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>비밀번호 찾기</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<jsp:include page="/WEB-INF/views/include/header.jsp" />

<!-- 메시지 출력 -->
<c:if test="${not empty message}">
    <div class="alert alert-${messageType} text-center" role="alert">
        ${message}
    </div>
</c:if>

<div class="container d-flex justify-content-center align-items-center" style="min-height: 80vh;">
    <div class="card p-4 shadow w-100" style="max-width: 450px;">
        <h3 class="mb-3 text-center">비밀번호 찾기</h3>

        <!-- 이메일 입력 폼 -->
        <form action="${root}/user/findpw" method="post">
            <input type="hidden" name="step" value="email" />

            <div class="mb-3">
                <label class="form-label">이메일</label>
                <input type="email" name="email" class="form-control" value="${email}" required />
            </div>

            <button type="submit" class="btn btn-primary w-100">이메일 확인</button>
        </form>
    </div>
</div>
</body>
</html>
