// LeetCode 239 Sliding Window Maximum
// C++ 实现

/**
 * LeetCode 239 Sliding Window Maximum
 * 
 * 题目描述：
 * 给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。
 * 你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。
 * 返回滑动窗口中的最大值。
 * 
 * 解题思路：
 * 我们可以使用稀疏表来预处理数组，然后对每个窗口查询最大值。
 * 1. 使用稀疏表预处理数组，构建RMQ数据结构
 * 2. 对每个窗口使用稀疏表查询最大值
 * 
 * 时间复杂度：O(n log n + m)
 * 空间复杂度：O(n log n)
 */

// 由于编译环境限制，这里提供算法思路和伪代码实现

/*
#include <stdlib.h>
#include <math.h>

int* maxSlidingWindow(int* nums, int numsSize, int k, int* returnSize) {
    if (numsSize == 0 || k == 0) {
        *returnSize = 0;
        return NULL;
    }
    
    // 计算稀疏表的大小
    int logSize = (int)(log2(numsSize)) + 1;
    
    // 创建稀疏表
    int** sparseTable = (int**)malloc(numsSize * sizeof(int*));
    for (int i = 0; i < numsSize; i++) {
        sparseTable[i] = (int*)malloc(logSize * sizeof(int));
        sparseTable[i][0] = nums[i];
    }
    
    // 构建稀疏表
    for (int j = 1; (1 << j) <= numsSize; j++) {
        for (int i = 0; i + (1 << j) - 1 < numsSize; i++) {
            sparseTable[i][j] = (sparseTable[i][j-1] > sparseTable[i + (1 << (j-1))][j-1]) ? 
                               sparseTable[i][j-1] : sparseTable[i + (1 << (j-1))][j-1];
        }
    }
    
    // 查询每个窗口的最大值
    int resultSize = numsSize - k + 1;
    int* result = (int*)malloc(resultSize * sizeof(int));
    *returnSize = resultSize;
    
    for (int i = 0; i < resultSize; i++) {
        int l = i, r = i + k - 1;
        int len = r - l + 1;
        int logLen = (int)(log2(len));
        int max1 = sparseTable[l][logLen];
        int max2 = sparseTable[r - (1 << logLen) + 1][logLen];
        result[i] = (max1 > max2) ? max1 : max2;
    }
    
    // 释放内存
    for (int i = 0; i < numsSize; i++) {
        free(sparseTable[i]);
    }
    free(sparseTable);
    
    return result;
}

// 算法核心思想：
// 1. 使用稀疏表预处理数组，构建RMQ数据结构
// 2. 对每个窗口使用稀疏表查询最大值

// 时间复杂度分析：
// - 构建稀疏表：O(n log n)
// - 查询每个窗口：O(1)
// - 总体时间复杂度：O(n log n + m)
// - 空间复杂度：O(n log n)
*/

// 算法应用场景：
// 1. 滑动窗口问题
// 2. RMQ问题
// 3. 稀疏表应用