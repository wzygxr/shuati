// 不同的子序列II (Distinct Subsequences II)
// 给定一个字符串 s，计算 s 的不同非空子序列的个数。
// 因为结果可能很大，所以返回答案模 10^9 + 7。
// 字符串的子序列是由原字符串删除一些（也可以不删除）字符而不改变剩余字符相对位置形成的新字符串。
// 例如，"ace" 是 "abcde" 的一个子序列，而 "aec" 不是。
// 测试链接 : https://leetcode.cn/problems/distinct-subsequences-ii/

#include <iostream>
#include <vector>
#include <string>
using namespace std;

class Solution {
private:
    static const int MOD = 1000000007;
    
public:
    // 方法1：动态规划
    // 时间复杂度：O(n) - n为字符串长度
    // 空间复杂度：O(1) - 使用固定大小的数组
    // 核心思路：记录以每个字母结尾的子序列数量，避免重复计数
    int distinctSubseqII(string s) {
        vector<long long> last(26, 0); // 记录以每个字母结尾的子序列数量
        long long total = 1; // 空子序列
        
        for (char c : s) {
            int idx = c - 'a';
            long long newSubseq = total; // 当前字符可以添加到所有现有子序列后面
            long long duplicate = last[idx]; // 重复的数量
            
            // 更新以当前字符结尾的子序列数量
            last[idx] = (last[idx] + newSubseq - duplicate + MOD) % MOD;
            
            // 更新总子序列数量
            total = (total + newSubseq - duplicate + MOD) % MOD;
        }
        
        return (total - 1 + MOD) % MOD; // 减去空子序列
    }
    
    // 方法2：更直观的动态规划
    // 时间复杂度：O(n) - 遍历字符串一次
    // 空间复杂度：O(1) - 使用固定大小的数组
    // 核心思路：每次遇到新字符时，新的子序列数量等于之前的总数
    int distinctSubseqII2(string s) {
        vector<long long> dp(26, 0);
        long long total = 0;
        
        for (char c : s) {
            int idx = c - 'a';
            long long newCount = (total + 1) % MOD; // 当前字符单独作为子序列 + 添加到所有现有子序列后面
            
            // 减去重复的部分（之前以相同字符结尾的子序列）
            long long duplicate = dp[idx];
            dp[idx] = newCount;
            total = (total + newCount - duplicate + MOD) % MOD;
        }
        
        return total;
    }
    
    // 方法3：使用数组记录最后出现位置
    // 时间复杂度：O(n) - 遍历字符串一次
    // 空间复杂度：O(1) - 使用固定大小的数组
    // 核心思路：记录每个字符最后出现时的子序列数量
    int distinctSubseqII3(string s) {
        vector<long long> last(26, 0);
        long long total = 0;
        
        for (char c : s) {
            int idx = c - 'a';
            long long prevTotal = total;
            
            // 新的子序列数量 = 之前的总数 + 1（当前字符单独作为子序列）
            total = (total * 2 + 1) % MOD;
            
            // 减去重复的部分（之前以相同字符结尾的子序列）
            total = (total - last[idx] + MOD) % MOD;
            
            // 更新最后出现位置的子序列数量
            last[idx] = (prevTotal + 1) % MOD;
        }
        
        return total;
    }
};

// 测试用例和性能对比
int main() {
    Solution solution;
    
    // 测试用例1
    string s1 = "abc";
    cout << "测试用例1 - s: \"abc\"" << endl;
    cout << "方法1结果: " << solution.distinctSubseqII(s1) << endl;
    cout << "方法2结果: " << solution.distinctSubseqII2(s1) << endl;
    cout << "方法3结果: " << solution.distinctSubseqII3(s1) << endl;
    cout << "预期结果: 7" << endl << endl;
    
    // 测试用例2
    string s2 = "aba";
    cout << "测试用例2 - s: \"aba\"" << endl;
    cout << "方法1结果: " << solution.distinctSubseqII(s2) << endl;
    cout << "方法2结果: " << solution.distinctSubseqII2(s2) << endl;
    cout << "方法3结果: " << solution.distinctSubseqII3(s2) << endl;
    cout << "预期结果: 6" << endl << endl;
    
    // 测试用例3
    string s3 = "aaa";
    cout << "测试用例3 - s: \"aaa\"" << endl;
    cout << "方法1结果: " << solution.distinctSubseqII(s3) << endl;
    cout << "方法2结果: " << solution.distinctSubseqII2(s3) << endl;
    cout << "方法3结果: " << solution.distinctSubseqII3(s3) << endl;
    cout << "预期结果: 3" << endl << endl;
    
    // 测试用例4
    string s4 = "z";
    cout << "测试用例4 - s: \"z\"" << endl;
    cout << "方法1结果: " << solution.distinctSubseqII(s4) << endl;
    cout << "方法2结果: " << solution.distinctSubseqII2(s4) << endl;
    cout << "方法3结果: " << solution.distinctSubseqII3(s4) << endl;
    cout << "预期结果: 1" << endl;
    
    return 0;
}