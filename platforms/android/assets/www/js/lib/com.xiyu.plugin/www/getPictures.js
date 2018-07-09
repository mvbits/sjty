cordova.define("com.xiyu.plugin.getPictures", function(require, exports, module) {
var exec = require('cordova/exec');

module.exports = {
    
    /**
     *  消失通讯框
     *
     */
    show: function (success, fail, options) {
    	options = options || {}

    	  options.minImages = options.minImages || 0
    	  options.maxImages = options.maxImages || 5
    	  options.mediaType = options.mediaType || 'any'
    	  options.width = options.width || 0
    	  options.height = options.height || 0
    	  options.quality = options.quality || 100
    	exec(success, fail, 'ImagePicker', 'getPictures', [options])
        },
       
       
               
   

};
});