YUI().use("io-base", function(Y){
	
	function complete(id, o, args) {
        var id = id; // Transaction ID.
        var data = o.responseText; // Response data.
        var args = args[1]; // 'ipsum'.
        console.log(data);
    };
    
    var cfg = {
    	    headers: {
    	        'Content-Type': 'application/json',    	        
    	        'version' : 'international_eub_us_1.1',
    	        'authenticate' : 'emseub_15bbd18f29bd3696a2aad23bcb857829'
    	    }
    	};
	
    Y.on('io:complete', complete, Y, ['lorem', 'ipsum']);
	var request = Y.io("http://www.ems.com.cn/partner/api/public/p/area/cn/province/list", cfg);
});