package tz.app.sample.catfacts.communication.service;

import retrofit2.Call;
import retrofit2.Response;

import tz.app.sample.catfacts.communication.ICommunicationModule;

public final class ApiCallback<T> implements retrofit2.Callback<T> {

    private final ICommunicationModule.ServiceCallback<T> serviceCallback;

    public ApiCallback(ICommunicationModule.ServiceCallback<T> _callback) {
        serviceCallback = _callback;
    }

    @Override
    public final void onResponse(final Call<T> _call, final Response<T> _response) {
        if (_response.isSuccessful()) {
            serviceCallback.onSuccess(_response.body());
        } else {
            serviceCallback.onError("Error code: " + _response.code());
        }
    }

    @Override
    public final void onFailure(final Call<T> _call, final Throwable _tr) {
        serviceCallback.onError("Error msg: " + _tr.getMessage());
    }

}
