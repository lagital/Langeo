package com.github.lagital.langeo;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;

/**
  * Add your first API methods in this class, or you may create another class. In that case, please
  * update your web.xml accordingly.
 **/
@Api(
        name = "langeo",
        version = "v1",
        scopes = "https://www.googleapis.com/auth/userinfo.email"
)
public class LangeoAPI {

    @ApiMethod(httpMethod = "GET", path = "users/{id}")
    public User getUser(@Named("id") String id) {
        return null;
    }

}
