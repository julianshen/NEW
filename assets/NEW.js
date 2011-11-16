var scripts = document.getElementsByTagName("script");
var thisScript = scripts[scripts.length-1];
var thisScriptsSrc = thisScript.src;

var scriptOrigin = thisScript.src.substr(0, thisScript.src.length - '/web/NEW.js'.length);

function jsonGet(url, data, callback) {
	url = scriptOrigin + url;
	if(typeof(Ext) != 'undefined') {
		Ext.util.JSONP.request({
		    url: url,
		    params: data,
		    callbackKey: 'callback',
		    callback: function(response){
		        callback(response);
		    }
		});
	} else {
		//jquery
		$.ajax(url, {
			data: data,
			dataType: 'json',
			success: function(data) {
				callback(data);
			}
		}
		);
	}
}

var NEW = {
	voiceInput: function(callback) {
		console.log('voice');
		jsonGet('/voice', {}, callback);
	},
	echo: function(msg, callback) {
		jsonGet('/echo', {message:msg}, callback);
	}
};