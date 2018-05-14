package commands;

import utils.SessionRequestContent;
import utils.CommandResult;

public interface ActionCommand {
    CommandResult execute(SessionRequestContent sessionRequestContent);
}
