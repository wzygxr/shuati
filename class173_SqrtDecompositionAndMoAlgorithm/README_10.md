# 线性枚举算法专题

## 算法原理

线性枚举（Linear Enumeration），也称为穷举法或暴力搜索，是一种最基本、最直接的算法思想。它的核心思想是：

- 按照某种顺序（通常是从小到大或从左到右）依次检查所有可能的情况
- 对于满足条件的情况进行处理或记录
- 遍历完所有情况后，得到问题的解或结果

线性枚举的优点是实现简单、逻辑清晰，缺点是效率通常较低，时间复杂度往往为O(n)或更高。但在某些问题中，尤其是当问题规模较小时，线性枚举是一种非常实用的算法。

## 常见应用场景

1. **数组和链表的遍历处理**
2. **简单查找问题**
3. **计数问题**
4. **模拟过程问题**
5. **生成所有可能解**

## 经典算法题目

### 1. 两数之和

**题目链接**：https://leetcode.com/problems/two-sum/

**题目描述**：给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出和为目标值 target 的那两个整数，并返回它们的数组下标。

**解题思路**：
- 使用两层循环枚举所有可能的数对
- 对于每对数，检查它们的和是否等于目标值
- 如果找到符合条件的数对，返回它们的下标

**代码实现**：

**Java代码**：
```java
class Solution {
    public int[] twoSum(int[] nums, int target) {
        int n = nums.length;
        // 枚举所有可能的数对
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (nums[i] + nums[j] == target) {
                    return new int[] {i, j};
                }
            }
        }
        // 没有找到符合条件的数对
        return new int[] {};
    }
}
```

**Python代码**：
```python
class Solution:
    def twoSum(self, nums: List[int], target: int) -> List[int]:
        n = len(nums)
        # 枚举所有可能的数对
        for i in range(n):
            for j in range(i + 1, n):
                if nums[i] + nums[j] == target:
                    return [i, j]
        # 没有找到符合条件的数对
        return []
```

**C++代码**：
```cpp
class Solution {
public:
    vector<int> twoSum(vector<int>& nums, int target) {
        int n = nums.size();
        // 枚举所有可能的数对
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (nums[i] + nums[j] == target) {
                    return {i, j};
                }
            }
        }
        // 没有找到符合条件的数对
        return {};
    }
};
```

### 2. 最大子数组和

**题目链接**：https://leetcode.com/problems/maximum-subarray/

**题目描述**：给你一个整数数组 nums ，请你找出一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。

**解题思路**：
- 枚举所有可能的子数组
- 计算每个子数组的和，并记录最大值

**代码实现**：

**Java代码**：
```java
class Solution {
    public int maxSubArray(int[] nums) {
        int n = nums.length;
        int maxSum = Integer.MIN_VALUE;
        
        // 枚举子数组的起始位置
        for (int i = 0; i < n; i++) {
            int currentSum = 0;
            // 枚举子数组的结束位置
            for (int j = i; j < n; j++) {
                currentSum += nums[j];
                maxSum = Math.max(maxSum, currentSum);
            }
        }
        
        return maxSum;
    }
}
```

**Python代码**：
```python
class Solution:
    def maxSubArray(self, nums: List[int]) -> int:
        n = len(nums)
        max_sum = float('-inf')
        
        # 枚举子数组的起始位置
        for i in range(n):
            current_sum = 0
            # 枚举子数组的结束位置
            for j in range(i, n):
                current_sum += nums[j]
                if current_sum > max_sum:
                    max_sum = current_sum
        
        return max_sum
```

**C++代码**：
```cpp
class Solution {
public:
    int maxSubArray(vector<int>& nums) {
        int n = nums.size();
        int maxSum = INT_MIN;
        
        // 枚举子数组的起始位置
        for (int i = 0; i < n; i++) {
            int currentSum = 0;
            // 枚举子数组的结束位置
            for (int j = i; j < n; j++) {
                currentSum += nums[j];
                maxSum = max(maxSum, currentSum);
            }
        }
        
        return maxSum;
    }
};
```

### 3. 有效的括号

**题目链接**：https://leetcode.com/problems/valid-parentheses/

**题目描述**：给定一个只包括 '('，')'，'{'，'}'，'['，']' 的字符串 s ，判断字符串是否有效。

**解题思路**：
- 使用栈来匹配括号
- 线性枚举字符串中的每个字符
- 如果是左括号，入栈
- 如果是右括号，检查栈顶元素是否匹配

**代码实现**：

**Java代码**：
```java
import java.util.Stack;

class Solution {
    public boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();
        
        // 线性枚举每个字符
        for (char c : s.toCharArray()) {
            // 左括号入栈
            if (c == '(' || c == '{' || c == '[') {
                stack.push(c);
            } else {
                // 右括号，检查栈是否为空或栈顶是否匹配
                if (stack.isEmpty()) {
                    return false;
                }
                
                char top = stack.pop();
                if ((c == ')' && top != '(') ||
                    (c == '}' && top != '{') ||
                    (c == ']' && top != '[')) {
                    return false;
                }
            }
        }
        
        // 最后栈应该为空
        return stack.isEmpty();
    }
}
```

**Python代码**：
```python
class Solution:
    def isValid(self, s: str) -> bool:
        stack = []
        
        # 定义括号匹配关系
        brackets_map = {
            ')': '(',
            '}': '{',
            ']': '['
        }
        
        # 线性枚举每个字符
        for char in s:
            # 左括号入栈
            if char in '({[':
                stack.append(char)
            # 右括号，检查匹配
            elif char in ')}]':
                if not stack or stack.pop() != brackets_map[char]:
                    return False
        
        # 最后栈应该为空
        return len(stack) == 0
```

**C++代码**：
```cpp
#include <stack>
#include <unordered_map>
using namespace std;

class Solution {
public:
    bool isValid(string s) {
        stack<char> st;
        unordered_map<char, char> bracketsMap = {
            {')', '('},
            {'}', '{'},
            {']', '['}
        };
        
        // 线性枚举每个字符
        for (char c : s) {
            // 左括号入栈
            if (c == '(' || c == '{' || c == '[') {
                st.push(c);
            } else {
                // 右括号，检查栈是否为空或栈顶是否匹配
                if (st.empty() || st.top() != bracketsMap[c]) {
                    return false;
                }
                st.pop();
            }
        }
        
        // 最后栈应该为空
        return st.empty();
    }
};
```

### 4. 字符串中的第一个唯一字符

**题目链接**：https://leetcode.com/problems/first-unique-character-in-a-string/

**题目描述**：给定一个字符串 s ，找到它的第一个不重复的字符，并返回它的索引。如果不存在，则返回 -1。

**解题思路**：
- 第一次线性枚举：统计每个字符出现的次数
- 第二次线性枚举：找到第一个出现次数为1的字符

**代码实现**：

**Java代码**：
```java
class Solution {
    public int firstUniqChar(String s) {
        // 统计每个字符出现的次数
        int[] count = new int[26];
        for (char c : s.toCharArray()) {
            count[c - 'a']++;
        }
        
        // 找到第一个出现次数为1的字符
        for (int i = 0; i < s.length(); i++) {
            if (count[s.charAt(i) - 'a'] == 1) {
                return i;
            }
        }
        
        return -1;
    }
}
```

**Python代码**：
```python
class Solution:
    def firstUniqChar(self, s: str) -> int:
        # 统计每个字符出现的次数
        char_count = {}
        for char in s:
            char_count[char] = char_count.get(char, 0) + 1
        
        # 找到第一个出现次数为1的字符
        for i, char in enumerate(s):
            if char_count[char] == 1:
                return i
        
        return -1
```

**C++代码**：
```cpp
#include <unordered_map>
using namespace std;

class Solution {
public:
    int firstUniqChar(string s) {
        // 统计每个字符出现的次数
        unordered_map<char, int> charCount;
        for (char c : s) {
            charCount[c]++;
        }
        
        // 找到第一个出现次数为1的字符
        for (int i = 0; i < (int)s.length(); i++) {
            if (charCount[s[i]] == 1) {
                return i;
            }
        }
        
        return -1;
    }
};
```

### 5. 旋转数组

**题目链接**：https://leetcode.com/problems/rotate-array/

**题目描述**：给定一个数组，将数组中的元素向右移动 k 个位置，其中 k 是非负数。

**解题思路**：
- 线性枚举数组元素
- 计算每个元素移动后的新位置
- 使用一个临时数组来存储移动后的结果
- 将临时数组复制回原数组

**代码实现**：

**Java代码**：
```java
class Solution {
    public void rotate(int[] nums, int k) {
        int n = nums.length;
        k = k % n;  // 处理k大于n的情况
        
        int[] temp = new int[n];
        
        // 计算每个元素移动后的位置
        for (int i = 0; i < n; i++) {
            temp[(i + k) % n] = nums[i];
        }
        
        // 将临时数组复制回原数组
        System.arraycopy(temp, 0, nums, 0, n);
    }
}
```

**Python代码**：
```python
class Solution:
    def rotate(self, nums: List[int], k: int) -> None:
        """Do not return anything, modify nums in-place instead."""
        n = len(nums)
        k = k % n  # 处理k大于n的情况
        
        # 切片操作实现旋转
        nums[:] = nums[-k:] + nums[:-k]
```

**C++代码**：
```cpp
class Solution {
public:
    void rotate(vector<int>& nums, int k) {
        int n = nums.size();
        k = k % n;  // 处理k大于n的情况
        
        vector<int> temp(n);
        
        // 计算每个元素移动后的位置
        for (int i = 0; i < n; i++) {
            temp[(i + k) % n] = nums[i];
        }
        
        // 将临时数组复制回原数组
        nums = temp;
    }
};
```

### 6. 存在重复元素

**题目链接**：https://leetcode.com/problems/contains-duplicate/

**题目描述**：给定一个整数数组，判断是否存在重复元素。

**解题思路**：
- 线性枚举数组中的每个元素
- 使用哈希集合记录已经出现过的元素
- 如果当前元素已经在集合中，说明存在重复

**代码实现**：

**Java代码**：
```java
import java.util.HashSet;
import java.util.Set;

class Solution {
    public boolean containsDuplicate(int[] nums) {
        Set<Integer> seen = new HashSet<>();
        
        for (int num : nums) {
            if (seen.contains(num)) {
                return true;
            }
            seen.add(num);
        }
        
        return false;
    }
}
```

**Python代码**：
```python
class Solution:
    def containsDuplicate(self, nums: List[int]) -> bool:
        seen = set()
        
        for num in nums:
            if num in seen:
                return True
            seen.add(num)
        
        return False
```

**C++代码**：
```cpp
#include <unordered_set>
using namespace std;

class Solution {
public:
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
};
```

### 7. 加一

**题目链接**：https://leetcode.com/problems/plus-one/

**题目描述**：给定一个由 整数 组成的 非空 数组所表示的非负整数，在该数的基础上加一。

**解题思路**：
- 从数组的最后一个元素开始，线性枚举到第一个元素
- 对当前位加1，如果不需要进位，直接返回
- 如果需要进位，处理进位并继续枚举

**代码实现**：

**Java代码**：
```java
class Solution {
    public int[] plusOne(int[] digits) {
        int n = digits.length;
        
        // 从最后一位开始加1
        for (int i = n - 1; i >= 0; i--) {
            digits[i]++;
            // 如果不需要进位，直接返回
            if (digits[i] < 10) {
                return digits;
            }
            // 需要进位，当前位置0
            digits[i] = 0;
        }
        
        // 如果所有位都需要进位，创建新数组
        int[] newDigits = new int[n + 1];
        newDigits[0] = 1;
        return newDigits;
    }
}
```

**Python代码**：
```python
class Solution:
    def plusOne(self, digits: List[int]) -> List[int]:
        n = len(digits)
        
        # 从最后一位开始加1
        for i in range(n - 1, -1, -1):
            digits[i] += 1
            # 如果不需要进位，直接返回
            if digits[i] < 10:
                return digits
            # 需要进位，当前位置0
            digits[i] = 0
        
        # 如果所有位都需要进位，创建新数组
        return [1] + digits
```

**C++代码**：
```cpp
class Solution {
public:
    vector<int> plusOne(vector<int>& digits) {
        int n = digits.size();
        
        // 从最后一位开始加1
        for (int i = n - 1; i >= 0; i--) {
            digits[i]++;
            // 如果不需要进位，直接返回
            if (digits[i] < 10) {
                return digits;
            }
            // 需要进位，当前位置0
            digits[i] = 0;
        }
        
        // 如果所有位都需要进位，创建新数组
        vector<int> newDigits(n + 1, 0);
        newDigits[0] = 1;
        return newDigits;
    }
};
```

### 8. 移动零

**题目链接**：https://leetcode.com/problems/move-zeroes/

**题目描述**：给定一个数组 nums，编写一个函数将所有 0 移动到数组的末尾，同时保持非零元素的相对顺序。

**解题思路**：
- 线性枚举数组，用一个指针记录非零元素应该放置的位置
- 当遇到非零元素时，将其移动到指针位置，并将指针后移
- 最后，将指针之后的所有元素都设为0

**代码实现**：

**Java代码**：
```java
class Solution {
    public void moveZeroes(int[] nums) {
        int n = nums.length;
        int nonZeroPos = 0;  // 非零元素应该放置的位置
        
        // 第一步：将所有非零元素移到前面
        for (int i = 0; i < n; i++) {
            if (nums[i] != 0) {
                nums[nonZeroPos++] = nums[i];
            }
        }
        
        // 第二步：将剩下的位置填充为0
        for (int i = nonZeroPos; i < n; i++) {
            nums[i] = 0;
        }
    }
}
```

**Python代码**：
```python
class Solution:
    def moveZeroes(self, nums: List[int]) -> None:
        """Do not return anything, modify nums in-place instead."""
        n = len(nums)
        non_zero_pos = 0  # 非零元素应该放置的位置
        
        # 第一步：将所有非零元素移到前面
        for i in range(n):
            if nums[i] != 0:
                nums[non_zero_pos] = nums[i]
                non_zero_pos += 1
        
        # 第二步：将剩下的位置填充为0
        for i in range(non_zero_pos, n):
            nums[i] = 0
```

**C++代码**：
```cpp
class Solution {
public:
    void moveZeroes(vector<int>& nums) {
        int n = nums.size();
        int nonZeroPos = 0;  // 非零元素应该放置的位置
        
        // 第一步：将所有非零元素移到前面
        for (int i = 0; i < n; i++) {
            if (nums[i] != 0) {
                nums[nonZeroPos++] = nums[i];
            }
        }
        
        // 第二步：将剩下的位置填充为0
        for (int i = nonZeroPos; i < n; i++) {
            nums[i] = 0;
        }
    }
};
```

### 9. 合并两个有序数组

**题目链接**：https://leetcode.com/problems/merge-sorted-array/

**题目描述**：给你两个按 非递减顺序 排列的整数数组 nums1 和 nums2，另有两个整数 m 和 n ，分别表示 nums1 和 nums2 中的元素数目。请你 合并 nums2 到 nums1 中，使合并后的数组同样按 非递减顺序 排列。

**解题思路**：
- 从后往前线性枚举两个数组
- 比较两个数组的当前元素，将较大的放入nums1的末尾
- 处理剩余元素

**代码实现**：

**Java代码**：
```java
class Solution {
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int i = m - 1;      // nums1有效元素的最后一个位置
        int j = n - 1;      // nums2的最后一个位置
        int k = m + n - 1;  // 合并后数组的最后一个位置
        
        // 从后往前合并
        while (i >= 0 && j >= 0) {
            if (nums1[i] > nums2[j]) {
                nums1[k--] = nums1[i--];
            } else {
                nums1[k--] = nums2[j--];
            }
        }
        
        // 处理nums2中剩余的元素
        while (j >= 0) {
            nums1[k--] = nums2[j--];
        }
        
        // nums1中剩余的元素已经在正确的位置，不需要处理
    }
}
```

**Python代码**：
```python
class Solution:
    def merge(self, nums1: List[int], m: int, nums2: List[int], n: int) -> None:
        """Do not return anything, modify nums1 in-place instead."""
        i = m - 1      # nums1有效元素的最后一个位置
        j = n - 1      # nums2的最后一个位置
        k = m + n - 1  # 合并后数组的最后一个位置
        
        # 从后往前合并
        while i >= 0 and j >= 0:
            if nums1[i] > nums2[j]:
                nums1[k] = nums1[i]
                i -= 1
            else:
                nums1[k] = nums2[j]
                j -= 1
            k -= 1
        
        # 处理nums2中剩余的元素
        while j >= 0:
            nums1[k] = nums2[j]
            j -= 1
            k -= 1
```

**C++代码**：
```cpp
class Solution {
public:
    void merge(vector<int>& nums1, int m, vector<int>& nums2, int n) {
        int i = m - 1;      // nums1有效元素的最后一个位置
        int j = n - 1;      // nums2的最后一个位置
        int k = m + n - 1;  // 合并后数组的最后一个位置
        
        // 从后往前合并
        while (i >= 0 && j >= 0) {
            if (nums1[i] > nums2[j]) {
                nums1[k--] = nums1[i--];
            } else {
                nums1[k--] = nums2[j--];
            }
        }
        
        // 处理nums2中剩余的元素
        while (j >= 0) {
            nums1[k--] = nums2[j--];
        }
    }
};
```

### 10. 只出现一次的数字

**题目链接**：https://leetcode.com/problems/single-number/

**题目描述**：给定一个非空整数数组，除了某个元素只出现一次以外，其余每个元素均出现两次。找出那个只出现了一次的元素。

**解题思路**：
- 使用异或运算的性质：a ^ a = 0, 0 ^ b = b
- 线性枚举数组中的每个元素，依次异或
- 最终结果就是只出现一次的元素

**代码实现**：

**Java代码**：
```java
class Solution {
    public int singleNumber(int[] nums) {
        int result = 0;
        // 异或所有元素
        for (int num : nums) {
            result ^= num;
        }
        return result;
    }
}
```

**Python代码**：
```python
class Solution:
    def singleNumber(self, nums: List[int]) -> int:
        result = 0
        # 异或所有元素
        for num in nums:
            result ^= num
        return result
```

**C++代码**：
```cpp
class Solution {
public:
    int singleNumber(vector<int>& nums) {
        int result = 0;
        // 异或所有元素
        for (int num : nums) {
            result ^= num;
        }
        return result;
    }
};
```

## 线性枚举的优化技巧

1. **剪枝优化**：在枚举过程中，对于明显不满足条件的情况提前跳过
2. **前缀和/前缀积**：预处理前缀信息，减少重复计算
3. **双指针技术**：使用两个指针同时枚举，优化时间复杂度
4. **哈希表/集合**：使用哈希结构加速查找和判断
5. **位运算**：利用位运算的特性简化计算

## 总结

线性枚举虽然是一种基础的算法思想，但它在解决各种算法问题中有着广泛的应用。通过本专题的学习，我们了解了线性枚举的基本原理、实现方法和优化技巧，并通过多个经典题目进行了实践。

在实际应用中，我们需要根据问题的特点，灵活运用线性枚举算法，并结合其他算法思想进行优化，以达到更好的性能。","}}}