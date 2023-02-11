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

You can find generated java doc by path: 

```$xslt
target/apidocs/index.html
```

## How to use?

### List of features

* **_IoC container_** - container is a core component of the Bring Framework. It is responsible for managing the creation, configuration, and assembly of objects in an application.
  The IoC container acts as a central manager for the components in an application, enabling the developer to focus on writing the business logic rather than managing the object creation and assembly.
#### Ioc Pattern Example
    package com.demo
    @Component
    class MessageService{
          private String message;

          public void setMessage(String message){
              this.message = message
          }
          public String getMessage(){
              return this.message;
          }
    }
    @Component
    class PrinterService{
          
          private MessageService messageService;
          @Autowired
          public void setMessageService(MessageService messageService){
                this.messageService = messageService
          }
          public void print(){
              System.out.println(messageService.getMessage())
          }
    }
    var context = new AnnotationConfigApplicationContext("com.demo");
    var messageService = context.getBean(MessageService.class);
    var printerService = context.getBean(PrinterService.class);
    messageService.setMessage("MY_MESSAGE");
    printerService.print();  -> "MY_MESSAGE"
<p> In example above MessageService instance is set to the field of messageService of the PrinterService by Bring Framework. 
Developer does not need to create instance of MessageService in PrinterService himself all is managed by IoC container. Bring framework automatically detects 
Autowired annoation and injects instance</p>

* **_Annotations_**
  * @Component - annotation that used to register instance of class in Context. Default value is empty string. Alternatively, name of component can specified through the **_value_**
  * @Bean - annotatin that used to mark any dependecy that we need. Bring will register singletone instance in container. Custom name can specified with **_value_**
  * @Configuration - annotation that used to create a configuration class. Proxy will be created for each bean with the annotation @Bean via CGLIB library.
  * @Autowired - annotation that allows Bring IoC inject beans in class instance.

* **_Context Creation_** - Package name (delimited by dot com.example) should be provided to Bring. Bring will scan all classes marked with @Component or @Bean and will register beans in context.
#### Context Creation
    var context = new AnnotationConfigApplicationContext("com.example");
    var myService = context.getBean(MyService.class);
* Class configuration for dependecnies  - @Configuration is used to register bean dependencies. Bring Container will use CGLIB to create Proxy of the particular Bean Dependency 
##### Component Configuration and Bean

    @Configuration
    public class AppConfig {
    
        @Bean
        public RestTemplate() {
           return new RestTemplate()
        }
    }

* Several Injections are supported
  * **_Injection Via Constructor_** - our IoC container automatically detects bean dependency written in constructor and injects corresponding bean.
#### Injection Via Constructor
    package com.demo
    @Component
    class MessageService{
          private String message;
          public void setMessage(String message){
              this.message = message
          }
          public String getMessage(){
              return this.message;
          }
    }
    @Component
    class PrinterService{
          private MessageService messageService;
          public PrinterService(MessageService messageService){
              this.messageService = messageService
          }
          public void print(){
              System.out.println(messageService.getMessage())
          }
    }
  * **_Injection Via Field_** - our IoC container injects beans via annotation @Autowired
#### Injection Via Field
    package com.demo
    @Component
    class MessageService{
          private String message;
          public void setMessage(String message){
              this.message = message
          }
          public String getMessage(){
              return this.message;
          }
    }
    @Component
    class PrinterService{
          @Autowired
          private MessageService messageService;
          
          public void print(){
              System.out.println(messageService.getMessage())
          }
    }
  * **_Injection Via Setter_** - our IoC container injects beans via setter and annotation @Autowired
#### Injection Via Setter
    @Component
    class MessageService{
          private String message;

          public void setMessage(String message){
              this.message = message
          }
          public String getMessage(){
              return this.message;
          }
    }
    
    @Component
    class PrinterService{

          private MessageService messageService;
          @Autowired
          public void setMessageService(MessageService messageService){
                this.messageService = messageService
          }
          public void print(){
              System.out.println(messageService.getMessage())
          }
    }
  * **_Injection of Beans List_** - our IoC container allows to inject List of beans if they implement same interface. 
#### Injection of Beans List
    @Component
    class MessageService1 implements MessageInterface{
          private String message;

          public void setMessage(String message){
              this.message = message
          }
          public String getMessage(){
              return this.message;
          }
    }
    @Component
    class MessageService2 implements MessageInterface{
          private String message;

          public void setMessage(String message){
              this.message = message
          }
          public String getMessage(){
              return this.message;
          }
    }
    @Component
    class PrinterService{
          private List<MessageInterface> messageServices;

          @Autowired
          public void setMessageService(List<MessageInterface> messageServices){
                this.messageServices = messageServices
          }
    }
  * **_Circle Dependency Resolution_** - our IoC container allows to resolve circualr dependecies correctly. Here component 1 depends on component 2 and  component 2 depends on component 1 
#### Circle Dependency Resolution
    @Component
    public class CircularComponent2 {
          @Autowired
          private CircularComponent1 component1;
    }
    @Component
    public class CircularComponent1 {
        @Autowired
        private CircularComponent2 component2;
        public CircularComponent1(){
      
        }
    }


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
