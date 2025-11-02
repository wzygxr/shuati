package class023;

// 剑指 Offer 40. 最小的k个数
// 测试链接 : https://leetcode.cn/problems/zui-xiao-de-kge-shu-lcof/
// 题目描述: 输入整数数组 arr ，找出其中最小的 k 个数。
// 解题思路: 使用快速选择算法或者快速排序算法找出最小的k个数。

/*
 * 补充题目列表:
 * 
 * 1. 剑指 Offer 40. 最小的k个数
 *    链接: https://leetcode.cn/problems/zui-xiao-de-kge-shu-lcof/
 *    题目描述: 输入整数数组 arr ，找出其中最小的 k 个数。
 *    解题思路: 使用快速选择算法或者快速排序算法找出最小的k个数。
 * 
 * 2. LeetCode 215. 数组中的第K个最大元素
 *    链接: https://leetcode.cn/problems/kth-largest-element-in-an-array/
 *    题目描述: 给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素。
 *    解题思路: 使用快速选择算法，在快速排序的基础上进行优化，只处理包含目标元素的区间。
 * 
 * 3. LeetCode 703. 数据流中的第K大元素
 *    链接: https://leetcode.cn/problems/kth-largest-element-in-a-stream/
 *    题目描述: 设计一个找到数据流中第 k 大元素的类。
 *    解题思路: 使用最小堆维护前k大的元素。
 * 
 * 4. LeetCode 347. 前 K 个高频元素
 *    链接: https://leetcode.cn/problems/top-k-frequent-elements/
 *    题目描述: 给你一个整数数组 nums 和一个整数 k ，请你返回其中出现频率前 k 高的元素。
 *    解题思路: 使用堆或者快速选择算法来找出前k个高频元素。
 * 
 * 5. LeetCode 973. 最接近原点的 K 个点
 *    链接: https://leetcode.cn/problems/k-closest-points-to-origin/
 *    题目描述: 给定一个points数组和整数K，返回最接近原点的K个点。
 *    解题思路: 计算每个点到原点的距离，然后使用快速选择算法找出最小的K个距离。
 * 
 * 6. 牛客网 - 最小的k个数
 *    链接: https://www.nowcoder.com/practice/6a296eb82cf844ca8539b57c23e6e9bf
 *    题目描述: 输入n个整数，找出其中最小的K个数。
 *    解题思路: 使用快速选择算法或者堆来解决。
 * 
 * 7. LeetCode 324. 摆动排序 II
 *    链接: https://leetcode.cn/problems/wiggle-sort-ii/
 *    题目描述: 给你一个整数数组 nums，将它重新排列成 nums[0] < nums[1] > nums[2] < nums[3]... 的顺序。
 *    解题思路: 先排序，然后通过特定的索引映射来构造摆动序列。
 * 
 * 8. LeetCode 215. Kth Largest Element in an Array (重复题目，但提供更多解法)
 *    链接: https://leetcode.cn/problems/kth-largest-element-in-an-array/
 *    解题思路: 
 *      方法1: 快速选择算法（平均时间复杂度O(n)）
 *      方法2: 堆排序（时间复杂度O(n log k)）
 *      方法3: 全排序后取第k个（时间复杂度O(n log n)）
 * 
 * 算法复杂度分析:
 * 时间复杂度:
 *   - 快速排序: O(n log n)
 *   - 快速选择: O(n) 平均情况
 *   - 堆排序: O(n log k)
 * 空间复杂度:
 *   - O(log n) - 递归调用栈的深度（快速选择/快速排序）
 *   - O(k) - 堆的存储空间
 * 
 * 算法优化策略:
 * 1. 随机选择基准值 - 避免最坏情况的出现
 * 2. 三路快排 - 处理重复元素较多的情况
 * 3. 剪枝优化 - 只处理包含目标元素的区间
 * 4. 堆优化 - 使用最小堆维护k个最大元素
 * 
 * 工程化考量:
 * 1. 异常处理: 处理k超出数组长度的情况
 * 2. 性能优化: 对于小数组使用插入排序优化
 * 3. 内存使用: 原地排序减少额外空间开销
 * 4. 稳定性: 快速选择算法不稳定，如需稳定需特殊处理
 * 
 * 调试技巧:
 * 1. 打印中间过程: 在分区操作后打印数组状态
 * 2. 断言验证: 验证分区后各部分的正确性
 * 3. 边界测试: 测试k=0, k=n等边界情况
 * 
 * 面试技巧:
 * 1. 理解快速选择与堆排序的比较
 * 2. 掌握不同算法在不同数据规模下的性能表现
 * 3. 理解各种算法的适用场景
 * 4. 能够根据具体需求选择合适的算法
 */

import java.util.Arrays;

public class Code04_QuickSort_JZ40 {

    /**
     * 获取数组中最小的k个数
     * 算法核心思想：使用快速选择算法找出最小的k个数
     * @param arr 输入数组
     * @param k 需要返回的最小数的个数
     * @return 包含最小k个数的数组
     */
    public int[] getLeastNumbers(int[] arr, int k) {
        // 边界条件检查：如果k大于等于数组长度，直接返回整个数组
        if (k >= arr.length) {
            return arr;
        }
        
        // 使用快速排序方法获取最小的k个数
        return quickSort(arr, 0, arr.length - 1, k);
    }

    /**
     * 快速排序方法获取最小的k个数
     * 与标准快速排序不同：只处理包含目标元素的子数组
     * @param arr 数组
     * @param l 当前处理区间的左边界（包含）
     * @param r 当前处理区间的右边界（包含）
     * @param k 需要返回的最小数的个数
     * @return 包含最小k个数的数组
     */
    private int[] quickSort(int[] arr, int l, int r, int k) {
        // 随机选择基准值，避免最坏情况
        int i = l + (int) (Math.random() * (r - l + 1));
        
        // 将随机选择的基准值交换到第一个位置，便于后续分区操作
        swap(arr, l, i);
        
        // 基准值
        int temp = arr[l];
        
        // 双指针分区：left从左向右扫描，right从右向左扫描
        int left = l, right = r;
        
        // 双指针分区操作
        while (left < right) {
            // 从右向左找小于基准值的元素
            while (left < right && arr[right] >= temp) {
                right--;
            }
            
            // 从左向右找大于基准值的元素
            while (left < right && arr[left] <= temp) {
                left++;
            }
            
            // 如果left < right，说明找到了需要交换的元素对
            if (left < right) {
                // 交换元素
                swap(arr, left, right);
            }
        }
        
        // 将基准值放到正确位置（left == right时的位置）
        swap(arr, l, left);
        
        // 根据分区点位置决定下一步操作
        if (k < left) {
            // 如果k小于分区点位置，说明前k个最小元素都在左半部分
            // 在左半部分继续查找
            return quickSort(arr, l, left - 1, k);
        }
        if (k > left) {
            // 如果k大于分区点位置，说明左半部分的所有元素（包括基准值）都是前k个最小元素
            // 但还需要在右半部分找剩余的元素
            // 在右半部分继续查找
            return quickSort(arr, left + 1, r, k);
        }
        
        // 如果k等于分区点位置，说明前k个最小元素正好是数组的前k个元素
        // 直接返回前k个元素
        return Arrays.copyOf(arr, k);
    }

    /**
     * 交换数组中两个元素的位置
     * @param arr 数组
     * @param i 索引1
     * @param j 索引2
     */
    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    /**
     * 测试用例和验证代码
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        Code04_QuickSort_JZ40 solution = new Code04_QuickSort_JZ40();
        
        // 测试用例1：普通情况
        // 数组: [3, 2, 1]，排序后为[1, 2, 3]
        // 最小的2个数是[1, 2]
        int[] arr1 = {3, 2, 1};
        int k1 = 2;
        int[] result1 = solution.getLeastNumbers(arr1, k1);
        System.out.println("数组: [3, 2, 1], k=2, 最小的2个数是: " + Arrays.toString(result1)); // 输出: [1, 2] 或 [2, 1]
        
        // 测试用例2：边界情况-k=1
        // 数组: [0, 1, 2, 1]，排序后为[0, 1, 1, 2]
        // 最小的1个数是[0]
        int[] arr2 = {0, 1, 2, 1};
        int k2 = 1;
        int[] result2 = solution.getLeastNumbers(arr2, k2);
        System.out.println("数组: [0, 1, 2, 1], k=1, 最小的1个数是: " + Arrays.toString(result2)); // 输出: [0]
        
        // 测试用例3：k等于数组长度
        int[] arr3 = {0, 1, 2, 1};
        int k3 = 4;
        int[] result3 = solution.getLeastNumbers(arr3, k3);
        System.out.println("数组: [0, 1, 2, 1], k=4, 最小的4个数是: " + Arrays.toString(result3)); // 输出: [0, 1, 2, 1]
        
        // 测试用例4：k为0
        int[] arr4 = {0, 1, 2, 1};
        int k4 = 0;
        int[] result4 = solution.getLeastNumbers(arr4, k4);
        System.out.println("数组: [0, 1, 2, 1], k=0, 最小的0个数是: " + Arrays.toString(result4)); // 输出: []
    }
}