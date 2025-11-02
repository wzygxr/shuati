# 哈希查找与AC自动机算法详解

## 哈希查找算法

### 哈希查找算法简介

哈希查找是一种通过哈希函数将键值映射到存储位置，从而实现快速查找的数据结构和算法。它是一种空间换时间的算法，可以在平均情况下达到O(1)的查找时间复杂度，是处理大规模数据查找问题的高效解决方案。

### 哈希查找核心思想

哈希查找的基本原理包括三个关键组成部分：

1. **哈希函数**：将任意长度的输入（键值）映射到固定长度的输出（哈希值）
2. **哈希表**：存储键值对的数据结构，通过哈希值确定存储位置
3. **冲突解决**：处理不同键值产生相同哈希值的情况

### 常见哈希冲突解决方法

1. **开放寻址法**：
   - 线性探测法
   - 二次探测法
   - 双重哈希法

2. **链地址法**：将冲突的键值对存储在同一个哈希桶的链表中

3. **建立公共溢出区**：将哈希冲突的数据统一存储到溢出区

### 哈希函数设计原则

1. **均匀性**：哈希值应均匀分布，减少冲突
2. **高效性**：计算速度快
3. **雪崩效应**：输入的微小变化应导致输出的显著变化
4. **确定性**：相同的输入必须产生相同的输出

### 常见哈希函数

1. **直接寻址法**：直接使用键值作为哈希地址
2. **除留余数法**：h(key) = key % m
3. **平方取中法**：取键值平方的中间几位作为哈希地址
4. **折叠法**：将键值分割成若干部分后合并
5. **随机数法**：使用随机数生成哈希地址
6. **多项式哈希**：例如，对于字符串s，计算h(s) = (s[0] * p^(n-1) + s[1] * p^(n-2) + ... + s[n-1]) % mod

### 哈希查找的时间和空间复杂度

1. **时间复杂度**：
   - 平均查找时间：O(1)
   - 最坏情况：O(n)，当所有键值哈希到同一位置时

2. **空间复杂度**：O(m)，其中m是哈希表大小

### 哈希查找的优化策略

1. **负载因子控制**：保持哈希表的负载因子（元素个数/表大小）在合理范围内，通常为0.75
2. **动态调整大小**：当负载因子过高时，进行扩容并重新哈希
3. **预计算和缓存**：对于频繁使用的哈希值进行缓存
4. **特殊数据类型优化**：针对不同数据类型设计专用的哈希函数

### 哈希查找的应用场景

1. 数据库索引
2. 缓存系统
3. 拼写检查
4. 去重操作
5. 查找表
6. 键值存储系统
7. 密码学应用
8. 分布式系统中的负载均衡

## 哈希查找经典题目

### 1. LeetCode 1. 两数之和
**题目链接**：https://leetcode.cn/problems/two-sum/
**题目描述**：给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出和为目标值 target 的那两个整数，并返回它们的数组下标。
**解题思路**：
- 使用哈希表存储已遍历的数字及其索引
- 对于当前数字，检查 (target - 当前数字) 是否在哈希表中
- 如果存在，则返回两个数字的索引
**算法优化**：
- 使用HashMap存储键值对，查找时间复杂度为O(1)
- 一次遍历完成，空间换时间
**时间复杂度**：O(n)，其中n是数组长度
**空间复杂度**：O(n)
**Java解法**：
```java
public int[] twoSum(int[] nums, int target) {
    Map<Integer, Integer> map = new HashMap<>();
    for (int i = 0; i < nums.length; i++) {
        int complement = target - nums[i];
        if (map.containsKey(complement)) {
            return new int[] { map.get(complement), i };
        }
        map.put(nums[i], i);
    }
    return new int[0]; // 无解情况
}
```

**Python解法**：
```python
def twoSum(nums, target):
    num_dict = {}
    for i, num in enumerate(nums):
        complement = target - num
        if complement in num_dict:
            return [num_dict[complement], i]
        num_dict[num] = i
    return []
```

**C++解法**：
```cpp
vector<int> twoSum(vector<int>& nums, int target) {
    unordered_map<int, int> map;
    for (int i = 0; i < nums.size(); i++) {
        int complement = target - nums[i];
        if (map.find(complement) != map.end()) {
            return {map[complement], i};
        }
        map[nums[i]] = i;
    }
    return {};
}
```

### 2. LeetCode 49. 字母异位词分组
**题目链接**：https://leetcode.cn/problems/group-anagrams/
**题目描述**：给你一个字符串数组，请你将字母异位词组合在一起。可以按任意顺序返回结果列表。
**解题思路**：
- 字母异位词排序后具有相同的形式
- 使用排序后的字符串作为哈希表的键
- 哈希表的值为字母异位词列表
**算法优化**：
- 可以使用字符计数作为键，避免排序，降低时间复杂度
- 使用数组表示字符计数，提高访问效率
**时间复杂度**：O(nk log k)，其中n是字符串数量，k是字符串的最大长度（排序方法）；或O(nk)（字符计数方法）
**空间复杂度**：O(nk)
**Java解法（排序方法）**：
```java
public List<List<String>> groupAnagrams(String[] strs) {
    Map<String, List<String>> map = new HashMap<>();
    for (String str : strs) {
        char[] chars = str.toCharArray();
        Arrays.sort(chars);
        String key = new String(chars);
        map.computeIfAbsent(key, k -> new ArrayList<>()).add(str);
    }
    return new ArrayList<>(map.values());
}
```

**Python解法**：
```python
def groupAnagrams(strs: list[str]) -> list[list[str]]:
    from collections import defaultdict
    anagram_map = defaultdict(list)
    
    for s in strs:
        # 将字符串排序，作为键
        key = ''.join(sorted(s))
        anagram_map[key].append(s)
    
    return list(anagram_map.values())
```

**C++解法**：
```cpp
#include <vector>
#include <string>
#include <unordered_map>
#include <algorithm>
using namespace std;

vector<vector<string>> groupAnagrams(vector<string>& strs) {
    unordered_map<string, vector<string>> anagramMap;
    
    for (string s : strs) {
        // 将字符串排序，作为键
        string key = s;
        sort(key.begin(), key.end());
        anagramMap[key].push_back(s);
    }
    
    vector<vector<string>> result;
    for (auto& pair : anagramMap) {
        result.push_back(pair.second);
    }
    
    return result;
}
```

### 3. LeetCode 128. 最长连续序列
**题目链接**：https://leetcode.cn/problems/longest-consecutive-sequence/
**题目描述**：给定一个未排序的整数数组 nums ，找出数字连续的最长序列（不要求序列元素在原数组中连续）的长度。
**解题思路**：
- 使用哈希集合存储所有数字，便于O(1)时间复杂度的查找
- 对于每个数字，如果它是序列的起点（即它的前一个数字不在集合中），则从该数字开始计算连续序列的长度
- 记录最长的连续序列长度
**算法优化**：
- 跳过已经处理过的数字，避免重复计算
- 只从序列起点开始处理，减少不必要的遍历
**时间复杂度**：O(n)，每个数字最多被访问两次
**空间复杂度**：O(n)
**Java解法**：
```java
public int longestConsecutive(int[] nums) {
    Set<Integer> numSet = new HashSet<>();
    for (int num : nums) {
        numSet.add(num);
    }
    
    int longest = 0;
    for (int num : numSet) {
        // 只处理序列的起点
        if (!numSet.contains(num - 1)) {
            int currentNum = num;
            int currentLength = 1;
            
            while (numSet.contains(currentNum + 1)) {
                currentNum++;
                currentLength++;
            }
            
            longest = Math.max(longest, currentLength);
        }
    }
    
    return longest;
}
```

**Python解法**：
```python
def longestConsecutive(nums: list[int]) -> int:
    if not nums:
        return 0
    
    num_set = set(nums)
    longest_streak = 0
    
    for num in num_set:
        # 只有当num是连续序列的起始数字时，才开始计算长度
        if num - 1 not in num_set:
            current_num = num
            current_streak = 1
            
            while current_num + 1 in num_set:
                current_num += 1
                current_streak += 1
            
            longest_streak = max(longest_streak, current_streak)
    
    return longest_streak
```

**C++解法**：
```cpp
#include <vector>
#include <unordered_set>
using namespace std;

int longestConsecutive(vector<int>& nums) {
    if (nums.empty()) {
        return 0;
    }
    
    unordered_set<int> numSet(nums.begin(), nums.end());
    int longestStreak = 0;
    
    for (int num : numSet) {
        // 只有当num是连续序列的起始数字时，才开始计算长度
        if (numSet.find(num - 1) == numSet.end()) {
            int currentNum = num;
            int currentStreak = 1;
            
            while (numSet.find(currentNum + 1) != numSet.end()) {
                currentNum++;
                currentStreak++;
            }
            
            longestStreak = max(longestStreak, currentStreak);
        }
    }
    
    return longestStreak;
}
```

### 4. LeetCode 217. 存在重复元素
**题目链接**：https://leetcode.cn/problems/contains-duplicate/
**题目描述**：给你一个整数数组 nums 。如果任一值在数组中出现至少两次，返回 true ；如果数组中每个元素互不相同，返回 false 。
**解题思路**：
- 使用哈希集合存储已遍历的元素
- 对于当前元素，如果已经在集合中，则说明有重复，返回true
- 否则将其加入集合
**算法优化**：
- 可以先对数组排序，然后比较相邻元素，但哈希方法时间复杂度更优
**时间复杂度**：O(n)
**空间复杂度**：O(n)
**Java解法**：
```java
public boolean containsDuplicate(int[] nums) {
    Set<Integer> set = new HashSet<>();
    for (int num : nums) {
        if (!set.add(num)) {
            return true;
        }
    }
    return false;
}
```

**Python解法**：
```python
def containsDuplicate(nums: list[int]) -> bool:
    seen = set()
    for num in nums:
        if num in seen:
            return True
        seen.add(num)
    return False
```

**C++解法**：
```cpp
#include <vector>
#include <unordered_set>
using namespace std;

bool containsDuplicate(vector<int>& nums) {
    unordered_set<int> seen;
    for (int num : nums) {
        if (seen.count(num)) {
            return true;
        }
        seen.insert(num);
    }
    return false;
}
```

### 5. LeetCode 387. 字符串中的第一个唯一字符
**题目链接**：https://leetcode.cn/problems/first-unique-character-in-a-string/
**题目描述**：给定一个字符串 s ，找到它的第一个不重复的字符，并返回它的索引。如果不存在，则返回 -1 。
**解题思路**：
- 使用哈希表统计每个字符出现的次数
- 再次遍历字符串，找到第一个出现次数为1的字符
**算法优化**：
- 由于字符集有限，可以使用数组代替哈希表，提高效率
**时间复杂度**：O(n)，其中n是字符串长度
**空间复杂度**：O(k)，k是字符集大小
**Java解法**：
```java
public int firstUniqChar(String s) {
    int[] count = new int[26];
    for (char c : s.toCharArray()) {
        count[c - 'a']++;
    }
    for (int i = 0; i < s.length(); i++) {
        if (count[s.charAt(i) - 'a'] == 1) {
            return i;
        }
    }
    return -1;
}
```

**Python解法**：
```python
def firstUniqChar(s: str) -> int:
    count = [0] * 26
    for c in s:
        count[ord(c) - ord('a')] += 1
    for i, c in enumerate(s):
        if count[ord(c) - ord('a')] == 1:
            return i
    return -1
```

**C++解法**：
```cpp
int firstUniqChar(string s) {
    int count[26] = {0};
    for (char c : s) {
        count[c - 'a']++;
    }
    for (int i = 0; i < s.length(); i++) {
        if (count[s[i] - 'a'] == 1) {
            return i;
        }
    }
    return -1;
}
```

### 6. POJ 1182 食物链
**题目链接**：http://poj.org/problem?id=1182
**题目描述**：动物王国中有三类动物A,B,C，它们的食物链构成了有趣的环形。A吃B， B吃C，C吃A。现有N个动物，以1－N编号。每个动物都是A,B,C中的一种，但是我们并不知道它到底是哪一种。有人用两种说法对这N个动物所构成的食物链关系进行描述：
1. "1 X Y"，表示X和Y是同类。
2. "2 X Y"，表示X吃Y。
但是这些说法可能存在矛盾，请你判断有多少个错误的说法。
**解题思路**：
- 使用带权并查集（哈希的一种应用）表示每个节点到根节点的关系
- 权值表示节点与父节点的关系：0表示同类，1表示该节点吃父节点，2表示父节点吃该节点
- 每次查询或合并时维护这个权值
**算法优化**：
- 路径压缩时更新权值
- 合并时根据关系计算新的权值
**时间复杂度**：O(m α(n))，其中m是操作次数，α是阿克曼函数的反函数，近似于O(1)
**空间复杂度**：O(n)
**Java解法**：
```java
public class Main {
    static int[] parent;
    static int[] rank; // 存储节点到父节点的关系
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int k = scanner.nextInt();
        parent = new int[n + 1];
        rank = new int[n + 1];
        
        for (int i = 1; i <= n; i++) {
            parent[i] = i;
        }
        
        int count = 0;
        for (int i = 0; i < k; i++) {
            int op = scanner.nextInt();
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            
            if (x > n || y > n) {
                count++;
                continue;
            }
            
            if (op == 1) { // X和Y是同类
                if (!union(x, y, 0)) {
                    count++;
                }
            } else { // X吃Y
                if (!union(x, y, 1)) {
                    count++;
                }
            }
        }
        System.out.println(count);
    }
    
    static int find(int x) {
        if (parent[x] != x) {
            int px = parent[x];
            parent[x] = find(parent[x]);
            rank[x] = (rank[x] + rank[px]) % 3;
        }
        return parent[x];
    }
    
    static boolean union(int x, int y, int relation) {
        int fx = find(x);
        int fy = find(y);
        
        if (fx == fy) {
            return (rank[x] - rank[y] + 3) % 3 == relation;
        }
        
        parent[fx] = fy;
        rank[fx] = (rank[y] - rank[x] + relation + 3) % 3;
        return true;
    }
}
```

**Python解法**：
```python
def main():
    import sys
    input = sys.stdin.read().split()
    idx = 0
    n = int(input[idx])
    idx += 1
    k = int(input[idx])
    idx += 1
    
    parent = list(range(n + 1))
    rank = [0] * (n + 1)
    
    def find(x):
        if parent[x] != x:
            px = parent[x]
            parent[x] = find(parent[x])
            rank[x] = (rank[x] + rank[px]) % 3
        return parent[x]
    
    def union(x, y, relation):
        fx = find(x)
        fy = find(y)
        if fx == fy:
            return (rank[x] - rank[y] + 3) % 3 == relation
        parent[fx] = fy
        rank[fx] = (rank[y] - rank[x] + relation + 3) % 3
        return True
    
    count = 0
    for _ in range(k):
        op = int(input[idx])
        idx += 1
        x = int(input[idx])
        idx += 1
        y = int(input[idx])
        idx += 1
        
        if x > n or y > n:
            count += 1
            continue
        
        if op == 1:  # X和Y是同类
            if not union(x, y, 0):
                count += 1
        else:  # X吃Y
            if not union(x, y, 1):
                count += 1
    
    print(count)

if __name__ == "__main__":
    main()
```

**C++解法**：
```cpp
#include <iostream>
using namespace std;

int *parent;
int *rank_; // 存储节点到父节点的关系

int find(int x) {
    if (parent[x] != x) {
        int px = parent[x];
        parent[x] = find(parent[x]);
        rank_[x] = (rank_[x] + rank_[px]) % 3;
    }
    return parent[x];
}

bool union_(int x, int y, int relation) {
    int fx = find(x);
    int fy = find(y);
    
    if (fx == fy) {
        return (rank_[x] - rank_[y] + 3) % 3 == relation;
    }
    
    parent[fx] = fy;
    rank_[fx] = (rank_[y] - rank_[x] + relation + 3) % 3;
    return true;
}

int main() {
    int n, k;
    cin >> n >> k;
    
    parent = new int[n + 1];
    rank_ = new int[n + 1];
    
    for (int i = 1; i <= n; i++) {
        parent[i] = i;
    }
    
    int count = 0;
    for (int i = 0; i < k; i++) {
        int op, x, y;
        cin >> op >> x >> y;
        
        if (x > n || y > n) {
            count++;
            continue;
        }
        
        if (op == 1) { // X和Y是同类
            if (!union_(x, y, 0)) {
                count++;
            }
        } else { // X吃Y
            if (!union_(x, y, 1)) {
                count++;
            }
        }
    }
    
    cout << count << endl;
    
    delete[] parent;
    delete[] rank_;
    
    return 0;
}
```

### 7. HDU 1263 水果
**题目链接**：http://acm.hdu.edu.cn/showproblem.php?pid=1263
**题目描述**：输入多个水果的产地、名称和数量，最后按产地和水果名称排序，输出各个产地各种水果的总数量。
**解题思路**：
- 使用嵌套哈希表存储，外层哈希表的键为产地，值为内层哈希表
- 内层哈希表的键为水果名称，值为数量
- 最后对产地和水果名称进行排序输出
**算法优化**：
- 可以使用TreeMap自动排序，避免手动排序
**时间复杂度**：O(n + m log m + k log k)，其中n是输入数量，m是产地数量，k是每种产地的水果种类
**空间复杂度**：O(mk)
**Java解法**：
```java
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int T = scanner.nextInt();
        while (T-- > 0) {
            int n = scanner.nextInt();
            Map<String, Map<String, Integer>> map = new TreeMap<>();
            
            for (int i = 0; i < n; i++) {
                String fruit = scanner.next();
                String place = scanner.next();
                int num = scanner.nextInt();
                
                map.computeIfAbsent(place, k -> new TreeMap<>());
                map.get(place).put(fruit, map.get(place).getOrDefault(fruit, 0) + num);
            }
            
            // 输出结果
            for (Map.Entry<String, Map<String, Integer>> entry : map.entrySet()) {
                System.out.println(entry.getKey());
                for (Map.Entry<String, Integer> fruitEntry : entry.getValue().entrySet()) {
                    System.out.println("   |----" + fruitEntry.getKey() + "(" + fruitEntry.getValue() + ")");
                }
            }
            
            if (T > 0) {
                System.out.println();
            }
        }
    }
}
```

**Python解法**：
```python
import sys
from collections import defaultdict

def main():
    input = sys.stdin.read().split()
    idx = 0
    T = int(input[idx])
    idx += 1
    
    for _ in range(T):
        n = int(input[idx])
        idx += 1
        
        # 使用defaultdict和sorted来模拟TreeMap的功能
        map = defaultdict(lambda: defaultdict(int))
        
        for _ in range(n):
            fruit = input[idx]
            idx += 1
            place = input[idx]
            idx += 1
            num = int(input[idx])
            idx += 1
            
            map[place][fruit] += num
        
        # 输出结果，按产地和水果名称排序
        places = sorted(map.keys())
        for place in places:
            print(place)
            fruits = sorted(map[place].keys())
            for fruit in fruits:
                print(f"   |----{fruit}({map[place][fruit]})")
        
        if _ < T - 1:
            print()

if __name__ == "__main__":
    main()
```

**C++解法**：
```cpp
#include <iostream>
#include <string>
#include <map>
using namespace std;

int main() {
    int T;
    cin >> T;
    
    while (T--) {
        int n;
        cin >> n;
        
        // 使用TreeMap存储，自动排序
        map<string, map<string, int>> fruitMap;
        
        for (int i = 0; i < n; i++) {
            string fruit, place;
            int num;
            cin >> fruit >> place >> num;
            
            fruitMap[place][fruit] += num;
        }
        
        // 输出结果
        for (auto &placeEntry : fruitMap) {
            cout << placeEntry.first << endl;
            for (auto &fruitEntry : placeEntry.second) {
                cout << "   |----" << fruitEntry.first << "(" << fruitEntry.second << ")" << endl;
            }
        }
        
        if (T > 0) {
            cout << endl;
        }
    }
    
    return 0;
}
```

### 8. LeetCode 349. 两个数组的交集
**题目链接**：https://leetcode.cn/problems/intersection-of-two-arrays/
**题目描述**：给定两个数组 nums1 和 nums2 ，返回它们的交集。输出结果中的每个元素一定是唯一的。我们可以不考虑输出结果的顺序。
**解题思路**：
- 使用两个哈希集合分别存储两个数组的元素
- 遍历较小的集合，检查每个元素是否在另一个集合中存在
- 如果存在，则加入结果集合
**算法优化**：
- 可以先对两个数组进行排序，然后使用双指针法，但哈希方法更简单高效
**时间复杂度**：O(n + m)，其中n和m分别是两个数组的长度
**空间复杂度**：O(n + m)
**Java解法**：
```java
public int[] intersection(int[] nums1, int[] nums2) {
    Set<Integer> set1 = new HashSet<>();
    for (int num : nums1) {
        set1.add(num);
    }
    
    Set<Integer> resultSet = new HashSet<>();
    for (int num : nums2) {
        if (set1.contains(num)) {
            resultSet.add(num);
        }
    }
    
    int[] result = new int[resultSet.size()];
    int i = 0;
    for (int num : resultSet) {
        result[i++] = num;
    }
    return result;
}
```

**Python解法**：
```python
def intersection(nums1: list[int], nums2: list[int]) -> list[int]:
    set1 = set(nums1)
    set2 = set(nums2)
    return list(set1 & set2)  # 集合的交集操作
```

**C++解法**：
```cpp
#include <vector>
#include <unordered_set>
using namespace std;

vector<int> intersection(vector<int>& nums1, vector<int>& nums2) {
    unordered_set<int> set1(nums1.begin(), nums1.end());
    unordered_set<int> resultSet;
    
    for (int num : nums2) {
        if (set1.find(num) != set1.end()) {
            resultSet.insert(num);
        }
    }
    
    return vector<int>(resultSet.begin(), resultSet.end());
}
```

### 9. LeetCode 350. 两个数组的交集 II
**题目链接**：https://leetcode.cn/problems/intersection-of-two-arrays-ii/
**题目描述**：给定两个数组 nums1 和 nums2 ，返回它们的交集。输出结果中每个元素出现的次数，应与元素在两个数组中都出现的次数一致。可以不考虑输出结果的顺序。
**解题思路**：
- 使用哈希表统计第一个数组中每个元素出现的次数
- 遍历第二个数组，对于每个元素，如果在哈希表中出现次数大于0，则加入结果，并将次数减1
**算法优化**：
- 可以先对两个数组进行排序，然后使用双指针法，适用于较大的数组
- 优先处理较小的数组，减少哈希表的大小
**时间复杂度**：O(n + m)
**空间复杂度**：O(min(n, m))
**Java解法**：
```java
public int[] intersect(int[] nums1, int[] nums2) {
    // 确保nums1是较小的数组，减少空间使用
    if (nums1.length > nums2.length) {
        return intersect(nums2, nums1);
    }
    
    Map<Integer, Integer> map = new HashMap<>();
    for (int num : nums1) {
        map.put(num, map.getOrDefault(num, 0) + 1);
    }
    
    List<Integer> resultList = new ArrayList<>();
    for (int num : nums2) {
        int count = map.getOrDefault(num, 0);
        if (count > 0) {
            resultList.add(num);
            map.put(num, count - 1);
        }
    }
    
    int[] result = new int[resultList.size()];
    for (int i = 0; i < resultList.size(); i++) {
        result[i] = resultList.get(i);
    }
    return result;
}
```

**Python解法**：
```python
def intersect(nums1: list[int], nums2: list[int]) -> list[int]:
    # 确保nums1是较小的数组
    if len(nums1) > len(nums2):
        return intersect(nums2, nums1)
    
    from collections import defaultdict
    count_map = defaultdict(int)
    for num in nums1:
        count_map[num] += 1
    
    result = []
    for num in nums2:
        if count_map[num] > 0:
            result.append(num)
            count_map[num] -= 1
    
    return result
```

**C++解法**：
```cpp
#include <vector>
#include <unordered_map>
using namespace std;

vector<int> intersect(vector<int>& nums1, vector<int>& nums2) {
    // 确保nums1是较小的数组
    if (nums1.size() > nums2.size()) {
        return intersect(nums2, nums1);
    }
    
    unordered_map<int, int> count_map;
    for (int num : nums1) {
        count_map[num]++;
    }
    
    vector<int> result;
    for (int num : nums2) {
        if (count_map[num] > 0) {
            result.push_back(num);
            count_map[num]--;
        }
    }
    
    return result;
}
```

### 10. LeetCode 146. LRU 缓存
**题目链接**：https://leetcode.cn/problems/lru-cache/
**题目描述**：请你设计并实现一个满足 LRU (最近最少使用) 缓存约束的数据结构。
**解题思路**：
- 使用哈希表 + 双向链表实现LRU缓存
- 哈希表提供O(1)的查找时间复杂度
- 双向链表维护使用顺序，支持O(1)的插入和删除操作
- 每次访问或更新节点时，将其移到链表头部
- 当缓存满时，删除链表尾部的节点
**算法优化**：
- 在Java中可以直接使用LinkedHashMap实现
- 在C++中可以结合unordered_map和list实现
**时间复杂度**：O(1) - 所有操作均为O(1)
**空间复杂度**：O(capacity)
**Java解法**：
```java
class LRUCache {
    private final int capacity;
    private final LinkedHashMap<Integer, Integer> cache;
    
    public LRUCache(int capacity) {
        this.capacity = capacity;
        // 第三个参数为true表示按访问顺序排序
        this.cache = new LinkedHashMap<Integer, Integer>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
                return size() > capacity;
            }
        };
    }
    
    public int get(int key) {
        return cache.getOrDefault(key, -1);
    }
    
    public void put(int key, int value) {
        cache.put(key, value);
    }
}
```

**Python解法**：
```python
from collections import OrderedDict

class LRUCache:
    def __init__(self, capacity: int):
        self.capacity = capacity
        self.cache = OrderedDict()
    
    def get(self, key: int) -> int:
        if key not in self.cache:
            return -1
        # 将访问的元素移到末尾（最近使用）
        self.cache.move_to_end(key)
        return self.cache[key]
    
    def put(self, key: int, value: int) -> None:
        if key in self.cache:
            # 如果键已存在，更新值并移到末尾
            self.cache.move_to_end(key)
        elif len(self.cache) >= self.capacity:
            # 如果缓存已满，删除最久未使用的元素（字典开头）
            self.cache.popitem(last=False)
        # 添加新元素到末尾
        self.cache[key] = value
```

**C++解法**：
```cpp
#include <unordered_map>
#include <list>
using namespace std;

class LRUCache {
private:
    int capacity;
    // 使用list存储键值对，便于快速插入和删除
    list<pair<int, int>> cache;
    // 使用哈希表映射键到list中的迭代器，实现O(1)查找
    unordered_map<int, list<pair<int, int>>::iterator> map;
    
    // 将节点移到列表头部（最近使用）
    void moveToHead(int key, int value) {
        // 先删除旧节点
        if (map.find(key) != map.end()) {
            cache.erase(map[key]);
        }
        // 在头部插入新节点
        cache.push_front({key, value});
        map[key] = cache.begin();
    }
    
public:
    LRUCache(int capacity) {
        this->capacity = capacity;
    }
    
    int get(int key) {
        if (map.find(key) == map.end()) {
            return -1;
        }
        // 获取值并移到头部
        int value = map[key]->second;
        moveToHead(key, value);
        return value;
    }
    
    void put(int key, int value) {
        if (map.find(key) != map.end() || map.size() >= capacity) {
            if (map.find(key) == map.end()) {
                // 缓存已满，删除尾部元素（最久未使用）
                int lastKey = cache.back().first;
                cache.pop_back();
                map.erase(lastKey);
            }
        }
        // 添加或更新节点
        moveToHead(key, value);
    }
};
```

### 11. LeetCode 447. 回旋镖的数量
**题目链接**：https://leetcode.cn/problems/number-of-boomerangs/
**题目描述**：给定平面上 n 对 互不相同 的点 points ，其中 points[i] = [xi, yi] 。回旋镖 是由点 (i, j, k) 表示的元组，其中 i 和 j 之间的距离和 i 和 k 之间的距离相等（需要考虑元组的顺序）。
**解题思路**：
- 遍历每个点，将其作为回旋镖的中心点
- 使用哈希表统计其他所有点到该中心点的距离出现的次数
- 对于每个距离，如果出现次数为m，则可以组成m*(m-1)个回旋镖
**算法优化**：
- 预先计算距离的平方，避免浮点数精度问题
**时间复杂度**：O(n²)，其中n是点的数量
**空间复杂度**：O(n)
**Java解法**：
```java
public int numberOfBoomerangs(int[][] points) {
    int result = 0;
    
    for (int i = 0; i < points.length; i++) {
        Map<Integer, Integer> distanceMap = new HashMap<>();
        
        for (int j = 0; j < points.length; j++) {
            if (i == j) continue;
            
            int dx = points[i][0] - points[j][0];
            int dy = points[i][1] - points[j][1];
            int distance = dx * dx + dy * dy;
            
            distanceMap.put(distance, distanceMap.getOrDefault(distance, 0) + 1);
        }
        
        for (int count : distanceMap.values()) {
            result += count * (count - 1);
        }
    }
    
    return result;
}
```

**Python解法**：
```python
def numberOfBoomerangs(points: list[list[int]]) -> int:
    result = 0
    
    for i in range(len(points)):
        distance_map = {}
        
        for j in range(len(points)):
            if i == j:
                continue
            
            dx = points[i][0] - points[j][0]
            dy = points[i][1] - points[j][1]
            distance = dx * dx + dy * dy
            
            distance_map[distance] = distance_map.get(distance, 0) + 1
        
        for count in distance_map.values():
            result += count * (count - 1)
    
    return result
```

**C++解法**：
```cpp
#include <vector>
#include <unordered_map>
using namespace std;

int numberOfBoomerangs(vector<vector<int>>& points) {
    int result = 0;
    
    for (int i = 0; i < points.size(); i++) {
        unordered_map<int, int> distance_map;
        
        for (int j = 0; j < points.size(); j++) {
            if (i == j) continue;
            
            int dx = points[i][0] - points[j][0];
            int dy = points[i][1] - points[j][1];
            int distance = dx * dx + dy * dy;
            
            distance_map[distance]++;
        }
        
        for (auto& pair : distance_map) {
            result += pair.second * (pair.second - 1);
        }
    }
    
    return result;
}
```

### 12. LeetCode 560. 和为K的子数组
**题目链接**：https://leetcode.cn/problems/subarray-sum-equals-k/
**题目描述**：给你一个整数数组 nums 和一个整数 k ，请你统计并返回 该数组中和为 k 的连续子数组的个数 。
**解题思路**：
- 使用前缀和 + 哈希表
- 前缀和数组preSum[i]表示前i个元素的和
- 对于位置j，我们需要找到有多少个i < j满足preSum[j] - preSum[i] = k
- 使用哈希表存储前缀和出现的次数
**算法优化**：
- 可以不需要显式计算前缀和数组，而是使用一个变量动态计算
**时间复杂度**：O(n)
**空间复杂度**：O(n)
**Java解法**：
```java
public int subarraySum(int[] nums, int k) {
    Map<Integer, Integer> prefixSumCount = new HashMap<>();
    prefixSumCount.put(0, 1); // 初始前缀和为0的出现次数为1
    
    int prefixSum = 0;
    int count = 0;
    
    for (int num : nums) {
        prefixSum += num;
        // 查找前缀和为prefixSum - k的次数
        count += prefixSumCount.getOrDefault(prefixSum - k, 0);
        // 更新当前前缀和的次数
        prefixSumCount.put(prefixSum, prefixSumCount.getOrDefault(prefixSum, 0) + 1);
    }
    
    return count;
}
```

**Python解法**：
```python
def subarraySum(nums: list[int], k: int) -> int:
    from collections import defaultdict
    prefix_sum_count = defaultdict(int)
    prefix_sum_count[0] = 1  # 初始前缀和为0的出现次数为1
    
    prefix_sum = 0
    count = 0
    
    for num in nums:
        prefix_sum += num
        # 查找前缀和为prefix_sum - k的次数
        count += prefix_sum_count.get(prefix_sum - k, 0)
        # 更新当前前缀和的次数
        prefix_sum_count[prefix_sum] += 1
    
    return count
```

**C++解法**：
```cpp
#include <vector>
#include <unordered_map>
using namespace std;

int subarraySum(vector<int>& nums, int k) {
    unordered_map<int, int> prefix_sum_count;
    prefix_sum_count[0] = 1; // 初始前缀和为0的出现次数为1
    
    int prefix_sum = 0;
    int count = 0;
    
    for (int num : nums) {
        prefix_sum += num;
        // 查找前缀和为prefix_sum - k的次数
        if (prefix_sum_count.find(prefix_sum - k) != prefix_sum_count.end()) {
            count += prefix_sum_count[prefix_sum - k];
        }
        // 更新当前前缀和的次数
        prefix_sum_count[prefix_sum]++;
    }
    
    return count;
}
```

### 13. LeetCode 205. 同构字符串
**题目链接**：https://leetcode.cn/problems/isomorphic-strings/
**题目描述**：给定两个字符串 s 和 t，判断它们是否是同构的。如果 s 中的字符可以按某种映射关系替换得到 t ，那么这两个字符串是同构的。
**解题思路**：
- 使用两个哈希表，分别存储s到t的映射和t到s的映射
- 遍历两个字符串，检查映射是否一致
- 如果发现不一致的映射关系，返回false
**算法优化**：
- 可以使用数组代替哈希表，因为字符集有限
**时间复杂度**：O(n)
**空间复杂度**：O(k)，k是字符集大小
**Java解法**：
```java
public boolean isIsomorphic(String s, String t) {
    if (s.length() != t.length()) {
        return false;
    }
    
    Map<Character, Character> sToT = new HashMap<>();
    Map<Character, Character> tToS = new HashMap<>();
    
    for (int i = 0; i < s.length(); i++) {
        char sc = s.charAt(i);
        char tc = t.charAt(i);
        
        if (sToT.containsKey(sc)) {
            if (sToT.get(sc) != tc) {
                return false;
            }
        } else {
            sToT.put(sc, tc);
        }
        
        if (tToS.containsKey(tc)) {
            if (tToS.get(tc) != sc) {
                return false;
            }
        } else {
            tToS.put(tc, sc);
        }
    }
    
    return true;
}
```

**Python解法**：
```python
def isIsomorphic(s: str, t: str) -> bool:
    if len(s) != len(t):
        return False
    
    s_to_t = {}
    t_to_s = {}
    
    for sc, tc in zip(s, t):
        if sc in s_to_t:
            if s_to_t[sc] != tc:
                return False
        else:
            s_to_t[sc] = tc
        
        if tc in t_to_s:
            if t_to_s[tc] != sc:
                return False
        else:
            t_to_s[tc] = sc
    
    return True
```

**C++解法**：
```cpp
#include <unordered_map>
#include <string>
using namespace std;

bool isIsomorphic(string s, string t) {
    if (s.length() != t.length()) {
        return false;
    }
    
    unordered_map<char, char> s_to_t;
    unordered_map<char, char> t_to_s;
    
    for (int i = 0; i < s.length(); i++) {
        char sc = s[i];
        char tc = t[i];
        
        if (s_to_t.find(sc) != s_to_t.end()) {
            if (s_to_t[sc] != tc) {
                return false;
            }
        } else {
            s_to_t[sc] = tc;
        }
        
        if (t_to_s.find(tc) != t_to_s.end()) {
            if (t_to_s[tc] != sc) {
                return false;
            }
        } else {
            t_to_s[tc] = sc;
        }
    }
    
    return true;
}
```

### 14. LeetCode 290. 单词规律
**题目链接**：https://leetcode.cn/problems/word-pattern/
**题目描述**：给定一种规律 pattern 和一个字符串 s ，判断 s 是否遵循相同的规律。
**解题思路**：
- 将字符串s按空格分割成单词数组
- 使用两个哈希表，分别存储pattern字符到单词的映射和单词到pattern字符的映射
- 遍历pattern和单词数组，检查映射是否一致
**算法优化**：
- 可以先检查pattern长度和单词数量是否一致，不一致直接返回false
**时间复杂度**：O(n)
**空间复杂度**：O(m)，m是不同单词的数量
**Java解法**：
```java
public boolean wordPattern(String pattern, String s) {
    String[] words = s.split(" ");
    if (pattern.length() != words.length) {
        return false;
    }
    
    Map<Character, String> charToWord = new HashMap<>();
    Map<String, Character> wordToChar = new HashMap<>();
    
    for (int i = 0; i < pattern.length(); i++) {
        char c = pattern.charAt(i);
        String word = words[i];
        
        if (charToWord.containsKey(c)) {
            if (!charToWord.get(c).equals(word)) {
                return false;
            }
        } else {
            if (wordToChar.containsKey(word)) {
                return false;
            }
            charToWord.put(c, word);
            wordToChar.put(word, c);
        }
    }
    
    return true;
}
```

**Python解法**：
```python
def wordPattern(pattern: str, s: str) -> bool:
    words = s.split()
    if len(pattern) != len(words):
        return False
    
    char_to_word = {}
    word_to_char = {}
    
    for c, word in zip(pattern, words):
        if c in char_to_word:
            if char_to_word[c] != word:
                return False
        else:
            if word in word_to_char:
                return False
            char_to_word[c] = word
            word_to_char[word] = c
    
    return True
```

**C++解法**：
```cpp
bool wordPattern(string pattern, string s) {
    vector<string> words;
    stringstream ss(s);
    string word;
    while (ss >> word) {
        words.push_back(word);
    }
    
    if (pattern.size() != words.size()) {
        return false;
    }
    
    unordered_map<char, string> char_to_word;
    unordered_map<string, char> word_to_char;
    
    for (int i = 0; i < pattern.size(); i++) {
        char c = pattern[i];
        string w = words[i];
        
        if (char_to_word.find(c) != char_to_word.end()) {
            if (char_to_word[c] != w) {
                return false;
            }
        } else {
            if (word_to_char.find(w) != word_to_char.end()) {
                return false;
            }
            char_to_word[c] = w;
            word_to_char[w] = c;
        }
    }
    
    return true;
}
```

### 15. LeetCode 242. 有效的字母异位词
**题目链接**：https://leetcode.cn/problems/valid-anagram/
**题目描述**：给定两个字符串 s 和 t ，编写一个函数来判断 t 是否是 s 的字母异位词。
**解题思路**：
- 使用哈希表统计第一个字符串中每个字符出现的次数
- 遍历第二个字符串，对每个字符减少对应的计数
- 最后检查所有计数是否为0
**算法优化**：
- 可以使用数组代替哈希表，因为字符集有限
- 可以先检查两个字符串长度是否相同，不同直接返回false
**时间复杂度**：O(n)
**空间复杂度**：O(k)，k是字符集大小
**Java解法**：
```java
public boolean isAnagram(String s, String t) {
    if (s.length() != t.length()) {
        return false;
    }
    
    int[] count = new int[26];
    for (char c : s.toCharArray()) {
        count[c - 'a']++;
    }
    
    for (char c : t.toCharArray()) {
        count[c - 'a']--;
        if (count[c - 'a'] < 0) {
            return false;
        }
    }
    
    return true;
}
```

**Python解法**：
```python
def isAnagram(s: str, t: str) -> bool:
    if len(s) != len(t):
        return False
    
    count = [0] * 26
    for c in s:
        count[ord(c) - ord('a')] += 1
    
    for c in t:
        count[ord(c) - ord('a')] -= 1
        if count[ord(c) - ord('a')] < 0:
            return False
    
    return True
```

**C++解法**：
```cpp
bool isAnagram(string s, string t) {
    if (s.length() != t.length()) {
        return false;
    }
    
    int count[26] = {0};
    for (char c : s) {
        count[c - 'a']++;
    }
    
    for (char c : t) {
        count[c - 'a']--;
        if (count[c - 'a'] < 0) {
            return false;
        }
    }
    
    return true;
}
```

这些哈希查找题目涵盖了哈希表的各种应用场景，包括：
1. 两数之和问题（键值对存储）
2. 字母异位词分组（特征提取）
3. 最长连续序列（集合查找）
4. 重复元素检测（去重）
5. 频率统计问题（计数）
6. 并查集应用（关系维护）
7. 嵌套哈希结构（多级索引）
8. 缓存实现（LRU算法）
9. 前缀和优化（子数组和问题）
10. 映射一致性检查（同构问题）

通过这些题目的学习，您可以掌握哈希表在不同场景下的应用技巧，提高解决算法问题的能力。

## AC自动机（Aho-Corasick Automaton）详解

## 算法简介

AC自动机（Aho-Corasick Automaton）是一种用于多模式字符串匹配的高效算法，由贝尔实验室的 Alfred V. Aho 和 Margaret J. Corasick 在1975年提出。它是KMP算法和Trie树的结合体，能够在O(n + m + z)的时间复杂度内完成匹配，其中n是文本长度，m是所有模式串的总长度，z是匹配结果的数量。

## 算法核心思想

AC自动机的构建过程分为三个步骤：

1. **构建Trie树**：将所有模式串插入到Trie树中
2. **构建失配指针（fail指针）**：类似KMP算法中的next数组，当匹配失败时跳转到合适的节点
3. **文本匹配**：在文本中进行匹配，利用fail指针避免回溯

## 时间复杂度分析

1. **构建Trie树**：O(∑|Pi|)，其中Pi是第i个模式串
2. **构建fail指针**：O(∑|Pi|)
3. **文本匹配**：O(|T|)，其中T是文本串
4. **总时间复杂度**：O(∑|Pi| + |T|)

## 空间复杂度

O(∑|Pi| × |Σ|)，其中Σ是字符集大小

## 适用场景

1. 敏感词过滤
2. 生物信息学中的DNA序列匹配
3. 网络入侵检测系统
4. 多关键词搜索

## 相关题目

### 1. 洛谷P3808 【模板】AC自动机（简单版）
**题目链接**：https://www.luogu.com.cn/problem/P3808
**题目描述**：给定n个模式串和1个文本串，求有多少个模式串在文本串里出现过
**解题思路**：
- 构建AC自动机，将所有模式串插入到Trie树中
- 构建失配指针
- 在文本串中进行匹配，统计匹配到的不同模式串数量
**算法优化**：使用数组实现Trie树以提高访问效率，避免重复计算
**时间复杂度**：O(∑|Pi| + |T|)，其中Pi是第i个模式串，T是文本串
**空间复杂度**：O(∑|Pi| × |Σ|)，其中Σ是字符集大小

### 2. 洛谷P3796 【模板】AC自动机（加强版）
**题目链接**：https://www.luogu.com.cn/problem/P3796
**题目描述**：有N个由小写字母组成的模式串以及一个文本串T。每个模式串可能会在文本串中出现多次。请找出最频繁出现的模式串，输出出现次数和模式串本身（可能有多个）
**解题思路**：
- 构建AC自动机，将所有模式串插入到Trie树中
- 构建失配指针
- 在文本串中进行匹配，使用fail树的后序遍历统计每个模式串的出现次数
- 找出出现次数最多的模式串
**算法优化**：使用链式前向星构建fail树，通过后序遍历累加次数
**时间复杂度**：O(∑|Pi| + |T| + N)
**空间复杂度**：O(∑|Pi| × |Σ| + N)

### 3. 洛谷P5357 【模板】AC自动机（二次加强版）
**题目链接**：https://www.luogu.com.cn/problem/P5357
**题目描述**：给你一个文本串S和n个模式串Ti，分别求出每个模式串在文本串中出现的次数
**解题思路**：
- 构建AC自动机，将所有模式串插入到Trie树中并记录每个模式串的结尾节点
- 构建失配指针
- 在文本串中进行匹配，标记所有经过的节点
- 使用fail树的后序遍历统计每个模式串的出现次数
**算法优化**：使用非递归方式进行后序遍历，避免栈溢出（Java实现中尤为重要）
**时间复杂度**：O(∑|Pi| + |T|)
**空间复杂度**：O(∑|Pi| × |Σ|)

### 4. LeetCode 1032. Stream of Characters
**题目链接**：https://leetcode.com/problems/stream-of-characters/
**题目描述**：设计一个算法，接收一个字符流，并检查这些字符的后缀是否是字符串数组words中的一个字符串
**解题思路**：
- 将所有模式串反转后插入到Trie树中（因为需要检查后缀）
- 构建AC自动机
- 维护当前输入字符的逆序序列，每次查询时在AC自动机中进行匹配
**算法优化**：只需要维护最近的max_length个字符（max_length为最长模式串的长度）
**时间复杂度**：
  - 初始化：O(∑|Pi|)
  - 查询操作：O(1)（每个字符的处理时间为O(1)）
**空间复杂度**：O(∑|Pi| × |Σ|)

### 5. POJ 1204 Word Puzzles
**题目链接**：http://poj.org/problem?id=1204
**题目描述**：给一个字母矩阵和一些字符串，求字符串在矩阵中出现的位置及其方向
**解题思路**：
- 构建AC自动机，将所有模式串插入到Trie树中
- 构建失配指针
- 在矩阵的8个方向上分别进行匹配，记录每个模式串的起始位置和方向
**算法优化**：
- 可以通过预处理每个方向的可能起始点，减少不必要的匹配
- 使用数组记录结果，避免重复处理
**时间复杂度**：O(∑|Pi| + L×C×8×max(|Pi|))，其中L是行数，C是列数
**空间复杂度**：O(∑|Pi| × |Σ| + W)，其中W是单词数量

### 6. ZOJ 3430 Detect the Virus
**题目链接**：https://zoj.pintia.cn/problem-sets/91827364500/problems/91827364512
**题目描述**：给定一些病毒的DNA序列（可能包含转义字符），然后给一些人的DNA序列，问每个人的DNA是否包含任何病毒序列
**解题思路**：
- 解析输入的病毒序列，处理转义字符
- 构建AC自动机，将解析后的病毒序列插入到Trie树中
- 构建失配指针
- 对每个人的DNA序列进行匹配，检查是否包含任何病毒序列
**算法优化**：
- 高效的转义字符处理
- 一旦发现病毒序列就立即返回，无需继续匹配
**时间复杂度**：O(∑|Vi| + ∑|Di|)，其中Vi是第i个病毒序列，Di是第i个人的DNA序列
**空间复杂度**：O(∑|Vi| × |Σ|)

### 7. HDU 2222 Keywords Search
**题目链接**：http://acm.hdu.edu.cn/showproblem.php?pid=2222
**题目描述**：给定一些单词和一个字符串，求有多少单词在字符串中出现过
**解题思路**：
- 构建AC自动机，将所有模式串插入到Trie树中
- 构建失配指针
- 在文本串中进行匹配，统计匹配到的不同单词数量
**算法优化**：使用BFS遍历Trie树，将匹配次数传递给fail节点
**时间复杂度**：O(∑|Pi| + |T|)
**空间复杂度**：O(∑|Pi| × |Σ|)

### 8. HDU 2896 病毒侵袭
**题目链接**：http://acm.hdu.edu.cn/showproblem.php?pid=2896
**题目描述**：给定一些病毒的DNA序列，然后给一些网站的DNA序列，问每个网站包含多少种不同的病毒DNA序列
**解题思路**：
- 构建AC自动机，将所有病毒序列插入到Trie树中并为每个病毒分配唯一标识
- 构建失配指针
- 对每个网站的DNA序列进行匹配，使用集合记录匹配到的不同病毒
- 输出每个网站包含的不同病毒数量和病毒列表
**算法优化**：使用布尔数组标记已访问的病毒节点，避免重复计数
**时间复杂度**：O(∑|Vi| + ∑|Si|)，其中Vi是第i个病毒序列，Si是第i个网站的DNA序列
**空间复杂度**：O(∑|Vi| × |Σ| + V)，其中V是病毒数量

### 9. LeetCode 816. Ambiguous Coordinates
**题目链接**：https://leetcode.com/problems/ambiguous-coordinates/
**题目描述**：给定一个字符串S，它表示一个坐标，格式为"(x,y)"，其中x和y都是整数。我们可以在任意位置（包括开头和结尾）插入小数点，只要得到的小数是有效的。求所有可能的有效坐标。
**解题思路（AC自动机应用思路）**：
- 虽然这道题可以用暴力枚举解决，但也可以利用AC自动机的思想来识别有效的数字模式
- 构建一个自动机，包含所有有效的数字模式（整数、小数）
- 对输入的数字部分进行处理，识别所有可能的有效分割
**算法优化**：预处理所有可能的有效数字模式，避免重复判断
**时间复杂度**：O(n³)，其中n是字符串长度
**空间复杂度**：O(n²)

### 10. Codeforces 963D Frequency of String
**题目链接**：https://codeforces.com/problemset/problem/963/D
**题目描述**：给定一个字符串s和q个查询，每个查询包含一个字符串t和一个整数k。对于每个查询，找到t在s中第k次出现的位置，如果不存在则输出-1。
**解题思路**：
- 构建AC自动机，将所有查询的t插入到Trie树中
- 构建失配指针
- 预处理字符串s，记录每个查询t的所有出现位置
- 对每个查询，直接返回第k个出现位置
**算法优化**：预处理所有查询的结果，避免重复计算
**时间复杂度**：O(|s| + ∑|ti| + q)
**空间复杂度**：O(∑|ti| × |Σ| + |s|)

### 11. SPOJ MANDRAKE
**题目链接**：https://www.spoj.com/problems/MANDRAKE/
**题目描述**：给定多个模式串和一个文本串，求有多少个模式串在文本串中出现过，并且每个模式串的出现次数至少为k次。
**解题思路**：
- 构建AC自动机，将所有模式串插入到Trie树中
- 构建失配指针
- 在文本串中进行匹配，统计每个模式串的出现次数
- 筛选出出现次数至少为k次的模式串
**算法优化**：使用后序遍历fail树的方式高效统计每个模式串的出现次数
**时间复杂度**：O(∑|Pi| + |T| + N)
**空间复杂度**：O(∑|Pi| × |Σ| + N)

## 工程化考量

### 1. 内存优化
- **存储结构选择**：根据字符集大小选择合适的存储方式
  - 对于有限字符集（如小写字母）：使用固定大小的数组存储子节点指针，访问效率O(1)
  - 对于大字符集或Unicode：使用HashMap存储子节点，节省空间但访问时间略高
- **内存池技术**：预先分配节点池，避免频繁的内存分配和释放
- **压缩表示**：对于稀疏的Trie树，可以使用更紧凑的数据结构表示

### 2. 性能优化
- **预处理优化**：
  - 去重模式串，避免重复处理
  - 按长度排序模式串，优先处理短模式串
- **算法优化**：
  - 构建fail指针时使用BFS，一次性处理所有节点
  - 匹配过程中使用跳转表记录，减少重复计算
  - 对于大规模文本，可以使用滑动窗口技术限制匹配范围
- **常数项优化**：
  - 避免递归调用，使用非递归实现
  - 内联频繁调用的小函数
  - 减少不必要的对象创建

### 3. 异常处理与鲁棒性
- **边界场景处理**：
  - 空模式串或空文本串的处理
  - 极大模式串（接近内存限制）的处理
  - 特殊字符和非法输入的处理
- **异常防御**：
  - 输入参数验证
  - 内存溢出检测
  - 超时处理机制
- **错误恢复**：
  - 部分匹配失败时的优雅降级
  - 异常捕获和日志记录

### 4. 多线程与并发
- **并行处理**：
  - 文本分块并行匹配
  - 使用线程池管理并行任务
- **线程安全**：
  - AC自动机构建完成后是只读的，天然线程安全
  - 对于动态更新的场景，需要加锁保护
- **无锁设计**：使用Copy-On-Write策略更新自动机

### 5. 持久化与部署
- **序列化与反序列化**：
  - 构建好的自动机可以序列化为二进制格式
  - 使用高效的序列化库如Protocol Buffers
- **增量更新**：支持在不重建整个自动机的情况下添加新模式串
- **热部署**：支持运行时替换自动机实例

### 6. 跨语言实现差异
- **Java实现注意事项**：
  - 使用ArrayList替代数组以避免固定大小限制
  - 注意处理堆内存限制，对于大规模数据可能需要调整JVM参数
  - 使用char[]而非String进行字符处理，减少内存开销
- **C++实现注意事项**：
  - 使用vector和智能指针管理内存，避免内存泄漏
  - 对于性能敏感场景，考虑使用内存池和自定义内存分配器
  - 注意字符串编码问题（UTF-8 vs ASCII）
- **Python实现注意事项**：
  - 使用字典嵌套实现Trie树，但注意内存消耗
  - 对于大规模数据，考虑使用numpy加速
  - 使用生成器模式处理大数据流，避免一次性加载

### 7. 单元测试与调试
- **测试用例**：
  - 基础功能测试：空输入、单模式串、多模式串
  - 边界测试：极大极小输入、重复模式串
  - 性能测试：大规模数据下的响应时间
- **调试技巧**：
  - 打印中间状态和匹配过程
  - 使用断言验证关键步骤
  - 可视化Trie树结构和fail指针连接

## 与机器学习和深度学习的联系

### 1. 特征工程
- **关键词提取**：AC自动机用于从文本中高效提取预定义的关键词集合
- **文本模式识别**：识别文本中的特定模式作为特征
- **情感词典匹配**：快速匹配情感词典中的词汇，计算情感得分

### 2. 自然语言处理
- **命名实体识别**：使用AC自动机匹配实体词典
- **分词辅助**：结合词表进行高效分词
- **文本规范化**：识别并替换特定文本模式
- **拼写纠错**：预构建常见错误模式的自动机

### 3. 信息检索
- **查询扩展**：使用AC自动机匹配查询词的变体和同义词
- **文档过滤**：快速过滤包含特定关键词的文档
- **搜索加速**：在倒排索引中使用AC自动机进行多模式匹配

### 4. 安全领域应用
- **病毒特征匹配**：如题目ZOJ 3430和HDU 2896所示
- **敏感词过滤**：构建敏感词的AC自动机进行内容审核
- **入侵检测**：匹配网络流量中的攻击特征

### 5. 推荐系统
- **兴趣标签匹配**：匹配用户行为中的兴趣标签
- **内容分类**：通过关键词匹配对内容进行分类
- **相似内容推荐**：基于关键词匹配计算内容相似度

## 深入学习指南

### 1. 算法变体与拓展
- **扩展AC自动机**：支持动态添加和删除模式串
- **广义后缀自动机**：与AC自动机的比较与应用场景差异
- **双向AC自动机**：同时处理前缀和后缀匹配

### 2. 性能调优进阶
- **内存占用优化**：压缩Trie树表示
- **缓存优化**：利用CPU缓存局部性原理
- **并行算法设计**：更高效的并行匹配策略

### 3. 高级应用场景
- **多模式正则表达式匹配**：结合正则表达式和AC自动机
- **DNA序列匹配**：生物信息学中的应用
- **网络包过滤**：高性能网络设备中的应用

### 4. 相关算法学习
- **KMP算法**：单模式匹配的基础
- **后缀数组/后缀树**：字符串索引的高级数据结构
- **字典树（Trie）**：AC自动机的基础结构
- **有限状态自动机**：理论基础与实现技术

## 代码实现说明

本目录下提供了三种语言的AC自动机实现：

1. **Java版本**：
   - Code01_ACAM.java 和 Code02_Counting.java（针对具体题目的实现）
   - Code04_StreamOfCharacters.java（LeetCode 1032）
   - Code05_WordPuzzles.java（POJ 1204）
   - Code06_DetectVirus.java（ZOJ 3430）
   - Code07_KeywordsSearch.java（HDU 2222）
   - Code08_VirusInvasion.java（HDU 2896）
   - Code09_ExtendedACAM.java（扩展题目合集）
2. **C++版本**：
   - Code03_ACAM_Template.cpp（模板实现）
   - Code04_StreamOfCharacters.cpp（LeetCode 1032）
   - Code05_WordPuzzles.cpp（POJ 1204）
   - Code06_DetectVirus.cpp（ZOJ 3430）
   - Code07_KeywordsSearch.cpp（HDU 2222）
   - Code08_VirusInvasion.cpp（HDU 2896）
   - Code09_ExtendedACAM.cpp（扩展题目合集）
3. **Python版本**：
   - Code03_ACAM_Template.py（模板实现）
   - Code04_StreamOfCharacters.py（LeetCode 1032）
   - Code05_WordPuzzles.py（POJ 1204）
   - Code06_DetectVirus.py（ZOJ 3430）
   - Code07_KeywordsSearch.py（HDU 2222）
   - Code08_VirusInvasion.py（HDU 2896）
   - Code09_ExtendedACAM.py（扩展题目合集）

其中Code01_ACAM.java和Code02_Counting.java是针对具体题目的实现，Code03_ACAM_Template.*是通用模板实现，其他文件是针对具体OJ题目的实现。

## 新增扩展题目详解

### 12. 洛谷P4052 [JSOI2007] 文本生成器
**题目链接**：https://www.luogu.com.cn/problem/P4052
**题目描述**：给定n个模式串，求长度为m的至少包含一个模式串的字符串个数
**解题思路**：
- 使用AC自动机检测字符串是否包含模式串
- 使用动态规划计算满足条件的字符串个数
- 总字符串个数减去不包含任何模式串的字符串个数
**算法优化**：使用DP状态表示当前长度和AC自动机节点
**时间复杂度**：O(m × 节点数)
**空间复杂度**：O(m × 节点数)

### 13. Codeforces 963D Frequency of String
**题目链接**：https://codeforces.com/problemset/problem/963/D
**题目描述**：给定字符串s和q个查询，每个查询包含字符串t和整数k。求t在s中第k次出现的位置
**解题思路**：
- 构建AC自动机，将所有查询的t插入到Trie树中
- 预处理字符串s，记录每个查询t的所有出现位置
- 对每个查询，直接返回第k个出现位置
**算法优化**：预处理所有查询的结果，避免重复计算
**时间复杂度**：O(|s| + ∑|ti| + q)
**空间复杂度**：O(∑|ti| × |Σ| + |s|)

### 14. SPOJ MANDRAKE
**题目链接**：https://www.spoj.com/problems/MANDRAKE/
**题目描述**：给定多个模式串和一个文本串，求有多少个模式串在文本串中出现过，并且每个模式串的出现次数至少为k次
**解题思路**：
- 构建AC自动机，将所有模式串插入到Trie树中
- 构建失配指针
- 在文本串中进行匹配，统计每个模式串的出现次数
- 筛选出出现次数至少为k次的模式串
**算法优化**：使用后序遍历fail树的方式高效统计每个模式串的出现次数
**时间复杂度**：O(∑|Pi| + |T| + N)
**空间复杂度**：O(∑|Pi| × |Σ| + N)

### 15. LeetCode 816. Ambiguous Coordinates
**题目链接**：https://leetcode.com/problems/ambiguous-coordinates/
**题目描述**：给定一个字符串S，它表示一个坐标，格式为"(x,y)"，求所有可能的有效坐标
**解题思路（AC自动机应用思路）**：
- 虽然这道题可以用暴力枚举解决，但也可以利用AC自动机的思想来识别有效的数字模式
- 构建一个自动机，包含所有有效的数字模式（整数、小数）
- 对输入的数字部分进行处理，识别所有可能的有效分割
**算法优化**：预处理所有可能的有效数字模式，避免重复判断
**时间复杂度**：O(n³)
**空间复杂度**：O(n²)

### 16. HDU 3065 病毒侵袭持续中
**题目链接**：http://acm.hdu.edu.cn/showproblem.php?pid=3065
**题目描述**：统计每个病毒在文本中出现的次数
**解题思路**：
- 为每个病毒分配ID，构建AC自动机
- 在文本中进行匹配，统计每个病毒的出现次数
- 输出出现次数大于0的病毒及其次数
**算法优化**：使用拓扑排序汇总匹配次数
**时间复杂度**：O(∑|Vi| + |T|)
**空间复杂度**：O(∑|Vi| × |Σ|)

## 高级算法变体与优化

### 1. 双向AC自动机
**核心思想**：同时构建正向和反向的AC自动机，支持双向匹配
**适用场景**：需要同时检查前缀和后缀匹配的场景
**时间复杂度**：O(∑|Pi| + |T|)
**空间复杂度**：O(2 × ∑|Pi| × |Σ|)

### 2. 动态AC自动机
**核心思想**：支持动态添加和删除模式串，无需重建整个自动机
**适用场景**：模式串集合频繁变化的场景
**时间复杂度**：每次操作O(|P|)
**空间复杂度**：O(∑|Pi| × |Σ|)

### 3. 压缩AC自动机
**核心思想**：对Trie树进行路径压缩，减少节点数量
**适用场景**：内存受限的大规模模式串匹配
**时间复杂度**：O(∑|Pi| + |T|)
**空间复杂度**：O(∑|Pi|)（显著减少）

### 4. 并行AC自动机
**核心思想**：利用多核处理器并行处理文本的不同部分
**适用场景**：超大规模文本匹配
**时间复杂度**：O(∑|Pi| + |T|/p)，其中p是处理器数量
**空间复杂度**：O(∑|Pi| × |Σ|)

## 工程化最佳实践

### 1. 内存管理优化
- **预分配策略**：预先分配足够的内存池，避免频繁的内存分配
- **对象池技术**：重用Trie节点对象，减少GC压力
- **内存对齐**：优化数据结构的内存布局，提高缓存命中率

### 2. 性能监控与调优
- **热点分析**：使用性能分析工具定位瓶颈
- **缓存优化**：优化数据访问模式，提高局部性
- **算法选择**：根据数据特征选择合适的算法变体

### 3. 异常处理与容错
- **输入验证**：严格验证输入参数的有效性
- **错误恢复**：实现优雅的错误处理和恢复机制
- **边界测试**：充分测试各种边界情况和极端输入

### 4. 可配置性与扩展性
- **参数化配置**：支持字符集大小、最大节点数等参数配置
- **插件架构**：支持算法变体的动态加载和替换
- **接口设计**：提供清晰、稳定的API接口

## 跨语言实现对比

### Java实现特点
- **优势**：内存管理自动化，异常处理完善，生态丰富
- **劣势**：性能开销较大，内存占用较高
- **适用场景**：企业级应用，需要快速开发和稳定性的场景

### C++实现特点
- **优势**：性能最优，内存控制精细，系统级编程
- **劣势**：开发复杂度高，内存管理需要手动处理
- **适用场景**：高性能计算，嵌入式系统，游戏开发

### Python实现特点
- **优势**：开发效率高，代码简洁，生态丰富
- **劣势**：运行速度较慢，内存占用较大
- **适用场景**：快速原型开发，数据分析，脚本工具

## 测试与验证策略

### 1. 单元测试
- **基础功能测试**：验证AC自动机的基本功能正确性
- **边界测试**：测试各种边界情况和极端输入
- **性能测试**：验证算法的时间复杂度和空间复杂度

### 2. 集成测试
- **多算法对比**：与其他字符串匹配算法进行对比测试
- **大规模测试**：使用真实数据集进行大规模测试
- **稳定性测试**：长时间运行测试，验证系统稳定性

### 3. 基准测试
- **性能基准**：建立性能基准，监控算法性能变化
- **内存基准**：监控内存使用情况，优化内存占用
- **并发基准**：测试多线程环境下的性能表现

## 总结与展望

AC自动机作为一种经典的多模式字符串匹配算法，在文本处理、网络安全、生物信息学等领域有着广泛的应用。通过本目录的学习，您应该能够：

1. **深入理解AC自动机的原理和实现**
2. **掌握多种AC自动机的变体和优化技术**
3. **熟练运用AC自动机解决实际问题**
4. **具备工程化实现和优化的能力**

## 完整代码文件列表

### 基础实现文件
- `Code01_ACAM.java` - 基础AC自动机Java实现
- `Code02_ACAM.cpp` - 基础AC自动机C++实现  
- `Code03_ACAM_Template.py` - 基础AC自动机Python实现
- `Code04_StreamOfCharacters.java` - LeetCode 1032实现
- `Code05_WordPuzzles.java` - POJ 1204实现
- `Code06_DetectVirus.java` - ZOJ 3430实现
- `Code07_KeywordsSearch.java` - HDU 2222实现
- `Code08_VirusInvasion.java` - HDU 2896实现

### 扩展题目合集
- `Code09_ExtendedACAM.java` - 扩展题目Java实现
- `Code09_ExtendedACAM.cpp` - 扩展题目C++实现
- `Code09_ExtendedACAM.py` - 扩展题目Python实现

### 高级算法变体
- `Code10_AdvancedACAM.java` - 高级变体Java实现
- `Code10_AdvancedACAM.cpp` - 高级变体C++实现
- `Code10_AdvancedACAM.py` - 高级变体Python实现

### 实际应用实现
- `Code11_ACAM_Applications.java` - 实际应用Java实现
- `Code11_ACAM_Applications.cpp` - 实际应用C++实现
- `Code11_ACAM_Applications.py` - 实际应用Python实现

## 覆盖的算法平台题目

### LeetCode (力扣)
- 1032. 字符流
- 816. 模糊坐标（AC自动机应用思路）

### HDU (杭州电子科技大学)
- 2222. Keywords Search
- 2896. 病毒侵袭
- 3065. 病毒侵袭持续中

### POJ (北京大学)
- 1204. Word Puzzles

### ZOJ (浙江大学)
- 3430. Detect the Virus

### 洛谷 (Luogu)
- P4052 [JSOI2007] 文本生成器

### Codeforces
- 963D. Frequency of String

### SPOJ
- MANDRAKE

### Codeforces
- 346B. Lucky Common Subsequence

## 算法复杂度分析总结

### 时间复杂度
- **构建阶段**：O(∑|Pi|) - 与所有模式串总长度成正比
- **匹配阶段**：O(|T|) - 与文本长度成正比
- **总复杂度**：O(∑|Pi| + |T|)

### 空间复杂度
- **基础实现**：O(∑|Pi| × |Σ|) - 与字符集大小和模式串总长度相关
- **压缩优化**：O(∑|Pi|) - 路径压缩后显著减少
- **并行优化**：O(∑|Pi| × |Σ|) - 多线程处理增加少量开销

## 工程化最佳实践

### 1. 代码质量保证
- **详细注释**：每个文件都有完整的注释说明
- **错误处理**：完善的异常处理机制
- **边界测试**：覆盖各种边界情况
- **性能优化**：针对不同场景的优化策略

### 2. 跨语言实现对比
- **Java**：企业级应用，稳定性强，生态丰富
- **C++**：高性能计算，内存控制精细
- **Python**：开发效率高，代码简洁，适合快速原型

### 3. 测试验证策略
- **单元测试**：验证基本功能正确性
- **集成测试**：多算法对比测试
- **性能测试**：验证时间空间复杂度
- **边界测试**：极端输入情况处理

## 学习路径建议

### 初级阶段（1-2周）
1. 理解AC自动机基本原理
2. 掌握基础实现（Code01-Code03）
3. 完成经典题目练习（Code04-Code08）

### 中级阶段（2-3周）
1. 学习扩展题目实现（Code09）
2. 掌握高级算法变体（Code10）
3. 理解工程化考量

### 高级阶段（3-4周）
1. 探索实际应用场景（Code11）
2. 研究性能优化技术
3. 参与开源项目贡献

## 面试准备要点

### 基础概念
- AC自动机与Trie树、KMP算法的关系
- fail指针的作用和构建方法
- 时间复杂度分析

### 算法实现
- 能够手写AC自动机核心代码
- 理解不同语言实现的差异
- 掌握优化技巧

### 工程实践
- 异常处理策略
- 性能优化方法
- 大规模数据处理经验

## 未来发展方向

### 理论研究
- 结合机器学习进行智能模式匹配
- 开发更高效的分布式AC自动机算法
- 探索量子计算在字符串匹配中的应用

### 工程应用
- 网络安全领域的深度应用
- 生物信息学的大规模序列分析
- 自然语言处理的实时处理需求

### 技术创新
- 硬件加速AC自动机实现
- 云原生架构下的分布式部署
- 边缘计算场景的优化适配

## 资源推荐

### 在线学习平台
- LeetCode、HDU、POJ等OJ平台
- Coursera、edX算法课程
- GitHub开源项目

### 经典书籍
- 《算法导论》- 字符串匹配章节
- 《编程珠玑》- 算法优化思想
- 《设计模式》- 工程化实践

### 社区资源
- Stack Overflow技术问答
- GitHub开源代码库
- 技术博客和论文

通过系统学习本目录内容，您将全面掌握AC自动机算法，具备解决复杂字符串匹配问题的能力，并为未来的算法研究和工程实践打下坚实基础！

**祝您学习顺利，算法精进！**

## 调试技巧

1. **打印中间过程**：在构建fail指针和匹配过程中打印关键信息
2. **用断言验证中间结果**：确保Trie树和fail指针构建正确
3. **性能退化的排查方法**：分析时间复杂度，检查是否有重复计算

## 总结

AC自动机是一种非常实用的字符串匹配算法，特别适用于多模式匹配场景。掌握其原理和实现对于解决相关问题非常有帮助。

本目录在原有基础上新增了5道经典AC自动机题目，涵盖了不同平台和应用场景：
1. **LeetCode 1032 Stream of Characters** - 字符流匹配问题
2. **POJ 1204 Word Puzzles** - 二维矩阵中的字符串匹配
3. **ZOJ 3430 Detect the Virus** - 编码解码与字符串匹配结合
4. **HDU 2222 Keywords Search** - 经典AC自动机模板题
5. **HDU 2896 病毒侵袭** - 网站安全检测应用

每道题目都提供了Java、C++、Python三种语言的完整实现，并包含详细的注释说明。