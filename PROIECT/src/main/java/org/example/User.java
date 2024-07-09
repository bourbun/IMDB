package org.example;

import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;

import java.time.LocalDateTime;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class User implements Observer {
    public Information info;

    protected AccountType type;

    protected String name;

    protected String experience;

    protected List<String> notifications;

    public SortedSet<Sortable> favorites;
    public SortedSet<Sortable> contributions;

    public User() {
        favorites = new TreeSet<>();
        contributions = new TreeSet<>();
    }



    @Override
    public void update(String message) {
        notifications.add(message);
    }




    //set Information
    public void setInformation(Information info) {
        this.info = info;
    }

    public Credentials getCredentials() {
        return info.getCredentials();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }


    public void setNotifications(JSONArray notifications) {
        this.notifications = (List<String>) notifications;
    }

    public String getName() {
        return name;
    }


    public String getExperience() {
        return experience;
    }

    public void addFavoriteProduction(Production production) {
        this.favorites.add(production);
    }

    public void addFavoriteActor(Actor actor) {
        this.favorites.add(actor);
    }
    public void addContributionProduction(Production production) {
        contributions.add(production);
    }
    public void addContributionActor(Actor actor) {
        contributions.add(actor);
    }

    public abstract int compareTo(@NotNull Object o);

    public Information getInformation() {
        return info;
    }

    //get contributions
    public SortedSet<Sortable> getContributions() {
        return contributions;
    }

    public void addFavorite(Production production) {
        favorites.add(production);
        production.addObserver(this);
    }

    public void removeFavorite(Production production) {
        favorites.remove(production);
        production.removeObserver(this);
    }

    public List<String> getNotifications() {
        return notifications;
    }

    public SortedSet<Sortable> getFavorites() {
        return favorites;
    }

    public static class Information {
        private Credentials credentials;

        private String name;

        private String country;

        private String age;

        private String gender;

        private LocalDateTime birthday;

        public Information() {
            this.name = "";
            this.country = "";
            this.age = "";
            this.gender="";
            this.birthday=LocalDateTime.now();
        }

        public Object getEmail() {
            return credentials.getEmail();
        }

        public Object getPassword() {
            return credentials.getPassword();
        }

        public String getName() {
            return this.name;
        }

        public String getCountry() {
            return this.country;
        }

        public String getAge() {
            return this.age;
        }




        public static class Builder {
            private String email;
            private String password;

            private String name;
            private String country;
            private String age;
            private String gender;
            private LocalDateTime birthday;



            public Builder(String email, String password) {
                this.email = email;
                this.password = password;
            }

            public Builder name(String name) {
                this.name = name;
                return this;
            }

            public Builder country(String country) {
                this.country = country;
                return this;
            }

            public Builder age(String age) {
                this.age = age;
                return this;
            }

            public Builder gender(String gender) {
                this.gender=gender;
                return this;
            }
            public Builder birthday(LocalDateTime birthday) {
                this.birthday=birthday;
                return this;
            }


            public Information build() {
                Information info = new User.Information();
                info.credentials = new Credentials(this.email, this.password);
                info.name = this.name;
                info.country = this.country;
                info.gender = this.gender;
                info.age = this.age;
                info.birthday = this.birthday;
                return info;
            }

        }

        public Credentials getCredentials() {
            return this.credentials;
        }
    }
}