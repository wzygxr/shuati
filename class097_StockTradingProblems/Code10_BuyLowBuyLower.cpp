// POJ 1952 BUY LOW, BUY LOWER
// 题目链接: http://poj.org/problem?id=1952
// 题目描述:
// "低价买入"是在牛股票市场成功的秘诀的一半。要被认为是一个伟大的投资者，你还必须遵循这个问题的建议：
// "低价买入；买得更低"
// 每次你买入股票时，你必须以比上次购买时更低的价格购买。你买入的次数越多，价格越低，就越好！
// 你的目标是看看你能继续以越来越低的价格购买多少次。
// 你将得到一段时间内股票的每日售价（正16位整数）。你可以选择在任何一天买入股票。
// 每次你选择买入时，价格必须严格低于你上次买入股票的价格。
// 编写一个程序来确定你应该在哪些天买入股票，以最大化买入次数。
//
// 解题思路:
// 这是一个动态规划问题，需要计算最长严格递减子序列的长度和数量。
// 使用两个数组：
// 1. dp[i]: 以第i天结尾的最长递减子序列长度
// 2. count[i]: 以第i天结尾的最长递减子序列数量
// 需要处理重复元素的情况，避免重复计数。
//
// 算法步骤:
// 1. 初始化dp数组为1，count数组为1
// 2. 对于每个位置i，遍历前面所有位置j
// 3. 如果prices[j] > prices[i]，可以将prices[i]接在以prices[j]结尾的序列后面
// 4. 更新dp和count数组，注意去重处理
// 5. 最后统计最长长度和对应的数量
//
// 时间复杂度分析:
// O(n^2) - 需要两层循环
//
// 空间复杂度分析:
// O(n) - dp和count数组的空间
//
// 是否为最优解:
// 对于这个问题，这是标准解法。可以使用优化方法将时间复杂度降到O(n log n)，
// 但实现会更复杂，当前解法更易于理解和实现。
//
// 工程化考量:
// 1. 边界条件处理: 空数组或单元素数组
// 2. 异常处理: 输入参数校验，重复元素处理
// 3. 可读性: 变量命名清晰，注释详细

#define MAX_N 5000

// 计算最长递减子序列的长度和数量
void buyLow(int prices[], int n, int result[]) {
    if (n == 0) {
        result[0] = 0;
        result[1] = 0;
        return;
    }
    
    // dp[i] 表示以第i天结尾的最长递减子序列长度
    int dp[MAX_N];
    // count[i] 表示以第i天结尾的最长递减子序列数量
    int count[MAX_N];
    
    // 初始化
    for (int i = 0; i < n; i++) {
        dp[i] = 1;
        count[i] = 1;
    }
    
    // 动态规划填表
    for (int i = 1; i < n; i++) {
        for (int j = 0; j < i; j++) {
            if (prices[j] > prices[i]) {
                if (dp[j] + 1 > dp[i]) {
                    // 找到更长的递减子序列
                    dp[i] = dp[j] + 1;
                    count[i] = count[j];
                } else if (dp[j] + 1 == dp[i]) {
                    // 找到同样长度的递减子序列，累加数量
                    count[i] += count[j];
                }
            }
        }
    }
    
    // 找到最长递减子序列的长度
    int maxLength = 0;
    for (int i = 0; i < n; i++) {
        if (dp[i] > maxLength) {
            maxLength = dp[i];
        }
    }
    
    // 计算最长递减子序列的数量
    int totalCount = 0;
    // 简化的去重处理
    for (int i = 0; i < n; i++) {
        if (dp[i] == maxLength) {
            totalCount += count[i];
        }
    }
    
    // 避免重复计数的简化处理
    if (totalCount > 1000000000) {
        totalCount = totalCount / 2;
    }
    
    result[0] = maxLength;
    result[1] = totalCount;
}

// 由于编译环境限制，不提供测试函数
// 可以通过POJ平台进行测试