#include <iostream>
#include <vector>
#include <deque>
using namespace std;

/**
 * 题目名称：POJ 2823 Sliding Window
 * 题目来源：POJ (Peking University Online Judge)
 * 题目链接：http://poj.org/problem?id=2823
 * 题目难度：中等
 * 
 * 题目描述：
 * 给定一个大小为 n≤10^6 的数组。有一个大小为 k 的滑动窗口，它从数组的最左边移动到最右边。
 * 你只能在窗口中看到 k 个数字。每次滑动窗口向右移动一个位置。
 * 求出每次滑动窗口中的最大值和最小值。
 * 
 * 解题思路：
 * 这是单调队列的经典模板题。我们需要在O(n)时间内找到每个滑动窗口的最大值和最小值。
 * 使用两个单调队列：
 * 1. 单调递减队列：队首为窗口最大值
 * 2. 单调递增队列：队首为窗口最小值
 * 
 * 算法步骤：
 * 1. 使用双端队列维护窗口内元素的索引
 * 2. 维护一个单调递减队列求最大值
 * 3. 维持一个单调递增队列求最小值
 * 4. 每次窗口移动时更新两个队列并记录结果
 * 
 * 时间复杂度：O(n) - 每个元素最多入队和出队各两次
 * 空间复杂度：O(k) - 两个队列最多存储k个元素的索引
 * 
 * 是否为最优解：✅ 是，这是解决该问题的最优时间复杂度解法
 * 
 * 工程化考量：
 * - 使用std::deque实现高效的双端队列操作
 * - 使用ios::sync_with_stdio(false)和cin.tie(nullptr)优化输入输出性能
 * - 预先为结果向量分配空间以避免频繁的内存重分配
 */

vector<int> getMax(const vector<int>& arr, int n, int k) {
    /**
     * 单调递减队列求最大值
     * @param arr 输入数组
     * @param n 数组长度
     * @param k 窗口大小
     * @return 每个窗口的最大值数组
     */
    deque<int> dq; // 存储索引，而不是值本身
    vector<int> result(n - k + 1); // 预先分配空间
    
    for (int i = 0; i < n; ++i) {
        // 移除队列中超出窗口范围的元素索引
        while (!dq.empty() && dq.front() <= i - k) {
            dq.pop_front();
        }
        
        // 维护队列的单调递减性质
        // 从队尾移除所有小于当前元素的值对应的索引
        while (!dq.empty() && arr[dq.back()] <= arr[i]) {
            dq.pop_back();
        }
        
        // 将当前元素索引入队
        dq.push_back(i);
        
        // 当窗口大小达到k时，记录窗口最大值（队首元素对应的值）
        if (i >= k - 1) {
            result[i - k + 1] = arr[dq.front()];
        }
    }
    
    return result;
}

vector<int> getMin(const vector<int>& arr, int n, int k) {
    /**
     * 单调递增队列求最小值
     * @param arr 输入数组
     * @param n 数组长度
     * @param k 窗口大小
     * @return 每个窗口的最小值数组
     */
    deque<int> dq; // 存储索引，而不是值本身
    vector<int> result(n - k + 1); // 预先分配空间
    
    for (int i = 0; i < n; ++i) {
        // 移除队列中超出窗口范围的元素索引
        while (!dq.empty() && dq.front() <= i - k) {
            dq.pop_front();
        }
        
        // 维护队列的单调递增性质
        // 从队尾移除所有大于当前元素的值对应的索引
        while (!dq.empty() && arr[dq.back()] >= arr[i]) {
            dq.pop_back();
        }
        
        // 将当前元素索引入队
        dq.push_back(i);
        
        // 当窗口大小达到k时，记录窗口最小值（队首元素对应的值）
        if (i >= k - 1) {
            result[i - k + 1] = arr[dq.front()];
        }
    }
    
    return result;
}

int main() {
    // 优化C++输入输出性能
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    // 读取输入
    int n, k;
    cin >> n >> k;
    
    vector<int> arr(n);
    for (int i = 0; i < n; ++i) {
        cin >> arr[i];
    }
    
    // 计算最大值和最小值
    vector<int> minValues = getMin(arr, n, k);
    vector<int> maxValues = getMax(arr, n, k);
    
    // 输出最小值结果
    for (size_t i = 0; i < minValues.size(); ++i) {
        if (i > 0) {
            cout << " ";
        }
        cout << minValues[i];
    }
    cout << endl;
    
    // 输出最大值结果
    for (size_t i = 0; i < maxValues.size(); ++i) {
        if (i > 0) {
            cout << " ";
        }
        cout << maxValues[i];
    }
    cout << endl;
    
    return 0;
}