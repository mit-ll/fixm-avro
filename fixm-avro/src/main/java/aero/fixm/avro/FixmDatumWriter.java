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

import aero.faa.nas._4.NasAircraftPositionType;
import aero.faa.nas._4.NasAirspaceConstraintType;
import aero.faa.nas._4.NasEnRouteType;
import aero.faa.nas._4.NasFlightType;
import aero.faa.nas._4.PlannedReportingPositionType;
import aero.faa.nas._4.PointoutType;
import aero.faa.nas._4.SimpleAltitudeType;
import aero.fixm.base._4.TimeType;
import java.io.IOException;
import javax.xml.bind.JAXBElement;
import org.apache.avro.Schema;
import org.apache.avro.io.Encoder;
import org.apache.avro.reflect.ReflectDatumWriter;

class FixmDatumWriter<T> extends ReflectDatumWriter<T> {

  public FixmDatumWriter(Schema schema, FixmReflectData reflectData) {
    super(schema, reflectData);
  }

  @Override
  protected void writeField(Object record, Schema.Field f, Encoder out,
          Object state) throws IOException {
    if (record instanceof NasFlightType
            && f.name().equals("interimAltitude")) {
      // Write a NasFlightType
      JAXBElement<SimpleAltitudeType> interimAltitude
              = ((NasFlightType) record).getInterimAltitude();
      writeInterimAltitude(interimAltitude, f, out, "interimAltitude");
      return;
    }
    if (record instanceof NasAirspaceConstraintType
            && f.name().equals("airspaceControlledEntryTime")) {
      // Write a NasAirspaceConstraintType
      JAXBElement<TimeType> entryTime = ((NasAirspaceConstraintType) record)
              .getAirspaceControlledEntryTime();
      writeAirspaceControlledEntryTime(entryTime, f, out,
              "airspaceControlledEntryTime");
      return;
    }
    if (record instanceof NasEnRouteType
            && f.name().equals("pointout")) {
      // Write a NasEnRouteType
      JAXBElement<PointoutType> pointout = ((NasEnRouteType) record)
              .getPointout();
      writePointout(pointout, f, out, "pointout");
      return;
    }
    if (record instanceof NasAircraftPositionType
            && f.name().equals("followingPosition")) {
      NasAircraftPositionType position = (NasAircraftPositionType) record;
      writePosition(position.getFollowingPosition(), f.schema(), out,
              "followingPosition");
      return;
    }
    if (record instanceof NasAircraftPositionType
            && f.name().equals("nextPosition")) {
      NasAircraftPositionType position = (NasAircraftPositionType) record;
      writePosition(position.getNextPosition(), f.schema(), out,
              "nextPosition");
      return;
    }
    super.writeField(record, f, out, state);
  }

  private void writeInterimAltitude(
          JAXBElement<SimpleAltitudeType> interimAltitude, Schema.Field f,
          Encoder out, String fieldname) throws IOException {
    Schema schema = f.schema();
    if (interimAltitude == null) {
      writeNull(schema, out);
      return;
    }

    // Write the object to Avro
    String className = SimpleAltitudeType.class.getCanonicalName();
    className += "JaxbElement";
    int unionIndex = schema.getIndexNamed(className);
    out.writeIndex(unionIndex);
    Schema recordSchema = schema.getTypes().get(unionIndex);
    Schema nilSchema = recordSchema.getField("nil").schema();
    write(nilSchema, interimAltitude.isNil(), out);
    Schema fieldnameSchema = recordSchema.getField("fieldname").schema();
    write(fieldnameSchema, fieldname, out);
    Schema dataSchema = recordSchema.getField("data").schema();
    write(dataSchema, interimAltitude.getValue(), out);
  }

  private void writeAirspaceControlledEntryTime(
          JAXBElement<TimeType> airspaceControlledEntryTime, Schema.Field f,
          Encoder out, String fieldname) throws IOException {
    Schema schema = f.schema();
    if (airspaceControlledEntryTime == null) {
      writeNull(schema, out);
      return;
    }

    // Write the object to Avro
    String className = TimeType.class.getCanonicalName();
    className += "JaxbElement";
    int unionIndex = schema.getIndexNamed(className);
    out.writeIndex(unionIndex);
    Schema recordSchema = schema.getTypes().get(unionIndex);
    Schema nilSchema = recordSchema.getField("nil").schema();
    write(nilSchema, airspaceControlledEntryTime.isNil(), out);
    Schema fieldnameSchema = recordSchema.getField("fieldname").schema();
    write(fieldnameSchema, fieldname, out);
    Schema dataSchema = recordSchema.getField("data").schema();
    write(dataSchema, airspaceControlledEntryTime.getValue(), out);
  }

  private void writePointout(JAXBElement<PointoutType> pointout,
          Schema.Field f, Encoder out, String fieldname) throws IOException {
    Schema schema = f.schema();
    if (pointout == null) {
      writeNull(schema, out);
      return;
    }

    // Write the object to Avro
    String className = PointoutType.class.getCanonicalName();
    className += "JaxbElement";
    int unionIndex = schema.getIndexNamed(className);
    out.writeIndex(unionIndex);
    Schema recordSchema = schema.getTypes().get(unionIndex);
    Schema nilSchema = recordSchema.getField("nil").schema();
    write(nilSchema, pointout.isNil(), out);
    Schema fieldnameSchema = recordSchema.getField("fieldname").schema();
    write(fieldnameSchema, fieldname, out);
    Schema dataSchema = recordSchema.getField("data").schema();
    write(dataSchema, pointout.getValue(), out);
  }

  private void writePosition(
          JAXBElement<PlannedReportingPositionType> position,
          Schema schema, Encoder out, String fieldname) throws IOException {
    if (position == null) {
      writeNull(schema, out);
      return;
    }

    // Write the object to Avro
    String className = PlannedReportingPositionType.class.getCanonicalName();
    className += "JaxbElement";
    int unionIndex = schema.getIndexNamed(className);
    out.writeIndex(unionIndex);
    Schema recordSchema = schema.getTypes().get(unionIndex);
    Schema nilSchema = recordSchema.getField("nil").schema();
    write(nilSchema, position.isNil(), out);
    Schema fieldnameSchema = recordSchema.getField("fieldname").schema();
    write(fieldnameSchema, fieldname, out);
    Schema dataSchema = recordSchema.getField("data").schema();
    write(dataSchema, position.getValue(), out);
  }

  private void writeNull(Schema schema, Encoder out) throws IOException {
    int unionIndex = schema.getIndexNamed("null");
    out.writeIndex(unionIndex);
    out.writeNull();
  }

}
