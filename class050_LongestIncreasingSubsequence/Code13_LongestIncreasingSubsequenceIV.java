package class072;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 最长递增子序列 IV - LeetCode 2407
 * 题目来源：https://leetcode.cn/problems/longest-increasing-subsequence-iv/
 * 难度：困难
 * 题目描述：给你一个整数数组 nums 和一个整数 k 。找到最长子序列的长度，满足子序列中的每个元素 严格递增 ，且子序列中相邻元素的差的绝对值 至少 为 k 。
 * 子序列 是指从原数组中删除一些元素（可能一个也不删除）后，剩余元素保持原来顺序而形成的新数组。
 * 
 * 核心思路：
 * 1. 这道题是LIS问题的一个变体，增加了相邻元素差的绝对值至少为k的约束
 * 2. 使用动态规划 + 线段树优化来解决这个问题
 * 3. 对于每个元素nums[i]，我们需要找到所有满足nums[j] < nums[i]且nums[i] - nums[j] >= k的j，然后dp[i] = max(dp[j]) + 1
 * 4. 使用线段树来高效查询区间内的最大值
 * 
 * 复杂度分析：
 * 时间复杂度：O(n log n)，其中n是数组长度。离散化需要O(n log n)，构建线段树需要O(n)，每次查询和更新需要O(log n)，总共有n次查询和更新
 * 空间复杂度：O(n)，用于存储离散化后的值、dp数组和线段树
 */
public class Code13_LongestIncreasingSubsequenceIV {

    /**
     * 线段树节点类
     */
    class SegmentTreeNode {
        int start, end;
        int max;
        SegmentTreeNode left, right;
        
        public SegmentTreeNode(int start, int end) {
            this.start = start;
            this.end = end;
            this.max = 0;
            this.left = null;
            this.right = null;
        }
    }
    
    /**
     * 构建线段树
     * @param node 当前节点
     * @return 构建好的线段树节点
     */
    private SegmentTreeNode buildSegmentTree(SegmentTreeNode node) {
        if (node.start == node.end) {
            return node;
        }
        
        int mid = node.start + (node.end - node.start) / 2;
        node.left = new SegmentTreeNode(node.start, mid);
        node.right = new SegmentTreeNode(mid + 1, node.end);
        
        buildSegmentTree(node.left);
        buildSegmentTree(node.right);
        
        return node;
    }
    
    /**
     * 查询区间内的最大值
     * @param node 当前节点
     * @param start 查询区间的起始位置
     * @param end 查询区间的结束位置
     * @return 区间内的最大值
     */
    private int queryMax(SegmentTreeNode node, int start, int end) {
        if (start > node.end || end < node.start) {
            return 0;
        }
        
        if (start <= node.start && end >= node.end) {
            return node.max;
        }
        
        int leftMax = queryMax(node.left, start, end);
        int rightMax = queryMax(node.right, start, end);
        
        return Math.max(leftMax, rightMax);
    }
    
    /**
     * 更新线段树中的值
     * @param node 当前节点
     * @param index 要更新的索引
     * @param value 要更新的值
     */
    private void updateSegmentTree(SegmentTreeNode node, int index, int value) {
        if (node.start == node.end && node.start == index) {
            node.max = Math.max(node.max, value);
            return;
        }
        
        int mid = node.start + (node.end - node.start) / 2;
        if (index <= mid) {
            updateSegmentTree(node.left, index, value);
        } else {
            updateSegmentTree(node.right, index, value);
        }
        
        node.max = Math.max(node.left.max, node.right.max);
    }
    
    /**
     * 最优解法：动态规划 + 线段树优化
     * @param nums 输入数组
     * @param k 相邻元素差的最小绝对值
     * @return 满足条件的最长递增子序列的长度
     */
    public int lengthOfLIS(int[] nums, int k) {
        // 离散化处理
        int[] sortedNums = Arrays.copyOf(nums, nums.length);
        Arrays.sort(sortedNums);
        
        // 创建映射，将原始值映射到离散化后的值
        Map<Integer, Integer> valueToIndex = new HashMap<>();
        int index = 1; // 从1开始，便于线段树处理
        for (int num : sortedNums) {
            if (!valueToIndex.containsKey(num)) {
                valueToIndex.put(num, index++);
            }
        }
        
        int n = valueToIndex.size();
        SegmentTreeNode root = new SegmentTreeNode(1, n);
        buildSegmentTree(root);
        
        int maxLength = 0;
        for (int num : nums) {
            // 找到所有小于num且num - value >= k的元素的最大长度
            // 即找到所有value <= num - k的元素
            int target = num - k;
            // 找到最大的不大于target的值
            int left = 0;
            int right = sortedNums.length - 1;
            int best = -1;
            while (left <= right) {
                int mid = left + (right - left) / 2;
                if (sortedNums[mid] <= target) {
                    best = mid;
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            
            int currentLength = 1;
            if (best != -1) {
                int queryEnd = valueToIndex.get(sortedNums[best]);
                currentLength = queryMax(root, 1, queryEnd) + 1;
            }
            
            // 更新线段树
            int numIndex = valueToIndex.get(num);
            updateSegmentTree(root, numIndex, currentLength);
            
            maxLength = Math.max(maxLength, currentLength);
        }
        
        return maxLength;
    }
    
    /**
     * 另一种实现方式：使用TreeMap优化
     * @param nums 输入数组
     * @param k 相邻元素差的最小绝对值
     * @return 满足条件的最长递增子序列的长度
     */
    public int lengthOfLISAlternative(int[] nums, int k) {
        // 离散化处理
        int[] sortedNums = Arrays.copyOf(nums, nums.length);
        Arrays.sort(sortedNums);
        
        // 创建映射，将原始值映射到离散化后的值
        Map<Integer, Integer> valueToIndex = new HashMap<>();
        int index = 1;
        for (int num : sortedNums) {
            if (!valueToIndex.containsKey(num)) {
                valueToIndex.put(num, index++);
            }
        }
        
        int n = valueToIndex.size();
        // 使用数组模拟线段树
        int[] segmentTree = new int[4 * n];
        
        int maxLength = 0;
        for (int num : nums) {
            int target = num - k;
            // 找到最大的不大于target的值
            int left = 0;
            int right = sortedNums.length - 1;
            int best = -1;
            while (left <= right) {
                int mid = left + (right - left) / 2;
                if (sortedNums[mid] <= target) {
                    best = mid;
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            
            int currentLength = 1;
            if (best != -1) {
                int queryEnd = valueToIndex.get(sortedNums[best]);
                currentLength = queryMax(segmentTree, 0, 1, n, 1, queryEnd) + 1;
            }
            
            // 更新线段树
            int numIndex = valueToIndex.get(num);
            updateSegmentTree(segmentTree, 0, 1, n, numIndex, currentLength);
            
            maxLength = Math.max(maxLength, currentLength);
        }
        
        return maxLength;
    }
    
    /**
     * 数组实现的线段树查询方法
     */
    private int queryMax(int[] tree, int node, int start, int end, int l, int r) {
        if (r < start || end < l) {
            return 0;
        }
        if (l <= start && end <= r) {
            return tree[node];
        }
        int mid = start + (end - start) / 2;
        int leftMax = queryMax(tree, 2 * node + 1, start, mid, l, r);
        int rightMax = queryMax(tree, 2 * node + 2, mid + 1, end, l, r);
        return Math.max(leftMax, rightMax);
    }
    
    /**
     * 数组实现的线段树更新方法
     */
    private void updateSegmentTree(int[] tree, int node, int start, int end, int idx, int val) {
        if (start == end) {
            tree[node] = Math.max(tree[node], val);
        } else {
            int mid = start + (end - start) / 2;
            if (idx <= mid) {
                updateSegmentTree(tree, 2 * node + 1, start, mid, idx, val);
            } else {
                updateSegmentTree(tree, 2 * node + 2, mid + 1, end, idx, val);
            }
            tree[node] = Math.max(tree[2 * node + 1], tree[2 * node + 2]);
        }
    }
    
    /**
     * 解释性更强的版本，添加了更多注释和中间变量
     * @param nums 输入数组
     * @param k 相邻元素差的最小绝对值
     * @return 满足条件的最长递增子序列的长度
     */
    public int lengthOfLISExplained(int[] nums, int k) {
        // 步骤1: 离散化处理
        // 由于数组中的值可能很大，我们需要对其进行离散化，以便使用线段树
        int[] sortedNums = Arrays.copyOf(nums, nums.length);
        Arrays.sort(sortedNums);
        
        // 创建值到索引的映射，将原始值映射到较小的范围内
        Map<Integer, Integer> valueToIndex = new HashMap<>();
        int index = 1; // 从1开始，便于线段树处理
        for (int num : sortedNums) {
            if (!valueToIndex.containsKey(num)) {
                valueToIndex.put(num, index++);
            }
        }
        
        // 步骤2: 初始化线段树
        int n = valueToIndex.size();
        SegmentTreeNode root = new SegmentTreeNode(1, n);
        buildSegmentTree(root);
        
        // 步骤3: 动态规划 + 线段树优化
        int maxLength = 0; // 用于保存最长子序列的长度
        
        for (int num : nums) {
            // 对于每个元素num，我们需要找到所有满足nums[j] < num且num - nums[j] >= k的j
            // 即nums[j] <= num - k
            int target = num - k;
            
            // 二分查找找到最大的不大于target的值
            int left = 0;
            int right = sortedNums.length - 1;
            int best = -1; // 记录找到的索引
            
            while (left <= right) {
                int mid = left + (right - left) / 2;
                if (sortedNums[mid] <= target) {
                    best = mid; // 找到一个可能的候选
                    left = mid + 1; // 继续向右查找更大的符合条件的值
                } else {
                    right = mid - 1; // 向左查找
                }
            }
            
            // 计算以当前元素结尾的最长递增子序列长度
            int currentLength = 1; // 至少包含当前元素
            if (best != -1) { // 如果找到了符合条件的元素
                int queryEnd = valueToIndex.get(sortedNums[best]);
                // 查询区间[1, queryEnd]内的最大值，并加1
                currentLength = queryMax(root, 1, queryEnd) + 1;
            }
            
            // 更新线段树中当前值的位置
            int numIndex = valueToIndex.get(num);
            updateSegmentTree(root, numIndex, currentLength);
            
            // 更新全局最长子序列长度
            maxLength = Math.max(maxLength, currentLength);
        }
        
        return maxLength;
    }
    
    /**
     * 主方法，用于测试
     */
    public static void main(String[] args) {
        Code13_LongestIncreasingSubsequenceIV solution = new Code13_LongestIncreasingSubsequenceIV();
        
        // 测试用例1
        int[] nums1 = {4, 2, 1, 4, 3, 4, 5, 8, 15};
        int k1 = 3;
        System.out.println("测试用例1：");
        System.out.println("数组: " + Arrays.toString(nums1));
        System.out.println("k: " + k1);
        System.out.println("结果: " + solution.lengthOfLIS(nums1, k1) + "，预期: 5");
        System.out.println();
        
        // 测试用例2
        int[] nums2 = {7, 4, 5, 1, 8, 12, 4, 7};
        int k2 = 5;
        System.out.println("测试用例2：");
        System.out.println("数组: " + Arrays.toString(nums2));
        System.out.println("k: " + k2);
        System.out.println("结果: " + solution.lengthOfLIS(nums2, k2) + "，预期: 3");
        System.out.println();
        
        // 测试用例3：边界情况
        int[] nums3 = {1};
        int k3 = 1;
        System.out.println("测试用例3：");
        System.out.println("数组: " + Arrays.toString(nums3));
        System.out.println("k: " + k3);
        System.out.println("结果: " + solution.lengthOfLIS(nums3, k3) + "，预期: 1");
        
        // 运行所有解法的对比测试
        runAllSolutionsTest(solution, nums1, k1);
        runAllSolutionsTest(solution, nums2, k2);
        runAllSolutionsTest(solution, nums3, k3);
    }
    
    /**
     * 运行所有解法的对比测试
     */
    public static void runAllSolutionsTest(Code13_LongestIncreasingSubsequenceIV solution, int[] nums, int k) {
        System.out.println("\n对比测试：");
        System.out.println("数组: " + Arrays.toString(nums));
        System.out.println("k: " + k);
        
        // 测试线段树节点实现
        long startTime = System.nanoTime();
        int result1 = solution.lengthOfLIS(nums, k);
        long endTime = System.nanoTime();
        System.out.println("线段树节点实现结果: " + result1);
        System.out.println("耗时: " + (endTime - startTime) / 1000 + " μs");
        
        // 测试数组实现的线段树
        startTime = System.nanoTime();
        int result2 = solution.lengthOfLISAlternative(nums, k);
        endTime = System.nanoTime();
        System.out.println("数组实现线段树结果: " + result2);
        System.out.println("耗时: " + (endTime - startTime) / 1000 + " μs");
        
        // 测试解释性版本
        startTime = System.nanoTime();
        int result3 = solution.lengthOfLISExplained(nums, k);
        endTime = System.nanoTime();
        System.out.println("解释性版本结果: " + result3);
        System.out.println("耗时: " + (endTime - startTime) / 1000 + " μs");
        
        System.out.println("----------------------------------------");
    }
    
    /**
     * 性能测试函数
     */
    public static void performanceTest(Code13_LongestIncreasingSubsequenceIV solution, int size) {
        // 生成随机测试数据
        int[] nums = new int[size];
        for (int i = 0; i < size; i++) {
            nums[i] = (int)(Math.random() * 10000);
        }
        int k = (int)(Math.random() * 100);
        
        System.out.println("\n性能测试：数组大小 = " + size);
        
        // 测试线段树节点实现
        long startTime = System.nanoTime();
        int result1 = solution.lengthOfLIS(nums, k);
        long endTime = System.nanoTime();
        System.out.println("线段树节点实现耗时: " + (endTime - startTime) / 1_000_000 + " ms, 结果: " + result1);
    }
}