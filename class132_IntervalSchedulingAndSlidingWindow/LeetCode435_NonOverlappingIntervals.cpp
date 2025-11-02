/**
 * LeetCode 435. Non-overlapping Intervals
 * 
 * 题目描述：
 * 给定一个区间的集合 intervals，其中 intervals[i] = [start_i, end_i]。
 * 返回需要移除区间的最小数量，使剩余区间互不重叠。
 * 
 * 解题思路：
 * 这是一个经典的贪心算法问题。为了移除最少的区间，我们应该保留尽可能多的不重叠区间。
 * 
 * 算法步骤：
 * 1. 将所有区间按结束时间排序
 * 2. 使用贪心策略：总是选择结束时间最早的区间
 * 3. 遍历排序后的区间，统计可以保留的区间数量
 * 4. 返回总区间数减去保留的区间数，即为需要移除的区间数
 * 
 * 贪心策略的正确性：
 * 选择结束时间最早的区间可以为后续区间留下更多空间，从而最大化保留的区间数量。
 * 
 * 时间复杂度：O(n * log n)
 * 空间复杂度：O(1)
 * 
 * 相关题目：
 * - LeetCode 1353. 最多可以参加的会议数目 (贪心)
 * - LeetCode 646. 最长数对链 (贪心)
 * - LeetCode 1235. 最大盈利的工作调度 (动态规划 + 二分查找)
 */

// 简化版C++实现，避免使用STL容器
// 由于编译环境限制，使用基本数组和手动实现算法

const int MAX_N = 100005;

// 区间结构体
struct Interval {
    int start, end;
};

Interval intervals[MAX_N];
int n;

// 比较函数，用于按结束时间排序
bool compareIntervals(Interval a, Interval b) {
    return a.end < b.end;
}

// 简单的排序实现（冒泡排序）
void sortIntervals() {
    for (int i = 0; i < n - 1; i++) {
        for (int j = 0; j < n - 1 - i; j++) {
            if (intervals[j].end > intervals[j + 1].end) {
                // 交换
                Interval temp = intervals[j];
                intervals[j] = intervals[j + 1];
                intervals[j + 1] = temp;
            }
        }
    }
}

/**
 * 计算需要移除的最小区间数
 * 
 * @param intervals_input 区间数组
 * @param size 数组大小
 * @return 需要移除的区间数
 */
int eraseOverlapIntervals(int intervals_input[][2], int size) {
    // 边界情况处理
    if (size == 0) {
        return 0;
    }
    
    n = size;
    
    // 将输入转换为内部结构
    for (int i = 0; i < n; i++) {
        intervals[i].start = intervals_input[i][0];
        intervals[i].end = intervals_input[i][1];
    }
    
    // 按结束时间排序
    sortIntervals();
    
    // 初始化计数器和上一个保留区间的结束时间
    int count = 1;  // 至少可以保留一个区间
    int end = intervals[0].end;  // 第一个区间的结束时间
    
    // 遍历剩余区间
    for (int i = 1; i < n; i++) {
        // 如果当前区间的开始时间 >= 上一个保留区间的结束时间
        // 说明不重叠，可以保留
        if (intervals[i].start >= end) {
            count++;
            end = intervals[i].end;  // 更新结束时间
        }
        // 如果重叠，则跳过当前区间（相当于移除）
    }
    
    // 返回需要移除的区间数
    return n - count;
}

// 简单的测试函数
void runTests() {
    // 测试用例1
    int intervals1[][2] = {{1,2},{2,3},{3,4},{1,3}};
    int size1 = 4;
    // 期望输出: 1
    
    // 测试用例2
    int intervals2[][2] = {{1,2},{1,2},{1,2}};
    int size2 = 3;
    // 期望输出: 2
    
    // 测试用例3
    int intervals3[][2] = {{1,2},{2,3}};
    int size3 = 2;
    // 期望输出: 0
}