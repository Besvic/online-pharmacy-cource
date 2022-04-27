package com.pharmacy.traning.model.entity;

import com.pharmacy.traning.model.pojo.Position;
import com.pharmacy.traning.model.pojo.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Besarab Victor
 * The type User.
 */
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
@ToString
//@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "user_position")
    private Position position;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "user_status")
    private UserStatus userStatus;
    @Column(name = "user_name", length = 100, nullable = false)
    private String name;
    @Column(name = "user_cash")
    private Double cash;
    @Column(name = "user_login", length = 100, nullable = false, unique = true)
    private String login;
    @Column(name = "user_password", length = 100, nullable = false)
    private String password;
    @Column(name = "user_photo", length = 100)
    private String photo;

    @OneToMany(mappedBy = "user")
    private List<Order> orderList = new ArrayList<>();

    public void userStatus(Integer userStatus) {
        switch (userStatus) {
            case 0 -> this.userStatus = UserStatus.IN_REGISTER;
            case 1 -> this.userStatus = UserStatus.ACTIVE;
            case 2 -> this.userStatus = UserStatus.DELETE;
        }

    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets position.
     *
     * @return the position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Sets position.
     *
     * @param position the position
     */
    public void setPosition(String position) {
        this.position = Position.valueOf(position.toUpperCase());
    }

    /**
     * Gets user status.
     *
     * @return the user status
     */
    public UserStatus getUserStatus() {
        return userStatus;
    }

    /**
     * Sets user status.
     *
     * @param userStatus the user status
     */
    public void setUserStatus(String userStatus) {
        this.userStatus = UserStatus.valueOf(userStatus.toUpperCase());
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets cash.
     *
     * @return the cash
     */
    public double getCash() {
        return cash;
    }

    /**
     * Sets cash.
     *
     * @param cash the cash
     */
    public void setCash(double cash) {
        this.cash = cash;
    }

    /**
     * Gets login.
     *
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * Sets login.
     *
     * @param login the login
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets photo.
     *
     * @return the photo
     */
    public String getPhoto() {
        return photo;
    }

    /**
     * Sets photo.
     *
     * @param photo the photo
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }

   /* public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        User user = (User) o;
        return id == user.id && cash == user.cash && userStatus == user.userStatus &&
                position == null ? user.position == null : position.equals(user.position)
                && name == null ? user.name == null : name.equals(user.name) &&
                login == null ? user.login == null : login.equals(user.login) &&
                password == null ? user.password == null : password.equals(user.password) &&
                photo == null ? user.photo == null : photo.equals(user.photo);
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id) + Double.hashCode(cash) + position.hashCode() + userStatus.hashCode()
                + login.hashCode() + password.hashCode() + name.hashCode() + photo.hashCode();
    }

    @Override
    public String toString() {
        return new String(new StringBuffer()
                .append("User{ id= ").append(id)
                .append(", name= ").append(name)
                .append(", cash= ").append(cash)
                .append(", login= ").append(login)
                .append(", password = ").append(password)
                .append(", status= ").append(userStatus)
                .append(", position= ").append(position)
                .append(" }"));

    }*/


    public static class UserBuilder{

        private long id;
        private String name;
        private double cash;
        private String login;
        private String password;
        private UserStatus userStatus;
        private Position position;
        private String photo;

        public UserBuilder setId(long id) {
            this.id = id;
            return this;
        }

        public UserBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public UserBuilder setCash(double cash) {
            this.cash = cash;
            return this;
        }

        public UserBuilder setLogin(String login) {
            this.login = login;
            return this;
        }

        public UserBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder setUserStatus(String userStatus) {
            this.userStatus = UserStatus.valueOf(userStatus.toUpperCase());
            return this;
        }

        public UserBuilder setPosition(String position) {
            this.position = Position.valueOf(position.toUpperCase());
            return this;
        }

        public UserBuilder setPhoto(String photo) {
            this.photo = photo;
            return this;
        }

        public User createUser() {
            return new User(id, position, userStatus, name, cash, login, password,  photo, Collections.emptyList());
        }
    }

}
