import java.util.*;

/**
 * LeetCode 327. Count of Range Sum
 * 题目链接：https://leetcode.com/problems/count-of-range-sum/
 * 
 * 题目描述：
 * 给定一个整数数组nums，以及两个整数lower和upper，返回区间和位于[lower, upper]之间的子数组数量。
 * 子数组是数组中连续的非空序列。
 * 
 * 解题思路：
 * 使用平方根分解 + 前缀和 + 排序分块
 * 1. 计算前缀和数组prefix
 * 2. 问题转化为：对于每个j，统计满足 lower <= prefix[j] - prefix[i] <= upper 的i的数量
 * 3. 使用分块维护前缀和的有序性，在块内进行二分查找
 * 
 * 时间复杂度：O(n * sqrt(n) * log(sqrt(n)))
 * 空间复杂度：O(n)
 * 
 * 工程化考量：
 * 1. 使用long防止整数溢出
 * 2. 处理空数组和边界情况
 * 3. 优化二分查找性能
 */
public class Code24_LeetCode327_Java {
    
    public int countRangeSum(int[] nums, int lower, int upper) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        int n = nums.length;
        // 计算前缀和
        long[] prefix = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
        }
        
        // 计算块大小
        int blockSize = (int) Math.sqrt(n);
        if (blockSize == 0) blockSize = 1;
        int blockCount = (n + blockSize - 1) / blockSize;
        
        // 每个块维护一个有序列表
        List<Long>[] blocks = new ArrayList[blockCount];
        for (int i = 0; i < blockCount; i++) {
            blocks[i] = new ArrayList<>();
        }
        
        int result = 0;
        
        // 从右向左处理每个前缀和
        for (int j = 1; j <= n; j++) {
            long currentPrefix = prefix[j];
            
            // 对于当前前缀和prefix[j]，我们需要找到满足条件的prefix[i]
            // lower <= prefix[j] - prefix[i] <= upper
            // 即：prefix[j] - upper <= prefix[i] <= prefix[j] - lower
            
            long lowerBound = currentPrefix - upper;
            long upperBound = currentPrefix - lower;
            
            // 在当前块之前的所有完整块中二分查找
            int currentBlock = (j - 1) / blockSize;
            
            for (int i = 0; i < currentBlock; i++) {
                List<Long> block = blocks[i];
                if (!block.isEmpty()) {
                    // 在有序块中查找满足条件的数量
                    int leftIdx = findFirstGreaterOrEqual(block, lowerBound);
                    int rightIdx = findLastLessOrEqual(block, upperBound);
                    if (leftIdx <= rightIdx) {
                        result += (rightIdx - leftIdx + 1);
                    }
                }
            }
            
            // 在当前块中查找（只考虑j之前的位置）
            int startInBlock = currentBlock * blockSize;
            int endInBlock = Math.min((currentBlock + 1) * blockSize, j);
            
            for (int i = startInBlock; i < endInBlock; i++) {
                if (prefix[i] >= lowerBound && prefix[i] <= upperBound) {
                    result++;
                }
            }
            
            // 将当前前缀和插入到对应块中（保持有序）
            int insertPos = findInsertPosition(blocks[currentBlock], prefix[j - 1]);
            blocks[currentBlock].add(insertPos, prefix[j - 1]);
        }
        
        return result;
    }
    
    /**
     * 在有序列表中查找第一个大于等于target的元素位置
     */
    private int findFirstGreaterOrEqual(List<Long> list, long target) {
        int left = 0, right = list.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (list.get(mid) < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return left;
    }
    
    /**
     * 在有序列表中查找最后一个小于等于target的元素位置
     */
    private int findLastLessOrEqual(List<Long> list, long target) {
        int left = 0, right = list.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (list.get(mid) <= target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return right;
    }
    
    /**
     * 在有序列表中查找插入位置（保持有序）
     */
    private int findInsertPosition(List<Long> list, long value) {
        int left = 0, right = list.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (list.get(mid) < value) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return left;
    }
    
    /**
     * 单元测试方法
     */
    public static void test() {
        Code24_LeetCode327_Java solution = new Code24_LeetCode327_Java();
        
        // 测试用例1
        int[] nums1 = {-2, 5, -1};
        int lower1 = -2, upper1 = 2;
        int result1 = solution.countRangeSum(nums1, lower1, upper1);
        System.out.println("测试用例1结果: " + result1 + " (预期: 3)");
        
        // 测试用例2
        int[] nums2 = {0};
        int lower2 = 0, upper2 = 0;
        int result2 = solution.countRangeSum(nums2, lower2, upper2);
        System.out.println("测试用例2结果: " + result2 + " (预期: 1)");
        
        // 测试用例3：空数组
        int[] nums3 = {};
        int result3 = solution.countRangeSum(nums3, 0, 0);
        System.out.println("测试用例3结果: " + result3 + " (预期: 0)");
    }
    
    public static void main(String[] args) {
        test();
    }
}