package bg.sofia.uni.fmi.javacourse.authenticationserver.sever;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class MyObjectOutputStream extends ObjectOutputStream {
    MyObjectOutputStream() throws IOException {
        super();
    }

    MyObjectOutputStream(OutputStream o) throws IOException {
        super(o);
    }

    public void writeStreamHeader() throws IOException {
        return;
    }
}
