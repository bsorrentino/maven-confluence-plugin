
```
 __   __        ___            ___       __   ___     __    ___  ___ 
/  ` /  \ |\ | |__  |    |  | |__  |\ | /  ` |__     /__` |  |  |__  
\__, \__/ | \| |    |___ \__/ |___ | \| \__, |___    .__/ |  |  |___ 
```

A CLI (*Command Line Interface*) to manage a Confluence's Site. 

This project should be considered as **[NodeJS](https://nodejs.org/)** version of the [confluence maven plugin](https://github.com/bsorrentino/maven-confluence-plugin) developed using [reactive javascript extension](https://github.com/Reactive-Extensions/RxJS)

The Site is described using a [Site descriptor](http://bsorrentino.github.io/maven-confluence-plugin/site_guide.html) that is compatible with the one used by [confluence maven plugin](https://github.com/bsorrentino/maven-confluence-plugin)


> Currently only **xmlrpc** protocol is supported (*REST coming soon*)

## Intall 

```
npm install confluence-site -g
```

## Usage

```
Usage: confluence-site 

init --serverid <serverid>	// create/update configuration

deploy [--config]		// deploy site to confluence

delete				// delete site

info				// show configuration

Options:

 --serverid 	// it is the credentials' profile.
 --config	// force reconfiguration
```

## init 

Initilaize (create/update) the configuration. The configuration is stored into file `./config.json`

 key | description |
---- | ---- |
serverId | It is the credentials' profile. Provided from command line option `--serverid`  |
protocol | `http|https`. This information is deducted from url|
host | host name or ip address. This information is deducted from url|
port | port number. This information is deducted from url|
path | url path. This information is deducted from url|
spaceId | Confluence target *space identifier* |
parentPageTitle | Confluence container page|
sitePath | Path where the *site descriptor* is located. By default is `./site.xml`|

> Credentials are stored into a separate crypted file (see [preferences](https://www.npmjs.com/package/preferences)) indentified by **serverId** 

## deploy

Deploy pages defined into **site descriptor** directly in confluence 

## delete

Delete pages tree startig from *home* defined into **site descriptor**

## info

Show current configuration

> Example
> ```
> site path:		site.xml
> confluence url:		http://localhost:8080/
> confluence space id:	MySpace
> confluence parent page:	Home
> serverid:		test
> confluence username:	admin
> confluence password:	*****
> ```
