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

import static aero.fixm.avro.FixmNamespacePrefixMapper.FIXM_NAS_EXTENSION_NAMESPACE;
import edu.mit.ll.xml_avro_converter.AvroSchemaGenerator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.namespace.QName;
import org.apache.avro.Schema;
import org.apache.avro.Schema.Field;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.reflect.ReflectData;

public class FixmReflectData extends ReflectData {

  private static final Map<QName, Class> genericTypeMappings;

  static {
    // Create mappings for generic types
    genericTypeMappings = new HashMap<>();
    genericTypeMappings.put(
            new QName(FIXM_NAS_EXTENSION_NAMESPACE, "followingPosition"),
            aero.faa.nas._4.PlannedReportingPositionType.class
    );
    genericTypeMappings.put(
            new QName(FIXM_NAS_EXTENSION_NAMESPACE, "nextPosition"),
            aero.faa.nas._4.PlannedReportingPositionType.class
    );
    genericTypeMappings.put(
            new QName(FIXM_NAS_EXTENSION_NAMESPACE,
                    "airspaceControlledEntryTime"),
            aero.fixm.base._4.TimeType.class
    );
    genericTypeMappings.put(
            new QName(FIXM_NAS_EXTENSION_NAMESPACE, "pointout"),
            aero.faa.nas._4.PointoutType.class
    );
    genericTypeMappings.put(
            new QName(FIXM_NAS_EXTENSION_NAMESPACE, "interimAltitude"),
            aero.faa.nas._4.SimpleAltitudeType.class
    );
  }

  private AvroSchemaGenerator schemaGenerator;

  public AvroSchemaGenerator getSchemaGenerator() {
    return schemaGenerator;
  }

  public void setSchemaGenerator(AvroSchemaGenerator schemaGenerator) {
    this.schemaGenerator = schemaGenerator;
  }

  // Create a JaxbElement record in the Avro schema
  private Schema createJaxbElementSchema(XmlElementRef annotationData) {
    Class dataType = genericTypeMappings.get(
            new QName(annotationData.namespace(), annotationData.name()));
    String namespace = dataType.getPackage().getName();
    String name = dataType.getSimpleName() + "JaxbElement";

    List<Field> fields = new ArrayList<>();
    fields.add(new Field("nil", Schema.create(Schema.Type.BOOLEAN),
            null, null));
    fields.add(new Field("fieldname", Schema.create(Schema.Type.STRING),
            null, null));
    fields.add(new Field("data", getSchema(dataType),
            null, null));
    Schema customSchema = Schema.createRecord(
            name, null, namespace, false, fields);
    schemaGenerator.declareCustomNamedSchema(customSchema);
    return customSchema;
  }

  @Override
  protected Schema createFieldSchema(java.lang.reflect.Field field,
          Map<String, Schema> names) {
    // Create the field schema based on the annotation data
    XmlElementRef annotationData = field.getAnnotation(XmlElementRef.class);
    if (annotationData == null) {
      return super.createFieldSchema(field, names);
    }

    if (annotationData.type() == JAXBElement.class) {
      return createJaxbElementSchema(annotationData);
    }

    throw new UnsupportedOperationException("Unexpected annotation data: "
            + annotationData.type());
  }

  @Override
  public DatumWriter createDatumWriter(Schema schema) {
    return new FixmDatumWriter(schema, this);
  }

  @Override
  public DatumReader createDatumReader(Schema writer, Schema reader) {
    return new FixmDatumReader(writer, reader);
  }

  @Override
  public DatumReader createDatumReader(Schema schema) {
    return new FixmDatumReader(schema);
  }
}
