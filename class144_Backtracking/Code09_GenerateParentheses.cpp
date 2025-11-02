#include <vector>
#include <string>
#include <iostream>

using namespace std;

/**
 * LeetCode 22. 括号生成
 * 
 * 题目描述：
 * 数字 n 代表生成括号的对数，请你设计一个函数，用于能够生成所有可能的并且有效的括号组合。
 * 
 * 示例：
 * 输入：n = 3
 * 输出：["((()))","(()())","(())()","()(())","()()()"]
 * 
 * 输入：n = 1
 * 输出：["()"]
 * 
 * 提示：
 * 1 <= n <= 8
 * 
 * 链接：https://leetcode.cn/problems/generate-parentheses/
 */

class Solution {
public:
    /**
     * 生成所有可能的有效括号组合
     * 
     * 算法思路：
     * 1. 使用回溯算法生成有效括号组合
     * 2. 维护左右括号的数量，确保生成的括号始终有效
     * 3. 左括号数量不能超过n
     * 4. 右括号数量不能超过左括号数量
     * 
     * 时间复杂度：O(4^n / sqrt(n))，第n个卡塔兰数
     * 空间复杂度：O(4^n / sqrt(n))，用于存储所有组合
     * 
     * @param n 括号对数
     * @return 所有可能的有效括号组合
     */
    vector<string> generateParenthesis(int n) {
        vector<string> result;
        backtrack(result, "", 0, 0, n);
        return result;
    }

private:
    /**
     * 回溯函数生成有效括号组合
     * 
     * @param result 结果列表
     * @param current 当前已生成的字符串
     * @param open 已使用的左括号数量
     * @param close 已使用的右括号数量
     * @param max 括号对数
     */
    void backtrack(vector<string>& result, string current, int open, int close, int max) {
        // 终止条件：已生成2*max个字符
        if (current.length() == max * 2) {
            result.push_back(current);
            return;
        }
        
        // 添加左括号（左括号数量小于max时）
        if (open < max)
            backtrack(result, current + "(", open + 1, close, max);
        
        // 添加右括号（右括号数量小于左括号数量时）
        if (close < open)
            backtrack(result, current + ")", open, close + 1, max);
    }
};

// 测试方法
int main() {
    Solution solution;
    
    // 测试用例1
    int n1 = 3;
    vector<string> result1 = solution.generateParenthesis(n1);
    cout << "输入: n = " << n1 << endl;
    cout << "输出: [";
    for (int i = 0; i < result1.size(); i++) {
        cout << "\"" << result1[i] << "\"";
        if (i < result1.size() - 1) cout << ", ";
    }
    cout << "]" << endl;
    
    // 测试用例2
    int n2 = 1;
    vector<string> result2 = solution.generateParenthesis(n2);
    cout << "\n输入: n = " << n2 << endl;
    cout << "输出: [";
    for (int i = 0; i < result2.size(); i++) {
        cout << "\"" << result2[i] << "\"";
        if (i < result2.size() - 1) cout << ", ";
    }
    cout << "]" << endl;
    
    // 测试用例3
    int n3 = 2;
    vector<string> result3 = solution.generateParenthesis(n3);
    cout << "\n输入: n = " << n3 << endl;
    cout << "输出: [";
    for (int i = 0; i < result3.size(); i++) {
        cout << "\"" << result3[i] << "\"";
        if (i < result3.size() - 1) cout << ", ";
    }
    cout << "]" << endl;
    
    return 0;
}