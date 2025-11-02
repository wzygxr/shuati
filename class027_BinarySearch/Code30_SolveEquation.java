package class051;

/**
 * 杭电OJ 2199 - Can you solve this equation?
 * 问题描述：求解方程f(x)=0在区间内的解
 * 解法：二分答案（二分查找根）
 * 时间复杂度：O(log((max-min)/epsilon))
 * 空间复杂度：O(1)
 * 链接：http://acm.hdu.edu.cn/showproblem.php?pid=2199
 * 
 * 解题思路：
 * 1. 这是一个数值求解问题，使用二分法在区间内寻找方程的根
 * 2. 方程在区间内单调递增或递减，确保有唯一解
 * 3. 使用二分法不断缩小解的范围，直到达到精度要求
 */
public class Code30_SolveEquation {
    
    /**
     * 求解方程 8*x^4 + 7*x^3 + 2*x^2 + 3*x + 6 = Y 在[0,100]内的解
     * @param Y 方程右边的值
     * @return 方程的解，保留4位小数
     */
    public double solveEquation(double Y) {
        // 定义方程函数
        double left = 0.0;
        double right = 100.0;
        double epsilon = 1e-7; // 精度要求
        
        // 检查边界条件
        double fLeft = f(left) - Y;
        double fRight = f(right) - Y;
        
        // 如果方程在端点处值为0，直接返回
        if (Math.abs(fLeft) < epsilon) {
            return left;
        }
        if (Math.abs(fRight) < epsilon) {
            return right;
        }
        
        // 如果端点值同号，说明无解
        if (fLeft * fRight > 0) {
            return -1; // 表示无解
        }
        
        // 二分搜索解
        while (right - left > epsilon) {
            double mid = left + (right - left) / 2;
            double fMid = f(mid) - Y;
            
            if (Math.abs(fMid) < epsilon) {
                return mid;
            }
            
            if (fLeft * fMid < 0) {
                // 根在左半区间
                right = mid;
                fRight = fMid;
            } else {
                // 根在右半区间
                left = mid;
                fLeft = fMid;
            }
        }
        
        return (left + right) / 2;
    }
    
    /**
     * 方程函数：f(x) = 8*x^4 + 7*x^3 + 2*x^2 + 3*x + 6
     * @param x 自变量
     * @return 函数值
     */
    private double f(double x) {
        return 8 * Math.pow(x, 4) + 7 * Math.pow(x, 3) + 2 * Math.pow(x, 2) + 3 * x + 6;
    }
    
    /*
     * 复杂度分析：
     * 时间复杂度：O(log((max-min)/epsilon))
     *   - 二分搜索区间长度从100减少到epsilon，二分次数为O(log(100/epsilon))
     *   - 每次二分需要计算一次函数值，时间复杂度为O(1)
     *   - 总时间复杂度为O(log(1/epsilon))
     * 
     * 空间复杂度：O(1)
     *   - 只使用了常数个额外变量
     * 
     * 工程化考量：
     * 1. 精度控制：设置合适的epsilon值
     * 2. 边界检查：检查端点值是否满足方程
     * 3. 无解处理：当端点值同号时返回无解
     * 
     * 测试用例：
     * - 输入：Y = 100
     * - 输出：约1.6152（方程在[0,100]内的解）
     */
}

/**
 * C++ 实现
 */
/*
#include <iostream>
#include <cmath>
#include <iomanip>
using namespace std;

class Solution {
public:
    double solveEquation(double Y) {
        double left = 0.0;
        double right = 100.0;
        double epsilon = 1e-7;
        
        double fLeft = f(left) - Y;
        double fRight = f(right) - Y;
        
        if (abs(fLeft) < epsilon) return left;
        if (abs(fRight) < epsilon) return right;
        if (fLeft * fRight > 0) return -1;
        
        while (right - left > epsilon) {
            double mid = left + (right - left) / 2;
            double fMid = f(mid) - Y;
            
            if (abs(fMid) < epsilon) return mid;
            
            if (fLeft * fMid < 0) {
                right = mid;
                fRight = fMid;
            } else {
                left = mid;
                fLeft = fMid;
            }
        }
        
        return (left + right) / 2;
    }
    
private:
    double f(double x) {
        return 8 * pow(x, 4) + 7 * pow(x, 3) + 2 * pow(x, 2) + 3 * x + 6;
    }
};
*/

/**
 * Python 实现
 */
/*
import math

class Solution:
    def solve_equation(self, Y: float) -> float:
        left = 0.0
        right = 100.0
        epsilon = 1e-7
        
        f_left = self.f(left) - Y
        f_right = self.f(right) - Y
        
        if abs(f_left) < epsilon:
            return left
        if abs(f_right) < epsilon:
            return right
        if f_left * f_right > 0:
            return -1
            
        while right - left > epsilon:
            mid = left + (right - left) / 2
            f_mid = self.f(mid) - Y
            
            if abs(f_mid) < epsilon:
                return mid
                
            if f_left * f_mid < 0:
                right = mid
                f_right = f_mid
            else:
                left = mid
                f_left = f_mid
                
        return (left + right) / 2
    
    def f(self, x: float) -> float:
        return 8 * x**4 + 7 * x**3 + 2 * x**2 + 3 * x + 6
*/