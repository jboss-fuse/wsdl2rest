Jump from SOAP to REST with WSDL2REST
-------------------------------------

A tool chain allowing quick migration from existing SOAP webservices
implementation to RESTful webservices implementation by using existing WSDL.

Derived from https://sourceforge.net/projects/wsdl2rest

Build
-----

```
$ mvn install:install-file -Dfile=lib/wsif.jar -DgroupId=org.apache.wsif -DartifactId=wsif -Dversion=2.0 -Dpackaging=jar
$ mvn clean install
```