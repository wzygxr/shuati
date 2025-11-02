package class051;

/**
 * 补充题目：LeetCode 1283. 使结果不超过阈值的最小除数
 * 问题描述：给定一个数组和阈值，找出最小的除数，使得所有元素除以除数的和不超过阈值
 * 解法：二分答案 + 贪心验证
 * 时间复杂度：O(n * log(max))，其中n是数组长度，max是数组中的最大值
 * 空间复杂度：O(1)
 * 链接：https://leetcode.cn/problems/find-the-smallest-divisor-given-a-threshold/
 */
public class Code14_FindSmallestDivisor {
    
    /**
     * 寻找最小的除数，使得所有元素除以除数的和不超过阈值
     * @param nums 输入数组
     * @param threshold 阈值
     * @return 最小的除数
     */
    public int smallestDivisor(int[] nums, int threshold) {
        // 确定二分搜索的范围
        int left = 1; // 最小可能的除数是1
        int right = 0; // 最大可能的除数是数组中的最大值
        for (int num : nums) {
            right = Math.max(right, num);
        }
        
        // 二分搜索
        int result = right; // 初始化为最大值，确保有解
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            
            // 计算当前除数下的和
            long sum = calculateSum(nums, mid);
            
            // 判断是否满足条件
            if (sum <= threshold) {
                // 满足条件，尝试更小的除数
                result = mid;
                right = mid - 1;
            } else {
                // 不满足条件，需要增大除数
                left = mid + 1;
            }
        }
        
        return result;
    }
    
    /**
     * 计算数组元素除以除数的和（向上取整）
     * @param nums 输入数组
     * @param divisor 除数
     * @return 元素除以除数的和
     */
    private long calculateSum(int[] nums, int divisor) {
        long sum = 0;
        for (int num : nums) {
            // (a + b - 1) / b 是对a/b向上取整的经典写法
            sum += (num + divisor - 1) / divisor;
        }
        return sum;
    }
    
    /*
     * 解题思路详解：
     * 1. 这是一个典型的二分答案问题，我们需要找到最小的除数，使得所有元素除以除数的和不超过阈值
     * 2. 除数的可能范围是1到数组中的最大值
     * 3. 对于每个候选除数，我们计算所有元素除以该除数的和（向上取整），并判断是否不超过阈值
     * 4. 如果和不超过阈值，说明可以尝试更小的除数；否则需要增大除数
     * 
     * 工程化考量：
     * 1. 整数溢出：使用long类型来存储sum，避免计算过程中溢出
     * 2. 边界条件：确保除数至少为1，最大为数组中的最大值
     * 3. 性能优化：使用位运算(right - left) >> 1代替除法运算，略微提高效率
     * 
     * 二分答案的关键在于：
     * 1. 确定搜索范围
     * 2. 设计判断函数（这里是calculateSum）
     * 3. 根据判断结果调整搜索范围
     * 
     * 测试用例：
     * - 输入：nums = [1,2,5,9], threshold = 6
     * - 输出：5
     * - 解释：5是最小的除数，使得(1+2+5+9)/5的向上取整和为1+1+1+2=5，不超过6
     */
    
    // 主函数用于测试
    public static void main(String[] args) {
        Code14_FindSmallestDivisor solution = new Code14_FindSmallestDivisor();
        
        // 测试用例1
        int[] nums1 = {1, 2, 5, 9};
        int threshold1 = 6;
        System.out.println("测试用例1: " + solution.smallestDivisor(nums1, threshold1)); // 预期输出：5
        
        // 测试用例2
        int[] nums2 = {44, 22, 33, 11, 1};
        int threshold2 = 5;
        System.out.println("测试用例2: " + solution.smallestDivisor(nums2, threshold2)); // 预期输出：44
        
        // 测试用例3
        int[] nums3 = {2, 3, 5, 7, 11};
        int threshold3 = 11;
        System.out.println("测试用例3: " + solution.smallestDivisor(nums3, threshold3)); // 预期输出：3
    }
}

/**
 * 补充题目：LeetCode 1552. 两球之间的磁力
 * 问题描述：在给定位置放置球，使得任意两球之间的最小磁力最大
 * 解法：二分答案 + 贪心验证
 * 时间复杂度：O(n * log(max-min))
 * 空间复杂度：O(log(n))
 * 链接：https://leetcode.cn/problems/magnetic-force-between-two-balls/
 */
class Code15_MagneticForceBetweenTwoBalls {
    
    /**
     * 计算在给定位置放置球时的最大可能最小磁力
     * @param position 篮子的位置数组
     * @param m 球的数量
     * @return 最大可能的最小磁力
     */
    public int maxDistance(int[] position, int m) {
        // 对位置数组进行排序
        java.util.Arrays.sort(position);
        
        // 确定二分搜索的范围
        int left = 1; // 最小可能的磁力是1
        int right = position[position.length - 1] - position[0]; // 最大可能的磁力是最远两个位置的距离
        
        int result = 0;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            
            // 判断是否能以mid为最小磁力放置m个球
            if (canPlaceBalls(position, m, mid)) {
                // 可以放置，尝试更大的磁力
                result = mid;
                left = mid + 1;
            } else {
                // 不能放置，减小磁力
                right = mid - 1;
            }
        }
        
        return result;
    }
    
    /**
     * 判断是否能以minForce为最小磁力放置m个球
     * @param position 排序后的位置数组
     * @param m 球的数量
     * @param minForce 最小磁力
     * @return 是否可以放置
     */
    private boolean canPlaceBalls(int[] position, int m, int minForce) {
        int count = 1; // 第一个球放在第一个位置
        int lastPos = position[0];
        
        // 贪心策略：尽可能早地放置球
        for (int i = 1; i < position.length; i++) {
            if (position[i] - lastPos >= minForce) {
                count++;
                lastPos = position[i];
                
                // 如果已经放置了m个球，返回true
                if (count == m) {
                    return true;
                }
            }
        }
        
        // 无法放置m个球
        return false;
    }
    
    /*
     * 解题思路详解：
     * 1. 这是一个经典的"最大化最小值"问题，我们需要找到最大的最小磁力
     * 2. 首先对位置数组进行排序，这样可以方便地计算距离
     * 3. 磁力的可能范围是1到最远两个位置的距离
     * 4. 对于每个候选磁力，我们使用贪心策略判断是否能放置m个球
     * 5. 如果可以放置，说明可以尝试更大的磁力；否则需要减小磁力
     * 
     * 工程化考量：
     * 1. 排序是必须的，这有助于贪心策略的实现
     * 2. 贪心策略是正确的，因为我们总是在满足条件的情况下尽早放置球
     * 3. 边界条件处理：确保至少有两个位置和两个球
     * 
     * 测试用例：
     * - 输入：position = [1,2,3,4,7], m = 3
     * - 输出：3
     * - 解释：放置在位置1、4、7，最小磁力为3
     */
}

/**
 * 补充题目：LeetCode 287. 寻找重复数
 * 问题描述：找出数组中重复的数（数组长度为n+1，元素值在1到n之间，且只有一个重复数）
 * 解法：二分答案 + 抽屉原理
 * 时间复杂度：O(n * log n)
 * 空间复杂度：O(1)
 * 链接：https://leetcode.cn/problems/find-the-duplicate-number/
 */
class Code16_FindTheDuplicateNumber {
    
    /**
     * 找出数组中重复的数
     * @param nums 输入数组
     * @return 重复的数
     */
    public int findDuplicate(int[] nums) {
        // 确定二分搜索的范围
        int left = 1;
        int right = nums.length - 1; // 数组长度为n+1，元素值在1到n之间
        
        while (left < right) {
            int mid = left + ((right - left) >> 1);
            
            // 计算数组中小于等于mid的元素个数
            int count = countLessEqual(nums, mid);
            
            // 应用抽屉原理：如果count > mid，说明[1,mid]范围内有重复数
            if (count > mid) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        
        return left;
    }
    
    /**
     * 计算数组中小于等于target的元素个数
     * @param nums 输入数组
     * @param target 目标值
     * @return 小于等于target的元素个数
     */
    private int countLessEqual(int[] nums, int target) {
        int count = 0;
        for (int num : nums) {
            if (num <= target) {
                count++;
            }
        }
        return count;
    }
    
    /*
     * 解题思路详解：
     * 1. 这道题利用了抽屉原理：如果有n+1个物品放进n个抽屉，那么至少有一个抽屉有至少两个物品
     * 2. 我们对数值范围[1,n]进行二分搜索
     * 3. 对于每个中间值mid，我们计算数组中小于等于mid的元素个数
     * 4. 如果count > mid，说明[1,mid]范围内有重复数（因为正常情况下最多有mid个不同的数）
     * 5. 否则，重复数在[mid+1,n]范围内
     * 
     * 工程化考量：
     * 1. 这种解法不修改原数组，符合题目的要求
     * 2. 空间复杂度为O(1)，不需要额外空间
     * 3. 相比于快慢指针的解法，这种方法更容易理解，但时间复杂度稍高
     * 
     * 测试用例：
     * - 输入：nums = [1,3,4,2,2]
     * - 输出：2
     */
}