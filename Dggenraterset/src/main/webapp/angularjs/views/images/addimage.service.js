(function () {
    'use strict';
    angular.module('app').factory('ImageeeeService', ImageeeeService)

    ImageeeeService.$inject = ['$q', '$http'];
    
    function ImageeeeService($q, $http) {
        return {
        	sendrequeut: sendrequeut
        	
        }
        
        
        function sendrequeut(url, requestData) {
        var deferred = $q.defer();
         $http({
			        method: 'POST',
			        url: url,
			        headers: {'Content-Type': undefined},
			        data: requestData,
			        transformRequest: function(data, headersGetterFunction) {
			                        return data;
			         }
			     })
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