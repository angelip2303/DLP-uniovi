package ast.types;

import ast.program.VariableDefinition;
import visitor.Visitor;

import java.util.List;
import java.util.stream.Collectors;

public class FunctionType extends AbstractType {

    public String name;
    public Type returnType;
    public List<VariableDefinition> parameters;

    public FunctionType(int line, int column, String name, Type returnType, List<VariableDefinition> parameters) {
        super(line, column);

        this.name = name;
        this.returnType = returnType;
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "Function{" +
                "name='" + name + '\'' +
                ", returnType=" + returnType.toString() +
                ", parameters=" +
                    this.parameters
                            .stream()
                            .map(Object::toString)
                            .collect(Collectors.joining(", ", "{", "}"))
                + '}';
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP param) {
        return visitor.visit(this, param);
    }

    @Override
    public String getDataType() {
        return "function";
    }

    @Override
    public Type parenthesis(List<Type> argTypes) {
        StringBuilder message = new StringBuilder();
        VariableDefinition expectedParameter;
        Type actualParameter;

        if (argTypes.size() != parameters.size())
            message = new StringBuilder(
                    "You have to provide the exact same amount parameters as the ones of the function: " + this.name
            );
        else
            for (int i = 0; i < parameters.size(); i++) {
                expectedParameter = parameters.get(i);
                actualParameter = argTypes.get(i);

                if (!expectedParameter.getType().getClass().equals(actualParameter.getClass())) {
                    if (message.length() == 0) message = new StringBuilder("Cannot assign parameters: ");

                    // Proper formatting of the error messages
                    if (i == parameters.size() - 1)
                        message.append(
                                parenthesisErrorMessage(
                                        "%s is of type %s and cannot be assigned to type %s.",
                                        expectedParameter,
                                        actualParameter
                                )
                        );
                    else
                        message.append(
                                parenthesisErrorMessage(
                                        "%s is of type %s and cannot be assigned to type %s, ",
                                        expectedParameter,
                                        actualParameter
                                )
                        );
                }
            }

        if (message.length() == 0) return this;
        return new ErrorType(
                this.getLine(),
                this.getColumn(),
                message.toString()
        );
    }

    private String parenthesisErrorMessage(
            String message,
            VariableDefinition expectedParameter,
            Type actualParameter) {
        return String.format(
                message,
                expectedParameter.getName(),
                expectedParameter.getType().getDataType(),
                actualParameter.getDataType()
        );
    }

    @Override
    public Type writable() {
        return this;
    }

    @Override
    public int numberOfBytes() {
        return returnType.numberOfBytes();
    }

    @Override
    public String suffix(){
        return returnType.suffix();
    }

}
