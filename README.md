Sweetie
=======

Маленький Java MVC веб-фреймворк для больших задач.


## Установка

Для начала использования фреймворка Sweetie необходимо загрузить и импортировать основной проект в вашу IDE и в кофигурационном файле `pom.xml` добавить следующую зависимость maven:
```
<dependency>
  <groupId>${project.groupId}</groupId>
  <artifactId>sweetie</artifactId>
  <version>${project.version}</version>
</dependency>
```

## Использование

Каждое приложение как минимум должно содержать в себе главный класс запускающий приложение, контроллер и файл маршрутов.

Создадим простейший контроллер, возвращающий каждому клиенту строку "Привет мир!":
~~~
package com.example.controllers;

import com.showvars.sweetie.foundation.Context;
import com.showvars.sweetie.foundation.Controller;
import com.showvars.sweetie.foundation.Response;

public class HelloWorldController extends Controller {

    public static Response index(Context ctx) throws Exception {
        return ok("Привет мир!");
    }

}
~~~

И забиндим этот контроллер на любой запрос от пользователя в списке маршрутов. Создадим файл с `helloworld.routesmap` в папке `resources/routes/` со следующим содержимым:
```
GET     /   com.example.controllers.HelloWorldController.index()
```

Теперь запустим наше приложение. Для этого создадим главный класс примерно с таким содержимым:
```
package com.example;

import com.showvars.sweetie.SweetieApp;
import java.io.InputStreamReader;

public class HelloWorldApp {

    public static void main(String[] args) throws Exception {
        SweetieApp ss = SweetieApp.prepare();
        ss.getRouter().load(new InputStreamReader(HelloWorldApp.class.getResourceAsStream("/routes/helloworld.routesmap")));
        ss.getStarted();
    }

}
```

Компилируем и запускаем. Теперь, если ввести в браузере адрес `http://localhost:8080/`, то на экран выведится наше "Привет мир!".
