package class091;

/**
 * 柠檬水找零
 * 
 * 题目描述：
 * 在柠檬水摊上，每一杯柠檬水的售价为 5 美元。
 * 顾客排队购买你的产品，一次购买一杯。
 * 每位顾客只买一杯柠檬水，然后向你支付 5 美元、10 美元或 20 美元。
 * 你必须给每个顾客正确找零。
 * 注意，一开始你手头没有任何零钱。
 * 如果你能给每位顾客正确找零，返回 true ，否则返回 false 。
 * 
 * 来源：LeetCode 860
 * 链接：https://leetcode.cn/problems/lemonade-change/
 * 
 * 算法思路：
 * 使用贪心算法：
 * 1. 维护两个变量：fiveCount（5美元数量）和 tenCount（10美元数量）
 * 2. 遍历每个顾客的支付金额：
 *    - 如果支付5美元：直接收下，fiveCount++
 *    - 如果支付10美元：需要找零5美元，检查是否有足够的5美元
 *    - 如果支付20美元：优先使用10美元+5美元找零（贪心策略），如果没有10美元则使用3张5美元
 * 3. 如果无法找零，返回false；否则处理完所有顾客后返回true
 * 
 * 时间复杂度：O(n) - 只需要遍历一次顾客数组
 * 空间复杂度：O(1) - 只使用常数空间存储5美元和10美元的数量
 * 
 * 关键点分析：
 * - 贪心策略：支付20美元时优先使用10美元+5美元的组合
 * - 边界处理：检查零钱是否足够
 * - 异常场景：大额支付无法找零的情况
 * 
 * 工程化考量：
 * - 输入验证：检查账单数组是否为空或包含非法面额
 * - 边界处理：处理第一个顾客支付20美元的情况
 * - 性能优化：使用基本类型而非包装类
 * - 可读性：清晰的变量命名和注释
 */
public class Code28_LemonadeChange {
    
    /**
     * 判断是否能给所有顾客正确找零
     * 
     * @param bills 顾客支付的账单数组
     * @return 是否能正确找零
     * @throws IllegalArgumentException 如果账单包含非法面额
     */
    public static boolean lemonadeChange(int[] bills) {
        // 输入验证
        if (bills == null) {
            throw new IllegalArgumentException("账单数组不能为null");
        }
        
        // 特殊情况：空数组
        if (bills.length == 0) {
            return true;
        }
        
        int fiveCount = 0; // 5美元数量
        int tenCount = 0;  // 10美元数量
        
        for (int bill : bills) {
            // 验证账单面额合法性
            if (bill != 5 && bill != 10 && bill != 20) {
                throw new IllegalArgumentException("非法账单面额: " + bill + "，只支持5、10、20美元");
            }
            
            switch (bill) {
                case 5:
                    // 支付5美元，直接收下
                    fiveCount++;
                    break;
                    
                case 10:
                    // 支付10美元，需要找零5美元
                    if (fiveCount > 0) {
                        fiveCount--;
                        tenCount++;
                    } else {
                        // 没有5美元找零
                        return false;
                    }
                    break;
                    
                case 20:
                    // 支付20美元，需要找零15美元
                    // 贪心策略：优先使用10美元+5美元的组合
                    if (tenCount > 0 && fiveCount > 0) {
                        tenCount--;
                        fiveCount--;
                    } else if (fiveCount >= 3) {
                        // 如果没有10美元，使用3张5美元
                        fiveCount -= 3;
                    } else {
                        // 无法找零
                        return false;
                    }
                    break;
            }
            
            // 调试信息：打印当前零钱状态（实际工程中可移除）
            // System.out.println("处理账单 " + bill + " 后: 5美元=" + fiveCount + ", 10美元=" + tenCount);
        }
        
        return true;
    }
    
    /**
     * 另一种实现方式：使用更详细的错误信息
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     */
    public static boolean lemonadeChangeWithDetails(int[] bills) {
        if (bills == null || bills.length == 0) {
            return true;
        }
        
        int five = 0, ten = 0;
        
        for (int i = 0; i < bills.length; i++) {
            int bill = bills[i];
            
            // 验证输入
            if (bill != 5 && bill != 10 && bill != 20) {
                System.err.println("错误：第" + (i+1) + "位顾客支付了非法面额 " + bill);
                return false;
            }
            
            if (bill == 5) {
                five++;
            } else if (bill == 10) {
                if (five == 0) {
                    System.err.println("错误：第" + (i+1) + "位顾客支付10美元，但无法找零5美元");
                    return false;
                }
                five--;
                ten++;
            } else { // bill == 20
                if (ten > 0 && five > 0) {
                    ten--;
                    five--;
                } else if (five >= 3) {
                    five -= 3;
                } else {
                    System.err.println("错误：第" + (i+1) + "位顾客支付20美元，但无法找零15美元");
                    return false;
                }
            }
        }
        
        return true;
    }
    
    // 测试用例
    public static void main(String[] args) {
        // 测试用例1: [5,5,5,10,20] -> true
        int[] bills1 = {5, 5, 5, 10, 20};
        System.out.println("测试用例1: " + java.util.Arrays.toString(bills1));
        System.out.println("结果: " + lemonadeChange(bills1)); // 期望: true
        
        // 测试用例2: [5,5,10,10,20] -> false
        int[] bills2 = {5, 5, 10, 10, 20};
        System.out.println("\n测试用例2: " + java.util.Arrays.toString(bills2));
        System.out.println("结果: " + lemonadeChange(bills2)); // 期望: false
        
        // 测试用例3: [5,5,10] -> true
        int[] bills3 = {5, 5, 10};
        System.out.println("\n测试用例3: " + java.util.Arrays.toString(bills3));
        System.out.println("结果: " + lemonadeChange(bills3)); // 期望: true
        
        // 测试用例4: [10,10] -> false (第一个顾客支付10美元就无法找零)
        int[] bills4 = {10, 10};
        System.out.println("\n测试用例4: " + java.util.Arrays.toString(bills4));
        System.out.println("结果: " + lemonadeChange(bills4)); // 期望: false
        
        // 测试用例5: [5,5,10,10,5,20,5,10,5,5] -> true
        int[] bills5 = {5,5,10,10,5,20,5,10,5,5};
        System.out.println("\n测试用例5: " + java.util.Arrays.toString(bills5));
        System.out.println("结果: " + lemonadeChange(bills5)); // 期望: true
        
        // 测试用例6: 空数组 -> true
        int[] bills6 = {};
        System.out.println("\n测试用例6: " + java.util.Arrays.toString(bills6));
        System.out.println("结果: " + lemonadeChange(bills6)); // 期望: true
        
        // 边界测试：单个5美元
        int[] bills7 = {5};
        System.out.println("\n测试用例7: " + java.util.Arrays.toString(bills7));
        System.out.println("结果: " + lemonadeChange(bills7)); // 期望: true
        
        // 异常测试：非法面额
        try {
            int[] bills8 = {5, 15, 10};
            System.out.println("\n测试用例8: " + java.util.Arrays.toString(bills8));
            System.out.println("结果: " + lemonadeChange(bills8));
        } catch (IllegalArgumentException e) {
            System.out.println("异常测试通过: " + e.getMessage());
        }
    }
    
    /**
     * 性能测试方法
     */
    public static void performanceTest() {
        // 生成大规模测试数据
        int[] largeBills = new int[1000000];
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < largeBills.length; i++) {
            // 随机生成5、10、20美元，比例大致为6:3:1
            int rand = random.nextInt(10);
            if (rand < 6) {
                largeBills[i] = 5;
            } else if (rand < 9) {
                largeBills[i] = 10;
            } else {
                largeBills[i] = 20;
            }
        }
        
        long startTime = System.currentTimeMillis();
        boolean result = lemonadeChange(largeBills);
        long endTime = System.currentTimeMillis();
        
        System.out.println("大规模测试结果: " + result);
        System.out.println("执行时间: " + (endTime - startTime) + "ms");
        System.out.println("数据规模: " + largeBills.length + " 个顾客");
    }
    
    /**
     * 算法正确性验证
     */
    public static void correctnessTest() {
        System.out.println("\n=== 算法正确性验证 ===");
        
        // 验证贪心策略的正确性
        // 场景：支付20美元时，优先使用10美元+5美元 vs 使用3张5美元
        int[] test1 = {5, 5, 10, 20}; // 应该成功
        int[] test2 = {5, 5, 5, 20};  // 应该成功
        int[] test3 = {5, 10, 10, 20}; // 应该失败（只有一个5美元）
        
        System.out.println("测试1 [5,5,10,20]: " + lemonadeChange(test1)); // true
        System.out.println("测试2 [5,5,5,20]: " + lemonadeChange(test2));  // true
        System.out.println("测试3 [5,10,10,20]: " + lemonadeChange(test3)); // false
        
        // 验证贪心策略的必要性
        // 如果不使用贪心策略（先尝试3张5美元），以下测试会失败
        int[] test4 = {5, 5, 10, 10, 20}; // 贪心策略能成功
        System.out.println("测试4 [5,5,10,10,20]: " + lemonadeChange(test4)); // true
    }
}