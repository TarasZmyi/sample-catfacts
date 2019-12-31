package tz.app.sample.catfacts;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import tz.app.sample.catfacts.communication.ICommunicationModule;
import tz.app.sample.catfacts.communication.dto.json.catfact.CatFactDto;
import tz.app.sample.catfacts.domain.catdata.DataModuleImpl;
import tz.app.sample.catfacts.domain.catdata.IDataModule;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public final class CatDataModuleTest {

    private IDataModule catDataModule;

    @Mock
    private ICommunicationModule.CatFactService catFactService;

    @Mock
    private ICommunicationModule.CatImgService catImgService;

    @Before
    public final void Init() {
        MockitoAnnotations.initMocks(this);
        catDataModule = new DataModuleImpl(catFactService, catImgService);
    }

    @Test
    public final void LoadRandomCatFact_Success() {
        doAnswer((Answer<Object>) invocation -> {
            ICommunicationModule.ServiceCallback<CatFactDto> serviceCallback = invocation.getArgument(0);
            final CatFactDto catFactDto = new CatFactDto();
            final String txt = "abcdfabcdf" + System.currentTimeMillis();
            catFactDto.setFact(txt);
            catFactDto.setLength(txt.length());
            serviceCallback.onSuccess(catFactDto);
            return null;
        }).when(catFactService).getRandomCatFact(ArgumentMatchers.any());

        final IDataModule.Callback callback = mock(IDataModule.Callback.class);
        catDataModule.loadRandomCatFact(callback);

        verify(callback, times(1)).success();
        verify(callback, never()).fail();

        Assert.assertNotNull(catDataModule.getRandomCatFact());
    }

    @Test
    public final void LoadRandomCatFact_Error() {
        doAnswer((Answer<Object>) invocation -> {
            ICommunicationModule.ServiceCallback<CatFactDto> serviceCallback = invocation.getArgument(0);

            final String txt = "error" + System.currentTimeMillis();
            serviceCallback.onError(txt);
            return null;
        }).when(catFactService).getRandomCatFact(ArgumentMatchers.any());

        final IDataModule.Callback callback = mock(IDataModule.Callback.class);
        catDataModule.loadRandomCatFact(callback);

        verify(callback, times(1)).fail();
        verify(callback, never()).success();

        Assert.assertNull(catDataModule.getRandomCatFact());
    }
}
