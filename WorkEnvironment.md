Preparing the work environment

# Introduction #

Steps to prepare the work environment, to build and develop.

# Software required #

**To build you need Maven: http://maven.apache.org/download.html**

**NetBeans IDE 6.8 (distribuição Java SE): http://netbeans.org/downloads/index.html**

**OpenOffice.org 3.2.0 Software Development Kit (SDK): http://download.openoffice.org/sdk/index.html**

**Plug-in OOo Netbeans (available in NetBeans plugin management)**

**BrOffice.org 3.2.0: http://broffice.org/download**

# Building #

**Open the SpeechOO/SpeechOO project in NetBeans (don't open it as a Maven project otherwise OOo Netbeans plugin will not load).**

**Right click the project and Create OXT, the OXT will be in dist folder.**




# Try JNI without OOo extension #

CorujaJNI class has an main method that invokes native methods. You can use it if only testing the Java C++ bind.
http://code.google.com/p/speechoo/source/browse/trunk/SpeechOO/CorujaJNI/java/src/main/java/org/speechoo/coruja/CorujaJNI.java