// LeetCode 50. Pow(x, n) (快速幂递归)
// 测试链接 : https://leetcode.cn/problems/powx-n/

#include <iostream>
using namespace std;

class LC50_Pow {
public:
    double myPow(double x, int n) {
        // 处理负指数
        long long N = n;
        if (N < 0) {
            x = 1 / x;
            N = -N;
        }
        
        return fastPow(x, N);
    }
    
private:
    double fastPow(double x, long long n) {
        // 基础情况
        if (n == 0) {
            return 1.0;
        }
        
        // 递归计算
        double half = fastPow(x, n / 2);
        
        if (n % 2 == 0) {
            return half * half;
        } else {
            return half * half * x;
        }
    }
};

// 测试函数
int main() {
    LC50_Pow solution;
    
    // 测试用例1
    double x1 = 2.00000;
    int n1 = 10;
    cout << "输入: x = " << x1 << ", n = " << n1 << endl;
    cout << "输出: " << solution.myPow(x1, n1) << endl;
    cout << "期望: 1024.00000" << endl << endl;
    
    // 测试用例2
    double x2 = 2.10000;
    int n2 = 3;
    cout << "输入: x = " << x2 << ", n = " << n2 << endl;
    cout << "输出: " << solution.myPow(x2, n2) << endl;
    cout << "期望: 9.26100" << endl << endl;
    
    // 测试用例3
    double x3 = 2.00000;
    int n3 = -2;
    cout << "输入: x = " << x3 << ", n = " << n3 << endl;
    cout << "输出: " << solution.myPow(x3, n3) << endl;
    cout << "期望: 0.25000" << endl << endl;
    
    return 0;
}