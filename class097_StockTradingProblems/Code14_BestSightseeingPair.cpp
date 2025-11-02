// LeetCode 1014. 最佳观光组合
// 题目链接: https://leetcode.cn/problems/best-sightseeing-pair/
// 题目描述:
// 给你一个正整数数组 values，其中 values[i] 表示第 i 个观光景点的评分，
// 并且两个景点 i 和 j 之间的距离为 j - i。
// 一对景点（i < j）组成的观光组合的得分为 values[i] + values[j] + i - j，
// 也就是景点的评分之和减去它们两者之间的距离。
// 返回一对观光景点能取得的最高分。
//
// 解题思路:
// 这是一个数学优化问题。表达式 values[i] + values[j] + i - j 可以重写为:
// (values[i] + i) + (values[j] - j)
// 对于每个j，我们只需要找到最大的(values[i] + i) (i < j)即可。
// 因此可以使用一次遍历解决。
//
// 算法步骤:
// 1. 维护一个变量maxI，表示到目前为止最大的(values[i] + i)
// 2. 从索引1开始遍历数组:
//    - 计算当前得分: maxI + values[j] - j
//    - 更新全局最大得分
//    - 更新maxI为max(maxI, values[j] + j)
//
// 时间复杂度分析:
// O(n) - 只需要遍历一次数组
//
// 空间复杂度分析:
// O(1) - 只使用了常数级别的额外空间
//
// 是否为最优解:
// 是，这是解决该问题的最优解

// 计算最佳观光组合得分
// 参数说明:
// values: 景点评分数组
// n: 数组长度
// 返回值: 最高得分
int maxScoreSightseeingPair(int values[], int n) {
    // 边界条件处理
    if (n < 2) {
        return 0;
    }
    
    // maxI 表示到目前为止最大的(values[i] + i)
    int maxI = values[0] + 0;  // values[0] + 0
    
    // maxScore 表示到目前为止的最大得分
    int maxScore = 0;
    
    // 从索引1开始遍历数组
    for (int j = 1; j < n; j++) {
        // 计算当前得分: maxI + values[j] - j
        if (maxScore < maxI + values[j] - j) {
            maxScore = maxI + values[j] - j;
        }
        
        // 更新maxI为max(maxI, values[j] + j)
        if (maxI < values[j] + j) {
            maxI = values[j] + j;
        }
    }
    
    return maxScore;
}

// 由于编译环境限制，不提供测试函数
// 可以通过LeetCode平台进行测试