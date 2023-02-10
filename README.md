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

#### Create context

    var context = new AnnotationConfigApplicationContext("com.example");
    var myService = context.getBean(MyService.class);

##### Description of methods.

#### Configure beans

    @Component
    public class RandomService {
    }

##### Component Configuration and Bean

    @Configuration
    public class AppConfig {
    
        @Bean
        public MyService() {
           MyService myService = new MyService();
           myService.setDatabaseHandler(RepositoryHandler.getSpecificDb("h2"));
        }
    }

#### Injections

##### Via constructor (the annotation is not required here)

    @Autowired
    public MyService(RandomService randomService) {
    this.randomService = randomService;
    }

##### Via field

    @Autowired
    private RandomService randomService;

##### Via setter

    @Autowired
    public void setRandomService(RandomService randomService) {
      this.randomService = randomService;
    }

##### Via list of objects

    @Autowired
    private List<DataService> randomService;

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