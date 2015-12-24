'use strict';

angular.module('dictionaryApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('translation', {
                parent: 'entity',
                url: '/translations',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Translations'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/translation/translations.html',
                        controller: 'TranslationController'
                    }
                },
                resolve: {
                }
            })
            .state('translation.detail', {
                parent: 'entity',
                url: '/translation/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Translation'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/translation/translation-detail.html',
                        controller: 'TranslationDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Translation', function($stateParams, Translation) {
                        return Translation.get({id : $stateParams.id});
                    }]
                }
            })
            .state('translation.new', {
                parent: 'translation',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/translation/translation-dialog.html',
                        controller: 'TranslationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    usage: null,
                                    priority: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('translation', null, { reload: true });
                    }, function() {
                        $state.go('translation');
                    })
                }]
            })
            .state('translation.edit', {
                parent: 'translation',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/translation/translation-dialog.html',
                        controller: 'TranslationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Translation', function(Translation) {
                                return Translation.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('translation', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('translation.delete', {
                parent: 'translation',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/translation/translation-delete-dialog.html',
                        controller: 'TranslationDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Translation', function(Translation) {
                                return Translation.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('translation', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
