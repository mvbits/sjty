cordova.define("com.xiyu.plugin", function(require, exports, module) {
var exec = require('cordova/exec');

module.exports = {
    
    /**
     *  消失通讯框
     *
     */
    call_phone: function(msg) {
        exec(null, null, "MyAlert","call_phone", [msg]);
        },
               
   

};
});