// LeetCode 1167. Minimum Cost to Connect Sticks
// 题目链接: https://leetcode.cn/problems/minimum-cost-to-connect-sticks/
// 
// 题目描述:
# 给你n个长度不同的木棍，每次你可以把两个木棍接在一起，花费是这两个木棍的长度之和。
# 你需要把所有木棍接成一个木棍，求最小的总花费。
//
// 解题思路:
# 这是一个典型的哈夫曼编码问题，可以使用最小堆（优先队列）来解决：
# 1. 将所有木棍长度放入最小堆
# 2. 每次从堆中取出两个最小的木棍，合并它们，将合并后的木棍放回堆中
# 3. 重复步骤2，直到堆中只剩下一个木棍
# 4. 每次合并的花费累加，最后得到总花费
//
// 这个问题可以看作是构建一个最小生成树的特殊情况，其中每个节点是一个木棍，边权是合并两个木棍的花费
//
// 时间复杂度: O(n log n)，其中n是木棍的数量，主要是堆操作的时间复杂度
// 空间复杂度: O(n)
// 是否为最优解: 是，使用最小堆的贪心算法是解决此类问题的最优方法

#include <iostream>
#include <vector>
#include <queue>
using namespace std;

int connectSticks(vector<int>& sticks) {
    if (sticks.size() <= 1) {
        return 0;  // 如果没有木棍或只有一个木棍，不需要合并，花费为0
    }
    
    // 构建最小堆
    priority_queue<int, vector<int>, greater<int>> minHeap(sticks.begin(), sticks.end());
    
    int totalCost = 0;
    
    // 当堆中不止一个木棍时，继续合并
    while (minHeap.size() > 1) {
        // 取出两个最小的木棍
        int first = minHeap.top();
        minHeap.pop();
        int second = minHeap.top();
        minHeap.pop();
        
        // 合并这两个木棍
        int merged = first + second;
        totalCost += merged;
        
        // 将合并后的木棍放回堆中
        minHeap.push(merged);
    }
    
    return totalCost;
}

// 测试函数
void test() {
    // 测试用例1
    vector<int> sticks1 = {2, 4, 3};
    cout << "Test 1: " << connectSticks(sticks1) << endl;  // 预期输出: 14
    // 解释: 先合并2和3得到5(花费5)，再合并5和4得到9(花费9)，总花费5+9=14
    
    // 测试用例2
    vector<int> sticks2 = {1, 8, 3, 5};
    cout << "Test 2: " << connectSticks(sticks2) << endl;  // 预期输出: 30
    // 解释: 合并1和3(花费4)，合并4和5(花费9)，合并9和8(花费17)，总花费4+9+17=30
    
    // 测试用例3
    vector<int> sticks3 = {5};
    cout << "Test 3: " << connectSticks(sticks3) << endl;  // 预期输出: 0
    // 解释: 只有一个木棍，不需要合并
    
    // 测试用例4
    vector<int> sticks4 = {};
    cout << "Test 4: " << connectSticks(sticks4) << endl;  // 预期输出: 0
    // 解释: 没有木棍，花费为0
}

int main() {
    test();
    return 0;
}