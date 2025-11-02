package class093;

import java.util.Arrays;

/**
 * 分发饼干（Assign Cookies）
 * 题目来源：LeetCode 455
 * 题目链接：https://leetcode.cn/problems/assign-cookies/
 * 
 * 问题描述：
 * 假设你是一位很棒的家长，想要给你的孩子们一些小饼干。每个孩子最多只能给一块饼干。
 * 对每个孩子i，都有一个胃口值g[i]，这是能让孩子们满足胃口的饼干的最小尺寸；
 * 对每个饼干j，都有一个尺寸s[j]。如果s[j] >= g[i]，我们可以将这个饼干j分配给孩子i，
 * 这个孩子会得到满足。你的目标是尽可能满足越多数量的孩子，并输出这个最大数值。
 * 
 * 算法思路：
 * 使用贪心策略，将胃口最小的孩子分配给能满足他的最小饼干，这样可以最大化满足的孩子数量。
 * 具体步骤：
 * 1. 将孩子的胃口数组g和饼干尺寸数组s进行排序
 * 2. 使用两个指针分别遍历g和s数组
 * 3. 如果当前饼干能满足当前孩子，两个指针都向后移动
 * 4. 否则，只移动饼干指针，寻找更大的饼干
 * 
 * 时间复杂度：O(n log n + m log m)，其中n是孩子数量，m是饼干数量，主要是排序的时间复杂度
 * 空间复杂度：O(log n + log m)，排序所需的额外空间
 * 
 * 是否最优解：是。贪心策略在此问题中能得到最优解。
 * 
 * 适用场景：
 * 1. 资源分配问题，将有限资源分配给多个需求者
 * 2. 匹配问题，需要最大化匹配数量
 * 
 * 异常处理：
 * 1. 处理空数组情况
 * 2. 处理数组长度为0的边界情况
 * 
 * 工程化考量：
 * 1. 输入验证：检查数组是否为空
 * 2. 边界条件：当没有孩子或没有饼干时，直接返回0
 * 3. 性能优化：使用快速排序提高效率
 */
public class Code06_AssignCookies {
    
    /**
     * 计算最多能满足的孩子数量
     * 
     * @param g 孩子的胃口数组
     * @param s 饼干尺寸数组
     * @return 最多能满足的孩子数量
     */
    public static int findContentChildren(int[] g, int[] s) {
        // 边界条件检查
        if (g == null || s == null || g.length == 0 || s.length == 0) {
            return 0;
        }
        
        // 对胃口和饼干尺寸进行排序
        Arrays.sort(g);
        Arrays.sort(s);
        
        int childIndex = 0; // 指向当前需要满足的孩子
        int cookieIndex = 0; // 指向当前尝试分配的饼干
        int satisfiedChildren = 0; // 已满足的孩子数量
        
        // 遍历所有饼干和孩子
        while (childIndex < g.length && cookieIndex < s.length) {
            // 如果当前饼干能满足当前孩子的胃口
            if (s[cookieIndex] >= g[childIndex]) {
                satisfiedChildren++; // 满足的孩子数量加1
                childIndex++; // 移动到下一个孩子
            }
            // 无论是否满足，都需要尝试下一个饼干
            cookieIndex++;
        }
        
        return satisfiedChildren;
    }
    
    /**
     * 测试函数，验证算法正确性
     */
    public static void main(String[] args) {
        // 测试用例1: 基本情况
        int[] g1 = {1, 2, 3};
        int[] s1 = {1, 1};
        int result1 = findContentChildren(g1, s1);
        System.out.println("测试用例1:");
        System.out.println("孩子胃口: " + Arrays.toString(g1));
        System.out.println("饼干尺寸: " + Arrays.toString(s1));
        System.out.println("最多能满足的孩子数量: " + result1);
        System.out.println("期望输出: 1");
        System.out.println();
        
        // 测试用例2: 所有孩子都能被满足
        int[] g2 = {1, 2};
        int[] s2 = {1, 2, 3};
        int result2 = findContentChildren(g2, s2);
        System.out.println("测试用例2:");
        System.out.println("孩子胃口: " + Arrays.toString(g2));
        System.out.println("饼干尺寸: " + Arrays.toString(s2));
        System.out.println("最多能满足的孩子数量: " + result2);
        System.out.println("期望输出: 2");
        System.out.println();
        
        // 测试用例3: 边界情况 - 没有饼干
        int[] g3 = {1, 2, 3};
        int[] s3 = {};
        int result3 = findContentChildren(g3, s3);
        System.out.println("测试用例3:");
        System.out.println("孩子胃口: " + Arrays.toString(g3));
        System.out.println("饼干尺寸: " + Arrays.toString(s3));
        System.out.println("最多能满足的孩子数量: " + result3);
        System.out.println("期望输出: 0");
        System.out.println();
        
        // 测试用例4: 边界情况 - 没有孩子
        int[] g4 = {};
        int[] s4 = {1, 2, 3};
        int result4 = findContentChildren(g4, s4);
        System.out.println("测试用例4:");
        System.out.println("孩子胃口: " + Arrays.toString(g4));
        System.out.println("饼干尺寸: " + Arrays.toString(s4));
        System.out.println("最多能满足的孩子数量: " + result4);
        System.out.println("期望输出: 0");
        System.out.println();
        
        // 测试用例5: 胃口数组和饼干数组长度不同
        int[] g5 = {1, 2, 3, 4, 5};
        int[] s5 = {1, 2, 3};
        int result5 = findContentChildren(g5, s5);
        System.out.println("测试用例5:");
        System.out.println("孩子胃口: " + Arrays.toString(g5));
        System.out.println("饼干尺寸: " + Arrays.toString(s5));
        System.out.println("最多能满足的孩子数量: " + result5);
        System.out.println("期望输出: 3");
    }
}