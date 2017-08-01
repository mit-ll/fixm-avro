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

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;
import java.util.HashMap;
import java.util.Map;

public class FixmNamespacePrefixMapper extends NamespacePrefixMapper {

  private static final FixmNamespacePrefixMapper instance;
  private static final Map<String, String> namespacePrefixes;

  // FIXM namespaces
  public static final String FIXM_MESSAGING_NAMESPACE = "http://www.fixm.aero/messaging/4.0";
  public static final String FIXM_BASE_NAMESPACE = "http://www.fixm.aero/base/4.0";
  public static final String FIXM_FLIGHT_NAMESPACE = "http://www.fixm.aero/flight/4.0";
  public static final String FIXM_NAS_EXTENSION_NAMESPACE = "http://www.faa.aero/nas/4.0";
  public static final String XML_SCHEMA_NAMESPACE = "http://www.w3.org/2001/XMLSchema-instance";

  static {
    // Create the namespace prefixes
    instance = new FixmNamespacePrefixMapper();
    namespacePrefixes = new HashMap<>();
    namespacePrefixes.put(FIXM_MESSAGING_NAMESPACE, "msg");
    namespacePrefixes.put(FIXM_BASE_NAMESPACE, "fb");
    namespacePrefixes.put(FIXM_FLIGHT_NAMESPACE, "fl");
    namespacePrefixes.put(FIXM_NAS_EXTENSION_NAMESPACE, "nas");
    namespacePrefixes.put(XML_SCHEMA_NAMESPACE, "xsi");
  }

  public static FixmNamespacePrefixMapper getInstance() {
    return instance;
  }

  private FixmNamespacePrefixMapper() {
  }

  @Override
  public String getPreferredPrefix(String namespaceUri, String suggestion,
          boolean requirePrefix) {
    String prefix = namespacePrefixes.get(namespaceUri);
    if (prefix == null) {
      return suggestion;
    }
    return prefix;
  }

  @Override
  public String[] getPreDeclaredNamespaceUris() {
    String[] uris = new String[5];
    uris[0] = FIXM_MESSAGING_NAMESPACE;
    uris[1] = FIXM_BASE_NAMESPACE;
    uris[2] = FIXM_FLIGHT_NAMESPACE;
    uris[3] = FIXM_NAS_EXTENSION_NAMESPACE;
    uris[4] = XML_SCHEMA_NAMESPACE;
    return uris;
  }

}
