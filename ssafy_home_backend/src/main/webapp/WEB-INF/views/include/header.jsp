<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="root" value="${pageContext.request.contextPath}" />

<!-- Bootstrap CSS -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<!-- Bootstrap Icons -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">

<nav class="navbar navbar-expand-lg navbar-light bg-light px-3">
  <a class="navbar-brand d-flex align-items-center" href="${root}/index">
    <img src="${root}/img/homeslogo.png" alt="Logo" height="30" class="me-0" />
    Homes
  </a>

  <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
    data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false"
    aria-label="Toggle navigation">
    <span class="navbar-toggler-icon"></span>
  </button>

  <div class="collapse navbar-collapse" id="navbarNav">
    <ul class="navbar-nav me-auto ms-3">
      <li class="nav-item">
        <a class="navbar-brand d-flex align-items-center" href="/home/search">
          지도
        </a>
      </li>
    </ul>

    <!-- 오른쪽 메뉴 -->
    <ul class="navbar-nav ms-auto d-flex align-items-center gap-2">
      <c:choose>
        <c:when test="${empty sessionScope.loginUser}">
          <!-- 로그인 X -->
          <li class="nav-item">
            <a class="btn btn-outline-primary" href="${root}/user/goregistpage">
              <i class="bi bi-person-plus"></i> 회원가입
            </a>
          </li>
          <li class="nav-item">
            <a class="btn btn-outline-primary" href="${root}/user/gologinpage">
              <i class="bi bi-box-arrow-in-right"></i> 로그인
            </a>
          </li>
        </c:when>
        <c:otherwise>
          <!-- 로그인 O -->
          <li class="nav-item d-flex align-items-center">
            <div class="badge rounded-pill bg-primary text-white px-3 py-2 d-flex align-items-center" style="font-size: 0.9rem;">
              ${sessionScope.loginUser.name}님
            </div>
          </li>
          <li class="nav-item">
            <a class="btn btn-outline-primary" href="${root}/user/mypage">
              <i class="bi bi-person-circle"></i> 마이페이지
            </a>
          </li>
          <li class="nav-item">
            <a class="btn btn-outline-primary" href="${root}/user/logout">
              <i class="bi bi-box-arrow-right"></i> 로그아웃
            </a>
          </li>
        </c:otherwise>
      </c:choose>
    </ul>
  </div>
</nav>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
