function callGETJSONAjax(url,responceFunc){

	
	try{
		var jqxhr =
		$.ajax({
			url: url,
			processData : false,
			type : "GET"
		})
		.done (function(data) {eval(responceFunc)(data);})
		.fail   (function()  {eval(responceFunc)("Error");});
	}
	catch(err){
		var jqxhr =
		parent.$.ajax({
			url: url,
			processData : false,
			type : "GET"
		})
		.done (function(data) {eval(responceFunc)(data);  })
		.fail   (function()  {eval(responceFunc)("Error");});
	}
}



function callPOSTJSONAjax(url,data,responceFunc){
	try{
		
		
		var jqxhr =$.ajax({
		        url: url,
		        method: 'POST',
		        data: data,
		        crossDomain: true,
		        contentType: 'application/json',
		        beforeSend: function ( xhr ) {
		            xhr.setRequestHeader( 'Authorization', 'Basic username:password' );
		        },
			})
				.done (function(data) {eval(responceFunc)(data);})
				.fail   (function()  {eval(responceFunc)("Error");});
	}
	catch(err){
		//alert(err);
		var jqxhr =parent$.ajax({
	        url: url,
	        method: 'POST',
	        data: data,
	        crossDomain: true,
	        contentType: 'application/json',
	        beforeSend: function ( xhr ) {
	            xhr.setRequestHeader( 'Authorization', 'Basic username:password' );
	        },
		})
			.done (function(data) {eval(responceFunc)(data);})
			.fail   (function()  {eval(responceFunc)("Error");});
	}
}

function callPUTJSONAjax(url,data,responceFunc){
	try{
		var jqxhr =$.ajax({
		        url: url,
		        method: 'PUT',
		        data: data,
		        crossDomain: true,
		        contentType: 'application/json',
		        beforeSend: function ( xhr ) {
		            xhr.setRequestHeader( 'Authorization', 'Basic username:password' );
		        },
			})
				.done (function(data) {eval(responceFunc)(data);})
				.fail   (function()  {eval(responceFunc)("Error");});
	}
	catch(err){
		//alert(err);
		var jqxhr =parent$.ajax({
	        url: url,
	        method: 'PUT',
	        data: data,
	        crossDomain: true,
	        contentType: 'application/json',
	        beforeSend: function ( xhr ) {
	            xhr.setRequestHeader( 'Authorization', 'Basic username:password' );
	        },
		})
			.done (function(data) {eval(responceFunc)(data);})
			.fail   (function()  {eval(responceFunc)("Error");});
	}
}

function callDELETEJSONAjax(url,responceFunc){

	try{
		var jqxhr =
		$.ajax({
			url: url,
			processData : false,
			type : "DELETE"
		})
		.done (function(data) {eval(responceFunc)(data);})
		.fail   (function()  {eval(responceFunc)("Error");});
	}
	catch(err){
		var jqxhr =
		parent.$.ajax({
			url: url,
			processData : false,
			type : "DELETE"
		})
		.done (function(data) {eval(responceFunc)(data);  })
		.fail   (function()  {eval(responceFunc)("Error");});
	}
}