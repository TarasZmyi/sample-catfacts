package tzpace.app.catfactssample.communication.dto.xml.help;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@SuppressWarnings("unused")
@Root(strict = false)
public class Images {

    @ElementList
    private List<Image> images;

    public Images() {
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
