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

package com.appspot.id.app.langeo;

/**
 * Service definition for Langeo (v1).
 *
 * <p>
 * This is an API
 * </p>
 *
 * <p>
 * For more information about this service, see the
 * <a href="" target="_blank">API Documentation</a>
 * </p>
 *
 * <p>
 * This service uses {@link LangeoRequestInitializer} to initialize global parameters via its
 * {@link Builder}.
 * </p>
 *
 * @since 1.3
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public class Langeo extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient {

  // Note: Leave this static initializer at the top of the file.
  static {
    com.google.api.client.util.Preconditions.checkState(
        com.google.api.client.googleapis.GoogleUtils.MAJOR_VERSION == 1 &&
        com.google.api.client.googleapis.GoogleUtils.MINOR_VERSION >= 15,
        "You are currently running with version %s of google-api-client. " +
        "You need at least version 1.15 of google-api-client to run version " +
        "1.21.0 of the langeo library.", com.google.api.client.googleapis.GoogleUtils.VERSION);
  }

  /**
   * The default encoded root URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_ROOT_URL = "https://${app.id}.appspot.com/_ah/api/";

  /**
   * The default encoded service path of the service. This is determined when the library is
   * generated and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_SERVICE_PATH = "langeo/v1/";

  /**
   * The default encoded base URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   */
  public static final String DEFAULT_BASE_URL = DEFAULT_ROOT_URL + DEFAULT_SERVICE_PATH;

  /**
   * Constructor.
   *
   * <p>
   * Use {@link Builder} if you need to specify any of the optional parameters.
   * </p>
   *
   * @param transport HTTP transport, which should normally be:
   *        <ul>
   *        <li>Google App Engine:
   *        {@code com.google.api.client.extensions.appengine.http.UrlFetchTransport}</li>
   *        <li>Android: {@code newCompatibleTransport} from
   *        {@code com.google.api.client.extensions.android.http.AndroidHttp}</li>
   *        <li>Java: {@link com.google.api.client.googleapis.javanet.GoogleNetHttpTransport#newTrustedTransport()}
   *        </li>
   *        </ul>
   * @param jsonFactory JSON factory, which may be:
   *        <ul>
   *        <li>Jackson: {@code com.google.api.client.json.jackson2.JacksonFactory}</li>
   *        <li>Google GSON: {@code com.google.api.client.json.gson.GsonFactory}</li>
   *        <li>Android Honeycomb or higher:
   *        {@code com.google.api.client.extensions.android.json.AndroidJsonFactory}</li>
   *        </ul>
   * @param httpRequestInitializer HTTP request initializer or {@code null} for none
   * @since 1.7
   */
  public Langeo(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
      com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
    this(new Builder(transport, jsonFactory, httpRequestInitializer));
  }

  /**
   * @param builder builder
   */
  Langeo(Builder builder) {
    super(builder);
  }

  @Override
  protected void initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest<?> httpClientRequest) throws java.io.IOException {
    super.initialize(httpClientRequest);
  }

  /**
   * An accessor for creating requests from the LangeoAPI collection.
   *
   * <p>The typical use is:</p>
   * <pre>
   *   {@code Langeo langeo = new Langeo(...);}
   *   {@code Langeo.LangeoAPI.List request = langeo.langeoAPI().list(parameters ...)}
   * </pre>
   *
   * @return the resource collection
   */
  public LangeoAPI langeoAPI() {
    return new LangeoAPI();
  }

  /**
   * The "langeoAPI" collection of methods.
   */
  public class LangeoAPI {

    /**
     * Create a request for the method "langeoAPI.getOnlineUsersForCity".
     *
     * This request holds the parameters needed by the langeo server.  After setting any optional
     * parameters, call the {@link GetOnlineUsersForCity#execute()} method to invoke the remote
     * operation.
     *
     * @param id
     * @return the request
     */
    public GetOnlineUsersForCity getOnlineUsersForCity(java.lang.String id) throws java.io.IOException {
      GetOnlineUsersForCity result = new GetOnlineUsersForCity(id);
      initialize(result);
      return result;
    }

    public class GetOnlineUsersForCity extends LangeoRequest<com.appspot.id.app.langeo.model.UserCollection> {

      private static final String REST_PATH = "cities/{id}/onlineUsers";

      /**
       * Create a request for the method "langeoAPI.getOnlineUsersForCity".
       *
       * This request holds the parameters needed by the the langeo server.  After setting any optional
       * parameters, call the {@link GetOnlineUsersForCity#execute()} method to invoke the remote
       * operation. <p> {@link GetOnlineUsersForCity#initialize(com.google.api.client.googleapis.service
       * s.AbstractGoogleClientRequest)} must be called to initialize this instance immediately after
       * invoking the constructor. </p>
       *
       * @param id
       * @since 1.13
       */
      protected GetOnlineUsersForCity(java.lang.String id) {
        super(Langeo.this, "GET", REST_PATH, null, com.appspot.id.app.langeo.model.UserCollection.class);
        this.id = com.google.api.client.util.Preconditions.checkNotNull(id, "Required parameter id must be specified.");
      }

      @Override
      public com.google.api.client.http.HttpResponse executeUsingHead() throws java.io.IOException {
        return super.executeUsingHead();
      }

      @Override
      public com.google.api.client.http.HttpRequest buildHttpRequestUsingHead() throws java.io.IOException {
        return super.buildHttpRequestUsingHead();
      }

      @Override
      public GetOnlineUsersForCity setAlt(java.lang.String alt) {
        return (GetOnlineUsersForCity) super.setAlt(alt);
      }

      @Override
      public GetOnlineUsersForCity setFields(java.lang.String fields) {
        return (GetOnlineUsersForCity) super.setFields(fields);
      }

      @Override
      public GetOnlineUsersForCity setKey(java.lang.String key) {
        return (GetOnlineUsersForCity) super.setKey(key);
      }

      @Override
      public GetOnlineUsersForCity setOauthToken(java.lang.String oauthToken) {
        return (GetOnlineUsersForCity) super.setOauthToken(oauthToken);
      }

      @Override
      public GetOnlineUsersForCity setPrettyPrint(java.lang.Boolean prettyPrint) {
        return (GetOnlineUsersForCity) super.setPrettyPrint(prettyPrint);
      }

      @Override
      public GetOnlineUsersForCity setQuotaUser(java.lang.String quotaUser) {
        return (GetOnlineUsersForCity) super.setQuotaUser(quotaUser);
      }

      @Override
      public GetOnlineUsersForCity setUserIp(java.lang.String userIp) {
        return (GetOnlineUsersForCity) super.setUserIp(userIp);
      }

      @com.google.api.client.util.Key
      private java.lang.String id;

      /**

       */
      public java.lang.String getId() {
        return id;
      }

      public GetOnlineUsersForCity setId(java.lang.String id) {
        this.id = id;
        return this;
      }

      @Override
      public GetOnlineUsersForCity set(String parameterName, Object value) {
        return (GetOnlineUsersForCity) super.set(parameterName, value);
      }
    }
    /**
     * Create a request for the method "langeoAPI.getUser".
     *
     * This request holds the parameters needed by the langeo server.  After setting any optional
     * parameters, call the {@link GetUser#execute()} method to invoke the remote operation.
     *
     * @param id
     * @return the request
     */
    public GetUser getUser(java.lang.String id) throws java.io.IOException {
      GetUser result = new GetUser(id);
      initialize(result);
      return result;
    }

    public class GetUser extends LangeoRequest<com.appspot.id.app.langeo.model.User> {

      private static final String REST_PATH = "users/{id}";

      /**
       * Create a request for the method "langeoAPI.getUser".
       *
       * This request holds the parameters needed by the the langeo server.  After setting any optional
       * parameters, call the {@link GetUser#execute()} method to invoke the remote operation. <p>
       * {@link
       * GetUser#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)} must
       * be called to initialize this instance immediately after invoking the constructor. </p>
       *
       * @param id
       * @since 1.13
       */
      protected GetUser(java.lang.String id) {
        super(Langeo.this, "GET", REST_PATH, null, com.appspot.id.app.langeo.model.User.class);
        this.id = com.google.api.client.util.Preconditions.checkNotNull(id, "Required parameter id must be specified.");
      }

      @Override
      public com.google.api.client.http.HttpResponse executeUsingHead() throws java.io.IOException {
        return super.executeUsingHead();
      }

      @Override
      public com.google.api.client.http.HttpRequest buildHttpRequestUsingHead() throws java.io.IOException {
        return super.buildHttpRequestUsingHead();
      }

      @Override
      public GetUser setAlt(java.lang.String alt) {
        return (GetUser) super.setAlt(alt);
      }

      @Override
      public GetUser setFields(java.lang.String fields) {
        return (GetUser) super.setFields(fields);
      }

      @Override
      public GetUser setKey(java.lang.String key) {
        return (GetUser) super.setKey(key);
      }

      @Override
      public GetUser setOauthToken(java.lang.String oauthToken) {
        return (GetUser) super.setOauthToken(oauthToken);
      }

      @Override
      public GetUser setPrettyPrint(java.lang.Boolean prettyPrint) {
        return (GetUser) super.setPrettyPrint(prettyPrint);
      }

      @Override
      public GetUser setQuotaUser(java.lang.String quotaUser) {
        return (GetUser) super.setQuotaUser(quotaUser);
      }

      @Override
      public GetUser setUserIp(java.lang.String userIp) {
        return (GetUser) super.setUserIp(userIp);
      }

      @com.google.api.client.util.Key
      private java.lang.String id;

      /**

       */
      public java.lang.String getId() {
        return id;
      }

      public GetUser setId(java.lang.String id) {
        this.id = id;
        return this;
      }

      @Override
      public GetUser set(String parameterName, Object value) {
        return (GetUser) super.set(parameterName, value);
      }
    }
    /**
     * Create a request for the method "langeoAPI.putUser".
     *
     * This request holds the parameters needed by the langeo server.  After setting any optional
     * parameters, call the {@link PutUser#execute()} method to invoke the remote operation.
     *
     * @param id
     * @param content the {@link com.appspot.id.app.langeo.model.User}
     * @return the request
     */
    public PutUser putUser(java.lang.String id, com.appspot.id.app.langeo.model.User content) throws java.io.IOException {
      PutUser result = new PutUser(id, content);
      initialize(result);
      return result;
    }

    public class PutUser extends LangeoRequest<Void> {

      private static final String REST_PATH = "users/{id}";

      /**
       * Create a request for the method "langeoAPI.putUser".
       *
       * This request holds the parameters needed by the the langeo server.  After setting any optional
       * parameters, call the {@link PutUser#execute()} method to invoke the remote operation. <p>
       * {@link
       * PutUser#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)} must
       * be called to initialize this instance immediately after invoking the constructor. </p>
       *
       * @param id
       * @param content the {@link com.appspot.id.app.langeo.model.User}
       * @since 1.13
       */
      protected PutUser(java.lang.String id, com.appspot.id.app.langeo.model.User content) {
        super(Langeo.this, "PUT", REST_PATH, content, Void.class);
        this.id = com.google.api.client.util.Preconditions.checkNotNull(id, "Required parameter id must be specified.");
      }

      @Override
      public PutUser setAlt(java.lang.String alt) {
        return (PutUser) super.setAlt(alt);
      }

      @Override
      public PutUser setFields(java.lang.String fields) {
        return (PutUser) super.setFields(fields);
      }

      @Override
      public PutUser setKey(java.lang.String key) {
        return (PutUser) super.setKey(key);
      }

      @Override
      public PutUser setOauthToken(java.lang.String oauthToken) {
        return (PutUser) super.setOauthToken(oauthToken);
      }

      @Override
      public PutUser setPrettyPrint(java.lang.Boolean prettyPrint) {
        return (PutUser) super.setPrettyPrint(prettyPrint);
      }

      @Override
      public PutUser setQuotaUser(java.lang.String quotaUser) {
        return (PutUser) super.setQuotaUser(quotaUser);
      }

      @Override
      public PutUser setUserIp(java.lang.String userIp) {
        return (PutUser) super.setUserIp(userIp);
      }

      @com.google.api.client.util.Key
      private java.lang.String id;

      /**

       */
      public java.lang.String getId() {
        return id;
      }

      public PutUser setId(java.lang.String id) {
        this.id = id;
        return this;
      }

      @Override
      public PutUser set(String parameterName, Object value) {
        return (PutUser) super.set(parameterName, value);
      }
    }

  }

  /**
   * Builder for {@link Langeo}.
   *
   * <p>
   * Implementation is not thread-safe.
   * </p>
   *
   * @since 1.3.0
   */
  public static final class Builder extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient.Builder {

    /**
     * Returns an instance of a new builder.
     *
     * @param transport HTTP transport, which should normally be:
     *        <ul>
     *        <li>Google App Engine:
     *        {@code com.google.api.client.extensions.appengine.http.UrlFetchTransport}</li>
     *        <li>Android: {@code newCompatibleTransport} from
     *        {@code com.google.api.client.extensions.android.http.AndroidHttp}</li>
     *        <li>Java: {@link com.google.api.client.googleapis.javanet.GoogleNetHttpTransport#newTrustedTransport()}
     *        </li>
     *        </ul>
     * @param jsonFactory JSON factory, which may be:
     *        <ul>
     *        <li>Jackson: {@code com.google.api.client.json.jackson2.JacksonFactory}</li>
     *        <li>Google GSON: {@code com.google.api.client.json.gson.GsonFactory}</li>
     *        <li>Android Honeycomb or higher:
     *        {@code com.google.api.client.extensions.android.json.AndroidJsonFactory}</li>
     *        </ul>
     * @param httpRequestInitializer HTTP request initializer or {@code null} for none
     * @since 1.7
     */
    public Builder(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
        com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
      super(
          transport,
          jsonFactory,
          DEFAULT_ROOT_URL,
          DEFAULT_SERVICE_PATH,
          httpRequestInitializer,
          false);
    }

    /** Builds a new instance of {@link Langeo}. */
    @Override
    public Langeo build() {
      return new Langeo(this);
    }

    @Override
    public Builder setRootUrl(String rootUrl) {
      return (Builder) super.setRootUrl(rootUrl);
    }

    @Override
    public Builder setServicePath(String servicePath) {
      return (Builder) super.setServicePath(servicePath);
    }

    @Override
    public Builder setHttpRequestInitializer(com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
      return (Builder) super.setHttpRequestInitializer(httpRequestInitializer);
    }

    @Override
    public Builder setApplicationName(String applicationName) {
      return (Builder) super.setApplicationName(applicationName);
    }

    @Override
    public Builder setSuppressPatternChecks(boolean suppressPatternChecks) {
      return (Builder) super.setSuppressPatternChecks(suppressPatternChecks);
    }

    @Override
    public Builder setSuppressRequiredParameterChecks(boolean suppressRequiredParameterChecks) {
      return (Builder) super.setSuppressRequiredParameterChecks(suppressRequiredParameterChecks);
    }

    @Override
    public Builder setSuppressAllChecks(boolean suppressAllChecks) {
      return (Builder) super.setSuppressAllChecks(suppressAllChecks);
    }

    /**
     * Set the {@link LangeoRequestInitializer}.
     *
     * @since 1.12
     */
    public Builder setLangeoRequestInitializer(
        LangeoRequestInitializer langeoRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(langeoRequestInitializer);
    }

    @Override
    public Builder setGoogleClientRequestInitializer(
        com.google.api.client.googleapis.services.GoogleClientRequestInitializer googleClientRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(googleClientRequestInitializer);
    }
  }
}
