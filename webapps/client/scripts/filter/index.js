'use strict';



/**
 * @module  cam.tasklist.filter
 * @belongsto cam.tasklist
 *
 * Filters are predefined filters for tasks.
 */



define([
  'require',
  'angular',
  'moment',
  './directives/cam-tasklist-filters',
  './directives/cam-tasklist-tasks',
  './modals/cam-tasklist-filter-form',
  'camunda-tasklist-ui/utils',
  'camunda-tasklist-ui/api'
], function(
  require,
  angular,
  moment,
  camTasklistFilters,
  camTasklistFilterTasks,
  camTasklistFilterForm,
  utils,
  api
) {

  var filterModule = angular.module('cam.tasklist.filter', [
    utils.name,
    api.name,
    'ui.bootstrap',
    'cam.form',
    'angularMoment'
  ]);


  filterModule.factory('camTasklistFilterCriteriaConversion', [
    '$rootScope',
  function(
    $rootScope
  ) {
    var tokenExp = /(\{[^\}]+\})/g;


    function tokenReplace(val) {
      var user = $rootScope.authentication;
      if (val === '{self}') {
        return user ? user.name : 'anonymous';
      }

      if (val === '{now}') {
        return Math.round((new Date()).getTime() / 1000);
      }

      if (val === '{day}') {
        return 60 * 60 * 24;
      }

      if (!tokenExp.test(val)) {
        return val;
      }

      var pieces = [];

      angular.forEach(val.split(tokenExp), function(piece) {
        pieces.push(tokenReplace(piece));
      });

      return pieces.join('');
    }

    return tokenReplace;
  }]);




  filterModule.controller('filterCreateModalCtrl', [
    '$modalInstance',
    '$scope',
  function(
    $modalInstance,
    $scope
  ) {
    $scope.createFilter = function() {
      console.info('createFilter', arguments);
    };

    $scope.addFilter = function() {
      console.info('addFilter', arguments, $scope);
    };

    $scope.abort = $modalInstance.dismiss;
  }]);


  filterModule.controller('filterFormCtrl', camTasklistFilterForm);

  filterModule.directive('camTasklistFilters', camTasklistFilters);

  filterModule.directive('camTasklistFilterTasks', camTasklistFilterTasks);

  return filterModule;
});
