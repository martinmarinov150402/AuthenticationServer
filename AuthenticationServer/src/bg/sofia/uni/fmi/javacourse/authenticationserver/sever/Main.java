package bg.sofia.uni.fmi.javacourse.authenticationserver.sever;

import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands.LoginCommand;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands.Command;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands.RegisterCommand;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands.ResetPasswordCommand;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands.UpdateUserCommand;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands.LogoutCommand;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands.admin.AddAdminCommand;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands.admin.DeleteUserCommand;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands.admin.RemoveAdminCommand;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class Main {
    public static UserRepository userRepository;
    public static AdminRepository adminRepository;

    public static AuditRepository auditRepository;

    static Command resolveLogin(String[] args) {
        Command command;
        String username = null;
        String password = null;
        int sessionId = -1;
        for (int i = 1; i < args.length; i += 2) {
            if (args[i].equals("--username")) {
                username = args[i + 1];
            } else if (args[i].equals("--password")) {
                password = args[i + 1];
            } else if (args[i].equals("--session-id")) {
                sessionId = Integer.parseInt(args[i + 1]);
            }
        }
        command = new LoginCommand(username, password, sessionId);
        return command;
    }

    static Command resolveRegister(String[] args) {
        Command command;
        String username = null;
        String password = null;
        String firstName = null;
        String lastName = null;
        String email = null;
        for (int i = 1; i < args.length; i += 2) {
            if (args[i].equals("--username")) {
                username = args[i + 1];
            } else if (args[i].equals("--password")) {
                password = args[i + 1];
            } else if (args[i].equals("--first-name")) {
                firstName = args[i + 1];
            } else if (args[i].equals("--last-name")) {
                lastName = args[i + 1];
            } else if (args[i].equals("--email")) {
                email = args[i + 1];
            }
        }
        command = new RegisterCommand(username, password, firstName, lastName, email);
        return command;
    }

    static Command resolveUpdate(String[] args) {
        Command command;
        String username = null;
        int sessionId = -1;
        String firstName = null;
        String lastName = null;
        String email = null;
        for (int i = 1; i < args.length; i += 2) {
            if (args[i].equals("--new-username")) {
                username = args[i + 1];
            } else if (args[i].equals("--session-id")) {
                sessionId = Integer.parseInt(args[i + 1]);
            } else if (args[i].equals("--new-first-name")) {
                firstName = args[i + 1];
            } else if (args[i].equals("--new-last-name")) {
                lastName = args[i + 1];
            } else if (args[i].equals("--new-email")) {
                email = args[i + 1];
            }
        }
        command = new UpdateUserCommand(sessionId, username, firstName, lastName, email);
        return command;
    }

    static Command resolveLogout(String[] args) {
        Command command;
        int sessionId = -1;
        for (int i = 1; i < args.length; i += 2) {
            if (args[i].equals("--session-id")) {
                sessionId = Integer.parseInt(args[i + 1]);
            }
        }
        command = new LogoutCommand(sessionId);
        return command;
    }

    static Command resolveResetPassword(String[] args) {
        Command command;
        int sessionId = -1;
        String oldPassword = null;
        String newPassword = null;

        for (int i = 1; i < args.length; i += 2) {
            if (args[i].equals("--session-id")) {
                sessionId = Integer.parseInt(args[i + 1]);
            } else if (args[i].equals("--old-password")) {
                oldPassword = args[i + 1];
            } else if (args[i].equals("--new-password")) {
                newPassword = args[i + 1];
            }
        }
        command = new ResetPasswordCommand(sessionId, oldPassword, newPassword);
        return command;
    }

    static Command resolveAddAdmin(String[] args) {
        Command command;
        int sessionId = -1;
        String username = null;
        for (int i = 1; i < args.length; i += 2) {
            if (args[i].equals("--session-id")) {
                sessionId = Integer.parseInt(args[i + 1]);
            } else if (args[i].equals("--username")) {
                username = args[i + 1];
            }
        }
        command = new AddAdminCommand(sessionId, username);
        return command;
    }

    static Command resolveRemoveAdmin(String[] args) {
        Command command;
        int sessionId = -1;
        String username = null;
        for (int i = 1; i < args.length; i += 2) {
            if (args[i].equals("--session-id")) {
                sessionId = Integer.parseInt(args[i + 1]);
            } else if (args[i].equals("--username")) {
                username = args[i + 1];
            }
        }
        command = new RemoveAdminCommand(sessionId, username);
        return command;
    }

    static Command resolveDeleteUser(String[] args) {
        Command command;
        int sessionId = -1;
        String username = null;
        for (int i = 1; i < args.length; i += 2) {
            if (args[i].equals("--session-id")) {
                sessionId = Integer.parseInt(args[i + 1]);
            } else if (args[i].equals("--username")) {
                username = args[i + 1];
            }
        }
        command = new DeleteUserCommand(sessionId, username);
        return command;
    }

    private static int resolveCommand(String cmd) {
        Command command = null;
        String[] args = cmd.split(" ");
        if (args[0].equals("login")) {
            command = resolveLogin(args);
        }
        if (args[0].equals("register")) {
            command = resolveRegister(args);
        }
        if (args[0].equals("update-user")) {
            command = resolveUpdate(args);
        }
        if (args[0].equals("reset-password")) {
            command = resolveResetPassword(args);
        }
        if (args[0].equals("logout")) {
            command = resolveLogout(args);
        }
        if (args[0].equals("add-admin-user")) {
            command = resolveAddAdmin(args);
        }
        if (args[0].equals("remove-admin-user")) {
            command = resolveRemoveAdmin(args);
        }
        if (args[0].equals("delete-user")) {
            command = resolveDeleteUser(args);
        }
        assert command != null;
        return command.execute();
    }

    public static void main(String[] args) {
        userRepository = new UserRepository();
        adminRepository = new AdminRepository();
        auditRepository = new AuditRepository();

        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.bind(new InetSocketAddress(Config.SERVER_HOST, Config.SERVER_PORT));
            serverSocketChannel.configureBlocking(false);
            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            ByteBuffer buffer = ByteBuffer.allocate(Config.BUFFER_SIZE);
            while (true) {
                int readyChannels = selector.select();
                if (readyChannels == 0) {
                    // select() is blocking but may still return with 0, check javadoc
                    continue;
                }
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    if (key.isReadable()) {
                        SocketChannel sc = (SocketChannel) key.channel();

                        buffer.clear();
                        int r = sc.read(buffer);
                        if (r < 0) {
                            //System.out.println("Client has closed the connection");
                            sc.close();
                            continue;
                        }
                        buffer.flip();
                        String cmd = new String(buffer.array(), StandardCharsets.UTF_8);
                        cmd = cmd.split("\0")[0];
                        int res = resolveCommand(cmd);
                        buffer.clear();
                        buffer.putInt(res);
                        System.out.println("Message sending code: " + res);
                        buffer.flip();
                        sc.write(buffer);

                    } else if (key.isAcceptable()) {
                        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
                        SocketChannel accept = sockChannel.accept();
                        accept.configureBlocking(false);
                        accept.register(selector, SelectionKey.OP_READ);
                    }

                    keyIterator.remove();
                }

            }

        } catch (IOException e) {
            throw new RuntimeException("There is a problem with the server socket", e);
        }
    }
}