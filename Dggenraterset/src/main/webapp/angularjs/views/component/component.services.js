(function () {
    'use strict';
    angular.module('app').factory('componentService', componentService)

    componentService.$inject = ['$q', '$http'];
    
    function componentService($q, $http) {
        return {
        	sendrequeut: sendrequeut
        	
        }
        
        
        function sendrequeut(url, reqData) {
            var deferred = $q.defer();
            $http.post(url, reqData)
                .then(function (response) {
                    deferred.resolve(response);
                },
                function (errResponse) {
                    console.error('Error while post request');
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        }
        
    }
    
    })();