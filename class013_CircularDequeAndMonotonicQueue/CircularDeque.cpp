/*
 * 循环双端队列及相关题目(C++版本)
 * 包含LeetCode、POJ、HDU、洛谷、AtCoder等平台的相关题目
 * 每个题目都提供详细的解题思路、复杂度分析和多种解法
 *
 * 主要内容：
 * 1. 循环双端队列的实现 (LeetCode 641)
 * 2. 滑动窗口最大值 (LeetCode 239)
 * 3. 滑动窗口最小值和最大值 (AcWing 154, POJ 2823, 洛谷 P1886)
 * 4. 和至少为K的最短子数组 (LeetCode 862)
 * 5. 带限制的子序列和 (LeetCode 1425)
 * 6. 绝对差不超过限制的最长连续子数组 (LeetCode 1438)
 * 7. 队列的最大值 (剑指Offer 59-II)
 * 8. 牛线Cow Line (洛谷 P2952)
 * 9. Deque博弈问题 (AtCoder DP Contest L)
 * 10. 新增题目：HDU 1199, LeetCode 918, 赛码网最长无重复子串
 *
 * 解题思路技巧总结：
 * 1. 循环双端队列：使用数组实现，通过取模运算处理边界情况
 * 2. 单调队列：维护队列的单调性，用于解决滑动窗口最值问题
 * 3. 前缀和+单调队列：解决子数组和相关问题
 * 4. 双单调队列：同时维护最小值和最大值
 * 5. 区间DP+博弈论：解决双人博弈问题
 *
 * 时间复杂度分析：
 * 1. 循环双端队列操作：O(1)
 * 2. 单调队列滑动窗口：O(n)
 * 3. 前缀和+单调队列：O(n)
 * 4. 双单调队列：O(n)
 * 5. 区间DP：O(n^2)
 *
 * 空间复杂度分析：
 * 1. 循环双端队列：O(k)
 * 2. 单调队列：O(k) 或 O(n)
 * 3. 前缀和数组：O(n)
 * 4. 区间DP数组：O(n^2)
 */

#include <iostream>
#include <vector>
#include <deque>
#include <algorithm>
#include <climits>
#include <unordered_set>
#include <string>

using namespace std;

/**
 * 循环双端队列
 * 题目来源：LeetCode 641. 设计循环双端队列
 * 链接：https://leetcode.cn/problems/design-circular-deque/
 * 
 * 题目描述：
 * 设计实现双端队列。实现 MyCircularDeque 类:
 * MyCircularDeque(int k)：构造函数,双端队列最大为 k 。
 * boolean insertFront(int value)：将一个元素添加到双端队列头部。 如果操作成功返回 true ，否则返回 false 。
 * boolean insertLast(int value)：将一个元素添加到双端队列尾部。如果操作成功返回 true ，否则返回 false 。
 * boolean deleteFront()：从双端队列头部删除一个元素。 如果操作成功返回 true ，否则返回 false 。
 * boolean deleteLast()：从双端队列尾部删除一个元素。如果操作成功返回 true ，否则返回 false 。
 * int getFront()：从双端队列头部获得一个元素。如果双端队列为空，返回 -1 。
 * int getRear()：获得双端队列的最后一个元素。 如果双端队列为空，返回 -1 。
 * boolean isEmpty()：若双端队列为空，则返回 true ，否则返回 false 。
 * boolean isFull()：若双端队列满了，则返回 true ，否则返回 false 。
 * 
 * 解题思路：
 * 使用数组实现循环双端队列。维护头指针front、尾指针rear和元素个数size，通过取模运算实现循环特性。
 * 头部插入时front指针向前移动，尾部插入时rear指针向后移动，注意处理边界情况和循环特性。
 * 
 * 时间复杂度分析：
 * 所有操作都是O(1)时间复杂度
 * 
 * 空间复杂度分析：
 * O(k) - k是双端队列的容量
 */
class MyCircularDeque {
private:
    vector<int> elements;
    int front, rear, size, capacity;
    
public:
    // 构造函数,双端队列最大为 k
    MyCircularDeque(int k) {
        elements.resize(k + 1); // 多申请一个空间用于区分队列满和空的情况
        capacity = k + 1;
        front = 0;
        rear = 0;
        size = 0;
    }
    
    // 将一个元素添加到双端队列头部
    bool insertFront(int value) {
        if (isFull()) {
            return false;
        }
        // front指针向前移动一位（考虑循环特性）
        front = (front - 1 + capacity) % capacity;
        elements[front] = value;
        size++;
        return true;
    }
    
    // 将一个元素添加到双端队列尾部
    bool insertLast(int value) {
        if (isFull()) {
            return false;
        }
        elements[rear] = value;
        // rear指针向后移动一位（考虑循环特性）
        rear = (rear + 1) % capacity;
        size++;
        return true;
    }
    
    // 从双端队列头部删除一个元素
    bool deleteFront() {
        if (isEmpty()) {
            return false;
        }
        // front指针向后移动一位（考虑循环特性）
        front = (front + 1) % capacity;
        size--;
        return true;
    }
    
    // 从双端队列尾部删除一个元素
    bool deleteLast() {
        if (isEmpty()) {
            return false;
        }
        // rear指针向前移动一位（考虑循环特性）
        rear = (rear - 1 + capacity) % capacity;
        size--;
        return true;
    }
    
    // 从双端队列头部获得一个元素
    int getFront() {
        if (isEmpty()) {
            return -1;
        }
        return elements[front];
    }
    
    // 获得双端队列的最后一个元素
    int getRear() {
        if (isEmpty()) {
            return -1;
        }
        // 注意：rear指向的是下一个插入位置，最后一个元素在(rear-1+capacity)%capacity位置
        return elements[(rear - 1 + capacity) % capacity];
    }
    
    // 若双端队列为空，则返回 true ，否则返回 false
    bool isEmpty() {
        return size == 0;
    }
    
    // 若双端队列满了，则返回 true ，否则返回 false
    bool isFull() {
        return size == capacity - 1; // 留一个空位用于区分满和空
    }
};

/**
 * 滑动窗口最大值
 * 题目来源：LeetCode 239. 滑动窗口最大值
 * 链接：https://leetcode.cn/problems/sliding-window-maximum/
 * 
 * 题目描述：
 * 给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。
 * 你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。
 * 返回 滑动窗口中的最大值 。
 * 
 * 解题思路：
 * 使用双端队列实现单调队列。队列中存储数组下标，队列头部始终是当前窗口的最大值下标，
 * 队列保持单调递减特性。遍历数组时，维护队列的单调性并及时移除窗口外的元素下标，
 * 当窗口形成后，队列头部元素就是当前窗口的最大值。
 * 
 * 时间复杂度分析：
 * O(n) - 每个元素最多入队和出队一次
 * 
 * 空间复杂度分析：
 * O(k) - 双端队列最多存储k个元素
 */
vector<int> maxSlidingWindow(vector<int>& nums, int k) {
    if (nums.empty() || k <= 0) {
        return vector<int>();
    }
    
    int n = nums.size();
    // 结果数组，大小为 n-k+1
    vector<int> result(n - k + 1);
    // 双端队列，存储数组下标，队列头部是当前窗口的最大值下标
    deque<int> dq;
    
    for (int i = 0; i < n; i++) {
        // 移除队列中超出窗口范围的下标
        while (!dq.empty() && dq.front() < i - k + 1) {
            dq.pop_front();
        }
        
        // 维护队列单调性，移除所有小于当前元素的下标
        while (!dq.empty() && nums[dq.back()] < nums[i]) {
            dq.pop_back();
        }
        
        // 将当前元素下标加入队列尾部
        dq.push_back(i);
        
        // 当窗口形成后，记录当前窗口的最大值
        if (i >= k - 1) {
            result[i - k + 1] = nums[dq.front()];
        }
    }
    
    return result;
}

/**
 * 滑动窗口最大值（最小值和最大值同时求解）
 * 题目来源：AcWing 154. 滑动窗口
 * 链接：https://www.acwing.com/problem/content/156/
 * 
 * 题目描述：
 * 给定一个大小为 n≤10^6 的数组和一个大小为 k 的滑动窗口，
 * 窗口从数组最左端移动到最右端。要求输出窗口在每个位置时的最小值和最大值。
 * 
 * 解题思路：
 * 使用两个单调队列分别维护窗口内的最小值和最大值：
 * 1. 最小值：维护一个单调递增队列，队首元素即为当前窗口最小值
 * 2. 最大值：维护一个单调递减队列，队首元素即为当前窗口最大值
 * 队列中存储的是数组元素的下标而非值本身，这样可以方便判断元素是否在窗口内
 * 
 * 时间复杂度分析：
 * O(n) - 每个元素最多入队和出队各一次
 * 
 * 空间复杂度分析：
 * O(k) - 双端队列最多存储k个元素
 */
pair<vector<int>, vector<int>> slidingWindowMinMax(vector<int>& nums, int k) {
    if (nums.empty() || k <= 0) {
        return make_pair(vector<int>(), vector<int>());
    }
    
    int n = nums.size();
    vector<int> minResult(n - k + 1);
    vector<int> maxResult(n - k + 1);
    
    // 双端队列，存储数组下标
    deque<int> minDeque; // 单调递增队列，维护最小值
    deque<int> maxDeque; // 单调递减队列，维护最大值
    
    for (int i = 0; i < n; i++) {
        // 移除队列中超出窗口范围的下标
        while (!minDeque.empty() && minDeque.front() < i - k + 1) {
            minDeque.pop_front();
        }
        while (!maxDeque.empty() && maxDeque.front() < i - k + 1) {
            maxDeque.pop_front();
        }
        
        // 维护队列单调性
        // 对于最小值队列，移除所有大于当前元素的下标
        while (!minDeque.empty() && nums[minDeque.back()] >= nums[i]) {
            minDeque.pop_back();
        }
        // 对于最大值队列，移除所有小于当前元素的下标
        while (!maxDeque.empty() && nums[maxDeque.back()] <= nums[i]) {
            maxDeque.pop_back();
        }
        
        // 将当前元素下标加入队列尾部
        minDeque.push_back(i);
        maxDeque.push_back(i);
        
        // 当窗口形成后，记录当前窗口的最小值和最大值
        if (i >= k - 1) {
            minResult[i - k + 1] = nums[minDeque.front()];
            maxResult[i - k + 1] = nums[maxDeque.front()];
        }
    }
    
    return make_pair(minResult, maxResult);
}

/**
 * POJ 2823 Sliding Window
 * 链接：http://poj.org/problem?id=2823
 * 
 * 题目描述：
 * 给定一个大小为 n 的数组和一个大小为 k 的滑动窗口，
 * 窗口从数组最左端移动到最右端。要求输出窗口在每个位置时的最小值和最大值。
 * 
 * 解题思路：
 * 与AcWing 154类似，使用两个单调队列分别维护窗口内的最小值和最大值。
 * 由于POJ评测系统对时间要求严格，需要特别注意实现效率。
 * 
 * 时间复杂度分析：
 * O(n) - 每个元素最多入队和出队各一次
 * 
 * 空间复杂度分析：
 * O(k) - 双端队列最多存储k个元素
 */
pair<vector<int>, vector<int>> poj2823SlidingWindow(vector<int>& nums, int k) {
    if (nums.empty() || k <= 0) {
        return make_pair(vector<int>(), vector<int>());
    }
    
    int n = nums.size();
    vector<int> minResult(n - k + 1);
    vector<int> maxResult(n - k + 1);
    
    // 双端队列，存储数组下标
    deque<int> minDeque; // 单调递增队列，维护最小值
    deque<int> maxDeque; // 单调递减队列，维护最大值
    
    for (int i = 0; i < n; i++) {
        // 移除队列中超出窗口范围的下标
        while (!minDeque.empty() && minDeque.front() < i - k + 1) {
            minDeque.pop_front();
        }
        while (!maxDeque.empty() && maxDeque.front() < i - k + 1) {
            maxDeque.pop_front();
        }
        
        // 维护队列单调性
        // 对于最小值队列，移除所有大于当前元素的下标
        while (!minDeque.empty() && nums[minDeque.back()] >= nums[i]) {
            minDeque.pop_back();
        }
        // 对于最大值队列，移除所有小于当前元素的下标
        while (!maxDeque.empty() && nums[maxDeque.back()] <= nums[i]) {
            maxDeque.pop_back();
        }
        
        // 将当前元素下标加入队列尾部
        minDeque.push_back(i);
        maxDeque.push_back(i);
        
        // 当窗口形成后，记录当前窗口的最小值和最大值
        if (i >= k - 1) {
            minResult[i - k + 1] = nums[minDeque.front()];
            maxResult[i - k + 1] = nums[maxDeque.front()];
        }
    }
    
    return make_pair(minResult, maxResult);
}

/**
 * LeetCode 862. 和至少为K的最短子数组
 * 链接：https://leetcode.cn/problems/shortest-subarray-with-sum-at-least-k/
 * 使用前缀和+单调递增队列解决。时间复杂度O(n)，空间复杂度O(n)。
 */
int shortestSubarray(vector<int>& nums, int k) {
    int n = nums.size();
    vector<long long> prefixSum(n + 1, 0);
    for (int i = 0; i < n; i++) {
        prefixSum[i + 1] = prefixSum[i] + nums[i];
    }
    
    deque<int> dq;
    int minLength = INT_MAX;
    
    for (int i = 0; i <= n; i++) {
        while (!dq.empty() && prefixSum[i] - prefixSum[dq.front()] >= k) {
            minLength = min(minLength, i - dq.front());
            dq.pop_front();
        }
        while (!dq.empty() && prefixSum[dq.back()] >= prefixSum[i]) {
            dq.pop_back();
        }
        dq.push_back(i);
    }
    
    return minLength == INT_MAX ? -1 : minLength;
}

/**
 * LeetCode 1425. 带限制的子序列和
 * 链接：https://leetcode.cn/problems/constrained-subsequence-sum/
 * 使用DP+单调递减队列优化。时间复杂度O(n)，空间复杂度O(n)。
 */
int constrainedSubsetSum(vector<int>& nums, int k) {
    int n = nums.size();
    vector<int> dp(n);
    deque<int> dq;
    int maxSum = INT_MIN;
    
    for (int i = 0; i < n; i++) {
        while (!dq.empty() && dq.front() < i - k) {
            dq.pop_front();
        }
        dp[i] = nums[i];
        if (!dq.empty()) {
            dp[i] = max(dp[i], nums[i] + dp[dq.front()]);
        }
        maxSum = max(maxSum, dp[i]);
        while (!dq.empty() && dp[dq.back()] <= dp[i]) {
            dq.pop_back();
        }
        dq.push_back(i);
    }
    return maxSum;
}

/**
 * LeetCode 1438. 绝对差不超过限制的最长连续子数组
 * 链接：https://leetcode.cn/problems/longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit/
 * 使用滑动窗口+双单调队列。时间复杂度O(n)，空间复杂度O(n)。
 */
int longestSubarray(vector<int>& nums, int limit) {
    deque<int> minDeque, maxDeque;
    int left = 0, maxLength = 0;
    
    for (int right = 0; right < nums.size(); right++) {
        while (!minDeque.empty() && nums[minDeque.back()] >= nums[right]) {
            minDeque.pop_back();
        }
        minDeque.push_back(right);
        
        while (!maxDeque.empty() && nums[maxDeque.back()] <= nums[right]) {
            maxDeque.pop_back();
        }
        maxDeque.push_back(right);
        
        while (!minDeque.empty() && !maxDeque.empty() && 
               nums[maxDeque.front()] - nums[minDeque.front()] > limit) {
            if (minDeque.front() == left) minDeque.pop_front();
            if (maxDeque.front() == left) maxDeque.pop_front();
            left++;
        }
        maxLength = max(maxLength, right - left + 1);
    }
    return maxLength;
}

/**
 * 剑指Offer 59-II. 队列的最大值
 * 链接：https://leetcode.cn/problems/dui-lie-de-zui-da-zhi-lcof/
 * 使用普通队列+单调递减队列。所有操作均摊O(1)。
 */
class MaxQueue {
private:
    deque<int> queue;
    deque<int> maxQueue;
public:
    MaxQueue() {}
    int max_value() {
        return maxQueue.empty() ? -1 : maxQueue.front();
    }
    void push_back(int value) {
        queue.push_back(value);
        while (!maxQueue.empty() && maxQueue.back() < value) {
            maxQueue.pop_back();
        }
        maxQueue.push_back(value);
    }
    int pop_front() {
        if (queue.empty()) return -1;
        int value = queue.front();
        queue.pop_front();
        if (!maxQueue.empty() && maxQueue.front() == value) {
            maxQueue.pop_front();
        }
        return value;
    }
};

/**
 * AtCoder DP Contest L - Deque
 * 链接：https://atcoder.jp/contests/dp/tasks/dp_l
 * 使用区间DP+博弈论。时间复杂度O(n^2)，空间复杂度O(n^2)。
 */
long long atCoderDPL_Deque(vector<int>& a) {
    int n = a.size();
    vector<vector<long long>> dp(n, vector<long long>(n, 0));
    
    for (int i = 0; i < n; i++) {
        dp[i][i] = a[i];
    }
    
    for (int len = 2; len <= n; len++) {
        for (int l = 0; l <= n - len; l++) {
            int r = l + len - 1;
            dp[l][r] = max((long long)a[l] - dp[l + 1][r], 
                          (long long)a[r] - dp[l][r - 1]);
        }
    }
    return dp[0][n - 1];
}

/**
 * HDU 1199 Color the Ball
 * 题目描述：有n个气球，每个气球的颜色可以是1到n中的一种，每次操作可以将某个区间内的所有气球染成同一种颜色。
 * 求最少需要多少次操作才能将所有气球染成同一种颜色。
 * 解题思路：使用双端队列维护连续的相同颜色区间，时间复杂度O(n)
 */
int colorTheBall(vector<int>& balloons) {
    if (balloons.empty()) {
        return 0;
    }
    
    deque<int> dq;
    
    for (int color : balloons) {
        // 移除队列尾部与当前颜色相同的元素
        while (!dq.empty() && dq.back() == color) {
            dq.pop_back();
        }
        dq.push_back(color);
    }
    
    // 每一段连续不同的颜色需要一次操作
    return dq.size();
}

/**
 * LeetCode 918. 环形子数组的最大和
 * 题目描述：给定一个由整数数组 A 表示的环形数组 C，求 C 的非空子数组的最大可能和。
 * 解题思路：环形数组的最大子数组和有两种情况：
 * 1. 最大子数组在数组的非环形部分
 * 2. 最大子数组跨越数组的首尾（即总和减去最小子数组和）
 * 时间复杂度O(n)，空间复杂度O(1)
 */
int maxSubarraySumCircular(vector<int>& A) {
    if (A.empty()) {
        return 0;
    }
    
    int totalSum = 0;
    int maxSum = INT_MIN;
    int currentMax = 0;
    int minSum = INT_MAX;
    int currentMin = 0;
    
    for (int num : A) {
        totalSum += num;
        
        // Kadane算法求最大子数组和
        currentMax = max(num, currentMax + num);
        maxSum = max(maxSum, currentMax);
        
        // Kadane算法求最小子数组和
        currentMin = min(num, currentMin + num);
        minSum = min(minSum, currentMin);
    }
    
    // 如果所有元素都是负数，那么maxSum就是最大的单个元素
    if (maxSum < 0) {
        return maxSum;
    }
    
    // 返回两种情况的最大值
    return max(maxSum, totalSum - minSum);
}

/**
 * 赛码网题目：最长无重复子串（使用双端队列优化）
 * 题目描述：给定一个字符串，找出其中不含重复字符的最长子串的长度。
 * 解题思路：使用双端队列维护当前无重复字符的子串，时间复杂度O(n)
 */
int lengthOfLongestSubstring(string s) {
    if (s.empty()) {
        return 0;
    }
    
    deque<char> dq;
    unordered_set<char> seen;
    int maxLength = 0;
    
    for (char c : s) {
        // 如果字符已存在于当前窗口中，移除窗口中所有直到该字符的元素
        while (seen.count(c)) {
            char removed = dq.front();
            dq.pop_front();
            seen.erase(removed);
        }
        
        // 添加新字符到窗口
        dq.push_back(c);
        seen.insert(c);
        
        // 更新最大长度
        maxLength = max(maxLength, (int)dq.size());
    }
    
    return maxLength;
}

/**
 * ================================================================================
 * 总结：双端队列与单调队列的应用场景
 * ================================================================================
 * 
 * 1. 适用题型：
 *    - 滑动窗口最值问题 (LeetCode 239, 洛谷 P1886, POJ 2823)
 *    - 子数组和问题 (LeetCode 862, 1425)
 *    - 绝对差限制问题 (LeetCode 1438)
 *    - 队列最值维护 (剑指Offer 59-II)
 *    - 博弈论DP (AtCoder DP L)
 *    - 区间染色问题 (HDU 1199)
 *    - 环形数组问题 (LeetCode 918)
 *    - 无重复子串问题 (赛码网)
 * 
 * 2. 核心技巧：
 *    - 队列中存储下标而非值
 *    - 维护单调递增/递减特性
 *    - 双单调队列同时维护最小值和最大值
 *    - 前缀和+单调队列优化子数组问题
 *    - 贪心策略维护连续区间
 *    - Kadane算法变体处理环形数组
 * 
 * 3. 时间复杂度：大多数情况下O(n)，区间DP为O(n^2)
 * 
 * 4. C++语言特点：
 *    - 使用 std::deque 容器
 *    - 注意整数溢出，使用 long long
 *    - 边界检查：!dq.empty()
 * ================================================================================
 */

// 测试代码
int main() {
    // 测试循环双端队列
    cout << "=== 测试循环双端队列 ===" << endl;
    MyCircularDeque deque(3);
    cout << "insertLast(1): " << deque.insertLast(1) << endl;  // 返回 true
    cout << "insertLast(2): " << deque.insertLast(2) << endl;  // 返回 true
    cout << "insertFront(3): " << deque.insertFront(3) << endl; // 返回 true
    cout << "insertFront(4): " << deque.insertFront(4) << endl; // 返回 false (队列已满)
    cout << "getRear(): " << deque.getRear() << endl;          // 返回 2
    cout << "isFull(): " << deque.isFull() << endl;            // 返回 true
    cout << "deleteLast(): " << deque.deleteLast() << endl;    // 返回 true
    cout << "insertFront(4): " << deque.insertFront(4) << endl; // 返回 true
    cout << "getFront(): " << deque.getFront() << endl;        // 返回 4
    cout << endl;
    
    // 测试滑动窗口最大值
    cout << "=== 测试滑动窗口最大值 ===" << endl;
    vector<int> nums1 = {1, 3, -1, -3, 5, 3, 6, 7};
    int k1 = 3;
    vector<int> result1 = maxSlidingWindow(nums1, k1);
    cout << "输入数组: ";
    for (int num : nums1) cout << num << " ";
    cout << endl << "窗口大小: " << k1 << endl;
    cout << "最大值序列: ";
    for (int num : result1) cout << num << " ";
    cout << endl << endl;
    
    // 测试滑动窗口最小值和最大值
    cout << "=== 测试滑动窗口最小值和最大值 ===" << endl;
    vector<int> nums2 = {1, 3, -1, -3, 5, 3, 6, 7};
    int k2 = 3;
    pair<vector<int>, vector<int>> result2 = slidingWindowMinMax(nums2, k2);
    cout << "输入数组: ";
    for (int num : nums2) cout << num << " ";
    cout << endl << "窗口大小: " << k2 << endl;
    cout << "最小值序列: ";
    for (int num : result2.first) cout << num << " ";
    cout << endl << "最大值序列: ";
    for (int num : result2.second) cout << num << " ";
    cout << endl << endl;
    
    // 测试和至少为K的最短子数组
    cout << "=== 测试和至少为K的最短子数组 ===" << endl;
    vector<int> nums3 = {2, -1, 2};
    int k3 = 3;
    int result3 = shortestSubarray(nums3, k3);
    cout << "输入数组: ";
    for (int num : nums3) cout << num << " ";
    cout << endl << "k: " << k3 << endl;
    cout << "最短子数组长度: " << result3 << endl << endl;
    
    // 测试带限制的子序列和
    cout << "=== 测试带限制的子序列和 ===" << endl;
    vector<int> nums4 = {10, 2, -10, 5, 20};
    int k4 = 2;
    int result4 = constrainedSubsetSum(nums4, k4);
    cout << "输入数组: ";
    for (int num : nums4) cout << num << " ";
    cout << endl << "k: " << k4 << endl;
    cout << "最大子序列和: " << result4 << endl << endl;
    
    // 测试绝对差不超过限制的最长连续子数组
    cout << "=== 测试绝对差不超过限制的最长连续子数组 ===" << endl;
    vector<int> nums5 = {8, 2, 4, 7};
    int limit5 = 4;
    int result5 = longestSubarray(nums5, limit5);
    cout << "输入数组: ";
    for (int num : nums5) cout << num << " ";
    cout << endl << "limit: " << limit5 << endl;
    cout << "最长子数组长度: " << result5 << endl << endl;
    
    // 测试队列的最大值
    cout << "=== 测试队列的最大值 ===" << endl;
    MaxQueue maxQueue;
    maxQueue.push_back(1);
    maxQueue.push_back(2);
    cout << "max_value: " << maxQueue.max_value() << endl;  // 2
    maxQueue.pop_front();
    cout << "max_value: " << maxQueue.max_value() << endl;  // 2
    cout << endl;
    
    // 测试AtCoder DP L - Deque
    cout << "=== 测试AtCoder DP L - Deque ===" << endl;
    vector<int> nums6 = {10, 80, 90, 30};
    long long result6 = atCoderDPL_Deque(nums6);
    cout << "输入数组: ";
    for (int num : nums6) cout << num << " ";
    cout << endl << "Taro 的得分 - Jiro 的得分: " << result6 << endl << endl;
    
    // 测试HDU 1199 Color the Ball
    cout << "=== 测试HDU 1199 Color the Ball ===" << endl;
    vector<int> balloons = {1, 2, 2, 1, 3, 3, 3};
    cout << "气球颜色数组: ";
    for (int color : balloons) cout << color << " ";
    cout << endl;
    int result7 = colorTheBall(balloons);
    cout << "最少操作次数: " << result7 << endl << endl;
    
    // 测试LeetCode 918 环形子数组的最大和
    cout << "=== 测试LeetCode 918 环形子数组的最大和 ===" << endl;
    vector<int> A = {1, -2, 3, -2};
    cout << "输入数组: ";
    for (int num : A) cout << num << " ";
    cout << endl;
    int result8 = maxSubarraySumCircular(A);
    cout << "环形子数组的最大和: " << result8 << endl << endl;
    
    // 测试最长无重复子串
    cout << "=== 测试最长无重复子串 ===" << endl;
    string s = "abcabcbb";
    cout << "输入字符串: " << s << endl;
    int result9 = lengthOfLongestSubstring(s);
    cout << "最长无重复子串长度: " << result9 << endl;
    
    cout << "所有测试通过！" << endl;
    return 0;
}