(function () {
    'use strict';
    angular.module('app', ['ui.router','ngMessages'/*, 'ngCookies', 'toastr', 'ngMaterial',*/])
    .run(function($rootScope){
        $rootScope
            .$on('$stateChangeStart', 
                function(event, toState, toParams, fromState, fromParams){ 
                    $("#ui-view").html("");
                    $(".page-loading").removeClass("hidden");
            });

        $rootScope
            .$on('$stateChangeSuccess',
                function(event, toState, toParams, fromState, fromParams){ 
                    $(".page-loading").addClass("hidden");
            });
      /*  $rootScope.$on('$stateChangeSuccess', 
        		function(event, toState, toParams, fromState, fromParams){ 
        		   alert("snalsnalk");
        		});*/
    }).config(routes);

    function routes($urlRouterProvider, $stateProvider) {
        console.log('route call');
        // $httpProvider.interceptors.push('httpInterceptor');
      //  $urlRouterProvider.otherwise('componentpage');newdashboard
       $urlRouterProvider.otherwise('newdashboard');
        var appState = {
            abstract: true,
            name: 'app',
            url: '',
            views: {
                '': { template: '<div ui-view ></div>' },
                'header': { templateUrl: 'angularjs/views/header.jsp' },
                'footer': { templateUrl: 'angularjs/views/footer.jsp' }
            },
            // data: {
            //     requireLogin: true
            // }
        }

        
        var addimage={
        		 name: 'app.addimage',
                 url: '/addimage',
                 templateUrl: 'angularjs/views/images/Addimages.jsp',
                 controller: 'ImageeeeController',
                 controllerAs: 'vm',
        }
        
        var componentpage={
       		 name: 'app.componentpage',
                url: '/componentpage',
                templateUrl: 'angularjs/views/component/Componetlist.jsp',
                controller: 'componentController',
                controllerAs: 'vm',
       }
        
        
        var devicesetup={
         		 name: 'app.devicesetup',
                  url: '/devicesetup',
                  templateUrl: 'angularjs/views/device/devicesetup.jsp',
                //  controller: 'componentController',
                  controllerAs: 'vm',
         }
        
        
        var parameterlist={
        		 name: 'app.parameterlist',
                 url: '/parameterlist',
                 templateUrl: 'angularjs/views/parameter/Parameterlist.jsp',
               //  controller: 'componentController',
                 controllerAs: 'vm',
        }
        
        var deviceprofilelist={
       		 name: 'app.deviceprofilelist',
             url: '/deviceprofilelist',
             templateUrl: 'angularjs/views/Deviceprofile/Deviceprofilelist.jsp',
           //  controller: 'componentController',
             controllerAs: 'vm',
    }
        
        var deviceinfo={
          		 name: 'app.deviceinfo',
                 url: '/deviceinfo',
                 templateUrl: 'angularjs/views/device/deviceinfo.jsp',
               //  controller: 'componentController',
                 controllerAs: 'vm',
        }
        
        var devicemaster={
         		 name: 'app.devicemaster',
                url: '/devicemaster',
                templateUrl: 'angularjs/views/Deviceprofile/Devicemaster.jsp',
              //  controller: 'componentController',
                controllerAs: 'vm',
       }
        
     var analogdevicesetting={
     		 name: 'app.analogdevicesetting',
             url: '/analogdevicesetting',
             templateUrl: 'angularjs/views/Analogsettings/AnalogDeviceSetting.jsp',
           //  controller: 'componentController',
             controllerAs: 'vm',
    }
        
        var addprofile={
        		 name: 'app.addprofile',
                url: '/addprofile',
                templateUrl: 'angularjs/views/Deviceprofile/AddDeviceProfile1.jsp',
              //  controller: 'componentController',
                controllerAs: 'vm',
       }

        var digiexample={
       		 name: 'app.digiexample',
             url: '/addprofile',
             templateUrl: 'angularjs/views/digiexample.jsp',
           //  controller: 'componentController',
             controllerAs: 'vm',
    }
        
        var imgesetupview={
          		 name: 'app.imgesetupview',
                url: '/imgeview',
                templateUrl: 'angularjs/views/guiview.jsp',
                controllerAs: 'vm',
       }
        
        
        var dashboardlist={
         		 name: 'app.dashboardlist',
               url: '/dashboardlist',
               templateUrl: 'angularjs/views/dashboard/dashboardlist.jsp',
               controller: 'DashboardController',
               controllerAs: 'vm',
               
      }
        
        var createdashboard={
        		 name: 'app.	',
              url: '/createdashboard',
              templateUrl: 'angularjs/views/dashboard/dashboardcreation.jsp',
            //  controller: 'DashboardController',
              controllerAs: 'vm',
              
     }
        
        var newdashboard={
        		 name: 'app.newdashboard',
              url: '/newdashboard',
              templateUrl: 'angularjs/views/dashboard/Dashboard.jsp',
          //    controller: 'DashboardController',
              controllerAs: 'vm',
              
     }
        
        var digital1={
       		 name: 'app.digitalData',
             url: '/digitalData',
             templateUrl: 'angularjs/views/dashboard/digitalData.jsp',
            // controller: 'DashboardController',
             controllerAs: 'vm',
    }
        
        var enegeryMeter={
          		 name: 'app.EnegeryMeter',
                url: '/EnegeryMeter',
                templateUrl: 'angularjs/views/dashboard/EnegeryMeter.jsp',
               // controller: 'DashboardController',
                controllerAs: 'vm',
       }
        
        
        $stateProvider.state(enegeryMeter);
        $stateProvider.state(digital1);
        $stateProvider.state(newdashboard);
        $stateProvider.state(appState);
        $stateProvider.state(addimage);
        $stateProvider.state(componentpage);
        $stateProvider.state(addprofile);
        $stateProvider.state(deviceprofilelist);
        $stateProvider.state(deviceinfo);
        $stateProvider.state(parameterlist);
        $stateProvider.state(analogdevicesetting);
        $stateProvider.state(digiexample);
        $stateProvider.state(imgesetupview);
        $stateProvider.state(dashboardlist);
        $stateProvider.state(createdashboard);
       // $stateProvider.state(analogboard);
       
     
       
    }

})();

