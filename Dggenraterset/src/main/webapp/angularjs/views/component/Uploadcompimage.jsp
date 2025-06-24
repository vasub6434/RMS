<script src="plugins/jQuery/jQuery-2.1.4.min.js"></script>
<script src="js/angular.min.js" ></script> 
<script type="text/javascript">
 
 angular.module('postApp', []).controller('Controller', function($scope, $http) 
			{
	  
	$scope.continueFileUpload=function (){
		var formData=new FormData();
//alert("alert"+$("#cid").val())
		formData.append("id",$("#cid").val());
		formData.append("file",file.files[0]);
		 $http({
		        method: 'POST',
		        url: 'admin/uploadcompimage',
		        headers: {'Content-Type': undefined},
		        data: formData,
		        transformRequest: function(data, headersGetterFunction) {
		                        return data;
		         }
		     })
		    .success(function(data, status) {   
		                    alert("successfully inserted");
		                    
		     })
		};
			});

    
    
     </script>
         <div  ng-app="postApp" ng-controller="Controller">
   <form id="fromFileUpload" enctype="multipart/form-data"  method="post" >
      <div id="displaystatus"></div>  
                     <div class="row"><div class="col-lg-12">
                    <label class="control-label" >Image:</label>
                    <input type="file" name="file" ng-model="file" data-rule-required="true" id="file">
                    </div></div>
					<div align="center" style="margin-top: 2%;">
                    <input type="submit" class="btn btn-primary" ng-click="continueFileUpload()"/>     
                        <button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
                    </div>
                    	</form>
           </div>