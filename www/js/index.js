/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

//document.write("<script language=javascript src='cordova.js'></script>");
//if(top.location!=self.location) top.location=self.location;

/*公司服务器*/
//var IP="59.172.63.26";
//var PORT="9996";
var IP="139.224.70.151";
//var PORT="8888";
var PORT="9999";
//var IP="http://www.sjyjr.net/"
var errmsg=[{
		"200":"交易成功完成",
		"487":"",
		"488":"",
		"401":"用户名密码指纹等验证不通过",
		"470":"服务繁忙,请稍后再试.",
		"500":"服务器发生未知异常,请联系维护人员.",
		"471":"用户您好,请先登录.",
		"471":"用户您好,会话已过期,请重新登录.",
		"473":"服务端要求上送位置信息",
		"474":"您当前的地理位置超出了业务办理所允许的范围了,请回到既定区域内再办理业务",
		"475":"服务器已经接受您的请求,请您放心!",
		"476":"系统繁忙,请稍后再试."
}];
var cpzl_map = {
        "8001":"公积金贷款",
        "8002":"商贷贷款",
        "8003":"组合贷款",
        "8004":"消费贷",
        "8005":"安居贷-没了",
        "8006":"车供贷",
        "8007":"房供贷",
        "8008":"保单贷",
        "8009":"赎楼贷（交易）",
        "8010":"尾款贷",
        "8011":"积金贷",
        "8012":"小赢优贷-没了",
        "8013":"装修贷",
        "8014":"抵车贷",
        "8015":"购车贷",
        "8016":"经营贷",
        "8017":"房易贷",
        "8018":"现金贷",
        "8019":"赎楼贷（非交易）",
        "8020":"过桥贷"
    };
var cpzl_image = {
        "8001":"img/gjj.png",
        "8002":"img/sddk.png",
        "8003":"img/zhd.png",
        "8004":"img/fdd.png",
        "8005":"img/ajd.png",
        "8006":"img/dsjr.png",
        "8007":"img/yjs.png",
        "8008":"img/paxyd.png",
        "8009":"img/sld.png",
        "8010":"img/wkd.png",
        "8011":"img/sld.png",
        "8012":"img/sld.png",
        "8013":"img/sld.png",
    };
var LoanType_Data = [{
    value: '0000',
    text: '全部',
  children: [{
             value: "8001-8002-8003-8004-8005-8006-8007-8008-8009-8010-8011-8012-8013",
             text: "全部"
             }]
    },{
    value: '8888',
    text: '按揭贷',
  children: [{
             value: "8001-8002-8003",
             text: "按揭贷-全部"
             }, {
               value: "8001",
               text: "公积金贷款"
               }, {
               value: "8002",
               text: "商业贷款"
               }, {
               value: "8003",
               text: "组合贷款"
               }]
    },{
    value: '9999',
    text: '房抵类',
  children: [{
             value: "8004",
             text: "房抵类-全部"
             }, {
               value: "8004",
               text: "消费贷"
               }]
    },{
    value: '7777',
    text: '信用类',
  children: [{
             value: "8013-8006-8007-8008-8011",
             text: "信用类-全部"
             }, {
               value: "8013",
               text: "装修贷"
               }, {
               value: "8006",
               text: "车供贷"
               }, {
               value: "8007",
               text: "房供贷"
               }, {
               value: "8008",
               text: "保单贷"
               },{
                   value: "8011",
                   text: "积金贷"
                   }]
    },{
    value: '6666',
    text: '垫资过桥贷',
  children: [{
             value: "8009-8010",
             text: "资金贷-全部"
             },{
               value: "8009",
               text: "赎楼贷"
               }, {
               value: "8010",
               text: "尾款贷"
               }]
    },{
        value: '5555',
        text: '车抵类',
      children: [{
                 value: "8023",
                 text: "抵押贷-全部"
                 }, {
                   value: "8023",
                   text: "抵押贷"
                   }]
        }];
var LoanType_new = [{
    value: '8888',
    text: '购房类',
  children: [{
             value: "8001-8002-8003",
             text: "全部"
             }, {
               value: "8001",
               text: "公积金贷款"
               }, {
               value: "8002",
               text: "商业贷款"
               }, {
               value: "8003",
               text: "组合贷款"
               }]
    },{
    value: '9999',
    text: '房抵贷',
  children: [{
             value: "8004",
             text: "全部"
             }, {
               value: "8004",
               text: "消费贷"
               }, {
                   value: "8022",
                   text: "经营贷"
                   }]
    },{
    value: '7777',
    text: '信用贷',
  children: [{
             value: "8005-8006-8007-8008-8011-8012-8013",
             text: "全部"
             }, {
               value: "8005",
               text: "安居贷"
               }, {
               value: "8006",
               text: "大树金融"
               }, {
               value: "8007",
               text: "友金所"
               }, {
               value: "8008",
               text: "平安新一贷"
               }, { value: "8011",
               text: "湖北消费金融"
			    }, {
			    value: "8012",
			    text: "小赢优贷"
			    }, {
			    value: "8013",
			    text: "装修贷"
			    }, {
				    value: "8014",
				    text: "房供贷"
				    }, {
					    value: "8015",
					    text: "全款房贷"
					    }, {
						    value: "8016",
						    text: "保单贷"
						    }, {
							    value: "8017",
							    text: "积金贷"
							    }, {
								    value: "8018",
								    text: "车供贷"
								    }, {
									    value: "8019",
									    text: "流水贷"
									    }, {
										    value: "8020",
										    text: "装修贷"
										    }]
    },{
    value: '6666',
    text: '垫资过桥贷',
  children: [{
             value: "8009-8010",
             text: "全部"
             },{
               
                   value: "8009",
                   text: "赎楼贷"
                   }, {
                   value: "8010",
                   text: "尾款贷"
                   }, {
                       value: "8021",
                       text: "过桥贷"
                       }]
    
    },{
        value: '5555',
        text: '车抵类',
      children: [{
                 value: "8023",
                 text: "全部"
                 }, {
                   value: "8023",
                   text: "抵押贷"
                   }]
        }];

var Marital_Status = [{
	value: '0',
	text: '未婚'
}, {
	value: '1',
	text: '已婚'
}, {
	value: '2',
	text: '离异'
}, {
	value: '3',
	text: '丧偶'
}];

var Repayment_Mode = [{
	value: '1',
	text: '等额本金'
}, {
	value: '2',
	text: '等额本息'
}, {
	value: '3',
	text: '先息后本'
}, {
	value: '4',
	text: '等本等息'
}]; 
	

	
var Yes_No = [{
	value: '0',
	text: '没有'
}, {
	value: '1',
	text: '有'
}];
//验证手机号码格式
function PhoneNoCheck(code){

var tip = "";
var pass= true;
if(code.length != 11){
tip = "手机号码长度不对";
pass = false;
}

if(!code || !/^1[0-9]{10}$/i.test(code)){
tip = "手机号码格式错误";
pass = false;
}
if(!pass) top.alertInfo(tip);
return pass;
}

function isCardID_r(sId){
var cardLength = sId.length;
if(cardLength == 15 || cardLength == 18){
return false;
}else{
return "您输入的证件号码长度有误";

}

}
//验证身份证号
function isCardID(sId) {
var aCity = {
11: "北京",
12: "天津",
13: "河北",
14: "山西",
15: "内蒙古",
21: "辽宁",
22: "吉林",
23: "黑龙江",
31: "上海",
32: "江苏",
33: "浙江",
34: "安徽",
35: "福建",
36: "江西",
37: "山东",
41: "河南",
42: "湖北",
43: "湖南",
44: "广东",
45: "广西",
46: "海南",
50: "重庆",
51: "四川",
52: "贵州",
53: "云南",
54: "西藏",
61: "陕西",
62: "甘肃",
63: "青海",
64: "宁夏",
65: "新疆",
71: "台湾",
81: "香港",
82: "澳门",
91: "国外"
}
var iSum = 0;
var info = "";
if(!/^\d{17}(\d|x)$/i.test(sId)) {
return "你输入的身份证长度或格式错误";
}

sId = sId.replace(/x$/i, "a");
if(aCity[parseInt(sId.substr(0, 2))] == null) {
return "你的身份证地区非法";
}
sBirthday = sId.substr(6, 4) + "-" + Number(sId.substr(10, 2)) + "-" + Number(sId.substr(12, 2));
var d = new Date(sBirthday.replace(/-/g, "/"));
if(sBirthday != (d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d.getDate())) {
return "身份证上的出生日期非法";
}

for(var i = 17; i >= 0; i--) {iSum += (Math.pow(2, i) % 11) * parseInt(sId.charAt(17 - i), 11);}
if(iSum % 11 != 1) {
return "你输入的身份证号非法";
}
//aCity[parseInt(sId.substr(0,2))]+","+sBirthday+","+(sId.substr(16,1)%2?"男":"女");//此次还可以判断出输入的身份证号的人性别
return false;
}

function show_error(msg){
	
	var json=eval("("+msg+")");	
	var info=json.tips.message;
	var map=errmsg[0];
	
	if( json.tips.statusCode == "487" || json.tips.statusCode == "488")
		alert(json.tops.message);
	else
		alert(map(json.tips.statusCode));

}

function chance_date(name){
		var currYear = (new Date()).getFullYear();	
		var opt={};
		opt.date = {preset : 'date'};
		//opt.datetime = { preset : 'datetime', minDate: new Date(2012,3,10,9,22), maxDate: new Date(2014,7,30,15,44), stepMinute: 5  };
		opt.datetime = {preset : 'datetime'};
		opt.time = {preset : 'time'};
		opt.default = {
			theme: 'android-ics light', //皮肤样式
	        display: 'modal', //显示方式 
	        mode: 'scroller', //日期选择模式
			lang:'zh',
	        startYear:currYear - 10, //开始年份
	        endYear:currYear + 10 //结束年份
		};
		
		$("#"+name).val('').scroller('destroy').scroller($.extend(opt['date'], opt['default']));
	}

function back_home(){
	window.location.href="index.html";
}

//判断身份证号是否合法
function IdentityCodeValid(code) { 
    var city={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江 ",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北 ",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏 ",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外 "};
    var tip = "";
    var pass= true;
    
 //   if(!code || !/^\d{6}(18|19|20)?\d{2}(0[1-9]|1[12])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$/i.test(code)){
  //      tip = "身份证号格式错误";
  //      pass = false;
  //  }
    
//   else if(!city[code.substr(0,2)]){
    
    if(code.length == 0 )
    	{
    	tip = "身份证号不能为空";
        pass = false;
    	}
    if(!city[code.substr(0,2)]){
        tip = "身份证地址编码错误";
        pass = false;
    }
    else{
        //18位身份证需要验证最后一位校验位
        if(code.length == 18){
            code = code.split('');
            //∑(ai×Wi)(mod 11)
            //加权因子
            var factor = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ];
            //校验位
            var parity = [ 1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2 ];
            var sum = 0;
            var ai = 0;
            var wi = 0;
            for (var i = 0; i < 17; i++)
            {
                ai = code[i];
                wi = factor[i];
                sum += ai * wi;
            }
            var last = parity[sum % 11];
            if(parity[sum % 11] != code[17]){
                tip = "身份证校验位错误";
                pass =false;
            }
        }
    }
    if(!pass) alert(tip);
    return pass;
}

function chance_date(name){
		var currYear = (new Date()).getFullYear();	
		var opt={};
		opt.date = {preset : 'date'};
		//opt.datetime = { preset : 'datetime', minDate: new Date(2012,3,10,9,22), maxDate: new Date(2014,7,30,15,44), stepMinute: 5  };
		opt.datetime = {preset : 'datetime'};
		opt.time = {preset : 'time'};
		opt.default = {
			theme: 'android-ics light', //皮肤样式
	        display: 'modal', //显示方式 
	        mode: 'scroller', //日期选择模式
			lang:'zh',
	        startYear:currYear - 10, //开始年份
	        endYear:currYear + 10 //结束年份
		};
		
		$("#"+name).val('').scroller('destroy').scroller($.extend(opt['date'], opt['default']));
	}

var app = {
    // Application Constructor
    initialize: function() {
        this.bindEvents();
    },
    // Bind Event Listeners
    //
    // Bind any events that are required on startup. Common events are:
    // 'load', 'deviceready', 'offline', and 'online'.
    bindEvents: function() {
        document.addEventListener('deviceready', this.onDeviceReady, false);
        
    },
    
    onDeviceReady: function() {
    	
        app.receivedEvent('deviceready');
       // navigator.splashscreen.hide();
     
        document.addEventListener('deviceready', initial, false);
        document.addEventListener("backbutton", onBackKeyDown, false); //返回键  
        document.addEventListener("resume", onResume, false); //监听返回前台事件
  //      document.addEventListener("backbutton", onKeyBoardShow, false); //软键盘弹出操作
  //      document.addEventListener("backbutton", onKeyBoardHide, false); //软键盘缩回操作
    },

    receivedEvent: function(id) {
        console.log('Received Event: ' + id);
    }
   
};
function onResume(){
	setTimeout(function(){
		top.check_connect_stat();
	},1000);
}
function onKeyBoardShow(bottom) {
	 var diff = ($('input[type=text]:focus').offset().top - bottom) + 50;
	 alert("KeyBoardShow diff="+diff);
	 if(diff > 0) {
	  $('body').css("top", (diff * -1) + "px");
	 }
	};
	
function onKeyBoardHide() {
	alert("KeyBoardHide");
	 $('body').css("top", "0px");
};

function host_hide(){
	$('#host').hide();
}

function host_show(){
	$("#host").show();
}

function p_hide(){	
	$("#process_s").hide();
}

function p_show(){
	$("#process_s").show();
}

function onBackKeyDown() {
	
		var acount=localStorage.getItem("acount");
		var pagenum = localStorage.getItem("pagenum");
		var pageimgnum = localStorage.getItem("pageimgnum");
		
		if(pagenum > 0){
			if(pageimgnum > 0){
				pageimgnum--;
				localStorage.setItem("pageimgnum", pageimgnum);
				var len=top.pagearr.length;
				
				if(len>0){
					pagearr[len-1]["win"].hideFrame(pagearr[len-1]["method"]);
					pagearr[len-1]["win"]=null;
					pagearr[len-1]["method"]=null;
					pagearr.pop();
				}
			}else{
				
				var h_back = localStorage.getItem("h_back_flag");
					if(h_back == '0'){
						$("#mainFrame5")[0].contentWindow.h_back();
					}else{
						document.getElementById('mainFrame5').contentWindow.document.getElementById('mainFrame5').contentWindow.h_back();
					}

				
			}
			
		}else{
			if( acount == 0 || acount < 0){		
				 top.confirmInfo("是否确认退出App?", function(msg){
					 if(msg == 1) //确认
                     {		
						top.Settings.loginState = false;
						set_settings(top.Settings);
						top.logout(navigator.app.exitApp());					
                     }
					 
				 });
								
			}else{					
				var back_type = localStorage.getItem("back_alert");
				
				if( acount == 1 && back_type == '1'){
				
					   top.confirmInfo("当前页面正在编辑中，是否确认退出", function(msg){
	                        if(msg == 1) //确认
	                        {
	                        	localStorage.setItem("back_alert", "0");
	                        	
	                        	localStorage.setItem("pagenum", '0');
	                        	localStorage.setItem("pageimgnum",'0');
	                        	
	                        	var len=top.pagearr.length;
	    						
	    						if(len>0){
	    							pagearr[len-1]["win"].hideFrame(pagearr[len-1]["method"]);
	    							pagearr[len-1]["win"]=null;
	    							pagearr[len-1]["method"]=null;
	    							pagearr.pop();
	    						}
	                        }else{
	                        	return;
	                        }
	                        });
					
				}else{
					var len=top.pagearr.length;
					
					if(len>0){
						pagearr[len-1]["win"].hideFrame(pagearr[len-1]["method"]);
						pagearr[len-1]["win"]=null;
						pagearr[len-1]["method"]=null;
						pagearr.pop();
					}
				}
			}
		}
		

}
function jquery_chance(page){
			alert(jQuery.mobile);
			alert(jQuery.mobile.changePage);
	  jQuery.mobile.changePage('#'+page, {transition : 'slide'});
}  
function up_page(){
	localStorage.setItem("pagenum", 0);
	localStorage.setItem("pageimgnum",0);
}
function back_history(){

		var len=top.pagearr.length;
		if(len>0){
			top.pagearr[len-1]["win"].hideFrame(top.pagearr[len-1]["method"]);
			top.pagearr[len-1]["win"]=null;
			top.pagearr[len-1]["method"]=null;
			top.pagearr.pop();
		}

}
function back_history_old(){
	var pageimgnum = localStorage.getItem("pageimgnum");
	pageimgnum--;
	localStorage.setItem("pageimgnum", pageimgnum);
	
	var len=top.pagearr.length;
	if(len>0){
		top.pagearr[len-1]["win"].hideFrame(top.pagearr[len-1]["method"]);
		top.pagearr[len-1]["win"]=null;
		top.pagearr[len-1]["method"]=null;
		top.pagearr.pop();
	}
}
function login_again(){
    //localStorage.clear();
	top.alertInfo('通信失败,请重新登录');
	top.start_process();
	 $.ajax({
         type : "post",
         timeout: 5000,
         url: "http://"+IP+":"+PORT+"/app/6026",
         datatype : "json",
         success : function(){
        	 top.stop_process();
        	// top.location.href = "login_new.html";
        	 top.login_show();
         },
         error:function(msg){
      	   top.stop_process();
      	 top.login_show();
       //  top.location.href = "login_new.html";
         }
         });
    
}
function get_time_str(date1){
	 
    var date2 = new Date();    //结束时间  
    var date3 = date2.getTime() - new Date(date1).getTime();   //时间差的毫秒数        
  
    //------------------------------  
  
    //计算出相差天数  
    var days=Math.floor(date3/(24*3600*1000));  
  
    //计算出小时数  
  
    var leave1=date3%(24*3600*1000);    //计算天数后剩余的毫秒数  
    var hours=Math.floor(leave1/(3600*1000));  
    //计算相差分钟数  
    var leave2=leave1%(3600*1000);        //计算小时数后剩余的毫秒数  
    var minutes=Math.floor(leave2/(60*1000));  
    //计算相差秒数  
    var leave3=leave2%(60*1000);      //计算分钟数后剩余的毫秒数  
    var seconds=Math.round(leave3/1000);  
//    alert(" 相差 "+days+"天 "+hours+"小时 "+minutes+" 分钟"+seconds+" 秒")  
    /*
    if(days > 365 ){
    	return "一年前";
    }else if(days > 31 ){
    	return days/30+"月前";
    }else*/ 
    if(days > 0 ){
    	return date1.substr(0,10);
    }else{
    	if(hours > 0){
    		return hours + "小时前";
    	}else{
    		if(minutes > 0){
    			return minutes + "分钟前";
    		}else{
    			return "刚才";
    		}
    	}
    }
}
function get_imgname(){
	date = new Date();  
	
	var year = date.getFullYear();  
	var month = date.getMonth()+1;  
	var day = date.getDate();
	if(month<10) month = "0"+month;  
	if(day<10) day = "0"+day;
	
	var hours = date.getHours();  
	var mins = date.getMinutes();  
	var secs = date.getSeconds();  
	var msecs = date.getMilliseconds();  
	if(hours<10) hours = "0"+hours;  
	if(mins<10) mins = "0"+mins;  
	if(secs<10) secs = "0"+secs;  
	if(msecs<10) secs = "0"+msecs; 
	return (year+month+day+hours+mins+secs+msecs);
}
function get_trandate(){
	date = new Date();  
	
	var year = date.getFullYear();  
	var month = date.getMonth()+1;  
	var day = date.getDate();
	if(month<10) month = "0"+month;  
	if(day<10) day = "0"+day;
	
	var hours = date.getHours();  
	var mins = date.getMinutes();  
	var secs = date.getSeconds();  
	var msecs = date.getMilliseconds();  
	if(hours<10) hours = "0"+hours;  
	if(mins<10) mins = "0"+mins;  
	if(secs<10) secs = "0"+secs;  
	if(msecs<10) secs = "0"+msecs; 
	return (year+'/'+month+'/'+day+' '+hours+':'+mins);
}
function scroll_auto(){				
	if($("body").css("overflow") == "auto"){
		$("body").attr("style", "overflow:scroll");
	}else{
		$("body").attr("style", "overflow:auto");
	}
}

function set_settings(settings){
	top.writelog("set setttings="+JSON.stringify(settings));
	localStorage.setItem("settings", JSON.stringify(settings));
}

function get_settings(){
	
	var settingsText = localStorage.getItem('settings') || "{}";
	top.writelog("get settings="+settingsText);
	return JSON.parse(settingsText);
}

function exitApp_index() {
		
		navigator.app.exitApp(); 
}
app.initialize();