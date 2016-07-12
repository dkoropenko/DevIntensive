package com.softdesign.devintensive.data.network.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smalew on 12.07.16.
 */
public class LoginModelRes {
    @SerializedName("success")
    @Expose
    public Boolean success;
    @SerializedName("data")
    @Expose
    public Data data;

    public Data getData() {
        return data;
    }

    public class Contacts {

        @SerializedName("vk")
        @Expose
        public String vk;
        @SerializedName("phone")
        @Expose
        public String phone;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("updated")
        @Expose
        public String updated;

        public String getVk() {
            return vk;
        }

        public String getPhone() {
            return phone;
        }

        public String getEmail() {
            return email;
        }
    }
    public class Data {

        @SerializedName("_id")
        @Expose
        public String id;
        @SerializedName("first_name")
        @Expose
        public String firstName;
        @SerializedName("second_name")
        @Expose
        public String secondName;
        @SerializedName("__v")
        @Expose
        public Integer v;
        @SerializedName("repositories")
        @Expose
        public Repositories repositories;
        @SerializedName("contacts")
        @Expose
        public Contacts contacts;
        @SerializedName("profileValues")
        @Expose
        public ProfileValues profileValues;
        @SerializedName("publicInfo")
        @Expose
        public PublicInfo publicInfo;
        @SerializedName("specialization")
        @Expose
        public String specialization;
        @SerializedName("role")
        @Expose
        public String role;
        @SerializedName("updated")
        @Expose
        public String updated;

        public String getFirstName() {
            return firstName;
        }

        public String getSecondName() {
            return secondName;
        }

        public Repositories getRepositories() {
            return repositories;
        }

        public Contacts getContacts() {
            return contacts;
        }

        public PublicInfo getPublicInfo() {
            return publicInfo;
        }
    }

    public class ProfileValues {

        @SerializedName("homeTask")
        @Expose
        public Integer homeTask;
        @SerializedName("projects")
        @Expose
        public Integer projects;
        @SerializedName("linesCode")
        @Expose
        public Integer linesCode;
        @SerializedName("rait")
        @Expose
        public Integer rait;
        @SerializedName("updated")
        @Expose
        public String updated;

        public Integer getProjects() {
            return projects;
        }

        public Integer getLinesCode() {
            return linesCode;
        }

        public Integer getRait() {
            return rait;
        }
    }

    public class PublicInfo {

        @SerializedName("bio")
        @Expose
        public String bio;
        @SerializedName("avatar")
        @Expose
        public String avatar;
        @SerializedName("photo")
        @Expose
        public String photo;
        @SerializedName("updated")
        @Expose
        public String updated;

        public String getBio() {
            return bio;
        }

        public String getAvatar() {
            return avatar;
        }

        public String getPhoto() {
            return photo;
        }
    }

    public class Repo {

        @SerializedName("_id")
        @Expose
        public String id;
        @SerializedName("git")
        @Expose
        public String git;
        @SerializedName("title")
        @Expose
        public String title;

        public String getGit() {
            return git;
        }
    }

    public class Repositories {

        @SerializedName("repo")
        @Expose
        public List<Repo> repo = new ArrayList<Repo>();
        @SerializedName("updated")
        @Expose
        public String updated;

        public List<Repo> getRepo() {
            return repo;
        }
    }
}
