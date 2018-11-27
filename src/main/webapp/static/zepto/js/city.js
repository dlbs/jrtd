
var cityList = [
  { key: 'A', data: ["阿拉善盟","鞍山","安庆","安阳","阿坝","安顺","安康","阿克苏","阿勒泰"]},
  { key: 'B', data: ["北京","保定","包头","巴彦淖尔","本溪","白山","白城","蚌埠","亳州","滨州","北海","百色","巴中","毕节","保山","宝鸡","白银","博尔塔拉","博乐","巴音郭楞"]},
  { key: 'C', data: ["承德","沧州","长治","赤峰","朝阳","长春","常州","巢湖","滁州","池州","长沙","常德","郴州","潮州","崇左","重庆","成都","楚雄","昌吉"]},
  { key: 'D', data: ["大同","大连","丹东","大庆","大兴安岭","东营","德州","东莞","儋州","东方","德阳","达州","都匀","大理","德宏","迪庆","定西","德令哈"]},
  { key: 'E', data: ["鄂州","恩施"]},
  { key: 'F', data: ["抚顺","阜新","阜阳","福州","抚州","佛山","防城港","涪陵"]},
  { key: 'G', data: ["赣州","广州","桂林","贵港","广元","广安","甘孜","贵阳","甘南","共和","果洛","格尔木","固原"]},
  { key: 'H', data: ["邯郸","衡水","呼和浩特","呼伦贝尔","海拉尔","兴安盟","葫芦岛","珲春","哈尔滨","鹤岗","黑河","淮安","杭州","湖州","合肥","淮南","淮北","黄山","菏泽","鹤壁","潢川","黄石","黄冈","衡阳","怀化","惠州","河源","贺州","河池","海口","红河","汉中","海东","海北","海晏","黄南","海南","海西","哈密","和田"]},
  { key: 'J', data: ["晋城","晋中","集宁","锦州","吉林","鸡西","佳木斯","嘉兴","金华","景德镇","九江","吉安","济南","济宁","焦作","济源","荆门","荆州","吉首","江门","揭阳","嘉峪关","金昌","酒泉"]},
  { key: 'K', data: ["开封","凯里","昆明","克拉玛依","库尔勒","克孜勒苏","喀什","奎屯",]},
  { key: 'L', data: ["廊坊","临汾","吕梁","临河","辽阳","辽源","连云港","丽水","六安","龙岩","莱芜","临沂","聊城","洛阳","漯河","娄底","柳州","来宾","泸州","乐山","凉山","六盘水","丽江","临沧","拉萨","兰州","陇南","临夏"]},
  { key: 'M', data: ["梅河口","牡丹江","马鞍山","茂名","梅州","绵阳","眉山"]},
  { key: 'N', data: ["南京","南通","宁波","南平","宁德","南昌","南阳","南宁","内江","南充","怒江","林芝","那曲","阿里"]},
  { key: 'O', data: ["鄂尔多斯"]},
  { key: 'P', data: ["盘锦","莆田","萍乡","平顶山","濮阳","攀枝花","普洱","平凉"]},
  { key: 'Q', data: ["秦皇岛","齐齐哈尔","七台河","衢州","泉州","青岛","潜江","清远","钦州","琼海","黔江","黔西南","黔东南","黔南","曲靖","昌都","庆阳","塔城"]},
  { key: 'R', data: ["日照", "日喀则"]},
  { key: 'S', data: ["石家庄","朔州","沈阳","四平","松原","双鸭山","绥化","上海","苏州","宿迁","绍兴","宿州","三明","上饶","三门峡","商丘","十堰","随州","神农架","邵阳","韶关","深圳","汕头","汕尾","三亚","三沙","遂宁","思茅","山南","商洛","商州","石嘴山","石河子"]},
  { key: 'T', data: ["天津","唐山","太原","通辽","铁岭","通化","泰州","台州","铜陵","泰安","天门","铜仁","铜川","天水","吐鲁番"]},
  { key: 'W', data: ["乌兰察布", "乌鲁木齐","乌海","乌兰浩特","无锡","温州","芜湖","潍坊","威海","武汉","梧州","五指山","文昌","万宁","万州","文山","渭南","武威","吴忠"]},
  { key: 'X', data: ["邢台","忻州","锡林郭勒盟","锡林浩特","徐州","宣城","厦门","新余","新乡","许昌","信阳","襄阳","孝感","咸宁","仙桃","湘潭","湘西","西昌","兴义","西双版纳","西安","咸阳","西宁"]},
  { key: "Y", data: ["阳泉","运城","营口","延边","延吉","伊春","盐城","扬州","鹰潭","宜春","烟台","宜昌","岳阳","益阳","永州","阳江","云浮","玉林","宜宾","雅安","玉溪","延安","榆林","玉树","银川"]},
  { key: 'Z', data: ["张家口","镇江","舟山","漳州","淄博","枣庄","郑州","周口","驻马店","株洲","张家界","珠海","湛江","肇庆","中山","自贡","资阳","遵义","昭通","张掖","中卫"]}
]

var hotCity = ["全国", "北京","上海","广州","深圳","西安","成都","天津","重庆"]
  
	$(function () {
		init();
		$('body').on('click', '.city-list p', function () {
			if ($("#type").val() == 1) {
				window.location.href = "/home?city=" + $(this).text();
			} else if ($("#type").val() == 2) {
				window.location.href = "/td?city=" + $(this).text();
			}          
		});
	})
  
  
  function init() {
	  var ele = $('<div class="city-list"></div>');
      $('.city').html('');
      var hotHtml = '';
      hotHtml += '<span class="city-letter" id="★">热门城市</span>';
      $.each(hotCity, function (i, item) {
          hotHtml += '<p data-id="">' + item + '</p>'
      })
      ele.append(hotHtml);
      
      var html = '';
      $.each(cityList, function (i, item) {
          html += '<span class="city-letter" id="' + item.key + '">' + item.key + '</span>';
          $.each(item.data, function (j, data) {
              html += '<p data-id="">' + data + '</p>';
          })
          html += '';
      })
      ele.append(html);
      $('.city').append(ele);
  }
  
  ; (function ($) {
      $('.letter').bind("touchstart touchmove", function (e) {
          var top = $(window).scrollTop() + 40;
          e.preventDefault();
          var touch = e.touches[0];
          var ele = document.elementFromPoint(touch.pageX, touch.pageY - top + 40);
          if (ele.tagName === 'A') {
              var s = $(ele).text();
              $(window).scrollTop($('#' + s).offset().top - 40)
              $("#showLetter span").html(s.substring(0, 1));
              $("#showLetter").show().delay(500).hide(0);
          }
      });
  
      $('.letter').bind("touchend", function (e) {
          $("#showLetter").hide(0);
      });
  
  })(Zepto); 
  