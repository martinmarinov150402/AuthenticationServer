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

import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.InvalidArgumentsException;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.UserDoesntExistException;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.WrongPasswordException;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.UnauthorizedException;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.OnlyAdminException;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.LockedAccountException;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.InvalidSessionException;

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

    public static final int USER_DOESNT_EXIST = -404;
    public static final int UNAUTHORIZED = -401;
    public static final int WRONG_PASSWORD = -456;
    public static final int INVALID_SESSION = -457;
    public static final int ONLY_ADMIN = -850;

    public static final int LOCKED_ACCOUNT = -975;

    public static final int INVALID_ARGUMENTS = -555;
    public static final int INVALID_COMMAND = -875;
    public static UserRepository userRepository;
    public static AdminRepository adminRepository;

    public static AuditRepository auditRepository;

    static Command resolveLogin(String[] args, String ip) {
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
        command = new LoginCommand(username, password, sessionId, ip, userRepository, auditRepository);
        return command;
    }

    static Command resolveRegister(String[] args) throws InvalidArgumentsException {
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
        command = new RegisterCommand(username, password, firstName, lastName, email, userRepository);
        return command;
    }

    static Command resolveUpdate(String[] args) throws InvalidArgumentsException {
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
        command = new UpdateUserCommand(sessionId, username, firstName, lastName, email, userRepository);
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
        command = new LogoutCommand(sessionId, userRepository);
        return command;
    }

    static Command resolveResetPassword(String[] args) throws InvalidArgumentsException {
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
        command = new ResetPasswordCommand(sessionId, oldPassword, newPassword, userRepository);
        return command;
    }

    static Command resolveAddAdmin(String[] args, String ip) throws InvalidArgumentsException {
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
        command = new AddAdminCommand(sessionId, username, userRepository, auditRepository, adminRepository, ip);
        return command;
    }

    static Command resolveRemoveAdmin(String[] args) throws InvalidArgumentsException {
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
        command = new RemoveAdminCommand(sessionId, username, userRepository, adminRepository, auditRepository);
        return command;
    }

    static Command resolveDeleteUser(String[] args) throws InvalidArgumentsException {
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
        command = new DeleteUserCommand(sessionId, username, userRepository, adminRepository);
        return command;
    }

    public static Command checkCommand(String[] args, String ip) throws InvalidArgumentsException {
        if (args[0].equals("login")) {
            return resolveLogin(args, ip);
        }
        if (args[0].equals("register")) {
            return resolveRegister(args);
        }
        if (args[0].equals("update-user")) {
            return resolveUpdate(args);
        }
        if (args[0].equals("reset-password")) {
            return resolveResetPassword(args);
        }
        if (args[0].equals("logout")) {
            return resolveLogout(args);
        }
        if (args[0].equals("add-admin-user")) {
            return resolveAddAdmin(args, ip);
        }
        if (args[0].equals("remove-admin-user")) {
            return resolveRemoveAdmin(args);
        }
        if (args[0].equals("delete-user")) {
            return resolveDeleteUser(args);
        }
        return null;
    }

    private static int resolveCommand(String cmd, String ip) {
        System.out.println(cmd);
        String[] args = cmd.split(" ");

        try {
            Command command = checkCommand(args, ip);
            if (command == null) {
                return INVALID_COMMAND;
            }
            return command.execute();
        } catch (UserDoesntExistException e) {
            return USER_DOESNT_EXIST;
        } catch (WrongPasswordException e) {
            return WRONG_PASSWORD;
        } catch (InvalidSessionException e) {
            return INVALID_SESSION;
        } catch (UnauthorizedException e) {
            return UNAUTHORIZED;
        } catch (OnlyAdminException e) {
            return ONLY_ADMIN;
        } catch (LockedAccountException e) {
            return LOCKED_ACCOUNT;
        } catch (InvalidArgumentsException e) {
            return INVALID_ARGUMENTS;
        }
    }

    private static String getResponse(int res) {
        if (res == USER_DOESNT_EXIST) {
            return "User doesn't exist!";
        } else if (res == WRONG_PASSWORD) {
            return "Wrong password!";
        } else if (res == UNAUTHORIZED) {
            return "Unauthorized!";
        } else if (res == INVALID_SESSION) {
            return "Invalid session!";
        } else if (res == ONLY_ADMIN) {
            return "This command is only for admins!";
        } else if (res == LOCKED_ACCOUNT) {
            return "This account is temporarily locked!";
        } else if (res == INVALID_ARGUMENTS) {
            return "Invalid arguments!";
        } else if (res == INVALID_COMMAND) {
            return "Invalid command";
        }
        return "";
    }

    private static void configureServerSocketChannel(ServerSocketChannel serverSocketChannel, Selector selector)
            throws IOException {
        serverSocketChannel.bind(new InetSocketAddress(Config.SERVER_HOST, Config.SERVER_PORT));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private static void handleRead(SocketChannel sc, ByteBuffer buffer) throws IOException {
        buffer.clear();
        int r = sc.read(buffer);
        if (r < 0) {
            sc.close();
            return;
        }
        buffer.flip();
        String cmd = new String(buffer.array(), StandardCharsets.UTF_8);
        cmd = cmd.split("\0")[0];
        int res = resolveCommand(cmd, sc.getRemoteAddress().toString());
        buffer.clear();
        if (res < 0) {
            buffer.put(getResponse(res).getBytes(StandardCharsets.UTF_8));
        } else {
            buffer.put(Integer.toString(res).getBytes(StandardCharsets.UTF_8));
        }
        System.out.println("Sending: " + res);
        buffer.flip();
        sc.write(buffer);
    }

    private static void acceptConnection(SelectionKey key, Selector selector) throws IOException {
        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
        SocketChannel accept = sockChannel.accept();
        accept.configureBlocking(false);
        accept.register(selector, SelectionKey.OP_READ);
    }

    public static void main(String[] args) {
        userRepository = new UserRepository("users.db");
        adminRepository = new AdminRepository(userRepository);
        auditRepository = new AuditRepository();
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            Selector selector = Selector.open();
            configureServerSocketChannel(serverSocketChannel, selector);
            ByteBuffer buffer = ByteBuffer.allocate(Config.BUFFER_SIZE);
            while (true) {
                int readyChannels = selector.select();
                if (readyChannels == 0) {
                    continue;
                }
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    if (key.isReadable()) {
                        SocketChannel sc = (SocketChannel) key.channel();
                        handleRead(sc, buffer);
                    } else if (key.isAcceptable()) {
                        acceptConnection(key, selector);
                    }
                    keyIterator.remove();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("There is a problem with the server socket", e);
        }
    }
}