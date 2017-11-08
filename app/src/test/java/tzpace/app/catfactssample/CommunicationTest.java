package tzpace.app.catfactssample;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.net.URL;

import tzpace.app.catfactssample.communication.CommunicationModuleImpl;
import tzpace.app.catfactssample.communication.service.ApiHelper;

public final class CommunicationTest {

    @Mock
    private ApiHelper apiHelper;

    @Before
    public final void Init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public final void IsCatFactApiServerOnline() throws IOException {
        final URL url = new URL("https://catfact.ninja");
        url.openConnection().connect();
    }

    @Test
    public final void IsCatImgApiServerOnline() throws IOException {
        final URL url = new URL("https://thecatapi.com");
        url.openConnection().connect();
    }

    @Test
    public final void IsServicesCreated() throws IOException {
        final CommunicationModuleImpl communicationModule = new CommunicationModuleImpl(apiHelper);
        Assert.assertNotNull(communicationModule.getCatFactService());
        Assert.assertNotNull(communicationModule.getCatImgService());
    }

}
