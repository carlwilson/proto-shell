function checkNewManifests() {
	$("#new-accessions").find("li").each(function() {
		var listItem = $(this);
		$.ajax(
				{url : "/beam/manifests/" + listItem.attr("id"), // add collection url
				 type : "GET", // PUT request
				 dataType:"json",
				 statusCode : { // Map the status codes expected
					404 : function(xhr, txtStts, errThrwn) { // 404 == root
															// dir doesn't
					 	listItem.click(function(event) {queueNewManifest(event);});
					 	listItem.children("label").first().append("<strong>Not queued for scanning.</strong>");
					}},
				 success : function(data, status, jqXhr) {
					 	listItem.click(function(event) {window.location = "/beam/manifests/" + listItem.attr("id");});
					 	var percent = data.progress * 100;
					 	if (percent < 1) {
						 	listItem.children("label").first().append("<strong>Queued for scanning.</strong>");
						 	listItem.find(".progressbar").children().first().width(percent + "%");
					 	} else if (percent > 99) {
					 		listItem.find(".progressbar").replaceWith("<a>Click to add.</a>");
					 	} else {
						 	listItem.children("label").first().append("<strong>Currently scanning: " + percent.toFixed(2) + "% complete</strong>");
						 	listItem.find(".progressbar").children().first().width(percent + "%");
					 	}
				 	}
			});
		});
	setTimeout(function() {checkProgress();}, 1000);
}

function checkProgress() {
	$("#new-accessions").find("li").each(function() {
		var listItem = $(this);
	 	var progWidth = listItem.find(".progressbar").css("width");
	 	var barWidth = listItem.find(".progressbar").children().first().css("width");
	 	if (progWidth != barWidth) {
			$.ajax(
					{url : "/beam/manifests/" + listItem.attr("id"), // add collection url
					 type : "GET", // PUT request
					 dataType:"json",
					 success : function(data, status, jqXhr) {
	 					 	var percent = data.progress * 100;
						 	if (percent > 99) {
						 		listItem.find("strong").replaceWith();
						 		listItem.find(".progressbar").replaceWith("<a class>Click to add.</a>");
						 	} else if (percent > 0) {
						 		listItem.find("strong").replaceWith("<strong>Currently scanning: " + percent.toFixed(2) + "% complete</strong>");
							 	listItem.find(".progressbar").children().first().width(percent + "%");
						 	}
					 }
				});
	 	}
	});
	setTimeout(function() {checkProgress();}, 5000);
}

/**
 * Function to update a collection
 */
function queueNewManifest(event) {
	var listItem = $(event.target);
	if (!($(listItem).is("li"))) {
		listItem = $(listItem).parents("li").first();		
	}
		
	$.ajax(
		{url : "/beam/manifests/" + listItem.attr("id"), // add collection url
		 type : "PUT", // PUT request
		 contentType:"application/json; charset=utf-8",
		 statusCode : { // Map the status codes expected
			404 : function(xhr, txtStts, errThrwn) { // 404 == root
													// dir doesn't
				alert("404 Problem - root directory for collection not found.");
			}},
		 success : function(data, status, jqXhr) {
			 	window.location = jqXhr.getResponseHeader('Location');
		 	}
	});
}
