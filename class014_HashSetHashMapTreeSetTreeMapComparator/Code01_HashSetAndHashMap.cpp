#include <iostream>
#include <vector>
#include <unordered_map>
#include <unordered_set>
#include <algorithm>
#include <string>
#include <list>
#include <queue>
#include <climits>

using namespace std;

/**
 * HashSet和HashMap相关题目与解析
 * 
 * C++中使用unordered_set和unordered_map
 * unordered_set基于哈希表，查询时间复杂度O(1)，元素无序
 * unordered_map基于哈希表，查询时间复杂度O(1)，键值对无序
 * 
 * 相关平台题目：
 * 1. LeetCode 1. Two Sum (两数之和) - https://leetcode.com/problems/two-sum/
 * 2. LeetCode 3. Longest Substring Without Repeating Characters (无重复字符的最长子串) - https://leetcode.com/problems/longest-substring-without-repeating-characters/
 * 3. LeetCode 36. Valid Sudoku (有效的数独) - https://leetcode.com/problems/valid-sudoku/
 * 4. LeetCode 136. Single Number (只出现一次的数字) - https://leetcode.com/problems/single-number/
 * 5. LeetCode 202. Happy Number (快乐数) - https://leetcode.com/problems/happy-number/
 * 6. LeetCode 217. Contains Duplicate (存在重复元素) - https://leetcode.com/problems/contains-duplicate/
 * 7. LeetCode 219. Contains Duplicate II (存在重复元素 II) - https://leetcode.com/problems/contains-duplicate-ii/
 * 8. LeetCode 242. Valid Anagram (有效的字母异位词) - https://leetcode.com/problems/valid-anagram/
 * 9. LeetCode 349. Intersection of Two Arrays (两个数组的交集) - https://leetcode.com/problems/intersection-of-two-arrays/
 * 10. LeetCode 350. Intersection of Two Arrays II (两个数组的交集 II) - https://leetcode.com/problems/intersection-of-two-arrays-ii/
 * 11. LeetCode 387. First Unique Character in a String (字符串中的第一个唯一字符) - https://leetcode.com/problems/first-unique-character-in-a-string/
 * 12. LeetCode 448. Find All Numbers Disappeared in an Array (找到所有数组中消失的数字) - https://leetcode.com/problems/find-all-numbers-disappeared-in-an-array/
 * 13. LeetCode 575. Distribute Candies (分糖果) - https://leetcode.com/problems/distribute-candies/
 * 14. LeetCode 811. Subdomain Visit Count (子域名访问计数) - https://leetcode.com/problems/subdomain-visit-count/
 * 15. LeetCode 705. Design HashSet (设计哈希集合) - https://leetcode.com/problems/design-hashset/
 * 16. LeetCode 706. Design HashMap (设计哈希映射) - https://leetcode.com/problems/design-hashmap/
 * 17. HackerRank Java Hashset (Java哈希集) - https://www.hackerrank.com/challenges/java-hashset
 * 18. LeetCode 128. Longest Consecutive Sequence (最长连续序列) - https://leetcode.com/problems/longest-consecutive-sequence/
 * 19. LeetCode 49. Group Anagrams (字母异位词分组) - https://leetcode.com/problems/group-anagrams/
 * 20. LeetCode 347. Top K Frequent Elements (前 K 个高频元素) - https://leetcode.com/problems/top-k-frequent-elements/
 * 21. LeetCode 3. Longest Substring Without Repeating Characters (无重复字符的最长子串) - https://leetcode.com/problems/longest-substring-without-repeating-characters/
 * 22. LeetCode 36. Valid Sudoku (有效的数独) - https://leetcode.com/problems/valid-sudoku/
 * 23. LeetCode 141. Linked List Cycle (环形链表) - https://leetcode.com/problems/linked-list-cycle/
 * 24. LeetCode 160. Intersection of Two Linked Lists (相交链表) - https://leetcode.com/problems/intersection-of-two-linked-lists/
 * 25. LintCode 547. Intersection of Two Arrays (两个数组的交集) - https://www.lintcode.com/problem/intersection-of-two-arrays/
 * 26. Codeforces 4C. Registration System (注册系统) - https://codeforces.com/problemset/problem/4/C
 * 27. AtCoder ABC 217 D - Cutting Woods (切割木材) - https://atcoder.jp/contests/abc217/tasks/abc217_d
 * 28. USACO Silver: Why Did the Cow Cross the Road (为什么奶牛要过马路) - http://www.usaco.org/index.php?page=viewproblem2&cpid=714
 * 29. 洛谷 P3374 【模板】树状数组 1 (模板树状数组1) - https://www.luogu.com.cn/problem/P3374
 * 30. CodeChef STFOOD (街头食物) - https://www.codechef.com/problems/STFOOD
 * 31. SPOJ ANARC09A - Seinfeld (宋飞正传) - https://www.spoj.com/problems/ANARC09A/
 * 32. Project Euler Problem 1: Multiples of 3 and 5 (3和5的倍数) - https://projecteuler.net/problem=1
 * 33. HackerRank Frequency Queries (频率查询) - https://www.hackerrank.com/challenges/frequency-queries
 * 34. 计蒜客 T1100: 计算2的N次方 (计算2的N次方) - https://www.jisuanke.com/t/T1100
 * 35. 杭电 OJ 1002: A + B Problem II (A+B问题II) - http://acm.hdu.edu.cn/showproblem.php?pid=1002
 * 36. 牛客网 剑指Offer 03: 数组中重复的数字 (数组中重复的数字) - https://www.nowcoder.com/practice/623a5ac0ea5b4e5f95552655361ae0a8
 * 37. acwing 799. 最长连续不重复子序列 (最长连续不重复子序列) - https://www.acwing.com/problem/content/801/
 * 38. POJ 1002: 487-3279 (电话号码) - http://poj.org/problem?id=1002
 * 39. UVa OJ 100: The 3n + 1 problem (3n+1问题) - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=36
 * 40. Timus OJ 1001: Reverse Root (反转平方根) - https://acm.timus.ru/problem.aspx?space=1&num=1001
 * 41. Aizu OJ ALDS1_4_C: Dictionary (字典) - http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_4_C
 * 42. Comet OJ Contest #0: 热身赛 A. 签到题 (签到题) - https://cometoj.com/contest/0/problem/A
 * 43. MarsCode 火星编程竞赛: 字符串去重排序 (字符串去重排序) - https://www.marscode.cn/contest/1/problem/1001
 * 44. ZOJ 1001: A + B Problem (A+B问题) - http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=1001
 * 45. LOJ 100: 顺序的分数 (顺序的分数) - https://loj.ac/p/100
 * 46. 各大高校OJ: 清华大学OJ 1000: A+B Problem (A+B问题) - http://dsa.cs.tsinghua.edu.cn/oj/problem.shtml?id=1000
 */
/**
 * LeetCode 1. Two Sum (两数之和)
 * 
 * 题目描述：
 * 给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出和为目标值的那两个整数，并返回它们的数组下标。
 * 你可以假设每种输入只会对应一个答案。但是，数组中同一个元素不能使用两遍。
 * 你可以按任意顺序返回答案。
 * 
 * 解题思路：
 * 使用unordered_map存储数组中的元素及其索引，遍历数组时，对于每个元素，计算其与目标值的差值，
 * 检查该差值是否存在于unordered_map中，如果存在，则找到了两个数，返回它们的索引。
 * 
 * 时间复杂度：O(n)，其中n是数组长度，我们只需要遍历数组一次
 * 空间复杂度：O(n)，最坏情况下需要存储n个元素
 */
vector<int> twoSum(vector<int>& nums, int target) {
    // 创建unordered_map存储数字和其索引
    unordered_map<int, int> map;
    
    // 遍历数组
    for (int i = 0; i < nums.size(); i++) {
        // 计算需要找到的另一个数字
        int complement = target - nums[i];
        
        // 检查该数字是否已存在于unordered_map中
        if (map.find(complement) != map.end()) {
            // 如果存在，返回两个数字的索引
            return {map[complement], i};
        }
        
        // 将当前数字和索引存入unordered_map
        map[nums[i]] = i;
    }
    
    // 根据题目保证总会有一个解，这里仅为避免编译错误
    return {};
}

// LeetCode 242. Valid Anagram (有效的字母异位词)
bool isAnagram(string s, string t) {
    // 如果两个字符串长度不同，肯定不是字母异位词
    if (s.length() != t.length()) {
        return false;
    }
    
    // 创建unordered_map记录字符出现次数
    unordered_map<char, int> charCount;
    
    // 遍历字符串s，统计每个字符出现次数
    for (char c : s) {
        charCount[c]++;
    }
    
    // 遍历字符串t，减少对应字符计数
    for (char c : t) {
        // 如果字符不存在或计数已为0，返回false
        if (charCount.find(c) == charCount.end() || charCount[c] == 0) {
            return false;
        }
        // 减少字符计数
        charCount[c]--;
    }
    
    return true;
}

// LeetCode 349. Intersection of Two Arrays (两个数组的交集)
vector<int> intersection(vector<int>& nums1, vector<int>& nums2) {
    // 使用unordered_set存储nums1中的元素
    unordered_set<int> set1(nums1.begin(), nums1.end());
    
    // 使用unordered_set存储交集结果，自动去重
    unordered_set<int> intersection;
    
    // 遍历nums2，查找交集
    for (int num : nums2) {
        if (set1.find(num) != set1.end()) {
            intersection.insert(num);
        }
    }
    
    // 将结果转换为vector
    return vector<int>(intersection.begin(), intersection.end());
}

// LeetCode 705. Design HashSet (设计哈希集合)
class MyHashSet {
private:
    static const int BASE = 10000;
    list<int> data[BASE];
    
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

// LeetCode 706. Design HashMap (设计哈希映射)
class MyHashMap {
private:
    static const int BASE = 10000;
    list<pair<int, int>> data[BASE];
    
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
            if ((*it).first == key) {
                (*it).second = value;
                return;
            }
        }
        data[h].push_back(make_pair(key, value));
    }
    
    /** Returns the value to which the specified key is mapped, or -1 if this map contains no mapping for the key */
    int get(int key) {
        int h = hash(key);
        for (auto it = data[h].begin(); it != data[h].end(); ++it) {
            if ((*it).first == key) {
                return (*it).second;
            }
        }
        return -1;
    }
    
    /** Removes the mapping of the specified value key if this map contains a mapping for the key */
    void remove(int key) {
        int h = hash(key);
        for (auto it = data[h].begin(); it != data[h].end(); ++it) {
            if ((*it).first == key) {
                data[h].erase(it);
                return;
            }
        }
    }
};

// LeetCode 128. Longest Consecutive Sequence (最长连续序列)
int longestConsecutive(vector<int>& nums) {
    unordered_set<int> numSet(nums.begin(), nums.end());
    
    int longestStreak = 0;
    
    for (int num : numSet) {
        // 只有当num-1不存在时，num才是一个序列的开始
        if (numSet.find(num - 1) == numSet.end()) {
            int currentNum = num;
            int currentStreak = 1;
            
            // 向后查找连续的数字
            while (numSet.find(currentNum + 1) != numSet.end()) {
                currentNum += 1;
                currentStreak += 1;
            }
            
            longestStreak = max(longestStreak, currentStreak);
        }
    }
    
    return longestStreak;
}

// LeetCode 49. Group Anagrams (字母异位词分组)
vector<vector<string>> groupAnagrams(vector<string>& strs) {
    unordered_map<string, vector<string>> map;
    
    for (string str : strs) {
        // 将字符串排序后作为键
        string key = str;
        sort(key.begin(), key.end());
        
        // 将原字符串添加到对应的列表中
        map[key].push_back(str);
    }
    
    vector<vector<string>> result;
    for (auto& pair : map) {
        result.push_back(pair.second);
    }
    
    return result;
}

// LeetCode 347. Top K Frequent Elements (前 K 个高频元素)
vector<int> topKFrequent(vector<int>& nums, int k) {
    // 统计每个元素的频率
    unordered_map<int, int> freqMap;
    for (int num : nums) {
        freqMap[num]++;
    }
    
    // 使用最小堆维护前k个高频元素
    priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> heap;
    
    // 遍历频率映射，维护堆的大小为k
    for (auto& pair : freqMap) {
        heap.push(make_pair(pair.second, pair.first));
        if (heap.size() > k) {
            heap.pop();
        }
    }
    
    // 从堆中取出所有元素
    vector<int> result;
    while (!heap.empty()) {
        result.push_back(heap.top().second);
        heap.pop();
    }
    
    return result;
}

/**
 * LeetCode 219. Contains Duplicate II (存在重复元素 II)
 * 
 * 题目描述：
 * 给定一个整数数组和一个整数 k，判断数组中是否存在两个不同的索引 i 和 j，
 * 使得 nums[i] == nums[j]，并且 i 和 j 的差的绝对值至多为 k。
 * 
 * 解题思路：
 * 使用unordered_map存储每个元素最后一次出现的索引，遍历数组时，检查当前元素是否在unordered_map中存在，
 * 如果存在且当前索引与存储的索引之差的绝对值小于等于k，则返回true；
 * 否则更新unordered_map中该元素的索引为当前索引。
 * 
 * 时间复杂度：O(n)，其中n是数组长度，我们只需要遍历数组一次
 * 空间复杂度：O(min(n,k))，最坏情况下需要存储min(n,k)个元素
 */
bool containsNearbyDuplicate(vector<int>& nums, int k) {
	unordered_map<int, int> numMap;
	for (int i = 0; i < nums.size(); i++) {
		if (numMap.find(nums[i]) != numMap.end()) {
			int prevIndex = numMap[nums[i]];
			if (i - prevIndex <= k) {
				return true;
			}
		}
		numMap[nums[i]] = i;
	}
	return false;
}

/**
 * LeetCode 3. Longest Substring Without Repeating Characters (无重复字符的最长子串)
 * 
 * 题目描述：
 * 给定一个字符串 s，请你找出其中不含有重复字符的最长子串的长度。
 * 
 * 解题思路：
 * 使用滑动窗口和unordered_map，unordered_map记录每个字符最后一次出现的位置。
 * 维护一个左边界left，当遇到重复字符时，将left更新为重复字符上一次出现位置的下一个位置。
 * 计算当前窗口长度i-left+1，并更新最大长度。
 * 
 * 时间复杂度：O(n)，其中n是字符串长度
 * 空间复杂度：O(min(n, m))，其中m是字符集大小
 */
int lengthOfLongestSubstring(string s) {
	unordered_map<char, int> charMap;
	int maxLength = 0;
	int left = 0;
	
	for (int i = 0; i < s.length(); i++) {
		char c = s[i];
		// 如果字符已存在且在当前窗口内，更新左边界
		if (charMap.find(c) != charMap.end() && charMap[c] >= left) {
			left = charMap[c] + 1;
		}
		// 更新字符位置
		charMap[c] = i;
		// 更新最大长度
		maxLength = max(maxLength, i - left + 1);
	}
	
	return maxLength;
}

/**
 * LeetCode 36. Valid Sudoku (有效的数独)
 * 
 * 题目描述：
 * 请你判断一个 9 x 9 的数独是否有效。只需要 根据以下规则 ，验证已经填入的数字是否有效即可。
 * 1. 数字 1-9 在每一行只能出现一次。
 * 2. 数字 1-9 在每一列只能出现一次。
 * 3. 数字 1-9 在每一个以粗实线分隔的 3x3 宫内只能出现一次。
 * 
 * 解题思路：
 * 使用三个unordered_set数组分别记录每一行、每一列和每一个3x3宫格中出现过的数字。
 * 遍历数独，对于每个非空白字符，检查是否已经在对应的行、列或宫格中出现过。
 * 宫格索引可以通过公式：boxIndex = (row / 3) * 3 + (col / 3) 计算得到。
 * 
 * 时间复杂度：O(1)，因为数独大小固定为9x9
 * 空间复杂度：O(1)，固定大小的unordered_set数组
 */
bool isValidSudoku(vector<vector<char>>& board) {
	vector<unordered_set<char>> rows(9);
	vector<unordered_set<char>> cols(9);
	vector<unordered_set<char>> boxes(9);
	
	// 遍历数独
	for (int row = 0; row < 9; row++) {
		for (int col = 0; col < 9; col++) {
			char c = board[row][col];
			// 跳过空白格
			if (c == '.') {
				continue;
			}
			
			// 计算宫格索引
			int boxIndex = (row / 3) * 3 + (col / 3);
			
			// 检查是否重复
			if (rows[row].count(c) || cols[col].count(c) || boxes[boxIndex].count(c)) {
				return false;
			}
			
			// 添加到对应的unordered_set中
			rows[row].insert(c);
			cols[col].insert(c);
			boxes[boxIndex].insert(c);
		}
	}
	
	return true;
}

/**
 * LeetCode 141. Linked List Cycle (环形链表)
 * 
 * 题目描述：
 * 给定一个链表，判断链表中是否有环。
 * 如果链表中存在环，则返回 true 。 否则，返回 false 。
 * 
 * 解题思路1（使用unordered_set）：
 * 遍历链表，将每个节点存入unordered_set中，如果遇到已存在的节点，则说明有环。
 * 
 * 时间复杂度：O(n)，其中n是链表长度
 * 空间复杂度：O(n)，需要存储所有节点
 */
// 定义链表节点结构
struct ListNode {
	int val;
	ListNode *next;
	ListNode(int x) : val(x), next(nullptr) {}
};

bool hasCycle(ListNode *head) {
	unordered_set<ListNode*> seen;
	ListNode *current = head;
	
	while (current != nullptr) {
		if (seen.count(current)) {
			return true; // 发现环
		}
		seen.insert(current);
		current = current->next;
	}
	
	return false; // 没有环
}

/**
 * LeetCode 160. Intersection of Two Linked Lists (相交链表)
 * 
 * 题目描述：
 * 给你两个单链表的头节点 headA 和 headB ，请你找出并返回两个单链表相交的起始节点。如果两个链表没有交点，返回 nullptr 。
 * 
 * 解题思路1（使用unordered_set）：
 * 遍历链表A，将每个节点存入unordered_set中，然后遍历链表B，检查节点是否存在于unordered_set中。
 * 
 * 时间复杂度：O(m+n)，其中m和n分别是两个链表的长度
 * 空间复杂度：O(m)，需要存储链表A的所有节点
 */
ListNode *getIntersectionNode(ListNode *headA, ListNode *headB) {
	unordered_set<ListNode*> seen;
	ListNode *current = headA;
	
	// 将链表A的所有节点存入unordered_set
	while (current != nullptr) {
		seen.insert(current);
		current = current->next;
	}
	
	// 遍历链表B，查找交集
	current = headB;
	while (current != nullptr) {
		if (seen.count(current)) {
			return current; // 找到交点
		}
		current = current->next;
	}
	
	return nullptr; // 没有交点
}

int main() {
    // 测试两数之和
    cout << "测试两数之和:" << endl;
    vector<int> nums1 = {2, 7, 11, 15};
    int target = 9;
    vector<int> result1 = twoSum(nums1, target);
    cout << "[" << result1[0] << ", " << result1[1] << "]" << endl;
    
    // 测试有效的字母异位词
    cout << "测试有效的字母异位词:" << endl;
    cout << isAnagram("anagram", "nagaram") << endl; // 1 (true)
    cout << isAnagram("rat", "car") << endl; // 0 (false)
    
    // 测试两个数组的交集
    cout << "测试两个数组的交集:" << endl;
    vector<int> nums2 = {1, 2, 2, 1};
    vector<int> nums3 = {2, 2};
    vector<int> result2 = intersection(nums2, nums3);
    cout << "交集: [";
    for (int i = 0; i < result2.size(); i++) {
        cout << result2[i];
        if (i < result2.size() - 1) {
            cout << ", ";
        }
    }
    cout << "]" << endl;
    
    // 测试设计哈希集合
    cout << "测试设计哈希集合:" << endl;
    MyHashSet myHashSet;
    myHashSet.add(1);
    myHashSet.add(2);
    cout << myHashSet.contains(1) << endl; // 1 (true)
    cout << myHashSet.contains(3) << endl; // 0 (false)
    myHashSet.add(2);
    cout << myHashSet.contains(2) << endl; // 1 (true)
    myHashSet.remove(2);
    cout << myHashSet.contains(2) << endl; // 0 (false)
    
    // 测试设计哈希映射
    cout << "测试设计哈希映射:" << endl;
    MyHashMap myHashMap;
    myHashMap.put(1, 1);
    myHashMap.put(2, 2);
    cout << myHashMap.get(1) << endl; // 1
    cout << myHashMap.get(3) << endl; // -1
    myHashMap.put(2, 1);
    cout << myHashMap.get(2) << endl; // 1
    myHashMap.remove(2);
    cout << myHashMap.get(2) << endl; // -1
    
    // 测试最长连续序列
    cout << "测试最长连续序列:" << endl;
    vector<int> nums4 = {100, 4, 200, 1, 3, 2};
    cout << longestConsecutive(nums4) << endl; // 4
    
    // 测试字母异位词分组
    cout << "测试字母异位词分组:" << endl;
    vector<string> strs = {"eat", "tea", "tan", "ate", "nat", "bat"};
    vector<vector<string>> groups = groupAnagrams(strs);
    for (const auto& group : groups) {
        cout << "[";
        for (int i = 0; i < group.size(); i++) {
            cout << group[i];
            if (i < group.size() - 1) {
                cout << ", ";
            }
        }
        cout << "] ";
    }
    cout << endl;
    
    // 测试前K个高频元素
    cout << "测试前K个高频元素:" << endl;
    vector<int> nums5 = {1, 1, 1, 2, 2, 3};
    int k = 2;
    vector<int> result3 = topKFrequent(nums5, k);
    cout << "[";
    for (int i = 0; i < result3.size(); i++) {
        cout << result3[i];
        if (i < result3.size() - 1) {
            cout << ", ";
        }
    }
    cout << "]" << endl;
    
    return 0;
}   vector<string> strs = {"eat", "tea", "tan", "ate", "nat", "bat"};
    vector<vector<string>> groups = groupAnagrams(strs);
    for (const auto& group : groups) {
        cout << "[";
        for (int i = 0; i < group.size(); i++) {
            cout << group[i];
            if (i < group.size() - 1) {
                cout << ", ";
            }
        }
        cout << "] ";
    }
    cout << endl;
    
    // 测试前K个高频元素
    cout << "测试前K个高频元素:" << endl;
    vector<int> nums5 = {1, 1, 1, 2, 2, 3};
    int k = 2;
    vector<int> result3 = topKFrequent(nums5, k);
    cout << "[";
    for (int i = 0; i < result3.size(); i++) {
        cout << result3[i];
        if (i < result3.size() - 1) {
            cout << ", ";
        }
    }
    cout << "]" << endl;
    
    return 0;
}