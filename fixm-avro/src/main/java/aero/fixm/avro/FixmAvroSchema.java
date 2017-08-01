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

import edu.mit.ll.xml_avro_converter.AvroSchemaGenerator;
import org.apache.avro.Schema;

import aero.fixm.messaging._4.MessageCollectionType;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

class FixmAvroSchema {

  public static Schema generate(FixmReflectData fixmReflectData) {
    AvroSchemaGenerator schemaGenerator = fixmReflectData.getSchemaGenerator();
    declarePolymorphicTypes(schemaGenerator);
    return schemaGenerator.generateSchema(MessageCollectionType.class);
  }

  private static AvroSchemaGenerator declarePolymorphicTypes(
          AvroSchemaGenerator schemaGenerator) {

    // Declare polymorphic types
    schemaGenerator.declarePolymorphicType(
            XMLGregorianCalendarImpl.class,
            aero.fixm.messaging._4.MessageCollectionType.class,
            aero.fixm.messaging._4.MessageType.class,
            aero.fixm.base._4.AltitudeType.class,
            aero.fixm.base._4.BearingType.class,
            aero.fixm.base._4.DesignatedPointOrNavaidType.class,
            aero.fixm.base._4.DistanceType.class,
            aero.fixm.base._4.FlightLevelType.class,
            aero.fixm.base._4.HeightType.class,
            aero.fixm.base._4.IcaoAerodromeReferenceType.class,
            aero.fixm.base._4.IcaoUnitReferenceType.class,
            aero.fixm.base._4.OtherReferenceType.class,
            aero.fixm.base._4.OtherUnitReferenceType.class,
            aero.fixm.base._4.PositionPointType.class,
            aero.fixm.base._4.RelativePointType.class,
            aero.fixm.base._4.WindDirectionType.class,
            aero.fixm.flight._4.AltitudeInTransitionType.class,
            aero.fixm.flight._4.CruiseClimbStartType.class,
            aero.fixm.flight._4.IcaoAircraftTypeReferenceType.class,
            aero.fixm.flight._4.LevelChangeType.class,
            aero.fixm.flight._4.LevelConstraintType.class,
            aero.fixm.flight._4.OtherAircraftTypeReferenceType.class,
            aero.fixm.flight._4.SpeedChangeType.class,
            aero.fixm.flight._4.SpeedConstraintType.class,
            aero.fixm.flight._4.StructuredPostalAddressType.class,
            aero.fixm.flight._4.TimeConstraintType.class,
            aero.fixm.flight._4.TrajectoryPoint4DType.class,
            aero.faa.nas._4.AboveAltitudeType.class,
            aero.faa.nas._4.NasAdaptedArrivalRouteType.class,
            aero.faa.nas._4.NasAircraftType.class,
            aero.faa.nas._4.NasArrivalType.class,
            aero.faa.nas._4.NasBoundaryCrossingType.class,
            aero.faa.nas._4.NasDepartureType.class,
            aero.faa.nas._4.NasDestinationType.class,
            aero.faa.nas._4.NasEnRouteType.class,
            aero.faa.nas._4.NasFlightIdentificationType.class,
            aero.faa.nas._4.NasFlightType.class,
            aero.faa.nas._4.NasInBlockEstimatedType.class,
            aero.faa.nas._4.NasMessageType.class,
            aero.faa.nas._4.NasOffBlockEstimatedType.class,
            aero.faa.nas._4.NasRouteElementType.class,
            aero.faa.nas._4.NasRouteInformationType.class,
            aero.faa.nas._4.NasRunwayActualType.class,
            aero.faa.nas._4.NasRunwayArrivalEstimatedType.class,
            aero.faa.nas._4.NasRunwayDepartureEstimatedType.class,
            aero.faa.nas._4.NasTrajectoryOptionType.class,
            aero.faa.nas._4.ReportedAltitudeType.class,
            aero.faa.nas._4.SimpleAltitudeType.class,
            aero.faa.nas._4.TargetAltitudeType.class,
            aero.faa.nas._4.VfrOnTopPlusAltitudeType.class,
            aero.faa.nas._4.VfrPlusAltitudeType.class
    );

    return schemaGenerator;
  }

}
