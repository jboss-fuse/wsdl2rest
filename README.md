Generates REST endpoints from WSDL
----------------------------------

A tool chain allowing quick migration from existing 
JAX-WS services to RESTful services by using existing WSDL.

Derived from https://sourceforge.net/projects/wsdl2rest

Usage
-----

```
wsdl2rest [options...]
 --out PATH            : Output path for generated artefacts (required)
 --target-address URL  : Address for the generated camel endpoint
 --target-bean VAL     : Classname for the bean that camel delegates to
 --target-context PATH : Path to the generated camel context
 --wsdl URL            : URL to the input WSDL (required)
```