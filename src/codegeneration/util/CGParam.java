package codegeneration.util;

public class CGParam {

    public int bytesLocals;
    public int bytesParams;
    public int bytesReturn;

    public CGParam(int bytesLocals, int bytesParams, int bytesReturn) {
        this.bytesLocals = bytesLocals;
        this.bytesParams = bytesParams;
        this.bytesReturn = bytesReturn;
    }

}
