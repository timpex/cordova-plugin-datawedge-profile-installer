var argscheck = require('cordova/argscheck'),
    exec      = require('cordova/exec');

function DataWedgeProfileInstaller () {};

DataWedgeProfileInstaller.prototype = {

    install: function (url, success, failure)
    {
        if(!url) throw new Error('url is required!');
        argscheck.checkArgs('*fF', 'DataWedgeProfileInstaller.install', arguments);

        var targetFileName = extractFileNameFromUrl(url);
        exec(success, failure, 'DataWedgeProfileInstaller', 'install', [{
            url: url,
            targetFileName: targetFileName
        }]);
    },

};

function extractFileNameFromUrl(url) {
    url = decodeURIComponent(url);
    var index = url.lastIndexOf('/dwprofile');
    return url.substring(index + 1);
}

module.exports = new DataWedgeProfileInstaller;
