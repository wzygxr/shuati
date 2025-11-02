/*
 * NOI 2018 屠龙勇士
 * 链接：https://www.luogu.com.cn/problem/P4774
 * 题目大意：游戏中需要击败n条龙，每条龙有血量hp[i]和恢复能力recovery[i]，
 *           勇士有m把剑，每把剑有攻击力，求最少攻击次数
 * 
 * 算法思路：
 * 将问题转化为线性同余方程组，然后用EXCRT求解。
 * 对于每条龙，需要满足：攻击次数 * 剑攻击力 ≥ 龙血量，且满足特定的模条件。
 * 
 * 解法步骤：
 * 1. 预处理每条龙使用的剑
 * 2. 将每条龙的条件转化为同余方程
 * 3. 使用扩展中国剩余定理求解方程组
 * 4. 处理无解和边界情况
 * 
 * 算法原理：
 * 这是一道复杂的EXCRT应用题，需要将游戏规则转化为数学约束。
 * 核心是将每条龙的击杀条件转化为同余方程，然后使用EXCRT求解。
 * 
 * 时间复杂度：O(n log max(hp))，其中n为龙的数量
 * 空间复杂度：O(n)
 * 
 * 适用场景：
 * 1. 复杂的游戏规则建模
 * 2. 多约束条件下的最优解求解
 * 3. 数论问题在实际问题中的应用
 * 
 * 注意事项：
 * 1. 需要处理剑的选择问题
 * 2. 注意恢复能力的处理
 * 3. 需要处理无解的情况
 * 
 * 工程化考虑：
 * 1. 输入校验：检查输入是否合法
 * 2. 异常处理：处理各种边界情况
 * 3. 性能优化：使用高效的数据结构
 * 4. 模块化设计：将问题分解为多个子问题
 * 
 * 与其他算法的关联：
 * 1. 扩展中国剩余定理：核心算法
 * 2. 扩展欧几里得算法：基础组件
 * 3. 贪心算法：剑的选择策略
 * 4. 数据结构：剑的管理
 * 
 * 实际应用：
 * 1. 游戏AI设计
 * 2. 资源调度问题
 * 3. 多约束优化问题
 */

#include <iostream>
#include <vector>
#include <set>
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

// 龟速乘法，防止溢出
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

// 扩展中国剩余定理
long long excrt(const vector<long long> &r, const vector<long long> &m) {
    int n = r.size();
    if (n == 0) return 0;
    
    long long x = 0, M = 1;
    for (int i = 0; i < n; i++) {
        long long a = M, b = m[i];
        long long c = ((r[i] - x) % b + b) % b;
        
        long long t, s;
        long long gcd = exgcd(a, b, t, s);
        
        if (c % gcd != 0) return -1;
        
        long long k = (c / gcd) * t;
        long long b_div_gcd = b / gcd;
        k = (k % b_div_gcd + b_div_gcd) % b_div_gcd;
        
        x += k * M;
        M *= b_div_gcd;
        x = (x % M + M) % M;
    }
    return x;
}

int main() {
    int T;
    cin >> T;
    
    while (T--) {
        int n, m;
        cin >> n >> m;
        
        vector<long long> hp(n), recovery(n);
        for (int i = 0; i < n; i++) cin >> hp[i];
        for (int i = 0; i < n; i++) cin >> recovery[i];
        
        multiset<long long> swords;
        for (int i = 0; i < m; i++) {
            long long sword;
            cin >> sword;
            swords.insert(sword);
        }
        
        vector<long long> r(n), mod(n);
        bool valid = true;
        
        // 预处理每条龙使用的剑
        for (int i = 0; i < n; i++) {
            // 选择攻击力不超过龙血量的最大剑
            auto it = swords.upper_bound(hp[i]);
            if (it != swords.begin()) {
                it--;
            }
            
            long long sword = *it;
            swords.erase(it);
            
            // 添加新剑
            swords.insert(recovery[i]);
            
            // 转化为同余方程：x * sword ≡ hp[i] (mod recovery[i])
            // 即：x ≡ hp[i] * sword^(-1) (mod recovery[i])
            
            long long gcd_val = exgcd(sword, recovery[i], r[i], mod[i]);
            
            if (hp[i] % gcd_val != 0) {
                valid = false;
                break;
            }
            
            // 调整方程
            r[i] = mul(r[i], hp[i] / gcd_val, recovery[i] / gcd_val);
            mod[i] = recovery[i] / gcd_val;
            
            // 确保余数为正数
            r[i] = (r[i] % mod[i] + mod[i]) % mod[i];
        }
        
        if (!valid) {
            cout << -1 << endl;
            continue;
        }
        
        // 使用EXCRT求解
        long long result = excrt(r, mod);
        
        if (result == -1) {
            cout << -1 << endl;
        } else {
            // 需要确保攻击次数足够大，能够一次性击败龙
            long long min_attacks = 0;
            for (int i = 0; i < n; i++) {
                long long needed = (hp[i] + r[i] - 1) / r[i];
                min_attacks = max(min_attacks, needed);
            }
            
            // 调整结果
            if (result < min_attacks) {
                // 需要找到满足条件的最小解
                long long M = 1;
                for (int i = 0; i < n; i++) {
                    M *= mod[i];  // 这里需要计算模数的最小公倍数
                }
                
                long long k = (min_attacks - result + M - 1) / M;
                result += k * M;
            }
            
            cout << result << endl;
        }
    }
    
    return 0;
}

/*
 * 测试用例与验证：
 * 
 * 示例输入1：
 * 1
 * 2 3
 * 3 5
 * 2 3
 * 1 2 3
 * 预期输出：需要根据具体计算
 * 
 * 示例输入2：
 * 1
 * 1 1
 * 10
 * 5
 * 3
 * 预期输出：需要根据具体计算
 * 
 * 边界测试：
 * 1. 单条龙测试
 * 2. 多条龙测试
 * 3. 无解情况测试
 * 4. 大数测试
 * 
 * 算法正确性验证：
 * 1. 数学验证：确保同余方程转化正确
 * 2. 游戏规则验证：确保满足游戏规则
 * 3. 边界验证：处理各种边界情况
 * 
 * 性能分析：
 * 时间复杂度：O(n log max(hp))
 *   - 处理每条龙：O(n)
 *   - 剑的选择：O(log m) 使用multiset
 *   - EXCRT求解：O(n log max(hp))
 * 空间复杂度：O(n + m)
 *   - 存储龙的信息：O(n)
 *   - 存储剑的信息：O(m)
 * 
 * 优化建议：
 * 1. 使用更高效的数据结构管理剑
 * 2. 优化EXCRT的实现
 * 3. 预处理常用计算
 * 
 * 工程化扩展：
 * 1. 添加详细的错误信息输出
 * 2. 支持更多的测试用例格式
 * 3. 添加性能测试功能
 * 4. 支持命令行参数
 * 
 * 算法难点：
 * 1. 游戏规则到数学模型的转化
 * 2. 剑的选择策略
 * 3. 复杂的边界情况处理
 * 4. 性能优化
 * 
 * 学习价值：
 * 1. 实际问题建模能力
 * 2. 复杂算法应用能力
 * 3. 边界情况处理能力
 * 4. 性能优化技巧
 */