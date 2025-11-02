/**
 * 加油站
 * 
 * 题目描述：
 * 在一条环路上有 n 个加油站，其中第 i 个加油站有汽油 gas[i] 升。
 * 你有一辆油箱容量无限的汽车，从第 i 个加油站开往第 i+1 个加油站需要消耗汽油 cost[i] 升。
 * 你从其中的一个加油站出发，开始时油箱为空。
 * 如果你可以绕环路行驶一周，则返回出发时加油站的编号，否则返回 -1。
 * 
 * 来源：LeetCode 134
 * 链接：https://leetcode.cn/problems/gas-station/
 * 
 * 算法思路：
 * 使用贪心算法：
 * 1. 计算每个加油站的净收益（gas[i] - cost[i]）
 * 2. 如果总净收益小于0，则无法绕行一周
 * 3. 否则，从起点开始遍历，维护当前油量和总油量
 * 4. 如果当前油量小于0，说明从当前起点无法完成，重置起点为下一个加油站
 * 
 * 时间复杂度：O(n) - 只需要遍历一次数组
 * 空间复杂度：O(1) - 只使用常数空间
 * 
 * 关键点分析：
 * - 贪心策略：选择净收益最大的起点
 * - 数学证明：总油量必须大于等于总消耗
 * - 边界处理：环形数组的处理
 * 
 * 工程化考量：
 * - 输入验证：检查数组是否为空
 * - 性能优化：避免不必要的计算
 * - 可读性：清晰的变量命名和注释
 */
public class Code34_GasStation {
    
    /**
     * 找到可以绕行一周的加油站起点
     * 
     * @param gas 汽油数组
     * @param cost 消耗数组
     * @return 起点索引，如果无法完成返回-1
     */
    public static int canCompleteCircuit(int[] gas, int[] cost) {
        // 输入验证
        if (gas == null || cost == null || gas.length != cost.length) {
            throw new IllegalArgumentException("输入数组不能为空且长度必须相等");
        }
        if (gas.length == 0) {
            return -1;
        }
        
        int n = gas.length;
        int totalGas = 0;    // 总汽油量
        int totalCost = 0;  // 总消耗量
        int currentGas = 0; // 当前油量
        int start = 0;      // 起点索引
        
        for (int i = 0; i < n; i++) {
            totalGas += gas[i];
            totalCost += cost[i];
            currentGas += gas[i] - cost[i];
            
            // 如果当前油量小于0，说明从当前起点无法完成
            if (currentGas < 0) {
                start = i + 1; // 重置起点为下一个加油站
                currentGas = 0; // 重置当前油量
            }
        }
        
        // 如果总汽油量小于总消耗量，无法完成
        if (totalGas < totalCost) {
            return -1;
        }
        
        return start;
    }
    
    /**
     * 另一种实现：两次遍历法
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     */
    public static int canCompleteCircuitTwoPass(int[] gas, int[] cost) {
        if (gas == null || cost == null || gas.length != cost.length) {
            throw new IllegalArgumentException("输入数组不能为空且长度必须相等");
        }
        if (gas.length == 0) {
            return -1;
        }
        
        int n = gas.length;
        int total = 0;
        int current = 0;
        int start = 0;
        
        // 第一次遍历：检查总油量是否足够
        for (int i = 0; i < n; i++) {
            total += gas[i] - cost[i];
        }
        
        if (total < 0) {
            return -1;
        }
        
        // 第二次遍历：找到起点
        for (int i = 0; i < n; i++) {
            current += gas[i] - cost[i];
            if (current < 0) {
                start = i + 1;
                current = 0;
            }
        }
        
        return start;
    }
    
    /**
     * 暴力解法：检查每个起点
     * 时间复杂度：O(n^2)
     * 空间复杂度：O(1)
     */
    public static int canCompleteCircuitBruteForce(int[] gas, int[] cost) {
        if (gas == null || cost == null || gas.length != cost.length) {
            throw new IllegalArgumentException("输入数组不能为空且长度必须相等");
        }
        if (gas.length == 0) {
            return -1;
        }
        
        int n = gas.length;
        
        for (int start = 0; start < n; start++) {
            int currentGas = 0;
            boolean canComplete = true;
            
            for (int i = 0; i < n; i++) {
                int index = (start + i) % n;
                currentGas += gas[index] - cost[index];
                
                if (currentGas < 0) {
                    canComplete = false;
                    break;
                }
            }
            
            if (canComplete) {
                return start;
            }
        }
        
        return -1;
    }
    
    /**
     * 验证函数：检查起点是否正确
     */
    public static boolean validateCircuit(int[] gas, int[] cost, int start) {
        if (start == -1) {
            // 检查是否真的无法完成
            int total = 0;
            for (int i = 0; i < gas.length; i++) {
                total += gas[i] - cost[i];
            }
            return total < 0;
        }
        
        int n = gas.length;
        int currentGas = 0;
        
        for (int i = 0; i < n; i++) {
            int index = (start + i) % n;
            currentGas += gas[index] - cost[index];
            if (currentGas < 0) {
                return false;
            }
        }
        
        return true;
    }
    
    // 测试用例
    public static void main(String[] args) {
        // 测试用例1: gas = [1,2,3,4,5], cost = [3,4,5,1,2] -> 3
        int[] gas1 = {1,2,3,4,5};
        int[] cost1 = {3,4,5,1,2};
        System.out.println("测试用例1:");
        System.out.println("Gas: " + java.util.Arrays.toString(gas1));
        System.out.println("Cost: " + java.util.Arrays.toString(cost1));
        int result1 = canCompleteCircuit(gas1, cost1);
        int result2 = canCompleteCircuitTwoPass(gas1, cost1);
        int result3 = canCompleteCircuitBruteForce(gas1, cost1);
        System.out.println("方法1结果: " + result1); // 3
        System.out.println("方法2结果: " + result2); // 3
        System.out.println("方法3结果: " + result3); // 3
        System.out.println("验证: " + validateCircuit(gas1, cost1, result1));
        
        // 测试用例2: gas = [2,3,4], cost = [3,4,3] -> -1
        int[] gas2 = {2,3,4};
        int[] cost2 = {3,4,3};
        System.out.println("\n测试用例2:");
        System.out.println("Gas: " + java.util.Arrays.toString(gas2));
        System.out.println("Cost: " + java.util.Arrays.toString(cost2));
        result1 = canCompleteCircuit(gas2, cost2);
        result2 = canCompleteCircuitTwoPass(gas2, cost2);
        result3 = canCompleteCircuitBruteForce(gas2, cost2);
        System.out.println("方法1结果: " + result1); // -1
        System.out.println("方法2结果: " + result2); // -1
        System.out.println("方法3结果: " + result3); // -1
        System.out.println("验证: " + validateCircuit(gas2, cost2, result1));
        
        // 测试用例3: gas = [5,1,2,3,4], cost = [4,4,1,5,1] -> 4
        int[] gas3 = {5,1,2,3,4};
        int[] cost3 = {4,4,1,5,1};
        System.out.println("\n测试用例3:");
        System.out.println("Gas: " + java.util.Arrays.toString(gas3));
        System.out.println("Cost: " + java.util.Arrays.toString(cost3));
        result1 = canCompleteCircuit(gas3, cost3);
        result2 = canCompleteCircuitTwoPass(gas3, cost3);
        result3 = canCompleteCircuitBruteForce(gas3, cost3);
        System.out.println("方法1结果: " + result1); // 4
        System.out.println("方法2结果: " + result2); // 4
        System.out.println("方法3结果: " + result3); // 4
        System.out.println("验证: " + validateCircuit(gas3, cost3, result1));
        
        // 测试用例4: gas = [5], cost = [4] -> 0
        int[] gas4 = {5};
        int[] cost4 = {4};
        System.out.println("\n测试用例4:");
        System.out.println("Gas: " + java.util.Arrays.toString(gas4));
        System.out.println("Cost: " + java.util.Arrays.toString(cost4));
        result1 = canCompleteCircuit(gas4, cost4);
        result2 = canCompleteCircuitTwoPass(gas4, cost4);
        result3 = canCompleteCircuitBruteForce(gas4, cost4);
        System.out.println("方法1结果: " + result1); // 0
        System.out.println("方法2结果: " + result2); // 0
        System.out.println("方法3结果: " + result3); // 0
        System.out.println("验证: " + validateCircuit(gas4, cost4, result1));
        
        // 边界测试：空数组
        int[] gas5 = {};
        int[] cost5 = {};
        System.out.println("\n测试用例5:");
        System.out.println("Gas: " + java.util.Arrays.toString(gas5));
        System.out.println("Cost: " + java.util.Arrays.toString(cost5));
        result1 = canCompleteCircuit(gas5, cost5);
        result2 = canCompleteCircuitTwoPass(gas5, cost5);
        result3 = canCompleteCircuitBruteForce(gas5, cost5);
        System.out.println("方法1结果: " + result1); // -1
        System.out.println("方法2结果: " + result2); // -1
        System.out.println("方法3结果: " + result3); // -1
        System.out.println("验证: " + validateCircuit(gas5, cost5, result1));
        
        // 性能测试
        performanceTest();
    }
    
    /**
     * 性能测试方法
     */
    public static void performanceTest() {
        // 生成大规模测试数据
        int n = 10000;
        int[] gas = new int[n];
        int[] cost = new int[n];
        java.util.Random random = new java.util.Random();
        
        for (int i = 0; i < n; i++) {
            gas[i] = random.nextInt(10) + 1; // 1-10
            cost[i] = random.nextInt(10) + 1; // 1-10
        }
        
        System.out.println("\n=== 性能测试 ===");
        
        long startTime1 = System.currentTimeMillis();
        int result1 = canCompleteCircuit(gas, cost);
        long endTime1 = System.currentTimeMillis();
        System.out.println("方法1执行时间: " + (endTime1 - startTime1) + "ms");
        System.out.println("结果: " + result1);
        System.out.println("验证: " + validateCircuit(gas, cost, result1));
        
        long startTime2 = System.currentTimeMillis();
        int result2 = canCompleteCircuitTwoPass(gas, cost);
        long endTime2 = System.currentTimeMillis();
        System.out.println("方法2执行时间: " + (endTime2 - startTime2) + "ms");
        System.out.println("结果: " + result2);
        System.out.println("验证: " + validateCircuit(gas, cost, result2));
        
        long startTime3 = System.currentTimeMillis();
        int result3 = canCompleteCircuitBruteForce(gas, cost);
        long endTime3 = System.currentTimeMillis();
        System.out.println("方法3执行时间: " + (endTime3 - startTime3) + "ms");
        System.out.println("结果: " + result3);
        System.out.println("验证: " + validateCircuit(gas, cost, result3));
    }
    
    /**
     * 算法复杂度分析
     */
    public static void analyzeComplexity() {
        System.out.println("\n=== 算法复杂度分析 ===");
        System.out.println("方法1（贪心算法）:");
        System.out.println("- 时间复杂度: O(n)");
        System.out.println("  - 只需要遍历一次数组");
        System.out.println("  - 每个加油站处理一次");
        System.out.println("- 空间复杂度: O(1)");
        System.out.println("  - 只使用常数空间");
        
        System.out.println("\n方法2（两次遍历）:");
        System.out.println("- 时间复杂度: O(n)");
        System.out.println("  - 两次遍历数组");
        System.out.println("  - 总体线性时间复杂度");
        System.out.println("- 空间复杂度: O(1)");
        System.out.println("  - 只使用常数空间");
        
        System.out.println("\n方法3（暴力解法）:");
        System.out.println("- 时间复杂度: O(n^2)");
        System.out.println("  - 外层循环: O(n)");
        System.out.println("  - 内层循环: O(n)");
        System.out.println("  - 总体平方时间复杂度");
        System.out.println("- 空间复杂度: O(1)");
        System.out.println("  - 只使用常数空间");
        
        System.out.println("\n贪心策略证明:");
        System.out.println("1. 总油量必须大于等于总消耗是必要条件");
        System.out.println("2. 如果从起点i无法到达j，那么i到j之间的任何点都无法到达j");
        System.out.println("3. 数学归纳法证明贪心选择性质");
        
        System.out.println("\n工程化考量:");
        System.out.println("1. 输入验证：处理空数组和边界情况");
        System.out.println("2. 性能优化：避免不必要的计算");
        System.out.println("3. 可读性：清晰的算法逻辑和注释");
        System.out.println("4. 测试覆盖：全面的测试用例设计");
    }
}