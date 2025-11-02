/**
 * 比特位计数（Java实现）
 * 测试链接：https://leetcode.cn/problems/counting-bits/
 * 
 * 题目描述：
 * 给定一个非负整数 n，计算从 0 到 n 的每个整数的二进制表示中 1 的个数，并返回一个数组。
 * 
 * 示例：
 * 输入: n = 2
 * 输出: [0,1,1]
 * 
 * 输入: n = 5
 * 输出: [0,1,1,2,1,2]
 * 
 * 解题思路：
 * 使用动态规划方法：
 * 对于每个数字i，ans[i] = ans[i >> 1] + (i & 1)
 * i >> 1 相当于 i / 2（整数除法）
 * i & 1 判断i是否为奇数，奇数为1，偶数为0
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 * 
 * 补充题目：
 * 1. 洛谷 P10118 『STA - R4』And: https://www.luogu.com.cn/problem/P10118
 * 2. 洛谷 P9451 [ZSHOI-R1] 新概念报数: https://www.luogu.com.cn/problem/P9451
 * 3. 洛谷 P10114 [LMXOI Round 1] Size: https://www.luogu.com.cn/problem/P10114
 * 4. 洛谷 P1469 找筷子: https://www.luogu.com.cn/problem/P1469
 * 5. Codeforces 276D Little Girl and Maximum XOR: https://www.luogu.com.cn/problem/CF276D
 */
public class Code33_CountingBits {
    
    /**
     * 计算0到num范围内每个数字二进制表示中1的个数
     * 使用动态规划方法：
     * 对于每个数字i，ans[i] = ans[i >> 1] + (i & 1)
     * i >> 1 相当于 i / 2（整数除法）
     * i & 1 判断i是否为奇数，奇数为1，偶数为0
     * 
     * @param num 非负整数
     * @return 结果数组，ans[i]表示i的二进制中1的个数
     */
    public static int[] countBits(int num) {
        int[] ans = new int[num + 1];
        ans[0] = 0;
        
        // 动态规划方法
        // 对于每个数字i，ans[i] = ans[i >> 1] + (i & 1)
        // i >> 1 相当于 i / 2（整数除法）
        // i & 1 判断i是否为奇数，奇数为1，偶数为0
        for (int i = 1; i <= num; i++) {
            ans[i] = ans[i >> 1] + (i & 1);
        }
        
        return ans;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1：正常情况
        int[] result1 = countBits(2);
        // 预期结果: [0,1,1]
        System.out.print("Test 1: [");
        for (int i = 0; i < result1.length; i++) {
            System.out.print(result1[i]);
            if (i < result1.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
        
        // 测试用例2：正常情况
        int[] result2 = countBits(5);
        // 预期结果: [0,1,1,2,1,2]
        System.out.print("Test 2: [");
        for (int i = 0; i < result2.length; i++) {
            System.out.print(result2[i]);
            if (i < result2.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
        
        // 测试用例3：边界情况
        int[] result3 = countBits(0);
        // 预期结果: [0]
        System.out.print("Test 3: [");
        for (int i = 0; i < result3.length; i++) {
            System.out.print(result3[i]);
            if (i < result3.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }
}