package server;

import server.command.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CommandManagerV4 implements CommandManager {

    private static final String DELIMITER = " \\| ";
    private final Map<String, Command> commands = new HashMap<>();

    public CommandManagerV4(SessionManager sessionManager) {
        commands.put("/join", new JoinCommand(sessionManager));
        commands.put("/message", new MessageCommand(sessionManager));
        commands.put("/change", new ChangeCommand(sessionManager));
        commands.put("/users", new UserCommand(sessionManager));
        commands.put("/exit", new ExitCommand());
    }


    @Override
    public void execute(String totalMessage, Session session) throws IOException {
        String[] args = totalMessage.split(DELIMITER);
        String key = args[0];

        // Null Object Pattern
        Command command = commands.get(key);
        command.execute(args, session);
    }
}
