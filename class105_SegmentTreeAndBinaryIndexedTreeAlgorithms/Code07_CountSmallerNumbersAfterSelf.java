package class132;

import java.util.*;

// LeetCode 315. 计算右侧小于当前元素的个数
// 给你一个整数数组 nums ，按要求返回一个新数组 counts 。
// 数组 counts 有该性质：counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。
// 测试链接: https://leetcode.cn/problems/count-of-smaller-numbers-after-self/

public class Code07_CountSmallerNumbersAfterSelf {

    /**
     * 使用树状数组解决计算右侧小于当前元素的个数问题
     * 
     * 解题思路:
     * 1. 由于元素值范围可能很大，需要进行离散化处理
     * 2. 从右向左遍历数组，对于每个元素，在树状数组中查询比它小的元素个数
     * 3. 然后将当前元素加入树状数组
     * 4. 这样可以保证查询的都是右侧已经遍历过的元素
     * 
     * 时间复杂度分析:
     * - 离散化: O(n log n)
     * - 遍历数组并查询更新: O(n log n)
     * - 总时间复杂度: O(n log n)
     * 
     * 空间复杂度分析:
     * - 树状数组: O(n)
     * - 离散化数组: O(n)
     * - 总空间复杂度: O(n)
     * 
     * 工程化考量:
     * 1. 离散化处理大数值范围
     * 2. 边界条件处理
     * 3. 异常输入检查
     * 4. 详细注释和变量命名
     */

    // 树状数组类
    static class FenwickTree {
        private int[] tree;
        private int n;

        public FenwickTree(int size) {
            this.n = size;
            this.tree = new int[n + 1];
        }

        private int lowbit(int x) {
            return x & (-x);
        }

        // 在位置i上增加值delta
        public void add(int i, int delta) {
            while (i <= n) {
                tree[i] += delta;
                i += lowbit(i);
            }
        }

        // 查询[1, i]的前缀和
        public int query(int i) {
            int sum = 0;
            while (i > 0) {
                sum += tree[i];
                i -= lowbit(i);
            }
            return sum;
        }
    }

    public static List<Integer> countSmaller(int[] nums) {
        int n = nums.length;
        List<Integer> result = new ArrayList<>(Collections.nCopies(n, 0));
        
        // 离散化处理
        // 1. 获取所有不重复的元素并排序
        Set<Integer> uniqueNumbers = new HashSet<>();
        for (int num : nums) {
            uniqueNumbers.add(num);
        }
        
        List<Integer> sortedNumbers = new ArrayList<>(uniqueNumbers);
        Collections.sort(sortedNumbers);
        
        // 2. 建立数值到离散化索引的映射
        Map<Integer, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < sortedNumbers.size(); i++) {
            indexMap.put(sortedNumbers.get(i), i + 1);
        }
        
        // 3. 创建树状数组，大小为离散化后的元素个数
        FenwickTree fenwickTree = new FenwickTree(sortedNumbers.size());
        
        // 4. 从右向左遍历数组
        for (int i = n - 1; i >= 0; i--) {
            // 获取当前元素的离散化索引
            int index = indexMap.get(nums[i]);
            
            // 查询比当前元素小的元素个数
            // 即查询[1, index-1]范围内已存在的元素个数
            result.set(i, fenwickTree.query(index - 1));
            
            // 将当前元素加入树状数组
            fenwickTree.add(index, 1);
        }
        
        return result;
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {5, 2, 6, 1};
        List<Integer> result1 = countSmaller(nums1);
        System.out.println("Input: [5, 2, 6, 1]");
        System.out.println("Output: " + result1); // 期望输出: [2, 1, 1, 0]
        
        // 测试用例2
        int[] nums2 = {-1};
        List<Integer> result2 = countSmaller(nums2);
        System.out.println("Input: [-1]");
        System.out.println("Output: " + result2); // 期望输出: [0]
        
        // 测试用例3
        int[] nums3 = {-1, -1};
        List<Integer> result3 = countSmaller(nums3);
        System.out.println("Input: [-1, -1]");
        System.out.println("Output: " + result3); // 期望输出: [0, 0]
        
        // 测试用例4
        int[] nums4 = {26, 78, 27, 100, 33, 67, 90, 23, 66, 5, 38, 7, 35, 23, 52, 22, 83, 51, 98, 69, 81, 32, 78, 28, 94, 13, 2, 97, 3, 76, 99, 51, 9, 21, 84, 66, 65, 36, 100, 41};
        List<Integer> result4 = countSmaller(nums4);
        System.out.println("Large input test case result size: " + result4.size());
    }
}