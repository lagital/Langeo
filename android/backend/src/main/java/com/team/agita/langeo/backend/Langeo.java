package com.team.agita.langeo.backend;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.impl.translate.opt.joda.JodaTimeTranslators;

import java.util.Objects;

import static com.googlecode.objectify.ObjectifyService.factory;
import static com.googlecode.objectify.ObjectifyService.ofy;
import static java.lang.String.format;

// TODO: transactions
// TODO: remove double-code
// TODO: tests
public class Langeo {
    static {
        JodaTimeTranslators.add(factory());
        ObjectifyService.register(MutableUserImpl.class);
        ObjectifyService.register(MutableMeetingImpl.class);
    }


    public static User getUserByEmail(String email) throws UserNotFoundByEmailException {
        User user = ofy().load()
                .type(MutableUserImpl.class)
                .filter("email == ", email)
                .first().now();
        if (user == null) {
            throw new UserNotFoundByEmailException(email);
        }
        return user;
    }

    public static void changeOrCreateUserByEmail(String email, Changer<MutableUser> userChanger) {
        MutableUser user = ofy().load()
                .type(MutableUserImpl.class)
                .filter("email == ", email)
                .first().now();
        if (user == null) {
            user = new MutableUserImpl();
            user.setEmail(email);
        }
        userChanger.change(user);
        ofy().save().entity(user).now();
        /*ofy().transact(new Work<Void>() {
            @Override
            public Void run() {
                return null;
            }
        });*/
    }


    public static Meeting getMeeting(long id) throws MeetingNotFoundException {
        Meeting meeting = ofy().load()
                .type(MutableMeetingImpl.class)
                .id(id).now();
        if (meeting == null) {
            throw new MeetingNotFoundException(id);
        }
        return meeting;
    }

    public static Iterable<? extends Meeting> getMeetings(String location) {
        return ofy().load()
                .type(MutableMeetingImpl.class)
                .filter("location == ", location)
                .iterable();
    }

    public static Meeting createMeeting(String userEmail, Changer<MutableMeeting> meetingChanger) throws UserNotFoundByEmailException {
        User user = ofy().load()
                .type(MutableUserImpl.class)
                .filter("email == ", userEmail)
                .first().now();
        if (user == null) {
            throw new UserNotFoundByEmailException(userEmail);
        }
        MutableMeeting meeting = new MutableMeetingImpl();
        meeting.setOwnerUserId(user.getId());
        meetingChanger.change(meeting);
        ofy().save().entity(meeting).now();
        return meeting;
    }

    public static void changeMeeting(long meetingId, String userEmail, Changer<MutableMeeting> meetingChanger)
            throws UserNotFoundByEmailException, MeetingNotFoundException, TryingToChangeMeetingThatBelongsToOtherUserException {
        User user = ofy().load()
                .type(MutableUserImpl.class)
                .filter("email == ", userEmail)
                .first().now();
        if (user == null) {
            throw new UserNotFoundByEmailException(userEmail);
        }
        MutableMeeting meeting = ofy().load()
                .type(MutableMeetingImpl.class)
                .id(meetingId).now();
        if (meeting == null) {
            throw new MeetingNotFoundException(meetingId);
        }
        if (!Objects.equals(meeting.getOwnerUserId(), user.getId())) {
            throw new TryingToChangeMeetingThatBelongsToOtherUserException(meetingId, meeting.getOwnerUserId(), user.getId());
        }
        meetingChanger.change(meeting);
        ofy().save().entity(meeting).now();
    }


    public interface User {
        Long getId();
        String getEmail();
        boolean isVisible();
        UserAchievement[] getAchievements();
        UserType getType();
        String getContactVk();
        String getContactFacebook();
    }

    public interface MutableUser extends User {
        void setEmail(String email);
        void setVisible(boolean visible);
        void setAchievements(UserAchievement... achievements);
        void setType(UserType type);
        void setContactVk(String contactVk);
        void setContactFacebook(String contactFacebook);
    }

    public enum UserType {
        STANDARD,
        TEACHER,
        LANGUAGE_SCHOOL,
    }

    public enum UserAchievement {
        TEST_ACHIEVEMENT,
    }


    public interface Meeting {
        Long getId();
        String getName();
        String getLocation();
        Coordinates getCoordinates();
        Long getTimestampFrom();
        Long getTimestampTo();
        Long getOwnerUserId();
        String getLanguage();
    }

    public interface MutableMeeting extends Meeting {
        void setName(String name);
        void setLocation(String location);
        @Override MutableCoordinates getCoordinates();
        void setTimestampFrom(Long timestampFrom);
        void setTimestampTo(Long timestampTo);
        void setOwnerUserId(Long ownerUserId);
        void setLanguage(String language);
    }


    public interface Coordinates {
        Double getLatitude();
        Double getLongitude();
    }

    public interface MutableCoordinates extends Coordinates {
        void setLatitude(Double latitude);
        void setLongitude(Double longitude);
    }


    public interface Changer<T> {
        void change(T t);
    }



    public static class NotFoundException extends Exception {
        public NotFoundException(String entity, String field, String value) {
            super(format("%s with %s = '%s' doesn't exist", entity, field, value));
        }
    }

    public static class UserNotFoundException extends NotFoundException {
        public UserNotFoundException(String field, String value) {
            super("User", field, value);
        }
    }

    public static class UserNotFoundByEmailException extends UserNotFoundException {
        public UserNotFoundByEmailException(String value) {
            super("email", value);
        }
    }

    public static class MeetingNotFoundException extends NotFoundException {
        public MeetingNotFoundException(Long id) {
            super("Meeting", "id", String.valueOf(id));
        }
    }

    public static class TryingToChangeMeetingThatBelongsToOtherUserException extends Exception {
        public TryingToChangeMeetingThatBelongsToOtherUserException(Long meetingId, Long correctUserId, Long incorrectUserId) {
            super(format("You are trying to change meeting %d that belongs to user %d. You are user %d.", meetingId,
                    correctUserId, incorrectUserId));
        }
    }


    @Entity
    private static class MutableUserImpl implements MutableUser {
        @Id private Long id;
        @Index private String email;
        private boolean visible;
        private UserAchievement[] achievements;
        private UserType type;
        private String contactVk;
        private String contactFacebook;

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public String getEmail() {
            return email;
        }

        @Override
        public void setEmail(String email) {
            this.email = email;
        }

        @Override
        public boolean isVisible() {
            return visible;
        }

        @Override
        public void setVisible(boolean visible) {
            this.visible = visible;
        }

        @Override
        public UserAchievement[] getAchievements() {
            return achievements;
        }

        @Override
        public void setAchievements(UserAchievement... achievements) {
            this.achievements = achievements;
        }

        @Override
        public UserType getType() {
            return type;
        }

        @Override
        public void setType(UserType type) {
            this.type = type;
        }

        @Override
        public String getContactVk() {
            return contactVk;
        }

        @Override
        public void setContactVk(String contactVk) {
            this.contactVk = contactVk;
        }

        @Override
        public String getContactFacebook() {
            return contactFacebook;
        }

        @Override
        public void setContactFacebook(String contactFacebook) {
            this.contactFacebook = contactFacebook;
        }
    }


    @Entity
    private static class MutableMeetingImpl implements MutableMeeting {
        @Id private Long id;
        private String name;
        @Index private String location;
        private MutableCoordinatesImpl coordinates;
        private Long timestampFrom;
        private Long timestampTo;
        private Long ownerUserId;
        private String language;

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String getLocation() {
            return location;
        }

        @Override
        public void setLocation(String location) {
            this.location = location;
        }

        @Override
        public MutableCoordinates getCoordinates() {
            if (coordinates == null) {
                coordinates = new MutableCoordinatesImpl();
            }
            return coordinates;
        }

        @Override
        public Long getTimestampFrom() {
            return timestampFrom;
        }

        @Override
        public void setTimestampFrom(Long timestampFrom) {
            this.timestampFrom = timestampFrom;
        }

        @Override
        public Long getTimestampTo() {
            return timestampTo;
        }

        @Override
        public void setTimestampTo(Long timestampTo) {
            this.timestampTo = timestampTo;
        }

        @Override
        public Long getOwnerUserId() {
            return ownerUserId;
        }

        @Override
        public void setOwnerUserId(Long ownerUserId) {
            this.ownerUserId = ownerUserId;
        }

        @Override
        public String getLanguage() {
            return language;
        }

        @Override
        public void setLanguage(String language) {
            this.language = language;
        }
    }


    private static class MutableCoordinatesImpl implements MutableCoordinates {
        private Double latitude;
        private Double longitude;

        @Override
        public Double getLatitude() {
            return latitude;
        }

        @Override
        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        @Override
        public Double getLongitude() {
            return longitude;
        }

        @Override
        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }
    }
}
