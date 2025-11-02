package class031;

// 格雷编码 - Gray Code
// 测试链接 : https://leetcode.cn/problems/gray-code/
// 相关题目:
// 1. 二进制手表 - Binary Watch: https://leetcode.cn/problems/binary-watch/
// 2. 比特位计数 - Counting Bits: https://leetcode.cn/problems/counting-bits/
// 3. 位1的个数 - Number of 1 Bits: https://leetcode.cn/problems/number-of-1-bits/
// 4. 颠倒二进制位 - Reverse Bits: https://leetcode.cn/problems/reverse-bits/
// 5. 2的幂 - Power of Two: https://leetcode.cn/problems/power-of-two/

/*
题目描述：
n 位格雷码序列是一个由 2^n 个整数组成的序列，其中：
- 每个整数都在范围 [0, 2^n - 1] 内（含 0 和 2^n - 1）
- 第一个整数是 0
- 一个整数在序列中出现不超过一次
- 每对相邻整数的二进制表示恰好一位不同
- 对于序列中的第一个和最后一个整数，其二进制表示也恰好一位不同

给定一个整数 n ，返回任一有效的 n 位格雷码序列。

示例：
输入：n = 2
输出：[0,1,3,2]
解释：
[0,1,3,2] 的二进制表示是 [00,01,11,10] 。
- 00 和 01 有一位不同
- 01 和 11 有一位不同
- 11 和 10 有一位不同
- 10 和 00 有一位不同

输入：n = 1
输出：[0,1]

提示：
1 <= n <= 16

解题思路：
格雷码（Gray Code）是一个二进制数字系统，在该系统中，两个连续的数值仅有一个位数的差异。

方法1：公式法（最优解）
格雷码有一个数学公式：第i个格雷码 G(i) = i ^ (i >> 1)
这个公式的核心思想是：
- 将数字i右移一位，然后与原数字进行异或运算
- 这样可以保证相邻的两个格雷码只有一位不同

例如：n=3时的格雷码序列
i    i>>1    i^(i>>1)  二进制表示
0    0       0         000
1    0       1         001
2    1       3         011
3    1       2         010
4    2       6         110
5    2       7         111
6    3       5         101
7    3       4         100

方法2：对称生成法
1. 从[0]开始
2. 每次将当前序列反转，并在每个元素前加上1（即加上2^i），然后追加到原序列后面
3. 重复n次

例如：n=3时的生成过程
初始：[0]
第1次：[0] + [0+1] = [0,1]
第2次：[0,1] + [1+2,0+2] = [0,1,3,2]
第3次：[0,1,3,2] + [2+4,3+4,1+4,0+4] = [0,1,3,2,6,7,5,4]

方法3：递归法
G(n) = G(n-1) + reverse(G(n-1)) + 2^(n-1)
即n位格雷码等于(n-1)位格雷码连接上将(n-1)位格雷码反转后每个元素加上2^(n-1)

时间复杂度：
方法1：O(2^n) - 需要生成2^n个数字
方法2：O(2^n) - 需要生成2^n个数字
方法3：O(2^n) - 递归生成所有数字

空间复杂度：
方法1：O(1) - 不考虑输出数组
方法2：O(1) - 不考虑输出数组
方法3：O(n) - 递归调用栈的深度

补充题目：
1. 洛谷 P10118 『STA - R4』And: https://www.luogu.com.cn/problem/P10118
2. 洛谷 P9451 [ZSHOI-R1] 新概念报数: https://www.luogu.com.cn/problem/P9451
3. 洛谷 P10114 [LMXOI Round 1] Size: https://www.luogu.com.cn/problem/P10114
4. 洛谷 P1469 找筷子: https://www.luogu.com.cn/problem/P1469
5. Codeforces 276D Little Girl and Maximum XOR: https://www.luogu.com.cn/problem/CF276D
*/
public class Code14_GrayCode {

    /**
     * 生成n位格雷码序列
     * 使用公式法：第i个格雷码 G(i) = i ^ (i >> 1)
     * 
     * @param n 位数
     * @return 格雷码序列
     */
    public static java.util.List<Integer> grayCode(int n) {
        java.util.List<Integer> result = new java.util.ArrayList<>();
        
        // 方法1：公式法
        // 第i个格雷码 G(i) = i ^ (i >> 1)
        for (int i = 0; i < (1 << n); i++) {
            result.add(i ^ (i >> 1));
        }
        
        return result;
    }
    
    // 方法2：对称生成法
    // public static java.util.List<Integer> grayCode(int n) {
    //     java.util.List<Integer> result = new java.util.ArrayList<>();
    //     result.add(0);
    //     
    //     // 每次迭代生成更高位的格雷码
    //     for (int i = 0; i < n; i++) {
    //         int size = result.size();
    //         // 从后往前遍历，将当前序列反转并加上2^i
    //         for (int j = size - 1; j >= 0; j--) {
    //             result.add(result.get(j) + (1 << i));
    //         }
    //     }
    //     
    //     return result;
    // }
    
    // 方法3：递归法
    // public static java.util.List<Integer> grayCode(int n) {
    //     if (n == 0) {
    //         java.util.List<Integer> result = new java.util.ArrayList<>();
    //         result.add(0);
    //         return result;
    //     }
    //     
    //     // 递归生成(n-1)位格雷码
    //     java.util.List<Integer> prev = grayCode(n - 1);
    //     java.util.List<Integer> result = new java.util.ArrayList<>(prev);
    //     
    //     // 将(prev)位格雷码反转后每个元素加上2^(n-1)
    //     int head = 1 << (n - 1);
    //     for (int i = prev.size() - 1; i >= 0; i--) {
    //         result.add(head + prev.get(i));
    //     }
    //     
    //     return result;
    // }
    
    // 测试方法
    public static void main(String[] args) {
        System.out.println("Test n=1: " + grayCode(1));  // 输出: [0, 1]
        System.out.println("Test n=2: " + grayCode(2));  // 输出: [0, 1, 3, 2]
        System.out.println("Test n=3: " + grayCode(3));  // 输出: [0, 1, 3, 2, 6, 7, 5, 4]
    }

}