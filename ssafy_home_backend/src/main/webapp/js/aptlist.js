/*******************************************
 * 1. 시도/구군/동 선택 로직
 *******************************************/
// (A) 페이지 로드 시 시도 목록 먼저 요청
sendRequest("sido", "*00000000");

// (B) 시도 변경 시 구군 목록 요청
document.querySelector("#sido").addEventListener("change", function () {
    if (this.value) {
        let regcode = this.value.substr(0, 2) + "*00000";
        sendRequest("gugun", regcode);
    } else {
        initOption("gugun");
        initOption("dong");
    }
});

// (C) 구군 변경 시 동 목록 요청
document.querySelector("#gugun").addEventListener("change", function () {
    if (this.value) {
        let regcode = this.value.substr(0, 5) + "*";
        sendRequest("dong", regcode);
    } else {
        initOption("dong");
    }
});

/**
 * grpc-proxy로 시도/구군/동 목록 가져옴
 * @param {string} selid - "sido" | "gugun" | "dong"
 * @param {string} regcode - 예: "*00000000", "11*00000", "11110*"
 */
function sendRequest(selid, regcode) {
    const url = "https://grpc-proxy-server-mkvo6j4wsq-du.a.run.app/v1/regcodes";
    const params = "regcode_pattern=" + regcode + "&is_ignore_zero=true";
    fetch(`${url}?${params}`)
        .then(res => res.json())
        .then(data => addOption(selid, data));
}

/**
 * select 태그에 옵션을 동적으로 추가
 * @param {string} selid - "sido" | "gugun" | "dong"
 * @param {Object} data - fetch 결과
 */
function addOption(selid, data) {
    let opt = "";
    initOption(selid);

    switch (selid) {
        // ① 시도
        case "sido":
            opt += `<option value="">시도선택</option>`;
            data.regcodes.forEach(rc => {
                opt += `<option value="${rc.code}">${rc.name}</option>`;
            });
            break;

        // ② 구군
        case "gugun":
            opt += `<option value="">구군선택</option>`;
            // 이름이 겹치는 경우(예: OO시/OO시 OO구) 예외 처리
            for (let i = 0; i < data.regcodes.length - 1; i++) {
                if (
                    data.regcodes[i].name.split(" ")[1] === data.regcodes[i + 1].name.split(" ")[1] &&
                    data.regcodes[i].name.split(" ").length !== data.regcodes[i + 1].name.split(" ").length
                ) {
                    data.regcodes.splice(i, 1);
                    i--;
                }
            }
            data.regcodes.forEach(rc => {
                const arr = rc.name.split(" ");
                const name = arr.length === 2 ? arr[1] : arr[1] + " " + arr[2];
                opt += `<option value="${rc.code}">${name}</option>`;
            });
            break;

        // ③ 동
        case "dong":
            opt += `<option value="">동선택</option>`;
            data.regcodes.forEach(rc => {
                const arr = rc.name.split(" ");
                const dongName = arr.length === 3 ? arr[2] : arr[3];
                opt += `<option value="${rc.code}">${dongName}</option>`;
            });
            break;
    }
    document.querySelector(`#${selid}`).innerHTML = opt;
}

/**
 * select 태그 초기화
 * @param {string} selid - "sido" | "gugun" | "dong"
 */
function initOption(selid) {
    document.querySelector(`#${selid}`).innerHTML = "";
}

