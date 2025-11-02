package class072;

import java.util.*;

/**
 * HackerRank LIS挑战 - 最长递增子序列变种
 * 
 * 题目来源：HackerRank - The Longest Increasing Subsequence
 * 题目链接：https://www.hackerrank.com/challenges/longest-increasing-subsequence/problem
 * 题目描述：给定一个整数序列，找到最长严格递增子序列的长度。
 * 但HackerRank的测试数据规模较大，需要O(n*logn)的解法。
 * 
 * 算法思路：
 * 1. 使用贪心+二分查找的标准解法
 * 2. 维护ends数组，ends[i]表示长度为i+1的递增子序列的最小结尾元素
 * 3. 对于每个元素，在ends中二分查找>=num的最左位置
 * 
 * 时间复杂度：O(n*logn)
 * 空间复杂度：O(n)
 * 是否最优解：是，这是最优解法
 * 
 * 示例：
 * 输入: [2, 7, 4, 3, 8]
 * 输出: 3
 * 解释: 最长递增子序列为[2,4,8]或[2,7,8]，长度为3。
 */

public class Code22_HackerRankLISChallenge {

    /**
     * 计算最长递增子序列的长度（HackerRank标准解法）
     * 
     * @param arr 输入的整数数组
     * @return 最长递增子序列的长度
     */
    public static int longestIncreasingSubsequence(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        int n = arr.length;
        // tails[i]表示长度为i+1的所有递增子序列中结尾元素的最小值
        int[] tails = new int[n];
        int size = 0;
        
        for (int x : arr) {
            // 二分查找x在tails中的插入位置
            int left = 0, right = size;
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (tails[mid] < x) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }
            
            tails[left] = x;
            if (left == size) {
                size++;
            }
        }
        
        return size;
    }

    /**
     * 使用ArrayList的简化版本（更易理解）
     * 
     * 算法思路：
     * 1. 使用ArrayList动态维护ends列表
     * 2. 对于每个元素，二分查找插入位置
     * 3. 如果元素大于所有现有元素，添加到末尾；否则替换第一个大于等于它的元素
     * 
     * 时间复杂度：O(n*logn)
     * 空间复杂度：O(n)
     * 是否最优解：是，功能相同但代码更简洁
     * 
     * @param arr 输入的整数数组
     * @return 最长递增子序列的长度
     */
    public static int longestIncreasingSubsequenceSimple(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        List<Integer> list = new ArrayList<>();
        
        for (int num : arr) {
            if (list.isEmpty() || num > list.get(list.size() - 1)) {
                // 如果大于最后一个元素，直接添加到末尾
                list.add(num);
            } else {
                // 二分查找插入位置
                int left = 0, right = list.size() - 1;
                int pos = -1;
                
                while (left <= right) {
                    int mid = left + (right - left) / 2;
                    if (list.get(mid) < num) {
                        left = mid + 1;
                    } else {
                        pos = mid;
                        right = mid - 1;
                    }
                }
                
                if (pos != -1) {
                    list.set(pos, num);
                }
            }
        }
        
        return list.size();
    }

    /**
     * 使用Java内置的二分查找方法
     * 
     * 算法思路：
     * 1. 使用Collections.binarySearch进行二分查找
     * 2. 如果返回负值，说明需要插入新位置
     * 3. 更简洁的实现
     * 
     * 时间复杂度：O(n*logn)
     * 空间复杂度：O(n)
     * 是否最优解：是，代码最简洁
     * 
     * @param arr 输入的整数数组
     * @return 最长递增子序列的长度
     */
    public static int longestIncreasingSubsequenceBuiltIn(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        List<Integer> list = new ArrayList<>();
        
        for (int num : arr) {
            int pos = Collections.binarySearch(list, num);
            if (pos < 0) {
                pos = -pos - 1; // 转换为插入位置
            }
            
            if (pos == list.size()) {
                list.add(num);
            } else {
                list.set(pos, num);
            }
        }
        
        return list.size();
    }

    /**
     * 动态规划解法（用于对比，不适用于大规模数据）
     * 
     * 算法思路：
     * 1. 传统DP方法，计算每个位置结尾的LIS长度
     * 2. 时间复杂度O(n²)，适用于小规模数据
     * 
     * 时间复杂度：O(n²)
     * 空间复杂度：O(n)
     * 是否最优解：否，不适用于大规模数据
     * 
     * @param arr 输入的整数数组
     * @return 最长递增子序列的长度
     */
    public static int longestIncreasingSubsequenceDP(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        int n = arr.length;
        int[] dp = new int[n];
        Arrays.fill(dp, 1);
        int maxLen = 1;
        
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (arr[j] < arr[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            maxLen = Math.max(maxLen, dp[i]);
        }
        
        return maxLen;
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1：HackerRank示例
        int[] arr1 = {2, 7, 4, 3, 8};
        System.out.println("输入: [2,7,4,3,8]");
        System.out.println("标准方法输出: " + longestIncreasingSubsequence(arr1));
        System.out.println("简化方法输出: " + longestIncreasingSubsequenceSimple(arr1));
        System.out.println("内置方法输出: " + longestIncreasingSubsequenceBuiltIn(arr1));
        System.out.println("DP方法输出: " + longestIncreasingSubsequenceDP(arr1));
        System.out.println("期望: 3");
        System.out.println();
        
        // 测试用例2
        int[] arr2 = {10, 9, 2, 5, 3, 7, 101, 18};
        System.out.println("输入: [10,9,2,5,3,7,101,18]");
        System.out.println("标准方法输出: " + longestIncreasingSubsequence(arr2));
        System.out.println("简化方法输出: " + longestIncreasingSubsequenceSimple(arr2));
        System.out.println("内置方法输出: " + longestIncreasingSubsequenceBuiltIn(arr2));
        System.out.println("DP方法输出: " + longestIncreasingSubsequenceDP(arr2));
        System.out.println("期望: 4");
        System.out.println();
        
        // 测试用例3：严格递增
        int[] arr3 = {1, 2, 3, 4, 5};
        System.out.println("输入: [1,2,3,4,5]");
        System.out.println("标准方法输出: " + longestIncreasingSubsequence(arr3));
        System.out.println("简化方法输出: " + longestIncreasingSubsequenceSimple(arr3));
        System.out.println("内置方法输出: " + longestIncreasingSubsequenceBuiltIn(arr3));
        System.out.println("DP方法输出: " + longestIncreasingSubsequenceDP(arr3));
        System.out.println("期望: 5");
        System.out.println();
        
        // 测试用例4：严格递减
        int[] arr4 = {5, 4, 3, 2, 1};
        System.out.println("输入: [5,4,3,2,1]");
        System.out.println("标准方法输出: " + longestIncreasingSubsequence(arr4));
        System.out.println("简化方法输出: " + longestIncreasingSubsequenceSimple(arr4));
        System.out.println("内置方法输出: " + longestIncreasingSubsequenceBuiltIn(arr4));
        System.out.println("DP方法输出: " + longestIncreasingSubsequenceDP(arr4));
        System.out.println("期望: 1");
        System.out.println();
        
        // 性能测试：大规模数据
        int[] largeArr = new int[100000];
        Random random = new Random();
        for (int i = 0; i < 100000; i++) {
            largeArr[i] = random.nextInt(1000000);
        }
        
        // 只测试优化方法（DP方法会超时）
        long startTime = System.currentTimeMillis();
        int result1 = longestIncreasingSubsequence(largeArr);
        long endTime = System.currentTimeMillis();
        System.out.println("标准方法处理100000个元素耗时: " + (endTime - startTime) + "ms");
        
        startTime = System.currentTimeMillis();
        int result2 = longestIncreasingSubsequenceSimple(largeArr);
        endTime = System.currentTimeMillis();
        System.out.println("简化方法处理100000个元素耗时: " + (endTime - startTime) + "ms");
        
        startTime = System.currentTimeMillis();
        int result3 = longestIncreasingSubsequenceBuiltIn(largeArr);
        endTime = System.currentTimeMillis();
        System.out.println("内置方法处理100000个元素耗时: " + (endTime - startTime) + "ms");
        
        System.out.println("三种优化方法结果是否一致: " + 
            (result1 == result2 && result2 == result3));
        
        // 小规模数据测试DP方法
        int[] smallArr = Arrays.copyOf(largeArr, 1000);
        startTime = System.currentTimeMillis();
        int result4 = longestIncreasingSubsequenceDP(smallArr);
        endTime = System.currentTimeMillis();
        System.out.println("DP方法处理1000个元素耗时: " + (endTime - startTime) + "ms");
        System.out.println("DP方法与优化方法结果是否一致: " + (result1 == result4));
    }
}