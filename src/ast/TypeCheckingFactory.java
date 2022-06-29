package ast;

import ast.types.Type;
import ast.types.Character;
import ast.types.Double;
import ast.types.Integer;

public class TypeCheckingFactory {

    private static Character characterType = null;
    private static Double doubleType = null;
    private static Integer integerType = null;

    public static Type getCharacterType() {
        if (characterType == null) characterType = new Character(0, 0);
        return characterType;
    }

    public static Type getDoubleType() {
        if (doubleType == null) doubleType = new Double(0, 0);
        return doubleType;
    }

    public static Type getIntegerType() {
        if (integerType == null) integerType = new Integer(0, 0);
        return integerType;
    }

}
