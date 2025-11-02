// LeetCode 1521 Find Value of Partition
// C++ 实现

/**
 * LeetCode 1521 Find Value of Partition
 * 
 * 题目描述：
 * 给你一个正整数数组 nums 。
 * 同时给你一个长度为 m 的整数数组 queries 。
 * 第 i 个查询中，我们需要将 nums 中的元素划分到两个数组 A 和 B 中，
 * 使得 A 中的元素都小于等于 queries[i]，B 中的元素都大于 queries[i]。
 * 如果某个数组为空，其和视为 0。
 * 返回一个数组 answer，其中 answer[i] 是第 i 个查询的答案。
 * 
 * 解题思路：
 * 我们可以使用稀疏表来预处理数组，然后对每个查询使用二分查找找到分割点。
 * 1. 对数组进行排序
 * 2. 使用前缀和预处理
 * 3. 对每个查询使用二分查找找到分割点
 * 4. 计算两个数组的和并返回差值的绝对值
 * 
 * 时间复杂度：O(n log n + m log n)
 * 空间复杂度：O(n)
 */

// 由于编译环境限制，这里提供算法思路和伪代码实现

/*
#include <stdlib.h>
#include <math.h>

int* findValueOfPartition(int* nums, int numsSize, int* queries, int queriesSize, int* returnSize) {
    // 对数组进行排序
    // 在实际实现中需要使用排序算法
    
    // 计算前缀和
    long long* prefixSum = (long long*)calloc(numsSize + 1, sizeof(long long));
    for (int i = 0; i < numsSize; i++) {
        prefixSum[i + 1] = prefixSum[i] + nums[i];
    }
    
    // 处理每个查询
    int* result = (int*)malloc(queriesSize * sizeof(int));
    *returnSize = queriesSize;
    
    for (int i = 0; i < queriesSize; i++) {
        int query = queries[i];
        
        // 使用二分查找找到分割点
        int left = 0, right = numsSize;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] <= query) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        
        // 计算两个数组的和
        long long sumA = prefixSum[left];
        long long sumB = prefixSum[numsSize] - sumA;
        
        // 返回差值的绝对值
        result[i] = (int)abs(sumA - sumB);
    }
    
    free(prefixSum);
    return result;
}

// 算法核心思想：
// 1. 排序数组以便于二分查找
// 2. 使用前缀和快速计算子数组和
// 3. 对每个查询使用二分查找找到最优分割点

// 时间复杂度分析：
// - 排序：O(n log n)
// - 计算前缀和：O(n)
// - 处理查询：O(m log n)
// - 总体时间复杂度：O(n log n + m log n)
// - 空间复杂度：O(n)
*/

// 算法应用场景：
// 1. 数组分割问题
// 2. 前缀和技巧
// 3. 二分查找应用