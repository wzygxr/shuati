package class111;

import java.util.ArrayList;
import java.util.List;

// Count of Smaller Numbers After Self (计算右侧小于当前元素的个数)
// 题目来源: LeetCode 315. Count of Smaller Numbers After Self
// 题目链接: https://leetcode.cn/problems/count-of-smaller-numbers-after-self
// 题目链接: https://leetcode.com/problems/count-of-smaller-numbers-after-self
// 
// 题目描述:
// 给你一个整数数组 nums ，按要求返回一个新数组 counts 。
// 数组 counts 有该性质：counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。
// 示例 1：
// 输入：nums = [5,2,6,1]
// 输出：[2,1,1,0]
// 解释：
// 5 的右侧有 2 个更小的元素 (2 和 1)
// 2 的右侧有 1 个更小的元素 (1)
// 6 的右侧有 1 个更小的元素 (1)
// 1 的右侧有 0 个更小的元素
// 示例 2：
// 输入：nums = [-1]
// 输出：[0]
// 示例 3：
// 输入：nums = [-1,-1]
// 输出：[0,0]
// 提示：
// 1 <= nums.length <= 10^5
// -10^4 <= nums[i] <= 10^4
//
// 解题思路:
// 1. 使用离散化+线段树的方法解决
// 2. 从右向左遍历数组，维护一个值域线段树
// 3. 对于每个元素，查询值域中小于它的元素个数
// 4. 将当前元素插入到线段树中
// 5. 利用离散化处理大范围的值域
//
// 时间复杂度: O(n log n)，其中n为数组长度
// 空间复杂度: O(n)

public class Code10_CountOfSmallerNumbersAfterSelf {

    // 线段树实现（用于离散化后的值域）
    private static class SegmentTree {
        private int[] tree;
        private int n;

        public SegmentTree(int size) {
            this.n = size;
            // 线段树需要4*n的空间
            this.tree = new int[4 * size];
        }
    
        // 更新节点值（单点更新）
        public void update(int index, int val) {
            updateHelper(0, 0, n - 1, index, val);
        }

        // 更新辅助函数
        private void updateHelper(int node, int start, int end, int index, int val) {
            // 找到叶子节点，更新值
            if (start == end) {
                tree[node] += val;
                return;
            }

            // 在左右子树中查找需要更新的索引
            int mid = (start + end) / 2;
            if (index <= mid) {
                // 在左子树中
                updateHelper(2 * node + 1, start, mid, index, val);
            } else {
                // 在右子树中
                updateHelper(2 * node + 2, mid + 1, end, index, val);
            }

            // 更新当前节点的值为左右子节点值的和
            tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
        }

        // 查询区间和
        public int query(int left, int right) {
            // 处理边界情况
            if (left > right) return 0;
            return queryHelper(0, 0, n - 1, left, right);
        }

        // 查询区间和辅助函数
        private int queryHelper(int node, int start, int end, int left, int right) {
            // 当前区间与查询区间无交集
            if (right < start || left > end) {
                return 0;
            }

            // 当前区间完全包含在查询区间内
            if (left <= start && end <= right) {
                return tree[node];
            }

            // 当前区间与查询区间有部分交集，递归查询左右子树
            int mid = (start + end) / 2;
            int leftSum = queryHelper(2 * node + 1, start, mid, left, right);
            int rightSum = queryHelper(2 * node + 2, mid + 1, end, left, right);
            return leftSum + rightSum;
        }
    }
    
    // ==========================================================================================
    // LeetCode 1649. Create Sorted Array through Instructions
    // 题目链接：https://leetcode.com/problems/create-sorted-array-through-instructions/
    // 题目描述：
    // 给你一个整数数组 instructions，你需要根据 instructions 中的元素创建一个有序数组。
    // 一开始数组为空。你需要依次读取 instructions 中的元素，并将它插入到有序数组中的正确位置。
    // 每次插入操作的代价是以下两者的较小值：
    // 1. 有多少个元素严格小于 instructions[i]（左边）
    // 2. 有多少个元素严格大于 instructions[i]（右边）
    // 返回插入所有元素的总最小代价。由于答案可能很大，请返回它对 10^9 + 7 取模的结果。
    // 
    // 示例：
    // 输入：instructions = [1,5,6,2]
    // 输出：1
    // 解释：插入 1 时，数组为空，代价为 0。
    // 插入 5 时，左边有 1 个元素比 5 小，右边没有元素，代价为 min(1, 0) = 0。
    // 插入 6 时，左边有 2 个元素比 6 小，右边没有元素，代价为 min(2, 0) = 0。
    // 插入 2 时，左边有 1 个元素比 2 小，右边有 2 个元素比 2 大，代价为 min(1, 2) = 1。
    // 总代价为 0 + 0 + 0 + 1 = 1
    // ==========================================================================================
    
    public static class CreateSortedArrayThroughInstructions {
        private static final int MOD = 1000000007;
        
        /**
         * 计算创建有序数组的最小代价
         * @param instructions 指令数组
         * @return 总最小代价
         */
        public int createSortedArray(int[] instructions) {
            // 离散化处理
            int n = instructions.length;
            int[] sorted = instructions.clone();
            java.util.Arrays.sort(sorted);
            
            // 去重
            int uniqueSize = 1;
            for (int i = 1; i < n; i++) {
                if (sorted[i] != sorted[i - 1]) {
                    sorted[uniqueSize++] = sorted[i];
                }
            }
            
            // 创建线段树
            SegmentTree st = new SegmentTree(uniqueSize);
            long totalCost = 0; // 使用long避免溢出
            
            // 处理每个指令
            for (int i = 0; i < instructions.length; i++) {
                int value = instructions[i];
                
                // 找到离散化后的位置
                int pos = binarySearch(sorted, 0, uniqueSize - 1, value);
                
                // 计算左边比当前元素小的个数
                int smallerCount = st.query(0, pos - 1);
                
                // 计算右边比当前元素大的个数（总元素数减去到当前索引的前缀和）
                int largerCount = i - st.query(0, pos);
                
                // 取较小值作为当前操作的代价
                totalCost = (totalCost + Math.min(smallerCount, largerCount)) % MOD;
                
                // 更新线段树，将当前元素的计数加1
                st.update(pos, 1);
            }
            
            return (int) totalCost;
        }
    }
    
    // 测试LeetCode 1649题的方法
    public static void testLeetCode1649() {
        CreateSortedArrayThroughInstructions solution = new CreateSortedArrayThroughInstructions();
        
        // 测试用例1
        int[] instructions1 = {1, 5, 6, 2};
        System.out.println("LeetCode 1649 测试用例1结果: " + solution.createSortedArray(instructions1)); // 预期输出: 1
        
        // 测试用例2
        int[] instructions2 = {1, 2, 3, 6, 5, 4};
        System.out.println("LeetCode 1649 测试用例2结果: " + solution.createSortedArray(instructions2)); // 预期输出: 3
        
        // 测试用例3
        int[] instructions3 = {1, 3, 3, 3, 2, 4, 2, 1, 2};
        System.out.println("LeetCode 1649 测试用例3结果: " + solution.createSortedArray(instructions3)); // 预期输出: 4
    }

    public static List<Integer> countSmaller(int[] nums) {
        int n = nums.length;
        List<Integer> result = new ArrayList<>();

        // 离散化处理
        // 1. 收集所有不同的值
        int[] sorted = nums.clone();
        java.util.Arrays.sort(sorted);

        // 2. 去重
        int uniqueSize = 1;
        for (int i = 1; i < n; i++) {
            if (sorted[i] != sorted[i - 1]) {
                sorted[uniqueSize++] = sorted[i];
            }
        }

        // 3. 创建离散化映射
        // 线段树的大小为去重后的元素个数
        SegmentTree st = new SegmentTree(uniqueSize);

        // 从右向左遍历数组
        for (int i = n - 1; i >= 0; i--) {
            // 找到当前元素在离散化数组中的位置
            int pos = binarySearch(sorted, 0, uniqueSize - 1, nums[i]);
            
            // 查询比当前元素小的元素个数（在值域上查询[0, pos-1]区间和）
            int count = st.query(0, pos - 1);
            result.add(0, count); // 插入到结果列表的开头
            
            // 更新当前元素的计数（在值域上对位置pos进行+1操作）
            st.update(pos, 1);
        }

        return result;
    }

    // 二分查找元素在排序数组中的位置
    private static int binarySearch(int[] arr, int left, int right, int target) {
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] == target) {
                return mid;
            } else if (arr[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return left;
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {5, 2, 6, 1};
        System.out.println(countSmaller(nums1)); // 输出: [2, 1, 1, 0]

        // 测试用例2
        int[] nums2 = {-1};
        System.out.println(countSmaller(nums2)); // 输出: [0]

        // 测试用例3
        int[] nums3 = {-1, -1};
        System.out.println(countSmaller(nums3)); // 输出: [0, 0]
        
        // 测试LeetCode 1649题
        System.out.println("\n测试LeetCode 1649题:");
        testLeetCode1649();
    }
}