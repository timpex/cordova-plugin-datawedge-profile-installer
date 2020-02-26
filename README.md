# cordova-plugin-datawedge-profile-installer
This plugin installs and imports a datawedge-profile or datawedge-configuration.

# Installation
``` sh
cordova plugin add https://github.com/timpex/cordova-plugin-datawedge-profile-installer
```

# API

## Install profile

``` js
timpex.dataWedgeProfileInstaller.install(url, onSuccess, onFailure);
```

Arguments:
	
* url: Url to where your profile exists.
``` js
	//For datawedge-profile
	'http://example.com/dwprofile_<profile name>.db' 
	
	//For datawedge configuration-file
	'http://example.com/datawedge.db'
```
* onSuccess: function () {...} *Callback for successful install.*
* onFailure: function (e) {...} *Callback for error*

# Hints/Tips
After exporting your datawedge-profile **DO NOT RENAME IT** this will make it unable to install.

