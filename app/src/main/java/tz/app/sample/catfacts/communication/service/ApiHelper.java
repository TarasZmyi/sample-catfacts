package tz.app.sample.catfacts.communication.service;

import com.google.gson.GsonBuilder;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tz.app.sample.catfacts.BuildConfig;
import tz.app.sample.catfacts.communication.ApiConst;
import tz.app.sample.catfacts.communication.service.catfact.ICatFactApi;
import tz.app.sample.catfacts.communication.service.catimg.ICatImgApi;
import tz.app.sample.catfacts.utils.log.ILogger;
import tz.app.sample.catfacts.utils.log.LogManager;

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
        final Converter.Factory converterFactory = GsonConverterFactory.create(new GsonBuilder().create());

        return createApi(_address, ICatImgApi.class, converterFactory);

    }

    private <T> T createApi(final URL _address, final Class<T> _class, Converter.Factory converterFactory) {

        logger.debug(TAG, String.format("createApi, address = %s, class = %s", _address.toString(), _class.getSimpleName()));

        final OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                //.connectionSpecs(Collections.singletonList(ConnectionSpec.COMPATIBLE_TLS))
                .connectTimeout(ApiConst.CONN_TIMEOUT, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG) {
            logger.debug(TAG, "enabling HTTP logs, use filter tag: OkHttp");
            enableHttpLogging(clientBuilder, HttpLoggingInterceptor.Level.HEADERS);
            enableHttpLogging(clientBuilder, HttpLoggingInterceptor.Level.BODY);
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
