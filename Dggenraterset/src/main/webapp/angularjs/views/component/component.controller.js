(function () {
    'use strict';	
angular.module('app').controller('componentController',componentController)

	componentController.$inject = ['$scope', '$http', 'componentService'];
 	
	function componentController($scope, $http,componentService) 
					{
        var vm = this;
        vm.addcomponetData=addcomponetData;
        
        function addcomponetData(isValid, validateData)
        {
            if (isValid) {
        	var requestData =
            {
                "componet":validateData.componet,
                "unit": validateData.unit,
                "image": validateData.image
            }
        	console.log(requestData);
        	componentService.sendrequeut("admin/componet",requestData).then(function (response) {
					 // console.log(response);
					if(response.data.status)
						{
		                        swal({
		                            title: response.data['message'],
		                            text: "Successfully Added !",
		                            type: "success"
		                        });
		                  //  alert("Device Added successfully");
		                }else
		               	 {
		               	 swal({
		                        title: "Get Error",
		                        text: "Get Error",
		                        type: "error"
		                    });
		               	}
                }, function (error) {
                    console.log(error);
                });
            }
				
        }
        
      
					}

})();