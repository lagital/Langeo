package com.team.agita.langeo.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.ForbiddenException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;

import java.util.ArrayList;
import java.util.Collection;

import static com.google.api.server.spi.config.ApiMethod.HttpMethod.GET;
import static com.google.api.server.spi.config.ApiMethod.HttpMethod.POST;
import static com.google.api.server.spi.config.ApiMethod.HttpMethod.PUT;

@SuppressWarnings({"TypeMayBeWeakened", "MethodMayBeStatic", "ThrowInsideCatchBlockWhichIgnoresCaughtException"})
@Api(
        name = "langeo",
        version = "v1",
        scopes = "https://www.googleapis.com/auth/userinfo.email"
)
public class LangeoAPI {
    @ApiMethod(httpMethod = GET, path = "currentUser")
    public GetCurrentUserResponse getCurrentUser(User user) throws OAuthRequestException, NotFoundException {
        try {
            return new GetCurrentUserResponse(Langeo.getUserByEmail(getUserEmail(user)));
        } catch (Langeo.UserNotFoundByEmailException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @ApiMethod(httpMethod = PUT, path = "currentUser")
    public void putCurrentUser(User user, PutCurrentUserRequest request) throws OAuthRequestException {
        Langeo.changeOrCreateUserByEmail(getUserEmail(user), request);
    }


    @ApiMethod(httpMethod = GET, path = "meetings/{id}")
    public GetMeetingResponse getMeeting(@Named("id") long id) throws NotFoundException {
        try {
            return new GetMeetingResponse(Langeo.getMeeting(id));
        } catch (Langeo.MeetingNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @ApiMethod(httpMethod = GET, path = "meetings")
    public GetMeetingsResponse getMeetings(@Named("location") String location) throws NotFoundException {
        Collection<GetMeetingResponse> getMeetingResponses = new ArrayList<>();
        for (Langeo.Meeting meeting : Langeo.getMeetings(location)) {
            getMeetingResponses.add(new GetMeetingResponse(meeting));
        }
        return new GetMeetingsResponse(getMeetingResponses);
    }

    @ApiMethod(httpMethod = POST, path = "meetings")
    public GetMeetingResponse postMeeting(User user, PostOrPutMeetingRequest request)
            throws OAuthRequestException, NotFoundException {
        try {
            return new GetMeetingResponse(Langeo.createMeeting(getUserEmail(user), request));
        } catch (Langeo.UserNotFoundByEmailException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @ApiMethod(httpMethod = PUT, path = "meetings/{id}")
    public void putMeeting(@Named("id") long id, User user, PostOrPutMeetingRequest request)
            throws OAuthRequestException, NotFoundException, ForbiddenException {
        try {
            Langeo.changeMeeting(id, getUserEmail(user), request);
        } catch (Langeo.UserNotFoundByEmailException | Langeo.MeetingNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (Langeo.TryingToChangeMeetingThatBelongsToOtherUserException e) {
            throw new ForbiddenException(e.getMessage());
        }
    }



    public static class GetCurrentUserResponse {
        public final Long id;
        public final String email;
        public final Boolean isVisible;
        public final Langeo.UserAchievement[] achievements;
        public final Langeo.UserType type;
        public final String contactVk;
        public final String contactFacebook;

        public GetCurrentUserResponse(Langeo.User user) {
            this.id = user.getId();
            this.email = user.getEmail();
            this.isVisible = user.isVisible();
            this.achievements = user.getAchievements() != null && user.getAchievements().length > 0 ? user.getAchievements() : null;
            this.type = user.getType();
            this.contactVk = user.getContactVk();
            this.contactFacebook = user.getContactFacebook();
        }
    }

    public static class PutCurrentUserRequest implements Langeo.Changer<Langeo.MutableUser> {
        public Boolean isVisible;
        public Langeo.UserAchievement[] achievements;
        public Langeo.UserType type;
        public String contactVk;
        public String contactFacebook;

        @Override
        public void change(Langeo.MutableUser user) {
            user.setVisible(isVisible != null && isVisible);
            user.setAchievements(achievements);
            user.setType(type);
            user.setContactVk(contactVk);
            user.setContactFacebook(contactFacebook);
        }
    }


    public static class GetMeetingResponse {
        public final Long id;
        public final String name;
        public final String location;
        public final Coordinates coordinates;
        public final Long timestampFrom;
        public final Long timestampTo;
        public final Long ownerUserId;
        public final String language;

        public GetMeetingResponse(Langeo.Meeting meeting) {
            this.id = meeting.getId();
            this.name = meeting.getName();
            this.location = meeting.getLocation();
            this.coordinates = new Coordinates(meeting.getCoordinates());
            this.timestampFrom = meeting.getTimestampFrom();
            this.timestampTo = meeting.getTimestampTo();
            this.ownerUserId = meeting.getOwnerUserId();
            this.language = meeting.getLanguage();
        }
    }

    public static class GetMeetingsResponse {
        public final Collection<GetMeetingResponse> meetings;

        public GetMeetingsResponse(Collection<GetMeetingResponse> meetings) {
            this.meetings = meetings;
        }
    }

    public static class PostOrPutMeetingRequest implements Langeo.Changer<Langeo.MutableMeeting> {
        public String name;
        public String location;
        public Coordinates coordinates;
        public Long timestampFrom;
        public Long timestampTo;
        public Long ownerUserId;
        public String language;

        @Override
        public void change(Langeo.MutableMeeting meeting) {
            if (name != null) {
                meeting.setName(name);
            }
            if (location != null) {
                meeting.setLocation(location);
            }
            if (coordinates != null) {
                if (coordinates.latitude != null) {
                    meeting.getCoordinates().setLatitude(coordinates.latitude);
                }
                if (coordinates.longitude != null) {
                    meeting.getCoordinates().setLongitude(coordinates.longitude);
                }
            }
            if (timestampFrom != null) {
                meeting.setTimestampFrom(timestampFrom);
            }
            meeting.setTimestampTo(timestampTo);
            if (ownerUserId != null) {
                meeting.setOwnerUserId(ownerUserId);
            }
            if (language != null) {
                meeting.setLanguage(language);
            }
        }
    }

    public static class Coordinates {
        public Double latitude;
        public Double longitude;

        public Coordinates() {
        }

        public Coordinates(Langeo.Coordinates coordinates) {
            this.latitude = coordinates.getLatitude();
            this.longitude = coordinates.getLongitude();
        }
    }


    private static String getUserEmail(User user) throws OAuthRequestException {
        if (user == null) {
            throw new OAuthRequestException("You are not signed in");
        }
        if (user.getEmail() == null) {
            throw new OAuthRequestException("You are signed in but Google doesn't show your email");
        }
        return user.getEmail();
    }
}