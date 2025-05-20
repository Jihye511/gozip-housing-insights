let map;
let markers = [];

function loadMap() {
	if (!window.KAKAO_APP_KEY) {
		console.error("KAKAO_APP_KEY undefined");
		return;
	}

	const script = document.createElement('script');
	script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${KAKAO_APP_KEY}&autoload=false`;
	script.onload = function () {
		kakao.maps.load(initMap);
	};
	document.head.appendChild(script);
}

function mergeSort(arr, keyExtractor) {
	if (arr.length <= 1) return arr;
	const mid = Math.floor(arr.length / 2);
	const left = mergeSort(arr.slice(0, mid), keyExtractor);
	const right = mergeSort(arr.slice(mid), keyExtractor);
	return merge(left, right, keyExtractor);
}

function merge(left, right, keyExtractor) {
	const result = [];
	let i = 0, j = 0;
	while (i < left.length && j < right.length) {
		if (keyExtractor(left[i]) > keyExtractor(right[j])) {
			result.push(left[i++]);
		} else {
			result.push(right[j++]);
		}
	}
	return result.concat(left.slice(i)).concat(right.slice(j));
}

function initMap() {
	const aptList = window.aptListFromServer || [];
	console.log(window.aptListFromServer);

	let centerLatLng;
	if (aptList.length > 0 && !isNaN(aptList[0].lat) && !isNaN(aptList[0].lng)) {
		centerLatLng = new kakao.maps.LatLng(aptList[0].lat, aptList[0].lng);
	} else {
		centerLatLng = new kakao.maps.LatLng(37.5665, 126.9780);
	}

	const mapContainer = document.getElementById('map');
	const mapOption = {
		center: centerLatLng,
		level: 3
	};

	const infowindow = new kakao.maps.InfoWindow({ zIndex: 1 });
	map = new kakao.maps.Map(mapContainer, mapOption);

	aptList.forEach((apt) => {
		if (isNaN(apt.lat) || isNaN(apt.lng)) {
			console.warn("잘못된 좌표:", apt);
			return;
		}

		const marker = new kakao.maps.Marker({
			position: new kakao.maps.LatLng(apt.lat, apt.lng),
			map: map,
			title: apt.name
		});
		markers.push(marker);
	});

	document.querySelectorAll(".apt-item").forEach(item => {
		item.addEventListener("click", function () {
			const lat = parseFloat(this.dataset.lat);
			const lng = parseFloat(this.dataset.lng);
			const name = this.querySelector("strong").innerText;

			if (!isNaN(lat) && !isNaN(lng)) {
				const latLng = new kakao.maps.LatLng(lat, lng);
				map.setCenter(latLng);
				map.setLevel(2);

				const targetMarker = markers.find(m =>
					m.getPosition().getLat().toFixed(5) == lat.toFixed(5) &&
					m.getPosition().getLng().toFixed(5) == lng.toFixed(5)
				);

				if (targetMarker) {
					infowindow.setContent(`<div style="padding:5px;">${name}</div>`);
					infowindow.open(map, targetMarker);
				}
			}
		});
	});

	document.querySelectorAll(".show-deals-btn").forEach(btn => {
		btn.addEventListener("click", function (e) {
			e.stopPropagation();
			const aptItem = this.closest(".apt-item");
			const aptSeq = aptItem.dataset.aptSeq;

			fetch(`${contextPath}/home/deal?aptSeq=${aptSeq}`)
				.then(res => res.json())
				.then(deals => {
					if (!deals || deals.length === 0) {
						document.getElementById("dealModalBody").innerHTML = "<p class='text-muted'>거래내역이 없습니다.</p>";
					} else {
						document.getElementById("dealModalBody").innerHTML = `
							<div class="d-flex justify-content-end mb-2 gap-2">
								<button class="btn btn-outline-primary btn-sm sort-btn" data-sort="date">날짜</button>
								<button class="btn btn-outline-primary btn-sm sort-btn" data-sort="price">금액</button>
								<button class="btn btn-outline-primary btn-sm sort-btn" data-sort="area">면적</button>
								<button class="btn btn-outline-primary btn-sm sort-btn" data-sort="floor">층수</button>
							</div>
							<div id="dealTableArea"></div>
						`;

						let currentDeals = deals.slice();

						function renderDealTable(data) {
							const html = data.map(d => `
								<tr>
									<td>${d.deal_year}.${d.deal_month}.${d.deal_day}</td>
									<td>${d.deal_amount}만원</td>
									<td>${d.exclu_use_ar}㎡</td>
									<td>${d.floor}층</td>
								</tr>
							`).join('');

							document.getElementById("dealTableArea").innerHTML = `
								<table class="table table-bordered table-sm">
									<thead class="table-light">
										<tr><th>거래일</th><th>금액</th><th>면적</th><th>층</th></tr>
									</thead>
									<tbody>${html}</tbody>
								</table>
							`;
						}

						renderDealTable(currentDeals);

						document.querySelectorAll(".sort-btn").forEach(btn => {
							btn.addEventListener("click", function () {
								document.querySelectorAll(".sort-btn").forEach(b => b.classList.remove("active"));
								this.classList.add("active");

								const type = this.dataset.sort;
								let sorted = [];

								switch (type) {
									case "date":
										sorted = mergeSort(currentDeals, d => new Date(d.deal_year, d.deal_month - 1, d.deal_day).getTime());
										break;
									case "price":
										sorted = mergeSort(currentDeals, d => parseInt(d.deal_amount.replace(/[^0-9]/g, '')));
										break;
									case "area":
										sorted = mergeSort(currentDeals, d => d.exclu_use_ar);
										break;
									case "floor":
										sorted = mergeSort(currentDeals, d => d.floor);
										break;
								}

								renderDealTable(sorted);
							});
						});
					}

					new bootstrap.Modal(document.getElementById("dealModal")).show();
				})
				.catch(err => {
					console.error(err);
					document.getElementById("dealModalBody").innerHTML = "<p class='text-danger'>거래내역 로딩 실패</p>";
					new bootstrap.Modal(document.getElementById("dealModal")).show();
				});
		});
	});
}

loadMap();
