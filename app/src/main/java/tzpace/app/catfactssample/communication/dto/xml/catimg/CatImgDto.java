package tzpace.app.catfactssample.communication.dto.xml.catimg;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.List;

import tzpace.app.catfactssample.communication.dto.BaseDto;
import tzpace.app.catfactssample.communication.dto.xml.help.Data;
import tzpace.app.catfactssample.communication.dto.xml.help.Image;

@SuppressWarnings("unused")
@Root(name = "response", strict = false)
public final class CatImgDto extends BaseDto {

    @Element(name = "data")
    private Data data;

    public void setData(Data data) {
        this.data = data;
    }

    public Data setData() {
        return data;
    }

    public List<Image> getImages() {
        return data.getImages();
    }

}
