// LeetCode 673. 最长递增子序列的个数
// 给定一个未排序的整数数组 nums ， 返回最长递增子序列的个数 。
// 注意 这个数列必须是 严格 递增的。
// 测试链接 : https://leetcode.cn/problems/number-of-longest-increasing-subsequence/

/*
 * 算法详解：最长递增子序列的个数（LeetCode 673）
 * 
 * 问题描述：
 * 给定一个未排序的整数数组 nums ， 返回最长递增子序列的个数 。
 * 注意 这个数列必须是 严格 递增的。
 * 
 * 算法思路：
 * 在LIS的基础上扩展，不仅要计算最长长度，还要计算该长度的子序列个数。
 * 1. 使用动态规划计算以每个位置结尾的LIS长度和个数
 * 2. 维护全局最长长度和对应的个数
 * 
 * 时间复杂度分析：
 * 1. 遍历数组：O(n)
 * 2. 内层循环：O(n)
 * 3. 总体时间复杂度：O(n^2)
 * 
 * 空间复杂度分析：
 * 1. dp数组：O(n)
 * 2. count数组：O(n)
 * 3. 总体空间复杂度：O(n)
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入数组是否为空
 * 2. 边界处理：处理空数组和单元素数组的情况
 * 3. 整数溢出：注意计数可能很大，使用适当的数据类型
 * 
 * 极端场景验证：
 * 1. 输入数组为空的情况
 * 2. 输入数组只有一个元素的情况
 * 3. 输入数组元素全部相同的情况
 * 4. 输入数组严格递增的情况
 * 5. 输入数组严格递减的情况
 */

// 由于环境限制，此处只提供算法核心实现思路，不包含完整的可编译代码
// 在实际使用中，需要根据具体环境添加适当的头文件和类型定义

/*
int findNumberOfLIS(int* nums, int numsSize) {
    // 异常处理：检查输入数组是否为空
    if (nums == 0 || numsSize == 0) {
        return 0;
    }
    
    // 特殊情况：只有一个元素
    if (numsSize == 1) {
        return 1;
    }
    
    // dp[i] 表示以nums[i]结尾的最长递增子序列长度
    int dp[2000]; // 假设最大长度为2000
    // count[i] 表示以nums[i]结尾的最长递增子序列个数
    int count[2000]; // 假设最大长度为2000
    
    // 初始化：每个元素自身构成长度为1的子序列，个数为1
    for (int i = 0; i < numsSize; i++) {
        dp[i] = 1;
        count[i] = 1;
    }
    
    // 记录全局最长长度和对应的个数
    int maxLength = 1;
    int maxCount = 1;
    
    // 填充dp和count数组
    for (int i = 1; i < numsSize; i++) {
        for (int j = 0; j < i; j++) {
            // 如果nums[j] < nums[i]，可以将nums[i]接在以nums[j]结尾的子序列后面
            if (nums[j] < nums[i]) {
                // 如果通过nums[j]能得到更长的子序列
                if (dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                    count[i] = count[j]; // 个数等于以nums[j]结尾的个数
                } 
                // 如果通过nums[j]能得到相同长度的子序列
                else if (dp[j] + 1 == dp[i]) {
                    count[i] += count[j]; // 个数累加
                }
            }
        }
        
        // 更新全局最长长度和对应的个数
        if (dp[i] > maxLength) {
            maxLength = dp[i];
            maxCount = count[i];
        } else if (dp[i] == maxLength) {
            maxCount += count[i];
        }
    }
    
    return maxCount;
}
*/