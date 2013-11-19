YUI().use("datatable", "datatable-paginator", "datatype-number", "datasource-io", "datasource-jsonschema", function(Y){
    var foods = ('Achoccha|Amaranth|Angelica|Anise|Apple|Arrowroot|Arrugula|' +
            'Artichoke|Asparagus|Atemoya|Avocado|Balsam Apple|Balsam Pe' +
            'ar|Bambara groundnut|Bamboo|Banana and Plantains|Barbados ' +
            'Cherry|Beans|Beet|Blackberry|Blueberry|Bok Choy|Boniato (T' +
            'ropical Sweetpotato, also Batatas or Carnote)|Broccoli|Bro' +
            'ccoli|Brussels sprouts|Bunch Grape|Burdock|Cabbage|Calabaz' +
            'a (Tropical Pumpkin)|Cantaloupes and Muskmelons|Capers|Car' +
            'ambola (Star Fruit)|Cardoon|Carrot|Cassava (Spanish common' +
            ' name Yuca)|Cauliflower|Celeriac|Celery|Celtuce|Chard|Chay' +
            'a|Chayote|Chicory|Chinese Cabbage (Bok Choy)|Chinese Jujub' +
            'e|Chinese Radish|Chives|Chrysanthemum|Chufa|Cilantro|Citro' +
            'n|Coconut Palm|Collards|Comfrey|Corn salad|Corn|Cuban Swee' +
            't Potato|Cucumber|Cushcush|Daikon|Dandelion|Dasheen|Dill|E' +
            'ggplant|Endive|Eugenia|Fennel|Fig|Galia Muskmelon|Garbanzo' +
            '|Garlic|Gherkin, West Indian|Ginger|Ginseng|Gourds|Grape|G' +
            'uar|Guava|Hanover Salad|Horseradish|Horseradish tree|Huckl' +
            'eberry|Ice Plant|Jaboticaba|Jackfruit|Jicama|Jojoba|Kale|K' +
            'angkong|Kohlrabi|Leek|Lentils|Lettuce|Longan|Loquat|Lovage' +
            '|Luffa Gourd|Lychee|Macadamia|Malanga (also called Tannia ' +
            'or Tanier)|Mamey Sapote|Mango|Martynia|Melon|Momordica|Mus' +
            'cadine Grape|Mushroom|Muskmelons and Cantaloupes|Mustard|M' +
            'ustard collard|Mustard, potherb|Naranjillo|Nasturtium|Nect' +
            'arine|Okra|Onion|Orach|Oriental Persimmon|Papaya|Paprika|P' +
            'arsley|Parsley root|Parsnip|Passion Fruit|Peach|Peas|Peanu' +
            't Production|Pear|Pecan|Pepper|Persimmon|Pimiento|Pineappl' +
            'e|Pineapple Guava|Pitaya|Plum|Pokeweed|Pomegranate|Potato|' +
            'Pumpkin|Purslane|Radicchio|Radish|Rakkyo|Rampion|Raspberry' +
            '|Rhubarb|Romaine Lettuce|Roselle|Rutabaga|Saffron|Salsify|' +
            'Sapodilla|Sarsaparilla|Sassafrass|Scorzonera|Sea kale|Seag' +
            'rape|Shallot|Skirret|Smallage|Sorrel|Southern pea|Soybeans' +
            '|Spinach|Spondias|Squash|Strawberries|Sugar Apple|Swamp Ca' +
            'bbage|Sweet Basil|Sweet Corn|Sweet potato|Swiss Chard|Toma' +
            'tillo|Tomato|Truffles|Turnip|Upland cress|Water celery|Wat' +
            'erchestnut|Watercress|Watermelon|Yams').split('|'),
    data = [],
    i,
    len;

	for (i = 0, len = foods.length; i < len; i++) {
		data.push({
			id : i + 1,
			name : foods[i],
			price : Math.round((Math.random()) * 500) / 500,
			qty : Math.ceil(len * Math.random())
		});
	}

	var myDataSource = new Y.DataSource.IO({
		source : "pagination"
	});

	myDataSource.plug(Y.Plugin.DataSourceJSONSchema, {
		schema : {
			resultListLocator : "ResultSet.Result",
			resultFields : [ "Title" ]
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
		data : data,
		rowsPerPage : 10,
		paginatorLocation : [ 'header', 'footer' ]
	});

	table.plug(Y.Plugin.DataTableDataSource, {
		datasource : myDataSource
	});

	table.render('#dtable').showMessage('loadingMessage');

	// Load the data into the table
	table.datasource.load();
});