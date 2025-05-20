<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<% String contextPath = request.getContextPath(); %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>지도</title>
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
	<style>
		.map-container {
			height: 90vh;
		}
		#map {
			width: 100%;
			height: 100%;
		}
	</style>
</head>
<body>
	<jsp:include page="/WEB-INF/views/include/header.jsp" />

	<div class="container-fluid map-container d-flex position-relative">
		<!-- 왼쪽: 검색 및 아파트 목록 -->
		<div class="p-2 border-end overflow-auto" style="flex: 1;">
			<!-- 지역 검색 -->
			<form class="mb-3" method="get" action="/home/search/region">
				<div class="row g-2 align-items-center">
					<div class="col-md-4">
						<select class="form-select" id="sido" name="sido">
							<option value="">시도</option>
						</select>
					</div>
					<div class="col-md-3">
						<select class="form-select" id="gugun" name="gugun">
							<option value="">구군</option>
						</select>
					</div>
					<div class="col-md-3">
						<select class="form-select" id="dong" name="dong">
							<option value="">동</option>
						</select>
					</div>
					<div class="col-md-2">
						<button class="btn btn-primary w-100" type="submit">조회</button>
					</div>
				</div>
			</form>

			<!-- 아파트명 검색 -->
			<form class="mb-3" method="get" action="/home/search/apt">
				<div class="input-group">
					<input type="text" name="aptName" id="aptSearch" class="form-control" placeholder="아파트명 검색">
					<button class="btn btn-primary" type="submit">검색</button>
				</div>
			</form>

			<!-- aptList를 JS로 넘기기 -->
			<script>
				window.aptListFromServer = [
					<c:forEach var="apt" items="${aptList}" varStatus="loop">
					{
						name: "${apt.apt_nm}",
						lat: ${apt.latitude},
						lng: ${apt.longitude}
					}<c:if test="${!loop.last}">,</c:if>
					</c:forEach>
				];
			</script>

			<!-- 아파트 목록 -->
			<div id="searchResultList" class="mt-3">
				<c:forEach var="apt" items="${aptList}">
					<div class="apt-item border-bottom py-2"
					     data-lat="${apt.latitude}"
					     data-lng="${apt.longitude}"
					     data-apt-seq="${apt.apt_seq}"
					     style="cursor: pointer;">
						<strong>${apt.apt_nm}</strong><br/>
						<span>${apt.road_nm} ${apt.road_nm_bonbun}</span><br/>
						<span>${apt.umd_nm} ${apt.jibun}</span><br/>
						<button class="btn btn-sm btn-outline-primary show-deals-btn mt-1">상세 거래내역</button>
					</div>
				</c:forEach>
				<c:if test="${empty aptList}">
					<p class="text-muted">검색 결과가 없습니다.</p>
				</c:if>
			</div>
		</div>

		<!-- 오른쪽: 지도 -->
		<div class="p-2" style="flex: 3;">
			<div id="map" class="rounded" style="height: 100%;"></div>
		</div>
	</div>

	<!-- 거래내역 모달 -->
	<div class="modal fade" id="dealModal" tabindex="-1" aria-labelledby="dealModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-lg modal-dialog-scrollable">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="dealModalLabel">거래내역</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
				</div>
				<div class="modal-body" id="dealModalBody">
					<!-- 거래내역 들어감 -->
				</div>
			</div>
		</div>
	</div>

	<!-- JS 파일 및 변수 -->
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
	<script src="<c:url value='/js/aptlist.js' />"></script>
	<script src="<c:url value='/js/key.js' />"></script>
	<script>
		const contextPath = "${pageContext.request.contextPath}";
	</script>
	<script src="<c:url value='/js/dealmap.js' />"></script>
</body>
</html>
