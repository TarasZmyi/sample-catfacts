package tz.app.sample.catfacts;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.net.URL;

import tz.app.sample.catfacts.communication.CommunicationModuleImpl;
import tz.app.sample.catfacts.communication.service.ApiHelper;

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
    public final void IsServicesCreated() {
        final CommunicationModuleImpl communicationModule = new CommunicationModuleImpl(apiHelper);
        Assert.assertNotNull(communicationModule.getCatFactService());
        Assert.assertNotNull(communicationModule.getCatImgService());
    }

}
