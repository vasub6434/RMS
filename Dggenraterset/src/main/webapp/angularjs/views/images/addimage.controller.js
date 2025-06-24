(function () {
    'use strict';	
angular.module('app').controller('ImageeeeController',ImageeeeController)

	ImageeeeController.$inject = ['$scope', '$http', 'ImageeeeService'];
 	
	function ImageeeeController($scope, $http,ImageeeeService) 
					{
        var vm = this;
        vm.continueFileUpload=continueFileUpload;
        
        function continueFileUpload()
        {
				var formData=new FormData();
				formData.append("file",file.files[0]);
				//formData.append("id",file.files[0]);
				formData.append("imgname",$("#imgname").val());
				var url='admin/adddimage';
				ImageeeeService.sendrequeut(url,formData).then(function (response) {
					  console.log(response);
					if(response.data.status)
						{
						 alert(response.data.message);
						}
                }, function (error) {
                    console.log(error);
                });
				
				}
        
    
					}

})();