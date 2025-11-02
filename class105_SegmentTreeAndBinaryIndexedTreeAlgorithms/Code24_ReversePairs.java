package class132;

import java.util.*;

/**
 * LeetCode 493. 翻转对 (Reverse Pairs)
 * 
 * 题目描述：
 * 给定一个数组 nums ，如果 i < j 且 nums[i] > 2*nums[j] 我们就将 (i,j) 称作一个重要翻转对。
 * 你需要返回给定数组中的重要翻转对的数量。
 * 
 * 解题思路：
 * 使用树状数组（Fenwick Tree）+ 离散化来高效统计重要翻转对的数量。
 * 核心思想：
 * 1. 从右向左遍历数组
 * 2. 对于每个元素 nums[i]，需要统计右侧已经遍历过的元素中满足 nums[i] > 2*nums[j] 的个数
 * 3. 使用树状数组维护已经遍历过的元素的出现次数
 * 4. 通过离散化处理大数值范围问题
 * 
 * 时间复杂度分析：
 * - 离散化处理：O(n log n)
 * - 树状数组操作：O(n log n)
 * - 总时间复杂度：O(n log n)
 * 
 * 空间复杂度分析：
 * - 离散化数组：O(n)
 * - 树状数组：O(n)
 * - 总空间复杂度：O(n)
 * 
 * 工程化考量：
 * 1. 边界条件处理：处理空数组、单个元素等情况
 * 2. 数值溢出处理：使用long类型避免整数溢出
 * 3. 离散化优化：使用TreeSet去重并排序
 * 4. 异常处理：检查输入参数合法性
 * 
 * 算法技巧：
 * - 离散化：将大范围的数值映射到小范围的索引
 * - 树状数组：高效统计元素出现次数
 * - 逆序遍历：从右向左处理，便于统计右侧元素
 * - 二分查找：快速定位满足条件的边界
 * 
 * 适用场景：
 * - 需要统计数组中满足特定条件的翻转对数量
 * - 数值范围较大，需要离散化处理
 * - 对时间复杂度要求较高的场景
 * 
 * 测试用例：
 * 输入：nums = [1,3,2,3,1]
 * 输出：2
 * 解释：两个重要翻转对：(1,4) 和 (3,4)
 */
public class Code24_ReversePairs {
    
    /**
     * 计算重要翻转对的数量
     * 
     * @param nums 输入数组
     * @return 重要翻转对的数量
     * 
     * 算法步骤：
     * 1. 离散化处理数组元素
     * 2. 从右向左遍历数组
     * 3. 对于每个元素 nums[i]，需要找到满足 nums[i] > 2*nums[j] 的 nums[j] 的范围
     * 4. 使用树状数组统计该范围内的元素个数
     * 5. 更新树状数组，记录当前元素的出现
     */
    public int reversePairs(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        int n = nums.length;
        
        // 1. 离散化处理：收集所有需要离散化的值
        TreeSet<Long> set = new TreeSet<>();
        for (int num : nums) {
            set.add((long) num);
            set.add(2L * num);
        }
        
        // 构建离散化映射
        Map<Long, Integer> map = new HashMap<>();
        int idx = 1;
        for (long num : set) {
            map.put(num, idx++);
        }
        
        // 2. 使用树状数组统计
        FenwickTree tree = new FenwickTree(map.size());
        int count = 0;
        
        // 从右向左遍历
        for (int i = n - 1; i >= 0; i--) {
            long currentNum = nums[i];
            long target = currentNum - 1; // 我们需要找到小于等于 target 的值
            
            // 找到满足 nums[i] > 2*nums[j] 的最大 nums[j]
            // 即：nums[j] < nums[i] / 2.0
            long maxAllowed = (long) Math.floor((currentNum - 1) / 2.0);
            
            // 如果 maxAllowed 在离散化映射中不存在，需要找到最大的小于等于 maxAllowed 的值
            Long floorKey = set.floor(maxAllowed);
            if (floorKey != null) {
                int targetIdx = map.get(floorKey);
                count += tree.query(targetIdx);
            }
            
            // 更新树状数组，记录当前元素的出现
            int currentIdx = map.get(currentNum);
            tree.update(currentIdx, 1);
        }
        
        return count;
    }
    
    /**
     * 方法二：使用归并排序统计翻转对（备选方案）
     * 
     * 解题思路：
     * 1. 使用归并排序的过程统计重要翻转对的数量
     * 2. 在合并两个有序数组之前，先统计满足条件的翻转对
     * 3. 这种方法同样具有 O(n log n) 的时间复杂度
     * 
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(n)
     */
    public int reversePairsMergeSort(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        return mergeSort(nums, 0, nums.length - 1);
    }
    
    private int mergeSort(int[] nums, int left, int right) {
        if (left >= right) {
            return 0;
        }
        
        int mid = left + (right - left) / 2;
        int count = mergeSort(nums, left, mid) + mergeSort(nums, mid + 1, right);
        
        // 统计满足条件的翻转对
        int j = mid + 1;
        for (int i = left; i <= mid; i++) {
            while (j <= right && (long) nums[i] > 2L * nums[j]) {
                j++;
            }
            count += j - (mid + 1);
        }
        
        // 合并两个有序数组
        merge(nums, left, mid, right);
        
        return count;
    }
    
    private void merge(int[] nums, int left, int mid, int right) {
        int[] temp = new int[right - left + 1];
        int i = left, j = mid + 1, k = 0;
        
        while (i <= mid && j <= right) {
            if (nums[i] <= nums[j]) {
                temp[k++] = nums[i++];
            } else {
                temp[k++] = nums[j++];
            }
        }
        
        while (i <= mid) {
            temp[k++] = nums[i++];
        }
        
        while (j <= right) {
            temp[k++] = nums[j++];
        }
        
        System.arraycopy(temp, 0, nums, left, temp.length);
    }
    
    /**
     * 树状数组（Fenwick Tree）实现
     * 用于高效统计元素出现次数
     */
    private static class FenwickTree {
        private int[] tree;
        private int size;
        
        /**
         * 构造函数
         * 
         * @param size 树状数组大小
         */
        public FenwickTree(int size) {
            this.size = size;
            this.tree = new int[size + 1];
        }
        
        /**
         * 更新操作：在指定位置增加一个值
         * 
         * @param index 位置索引
         * @param delta 增加值
         */
        public void update(int index, int delta) {
            while (index <= size) {
                tree[index] += delta;
                index += lowbit(index);
            }
        }
        
        /**
         * 查询前缀和：从1到index的和
         * 
         * @param index 位置索引
         * @return 前缀和
         */
        public int query(int index) {
            int sum = 0;
            while (index > 0) {
                sum += tree[index];
                index -= lowbit(index);
            }
            return sum;
        }
        
        /**
         * 计算lowbit（最低位的1）
         * 
         * @param x 输入数字
         * @return lowbit值
         */
        private int lowbit(int x) {
            return x & -x;
        }
    }
    
    /**
     * 测试方法：验证算法正确性
     * 
     * 测试用例设计：
     * 1. 正常情况测试
     * 2. 边界情况测试
     * 3. 空数组测试
     * 4. 大数值测试
     */
    public static void main(String[] args) {
        Code24_ReversePairs solution = new Code24_ReversePairs();
        
        // 测试用例1：正常情况
        int[] nums1 = {1, 3, 2, 3, 1};
        int result1 = solution.reversePairs(nums1);
        System.out.println("测试用例1结果：" + result1 + " (期望：2)");
        
        // 测试用例2：边界情况
        int[] nums2 = {2, 4, 3, 5, 1};
        int result2 = solution.reversePairs(nums2);
        System.out.println("测试用例2结果：" + result2 + " (期望：3)");
        
        // 测试用例3：空数组
        int[] nums3 = {};
        int result3 = solution.reversePairs(nums3);
        System.out.println("测试用例3结果：" + result3 + " (期望：0)");
        
        // 测试用例4：大数值
        int[] nums4 = {2147483647, 2147483647, 2147483647, 2147483647, 2147483647};
        int result4 = solution.reversePairs(nums4);
        System.out.println("测试用例4结果：" + result4 + " (期望：0)");
        
        // 测试用例5：负数情况
        int[] nums5 = {-5, -5};
        int result5 = solution.reversePairs(nums5);
        System.out.println("测试用例5结果：" + result5 + " (期望：1)");
        
        System.out.println("所有测试用例执行完成！");
    }
}