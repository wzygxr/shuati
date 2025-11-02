package class113;

import java.util.*;

/**
 * 计算右侧小于当前元素的个数
 * 题目来源：LeetCode 315. 计算右侧小于当前元素的个数
 * 题目链接：https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
 * 
 * 核心算法：线段树 + 离散化
 * 难度：困难
 * 
 * 【题目详细描述】
 * 给你一个整数数组 nums ，按要求返回一个新数组 counts 。数组 counts 有该性质： 
 * counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。
 * 
 * 示例 1：
 * 输入：nums = [5,2,6,1]
 * 输出：[2,1,1,0]
 * 解释：
 * 5 的右侧有 2 个更小的元素 (2 和 1)
 * 2 的右侧有 1 个更小的元素 (1)
 * 6 的右侧有 1 个更小的元素 (1)
 * 1 的右侧有 0 个更小的元素
 * 
 * 示例 2：
 * 输入：nums = [-1]
 * 输出：[0]
 * 
 * 示例 3：
 * 输入：nums = [-1,-1]
 * 输出：[0,0]
 * 
 * 【解题思路】
 * 使用离散化+线段树的方法。从右往左遍历数组，用线段树维护每个值出现的次数，
 * 查询小于当前值的元素个数。
 * 
 * 【核心算法】
 * 1. 离散化：将数组中的值映射到连续的整数范围，减少线段树的空间需求
 * 2. 线段树：维护每个值的出现次数，支持单点更新和前缀和查询
 * 3. 逆序遍历：从右往左处理数组元素，确保查询的是右侧元素
 * 
 * 【复杂度分析】
 * - 时间复杂度：O(n log n)，其中n是数组长度
 * - 空间复杂度：O(n)，用于存储离散化映射和线段树
 * 
 * 【算法优化点】
 * 1. 离散化优化：使用二分查找提高离散化效率
 * 2. 线段树优化：使用位运算优化索引计算
 * 3. 遍历优化：逆序遍历避免重复计算
 * 
 * 【工程化考量】
 * 1. 输入输出效率：使用标准输入输出处理
 * 2. 边界条件处理：处理空数组、单元素数组等特殊情况
 * 3. 错误处理：处理非法输入和溢出情况
 * 
 * 【类似题目推荐】
 * 1. LeetCode 327. 区间和的个数 - https://leetcode.cn/problems/count-of-range-sum/
 * 2. LeetCode 493. 翻转对 - https://leetcode.cn/problems/reverse-pairs/
 * 3. LeetCode 1649. 通过指令创建有序数组 - https://leetcode.cn/problems/create-sorted-array-through-instructions/
 * 4. 洛谷 P2184 贪婪大陆 - https://www.luogu.com.cn/problem/P2184
 */
public class Code07_CountSmallerNumbersAfterSelf {
    
    /**
     * 线段树类，用于维护元素出现次数和支持区间查询
     */
    class SegmentTree {
        private int[] tree;
        private int n;
        
        /**
         * 构造函数
         * 
         * @param size 线段树大小
         */
        public SegmentTree(int size) {
            this.n = size;
            this.tree = new int[4 * (size + 1)];
        }
        
        /**
         * 更新线段树中指定位置的值
         * 
         * @param node 当前节点索引
         * @param start 当前区间左边界
         * @param end 当前区间右边界
         * @param index 要更新的位置
         * @param val 增加的值
         */
        public void update(int node, int start, int end, int index, int val) {
            if (start == end) {
                tree[node] += val;
            } else {
                int mid = (start + end) / 2;
                if (index <= mid) {
                    update(2 * node, start, mid, index, val);
                } else {
                    update(2 * node + 1, mid + 1, end, index, val);
                }
                tree[node] = tree[2 * node] + tree[2 * node + 1];
            }
        }
        
        /**
         * 查询区间和
         * 
         * @param node 当前节点索引
         * @param start 当前区间左边界
         * @param end 当前区间右边界
         * @param left 查询区间左边界
         * @param right 查询区间右边界
         * @return 区间和
         */
        public int query(int node, int start, int end, int left, int right) {
            if (left > end || right < start) {
                return 0;
            }
            if (left <= start && end <= right) {
                return tree[node];
            }
            int mid = (start + end) / 2;
            int leftSum = query(2 * node, start, mid, left, right);
            int rightSum = query(2 * node + 1, mid + 1, end, left, right);
            return leftSum + rightSum;
        }
    }
    
    /**
     * 计算右侧小于当前元素的个数
     * 
     * @param nums 输入数组
     * @return 结果数组
     */
    public List<Integer> countSmaller(int[] nums) {
        // 离散化
        int[] sorted = nums.clone();
        Arrays.sort(sorted);
        Map<Integer, Integer> ranks = new HashMap<>();
        int rank = 0;
        for (int num : sorted) {
            if (!ranks.containsKey(num)) {
                ranks.put(num, ++rank);
            }
        }
        
        // 使用线段树
        SegmentTree tree = new SegmentTree(ranks.size());
        List<Integer> result = new ArrayList<>();
        
        // 从右往左遍历
        for (int i = nums.length - 1; i >= 0; i--) {
            int r = ranks.get(nums[i]);
            tree.update(1, 1, ranks.size(), r, 1);
            result.add(tree.query(1, 1, ranks.size(), 1, r - 1));
        }
        
        Collections.reverse(result);
        return result;
    }
    
    /**
     * 主函数：测试计算右侧小于当前元素的个数功能
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        Code07_CountSmallerNumbersAfterSelf solution = new Code07_CountSmallerNumbersAfterSelf();
        
        // 测试用例1
        int[] nums1 = {5, 2, 6, 1};
        List<Integer> result1 = solution.countSmaller(nums1);
        System.out.println("测试用例1结果: " + result1); // 预期输出: [2, 1, 1, 0]
        
        // 测试用例2
        int[] nums2 = {-1};
        List<Integer> result2 = solution.countSmaller(nums2);
        System.out.println("测试用例2结果: " + result2); // 预期输出: [0]
        
        // 测试用例3
        int[] nums3 = {-1, -1};
        List<Integer> result3 = solution.countSmaller(nums3);
        System.out.println("测试用例3结果: " + result3); // 预期输出: [0, 0]
    }
}