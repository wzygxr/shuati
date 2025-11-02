package class039;

public class TestOriginalCode {
    public static void main(String[] args) {
        // 测试Code01_BasicCalculatorIII
        System.out.println("=== Code01_BasicCalculatorIII ===");
        String s1 = "2*(5+5*2)/3+(6/2+8)";
        System.out.println("输入: " + s1);
        System.out.println("输出: " + Code01_BasicCalculatorIII.calculate(s1));
        System.out.println("期望: 21\n");
        
        // 测试Code02_DecodeString
        System.out.println("=== Code02_DecodeString ===");
        String s2 = "3[a2[c]]";
        System.out.println("输入: " + s2);
        System.out.println("输出: " + Code02_DecodeString.decodeString(s2));
        System.out.println("期望: accaccacc\n");
        
        // 测试Code03_NumberOfAtoms
        System.out.println("=== Code03_NumberOfAtoms ===");
        String s3 = "Mg(OH)2";
        System.out.println("输入: " + s3);
        System.out.println("输出: " + Code03_NumberOfAtoms.countOfAtoms(s3));
        System.out.println("期望: H2MgO2\n");
    }
}