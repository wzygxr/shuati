/**
 * 数组操作 (Array Manipulation)
 * 
 * 题目描述:
 * 给定一个长度为n的数组，初始时所有元素都为0。然后进行m次操作，每次操作给定三个整数a, b, k，
 * 表示将数组中从索引a到索引b（包含a和b）的所有元素都增加k。求执行完所有操作后数组中的最大值。
 * 
 * 示例:
 * 输入: n = 5, queries = [[1,2,100],[2,5,100],[3,4,100]]
 * 输出: 200
 * 解释: 
 * 初始数组: [0, 0, 0, 0, 0]
 * 操作1: [100, 100, 0, 0, 0]
 * 操作2: [100, 200, 100, 100, 100]
 * 操作3: [100, 200, 200, 200, 100]
 * 最大值: 200
 * 
 * 提示:
 * 3 <= n <= 10^7
 * 1 <= m <= 2*10^5
 * 1 <= a <= b <= n
 * 0 <= k <= 10^9
 * 
 * 题目链接: https://www.hackerrank.com/challenges/crush/problem
 * 
 * 解题思路:
 * 使用差分数组技巧结合前缀和来优化区间更新操作。
 * 1. 创建一个差分数组diff，大小为n+1
 * 2. 对于每个操作[a,b,k]，执行diff[a-1] += k和diff[b] -= k
 * 3. 对差分数组计算前缀和，得到最终数组
 * 4. 在计算前缀和的过程中记录最大值
 * 
 * 时间复杂度: O(n + m) - 需要遍历所有操作和数组一次
 * 空间复杂度: O(n) - 需要额外的差分数组空间
 * 
 * 工程化考量:
 * 1. 边界条件处理：n=0、空操作数组等特殊情况
 * 2. 索引转换：题目使用1-based索引，需要转换为0-based
 * 3. 整数溢出：使用long long避免大数溢出
 * 4. 性能优化：差分数组方法将O(n*m)优化到O(n+m)
 * 
 * 最优解分析:
 * 这是最优解，因为需要处理所有操作，而且数组大小可能很大，不能使用暴力方法。
 * 差分数组方法将时间复杂度从O(n*m)优化到O(n+m)。
 * 
 * 算法核心:
 * 差分数组的思想：对于区间[a,b]增加k，只需要在a位置+k，在b+1位置-k。
 * 然后通过前缀和还原整个数组，同时记录最大值。
 */

#include <vector>
#include <iostream>
#include <climits>
#include <cstdlib>
#include <ctime>
#include <chrono>

using namespace std;

class Solution {
public:
    /**
     * 计算数组操作后的最大值
     * 
     * @param n 数组长度
     * @param queries 操作数组，每个操作包含[起始索引, 结束索引, 增加值]
     * @return 操作后数组的最大值
     * 
     * 异常场景处理:
     * - n <= 0：返回0
     * - queries为空：返回0
     * - 索引越界：题目保证1 <= a <= b <= n
     * - 大数溢出：使用long long避免
     */
    long long arrayManipulation(int n, vector<vector<int>>& queries) {
        // 边界情况处理
        if (n <= 0 || queries.empty()) {
            return 0;
        }
        
        // 创建差分数组，大小为n+1以便处理边界情况
        vector<long long> diff(n + 1, 0);
        
        // 处理每个操作
        for (auto& query : queries) {
            int a = query[0];      // 起始索引（1-based）
            int b = query[1];      // 结束索引（1-based）
            int k = query[2];      // 增加值
            
            // 在差分数组中标记区间更新
            // 在a-1位置增加k（0-based索引）
            diff[a - 1] += k;
            // 在b位置减少k（如果b < n，避免数组越界）
            if (b < n) {
                diff[b] -= k;
            }
        }
        
        // 通过计算差分数组的前缀和得到最终数组，并记录最大值
        long long maxVal = LLONG_MIN;
        long long currentSum = 0;
        
        for (int i = 0; i < n; i++) {
            currentSum += diff[i];
            if (currentSum > maxVal) {
                maxVal = currentSum;
            }
        }
        
        return maxVal;
    }
};

/**
 * 测试函数
 */
void testArrayManipulation() {
    Solution solution;
    
    // 测试用例1：经典情况
    int n1 = 5;
    vector<vector<int>> queries1 = {{1, 2, 100}, {2, 5, 100}, {3, 4, 100}};
    long long result1 = solution.arrayManipulation(n1, queries1);
    cout << "测试用例1 n=5, queries=[[1,2,100],[2,5,100],[3,4,100]]: " << result1 << " (预期: 200)" << endl;
    
    // 测试用例2：复杂操作
    int n2 = 10;
    vector<vector<int>> queries2 = {{2, 6, 8}, {3, 5, 7}, {1, 8, 1}, {5, 9, 15}};
    long long result2 = solution.arrayManipulation(n2, queries2);
    cout << "测试用例2 n=10, queries=[[2,6,8],[3,5,7],[1,8,1],[5,9,15]]: " << result2 << " (预期: 31)" << endl;
    
    // 测试用例3：边界情况
    int n3 = 4;
    vector<vector<int>> queries3 = {{1, 2, 5}, {2, 4, 10}, {1, 3, 3}};
    long long result3 = solution.arrayManipulation(n3, queries3);
    cout << "测试用例3 n=4, queries=[[1,2,5],[2,4,10],[1,3,3]]: " << result3 << " (预期: 18)" << endl;
    
    // 测试用例4：n=1
    int n4 = 1;
    vector<vector<int>> queries4 = {{1, 1, 5}};
    long long result4 = solution.arrayManipulation(n4, queries4);
    cout << "测试用例4 n=1, queries=[[1,1,5]]: " << result4 << " (预期: 5)" << endl;
    
    // 测试用例5：空操作
    int n5 = 5;
    vector<vector<int>> queries5 = {};
    long long result5 = solution.arrayManipulation(n5, queries5);
    cout << "测试用例5 n=5, queries=[]: " << result5 << " (预期: 0)" << endl;
    
    // 测试用例6：n=0
    int n6 = 0;
    vector<vector<int>> queries6 = {{1, 1, 5}};
    long long result6 = solution.arrayManipulation(n6, queries6);
    cout << "测试用例6 n=0, queries=[[1,1,5]]: " << result6 << " (预期: 0)" << endl;
    
    // 测试用例7：大数测试
    int n7 = 3;
    vector<vector<int>> queries7 = {{1, 3, 1000000000}};
    long long result7 = solution.arrayManipulation(n7, queries7);
    cout << "测试用例7 n=3, queries=[[1,3,1000000000]]: " << result7 << " (预期: 1000000000)" << endl;
}

/**
 * 性能测试函数
 */
void performanceTest() {
    cout << "\n=== 性能测试 ===" << endl;
    Solution solution;
    int n = 100000; // 10万元素（减少规模避免超时）
    int m = 20000;   // 2万次操作
    
    // 生成随机操作
    vector<vector<int>> queries(m, vector<int>(3));
    srand(time(0));
    
    for (int i = 0; i < m; i++) {
        int a = rand() % n + 1; // 1-based索引
        int b = a + rand() % (n - a + 1); // b >= a
        int k = rand() % 1000000000; // k在0到10^9之间
        
        queries[i][0] = a;
        queries[i][1] = b;
        queries[i][2] = k;
    }
    
    auto startTime = chrono::high_resolution_clock::now();
    long long result = solution.arrayManipulation(n, queries);
    auto endTime = chrono::high_resolution_clock::now();
    
    auto duration = chrono::duration_cast<chrono::milliseconds>(endTime - startTime);
    cout << "处理 n=" << n << ", m=" << m << " 耗时: " << duration.count() << "ms" << endl;
    cout << "最大值: " << result << endl;
}

/**
 * 主函数
 */
int main() {
    cout << "=== 数组操作测试 ===" << endl;
    testArrayManipulation();
    
    // 运行性能测试（可选）
    // performanceTest();
    
    cout << "\n=== 测试完成 ===" << endl;
    return 0;
}