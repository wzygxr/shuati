/*
 * 洛谷 P3868【TJOI2009】猜数字
 * 链接：https://www.luogu.com.cn/problem/P3868
 * 题目大意：现在有两组数字，a1,a2,...,an 和 b1,b2,...,bn。其中，ai两两互质。
 *           现在请求出一个最小的正整数N，使得bi | (N - ai) 对所有i成立。
 *           条件bi | (N - ai)等价于N ≡ ai (mod bi)，这就转化为了标准的中国剩余定理问题。
 * 
 * 算法思路：
 * 将条件bi | (N - ai)转化为N ≡ ai (mod bi)，然后使用中国剩余定理求解。
 * 题目保证bi两两互质，所以可以直接应用CRT。
 * 
 * 解法步骤：
 * 1. 将问题转化为同余方程组：N ≡ ai (mod bi)
 * 2. 使用中国剩余定理求解
 * 3. 注意处理大数运算和溢出问题
 * 
 * 算法原理：
 * 这是一道将整除条件转化为同余方程的典型题目，展示了CRT在实际问题中的应用。
 * 通过将整除条件转化为同余方程，我们可以利用CRT高效求解。
 * 
 * 时间复杂度：O(n² log max(bi))，其中n为方程个数
 * 空间复杂度：O(n)
 * 
 * 适用场景：
 * 1. 数论问题中的整除条件求解
 * 2. 密码学中的模运算问题
 * 
 * 注意事项：
 * 1. 题目保证bi两两互质
 * 2. 需要处理大数运算，防止溢出
 * 3. 注意结果可能很大，需要使用long long类型
 * 
 * 工程化考虑：
 * 1. 输入校验：检查输入是否合法
 * 2. 异常处理：处理无解的情况
 * 3. 大数处理：使用龟速乘法防止溢出
 * 4. 快速IO：优化输入输出效率
 * 
 * 与其他算法的关联：
 * 1. 中国剩余定理：核心算法
 * 2. 扩展欧几里得算法：用于求逆元
 * 3. 龟速乘法：防止大数乘法溢出
 * 
 * 实际应用：
 * 1. 数论问题求解
 * 2. 密码学算法实现
 * 3. 大整数计算
 * 
 * 相关题目：
 * 1. 洛谷 P1495 - 曹冲养猪
 * 2. 51Nod 1079 - 中国剩余定理
 * 3. POJ 1006 - Biorhythms
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

// 中国剩余定理求解函数
long long crt(const vector<long long> &a, const vector<long long> &b) {
    int n = a.size();
    if (n == 0) return 0;
    
    // 计算所有模数的乘积
    long long M = 1;
    for (int i = 0; i < n; i++) {
        if (b[i] <= 0) return -1;
        M *= b[i];
    }
    
    long long result = 0;
    for (int i = 0; i < n; i++) {
        long long Mi = M / b[i];
        long long x, y;
        
        // 求Mi在模b[i]意义下的逆元
        long long gcd = exgcd(Mi, b[i], x, y);
        if (gcd != 1) {
            return -1;  // 理论上应该互质
        }
        
        // 确保逆元为正数
        x = (x % b[i] + b[i]) % b[i];
        
        // 计算当前项的贡献
        long long term = mul(a[i], mul(Mi, x, M), M);
        result = (result + term) % M;
    }
    
    // 确保结果为正数
    result = (result % M + M) % M;
    return result;
}

int main() {
    int n;
    cin >> n;
    
    vector<long long> a(n), b(n);
    
    // 读取输入数据
    for (int i = 0; i < n; i++) {
        cin >> a[i];
    }
    for (int i = 0; i < n; i++) {
        cin >> b[i];
        // 题目保证b[i]两两互质
    }
    
    // 注意：题目中ai可能为负数，需要处理
    for (int i = 0; i < n; i++) {
        a[i] = (a[i] % b[i] + b[i]) % b[i];
    }
    
    // 使用中国剩余定理求解
    long long result = crt(a, b);
    
    // 输出结果
    cout << result << endl;
    
    return 0;
}

/*
 * 测试用例与验证：
 * 
 * 示例输入1：
 * 3
 * 1 2 3
 * 2 3 5
 * 预期输出：23
 * 验证：23 ≡ 1 (mod 2), 23 ≡ 2 (mod 3), 23 ≡ 3 (mod 5)
 * 
 * 示例输入2：
 * 2
 * 2 3
 * 3 5
 * 预期输出：8
 * 验证：8 ≡ 2 (mod 3), 8 ≡ 3 (mod 5)
 * 
 * 示例输入3（包含负数）：
 * 2
 * -1 4
 * 3 5
 * 预期输出：满足条件的正整数
 * 
 * 边界测试：
 * 1. n=0：空方程组
 * 2. n=1：单个方程
 * 3. 大数测试：模数和余数都很大
 * 4. 负数处理：确保能正确处理负数余数
 * 
 * 算法正确性验证：
 * 1. 数学定理保证：当模数两两互质时，CRT必然有唯一解
 * 2. 验证方法：将结果代入每个方程验证
 * 3. 小数据手动验证
 * 
 * 性能分析：
 * 时间复杂度：O(n² log max(bi))
 *   - n次循环：O(n)
 *   - 每次循环调用exgcd：O(log max(bi))
 *   - 龟速乘法：O(log b)
 * 空间复杂度：O(n)
 * 
 * 优化建议：
 * 1. 使用快速IO优化输入输出
 * 2. 对于特定情况，可以优化逆元计算
 * 3. 考虑使用迭代而非递归的exgcd实现
 * 
 * 工程化扩展：
 * 1. 添加详细的错误信息输出
 * 2. 支持多组测试数据
 * 3. 添加性能测试功能
 * 4. 支持命令行参数
 */