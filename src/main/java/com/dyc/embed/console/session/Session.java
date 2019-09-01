package com.dyc.embed.console.session;

import com.dyc.embed.console.interaction.Interaction;
import io.termd.core.util.Vector;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author daiyc
 */
public interface Session {
    void load(Interaction interaction);

    Interaction unload();

    Interaction interaction();

    boolean close();

    Vector size();

    void send(String data);

    void send(int... data);

    void start(Interaction interaction);

    InputStream inputStream();

    OutputStream outputStream();
}
