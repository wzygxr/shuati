// HackerRank Day 9: Recursion 3 (阶乘递归)
// 测试链接 : https://www.hackerrank.com/challenges/30-recursion/problem

#include <iostream>
using namespace std;

class HR_Day9_Recursion3 {
public:
    int factorial(int n) {
        // 基础情况
        if (n <= 1) {
            return 1;
        }
        
        // 递归情况
        return n * factorial(n - 1);
    }
};

// 测试函数
int main() {
    HR_Day9_Recursion3 solution;
    
    // 测试用例1
    int n1 = 3;
    cout << "输入: " << n1 << endl;
    cout << "输出: " << solution.factorial(n1) << endl;
    cout << "期望: 6" << endl << endl;
    
    // 测试用例2
    int n2 = 5;
    cout << "输入: " << n2 << endl;
    cout << "输出: " << solution.factorial(n2) << endl;
    cout << "期望: 120" << endl << endl;
    
    return 0;
}