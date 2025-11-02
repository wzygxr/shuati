/**
 * 哈希算法经典题目集合 - C++版本
 * 
 * 本文件包含各大算法平台（LeetCode、Codeforces、剑指Offer等）的哈希相关经典题目
 * 每个题目都提供详细的注释、复杂度分析和多种解法
 * 
 * 哈希算法核心思想：
 * 1. 使用哈希表实现O(1)时间复杂度的查找、插入和删除
 * 2. 处理哈希冲突的方法：链地址法、开放地址法、再哈希法等
 * 3. 哈希函数设计原则：均匀分布、计算简单、冲突率低
 * 
 * 时间复杂度分析：
 * - 理想情况下：O(1) 查找、插入、删除
 * - 最坏情况下：O(n) 当所有元素哈希到同一位置时
 * 
 * 空间复杂度分析：
 * - 哈希表：O(n) 存储n个元素
 * - 额外空间：O(k) 用于存储辅助信息
 */

#include <iostream>
#include <vector>
#include <unordered_map>
#include <unordered_set>
#include <algorithm>
#include <string>
#include <map>
#include <set>
#include <queue>
#include <stack>
#include <functional>
#include <random>
#include <stdexcept>
#include <utility>
#include <exception>
#include <functional>
#include <sstream>
#include <iomanip>

using namespace std;

/**
 * LeetCode 1. 两数之和 (Two Sum)
 * 题目来源：https://leetcode.com/problems/two-sum/
 * 题目描述：给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出和为目标值 target 的那两个整数，并返回它们的数组下标。
 * 
 * 算法思路：
 * 1. 使用哈希表存储每个数字及其对应的索引
 * 2. 遍历数组时检查 target - nums[i] 是否在哈希表中
 * 3. 如果存在，则返回两个索引
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 */
class TwoSumSolution {
public:
    vector<int> twoSum(vector<int>& nums, int target) {
        // 输入验证
        if (nums.size() < 2) {
            throw invalid_argument("数组长度必须大于等于2");
        }
        
        unordered_map<int, int> numMap;
        
        for (int i = 0; i < nums.size(); i++) {
            int complement = target - nums[i];
            
            // 检查补数是否在哈希表中
            if (numMap.find(complement) != numMap.end()) {
                return {numMap[complement], i};
            }
            
            // 将当前数字和索引存入哈希表
            numMap[nums[i]] = i;
        }
        
        throw invalid_argument("没有找到符合条件的两个数");
    }
    
    /**
     * 两数之和的暴力解法（用于对比）
     * 时间复杂度：O(n²)
     * 空间复杂度：O(1)
     */
    vector<int> twoSumBruteForce(vector<int>& nums, int target) {
        for (int i = 0; i < nums.size(); i++) {
            for (int j = i + 1; j < nums.size(); j++) {
                if (nums[i] + nums[j] == target) {
                    return {i, j};
                }
            }
        }
        throw invalid_argument("没有找到符合条件的两个数");
    }
    
    /**
     * 两数之和的排序双指针解法
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(n)
     */
    vector<int> twoSumTwoPointers(vector<int>& nums, int target) {
        // 创建索引数组
        vector<pair<int, int>> indexedNums;
        for (int i = 0; i < nums.size(); i++) {
            indexedNums.push_back({nums[i], i});
        }
        
        // 按数值排序
        sort(indexedNums.begin(), indexedNums.end());
        
        int left = 0, right = nums.size() - 1;
        while (left < right) {
            int sum = indexedNums[left].first + indexedNums[right].first;
            
            if (sum == target) {
                return {indexedNums[left].second, indexedNums[right].second};
            } else if (sum < target) {
                left++;
            } else {
                right--;
            }
        }
        
        throw invalid_argument("没有找到符合条件的两个数");
    }
};

/**
 * LeetCode 49. 字母异位词分组 (Group Anagrams)
 * 题目来源：https://leetcode.com/problems/group-anagrams/
 * 题目描述：给你一个字符串数组，请你将字母异位词组合在一起。可以按任意顺序返回结果列表。
 * 
 * 算法思路：
 * 1. 使用排序后的字符串作为哈希表的键
 * 2. 将具有相同排序字符串的单词分组
 * 3. 返回分组后的结果
 * 
 * 时间复杂度：O(n * k log k)，其中n是字符串数量，k是字符串最大长度
 * 空间复杂度：O(n * k)
 */
class GroupAnagramsSolution {
public:
    vector<vector<string>> groupAnagrams(vector<string>& strs) {
        if (strs.empty()) {
            return {};
        }
        
        unordered_map<string, vector<string>> anagramMap;
        
        for (const string& str : strs) {
            // 将字符串排序作为键
            string sortedStr = str;
            sort(sortedStr.begin(), sortedStr.end());
            
            // 添加到对应的分组
            anagramMap[sortedStr].push_back(str);
        }
        
        vector<vector<string>> result;
        for (auto& pair : anagramMap) {
            result.push_back(pair.second);
        }
        
        return result;
    }
    
    /**
     * 使用字符计数作为键的优化版本
     * 时间复杂度：O(n * k)
     * 空间复杂度：O(n * k)
     */
    vector<vector<string>> groupAnagramsOptimized(vector<string>& strs) {
        if (strs.empty()) {
            return {};
        }
        
        unordered_map<string, vector<string>> anagramMap;
        
        for (const string& str : strs) {
            // 使用字符计数作为键
            string key = getCharacterCountKey(str);
            
            anagramMap[key].push_back(str);
        }
        
        vector<vector<string>> result;
        for (auto& pair : anagramMap) {
            result.push_back(pair.second);
        }
        
        return result;
    }
    
private:
    string getCharacterCountKey(const string& str) {
        vector<int> count(26, 0);
        for (char c : str) {
            count[c - 'a']++;
        }
        
        stringstream keyBuilder;
        for (int i = 0; i < 26; i++) {
            if (count[i] > 0) {
                keyBuilder << char('a' + i) << count[i];
            }
        }
        return keyBuilder.str();
    }
};

/**
 * LeetCode 242. 有效的字母异位词 (Valid Anagram)
 * 题目来源：https://leetcode.com/problems/valid-anagram/
 * 题目描述：给定两个字符串 s 和 t ，编写一个函数来判断 t 是否是 s 的字母异位词。
 * 
 * 算法思路：
 * 1. 使用哈希表统计每个字符出现的次数
 * 2. 比较两个字符串的字符频率
 * 3. 如果所有字符频率相同，则是字母异位词
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(1)，因为字符集大小固定为26
 */
class ValidAnagramSolution {
public:
    bool isAnagram(string s, string t) {
        if (s.length() != t.length()) {
            return false;
        }
        
        vector<int> charCount(26, 0);
        
        // 统计字符串s的字符频率
        for (char c : s) {
            charCount[c - 'a']++;
        }
        
        // 减去字符串t的字符频率
        for (char c : t) {
            charCount[c - 'a']--;
            // 如果出现负数，说明t中某个字符比s中多
            if (charCount[c - 'a'] < 0) {
                return false;
            }
        }
        
        // 检查所有字符频率是否归零
        for (int count : charCount) {
            if (count != 0) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 使用排序的解法
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(n)
     */
    bool isAnagramSort(string s, string t) {
        if (s.length() != t.length()) {
            return false;
        }
        
        sort(s.begin(), s.end());
        sort(t.begin(), t.end());
        
        return s == t;
    }
    
    /**
     * 使用哈希表的通用解法（支持Unicode字符）
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    bool isAnagramUnicode(string s, string t) {
        if (s.length() != t.length()) {
            return false;
        }
        
        unordered_map<char, int> charMap;
        
        // 统计字符串s的字符频率
        for (char c : s) {
            charMap[c]++;
        }
        
        // 减去字符串t的字符频率
        for (char c : t) {
            if (charMap.find(c) == charMap.end()) {
                return false;
            }
            charMap[c]--;
            if (charMap[c] == 0) {
                charMap.erase(c);
            }
        }
        
        return charMap.empty();
    }
};

/**
 * LeetCode 3. 无重复字符的最长子串 (Longest Substring Without Repeating Characters)
 * 题目来源：https://leetcode.com/problems/longest-substring-without-repeating-characters/
 * 题目描述：给定一个字符串 s ，请你找出其中不含有重复字符的最长子串的长度。
 * 
 * 算法思路：
 * 1. 使用滑动窗口和哈希表记录字符最后出现的位置
 * 2. 维护左右指针，右指针遍历字符串
 * 3. 当遇到重复字符时，移动左指针到重复字符的下一个位置
 * 4. 更新最大长度
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(min(m, n))，其中m是字符集大小
 */
class LongestSubstringSolution {
public:
    int lengthOfLongestSubstring(string s) {
        if (s.empty()) {
            return 0;
        }
        
        unordered_map<char, int> charIndexMap;
        int maxLength = 0;
        int left = 0;
        
        for (int right = 0; right < s.length(); right++) {
            char currentChar = s[right];
            
            // 如果字符已经存在，并且索引在窗口内
            if (charIndexMap.find(currentChar) != charIndexMap.end() && charIndexMap[currentChar] >= left) {
                // 移动左指针到重复字符的下一个位置
                left = charIndexMap[currentChar] + 1;
            }
            
            // 更新字符的最新位置
            charIndexMap[currentChar] = right;
            
            // 更新最大长度
            maxLength = max(maxLength, right - left + 1);
        }
        
        return maxLength;
    }
    
    /**
     * 使用数组代替哈希表的优化版本（仅适用于ASCII字符）
     * 时间复杂度：O(n)
     * 空间复杂度：O(128)
     */
    int lengthOfLongestSubstringArray(string s) {
        if (s.empty()) {
            return 0;
        }
        
        vector<int> charIndex(128, -1); // ASCII字符集
        int maxLength = 0;
        int left = 0;
        
        for (int right = 0; right < s.length(); right++) {
            char currentChar = s[right];
            
            // 如果字符已经存在，并且索引在窗口内
            if (charIndex[currentChar] >= left) {
                left = charIndex[currentChar] + 1;
            }
            
            // 更新字符的最新位置
            charIndex[currentChar] = right;
            
            // 更新最大长度
            maxLength = max(maxLength, right - left + 1);
        }
        
        return maxLength;
    }
};

/**
 * LeetCode 560. 和为K的子数组 (Subarray Sum Equals K)
 * 题目来源：https://leetcode.com/problems/subarray-sum-equals-k/
 * 题目描述：给你一个整数数组 nums 和一个整数 k ，请你统计并返回该数组中和为 k 的连续子数组的个数。
 * 
 * 算法思路：
 * 1. 使用前缀和和哈希表
 * 2. 记录每个前缀和出现的次数
 * 3. 对于当前前缀和sum，检查sum - k是否在哈希表中
 * 4. 如果存在，则说明存在子数组和为k
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 */
class SubarraySumSolution {
public:
    int subarraySum(vector<int>& nums, int k) {
        if (nums.empty()) {
            return 0;
        }
        
        unordered_map<int, int> prefixSumCount;
        prefixSumCount[0] = 1; // 前缀和为0出现1次
        
        int count = 0;
        int prefixSum = 0;
        
        for (int num : nums) {
            prefixSum += num;
            
            // 检查是否存在前缀和等于prefixSum - k
            if (prefixSumCount.find(prefixSum - k) != prefixSumCount.end()) {
                count += prefixSumCount[prefixSum - k];
            }
            
            // 更新当前前缀和的出现次数
            prefixSumCount[prefixSum]++;
        }
        
        return count;
    }
    
    /**
     * 暴力解法（用于对比）
     * 时间复杂度：O(n²)
     * 空间复杂度：O(1)
     */
    int subarraySumBruteForce(vector<int>& nums, int k) {
        int count = 0;
        for (int i = 0; i < nums.size(); i++) {
            int sum = 0;
            for (int j = i; j < nums.size(); j++) {
                sum += nums[j];
                if (sum == k) {
                    count++;
                }
            }
        }
        return count;
    }
};

/**
 * 剑指Offer 50. 第一个只出现一次的字符
 * 题目来源：剑指Offer面试题50
 * 题目描述：在字符串s中找出第一个只出现一次的字符
 * 
 * 算法思路：
 * 1. 使用哈希表统计每个字符出现的次数
 * 2. 再次遍历字符串，找到第一个出现次数为1的字符
 * 3. 返回该字符
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(1)，因为字符集大小固定
 */
class FirstUniqueCharSolution {
public:
    char firstUniqChar(string s) {
        if (s.empty()) {
            return ' ';
        }
        
        vector<int> charCount(256, 0); // 扩展ASCII字符集
        
        // 第一次遍历：统计字符频率
        for (char c : s) {
            charCount[c]++;
        }
        
        // 第二次遍历：找到第一个唯一字符
        for (char c : s) {
            if (charCount[c] == 1) {
                return c;
            }
        }
        
        return ' ';
    }
    
    /**
     * 使用有序哈希表保持插入顺序的解法
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    char firstUniqCharOrderedMap(string s) {
        if (s.empty()) {
            return ' ';
        }
        
        map<char, int> charMap;
        
        // 统计字符频率
        for (char c : s) {
            charMap[c]++;
        }
        
        // 找到第一个出现次数为1的字符
        for (char c : s) {
            if (charMap[c] == 1) {
                return c;
            }
        }
        
        return ' ';
    }
};

/**
 * 单元测试函数
 */
void testHashAlgorithmProblems() {
    cout << "=== 哈希算法经典题目测试 ===" << endl << endl;
    
    // 测试两数之和
    cout << "1. 两数之和测试:" << endl;
    TwoSumSolution twoSum;
    vector<int> nums1 = {2, 7, 11, 15};
    int target1 = 9;
    vector<int> result1 = twoSum.twoSum(nums1, target1);
    cout << "数组: [";
    for (int i = 0; i < nums1.size(); i++) {
        cout << nums1[i] << (i < nums1.size() - 1 ? ", " : "]");
    }
    cout << ", 目标: " << target1 << endl;
    cout << "结果: [" << result1[0] << ", " << result1[1] << "]" << endl;
    
    // 测试字母异位词分组
    cout << endl << "2. 字母异位词分组测试:" << endl;
    GroupAnagramsSolution groupAnagrams;
    vector<string> strs = {"eat", "tea", "tan", "ate", "nat", "bat"};
    vector<vector<string>> anagramGroups = groupAnagrams.groupAnagrams(strs);
    cout << "输入: [";
    for (int i = 0; i < strs.size(); i++) {
        cout << "\"" << strs[i] << "\"" << (i < strs.size() - 1 ? ", " : "]");
    }
    cout << endl << "分组结果: ";
    for (const auto& group : anagramGroups) {
        cout << "[";
        for (int i = 0; i < group.size(); i++) {
            cout << "\"" << group[i] << "\"" << (i < group.size() - 1 ? ", " : "] ");
        }
    }
    cout << endl;
    
    // 测试有效的字母异位词
    cout << endl << "3. 有效的字母异位词测试:" << endl;
    ValidAnagramSolution validAnagram;
    string s1 = "anagram", s2 = "nagaram";
    bool isAnagram = validAnagram.isAnagram(s1, s2);
    cout << "s1 = \"" << s1 << "\", s2 = \"" << s2 << "\"" << endl;
    cout << "是否是字母异位词: " << (isAnagram ? "true" : "false") << endl;
    
    // 测试无重复字符的最长子串
    cout << endl << "4. 无重复字符的最长子串测试:" << endl;
    LongestSubstringSolution longestSubstring;
    string testStr = "abcabcbb";
    int maxLength = longestSubstring.lengthOfLongestSubstring(testStr);
    cout << "字符串: \"" << testStr << "\"" << endl;
    cout << "最长无重复子串长度: " << maxLength << endl;
    
    // 测试和为K的子数组
    cout << endl << "5. 和为K的子数组测试:" << endl;
    SubarraySumSolution subarraySum;
    vector<int> nums2 = {1, 1, 1};
    int k = 2;
    int subarrayCount = subarraySum.subarraySum(nums2, k);
    cout << "数组: [";
    for (int i = 0; i < nums2.size(); i++) {
        cout << nums2[i] << (i < nums2.size() - 1 ? ", " : "]");
    }
    cout << ", k = " << k << endl;
    cout << "和为" << k << "的子数组个数: " << subarrayCount << endl;
    
    // 测试第一个只出现一次的字符
    cout << endl << "6. 第一个只出现一次的字符测试:" << endl;
    FirstUniqueCharSolution firstUniqueChar;
    string testStr2 = "leetcode";
    char uniqueChar = firstUniqueChar.firstUniqChar(testStr2);
    cout << "字符串: \"" << testStr2 << "\"" << endl;
    cout << "第一个只出现一次的字符: " << uniqueChar << endl;
    
    cout << endl << "=== 算法复杂度分析 ===" << endl;
    cout << "1. 两数之和: O(n) 时间, O(n) 空间" << endl;
    cout << "2. 字母异位词分组: O(n*k log k) 时间, O(n*k) 空间" << endl;
    cout << "3. 有效的字母异位词: O(n) 时间, O(1) 空间" << endl;
    cout << "4. 无重复字符的最长子串: O(n) 时间, O(min(m,n)) 空间" << endl;
    cout << "5. 和为K的子数组: O(n) 时间, O(n) 空间" << endl;
    cout << "6. 第一个只出现一次的字符: O(n) 时间, O(1) 空间" << endl;
    
    cout << endl << "=== C++工程化建议 ===" << endl;
    cout << "1. 使用unordered_map代替map以获得更好的平均时间复杂度" << endl;
    cout << "2. 注意C++ STL容器的内存管理特性" << endl;
    cout << "3. 使用reserve()预分配哈希表空间以提高性能" << endl;
    cout << "4. 注意迭代器的失效问题" << endl;
    cout << "5. 使用移动语义减少不必要的拷贝" << endl;
    cout << "6. 注意多线程环境下的线程安全问题" << endl;
}

int main() {
    testHashAlgorithmProblems();
    return 0;
}