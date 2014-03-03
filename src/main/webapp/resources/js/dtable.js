YUI({filter:'raw'}).use("datatable", "datatable-paginator", "datatype-number", "datasource-io", "datasource-jsonschema", function(Y){
    
	var myDataSource = new Y.DataSource.IO({
		source : "pagination"
	});

	myDataSource.plug(Y.Plugin.DataSourceJSONSchema, {
		schema : {
			resultFields : [ "name", "price", "qty"]
		}
	});

	var table = new Y.DataTable({
		columns : [
				{
					key : 'id',
					label : ' ',
					width : '70px',
					className : 'numeric'
				},
				{
					key : 'name',
					label : 'Food'
				},
				{
					key : 'price',
					label : 'Price',
					formatter : function(o) {
						return Y.Number.format(o.data.price, {
							prefix : "$",
							thousandsSeparator : ",",
							decimalSeparator : ".",
							decimalPlaces : 2
						})
					},
					width : '100px',
					className : 'numeric'
				},
				{
					key : 'qty',
					label : 'QTY',
					width : '80px',
					className : 'numeric'
				},
				{
					key : 'fn',
					label : 'Price',
					formatter : function(o) {
						return Y.Number.format(o.data.price
								* o.data.qty, {
							prefix : "$",
							thousandsSeparator : ",",
							decimalSeparator : ".",
							decimalPlaces : 2
						});
					},
					width : '120px',
					className : 'numeric'
				} ],
		rowsPerPage : 10,
		paginatorLocation : [ 'header', 'footer' ]
	});

	table.plug(Y.Plugin.DataTableDataSource, {
		datasource : myDataSource
	});

	table.render('#dtable').showMessage('loadingMessage');

	// Load the data into the table
	table.datasource.load();
        
        table.after("change", function(e){
            console.log(e);
        });
});