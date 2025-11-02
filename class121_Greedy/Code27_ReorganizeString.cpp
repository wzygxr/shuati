#include <iostream>
#include <vector>
#include <queue>
#include <string>
#include <unordered_map>
#include <algorithm>
using namespace std;

// 重构字符串
// 给定一个字符串 s ，检查是否能重新排布其中的字母，使得两相邻的字符不同。
// 如果可以，输出任意可行的结果。如果不可行，返回空字符串。
// 测试链接: https://leetcode.cn/problems/reorganize-string/

class Solution {
public:
    /**
     * 重构字符串问题的贪心解法
     * 
     * 解题思路：
     * 1. 统计每个字符的出现频率
     * 2. 使用最大堆存储字符及其频率，按频率降序排列
     * 3. 每次从堆中取出频率最高的两个字符，交替放置
     * 4. 如果某个字符频率过高，无法重构，返回空字符串
     * 
     * 贪心策略的正确性：
     * 局部最优：每次选择频率最高的两个字符交替放置
     * 全局最优：得到相邻字符不同的字符串
     * 
     * 时间复杂度：O(n log k)，其中k是字符种类数
     * 空间复杂度：O(k)，用于存储字符频率的堆
     * 
     * @param s 输入字符串
     * @return 重构后的字符串或空字符串
     */
    string reorganizeString(string s) {
        // 边界条件处理
        if (s.empty()) return "";
        
        // 统计字符频率
        vector<int> freq(26, 0);
        for (char c : s) {
            freq[c - 'a']++;
        }
        
        // 使用最大堆存储字符频率（按频率降序排列）
        priority_queue<pair<int, char>> maxHeap;
        
        // 将字符及其频率加入堆中
        for (int i = 0; i < 26; i++) {
            if (freq[i] > 0) {
                maxHeap.push({freq[i], 'a' + i});
            }
        }
        
        // 如果最高频率超过一半加一，无法重构
        int maxFreq = maxHeap.top().first;
        if (maxFreq > (s.length() + 1) / 2) {
            return "";
        }
        
        // 构建结果字符串
        string result = "";
        
        while (maxHeap.size() >= 2) {
            // 取出频率最高的两个字符
            auto first = maxHeap.top(); maxHeap.pop();
            auto second = maxHeap.top(); maxHeap.pop();
            
            // 交替放置这两个字符
            result += first.second;
            result += second.second;
            
            // 减少频率并重新加入堆中
            if (--first.first > 0) {
                maxHeap.push(first);
            }
            if (--second.first > 0) {
                maxHeap.push(second);
            }
        }
        
        // 处理最后一个字符（如果有）
        if (!maxHeap.empty()) {
            auto last = maxHeap.top();
            result += last.second;
        }
        
        return result;
    }

    /**
     * 重构字符串问题的另一种解法（基于奇偶位置）
     * 
     * 解题思路：
     * 1. 统计字符频率，找到最高频率字符
     * 2. 如果最高频率超过一半加一，无法重构
     * 3. 将最高频率字符放在偶数位置，其他字符放在奇数位置
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    string reorganizeString2(string s) {
        if (s.empty()) return "";
        
        // 统计字符频率
        vector<int> freq(26, 0);
        for (char c : s) {
            freq[c - 'a']++;
        }
        
        // 找到最高频率字符
        int maxFreq = 0;
        char maxChar = 'a';
        for (int i = 0; i < 26; i++) {
            if (freq[i] > maxFreq) {
                maxFreq = freq[i];
                maxChar = 'a' + i;
            }
        }
        
        // 检查是否可重构
        if (maxFreq > (s.length() + 1) / 2) {
            return "";
        }
        
        // 创建结果数组
        vector<char> result(s.length(), ' ');
        int index = 0;
        
        // 先放置最高频率字符在偶数位置
        while (freq[maxChar - 'a'] > 0) {
            result[index] = maxChar;
            index += 2;
            freq[maxChar - 'a']--;
            
            // 如果偶数位置用完，转到奇数位置
            if (index >= s.length()) {
                index = 1;
            }
        }
        
        // 放置其他字符
        for (int i = 0; i < 26; i++) {
            while (freq[i] > 0) {
                if (index >= s.length()) {
                    index = 1;
                }
                result[index] = 'a' + i;
                index += 2;
                freq[i]--;
            }
        }
        
        return string(result.begin(), result.end());
    }
};

// 测试函数
void testReorganizeString() {
    Solution solution;
    
    // 测试用例1
    // 输入: s = "aab"
    // 输出: "aba"
    string s1 = "aab";
    cout << "测试用例1结果: " << solution.reorganizeString(s1) << endl; // 期望输出: "aba"
    
    // 测试用例2
    // 输入: s = "aaab"
    // 输出: ""
    // 解释: 无法重构，因为'a'出现次数过多
    string s2 = "aaab";
    cout << "测试用例2结果: " << solution.reorganizeString(s2) << endl; // 期望输出: ""
    
    // 测试用例3
    // 输入: s = "vvvlo"
    // 输出: "vlvov" 或 "vovlv"
    string s3 = "vvvlo";
    string result3 = solution.reorganizeString(s3);
    cout << "测试用例3结果: " << result3 << endl; // 期望输出非空
    cout << "测试用例3长度验证: " << (result3.length() == s3.length() ? "通过" : "失败") << endl;
    
    // 测试用例4：边界情况
    // 输入: s = "a"
    // 输出: "a"
    string s4 = "a";
    cout << "测试用例4结果: " << solution.reorganizeString(s4) << endl; // 期望输出: "a"
    
    // 测试用例5：复杂情况
    // 输入: s = "abbcccdddd"
    // 输出: 非空字符串
    string s5 = "abbcccdddd";
    string result5 = solution.reorganizeString(s5);
    cout << "测试用例5结果: " << result5 << endl; // 期望输出非空
    cout << "测试用例5长度验证: " << (result5.length() == s5.length() ? "通过" : "失败") << endl;
    
    // 测试用例6：极限情况
    // 输入: s = "aaaaabbbcc"
    // 输出: 非空字符串
    string s6 = "aaaaabbbcc";
    string result6 = solution.reorganizeString(s6);
    cout << "测试用例6结果: " << result6 << endl; // 期望输出非空
    cout << "测试用例6长度验证: " << (result6.length() == s6.length() ? "通过" : "失败") << endl;
}

int main() {
    testReorganizeString();
    return 0;
}