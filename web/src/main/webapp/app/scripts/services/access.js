'use strict';

/**
 * @ngdoc service
 * @name oncokb.access
 * @description
 * # access
 * Service in the oncokb.
 */
angular.module('oncokb')
  .service('access', function access($rootScope, storage, config, users) {
    var self = this;
    var userRoles = config.userRoles;
    var accessLevel = config.accessLevel;
    var loginCallback = '';

    self.url = '';

    function getUserInfo(callback) {
        storage.requireAuth(true).then(function(result){
            gapi.client.load('plus','v1', function(){
                gapi.client.plus.people.get({
                    'userId' : 'me'
                }).execute(user);
            });
        });
    }

    function user(userInfo) {
        var user = {};
        if(userInfo.emails) {
            for (var i = 0; i < userInfo.emails.length; i++) {
                if(userInfo.emails[i].type === 'account'){
                    user.email = userInfo.emails[i].value;
                    break;
                }
            }
        }

        users.setMe(user);
        user = users.getMe();

        if(userInfo.image && userInfo.image.url) {
            user.avatar = userInfo.image.url;
        }

        //if user already login, but in the user spreadsheet, his/her role is empty or is not 2, then set to 2
        if(!(user.role && user.role > 1)) {
            user.role = uuserRoles.user;
        }

        $rootScope.user = user;
        loginCallback();
    }

    function setRedirectURL(url) {

    }

    return {
        authorize: function(accessLevel, role) {
            if(role === undefined)
                role = $rootScope.user.role;
            return accessLevel & role;
        },

        isLoggedIn: function(user) {
            if(user === undefined)
                user = $rootScope.user;
            return user.role === userRoles.user || user.role === userRoles.curator || user.role === userRoles.admin;
        },

        login: function(callback) {
            loginCallback = callback;
            getUserInfo();
        },

        logout: function(success, error) {
            $rootScope.user = {
                role: userRoles.public
            };
            users.resetMe();
        },

        setURL: function(url) {
            self.url = url;
        },

        getURL: function() {
            return self.url;
        }
    };
  });
