package command;

import org.codehaus.commons.compiler.CompileException;

import java.lang.reflect.InvocationTargetException;

public class Assert {

    public static void main(String[] args) {
        int x, y;
        x = 1;
        y = 2;
        assert (x == y):"x != Y";

    }
}
