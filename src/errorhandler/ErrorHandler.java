package errorhandler;

import ast.types.ErrorType;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class ErrorHandler {

    public List<ErrorType> errors;
    private static ErrorHandler errorHandler;

    private ErrorHandler() {
        this.errors = new ArrayList<>();
    }

    public static ErrorHandler getInstance() {
        if (errorHandler == null) errorHandler = new ErrorHandler();
        return errorHandler;
    }

    public boolean anyErrors() {
        return this.errors.size() > 0;
    }

    public void showErrors(PrintStream out) {
        errors.forEach(out::println);
    }

    public void addError(ErrorType error) {
        errors.add(error);
    }

}
