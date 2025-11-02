// LeetCode 70. Climbing Stairs (爬楼梯递归)
// 测试链接 : https://leetcode.cn/problems/climbing-stairs/

#include <iostream>
#include <unordered_map>
using namespace std;

class LC70_ClimbingStairs {
public:
    int climbStairs(int n) {
        // 使用记忆化递归
        unordered_map<int, int> memo;
        return climbStairsHelper(n, memo);
    }
    
private:
    int climbStairsHelper(int n, unordered_map<int, int>& memo) {
        // 基础情况
        if (n <= 2) {
            return n;
        }
        
        // 如果已经计算过，直接返回
        if (memo.find(n) != memo.end()) {
            return memo[n];
        }
        
        // 递归计算并存储结果
        memo[n] = climbStairsHelper(n - 1, memo) + climbStairsHelper(n - 2, memo);
        return memo[n];
    }
};

// 测试函数
int main() {
    LC70_ClimbingStairs solution;
    
    // 测试用例1
    int n1 = 2;
    cout << "输入: " << n1 << endl;
    cout << "输出: " << solution.climbStairs(n1) << endl;
    cout << "期望: 2" << endl << endl;
    
    // 测试用例2
    int n2 = 3;
    cout << "输入: " << n2 << endl;
    cout << "输出: " << solution.climbStairs(n2) << endl;
    cout << "期望: 3" << endl << endl;
    
    return 0;
}