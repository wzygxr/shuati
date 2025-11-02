package class111;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * LeetCode 1649. Create Sorted Array through Instructions (通过指令创建有序数组)
 * 题目链接：https://leetcode.com/problems/create-sorted-array-through-instructions/
 * 
 * 题目描述：
 * 给你一个整数数组 instructions，你需要根据 instructions 中的元素创建一个有序数组。
 * 一开始数组为空。你需要依次读取 instructions 中的元素，并将它插入到有序数组中的正确位置。
 * 每次插入操作的代价是以下两者的较小值：
 * 1. 有多少个元素严格小于 instructions[i]（左边）
 * 2. 有多少个元素严格大于 instructions[i]（右边）
 * 返回插入所有元素的总最小代价。由于答案可能很大，请返回它对 10^9 + 7 取模的结果。
 * 
 * 示例：
 * 输入：instructions = [1,5,6,2]
 * 输出：1
 * 解释：插入 1 时，数组为空，代价为 0。
 * 插入 5 时，左边有 1 个元素比 5 小，右边没有元素，代价为 min(1, 0) = 0。
 * 插入 6 时，左边有 2 个元素比 6 小，右边没有元素，代价为 min(2, 0) = 0。
 * 插入 2 时，左边有 1 个元素比 2 小，右边有 2 个元素比 2 大，代价为 min(1, 2) = 1。
 * 总代价为 0 + 0 + 0 + 1 = 1
 * 
 * 解题思路：
 * 这道题可以使用离散化线段树来解决。我们需要高效地统计数组中有多少元素小于当前元素，以及有多少元素大于当前元素。
 * 具体步骤：
 * 1. 离散化处理：将指令数组中的所有元素进行排序去重，得到每个元素的排名（离散化值）
 * 2. 构建线段树：维护每个值出现的次数
 * 3. 对于每个指令：
 *    a. 查询当前小于该元素的个数（即离散化后值减1的前缀和）
 *    b. 查询当前大于该元素的个数（即总元素数减去离散化后值的前缀和）
 *    c. 计算当前代价并累加到结果中
 *    d. 更新线段树，将该元素的计数加1
 * 
 * 时间复杂度分析：
 * - 离散化：O(n log n)，其中n是指令数组的长度
 * - 线段树构建：O(m)，其中m是离散化后的不同元素个数
 * - 每个查询和更新操作：O(log m)
 * - 总时间复杂度：O(n log n + n log m) = O(n log n)，因为m ≤ n
 * 
 * 空间复杂度分析：
 * - 线段树空间：O(m)
 * - 离散化数组：O(m)
 * - 总空间复杂度：O(m) = O(n)
 * 
 * 本题最优解：线段树是本题的最优解之一，另外也可以使用树状数组（Fenwick Tree）实现，时间复杂度相同。
 */
public class Code12_CreateSortedArrayThroughInstructions {
    // 取模常量
    private static final int MOD = 1000000007;
    
    /**
     * 线段树节点类
     */
    private static class SegmentTree {
        private int[] tree; // 线段树数组
        private int n;      // 原始数据长度
        
        /**
         * 构造线段树
         * @param size 离散化后的值域大小
         */
        public SegmentTree(int size) {
            n = 1;
            // 计算大于等于size的最小2的幂次
            while (n < size) {
                n <<= 1;
            }
            // 初始化线段树数组，大小为2*n
            tree = new int[2 * n];
        }
        
        /**
         * 更新线段树中的某个位置的值
         * @param idx 离散化后的值对应的索引
         * @param delta 要增加的值（这里是1）
         */
        public void update(int idx, int delta) {
            idx += n; // 转换为线段树叶子节点索引
            tree[idx] += delta;
            // 向上更新父节点
            for (int i = idx >> 1; i >= 1; i >>= 1) {
                tree[i] = tree[2 * i] + tree[2 * i + 1];
            }
        }
        
        /**
         * 查询区间[0, idx]的和
         * @param idx 离散化后的值对应的索引
         * @return 区间和
         */
        public int query(int idx) {
            if (idx < 0) return 0;
            idx = Math.min(idx, n - 1); // 防止越界
            int res = 0;
            int l = n;       // 左边界（线段树叶子节点索引）
            int r = n + idx; // 右边界（线段树叶子节点索引）
            
            // 区间查询
            while (l <= r) {
                // 如果l是右孩子
                if ((l & 1) == 1) {
                    res += tree[l];
                    l++;
                }
                // 如果r是左孩子
                if ((r & 1) == 0) {
                    res += tree[r];
                    r--;
                }
                l >>= 1;
                r >>= 1;
            }
            return res;
        }
    }
    
    /**
     * 计算创建有序数组的最小代价
     * @param instructions 指令数组
     * @return 总最小代价
     */
    public int createSortedArray(int[] instructions) {
        // 离散化处理
        Set<Integer> uniqueVals = new HashSet<>();
        for (int num : instructions) {
            uniqueVals.add(num);
        }
        
        // 转换为有序数组并排序
        Integer[] sortedVals = uniqueVals.toArray(new Integer[0]);
        Arrays.sort(sortedVals);
        
        // 创建值到离散化索引的映射
        int[] valueToIndex = new int[100001]; // 题目中说指令中的元素不超过10^5
        for (int i = 0; i < sortedVals.length; i++) {
            valueToIndex[sortedVals[i]] = i;
        }
        
        // 构建线段树
        SegmentTree segmentTree = new SegmentTree(sortedVals.length);
        long totalCost = 0; // 使用long避免溢出
        
        // 处理每个指令
        for (int i = 0; i < instructions.length; i++) {
            int value = instructions[i];
            int idx = valueToIndex[value];
            
            // 计算左边比当前元素小的个数（即前缀和）
            int smallerCount = segmentTree.query(idx - 1);
            
            // 计算右边比当前元素大的个数（总元素数减去到当前索引的前缀和）
            int largerCount = i - segmentTree.query(idx);
            
            // 取较小值作为当前操作的代价
            totalCost += Math.min(smallerCount, largerCount);
            totalCost %= MOD; // 取模
            
            // 更新线段树，将当前元素的计数加1
            segmentTree.update(idx, 1);
        }
        
        return (int) totalCost;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        Code12_CreateSortedArrayThroughInstructions solution = new Code12_CreateSortedArrayThroughInstructions();
        
        // 测试用例1
        int[] instructions1 = {1, 5, 6, 2};
        System.out.println("测试用例1结果: " + solution.createSortedArray(instructions1)); // 预期输出: 1
        
        // 测试用例2
        int[] instructions2 = {1, 2, 3, 6, 5, 4};
        System.out.println("测试用例2结果: " + solution.createSortedArray(instructions2)); // 预期输出: 3
        
        // 测试用例3
        int[] instructions3 = {1, 3, 3, 3, 2, 4, 2, 1, 2};
        System.out.println("测试用例3结果: " + solution.createSortedArray(instructions3)); // 预期输出: 4
    }
}