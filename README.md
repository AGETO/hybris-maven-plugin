[![Build Status](https://travis-ci.org/AGETO/hybris-maven-plugin.svg?branch=master)](https://travis-ci.org/AGETO/hybris-maven-plugin)
[![Dependency Status](https://www.versioneye.com/user/projects/57acbc42fc256900403eab3e/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/57acbc42fc256900403eab3e)
[![Issue Stats](http://issuestats.com/github/AGETO/hybris-maven-plugin/badge/pr)](http://issuestats.com/github/AGETO/hybris-maven-plugin)
[![Issue Stats](http://issuestats.com/github/AGETO/hybris-maven-plugin/badge/issue)](http://issuestats.com/github/AGETO/hybris-maven-plugin)

# Hybris Maven Plugin
This maven plugin aims for allowing the hybris commerce suite to be build to java archive files.
It converts a given hybris installation to a maven project. 
It allows for compiling extensions into separate modules for better dependency management of the classes of the hybris commerce suite and better maintainability of its source code.
In the end it should also be possible to build a hybris installation on a dedicated build server by simply typing `mvn hybris:install` and
copying the resulting class files to the productive system.

**ATTENTION**
This plugin is currently WiP, not stable and not recommended for productive
use! The only tested version is 5.5.1.1

## Motivation
Currently Hybris is written for eclipse developers and is hard to be imported into other IDEs.
With maven your are able to easily import projects into all bigger IDEs (Intellij, Netbeans, Eclipse, etc.) because of their quite good maven integration. 
Maven allows for automatic dependency management, so you do not have to find dependencies of your hybris plugins by trial and error.
Also maven gives you good support while the life cycle of your project.

## In detail
First it determines the version of the hybris installation and then disassembles and restructures the sources and resources of the suite in a temporary folder. After that, it calls the hybris build tools for preparing the build of the hybris suite, before it runs the actual build process and installs the reactor build.
Basing on the resulting hybris artifacts in your local maven repository you can develop your custom extenions before building and deploying your hybris installation.

## Current TODO's

* rearrange all extensions for hybris suite
  * ~~core~~
  * *all other extensions*
* generate code
  * ~~utilize CodeGenerator from ybootstrap.jar~~
  * execute code generation
* ~~execute the reactor build and install hybris suite artifacts~~
