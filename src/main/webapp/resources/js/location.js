YUI({filter : "raw"}).use("jsonp",function(Y){
	var url="http://api.dangqian.com/apidiqu2/api.asp?format=json&id=000000000000&callback='{callback}'";
	function handleJSONP(response){
		console.log(response);
	}
	Y.jsonp(url,handleJSONP);	
});