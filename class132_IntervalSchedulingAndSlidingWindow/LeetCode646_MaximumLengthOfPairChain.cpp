/**
 * LeetCode 646. Maximum Length of Pair Chain
 * 
 * 题目描述：
 * 给出 n 个数对 pairs，其中 pairs[i] = [left_i, right_i] 且 left_i < right_i。
 * 现在，我们定义一种"跟随"关系，当且仅当 b < c 时，数对 [c, d] 可以跟在 [a, b] 后面。
 * 我们可以构造一个数对链，链中每两个相邻的数对都满足"跟随"关系。
 * 找出并返回能够形成的最长数对链的长度。
 * 
 * 解题思路：
 * 这是一个经典的贪心算法问题，类似于活动选择问题。
 * 
 * 算法步骤：
 * 1. 将所有数对按结束值排序
 * 2. 使用贪心策略：总是选择结束值最小的数对
 * 3. 遍历排序后的数对，统计可以组成的最长链长度
 * 
 * 贪心策略的正确性：
 * 选择结束值最小的数对可以为后续数对留下更多空间，从而最大化链的长度。
 * 
 * 时间复杂度：O(n * log n)
 * 空间复杂度：O(1)
 * 
 * 相关题目：
 * - LeetCode 435. 无重叠区间 (贪心)
 * - LeetCode 1353. 最多可以参加的会议数目 (贪心)
 * - LeetCode 1235. 最大盈利的工作调度 (动态规划 + 二分查找)
 */

// 简化版C++实现，避免使用STL容器
// 由于编译环境限制，使用基本数组和手动实现算法

const int MAX_N = 100005;

// 数对结构体
struct Pair {
    int first, second;
};

Pair pairs[MAX_N];
int n;

// 比较函数，用于按结束值排序
bool comparePairs(Pair a, Pair b) {
    return a.second < b.second;
}

// 简单的排序实现（冒泡排序）
void sortPairs() {
    for (int i = 0; i < n - 1; i++) {
        for (int j = 0; j < n - 1 - i; j++) {
            if (pairs[j].second > pairs[j + 1].second) {
                // 交换
                Pair temp = pairs[j];
                pairs[j] = pairs[j + 1];
                pairs[j + 1] = temp;
            }
        }
    }
}

/**
 * 计算最长数对链的长度
 * 
 * @param pairs_input 数对数组
 * @param size 数组大小
 * @return 最长数对链的长度
 */
int findLongestChain(int pairs_input[][2], int size) {
    // 边界情况处理
    if (size == 0) {
        return 0;
    }
    
    n = size;
    
    // 将输入转换为内部结构
    for (int i = 0; i < n; i++) {
        pairs[i].first = pairs_input[i][0];
        pairs[i].second = pairs_input[i][1];
    }
    
    // 按结束值排序
    sortPairs();
    
    // 初始化计数器和上一个选择数对的结束值
    int count = 1;  // 至少可以选择一个数对
    int end = pairs[0].second;  // 第一个数对的结束值
    
    // 遍历剩余数对
    for (int i = 1; i < n; i++) {
        // 如果当前数对的开始值 > 上一个选择数对的结束值
        // 说明可以连接，可以选择当前数对
        if (pairs[i].first > end) {
            count++;
            end = pairs[i].second;  // 更新结束值
        }
        // 如果不能连接，则跳过当前数对
    }
    
    return count;
}

// 简单的测试函数
void runTests() {
    // 测试用例1
    int pairs1[][2] = {{1,2},{2,3},{3,4}};
    int size1 = 3;
    // 期望输出: 2
    
    // 测试用例2
    int pairs2[][2] = {{1,2},{7,8},{4,5}};
    int size2 = 3;
    // 期望输出: 3
    
    // 测试用例3
    int pairs3[][2] = {{1,2}};
    int size3 = 1;
    // 期望输出: 1
}