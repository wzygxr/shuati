#include <iostream>
#include <vector>
#include <queue>
#include <unordered_set>
using namespace std;

/**
 * 相关题目3: LeetCode 264. 丑数 II
 * 题目链接: https://leetcode.cn/problems/ugly-number-ii/
 * 题目描述: 给你一个整数 n ，请你找出并返回第 n 个 丑数 。
 * 丑数 就是只包含质因数 2、3 和 5 的正整数。
 * 解题思路: 使用最小堆维护候选丑数，确保每次取出的是当前最小的丑数
 * 时间复杂度: O(n log n)，每次堆操作需要O(log n)时间
 * 空间复杂度: O(n)，堆和集合都需要O(n)空间
 * 是否最优解: 是，另一种更优的动态规划解法可以达到O(n)时间复杂度，但堆解法更直观
 * 
 * 本题属于堆的典型应用场景：需要频繁获取最小值并生成新的候选值
 */
class Solution {
public:
    /**
     * 查找第n个丑数
     * @param n 需要查找的丑数的位置（从1开始计数）
     * @return 第n个丑数
     * @throws invalid_argument 当输入参数无效时抛出异常
     */
    int nthUglyNumber(int n) {
        // 异常处理：检查n是否为正整数
        if (n <= 0) {
            throw invalid_argument("n必须是正整数");
        }
        
        // 特殊情况处理
        if (n == 1) {
            return 1; // 第一个丑数是1
        }
        
        // 定义质因数
        vector<int> factors = {2, 3, 5};
        
        // 使用最小堆维护候选丑数
        priority_queue<long, vector<long>, greater<long>> minHeap;
        // 使用无序集合去重
        unordered_set<long> seen;
        
        // 初始化堆和集合，第一个丑数是1
        minHeap.push(1L);
        seen.insert(1L);
        
        long ugly = 0;
        // 执行n次操作，每次取出最小的丑数并生成新的候选丑数
        for (int i = 0; i < n; i++) {
            // 取出当前最小的丑数
            ugly = minHeap.top();
            minHeap.pop();
            
            // 调试信息：打印当前处理的丑数
            // cout << "当前丑数: " << ugly << "，是第" << (i + 1) << "个丑数" << endl;
            
            // 生成新的候选丑数：将当前丑数分别乘以2、3、5
            for (int factor : factors) {
                long nextUgly = ugly * factor;
                // 如果新生成的数没有出现过，则加入堆和集合
                if (seen.find(nextUgly) == seen.end()) {
                    seen.insert(nextUgly);
                    minHeap.push(nextUgly);
                    // 调试信息：打印新加入的候选丑数
                    // cout << "新加入候选丑数: " << nextUgly << endl;
                }
            }
        }
        
        // 第n次取出的就是第n个丑数
        return static_cast<int>(ugly);
    }
};

/**
 * 测试函数，验证算法在不同输入情况下的正确性
 */
int main() {
    Solution solution;
    
    // 测试用例1：基本情况
    int n1 = 10;
    cout << "示例1输出: " << solution.nthUglyNumber(n1) << endl; // 期望输出: 12
    
    // 测试用例2：边界情况 - n=1
    int n2 = 1;
    cout << "示例2输出: " << solution.nthUglyNumber(n2) << endl; // 期望输出: 1
    
    // 测试用例3：较大的n
    int n3 = 1500;
    cout << "示例3输出: " << solution.nthUglyNumber(n3) << endl; // 期望输出: 859963392
    
    // 测试用例4：中等大小的n
    int n4 = 1690;
    cout << "示例4输出: " << solution.nthUglyNumber(n4) << endl; // 期望输出: 2123366400
    
    // 测试异常情况
    try {
        solution.nthUglyNumber(0);
        cout << "异常测试失败：未抛出预期的异常" << endl;
    } catch (const invalid_argument& e) {
        cout << "异常测试通过: " << e.what() << endl;
    }
    
    try {
        solution.nthUglyNumber(-5);
        cout << "异常测试失败：未抛出预期的异常" << endl;
    } catch (const invalid_argument& e) {
        cout << "异常测试通过: " << e.what() << endl;
    }
    
    return 0;
}