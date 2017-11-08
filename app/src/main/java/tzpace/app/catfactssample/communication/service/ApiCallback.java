package tzpace.app.catfactssample.communication.service;

import retrofit2.Call;
import retrofit2.Response;
import tzpace.app.catfactssample.communication.ICommunicationModule;
import tzpace.app.catfactssample.communication.dto.BaseDto;

public final class ApiCallback<T extends BaseDto> implements retrofit2.Callback<T> {

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
