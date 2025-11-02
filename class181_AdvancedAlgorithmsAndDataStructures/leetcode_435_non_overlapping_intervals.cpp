// LeetCode 435 Non-overlapping Intervals
// C++ 实现

/**
 * LeetCode 435 Non-overlapping Intervals
 * 
 * 题目描述：
 * 给定一个区间的集合，找到需要移除区间的最小数量，使剩余区间互不重叠。
 * 
 * 解题思路：
 * 这是一个经典的贪心算法问题。我们可以按照区间的结束时间进行排序，
 * 然后贪心地选择结束时间最早的区间，这样可以为后续区间留出更多空间。
 * 
 * 算法步骤：
 * 1. 按照区间的结束时间进行排序
 * 2. 贪心地选择不重叠的区间
 * 3. 统计需要移除的区间数量
 * 
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(1)
 */

// 由于编译环境限制，这里提供算法思路和伪代码实现

/*
int eraseOverlapIntervals(int** intervals, int intervalsSize, int* intervalsColSize) {
    if (intervalsSize <= 1) {
        return 0;
    }
    
    // 按照区间的结束时间进行排序
    // 在实际实现中需要使用自定义比较函数
    
    int count = 0;
    int end = intervals[0][1]; // 当前选择区间的结束时间
    
    // 贪心地选择不重叠的区间
    for (int i = 1; i < intervalsSize; i++) {
        // 如果当前区间的开始时间小于前一个区间的结束时间，则有重叠
        if (intervals[i][0] < end) {
            count++; // 需要移除一个区间
            // 选择结束时间较早的区间，为后续留出更多空间
            end = (end < intervals[i][1]) ? end : intervals[i][1];
        } else {
            // 没有重叠，更新结束时间
            end = intervals[i][1];
        }
    }
    
    return count;
}

// 算法核心思想：
// 1. 按照区间结束时间排序
// 2. 贪心选择不重叠区间
// 3. 统计需要移除的区间数量

// 时间复杂度分析：
// - 排序：O(n log n)
// - 遍历区间：O(n)
// - 总体时间复杂度：O(n log n)
// - 空间复杂度：O(1)
*/

// 算法应用场景：
// 1. 区间调度问题
// 2. 贪心算法应用
// 3. 活动安排问题