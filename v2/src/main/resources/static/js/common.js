// IDをパラメタにつけてPOST（自分）)
function postSelectId(id, ac) {

	const form = document.createElement('form');
	form.method = 'post';
	form.action = ac;

	const hiddenField = document.createElement('input');
	hiddenField.type = 'hidden';
	hiddenField.name = 'pid';
	hiddenField.value = id;
	form.appendChild(hiddenField);

	document.body.appendChild(form);
	form.submit();

}

// ACTIONを引数にしてPOST
function postSelectAc(ac) {

	const form = document.createElement('form');
	form.method = 'post';
	form.action = ac;
	document.body.appendChild(form);
	form.submit();

}
