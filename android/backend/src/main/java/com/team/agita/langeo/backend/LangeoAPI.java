package com.team.agita.langeo.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.ForbiddenException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.impl.translate.opt.joda.JodaTimeTranslators;
import org.joda.time.DateTime;

import java.util.List;
import java.util.Objects;

import static com.google.api.server.spi.config.ApiMethod.HttpMethod.GET;
import static com.google.api.server.spi.config.ApiMethod.HttpMethod.PUT;
import static com.googlecode.objectify.ObjectifyService.factory;
import static com.googlecode.objectify.ObjectifyService.ofy;
import static java.lang.String.format;

@Api(
        name = "langeo",
        version = "v1",
        scopes = "https://www.googleapis.com/auth/userinfo.email"
)
public class LangeoAPI {
    static {
        JodaTimeTranslators.add(factory());
        ObjectifyService.register(User.class);
    }


    @ApiMethod(httpMethod = GET, path = "users/{id}")
    public User getUser(@Named("id") String id, com.google.appengine.api.users.User user) throws OAuthRequestException, NotFoundException {
        if (user == null) {
            throw new OAuthRequestException("");
        }
        User langeoUser = ofy().load()
                .type(User.class)
                .id(id)
                .now();
        if (langeoUser == null) {
            throw new NotFoundException(format("There is no user %s", user.getEmail()));
//            langeoUser = new User();
//            langeoUser.id = id;
        }
//        langeoUser.lastRequestTime = DateTime.now();
        ofy().save().entity(langeoUser).now();
        return langeoUser;
    }

    @ApiMethod(httpMethod = PUT, path = "users/{id}")
    public void putUser(@Named("id") String email, User langeoUser, com.google.appengine.api.users.User user) throws OAuthRequestException, ForbiddenException {
        if (user == null) {
            throw new OAuthRequestException("");
        }
        if (!Objects.equals(user.getEmail(), email)) {
            throw new ForbiddenException(format("You (%s) are trying to change information about other user (%s)", user.getEmail(), email));
        }
        langeoUser.id = email;
//        langeoUser.lastRequestTime = DateTime.now();
        ofy().save().entity(langeoUser).now();
    }


    @ApiMethod(httpMethod = GET, path = "cities/{id}/onlineUsers")
    public List<User> getOnlineUsersForCity(@Named("id") String cityId, com.google.appengine.api.users.User user)
            throws OAuthRequestException, NotFoundException {
        if (user == null) {
            throw new OAuthRequestException("");
        }
        User langeoUser = ofy().load()
                .type(User.class)
                .id(user.getEmail())
                .now();
        if (langeoUser != null) {
//            langeoUser.lastRequestTime = DateTime.now();
            ofy().save().entity(langeoUser).now();
        }
        return ofy().load()
                .type(User.class)
                .filter("lastRequestTime >= ", DateTime.now().minusMinutes(3))
                .filter("cityId == ", cityId)
                .list();
    }
}