io.javaalmanac.javadoclink - Deep links for Javadoc
===================================================

Small Java library to create deep links into generated Javadoc for all Java
versions from 1.1 to 18.

The library can create links for

* modules
* packages
* types (classes, interfaces, enums, records, annotations)
* methods and constructors
* fields

based on the internal names (class files) or on Java reflection types. For example
this snippet creates a link to the `String` class:

```java
JavaDocLink.forVersion("11")
           .withBaseUrl("https://docs.oracle.com/en/java/javase/11/docs/api/")
           .classLink(String.class);
```

Or using internal vm names:

```java
JavaDocLink.forVersion("11")
           .withBaseUrl("https://docs.oracle.com/en/java/javase/11/docs/api/")
           .classLink("java.base", "java/lang/String");
```


## License

This code is provided "as is" under the [MIT License](LICENSE.md), without warranty of any kind.


## Trademarks

Java are registered trademarks of Oracle and/or its affiliates. Other names may be trademarks of their respective owners.
