/**
 * Copyright 2016-2017 MIT Lincoln Laboratory, Massachusetts Institute of Technology
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package aero.fixm.avro;

import edu.mit.ll.xml_avro_converter.XmlSerializer;
import edu.mit.ll.xml_avro_converter.AvroSerializer;
import edu.mit.ll.xml_avro_converter.AvroSchemaGenerator;
import java.net.URL;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.SAXException;

// Convert FIXM to Avro
public class FixmAvroConverter<T> {

  private static JAXBContext jaxbContext;
  private static javax.xml.validation.Schema xmlSchema;

  private static org.apache.avro.Schema avroSchema;
  private static FixmReflectData fixmReflectData;

  public synchronized static JAXBContext getFixmJaxbContext()
          throws JAXBException {
    if (jaxbContext == null) {
      jaxbContext = JAXBContext.newInstance(
              aero.fixm.base._4.ObjectFactory.class,
              aero.fixm.flight._4.ObjectFactory.class,
              aero.fixm.messaging._4.ObjectFactory.class,
              aero.faa.nas._4.ObjectFactory.class);
    }
    return jaxbContext;
  }

  public synchronized static javax.xml.validation.Schema getXmlSchema()
          throws JAXBException, SAXException {
    if (xmlSchema == null) {
      URL xsdUrl = FixmAvroConverter.class.getResource(
              "/schemas/fixm/4.0/core-nas-wrapper.xsd");
      SchemaFactory xsdFactory = SchemaFactory.newInstance(
              XMLConstants.W3C_XML_SCHEMA_NS_URI);
      xmlSchema = xsdFactory.newSchema(xsdUrl);
    }
    // Return the schema
    return xmlSchema;
  }

  public static <T> XmlSerializer<JAXBElement<T>> getXmlSerializer()
          throws JAXBException, SAXException {
    // Create a serializer
    XmlSerializer<JAXBElement<T>> xmlSerializer = new XmlSerializer<>(
            getFixmJaxbContext(), getXmlSchema());
    xmlSerializer.setNamespacePrefixMapper(
            FixmNamespacePrefixMapper.getInstance());
    return xmlSerializer;
  }

  public synchronized static org.apache.avro.Schema getAvroSchema() {
    if (avroSchema == null) {
      avroSchema = FixmAvroSchema.generate(getFixmReflectData());
    }
    return avroSchema;
  }

  public static <T> AvroSerializer<T> getAvroSerializer() {
    return new AvroSerializer<>(getAvroSchema(), getFixmReflectData());
  }

  private synchronized static FixmReflectData getFixmReflectData() {
    if (fixmReflectData == null) {
      fixmReflectData = new FixmReflectData();
      fixmReflectData.setSchemaGenerator(new AvroSchemaGenerator(
              fixmReflectData));
    }
    return fixmReflectData;
  }
}
