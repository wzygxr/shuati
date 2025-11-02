package class023;

// LeetCode 215. 数组中的第K个最大元素
// 测试链接 : https://leetcode.cn/problems/kth-largest-element-in-an-array/
// 题目描述: 给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素。
// 解题思路: 使用快速选择算法，在快速排序的基础上进行优化，只处理包含目标元素的区间。

/*
 * 补充题目列表:
 * 
 * 1. LeetCode 215. 数组中的第K个最大元素
 *    链接: https://leetcode.cn/problems/kth-largest-element-in-an-array/
 *    题目描述: 给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素。
 *    解题思路: 使用快速选择算法，在快速排序的基础上进行优化，只处理包含目标元素的区间。
 * 
 * 2. LeetCode 347. 前 K 个高频元素
 *    链接: https://leetcode.cn/problems/top-k-frequent-elements/
 *    题目描述: 给你一个整数数组 nums 和一个整数 k ，请你返回其中出现频率前 k 高的元素。
 *    解题思路: 使用堆或者快速选择算法来找出前k个高频元素。
 * 
 * 3. LeetCode 973. 最接近原点的 K 个点
 *    链接: https://leetcode.cn/problems/k-closest-points-to-origin/
 *    题目描述: 给定一个points数组和整数K，返回最接近原点的K个点。
 *    解题思路: 计算每个点到原点的距离，然后使用快速选择算法找出最小的K个距离。
 * 
 * 4. LeetCode 324. 摆动排序 II
 *    链接: https://leetcode.cn/problems/wiggle-sort-ii/
 *    题目描述: 给你一个整数数组 nums，将它重新排列成 nums[0] < nums[1] > nums[2] < nums[3]... 的顺序。
 *    解题思路: 先排序，然后通过特定的索引映射来构造摆动序列。
 * 
 * 5. LeetCode 414. 第三大的数
 *    链接: https://leetcode.cn/problems/third-maximum-number/
 *    题目描述: 给你一个非空数组，返回此数组中第三大的数。
 *    解题思路: 使用一次遍历维护三个最大值，或者使用快速选择算法。
 * 
 * 6. LeetCode 462. 最少移动次数使数组元素相等 II
 *    链接: https://leetcode.cn/problems/minimum-moves-to-equal-array-elements-ii/
 *    题目描述: 给你一个长度为 n 的整数数组 nums ，返回使所有数组元素相等需要的最少移动数。
 *    解题思路: 找到中位数，所有元素向中位数移动的步数之和最小。
 * 
 * 7. LeetCode 703. 数据流中的第K大元素
 *    链接: https://leetcode.cn/problems/kth-largest-element-in-a-stream/
 *    题目描述: 设计一个找到数据流中第 k 大元素的类。
 *    解题思路: 使用最小堆维护前k大的元素。
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
 *   - 最好情况: O(n) - 每次划分都能将数组平均分成两部分
 *   - 平均情况: O(n) - 随机选择基准值的情况下
 *   - 最坏情况: O(n^2) - 每次选择的基准值都是最大或最小值
 * 空间复杂度:
 *   - O(log n) - 递归调用栈的深度
 * 
 * 算法优化策略:
 * 1. 随机选择基准值 - 避免最坏情况的出现
 * 2. 三路快排 - 处理重复元素较多的情况
 * 3. 剪枝优化 - 只处理包含目标元素的区间
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
 * 3. 边界测试: 测试k=1, k=n等边界情况
 * 
 * 面试技巧:
 * 1. 理解快速选择与二分查找的区别
 * 2. 掌握快速选择的优化方法（随机化、三路快排等）
 * 3. 理解快速选择在不同数据分布下的性能表现
 * 4. 能够分析快速选择的适用场景
 */

public class Code03_QuickSort_Leetcode215 {

    /**
     * 查找数组中第k个最大的元素
     * 算法核心思想：第k大元素等价于排序后第(nums.length - k)小元素
     * @param nums 整数数组
     * @param k 第k个最大的元素（1-based）
     * @return 第k个最大的元素值
     */
    public int findKthLargest(int[] nums, int k) {
        // 第k大元素在排序后数组中的索引是nums.length - k（0-based）
        // 例如：数组[1,2,3,4,5,6]，第2大元素是5，索引为6-2=4
        return quickSelect(nums, 0, nums.length - 1, nums.length - k);
    }

    /**
     * 快速选择算法实现
     * 与快速排序的区别：只处理包含目标元素的子数组，而非全部子数组
     * 平均时间复杂度：O(n)，最坏情况：O(n²)
     * @param nums 数组
     * @param l 当前处理区间的左边界（包含）
     * @param r 当前处理区间的右边界（包含）
     * @param index 目标元素在排序后数组中的索引位置
     * @return 目标元素值
     */
    private int quickSelect(int[] nums, int l, int r, int index) {
        // 随机选择基准值进行分区，避免最坏情况
        int q = randomPartition(nums, l, r);
        
        // 如果分区点正好是目标索引，说明找到了目标元素
        if (q == index) {
            // 直接返回目标元素
            return nums[q];
        } else {
            // 根据分区点与目标索引的关系，决定在哪个子数组中继续查找
            // 如果分区点索引小于目标索引，说明目标元素在右半部分
            if (q < index) {
                return quickSelect(nums, q + 1, r, index);
            } 
            // 如果分区点索引大于目标索引，说明目标元素在左半部分
            else {
                return quickSelect(nums, l, q - 1, index);
            }
        }
    }

    /**
     * 随机分区函数
     * 通过随机选择基准值来避免最坏情况的发生
     * @param nums 数组
     * @param l 左边界
     * @param r 右边界
     * @return 分区点索引（基准值在数组中的最终位置）
     */
    private int randomPartition(int[] nums, int l, int r) {
        // 在[l, r]范围内随机选择一个索引作为基准值
        int i = l + (int) (Math.random() * (r - l + 1));
        
        // 将随机选择的基准值交换到末尾位置，便于后续分区操作
        swap(nums, i, r);
        
        // 进行标准分区操作
        return partition(nums, l, r);
    }

    /**
     * 分区函数（标准实现）
     * 将数组分为两部分：小于等于基准值的部分和大于基准值的部分
     * @param nums 数组
     * @param l 左边界
     * @param r 右边界（基准值所在位置）
     * @return 基准值在分区后的最终位置索引
     */
    private int partition(int[] nums, int l, int r) {
        // 选择最后一个元素作为基准值
        int x = nums[r];
        
        // i表示小于等于基准值的区域的右边界（不包含）
        // 初始时小于等于区域为空，所以i = l - 1
        int i = l - 1;
        
        // 遍历数组[l, r-1]，将小于等于基准值的元素放到左侧
        for (int j = l; j < r; ++j) {
            // 如果当前元素小于等于基准值
            if (nums[j] <= x) {
                // 扩展小于等于区域
                i++;
                // 将当前元素交换到小于等于区域
                swap(nums, i, j);
            }
        }
        
        // 将基准值放到小于等于区域的下一个位置（即正确位置）
        swap(nums, i + 1, r);
        
        // 返回基准值的最终索引位置
        return i + 1;
    }

    /**
     * 交换数组中两个元素的位置
     * @param nums 数组
     * @param i 索引1
     * @param j 索引2
     */
    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    /**
     * 测试用例和验证代码
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        Code03_QuickSort_Leetcode215 solution = new Code03_QuickSort_Leetcode215();
        
        // 测试用例1：普通情况
        // 数组: [3, 2, 1, 5, 6, 4]，排序后为[1, 2, 3, 4, 5, 6]
        // 第2大的元素是5（索引为4）
        int[] nums1 = {3, 2, 1, 5, 6, 4};
        int k1 = 2;
        System.out.println("数组: [3, 2, 1, 5, 6, 4], k=2, 第2大的元素是: " + solution.findKthLargest(nums1, k1)); // 输出: 5
        
        // 测试用例2：包含重复元素的情况
        // 数组: [3, 2, 3, 1, 2, 4, 5, 5, 6]，排序后为[1, 2, 2, 3, 3, 4, 5, 5, 6]
        // 第4大的元素是4（索引为5）
        int[] nums2 = {3, 2, 3, 1, 2, 4, 5, 5, 6};
        int k2 = 4;
        System.out.println("数组: [3, 2, 3, 1, 2, 4, 5, 5, 6], k=4, 第4大的元素是: " + solution.findKthLargest(nums2, k2)); // 输出: 4
        
        // 测试用例3：边界情况-k=1（最大元素）
        int[] nums3 = {1, 2, 3, 4, 5};
        int k3 = 1;
        System.out.println("数组: [1, 2, 3, 4, 5], k=1, 最大元素是: " + solution.findKthLargest(nums3, k3)); // 输出: 5
        
        // 测试用例4：边界情况-k=n（最小元素）
        int[] nums4 = {1, 2, 3, 4, 5};
        int k4 = 5;
        System.out.println("数组: [1, 2, 3, 4, 5], k=5, 最小元素是: " + solution.findKthLargest(nums4, k4)); // 输出: 1
    }
}