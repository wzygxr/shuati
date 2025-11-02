// 无重叠区间
// 给定一个区间的集合 intervals ，其中 intervals[i] = [starti, endi] 。
// 返回需要移除区间的最小数量，使剩余区间互不重叠。
// 测试链接 : https://leetcode.cn/problems/non-overlapping-intervals/

/**
 * 简单排序函数（按区间结束位置排序）
 * 
 * @param intervals 区间数组
 * @param intervalsSize 数组长度
 */
void sortIntervalsByEnd(int intervals[][2], int intervalsSize) {
    for (int i = 0; i < intervalsSize - 1; i++) {
        for (int j = 0; j < intervalsSize - i - 1; j++) {
            if (intervals[j][1] > intervals[j + 1][1]) {
                // 交换区间
                int temp0 = intervals[j][0];
                int temp1 = intervals[j][1];
                intervals[j][0] = intervals[j + 1][0];
                intervals[j][1] = intervals[j + 1][1];
                intervals[j + 1][0] = temp0;
                intervals[j + 1][1] = temp1;
            }
        }
    }
}

/**
 * 无重叠区间
 * 
 * 算法思路：
 * 使用贪心策略：
 * 1. 按照区间的结束位置进行升序排序
 * 2. 贪心选择结束时间最早的区间，这样能为后续区间留出最多空间
 * 3. 遍历排序后的区间，统计重叠的区间数量
 * 
 * 正确性分析：
 * 1. 为了保留最多的区间，我们应该优先选择结束时间早的区间
 * 2. 这样可以为后面的区间留出更多空间
 * 3. 重叠的区间需要被移除
 * 
 * 时间复杂度：O(n*n) - 使用冒泡排序
 * 空间复杂度：O(1) - 只使用常数额外空间
 * 
 * @param intervals 区间数组
 * @param intervalsSize 数组长度
 * @return 需要移除的区间数量
 */
int eraseOverlapIntervals(int intervals[][2], int intervalsSize) {
    // 边界情况处理
    if (intervalsSize == 0) {
        return 0;
    }
    
    // 按照区间的结束位置进行升序排序
    sortIntervalsByEnd(intervals, intervalsSize);
    
    int count = 0;              // 需要移除的区间数量
    int end = intervals[0][1];  // 当前选择区间的结束位置
    
    // 从第二个区间开始遍历
    for (int i = 1; i < intervalsSize; i++) {
        // 如果当前区间的开始位置小于前一个区间的结束位置，说明重叠
        if (intervals[i][0] < end) {
            // 需要移除这个区间
            count++;
        } else {
            // 更新结束位置
            end = intervals[i][1];
        }
    }
    
    // 返回需要移除的区间数量
    return count;
}