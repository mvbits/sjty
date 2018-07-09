cordova.define("com.xiyu.plugin.myphone", function(require, exports, module) {
var exec = require('cordova/exec');

module.exports = {

/**
*  消失通讯框
*
*/
callphone: function(msg) {
exec(null, null, "CallPhone","call_phone", [msg]);
},



};
});