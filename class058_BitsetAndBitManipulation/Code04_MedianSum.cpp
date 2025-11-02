#include <iostream>
#include <bitset>
#include <vector>
using namespace std;

// AtCoder AGC020 C - Median Sum
// 题目链接: https://atcoder.jp/contests/agc020/tasks/agc020_c
// 题目大意:
// 给定N个整数A1, A2, ..., AN。
// 考虑A的所有非空子序列的和。有2^N-1个这样的和，这是一个奇数。
// 将这些和按非递减顺序排列为S1, S2, ..., S_{2^N-1}。
// 找到这个列表的中位数S_{2^{N-1}}。
//
// 约束条件:
// 1 ≤ N ≤ 2000
// 1 ≤ Ai ≤ 2000
// 所有输入值都是整数。
//
// 输入:
// 输入以以下格式从标准输入给出:
// N
// A1 A2 ... AN
//
// 输出:
// 打印A的所有非空子序列的和按排序后的中位数。
//
// 解题思路:
// 这是一个经典的bitset优化DP问题。
// 1. 使用bitset来表示所有可能的子集和
// 2. bitset的第i位为1表示存在一个子集的和为i
// 3. 对于每个元素x，我们执行: dp |= dp << x
//    这表示将之前所有可达的和都加上x，同时保留原来的和
// 4. 中位数的计算有一个技巧:
//    所有子集和的总和为sum，那么中位数就是从(sum+1)/2开始第一个可达的和
//
// 时间复杂度: O(N * sum / 32)  其中sum是所有元素的和
// 空间复杂度: O(sum) bit

// 定义最大可能的和: 2000 * 2000 = 4,000,000
const int MAX_SUM = 4000001;

// 主函数，处理输入并输出结果
int main() {
    // 优化输入输出速度，关闭stdio同步，解除cin与cout的绑定
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    int n;  // 元素数量
    // 读取元素数量
    cin >> n;
    
    // 存储输入的元素
    vector<int> a(n);
    int sum = 0;  // 所有元素的总和
    // 读取所有元素并计算总和
    for (int i = 0; i < n; i++) {
        cin >> a[i];
        sum += a[i];
    }
    
    // 使用bitset优化的DP
    // dp的第i位为1表示存在一个子集的和为i
    bitset<MAX_SUM> dp;
    // 初始状态，空集的和为0
    // 将dp的第0位设置为1
    dp[0] = 1;
    
    // 对于每个元素，更新所有可能的子集和
    for (int i = 0; i < n; i++) {
        // dp |= dp << a[i]
        // 这表示既保留原来的和，又加上a[i]后的新和
        // dp << a[i] 将dp中所有为1的位向左移动a[i]位
        // dp | (dp << a[i]) 按位或操作，将原来的和与新和合并
        dp |= (dp << a[i]);
    }
    
    // 找到中位数
    // 所有子集和的总数是2^N - 1，中位数是第2^(N-1)个
    // 有一个数学技巧: 从(sum+1)/2开始第一个可达的和就是中位数
    // 循环从(sum+1)/2开始，找到第一个dp[i]为1的位置
    for (int i = (sum + 1) / 2; ; i++) {
        // 检查dp的第i位是否为1
        if (dp[i]) {
            // 找到中位数，输出并结束程序
            cout << i << "\n";
            break;
        }
    }
    
    return 0;
}