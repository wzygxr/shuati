package class136;

// 元素问题（线性基+贪心）
// 题目来源：洛谷P4570 [BJWC2011] 元素
// 题目链接：https://www.luogu.com.cn/problem/P4570
// 题目描述：给定n个魔法矿石，每个矿石有状态和魔力，都是整数
// 若干矿石组成的组合能否有效，根据状态异或的结果来决定
// 如果一个矿石组合内部会产生异或和为0的子集，那么这个组合无效
// 返回有效的矿石组合中，最大的魔力和是多少
// 算法：线性基 + 贪心
// 时间复杂度：O(n * log(n) + n * log(max_value))
// 空间复杂度：O(n + log(max_value))
// 测试链接 : https://www.luogu.com.cn/problem/P4570

import java.util.*;

public class Code03_Elements {
    public static long[] status;  // 状态值数组
    public static int[] magic;    // 魔力值数组
    public static long[] basis;   // 线性基数组
    public static int n;          // 矿石数量
    public static final int BIT = 60;  // 最大位数

    /**
     * 线性基插入操作
     * 
     * 算法思路：
     * 1. 从最高位到最低位扫描数字的二进制位
     * 2. 如果当前位为1：
     *    - 如果线性基中该位为空，则将当前数插入该位置
     *    - 否则用线性基中该位的数异或当前数，继续处理低位
     * 3. 如果成功插入返回true，否则返回false（表示该数可由现有线性基表示）
     * 
     * 时间复杂度：O(BIT) = O(log(max_value))
     * 空间复杂度：O(1)
     * 
     * 关键细节：
     * - 从高位到低位处理：保证线性基中每个基的最高位唯一
     * - 异或操作：消除当前数中与现有基重叠的部分
     * - 返回值：true表示线性基增加了新基，false表示该数线性相关
     * 
     * 异常场景处理：
     * - 输入为0：直接返回false（0可由空集表示）
     * - 输入为负数：Java中右移操作会保留符号位，需要特别注意
     * 
     * @param num 要插入的数字
     * @return 如果线性基增加了新基返回true，否则返回false
     */
    public static boolean insert(long num) {
        // 边界情况：如果num为0，直接返回false（0可由空集表示）
        if (num == 0) {
            return false;
        }
        
        for (int i = BIT; i >= 0; i--) {
            if ((num >> i & 1) == 1) {
                if (basis[i] == 0) {
                    basis[i] = num;
                    return true;
                }
                num ^= basis[i];
            }
        }
        return false;
    }

    /**
     * 计算最大魔力和（线性基+贪心算法）
     * 
     * 算法思路：
     * 1. 将所有矿石按魔力值从大到小排序
     * 2. 清空线性基
     * 3. 贪心选择矿石：依次尝试将每个矿石加入线性基，如果能成功加入则将该矿石的魔力加入答案
     * 
     * 时间复杂度分析：
     * - 排序：O(n * log(n))
     * - 线性基构建：O(n * BIT) = O(n * log(max_value))
     * - 总时间复杂度：O(n * log(n) + n * log(max_value))
     * 
     * 空间复杂度分析：
     * - 线性基数组：O(BIT) = O(log(max_value))
     * - 输入数组：O(n)
     * - 索引数组：O(n)
     * - 总空间复杂度：O(n + log(max_value))
     * 
     * 贪心策略正确性证明：
     * 1. 按魔力值降序排序，优先选择魔力值大的矿石
     * 2. 如果当前矿石的状态值能插入线性基，说明它不会导致异或和为0
     * 3. 贪心选择保证了最终结果的魔力值最大
     * 
     * 关键细节：
     * - 排序策略：魔力值大的优先
     * - 线性基判断：避免异或和为0的组合
     * - 累加策略：只累加能成功插入线性基的矿石魔力
     * 
     * 异常场景处理：
     * - 空数组：返回0
     * - 所有矿石都线性相关：返回0
     * - 魔力值为负数：需要特殊处理（本题中魔力值为正数）
     * 
     * @return 最大魔力和
     */
    public static long compute() {
        // 边界情况：如果矿石数量为0，直接返回0
        if (n == 0) {
            return 0;
        }
        
        // 创建索引数组并按魔力值排序
        Integer[] indices = new Integer[n];
        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }
        
        // 按魔力值从大到小排序
        Arrays.sort(indices, (a, b) -> Integer.compare(magic[b], magic[a]));
        
        // 清空线性基
        Arrays.fill(basis, 0);
        
        long ans = 0;
        // 贪心选择矿石
        for (int i = 0; i < n; i++) {
            int idx = indices[i];
            if (insert(status[idx])) {
                ans += magic[idx];
            }
        }
        
        return ans;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();
        
        status = new long[n];
        magic = new int[n];
        basis = new long[BIT + 1];
        
        for (int i = 0; i < n; i++) {
            status[i] = scanner.nextLong();
            magic[i] = scanner.nextInt();
        }
        
        System.out.println(compute());
        scanner.close();
    }
}
