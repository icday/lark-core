package com.daiyc.lark.core.groovy;

import com.daiyc.lark.core.interaction.RetCode;
import com.daiyc.lark.core.session.Session;
import com.daiyc.lark.core.util.Helper;
import com.daiyc.lark.core.interaction.BaseInteraction;
import groovy.lang.Binding;
import org.codehaus.groovy.tools.shell.Groovysh;
import org.codehaus.groovy.tools.shell.IO;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

/**
 * @author daiyc
 */
public class GroovyInteraction extends BaseInteraction {
    private Groovysh groovysh;

    public GroovyInteraction(Session session) {
        super(session);

        init();
    }

    private void init() {
        Binding binding = initBinding();

        InputStream inputStream = session.inputStream();
        OutputStream outputStream = new PrintStream(session.outputStream());

        binding.setProperty("out", outputStream);
        IO io = new IO(inputStream, outputStream, outputStream);
        groovysh = new Groovysh(binding, io);
    }

    private Binding initBinding() {
        Binding binding = new Binding();
        List<GroovyContextVariable> vars = Helper.load(GroovyContextVariable.class);
        for (GroovyContextVariable var : vars) {
            if (var.isProperty()) {
                binding.setProperty(var.getName(), var.getValue());
            } else {
                binding.setVariable(var.getName(), var.getValue());
            }
        }
        return binding;
    }

    @Override
    public RetCode loop() {
        groovysh.run(null);
        return RetCode.EXIT;
    }
}
