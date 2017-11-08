package tzpace.app.catfactssample.utils.commander;

import java.util.ArrayList;
import java.util.List;

public final class CommanderImpl implements ICommander {

    private final List<ICommand> commands;

    public CommanderImpl() {
        commands = new ArrayList<>();
    }

    @Override
    public void storeCommand(ICommand _command) {
        commands.add(_command);
    }

    @Override
    public boolean hasCommands() {
        return !commands.isEmpty();
    }

    @Override
    public void executeCommands() {
        for (final ICommand command : commands) command.execute();
        commands.clear();
    }
}
