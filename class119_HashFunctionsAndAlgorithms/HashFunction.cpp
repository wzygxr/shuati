/**
 * C++版本的哈希函数实现
 */

#include <iostream>
#include <string>
#include <vector>
#include <list>
#include <unordered_map>
#include <map>
#include <algorithm>
#include <cmath>
#include <random>
#include <bitset>
#include <functional>
#include <chrono>
#include <climits>
using namespace std;

class HashFunction {
public:
    /**
     * LintCode 128. Hash Function
     * 题目来源: https://www.lintcode.com/problem/hash-function/description
     * 
     * 题目描述:
     * 在数据结构中，哈希函数是用来将一个字符串（或任何其他类型）转化为小于哈希表大小且大于等于零的整数。
     * 一个好的哈希函数可以尽可能少地产生冲突。
     * 一种广泛使用的哈希函数算法是使用数值33，假设任何字符串都是基于33的一个大整数，比如：
     * hashcode("abcd") = (ascii(a) * 33^3 + ascii(b) * 33^2 + ascii(c) *33 + ascii(d)) % HASH_SIZE
     *                  = (97* 33^3 + 98 * 33^2 + 99 * 33 +100) % HASH_SIZE
     *                  = 3595978 % HASH_SIZE
     * 其中HASH_SIZE表示哈希表的大小(可以假设一个哈希表就是一个索引0 ~ HASH_SIZE-1的数组)。
     * 给出一个字符串作为key和一个哈希表的大小，返回这个字符串的哈希值。
     * 
     * 样例:
     * 对于key="abcd" 并且 size=100， 返回 78
     * 
     * 算法思路:
     * 使用霍纳法则（Horner's Rule）优化计算，避免大数溢出:
     * hashcode = (ascii(a) * 33^3 + ascii(b) * 33^2 + ascii(c) *33 + ascii(d)) % HASH_SIZE
     * 可以转换为:
     * hashcode = ((((ascii(a) % HASH_SIZE) * 33 + ascii(b)) % HASH_SIZE) * 33 + ascii(c)) % HASH_SIZE) * 33 + ascii(d)) % HASH_SIZE
     * 
     * 时间复杂度: O(n)，其中n是字符串的长度
     * 空间复杂度: O(1)
     */
    static int hashCode(const char* key, int HASH_SIZE) {
        long long ans = 0;
        for (int i = 0; key[i] != '\0'; i++) {
            ans = (ans * 33 + (int)(key[i])) % HASH_SIZE;
        }
        return (int)ans;
    }
    
    /**
     * 重载版本，接受字符串对象
     */
    static int hashCode(const char* key, int length, int HASH_SIZE) {
        long long ans = 0;
        for (int i = 0; i < length; i++) {
            ans = (ans * 33 + (int)(key[i])) % HASH_SIZE;
        }
        return (int)ans;
    }
};

/**
 * LeetCode 705. Design HashSet (设计哈希集合)
 * 题目来源: https://leetcode.com/problems/design-hashset/
 * 
 * 题目描述:
 * 不使用任何内建的哈希表库设计一个哈希集合（HashSet）。
 * 实现 MyHashSet 类：
 * void add(key) 向哈希集合中插入值 key 。
 * bool contains(key) 返回哈希集合中是否存在这个值 key 。
 * void remove(key) 将给定值 key 从哈希集合中删除。如果哈希集合中没有这个值，什么也不做。
 * 
 * 示例:
 * 输入：
 * ["MyHashSet", "add", "add", "contains", "contains", "add", "contains", "remove", "contains"]
 * [[], [1], [2], [1], [3], [2], [2], [2], [2]]
 * 输出：
 * [null, null, null, true, false, null, true, null, false]
 * 
 * 约束条件:
 * 0 <= key <= 10^6
 * 最多调用 10^4 次 add、remove 和 contains
 * 
 * 算法思路:
 * 使用链地址法实现哈希表，创建一个固定大小的数组，每个数组元素是一个链表。
 * 当发生哈希冲突时，将元素添加到对应位置的链表中。
 * 
 * 时间复杂度: O(n/b)，其中n是元素个数，b是桶数（在实际实现中我们使用10000作为桶数）
 * 空间复杂度: O(n)，存储所有元素
 */
class MyHashSet {
private:
    static const int BASE = 10000;
    std::list<int> data[BASE];
    
    static int hash(int key) {
        return key % BASE;
    }
    
public:
    /** Initialize your data structure here. */
    MyHashSet() {
        // 构造函数不需要特殊处理
    }
    
    void add(int key) {
        int h = hash(key);
        for (auto it = data[h].begin(); it != data[h].end(); ++it) {
            if ((*it) == key) {
                return;
            }
        }
        data[h].push_back(key);
    }
    
    void remove(int key) {
        int h = hash(key);
        for (auto it = data[h].begin(); it != data[h].end(); ++it) {
            if ((*it) == key) {
                data[h].erase(it);
                return;
            }
        }
    }
    
    /** Returns true if this set contains the specified element */
    bool contains(int key) {
        int h = hash(key);
        for (auto it = data[h].begin(); it != data[h].end(); ++it) {
            if ((*it) == key) {
                return true;
            }
        }
        return false;
    }
};

/**
 * LeetCode 706. Design HashMap (设计哈希映射)
 * 题目来源: https://leetcode.com/problems/design-hashmap/
 * 
 * 题目描述:
 * 不使用任何内建的哈希表库设计一个哈希映射（HashMap）。
 * 实现 MyHashMap 类：
 * MyHashMap() 用空映射初始化对象
 * void put(int key, int value) 向 HashMap 插入一个键值对 (key, value) 。如果 key 已经存在于映射中，则更新其对应的值 value 。
 * int get(int key) 返回特定的 key 所映射的 value ；如果映射中不包含 key 的映射，返回 -1 。
 * void remove(key) 如果映射中存在 key 的映射，则移除 key 和它所对应的 value 。
 * 
 * 示例:
 * 输入：
 * ["MyHashMap", "put", "put", "get", "get", "put", "get", "remove", "get"]
 * [[], [1, 1], [2, 2], [1], [3], [2, 1], [2], [2], [2]]
 * 输出：
 * [null, null, null, 1, -1, null, 1, null, -1]
 * 
 * 约束条件:
 * 0 <= key, value <= 10^6
 * 最多调用 10^4 次 put、get 和 remove 方法
 * 
 * 算法思路:
 * 使用链地址法实现哈希表，创建一个固定大小的数组，每个数组元素是一个链表。
 * 每个链表节点存储键值对，当发生哈希冲突时，将节点添加到对应位置的链表中。
 * 
 * 时间复杂度: O(n/b)，其中n是元素个数，b是桶数（在实际实现中我们使用10000作为桶数）
 * 空间复杂度: O(n)，存储所有元素
 */
class MyHashMap {
private:
    static const int BASE = 10000;
    std::list<std::pair<int, int>> data[BASE];
    
    static int hash(int key) {
        return key % BASE;
    }
    
public:
    /** Initialize your data structure here. */
    MyHashMap() {
        // 构造函数不需要特殊处理
    }
    
    /** value will always be non-negative. */
    void put(int key, int value) {
        int h = hash(key);
        for (auto it = data[h].begin(); it != data[h].end(); ++it) {
            if (it->first == key) {
                it->second = value;
                return;
            }
        }
        data[h].push_back(std::make_pair(key, value));
    }
    
    /** Returns the value to which the specified key is mapped, or -1 if this map contains no mapping for the key */
    int get(int key) {
        int h = hash(key);
        for (auto it = data[h].begin(); it != data[h].end(); ++it) {
            if (it->first == key) {
                return it->second;
            }
        }
        return -1;
    }
    
    /** Removes the mapping of the specified value key if this map contains a mapping for the key */
    void remove(int key) {
        int h = hash(key);
        for (auto it = data[h].begin(); it != data[h].end(); ++it) {
            if (it->first == key) {
                data[h].erase(it);
                return;
            }
        }
    }
};

/**
 * LeetCode 28. Find the Index of the First Occurrence in a String (实现strStr())
 * 题目来源: https://leetcode.com/problems/find-the-index-of-the-first-occurrence-in-a-string/
 * 
 * 题目描述:
 * 给你两个字符串 haystack 和 needle，请你在 haystack 字符串中找出 needle 字符串的第一个匹配项的下标（下标从 0 开始）。
 * 如果 needle 不是 haystack 的一部分，则返回 -1。
 * 
 * 示例:
 * 输入：haystack = "sadbutsad", needle = "sad"
 * 输出：0
 * 
 * 输入：haystack = "leetcode", needle = "leeto"
 * 输出：-1
 * 
 * 算法思路:
 * 使用Rabin-Karp算法（滚动哈希）实现字符串匹配：
 * 1. 计算needle的哈希值
 * 2. 在haystack中维护一个长度为needle.length()的滑动窗口，计算其哈希值
 * 3. 当哈希值相等时，再进行字符串比较确认（避免哈希冲突）
 * 
 * 时间复杂度: O(n+m)，其中n是haystack长度，m是needle长度
 * 空间复杂度: O(1)
 */
int strStr(string haystack, string needle) {
    if (needle.empty()) return 0;
    if (haystack.length() < needle.length()) return -1;
    
    int base = 256; // 基数
    int mod = 1000000007; // 大质数，用于取模运算
    
    long long needleHash = 0;
    long long haystackHash = 0;
    long long h = 1; // 用于计算最高位的权重
    
    // 计算needle的哈希值和h的值
    for (int i = 0; i < needle.length(); i++) {
        needleHash = (needleHash * base + needle[i]) % mod;
        if (i < needle.length() - 1) {
            h = (h * base) % mod;
        }
    }
    
    // 计算haystack第一个窗口的哈希值
    for (int i = 0; i < needle.length(); i++) {
        haystackHash = (haystackHash * base + haystack[i]) % mod;
    }
    
    // 滑动窗口匹配
    for (int i = 0; i <= (int)(haystack.length() - needle.length()); i++) {
        // 如果哈希值相等，再进行字符串比较确认
        if (needleHash == haystackHash) {
            if (haystack.substr(i, needle.length()) == needle) {
                return i;
            }
        }
        
        // 计算下一个窗口的哈希值
        if (i < (int)(haystack.length() - needle.length())) {
            haystackHash = (base * (haystackHash - (haystack[i] * h) % mod) + haystack[i + needle.length()]) % mod;
            if (haystackHash < 0) {
                haystackHash += mod;
            }
        }
    }
    
    return -1;
}

/**
 * LeetCode 187. Repeated DNA Sequences (重复的DNA序列)
 * 题目来源: https://leetcode.com/problems/repeated-dna-sequences/
 * 
 * 题目描述:
 * DNA序列由一系列核苷酸组成，缩写为 'A', 'C', 'G' 和 'T'。
 * 例如，"ACGAATTCCG" 是一个 DNA序列。
 * 在研究 DNA 时，识别 DNA 中的重复序列非常有用。
 * 给定一个表示 DNA序列 的字符串 s，返回所有在 DNA 分子中出现不止一次的长度为 10 的序列(子字符串)。
 * 可以按任意顺序返回答案。
 * 
 * 示例:
 * 输入：s = "AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT"
 * 输出：["AAAAACCCCC","CCCCCAAAAA"]
 * 
 * 输入：s = "AAAAAAAAAAAAA"
 * 输出：["AAAAAAAAAA"]
 * 
 * 算法思路:
 * 使用滚动哈希技术：
 * 1. 将每个字符映射为数字：A=0, C=1, G=2, T=3
 * 2. 使用4进制表示长度为10的序列
 * 3. 滑动窗口遍历所有长度为10的子串，计算其哈希值
 * 4. 使用哈希表记录每个哈希值出现的次数
 * 5. 返回出现次数大于1的序列
 * 
 * 时间复杂度: O(n)，其中n是DNA序列长度
 * 空间复杂度: O(n)，存储所有子串的哈希值
 */
vector<string> findRepeatedDnaSequences(string s) {
    vector<string> result;
    if (s.length() < 10) return result;
    
    // 字符到数字的映射
    int map[256] = {0};
    map['A'] = 0;
    map['C'] = 1;
    map['G'] = 2;
    map['T'] = 3;
    
    int base = 4;
    int mod = 1000000007; // 大质数，用于取模运算
    int windowSize = 10;
    
    // 计算base^(windowSize-1) % mod
    long long h = 1;
    for (int i = 0; i < windowSize - 1; i++) {
        h = (h * base) % mod;
    }
    
    // 计算第一个窗口的哈希值
    long long hash = 0;
    for (int i = 0; i < windowSize; i++) {
        hash = (hash * base + map[s[i]]) % mod;
    }
    
    // 使用哈希表记录每个哈希值出现的次数
    unordered_map<long long, int> hashMap;
    hashMap[hash] = 1;
    
    // 滑动窗口计算后续哈希值
    for (int i = 1; i <= (int)(s.length() - windowSize); i++) {
        // 移除最高位字符，添加最低位字符
        hash = (base * (hash - (map[s[i - 1]] * h) % mod) + map[s[i + windowSize - 1]]) % mod;
        if (hash < 0) {
            hash += mod;
        }
        
        // 记录哈希值出现次数
        hashMap[hash]++;
        
        // 如果某个哈希值出现2次，将其对应的子串加入结果集
        if (hashMap[hash] == 2) {
            result.push_back(s.substr(i, windowSize));
        }
    }
    
    return result;
}

// 测试函数
/**
 * LeetCode 214. Shortest Palindrome (最短回文串)
 * 题目来源: https://leetcode.com/problems/shortest-palindrome/
 * 
 * 题目描述:
 * 给你一个字符串 s，你可以通过在字符串前面添加字符将其转换为回文串。
 * 找到并返回可以用这种方式转换的最短回文串。
 * 
 * 示例:
 * 输入：s = "aacecaaa"
 * 输出："aaacecaaa"
 * 
 * 输入：s = "abcd"
 * 输出："dcbabcd"
 * 
 * 算法思路:
 * 使用滚动哈希技术找到s的最长前缀回文串:
 * 1. 计算s的正向哈希和反向哈希
 * 2. 使用双指针从两端向中间移动，同时比较正向和反向哈希
 * 3. 当找到最长前缀回文串后，将剩余部分反转并添加到原字符串前面
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(n)
 */
string shortestPalindrome(string s) {
    if (s.length() <= 1) return s;
    
    long long n = s.length();
    long long base = 256;
    long long mod = 1000000007;
    long long forwardHash = 0;
    long long backwardHash = 0;
    long long power = 1;
    long long maxLen = 0;
    
    for (int i = 0; i < n; i++) {
        forwardHash = (forwardHash * base + s[i]) % mod;
        backwardHash = (backwardHash + s[i] * power) % mod;
        if (forwardHash == backwardHash) {
            maxLen = i + 1;
        }
        power = (power * base) % mod;
    }
    
    string suffix = s.substr(maxLen);
    reverse(suffix.begin(), suffix.end());
    return suffix + s;
}

/**
 * LeetCode 1. Two Sum (两数之和)
 * 题目来源: https://leetcode.com/problems/two-sum/
 * 
 * 题目描述:
 * 给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出和为目标值 target 的那两个整数，并返回它们的数组下标。
 * 你可以假设每种输入只会对应一个答案。但是，数组中同一个元素在答案里不能重复出现。
 * 你可以按任意顺序返回答案。
 * 
 * 示例:
 * 输入：nums = [2,7,11,15], target = 9
 * 输出：[0,1]
 * 解释：因为 nums[0] + nums[1] == 9 ，返回 [0, 1] 。
 * 
 * 算法思路:
 * 使用哈希表存储每个数字及其对应的索引，遍历数组时检查target - nums[i]是否在哈希表中
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(n)
 */
vector<int> twoSum(vector<int>& nums, int target) {
    unordered_map<int, int> map;
    for (int i = 0; i < nums.size(); i++) {
        int complement = target - nums[i];
        if (map.find(complement) != map.end()) {
            return {map[complement], i};
        }
        map[nums[i]] = i;
    }
    return {-1, -1};
}

/**
 * LeetCode 49. Group Anagrams (字母异位词分组)
 * 题目来源: https://leetcode.com/problems/group-anagrams/
 * 
 * 题目描述:
 * 给你一个字符串数组，请你将字母异位词组合在一起。可以按任意顺序返回结果列表。
 * 字母异位词是由重新排列源单词的所有字母得到的一个新单词。
 * 
 * 示例:
 * 输入: strs = ["eat", "tea", "tan", "ate", "nat", "bat"]
 * 输出: [["bat"],["nat","tan"],["ate","eat","tea"]]
 * 
 * 算法思路:
 * 使用排序后的字符串作为哈希表的键，将具有相同排序字符串的单词分组
 * 
 * 时间复杂度: O(n * k log k)，其中n是字符串数量，k是字符串最大长度
 * 空间复杂度: O(n * k)
 */
vector<vector<string>> groupAnagrams(vector<string>& strs) {
    unordered_map<string, vector<string>> map;
    for (string str : strs) {
        string key = str;
        sort(key.begin(), key.end());
        map[key].push_back(str);
    }
    
    vector<vector<string>> result;
    for (auto& pair : map) {
        result.push_back(pair.second);
    }
    return result;
}

/**
 * LeetCode 242. Valid Anagram (有效的字母异位词)
 * 题目来源: https://leetcode.com/problems/valid-anagram/
 * 
 * 题目描述:
 * 给定两个字符串 s 和 t ，编写一个函数来判断 t 是否是 s 的字母异位词。
 * 注意：若 s 和 t 中每个字符出现的次数都相同，则称 s 和 t 互为字母异位词。
 * 
 * 示例:
 * 输入: s = "anagram", t = "nagaram"
 * 输出: true
 * 
 * 算法思路:
 * 使用哈希表统计每个字符出现的次数，然后比较两个字符串的字符频率
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(1)，因为字符集大小固定为26
 */
bool isAnagram(string s, string t) {
    if (s.length() != t.length()) return false;
    
    vector<int> count(26, 0);
    for (char c : s) {
        count[c - 'a']++;
    }
    for (char c : t) {
        count[c - 'a']--;
        if (count[c - 'a'] < 0) return false;
    }
    return true;
}

/**
 * LeetCode 3. Longest Substring Without Repeating Characters (无重复字符的最长子串)
 * 题目来源: https://leetcode.com/problems/longest-substring-without-repeating-characters/
 * 
 * 题目描述:
 * 给定一个字符串 s ，请你找出其中不含有重复字符的最长子串的长度。
 * 
 * 示例:
 * 输入: s = "abcabcbb"
 * 输出: 3
 * 解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
 * 
 * 算法思路:
 * 使用滑动窗口和哈希表记录字符最后出现的位置
 * 当遇到重复字符时，移动窗口左边界到重复字符的下一个位置
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(min(m, n))，其中m是字符集大小
 */
int lengthOfLongestSubstring(string s) {
    unordered_map<char, int> map;
    int maxLength = 0;
    int left = 0;
    
    for (int right = 0; right < s.length(); right++) {
        char c = s[right];
        if (map.find(c) != map.end() && map[c] >= left) {
            left = map[c] + 1;
        }
        map[c] = right;
        maxLength = max(maxLength, right - left + 1);
    }
    return maxLength;
}

/**
 * LeetCode 76. Minimum Window Substring (最小覆盖子串)
 * 题目来源: https://leetcode.com/problems/minimum-window-substring/
 * 
 * 题目描述:
 * 给你一个字符串 s 、一个字符串 t 。返回 s 中涵盖 t 所有字符的最小子串。如果 s 中不存在涵盖 t 所有字符的子串，则返回空字符串 "" 。
 * 
 * 示例:
 * 输入：s = "ADOBECODEBANC", t = "ABC"
 * 输出："BANC"
 * 解释：最小覆盖子串 "BANC" 包含来自字符串 t 的 'A'、'B' 和 'C'。
 * 
 * 算法思路:
 * 使用滑动窗口和哈希表统计字符频率
 * 维护一个计数器记录还需要匹配的字符数量
 * 
 * 时间复杂度: O(m + n)
 * 空间复杂度: O(m + n)
 */
string minWindow(string s, string t) {
    if (s.length() < t.length()) return "";
    
    unordered_map<char, int> target;
    unordered_map<char, int> window;
    
    // 统计t中字符频率
    for (char c : t) {
        target[c]++;
    }
    
    int left = 0, right = 0;
    int required = target.size();
    int formed = 0;
    int minLength = INT_MAX;
    int minLeft = 0, minRight = 0;
    
    while (right < s.length()) {
        char c = s[right];
        window[c]++;
        
        if (target.find(c) != target.end() && window[c] == target[c]) {
            formed++;
        }
        
        while (left <= right && formed == required) {
            c = s[left];
            
            if (right - left + 1 < minLength) {
                minLength = right - left + 1;
                minLeft = left;
                minRight = right;
            }
            
            window[c]--;
            if (target.find(c) != target.end() && window[c] < target[c]) {
                formed--;
            }
            left++;
        }
        right++;
    }
    
    return minLength == INT_MAX ? "" : s.substr(minLeft, minRight - minLeft + 1);
}

/**
 * LeetCode 560. Subarray Sum Equals K (和为K的子数组)
 * 题目来源: https://leetcode.com/problems/subarray-sum-equals-k/
 * 
 * 题目描述:
 * 给你一个整数数组 nums 和一个整数 k ，请你统计并返回该数组中和为 k 的连续子数组的个数。
 * 
 * 示例:
 * 输入：nums = [1,1,1], k = 2
 * 输出：2
 * 
 * 算法思路:
 * 使用前缀和和哈希表，记录每个前缀和出现的次数
 * 当prefixSum - k在哈希表中存在时，说明存在和为k的子数组
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(n)
 */
int subarraySum(vector<int>& nums, int k) {
    unordered_map<int, int> prefixSumCount;
    prefixSumCount[0] = 1; // 前缀和为0出现1次
    int prefixSum = 0;
    int count = 0;
    
    for (int num : nums) {
        prefixSum += num;
        if (prefixSumCount.find(prefixSum - k) != prefixSumCount.end()) {
            count += prefixSumCount[prefixSum - k];
        }
        prefixSumCount[prefixSum]++;
    }
    return count;
}

/**
 * LeetCode 347. Top K Frequent Elements (前K个高频元素)
 * 题目来源: https://leetcode.com/problems/top-k-frequent-elements/
 * 
 * 题目描述:
 * 给你一个整数数组 nums 和一个整数 k ，请你返回其中出现频率前 k 高的元素。你可以按任意顺序返回答案。
 * 
 * 示例:
 * 输入: nums = [1,1,1,2,2,3], k = 2
 * 输出: [1,2]
 * 
 * 算法思路:
 * 使用哈希表统计频率，然后使用桶排序或优先队列找出前k个高频元素
 * 
 * 时间复杂度: O(n log k)
 * 空间复杂度: O(n)
 */
vector<int> topKFrequent(vector<int>& nums, int k) {
    unordered_map<int, int> frequencyMap;
    for (int num : nums) {
        frequencyMap[num]++;
    }
    
    // 使用桶排序
    vector<vector<int>> buckets(nums.size() + 1);
    for (auto& pair : frequencyMap) {
        buckets[pair.second].push_back(pair.first);
    }
    
    vector<int> result;
    for (int i = buckets.size() - 1; i >= 0 && result.size() < k; i--) {
        for (int num : buckets[i]) {
            result.push_back(num);
            if (result.size() == k) break;
        }
    }
    return result;
}

/**
 * LeetCode 380. Insert Delete GetRandom O(1) (常数时间插入、删除和获取随机元素)
 * 题目来源: https://leetcode.com/problems/insert-delete-getrandom-o1/
 * 
 * 题目描述:
 * 实现RandomizedSet类：
 * RandomizedSet() 初始化 RandomizedSet 对象
 * bool insert(int val) 当元素 val 不存在时，向集合中插入该项，并返回 true ；否则，返回 false 。
 * bool remove(int val) 当元素 val 存在时，从集合中移除该项，并返回 true ；否则，返回 false 。
 * int getRandom() 随机返回现有集合中的一项（测试用例保证调用此方法时集合中至少存在一个元素）。每个元素应该有相同的概率被返回。
 * 
 * 算法思路:
 * 使用哈希表存储值和索引的映射，使用动态数组存储值
 * 删除时将要删除的元素与最后一个元素交换，然后删除最后一个元素
 * 
 * 时间复杂度: O(1) 平均时间复杂度
 * 空间复杂度: O(n)
 */
class RandomizedSet {
private:
    unordered_map<int, int> valueToIndex;
    vector<int> values;
    default_random_engine generator;
    
public:
    RandomizedSet() {
        // 使用当前时间作为随机种子
        generator.seed(chrono::system_clock::now().time_since_epoch().count());
    }
    
    bool insert(int val) {
        if (valueToIndex.find(val) != valueToIndex.end()) {
            return false;
        }
        valueToIndex[val] = values.size();
        values.push_back(val);
        return true;
    }
    
    bool remove(int val) {
        if (valueToIndex.find(val) == valueToIndex.end()) {
            return false;
        }
        int index = valueToIndex[val];
        int lastElement = values.back();
        
        // 将要删除的元素与最后一个元素交换
        values[index] = lastElement;
        valueToIndex[lastElement] = index;
        
        // 删除最后一个元素
        values.pop_back();
        valueToIndex.erase(val);
        
        return true;
    }
    
    int getRandom() {
        uniform_int_distribution<int> distribution(0, values.size() - 1);
        return values[distribution(generator)];
    }
};

/**
 * LeetCode 146. LRU Cache (LRU缓存)
 * 题目来源: https://leetcode.com/problems/lru-cache/
 * 
 * 题目描述:
 * 请你设计并实现一个满足 LRU (最近最少使用) 缓存约束的数据结构。
 * 实现 LRUCache 类：
 * LRUCache(int capacity) 以正整数作为容量 capacity 初始化 LRU 缓存
 * int get(int key) 如果关键字 key 存在于缓存中，则返回关键字的值，否则返回 -1 。
 * void put(int key, int value) 如果关键字 key 已经存在，则变更其数据值 value ；如果不存在，则向缓存中插入该组 key-value 。
 * 如果插入操作导致关键字数量超过 capacity ，则应该逐出最久未使用的关键字。
 * 
 * 算法思路:
 * 使用哈希表+双向链表实现
 * 哈希表提供O(1)的查找，双向链表维护访问顺序
 * 
 * 时间复杂度: O(1)
 * 空间复杂度: O(capacity)
 */
class LRUCache {
private:
    struct DLinkedNode {
        int key;
        int value;
        DLinkedNode* prev;
        DLinkedNode* next;
        DLinkedNode() : key(0), value(0), prev(nullptr), next(nullptr) {}
        DLinkedNode(int k, int v) : key(k), value(v), prev(nullptr), next(nullptr) {}
    };
    
    unordered_map<int, DLinkedNode*> cache;
    int size;
    int capacity;
    DLinkedNode* head;
    DLinkedNode* tail;
    
    void addToHead(DLinkedNode* node) {
        node->prev = head;
        node->next = head->next;
        head->next->prev = node;
        head->next = node;
    }
    
    void removeNode(DLinkedNode* node) {
        node->prev->next = node->next;
        node->next->prev = node->prev;
    }
    
    void moveToHead(DLinkedNode* node) {
        removeNode(node);
        addToHead(node);
    }
    
    DLinkedNode* removeTail() {
        DLinkedNode* node = tail->prev;
        removeNode(node);
        return node;
    }
    
public:
    LRUCache(int capacity) {
        this->capacity = capacity;
        this->size = 0;
        head = new DLinkedNode();
        tail = new DLinkedNode();
        head->next = tail;
        tail->prev = head;
    }
    
    int get(int key) {
        if (cache.find(key) == cache.end()) {
            return -1;
        }
        DLinkedNode* node = cache[key];
        moveToHead(node);
        return node->value;
    }
    
    void put(int key, int value) {
        if (cache.find(key) != cache.end()) {
            DLinkedNode* node = cache[key];
            node->value = value;
            moveToHead(node);
        } else {
            DLinkedNode* newNode = new DLinkedNode(key, value);
            cache[key] = newNode;
            addToHead(newNode);
            size++;
            if (size > capacity) {
                DLinkedNode* tail = removeTail();
                cache.erase(tail->key);
                delete tail;
                size--;
            }
        }
    }
};

/**
 * 一致性哈希 (Consistent Hashing) 实现
 * 
 * 应用场景: 分布式系统中的负载均衡，如分布式缓存、数据库分片等
 * 算法原理: 将服务器和键都映射到一个虚拟环上，每个键被分配给顺时针方向遇到的第一个服务器
 * 优势: 当服务器增减时，只需要重新分配少量键，减少数据迁移
 */
class ConsistentHash {
private:
    map<long long, string> virtualNodes; // 虚拟节点环
    int replicas; // 每个真实节点对应的虚拟节点数
    vector<string> servers; // 真实服务器列表
    
    // FNV-1a哈希算法
    long long getHash(const string& key) {
        const long long FNV_32_INIT = 0x811c9dc5;
        const long long FNV_32_PRIME = 0x01000193;
        
        long long hash = FNV_32_INIT;
        for (char c : key) {
            hash ^= static_cast<long long>(c);
            hash *= FNV_32_PRIME;
            hash &= 0xFFFFFFFFLL; // 保持32位
        }
        return abs(hash);
    }
    
public:
    ConsistentHash(int replicas) : replicas(replicas) {}
    
    // 添加服务器
    void addServer(const string& server) {
        servers.push_back(server);
        // 为每个真实节点创建多个虚拟节点
        for (int i = 0; i < replicas; ++i) {
            string virtualNode = server + "#" + to_string(i);
            long long hash = getHash(virtualNode);
            virtualNodes[hash] = server;
        }
    }
    
    // 移除服务器
    void removeServer(const string& server) {
        auto it = find(servers.begin(), servers.end(), server);
        if (it != servers.end()) {
            servers.erase(it);
            // 移除对应的所有虚拟节点
            vector<long long> keysToRemove;
            for (const auto& node : virtualNodes) {
                if (node.second == server) {
                    keysToRemove.push_back(node.first);
                }
            }
            for (long long key : keysToRemove) {
                virtualNodes.erase(key);
            }
        }
    }
    
    // 获取键对应的服务器
    string getServer(const string& key) {
        if (virtualNodes.empty()) {
            return "";
        }
        
        long long hash = getHash(key);
        // 找到顺时针方向的第一个服务器
        auto it = virtualNodes.lower_bound(hash);
        // 如果没有比当前hash大的节点，则返回环的第一个节点
        if (it == virtualNodes.end()) {
            it = virtualNodes.begin();
        }
        return it->second;
    }
    
    // 获取服务器列表
    vector<string> getServers() const {
        return servers;
    }
};

/**
 * 布隆过滤器 (Bloom Filter) 实现
 * 
 * 应用场景: 快速判断一个元素是否可能存在于集合中，如垃圾邮件过滤、缓存穿透防护等
 * 算法原理: 使用多个哈希函数将元素映射到位数组的不同位置，查询时检查所有位置是否都为1
 * 特点: 存在一定的误判率，但不会漏判；删除元素困难
 */
class BloomFilter {
private:
    vector<bool> bits; // 位数组
    int size; // 位数组大小
    vector<int> seeds; // 多个哈希函数的种子
    
    // 哈希函数
    int getHash(const string& element, int seed) {
        int hash = 0;
        for (char c : element) {
            hash = seed * hash + static_cast<int>(c);
        }
        return abs(hash % size);
    }
    
public:
    BloomFilter(int size, int hashFunctions) : size(size), bits(size, false) {
        seeds.resize(hashFunctions);
        // 初始化哈希函数种子
        for (int i = 0; i < hashFunctions; ++i) {
            seeds[i] = i * 100 + 31; // 使用不同的种子
        }
    }
    
    // 添加元素
    void add(const string& element) {
        for (int seed : seeds) {
            int hash = getHash(element, seed);
            bits[hash] = true;
        }
    }
    
    // 判断元素是否可能存在
    bool mightContain(const string& element) {
        for (int seed : seeds) {
            int hash = getHash(element, seed);
            if (!bits[hash]) {
                return false; // 只要有一个位置为0，元素一定不存在
            }
        }
        return true; // 所有位置都为1，元素可能存在
    }
};

/**
 * 双重哈希 (Double Hashing) 实现的哈希表
 * 
 * 应用场景: 开放寻址法解决哈希冲突
 * 算法原理: 使用两个哈希函数，当发生冲突时，第二个哈希函数确定探测步长
 * 优势: 减少聚集现象，提高哈希表性能
 */
template <typename K, typename V>
class DoubleHashTable {
private:
    static const int DEFAULT_SIZE = 16;
    static constexpr double LOAD_FACTOR = 0.75;
    
    vector<K> keys;
    vector<V> values;
    vector<bool> occupied;
    int size;
    int count;
    
    // 第一个哈希函数
    int hash1(const K& key) {
        // 使用标准库的哈希函数
        hash<K> hasher;
        return abs(static_cast<int>(hasher(key) % size));
    }
    
    // 第二个哈希函数，用于计算步长
    int hash2(const K& key) {
        hash<K> hasher;
        return 1 + abs(static_cast<int>(hasher(key) % (size - 1)));
    }
    
    // 查找插入位置
    int findInsertionIndex(const K& key) {
        int h1 = hash1(key);
        int h2 = hash2(key);
        int index = h1;
        int step = 1;
        
        // 查找空位置或相同的键
        while (occupied[index]) {
            if (keys[index] == key) {
                return index; // 键已存在，返回该位置以更新值
            }
            index = (h1 + step * h2) % size;
            step++;
        }
        
        return index;
    }
    
    // 查找键的索引
    int findIndex(const K& key) {
        int h1 = hash1(key);
        int h2 = hash2(key);
        int index = h1;
        int step = 1;
        
        // 查找键
        while (occupied[index]) {
            if (keys[index] == key) {
                return index; // 找到键
            }
            index = (h1 + step * h2) % size;
            step++;
            // 避免无限循环
            if (step > size) {
                break;
            }
        }
        
        return -1; // 未找到键
    }
    
    // 扩容
    void rehash() {
        vector<K> oldKeys = keys;
        vector<V> oldValues = values;
        vector<bool> oldOccupied = occupied;
        
        // 扩大为原来的两倍
        int oldSize = size;
        size *= 2;
        keys.assign(size, K());
        values.assign(size, V());
        occupied.assign(size, false);
        count = 0;
        
        // 重新插入所有键值对
        for (int i = 0; i < oldSize; ++i) {
            if (oldOccupied[i]) {
                put(oldKeys[i], oldValues[i]);
            }
        }
    }
    
public:
    DoubleHashTable() : size(DEFAULT_SIZE), count(0) {
        keys.resize(DEFAULT_SIZE);
        values.resize(DEFAULT_SIZE);
        occupied.resize(DEFAULT_SIZE, false);
    }
    
    // 插入键值对
    void put(const K& key, const V& value) {
        // 检查是否需要扩容
        if (static_cast<double>(count) / size >= LOAD_FACTOR) {
            rehash();
        }
        
        int index = findInsertionIndex(key);
        keys[index] = key;
        values[index] = value;
        if (!occupied[index]) {
            occupied[index] = true;
            count++;
        }
    }
    
    // 获取值
    V* get(const K& key) {
        int index = findIndex(key);
        return (index != -1) ? &values[index] : nullptr;
    }
    
    // 删除键值对
    void remove(const K& key) {
        int index = findIndex(key);
        if (index != -1) {
            occupied[index] = false;
            count--;
        }
    }
    
    // 获取大小
    int getSize() const {
        return count;
    }
};

int main() {
    // 测试LintCode 128题
    cout << "=== LintCode 128. Hash Function ===" << endl;
    const char* key = "abcd";
    int HASH_SIZE = 100;
    int result = HashFunction::hashCode(key, HASH_SIZE);
    cout << "Key: " << key << ", HASH_SIZE: " << HASH_SIZE << ", Result: " << result << endl;
    cout << endl;
    
    // 测试LeetCode 705. Design HashSet
    cout << "=== LeetCode 705. Design HashSet ===" << endl;
    MyHashSet myHashSet;
    myHashSet.add(1);      // set = [1]
    myHashSet.add(2);      // set = [1, 2]
    cout << boolalpha << myHashSet.contains(1) << endl; // 返回 True
    cout << boolalpha << myHashSet.contains(3) << endl; // 返回 False （未找到）
    myHashSet.add(2);      // set = [1, 2]
    cout << boolalpha << myHashSet.contains(2) << endl; // 返回 True
    myHashSet.remove(2);   // set = [1]
    cout << boolalpha << myHashSet.contains(2) << endl; // 返回 False （已移除）
    
    // 测试LeetCode 706. Design HashMap
    cout << "\n=== LeetCode 706. Design HashMap ===" << endl;
    MyHashMap myHashMap;
    myHashMap.put(1, 1); // myHashMap 现在为 [[1,1]]
    myHashMap.put(2, 2); // myHashMap 现在为 [[1,1], [2,2]]
    cout << myHashMap.get(1) << endl;    // 返回 1 ，myHashMap 现在为 [[1,1], [2,2]]
    cout << myHashMap.get(3) << endl;    // 返回 -1（未找到），myHashMap 现在为 [[1,1], [2,2]]
    myHashMap.put(2, 1); // myHashMap 现在为 [[1,1], [2,1]]（更新已有的值）
    cout << myHashMap.get(2) << endl;    // 返回 1 ，myHashMap 现在为 [[1,1], [2,1]]
    myHashMap.remove(2); // 删除键为 2 的数据，myHashMap 现在为 [[1,1]]
    cout << myHashMap.get(2) << endl;    // 返回 -1（未找到），myHashMap 现在为 [[1,1]]
    
    // 测试LeetCode 28. Find the Index of the First Occurrence in a String
    cout << "\n=== LeetCode 28. Find the Index of the First Occurrence in a String ===" << endl;
    cout << strStr("sadbutsad", "sad") << endl; // 返回 0
    cout << strStr("leetcode", "leeto") << endl; // 返回 -1
    
    // 测试LeetCode 187. Repeated DNA Sequences
    cout << "\n=== LeetCode 187. Repeated DNA Sequences ===" << endl;
    vector<string> dnaResult = findRepeatedDnaSequences("AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT");
    for (const string& seq : dnaResult) {
        cout << seq << " ";
    }
    cout << endl;
    
    // 测试LeetCode 214. Shortest Palindrome
    cout << "\n=== LeetCode 214. Shortest Palindrome ===" << endl;
    cout << "shortestPalindrome(\"aacecaaa\"): " << shortestPalindrome("aacecaaa") << endl;
    cout << "shortestPalindrome(\"abcd\"): " << shortestPalindrome("abcd") << endl;
    
    // 测试一致性哈希
    cout << "\n=== 一致性哈希 (Consistent Hashing) ===" << endl;
    ConsistentHash consistentHash(100); // 每个服务器100个虚拟节点
    consistentHash.addServer("Server1");
    consistentHash.addServer("Server2");
    consistentHash.addServer("Server3");
    
    cout << "键 'user1' 分配到的服务器: " << consistentHash.getServer("user1") << endl;
    cout << "键 'user2' 分配到的服务器: " << consistentHash.getServer("user2") << endl;
    cout << "键 'user3' 分配到的服务器: " << consistentHash.getServer("user3") << endl;
    
    // 移除一个服务器后，观察键的重新分配情况
    cout << "\n移除 Server2 后:" << endl;
    consistentHash.removeServer("Server2");
    cout << "键 'user1' 分配到的服务器: " << consistentHash.getServer("user1") << endl;
    cout << "键 'user2' 分配到的服务器: " << consistentHash.getServer("user2") << endl;
    cout << "键 'user3' 分配到的服务器: " << consistentHash.getServer("user3") << endl;
    
    // 测试布隆过滤器
    cout << "\n=== 布隆过滤器 (Bloom Filter) ===" << endl;
    BloomFilter bloomFilter(10000, 7); // 10000位，7个哈希函数
    bloomFilter.add("apple");
    bloomFilter.add("banana");
    bloomFilter.add("orange");
    
    cout << "'apple' 可能在集合中: " << (bloomFilter.mightContain("apple") ? "true" : "false") << endl;
    cout << "'banana' 可能在集合中: " << (bloomFilter.mightContain("banana") ? "true" : "false") << endl;
    cout << "'orange' 可能在集合中: " << (bloomFilter.mightContain("orange") ? "true" : "false") << endl;
    cout << "'pear' 可能在集合中: " << (bloomFilter.mightContain("pear") ? "true" : "false") << endl;
    cout << "'grape' 可能在集合中: " << (bloomFilter.mightContain("grape") ? "true" : "false") << endl;
    
    // 测试双重哈希表
    cout << "\n=== 双重哈希 (Double Hashing) ===" << endl;
    DoubleHashTable<string, int> doubleHashTable;
    doubleHashTable.put("apple", 100);
    doubleHashTable.put("banana", 200);
    doubleHashTable.put("orange", 300);
    
    int* appleValue = doubleHashTable.get("apple");
    int* bananaValue = doubleHashTable.get("banana");
    int* orangeValue = doubleHashTable.get("orange");
    int* pearValue = doubleHashTable.get("pear");
    
    cout << "'apple' 的值: " << (appleValue ? to_string(*appleValue) : "null") << endl;
    cout << "'banana' 的值: " << (bananaValue ? to_string(*bananaValue) : "null") << endl;
    cout << "'orange' 的值: " << (orangeValue ? to_string(*orangeValue) : "null") << endl;
    cout << "'pear' 的值: " << (pearValue ? to_string(*pearValue) : "null") << endl;
    
    doubleHashTable.remove("banana");
    int* removedBananaValue = doubleHashTable.get("banana");
    cout << "移除 'banana' 后的值: " << (removedBananaValue ? to_string(*removedBananaValue) : "null") << endl;
    cout << "哈希表大小: " << doubleHashTable.getSize() << endl;
    
    // 测试更多哈希相关题目
    cout << "\n=== 更多哈希相关题目测试 ===" << endl;
    
    // 测试LeetCode 1. Two Sum
    cout << "\n=== LeetCode 1. Two Sum ===" << endl;
    vector<int> nums = {2, 7, 11, 15};
    int target = 9;
    vector<int> twoSumResult = twoSum(nums, target);
    cout << "nums: [2, 7, 11, 15], target: 9, result: [" << twoSumResult[0] << ", " << twoSumResult[1] << "]" << endl;
    
    // 测试LeetCode 49. Group Anagrams
    cout << "\n=== LeetCode 49. Group Anagrams ===" << endl;
    vector<string> strs = {"eat", "tea", "tan", "ate", "nat", "bat"};
    vector<vector<string>> anagramResult = groupAnagrams(strs);
    cout << "strs: [eat, tea, tan, ate, nat, bat]" << endl;
    cout << "grouped anagrams: ";
    for (const auto& group : anagramResult) {
        cout << "[";
        for (size_t i = 0; i < group.size(); i++) {
            cout << group[i];
            if (i < group.size() - 1) cout << ", ";
        }
        cout << "] ";
    }
    cout << endl;
    
    // 测试LeetCode 242. Valid Anagram
    cout << "\n=== LeetCode 242. Valid Anagram ===" << endl;
    string s1 = "anagram", s2 = "nagaram";
    bool anagramCheck = isAnagram(s1, s2);
    cout << "'" << s1 << "' and '" << s2 << "' are anagrams: " << (anagramCheck ? "true" : "false") << endl;
    
    // 测试LeetCode 3. Longest Substring Without Repeating Characters
    cout << "\n=== LeetCode 3. Longest Substring Without Repeating Characters ===" << endl;
    string s = "abcabcbb";
    int longestSubstring = lengthOfLongestSubstring(s);
    cout << "String: '" << s << "', longest substring length: " << longestSubstring << endl;
    
    // 测试LeetCode 76. Minimum Window Substring
    cout << "\n=== LeetCode 76. Minimum Window Substring ===" << endl;
    string sStr = "ADOBECODEBANC";
    string tStr = "ABC";
    string minWindowResult = minWindow(sStr, tStr);
    cout << "s: '" << sStr << "', t: '" << tStr << "', min window: '" << minWindowResult << "'" << endl;
    
    // 测试LeetCode 560. Subarray Sum Equals K
    cout << "\n=== LeetCode 560. Subarray Sum Equals K ===" << endl;
    vector<int> sumNums = {1, 1, 1};
    int k = 2;
    int subarraySumResult = subarraySum(sumNums, k);
    cout << "nums: [1, 1, 1], k: 2, subarray count: " << subarraySumResult << endl;
    
    // 测试LeetCode 347. Top K Frequent Elements
    cout << "\n=== LeetCode 347. Top K Frequent Elements ===" << endl;
    vector<int> freqNums = {1, 1, 1, 2, 2, 3};
    int kFreq = 2;
    vector<int> topKFrequentResult = topKFrequent(freqNums, kFreq);
    cout << "nums: [1, 1, 1, 2, 2, 3], k: 2, top k frequent: [";
    for (size_t i = 0; i < topKFrequentResult.size(); i++) {
        cout << topKFrequentResult[i];
        if (i < topKFrequentResult.size() - 1) cout << ", ";
    }
    cout << "]" << endl;
    
    // 测试LeetCode 380. Insert Delete GetRandom O(1)
    cout << "\n=== LeetCode 380. Insert Delete GetRandom O(1) ===" << endl;
    RandomizedSet randomizedSet;
    cout << "Insert 1: " << (randomizedSet.insert(1) ? "true" : "false") << endl;
    cout << "Insert 2: " << (randomizedSet.insert(2) ? "true" : "false") << endl;
    cout << "Insert 1 again: " << (randomizedSet.insert(1) ? "true" : "false") << endl;
    cout << "Get random: " << randomizedSet.getRandom() << endl;
    cout << "Remove 2: " << (randomizedSet.remove(2) ? "true" : "false") << endl;
    cout << "Remove 3: " << (randomizedSet.remove(3) ? "true" : "false") << endl;
    cout << "Get random: " << randomizedSet.getRandom() << endl;
    
    // 测试LeetCode 146. LRU Cache
    cout << "\n=== LeetCode 146. LRU Cache ===" << endl;
    LRUCache lruCache(2);
    lruCache.put(1, 1);
    lruCache.put(2, 2);
    cout << "Get 1: " << lruCache.get(1) << endl;
    lruCache.put(3, 3);  // 这会使得键2被移除
    cout << "Get 2: " << lruCache.get(2) << endl;
    cout << "Get 3: " << lruCache.get(3) << endl;
    
    return 0;
}