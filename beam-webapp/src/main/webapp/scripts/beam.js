// jquery extension 
$.extend({
	// Function to get the URL vars
	getUrlVars : function() {
		var vars = [], hash;
		var hashes = window.location.href.slice(
				window.location.href.indexOf('?') + 1).split('&');
		for ( var i = 0; i < hashes.length; i++) {
			hash = hashes[i].split('=');
			vars.push(hash[0]);
			vars[hash[0]] = hash[1];
		}
		return vars;
	},
	// Function to get them by name
	getUrlVar : function(name) {
		return $.getUrlVars()[name];
	}
});

/**
 * Popup a Modal Prompt with the messages, currently needs the modal popup HTML in the 
 * page, though this could be injected from a fragment.
 * 
 * Passing a non-string as a param means the text will be left as is, use nulls to skip params.
 * 
 * @param header the header test
 * @param message the message
 * @param instruction any button instructions
 * @param buttonText the button text
 */
function popUpModalPrompt(header, message, instruction, buttonText) {
	if (typeof header=="string") $('#message').children('h2').first().text(header);
	if (typeof message=="string") $('#message').children('p').first().text(message);
	if (typeof instruction=="string") $('#message').children('p').last().text(instruction);
	if (typeof buttonText=="string") $('#message').children('button').first().text(buttonText);
	$('#prompt').overlay().load();
}

function formatDate(date) {
	return date.toDateString() + " " + date.getHours() + ":" + date.getMinutes();
}

/**
 * Populate the nav-bar from the fragment
 * @param itemId the item to select
 */
function getNavbar(itemId) {
	$.get('/frag/navbar.html', function(response) {
		$('#navbar').html(response);
		if (typeof itemId=="string") selNavbarItem(itemId);
	});			
}

/**
 * Set the passed item as selected.
 * @param itemId the item to select
 */
function selNavbarItem(itemId) {
	$('#navbar').find('a').each(function() {
		$(this).removeClass("select");
		if ($(this).attr("id") == itemId) $(this).addClass("select");
	});
}

/**
 * Convert number of bytes into human readable format
 *
 * @param integer bytes     Number of bytes to convert
 * @param integer precision Number of digits after the decimal separator
 * @return string
 */
function bytesToSize(bytes)
{  
    var kilobyte = 1024;
    var megabyte = kilobyte * 1024;
    var gigabyte = megabyte * 1024;
    var terabyte = gigabyte * 1024;
    var precision = 2;
    if ((bytes >= 0) && (bytes < kilobyte)) {
        return bytes + ' B';
 
    } else if ((bytes >= kilobyte) && (bytes < megabyte)) {
        return (bytes / kilobyte).toFixed(precision) + ' KB';
 
    } else if ((bytes >= megabyte) && (bytes < gigabyte)) {
        return (bytes / megabyte).toFixed(precision) + ' MB';
 
    } else if ((bytes >= gigabyte) && (bytes < terabyte)) {
        return (bytes / gigabyte).toFixed(precision) + ' GB';
 
    } else if (bytes >= terabyte) {
        return (bytes / terabyte).toFixed(precision) + ' TB';
 
    } else {
        return bytes + ' B';
    }
}
