<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>로그인</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="/WEB-INF/views/include/header.jsp" />

    <!-- 회원가입 성공 메시지 -->
    <c:if test="${not empty sessionScope.signupSuccess}">
        <div class="alert alert-success text-center" role="alert">
            ${sessionScope.signupSuccess}
        </div>
        <c:remove var="signupSuccess" scope="session" />
    </c:if>

    <!-- 최초 로그인 버튼 누를 시 에러 메시지 제거 -->
    <c:if test="${empty param.action}">
        <c:remove var="error" scope="request" />
    </c:if>

    <!-- 로그인 실패 시 에러 메시지 표시 -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger text-center" role="alert">
            ${error}
        </div>
    </c:if>

    <div class="container d-flex justify-content-center align-items-center" style="min-height: 80vh;">
        <div class="card p-4 shadow w-100" style="max-width: 400px;">
            <h3 class="mb-3 text-center">로그인</h3>

            <!-- 폼 전송: FrontController(/homes/*)로 -->
            <form action="${root}/user/login" method="post">
                <!-- 서블릿에서 파라미터 확인용 (필요 없다면 제거 가능) -->
                <input type="hidden" name="action" value="login" />

                <div class="mb-3">
                    <label class="form-label">아이디</label>
                    <input type="text" name="user_id" class="form-control" required />
                </div>

                <div class="mb-3">
                    <label class="form-label">비밀번호</label>
                    <input type="password" name="pw" class="form-control" required />
                </div>

                <!-- 비밀번호 찾기 버튼 -->
                <div class="d-flex justify-content-end mb-3">
                   <a href="${root}/user/findpasswordform" class="btn btn-link p-0" style="background-color: transparent; color: #0d6efd;">
                        비밀번호 찾기
                    </a>
                </div>

                <button type="submit" class="btn btn-primary w-100">로그인</button>
            </form>
        </div>
    </div>
</body>
</html>
