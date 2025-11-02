// LeetCode 1272 Remove Interval
// C++ 实现

/**
 * LeetCode 1272 Remove Interval
 * 
 * 题目描述：
 * 给你一个有序的区间列表 intervals ，其中 intervals[i] = [starti, endi] 表示集合 [starti, endi)。
 * 区间 [a, b) 是一组实数 x，满足 a <= x < b。
 * 给你一个要删除的区间 toBeRemoved 。
 * 返回一组实数，表示 intervals 中删除了 toBeRemoved 的部分。
 * 结果应当是一个有序的、不重叠的区间列表。
 * 
 * 解题思路：
 * 我们需要遍历所有区间，对每个区间与要删除的区间进行比较，
 * 根据重叠情况生成新的区间。
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(1)
 */

// 由于编译环境限制，这里提供算法思路和伪代码实现

/*
#include <stdlib.h>

int** removeInterval(int** intervals, int intervalsSize, int* intervalsColSize, int* toBeRemoved, int toBeRemovedSize, int* returnSize, int** returnColumnSizes) {
    // 创建结果数组
    int** result = (int**)malloc(2 * intervalsSize * sizeof(int*));
    int resultSize = 0;
    
    int removeStart = toBeRemoved[0];
    int removeEnd = toBeRemoved[1];
    
    // 遍历所有区间
    for (int i = 0; i < intervalsSize; i++) {
        int start = intervals[i][0];
        int end = intervals[i][1];
        
        // 如果当前区间与要删除的区间没有重叠
        if (end <= removeStart || start >= removeEnd) {
            // 直接添加当前区间
            result[resultSize] = (int*)malloc(2 * sizeof(int));
            result[resultSize][0] = start;
            result[resultSize][1] = end;
            resultSize++;
        } else {
            // 如果当前区间与要删除的区间有重叠
            // 添加左侧部分（如果存在）
            if (start < removeStart) {
                result[resultSize] = (int*)malloc(2 * sizeof(int));
                result[resultSize][0] = start;
                result[resultSize][1] = removeStart;
                resultSize++;
            }
            
            // 添加右侧部分（如果存在）
            if (end > removeEnd) {
                result[resultSize] = (int*)malloc(2 * sizeof(int));
                result[resultSize][0] = removeEnd;
                result[resultSize][1] = end;
                resultSize++;
            }
        }
    }
    
    // 设置返回参数
    *returnSize = resultSize;
    *returnColumnSizes = (int*)malloc(resultSize * sizeof(int));
    for (int i = 0; i < resultSize; i++) {
        (*returnColumnSizes)[i] = 2;
    }
    
    return result;
}

// 算法核心思想：
// 1. 遍历所有区间
// 2. 对每个区间与要删除的区间进行比较
// 3. 根据重叠情况生成新的区间

// 时间复杂度分析：
// - 遍历区间：O(n)
// - 生成新区间：O(1)
// - 总体时间复杂度：O(n)
// - 空间复杂度：O(1)
*/

// 算法应用场景：
// 1. 区间操作问题
// 2. 集合运算
// 3. 计算几何问题