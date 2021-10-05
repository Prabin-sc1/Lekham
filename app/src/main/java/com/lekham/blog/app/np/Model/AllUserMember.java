package com.lekham.blog.app.np.Model;

public class AllUserMember {
    String name, prof, uid, url;

    public AllUserMember(String name, String prof, String uid, String url) {
        this.name = name;
        this.prof = prof;
        this.uid = uid;
        this.url = url;
    }

    public AllUserMember() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProf() {
        return prof;
    }

    public void setProf(String prof) {
        this.prof = prof;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
