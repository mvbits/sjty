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


//		var IP="183.93.198.51";
//		var PORT="88";
var IP="192.168.1.9";
var PORT="8080";
//var IP="192.168.254.67";
//var PORT="88";
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

function show_error(msg){
	
	var json=eval("("+msg+")");	
	var info=json.tips.message;
	var map=errmsg[0];
	
	if( json.tips.statusCode == "487" || json.tips.statusCode == "488")
		alert(json.tops.message);
	else
		alert(map(json.tips.statusCode));

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
       // document.addEventListener("backbutton", eventBackButton, false); //返回键     
        document.addEventListener("backbutton", onBackKeyDown, false); //返回键   
    },

    receivedEvent: function(id) {
        console.log('Received Event: ' + id);
    }
   
};

function eventBackButton() { 	
	alert("why");
    document.removeEventListener("backbutton", eventBackButton, false); // 注销返回键 
    document.addEventListener("backbutton", exitApp, false);//绑定退出事件 
    // 3秒后重新注册 
    var intervalID = window.setInterval(function() { 
       window.clearInterval(intervalID); 
       document.removeEventListener("backbutton", exitApp, false); // 注销返回键 
       document.addEventListener("backbutton", eventBackButton, false); // 返回键 
   }, 3000); 
                     
} 

function onBackKeyDown() {
	navigator.app.exitApp();

}
function exitApp() {
		navigator.app.exitApp(); 
}
app.initialize();