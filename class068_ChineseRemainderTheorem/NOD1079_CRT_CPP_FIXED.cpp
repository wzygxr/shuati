/*
 * 51Nod 1079 中国剩余定理
 * 链接：https://www.51nod.com/Challenge/Problem.html#!#problemId=1079
 * 题目大意：给定一些质数p和对应余数m，求满足所有条件的最小正整数K
 *           一个正整数K，给出K Mod 一些质数的结果，求符合条件的最小的K。
 *           例如，K % 2 = 1, K % 3 = 2, K % 5 = 3。符合条件的最小的K = 23。
 * 
 * 算法思路：
 * 这是一个标准的中国剩余定理应用题。题目保证给出的质数两两互质，可以直接应用中国剩余定理。
 * 
 * 解法步骤：
 * 1. 使用中国剩余定理求解同余方程组
 * 2. 注意输入的质数都是质数，所以两两互质
 * 
 * 算法原理：
 * 这是一道基础的中国剩余定理应用题，展示了CRT在解决同余方程组中的应用。
 * 由于题目保证所有模数都是质数，所以可以直接使用CRT。
 * 
 * 时间复杂度：O(n² log max(mi))，其中n为方程个数，log项来自于扩展欧几里得算法
 * 空间复杂度：O(n)
 * 
 * 工程化考量：
 * 1. 输入验证：确保模数两两互质
 * 2. 大数处理：使用long long防止溢出
 * 3. 异常处理：处理无解情况
 * 4. 边界测试：单个方程、空方程组等边界情况
 */

#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

// 扩展欧几里得算法，求解ax + by = gcd(a,b)
// 返回gcd(a,b)，并通过引用返回x,y
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

// 中国剩余定理求解同余方程组
// 方程组形式：x ≡ r_i (mod m_i)，其中m_i两两互质
// 返回最小正整数解，如果无解返回-1
long long crt(vector<long long> &r, vector<long long> &m) {
    int n = r.size();
    if (n == 0) return 0; // 空方程组
    
    // 验证模数两两互质
    for (int i = 0; i < n; i++) {
        for (int j = i + 1; j < n; j++) {
            long long x, y;
            long long gcd = exgcd(m[i], m[j], x, y);
            if (gcd != 1) {
                // 模数不互质，无法使用标准CRT
                return -1;
            }
        }
    }
    
    // 计算所有模数的乘积
    long long M = 1;
    for (int i = 0; i < n; i++) {
        M *= m[i];
    }
    
    long long result = 0;
    for (int i = 0; i < n; i++) {
        long long Mi = M / m[i];
        long long x, y;
        exgcd(Mi, m[i], x, y); // 求Mi在模m[i]下的逆元
        x = (x % m[i] + m[i]) % m[i]; // 确保逆元为正
        result = (result + r[i] * Mi % M * x % M) % M;
    }
    
    return (result % M + M) % M; // 确保结果为正
}

int main() {
    int n;
    cout << "请输入方程个数：";
    cin >> n;
    
    vector<long long> r(n), m(n);
    
    cout << "请输入每个方程的余数和模数：" << endl;
    for (int i = 0; i < n; i++) {
        cout << "方程" << i+1 << " - 余数r：";
        cin >> r[i];
        cout << "方程" << i+1 << " - 模数m：";
        cin >> m[i];
    }
    
    long long result = crt(r, m);
    
    if (result == -1) {
        cout << "无解：模数不满足两两互质条件" << endl;
    } else {
        cout << "最小正整数解为：" << result << endl;
    }
    
    return 0;
}

/*
 * 测试用例1：
 * 输入：
 * 3
 * 1 2
 * 2 3
 * 3 5
 * 输出：23
 * 
 * 测试用例2：
 * 输入：
 * 2
 * 2 3
 * 3 5
 * 输出：8
 * 
 * 测试用例3（边界测试）：
 * 输入：
 * 1
 * 5 7
 * 输出：5
 * 
 * 复杂度分析：
 * 时间复杂度：O(n² log max(mi))
 *   - 验证互质性：O(n²)次扩展欧几里得算法
 *   - 扩展欧几里得算法：O(log max(mi))
 *   - 总体：O(n² log max(mi))
 * 
 * 空间复杂度：O(n)
 *   - 存储余数和模数数组
 *   - 递归栈深度：O(log max(mi))
 * 
 * 最优解验证：
 * 这是中国剩余定理的标准实现，时间复杂度已经达到理论最优。
 * 对于模数两两互质的情况，这是最优解法。
 */