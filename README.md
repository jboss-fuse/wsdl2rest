Generates REST endpoints from WSDL
----------------------------------

A tool allowing quick migration from existing JAX-WS services to REST services.

Derived from https://sourceforge.net/projects/wsdl2rest

Usage
-----

```
wsdl2rest [options...]
 --wsdl URL             : URL to the input WSDL (required)
 --out PATH             : Output path for generated artefacts (required)
 --camel-context PATH   : Path to the generated camel context file name
 --jaxrs URL            : Address of the generated jaxrs endpoint
 --jaxws URL            : Address of the target jaxws endpoint
```