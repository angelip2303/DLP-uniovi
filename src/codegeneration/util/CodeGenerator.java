package codegeneration.util;

import ast.program.Program;
import codegeneration.AddressCGVisitor;
import codegeneration.ExecuteCGVisitor;
import codegeneration.ValueCGVisitor;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CodeGenerator {

    private List<String> linesOfCode = new ArrayList<>();
    private int labelCount = 0;

    private static AddressCGVisitor addressCGVisitor;
    private static ValueCGVisitor valueCGVisitor;
    private static ExecuteCGVisitor executeCGVisitor;

    public AddressCGVisitor getAddressCGVisitor() {
        if (addressCGVisitor == null) addressCGVisitor = new AddressCGVisitor(this);
        return addressCGVisitor;
    }

    public ValueCGVisitor getValueCGVisitor() {
        if (valueCGVisitor == null) valueCGVisitor = new ValueCGVisitor(this);
        return valueCGVisitor;
    }

    public ExecuteCGVisitor getExecuteCGVisitor() {
        if (executeCGVisitor == null) executeCGVisitor = new ExecuteCGVisitor(this);
        return executeCGVisitor;
    }

    public void writeSource(String output, String input, Program program) {
        FileWriter out;

        // We execute the program so the code-lines are generated :D
        program.accept(getExecuteCGVisitor(), null);

        try {
            out = new FileWriter(output);
            var formattedInput = input.split("/");
            out.write(String.format("#source \"%s\"\n\n", formattedInput[formattedInput.length - 1]));
            for(var lineOfCode : linesOfCode) {
                out.write(lineOfCode + "\n");
            }
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(String aux) {
        linesOfCode.add(aux);
    }

    public void writeLineOfCode(String lineOfCode) {
        if (!lineOfCode.trim().isEmpty())
            write("\t" + lineOfCode);
    }

    public void writeANewLine(int line) {
        write("");
        write("#line " + line);
    }

    public void writeALabel(String label) {
        write(" " + label);
    }

    public String nextLabel() {
        // Update the number of labels generated so-far after the return has been done :D
        return "label" + labelCount++;
    }

}
