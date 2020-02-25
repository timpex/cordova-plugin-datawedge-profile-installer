var argscheck = require('cordova/argscheck'),
    exec      = require('cordova/exec');

function DataWedgeProfileInstaller () {};

DataWedgeProfileInstaller.prototype = {

    install: function (params, success, failure)
    {
        argscheck.checkArgs('*fF', 'DataWedgeProfileInstaller.install', arguments);
        params = params || {};
        exec(success, failure, 'DataWedgeProfileInstaller', 'install', [params]);
    },

};

module.exports = new DataWedgeProfileInstaller;
