var type = "user";
var page = 1;
var size = 20;
var num = 0;

Date.prototype.format =function(format)
{
var o = {
"M+" : this.getMonth()+1, //month
"d+" : this.getDate(), //day
"h+" : this.getHours(), //hour
"m+" : this.getMinutes(), //minute
"s+" : this.getSeconds(), //second
"q+" : Math.floor((this.getMonth()+3)/3), //quarter
"S" : this.getMilliseconds() //millisecond
}
if(/(y+)/.test(format)) format=format.replace(RegExp.$1,
(this.getFullYear()+"").substr(4- RegExp.$1.length));
for(var k in o)if(new RegExp("("+ k +")").test(format))
format = format.replace(RegExp.$1,
RegExp.$1.length==1? o[k] :
("00"+ o[k]).substr((""+ o[k]).length));
return format;
}

$(function() {
	loadUser();
	$(".nav_li").on("click", function() {
		if (!$(this).attr("class").indexOf("flag") >= 0) {
			$(this).addClass("flag");
			$(this).siblings().removeClass("flag");
			type = $(this).attr("data-type");
			page = 1;
			loadElement();
			num = 0
		}
	});
});

function loadElement() {
	if (type == "user") {
		loadUser();
	} else if (type == "product"){
		loadProduct();
	} else if (type == "recharge-log") {
		loadRecharge();
	} else if (type == "requit") {
		loadRequit();
	}
}

function loadUser() {
	$.post("/admin/user/list", {"page":page, "size":size}, function(data) {
		if(page == 1) {
			$("#tbody").empty();
			$("#thead").empty();
			$("#thead").append($('<tr class = "tbody_tr" >'
                    + '<th  align="center">序号</th>'
                    + '<th  align="center">姓名</th>'
                    + '<th  align="center">身份证号</th>'
                    + '<th  align="center">电话</th>'
                    + '<th  align="center">城市</th>'
                    + '<th  align="center">公司全称</th>'
                    + '<th  align="center">身份证正面照</th>'
                    + '<th  align="center">身份证背面照</th>'
                    + '<th  align="center">个人名片或工牌</th>'
                    + '<th  align="center">与公司logo合影</th>'
                    + '<th  align="center">状态</th>'
                    + '<th  align="center">注册时间</th>'
                    + '<th  align="center">操作</th>'
                    + '</tr>'));
		}
		
		var str = "";
		if (data.length > 0) {
			for(var i= 0; i < data.length; ++i) {
				var imgIdentity =  "";
				var imgIdentityBack = "";
				var imgPicCard = "";
				var imgPicLogo = "";
				var statusStr = data[i].status == 0?"未认证":data[i].status == 1?"审核中":data[i].status == 2?"通过":"未通过";
				var optStr = '';
				if (data[i].picIdentity && data[i].picIdentity != "") {
					imgIdentity = '<img  src= "' + data[i].picIdentity + '">';
				}
				
				if (data[i].picIdentityBack && data[i].picIdentityBack != "") {
					imgIdentityBack = '<img  src= "' + data[i].picIdentityBack + '">';
				}
				
				if (data[i].picCard && data[i].picCard != "") {
					imgPicCard = '<img  src= "' + data[i].picCard + '">';
				}
				
				if (data[i].picLogo && data[i].picLogo != "") {
					imgPicLogo = '<img  src= "' + data[i].picLogo + '">';
				}
				
				if (data[i].status == 1) {
					optStr = '<button onclick="checkUserAuth(' + data[i].id + ',' + 2+ ')">通过</button>&nbsp;&nbsp;<button onclick="checkUserAuth(' + data[i].id + ',' + 3 + ')">驳回</button>';
				}
				
				str += '<tr><td align="center">' + (++num) + '</td>'
				+ '<td align="center">' + data[i].name + '</td>'
				+ '<td align="center">' + data[i].identity + '</td>'
				+ '<td align="center">' + data[i].mobile + '</td>'
				+ '<td align="center">' + data[i].city + '</td>'
				+ '<td align="center">' + data[i].compName + '</td>'
				+ '<td align="center">' + imgIdentity + '</td>'
				+ '<td align="center">' + imgIdentityBack + '</td>'
				+ '<td align="center">' + imgPicCard + '</td>'
				+ '<td align="center">' + imgPicLogo + '</td>'
				+ '<td align="center">' + statusStr + '</td>'
				+ '<td align="center">' + new Date(data[i].addTime).format('yyyy-MM-dd hh:mm:ss') + '</td>'
				+ '<td align="center">' + optStr + '</td>'
				+ '</tr>';
			}
			$("#tbody").append($(str));
		}
		
	});
}

function loadProduct() {
	$.post("/admin/product/list", {"page":page, "size":size}, function(data) {
		if(page == 1) {
			$("#thead").empty();
			$("#tbody").empty();
			$("#thead").append($('<tr class = "tbody_tr" >'
                    + '<th  align="center">序号</th>'
                    + '<th  align="center">姓名</th>'
                    + '<th  align="center">年纪</th>'
                    + '<th  align="center">电话</th>'
                    + '<th  align="center">身份证号/出生日期</th>'
                    + '<th  align="center">性别</th>'
                    + '<th  align="center">城市</th>'
                    + '<th  align="center">工作类型</th>'
                    + '<th  align="center">工作年限</th>'
                    + '<th  align="center">月收入</th>'
                    + '<th  align="center">工资发放形式</th>'
                    + '<th  align="center">车产</th>'
                    + '<th  align="center">房产</th>'
                    + '<th  align="center">信用卡</th>'
                    + '<th  align="center">公积金</th>'
                    + '<th  align="center">社保</th>'
                    + '<th  align="center">寿险</th>'
                    + '<th  align="center">微粒贷</th>'
                    + '<th  align="center">贷款金额</th>'
                    + '<th  align="center">申请日期</th>'
                    + '<th  align="center">数据状态</th>'
                    + '<th  align="center">操作</th>'
                    + '</tr>'));
		}
		var str = "";
		if (data.length > 0) {
			for (var i = 0; i < data.length; ++i) {				
				var sex = data[i].sex == 0? "男":"女";
				var vocation = data[i].vocation == 0?"上班族":data[i].vocation == 1?"个体户":"企业主";
				var workTime = data[i].workTime == 0?"半年以下":data[i].workTime == 1?"半年到一年":"一年以上";
				var monthIncome = data[i].monthIncome == 0?"":data[i].monthIncome == 1?"3千以下":data[i].monthIncome == 2?"3千到5千":data[i].monthIncome == 3?"5千到8千":"1万以上";
				var wagesType = data[i].wagesType == 0?"银行转账":"现金发放";
				var car = data[i].car == 0?"无车":"有车";
				var building = data[i].building == 0?"无房":"有房";
				var creditCard = data[i].creditCard == 0?"无":"有";
				var gjj = data[i].accumulationFund == 0?"无":"有";
				var sb = data[i].socialInsurance == 0?"无":"有";
				var sx = data[i].lifeInsurance == 0?"无":"有";
				var wld = data[i].weiLiDai == 0?"无":"有";
				var sum = data[i].sum/10000 + "万";
				var applyTime = new Date(data[i].applyTime).format('yyyy-MM-dd hh:mm:ss')
				var status = data[i].status == 0?"未抢":"已抢";
				var opt = (data[i].status == 0 || data[i].status == 1)? '<td align="center"><button onclick="updateProduct(' + data[i].id + ',' + 2 + ')">加精</button>&nbsp;&nbsp;&nbsp;<button onclick="updateProduct(' + data[i].id + ',' + 3 + ')">废弃</button></td>':"<td></td>";
				str += '<tr><td align="center">' + (++num) + '</td>'
					+ '<td align="center">' + data[i].name + '</td>'
					+ '<td align="center">' + data[i].age + '</td>'
					+ '<td align="center">' + data[i].mobile + '</td>'
					+ '<td align="center">' + data[i].identyNumber + '</td>'
					+ '<td align="center">' + sex  + '</td>'
					+ '<td align="center">' + data[i].city + '</td>'
					+ '<td align="center">' + vocation + '</td>'
					+ '<td align="center">' + workTime + '</td>'
					+ '<td align="center">' + monthIncome + '</td>'
					+ '<td align="center">' + wagesType + '</td>'
					+ '<td align="center">' + car + '</td>'
					+ '<td align="center">' + building + '</td>'
					+ '<td align="center">' + creditCard + '</td>'
					+ '<td align="center">' + gjj + '</td>'
					+ '<td align="center">' + sb + '</td>'
					+ '<td align="center">' + sx + '</td>'
					+ '<td align="center">' + wld + '</td>'
					+ '<td align="center">' + sum + '</td>'
					+ '<td align="center">' + applyTime + '</td>'
					+ '<td align="center">' + status + '</td>'
					+ opt
					+ '</tr>';
			}
			$("#tbody").append($(str));				
		}
	});	
}

function loadRecharge() {
	$.post("/admin/recharge/list", {"page":page, "size":size}, function(data) {
		if (page == 1) {
			$("#tbody").empty();
			$("#thead").empty();
			$("#thead").append($('<tr class = "tbody_tr" >'
                    + '<th  align="center">序号</th>'
                    + '<th  align="center">姓名</th>'
                    + '<th  align="center">电话</th>'
                    + '<th  align="center">精品单购买数量</th>'
                    + '<th  align="center">淘单购买数量</th>'
                    + '<th  align="center">充值总金额</th>'
                    + '<th  align="center">充值状态</th>'
                    + '<th  align="center">充值时间</th>'
                    + '</tr>'));			
		}
		
		if (data.length > 0) { 
			var str = "";
			for (var i = 0; i < data.length; ++i) {
				var status = data[i].status == 2?"成功":"失败";
				str += '<tr><td align="center">' + (++num) + '</td>'
				+ '<td align="center">' + data[i].name + '</td>'
				+ '<td align="center">' + data[i].mobile + '</td>'
				+ '<td align="center">' + data[i].times + '</td>'
				+ '<td align="center">' + data[i].tdTimes + '</td>'
				+ '<td align="center">' + data[i].sum + '元</td>'
				+ '<td align="center">' + status + '</td>'
				+ '<td align="center">' + new Date(data[i].addTime).format('yyyy-MM-dd hh:mm:ss') + '</td>'
				+ '</tr>';
			}
			$("#tbody").append($(str));
		}
	});	
}

function loadRequit() {
	$.post("/admin/requit/list", {"page":page, "size":size}, function(data) {
		if (page == 1) {
			$("#tbody").empty();
			$("#thead").empty();
			$("#thead").append($('<tr class = "tbody_tr" >'
                    + '<th  align="center">序号</th>'
                    + '<th  align="center">姓名</th>'
                    + '<th  align="center">电话</th>'
                    + '<th  align="center">贷款人姓名</th>'
                    + '<th  align="center">贷款人电话</th>'
                    + '<th  align="center">贷款总额</th>'
                    + '<th  align="center">申退原因</th>'
                    + '<th  align="center">申退状态</th>'
                    + '<th  align="center">驳回原因</th>'
                    + '<th  align="center">申退时间</th>'
                    + '<th  align="center">修改时间</th>'
                    + '<th  align="center">操作</th>'
                    + '</tr>'));			
		}
		
		if (data.length > 0) {
			var str = "";
			for (var i = 0; i < data.length; ++i) {
				var opt = data[i].status == 0?'<td align="center"><button  onclick="updateRequit(' + data[i].id + ',' + 1 + ')">通过</button>&nbsp;&nbsp;&nbsp;<button onclick="updateRequit(' + data[i].id + ',' + 2 + ')">驳回</button></td>':'<td align="center"></td>';
				var status = data[i].status == 0? '<td align="center">未处理</td>':data[i].status == 1?'<td align="center">退单成功</td>':'<td align="center">已驳回</td>';
				str += '<tr><td align="center">' + (++num) + '</td>'
				+ '<td align="center">' + data[i].name + '</td>'
				+ '<td align="center">' + data[i].mobile + '</td>'
				+ '<td align="center">' + data[i].productName + '</td>'
				+ '<td align="center">' + data[i].productMobile + '</td>'
				+ '<td align="center">' + data[i].sum + '元</td>'
				+ '<td align="center">' + data[i].requitReason + '</td>'
				+ status
				+ '<td align="center">' + data[i].reason + '</td>'
				+ '<td align="center">' + new Date(data[i].addTime).format('yyyy-MM-dd hh:mm:ss') + '</td>'
				+ '<td align="center">' + new Date(data[i].updateTime).format('yyyy-MM-dd hh:mm:ss') + '</td>'
				+ opt
				+ '</tr>';				
			}
			$("#tbody").append($(str));
		} else {
			if (page == 1) {
				$("#tbody").append($('<tr><td colspan="11" rowspan="5" align="center">暂无数据</td></tr>'));
			}
		}
	});	
}

function checkUserAuth(id, status) {
	$.post("/admin/checkAuth", {"id":id, "status":status}, function(data) {
		if (data.code == 200) {
			alert(data.msg);
			loadUser();
		}
	});
}

function updateProduct(id, status) {
	$.post("/admin/updateProduct", {"id":id, "status":status}, function(data) {
		if (data.code == 200) {
			alert(data.msg);
			loadProduct();
		}
	});		
}

function updateRequit(id, status) {
	$.post("/admin/updateRequit", {"id":id, "status":status}, function(data) {
		if (data.code == 200) {
			alert(data.msg);
			loadRequit();
		}
	});
}