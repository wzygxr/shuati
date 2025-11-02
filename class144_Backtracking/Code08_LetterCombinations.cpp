#include <vector>
#include <string>
#include <iostream>

using namespace std;

/**
 * LeetCode 17. 电话号码的字母组合
 * 
 * 题目描述：
 * 给定一个仅包含数字 2-9 的字符串，返回所有它能表示的字母组合。
 * 答案可以按任意顺序返回。
 * 
 * 示例：
 * 输入：digits = "23"
 * 输出：["ad","ae","af","bd","be","bf","cd","ce","cf"]
 * 
 * 输入：digits = ""
 * 输出：[]
 * 
 * 输入：digits = "2"
 * 输出：["a","b","c"]
 * 
 * 提示：
 * 0 <= digits.length <= 4
 * digits[i] 是范围 ['2', '9'] 的一个数字。
 * 
 * 链接：https://leetcode.cn/problems/letter-combinations-of-a-phone-number/
 */

class Solution {
public:
    /**
     * 生成电话号码的字母组合
     * 
     * 算法思路：
     * 1. 使用回溯算法生成所有可能的字母组合
     * 2. 建立数字到字母的映射关系
     * 3. 对于每个数字，遍历其对应的所有字母
     * 4. 递归处理下一个数字，直到处理完所有数字
     * 
     * 时间复杂度：O(3^m * 4^n)，其中m是对应3个字母的数字个数，n是对应4个字母的数字个数
     * 空间复杂度：O(3^m * 4^n)，用于存储所有组合
     * 
     * @param digits 输入的数字字符串
     * @return 所有可能的字母组合
     */
    vector<string> letterCombinations(string digits) {
        vector<string> result;
        // 边界条件：空字符串
        if (digits.empty()) return result;
        
        // 数字到字母的映射
        vector<string> mapping = {"0", "1", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};
        
        // 回溯生成所有组合
        backtrack(digits, mapping, result, "", 0);
        return result;
    }

private:
    /**
     * 回溯函数生成字母组合
     * 
     * @param digits 输入的数字字符串
     * @param mapping 数字到字母的映射数组
     * @param result 结果列表
     * @param current 当前已生成的字符串
     * @param index 当前处理的数字索引
     */
    void backtrack(const string& digits, const vector<string>& mapping, vector<string>& result, string current, int index) {
        // 终止条件：已处理完所有数字
        if (index == digits.length()) {
            result.push_back(current);
            return;
        }
        
        // 获取当前数字对应的字母
        int digit = digits[index] - '0';
        string letters = mapping[digit];
        
        // 遍历所有可能的字母
        for (char letter : letters) {
            // 递归处理下一个数字
            backtrack(digits, mapping, result, current + letter, index + 1);
        }
    }
};

// 测试方法
int main() {
    Solution solution;
    
    // 测试用例1
    string test1 = "23";
    vector<string> result1 = solution.letterCombinations(test1);
    cout << "输入: \"" << test1 << "\"" << endl;
    cout << "输出: [";
    for (int i = 0; i < result1.size(); i++) {
        cout << "\"" << result1[i] << "\"";
        if (i < result1.size() - 1) cout << ", ";
    }
    cout << "]" << endl;
    
    // 测试用例2
    string test2 = "";
    vector<string> result2 = solution.letterCombinations(test2);
    cout << "\n输入: \"" << test2 << "\"" << endl;
    cout << "输出: [";
    for (int i = 0; i < result2.size(); i++) {
        cout << "\"" << result2[i] << "\"";
        if (i < result2.size() - 1) cout << ", ";
    }
    cout << "]" << endl;
    
    // 测试用例3
    string test3 = "2";
    vector<string> result3 = solution.letterCombinations(test3);
    cout << "\n输入: \"" << test3 << "\"" << endl;
    cout << "输出: [";
    for (int i = 0; i < result3.size(); i++) {
        cout << "\"" << result3[i] << "\"";
        if (i < result3.size() - 1) cout << ", ";
    }
    cout << "]" << endl;
    
    // 测试用例4
    string test4 = "234";
    vector<string> result4 = solution.letterCombinations(test4);
    cout << "\n输入: \"" << test4 << "\"" << endl;
    cout << "输出: [";
    for (int i = 0; i < result4.size(); i++) {
        cout << "\"" << result4[i] << "\"";
        if (i < result4.size() - 1) cout << ", ";
    }
    cout << "]" << endl;
    
    return 0;
}