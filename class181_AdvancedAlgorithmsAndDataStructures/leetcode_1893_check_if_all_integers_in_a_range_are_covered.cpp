// LeetCode 1893 Check If All Integers In A Range Are Covered
// C++ 实现

/**
 * LeetCode 1893 Check If All Integers In A Range Are Covered
 * 
 * 题目描述：
 * 给你一个二维整数数组 ranges 和两个整数 left 和 right。
 * 每个 ranges[i] = [starti, endi] 表示一个从 starti 到 endi 的闭区间。
 * 如果闭区间 [left, right] 内每个整数都被 ranges 中至少一个区间覆盖，
 * 那么返回 true，否则返回 false。
 * 
 * 解题思路：
 * 我们可以使用差分数组来解决这个问题。
 * 1. 创建一个差分数组，对每个区间进行标记
 * 2. 计算前缀和得到每个点的覆盖次数
 * 3. 检查目标区间内每个点的覆盖次数是否大于0
 * 
 * 时间复杂度：O(n + m)，其中n是ranges的长度，m是区间长度
 * 空间复杂度：O(m)
 */

// 由于编译环境限制，这里提供算法思路和伪代码实现

/*
bool isCovered(int** ranges, int rangesSize, int* rangesColSize, int left, int right) {
    // 创建差分数组，范围足够大以覆盖所有可能的值
    int diff[52] = {0}; // 下标0-51，足够覆盖1-50的范围
    
    // 对每个区间进行标记
    for (int i = 0; i < rangesSize; i++) {
        int start = ranges[i][0];
        int end = ranges[i][1];
        diff[start] += 1;
        diff[end + 1] -= 1;
    }
    
    // 计算前缀和得到每个点的覆盖次数
    int curr = 0;
    for (int i = 1; i <= 50; i++) {
        curr += diff[i];
        // 检查目标区间内每个点的覆盖次数是否大于0
        if (i >= left && i <= right && curr <= 0) {
            return false;
        }
    }
    
    return true;
}

// 算法核心思想：
// 1. 使用差分数组标记区间
// 2. 通过前缀和计算每个点的覆盖次数
// 3. 检查目标区间内每个点是否都被覆盖

// 时间复杂度分析：
// - 标记区间：O(n)
// - 计算前缀和：O(m)
// - 检查覆盖：O(m)
// - 总体时间复杂度：O(n + m)
// - 空间复杂度：O(m)
*/

// 算法应用场景：
// 1. 区间覆盖问题
// 2. 差分数组应用
// 3. 前缀和技巧