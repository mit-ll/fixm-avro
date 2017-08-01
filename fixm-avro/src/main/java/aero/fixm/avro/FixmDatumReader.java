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

import aero.faa.nas._4.ObjectFactory;
import aero.faa.nas._4.PlannedReportingPositionType;
import aero.faa.nas._4.PointoutType;
import aero.faa.nas._4.SimpleAltitudeType;
import aero.fixm.base._4.TimeType;
import java.io.IOException;
import javax.xml.bind.JAXBElement;
import org.apache.avro.Schema;
import org.apache.avro.io.ResolvingDecoder;
import org.apache.avro.reflect.ReflectDatumReader;

class FixmDatumReader<T> extends ReflectDatumReader<T> {

  public FixmDatumReader(Schema writer, Schema reader) {
    super(writer, reader);
  }

  FixmDatumReader(Schema schema) {
    super(schema);
  }

  @Override
  protected Object readRecord(Object old, Schema expected,
          ResolvingDecoder in) throws IOException {
    switch (expected.getFullName()) {
      case "aero.faa.nas._4.SimpleAltitudeTypeJaxbElement":
        return readSimpleAltitudeType(old, in);
      case "aero.fixm.base._4.TimeTypeJaxbElement":
        return readAirspaceControlledEntryTime(old, in);
      case "aero.faa.nas._4.PointoutTypeJaxbElement":
        return readPointout(old, in);
      case "aero.faa.nas._4.PlannedReportingPositionTypeJaxbElement":
        return readPosition(old, in);
      default:
        return super.readRecord(old, expected, in);
    }
  }

  private Object readSimpleAltitudeType(Object old, ResolvingDecoder in)
          throws IOException {
    Boolean nil = null;
    SimpleAltitudeType data = null;
    // Read the fields
    for (Schema.Field field : in.readFieldOrder()) {
      String fieldName = field.name();
      switch (fieldName) {
        case "nil":
          nil = (Boolean) readWithoutConversion(old, field.schema(), in);
          break;
        case "fieldname":
          readWithoutConversion(old, field.schema(), in);
          break;
        case "data":
          data = (SimpleAltitudeType) readWithoutConversion(
                  old, field.schema(), in);
          break;
        default:
          throw new RuntimeException("Unexpected field: " + fieldName);
      }
    }

    if (nil == true && data != null) {
      throw new RuntimeException("nil was true but data was not null");
    }

    // Create the object and return it
    ObjectFactory objf = new ObjectFactory();
    JAXBElement<SimpleAltitudeType> interimAltitude
            = objf.createNasFlightTypeInterimAltitude(data);
    interimAltitude.setNil(nil);
    return interimAltitude;
  }

  private Object readAirspaceControlledEntryTime(Object old,
          ResolvingDecoder in) throws IOException {
    Boolean nil = null;
    TimeType data = null;
    // Create the object and return it
    for (Schema.Field field : in.readFieldOrder()) {
      String fieldName = field.name();
      switch (fieldName) {
        case "nil":
          nil = (Boolean) readWithoutConversion(old, field.schema(), in);
          break;
        case "fieldname":
          readWithoutConversion(old, field.schema(), in);
          break;
        case "data":
          data = (TimeType) readWithoutConversion(old, field.schema(), in);
          break;
        default:
          throw new RuntimeException("unexpected field: " + fieldName);
      }
    }

    if (data == null) {
      throw new RuntimeException("data was null");
    }

    if (nil == true && data.getValue() != null) {
      throw new RuntimeException("nil was true but data was not null");
    }

    // Create the object and return it
    ObjectFactory objf = new ObjectFactory();
    JAXBElement<TimeType> airspaceControlledEntryTime = objf.
            createNasAirspaceConstraintTypeAirspaceControlledEntryTime(data);
    airspaceControlledEntryTime.setNil(nil);
    return airspaceControlledEntryTime;
  }

  private Object readPointout(Object old, ResolvingDecoder in)
          throws IOException {
    Boolean nil = null;
    PointoutType data = null;
    // Create the object and return it
    for (Schema.Field field : in.readFieldOrder()) {
      String fieldName = field.name();
      switch (fieldName) {
        case "nil":
          nil = (Boolean) readWithoutConversion(old, field.schema(), in);
          break;
        case "fieldname":
          readWithoutConversion(old, field.schema(), in);
          break;
        case "data":
          data = (PointoutType) readWithoutConversion(old, field.schema(), in);
          break;
        default:
          throw new RuntimeException("unexpected field: " + fieldName);
      }
    }

    if (nil == true && data != null) {
      throw new RuntimeException("nil was true but data was not null");
    }

    // Create the object and return it
    ObjectFactory objf = new ObjectFactory();
    JAXBElement<PointoutType> pointout = objf.createNasEnRouteTypePointout(
            data);
    pointout.setNil(nil);
    return pointout;
  }

  private Object readPosition(Object old, ResolvingDecoder in)
          throws IOException {
    Boolean nil = null;
    String fieldname = null;
    PlannedReportingPositionType data = null;
    // Create the object and return it
    for (Schema.Field field : in.readFieldOrder()) {
      String fieldName = field.name();
      switch (fieldName) {
        case "nil":
          nil = (Boolean) readWithoutConversion(old, field.schema(), in);
          break;
        case "fieldname":
          fieldname = (String) readWithoutConversion(old, field.schema(), in);
          break;
        case "data":
          data = (PlannedReportingPositionType) readWithoutConversion(
                  old, field.schema(), in);
          break;
        default:
          throw new RuntimeException("unexpected field: " + fieldName);
      }
    }

    if (nil == true && data != null) {
      throw new RuntimeException("nil was true but data was not null");
    }

    if (fieldname == null) {
      throw new RuntimeException("fieldname was not provided");
    }

    // Create the object and return it
    ObjectFactory objf = new ObjectFactory();
    JAXBElement<PlannedReportingPositionType> position;
    switch (fieldname) {
      case "followingPosition":
        position = objf.createNasAircraftPositionTypeFollowingPosition(data);
        break;
      case "nextPosition":
        position = objf.createNasAircraftPositionTypeNextPosition(data);
        break;
      default:
        throw new RuntimeException("Unexpected value for fieldname: "
                + fieldname);
    }
    position.setNil(nil);
    return position;

  }

}
