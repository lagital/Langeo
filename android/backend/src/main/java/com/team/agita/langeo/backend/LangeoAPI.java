package com.team.agita.langeo.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;

import static com.google.api.server.spi.config.ApiMethod.HttpMethod.GET;
import static com.google.api.server.spi.config.ApiMethod.HttpMethod.PUT;
import static com.team.agita.langeo.backend.Langeo.changeOrCreateUserByEmail;
import static com.team.agita.langeo.backend.Langeo.getUserByEmail;

@Api(
        name = "langeo",
        version = "v1",
        scopes = "https://www.googleapis.com/auth/userinfo.email"
)
public class LangeoAPI {
    @ApiMethod(httpMethod = GET, path = "currentUser")
    public GetCurrentUserResponse getCurrentUser(User user) throws OAuthRequestException, NotFoundException {
        try {
            return new GetCurrentUserResponse(getUserByEmail(getUserEmail(user)));
        } catch (Langeo.UserNotFoundByEmailException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @ApiMethod(httpMethod = PUT, path = "currentUser")
    public void putCurrentUser(User user, PutCurrentUserRequest request) throws OAuthRequestException, NotFoundException {
        changeOrCreateUserByEmail(getUserEmail(user), new PutCurrentUserRequestUserChanger(request));
    }


    /*@ApiMethod(httpMethod = GET, path = "users/{id}")
    public GetUserResponse getUser(@Named("id") String id) throws NotFoundException {
    }*/



    public static class GetCurrentUserResponse {
        public final Long id;
        public final String email;
        public final Boolean isVisible;
        public final Langeo.UserAchievement[] achievements;
        public final Langeo.UserType type;

        public GetCurrentUserResponse(Langeo.User user) {
            this.id = user.getId();
            this.email = user.getEmail();
            this.isVisible = user.isVisible();
            this.achievements = user.getAchievements() != null && user.getAchievements().length > 0 ? user.getAchievements() : null;
            this.type = user.getType();
        }
    }


    public static class PutCurrentUserRequest {
        public Boolean isVisible;
        public Langeo.UserAchievement[] achievements;
        public Langeo.UserType type;
    }


    /*public static class GetUserResponse {

    }*/



    private static String getUserEmail(User user) throws OAuthRequestException {
        if (user == null) {
            throw new OAuthRequestException("You are not signed in");
        }
        if (user.getEmail() == null) {
            throw new OAuthRequestException("You are signed in but Google doesn't show your email");
        }
        return user.getEmail();
    }


    private static class PutCurrentUserRequestUserChanger implements Langeo.Changer<Langeo.MutableUser> {
        private final PutCurrentUserRequest request;

        PutCurrentUserRequestUserChanger(PutCurrentUserRequest request) {
            this.request = request;
        }

        @Override
        public void change(Langeo.MutableUser user) {
            if (request.isVisible != null) {
                user.setVisible(request.isVisible);
            }
            if (request.achievements != null) {
                user.setAchievements(request.achievements);
            }
            if (request.type != null) {
                user.setType(request.type);
            }
        }
    }
}