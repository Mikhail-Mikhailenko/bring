# Bring Framework

Bring Framework is an inversion of control and dependency injection framework.<br>
It allows you to declare what objects you need, and then it processes configuration, creates required objects, sets dependencies and brings objects that are ready to use.


| Requirements                   	 | Reason                         	     |
|----------------------------------|--------------------------------------|
| Java 17 LTS           	          | Application                        	 |    |
| Maven version 3.6.3+ 	           | Builder 	                          |

## Technology stack
| Technology name                   	 | Version                        	 |
|-------------------------------------|----------------------------------|
| JDK        	                         | 17 LTS                           |


###### Maven build
To build project using maven tool, please do the follow commands:
```$xslt
mvn clean install
```
###### Maven tests running
To run tests using maven tool, please do the follow commands:

```$xslt
mvn clean test
```

###### To get javadoc

To get javadoc, please do the follow commands:

```$xslt
mvn -X clean javadoc:aggregate-jar
```

#### How to use?

* The whole idea of a dependency injection is built on annotations.
  If you want to give up control of your class to a bring project, you need to add the @Component annotation on the java
  class.
  It will be enough to create an instance of such a class.

```
  @Component
  public class RandomService {
  }
```  

* Most of the classes have dependencies on other java classes. You can handle that as well with @Autowired annotation.
  This annotation can be used on a field, a constructor, or a method.

  - Field injection

    ```agsl
	    @Autowired
	    private RandomService randomService;
    ```
  - Constructor injection (the annotation is not required here)
    ```agsl
     @Autowired
     public MyService(RandomService randomService) {
	      this.randomService = randomService;
	    }
  - Setter injection
    ```agsl
	   @Autowired
	   public void setRandomService(RandomService randomService) {
		    this.randomService = randomService;
	   }
    ```

* If you need to add logic when creating an instance of a class or if you don't have direct access to a class, you can
  use
  a combination of @Configuration and @Bean annotations.
  You can declare a config class with any name you like and put a @Configuration annotation on it. Next, create desired
  methods, which would be your custom logic of creating an instance of a class. Put a @Bean annotation on such a method.

```agsl
@Configuration
public class AppConfig {

    @Bean
    public MyService() {
	   MyService myService = new MyService();
	   myService.setDatabaseHandler(RepositoryHandler.getSpecificDb("h2"));
    }
}

```

* Thanks to all these configurations, Bring would be able to create instances of classes, and you will have access to
  them
  through AnnotationConfigApplicationContext

```agsl
var context = new AnnotationConfigApplicationContext("com.example");
var myService = context.getBean(MyService.class);
```

Another great feature is that Bring can inject beans for classes in collections. For example, if you have such a
field  `List<DataService>` then the Bring will create instances of all classes that implement that interface and put it
to the map. You can inject through a field, constructor, and setter.
```agsl
@Autowired
private List<DataService> randomService;
```

