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
