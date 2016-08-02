# Hybris Maven Plugin
This maven plugin aims for allowing the hybris commerce suite to be build to java archive files.

**ATTENTION**
This plugin is currently WiP, not stable and not recommended for productive
use! The only tested version is 5.5.1.1

## Convert hybris suite to maven project

The first use of the plugin is the conversion of a given hybris installation to
a pure maven project.

This step is necessary for the next usage of the plugin.

Simply type `mvn hybris:install` within the hybris installation directory and
it will happen. After this your local maven repository contains various
artifacts according to your hybris installation.

### In detail
* determine the version of the hybris installation
* disassembly and re-structure the suites sources and resources in a temporary
folder
* utilize the hybris build tools (ybootstrap.jar etc.)
* install the reactor build for the hybris suite which automatically generates
the sources defined in *-items.xml and *-beans.xml

## Build custom extensions

With the hybris artifact in you local maven repository you can develop you
custom extension within a pure maven reactor build.

## Current TODO's

* rearrange all extensions for hybris suite
  * ~~core~~
  * *all other*
* generate code
  * ~~utilize CodeGenerator from ybootstrap.jar~~
  * execute code generation
* ~~execute the reactor build and install hybris suite artifacts~~
