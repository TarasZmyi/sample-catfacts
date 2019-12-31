package tz.app.sample.catfacts.utils.cmd;

public interface ICommander {

    void storeCommand(ICommand _command);

    boolean hasCommands();

    void executeCommands();

}
