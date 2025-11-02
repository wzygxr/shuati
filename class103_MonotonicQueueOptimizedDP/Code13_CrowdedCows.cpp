#include <iostream>
#include <vector>
#include <deque>
#include <algorithm>
using namespace std;

/**
 * USACO 挤奶牛 (Crowded Cows)
 * 题目来源：USACO
 * 
 * 题目描述：
 * 在一条直线上有N头奶牛，每头奶牛的位置为x[i]，权值为v[i]。
 * 一头奶牛被认为是"拥挤的"，当且仅当它左边有至少一头奶牛，右边也有至少一头奶牛，
 * 且左边那头发的权值不小于它，右边那头发的权值也不小于它。
 * 求有多少头奶牛是拥挤的。
 * 
 * 解题思路：
 * 这是一个典型的滑动窗口最大值问题，使用单调队列优化。
 * - 我们需要找出每个位置i左边窗口内的最大值和右边窗口内的最大值
 * - 对于每个位置i，如果左边最大值 >= v[i] 且右边最大值 >= v[i]，则这头奶牛是拥挤的
 * - 使用单调队列维护滑动窗口中的最大值
 * 
 * 时间复杂度：O(n)，每个元素最多被加入和弹出队列各一次
 * 空间复杂度：O(n)，需要存储最大值数组和单调队列
 */

/**
 * 解决挤奶牛问题
 * @param n 奶牛数量
 * @param d 窗口宽度
 * @param positions 奶牛的位置数组
 * @param values 奶牛的权值数组
 * @return 拥挤的奶牛数量
 */
int solve(int n, int d, vector<int>& positions, vector<int>& values) {
    // 记录每个位置右边窗口内的最大值
    vector<int> rightMax(n, -1);
    
    // 从右到左遍历，使用单调队列维护窗口内的最大值
    deque<int> dq;
    for (int i = n - 1; i >= 0; i--) {
        // 移除队列中位置超出窗口的元素（x[j] > x[i] + d）
        while (!dq.empty() && positions[dq.front()] > positions[i] + d) {
            dq.pop_front();
        }
        
        // 如果队列不为空，当前位置的右边最大值就是队列头部的元素
        if (!dq.empty()) {
            rightMax[i] = values[dq.front()];
        }
        
        // 维护队列的单调性，移除队列尾部小于等于当前元素的值
        while (!dq.empty() && values[i] >= values[dq.back()]) {
            dq.pop_back();
        }
        dq.push_back(i);
    }
    
    // 统计拥挤的奶牛数量
    int count = 0;
    dq.clear();
    
    // 从左到右遍历，使用单调队列维护窗口内的最大值
    for (int i = 0; i < n; i++) {
        // 移除队列中位置超出窗口的元素（x[j] < x[i] - d）
        while (!dq.empty() && positions[dq.front()] < positions[i] - d) {
            dq.pop_front();
        }
        
        // 如果左边有最大值且右边有最大值，并且都大于等于当前值，则是拥挤的奶牛
        if (!dq.empty() && rightMax[i] != -1 && values[dq.front()] >= values[i] && rightMax[i] >= values[i]) {
            count++;
        }
        
        // 维护队列的单调性，移除队列尾部小于等于当前元素的值
        while (!dq.empty() && values[i] >= values[dq.back()]) {
            dq.pop_back();
        }
        dq.push_back(i);
    }
    
    return count;
}

int main() {
    int n, d;
    cin >> n >> d;
    vector<pair<int, int>> cows(n);
    for (int i = 0; i < n; i++) {
        cin >> cows[i].first >> cows[i].second;
    }
    
    // 按照位置排序
    sort(cows.begin(), cows.end());
    
    vector<int> positions(n);
    vector<int> values(n);
    for (int i = 0; i < n; i++) {
        positions[i] = cows[i].first;
        values[i] = cows[i].second;
    }
    
    cout << solve(n, d, positions, values) << endl;
    
    return 0;
}