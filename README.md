# CoffeeCom - Direct Communication

**ABOUT:**

The purpose of this application is to provide a tool for straight forward and easy communication - locally or globally. Create your own communication-lobby and access it from wherever you may be.

This application only works inside a terminal, no GUI (application-window) is provided.

**This project is still in it's ALPHA-phase.**

**IMAGES:**

![Server-side](server_console.png "A peek in the console of the server")

![Client-side](client_console.png "A peek in the console of the client")

**REQUIRED:**

CoffeeCom requires `Java` to run. At the moment you also need to compile the sources yourself, so you need the [Java JDK](https://www.oracle.com/java/technologies/jdk8-downloads.html) (development kit).

**INSTALLATION:**

**NOTE:** The installation is quite messy at this point, will get improved.

1.  Compile the sources
    1. Navigate to the 'main' folder (@ coffeecom/src/main/)
    2. From the command-line, compile all sources using `javac -cp resources/jansi-1.18.jar:. java/solarplus/coffeecom/[package]/*.java (do this for all packages)
2.  Create a runnable jar
    1. Navigate to the 'java' folder (@ coffeecom/src/main/java/)
    2. From the command-line, create a jar using `jar cf [FILENAME].jar solarplus/coffeecom/serverside/*.class solarplus/coffeecom/clientside/*class solarplus/coffeecom/formatting/*.class`
3.  Run the jar
    1. Navigate to the 'main' folder (@ coffeecom/src/main/)
    2. From the command-line, run the jar using `java -cp java/[FILENAME].jar:resources/jansi-1.18.jar:. solarplus.coffeecom.clientside.Client` (substitute 'clientside.Client' with 'serverside.Server' if opting for a server-jar
4.  [OPTIONAL] If desired, port forwarding is a method for accessing server from anywhere. Read below if this applies to your server.

**ACCESS FROM ANYWHERE (PORT FORWARDING):**

1.  Create and assign a static IP to the machine running the CoffeeCom-server. (`Server.class`)
2.  Open the desired port from within the router (check if there is any regulation/restriction/standard existing for that port)
3.  Access the Coffeecom-server from anywhere using the static ip and port forwarded

**DISCLAIMER:**

*  I am not responsible for any misuse or violation you may do or act through/using this software.

*  I am not responsible for any loss of data, damage or value to you or anyone else.

*  This is an application solely intended for communication between individuals - use it with logic, common sense and good intentions.

USE THIS SOFTWARE AT YOUR OWN RISK.

**THANKS TO:** ❤

* [Jansi](https://github.com/fusesource/jansi) for enabling CoffeeCom to utilize colors and formatting in console.


`@author` solarplus
