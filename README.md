# XESLogCreator
This repository contains the source code of the XESLogCreator. The XESLogCreator is a Java application that retrieves event data from a MySQL database, filters the events, formats the data according to XES standard and creates a **.xes** event log file.

The instructions below are for setting up the complementary components and running the XESLogCreator.

## Requirements

* JAVA SE 8 [JDK](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html) version 1.8
* [Apache Ant](https://ant.apache.org/bindownload.cgi) version 1.9.1 or newer
* [Apache Tomcat](https://dlcdn.apache.org/tomcat/) version 7 or newer
* [Apache ODE War](http://archive.apache.org/dist/ode/) version 1.3.6
* [MySQL](https://dev.mysql.com/downloads/installer/) version 8
* [SoapUI](https://www.soapui.org/tools/soapui/) version 5.6 or newer

Note: The application was tested only with the aforementioned versions and MacOS Big Sur 11.4.

## Configuration

1. Install the tools mentioned above and configure them according to their documentation.
2. Copy the Apache ODE War to Tomcat's webapps directory and start Tomcat. Apache ODE can be accessed using http://localhost:8080/ode, but the port number can be changed if wanted (I used port 8082).
3. After the first start of Tomcat, a folder called **ode** was created inside the webapps directory. Copy the **"ATMWithdrawalService"** folder from this repository to the **"ode/WEB-INF/processes"** directory. If you wish, you can use another process or make your own using a tool like [Eclipse BPEL Designer](https://www.eclipse.org/bpel/). Beware that the ATMWithdrawalService runs on port 8082. The port number can be changed by editing the following line found at the end of the **"ATMWithdrawalServiceArtifacts.wsdl"** file: `<soap:address location="http://localhost:8082/ode/processes/ATMWithdrawalProcess" />`
4. Set up Apache ODE with a MySQL database using these [instructions](https://ode.ape.org/war-deployment.html).
5. Deploy the ATMWithdrawalService process (or the process you are using) by simply starting Tomcat and leave it running.
6. Feed input data to the process using the [sendsoap] command or by using SoapUI (**recommended**). In order to do so, navigate to http://localhost:8080/ode/deployment/bundles/, select your process and click on the **.wsdl** file. Copy the file's link (e.g. http://localhost:8082/ode/deployment/bundles/ATMWithdrawalService/ATMWithdrawalProcessArtifacts.wsdl) and start a new SOAP project with it in SoapUI, then send requests to the process using your own desired input.

## Running

Make sure to have MySQL running and open a new Command Line(Windows) or a Terminal(Unix). Navigate to the **"XESLogCreator"** directory using `cd` and run the app jar by using the following command.
```bash
java -jar XESLogCreator.jar
```
The new XES event log can be found in the **"GeneratedEventLogs"** directory.

## Source code documentation

The application includes a JavaDoc documentation. In order to access it, navigate to the **"XESLogCreator/doc"** directory and simply open the **"index.html"** file.

## Extra functionality

Be aware that the current source code only works with the ATMWithdrawalService process due to the added extra functionality. In order to run the application with any process, simply look for the **"Extra functionality""** comments in the java files and comment/delete lines according to the instruction mentioned there. For example: 
```java
/**
 * <b>Extra functionality</b>
 * Comment out the next two lines.
*/
messageDataSet = getMessageDataSet(getEventMessageExchangeId());
readProcessInput(messageDataSet);
```
Means commenting/deleting the `messageDataSet = getMessageDataSet(getEventMessageExchangeId());` and the `readProcessInput(messageDataSet);` lines.

And
```java
/**
 * <b>Extra functionality</b>
 * <i>Specific to the ATM Withdrawal process!</i>
 * Does not require commenting for the application to run without this functionality.
*/
```
Means that the application will still run even if the code following the comment is not commented/deleted.

## Support

For questions you may contact me at 📧 : emanuelnae@live.com
