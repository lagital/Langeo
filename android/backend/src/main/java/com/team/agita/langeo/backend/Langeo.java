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
        String getContactVk();
        String getContactFacebook();
    }

    public interface MutableUser extends User {
        void setId(Long id);
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
        private String contactVk;
        private String contactFacebook;

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public void setId(Long id) {
            this.id = id;
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
}
