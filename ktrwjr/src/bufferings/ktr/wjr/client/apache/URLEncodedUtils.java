/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package bufferings.ktr.wjr.client.apache;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * (edited by bufferings) A collection of utilities for encoding URLs.
 * 
 * @since 4.0
 */
public class URLEncodedUtils {

  public static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
  private static final String PARAMETER_SEPARATOR = "&";
  private static final String NAME_VALUE_SEPARATOR = "=";

  /**
   * Returns a String that is suitable for use as an
   * <code>application/x-www-form-urlencoded</code> list of parameters in an
   * HTTP PUT or HTTP POST.
   * 
   * @param parameters
   *          The parameters to include.
   * @param encoding
   *          The encoding to use.
   */
  public static String format(final List<? extends NameValuePair> parameters,
      final String encoding) {
    final StringBuilder result = new StringBuilder();
    for (final NameValuePair parameter : parameters) {
      final String encodedName = encode(parameter.getName(), encoding);
      final String value = parameter.getValue();
      final String encodedValue = value != null ? encode(value, encoding) : "";
      if (result.length() > 0)
        result.append(PARAMETER_SEPARATOR);
      result.append(encodedName);
      result.append(NAME_VALUE_SEPARATOR);
      result.append(encodedValue);
    }
    return result.toString();
  }

  private static String encode(final String content, final String encoding) {
    try {
      return URLEncoder.encode(content, encoding != null
        ? encoding
        : HTTP.DEFAULT_CONTENT_CHARSET);
    } catch (UnsupportedEncodingException problem) {
      throw new IllegalArgumentException(problem);
    }
  }

}
