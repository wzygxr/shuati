#include <iostream>
#include <vector>
#include <deque>
#include <climits>
using namespace std;

/**
 * 洛谷P1725 琪露诺问题
 * 题目来源：洛谷 P1725 琪露诺
 * 网址：https://www.luogu.com.cn/problem/P1725
 * 
 * 题目描述：
 * 在幻想乡，琪露诺是以笨蛋闻名的冰之妖精。一天，琪露诺又在玩速冻青蛙，
 * 她的魔法可以在地面上形成一个冰之阶梯，用来跳跃。每次跳跃的时候，
 * 琪露诺会消耗一点魔法，然后她可以从当前的格子跳到前面任意一个格子，
 * 前提是这两个格子之间的高度差不超过一个给定的值d。
 * 
 * 问题转化为：在给定数组中，找到从位置0到位置n的一条路径，
 * 每次可以向右跳到位置i（i > 当前位置），且满足abs(v[i] - v[j]) <= d，
 * 其中j是当前位置。要求路径长度（跳跃次数）的最小值。
 * 
 * 解题思路：
 * 这是一个典型的单调队列优化动态规划问题。
 * - 状态定义：dp[i] 表示到达位置i所需要的最少跳跃次数
 * - 状态转移方程：dp[i] = min(dp[j]) + 1，其中 j 满足 i - r <= j <= i - l
 * - 使用单调队列维护滑动窗口内的最小值
 * 
 * 时间复杂度：O(n)，每个元素最多被加入和弹出队列各一次
 * 空间复杂度：O(n)，需要dp数组和单调队列
 */

/**
 * 解决琪露诺跳跃问题
 * @param n 总格子数
 * @param l 最小跳跃距离
 * @param r 最大跳跃距离
 * @param v 每个格子的高度值
 * @return 到达终点的最少跳跃次数，不可达返回-1
 */
int solve(int n, int l, int r, vector<long long>& v) {
    // dp[i]表示到达位置i的最少跳跃次数
    vector<int> dp(n + 1, INT_MAX);
    dp[0] = 0;
    
    // 单调队列，保存的是索引，按照dp值单调递增
    deque<int> dq;
    dq.push_back(0);
    
    // 遍历每个位置i
    for (int i = 1; i <= n; i++) {
        // 移除队列中不在有效范围的元素（i - r <= j）
        while (!dq.empty() && dq.front() < i - r) {
            dq.pop_front();
        }
        
        // 如果队列不为空，当前dp[i]可以由队列头部的元素转移而来
        if (!dq.empty()) {
            dp[i] = dp[dq.front()] + 1;
        }
        
        // 当i >= l时，i可以作为后续位置的转移点
        if (i >= l) {
            // 维护队列的单调性，移除队列尾部dp值大于等于dp[i]的元素
            while (!dq.empty() && dp[i] <= dp[dq.back()]) {
                dq.pop_back();
            }
            dq.push_back(i);
        }
    }
    
    // 如果终点不可达，返回-1
    return dp[n] == INT_MAX ? -1 : dp[n];
}

int main() {
    int n, l, r;
    cin >> n >> l >> r;
    vector<long long> v(n + 1);
    for (int i = 0; i <= n; i++) {
        cin >> v[i];
    }
    
    cout << solve(n, l, r, v) << endl;
    
    return 0;
}