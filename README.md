SWT Tabeles
====

This project is a fork of the DynamicTable implementation from the [Opal project](https://code.google.com/a/eclipselabs.org/p/opal/)
with several additions and bug fixes. In a DynamicTable the width of columns can be specified relatively to the overall width
of the table. Moreover, individual columns can be hidden or shown via a context menu on the table header.

Example
------	

```Java
DynamicTable table = new DynamicTable(parent, SWT.NONE);

DynamicTableColumn column1 = new DynamicTableColumn(table, SWT.NONE);
column1.setWidth("50%", "100px");

DynamicTableColumn column2 = new DynamicTableColumn(table, SWT.NONE);
column2.setWidth("50%", "100px");
```

Download
------
A binary version (JAR file) is available for download [here](https://rawgithub.com/prasser/swttable/master/jars/swttable-0.0.1.jar).

The according Javadoc is available for download [here](https://rawgithub.com/prasser/swttable/master/jars/swttable-0.0.1-doc.jar). 

Documentation
------
Online documentation can be found [here](https://rawgithub.com/prasser/swttable/master/doc/index.html).

License
------
EPL 1.0
