YUI.add("dialog", function(Y) {

	/* Any frequently used shortcuts, strings and constants */
	var Lang = Y.Lang;

	function Dialog(config) {

		Dialog.superclass.constructor.apply(this, arguments);
	}

	Dialog.NAME = "dialog";

	Dialog.ATTRS = {
		msg : {},
		centered : {
			value : true
		},
		width : {
			value : 410
		},
		zIndex : {
			value : 6
		},
		buttons : {
			value : {
				footer : [ {
					name : 'cancel',
					label : 'Cancel',
					action : 'onCancel'
				},

				{
					name : 'proceed',
					label : 'OK',
					action : 'onOK'
				} ]
			}
		}
	};

	Y.extend(Dialog, Y.Panel, {

		initializer : function() {
		},

		destructor : function() {
		},
		renderUI : function() {
			this.set("bodyContent", '<div class="message icon-warn">'
					+ this.get("msg") + '</div>');
		},
		onCancel : function(e) {
			e.preventDefault();
			this.hide();
			// the callback is not executed, and is
			// callback reference removed, so it won't persist
			this.callback = false;
		}
	});

	Y.Dialog = Dialog;

}, "0.0.1", {
	requires : [ "panel" ]
});