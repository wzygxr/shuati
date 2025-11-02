package class128;

// 分割数组的最大值
// 给定一个非负整数数组 nums 和一个整数 m ，你需要将这个数组分成 m 个非空的连续子数组。
// 设计一个算法使得这 m 个子数组各自和的最大值最小。
// 
// 算法思路：
// 这是一个典型的二分答案问题。
// 1. 答案具有单调性：最大值越小，需要分割的子数组越多；最大值越大，需要分割的子数组越少
// 2. 二分搜索答案的范围：左边界是数组中的最大值，右边界是数组元素之和
// 3. 对于每个中间值，使用贪心算法检查是否能将数组分割成不超过 m 个子数组，
//    使得每个子数组的和都不超过该中间值
// 
// 时间复杂度：O(n * log(sum))
// 空间复杂度：O(1)
// 
// 测试链接 : https://leetcode.cn/problems/split-array-largest-sum/

public class Code08_SplitArray {
    
    /**
     * 分割数组使得子数组各自和的最大值最小
     * 
     * @param nums 非负整数数组
     * @param m 分割成的子数组数量
     * @return 分割后子数组各自和的最大值的最小值
     * 
     * 时间复杂度：O(n * log(sum))
     * 空间复杂度：O(1)
     */
    public static int splitArray(int[] nums, int m) {
        // 确定二分搜索的边界
        // 左边界：数组中的最大值（每个元素单独成一组的情况）
        // 右边界：数组元素之和（所有元素成一组的情况）
        long left = 0, right = 0;
        for (int num : nums) {
            right += num;
            left = Math.max(left, num);
        }
        
        long result = right;
        
        // 二分搜索答案
        while (left <= right) {
            long mid = left + (right - left) / 2;
            
            // 检查是否能将数组分割成不超过 m 个子数组，使得每个子数组的和都不超过 mid
            if (canSplit(nums, m, mid)) {
                result = mid;
                right = mid - 1;  // 尝试寻找更小的最大值
            } else {
                left = mid + 1;   // 需要更大的最大值才能满足分割要求
            }
        }
        
        return (int) result;
    }
    
    /**
     * 检查是否能将数组分割成不超过 m 个子数组，使得每个子数组的和都不超过给定值
     * 使用贪心算法实现
     * 
     * @param nums 非负整数数组
     * @param m 分割成的子数组数量上限
     * @param maxSum 每个子数组和的上限
     * @return 是否能满足分割要求
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     */
    private static boolean canSplit(int[] nums, int m, long maxSum) {
        int count = 1;      // 当前分割的子数组数量，初始为1
        long currentSum = 0; // 当前子数组的和
        
        for (int num : nums) {
            // 如果当前数字本身就超过了上限，无法满足要求
            if (num > maxSum) {
                return false;
            }
            
            // 如果加上当前数字会超过上限，则需要新开一个子数组
            if (currentSum + num > maxSum) {
                count++;
                currentSum = num;
                
                // 如果子数组数量超过了 m，无法满足要求
                if (count > m) {
                    return false;
                }
            } else {
                // 否则将当前数字加入当前子数组
                currentSum += num;
            }
        }
        
        return true;
    }
    
    // 为了测试
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {7, 2, 5, 10, 8};
        int m1 = 2;
        System.out.println("数组: [7, 2, 5, 10, 8], m = " + m1 + 
                           ", 结果 = " + splitArray(nums1, m1));  // 输出: 18
        
        // 测试用例2
        int[] nums2 = {1, 2, 3, 4, 5};
        int m2 = 2;
        System.out.println("数组: [1, 2, 3, 4, 5], m = " + m2 + 
                           ", 结果 = " + splitArray(nums2, m2));  // 输出: 9
        
        // 测试用例3
        int[] nums3 = {1, 4, 4};
        int m3 = 3;
        System.out.println("数组: [1, 4, 4], m = " + m3 + 
                           ", 结果 = " + splitArray(nums3, m3));  // 输出: 4
    }
}