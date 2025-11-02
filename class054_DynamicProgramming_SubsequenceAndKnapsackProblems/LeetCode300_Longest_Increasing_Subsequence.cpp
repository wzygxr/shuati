// LeetCode 300. 最长递增子序列
// 给你一个整数数组 nums ，找到其中最长严格递增子序列的长度。
// 子序列 是由数组派生而来的序列，删除（或不删除）数组中的元素而不改变其余元素的顺序。
// 测试链接 : https://leetcode.cn/problems/longest-increasing-subsequence/

/*
 * 算法详解：最长递增子序列（LeetCode 300）
 * 
 * 问题描述：
 * 给你一个整数数组 nums ，找到其中最长严格递增子序列的长度。
 * 子序列是由数组派生而来的序列，删除（或不删除）数组中的元素而不改变其余元素的顺序。
 * 
 * 算法思路：
 * 使用贪心+二分查找的方法计算LIS长度，时间复杂度O(n log n)。
 * 1. 维护一个ends数组，ends[i]表示长度为i+1的递增子序列的最小末尾元素
 * 2. 遍历原数组，对于每个元素使用二分查找找到其在ends数组中的合适位置
 * 3. 更新ends数组并记录最长长度
 * 
 * 时间复杂度分析：
 * 1. 遍历数组：O(n)
 * 2. 二分查找：O(log n)
 * 3. 总体时间复杂度：O(n log n)
 * 
 * 空间复杂度分析：
 * 1. ends数组：O(n)
 * 2. 总体空间复杂度：O(n)
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入数组是否为空
 * 2. 边界处理：处理空数组和单元素数组的情况
 * 3. 性能优化：使用二分查找将时间复杂度从O(n^2)优化到O(n log n)
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
int lengthOfLIS(int* nums, int numsSize) {
    // 异常处理：检查输入数组是否为空
    if (nums == 0 || numsSize == 0) {
        return 0;
    }
    
    // 特殊情况：只有一个元素
    if (numsSize == 1) {
        return 1;
    }
    
    // ends[i] 表示长度为i+1的递增子序列的最小末尾元素
    int ends[10000]; // 假设最大长度为10000
    // 当前最长LIS的长度
    int len = 0;
    
    // 遍历原数组
    for (int i = 0; i < numsSize; i++) {
        // 使用二分查找找到nums[i]在ends数组中的合适位置
        int index = binarySearch(ends, len, nums[i]);
        
        // 如果index等于len，说明nums[i]比所有元素都大，需要扩展ends数组
        if (index == len) {
            len++;
        }
        
        // 更新ends数组
        ends[index] = nums[i];
    }
    
    // 返回最长LIS长度
    return len;
}

// 二分查找：在ends数组中找到第一个大于等于target的位置
int binarySearch(int* ends, int len, int target) {
    int left = 0, right = len;
    
    while (left < right) {
        int mid = left + (right - left) / 2;
        if (ends[mid] < target) {
            left = mid + 1;
        } else {
            right = mid;
        }
    }
    
    return left;
}

// 动态规划解法：时间复杂度O(n^2)，空间复杂度O(n)
int lengthOfLISDP(int* nums, int numsSize) {
    // 异常处理：检查输入数组是否为空
    if (nums == 0 || numsSize == 0) {
        return 0;
    }
    
    // dp[i] 表示以nums[i]结尾的最长递增子序列长度
    int dp[10000]; // 假设最大长度为10000
    // 初始化：每个元素自身构成长度为1的子序列
    for (int i = 0; i < numsSize; i++) {
        dp[i] = 1;
    }
    
    // 记录最长长度
    int maxLen = 1;
    
    // 填充dp数组
    for (int i = 1; i < numsSize; i++) {
        for (int j = 0; j < i; j++) {
            // 如果nums[j] < nums[i]，可以将nums[i]接在以nums[j]结尾的子序列后面
            if (nums[j] < nums[i]) {
                int a = dp[i];
                int b = dp[j] + 1;
                dp[i] = (a > b) ? a : b;
            }
        }
        // 更新最长长度
        int a = maxLen;
        int b = dp[i];
        maxLen = (a > b) ? a : b;
    }
    
    return maxLen;
}
*/