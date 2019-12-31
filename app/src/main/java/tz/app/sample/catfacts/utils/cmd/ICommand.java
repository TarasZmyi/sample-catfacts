package tz.app.sample.catfacts.utils.cmd;

import java.io.Serializable;

public interface ICommand extends Serializable {

    void execute();

}