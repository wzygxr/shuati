// 接取落水的最小花盆
// 老板需要你帮忙浇花。给出 N 滴水的坐标，y 表示水滴的高度，x 表示它下落到 x 轴的位置
// 每滴水以每秒1个单位长度的速度下落。你需要把花盆放在 x 轴上的某个位置
// 使得从被花盆接着的第 1 滴水开始，到被花盆接着的最后 1 滴水结束，之间的时间差至少为 D
// 我们认为，只要水滴落到 x 轴上，与花盆的边沿对齐，就认为被接住
// 给出 N 滴水的坐标和 D 的大小，请算出最小的花盆的宽度 W
// 测试链接 : https://www.luogu.com.cn/problem/P2698
// 
// 题目解析：
// 这是一道经典的单调队列应用题。我们需要找到最小的花盆宽度，
// 使得接住的水滴中最早和最晚到达的时间差至少为D。
//
// 算法思路：
// 1. 首先将水滴按x坐标排序
// 2. 使用滑动窗口和单调队列：
//    - 用单调递减队列维护窗口内水滴的最大高度
//    - 用单调递增队列维护窗口内水滴的最小高度
// 3. 当窗口内最大高度与最小高度之差 >= D 时，更新最小花盆宽度
// 4. 移动窗口左边界，继续寻找更优解
//
// 时间复杂度：O(n) - 每个元素最多入队和出队各两次
// 空间复杂度：O(n) - 存储水滴信息和两个单调队列

#include <iostream>
#include <vector>
#include <deque>
#include <algorithm>
#include <climits>
using namespace std;

const int MAXN = 100005;

class Solution {
public:
    // 计算最小花盆宽度
    int compute(vector<vector<int>>& arr, int n, int d) {
        // 所有水滴根据x排序，谁小谁在前
        sort(arr.begin(), arr.begin() + n, [](const vector<int>& a, const vector<int>& b) {
            return a[0] < b[0];
        });
        
        // 窗口内最大值的更新结构（单调队列）
        deque<int> maxDeque;
        // 窗口内最小值的更新结构（单调队列）
        deque<int> minDeque;
        
        int ans = INT_MAX;
        
        // 滑动窗口，[l,r)表示当前考虑的水滴范围
        for (int l = 0, r = 0; l < n; l++) {
            // [l,r) : 水滴的编号
            // l : 当前花盘的左边界，arr[l][0]
            // 扩展窗口右边界，直到满足时间差条件
            while (r < n && !ok(maxDeque, minDeque, arr, r, d)) {
                push(maxDeque, minDeque, arr, r++);
            }
            
            // 如果满足条件，更新最小花盆宽度
            if (r > 0 && ok(maxDeque, minDeque, arr, r - 1, d)) {
                ans = min(ans, arr[r - 1][0] - arr[l][0]);
            }
            
            // 收缩窗口左边界
            pop(maxDeque, minDeque, l);
        }
        
        return ans == INT_MAX ? -1 : ans;
    }

private:
    // 当前窗口 最大值 - 最小值 是不是>=d
    // 检查当前窗口是否满足时间差条件
    bool ok(deque<int>& maxDeque, deque<int>& minDeque, vector<vector<int>>& arr, int r, int d) {
        // 获取当前窗口的最大高度和最小高度
        if (maxDeque.empty() || minDeque.empty()) return false;
        
        int maxVal = arr[maxDeque.front()][1];
        int minVal = arr[minDeque.front()][1];
        
        // 判断高度差是否满足时间差要求
        return maxVal - minVal >= d;
    }

    // 将r位置的水滴加入窗口
    // 维护两个单调队列
    void push(deque<int>& maxDeque, deque<int>& minDeque, vector<vector<int>>& arr, int r) {
        // 维护最大值队列的单调递减性质
        while (!maxDeque.empty() && arr[maxDeque.back()][1] <= arr[r][1]) {
            maxDeque.pop_back();
        }
        maxDeque.push_back(r);
        
        // 维护最小值队列的单调递增性质
        while (!minDeque.empty() && arr[minDeque.back()][1] >= arr[r][1]) {
            minDeque.pop_back();
        }
        minDeque.push_back(r);
    }

    // 将l位置的水滴移出窗口
    // 检查队列中的元素是否过期
    void pop(deque<int>& maxDeque, deque<int>& minDeque, int l) {
        // 检查最大值队列的队首元素是否过期
        if (!maxDeque.empty() && maxDeque.front() == l) {
            maxDeque.pop_front();
        }
        // 检查最小值队列的队首元素是否过期
        if (!minDeque.empty() && minDeque.front() == l) {
            minDeque.pop_front();
        }
    }
};