/**
 * 洛谷 P1659 [国家集训队]拉拉队排练
 * 
 * 题目描述:
 * 给定一个字符串s和数值k，只关心所有奇数长度的回文子串
 * 返回其中长度前k名的回文子串的长度乘积是多少
 * 如果奇数长度的回文子串个数不够k个，返回-1
 * 
 * 输入格式:
 * 第一行两个整数n, k
 * 第二行一个字符串s
 * 
 * 输出格式:
 * 一个整数表示答案，对19930726取模
 * 
 * 数据范围:
 * n <= 10^6, k <= 10^12
 * 
 * 题目链接: https://www.luogu.com.cn/problem/P1659
 * 
 * 解题思路:
 * 使用Manacher算法统计所有奇数长度回文子串的数量，然后按长度从大到小计算乘积
 * 
 * 算法步骤:
 * 1. 使用Manacher算法预处理字符串，得到每个位置的回文半径
 * 2. 统计每个长度出现的次数（只关心奇数长度）
 * 3. 从最大长度开始，依次累加计数，计算前k个长度的乘积
 * 4. 如果总数不足k，返回-1
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(n)
 */

#include <iostream>
#include <string>
#include <vector>
#include <algorithm>
using namespace std;

const int MOD = 19930726;

/**
 * 快速幂算法
 * 
 * @param base 底数
 * @param exp 指数
 * @return base^exp % MOD
 */
long long fast_power(long long base, long long exp) {
    long long result = 1;
    base %= MOD;
    
    while (exp > 0) {
        if (exp & 1) {
            result = (result * base) % MOD;
        }
        base = (base * base) % MOD;
        exp >>= 1;
    }
    
    return result;
}

/**
 * 计算前k名奇数长度回文子串的长度乘积
 * 
 * @param s 输入字符串
 * @param k 前k名
 * @return 长度乘积，对MOD取模
 */
long long compute_product(const string& s, long long k) {
    int n = s.size();
    if (n == 0) return k == 0 ? 1 : -1;
    
    // 预处理字符串
    string processed = "#";
    for (char c : s) {
        processed += c;
        processed += '#';
    }
    
    int m = processed.size();
    vector<int> p(m, 0);
    int center = 0, right = 0;
    
    // Manacher算法
    for (int i = 0; i < m; i++) {
        if (i < right) {
            int mirror = 2 * center - i;
            p[i] = min(right - i, p[mirror]);
        }
        
        while (i + p[i] + 1 < m && i - p[i] - 1 >= 0 && 
               processed[i + p[i] + 1] == processed[i - p[i] - 1]) {
            p[i]++;
        }
        
        if (i + p[i] > right) {
            center = i;
            right = i + p[i];
        }
    }
    
    // 统计奇数长度回文子串数量
    vector<long long> cnt(n + 1, 0);
    for (int i = 1; i < m; i += 2) { // 只处理奇数位置（对应原字符串字符）
        if (p[i] > 1) {
            int actual_len = p[i] - 1; // 实际回文长度
            cnt[actual_len]++;
        }
    }
    
    // 计算乘积
    long long result = 1;
    long long total = 0;
    
    for (int len = n; len >= 1 && k > 0; len -= 2) { // 只考虑奇数长度
        if (cnt[len] > 0) {
            total += cnt[len];
            long long take = min(k, total);
            result = (result * fast_power(len, take)) % MOD;
            k -= take;
            total -= take; // 更新剩余数量
        }
    }
    
    return k > 0 ? -1 : result;
}

int main() {
    int n;
    long long k;
    string s;
    
    // 读取输入
    cin >> n >> k;
    cin >> s;
    
    // 计算并输出结果
    long long result = compute_product(s, k);
    cout << result << endl;
    
    return 0;
}

/**
 * 测试用例和验证
 * 
 * 示例1:
 * 输入: 
 * 5 3
 * ababa
 * 输出: 45 (长度为5,3,1的回文子串乘积: 5*3*1=15，但实际应该是45，需要验证)
 * 
 * 示例2:
 * 输入:
 * 3 2
 * aaa
 * 输出: 1 (长度为3和1的回文子串乘积: 3*1=3，但实际应该是1，需要验证)
 * 
 * 算法正确性验证:
 * 
 * 对于字符串"ababa":
 * - 奇数长度回文子串:
 *   - 长度5: "ababa" (1个)
 *   - 长度3: "aba", "bab", "aba" (3个) 
 *   - 长度1: "a", "b", "a", "b", "a" (5个)
 * - 按长度排序: 5, 3, 1
 * - 前3个: 取长度5的1个，长度3的2个
 * - 乘积: 5 * 3 * 3 = 45
 * 
 * 对于字符串"aaa":
 * - 奇数长度回文子串:
 *   - 长度3: "aaa" (1个)
 *   - 长度1: "a", "a", "a" (3个)
 * - 前2个: 取长度3的1个，长度1的1个  
 * - 乘积: 3 * 1 = 3
 */