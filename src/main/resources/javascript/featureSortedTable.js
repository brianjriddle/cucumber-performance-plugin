jQuery(document).ready(function() {
	/* Feature-level table */
	jQuery('#sortedFeatureTable').dataTable({
		"aaData" : sortedFeatureData,
		"aoColumns" : [ {
			"sType" : "string"
		}, 
		null, 
		{
			"iDataSort" : 5
		}, {
			"iDataSort" : 6
		}, {
			"iDataSort" : 7
		}, {
			"bVisible" : false
		}, {
			"bVisible" : false
		}, {
			"bVisible" : false
		} ],
		aaSorting : [ [ 7, 'desc' ] ],
		"bPaginate" : false,
		"bFilter" : false,
		"bSort" : true,
		"bInfo" : false
	});
});