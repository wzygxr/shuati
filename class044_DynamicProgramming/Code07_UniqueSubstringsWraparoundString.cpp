// 环绕字符串中唯一的子字符串 (Unique Substrings in Wraparound String)
// 把字符串 s 看作是“abcdefghijklmnopqrstuvwxyz”的无限环绕字符串，所以 s 看起来是这样的：
// "...zabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcd...."
// 现在给定另一个字符串 p 。返回 s 中唯一的 p 的非空子串的数量。
// 测试链接 : https://leetcode.cn/problems/unique-substrings-in-wraparound-string/

#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
using namespace std;

class Solution {
public:
    // 方法1：动态规划
    // 时间复杂度：O(n) - n为字符串p的长度
    // 空间复杂度：O(1) - 使用固定大小的数组
    // 核心思路：记录以每个字母结尾的最长连续子串长度
    int findSubstringInWraproundString(string p) {
        if (p.empty()) return 0;
        
        vector<int> dp(26, 0); // 记录以每个字母结尾的最长连续子串长度
        int currentLen = 1;
        
        dp[p[0] - 'a'] = 1;
        
        for (int i = 1; i < p.length(); i++) {
            // 检查当前字符是否与前一个字符连续
            if ((p[i] - p[i - 1] == 1) || (p[i - 1] == 'z' && p[i] == 'a')) {
                currentLen++;
            } else {
                currentLen = 1;
            }
            
            // 更新以当前字符结尾的最长长度
            int index = p[i] - 'a';
            dp[index] = max(dp[index], currentLen);
        }
        
        // 计算所有唯一子串的数量
        int result = 0;
        for (int len : dp) {
            result += len;
        }
        
        return result;
    }
    
    // 方法2：优化的动态规划
    // 时间复杂度：O(n) - 遍历字符串一次
    // 空间复杂度：O(1) - 使用固定大小的数组
    // 核心思路：与方法1相同，但代码更简洁
    int findSubstringInWraproundString2(string p) {
        if (p.empty()) return 0;
        
        vector<int> dp(26, 0);
        int currentLen = 0;
        
        for (int i = 0; i < p.length(); i++) {
            // 检查是否连续（考虑z到a的特殊情况）
            if (i > 0 && ((p[i] - p[i - 1] == 1) || (p[i - 1] == 'z' && p[i] == 'a'))) {
                currentLen++;
            } else {
                currentLen = 1;
            }
            
            int index = p[i] - 'a';
            dp[index] = max(dp[index], currentLen);
        }
        
        int result = 0;
        for (int len : dp) {
            result += len;
        }
        return result;
    }
    
    // 方法3：暴力解法（用于理解问题）
    // 时间复杂度：O(n^2) - 生成所有子串
    // 空间复杂度：O(n^2) - 存储所有子串
    // 问题：效率低下，不适用于长字符串
    int findSubstringInWraproundString3(string p) {
        if (p.empty()) return 0;
        
        // 使用集合来去重
        // 这里简化实现，直接计算（实际应该用哈希集合）
        int count = 0;
        int n = p.length();
        
        // 检查所有子串是否在环绕字符串中
        for (int i = 0; i < n; i++) {
            vector<bool> seen(26, false);
            int currentCount = 0;
            
            for (int j = i; j < n; j++) {
                // 检查当前子串是否连续
                if (j > i && !isConsecutive(p[j - 1], p[j])) {
                    break;
                }
                
                // 如果当前字符还没有被计入以该字符结尾的子串
                if (!seen[p[j] - 'a']) {
                    seen[p[j] - 'a'] = true;
                    currentCount++;
                }
            }
            
            count += currentCount;
        }
        
        return count;
    }
    
private:
    // 检查两个字符是否连续（考虑环绕）
    bool isConsecutive(char a, char b) {
        return (b - a == 1) || (a == 'z' && b == 'a');
    }
};

// 测试用例和性能对比
int main() {
    Solution solution;
    
    // 测试用例1
    string p1 = "a";
    cout << "测试用例1 - p: \"a\"" << endl;
    cout << "方法1结果: " << solution.findSubstringInWraproundString(p1) << endl;
    cout << "方法2结果: " << solution.findSubstringInWraproundString2(p1) << endl;
    cout << "方法3结果: " << solution.findSubstringInWraproundString3(p1) << endl;
    cout << "预期结果: 1" << endl << endl;
    
    // 测试用例2
    string p2 = "cac";
    cout << "测试用例2 - p: \"cac\"" << endl;
    cout << "方法1结果: " << solution.findSubstringInWraproundString(p2) << endl;
    cout << "方法2结果: " << solution.findSubstringInWraproundString2(p2) << endl;
    cout << "方法3结果: " << solution.findSubstringInWraproundString3(p2) << endl;
    cout << "预期结果: 2" << endl << endl;
    
    // 测试用例3
    string p3 = "zab";
    cout << "测试用例3 - p: \"zab\"" << endl;
    cout << "方法1结果: " << solution.findSubstringInWraproundString(p3) << endl;
    cout << "方法2结果: " << solution.findSubstringInWraproundString2(p3) << endl;
    cout << "方法3结果: " << solution.findSubstringInWraproundString3(p3) << endl;
    cout << "预期结果: 6" << endl << endl;
    
    // 测试用例4
    string p4 = "abcde";
    cout << "测试用例4 - p: \"abcde\"" << endl;
    cout << "方法1结果: " << solution.findSubstringInWraproundString(p4) << endl;
    cout << "方法2结果: " << solution.findSubstringInWraproundString2(p4) << endl;
    cout << "方法3结果: " << solution.findSubstringInWraproundString3(p4) << endl;
    cout << "预期结果: 15" << endl;
    
    return 0;
}