#LinuxJavaFixes

Simple javaagent to fix problems in linux with non latin hotkeys in gui java applications.

Aimed to walkaround bug  with java gui apps: "Hotkeys not functional in non-latin keyboard layout in 13.10 and 14.04" https://bugs.launchpad.net/unity/+bug/1226962

## Swing java apps (IntelliJ Idea, Oracle SQL Developer etc.)

Copy to any directory 1 file:

**LinuxJavaFixes-1.0.0-SNAPSHOT.jar**

add 

`-javaagent:[path to directory with jar files]/LinuxJavaFixes-1.0.0-SNAPSHOT.jar`

to java run string

#### Examples:

##### SoapUI

Add line to **soapui.sh**.

`JAVA_OPTS="$JAVA_OPTS java -javaagent:[path to directory with jar files]/LinuxJavaFixes-1.0.0-SNAPSHOT.jar`

##### Oracle SQL Developer

Add line to **sqldeveloper/ide/bin/jdk.conf**.

`AddVMOption -javaagent:[path to directory with jar files]/LinuxJavaFixes-1.0.0-SNAPSHOT.jar`

##### IntelliJ Idea

Add line to **idea64.vmoptions* or *idea.vmoptions**

`-javaagent:[path to directory with jar files]/LinuxJavaFixes-1.0.0-SNAPSHOT.jar`

## Eclipse

Copy to any directory 1 files:

**LinuxJavaFixes-1.0.0-SNAPSHOT.jar**

Add following line to **eclipse.ini**.

`-javaagent:[path to directory with jar files]/LinuxJavaFixes-1.0.0-SNAPSHOT.jar=swt`

## Advanced part

### Modify kaybindings for swing apps in case non russian layout
 
If you want another mapping you can create it by yourself:

  - run any app with java vm option `-javaagent:[path]/LinuxJavaFixes-1.0.0-SNAPSHOT.jar=print`
  - after that utily begin print to console entered symbol codes using format

`XKeysymPatchAgent.keysym=[hex code]`
`-javaagent:[path to directory with jar files]/LinuxJavaFixes-1.0.0-SNAPSHOT.jar=swt`

  - then create file using format `[hex code]=[latin code of the same button]`

#### Example:

```
6ca=Q

6c3=W
```
etc.

  - replace hex codes wuth yours
  - use following option to run app with custom mapping:

`-javaagent:[path to directory with jar files]/LinuxJavaFixes-1.0.0-SNAPSHOT.jar=[your mapping file]`

### Modify keybindings for swt in case non russian layout

  - add following line to eclipse.ini 

`-javaagent:[path]/LinuxJavaFixes-1.0.0-SNAPSHOT.jar=swt:print`

  - then grab codes and create properties file with mapping

`[your locale key]=[latin key]`

  - then run eclipse wuth following config

`-javaagent:[path]/LinuxJavaFixes-1.0.0-SNAPSHOT.jar=swt:[path to your mapping file]`
