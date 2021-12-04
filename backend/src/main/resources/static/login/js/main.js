jQuery(document).ready(function($){
	toastr.options.positionClass = "toast-top-center";// 提示框，顶部中间显示。如果需要居中，请使用：toast-center-center

	let $form_modal = $('.cd-user-modal'),
		$form_login = $form_modal.find('#cd-login'),
		$form_signup = $form_modal.find('#cd-signup'),
		$form_modal_tab = $('.cd-switcher'),
		$tab_login = $form_modal_tab.children('li').eq(0).children('a'),
		$tab_signup = $form_modal_tab.children('li').eq(1).children('a');


	//切换tab
	$form_modal_tab.on('click', function(event) {
		event.preventDefault();
		( $(event.target).is( $tab_login ) ) ? login_selected() : signup_selected();
	});


	function login_selected(){
		$form_login.addClass('is-selected');
		$form_signup.removeClass('is-selected');
		$tab_login.addClass('selected');
		$tab_signup.removeClass('selected');
	}

	function signup_selected(){
		$form_login.removeClass('is-selected');
		$form_signup.addClass('is-selected');
		$tab_login.removeClass('selected');
		$tab_signup.addClass('selected');
	}

	//所有输入框在失焦时判断值是否为空，为空则提醒，获得焦点则取消提醒
	$("input").focus(function () {
		$(this).next(".cd-error-message").removeClass("is-visible");
	}).blur(function(){
		if($(this).val().length == 0){
			$(this).next(".cd-error-message").addClass("is-visible");
		}
	});
	//点击登录按钮
	$form_login.find('input[type="submit"]').on('click', function(event){
		let $username = $("#login-username");
		let $password = $("#login-password");
		event.preventDefault();
		let username = $username.val();
		if(!username){
			if(!$username.next(".cd-error-message").hasClass("is-visible")){
				$username.next(".cd-error-message").addClass("is-visible");
			}
			return;
		}
		let password = $password.val();
		if(!password){
			if(!$password.next(".cd-error-message").hasClass("is-visible")){
				$password.next(".cd-error-message").addClass("is-visible");
			}
			return;
		}
		//是否记住登陆
		let rememberMe = $("#remember-me").val() == "on" ? true : false;
		let data = {
			"username":username,
			"password":password,
			"rememberMe":rememberMe
		};
		$.post({
			url:"/login",
			data:data,
			success:function(res) {
				if (res.retCode == "0") {
					//显示服务端返回的消息
					toastr.error(res.retMsg);
				} else {
					toastr.success("登陆成功，正在跳转");
					location.href = "/index";
				}
			},
			error: function(){
				toastr.error("请求失败")
			}
		})
	});

	//IE9 placeholder fallback
	//credits http://www.hagenburger.net/BLOG/HTML5-Input-Placeholder-Fix-With-jQuery.html
	if(!Modernizr.input.placeholder){
		$('[placeholder]').focus(function() {
			var input = $(this);
			if (input.val() == input.attr('placeholder')) {
				input.val('');
		  	}
		}).blur(function() {
		 	var input = $(this);
		  	if (input.val() == '' || input.val() == input.attr('placeholder')) {
				input.val(input.attr('placeholder'));
		  	}
		}).blur();
		$('[placeholder]').parents('form').submit(function() {
		  	$(this).find('[placeholder]').each(function() {
				var input = $(this);
				if (input.val() == input.attr('placeholder')) {
			 		input.val('');
				}
		  	})
		});
	}

});


//credits http://css-tricks.com/snippets/jquery/move-cursor-to-end-of-textarea-or-input/
jQuery.fn.putCursorAtEnd = function() {
	return this.each(function() {
    	// If this function exists...
    	if (this.setSelectionRange) {
      		// ... then use it (Doesn't work in IE)
      		// Double the length because Opera is inconsistent about whether a carriage return is one character or two. Sigh.
      		var len = $(this).val().length * 2;
      		this.setSelectionRange(len, len);
    	} else {
    		// ... otherwise replace the contents with itself
    		// (Doesn't work in Google Chrome)
      		$(this).val($(this).val());
    	}
	});
};