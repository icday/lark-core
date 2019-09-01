package com.dyc.embed.console.server;

import com.dyc.embed.console.shell.Shell;
import io.termd.core.telnet.netty.NettyTelnetTtyBootstrap;

/**
 * @author daiyc
 */
public class TelnetServer implements Server {
    private ConsoleConfig config;

    public TelnetServer() {
        this(new ConsoleConfig());
    }

    public TelnetServer(ConsoleConfig config) {
        this.config = config;
    }

    private static void startHandler(Throwable ex) {
    }

    @Override
    public void start() {
        NettyTelnetTtyBootstrap bootstrap = new NettyTelnetTtyBootstrap();
        bootstrap
                .setHost(config.getHost())
                .setPort(config.getPort())
                .start(new Shell(config), TelnetServer::startHandler);
    }

    @Override
    public void stop() {
    }

    public static void main(String[] args) {
        new TelnetServer(new ConsoleConfig()).start();
    }
}