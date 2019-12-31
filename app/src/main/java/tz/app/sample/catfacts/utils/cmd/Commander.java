package tz.app.sample.catfacts.utils.cmd;

import java.util.ArrayList;
import java.util.List;

public final class Commander implements ICommander {

    private final List<ICommand> commands;

    public Commander() {
        commands = new ArrayList<>();
    }

    @Override
    public final void storeCommand(final ICommand _command) {
        commands.add(_command);
    }

    @Override
    public final boolean hasCommands() {
        return !commands.isEmpty();
    }

    @Override
    public final void executeCommands() {
        for (final ICommand command : commands) command.execute();
        commands.clear();
    }
}
