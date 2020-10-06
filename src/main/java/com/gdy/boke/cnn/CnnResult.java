package com.gdy.boke.cnn;

import java.util.Date;

public class CnnResult {

    private String _id;

    private String type;

    private String url;

    private String byLine;

    private String headline;

    private Date firstPublishDate;

    public Date getFirstPublishDate() {
        return firstPublishDate;
    }

    public void setFirstPublishDate(Date firstPublishDate) {
        this.firstPublishDate = firstPublishDate;
    }

    private String[] contributors;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getByLine() {
        return byLine;
    }

    public void setByLine(String byLine) {
        this.byLine = byLine;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String[] getContributors() {
        return contributors;
    }

    public void setContributors(String[] contributors) {
        this.contributors = contributors;
    }

    private String body;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
