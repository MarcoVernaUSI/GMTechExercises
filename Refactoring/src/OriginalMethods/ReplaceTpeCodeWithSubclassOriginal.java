package OriginalMethods;

public class ReplaceTpeCodeWithSubclassOriginal {
    class Employee{
    private int _type;
    static final int ENGINEER = 0;
    static final int SALESMAN = 1;
    static final int MANAGER = 2;
    
    Employee (int type) {
        setType(type);
    }

    public int getType() {
        return _type;
    }

    public void setType(int type) {
        _type = type;
    }
    }
}
