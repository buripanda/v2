$(function () {
  //チケット予約モーダルクローズ
  $('.reserve-close').click(function () {
    $('.modal-reserve').fadeOut(100);
	document.getElementById('ticket_num').value = "1";
	document.getElementById('datetime').value = "";	
  });
  //評価モーダルクローズ
  $('.rate-close').click(function () {
    $('.modal-rate').fadeOut(100);
    document.getElementById('star5').checked = true;
	document.getElementById('comment').value = "";	
  });
  //オーバーレイクローズ
  $('#reserve-overlay').click(function () {
    $('#reserve-overlay').fadeOut(100);
    var ele1 = document.getElementById('ticket_num');
    if (ele1 != null) {
		document.getElementById('ticket_num').value = "1";
	}
	var ele2 = document.getElementById('datetime');	
    if (ele2 != null) {
		document.getElementById('datetime').value = "";	
	}
    var ele3 = getElementsByName('rate');
    if (ele3 != null) {
		document.getElementById('star5').checked = true;
	}
	var ele4 = document.getElementById('comment');	
    if (ele4 != null) {
		document.getElementById('comment').value = "";	
	}
  });
  // 日程予約モーダルオープン
  $('.reserve-open').click(function () {
	console.log("日程予約");
    $('#reserve-overlay, .modal-reserve').fadeIn(200);
  });
  // 評価モーダルオープン
  $('.rate-open').click(function () {
	console.log("評価");
    $('#reserve-overlay, .modal-rate').fadeIn(200);
  });
});

// 購入処理（モーダル）
function restReserve(){
	var ticket_cnt = document.getElementById('ticket_num').value;
	var reserve_date = document.getElementById('datetime').value;
	var price = document.getElementById('price2').textContent;
	var pid = document.getElementById('hidden_pid').textContent;
	$.post(
		"/restReserve", 
		{pid: pid, ticket_cnt: ticket_cnt, reserve_date: reserve_date, price: price},
		function(data){
			if (data == "ok") {
			    //リクエストが成功した際に実行する関数
			    $('#overlay, .modal-reserve').fadeOut(100);
			    location.href = "/messagePartnerList?pid=" + pid;
			} else {
			document.getElementById('reserve_error').style.backgroundColor = "#f08080";
			document.getElementById('reserve_error').style.display = "block";
			document.getElementById('reserve_error').innerHTML = data;
			}
		}
	);
}

// 評価処理（モーダル）
function restRate(pid){
	var star;
	var star1 = document.getElementById('star1').checked;
	if (star1) {
		star = 1;
	}
	var star2 = document.getElementById('star2').checked;
	if (star2) {
		star = 2;
	}
	var star3 = document.getElementById('star3').checked;
	if (star3) {
		star = 3;
	}
	var star4 = document.getElementById('star4').checked;
	if (star4) {
		star = 4;
	}
	var star5 = document.getElementById('star5').checked;
	if (star5) {
		star = 5;
	}
	var comment = document.getElementById('comment').value;
	$.post(
		"/restRate", 
		{pid: pid, star: star, comment: comment},
		function(data){
			if (data == "ok") {
			    //リクエストが成功した際に実行する関数
			    $('#overlay, .modal-reserve').fadeOut(100);
			    location.href = "/messagePartnerList?pid=" + pid;
			} else {
			document.getElementById('rate_error').style.backgroundColor = "#f08080";
			document.getElementById('rate_error').style.display = "block";
			document.getElementById('rate_error').innerHTML = data;
			}
		}
	);
}