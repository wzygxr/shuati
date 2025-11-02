package class175.随机化与复杂度分析;

import java.util.Random;

/**
 * 随机化选择算法（Randomized Quick Select）
 * 算法思想：基于快速排序的思想，随机选择pivot，将数组分区，直到找到第k小的元素
 * 时间复杂度：期望 O(n)，最坏 O(n²)
 * 空间复杂度：O(log n) - 递归调用栈
 * 
 * 相关题目：
 * 1. LeetCode 215. 数组中的第K个最大元素 - https://leetcode-cn.com/problems/kth-largest-element-in-an-array/
 * 2. LintCode 5. 第k大元素 - https://www.lintcode.com/problem/5/
 * 3. CodeChef - KTHMAX - https://www.codechef.com/problems/KTHMAX
 * 4. HackerRank - Kth Largest Element - https://www.hackerrank.com/challenges/find-the-running-median/problem
 */
public class RandomizedSelect {
    private final Random random;

    public RandomizedSelect() {
        this.random = new Random();
    }

    /**
     * 查找数组中第k小的元素（k从1开始计数）
     * @param array 输入数组
     * @param k 第k小
     * @return 第k小的元素值
     */
    public int findKthSmallest(int[] array, int k) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("数组不能为空");
        }
        if (k < 1 || k > array.length) {
            throw new IllegalArgumentException("k的取值范围应为[1, " + array.length + "]");
        }

        return randomizedSelect(array, 0, array.length - 1, k - 1);
    }

    /**
     * 递归实现随机化选择
     * @param array 数组
     * @param left 左边界
     * @param right 右边界
     * @param index 目标索引（第index小，从0开始）
     * @return 目标元素
     */
    private int randomizedSelect(int[] array, int left, int right, int index) {
        if (left == right) {
            return array[left];
        }

        // 随机选择pivot并分区
        int pivotIndex = randomizedPartition(array, left, right);

        if (index == pivotIndex) {
            // 找到目标位置
            return array[index];
        } else if (index < pivotIndex) {
            // 在左半部分查找
            return randomizedSelect(array, left, pivotIndex - 1, index);
        } else {
            // 在右半部分查找
            return randomizedSelect(array, pivotIndex + 1, right, index);
        }
    }

    /**
     * 随机化分区函数
     * @param array 数组
     * @param left 左边界
     * @param right 右边界
     * @return pivot的最终位置
     */
    private int randomizedPartition(int[] array, int left, int right) {
        // 随机选择pivot位置
        int randomIndex = left + random.nextInt(right - left + 1);
        // 将pivot交换到末尾
        swap(array, randomIndex, right);
        
        return partition(array, left, right);
    }

    /**
     * 分区函数
     * @param array 数组
     * @param left 左边界
     * @param right 右边界
     * @return pivot的最终位置
     */
    private int partition(int[] array, int left, int right) {
        int pivot = array[right];
        int i = left - 1;

        for (int j = left; j < right; j++) {
            if (array[j] <= pivot) {
                i++;
                swap(array, i, j);
            }
        }
        
        swap(array, i + 1, right);
        return i + 1;
    }

    /**
     * 交换数组中两个元素
     */
    private void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        RandomizedSelect selector = new RandomizedSelect();
        int[] array = {3, 2, 1, 5, 6, 4};
        int k = 2;
        
        int result = selector.findKthSmallest(array, k);
        System.out.println("数组中第" + k + "小的元素是：" + result);
        
        // 验证结果
        System.out.print("原数组：");
        for (int num : array) {
            System.out.print(num + " ");
        }
        System.out.println();
    }
}