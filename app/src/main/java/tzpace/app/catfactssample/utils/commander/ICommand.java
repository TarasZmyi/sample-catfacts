package tzpace.app.catfactssample.utils.commander;

import java.io.Serializable;

public interface ICommand extends Serializable {

    void execute();

}