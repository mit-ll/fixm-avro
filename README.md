# fixm-avro

The `fixm-avro` library implements [FIXM]-specific databindings and logic for converting FIXM XML to and from [Avro] format. It makes use of `xml-avro-converter`, a generic library for converting XML schemas and data between Avro format.

[FIXM]: https://fixm.aero/
[Avro]: https://avro.apache.org/

# Motivation

FIXM is an XML standard for the exchange of flight information, developed as part of a collaboration between the [Federal Aviation Administration], [Eurocontrol], and other international aviation and data standards organizations.

[Federal Aviation Administration]: https://www.faa.gov/
[Eurocontrol]: https://www.eurocontrol.int/

The authors have observed that typical FIXM documents in Avro format are approximately 60% of the size in XML format. This size reduction was comparable to [EXI] compression of the same XML document. Furthermore, converting XML data into Avro format enables analysts to leverage FIXM data with all the benefits of Avro format and its integration with [Apache Hadoop] and other data analytics systems. The authors have demonstrated that when FIXM XML documents are converted to Avro and back to XML, both the original XML document and the XML document generated from the Avro data stream are identical. This means that users can store and transport FIXM documents in the format of their choosing, as they can decide whether XML or Avro format is more appropriate for their bandwidth and storage requirements.

[EXI]: https://www.w3.org/XML/EXI/
[Apache Hadoop]: https://hadoop.apache.org/

# Installation

If you are using a build system such as Maven, it is only necessary to add the fixm-avro library into the dependency section of the project POM:

```xml
<dependency>
  <groupId>aero.fixm.avro</groupId>
  <artifactId>fixm-avro</artifactId>
</dependency>
```

If you are using a build system which does not provide automatic dependency resolution, then it is necessary to include both the JAR files for both fixm-avro and xml-avro-converter on the project's classpath.

# Usage

The unit test found in [FixmAvroConverterTest.java](fixm-avro/src/test/java/aero/fixm/avro/FixmAvroConverterTest.java) provides a useful reference for how to use this library. A sample program is provided below:

```java
package aero.fixm.avro;

import aero.fixm.messaging._4.MessageCollectionType;
import edu.mit.ll.xml_avro_converter.AvroSerializer;
import edu.mit.ll.xml_avro_converter.XmlSerializer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import javax.xml.bind.JAXBElement;
import org.w3c.dom.Document;

public class ConvertFixmXmlToAvro {

  static final aero.fixm.messaging._4.ObjectFactory msg_objf
          = new aero.fixm.messaging._4.ObjectFactory();

  // Convert an XML file to Avro
  public static void main(String[] fileNames) throws Exception {
    XmlSerializer<JAXBElement<MessageCollectionType>> xmlSerializer
            = FixmAvroConverter.<MessageCollectionType>getXmlSerializer();
    AvroSerializer<MessageCollectionType> avroSerializer
            = FixmAvroConverter.<MessageCollectionType>getAvroSerializer();

    for (String fileName : fileNames) {
      JAXBElement<MessageCollectionType> sampleMsg
              = xmlSerializer.readFromXml(new FileInputStream(fileName));
      ByteArrayOutputStream avro_os = new ByteArrayOutputStream();
      avroSerializer.writeToAvro(avro_os, sampleMsg.getValue());
      byte[] avro_bytes = avro_os.toByteArray();

      MessageCollectionType avroMsg = avroSerializer.readFromAvro(
              new ByteArrayInputStream(avro_bytes));
      Document avroDoc = xmlSerializer.writeToXml(
              msg_objf.createMessageCollection(avroMsg));
      XmlSerializer.printDocument(avroDoc, System.out, false);
      System.out.println(String.format(
              "Avro document size: %d bytes", avro_bytes.length));

    }
  }
}
```

The ``XmlSerializer`` and ``AvroSerializer`` types provided by ``fixm-avro`` will handle the conversion of data to their respective types. Since the top-level element of a FIXM document is usually a ``MessageCollectionType``, this is the specific type of the generic ``XmlSerializer`` and ``AvroSerializer`` types.

The main loop then demonstrates how to use the ``XmlSerializer`` to read an ``InputStream`` (in this case, from a file whose name is provided at the command line) of FIXM XML data into a Java object. Then the``AvroSerializer`` is used to convert this Java object into a bytestream in Avro format. Then for demonstration purposes, the Avro bytestream is read back into a ``MessageCollectionType`` Java object which is then serialized into an XML ``Document``. Finally to demonstrate that the XML file is identical after Avro conversion, the document is printed at the command line.

# Navigating the Source Code

The `fixm-jaxb` module contains the FIXM XML schemas and JAXB bindings for generating the source code. The maven project will use these schemas to generate the FIXM Java bindings at compile time.

The `fixm-avro` module contains the FIXM-specific configuration of `xml-avro-converter`'s  capabilities. It defines the polymorphic types which must be added to the Avro schema manually. Additionally, a small number of fields in the FIXM schema require manual translation logic, which is also handled in these source code files.

* [FixmAvroConverter.java] is the primary source code file and the main class that users are expected to interact with. It instantiates most of the classes used for data conversion. It stores singleton instances of the `JAXBContext` and the XML schema used to create `XMLSerializer` instances. It also stores singleton instances of the FIXM Avro schema and the `FixmReflectData` used to instantiate `AvroSerializer` instances.

* [FixmAvroSchema.java] creates the FIXM Avro schema. It declares the polymorphic types that are in the schema and generates the Avro schema that incorporates them.

* [FixmNamespacePrefixMapper.java] specifies the prefixes to be used on elements in the XML documents.

* [FixmReflectData.java] specifies how to deal with with types in the XML schema that Avro cannot handle. It defines the portions of the Avro schema that handle `JAXBElement` members of the generated Java classes and it specifies that `FixmDatumReader` should be used to read FIXM Avro data and `FixmDatumWriter` should be used to write FIXM Avro data.

* [FixmDatumReader.java] and [FixmDatumWriter.java] contain the specific logic for reading from an Avro stream and writing to an Avro stream respectively.

[FixmAvroConverter.java]: fixm-avro/src/main/java/aero/fixm/avro/FixmAvroConverter.java
[FixmAvroSchema.java]: fixm-avro/src/main/java/aero/fixm/avro/FixmAvroSchema.java
[FixmNamespacePrefixMapper.java]: fixm-avro/src/main/java/aero/fixm/avro/FixmNamespacePrefixMapper.java
[FixmReflectData.java]: fixm-avro/src/main/java/aero/fixm/avro/FixmReflectData.java
[FixmDatumWriter.java]: fixm-avro/src/main/java/aero/fixm/avro/FixmDatumWriter.java
[FixmDatumReader.java]: fixm-avro/src/main/java/aero/fixm/avro/FixmDatumReader.java

# License

Copyright 2016-2017 MIT Lincoln Laboratory, Massachusetts Institute of Technology

Licensed under the Apache License, Version 2.0 (the "License"); you may not use these files except in compliance with the License.

You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

     Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

