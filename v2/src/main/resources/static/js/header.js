$(function () {
  // ログインモーダルオープン
  $('.login-open').click(function () {
    $('#overlay, .modal-login').fadeIn(200);
  });
  // 新規登録モーダルオープン
  $('.signup-open').click(function () {
    $('#overlay, .modal-signup').fadeIn(200);
  });
  //モーダルクローズ
  $('.login-close, .signup-close, #overlay').click(function () {
    $('#overlay, .modal-login, .modal-signup, .modal-reserve').fadeOut(100);
    if (document.getElementById('modal-login') != null) {
		document.getElementById('login_error').style.display = "hidden";
		document.getElementById('login_error').style.backgroundColor = "#FFFFFF";
		document.getElementById('login_error').innerHTML = "";
		document.getElementById('email').value = "";
		document.getElementById('password').value = "";
	}
    if (document.getElementById('modal-signup') != null) {
		document.getElementById('signup_error').style.display = "hidden";
		document.getElementById('signup_error').style.backgroundColor = "#FFFFFF";
		document.getElementById('signup_error').innerHTML = "";
		document.getElementById('s_userName').value = "";
		document.getElementById('s_email').value = "";
		document.getElementById('s_password').value = "";
	}	
  });
});
// ログイン処理
function restLogin(){
	var email = document.getElementById('email').value;
	var password = document.getElementById('password').value;
	$.post(
		"/restLogin", 
		{email: email, password: password},
		function(data){
			if (data == "ok") {
				loginMenu();
			    //リクエストが成功した際に実行する関数
			    $('#overlay, .modal-login').fadeOut(100);
			} else {
			document.getElementById('login_error').style.backgroundColor = "#f08080";
			document.getElementById('login_error').style.display = "block";
			document.getElementById('login_error').innerHTML = data;
			}
		}
	);
}

// 新規登録処理
function restSignup(){
	var userName = document.getElementById('s_userName').value;
	var email = document.getElementById('s_email').value;
	var password = document.getElementById('s_password').value;
	var sex_bool = document.getElementById('s_sex_1').checked;
	var sex;
	if (sex_bool) {
		sex = "1";
	} else {
		sex = "2";			
	}
	var agree = document.getElementById('s_agree').value;
	$.post(
		"/restSignup", 
		{userName: userName, email: email, password: password, sex: sex, agree: agree},
		function(data){
			if (data == "ok") {
				loginMenu();
			    //リクエストが成功した際に実行する関数
			    $('#overlay, .modal-signup').fadeOut(100);
			} else {
			document.getElementById('signup_error').style.backgroundColor = "#f08080";
			document.getElementById('signup_error').style.display = "block";
			document.getElementById('signup_error').innerHTML = data;
			}
		}
	);
}

// ログアウト処理
function restLogout(){
	$.post(
		"/restLogout", 
		{},
		function(data){
			logoutMenu();
		});
}

// ログイン中のメニュー
function loginMenu() {
	document.getElementById('login_01').style.display = "none";
	document.getElementById('login_02').style.display = "none";
	document.getElementById('login_03').style.display = "flex";
	document.getElementById('login_04').style.display = "flex";
	document.getElementById('login_05').style.display = "flex";
	document.getElementById('login_06').style.display = "flex";
	document.getElementById('menu01').style.display = "block";
	document.getElementById('menu02').style.display = "block";
	document.getElementById('menu03').style.display = "block";
	document.getElementById('menu04').style.display = "block";
	document.getElementById('menu05').style.display = "block";
	document.getElementById('menu06').style.display = "block";
    document.getElementById('menu07').style.display = "block";
}
// ログアウト中のメニュー
function logoutMenu() {
	document.getElementById('login_01').style.display = "flex";
	document.getElementById('login_02').style.display = "flex";
	document.getElementById('login_03').style.display = "none";
	document.getElementById('login_04').style.display = "none";
	document.getElementById('login_05').style.display = "none";
	document.getElementById('login_06').style.display = "none";
	document.getElementById('menu01').style.display = "none";
	document.getElementById('menu02').style.display = "none";
	document.getElementById('menu03').style.display = "none";
	document.getElementById('menu04').style.display = "none";
	document.getElementById('menu05').style.display = "none";
	document.getElementById('menu06').style.display = "none";
    document.getElementById('menu07').style.display = "none";
}

