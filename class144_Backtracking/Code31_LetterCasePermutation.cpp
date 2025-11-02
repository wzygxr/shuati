/*
LeetCode 784. 字母大小写全排列

给定一个字符串S，通过将字符串S中的每个字母转变大小写，我们可以获得一个新的字符串。
返回所有可能得到的字符串集合。以任意顺序返回输出。

算法思路：
使用回溯算法遍历字符串中的每个字符，对于字母字符，分别尝试大写和小写两种情况。

时间复杂度：O(2^n * n)，其中n是字符串中字母的个数
空间复杂度：O(2^n * n)，用于存储所有可能的字符串
*/

#include <vector>
#include <string>
#include <iostream>
#include <cctype>
using namespace std;

class Solution {
public:
    /**
     * 返回所有可能得到的字符串集合
     * @param s 输入字符串
     * @return 所有可能的字符串集合
     */
    vector<string> letterCasePermutation(string s) {
        vector<string> result;
        backtrack(s, 0, result);
        return result;
    }
    
private:
    /**
     * 回溯函数
     * @param s 字符串
     * @param index 当前处理的字符位置
     * @param result 结果列表
     */
    void backtrack(string s, int index, vector<string>& result) {
        // 终止条件：处理完所有字符
        if (index == s.length()) {
            result.push_back(s);
            return;
        }
        
        char ch = s[index];
        
        // 如果是字母，则分别尝试大写和小写
        if (isalpha(ch)) {
            // 尝试小写
            s[index] = tolower(ch);
            backtrack(s, index + 1, result);
            
            // 尝试大写
            s[index] = toupper(ch);
            backtrack(s, index + 1, result);
        } else {
            // 如果不是字母，直接处理下一个字符
            backtrack(s, index + 1, result);
        }
    }
};

// 测试代码
int main() {
    Solution solution;
    
    // 测试用例1
    cout << "Input: \"a1b2\"" << endl;
    vector<string> result1 = solution.letterCasePermutation("a1b2");
    for (const string& str : result1) {
        cout << str << " ";
    }
    cout << endl;
    
    // 测试用例2
    cout << "\nInput: \"3z4\"" << endl;
    vector<string> result2 = solution.letterCasePermutation("3z4");
    for (const string& str : result2) {
        cout << str << " ";
    }
    cout << endl;
    
    // 测试用例3
    cout << "\nInput: \"12345\"" << endl;
    vector<string> result3 = solution.letterCasePermutation("12345");
    for (const string& str : result3) {
        cout << str << " ";
    }
    cout << endl;
    
    return 0;
}