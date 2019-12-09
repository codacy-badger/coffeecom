# CoffeeCom - Communication done Simple & Clean

### ABOUT:

The purpose of this application is to provide a tool for straight forward and easy communication - locally or globally. Create your own communication-lobby and access it from wherever you may be.
This application only works inside a terminal, no GUI (application-window) is provided.

### IMAGES:

![Server-side](server_console.png "A peek in the console of the server")
![Client-side](client_console.png "A peek in the console of the client")

### REQUIREMENTS:

CoffeeCom requires [java](https://www.java.com/en/download/) to run. If you want to compile it yourself you need the [Java JDK](https://www.oracle.com/java/technologies/jdk8-downloads.html) (development kit).

### INSTALLATION:

Download the desired jar from [releases](https://github.com/zipsap/coffeecom/releases). You can then run the jar using the command `java -jar [release_name].jar`.
An optional approach is to place the program in a directory of your choice and then adding the program to path for easy access.

### ACCESS FROM ANYWHERE (PORT FORWARDING):

1.  Create and assign a static IP to the machine running the CoffeeCom-server. (`Server.class`)
2.  Open the desired port from within the router (check if there is any regulation/restriction/standard existing for that port)
3.  Access the Coffeecom-server from anywhere using the static ip and port forwarded

### TODO:

* [ ] Ability to use commands, e.g.: `:exit` and `:log 50`
    * [ ] Superuser-commands available to server or admin-user, e.g. `:kick [username]`
* [ ] See communication-log on startup, e.g. show 50 last messages
* [ ] Implement configuration file that each client can modify

### DISCLAIMER:

*  I am not responsible for any misuse or violation you may do or act through/using this software.
*  I am not responsible for any loss of data, damage or value to you or anyone else.
*  This is an application solely intended for communication between individuals - use it with logic, common sense and good intentions.

USE THIS SOFTWARE AT YOUR OWN RISK.

### THANKS TO: :heart:

* [Jansi](https://github.com/fusesource/jansi) for enabling CoffeeCom to utilize colors and formatting in console.

`@author` zipsap

(Project transferred from my previous (and now deleted) bitbucket-repo, where my username was "solarplus". The project is now updated to exclude "solarplus" as a package name, as it is not fitting.)
