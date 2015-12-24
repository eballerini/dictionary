'use strict';

angular.module('dictionaryApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('word', {
                parent: 'entity',
                url: '/words',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Words'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/word/words.html',
                        controller: 'WordController'
                    }
                },
                resolve: {
                }
            })
            .state('word.detail', {
                parent: 'entity',
                url: '/word/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Word'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/word/word-detail.html',
                        controller: 'WordDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Word', function($stateParams, Word) {
                        return Word.get({id : $stateParams.id});
                    }]
                }
            })
            .state('word.new', {
                parent: 'word',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/word/word-dialog.html',
                        controller: 'WordDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    word: null,
                                    original_word: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('word', null, { reload: true });
                    }, function() {
                        $state.go('word');
                    })
                }]
            })
            .state('word.edit', {
                parent: 'word',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/word/word-dialog.html',
                        controller: 'WordDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Word', function(Word) {
                                return Word.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('word', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('word.delete', {
                parent: 'word',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/word/word-delete-dialog.html',
                        controller: 'WordDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Word', function(Word) {
                                return Word.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('word', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
