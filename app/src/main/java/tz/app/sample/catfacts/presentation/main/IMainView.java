package tz.app.sample.catfacts.presentation.main;

import tz.app.sample.catfacts.presentation._base.IBaseView;

public interface IMainView extends IBaseView {

    void onCatDataLoadSuccess();

    void onCatDataLoadFailed();

}
