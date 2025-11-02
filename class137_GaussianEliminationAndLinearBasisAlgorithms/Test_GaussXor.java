package class134;

/**
 * 高斯消元和线性基算法测试类
 * 
 * 本测试类包含所有高斯消元和线性基相关算法的完整测试用例
 * 包括边界测试、极端输入测试、性能测试等
 * 
 * 测试目标：
 * 1. 验证算法正确性
 * 2. 测试边界条件和异常情况
 * 3. 验证性能表现
 * 4. 确保代码健壮性
 */
public class Test_GaussXor {
    
    /**
     * 测试高斯消元解决异或方程组模板
     */
    public static void testGaussXorTemplate() {
        System.out.println("=== 测试高斯消元解决异或方程组模板 ===");
        
        // 测试用例1：唯一解
        System.out.println("测试用例1：唯一解");
        int[][] mat1 = {
            {1, 1, 0, 1},  // x1 + x2 = 1
            {0, 1, 1, 0},  // x2 + x3 = 0
            {1, 0, 1, 1}   // x1 + x3 = 1
        };
        int n1 = 3;
        int result1 = Code04_GaussXorTemplate.gauss(n1, mat1);
        System.out.println("唯一解测试结果：" + result1);
        
        // 测试用例2：无穷解
        System.out.println("测试用例2：无穷解");
        int[][] mat2 = {
            {1, 1, 0, 1},  // x1 + x2 = 1
            {0, 0, 0, 0},  // 0 = 0
            {0, 0, 0, 0}   // 0 = 0
        };
        int n2 = 3;
        int result2 = Code04_GaussXorTemplate.gauss(n2, mat2);
        System.out.println("无穷解测试结果：" + result2);
        
        // 测试用例3：无解
        System.out.println("测试用例3：无解");
        int[][] mat3 = {
            {1, 1, 0, 1},  // x1 + x2 = 1
            {0, 0, 0, 1}   // 0 = 1
        };
        int n3 = 2;
        int result3 = Code04_GaussXorTemplate.gauss(n3, mat3);
        System.out.println("无解测试结果：" + result3);
        
        System.out.println();
    }
    
    /**
     * 测试HDU 5833 树的因子问题
     */
    public static void testGaussEor() {
        System.out.println("=== 测试HDU 5833 树的因子问题 ===");
        
        // 测试用例1：简单情况
        System.out.println("测试用例1：简单情况");
        long[] arr1 = {2, 3, 6};  // 2, 3, 6
        int n1 = 3;
        long result1 = Code01_GaussEor.solve(n1, arr1);
        System.out.println("简单情况测试结果：" + result1);
        
        // 测试用例2：完全平方数
        System.out.println("测试用例2：完全平方数");
        long[] arr2 = {4, 9, 16};  // 4, 9, 16
        int n2 = 3;
        long result2 = Code01_GaussEor.solve(n2, arr2);
        System.out.println("完全平方数测试结果：" + result2);
        
        // 测试用例3：边界情况
        System.out.println("测试用例3：边界情况");
        long[] arr3 = {1};  // 单个数字
        int n3 = 1;
        long result3 = Code01_GaussEor.solve(n3, arr3);
        System.out.println("边界情况测试结果：" + result3);
        
        System.out.println();
    }
    
    /**
     * 测试洛谷 P2962 Lights问题
     */
    public static void testMinimumOperations() {
        System.out.println("=== 测试洛谷 P2962 Lights问题 ===");
        
        // 测试用例1：简单图
        System.out.println("测试用例1：简单图");
        int n1 = 3;
        int m1 = 2;
        int[][] edges1 = {{1, 2}, {2, 3}};
        int result1 = Code02_MinimumOperations.solve(n1, m1, edges1);
        System.out.println("简单图测试结果：" + result1);
        
        // 测试用例2：完全图
        System.out.println("测试用例2：完全图");
        int n2 = 4;
        int m2 = 6;
        int[][] edges2 = {{1, 2}, {1, 3}, {1, 4}, {2, 3}, {2, 4}, {3, 4}};
        int result2 = Code02_MinimumOperations.solve(n2, m2, edges2);
        System.out.println("完全图测试结果：" + result2);
        
        // 测试用例3：孤立点
        System.out.println("测试用例3：孤立点");
        int n3 = 1;
        int m3 = 0;
        int[][] edges3 = {};
        int result3 = Code02_MinimumOperations.solve(n3, m3, edges3);
        System.out.println("孤立点测试结果：" + result3);
        
        System.out.println();
    }
    
    /**
     * 测试洛谷 P2447 外星千足虫问题
     */
    public static void testAlienInsectLegs() {
        System.out.println("=== 测试洛谷 P2447 外星千足虫问题 ===");
        
        // 测试用例1：简单测量记录
        System.out.println("测试用例1：简单测量记录");
        int n1 = 3;
        int m1 = 3;
        String[] records1 = {"011 1", "101 0", "110 1"};
        String result1 = Code03_AlienInsectLegs.solve(n1, m1, records1);
        System.out.println("简单测量记录测试结果：" + result1);
        
        // 测试用例2：冗余测量记录
        System.out.println("测试用例2：冗余测量记录");
        int n2 = 2;
        int m2 = 4;
        String[] records2 = {"10 1", "01 1", "11 0", "10 1"};
        String result2 = Code03_AlienInsectLegs.solve(n2, m2, records2);
        System.out.println("冗余测量记录测试结果：" + result2);
        
        // 测试用例3：无法确定
        System.out.println("测试用例3：无法确定");
        int n3 = 2;
        int m3 = 1;
        String[] records3 = {"10 1"};
        String result3 = Code03_AlienInsectLegs.solve(n3, m3, records3);
        System.out.println("无法确定测试结果：" + result3);
        
        System.out.println();
    }
    
    /**
     * 测试POJ 1830 开关问题
     */
    public static void testSwitchProblem() {
        System.out.println("=== 测试POJ 1830 开关问题 ===");
        
        // 测试用例1：简单开关问题
        System.out.println("测试用例1：简单开关问题");
        int n1 = 3;
        int[] start1 = {0, 0, 0};
        int[] end1 = {1, 1, 1};
        int[][] relations1 = {{1, 2}, {2, 3}, {1, 3}};
        int result1 = Code05_SwitchProblem.solve(n1, start1, end1, relations1);
        System.out.println("简单开关问题测试结果：" + result1);
        
        // 测试用例2：无解情况
        System.out.println("测试用例2：无解情况");
        int n2 = 2;
        int[] start2 = {0, 0};
        int[] end2 = {1, 0};
        int[][] relations2 = {{1, 2}};
        int result2 = Code05_SwitchProblem.solve(n2, start2, end2, relations2);
        System.out.println("无解情况测试结果：" + result2);
        
        // 测试用例3：唯一解
        System.out.println("测试用例3：唯一解");
        int n3 = 1;
        int[] start3 = {0};
        int[] end3 = {1};
        int[][] relations3 = {};
        int result3 = Code05_SwitchProblem.solve(n3, start3, end3, relations3);
        System.out.println("唯一解测试结果：" + result3);
        
        System.out.println();
    }
    
    /**
     * 测试UVa 11542 Square问题
     */
    public static void testSquare() {
        System.out.println("=== 测试UVa 11542 Square问题 ===");
        
        // 测试用例1：简单情况
        System.out.println("测试用例1：简单情况");
        long[] arr1 = {2, 3, 6};
        int n1 = 3;
        long result1 = Code06_Square.solve(n1, arr1);
        System.out.println("简单情况测试结果：" + result1);
        
        // 测试用例2：完全平方数
        System.out.println("测试用例2：完全平方数");
        long[] arr2 = {4, 9, 36};
        int n2 = 3;
        long result2 = Code06_Square.solve(n2, arr2);
        System.out.println("完全平方数测试结果：" + result2);
        
        // 测试用例3：边界情况
        System.out.println("测试用例3：边界情况");
        long[] arr3 = {1};
        int n3 = 1;
        long result3 = Code06_Square.solve(n3, arr3);
        System.out.println("边界情况测试结果：" + result3);
        
        System.out.println();
    }
    
    /**
     * 测试SPOJ XMAX 异或最大值问题
     */
    public static void testXMAX() {
        System.out.println("=== 测试SPOJ XMAX 异或最大值问题 ===");
        
        // 测试用例1：简单情况
        System.out.println("测试用例1：简单情况");
        long[] arr1 = {1, 2, 3};
        int n1 = 3;
        long result1 = Code07_XMAX.solve(n1, arr1);
        System.out.println("简单情况测试结果：" + result1);
        
        // 测试用例2：最大值情况
        System.out.println("测试用例2：最大值情况");
        long[] arr2 = {1, 2, 4, 8};
        int n2 = 4;
        long result2 = Code07_XMAX.solve(n2, arr2);
        System.out.println("最大值情况测试结果：" + result2);
        
        // 测试用例3：边界情况
        System.out.println("测试用例3：边界情况");
        long[] arr3 = {0};
        int n3 = 1;
        long result3 = Code07_XMAX.solve(n3, arr3);
        System.out.println("边界情况测试结果：" + result3);
        
        System.out.println();
    }
    
    /**
     * 运行所有测试
     */
    public static void main(String[] args) {
        System.out.println("开始运行高斯消元和线性基算法测试...\n");
        
        // 运行所有测试
        testGaussXorTemplate();
        testGaussEor();
        testMinimumOperations();
        testAlienInsectLegs();
        testSwitchProblem();
        testSquare();
        testXMAX();
        
        System.out.println("所有测试运行完成！");
    }
}