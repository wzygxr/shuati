# Class063 折半搜索与双向BFS算法专题 - 补充题目

## 目录
- [补充题目列表](#补充题目列表)
- [题目详解与实现](#题目详解与实现)
  - [1. Partition Array Into Two Arrays to Minimize Sum Difference](#1-partition-array-into-two-arrays-to-minimize-sum-difference)
  - [2. ABCDEF](#2-abcdef)
  - [3. 4 Values whose Sum is 0](#3-4-values-whose-sum-is-0)
  - [4. Celebrity Split](#4-celebrity-split)
  - [5. In Search of Truth (Easy Version)](#5-in-search-of-truth-easy-version)
- [多语言实现](#多语言实现)
- [复杂度分析](#复杂度分析)
- [扩展应用](#扩展应用)

## 补充题目列表

### LeetCode平台
1. **Partition Array Into Two Arrays to Minimize Sum Difference**  
   题目链接：https://leetcode.com/problems/partition-array-into-two-arrays-to-minimize-sum-difference/  
   难度：Hard  
   算法：折半搜索

2. **Split Array With Same Average**  
   题目链接：https://leetcode.com/problems/split-array-with-same-average/  
   难度：Hard  
   算法：折半搜索

3. **Closest Subsequence Sum**  
   题目链接：https://leetcode.com/problems/closest-subsequence-sum/  
   难度：Hard  
   算法：折半搜索

### Codeforces平台
4. **Anya and Cubes**  
   题目链接：https://codeforces.com/problemset/problem/525/E  
   难度：2000  
   算法：折半搜索

5. **In Search of Truth (Easy Version)**  
   题目链接：https://codeforces.com/problemset/problem/1840/G1  
   难度：1700  
   算法：折半搜索

### SPOJ平台
6. **Subset Sums (SUBSUMS)**  
   题目链接：https://www.spoj.com/problems/SUBSUMS/  
   算法：折半搜索

7. **ABCDEF**  
   题目链接：https://www.spoj.com/problems/ABCDEF/  
   算法：折半搜索

8. **4 Values whose Sum is 0 (SUMFOUR)**  
   题目链接：https://www.spoj.com/problems/SUMFOUR/  
   算法：折半搜索

### UVa平台
9. **15-Puzzle Problem**  
   题目链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=13&page=show_problem&problem=1122  
   算法：双向BFS

10. **Celebrity Split**  
    题目链接：https://onlinejudge.org/index.php?option=onlinejudge&page=show_problem&problem=1895  
    算法：折半搜索

## 题目详解与实现

### 1. Partition Array Into Two Arrays to Minimize Sum Difference

**题目描述：**
给定一个长度为2*n的整数数组nums，你需要将nums分割成两个长度为n的数组，使得两个数组元素和的绝对差值最小。

**算法思路：**
使用折半搜索算法，将数组分为两半，分别计算所有可能的子集和，然后通过双指针技术找到最小差值。

**时间复杂度：** O(n * 2^n)  
**空间复杂度：** O(2^n)

**Java实现：**
```java
import java.util.*;

public class PartitionArrayMinSumDiff {
    public static int minimumDifference(int[] nums) {
        int n = nums.length / 2;
        int totalSum = Arrays.stream(nums).sum();
        
        // 分别计算前n个和后n个元素的所有可能子集和
        List<List<Integer>> leftSums = generateSubsetSums(nums, 0, n);
        List<List<Integer>> rightSums = generateSubsetSums(nums, n, 2 * n);
        
        // 对右半部分的子集和进行排序
        for (int i = 0; i <= n; i++) {
            Collections.sort(rightSums.get(i));
        }
        
        int minDiff = Integer.MAX_VALUE;
        
        // 枚举左半部分选择的元素个数
        for (int i = 0; i <= n; i++) {
            List<Integer> left = leftSums.get(i);
            List<Integer> right = rightSums.get(n - i);
            
            for (int leftSum : left) {
                // 在右半部分中二分查找最接近的值
                int target = (totalSum - 2 * leftSum) / 2;
                int idx = Collections.binarySearch(right, target);
                
                if (idx < 0) {
                    idx = -idx - 1;
                }
                
                // 检查idx和idx-1位置的值
                if (idx < right.size()) {
                    int rightSum = right.get(idx);
                    int diff = Math.abs(totalSum - 2 * (leftSum + rightSum));
                    minDiff = Math.min(minDiff, diff);
                }
                
                if (idx > 0) {
                    int rightSum = right.get(idx - 1);
                    int diff = Math.abs(totalSum - 2 * (leftSum + rightSum));
                    minDiff = Math.min(minDiff, diff);
                }
            }
        }
        
        return minDiff;
    }
    
    private static List<List<Integer>> generateSubsetSums(int[] nums, int start, int end) {
        int n = end - start;
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            result.add(new ArrayList<>());
        }
        
        for (int mask = 0; mask < (1 << n); mask++) {
            int sum = 0;
            int count = 0;
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    sum += nums[start + i];
                    count++;
                }
            }
            result.get(count).add(sum);
        }
        
        return result;
    }
}
```

**Python实现：**
```python
from typing import List
import bisect

def minimumDifference(nums: List[int]) -> int:
    n = len(nums) // 2
    total_sum = sum(nums)
    
    # 生成子集和
    def generate_subset_sums(start, end):
        result = [[] for _ in range(end - start + 1)]
        for mask in range(1 << (end - start)):
            sum_val = 0
            count = 0
            for i in range(end - start):
                if mask & (1 << i):
                    sum_val += nums[start + i]
                    count += 1
            result[count].append(sum_val)
        # 对每个长度的子集和进行排序
        for i in range(len(result)):
            result[i].sort()
        return result
    
    # 分别计算前n个和后n个元素的所有可能子集和
    left_sums = generate_subset_sums(0, n)
    right_sums = generate_subset_sums(n, 2 * n)
    
    min_diff = float('inf')
    
    # 枚举左半部分选择的元素个数
    for i in range(n + 1):
        left = left_sums[i]
        right = right_sums[n - i]
        
        for left_sum in left:
            # 在右半部分中二分查找最接近的值
            target = (total_sum - 2 * left_sum) // 2
            idx = bisect.bisect_left(right, target)
            
            # 检查idx和idx-1位置的值
            if idx < len(right):
                right_sum = right[idx]
                diff = abs(total_sum - 2 * (left_sum + right_sum))
                min_diff = min(min_diff, diff)
            
            if idx > 0:
                right_sum = right[idx - 1]
                diff = abs(total_sum - 2 * (left_sum + right_sum))
                min_diff = min(min_diff, diff)
    
    return min_diff
```

**C++实现：**
```cpp
#include <vector>
#include <algorithm>
#include <climits>
using namespace std;

class Solution {
public:
    int minimumDifference(vector<int>& nums) {
        int n = nums.size() / 2;
        int totalSum = 0;
        for (int num : nums) totalSum += num;
        
        // 生成子集和
        auto generateSubsetSums = [&](int start, int end) {
            vector<vector<int>> result(end - start + 1);
            for (int mask = 0; mask < (1 << (end - start)); mask++) {
                int sum = 0, count = 0;
                for (int i = 0; i < (end - start); i++) {
                    if (mask & (1 << i)) {
                        sum += nums[start + i];
                        count++;
                    }
                }
                result[count].push_back(sum);
            }
            // 对每个长度的子集和进行排序
            for (int i = 0; i < result.size(); i++) {
                sort(result[i].begin(), result[i].end());
            }
            return result;
        };
        
        // 分别计算前n个和后n个元素的所有可能子集和
        auto leftSums = generateSubsetSums(0, n);
        auto rightSums = generateSubsetSums(n, 2 * n);
        
        int minDiff = INT_MAX;
        
        // 枚举左半部分选择的元素个数
        for (int i = 0; i <= n; i++) {
            auto& left = leftSums[i];
            auto& right = rightSums[n - i];
            
            for (int leftSum : left) {
                // 在右半部分中二分查找最接近的值
                int target = (totalSum - 2 * leftSum) / 2;
                auto it = lower_bound(right.begin(), right.end(), target);
                
                // 检查it和it-1位置的值
                if (it != right.end()) {
                    int rightSum = *it;
                    int diff = abs(totalSum - 2 * (leftSum + rightSum));
                    minDiff = min(minDiff, diff);
                }
                
                if (it != right.begin()) {
                    int rightSum = *(--it);
                    int diff = abs(totalSum - 2 * (leftSum + rightSum));
                    minDiff = min(minDiff, diff);
                }
            }
        }
        
        return minDiff;
    }
};
```

### 2. ABCDEF

**题目描述：**
给定一个集合S，找出不同的三元组(A,B,C)和(D,E,F)使得 (A+B+C) % S = (D+E+F) % S。

**算法思路：**
使用折半搜索，将等式变形为 (A+B+C-D-E-F) % S = 0，然后使用折半搜索计算所有可能的 (A+B+C) 和 (D+E+F) 的值。

**时间复杂度：** O(N^3)  
**空间复杂度：** O(N^3)

### 3. 4 Values whose Sum is 0

**题目描述：**
给定4个数组A, B, C, D，每个数组包含n个整数。找出有多少组(i, j, k, l)使得 A[i] + B[j] + C[k] + D[l] = 0。

**算法思路：**
使用折半搜索，将4个数组分为两组，分别计算前两个数组和后两个数组的所有可能和，然后通过哈希表查找匹配的组合。

**时间复杂度：** O(N^2)  
**空间复杂度：** O(N^2)

### 4. Celebrity Split

**题目描述：**
给定一个正整数集合，将其分割成两个子集，使得两个子集元素和的差值最小。

**算法思路：**
使用折半搜索，将集合分为两半，分别计算所有可能的子集和，然后通过双指针技术找到最小差值。

### 5. In Search of Truth (Easy Version)

**题目描述：**
给定一个函数f(x)，需要找到满足特定条件的x值。

**算法思路：**
使用折半搜索结合其他算法技术来解决问题。

## 多语言实现

### 语言特性差异

| 特性 | Java | Python | C++ |
|------|------|--------|-----|
| 数据结构 | HashMap, ArrayList | dict, list | unordered_map, vector |
| 排序 | Collections.sort | sort | sort |
| 二分查找 | Collections.binarySearch | bisect | lower_bound |
| 内存管理 | 自动垃圾回收 | 自动垃圾回收 | 手动管理 |
| 性能 | 中等 | 较慢 | 最快 |

## 复杂度分析

### 折半搜索优化效果

| 算法类型 | 优化前 | 优化后 | 优化倍数 |
|---------|--------|--------|----------|
| 子集枚举 | O(2^n) | O(2^(n/2)) | 2^(n/2)倍 |
| 状态搜索 | O(b^d) | O(b^(d/2)) | b^(d/2)倍 |

### 空间复杂度

折半搜索通常需要O(2^(n/2))的额外空间来存储中间结果。

## 扩展应用

### 1. 密码学
- 在密码分析中用于减少暴力破解的搜索空间

### 2. 机器学习
- 特征选择中的组合优化问题

### 3. 网络安全
- 在某些攻击算法中用于优化搜索过程

### 4. 生物信息学
- 序列比对中的优化算法

## 总结

折半搜索和双向BFS是处理大规模搜索问题的重要技术。通过合理的问题分解和状态管理，可以显著降低算法的时间复杂度和空间复杂度。在实际应用中，需要根据具体问题特点选择合适的算法变种和优化策略。