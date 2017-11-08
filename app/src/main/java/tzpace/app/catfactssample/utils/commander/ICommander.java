package tzpace.app.catfactssample.utils.commander;

public interface ICommander {

    void storeCommand(final ICommand _command);

    boolean hasCommands();

    void executeCommands();

}
