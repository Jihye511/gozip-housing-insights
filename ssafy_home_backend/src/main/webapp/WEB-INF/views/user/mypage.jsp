<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>마이페이지</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="/WEB-INF/views/include/header.jsp" />

    <div class="container d-flex justify-content-center align-items-center" style="min-height: 80vh;">
        <div class="card shadow p-4 w-100" style="max-width: 500px;">
            <h3 class="text-center mb-4">마이페이지</h3>
            <table class="table table-bordered text-center align-middle mb-4">
                <tbody>
                    <tr>
                        <th scope="row" class="bg-light" style="width: 30%;">ID</th>
                        <td>${user.user_id}</td>
                    </tr>
                    <tr>
                        <th scope="row" class="bg-light">이름</th>
                        <td>${user.name}</td>
                    </tr>
                    <tr>
                        <th scope="row" class="bg-light">전화번호</th>
                        <td>${user.phone}</td>
                    </tr>
                    <tr>
                        <th scope="row" class="bg-light">이메일</th>
                        <td>${user.email}</td>
                    </tr>
                </tbody>
            </table>

            <div class="row">
                <div class="col-6">
                    <form action="${root}/user/updatepage" method="get">
                        <button type="submit" class="btn btn-primary w-100">수정</button>
                    </form>
                </div>
                <div class="col-6">
                    <button class="btn btn-danger w-100" onclick="confirmDelete()">탈퇴</button>
                </div>
            </div>
        </div>
    </div>

    <!-- 탈퇴 확인 모달 -->
    <div class="modal fade" id="deleteModal" tabindex="-1" aria-labelledby="deleteModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="deleteModalLabel">회원 탈퇴</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="닫기"></button>
          </div>
          <div class="modal-body">
            정말 탈퇴하시겠습니까?
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
            <form action="${root}/user/delete" method="post">
                <input type="hidden" name="user_id" value="${user.user_id}" />
                <button type="submit" class="btn btn-danger">탈퇴</button>
            </form>
          </div>
        </div>
      </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function confirmDelete() {
            new bootstrap.Modal(document.getElementById('deleteModal')).show();
        }
    </script>
</body>
</html>
