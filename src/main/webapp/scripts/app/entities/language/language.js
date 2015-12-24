'use strict';

angular.module('dictionaryApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('language', {
                parent: 'entity',
                url: '/languages',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Languages'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/language/languages.html',
                        controller: 'LanguageController'
                    }
                },
                resolve: {
                }
            })
            .state('language.detail', {
                parent: 'entity',
                url: '/language/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Language'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/language/language-detail.html',
                        controller: 'LanguageDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Language', function($stateParams, Language) {
                        return Language.get({id : $stateParams.id});
                    }]
                }
            })
            .state('language.new', {
                parent: 'language',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/language/language-dialog.html',
                        controller: 'LanguageDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    language: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('language', null, { reload: true });
                    }, function() {
                        $state.go('language');
                    })
                }]
            })
            .state('language.edit', {
                parent: 'language',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/language/language-dialog.html',
                        controller: 'LanguageDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Language', function(Language) {
                                return Language.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('language', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('language.delete', {
                parent: 'language',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/language/language-delete-dialog.html',
                        controller: 'LanguageDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Language', function(Language) {
                                return Language.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('language', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
