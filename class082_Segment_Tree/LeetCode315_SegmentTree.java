package class109;

import java.util.*;

/**
 * LeetCode 315. 计算右侧小于当前元素的个数
 * 
 * 题目描述：
 * 给你一个整数数组 nums ，按要求返回一个新数组 counts 。数组 counts 有该性质：
 * counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。
 * 
 * 示例：
 * 输入：nums = [5,2,6,1]
 * 输出：[2,1,1,0]
 * 解释：
 * 5 的右侧有 2 个更小的元素 (2 和 1)
 * 2 的右侧仅有 1 个更小的元素 (1)
 * 6 的右侧有 1 个更小的元素 (1)
 * 1 的右侧有 0 个更小的元素
 * 
 * 解题思路：
 * 使用权值线段树来解决这个问题。
 * 1. 离散化：由于数组元素可能很大，需要先进行离散化处理，将元素映射到连续的小范围
 * 2. 权值线段树：线段树的每个节点存储某个值域范围内元素出现的次数
 * 3. 从右向左遍历数组，在权值线段树中查询比当前元素小的元素个数，然后将当前元素插入线段树
 * 
 * 时间复杂度分析：
 * - 离散化：O(n log n)
 * - 遍历数组并查询/更新：O(n log n)
 * - 总时间复杂度：O(n log n)
 * 
 * 空间复杂度分析：
 * - O(n)，线段树需要4*n的空间来存储节点信息
 * 
 * 链接：https://leetcode.cn/problems/count-of-smaller-numbers-after-self
 */
public class LeetCode315_SegmentTree {
    
    // 线段树数组，存储区间元素个数
    private int[] tree;
    // 离散化后的数组大小
    private int n;
    
    /**
     * 计算右侧小于当前元素的个数
     * @param nums 输入数组
     * @return 结果数组
     */
    public List<Integer> countSmaller(int[] nums) {
        List<Integer> result = new ArrayList<>();
        if (nums == null || nums.length == 0) {
            return result;
        }
        
        // 离散化处理
        int[] sorted = nums.clone();
        Arrays.sort(sorted);
        // 去重
        int[] unique = Arrays.stream(sorted).distinct().toArray();
        n = unique.length;
        
        // 线段树数组大小通常为4*n，确保足够容纳所有节点
        tree = new int[n << 2];
        
        // 从右向左遍历数组
        for (int i = nums.length - 1; i >= 0; i--) {
            // 找到当前元素在离散化数组中的位置
            int index = Arrays.binarySearch(unique, nums[i]);
            // 查询比当前元素小的元素个数（即查询[0, index-1]区间内的元素个数）
            int count = query(0, n - 1, 1, 0, index - 1);
            result.add(count);
            // 将当前元素插入线段树
            update(0, n - 1, 1, index);
        }
        
        // 由于是从右向左遍历的，需要反转结果
        Collections.reverse(result);
        return result;
    }
    
    /**
     * 更新线段树中的值（单点更新）
     * @param start 当前节点区间起始位置
     * @param end 当前节点区间结束位置
     * @param node 当前节点在tree数组中的索引
     * @param index 要更新的位置
     */
    private void update(int start, int end, int node, int index) {
        // 如果index不在当前区间范围内，直接返回
        if (start > index || end < index) {
            return;
        }
        
        // 如果是叶子节点，直接增加计数
        if (start == end) {
            tree[node]++;
            return;
        }
        
        // 递归更新子节点
        int mid = start + (end - start) / 2;
        if (index <= mid) {
            update(start, mid, node * 2, index);
        } else {
            update(mid + 1, end, node * 2 + 1, index);
        }
        
        // 合并左右子树信息
        tree[node] = tree[node * 2] + tree[node * 2 + 1];
    }
    
    /**
     * 查询线段树中指定区间的元素个数
     * @param start 当前节点区间起始位置
     * @param end 当前节点区间结束位置
     * @param node 当前节点在tree数组中的索引
     * @param left 查询区间左边界
     * @param right 查询区间右边界
     * @return 区间内元素个数
     */
    private int query(int start, int end, int node, int left, int right) {
        // 如果查询区间无效或当前区间与查询区间无重叠，返回0
        if (left > right || start > right || end < left) {
            return 0;
        }
        
        // 如果当前区间完全包含在查询区间内，返回当前节点的值
        if (start >= left && end <= right) {
            return tree[node];
        }
        
        // 递归查询左右子树
        int mid = start + (end - start) / 2;
        int leftCount = query(start, mid, node * 2, left, right);
        int rightCount = query(mid + 1, end, node * 2 + 1, left, right);
        
        return leftCount + rightCount;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例
        LeetCode315_SegmentTree solution = new LeetCode315_SegmentTree();
        int[] nums = {5, 2, 6, 1};
        
        System.out.println("输入数组: [5, 2, 6, 1]");
        List<Integer> result = solution.countSmaller(nums);
        System.out.println("输出结果: " + result); // 应该输出[2, 1, 1, 0]
    }
}