/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://github.com/google/apis-client-generator/
 * (build: 2016-01-08 17:48:37 UTC)
 * on 2016-01-27 at 19:49:18 UTC 
 * Modify at your own risk.
 */

package com.appspot.id.app.langeo.model;

/**
 * Model definition for UserCollection.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the langeo. For a detailed explanation see:
 * <a href="https://developers.google.com/api-client-library/java/google-http-java-client/json">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class UserCollection extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<User> items;

  /**
   * @return value or {@code null} for none
   */
  public java.util.List<User> getItems() {
    return items;
  }

  /**
   * @param items items or {@code null} for none
   */
  public UserCollection setItems(java.util.List<User> items) {
    this.items = items;
    return this;
  }

  @Override
  public UserCollection set(String fieldName, Object value) {
    return (UserCollection) super.set(fieldName, value);
  }

  @Override
  public UserCollection clone() {
    return (UserCollection) super.clone();
  }

}
