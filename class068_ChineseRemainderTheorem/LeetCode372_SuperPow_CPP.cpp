/*
 * LeetCode 372. 超级次方
 * 链接：https://leetcode.cn/problems/super-pow/
 * 题目大意：计算 a^b mod 1337，其中b是一个非常大的数，用数组表示
 * 
 * 算法思路：
 * 可以使用中国剩余定理优化计算：由于1337=7×191，且gcd(7,191)=1，
 * 我们可以分别计算x1 = a^b mod 7和x2 = a^b mod 191，
 * 然后使用中国剩余定理合并结果。
 * 
 * 解法步骤：
 * 1. 分解模数1337=7×191
 * 2. 分别计算a^b mod 7和a^b mod 191
 * 3. 使用中国剩余定理合并结果
 * 4. 使用快速幂优化指数运算
 * 
 * 算法原理：
 * 利用中国剩余定理将大模数运算分解为小模数运算，
 * 结合快速幂算法高效计算大数次方。
 * 
 * 时间复杂度：O(n log b)，其中n为b的位数
 * 空间复杂度：O(1)
 * 
 * 适用场景：
 * 1. 大数次方模运算
 * 2. 密码学中的模运算优化
 * 3. 数论问题求解
 * 
 * 注意事项：
 * 1. 需要处理a=0的特殊情况
 * 2. 注意b可能为0
 * 3. 需要正确实现快速幂算法
 * 
 * 工程化考虑：
 * 1. 输入校验：检查输入是否合法
 * 2. 异常处理：处理边界情况
 * 3. 性能优化：使用快速幂和CRT优化
 * 
 * 与其他算法的关联：
 * 1. 中国剩余定理：核心优化手段
 * 2. 快速幂算法：基础组件
 * 3. 模运算：基础数学运算
 * 
 * 实际应用：
 * 1. 密码学算法实现
 * 2. 大整数计算
 * 3. 数论问题求解
 * 
 * 相关题目：
 * 1. 中国剩余定理相关题目
 * 2. 快速幂相关题目
 * 3. 模运算相关题目
 */

#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

class Solution {
public:
    int superPow(int a, vector<int>& b) {
        if (b.empty()) return 1;
        
        // 分解模数1337=7×191
        const int mod1 = 7;
        const int mod2 = 191;
        const int mod = 1337;
        
        // 计算a^b mod 7
        int x1 = powMod(a, b, mod1);
        
        // 计算a^b mod 191
        int x2 = powMod(a, b, mod2);
        
        // 使用中国剩余定理合并结果
        return crt(x1, x2, mod1, mod2, mod);
    }
    
private:
    // 快速幂模运算
    int powMod(int a, vector<int>& b, int mod) {
        int result = 1;
        a %= mod;
        
        // 从高位到低位处理b的每一位
        for (int i = 0; i < b.size(); i++) {
            // result = result^10 * a^b[i] mod mod
            result = (pow(result, 10, mod) * pow(a, b[i], mod)) % mod;
        }
        
        return result;
    }
    
    // 快速幂计算a^b mod m
    int pow(int a, int b, int m) {
        int result = 1;
        a %= m;
        while (b > 0) {
            if (b & 1) {
                result = (result * a) % m;
            }
            a = (a * a) % m;
            b >>= 1;
        }
        return result;
    }
    
    // 中国剩余定理合并两个方程
    // x ≡ r1 (mod m1), x ≡ r2 (mod m2)
    // 其中m1和m2互质，模数为m1*m2
    int crt(int r1, int r2, int m1, int m2, int mod) {
        // 计算M1 = m2, M2 = m1
        long long M1 = m2, M2 = m1;
        
        // 求M1在模m1意义下的逆元
        long long x, y;
        exgcd(M1, m1, x, y);
        x = (x % m1 + m1) % m1;
        
        // 求M2在模m2意义下的逆元
        long long u, v;
        exgcd(M2, m2, u, v);
        u = (u % m2 + m2) % m2;
        
        // 计算最终解
        long long result = (r1 * M1 % mod * x % mod + r2 * M2 % mod * u % mod) % mod;
        return result;
    }
    
    // 扩展欧几里得算法
    void exgcd(long long a, long long b, long long &x, long long &y) {
        if (b == 0) {
            x = 1;
            y = 0;
            return;
        }
        exgcd(b, a % b, y, x);
        y -= a / b * x;
    }
};

int main() {
    Solution solution;
    
    // 测试用例1
    int a1 = 2;
    vector<int> b1 = {3};
    cout << "测试1: " << solution.superPow(a1, b1) << endl;  // 预期: 8
    
    // 测试用例2
    int a2 = 2;
    vector<int> b2 = {1, 0};
    cout << "测试2: " << solution.superPow(a2, b2) << endl;  // 预期: 1024
    
    // 测试用例3
    int a3 = 1;
    vector<int> b3 = {4, 3, 3, 8, 5, 2};
    cout << "测试3: " << solution.superPow(a3, b3) << endl;  // 预期: 1
    
    // 测试用例4
    int a4 = 2147483647;
    vector<int> b4 = {2, 0, 0};
    cout << "测试4: " << solution.superPow(a4, b4) << endl;  // 预期: 1198
    
    return 0;
}

/*
 * 测试用例与验证：
 * 
 * 示例输入1：a=2, b=[3]
 * 计算：2^3 = 8, 8 mod 1337 = 8
 * 预期输出：8
 * 
 * 示例输入2：a=2, b=[1,0]
 * 计算：2^10 = 1024, 1024 mod 1337 = 1024
 * 预期输出：1024
 * 
 * 示例输入3：a=1, b=[4,3,3,8,5,2]
 * 计算：1的任何次方都是1
 * 预期输出：1
 * 
 * 示例输入4：a=2147483647, b=[2,0,0]
 * 计算：2147483647^200 mod 1337
 * 预期输出：1198
 * 
 * 边界测试：
 * 1. a=0：0的任何正数次方都是0
 * 2. b为空数组：任何数的0次方都是1
 * 3. a=1：结果总是1
 * 4. b=[0]：任何数的0次方都是1
 * 5. 大数测试：a和b都很大
 * 
 * 算法正确性验证：
 * 1. 数学验证：使用直接计算验证CRT结果的正确性
 * 2. 对比验证：与不使用CRT的直接计算方法对比
 * 3. 边界验证：确保边界情况处理正确
 * 
 * 性能分析：
 * 时间复杂度：O(n log b)
 *   - 处理b的每一位：O(n)
 *   - 每次处理使用快速幂：O(log b)
 * 空间复杂度：O(1)
 *   - 只使用常数个变量
 * 
 * 优化建议：
 * 1. 使用更高效的快速幂实现
 * 2. 预处理模数分解结果
 * 3. 使用迭代而非递归的exgcd实现
 * 
 * 工程化扩展：
 * 1. 添加详细的错误信息输出
 * 2. 支持更多的测试用例
 * 3. 添加性能测试功能
 * 4. 支持命令行参数
 * 
 * 算法优势：
 * 1. 利用CRT将大模数运算分解为小模数运算
 * 2. 结合快速幂高效计算大数次方
 * 3. 适用于大数运算场景
 * 
 * 应用场景扩展：
 * 1. 密码学中的模幂运算
 * 2. 大整数计算库的实现
 * 3. 数论问题的求解
 */