package tzpace.app.catfactssample.communication.dto.xml.help;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@SuppressWarnings("unused")
@Root
public class Image {

    @Element(name = "url")
    private String url;

    @Element(name = "id")
    private String id;

    public Image() {
    }

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
}
