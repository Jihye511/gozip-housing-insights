<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>HOMES</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
<style>
.hero {
    position: relative;
    background: url('${root}/img/hero.jpg') no-repeat center center;
    background-size: cover;
    color: white;
    padding: 100px 20px;
    text-align: center;
}
.hero::before {
    content: "";
    position: absolute;
    top: 0; left: 0; right: 0; bottom: 0;
    background: rgba(0, 0, 0, 0.6);
    z-index: 1;
}
.hero .container {
    position: relative;
    z-index: 2;
}
.hero h1 {
    font-size: 3rem;
    font-weight: bold;
    text-shadow: 1px 1px 6px rgba(0, 0, 0, 0.8);
}
.hero p {
    font-size: 1.2rem;
    margin-bottom: 30px;
    text-shadow: 1px 1px 6px rgba(0, 0, 0, 0.8);
}
.hero .btn-custom {
    background-color: #0d6efd;
    color: white;
    font-weight: bold;
    padding: 12px 30px;
    border: none;
}
.hero .btn-custom:hover {
    background-color: #e0a800;
    color: white;
}
.card-img-top {
    height: 200px;
    object-fit: cover;
}
</style>
</head>
<body>

<jsp:include page="/WEB-INF/views/include/header.jsp" />

<!-- Hero Section -->
<section class="hero">
    <div class="container">
        <h1>당신의 새로운 집, HOMES에서</h1>
        <p>실거래가 조회부터 매물 검색까지, 부동산 정보의 시작</p>
        <a href="/home/search" class="btn btn-custom btn-lg">
            지도에서 조회하기
        </a>
    </div>
</section>

<!-- Feature Section -->
<section class="py-5 bg-light">
    <div class="container text-center">
        <div class="row g-4">
            <div class="col-md-4">
                <i class="bi bi-cash-coin" style="font-size: 2rem;"></i>
                <h5 class="mt-2">실거래가 조회</h5>
                <p>전국 아파트 실거래가를 간편하게 확인하세요.</p>
            </div>
            <div class="col-md-4">
                <i class="bi bi-map" style="font-size: 2rem;"></i>
                <h5 class="mt-2">지도 기반 검색</h5>
                <p>직접 원하는 지역을 선택해 매물 정보를 확인할 수 있어요.</p>
            </div>
            <div class="col-md-4">
                <i class="bi bi-bar-chart-line" style="font-size: 2rem;"></i>
                <h5 class="mt-2">신뢰도 높은 데이터</h5>
                <p>공공데이터 기반의 정확한 부동산 정보를 제공합니다.</p>
            </div>
        </div>
    </div>
</section>

<!-- Carousel Section -->
<section class="py-5">
    <div class="container">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h4 class="mb-0">신규 등록 매물</h4>
            <div>
                <button class="btn btn-outline-secondary btn-sm me-2" data-bs-target="#listingCarousel" data-bs-slide="prev">
                    <i class="bi bi-chevron-left"></i>
                </button>
                <button class="btn btn-outline-secondary btn-sm" data-bs-target="#listingCarousel" data-bs-slide="next">
                    <i class="bi bi-chevron-right"></i>
                </button>
            </div>
        </div>

        <div id="listingCarousel" class="carousel slide" data-bs-ride="carousel" data-bs-interval="5000">
            <div class="carousel-inner">
                <!-- Slide 1 -->
                <div class="carousel-item active">
                    <div class="row g-4">
                        <div class="col-md-4"><div class="card h-100 shadow-sm">
                            <img src="${root}/img/towerpalace.jpg" class="card-img-top" alt="타워팰리스">
                            <div class="card-body">
                                <h5 class="card-title">[서울] 타워팰리스1차</h5><p class="card-text">70평 / 55억</p>
                            </div>
                        </div></div>
                        <div class="col-md-4"><div class="card h-100 shadow-sm">
                            <img src="${root}/img/brigthen.jpg" class="card-img-top" alt="브라이튼여의도">
                            <div class="card-body">
                                <h5 class="card-title">[서울] 브라이튼여의도</h5><p class="card-text">42평 / 40억</p>
                            </div>
                        </div></div>
                        <div class="col-md-4"><div class="card h-100 shadow-sm">
                            <img src="${root}/img/lct.jpg" class="card-img-top" alt="엘시티">
                            <div class="card-body">
                                <h5 class="card-title">[부산] 엘시티</h5><p class="card-text">58평 / 27억</p>
                            </div>
                        </div></div>
                        <div class="col-md-4"><div class="card h-100 shadow-sm">
                            <img src="${root}/img/daejeon.jpg" class="card-img-top" alt="하늘채 스카이앤">
                            <div class="card-body">
                                <h5 class="card-title">[대전] 하늘채 스카이앤</h5><p class="card-text">34평 / 5억</p>
                            </div>
                        </div></div>
                        <div class="col-md-4"><div class="card h-100 shadow-sm">
                            <img src="${root}/img/daegu.jpg" class="card-img-top" alt="수성 범어 W">
                            <div class="card-body">
                                <h5 class="card-title">[대구] 수성 범어 W</h5><p class="card-text">34평 / 12억</p>
                            </div>
                        </div></div>
                        <div class="col-md-4"><div class="card h-100 shadow-sm">
                            <img src="${root}/img/dongtan.jpg" class="card-img-top" alt="동탄역 롯데캐슬">
                            <div class="card-body">
                                <h5 class="card-title">[경기] 동탄역 롯데캐슬</h5><p class="card-text">34평 / 15억</p>
                            </div>
                        </div></div>
                    </div>
                </div>

                <!-- Slide 2 -->
                <div class="carousel-item">
                    <div class="row g-4">
                        <div class="col-md-4"><div class="card h-100 shadow-sm">
                            <img src="${root}/img/incheon.jpg" class="card-img-top" alt="더샵 송도 마리나베이">
                            <div class="card-body">
                                <h5 class="card-title">[인천] 더샵 송도 마리나베이</h5><p class="card-text">33평 / 7억</p>
                            </div>
                        </div></div>
                        <div class="col-md-4"><div class="card h-100 shadow-sm">
                            <img src="${root}/img/gwanju.jpg" class="card-img-top" alt="그랜드센트럴">
                            <div class="card-body">
                                <h5 class="card-title">[광주] 그랜드센트럴</h5><p class="card-text">31평 / 4억</p>
                            </div>
                        </div></div>
                        <div class="col-md-4"><div class="card h-100 shadow-sm">
                            <img src="${root}/img/gangwon.jpg" class="card-img-top" alt="원주 푸르지오 더 센트럴">
                            <div class="card-body">
                                <h5 class="card-title">[강원] 원주 푸르지오 더 센트럴</h5><p class="card-text">29평 / 4억</p>
                            </div>
                        </div></div>
                        <div class="col-md-4"><div class="card h-100 shadow-sm">
                            <img src="${root}/img/sejong.jpg" class="card-img-top" alt="호반베르디움 어반시티">
                            <div class="card-body">
                                <h5 class="card-title">[세종] 호반베르디움 어반시티</h5><p class="card-text">32평 / 7억</p>
                            </div>
                        </div></div>
                        <div class="col-md-4"><div class="card h-100 shadow-sm">
                            <img src="${root}/img/pangyo.jpg" class="card-img-top" alt="판교 알파리움">
                            <div class="card-body">
                                <h5 class="card-title">[경기] 판교 알파리움</h5><p class="card-text">49평 / 27억</p>
                            </div>
                        </div></div>
                        <div class="col-md-4"><div class="card h-100 shadow-sm">
                            <img src="${root}/img/acro.jpg" class="card-img-top" alt="아크로 서울포레스트">
                            <div class="card-body">
                                <h5 class="card-title">[서울] 아크로 서울포레스트</h5><p class="card-text">60평 / 135억</p>
                            </div>
                        </div></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
