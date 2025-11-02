/*
题目：线性丢番图方程
来源：综合题目
内容：求解形如 ax + by = c 的线性丢番图方程

算法：扩展欧几里得算法
时间复杂度：O(log min(a, b))
空间复杂度：O(1)

思路：
1. 使用扩展欧几里得算法求解 ax + by = gcd(a,b)
2. 如果c不能被gcd(a,b)整除，则方程无解
3. 否则，将解乘以c/gcd(a,b)得到特解
4. 通解为：x = x0 + k*(b/g), y = y0 - k*(a/g)

工程化考量：
- 异常处理：处理除零、溢出等情况
- 边界条件：a=0或b=0的特殊情况
- 性能优化：使用迭代版本避免递归深度限制
*/

#include <iostream>
#include <vector>
#include <stdexcept>
#include <cmath>

using namespace std;

class DiophantineEquation {
public:
    /**
     * 扩展欧几里得算法（迭代版本）
     * 求解 ax + by = gcd(a,b) 的一组整数解 x, y
     */
    static long long extended_gcd(long long a, long long b, long long& x, long long& y) {
        if (a == 0 && b == 0) {
            throw invalid_argument("a和b不能同时为0");
        }
        
        if (b == 0) {
            x = 1;
            y = 0;
            return a;
        }
        
        long long x0 = 1, x1 = 0;
        long long y0 = 0, y1 = 1;
        long long r0 = a, r1 = b;
        
        while (r1 != 0) {
            long long q = r0 / r1;
            
            long long x_temp = x0 - q * x1;
            long long y_temp = y0 - q * y1;
            long long r_temp = r0 - q * r1;
            
            x0 = x1; x1 = x_temp;
            y0 = y1; y1 = y_temp;
            r0 = r1; r1 = r_temp;
        }
        
        x = x0;
        y = y0;
        return r0;
    }
    
    /**
     * 求解线性丢番图方程
     * 求解 ax + by = c 的一组整数解
     * 
     * @param a 系数a
     * @param b 系数b
     * @param c 等式右边
     * @param x 解x的引用
     * @param y 解y的引用
     * @return 是否有解
     */
    static bool solve_diophantine(long long a, long long b, long long c, long long& x, long long& y) {
        // 特殊情况处理
        if (a == 0 && b == 0) {
            return c == 0; // 0x + 0y = c 有解当且仅当c=0
        }
        
        if (a == 0) {
            if (c % b == 0) {
                x = 0;
                y = c / b;
                return true;
            }
            return false;
        }
        
        if (b == 0) {
            if (c % a == 0) {
                x = c / a;
                y = 0;
                return true;
            }
            return false;
        }
        
        // 使用扩展欧几里得算法
        long long g = extended_gcd(a, b, x, y);
        
        if (c % g != 0) {
            return false; // 无解
        }
        
        // 将解乘以c/g得到特解
        long long k = c / g;
        x *= k;
        y *= k;
        
        return true;
    }
    
    /**
     * 求线性丢番图方程的所有正整数解
     * 
     * @param a 系数a
     * @param b 系数b
     * @param c 等式右边
     * @param solutions 存储所有正整数解的向量
     * @return 正整数解的个数
     */
    static int find_positive_solutions(long long a, long long b, long long c, vector<pair<long long, long long>>& solutions) {
        solutions.clear();
        
        long long x, y;
        if (!solve_diophantine(a, b, c, x, y)) {
            return 0; // 无解
        }
        
        long long g = extended_gcd(a, b, x, y);
        long long a1 = a / g;
        long long b1 = b / g;
        
        // 通解：x = x0 + k*b1, y = y0 - k*a1
        // 需要满足 x > 0 且 y > 0
        
        // 计算k的范围
        // x > 0 => x0 + k*b1 > 0 => k > -x0/b1
        // y > 0 => y0 - k*a1 > 0 => k < y0/a1
        
        double k_min = -static_cast<double>(x) / b1;
        double k_max = static_cast<double>(y) / a1;
        
        long long k_start = ceil(k_min + 1e-9); // 向上取整
        long long k_end = floor(k_max - 1e-9);   // 向下取整
        
        if (k_start > k_end) {
            return 0; // 无正整数解
        }
        
        // 生成所有正整数解
        for (long long k = k_start; k <= k_end; k++) {
            long long x_sol = x + k * b1;
            long long y_sol = y - k * a1;
            
            if (x_sol > 0 && y_sol > 0) {
                solutions.push_back({x_sol, y_sol});
            }
        }
        
        return solutions.size();
    }
    
    /**
     * 判断线性丢番图方程是否有解
     * 根据裴蜀定理：ax + by = c 有整数解当且仅当 gcd(a,b) 整除 c
     */
    static bool has_solution(long long a, long long b, long long c) {
        if (a == 0 && b == 0) {
            return c == 0;
        }
        
        if (a == 0) {
            return c % b == 0;
        }
        
        if (b == 0) {
            return c % a == 0;
        }
        
        long long g = gcd(a, b);
        return c % g == 0;
    }
    
    /**
     * 欧几里得算法求最大公约数
     */
    static long long gcd(long long a, long long b) {
        return b == 0 ? a : gcd(b, a % b);
    }
    
    /**
     * 测试函数
     */
    static void run_tests() {
        cout << "=== 线性丢番图方程测试 ===" << endl;
        
        // 测试1：基本求解
        cout << "\n1. 基本求解测试:" << endl;
        long long a1 = 6, b1 = 9, c1 = 15;
        long long x1, y1;
        bool has_sol1 = solve_diophantine(a1, b1, c1, x1, y1);
        
        if (has_sol1) {
            cout << "方程 " << a1 << "x + " << b1 << "y = " << c1 << " 有解:" << endl;
            cout << "特解: x = " << x1 << ", y = " << y1 << endl;
            cout << "验证: " << a1 << "*" << x1 << " + " << b1 << "*" << y1 << " = " << a1*x1 + b1*y1 << endl;
        } else {
            cout << "方程 " << a1 << "x + " << b1 << "y = " << c1 << " 无解" << endl;
        }
        
        // 测试2：无解情况
        cout << "\n2. 无解情况测试:" << endl;
        long long a2 = 4, b2 = 6, c2 = 9;
        long long x2, y2;
        bool has_sol2 = solve_diophantine(a2, b2, c2, x2, y2);
        
        if (has_sol2) {
            cout << "方程 " << a2 << "x + " << b2 << "y = " << c2 << " 有解" << endl;
        } else {
            cout << "方程 " << a2 << "x + " << b2 << "y = " << c2 << " 无解" << endl;
            cout << "验证: gcd(" << a2 << ", " << b2 << ") = " << gcd(a2, b2) << ", " << c2 << " % gcd = " << c2 % gcd(a2, b2) << endl;
        }
        
        // 测试3：正整数解
        cout << "\n3. 正整数解测试:" << endl;
        long long a3 = 3, b3 = 5, c3 = 20;
        vector<pair<long long, long long>> solutions;
        int count = find_positive_solutions(a3, b3, c3, solutions);
        
        cout << "方程 " << a3 << "x + " << b3 << "y = " << c3 << " 的正整数解个数: " << count << endl;
        if (count > 0) {
            cout << "正整数解:" << endl;
            for (const auto& sol : solutions) {
                cout << "  x = " << sol.first << ", y = " << sol.second << endl;
            }
        }
        
        // 测试4：裴蜀定理验证
        cout << "\n4. 裴蜀定理验证:" << endl;
        cout << "方程 3x + 5y = 1 是否有解: " << (has_solution(3, 5, 1) ? "是" : "否") << endl;
        cout << "方程 4x + 6y = 1 是否有解: " << (has_solution(4, 6, 1) ? "是" : "否") << endl;
        cout << "方程 6x + 9y = 3 是否有解: " << (has_solution(6, 9, 3) ? "是" : "否") << endl;
        
        cout << "\n=== 测试完成 ===" << endl;
    }
};

int main() {
    try {
        DiophantineEquation::run_tests();
    } catch (const exception& e) {
        cerr << "程序异常: " << e.what() << endl;
        return 1;
    }
    
    return 0;
}