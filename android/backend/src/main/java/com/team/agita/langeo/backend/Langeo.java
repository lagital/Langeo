package com.team.agita.langeo.backend;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.impl.translate.opt.joda.JodaTimeTranslators;

import static com.googlecode.objectify.ObjectifyService.factory;
import static com.googlecode.objectify.ObjectifyService.ofy;
import static java.lang.String.format;

public class Langeo {
    static {
        JodaTimeTranslators.add(factory());
        ObjectifyService.register(MutableUserImpl.class);
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


    public interface User {
        Long getId();
        String getEmail();
        boolean isVisible();
        UserAchievement[] getAchievements();
        UserType getType();
    }

    public interface MutableUser extends User {
        void setId(Long newValue);
        void setEmail(String newValue);
        void setVisible(boolean newValue);
        void setAchievements(UserAchievement... newValue);
        void setType(UserType newValue);
    }

    public enum UserType {
        STANDARD,
        TEACHER,
        LANGUAGE_SCHOOL,
    }

    public enum UserAchievement {
        TEST_ACHIEVEMENT,
    }


    public interface Changer<T> {
        void change(T t);
    }



    public static class UserNotFoundException extends Exception {
        public UserNotFoundException(String field, String value) {
            super(format("User with %s = '%s' doesn't exist", field, value));
        }
    }

    public static class UserNotFoundByEmailException extends UserNotFoundException {
        public UserNotFoundByEmailException(String value) {
            super("email", value);
        }
    }


    @Entity
    private static class MutableUserImpl implements MutableUser {
        @Id private Long id;
        @Index private String email;
        private boolean visible;
        private UserAchievement[] achievements;
        private UserType type;

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public void setId(Long newValue) {
            this.id = newValue;
        }

        @Override
        public String getEmail() {
            return email;
        }

        @Override
        public void setEmail(String newValue) {
            this.email = newValue;
        }

        @Override
        public boolean isVisible() {
            return visible;
        }

        @Override
        public void setVisible(boolean newValue) {
            this.visible = newValue;
        }

        @Override
        public UserAchievement[] getAchievements() {
            return achievements;
        }

        @Override
        public void setAchievements(UserAchievement... newValue) {
            this.achievements = newValue;
        }

        @Override
        public UserType getType() {
            return type;
        }

        @Override
        public void setType(UserType newValue) {
            this.type = newValue;
        }
    }
}
