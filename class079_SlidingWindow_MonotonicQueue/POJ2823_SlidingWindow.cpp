// POJ 2823 Sliding Window
// 给定一个大小为 n≤10^6 的数组。有一个大小为 k 的滑动窗口，它从数组的最左边移动到最右边。
// 你只能在窗口中看到 k 个数字。每次滑动窗口向右移动一个位置。
// 求出每次滑动窗口中的最大值和最小值。
// 测试链接：http://poj.org/problem?id=2823
//
// 题目解析：
// 这是单调队列的经典模板题。我们需要在O(n)时间内找到每个滑动窗口的最大值和最小值。
// 使用两个单调队列：
// 1. 单调递减队列：队首为窗口最大值
// 2. 单调递增队列：队首为窗口最小值
//
// 算法思路：
// 1. 使用双端队列维护窗口内元素的索引
// 2. 维护一个单调递减队列求最大值
// 3. 维持一个单调递增队列求最小值
// 4. 每次窗口移动时更新两个队列并记录结果
//
// 时间复杂度：O(n) - 每个元素最多入队和出队各两次
// 空间复杂度：O(k) - 两个队列最多存储k个元素的索引

#include <iostream>
#include <vector>
#include <deque>
using namespace std;

const int MAXN = 1000005;

int n, k;
int arr[MAXN];
int max_result[MAXN], min_result[MAXN];

// 单调递减队列求最大值
void getMax() {
    deque<int> dq;
    for (int i = 0; i < n; i++) {
        // 移除队列中超出窗口范围的元素索引
        while (!dq.empty() && dq.front() <= i - k) {
            dq.pop_front();
        }
        
        // 维护队列的单调递减性质
        while (!dq.empty() && arr[dq.back()] <= arr[i]) {
            dq.pop_back();
        }
        
        // 将当前元素索引入队
        dq.push_back(i);
        
        // 当窗口大小达到k时，记录窗口最大值（队首元素）
        if (i >= k - 1) {
            max_result[i - k + 1] = arr[dq.front()];
        }
    }
}

// 单调递增队列求最小值
void getMin() {
    deque<int> dq;
    for (int i = 0; i < n; i++) {
        // 移除队列中超出窗口范围的元素索引
        while (!dq.empty() && dq.front() <= i - k) {
            dq.pop_front();
        }
        
        // 维护队列的单调递增性质
        while (!dq.empty() && arr[dq.back()] >= arr[i]) {
            dq.pop_back();
        }
        
        // 将当前元素索引入队
        dq.push_back(i);
        
        // 当窗口大小达到k时，记录窗口最小值（队首元素）
        if (i >= k - 1) {
            min_result[i - k + 1] = arr[dq.front()];
        }
    }
}

int main() {
    // 读取输入
    scanf("%d%d", &n, &k);
    for (int i = 0; i < n; i++) {
        scanf("%d", &arr[i]);
    }
    
    // 计算最大值和最小值
    getMax();
    getMin();
    
    // 输出最小值结果
    for (int i = 0; i < n - k + 1; i++) {
        printf("%d ", min_result[i]);
    }
    printf("\n");
    
    // 输出最大值结果
    for (int i = 0; i < n - k + 1; i++) {
        printf("%d ", max_result[i]);
    }
    printf("\n");
    
    return 0;
}