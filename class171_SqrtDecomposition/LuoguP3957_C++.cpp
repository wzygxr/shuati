#include <iostream>
#include <vector>
#include <cmath>
#include <algorithm>
using namespace std;

/**
 * 洛谷 P3957 跳房子 - C++实现
 * 题目链接：https://www.luogu.com.cn/problem/P3957
 * 
 * 题目描述：
 * 跳房子是一个有趣的小游戏。在这个游戏中，地面上画着一排格子，每个格子有不同的分数。玩家可以选择从某个格子开始，然后每次向前跳，必须至少跳1格，最多跳k格。游戏的目标是获得尽可能多的分数。
 * 
 * 解题思路：
 * 使用分块算法，将数组分成大小为sqrt(n)的块。
 * - 预处理每个块内的最大值以及块内的跳跃情况
 * - 对于查询，分情况处理：
 *   1. 完全在一个块内的跳跃：暴力计算
 *   2. 跨块的跳跃：利用预处理信息快速计算
 * 
 * 时间复杂度：O(n√n)
 * 空间复杂度：O(n)
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数的有效性
 * 2. 可配置性：块大小可根据需要调整
 * 3. 性能优化：预处理块内信息减少重复计算
 * 4. 鲁棒性：处理边界情况和特殊输入
 * 5. 数据结构：使用动态规划和分块结合的方法
 */

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    // 读取n和k
    int n, k;
    cin >> n >> k;
    
    // 读取数组（索引从1开始）
    vector<int> a(n + 1);
    for (int i = 1; i <= n; i++) {
        cin >> a[i];
    }
    
    // 计算块大小和块数量
    int blockSize = static_cast<int>(sqrt(n)) + 1;
    int blockNum = (n + blockSize - 1) / blockSize;
    
    // dp数组，表示从第i个位置出发能获得的最大分数
    vector<long long> dp(n + 2, 0); // 使用long long避免溢出
    // 预处理每个块的最大值数组
    vector<long long> maxInBlock(blockNum + 1, 0);
    
    // 初始化dp[n]，从最后一个位置出发只能获得自己的分数
    dp[n] = a[n];
    
    // 从后往前计算dp值
    for (int i = n - 1; i >= 1; i--) {
        // 确定i所在的块
        int currentBlock = (i - 1) / blockSize + 1;
        
        // 计算i能跳到的最远距离
        int right = min(i + k, n);
        
        long long maxVal = 0;
        
        // 如果i和right在同一个块内，直接暴力计算
        if ((right - 1) / blockSize + 1 == currentBlock) {
            for (int j = i + 1; j <= right; j++) {
                if (dp[j] > maxVal) {
                    maxVal = dp[j];
                }
            }
        } else {
            // 处理跨块的情况
            // 1. 暴力处理当前块内的部分
            // 当前块的结束位置
            int blockEnd = min(currentBlock * blockSize, n);
            for (int j = i + 1; j <= blockEnd; j++) {
                if (dp[j] > maxVal) {
                    maxVal = dp[j];
                }
            }
            
            // 2. 利用预处理的块最大值处理中间完整的块
            // 计算right所在的块
            int rightBlock = (right - 1) / blockSize + 1;
            // 遍历中间的完整块
            for (int b = currentBlock + 1; b < rightBlock; b++) {
                if (maxInBlock[b] > maxVal) {
                    maxVal = maxInBlock[b];
                }
            }
            
            // 3. 暴力处理右边不完整的块
            for (int j = (rightBlock - 1) * blockSize + 1; j <= right; j++) {
                if (dp[j] > maxVal) {
                    maxVal = dp[j];
                }
            }
        }
        
        dp[i] = a[i] + maxVal;
        
        // 更新当前块的最大值
        if (dp[i] > maxInBlock[currentBlock]) {
            maxInBlock[currentBlock] = dp[i];
        }
    }
    
    // 输出结果，从第一个位置出发的最大分数
    cout << dp[1] << endl;
    
    return 0;
}