      <div class="row" ng-app="app" ng-controller="ImageeeeController">
        <div class="col-xs-12">
	<div class="box">
            <div class="box-header">
              <h3 class="box-title">Add images </h3>
            </div> 
    <div class="box-body" >
   <form id="fromFileUpload" enctype="multipart/form-data"  method="post" ng-submit="vm.continueFileUpload()" >
      <div id="displaystatus"></div>  
                     <div class="row">
                     <div class="col-md-4">
                      <label class="control-label" >Image:</label>
                      <input type="file" name="file" ng-model="file" data-rule-required="true" id="file">
                     </div>
                     <div class="col-md-3">
                      <label class="control-label" >ImageName:</label>
                      <!-- <input type="text" name="file" ng-model="file" data-rule-required="true" id="file"> -->
                      <input type="text" name="imgname" class="form-control" placeholder="Enter Image Name" data-rule-required="true" ng-model="imgname" id="imgname" >
                     </div>
                     </div>
					<div align="center" style="margin-top: 2%;">
                    <input type="submit" class="btn btn-primary" />     
                        <button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
                    </div>
                    	</form>
           </div>
            <!-- /.box-body -->
          </div>
          <!-- /.box -->
        </div>
        <!-- /.col -->
      </div>