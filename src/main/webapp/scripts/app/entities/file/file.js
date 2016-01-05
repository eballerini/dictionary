'use strict';

angular.module('dictionaryApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('file', {
                parent: 'entity',
                url: '/files',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Files'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/file/files.html',
                        controller: 'FileController'
                    }
                },
                resolve: {
                }
            })
            .state('file.detail', {
                parent: 'entity',
                url: '/file/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'File'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/file/file-detail.html',
                        controller: 'FileDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'File', function($stateParams, File) {
                        return File.get({id : $stateParams.id});
                    }]
                }
            })
            .state('file.new', {
                parent: 'file',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/file/file-dialog.html',
                        controller: 'FileDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    date: null,
                                    status: null,
                                    comments: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('file', null, { reload: true });
                    }, function() {
                        $state.go('file');
                    })
                }]
            })
            .state('file.edit', {
                parent: 'file',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/file/file-dialog.html',
                        controller: 'FileDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['File', function(File) {
                                return File.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('file', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('file.delete', {
                parent: 'file',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/file/file-delete-dialog.html',
                        controller: 'FileDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['File', function(File) {
                                return File.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('file', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
