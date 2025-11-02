/**
 * 连接棒材的最低费用 - 贪心算法 + 最小堆解决方案（C++实现，LeetCode版本）
 * 
 * 题目描述：
 * 你有一些长度为正整数的棍子，这些长度以数组sticks的形式给出
 * sticks[i]是第i个木棍的长度
 * 你可以通过支付x+y的成本将任意两个长度为x和y的棍子连接成一个棍子
 * 你必须连接所有的棍子，直到剩下一个棍子
 * 返回以这种方式将所有给定的棍子连接成一个棍子的最小成本
 * 
 * 测试链接：https://leetcode.cn/problems/minimum-cost-to-connect-sticks/
 * 
 * 算法思想：
 * 贪心算法 + 最小堆（优先队列）
 * 1. 使用最小堆存储所有棍子的长度
 * 2. 每次从堆中取出两根最短的棍子进行连接
 * 3. 将连接后的新棍子放回堆中
 * 4. 重复直到只剩下一根棍子
 * 
 * 时间复杂度分析：
 * O(n*logn) - 每个棍子进出堆一次需要O(logn)，总共需要n-1次连接操作
 * 
 * 空间复杂度分析：
 * O(n) - 堆的大小为n
 * 
 * 是否为最优解：
 * 是，这是解决该问题的最优解（哈夫曼编码思想）
 * 
 * 工程化考量：
 * 1. 边界条件处理：处理空数组、单个棍子等特殊情况
 * 2. 输入验证：检查棍子长度是否为正整数
 * 3. 异常处理：对非法输入进行检查
 * 4. 可读性：使用清晰的变量命名和详细的注释
 * 
 * 贪心策略证明：
 * 这是经典的哈夫曼编码问题，每次选择最小的两个元素合并可以保证总成本最小
 * 这种策略满足贪心选择性质和最优子结构性质
 */

#include <iostream>
#include <vector>
#include <queue>
#include <stdexcept>
#include <algorithm>

using namespace std;

/**
 * 计算连接所有棍子的最小成本
 * 
 * @param sticks 棍子长度数组
 * @return 最小连接成本
 * 
 * 算法步骤：
 * 1. 将棍子长度加入最小堆
 * 2. 当堆中棍子数量大于1时：
 *    - 取出两根最短的棍子
 *    - 计算连接成本并累加
 *    - 将连接后的新棍子放回堆中
 * 3. 返回总成本
 */
int connectSticks(vector<int>& sticks) {
    // 输入验证
    if (sticks.empty()) {
        return 0;
    }
    
    // 单个棍子不需要连接
    if (sticks.size() == 1) {
        return 0;
    }
    
    // 最小堆，存储棍子长度
    priority_queue<int, vector<int>, greater<int>> heap;
    for (int stick : sticks) {
        // 验证棍子长度有效性
        if (stick <= 0) {
            throw invalid_argument("棍子长度必须为正整数");
        }
        heap.push(stick);
    }
    
    int totalCost = 0;  // 总连接成本
    
    // 当堆中还有多于一根棍子时继续连接
    while (heap.size() > 1) {
        // 取出两根最短的棍子
        int first = heap.top();
        heap.pop();
        int second = heap.top();
        heap.pop();
        
        // 计算连接成本
        int cost = first + second;
        totalCost += cost;
        
        // 将连接后的新棍子放回堆中
        heap.push(cost);
    }
    
    return totalCost;
}

/**
 * 调试版本：打印计算过程中的中间结果
 * 
 * @param sticks 棍子长度数组
 * @return 最小连接成本
 */
int debugConnectSticks(vector<int>& sticks) {
    if (sticks.empty()) {
        cout << "空数组，不需要连接" << endl;
        return 0;
    }
    
    if (sticks.size() == 1) {
        cout << "单个棍子，不需要连接" << endl;
        return 0;
    }
    
    cout << "原始棍子长度: ";
    for (int stick : sticks) {
        cout << stick << " ";
    }
    cout << endl;
    
    priority_queue<int, vector<int>, greater<int>> heap;
    for (int stick : sticks) {
        heap.push(stick);
    }
    
    int totalCost = 0;
    int step = 1;
    
    cout << "\n连接过程:" << endl;
    while (heap.size() > 1) {
        cout << "步骤 " << step << ":" << endl;
        
        // 取出两根最短的棍子
        int first = heap.top();
        heap.pop();
        int second = heap.top();
        heap.pop();
        cout << "  取出两根最短的棍子: " << first << " 和 " << second << endl;
        
        // 计算连接成本
        int cost = first + second;
        totalCost += cost;
        cout << "  连接成本: " << first << " + " << second << " = " << cost << endl;
        cout << "  累计总成本: " << totalCost << endl;
        
        // 将连接后的新棍子放回堆中
        heap.push(cost);
        cout << "  将新棍子(" << cost << ")放回堆中" << endl;
        
        // 打印当前堆的状态
        cout << "  当前堆中棍子: ";
        priority_queue<int, vector<int>, greater<int>> temp = heap;
        while (!temp.empty()) {
            cout << temp.top() << " ";
            temp.pop();
        }
        cout << endl;
        
        step++;
    }
    
    cout << "\n最终结果: 最小连接成本 = " << totalCost << endl;
    return totalCost;
}

/**
 * 打印数组的辅助函数
 * 
 * @param sticks 棍子长度数组
 */
void printSticks(const vector<int>& sticks) {
    cout << "[";
    for (int i = 0; i < sticks.size(); i++) {
        cout << sticks[i];
        if (i < sticks.size() - 1) cout << ",";
    }
    cout << "]";
}

/**
 * 测试函数：验证连接棒材算法的正确性
 */
void testConnectSticks() {
    cout << "连接棒材的最低费用算法测试开始" << endl;
    cout << "============================" << endl;
    
    // 测试用例1: [2,4,3]
    vector<int> sticks1 = {2, 4, 3};
    int result1 = connectSticks(sticks1);
    cout << "输入: ";
    printSticks(sticks1);
    cout << endl;
    cout << "输出: " << result1 << endl;
    cout << "预期: 14" << endl;
    cout << (result1 == 14 ? "✓ 通过" : "✗ 失败") << endl << endl;
    
    // 测试用例2: [1,8,3,5]
    vector<int> sticks2 = {1, 8, 3, 5};
    int result2 = connectSticks(sticks2);
    cout << "输入: ";
    printSticks(sticks2);
    cout << endl;
    cout << "输出: " << result2 << endl;
    cout << "预期: 30" << endl;
    cout << (result2 == 30 ? "✓ 通过" : "✗ 失败") << endl << endl;
    
    // 测试用例3: 空数组
    vector<int> sticks3 = {};
    int result3 = connectSticks(sticks3);
    cout << "输入: []" << endl;
    cout << "输出: " << result3 << endl;
    cout << "预期: 0" << endl;
    cout << (result3 == 0 ? "✓ 通过" : "✗ 失败") << endl << endl;
    
    // 测试用例4: 单个棍子
    vector<int> sticks4 = {5};
    int result4 = connectSticks(sticks4);
    cout << "输入: [5]" << endl;
    cout << "输出: " << result4 << endl;
    cout << "预期: 0" << endl;
    cout << (result4 == 0 ? "✓ 通过" : "✗ 失败") << endl << endl;
    
    cout << "测试结束" << endl;
}

/**
 * 性能测试：测试算法在大规模数据下的表现
 */
void performanceTest() {
    cout << "性能测试开始" << endl;
    cout << "============" << endl;
    
    // 生成大规模测试数据
    int n = 10000;
    vector<int> sticks;
    for (int i = 0; i < n; i++) {
        sticks.push_back(rand() % 1000 + 1);  // 1-1000的随机数
    }
    
    clock_t start = clock();
    int result = connectSticks(sticks);
    clock_t end = clock();
    
    double duration = double(end - start) / CLOCKS_PER_SEC * 1000; // 转换为毫秒
    
    cout << "数据规模: " << n << " 根棍子" << endl;
    cout << "执行时间: " << duration << " 毫秒" << endl;
    cout << "最小连接成本: " << result << endl;
    cout << "性能测试结束" << endl;
}

/**
 * 主函数：运行测试
 */
int main() {
    cout << "连接棒材的最低费用 - 贪心算法 + 最小堆解决方案（C++实现，LeetCode版本）" << endl;
    cout << "==================================================================" << endl;
    
    // 运行基础测试
    testConnectSticks();
    
    cout << endl << "调试模式示例:" << endl;
    vector<int> debugSticks = {2, 4, 3};
    cout << "对测试用例 [2,4,3] 进行调试跟踪:" << endl;
    int debugResult = debugConnectSticks(debugSticks);
    cout << "最终结果: " << debugResult << endl;
    
    cout << endl << "算法分析:" << endl;
    cout << "- 时间复杂度: O(n*logn) - 每个棍子进出堆一次需要O(logn)，总共需要n-1次连接操作" << endl;
    cout << "- 空间复杂度: O(n) - 堆的大小为n" << endl;
    cout << "- 贪心策略: 每次选择最短的两根棍子进行连接（哈夫曼编码思想）" << endl;
    cout << "- 最优性: 这种贪心策略能够得到全局最优解" << endl;
    cout << "- 数学原理: 这是经典的哈夫曼编码问题" << endl;
    
    // 可选：运行性能测试
    // cout << endl << "性能测试:" << endl;
    // performanceTest();
    
    return 0;
}