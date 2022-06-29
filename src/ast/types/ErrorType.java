package ast.types;

import errorhandler.ErrorHandler;
import visitor.Visitor;

public class ErrorType extends AbstractType {

    public String message;

    public ErrorType(int line, int column, String message) {
        super(line, column);
        this.message = message;

        // We register the error to the ErrorHandler
        ErrorHandler.getInstance().addError(this);
    }

    @Override
    public String toString() {
        return "ErrorType{" +
                "line=" + this.getLine() +
                ", column=" + this.getColumn() +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP param) {
        return visitor.visit(this, param);
    }

    @Override
    public Type readable() {
        return this;
    }

    @Override
    public Type writable() {
        return this;
    }

    @Override
    public String getDataType() {
        return "error";
    }

    @Override
    public int numberOfBytes() {
        return 0;
    }

    @Override
    public String suffix() {
        return "";
    }

}
