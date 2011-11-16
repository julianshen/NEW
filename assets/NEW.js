
function jsonGet(url, data, callback) {
	if(typeof(Ext) != 'undefined') {
		Ext.Ajax.request({
		    url: url,
		    params: data,
		    success: function(response){
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
}

var NEW = {
	voiceInput: function(callback) {
		jsonGet('/voice', {}, callback);
	},
	echo: function(callback, msg) {
		jsonGet('/echo', msg, callback);
	}
};