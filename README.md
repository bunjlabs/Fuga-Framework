Fuga Framework
=======
Small Java Web Framework for big projects.

[![Build Status](https://travis-ci.org/bunjlabs/Fuga-Framework.svg?branch=master)](https://travis-ci.org/bunjlabs/Fuga-Framework)
[![Dependency Status](https://www.versioneye.com/user/projects/55b8dac9653762001700136e/badge.svg?style=flat)](https://www.versioneye.com/user/projects/55b8dac9653762001700136e)

[Roadmap](https://github.com/IntCode/Fuga-Framework/wiki/Roadmap)
## Installation

Whe use Maven as build system. Because whe still in active developming whe don't have stable or beta versions. But you can manualy download and install snapshot releases GitHub. Anyway you can also feel free to use our maven snapshot repository:
```
<dependency>
  <groupId>com.bunjlabs</groupId>
  <artifactId>fuga</artifactId>
  <version>0.3.0-SNAPSHOT</version>
</dependency>
<repositories>
  <repository>
    <id>bunjlabs.repo</id>
    <name>Bunjlabs Maven Snapshots Repository</name>
    <url>http://maven.bunjlabs.com/snapshots</url>
  </repository>
</repositories>
```

## Using

Every web application must contain at least main class, one controller and routes map.

Ccreate simple cotroller that returns `Hello world!` for every client:

```
package com.example.controllers;

import com.bunjlabs.fugaframework.foundation.Context;
import com.bunjlabs.fugaframework.foundation.Controller;
import com.bunjlabs.fugaframework.foundation.Response;

public class HelloWorldController extends Controller {

    public Response index() {
        return ok("Hello world!");
    }

}
```

Now add this controller to routes map. Create file `default.routes` in `routes/` directory with this content:

```
use com.example.controllers

GET $/ HelloWorldController.index()
```


Create main class:

```
package com.example;

import com.showvars.fugaframework.FugaApp;

public class HelloWorldApp extends FugaApp{

	@Override
	public void prepare() {
		getRouter().load("routes/default.routes");
	}
	
	public static void main(String[] args) throws Exception {
		new HelloWorldApp().start();
	}

}
```

Compile and run. After that open link `http://localhost:8080/` in your browser and there you can see Hello world!.
