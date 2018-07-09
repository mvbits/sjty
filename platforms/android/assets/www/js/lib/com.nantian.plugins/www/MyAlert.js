cordova.define("com.nantian.plugins.myalert", function(require, exports, module) { var exec = require('cordova/exec');
               
               module.exports={
               
               /**
                *  存储数据到原生共享域
                *
                *  @param successCallback 存储成功的回调
                *  @param failCallback    存储失败的回调
                *  @param name  参数名
                *  @param value 参数值
                *
                */
               
               
               show: function(msg) {  
            	   exec(null, null, "MyAlert", "show", [msg]);  
       	    },
                                              
      };
                              
});