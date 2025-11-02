package class132;

import java.util.*;

// LeetCode 315. 计算右侧小于当前元素的个数
// 给定一个整数数组 nums，按要求返回一个新数组 counts。
// 数组 counts 有该性质：counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。
// 测试链接: https://leetcode.cn/problems/count-of-smaller-numbers-after-self/

public class Code12_CountSmallerNumbersAfterSelf_Enhanced {

    /**
     * 计算右侧小于当前元素的个数 - 树状数组解法
     * 
     * 解题思路:
     * 1. 题目要求计算每个元素右侧小于它的元素个数
     * 2. 我们可以将问题转化为：对于每个元素nums[i]，统计有多少个元素nums[j] (j > i)满足 nums[j] < nums[i]
     * 3. 使用树状数组可以高效地进行这类统计
     * 4. 具体步骤：
     *    a. 将数组元素离散化，以处理可能的大范围数值
     *    b. 从右到左遍历数组，对于每个元素nums[i]：
     *       - 查询树状数组中小于nums[i]的元素个数（即前缀和）
     *       - 将nums[i]加入树状数组
     *    c. 这样就能保证每次查询的都是当前元素右侧的元素
     * 
     * 时间复杂度分析:
     * - 离散化: O(n log n)
     * - 构建和操作树状数组: O(n log n)
     * - 总时间复杂度: O(n log n)
     * 
     * 空间复杂度分析:
     * - 树状数组: O(n)
     * - 离散化数组和映射: O(n)
     * - 结果数组: O(n)
     * - 总空间复杂度: O(n)
     * 
     * 工程化考量:
     * 1. 离散化处理：由于输入数组可能包含很大范围的整数，离散化可以有效减少空间使用
     * 2. 边界条件处理：处理空数组、单元素数组等特殊情况
     * 3. 数据类型溢出：使用long类型避免中间计算时的溢出
     * 4. 异常输入检查：验证输入的有效性
     * 
     * 算法优化点:
     * 1. 离散化时只处理必要的数值，节省空间
     * 2. 从右到左遍历，确保正确统计右侧元素
     * 3. 树状数组的lowbit操作高效计算区间和
     */

    // 树状数组类
    static class FenwickTree {
        private int[] tree;  // 树状数组
        private int size;    // 数组大小

        /**
         * 构造函数
         * @param size 树状数组大小
         */
        public FenwickTree(int size) {
            this.size = size;
            this.tree = new int[size + 1];  // 树状数组下标从1开始
        }

        /**
         * lowbit操作，获取x的二进制表示中最低位的1所代表的值
         * @param x 输入整数
         * @return x & (-x)
         */
        private int lowbit(int x) {
            return x & (-x);
        }

        /**
         * 在指定位置增加delta
         * @param index 索引位置（从1开始）
         * @param delta 增加的值
         */
        public void update(int index, int delta) {
            // 沿树状数组向上更新所有相关节点
            while (index <= size) {
                tree[index] += delta;
                index += lowbit(index);
            }
        }

        /**
         * 查询前缀和[1, index]
         * @param index 查询的右边界（从1开始）
         * @return 前缀和
         */
        public int query(int index) {
            int sum = 0;
            // 沿树状数组向下累加所有相关节点的值
            while (index > 0) {
                sum += tree[index];
                index -= lowbit(index);
            }
            return sum;
        }
    }

    /**
     * 计算右侧小于当前元素的个数
     * @param nums 输入数组
     * @return 结果数组
     */
    public List<Integer> countSmaller(int[] nums) {
        // 边界条件检查
        if (nums == null || nums.length == 0) {
            return new ArrayList<>();
        }

        int n = nums.length;
        List<Integer> result = new ArrayList<>(n);

        // 离散化处理
        // 1. 收集所有可能的数值
        Set<Long> valuesSet = new HashSet<>();
        for (int num : nums) {
            valuesSet.add((long) num);
        }

        // 2. 排序并去重
        List<Long> sortedValues = new ArrayList<>(valuesSet);
        Collections.sort(sortedValues);

        // 3. 建立值到索引的映射
        Map<Long, Integer> valueToIndex = new HashMap<>();
        for (int i = 0; i < sortedValues.size(); i++) {
            valueToIndex.put(sortedValues.get(i), i + 1);  // 索引从1开始
        }

        // 创建树状数组
        FenwickTree fenwickTree = new FenwickTree(sortedValues.size());

        // 从右到左遍历数组
        for (int i = n - 1; i >= 0; i--) {
            long currentValue = (long) nums[i];
            // 查询比当前值小的元素个数
            int count = 0;
            // 找到当前值在离散化数组中的位置
            int index = valueToIndex.get(currentValue);
            // 查询比当前值小的元素个数，即查询[1, index-1]的前缀和
            if (index > 1) {
                count = fenwickTree.query(index - 1);
            }
            // 将结果添加到列表（注意后续需要反转）
            result.add(count);
            // 将当前值加入树状数组
            fenwickTree.update(index, 1);
        }

        // 反转结果列表，因为我们是从右到左计算的
        Collections.reverse(result);
        return result;
    }

    /**
     * 另一种解法：归并排序过程中计算逆序对
     * 这种方法也能在O(n log n)时间内解决问题
     */
    public List<Integer> countSmallerMergeSort(int[] nums) {
        int n = nums.length;
        List<Integer> result = new ArrayList<>(Collections.nCopies(n, 0));
        if (n == 0) return result;
        
        // 创建索引数组，用于跟踪元素原始位置
        int[] indexes = new int[n];
        for (int i = 0; i < n; i++) {
            indexes[i] = i;
        }
        
        // 归并排序过程中计算右侧小于当前元素的个数
        mergeSort(nums, indexes, 0, n - 1, result);
        return result;
    }
    
    private void mergeSort(int[] nums, int[] indexes, int left, int right, List<Integer> result) {
        if (left >= right) return;
        
        int mid = left + (right - left) / 2;
        mergeSort(nums, indexes, left, mid, result);
        mergeSort(nums, indexes, mid + 1, right, result);
        merge(nums, indexes, left, mid, right, result);
    }
    
    private void merge(int[] nums, int[] indexes, int left, int mid, int right, List<Integer> result) {
        int[] tempIndexes = new int[right - left + 1];
        int i = left, j = mid + 1, k = 0;
        
        // 合并两个有序数组，并计算右侧小于当前元素的个数
        while (i <= mid && j <= right) {
            if (nums[indexes[i]] <= nums[indexes[j]]) {
                // 右侧比当前元素小的数量为j - (mid + 1)
                result.set(indexes[i], result.get(indexes[i]) + (j - (mid + 1)));
                tempIndexes[k++] = indexes[i++];
            } else {
                tempIndexes[k++] = indexes[j++];
            }
        }
        
        // 处理剩余元素
        while (i <= mid) {
            result.set(indexes[i], result.get(indexes[i]) + (j - (mid + 1)));
            tempIndexes[k++] = indexes[i++];
        }
        
        while (j <= right) {
            tempIndexes[k++] = indexes[j++];
        }
        
        // 将临时数组复制回原数组
        System.arraycopy(tempIndexes, 0, indexes, left, tempIndexes.length);
    }

    /**
     * 暴力解法（仅供比较，时间复杂度较高）
     * 时间复杂度: O(n²)
     * 空间复杂度: O(n)
     */
    public List<Integer> countSmallerBruteForce(int[] nums) {
        List<Integer> result = new ArrayList<>();
        if (nums == null || nums.length == 0) {
            return result;
        }
        
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            int count = 0;
            for (int j = i + 1; j < n; j++) {
                if (nums[j] < nums[i]) {
                    count++;
                }
            }
            result.add(count);
        }
        return result;
    }

    // 测试方法
    public static void main(String[] args) {
        Code12_CountSmallerNumbersAfterSelf_Enhanced solution = new Code12_CountSmallerNumbersAfterSelf_Enhanced();
        
        // 测试用例1
        int[] nums1 = {5, 2, 6, 1};
        System.out.println("测试用例1:");
        System.out.println("输入: [5, 2, 6, 1]");
        System.out.println("树状数组解法结果: " + solution.countSmaller(nums1));  // 期望输出: [2, 1, 1, 0]
        
        // 测试用例2
        int[] nums2 = {-1, -1};
        System.out.println("\n测试用例2:");
        System.out.println("输入: [-1, -1]");
        System.out.println("树状数组解法结果: " + solution.countSmaller(nums2));  // 期望输出: [0, 0]
        
        // 测试用例3 - 空数组
        int[] nums3 = {};
        System.out.println("\n测试用例3:");
        System.out.println("输入: []");
        System.out.println("树状数组解法结果: " + solution.countSmaller(nums3));  // 期望输出: []
        
        // 测试用例4 - 大规模数据
        int size = 1000;
        int[] nums4 = new int[size];
        for (int i = 0; i < size; i++) {
            nums4[i] = size - i;
        }
        System.out.println("\n测试用例4 (大规模逆序数组):");
        System.out.println("数组长度: " + size);
        
        long startTime1 = System.currentTimeMillis();
        List<Integer> result1 = solution.countSmaller(nums4);
        long endTime1 = System.currentTimeMillis();
        System.out.println("树状数组解法耗时: " + (endTime1 - startTime1) + "ms");
        
        long startTime2 = System.currentTimeMillis();
        List<Integer> result2 = solution.countSmallerMergeSort(nums4);
        long endTime2 = System.currentTimeMillis();
        System.out.println("归并排序解法耗时: " + (endTime2 - startTime2) + "ms");
        
        // 验证两种方法结果是否一致
        System.out.println("两种方法结果一致: " + result1.equals(result2));
        
        // 对比暴力解法（仅在小规模数据上测试）
        if (size <= 1000) {
            long startTime3 = System.currentTimeMillis();
            List<Integer> result3 = solution.countSmallerBruteForce(nums4);
            long endTime3 = System.currentTimeMillis();
            System.out.println("暴力解法耗时: " + (endTime3 - startTime3) + "ms");
            System.out.println("暴力解法与树状数组解法结果一致: " + result1.equals(result3));
        }
    }
}