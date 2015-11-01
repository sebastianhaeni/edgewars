package ch.sebastianhaeni.edgewars.logic.commands;

/**
 * Base command class that all command implementations inherit.
 * A command has an <code>execute()</code> method.
 */
public abstract class Command {

    /**
     * Constructor
     */
    Command() {
    }

    /**
     * Executes this command and changes the state.
     */
    public abstract void execute();
}
