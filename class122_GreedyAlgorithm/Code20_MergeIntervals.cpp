// 合并区间
// 以数组 intervals 表示若干个区间的集合，其中单个区间为 intervals[i] = [starti, endi] 。
// 请你合并所有重叠的区间，并返回一个不重叠的区间数组，该数组需恰好覆盖输入中的所有区间。
// 测试链接 : https://leetcode.cn/problems/merge-intervals/

/**
 * 简单排序函数（按区间开始位置排序）
 * 
 * @param intervals 区间数组
 * @param intervalsSize 数组长度
 */
void sortIntervals(int intervals[][2], int intervalsSize) {
    for (int i = 0; i < intervalsSize - 1; i++) {
        for (int j = 0; j < intervalsSize - i - 1; j++) {
            if (intervals[j][0] > intervals[j + 1][0]) {
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
 * 合并区间
 * 
 * 算法思路：
 * 使用贪心策略：
 * 1. 按照区间的开始位置进行升序排序
 * 2. 遍历排序后的区间：
 *    - 如果当前区间与前一个区间重叠，合并它们
 *    - 否则将前一个区间加入结果集
 * 
 * 正确性分析：
 * 1. 按开始位置排序后，重叠的区间会相邻
 * 2. 贪心选择：尽可能合并重叠区间
 * 3. 合并后的区间能覆盖所有被合并的区间
 * 
 * 时间复杂度：O(n^2) - 使用冒泡排序
 * 空间复杂度：O(1) - 只使用常数额外空间
 * 
 * @param intervals 区间数组
 * @param intervalsSize 区间数组长度
 * @param result 合并后的区间数组
 * @return 合并后的区间数量
 */
int merge(int intervals[][2], int intervalsSize, int result[][2]) {
    // 边界情况处理
    if (intervalsSize == 0) {
        return 0;
    }
    
    // 按照区间的开始位置进行升序排序
    sortIntervals(intervals, intervalsSize);
    
    int resultSize = 0;
    // 第一个区间作为当前合并区间
    int currentStart = intervals[0][0];
    int currentEnd = intervals[0][1];
    
    // 从第二个区间开始遍历
    for (int i = 1; i < intervalsSize; i++) {
        // 如果当前区间与前一个区间重叠
        if (intervals[i][0] <= currentEnd) {
            // 合并区间，更新结束位置为两者较大值
            if (intervals[i][1] > currentEnd) {
                currentEnd = intervals[i][1];
            }
        } else {
            // 不重叠，将前一个区间加入结果集
            result[resultSize][0] = currentStart;
            result[resultSize][1] = currentEnd;
            resultSize++;
            
            // 更新当前合并区间
            currentStart = intervals[i][0];
            currentEnd = intervals[i][1];
        }
    }
    
    // 将最后一个区间加入结果集
    result[resultSize][0] = currentStart;
    result[resultSize][1] = currentEnd;
    resultSize++;
    
    return resultSize;
}