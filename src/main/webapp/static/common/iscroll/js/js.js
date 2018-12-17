    var pageNum = 1;
	var temp_type = 1;
	var temp_status = "";
	var temp_td = false;
	var from = ""; 
	var canLoading = true;
    
    function getListData(page){
    	var count = 0;
    	var params = loadParams();
    	type = params.type;
    	if (typeof page == "object") {
    		params += "&page=" + page.num;
    		pageNum = page.num;
    	} else {
    		params += "&page=" + page;
    		pageNum = page;
    	}
    	params += "&size=7";
        $.ajaxSettings.async = false;
    	$.post("/order/search", params, function(data) {
        	if (pageNum == 1) {
        		$("#thelist").empty();
    			if (data.orders.length <= 0) {
        			$("#thelist").append($('<p style="text-align:center; font-size:15px;">-暂无数据-</p>'));
        			$(".pullUp").hide();
    			} 
        	}
        	
        	if (data.orders.length > 0) {
        		if (data.orders.length < 7) {
        			$(".pullUp").hide();
        		}
        		fillData(data.orders);
        	} 
    	});
    }
    
    function fillData(data) {
		from = temp_type == 1? "/home": temp_type == 2? "/td":"/mine";
		if (temp_status != "") {
			from += "&status=" + temp_status;
		}
		
		if (temp_td != "") {
			from += "&td=" + temp_td;
		}
		
		var now = new Date().getTime();
		var listDom=document.getElementById("temp");
		var now = new Date().getTime();
		for (var i = 0; i < data.length; i++) {
			var str = "";
			var temp = "";
			var vocation = data[i].vocation == 0 ? "上班族" : data[i].vocation == 1 ? "个体户" : data[i].vocation == 2 ? "企业主" : "无业游民";
			var month_income = data[i].monthIncome == 1 ? "3000-5000" : data[i].monthIncome == 2 ? "5001-8000" : data[i].monthIncome == 3 ? "8000-一万" : data[i].monthIncome == 4 ? "一万以上" : "3000以下";
			var building = data[i].building == 0 ? "无房" : "有房";
			var car = data[i].car == 0 ? "无车" : "有车";
			if (temp_type != 3) {
				if (data[i].status == 0) {
					temp = '<div ' + class_data + ' data-v-ecaca2b0="" class="weui-cell__ft cando">可抢单</div>';
				} else {
					temp = '<div ' + class_data + ' data-v-ecaca2b0="" class="weui-cell__ft cannotdo">已抢单</div>';
				}
			}
			var condition = "";
			if (data[i].lifeInsurance == 1) {
				condition += '<span ' + class_data + ' data-v-ecaca2b0="" class="seller-info" style="padding: 1px 3px; border: 1px solid dodgerblue;color: dodgerblue; border-radius: 5px; margin-top:5px; display:inline-block;">有保单</span>';
			}
			if (data[i].socialInsurance == 1) {
				condition += '<span ' + class_data + ' data-v-ecaca2b0="" class="seller-info" style="padding: 1px 3px; border: 1px solid dodgerblue; color: dodgerblue; border-radius: 5px; margin-top:5px; display:inline-block;">有社保</span>';
			}
			if (data[i].wagesType == 0) {
				condition += '<span ' + class_data + ' data-v-ecaca2b0="" class="seller-info" style="padding: 1px 3px; border: 1px solid dodgerblue; color: dodgerblue; border-radius: 5px; margin-top:5px; display:inline-block;">有银行流水</span>';
			}
			if (data[i].accumulationFund == 1) {
				condition += '<span ' + class_data + ' data-v-ecaca2b0="" class="seller-info" style="padding: 1px 3px; border: 1px solid dodgerblue; color: dodgerblue; border-radius: 5px; margin-top:5px; display:inline-block;">有公积金</span>';
			}
			if (data[i].weiLiDai == 1) {
				condition += '<span ' + class_data + ' data-v-ecaca2b0="" class="seller-info" style="padding: 1px 3px; border: 1px solid dodgerblue; color: dodgerblue; border-radius: 5px; margin-top:5px; display:inline-block;">微粒贷</span>';
			}
			var img = data[i].sex == 0? "/static/img/meil.png":"/static/img/femail.png";
			str += '<a ' + class_data + ' data-v-ecaca2b0="" class="menu" href="/order/detail/' + data[i].id + '?from=' + from + '">'
					+ '<span ' + class_data + ' data-v-ecaca2b0="" href="javascript:;" class="weui-cell weui-cell_access">'
					+ '<div ' + class_data + ' data-v-ecaca2b0="" class="weui-cell__bd">'
					+ '<div ' + class_data + ' data-v-ecaca2b0="" class="mb5">'
					+ '<div ' + class_data + ' data-v-ecaca2b0="" class="seller-name">' + data[i].name + '</div><span data-v-2559bb2a="" data-v-ecaca2b0="" style="font-size:16px;color: orange; margin-left:10px;">' + data[i].sum  + '万元</span></div>'
					+ '<p ' + class_data + ' data-v-ecaca2b0="" style="display:flex;"><span style="flex:1;">' + condition + '</span>'
					+ '<span ' + class_data + ' data-v-ecaca2b0="" class="seller-time" style="width:20%; display:flex;justify-content: center;">' + data[i].plusTime + '</span></p></div></span>'
					+ '<div ' + class_data + ' data-v-ecaca2b0="" class="weui-cell seller-bottom">'
					+ '<div ' + class_data + ' data-v-ecaca2b0="" class="weui-cell__bd">'
					+ '<p ' + class_data + ' data-v-ecaca2b0="" class="mb5">'
					+ '<i ' + class_data + ' data-v-ecaca2b0="" class="fa fa-map-marker fa-fw"></i>'
					+ '<span ' + class_data + ' data-v-ecaca2b0="" class="seller-info2">' + data[i].city + '</span>|'
					+ '<i ' + class_data + ' data-v-ecaca2b0="" class="fa fa-user fa-fw"></i>'
					+ '<span ' + class_data + ' data-v-ecaca2b0="" class="seller-info3">' + vocation + '</span></p>'
					+ '<p ' + class_data + ' data-v-ecaca2b0="" class="mb5">'
					+ '<i ' + class_data + ' data-v-ecaca2b0="" class="fa fa-cny fa-fw"></i>月收入'
					+ '<span ' + class_data + ' data-v-ecaca2b0="" class="seller-info3" >' + month_income + '</span></p>'
					+ '<p ' + class_data + ' data-v-ecaca2b0="">'
					+ '<i ' + class_data + ' data-v-ecaca2b0="" class="fa fa-database fa-fw"></i>'
					+ '<span ' + class_data + ' data-v-ecaca2b0="" class="seller-info2">' + building + '</span>|'
					+ '<i ' + class_data + ' data-v-ecaca2b0="" class="fa fa-car fa-fw"></i>'
					+ '<span ' + class_data + ' data-v-ecaca2b0="" class="seller-info3">' + car + '</span> </p> </div>'
					+ temp + '</div>' + '</a>';
			$("#thelist").append($(str));
		}
	    	
    }
