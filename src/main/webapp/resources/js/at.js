YUI.add('atwho', function(Y) {
    var Lang = Y.Lang;
    
    function AtWho(config){
        AtWho.superclass.constructor.apply(this, arguments);
    }
    
    AtWho.NAME = "atWho";
    
    AtWho.ATTRS = {
        
    };
    
    Y.extend(AtWho, Y.Widget, {
        
    });
    
    Y.AtWho = AtWho;
}, "0.0.1", {requires : ["widget"]});