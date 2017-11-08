package tzpace.app.catfactssample.presentation.main;

import tzpace.app.catfactssample.presentation.IBaseView;

public interface IMainView extends IBaseView {

    void onCatDataLoadSuccess();

    void onCatDataLoadFailed();

}
