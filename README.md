# Bring Description
Bring Framework is an open-source Java platform that provides infrastructure support for developing Java applications. 
It offers a some tools for creating robust, scalable, and secure applications. 
Some of its key features include dependency injection and inversion of control. For example, it supports field injection, 
list of objects injection.<br>

Dependency Injection (DI) is a software design pattern that allows the separation of concerns in an application by 
injecting dependent objects into a class, instead of having the class create or instantiate those objects itself. 
This approach promotes loose coupling between objects and results in more maintainable and testable code. 
With DI, objects can be easily swapped or modified without affecting the rest of the application, 
making it easier to change or update the implementation of a particular feature. 
The Bpring Framework provides support for DI through its inversion of control (IoC) container, 
which manages the creation and configuration of objects in the application. <br>

In Bring, proxy objects are used to provide additional behavior to existing objects, 
such as adding transaction management or security checks, without having to modify the original objects themselves.
The proxy objects are created by the Bring IoC container and are transparently inserted into the application at runtime. 
This allows the framework to add behavior to objects in a modular and non-invasive manner. <br>

The proxy pattern is a design pattern used in computer programming where a proxy object acts as a representative or 
surrogate for another object in order to control access to it. The Bring Framework makes use of proxy objects as 
part of its AOP (Aspect-Oriented Programming) functionality.

In Bring classes are proxied via CGLIB library. 
This type of proxy allows the Bring framework to add behavior to objects in a dynamic, 
flexible, and non-intrusive manner at runtime.<br>



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

## How to use?

### List of features
* IoC container
* set up of beans
* injection 
  * via constructor
  * via field
  * via setter
  * inject list of beans 
* resolve circle dependency 
* inter bean dependency 
### Code examples

#### Create context;
##### Description of methods.

#### Configure beans
##### Component Configuration and Bean

#### Injections
##### Via constructor 
##### Via field
##### Via setter


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

## How to add to project?

You can add Bring Framework to your project in several ways:

* Download jar
* Add Maven dependency

### JAR File
##### Download jar

* Go to Packages of Bring Framework project: https://github.com/Mikhail-Mikhailenko/bring/packages/1787566

* Download JAR File from Assets in the Latest version.

##### Install jar

- You can include that jar in your classpath of application
- Either you can install that jar file in your maven repos using command:
```agsl
mvn install:install-file -Dfile=<path-to-file> -DgroupId=<group-id> \
-DartifactId=<artifact-id> -Dversion=<version> -Dpackaging=<packaging>
```

### Maven Dependency

* Add authentication to Github Packages  by editing your `~/.m2/settings.xml` using following instructions (your repository url will be `https://maven.pkg.github.com/Mikhail-Mikhailenko/bring`):
  
    - https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry#authenticating-to-github-packages

* Go to Packages of Bring Framework project: https://github.com/Mikhail-Mikhailenko/bring/packages/1787566

* Copy dependency of the Latest version.

* Add dependency to `pom.xml` of your Project.