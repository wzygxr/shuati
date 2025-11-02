#include <vector>
#include <string>
#include <iostream>

using namespace std;

/**
 * LeetCode 131. 分割回文串
 * 
 * 题目描述：
 * 给定一个字符串 s，将 s 分割成一些子串，使每个子串都是回文串。返回 s 所有可能的分割方案。
 * 
 * 示例：
 * 输入：s = "aab"
 * 输出：[["a","a","b"],["aa","b"]]
 * 
 * 输入：s = "a"
 * 输出：[["a"]]
 * 
 * 提示：
 * 1 <= s.length <= 16
 * s 仅由小写英文字母组成
 * 
 * 链接：https://leetcode.cn/problems/palindrome-partitioning/
 */

class Solution {
public:
    /**
     * 分割回文串
     * 
     * 算法思路：
     * 1. 使用回溯算法生成所有可能的分割方案
     * 2. 对于每个位置，判断从当前位置开始的子串是否为回文串
     * 3. 如果是回文串，则将其加入路径，并递归处理剩余部分
     * 4. 回溯时移除当前子串，尝试其他分割方式
     * 
     * 时间复杂度：O(N * 2^N)，其中N是字符串长度。在最坏情况下，每个字符都可以单独作为回文串，共有O(2^N)种分割方案，每种方案需要O(N)时间检查回文。
     * 空间复杂度：O(N)，递归栈的深度加上存储当前路径的空间。
     * 
     * @param s 输入字符串
     * @return 所有可能的分割方案
     */
    vector<vector<string>> partition(string s) {
        vector<vector<string>> result;
        vector<string> path;
        
        // 回溯生成所有分割方案
        backtrack(s, 0, path, result);
        return result;
    }

private:
    /**
     * 回溯函数生成分割方案
     * 
     * @param s 输入字符串
     * @param start 当前处理的起始位置
     * @param path 当前分割路径
     * @param result 结果列表
     */
    void backtrack(const string& s, int start, vector<string>& path, vector<vector<string>>& result) {
        // 终止条件：已处理到字符串末尾
        if (start == s.size()) {
            result.push_back(path);
            return;
        }
        
        // 从start开始尝试不同长度的子串
        for (int end = start + 1; end <= s.size(); end++) {
            // 判断子串s[start...end-1]是否为回文串
            if (isPalindrome(s, start, end - 1)) {
                // 将回文子串加入路径
                path.push_back(s.substr(start, end - start));
                // 递归处理剩余部分
                backtrack(s, end, path, result);
                // 回溯：移除当前子串
                path.pop_back();
            }
        }
    }

    /**
     * 判断字符串的子串是否为回文串
     * 
     * @param s 原始字符串
     * @param left 左边界（包含）
     * @param right 右边界（包含）
     * @return 是否为回文串
     */
    bool isPalindrome(const string& s, int left, int right) {
        while (left < right) {
            if (s[left++] != s[right--]) {
                return false;
            }
        }
        return true;
    }
};

// 辅助函数：打印结果
void printResult(const vector<vector<string>>& result) {
    cout << "[";
    for (size_t i = 0; i < result.size(); i++) {
        cout << "[";
        for (size_t j = 0; j < result[i].size(); j++) {
            cout << "\"" << result[i][j] << "\"";
            if (j < result[i].size() - 1) {
                cout << ", ";
            }
        }
        cout << "]";
        if (i < result.size() - 1) {
            cout << ", ";
        }
    }
    cout << "]" << endl;
}

// 测试方法
int main() {
    Solution solution;
    
    // 测试用例1
    string test1 = "aab";
    vector<vector<string>> result1 = solution.partition(test1);
    cout << "输入: \"" << test1 << "\"" << endl;
    cout << "输出: ";
    printResult(result1);
    
    // 测试用例2
    string test2 = "a";
    vector<vector<string>> result2 = solution.partition(test2);
    cout << "\n输入: \"" << test2 << "\"" << endl;
    cout << "输出: ";
    printResult(result2);
    
    // 测试用例3
    string test3 = "aabb";
    vector<vector<string>> result3 = solution.partition(test3);
    cout << "\n输入: \"" << test3 << "\"" << endl;
    cout << "输出: ";
    printResult(result3);
    
    return 0;
}