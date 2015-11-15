Fuga Framework
=======
Small Java Web Framework for big projects.

[![Build Status](https://travis-ci.org/IntCode/Fuga-Framework.svg?branch=master)](https://travis-ci.org/IntCode/Fuga-Framework)
[![Dependency Status](https://www.versioneye.com/user/projects/55b8dac9653762001700136e/badge.svg?style=flat)](https://www.versioneye.com/user/projects/55b8dac9653762001700136e)

[Roadmap](https://github.com/IntCode/Fuga-Framework/wiki/Roadmap)
## Installation

First of all you need to download and import Fuga Framework in your IDE, create new maven project and add dependence in `pom.xml`:

```
<dependency>
  <groupId>${project.groupId}</groupId>
  <artifactId>fugaframework</artifactId>
  <version>${project.version}</version>
</dependency>
```

## Using

Every web application must contain at least main class, one controller and routes map.

Ccreate simple cotroller that returns `Hello world!` for every client:

```
package com.example.controllers;

import com.showvars.fugaframework.foundation.Context;
import com.showvars.fugaframework.foundation.Controller;
import com.showvars.fugaframework.foundation.Response;

public class HelloWorldController extends Controller {

    public static Response index(Context ctx) throws Exception {
        return ok("Hello world!");
    }

}
```

Now add this controller to routes map. Create file `helloworld.routesmap` in `resources/routes/` and add this:

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
		getRouter().loadFromResources("/routes/helloworld.routesmap");
	}
	
	public static void main(String[] args) throws Exception {
		new HelloWorldApp().start();
	}

}
```

Compile and run. After that open link `http://localhost:8080/` in your browser and there you can see Hello world!.
