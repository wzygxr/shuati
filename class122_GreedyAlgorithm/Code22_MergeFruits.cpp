#include <iostream>
#include <vector>
#include <queue>

// 合并果子
// 在一个果园里，小明已经将所有的果子打了下来，而且按果子的不同种类分成了不同的堆。
// 小明决定把所有的果子合成一堆。每一次合并，小明可以把两堆果子合并到一起，消耗的体力等于两堆果子的重量之和。
// 假设每个果子重量都为1，并且已知果子的种类数和每种果子的数目，你的任务是设计出合并的次序方案，使小明耗费的体力最少，并输出这个最小的体力耗费值。
// 测试链接 : https://www.luogu.com.cn/problem/P1090
using namespace std;

class Solution {
public:
    /**
     * 合并果子问题（霍夫曼编码的应用）
     * 
     * 算法思路：
     * 使用贪心策略结合优先队列（最小堆）：
     * 1. 每次选择当前最小的两堆果子进行合并
     * 2. 将合并后的新堆重新加入队列
     * 3. 重复上述过程直到只剩下一堆果子
     * 4. 每次合并的代价累加到总代价中
     * 
     * 正确性分析：
     * 1. 根据霍夫曼编码的最优性，每次合并最小的两堆可以得到最小的总代价
     * 2. 可以通过数学归纳法证明该策略的最优性
     * 
     * 时间复杂度：O(n*logn)
     * - 构建优先队列的时间为O(n)
     * - 每次从队列取出两个元素并插入一个元素的时间为O(logn)
     * - 总共需要进行n-1次合并操作
     * - 因此总时间复杂度为O(n*logn)
     * 
     * 空间复杂度：O(n) - 需要一个优先队列来存储所有堆的大小
     * 
     * @param arr 每种果子的数目数组
     * @return 最小的体力耗费值
     */
    int mergeFruits(vector<int>& arr) {
        // 边界检查
        if (arr.empty()) {
            return 0;
        }
        if (arr.size() == 1) {
            return 0; // 只有一堆不需要合并
        }
        
        // 创建最小堆（优先队列默认是最大堆，所以需要使用greater<int>来创建最小堆）
        priority_queue<int, vector<int>, greater<int>> minHeap;
        
        // 将所有堆的大小加入最小堆
        for (int num : arr) {
            minHeap.push(num);
        }
        
        int totalCost = 0; // 总代价
        
        // 当堆中元素数量大于1时，继续合并
        while (minHeap.size() > 1) {
            // 取出两个最小的堆
            int first = minHeap.top();
            minHeap.pop();
            int second = minHeap.top();
            minHeap.pop();
            
            // 计算合并代价
            int cost = first + second;
            totalCost += cost;
            
            // 将合并后的新堆加入队列
            minHeap.push(cost);
        }
        
        return totalCost;
    }
};

// 打印数组辅助函数
void printArray(const vector<int>& arr) {
    for (int num : arr) {
        cout << num << " ";
    }
    cout << endl;
}

// 测试函数
void test() {
    Solution solution;
    
    // 测试用例1: arr = [3, 2, 1, 5, 4] -> 输出: 33
    // 合并过程：
    // 1+2=3 (总代价3)
    // 3+3=6 (总代价9)
    // 4+5=9 (总代价18)
    // 6+9=15 (总代价33)
    vector<int> arr1 = {3, 2, 1, 5, 4};
    cout << "测试用例1:" << endl;
    cout << "果子数目: ";
    printArray(arr1);
    cout << "最小体力耗费: " << solution.mergeFruits(arr1) << endl; // 期望输出: 33
    
    // 测试用例2: arr = [1, 1, 1, 1] -> 输出: 8
    // 合并过程：
    // 1+1=2 (总代价2)
    // 1+1=2 (总代价4)
    // 2+2=4 (总代价8)
    vector<int> arr2 = {1, 1, 1, 1};
    cout << "\n测试用例2:" << endl;
    cout << "果子数目: ";
    printArray(arr2);
    cout << "最小体力耗费: " << solution.mergeFruits(arr2) << endl; // 期望输出: 8
    
    // 测试用例3: arr = [5] -> 输出: 0
    vector<int> arr3 = {5};
    cout << "\n测试用例3:" << endl;
    cout << "果子数目: ";
    printArray(arr3);
    cout << "最小体力耗费: " << solution.mergeFruits(arr3) << endl; // 期望输出: 0
    
    // 测试用例4: arr = [2, 3] -> 输出: 5
    vector<int> arr4 = {2, 3};
    cout << "\n测试用例4:" << endl;
    cout << "果子数目: ";
    printArray(arr4);
    cout << "最小体力耗费: " << solution.mergeFruits(arr4) << endl; // 期望输出: 5
}

int main() {
    test();
    return 0;
}