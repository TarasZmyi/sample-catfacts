package tzpace.app.catfactssample.communication.service;

import com.google.gson.GsonBuilder;

import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import tzpace.app.catfactssample.BuildConfig;
import tzpace.app.catfactssample.communication.ApiConst;
import tzpace.app.catfactssample.communication.service.catfact.ICatFactApi;
import tzpace.app.catfactssample.communication.service.catimg.ICatImgApi;
import tzpace.app.catfactssample.utils.log.ILogger;
import tzpace.app.catfactssample.utils.log.LogManager;

/**
 * Retrofit service helper
 */
public final class ApiHelper {

    private final String TAG = LogManager.logObjCreation(this);
    private final ILogger logger = LogManager.getLogger();

    public ApiHelper() {
    }

    public ICatFactApi createCatFactApi(final URL _address) {
        final Converter.Factory converterFactory = GsonConverterFactory.create(new GsonBuilder().create());
        return createApi(_address, ICatFactApi.class, converterFactory);
    }

    public ICatImgApi createCatImgApi(final URL _address) {
        final Converter.Factory converterFactory = SimpleXmlConverterFactory.createNonStrict(new Persister(new AnnotationStrategy()));
        return createApi(_address, ICatImgApi.class, converterFactory);
    }

    private <T> T createApi(final URL _address, final Class<T> _class, Converter.Factory converterFactory) {

        logger.debug(TAG, String.format("createApi | address = %s | class = %s", _address.toString(), _class.getSimpleName()));

        final OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectTimeout(ApiConst.CONN_TIMEOUT, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG) {
            logger.debug(TAG, "enabling HTTP logs | use filter tag: okhttp");
            enableHttpLogging(clientBuilder, HttpLoggingInterceptor.Level.BODY);
            enableHttpLogging(clientBuilder, HttpLoggingInterceptor.Level.HEADERS);
        }

        final Retrofit retrofit = new Retrofit.Builder()
                .client(clientBuilder.build())
                .baseUrl(_address.toExternalForm())
                .addConverterFactory(converterFactory)
                .build();

        return retrofit.create(_class);
    }

    private void enableHttpLogging(final OkHttpClient.Builder _clientBuilder,
                                   final HttpLoggingInterceptor.Level _logLevel) {
        _clientBuilder.addInterceptor(new HttpLoggingInterceptor().setLevel(_logLevel));
    }

}
