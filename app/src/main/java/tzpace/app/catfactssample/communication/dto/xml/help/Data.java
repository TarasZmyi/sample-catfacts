package tzpace.app.catfactssample.communication.dto.xml.help;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@SuppressWarnings("unused")
@Root(strict = false)
public class Data {

    @ElementList(name = "images")
    private List<Image> images;

    public Data() {
    }


    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
