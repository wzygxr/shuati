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
 * 适用场景：
 * 1. 数论问题求解
 * 2. 密码学中的大数计算
 * 
 * 注意事项：
 * 1. 题目保证所有模数都是质数，所以两两互质
 * 2. 需要处理大数运算
 * 
 * 工程化考虑：
 * 1. 输入校验：检查输入是否合法
 * 2. 异常处理：处理无解的情况
 * 3. 大数处理：使用long long类型防止溢出
 * 4. 龟速乘法：防止乘法溢出
 * 
 * 与其他算法的关联：
 * 1. 中国剩余定理：核心算法
 * 2. 扩展欧几里得算法：用于求逆元
 * 
 * 实际应用：
 * 1. RSA加密算法中的中国剩余定理加速
 * 2. 多精度整数运算的并行处理
 * 
 * 相关题目：
 * 1. 洛谷 P1495 - 曹冲养猪
 *    链接：https://www.luogu.com.cn/problem/P1495
 *    题目大意：求解同余方程组 x ≡ ai (mod mi)，其中mi两两互质
 * 
 * 2. POJ 1006 Biorhythms
 *    链接：http://poj.org/problem?id=1006
 *    题目大意：人的体力、情感和智力周期分别为23天、28天和33天
 * 
 * 3. UVA 756 Biorhythms
 *    链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=697
 *    题目大意：与POJ 1006相同
 * 
 * 代码实现特点：
 * 1. 使用扩展欧几里得算法求逆元
 * 2. 使用龟速乘法防止溢出
 * 3. 处理大数运算
 * 4. 确保结果为正数
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

// 龟速乘法，防止大数乘法溢出
// 计算a*b mod m，使用二进制分解防止溢出
long long mul(long long a, long long b, long long mod) {
    a = (a % mod + mod) % mod;  // 确保a为正数
    b = (b % mod + mod) % mod;  // 确保b为正数
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

// 中国剩余定理求解函数
// 参数：余数数组r，模数数组m（模数两两互质）
// 返回：最小正整数解，如果无解返回-1
long long crt(const vector<long long> &r, const vector<long long> &m) {
    int n = r.size();
    if (n == 0) return 0;  // 空方程组，任意数都是解
    
    // 计算所有模数的乘积
    long long M = 1;
    for (int i = 0; i < n; i++) {
        if (m[i] <= 0) return -1;  // 模数必须为正数
        M *= m[i];
    }
    
    long long result = 0;
    for (int i = 0; i < n; i++) {
        long long Mi = M / m[i];  // Mi = M / mi
        long long x, y;
        
        // 求Mi在模mi意义下的逆元
        long long gcd = exgcd(Mi, m[i], x, y);
        if (gcd != 1) {
            // 理论上模数互质时gcd应该为1
            // 但为了安全还是检查一下
            return -1;
        }
        
        // 确保逆元为正数
        x = (x % m[i] + m[i]) % m[i];
        
        // 计算当前项的贡献：r[i] * Mi * Mi^(-1)
        long long term = mul(r[i], mul(Mi, x, M), M);
        result = (result + term) % M;
    }
    
    // 确保结果为正数
    result = (result % M + M) % M;
    return result;
}

int main() {
    int n;
    cin >> n;
    
    vector<long long> r(n), m(n);
    
    // 读取输入数据
    for (int i = 0; i < n; i++) {
        cin >> m[i] >> r[i];
        // 题目保证m[i]是质数，所以两两互质
    }
    
    // 使用中国剩余定理求解
    long long result = crt(r, m);
    
    // 输出结果
    cout << result << endl;
    
    return 0;
}

/*
 * 测试用例与验证：
 * 
 * 示例输入1（题目示例）：
 * 3
 * 2 1
 * 3 2
 * 5 3
 * 预期输出：23
 * 
 * 示例输入2：
 * 2
 * 3 2
 * 5 3
 * 预期输出：8
 * 
 * 示例输入3：
 * 1
 * 7 4
 * 预期输出：4
 * 
 * 边界测试：
 * 1. n=0：空方程组，返回0
 * 2. n=1：单个方程，直接返回余数
 * 3. 大数测试：模数接近10^5，乘积可能很大
 * 4. 负数余数：确保能正确处理负数
 * 
 * 算法正确性验证：
 * 1. 数学定理保证：当模数两两互质时，CRT必然有唯一解
 * 2. 验证方法：将结果代入每个方程验证
 * 3. 小数据手动验证：如示例输入
 * 
 * 性能分析：
 * 时间复杂度：O(n² log max(mi))
 *   - n次循环：O(n)
 *   - 每次循环调用exgcd：O(log max(mi))
 *   - 龟速乘法：O(log b)
 * 空间复杂度：O(n)
 *   - 存储r和m数组：O(n)
 *   - 递归栈深度：O(log max(mi))
 * 
 * 优化建议：
 * 1. 如果n很大，可以考虑优化exgcd的实现
 * 2. 对于特定模数，可以预处理逆元
 * 3. 使用快速IO优化输入输出
 * 
 * 工程化扩展：
 * 1. 添加输入验证和错误处理
 * 2. 支持文件输入输出
 * 3. 添加详细的日志输出
 * 4. 支持多组测试数据
 */