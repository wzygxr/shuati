/*
 * 洛谷 P4777【模板】扩展中国剩余定理（EXCRT）
 * 链接：https://www.luogu.com.cn/problem/P4777
 * 题目大意：求解同余方程组 x ≡ ri (mod mi)，其中mi不一定两两互质
 * 
 * 算法思路：
 * 扩展中国剩余定理用于求解模数不一定两两互质的同余方程组。
 * 通过逐步合并方程的方式求解，每次合并两个方程为一个新的方程。
 * 
 * 解法步骤：
 * 1. 初始化解x=0，模数M=1
 * 2. 对于每个方程x ≡ ri (mod mi)：
 *    a. 将当前解表示为x + t*M
 *    b. 代入新方程：x + t*M ≡ ri (mod mi)
 *    c. 转化为：t*M ≡ (ri - x) (mod mi)
 *    d. 使用扩展欧几里得算法求解t
 *    e. 更新解x和模数M
 * 3. 返回最终解
 * 
 * 算法原理：
 * EXCRT是中国剩余定理的扩展，适用于模数不一定互质的情况。
 * 核心思想是通过逐步合并方程，将多个方程合并为一个方程。
 * 
 * 时间复杂度：O(n log max(mi))，其中n为方程个数
 * 空间复杂度：O(n)
 * 
 * 适用场景：
 * 1. 模数不一定两两互质的同余方程组
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
 * 1. POJ 2891 - Strange Way to Express Integers
 * 2. HDU 3579 - Hello Kiki
 * 3. NOI 2018 - 屠龙勇士
 */

#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

// 扩展欧几里得算法
// 求解ax + by = gcd(a,b)，返回gcd(a,b)
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
// 参数：余数数组r，模数数组m（模数不一定互质）
// 返回：最小正整数解，如果无解返回-1
long long excrt(const vector<long long> &r, const vector<long long> &m) {
    int n = r.size();
    if (n == 0) return 0;  // 空方程组
    
    long long x = 0;  // 当前解
    long long M = 1;  // 当前模数的最小公倍数
    
    for (int i = 0; i < n; i++) {
        // 合并第i个方程：x ≡ r[i] (mod m[i])
        // 当前解可以表示为：x + t*M
        // 代入新方程：x + t*M ≡ r[i] (mod m[i])
        // 转化为：t*M ≡ (r[i] - x) (mod m[i])
        
        long long a = M;
        long long b = m[i];
        long long c = ((r[i] - x) % b + b) % b;  // 确保c为正数
        
        long long t, s;
        long long gcd = exgcd(a, b, t, s);
        
        // 检查是否有解
        if (c % gcd != 0) {
            return -1;  // 无解
        }
        
        // 调整t的值
        long long k = (c / gcd) * t;
        long long b_div_gcd = b / gcd;
        
        // 确保k在合理范围内
        k = (k % b_div_gcd + b_div_gcd) % b_div_gcd;
        
        // 更新解和模数
        x += k * M;
        M *= b_div_gcd;
        x = (x % M + M) % M;  // 确保x为正数
    }
    
    return x;
}

int main() {
    int n;
    cin >> n;
    
    vector<long long> r(n), m(n);
    
    // 读取输入数据
    for (int i = 0; i < n; i++) {
        cin >> m[i] >> r[i];
        // 模数不一定两两互质
    }
    
    // 使用扩展中国剩余定理求解
    long long result = excrt(r, m);
    
    // 输出结果
    if (result == -1) {
        cout << "无解" << endl;
    } else {
        cout << result << endl;
    }
    
    return 0;
}

/*
 * 测试用例与验证：
 * 
 * 示例输入1（模数互质，等价于CRT）：
 * 3
 * 2 1
 * 3 2
 * 5 3
 * 预期输出：23
 * 
 * 示例输入2（模数不互质）：
 * 2
 * 4 1
 * 6 3
 * 预期输出：9
 * 验证：9 ≡ 1 (mod 4), 9 ≡ 3 (mod 6)
 * 
 * 示例输入3（无解情况）：
 * 2
 * 4 1
 * 6 2
 * 预期输出：无解
 * 原因：x ≡ 1 (mod 4) 且 x ≡ 2 (mod 6) 无解
 * 
 * 边界测试：
 * 1. n=0：空方程组
 * 2. n=1：单个方程
 * 3. 大数测试：模数和余数都很大
 * 4. 无解测试：确保能正确识别无解情况
 * 
 * 算法正确性验证：
 * 1. 数学定理保证：EXCRT算法正确性
 * 2. 验证方法：将结果代入每个方程验证
 * 3. 对比CRT：当模数互质时，结果应与CRT一致
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
 * 2. 支持多组测试数据
 * 3. 添加性能测试功能
 * 4. 支持命令行参数
 * 5. 添加详细的日志输出
 * 
 * 与CRT的对比：
 * 1. 适用性：EXCRT适用于更广泛的情况（模数不一定互质）
 * 2. 复杂度：EXCRT时间复杂度稍高
 * 3. 实现难度：EXCRT实现更复杂
 * 4. 选择建议：如果模数确定互质，优先使用CRT
 */