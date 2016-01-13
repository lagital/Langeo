package com.team.agita.langeo.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.ObjectifyService;

import static com.google.api.server.spi.config.ApiMethod.HttpMethod.GET;
import static com.google.api.server.spi.config.ApiMethod.HttpMethod.PUT;
import static com.googlecode.objectify.ObjectifyService.ofy;

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
    static {
        ObjectifyService.register(User.class);
    }

    @ApiMethod(httpMethod = GET, path = "users/{id}")
    public User getUser(@Named("id") String id) {
        return ofy().load()
                .type(User.class)
                .id(id).now();
    }

    @ApiMethod(httpMethod = PUT, path = "users/{id}")
    public void putUser(@Named("id") String id, User user) {
        user.id = id;
        ofy().save().entity(user).now();
    }
}
