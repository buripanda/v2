$(function () {
  //モーダルクローズ
  $('.reserve-close, #reserve-overlay').click(function () {
    $('#reserve-overlay, .modal-reserve').fadeOut(100);
	document.getElementById('ticket_num').value = "";
	document.getElementById('datetime').value = "";	
  });
  // 日程予約モーダルオープン
  $('.reserve-open').click(function () {
	console.log("日程予約");
    $('#reserve-overlay, .modal-reserve').fadeIn(200);
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