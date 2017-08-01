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

import static aero.fixm.avro.SampleFixmData.msg_objf;
import aero.fixm.messaging._4.MessageCollectionType;
import edu.mit.ll.xml_avro_converter.AvroSerializer;
import edu.mit.ll.xml_avro_converter.XmlSerializer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import javax.xml.bind.JAXBElement;
import org.custommonkey.xmlunit.XMLAssert;
import org.junit.Test;
import org.w3c.dom.Document;

public class FixmAvroConverterTest {

  @Test
  public void testSampleInterimAltitude() throws Exception {
    testSampleDocument("/sample_interim_altitude.xml");
  }

  @Test
  public void testSampleEntryTime() throws Exception {
    testSampleDocument("/sample_entry_time.xml");
  }

  @Test
  public void testSamplePointout() throws Exception {
    testSampleDocument("/sample_pointout.xml");
  }

  @Test
  public void testSampleAircraftPosition() throws Exception {
    testSampleDocument("/sample_aircraft_position.xml");
  }

  @Test
  public void testSampleNasMessageCollection() throws Exception {
    testSampleDocument("/sample_nas_message_collection.xml");
  }

  // Test a sample document on the classpath
  public void testSampleDocument(String resourceName) throws Exception {
    InputStream dataStream
            = FixmAvroConverterTest.class.getResourceAsStream(resourceName);
    XmlSerializer<JAXBElement<MessageCollectionType>> xmlSerializer
            = FixmAvroConverter.<MessageCollectionType>getXmlSerializer();
    AvroSerializer<MessageCollectionType> avroSerializer
            = FixmAvroConverter.<MessageCollectionType>getAvroSerializer();

    JAXBElement<MessageCollectionType> sampleMsg
            = xmlSerializer.readFromXml(dataStream);
    ByteArrayOutputStream avro_os = new ByteArrayOutputStream();
    avroSerializer.writeToAvro(avro_os, sampleMsg.getValue());
    byte[] avro_bytes = avro_os.toByteArray();

    MessageCollectionType avroMsg = avroSerializer.readFromAvro(
            new ByteArrayInputStream(avro_bytes));
    Document sampleDoc = xmlSerializer.writeToXml(sampleMsg);
    Document avroDoc = xmlSerializer.writeToXml(
            msg_objf.createMessageCollection(avroMsg));
    XMLAssert.assertXMLEqual(sampleDoc, avroDoc);
  }
}
