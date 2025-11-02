# 线性基（Linear Basis）问题扩展

本文档收录了更多与线性基数据结构相关的经典算法题目，提供详细的解题思路和实现代码。

## 核心思想

线性基类似于线性代数中的基向量概念，它是一组线性无关的向量集合，
能够表示原集合中所有数的异或组合。线性基有以下重要性质：

1. 原序列中的任意一个数都可以由线性基中的某些数异或得到
2. 线性基中的任意一些数异或起来都不能得到0
3. 在保持性质1的前提下，线性基中的数的个数是最少的
4. 线性基中每个元素的二进制最高位互不相同

线性基（Linear Basis）是一种处理异或问题的重要数据结构，主要用于解决以下几类问题：
1. 求n个数中选取任意个数异或能得到的最大值
2. 求n个数中选取任意个数异或能得到的第k小值
3. 判断一个数是否能由给定数组中的数异或得到
4. 求能异或得到的数的个数

## LeetCode 421. 数组中两个数的最大异或值

### 题目描述
给你一个整数数组 nums ，返回 nums[i] XOR nums[j] 的最大运算结果，其中 0 ≤ i ≤ j < n 。

### 题目链接
https://leetcode.com/problems/maximum-xor-of-two-numbers-in-an-array/

### 解题思路
这道题可以用字典树（前缀树）来实现线性基的功能。我们将每个数的二进制表示从最高位到最低位插入字典树中，然后对于每个数，在字典树中查找能够与其产生最大异或值的另一个数。

### 时间复杂度
O(n * 32)，其中n是数组长度，32是整数的二进制位数

### 空间复杂度
O(n * 32)

### 代码实现
三种语言的代码实现分别位于：
- Code421_MaximumXOROfTwoNumbersInAnArray.java
- Code421_MaximumXOROfTwoNumbersInAnArray.cpp
- Code421_MaximumXOROfTwoNumbersInAnArray.py

## LeetCode 1707. 与数组中元素的最大异或值

### 题目描述
给你一个由非负整数组成的数组 nums 。另有一个查询数组 queries ，其中 queries[i] = [xi, mi] 。

第 i 个查询的答案是 xi 和任何 nums 数组中不超过 mi 的元素按位异或（XOR）得到的最大值。如果 nums 数组中没有元素不超过 mi，整数 -1 。

返回一个整数数组 answer 作为查询的答案，其中 answer.length == queries.length 且 answer[i] 是第 i 个查询的答案。

### 题目链接
https://leetcode.com/problems/maximum-xor-with-an-element-from-array/

### 解题思路
这道题是上一题的扩展，需要处理带约束条件的查询。我们可以采用离线处理的方法：
1. 将nums数组排序
2. 将queries数组排序，并记录原始索引
3. 按照mi从小到大的顺序处理查询，将nums中不超过mi的元素插入字典树
4. 对于每个查询，在字典树中查询最大异或值

### 时间复杂度
O(n log n + q log q + (n + q) * 32)，其中n是数组长度，q是查询数量

### 空间复杂度
O(n * 32 + q)

### 代码实现
三种语言的代码实现分别位于：
- Code1707_MaximumXORWithAnElementFromArray.java
- Code1707_MaximumXORWithAnElementFromArray.cpp
- Code1707_MaximumXORWithAnElementFromArray.py

## Codeforces 895C. Square Subsets

### 题目描述
给定一个数组，求非空子集的数量，使得子集中所有元素的乘积是一个平方数。答案对10^9+7取模。

### 题目链接
https://codeforces.com/contest/895/problem/C

### 解题思路
这道题可以利用线性基和质因数分解来解决。
1. 对于每个数，我们可以将其质因数分解，保留次数为奇数的质因数
2. 这样，每个数可以表示为一个二进制向量，向量的每一位代表一个质数是否出现奇数次
3. 问题转化为：在数组中选择一个非空子集，使得子集中所有数的向量异或结果为零向量
4. 使用动态规划结合线性基来计算方案数

### 时间复杂度
O(n * m * log m)，其中n是数组长度，m是质数的个数

### 空间复杂度
O(2^m)，其中m是质数的个数

### 代码实现
三种语言的代码实现分别位于：
- Codeforces895C_SquareSubsets.java
- Codeforces895C_SquareSubsets.cpp
- Codeforces895C_SquareSubsets.py

## 线性基常见操作

### 1. 插入元素
```java
public static boolean insert(long num) {
    for (int i = BIT; i >= 0; i--) {
        if ((num >> i) == 1) {
            if (basis[i] == 0) {
                basis[i] = num;
                return true;
            }
            num ^= basis[i];
        }
    }
    return false;
}
```

### 2. 查询最大异或和
```java
public static long queryMax() {
    long ans = 0;
    for (int i = BIT; i >= 0; i--) {
        ans = Math.max(ans, ans ^ basis[i]);
    }
    return ans;
}
```

### 3. 查询最小异或和
```java
public static long queryMin() {
    for (int i = 0; i <= BIT; i++) {
        if (basis[i] != 0) {
            return basis[i];
        }
    }
    return 0;
}
```

### 4. 查询第k小异或和
```java
public static long queryKth(long k) {
    if (k >= (1L << tot)) return -1;
    long ret = 0;
    for (int i = 0; i <= BIT; i++) {
        if ((k >> i) & 1) ret ^= d[i];
    }
    return ret;
}
```

## 时间复杂度分析

线性基的核心操作是插入元素，时间复杂度为O(log W)，其中W是值域大小。
对于64位整数，时间复杂度为O(64) = O(1)。

## 空间复杂度分析

线性基的空间复杂度为O(log W)，即O(64) = O(1)。

## 工程化考量

1. 异常处理：需要处理空输入、重复元素等边界情况
2. 性能优化：对于大量查询，可以预处理线性基
3. 内存优化：线性基只存储log级别的元素，内存占用小
4. 可扩展性：可以扩展到支持区间查询、在线查询等场景

## CodeChef 111506 - XOR AND OR Problem

### 题目描述
给定一个长度为n的数组，求所有可能的子序列的异或和的最大值。

### 题目链接
https://www.codechef.com/problems/XORANDOR

### 解题思路
这是一道线性基的模板题。我们可以通过构建线性基，然后利用线性基的性质来找到最大异或和。具体步骤如下：
1. 构建数组的线性基
2. 从最高位到最低位遍历线性基数组，贪心选择能使异或和增大的元素

### 时间复杂度
O(n * log(max_value))，其中n是数组长度，max_value是数组中的最大值

### 空间复杂度
O(log(max_value))

### 代码实现
三种语言的代码实现分别位于：
- CodeChef111506_XORANDOR.java
- CodeChef111506_XORANDOR.cpp
- CodeChef111506_XORANDOR.py

## 与机器学习等领域的联系

1. 在机器学习中，线性基的概念类似于特征选择中的线性无关特征集合
2. 在密码学中，线性基用于分析线性密码的性质
3. 在编码理论中，线性基用于构造线性码

## LeetCode 421. 数组中两个数的最大异或值详解

### 题目描述
给你一个非空数组，求数组中两个数的最大异或值。

### 解题思路
这道题可以用字典树（前缀树）来实现线性基的功能。我们将每个数的二进制表示从最高位到最低位插入字典树中，然后对于每个数，在字典树中查找能够与其产生最大异或值的另一个数。

### 时间复杂度
- 时间复杂度：O(n * 32)，其中n是数组长度，32是整数的二进制位数
- 空间复杂度：O(n * 32)

### 代码实现

#### Java实现
```java
class Solution {
    // 字典树节点定义
    class TrieNode {
        TrieNode[] children = new TrieNode[2]; // 0和1两个子节点
    }
    
    private TrieNode root; // 字典树根节点
    private static final int HIGH_BIT = 30; // 整数的最高位是第30位（假设是32位整数）
    
    public int findMaximumXOR(int[] nums) {
        root = new TrieNode();
        int maxXor = 0;
        
        // 插入第一个数
        insert(nums[0]);
        
        // 对于每个数，先查询最大异或值，再插入到字典树中
        for (int i = 1; i < nums.length; i++) {
            maxXor = Math.max(maxXor, query(nums[i]));
            insert(nums[i]);
        }
        
        return maxXor;
    }
    
    // 将数字插入字典树
    private void insert(int num) {
        TrieNode node = root;
        for (int i = HIGH_BIT; i >= 0; i--) {
            int bit = (num >> i) & 1;
            if (node.children[bit] == null) {
                node.children[bit] = new TrieNode();
            }
            node = node.children[bit];
        }
    }
    
    // 查询与给定数字异或的最大值
    private int query(int num) {
        TrieNode node = root;
        int xorSum = 0;
        for (int i = HIGH_BIT; i >= 0; i--) {
            int bit = (num >> i) & 1;
            int desiredBit = 1 - bit; // 希望找到相反的位以获得最大异或值
            
            if (node.children[desiredBit] != null) {
                xorSum |= (1 << i); // 当前位可以取到1
                node = node.children[desiredBit];
            } else {
                node = node.children[bit]; // 只能取相同的位
            }
        }
        return xorSum;
    }
}
```

#### C++实现
```cpp
#include <vector>
#include <algorithm>
using namespace std;

class Solution {
private:
    struct TrieNode {
        TrieNode* children[2];
        TrieNode() {
            children[0] = children[1] = nullptr;
        }
    };
    
    TrieNode* root;
    const int HIGH_BIT = 30;
    
    void insert(int num) {
        TrieNode* node = root;
        for (int i = HIGH_BIT; i >= 0; --i) {
            int bit = (num >> i) & 1;
            if (!node->children[bit]) {
                node->children[bit] = new TrieNode();
            }
            node = node->children[bit];
        }
    }
    
    int query(int num) {
        TrieNode* node = root;
        int xorSum = 0;
        for (int i = HIGH_BIT; i >= 0; --i) {
            int bit = (num >> i) & 1;
            int desiredBit = 1 - bit;
            
            if (node->children[desiredBit]) {
                xorSum |= (1 << i);
                node = node->children[desiredBit];
            } else {
                node = node->children[bit];
            }
        }
        return xorSum;
    }
    
public:
    int findMaximumXOR(vector<int>& nums) {
        root = new TrieNode();
        int maxXor = 0;
        
        insert(nums[0]);
        for (int i = 1; i < nums.size(); ++i) {
            maxXor = max(maxXor, query(nums[i]));
            insert(nums[i]);
        }
        
        // 清理内存
        delete root;
        
        return maxXor;
    }
};
```

#### Python实现
```python
class Solution:
    def findMaximumXOR(self, nums):
        # 字典树节点定义
        class TrieNode:
            def __init__(self):
                self.children = [None, None]  # 0和1两个子节点
        
        root = TrieNode()
        HIGH_BIT = 30  # 整数的最高位是第30位
        max_xor = 0
        
        # 将数字插入字典树
        def insert(num):
            node = root
            for i in range(HIGH_BIT, -1, -1):
                bit = (num >> i) & 1
                if not node.children[bit]:
                    node.children[bit] = TrieNode()
                node = node.children[bit]
        
        # 查询与给定数字异或的最大值
        def query(num):
            node = root
            xor_sum = 0
            for i in range(HIGH_BIT, -1, -1):
                bit = (num >> i) & 1
                desired_bit = 1 - bit
                
                if node.children[desired_bit]:
                    xor_sum |= (1 << i)
                    node = node.children[desired_bit]
                else:
                    node = node.children[bit]
            return xor_sum
        
        # 插入第一个数
        insert(nums[0])
        
        # 对于每个数，先查询再插入
        for i in range(1, len(nums)):
            max_xor = max(max_xor, query(nums[i]))
            insert(nums[i])
        
        return max_xor
```

## LeetCode 1707. 与数组中元素的最大异或值详解

### 题目描述
给你一个由非负整数组成的数组 nums 。另有一个查询数组 queries ，其中 queries[i] = [xi, mi] 。第 i 个查询的答案是 xi 和任何 nums 数组中不超过 mi 的元素按位异或（XOR）得到的最大值。如果 nums 数组中不存在不超过 mi 的元素，那么答案是 -1 。

### 解题思路
这道题是上一题的扩展，需要处理带约束条件的查询。我们可以采用离线处理的方法：
1. 将nums数组排序
2. 将queries数组排序，并记录原始索引
3. 按照mi从小到大的顺序处理查询，将nums中不超过mi的元素插入字典树
4. 对于每个查询，在字典树中查询最大异或值

### 时间复杂度
- 时间复杂度：O(n log n + q log q + (n + q) * 32)，其中n是数组长度，q是查询数量
- 空间复杂度：O(n * 32 + q)

### 代码实现

#### Java实现
```java
import java.util.*;

class Solution {
    // 字典树节点定义
    class TrieNode {
        TrieNode[] children = new TrieNode[2];
    }
    
    private TrieNode root;
    private static final int HIGH_BIT = 30;
    
    public int[] maximizeXor(int[] nums, int[][] queries) {
        root = new TrieNode();
        int n = nums.length;
        int q = queries.length;
        int[] result = new int[q];
        
        // 对nums数组排序
        Arrays.sort(nums);
        
        // 对queries进行排序，并记录原始索引
        int[][] sortedQueries = new int[q][3];
        for (int i = 0; i < q; i++) {
            sortedQueries[i][0] = queries[i][0];  // xi
            sortedQueries[i][1] = queries[i][1];  // mi
            sortedQueries[i][2] = i;              // 原始索引
        }
        Arrays.sort(sortedQueries, Comparator.comparingInt(a -> a[1]));
        
        int numIndex = 0;
        for (int[] query : sortedQueries) {
            int xi = query[0];
            int mi = query[1];
            int originalIndex = query[2];
            
            // 将nums中不超过mi的元素插入字典树
            while (numIndex < n && nums[numIndex] <= mi) {
                insert(nums[numIndex]);
                numIndex++;
            }
            
            // 如果字典树为空，说明没有符合条件的元素
            if (numIndex == 0) {
                result[originalIndex] = -1;
            } else {
                result[originalIndex] = queryMaxXor(xi);
            }
        }
        
        return result;
    }
    
    private void insert(int num) {
        TrieNode node = root;
        for (int i = HIGH_BIT; i >= 0; i--) {
            int bit = (num >> i) & 1;
            if (node.children[bit] == null) {
                node.children[bit] = new TrieNode();
            }
            node = node.children[bit];
        }
    }
    
    private int queryMaxXor(int num) {
        TrieNode node = root;
        int maxXor = 0;
        for (int i = HIGH_BIT; i >= 0; i--) {
            int bit = (num >> i) & 1;
            int desiredBit = 1 - bit;
            
            if (node.children[desiredBit] != null) {
                maxXor |= (1 << i);
                node = node.children[desiredBit];
            } else {
                node = node.children[bit];
            }
        }
        return maxXor;
    }
}
```

#### C++实现
```cpp
#include <vector>
#include <algorithm>
using namespace std;

class Solution {
private:
    struct TrieNode {
        TrieNode* children[2];
        TrieNode() {
            children[0] = children[1] = nullptr;
        }
    };
    
    TrieNode* root;
    const int HIGH_BIT = 30;
    
    void insert(int num) {
        TrieNode* node = root;
        for (int i = HIGH_BIT; i >= 0; --i) {
            int bit = (num >> i) & 1;
            if (!node->children[bit]) {
                node->children[bit] = new TrieNode();
            }
            node = node->children[bit];
        }
    }
    
    int queryMaxXor(int num) {
        TrieNode* node = root;
        int maxXor = 0;
        for (int i = HIGH_BIT; i >= 0; --i) {
            int bit = (num >> i) & 1;
            int desiredBit = 1 - bit;
            
            if (node->children[desiredBit]) {
                maxXor |= (1 << i);
                node = node->children[desiredBit];
            } else {
                node = node->children[bit];
            }
        }
        return maxXor;
    }
    
public:
    vector<int> maximizeXor(vector<int>& nums, vector<vector<int>>& queries) {
        root = new TrieNode();
        int n = nums.size();
        int q = queries.size();
        vector<int> result(q);
        
        // 对nums数组排序
        sort(nums.begin(), nums.end());
        
        // 对queries进行排序，并记录原始索引
        vector<tuple<int, int, int>> sortedQueries;
        for (int i = 0; i < q; ++i) {
            sortedQueries.emplace_back(queries[i][1], queries[i][0], i);
        }
        sort(sortedQueries.begin(), sortedQueries.end());
        
        int numIndex = 0;
        for (auto& query : sortedQueries) {
            int mi = get<0>(query);
            int xi = get<1>(query);
            int originalIndex = get<2>(query);
            
            // 将nums中不超过mi的元素插入字典树
            while (numIndex < n && nums[numIndex] <= mi) {
                insert(nums[numIndex]);
                numIndex++;
            }
            
            if (numIndex == 0) {
                result[originalIndex] = -1;
            } else {
                result[originalIndex] = queryMaxXor(xi);
            }
        }
        
        // 清理内存
        // 注意：这里应该递归删除整个字典树，但为了简化代码，这里省略
        
        return result;
    }
};
```

#### Python实现
```python
class Solution:
    def maximizeXor(self, nums, queries):
        # 字典树节点定义
        class TrieNode:
            def __init__(self):
                self.children = [None, None]  # 0和1两个子节点
        
        root = TrieNode()
        HIGH_BIT = 30
        n = len(nums)
        q = len(queries)
        result = [0] * q
        
        # 将nums数组排序
        nums.sort()
        
        # 对queries进行排序，并记录原始索引
        sorted_queries = []
        for i in range(q):
            xi, mi = queries[i]
            sorted_queries.append((mi, xi, i))
        sorted_queries.sort()
        
        # 插入数字到字典树
        def insert(num):
            node = root
            for i in range(HIGH_BIT, -1, -1):
                bit = (num >> i) & 1
                if not node.children[bit]:
                    node.children[bit] = TrieNode()
                node = node.children[bit]
        
        # 查询最大异或值
        def query_max_xor(num):
            node = root
            max_xor = 0
            for i in range(HIGH_BIT, -1, -1):
                bit = (num >> i) & 1
                desired_bit = 1 - bit
                
                if node.children[desired_bit]:
                    max_xor |= (1 << i)
                    node = node.children[desired_bit]
                else:
                    node = node.children[bit]
            return max_xor
        
        num_index = 0
        for mi, xi, original_index in sorted_queries:
            # 将nums中不超过mi的元素插入字典树
            while num_index < n and nums[num_index] <= mi:
                insert(nums[num_index])
                num_index += 1
            
            if num_index == 0:
                result[original_index] = -1
            else:
                result[original_index] = query_max_xor(xi)
        
        return result
```

## Codeforces 895C Square Subsets详解

### 题目描述
给定一个数组，求选出一个子集，使得子集中所有数的乘积是完全平方数的方案数。

### 解题思路
完全平方数的每个质因数的指数都是偶数。我们可以将每个数转换为一个二进制向量，表示其质因数指数的奇偶性（奇数为1，偶数为0）。这样问题就转化为：求有多少个子集的异或和为0。

我们可以使用线性基来解决这个问题。对于每个数，如果它能被插入到线性基中，说明它与之前的数线性无关；否则，说明存在一个子集使得它们的异或和为0，此时总方案数需要乘以2。

### 时间复杂度
- 时间复杂度：O(n * log max_num)，其中n是数组长度，max_num是数组中的最大值
- 空间复杂度：O(log max_num)

### 代码实现

#### Java实现
```java
import java.util.*;

public class SquareSubsets {
    private static final int MOD = 1000000007;
    private static final int MAX_PRIME = 70; // 1e5以内的质数不超过70个
    private static int[] primes = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67};
    private static int PRIME_COUNT = primes.length;
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        scanner.close();
        
        // 统计每个数出现的次数
        Map<Integer, Integer> countMap = new HashMap<>();
        for (int num : a) {
            countMap.put(num, countMap.getOrDefault(num, 0) + 1);
        }
        
        // 线性基数组
        long[] basis = new long[PRIME_COUNT];
        
        // 处理每个数
        long result = 1;
        for (Map.Entry<Integer, Integer> entry : countMap.entrySet()) {
            int num = entry.getKey();
            int count = entry.getValue();
            
            // 计算num的质因数分解，统计每个质数的奇偶性
            long mask = 0;
            for (int i = 0; i < PRIME_COUNT; i++) {
                int prime = primes[i];
                int exponent = 0;
                int tmp = num;
                while (tmp % prime == 0) {
                    exponent++;
                    tmp /= prime;
                }
                if (exponent % 2 == 1) {
                    mask |= (1L << i);
                }
            }
            
            // 如果mask为0，说明num本身是平方数
            if (mask == 0) {
                // 每个平方数可以选择或不选择，但至少选择一个的情况需要排除
                result = (result * pow(2, count)) % MOD;
            } else {
                // 尝试将mask插入线性基
                long current = mask;
                for (int i = 0; i < PRIME_COUNT; i++) {
                    if ((current >> i & 1) == 1) {
                        if (basis[i] == 0) {
                            basis[i] = current;
                            break;
                        }
                        current ^= basis[i];
                    }
                }
                
                // 如果current变为0，说明存在线性相关
                if (current == 0) {
                    result = (result * (pow(2, count) - 1)) % MOD;
                }
            }
        }
        
        // 减去空集的情况
        result = (result - 1 + MOD) % MOD;
        System.out.println(result);
    }
    
    private static long pow(long base, int exponent) {
        long result = 1;
        while (exponent > 0) {
            if (exponent % 2 == 1) {
                result = (result * base) % MOD;
            }
            base = (base * base) % MOD;
            exponent /= 2;
        }
        return result;
    }
}
```

#### C++实现
```cpp
#include <iostream>
#include <vector>
#include <map>
using namespace std;

const int MOD = 1e9 + 7;
const int MAX_PRIME = 70;
vector<int> primes = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67};
int PRIME_COUNT = primes.size();

long long pow_mod(long long base, int exponent) {
    long long result = 1;
    while (exponent > 0) {
        if (exponent % 2 == 1) {
            result = (result * base) % MOD;
        }
        base = (base * base) % MOD;
        exponent /= 2;
    }
    return result;
}

int main() {
    int n;
    cin >> n;
    vector<int> a(n);
    for (int i = 0; i < n; i++) {
        cin >> a[i];
    }
    
    map<int, int> countMap;
    for (int num : a) {
        countMap[num]++;
    }
    
    vector<long long> basis(PRIME_COUNT, 0);
    long long result = 1;
    
    for (auto& entry : countMap) {
        int num = entry.first;
        int count = entry.second;
        
        long long mask = 0;
        for (int i = 0; i < PRIME_COUNT; i++) {
            int prime = primes[i];
            int exponent = 0;
            int tmp = num;
            while (tmp % prime == 0) {
                exponent++;
                tmp /= prime;
            }
            if (exponent % 2 == 1) {
                mask |= (1LL << i);
            }
        }
        
        if (mask == 0) {
            result = (result * pow_mod(2, count)) % MOD;
        } else {
            long long current = mask;
            for (int i = 0; i < PRIME_COUNT; i++) {
                if ((current >> i & 1) == 1) {
                    if (basis[i] == 0) {
                        basis[i] = current;
                        break;
                    }
                    current ^= basis[i];
                }
            }
            
            if (current == 0) {
                result = (result * (pow_mod(2, count) - 1)) % MOD;
            }
        }
    }
    
    result = (result - 1 + MOD) % MOD;
    cout << result << endl;
    
    return 0;
}
```

#### Python实现
```python
MOD = 10**9 + 7
primes = [2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67]
PRIME_COUNT = len(primes)

def pow_mod(base, exponent):
    result = 1
    while exponent > 0:
        if exponent % 2 == 1:
            result = (result * base) % MOD
        base = (base * base) % MOD
        exponent //= 2
    return result

def main():
    import sys
    from collections import defaultdict
    input = sys.stdin.read().split()
    n = int(input[0])
    a = list(map(int, input[1:n+1]))
    
    count_map = defaultdict(int)
    for num in a:
        count_map[num] += 1
    
    basis = [0] * PRIME_COUNT
    result = 1
    
    for num, count in count_map.items():
        mask = 0
        for i in range(PRIME_COUNT):
            prime = primes[i]
            exponent = 0
            tmp = num
            while tmp % prime == 0:
                exponent += 1
                tmp //= prime
            if exponent % 2 == 1:
                mask |= (1 << i)
        
        if mask == 0:
            result = (result * pow_mod(2, count)) % MOD
        else:
            current = mask
            for i in range(PRIME_COUNT):
                if (current >> i) & 1:
                    if basis[i] == 0:
                        basis[i] = current
                        break
                    current ^= basis[i]
            
            if current == 0:
                result = (result * (pow_mod(2, count) - 1)) % MOD
    
    # 减去空集的情况
    result = (result - 1 + MOD) % MOD
    print(result)

if __name__ == "__main__":
    main()
```