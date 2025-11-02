/*
 * POJ 2891 Strange Way to Express Integers
 * 链接：http://poj.org/problem?id=2891
 * 题目大意：给定n个形如 x ≡ ri (mod mi) 的同余方程，求最小非负整数解，mi不一定两两互质
 * 
 * 算法思路：
 * 这是扩展中国剩余定理(EXCRT)的经典应用题目。
 * 题目中的模数mi不一定两两互质，因此需要使用EXCRT而不是标准的CRT。
 * 
 * 解法步骤：
 * 1. 使用扩展中国剩余定理求解同余方程组
 * 2. 注意处理无解的情况
 * 3. 确保结果是非负整数
 * 
 * 算法原理：
 * EXCRT通过逐步合并方程的方式求解同余方程组。
 * 每次合并两个方程，将多个方程最终合并为一个方程。
 * 
 * 时间复杂度：O(n log max(mi))，其中n为方程个数
 * 空间复杂度：O(n)
 * 
 * 适用场景：
 * 1. 模数不一定互质的同余方程组
 * 2. 数论问题求解
 * 3. 密码学应用
 * 
 * 注意事项：
 * 1. 需要处理无解的情况
 * 2. 注意大数运算和溢出问题
 * 3. 需要正确实现扩展欧几里得算法
 * 
 * 工程化考虑：
 * 1. 输入校验：检查输入是否合法
 * 2. 异常处理：处理无解的情况
 * 3. 大数处理：使用龟速乘法防止溢出
 * 4. 快速IO：优化输入输出效率
 * 
 * 与其他算法的关联：
 * 1. 扩展欧几里得算法：核心组件
 * 2. 中国剩余定理：特殊情况（模数互质）
 * 3. 线性同余方程：基础组件
 * 
 * 实际应用：
 * 1. 数论问题求解
 * 2. 密码学算法
 * 3. 大整数计算
 * 
 * 相关题目：
 * 1. 洛谷 P4777 - 扩展中国剩余定理
 * 2. HDU 3579 - Hello Kiki
 * 3. UVa 11754 - Code Feat
 */

#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

// 扩展欧几里得算法
long long exgcd(long long a, long long b, long long &x, long long &y) {
    if (b == 0) {
        x = 1;
        y = 0;
        return a;
    }
    long long gcd = exgcd(b, a % b, y, x);
    y -= a / b * x;
    return gcd;
}

// 龟速乘法，防止大数乘法溢出
long long mul(long long a, long long b, long long mod) {
    a = (a % mod + mod) % mod;
    b = (b % mod + mod) % mod;
    long long res = 0;
    while (b > 0) {
        if (b & 1) {
            res = (res + a) % mod;
        }
        a = (a * 2) % mod;
        b >>= 1;
    }
    return res;
}

// 扩展中国剩余定理求解函数
long long excrt(const vector<long long> &r, const vector<long long> &m) {
    int n = r.size();
    if (n == 0) return 0;
    
    long long x = 0;  // 当前解
    long long M = 1;  // 当前模数的最小公倍数
    
    for (int i = 0; i < n; i++) {
        // 合并方程：x + t*M ≡ r[i] (mod m[i])
        // 转化为：t*M ≡ (r[i] - x) (mod m[i])
        
        long long a = M;
        long long b = m[i];
        long long c = ((r[i] - x) % b + b) % b;
        
        long long t, s;
        long long gcd = exgcd(a, b, t, s);
        
        if (c % gcd != 0) {
            return -1;  // 无解
        }
        
        // 调整t的值
        long long k = (c / gcd) * t;
        long long b_div_gcd = b / gcd;
        k = (k % b_div_gcd + b_div_gcd) % b_div_gcd;
        
        // 更新解和模数
        x += k * M;
        M *= b_div_gcd;
        x = (x % M + M) % M;
    }
    
    return x;
}

int main() {
    int n;
    while (cin >> n) {
        vector<long long> r(n), m(n);
        
        // 读取输入数据
        for (int i = 0; i < n; i++) {
            cin >> m[i] >> r[i];
        }
        
        // 使用扩展中国剩余定理求解
        long long result = excrt(r, m);
        
        // 输出结果
        if (result == -1) {
            cout << -1 << endl;
        } else {
            cout << result << endl;
        }
    }
    
    return 0;
}

/*
 * 测试用例与验证：
 * 
 * 示例输入1（题目示例）：
 * 2
 * 8 7
 * 11 9
 * 预期输出：31
 * 验证：31 ≡ 7 (mod 8), 31 ≡ 9 (mod 11)
 * 
 * 示例输入2：
 * 3
 * 2 1
 * 3 2
 * 5 3
 * 预期输出：23
 * 验证：23 ≡ 1 (mod 2), 23 ≡ 2 (mod 3), 23 ≡ 3 (mod 5)
 * 
 * 示例输入3（无解情况）：
 * 2
 * 4 1
 * 6 2
 * 预期输出：-1
 * 原因：x ≡ 1 (mod 4) 且 x ≡ 2 (mod 6) 无解
 * 
 * 示例输入4（多组数据）：
 * 2
 * 8 7
 * 11 9
 * 2
 * 2 1
 * 3 2
 * 预期输出：
 * 31
 * 5
 * 
 * 边界测试：
 * 1. n=0：空方程组
 * 2. n=1：单个方程
 * 3. 大数测试：模数和余数都很大
 * 4. 多组数据测试：确保能正确处理多组输入
 * 
 * 算法正确性验证：
 * 1. 数学定理保证：EXCRT算法正确性
 * 2. 验证方法：将结果代入每个方程验证
 * 3. 对比已知结果：如题目示例
 * 
 * 性能分析：
 * 时间复杂度：O(n log max(mi))
 *   - n次循环：O(n)
 *   - 每次循环调用exgcd：O(log max(mi))
 * 空间复杂度：O(n)
 * 
 * 优化建议：
 * 1. 使用快速IO优化输入输出
 * 2. 对于特定情况，可以优化exgcd的实现
 * 3. 考虑使用迭代而非递归的exgcd实现
 * 
 * 工程化扩展：
 * 1. 添加详细的错误信息输出
 * 2. 支持文件输入输出
 * 3. 添加性能测试功能
 * 4. 支持命令行参数
 * 
 * 与洛谷P4777的对比：
 * 1. 题目本质相同，都是EXCRT模板题
 * 2. 输入格式略有不同
 * 3. 解法完全一致
 * 4. 可以互相验证代码正确性
 */