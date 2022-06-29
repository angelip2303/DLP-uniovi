package ast.program;

import ast.ast_node.AbstractASTNode;
import visitor.Visitor;

import java.util.List;
import java.util.stream.Collectors;

public class Program extends AbstractASTNode {

    public List<Definition> definitions;

    public Program(int line, int column, List<Definition> definitions) {
        super(line, column);
        this.definitions = definitions;
    }

    @Override
    public String toString() {
        return "Program{" +
                "definitions=" +
                    this.definitions
                            .stream()
                            .map(Object::toString)
                            .collect(Collectors.joining(", ", "{", "}"))
                + '}';
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP param) {
        return visitor.visit(this, param);
    }

}
