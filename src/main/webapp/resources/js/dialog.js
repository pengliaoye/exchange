YUI.add("dialog", function(Y) {

    /* Any frequently used shortcuts, strings and constants */
    var Lang = Y.Lang;

    /* MyWidget class constructor */
    function Dialog(config) {
    	Dialog.superclass.constructor.apply(this, arguments);
    }

    /*
     * Required NAME static field, to identify the Widget class and
     * used as an event prefix, to generate class names etc. (set to the
     * class name in camel case).
     */
    Dialog.NAME = "dialog";

    /*
     * The attribute configuration for the widget. This defines the core user facing state of the widget
     */
    Dialog.ATTRS = {

        attrA : {
            value: "A"                     // The default value for attrA, used if the user does not set a value during construction.

            /*
            , valueFn: "_defAttrAVal"      // Can be used as a substitute for "value", when you need access to "this" to set the default value.

            , setter: "_setAttrA"          // Used to normalize attrA's value while during set. Refers to a prototype method, to make customization easier
            , getter: "_getAttrA"          // Used to normalize attrA's value while during get. Refers to a prototype method, to make customization easier
            , validator: "_validateAttrA"  // Used to validate attrA's value before updating it. Refers to a prototype method, to make customization easier

            , readOnly: true               // Cannot be set by the end user. Can be set by the component developer at any time, using _set
            , writeOnce: true              // Can only be set once by the end user (usually during construction). Can be set by the component developer at any time, using _set

            , lazyAdd: false               // Add (configure) the attribute during initialization.

                                           // You only need to set lazyAdd to false if your attribute is
                                           // setting some other state in your setter which needs to be set during initialization
                                           // (not generally recommended - the setter should be used for normalization.
                                           // You should use listeners to update alternate state).

            , broadcast: 1                 // Whether the attribute change event should be broadcast or not.
            */
        }

        // ... attrB, attrC, attrD ... attribute configurations.

        // Can also include attributes for the super class if you want to override or add configuration parameters
    };

    /*
     * The HTML_PARSER static constant is used if the Widget supports progressive enhancement, and is
     * used to populate the configuration for the MyWidget instance from markup already on the page.
     */
    MyWidget.HTML_PARSER = {

        attrA: function (srcNode) {
            // If progressive enhancement is to be supported, return the value of "attrA" based on the contents of the srcNode
        }

    };

    /* Templates for any markup the widget uses. Usually includes {} tokens, which are replaced through `Y.Lang.sub()` */
    MyWidget.MYNODE_TEMPLATE = "<div id={mynodeid}></div>";

    /* MyWidget extends the base Widget class */
    Y.extend(MyWidget, Y.Widget, {

        initializer: function() {
            /*
             * initializer is part of the lifecycle introduced by
             * the Base class. It is invoked during construction,
             * and can be used to setup instance specific state or publish events which
             * require special configuration (if they don't need custom configuration,
             * events are published lazily only if there are subscribers).
             *
             * It does not need to invoke the superclass initializer.
             * init() will call initializer() for all classes in the hierarchy.
             */

             this.publish("myEvent", {
                defaultFn: this._defMyEventFn,
                bubbles:false
             });
        },

        destructor : function() {
            /*
             * destructor is part of the lifecycle introduced by
             * the Widget class. It is invoked during destruction,
             * and can be used to cleanup instance specific state.
             *
             * Anything under the boundingBox will be cleaned up by the Widget base class
             * We only need to clean up nodes/events attached outside of the bounding Box
             *
             * It does not need to invoke the superclass destructor.
             * destroy() will call initializer() for all classes in the hierarchy.
             */
        },

        renderUI : function() {
            /*
             * renderUI is part of the lifecycle introduced by the
             * Widget class. Widget's renderer method invokes:
             *
             *     renderUI()
             *     bindUI()
             *     syncUI()
             *
             * renderUI is intended to be used by the Widget subclass
             * to create or insert new elements into the DOM.
             */

             // this._mynode = Node.create(Y.Lang.sub(MyWidget.MYNODE_TEMPLATE, {mynodeid: this.get("id") + "_mynode"}));
        },

        bindUI : function() {
            /*
             * bindUI is intended to be used by the Widget subclass
             * to bind any event listeners which will drive the Widget UI.
             *
             * It will generally bind event listeners for attribute change
             * events, to update the state of the rendered UI in response
             * to attribute value changes, and also attach any DOM events,
             * to activate the UI.
             */

             // this.after("attrAChange", this._afterAttrAChange);
        },

        syncUI : function() {
            /*
             * syncUI is intended to be used by the Widget subclass to
             * update the UI to reflect the initial state of the widget,
             * after renderUI. From there, the event listeners we bound above
             * will take over.
             */

            // this._uiSetAttrA(this.get("attrA"));
        },

        // Beyond this point is the MyWidget specific application and rendering logic

        /* Attribute state supporting methods (see attribute config above) */

        _defAttrAVal : function() {
            // this.get("id") + "foo";
        },

        _setAttrA : function(attrVal, attrName) {
            // return attrVal.toUpperCase();
        },

        _getAttrA : function(attrVal, attrName) {
            // return attrVal.toUpperCase();
        },

        _validateAttrA : function(attrVal, attrName) {
            // return Lang.isString(attrVal);
        },

        /* Listeners, UI update methods */

        _afterAttrAChange : function(e) {
            /* Listens for changes in state, and asks for a UI update (controller). */

            // this._uiSetAttrA(e.newVal);
        },

        _uiSetAttrA : function(val) {
            /* Update the state of attrA in the UI (view) */

            // this._mynode.set("innerHTML", val);
        },

        _defMyEventFn : function(e) {
            // The default behavior for the "myEvent" event.
        }
    });

    Y.Dialog = Dialog;

}, "0.0.1", {requires:["panel"]});

YUI().use("panel", "escape", function (Y) {  // loading escape only for security on this page

        var dialog = new Y.Panel({
        contentBox : Y.Node.create('<div id="dialog" />'),
        bodyContent: '<div class="message icon-warn">Are you sure you want to [take some action]?</div>',
        width      : 410,
        zIndex     : 6,
        centered   : true,
        modal      : false, // modal behavior
        render     : '.example',
        visible    : false, // make visible explicitly with .show()
        buttons    : {
            footer: [
                {
                    name  : 'cancel',
                    label : 'Cancel',
                    action: 'onCancel'
                },

                {
                    name     : 'proceed',
                    label    : 'OK',
                    action   : 'onOK'
                }
            ]
        }
    });

    dialog.onCancel = function (e) {
        e.preventDefault();
        this.hide();
        // the callback is not executed, and is
        // callback reference removed, so it won't persist
        this.callback = false;
    }

    dialog.onOK = function (e) {
        e.preventDefault();
        this.hide();
        // code that executes the user confirmed action goes here
        if(this.callback){
           this.callback();
        }
        // callback reference removed, so it won't persist
        this.callback = false;
    }

    
//////////////////////  Interactive Example JS ////////////////////////////////

    // action to do when the user clicks "OK" button.
    var doSomething = function(){ 
        Y.log('Something was done.');    
    };
    
    // handle a radio click: update the icon and the code snippet
    Y.all('#radios input').on('click', function(e){
        var iconClass = 'message ' + e.currentTarget.getAttribute('value');
        Y.one('#dialog .message').set('className', iconClass);
        dialog.callback = doSomething; // add a callback so when the user clicks "OK" button, something will get done
    });
    
    // handle the textarea keyup: put text in dialog, and update code snippet
    Y.one('#test-controls textarea').on('keyup', function(e){
        Y.one('#dialog .message').setHTML(Y.Escape.html(e.target.get('value')));
    });
    
    
    Y.one('.btn-show').on('click', function(){
        // if page is refreshed, radios stay selected, but message icon needs to by sync'ed.
        // this is for the example only.
        Y.all('#radios input').each(function (o, i) {
            if (o.get('checked') === true){
                Y.one('#dialog .message').set('className', 'message ' + o.getAttribute('value'));
            }
        });
        dialog.callback = doSomething; // if you want a callback, you must add it every time, because on-hide it's removed
        dialog.show();
    });
    
    // init with a callback
    dialog.callback = doSomething; // add a callback so when the user clicks "OK" button, something will get done

    });