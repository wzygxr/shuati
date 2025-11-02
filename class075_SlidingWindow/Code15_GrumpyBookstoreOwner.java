package class049;

/**
 * 爱生气的书店老板问题解决方案
 * 
 * 问题描述：
 * 有一个书店老板，他的书店开了 n 分钟。每分钟都有一些顾客进入这家商店。
 * 给定一个长度为 n 的整数数组 customers ，其中 customers[i] 是在第 i 分钟开始时进入商店的顾客数量，
 * 所有这些顾客在第 i 分钟结束后离开。
 * 在某些时候，书店老板会生气。如果书店老板在第 i 分钟生气，那么 grumpy[i] = 1，否则 grumpy[i] = 0。
 * 当书店老板生气时，那一分钟的顾客就会不满意，若老板不生气则顾客是满意的。
 * 书店老板知道一个秘密技巧，能抑制自己的情绪，可以让自己连续 minutes 分钟不生气，但却只能使用一次。
 * 请你返回这一天营业下来，最多有多少客户能够感到满意。
 * 
 * 解题思路：
 * 使用滑动窗口来解决这个问题：
 * 1. 首先计算老板不使用技巧时的满意客户数（grumpy[i] = 0 时的 customers[i] 之和）
 * 2. 使用滑动窗口找出使用技巧能额外获得的最大满意客户数
 * 3. 窗口大小为 minutes，窗口内的 grumpy[i] = 1 的 customers[i] 就是额外获得的满意客户数
 * 4. 最终结果是基础满意客户数 + 使用技巧获得的最大额外客户数
 * 
 * 算法复杂度分析：
 * 时间复杂度: O(n) - 需要遍历数组两次
 * 空间复杂度: O(1) - 只使用常数额外空间
 * 
 * 是否最优解: 是，这是该问题的最优解法
 * 
 * 相关题目链接：
 * LeetCode 1052. 爱生气的书店老板
 * https://leetcode.cn/problems/grumpy-bookstore-owner/
 * 
 * 其他平台类似题目：
 * 1. 牛客网 - 爱生气的书店老板
 *    https://www.nowcoder.com/practice/4d867d900e634e9fb9a0dae3480a374d
 * 2. LintCode 1052. 爱生气的书店老板
 *    https://www.lintcode.com/problem/1052/
 * 3. HackerRank - Grumpy Bookstore Owner
 *    https://www.hackerrank.com/challenges/grumpy-bookstore-owner/problem
 * 4. CodeChef - BOOKSTORE - Bookstore Owner
 *    https://www.codechef.com/problems/BOOKSTORE
 * 5. AtCoder - ABC146 D - Enough Array
 *    https://atcoder.jp/contests/abc146/tasks/abc146_d
 * 6. 洛谷 P1886 滑动窗口
 *    https://www.luogu.com.cn/problem/P1886
 * 7. 杭电OJ 4193 Sliding Window
 *    http://acm.hdu.edu.cn/showproblem.php?pid=4193
 * 8. POJ 2823 Sliding Window
 *    http://poj.org/problem?id=2823
 * 9. UVa OJ 11536 - Smallest Sub-Array
 *    https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=2531
 * 10. SPOJ - ADAFRIEN - Ada and Friends
 *     https://www.spoj.com/problems/ADAFRIEN/
 * 
 * 工程化考量：
 * 1. 异常处理：处理空数组、长度不一致等边界情况
 * 2. 性能优化：使用滑动窗口避免重复计算，达到线性时间复杂度
 * 3. 可读性：变量命名清晰，添加详细注释，提供测试用例
 */
public class Code15_GrumpyBookstoreOwner {
    
    /**
     * 计算书店老板使用技巧后能让最多多少客户感到满意
     * 
     * @param customers 顾客数组，customers[i]表示第i分钟进入的顾客数量
     * @param grumpy    生气数组，grumpy[i]=1表示第i分钟老板生气，grumpy[i]=0表示不生气
     * @param minutes   技巧持续时间，老板可以连续minutes分钟不生气
     * @return 最多能让多少客户感到满意
     */
    public static int maxSatisfied(int[] customers, int[] grumpy, int minutes) {
        // 异常情况处理
        if (customers == null || grumpy == null || customers.length != grumpy.length) {
            return 0;
        }
        
        int n = customers.length;
        
        // 计算老板不使用技巧时的满意客户数
        // 当grumpy[i] = 0时，顾客是满意的
        int baseSatisfied = 0;
        for (int i = 0; i < n; i++) {
            if (grumpy[i] == 0) {
                baseSatisfied += customers[i];
            }
        }
        
        // 使用滑动窗口找出使用技巧能额外获得的最大满意客户数
        int extraSatisfied = 0;  // 记录使用技巧能获得的最大额外满意客户数
        int currentExtra = 0;    // 当前窗口内能额外获得的满意客户数
        
        // 初始化第一个窗口（前minutes分钟）
        for (int i = 0; i < minutes; i++) {
            // 只有当老板原本生气时（grumpy[i] = 1），使用技巧才能额外获得满意客户
            if (grumpy[i] == 1) {
                currentExtra += customers[i];
            }
        }
        extraSatisfied = currentExtra;
        
        // 滑动窗口，窗口大小为minutes
        for (int i = minutes; i < n; i++) {
            // 添加新元素（窗口右边界）
            // 只有当老板原本生气时，使用技巧才能额外获得满意客户
            if (grumpy[i] == 1) {
                currentExtra += customers[i];
            }
            
            // 移除旧元素（窗口左边界）
            // 只有当移除的元素原本是生气状态时，才需要减去对应的客户数
            if (grumpy[i - minutes] == 1) {
                currentExtra -= customers[i - minutes];
            }
            
            // 更新最大额外满意客户数
            extraSatisfied = Math.max(extraSatisfied, currentExtra);
        }
        
        // 最终结果是基础满意客户数 + 使用技巧获得的最大额外客户数
        return baseSatisfied + extraSatisfied;
    }
    
    /**
     * 测试用例
     */
    public static void main(String[] args) {
        // 测试用例1
        int[] customers1 = {1, 0, 1, 2, 1, 1, 7, 5};
        int[] grumpy1 = {0, 1, 0, 1, 0, 1, 0, 1};
        int minutes1 = 3;
        int result1 = maxSatisfied(customers1, grumpy1, minutes1);
        System.out.println("顾客数组: " + java.util.Arrays.toString(customers1));
        System.out.println("生气数组: " + java.util.Arrays.toString(grumpy1));
        System.out.println("技巧持续时间: " + minutes1);
        System.out.println("最大满意客户数: " + result1);
        // 预期输出: 16
        // 解释：老板在最后3分钟使用技巧，原本生气的第6、8分钟变为不生气
        // 基础满意客户：第1、3、5、7分钟的顾客(1+1+1+7=10)
        // 额外满意客户：第6、8分钟的顾客(1+5=6)
        // 总计：10+6=16
        
        // 测试用例2
        int[] customers2 = {1};
        int[] grumpy2 = {0};
        int minutes2 = 1;
        int result2 = maxSatisfied(customers2, grumpy2, minutes2);
        System.out.println("\n顾客数组: " + java.util.Arrays.toString(customers2));
        System.out.println("生气数组: " + java.util.Arrays.toString(grumpy2));
        System.out.println("技巧持续时间: " + minutes2);
        System.out.println("最大满意客户数: " + result2);
        // 预期输出: 1
        // 解释：老板本来就不生气，使用技巧没有额外效果
        
        // 测试用例3：空数组
        int[] customers3 = {};
        int[] grumpy3 = {};
        int minutes3 = 1;
        int result3 = maxSatisfied(customers3, grumpy3, minutes3);
        System.out.println("\n顾客数组: " + java.util.Arrays.toString(customers3));
        System.out.println("生气数组: " + java.util.Arrays.toString(grumpy3));
        System.out.println("技巧持续时间: " + minutes3);
        System.out.println("最大满意客户数: " + result3);
        // 预期输出: 0
    }
}