YUI.add('atwho', function(Y) {
	
    var Lang = Y.Lang,    
    INPUT_NODE          = 'inputNode';
    
    function AtWho(config){
        AtWho.superclass.constructor.apply(this, arguments);
    }
    
    AtWho.NAME = "atWho";
    
    AtWho.ATTRS = {
        data : {
        	value : null
        }
    };
    
    AtWho.AT_DIV = "<div id='at-view' class='at-view'><ul id='at-view-ul'></ul></div>";
    AtWho.DEFAULT_TPL = "<li data-value='${name}'>${name}</li>";
    
    Y.extend(AtWho, Y.Widget, {   
    	
	    initializer: function() {
	    	var data = this.get("data");
	    	if(Lang.isString(data)){
	    		
	    		function complete(id, o, args) {
	    	        var id = id; // Transaction ID.
	    	        var data = o.responseText; // Response data.
	    	        var args = args[1]; // 'ipsum'.
	    	        data = JSON.parse(data);
	    	        data = data.map(function(item){
	    	        	if(Lang.isString(item)){
	    	        		item = {
	    	        			name : item
	    	        		}
	    	        	}
	    	        	return item;	    	        	
	    	        });
	    	        this.set("data", data);
	    	    };
	    		
	    	    Y.on('io:complete', complete, Y, ['lorem', 'ipsum']);
	    		var request = Y.io(uri);
	    	}
	    },
	
	    destructor : function() {},
	
	    renderUI : function() {},
	
	    bindUI : function() {
	    	this.get(INPUT_NODE).on
	    },
	
	    syncUI : function() {}
    });
    
    Y.AtWho = AtWho;
}, "0.0.1", {requires : ["widget"]});