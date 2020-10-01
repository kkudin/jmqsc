# JMSQC

jmqsc is library and command-line application for execution mq commands on IBM MQ Manager. It does not require installed MQ Client.

## Building
Building

## Usage as command line application

Options to use with command line application
```
-c,--channel <arg>    Channel. Default value is DEV.ADMIN.SVRCONN
-h,--host <arg>       MQ manager host. Default value is 127.0.0.1
   --help             Print this help
-i,--input <arg>      Source of commands. Default is standard input
-l,--login <arg>      User login
-p,--port <arg>       Port. Default value id 1414)
-s,--password <arg>   User password
```

Application supports scripts with a format described in https://www.ibm.com/support/knowledgecenter/SSFKSJ_9.0.0/com.ibm.mq.ref.adm.doc/q085110_.htm  

## Usage as library

Use library as dependency in your project
##### In maven projects

```xml
<dependency>
  <groupId>com.omvoid</groupId>
  <artifactId>jmqsc</artifactId>
  <version>1.0.0</version>
</dependency>
```

##### In gradle projects
```groovy
  compile('com.omvoid:jmqsc:1.0.0')
```
#
Example usage
```java
import com.omvoid.jmqsc.CommandExecutor;
import com.omvoid.jmqsc.CommandResult;
import com.ibm.mq.MQQueueManager;

public class Example() {
    public static void main(String[] args) throws Exception {
        MQQueueManager manager = new MQQueueManager("QM1"); //you need instance of MQManager
        CommandExecutor executor = new CommandExecutor(manager); //to create CommandExecutor
        CommandResult result = executor.executeCommand("DISPLAY QUEUE(*)");
        System.out.println(result.isSuccess() ? "Success" : "Failed");
        System.out.println(result.getResponseText());
    }
}
```

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[MIT](https://choosealicense.com/licenses/mit/)

