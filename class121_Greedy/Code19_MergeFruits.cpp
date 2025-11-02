#include <iostream>
#include <vector>
#include <queue>
using namespace std;

// 合并果子
// 题目描述：在一个果园里，多多已经将所有的果子打了下来，而且按果子的不同种类分成了不同的堆。
// 多多决定把所有的果子合成一堆。每一次合并，多多可以把两堆果子合并到一起，消耗的体力等于两堆果子的重量之和。
// 可以看出，所有的果子经过n-1次合并之后，就只剩下一堆了。多多在合并果子时总共消耗的体力等于每次合并所消耗体力之和。
// 多多想尽可能节省体力，让你计算出最小的体力消耗值。
// 测试链接: https://www.luogu.com.cn/problem/P1090

class Code19_MergeFruits {
public:
    /**
     * 合并果子问题的贪心解法
     * 
     * 解题思路：
     * 1. 使用最小堆（优先队列）来维护所有堆的重量
     * 2. 每次从堆顶取出两个最小的元素，合并后将结果放回堆中
     * 3. 重复步骤2，直到堆中只剩下一个元素
     * 4. 每次合并的代价累加到总代价中
     * 
     * 贪心策略的正确性：
     * 每次选择最小的两堆进行合并，这样可以确保后续合并的代价尽可能小。
     * 这类似于哈夫曼编码的思想，通过局部最优选择达到全局最优。
     * 
     * 时间复杂度：O(n log n)，其中n是果子的堆数
     * - 构建最小堆需要O(n)时间
     * - 每次堆操作（取出最小值、插入新值）需要O(log n)时间
     * - 总共需要n-1次合并操作，每次合并有两次取出和一次插入
     * - 总体时间复杂度为O(n + (n-1) * log n) = O(n log n)
     * 
     * 空间复杂度：O(n)，用于存储最小堆
     * 
     * @param weights 各堆果子的重量数组
     * @return 最小的体力消耗值
     */
    static int minCost(vector<int>& weights) {
        // 边界条件处理
        if (weights.empty() || weights.size() <= 1) {
            return 0; // 如果没有果子或只有一堆果子，不需要合并，代价为0
        }

        // 创建最小堆，并将所有果子的重量加入堆中
        // 在C++中，priority_queue默认是最大堆，需要使用greater<int>来创建最小堆
        priority_queue<int, vector<int>, greater<int>> minHeap;
        for (int weight : weights) {
            minHeap.push(weight);
        }

        int totalCost = 0; // 总体力消耗

        // 当堆中元素超过1个时，继续合并
        while (minHeap.size() > 1) {
            // 取出两个最小的元素
            int first = minHeap.top();
            minHeap.pop();
            int second = minHeap.top();
            minHeap.pop();

            // 合并这两堆，计算消耗的体力
            int cost = first + second;
            totalCost += cost;

            // 将合并后的堆放回堆中
            minHeap.push(cost);
        }

        return totalCost;
    }
};

// 测试方法
int main() {
    // 测试用例1
    // 输入: [3, 4, 5, 6]
    // 输出: 36
    // 解释: 合并顺序可以是 3+4=7，5+6=11，7+11=18，总消耗 7+11+18=36
    vector<int> weights1 = {3, 4, 5, 6};
    cout << "测试用例1结果: " << Code19_MergeFruits::minCost(weights1) << endl; // 期望输出: 36

    // 测试用例2
    // 输入: [1, 2, 3, 4, 5]
    // 输出: 33
    // 合并过程: 1+2=3(+3), 3+3=6(+6), 4+5=9(+9), 6+9=15(+15)，总3+6+9+15=33
    vector<int> weights2 = {1, 2, 3, 4, 5};
    cout << "测试用例2结果: " << Code19_MergeFruits::minCost(weights2) << endl; // 期望输出: 33

    // 测试用例3：边界情况 - 只有一堆果子
    // 输入: [5]
    // 输出: 0
    vector<int> weights3 = {5};
    cout << "测试用例3结果: " << Code19_MergeFruits::minCost(weights3) << endl; // 期望输出: 0

    // 测试用例4：边界情况 - 空数组
    // 输入: []
    // 输出: 0
    vector<int> weights4 = {};
    cout << "测试用例4结果: " << Code19_MergeFruits::minCost(weights4) << endl; // 期望输出: 0

    // 测试用例5：较大数据
    // 输入: [1, 1, 1, 1, 1]
    // 输出: 13
    // 合并过程: 1+1=2(+2), 1+1=2(+2), 2+2=4(+4), 4+1=5(+5)，总2+2+4+5=13
    vector<int> weights5 = {1, 1, 1, 1, 1};
    cout << "测试用例5结果: " << Code19_MergeFruits::minCost(weights5) << endl; // 期望输出: 13

    return 0;
}