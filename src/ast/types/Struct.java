package ast.types;

import ast.program.VariableDefinition;
import visitor.Visitor;

import java.util.List;
import java.util.stream.Collectors;

public class Struct extends AbstractType {

    public List<RecordField> recordFields;

    public Struct(int line, int column, List<RecordField> recordFields) {
        super(line, column);
        this.recordFields = recordFields;
    }

    @Override
    public String toString() {
        return "Struct{" +
                "recordFields=" +
                    this.recordFields
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
        return String.format(
                "record(%s)",
                recordFields
                        .stream()
                        .map(recordField -> String.format("(%s %s) ", recordField.type.getDataType(), recordField.name))
                        .reduce("", (element, aux) -> aux + element)
                        .trim()
        );
    }

    @Override
    public Type dot(String name) {
        for (RecordField element : recordFields)
            if (element.name.equals(name)) return element.type;

        return new ErrorType(
                this.getLine(),
                this.getColumn(),
                "The struct does not have the provided field name: " + name  + "."
        );
    }

    @Override
    public int numberOfBytes() {
        return recordFields
                .stream()
                .map(recordField -> recordField.type.numberOfBytes())
                .reduce(java.lang.Integer::sum)
                .orElse(0);
    }

    public RecordField getField(String name) {
        for(var recordField: recordFields)
            if(recordField.name.equals(name)) return recordField;

        throw new IllegalStateException("There is no field for: " + name);
    }

}
