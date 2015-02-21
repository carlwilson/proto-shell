/**
 * Function to update a collection
 */
function updateCollection(event) {
	event.preventDefault();
	$.get("/beam/collections/" + $('#name').val(),
		function(collection){
			collection.details.title = $('#title').val();
			$.ajax(
				{url : "/beam/collections/" + $('#name').val(), // add collection url
				 type : "PUT", // PUT request
				 contentType:"application/json; charset=utf-8",
  				 data : JSON.stringify(collection), // Json collection the form data
				 statusCode : { // Map the status codes expected
					404 : function(xhr, txtStts, errThrwn) { // 404 == root
															// dir doesn't
						alert("404 Problem - root directory for collection not found.");
					}},
				 success : function(data, status, jqXhr) {
					 	window.location = jqXhr.getResponseHeader('Location');
				 	}
			});
		}, "json");
}
/**
 * Function to create a collection
 */
function createCollection(event) {
	event.preventDefault();
	$.ajax(
		{url : "/beam/collections/" + $('#name').val(), // add collection url
		 type : "POST", // POST request
		 data : $('#coll-form').serialize(), // Serialize the form data
		 statusCode : { // Map the status codes expected
			403 : function(xhr, txtStts, errThrwn) { // 409 ==
													// collection
													// exists
				alert("403 Forbidden - it's naughty to be novel.");
			},
			409 : function(xhr, txtStts, errThrwn) { // 409 ==
													// collection
													// exists
				alert("409 Problem - collection with that name already exists");
			},
			404 : function(xhr, txtStts, errThrwn) { // 404 == root
													// dir doesn't
				alert("404 Problem - root directory for collection not found.");
			}},
		 success : function(data, status, jqXhr) {
			 	window.location = jqXhr.getResponseHeader('Location');
		 	}
	});
}
