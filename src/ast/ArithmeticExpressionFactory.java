package ast;

import ast.expressions.Arithmetic;
import ast.expressions.Expression;
import ast.expressions.Modulus;

public class ArithmeticExpressionFactory {

    public static Expression createArithmetic(
            int row,
            int column,
            String operator,
            Expression leftOperand,
            Expression rightOperand)
    {
        // In case the operator is the MODULUS %
        if (operator.equals("%"))
            return new Modulus(row, column, leftOperand, rightOperand);
        // In any other case: PRODUCT * or DIVISION /
        else
            return new Arithmetic(row, column, operator, leftOperand, rightOperand);
    }

}
