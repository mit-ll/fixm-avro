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

import aero.faa.nas._4.ControlElementType;
import aero.faa.nas._4.NasAircraftPositionType;
import aero.faa.nas._4.NasAirspaceConstraintType;
import aero.faa.nas._4.NasEnRouteType;
import aero.faa.nas._4.NasFlightType;
import aero.faa.nas._4.PlannedReportingPositionType;
import aero.faa.nas._4.PointoutType;
import aero.faa.nas._4.SimpleAltitudeType;
import aero.fixm.base._4.AltitudeType;
import aero.fixm.base._4.AtcUnitReferenceType;
import aero.fixm.base._4.DesignatedPointOrNavaidType;
import aero.fixm.base._4.IcaoUnitReferenceType;
import aero.fixm.base._4.TimeType;
import aero.fixm.base._4.UomAltitudeType;
import aero.fixm.messaging._4.AbstractMessageType;
import aero.fixm.messaging._4.MessageCollectionType;
import aero.fixm.messaging._4.MessageType;
import edu.mit.ll.xml_avro_converter.XmlSerializer;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.w3c.dom.Document;

// Utility class to create some sample FIXM documents
public class SampleFixmData {

  static final aero.faa.nas._4.ObjectFactory nas_objf
          = new aero.faa.nas._4.ObjectFactory();
  static final aero.fixm.base._4.ObjectFactory base_objf
          = new aero.fixm.base._4.ObjectFactory();
  static final aero.fixm.messaging._4.ObjectFactory msg_objf
          = new aero.fixm.messaging._4.ObjectFactory();

  public static void main(String[] args) throws Exception {
    printMessageCollectionType(createSampleInterimAltitudeData());
    printMessageCollectionType(createSamplePointoutData());
    printMessageCollectionType(createSampleEntryTimeData());
    printMessageCollectionType(createSampleAircreaftPositionData());
  }

  private static void printMessageCollectionType(MessageCollectionType mct)
          throws Exception {
    XmlSerializer<JAXBElement<MessageCollectionType>> xmlSerializer
            = FixmAvroConverter.<MessageCollectionType>getXmlSerializer();
    Document document = xmlSerializer.writeToXml(
            msg_objf.createMessageCollection(mct));
    XmlSerializer.printDocument(document, System.out, false);
  }

  private static MessageCollectionType createSampleInterimAltitudeData()
          throws Exception {
    SimpleAltitudeType sampleAltitudeType
            = createSampleAltitudeType(12345., UomAltitudeType.M);

    MessageCollectionType messageCollectionType
            = msg_objf.createMessageCollectionType();
    messageCollectionType.getMessage().add(
            createSampleInterimAltitudeMessage(
                    createSampleInterimAltitude(sampleAltitudeType, false)));
    messageCollectionType.getMessage().add(
            createSampleInterimAltitudeMessage(
                    createSampleInterimAltitude(null, true)));
    messageCollectionType.getMessage().add(
            createSampleInterimAltitudeMessage(
                    createSampleInterimAltitude(null, false)));

    return messageCollectionType;
  }

  private static JAXBElement<SimpleAltitudeType> createSampleInterimAltitude(
          SimpleAltitudeType altitudeType, boolean nil) {
    if (altitudeType == null && nil == false) {
      return null;
    }

    JAXBElement<SimpleAltitudeType> interimAltitude
            = nas_objf.createNasFlightTypeInterimAltitude(altitudeType);
    interimAltitude.setNil(nil);
    return interimAltitude;
  }

  private static SimpleAltitudeType createSampleAltitudeType(Double value,
          UomAltitudeType uom) {
    AltitudeType altitudeType = base_objf.createAltitudeType();
    altitudeType.setUom(UomAltitudeType.M);
    altitudeType.setValue(value);

    SimpleAltitudeType simpleAltitudeType
            = nas_objf.createSimpleAltitudeType();
    simpleAltitudeType.setAltitude(altitudeType);

    return simpleAltitudeType;
  }

  private static AbstractMessageType createSampleInterimAltitudeMessage(
          JAXBElement<SimpleAltitudeType> interimAltitude) {
    NasFlightType nasFlightType = nas_objf.createNasFlightType();
    nasFlightType.setInterimAltitude(interimAltitude);

    MessageType messageType = msg_objf.createMessageType();
    messageType.setFlight(nasFlightType);
    return messageType;
  }

  private static MessageCollectionType createSamplePointoutData()
          throws Exception {
    IcaoUnitReferenceType referenceType
            = base_objf.createIcaoUnitReferenceType();
    referenceType.setLocationIndicator("ABCD");
    referenceType.setControlSectorDesignator("FGHI");

    MessageCollectionType messageCollectionType
            = msg_objf.createMessageCollectionType();
    messageCollectionType.getMessage().add(createSamplePointoutMessage(
            createSamplePointout(null, false)));
    messageCollectionType.getMessage().add(createSamplePointoutMessage(
            createSamplePointout(null, true)));
    messageCollectionType.getMessage().add(createSamplePointoutMessage(
            createSamplePointout(referenceType, false)
    ));

    return messageCollectionType;
  }

  private static JAXBElement<PointoutType> createSamplePointout(
          AtcUnitReferenceType referenceType, boolean nil) {
    if (referenceType == null && nil == false) {
      return null;
    }

    PointoutType pointoutType = nas_objf.createPointoutType();
    pointoutType.setOriginatingUnit(referenceType);

    JAXBElement<PointoutType> pointout
            = nas_objf.createNasEnRouteTypePointout(pointoutType);
    pointout.setNil(nil);
    return pointout;
  }

  private static AbstractMessageType createSamplePointoutMessage(
          JAXBElement<PointoutType> pointoutType) {
    NasEnRouteType nasEnrouteType = nas_objf.createNasEnRouteType();
    nasEnrouteType.setPointout(pointoutType);

    NasFlightType nasFlightType = nas_objf.createNasFlightType();
    nasFlightType.setEnRoute(nasEnrouteType);

    MessageType messageType = msg_objf.createMessageType();
    messageType.setFlight(nasFlightType);
    return messageType;
  }

  private static MessageCollectionType createSampleEntryTimeData()
          throws Exception {
    MessageCollectionType messageCollectionType
            = msg_objf.createMessageCollectionType();
    messageCollectionType.getMessage().add(createSampleEntryTimeMessage());

    return messageCollectionType;
  }

  private static AbstractMessageType createSampleEntryTimeMessage()
          throws Exception {

    NasEnRouteType nasEnrouteType = nas_objf.createNasEnRouteType();
    nasEnrouteType.getControlElement().add(createSampleControlElementType(
            "2000-01-01T00:00:00Z", false));
    nasEnrouteType.getControlElement().add(createSampleControlElementType(
            null, true));
    nasEnrouteType.getControlElement().add(createSampleControlElementType(
            null, false));

    NasFlightType nasFlightType = nas_objf.createNasFlightType();
    nasFlightType.setEnRoute(nasEnrouteType);

    MessageType messageType = msg_objf.createMessageType();
    messageType.setFlight(nasFlightType);
    return messageType;
  }

  private static ControlElementType createSampleControlElementType(
          String date, boolean nil) throws Exception {
    JAXBElement<TimeType> controlledEntryTime = null;

    if (date != null || nil) {
      TimeType timeType = base_objf.createTimeType();
      timeType.setTimeReference(timeType.getTimeReference());
      timeType.setValue(date == null ? null : createDate(date));
      controlledEntryTime = nas_objf.
              createNasAirspaceConstraintTypeAirspaceControlledEntryTime(
                      timeType);
      controlledEntryTime.setNil(nil);
    }

    NasAirspaceConstraintType airspaceConstraintType
            = nas_objf.createNasAirspaceConstraintType();
    airspaceConstraintType.setAirspaceControlledEntryTime(controlledEntryTime);

    ControlElementType controlElementType
            = nas_objf.createControlElementType();
    controlElementType.setAirspace(airspaceConstraintType);

    return controlElementType;
  }

  private static XMLGregorianCalendar createDate(String formattedDate)
          throws Exception {
    SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss'Z'");
    Date date = dateFormat.parse("2000-01-01T00:00:00Z");
    String format = dateFormat.format(date);
    XMLGregorianCalendar calendar
            = DatatypeFactory.newInstance().newXMLGregorianCalendar(format);
    return calendar;
  }

  private static MessageCollectionType createSampleAircreaftPositionData()
          throws Exception {
    MessageCollectionType messageCollectionType
            = msg_objf.createMessageCollectionType();
    messageCollectionType.getMessage().add(createAircraftPositionMessage(
            "AA", false, null, true));
    messageCollectionType.getMessage().add(createAircraftPositionMessage(
            null, true, null, false));
    messageCollectionType.getMessage().add(createAircraftPositionMessage(
            null, false, "BB", false));

    return messageCollectionType;
  }

  private static AbstractMessageType createAircraftPositionMessage(
          String followingPosition, boolean followingNil,
          String nextPosition, boolean nextNil) {
    NasEnRouteType nasEnrouteType = nas_objf.createNasEnRouteType();
    nasEnrouteType.setAircraftPosition(createSampleAircraftPositionType(
            followingPosition, followingNil, nextPosition, nextNil));

    NasFlightType nasFlightType = nas_objf.createNasFlightType();
    nasFlightType.setEnRoute(nasEnrouteType);

    MessageType messageType = msg_objf.createMessageType();
    messageType.setFlight(nasFlightType);
    return messageType;
  }

  private static NasAircraftPositionType createSampleAircraftPositionType(
          String followingPosition, boolean followingNil,
          String nextPosition, boolean nextNil) {
    NasAircraftPositionType aircraftPositionType
            = nas_objf.createNasAircraftPositionType();

    aircraftPositionType.setFollowingPosition(createFollowingPosition(
            followingPosition, followingNil));
    aircraftPositionType.setNextPosition(createNextPosition(
            nextPosition, nextNil));
    return aircraftPositionType;
  }

  private static JAXBElement<PlannedReportingPositionType>
          createFollowingPosition(String position, boolean nil) {
    if (position == null && !nil) {
      return null;
    }
    PlannedReportingPositionType positionType = null;
    if (position != null) {
      positionType = createReportingPositionType(position);
    }
    JAXBElement<PlannedReportingPositionType> reportingPosition = nas_objf.
            createNasAircraftPositionTypeFollowingPosition(positionType);
    reportingPosition.setNil(nil);
    return reportingPosition;
  }

  private static JAXBElement<PlannedReportingPositionType>
          createNextPosition(String position, boolean nil) {
    if (position == null && !nil) {
      return null;
    }
    PlannedReportingPositionType positionType = null;
    if (position != null) {
      positionType = createReportingPositionType(position);
    }
    JAXBElement<PlannedReportingPositionType> reportingPosition = nas_objf.
            createNasAircraftPositionTypeNextPosition(positionType);
    reportingPosition.setNil(nil);
    return reportingPosition;
  }

  private static PlannedReportingPositionType createReportingPositionType(
          String position) {
    DesignatedPointOrNavaidType positionType
            = base_objf.createDesignatedPointOrNavaidType();
    positionType.setDesignator(position);

    PlannedReportingPositionType reportingPositionType
            = nas_objf.createPlannedReportingPositionType();
    reportingPositionType.setPosition(positionType);
    return reportingPositionType;
  }
}
