package command;

import workers.FileInterface;

public interface CommandExpression {
    void applyFunction(FileInterface fileInterface, String from, String to, String expression);
}
