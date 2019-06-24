package amata1219.tosochu.command;

public interface Command {

	String getName();

	void onCommand(Sender sender, Args args);

}
