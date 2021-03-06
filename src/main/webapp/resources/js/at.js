YUI.add('atwho', function(Y) {
	
    var Lang = Y.Lang,    
    Node = Y.Node,
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
    
    AtWho.CONTAINER  = "<div id='atwho-container'></div>";
    AtWho.AT_DIV = "<div id='at-view' class='at-view'><ul id='at-view-ul'></ul></div>";
    AtWho.DEFAULT_TPL = "<li data-value='{name}'>{name}</li>";
    
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
	    		var request = Y.io(data);
	    	}
	    },
	
	    destructor : function() {},
	
	    renderUI : function() {
	    	var bodyNode = Y.one(document.body);	    	
	    	bodyNode.append(AtWho.CONTAINER);
	    },
	
	    bindUI : function() {	 
	    	
	    	var sub = {
	    		keyup : this._onKeyup,
	    		keydown : this._onKeydown,
	    		scroll : function(e){
	    			console.log("scroll");
	    		},
	    		blur : function(e){
	    			console.log("blur");
	    		}
	    	};
	    	
	    	Y.on(sub, this.get(INPUT_NODE))
	    },
	
	    syncUI : function() {},
	    
	    _onKeyup : function(e){
	    	console.log("keyup");
	        var KEY_CODE = {
	        	      DOWN: 40,
	        	      UP: 38,
	        	      ESC: 27,
	        	      TAB: 9,
	        	      ENTER: 13
	        	    };
	        
	        switch(e.keyCode){
	        	case KEY_CODE.ESC:
	        		break;
	        	default : 
	        		break;
	        }
	    	
	    },
	    _onKeydown : function(e){
	    	console.log("keydown");
	    },
	    _dispatch : function(){
	    	this._lookUp();
	    },
	    _lookUp : function(){
	    	var data = this.get("data");
	    	this._renderView(data);
	    },
	    _renderView : function(data){
	    	for(var i = 0; i < data.length; i++){
	    		var li = Node.create(Y.Lang.sub(AtWho.DEFAULT_TPL, {name: data[i]}));
	    	}
	    },
	    _render : function(){
	    	
	    }
    });
    
    Y.AtWho = AtWho;
}, "0.0.1", {requires : ["widget", "io-base"]});