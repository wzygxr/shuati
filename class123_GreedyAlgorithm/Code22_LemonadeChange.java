package class092;

/**
 * LeetCode 860. 柠檬水找零
 * 题目链接：https://leetcode.cn/problems/lemonade-change/
 * 难度：简单
 * 
 * 问题描述：
 * 在柠檬水摊上，每一杯柠檬水的售价为 5 美元。顾客排队购买你的产品，（按账单 bills 支付的顺序）一次购买一杯。
 * 每位顾客只买一杯柠檬水，然后向你付 5 美元、10 美元或 20 美元。你必须给每个顾客正确找零，也就是说净交易是每位顾客向你支付 5 美元。
 * 注意，一开始你手头没有任何零钱。
 * 如果你能给每位顾客正确找零，返回 true ，否则返回 false 。
 * 
 * 示例：
 * 输入：[5,5,5,10,20]
 * 输出：true
 * 解释：
 * 前 3 位顾客那里，我们按顺序收取 3 张 5 美元的钞票。
 * 第 4 位顾客那里，我们收取一张 10 美元的钞票，并返还 5 美元。
 * 第 5 位顾客那里，我们找还一张 10 美元的钞票和一张 5 美元的钞票。
 * 由于所有客户都得到了正确的找零，所以我们输出 true。
 * 
 * 解题思路：
 * 贪心算法
 * 1. 维护5美元和10美元的数量
 * 2. 对于每个顾客的支付：
 *    - 5美元：直接收取，无需找零
 *    - 10美元：需要找零5美元，如果没有5美元则返回false
 *    - 20美元：优先使用10美元+5美元找零，如果没有10美元则使用3张5美元
 * 3. 贪心策略：在找零20美元时，优先使用10美元+5美元，因为5美元更灵活
 * 
 * 时间复杂度：O(n) - 只需要遍历数组一次
 * 空间复杂度：O(1) - 只使用了常数级别的额外空间
 * 
 * 最优性证明：
 * 贪心策略的正确性：优先使用10美元找零可以保留更多的5美元，因为5美元可以用于找零10美元和20美元，而10美元只能用于找零20美元。
 * 
 * 工程化考量：
 * 1. 边界条件处理：空数组、单元素数组
 * 2. 异常处理：非法面值（题目保证只有5,10,20）
 * 3. 性能优化：使用基本类型，避免对象创建
 */
public class Code22_LemonadeChange {
    
    /**
     * 柠檬水找零解决方案
     * @param bills 账单数组
     * @return 是否能正确找零
     */
    public static boolean lemonadeChange(int[] bills) {
        // 边界条件处理
        if (bills == null || bills.length == 0) {
            return true; // 空数组，没有交易，返回true
        }
        
        int fiveCount = 0; // 5美元数量
        int tenCount = 0;  // 10美元数量
        
        for (int bill : bills) {
            switch (bill) {
                case 5:
                    // 收到5美元，直接收取
                    fiveCount++;
                    break;
                    
                case 10:
                    // 收到10美元，需要找零5美元
                    if (fiveCount > 0) {
                        fiveCount--;
                        tenCount++;
                    } else {
                        return false; // 没有5美元找零
                    }
                    break;
                    
                case 20:
                    // 收到20美元，优先使用10美元+5美元找零
                    if (tenCount > 0 && fiveCount > 0) {
                        tenCount--;
                        fiveCount--;
                    } 
                    // 如果没有10美元，使用3张5美元
                    else if (fiveCount >= 3) {
                        fiveCount -= 3;
                    } else {
                        return false; // 无法找零
                    }
                    break;
                    
                default:
                    // 非法面值，虽然题目保证不会出现，但工程中需要处理
                    throw new IllegalArgumentException("非法面值: " + bill);
            }
            
            // 调试信息：可以打印当前钞票数量
            // System.out.printf("收到%d美元，当前5美元:%d张，10美元:%d张%n", bill, fiveCount, tenCount);
        }
        
        return true;
    }
    
    /**
     * 优化版本：添加详细注释和调试信息
     */
    public static boolean lemonadeChangeOptimized(int[] bills) {
        if (bills == null || bills.length == 0) {
            return true;
        }
        
        int fiveCount = 0;
        int tenCount = 0;
        
        System.out.println("开始处理柠檬水找零...");
        System.out.println("账单序列: " + java.util.Arrays.toString(bills));
        
        for (int i = 0; i < bills.length; i++) {
            int bill = bills[i];
            System.out.printf("第%d位顾客支付%d美元%n", i + 1, bill);
            
            switch (bill) {
                case 5:
                    fiveCount++;
                    System.out.println("  直接收取5美元，无需找零");
                    break;
                    
                case 10:
                    if (fiveCount > 0) {
                        fiveCount--;
                        tenCount++;
                        System.out.println("  找零5美元成功");
                    } else {
                        System.out.println("  无法找零5美元，交易失败");
                        return false;
                    }
                    break;
                    
                case 20:
                    // 贪心策略：优先使用10美元+5美元
                    if (tenCount > 0 && fiveCount > 0) {
                        tenCount--;
                        fiveCount--;
                        System.out.println("  使用10美元+5美元找零成功");
                    } 
                    // 次优策略：使用3张5美元
                    else if (fiveCount >= 3) {
                        fiveCount -= 3;
                        System.out.println("  使用3张5美元找零成功");
                    } else {
                        System.out.println("  无法找零20美元，交易失败");
                        return false;
                    }
                    break;
                    
                default:
                    throw new IllegalArgumentException("非法面值: " + bill);
            }
            
            System.out.printf("  当前库存: 5美元=%d张，10美元=%d张%n%n", fiveCount, tenCount);
        }
        
        System.out.println("所有交易成功完成");
        return true;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：标准示例
        int[] bills1 = {5, 5, 5, 10, 20};
        System.out.println("=== 测试用例1 ===");
        System.out.println("账单: " + java.util.Arrays.toString(bills1));
        boolean result1 = lemonadeChange(bills1);
        boolean result1Opt = lemonadeChangeOptimized(bills1);
        System.out.println("预期结果: true, 实际结果: " + result1);
        System.out.println("优化版本结果: " + result1Opt);
        System.out.println("结果一致性: " + (result1 == result1Opt));
        System.out.println();
        
        // 测试用例2：无法找零的情况
        int[] bills2 = {5, 5, 10, 10, 20};
        System.out.println("=== 测试用例2 ===");
        System.out.println("账单: " + java.util.Arrays.toString(bills2));
        boolean result2 = lemonadeChange(bills2);
        boolean result2Opt = lemonadeChangeOptimized(bills2);
        System.out.println("预期结果: false, 实际结果: " + result2);
        System.out.println("优化版本结果: " + result2Opt);
        System.out.println("结果一致性: " + (result1 == result1Opt));
        System.out.println();
        
        // 测试用例3：边界情况 - 只有5美元
        int[] bills3 = {5, 5, 5, 5};
        System.out.println("=== 测试用例3 ===");
        System.out.println("账单: " + java.util.Arrays.toString(bills3));
        boolean result3 = lemonadeChange(bills3);
        System.out.println("预期结果: true, 实际结果: " + result3);
        System.out.println();
        
        // 测试用例4：大量20美元需要找零
        int[] bills4 = {5, 5, 5, 10, 20, 20, 20};
        System.out.println("=== 测试用例4 ===");
        System.out.println("账单: " + java.util.Arrays.toString(bills4));
        boolean result4 = lemonadeChange(bills4);
        System.out.println("预期结果: true, 实际结果: " + result4);
        System.out.println();
        
        // 测试用例5：空数组
        int[] bills5 = {};
        System.out.println("=== 测试用例5 ===");
        System.out.println("账单: " + java.util.Arrays.toString(bills5));
        boolean result5 = lemonadeChange(bills5);
        System.out.println("预期结果: true, 实际结果: " + result5);
        System.out.println();
        
        // 性能测试
        System.out.println("=== 性能测试 ===");
        int[] largeBills = new int[100000];
        for (int i = 0; i < largeBills.length; i++) {
            // 随机生成账单，但保证有足够5美元
            int rand = (int) (Math.random() * 3);
            largeBills[i] = rand == 0 ? 5 : (rand == 1 ? 10 : 20);
        }
        
        long startTime = System.currentTimeMillis();
        boolean largeResult = lemonadeChange(largeBills);
        long endTime = System.currentTimeMillis();
        System.out.println("大规模测试结果: " + largeResult);
        System.out.println("耗时: " + (endTime - startTime) + "ms");
    }
}

/*
算法深度分析：

1. 贪心策略正确性证明：
   - 对于10美元找零：必须使用5美元，没有其他选择
   - 对于20美元找零：有两种选择：10+5或5+5+5
   - 贪心选择：优先使用10+5，因为5美元更灵活
   - 正确性：保留更多的5美元可以应对更多的10美元找零需求

2. 复杂度分析：
   - 时间复杂度：O(n)，每个账单处理一次
   - 空间复杂度：O(1)，只使用两个计数器

3. 边界条件处理：
   - 空数组：返回true（没有交易）
   - 非法面值：抛出异常（虽然题目保证不会出现）
   - 极端情况：大量20美元需要找零

4. 工程化考量：

4.1 异常处理：
   - 输入验证：检查null和空数组
   - 面值验证：确保只有5,10,20三种面值
   - 错误信息：提供清晰的错误描述

4.2 性能优化：
   - 使用基本类型：避免对象创建开销
   - 减少函数调用：内联简单操作
   - 避免不必要的计算：提前返回

4.3 可读性提升：
   - 清晰的变量命名：fiveCount, tenCount
   - 详细的注释：解释每个case的逻辑
   - 调试信息：可选打印交易过程

4.4 测试覆盖：
   - 正常情况：标准示例
   - 边界情况：空数组、单元素
   - 异常情况：无法找零、非法面值
   - 性能测试：大规模数据

5. 与机器学习联系：
   - 贪心策略在强化学习的ε-贪心算法中有应用
   - 该问题可以建模为状态转移问题
   - 可以用于训练智能体学习最优找零策略

6. 实际应用扩展：
   - 可以扩展支持更多面值
   - 可以添加交易记录功能
   - 可以集成到支付系统中

7. 面试技巧：
   - 能够解释贪心策略的正确性
   - 能够处理各种边界情况
   - 能够分析时间/空间复杂度
   - 能够进行代码优化和调试

8. 跨语言实现差异：
   - Java：使用switch-case语句
   - Python：使用if-elif-else语句
   - C++：使用switch语句，但需要处理枚举类型

9. 算法调试技巧：
   - 打印每个交易后的钞票数量
   - 使用断言验证中间状态
   - 构造特殊测试用例验证边界情况

10. 从代码到产品的思考：
    - 在实际系统中，需要考虑并发安全问题
    - 可以添加交易日志和审计功能
    - 需要考虑系统故障时的数据恢复
*/