$(function(){
	$("#confirmBtn").click(function () {
		$("#categoryModal").modal('show')
	})
	$("#publishBtn").click(publish);
	$("#backIndexBtn").click(backIndex);
});

function publish() {
	// 获取标题
	let title = $("#recipient-name").val();
	//内容
	let content = $("#message-text").val();
	//摘要
	let desc = $("#recipient-desc").val();
	//分类
	let category = $(".selectpicker").eq(0).val();
	//标签
	let tags = $(".selectpicker").eq(1).val();
	debugger;
	// 发送异步请求
	$.post(
		"/article/add",
		{
			"title": title,
			"description":desc,
			"content": content,
			"categoryId":category,
			"tagIds":tags
		},
		// 处理服务端返回的数据
		function (data) {
			$("#categoryModal").modal('hide')
			// String -> Json 对象
			data = $.parseJSON(data);
			// 在提示框 hintBody 显示服务端返回的消息
			$("#hintBody").text(data.msg);
			// 显示提示框
			$("#hintModal").modal("show");
			// 2s 后自动隐藏提示框
			setTimeout(function(){
				$("#hintModal").modal("hide");
				// 操作完成后，跳转到首页
				if (data.code == 0) {
					location.href = "/index";
				}
			}, 2000);

		}
	)
}

function backIndex() {
	location.href = CONTEXT_PATH + "/index";
}