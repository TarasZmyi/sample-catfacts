package tz.app.sample.catfacts.communication.dto.json.catfact;

import com.google.gson.annotations.SerializedName;

import tz.app.sample.catfacts.communication.dto.BaseDto;

@SuppressWarnings("unused")
public final class CatFactDto extends BaseDto {

    @SerializedName("fact")
    private String fact;

    @SerializedName("length")
    private Integer length; //optional

    public final String getFact() {
        return fact;
    }

    public final void setFact(final String _fact) {
        fact = _fact;
    }

    public final Integer getLength() {
        return length;
    }

    public final void setLength(final Integer _length) {
        length = _length;
    }

    @Override
    public String toString() {
        return "CatFactDto, fact = " + fact + ", length = " + length;
    }

}
