package tz.app.sample.catfacts.communication.dto.json.catimg;

import com.google.gson.annotations.SerializedName;

import tz.app.sample.catfacts.communication.dto.BaseDto;

@SuppressWarnings("unused")
public final class CatImgDto extends BaseDto {

    @SerializedName("url")
    private String url;

    @SerializedName("id")
    private String id;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CatImgDto, url = " + url + ", id = " + id;
    }
}
