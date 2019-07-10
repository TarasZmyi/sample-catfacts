package tzpace.app.catfactssample;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

import tzpace.app.catfactssample.communication.ICommunicationModule;
import tzpace.app.catfactssample.communication.dto.json.catfact.CatFactDto;
import tzpace.app.catfactssample.domain.catdata.CatDataModuleImpl;
import tzpace.app.catfactssample.domain.catdata.ICatDataModule;

public final class CatDataModuleTest {

    private ICatDataModule catDataModule;

    @Mock
    private ICommunicationModule.CatFactService catFactService;

    @Mock
    private ICommunicationModule.CatImgService catImgService;

    @Before
    public final void Init() {
        MockitoAnnotations.initMocks(this);
        catDataModule = new CatDataModuleImpl(catFactService, catImgService);
    }

    @Test
    public final void LoadRandomCatFact_Success() {
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                ICommunicationModule.ServiceCallback<CatFactDto> serviceCallback = invocation.getArgument(0);
                final CatFactDto catFactDto = new CatFactDto();
                final String txt = "abcdfabcdf" + System.currentTimeMillis();
                catFactDto.setFact(txt);
                catFactDto.setLength(txt.length());
                serviceCallback.onSuccess(catFactDto);
                return null;
            }
        }).when(catFactService).getRandomCatFact(ArgumentMatchers.<ICommunicationModule.ServiceCallback<CatFactDto>>any());

        final ICatDataModule.Callback callback = mock(ICatDataModule.Callback.class);
        catDataModule.loadRandomCatFact(callback);

        verify(callback, times(1)).success();
        verify(callback, never()).fail();

        Assert.assertNotNull(catDataModule.getRandomCatFact());
    }

    @Test
    public final void LoadRandomCatFact_Error() {
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                ICommunicationModule.ServiceCallback<CatFactDto> serviceCallback = invocation.getArgument(0);

                final String txt = "error" + System.currentTimeMillis();
                serviceCallback.onError(txt);
                return null;
            }
        }).when(catFactService).getRandomCatFact(ArgumentMatchers.<ICommunicationModule.ServiceCallback<CatFactDto>>any());

        final ICatDataModule.Callback callback = mock(ICatDataModule.Callback.class);
        catDataModule.loadRandomCatFact(callback);

        verify(callback, times(1)).fail();
        verify(callback, never()).success();

        Assert.assertNull(catDataModule.getRandomCatFact());
    }
}
