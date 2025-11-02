# 补充题目与解答

## 目录
1. [LeetCode 118. Pascal's Triangle](#leetcode-118-pascals-triangle)
2. [LeetCode 119. Pascal's Triangle II](#leetcode-119-pascals-triangle-ii)
3. [LeetCode 120. Triangle](#leetcode-120-triangle)
4. [LeetCode 62. Unique Paths](#leetcode-62-unique-paths)
5. [LeetCode 96. Unique Binary Search Trees](#leetcode-96-unique-binary-search-trees)
6. [洛谷 P5732 杨辉三角](#洛谷-p5732-杨辉三角)
7. [洛谷 P2822 组合数问题](#洛谷-p2822-组合数问题)
8. [Codeforces 815B - Karen and Test](#codeforces-815b---karen-and-test)
9. [LeetCode 357. 计算各个位数不同的数字个数](#leetcode-357-计算各个位数不同的数字个数)
10. [LeetCode 377. 组合总和 IV](#leetcode-377-组合总和-iv)
11. [LeetCode 416. 分割等和子集](#leetcode-416-分割等和子集)
12. [LeetCode 494. 目标和](#leetcode-494-目标和)

---

## LeetCode 118. Pascal's Triangle

### 题目描述
给定一个非负整数 numRows，生成「杨辉三角」的前 numRows 行。
在「杨辉三角」中，每个数是它左上方和右上方的数的和。

### 示例
```
输入: numRows = 5
输出: [[1],[1,1],[1,2,1],[1,3,3,1],[1,4,6,4,1]]
```

### 解题思路
使用动态规划方法，逐行生成杨辉三角。每行的首尾元素都是1，中间元素等于上一行相邻两个元素之和。

### Java 实现
```java
import java.util.*;

public class Solution {
    public List<List<Integer>> generate(int numRows) {
        List<List<Integer>> triangle = new ArrayList<>();
        
        // 逐行生成杨辉三角
        for (int i = 0; i < numRows; i++) {
            List<Integer> row = new ArrayList<>();
            
            // 每行的第一个和最后一个元素都是1
            for (int j = 0; j <= i; j++) {
                if (j == 0 || j == i) {
                    row.add(1);
                } else {
                    // 中间的元素等于上一行相邻两个元素之和
                    int left = triangle.get(i-1).get(j-1);
                    int right = triangle.get(i-1).get(j);
                    row.add(left + right);
                }
            }
            
            triangle.add(row);
        }
        
        return triangle;
    }
}
```

### Python 实现
```python
class Solution:
    def generate(self, numRows):
        """
        生成杨辉三角的前numRows行
        
        Args:
            numRows: 非负整数，要生成的行数
            
        Returns:
            二维列表，表示杨辉三角
        """
        # 初始化结果列表
        triangle = []
        
        # 逐行生成杨辉三角
        for i in range(numRows):
            # 创建当前行，长度为i+1
            row = [1] * (i + 1)
            
            # 计算中间的元素值
            for j in range(1, i):
                row[j] = triangle[i-1][j-1] + triangle[i-1][j]
            
            # 将当前行添加到结果中
            triangle.append(row)
        
        return triangle
```

### C++ 实现
```cpp
#include <vector>
using namespace std;

class Solution {
public:
    vector<vector<int>> generate(int numRows) {
        vector<vector<int>> triangle;
        
        // 逐行生成杨辉三角
        for (int i = 0; i < numRows; i++) {
            vector<int> row(i + 1, 1);  // 初始化为1
            
            // 计算中间的元素值
            for (int j = 1; j < i; j++) {
                row[j] = triangle[i-1][j-1] + triangle[i-1][j];
            }
            
            triangle.push_back(row);
        }
        
        return triangle;
    }
};
```

### 复杂度分析
- 时间复杂度：O(numRows²)
- 空间复杂度：O(numRows²)

---

## LeetCode 119. Pascal's Triangle II

### 题目描述
给定一个非负索引 rowIndex，返回「杨辉三角」的第 rowIndex 行。
在「杨辉三角」中，每个数是它左上方和右上方的数的和。

### 示例
```
输入: rowIndex = 3
输出: [1,3,3,1]
```

### 解题思路
使用滚动数组优化空间复杂度，只保存当前行和上一行的数据。

### Java 实现
```java
import java.util.*;

public class Solution {
    public List<Integer> getRow(int rowIndex) {
        List<Integer> row = new ArrayList<>();
        
        // 初始化第一行为[1]
        row.add(1);
        
        // 逐行计算到目标行
        for (int i = 1; i <= rowIndex; i++) {
            // 从右向左更新，避免覆盖还需要使用的值
            for (int j = i - 1; j > 0; j--) {
                row.set(j, row.get(j) + row.get(j-1));
            }
            // 每行末尾添加1
            row.add(1);
        }
        
        return row;
    }
}
```

### Python 实现
```python
class Solution:
    def getRow(self, rowIndex):
        """
        获取杨辉三角的第rowIndex行
        
        Args:
            rowIndex: 非负整数，行索引
            
        Returns:
            列表，表示杨辉三角的第rowIndex行
        """
        # 初始化第一行为[1]
        row = [1]
        
        # 逐行计算到目标行
        for i in range(1, rowIndex + 1):
            # 从右向左更新，避免覆盖还需要使用的值
            for j in range(i - 1, 0, -1):
                row[j] = row[j] + row[j-1]
            # 每行末尾添加1
            row.append(1)
        
        return row
```

### C++ 实现
```cpp
#include <vector>
using namespace std;

class Solution {
public:
    vector<int> getRow(int rowIndex) {
        vector<int> row(1, 1);  // 初始化第一行为[1]
        
        // 逐行计算到目标行
        for (int i = 1; i <= rowIndex; i++) {
            // 从右向左更新，避免覆盖还需要使用的值
            for (int j = i - 1; j > 0; j--) {
                row[j] = row[j] + row[j-1];
            }
            // 每行末尾添加1
            row.push_back(1);
        }
        
        return row;
    }
};
```

### 复杂度分析
- 时间复杂度：O(rowIndex²)
- 空间复杂度：O(rowIndex)

---

## LeetCode 120. Triangle

### 题目描述
给定一个三角形 triangle ，找出自顶向下的最小路径和。
每一步只能移动到下一行中相邻的结点上。相邻的结点 在这里指的是 下标 与 上一层结点下标 相同或者等于 上一层结点下标 + 1 的两个结点。

### 示例
```
输入：triangle = [[2],[3,4],[6,5,7],[4,1,8,3]]
输出：11
解释：最小路径是 2 + 3 + 5 + 1 = 11
```

### 解题思路
使用动态规划，从底向上计算每个位置到最底层的最小路径和。

### Java 实现
```java
import java.util.*;

public class Solution {
    public int minimumTotal(List<List<Integer>> triangle) {
        int n = triangle.size();
        // dp[i][j] 表示从位置(i,j)到底部的最小路径和
        int[][] dp = new int[n+1][n+1];
        
        // 从最后一行开始向上计算
        for (int i = n-1; i >= 0; i--) {
            for (int j = 0; j <= i; j++) {
                dp[i][j] = Math.min(dp[i+1][j], dp[i+1][j+1]) + triangle.get(i).get(j);
            }
        }
        
        return dp[0][0];
    }
}
```

### Python 实现
```python
class Solution:
    def minimumTotal(self, triangle):
        """
        计算三角形最小路径和
        
        Args:
            triangle: 二维列表，表示三角形
            
        Returns:
            整数，最小路径和
        """
        n = len(triangle)
        # dp[i][j] 表示从位置(i,j)到底部的最小路径和
        dp = [[0] * (n+1) for _ in range(n+1)]
        
        # 从最后一行开始向上计算
        for i in range(n-1, -1, -1):
            for j in range(i+1):
                dp[i][j] = min(dp[i+1][j], dp[i+1][j+1]) + triangle[i][j]
        
        return dp[0][0]
```

### C++ 实现
```cpp
#include <vector>
#include <algorithm>
using namespace std;

class Solution {
public:
    int minimumTotal(vector<vector<int>>& triangle) {
        int n = triangle.size();
        // dp[i][j] 表示从位置(i,j)到底部的最小路径和
        vector<vector<int>> dp(n+1, vector<int>(n+1, 0));
        
        // 从最后一行开始向上计算
        for (int i = n-1; i >= 0; i--) {
            for (int j = 0; j <= i; j++) {
                dp[i][j] = min(dp[i+1][j], dp[i+1][j+1]) + triangle[i][j];
            }
        }
        
        return dp[0][0];
    }
};
```

### 复杂度分析
- 时间复杂度：O(n²)
- 空间复杂度：O(n²)

---

## LeetCode 62. Unique Paths

### 题目描述
一个机器人位于一个 m x n 网格的左上角。机器人每次只能向下或者向右移动一步。机器人试图达到网格的右下角。问总共有多少条不同的路径？

### 示例
```
输入：m = 3, n = 7
输出：28
```

### 解题思路
这是一个组合数学问题。机器人总共需要走 (m-1)+(n-1) = m+n-2 步，其中需要选择 m-1 步向下走，所以答案是 C(m+n-2, m-1)。

### Java 实现
```java
public class Solution {
    public int uniquePaths(int m, int n) {
        // 计算组合数 C(m+n-2, m-1)
        long result = 1;
        for (int i = 1; i <= m-1; i++) {
            result = result * (n-1+i) / i;
        }
        return (int)result;
    }
}
```

### Python 实现
```python
class Solution:
    def uniquePaths(self, m, n):
        """
        计算不同路径数
        
        Args:
            m: 网格行数
            n: 网格列数
            
        Returns:
            整数，不同路径数
        """
        # 计算组合数 C(m+n-2, m-1)
        result = 1
        for i in range(1, m):
            result = result * (n-1+i) // i
        return result
```

### C++ 实现
```cpp
class Solution {
public:
    int uniquePaths(int m, int n) {
        // 计算组合数 C(m+n-2, m-1)
        long long result = 1;
        for (int i = 1; i <= m-1; i++) {
            result = result * (n-1+i) / i;
        }
        return (int)result;
    }
};
```

### 复杂度分析
- 时间复杂度：O(min(m,n))
- 空间复杂度：O(1)

---

## LeetCode 96. Unique Binary Search Trees

### 题目描述
给定一个整数 n，求恰由 n 个节点组成且节点值从 1 到 n 互不相同的二叉搜索树有多少种？

### 示例
```
输入：n = 3
输出：5
```

### 解题思路
这是卡塔兰数（Catalan Number）的应用。第n个卡塔兰数等于 C(2n, n) / (n+1)。

### Java 实现
```java
public class Solution {
    public int numTrees(int n) {
        // 计算第n个卡塔兰数
        long catalan = 1;
        for (int i = 0; i < n; i++) {
            catalan = catalan * 2 * (2 * i + 1) / (i + 2);
        }
        return (int)catalan;
    }
}
```

### Python 实现
```python
class Solution:
    def numTrees(self, n):
        """
        计算不同的二叉搜索树数量
        
        Args:
            n: 节点数
            
        Returns:
            整数，不同的二叉搜索树数量
        """
        # 计算第n个卡塔兰数
        catalan = 1
        for i in range(n):
            catalan = catalan * 2 * (2 * i + 1) // (i + 2)
        return catalan
```

### C++ 实现
```cpp
class Solution {
public:
    int numTrees(int n) {
        // 计算第n个卡塔兰数
        long long catalan = 1;
        for (int i = 0; i < n; i++) {
            catalan = catalan * 2 * (2 * i + 1) / (i + 2);
        }
        return (int)catalan;
    }
};
```

### 复杂度分析
- 时间复杂度：O(n)
- 空间复杂度：O(1)

---

## 洛谷 P5732 杨辉三角

### 题目描述
给出n(1≤n≤20)，输出杨辉三角的前n行。

### 解题思路
与LeetCode 118类似，生成杨辉三角的前n行。

### Java 实现
```java
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        
        // 生成杨辉三角
        long[][] triangle = new long[n][n];
        for (int i = 0; i < n; i++) {
            triangle[i][0] = triangle[i][i] = 1;
        }
        for (int i = 1; i < n; i++) {
            for (int j = 1; j < i; j++) {
                triangle[i][j] = triangle[i-1][j] + triangle[i-1][j-1];
            }
        }
        
        // 输出结果
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= i; j++) {
                System.out.print(triangle[i][j] + " ");
            }
            System.out.println();
        }
    }
}
```

### Python 实现
```python
n = int(input())

# 生成杨辉三角
triangle = []
for i in range(n):
    row = [1] * (i + 1)
    for j in range(1, i):
        row[j] = triangle[i-1][j-1] + triangle[i-1][j]
    triangle.append(row)

# 输出结果
for row in triangle:
    print(' '.join(map(str, row)))
```

### C++ 实现
```cpp
#include <iostream>
#include <vector>
using namespace std;

int main() {
    int n;
    cin >> n;
    
    // 生成杨辉三角
    vector<vector<long long>> triangle(n, vector<long long>(n));
    for (int i = 0; i < n; i++) {
        triangle[i][0] = triangle[i][i] = 1;
    }
    for (int i = 1; i < n; i++) {
        for (int j = 1; j < i; j++) {
            triangle[i][j] = triangle[i-1][j] + triangle[i-1][j-1];
        }
    }
    
    // 输出结果
    for (int i = 0; i < n; i++) {
        for (int j = 0; j <= i; j++) {
            cout << triangle[i][j] << " ";
        }
        cout << endl;
    }
    
    return 0;
}
```

### 复杂度分析
- 时间复杂度：O(n²)
- 空间复杂度：O(n²)

---

## 洛谷 P2822 组合数问题

### 题目描述
组合数C(i,j)表示从i个物品中选出j个物品的方案数。如果该数值是k的整数倍，那么称(i,j)是一个合法对。给定具体的一组数字n和m，当i和j满足：0 <= i <= n，0 <= j <= min(i,m)时，返回有多少合法对。

### 解题思路
预处理所有可能的组合数模k的值，构建前缀和数组用于快速查询。

### Java 实现
```java
import java.util.*;

public class Main {
    static final int MAXN = 2002;
    static int[][] c = new int[MAXN][MAXN];
    static int[][] f = new int[MAXN][MAXN];
    static int[][] sum = new int[MAXN][MAXN];
    
    public static void build(int k) {
        for (int i = 0; i <= 2000; i++) {
            c[i][0] = 1;
            for (int j = 1; j <= i; j++) {
                c[i][j] = (c[i-1][j] + c[i-1][j-1]) % k;
            }
        }
        for (int i = 1; i <= 2000; i++) {
            for (int j = 1; j <= i; j++) {
                f[i][j] = c[i][j] % k == 0 ? 1 : 0;
            }
        }
        for (int i = 2; i <= 2000; i++) {
            for (int j = 1; j <= i; j++) {
                sum[i][j] = sum[i][j-1] + sum[i-1][j] - sum[i-1][j-1] + f[i][j];
            }
            sum[i][i+1] = sum[i][i];
        }
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int t = scanner.nextInt();
        int k = scanner.nextInt();
        
        build(k);
        
        for (int i = 0; i < t; i++) {
            int n = scanner.nextInt();
            int m = scanner.nextInt();
            if (m > n) {
                System.out.println(sum[n][n]);
            } else {
                System.out.println(sum[n][m]);
            }
        }
    }
}
```

### Python 实现
```python
MAXN = 2002
c = [[0] * MAXN for _ in range(MAXN)]
f = [[0] * MAXN for _ in range(MAXN)]
sum_arr = [[0] * MAXN for _ in range(MAXN)]

def build(k):
    for i in range(2001):
        c[i][0] = 1
        for j in range(1, i+1):
            c[i][j] = (c[i-1][j] + c[i-1][j-1]) % k
    
    for i in range(1, 2001):
        for j in range(1, i+1):
            f[i][j] = 1 if c[i][j] % k == 0 else 0
    
    for i in range(2, 2001):
        for j in range(1, i+1):
            sum_arr[i][j] = sum_arr[i][j-1] + sum_arr[i-1][j] - sum_arr[i-1][j-1] + f[i][j]
        sum_arr[i][i+1] = sum_arr[i][i]

# 读取输入
t, k = map(int, input().split())
build(k)

for _ in range(t):
    n, m = map(int, input().split())
    if m > n:
        print(sum_arr[n][n])
    else:
        print(sum_arr[n][m])
```

### C++ 实现
```cpp
#include <iostream>
#include <cstring>
using namespace std;

const int MAXN = 2002;
int c[MAXN][MAXN];
int f[MAXN][MAXN];
int sum[MAXN][MAXN];

void build(int k) {
    memset(c, 0, sizeof(c));
    memset(f, 0, sizeof(f));
    memset(sum, 0, sizeof(sum));
    
    for (int i = 0; i <= 2000; i++) {
        c[i][0] = 1;
        for (int j = 1; j <= i; j++) {
            c[i][j] = (c[i-1][j] + c[i-1][j-1]) % k;
        }
    }
    for (int i = 1; i <= 2000; i++) {
        for (int j = 1; j <= i; j++) {
            f[i][j] = c[i][j] % k == 0 ? 1 : 0;
        }
    }
    for (int i = 2; i <= 2000; i++) {
        for (int j = 1; j <= i; j++) {
            sum[i][j] = sum[i][j-1] + sum[i-1][j] - sum[i-1][j-1] + f[i][j];
        }
        sum[i][i+1] = sum[i][i];
    }
}

int main() {
    int t, k;
    cin >> t >> k;
    
    build(k);
    
    for (int i = 0; i < t; i++) {
        int n, m;
        cin >> n >> m;
        if (m > n) {
            cout << sum[n][n] << endl;
        } else {
            cout << sum[n][m] << endl;
        }
    }
    
    return 0;
}
```

### 复杂度分析
- 预处理时间复杂度：O(n²)
- 查询时间复杂度：O(1)
- 空间复杂度：O(n²)

---

## Codeforces 815B - Karen and Test

### 题目描述
给定一个长度为n的数组，每次操作将数组中相邻的两个元素进行加法或减法运算（交替进行），直到只剩下一个元素。求最终结果。

### 解题思路
这个问题可以通过杨辉三角的系数来解决。每一轮操作后，每个原始元素的系数符合杨辉三角的规律。

### Java 实现
```java
import java.util.*;

public class Solution {
    static final long MOD = 1000000007;
    
    // 计算组合数 C(n, k) % MOD
    public static long comb(int n, int k) {
        if (k > n || k < 0) return 0;
        if (k == 0 || k == n) return 1;
        
        long[] fact = new long[n+1];
        fact[0] = 1;
        for (int i = 1; i <= n; i++) {
            fact[i] = (fact[i-1] * i) % MOD;
        }
        
        long result = fact[n];
        result = (result * modInverse(fact[k], MOD)) % MOD;
        result = (result * modInverse(fact[n-k], MOD)) % MOD;
        return result;
    }
    
    // 计算模逆元
    public static long modInverse(long a, long mod) {
        return power(a, mod-2, mod);
    }
    
    // 快速幂
    public static long power(long base, long exp, long mod) {
        long result = 1;
        while (exp > 0) {
            if (exp % 2 == 1) {
                result = (result * base) % mod;
            }
            base = (base * base) % mod;
            exp /= 2;
        }
        return result;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        long[] a = new long[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextLong();
        }
        
        long result = 0;
        // 根据杨辉三角系数计算最终结果
        for (int i = 0; i < n; i++) {
            long coeff = comb(n-1, i);
            if ((n-1-i) % 2 == 1) {
                coeff = (MOD - coeff) % MOD;  // 负系数
            }
            result = (result + (coeff * a[i]) % MOD) % MOD;
        }
        
        System.out.println(result);
    }
}
```

### Python 实现
```python
MOD = 1000000007

# 计算模逆元
def mod_inverse(a, mod):
    return power(a, mod-2, mod)

# 快速幂
def power(base, exp, mod):
    result = 1
    while exp > 0:
        if exp % 2 == 1:
            result = (result * base) % mod
        base = (base * base) % mod
        exp //= 2
    return result

# 计算组合数 C(n, k) % MOD
def comb(n, k):
    if k > n or k < 0:
        return 0
    if k == 0 or k == n:
        return 1
    
    fact = [1] * (n+1)
    for i in range(1, n+1):
        fact[i] = (fact[i-1] * i) % MOD
    
    result = fact[n]
    result = (result * mod_inverse(fact[k], MOD)) % MOD
    result = (result * mod_inverse(fact[n-k], MOD)) % MOD
    return result

# 读取输入
n = int(input())
a = list(map(int, input().split()))

result = 0
# 根据杨辉三角系数计算最终结果
for i in range(n):
    coeff = comb(n-1, i)
    if (n-1-i) % 2 == 1:
        coeff = (MOD - coeff) % MOD  # 负系数
    result = (result + (coeff * a[i]) % MOD) % MOD

print(result)
```

### C++ 实现
```cpp
#include <iostream>
#include <vector>
using namespace std;

const long long MOD = 1000000007;

// 快速幂
long long power(long long base, long long exp, long long mod) {
    long long result = 1;
    while (exp > 0) {
        if (exp % 2 == 1) {
            result = (result * base) % mod;
        }
        base = (base * base) % mod;
        exp /= 2;
    }
    return result;
}

// 计算模逆元
long long modInverse(long long a, long long mod) {
    return power(a, mod-2, mod);
}

// 计算组合数 C(n, k) % MOD
long long comb(int n, int k) {
    if (k > n || k < 0) return 0;
    if (k == 0 || k == n) return 1;
    
    vector<long long> fact(n+1);
    fact[0] = 1;
    for (int i = 1; i <= n; i++) {
        fact[i] = (fact[i-1] * i) % MOD;
    }
    
    long long result = fact[n];
    result = (result * modInverse(fact[k], MOD)) % MOD;
    result = (result * modInverse(fact[n-k], MOD)) % MOD;
    return result;
}

int main() {
    int n;
    cin >> n;
    vector<long long> a(n);
    for (int i = 0; i < n; i++) {
        cin >> a[i];
    }
    
    long long result = 0;
    // 根据杨辉三角系数计算最终结果
    for (int i = 0; i < n; i++) {
        long long coeff = comb(n-1, i);
        if ((n-1-i) % 2 == 1) {
            coeff = (MOD - coeff) % MOD;  // 负系数
        }
        result = (result + (coeff * a[i]) % MOD) % MOD;
    }
    
    cout << result << endl;
    return 0;
}
```

### 复杂度分析
- 时间复杂度：O(n²)
- 空间复杂度：O(n)

## 9. LeetCode 357. 计算各个位数不同的数字个数

### 题目描述
给定一个非负整数 n，计算各位数字都不同的数字 x 的个数，其中 0 ≤ x < 10^n。

### 示例
```
输入: n = 2
输出: 91
解释: 答案应为除去 11,22,...,99 外，在 [0,100) 区间内的所有数字，共有 91 个。
```

### 解题思路
- 对于n位数，第一位有9种选择（1-9），第二位有9种选择（0-9排除第一位），第三位有8种选择，以此类推
- 需要累加1位、2位、...、n位的情况，再加上0的情况

### Java 实现
```java
public class Solution {
    public int countNumbersWithUniqueDigits(int n) {
        if (n == 0) {
            return 1; // 只有0
        }
        if (n == 1) {
            return 10; // 0-9
        }
        
        int result = 10; // 包含0-9
        int current = 9; // 第一位的选择数
        int available = 9; // 第二位的可用数字数
        
        for (int i = 2; i <= n; i++) {
            current *= available;
            result += current;
            available--;
        }
        
        return result;
    }
}
```

### Python 实现
```python
class Solution:
    def countNumbersWithUniqueDigits(self, n):
        if n == 0:
            return 1
        if n == 1:
            return 10
        
        result = 10  # 包含0-9
        current = 9  # 第一位的选择数
        available = 9  # 第二位的可用数字数
        
        for i in range(2, n + 1):
            current *= available
            result += current
            available -= 1
            
        return result
```

### C++ 实现
```cpp
class Solution {
public:
    int countNumbersWithUniqueDigits(int n) {
        if (n == 0) return 1;
        if (n == 1) return 10;
        
        int result = 10;
        int current = 9;
        int available = 9;
        
        for (int i = 2; i <= n; i++) {
            current *= available;
            result += current;
            available--;
        }
        
        return result;
    }
};
```

### 复杂度分析
- 时间复杂度：O(n)
- 空间复杂度：O(1)

## 10. LeetCode 377. 组合总和 IV

### 题目描述
给定一个由正整数组成且不存在重复数字的数组，找出和为给定目标正整数的组合的个数。

### 示例
```
输入: nums = [1, 2, 3], target = 4
输出: 7
解释:
可能的组合方式为：
(1, 1, 1, 1)
(1, 1, 2)
(1, 2, 1)
(1, 3)
(2, 1, 1)
(2, 2)
(3, 1)
顺序不同的序列被视为不同的组合。
```

### 解题思路
- 使用动态规划，dp[i] 表示和为i的组合数
- 状态转移方程：dp[i] = sum(dp[i - num] for num in nums if i >= num)
- 初始条件：dp[0] = 1（空组合和为0）

### Java 实现
```java
public class Solution {
    public int combinationSum4(int[] nums, int target) {
        // dp[i] 表示和为i的组合数
        int[] dp = new int[target + 1];
        dp[0] = 1; // 空组合和为0
        
        for (int i = 1; i <= target; i++) {
            for (int num : nums) {
                if (i >= num) {
                    dp[i] += dp[i - num];
                }
            }
        }
        
        return dp[target];
    }
}
```

### Python 实现
```python
class Solution:
    def combinationSum4(self, nums, target):
        # dp[i] 表示和为i的组合数
        dp = [0] * (target + 1)
        dp[0] = 1  # 空组合和为0
        
        for i in range(1, target + 1):
            for num in nums:
                if i >= num:
                    dp[i] += dp[i - num]
        
        return dp[target]
```

### C++ 实现
```cpp
class Solution {
public:
    int combinationSum4(vector<int>& nums, int target) {
        // dp[i] 表示和为i的组合数
        vector<unsigned int> dp(target + 1, 0);
        dp[0] = 1; // 空组合和为0
        
        for (int i = 1; i <= target; i++) {
            for (int num : nums) {
                if (i >= num) {
                    dp[i] += dp[i - num];
                }
            }
        }
        
        return dp[target];
    }
};
```

### 复杂度分析
- 时间复杂度：O(target * len(nums))
- 空间复杂度：O(target)

## 11. LeetCode 416. 分割等和子集

### 题目描述
给定一个只包含正整数的非空数组。判断是否可以将这个数组分割成两个子集，使得两个子集的元素和相等。

### 示例
```
输入: [1, 5, 11, 5]
输出: true
解释: 数组可以分割成 [1, 5, 5] 和 [11].
```

### 解题思路
- 转化为0-1背包问题：是否存在子集和为总和的一半
- 首先判断总和是否为偶数，否则不可能分割
- dp[i] 表示是否可以凑出和为i的子集
- 状态转移方程：dp[i] = dp[i] or dp[i - num]
- 初始条件：dp[0] = true（空子集和为0）

### Java 实现
```java
public class Solution {
    public boolean canPartition(int[] nums) {
        int totalSum = 0;
        for (int num : nums) {
            totalSum += num;
        }
        
        // 如果总和是奇数，无法分割成两个等和子集
        if (totalSum % 2 != 0) {
            return false;
        }
        
        int target = totalSum / 2;
        // dp[i] 表示是否可以凑出和为i的子集
        boolean[] dp = new boolean[target + 1];
        dp[0] = true; // 空子集和为0
        
        for (int num : nums) {
            // 从大到小遍历，避免重复使用同一元素
            for (int i = target; i >= num; i--) {
                dp[i] = dp[i] || dp[i - num];
            }
        }
        
        return dp[target];
    }
}
```

### Python 实现
```python
class Solution:
    def canPartition(self, nums):
        total_sum = sum(nums)
        # 如果总和是奇数，无法分割成两个等和子集
        if total_sum % 2 != 0:
            return False
        
        target = total_sum // 2
        # dp[i] 表示是否可以凑出和为i的子集
        dp = [False] * (target + 1)
        dp[0] = True  # 空子集和为0
        
        for num in nums:
            # 从大到小遍历，避免重复使用同一元素
            for i in range(target, num - 1, -1):
                dp[i] = dp[i] or dp[i - num]
        
        return dp[target]
```

### C++ 实现
```cpp
class Solution {
public:
    bool canPartition(vector<int>& nums) {
        int totalSum = 0;
        for (int num : nums) {
            totalSum += num;
        }
        
        // 如果总和是奇数，无法分割成两个等和子集
        if (totalSum % 2 != 0) {
            return false;
        }
        
        int target = totalSum / 2;
        // dp[i] 表示是否可以凑出和为i的子集
        vector<bool> dp(target + 1, false);
        dp[0] = true; // 空子集和为0
        
        for (int num : nums) {
            // 从大到小遍历，避免重复使用同一元素
            for (int i = target; i >= num; i--) {
                dp[i] = dp[i] || dp[i - num];
            }
        }
        
        return dp[target];
    }
};
```

### 复杂度分析
- 时间复杂度：O(n * sum/2)
- 空间复杂度：O(sum/2)

## 12. LeetCode 494. 目标和

### 题目描述
给你一个整数数组nums和一个整数target。向数组中的每个整数前添加'+'或'-'，然后串联起所有整数，可以构造一个表达式。找出并返回可以构造出和为target的不同表达式的数目。

### 示例
```
输入: nums = [1,1,1,1,1], target = 3
输出: 5
解释:
-1+1+1+1+1 = 3
+1-1+1+1+1 = 3
+1+1-1+1+1 = 3
+1+1+1-1+1 = 3
+1+1+1+1-1 = 3
共有5种方法使最终目标和为3。
```

### 解题思路
- 数学推导：设正数和为P，负数和为N，则P - N = target，且P + N = sum(nums)
- 联立方程得：P = (sum + target) / 2
- 问题转化为：在数组中找出和为P的子集数目
- 特殊情况处理：sum + target必须为偶数，且sum >= |target|

### Java 实现
```java
public class Solution {
    public int findTargetSumWays(int[] nums, int target) {
        int totalSum = 0;
        for (int num : nums) {
            totalSum += num;
        }
        
        // 数学推导：正数和 - 负数和 = target，正数和 + 负数和 = totalSum
        // 所以正数和 = (totalSum + target) / 2
        if ((totalSum + target) % 2 != 0 || totalSum < Math.abs(target)) {
            return 0;
        }
        
        int positiveSum = (totalSum + target) / 2;
        // dp[i] 表示和为i的子集数目
        int[] dp = new int[positiveSum + 1];
        dp[0] = 1;
        
        for (int num : nums) {
            // 从大到小遍历，避免重复使用同一元素
            for (int i = positiveSum; i >= num; i--) {
                dp[i] += dp[i - num];
            }
        }
        
        return dp[positiveSum];
    }
}
```

### Python 实现
```python
class Solution:
    def findTargetSumWays(self, nums, target):
        total_sum = sum(nums)
        # 数学推导：正数和 - 负数和 = target，正数和 + 负数和 = total_sum
        # 所以正数和 = (total_sum + target) / 2
        if (total_sum + target) % 2 != 0 or total_sum < abs(target):
            return 0
        
        positive_sum = (total_sum + target) // 2
        # dp[i] 表示和为i的子集数目
        dp = [0] * (positive_sum + 1)
        dp[0] = 1
        
        for num in nums:
            # 从大到小遍历，避免重复使用同一元素
            for i in range(positive_sum, num - 1, -1):
                dp[i] += dp[i - num]
        
        return dp[positive_sum]
```

### C++ 实现
```cpp
class Solution {
public:
    int findTargetSumWays(vector<int>& nums, int target) {
        int totalSum = 0;
        for (int num : nums) {
            totalSum += num;
        }
        
        // 数学推导：正数和 - 负数和 = target，正数和 + 负数和 = totalSum
        // 所以正数和 = (totalSum + target) / 2
        if ((totalSum + target) % 2 != 0 || totalSum < abs(target)) {
            return 0;
        }
        
        int positiveSum = (totalSum + target) / 2;
        // dp[i] 表示和为i的子集数目
        vector<int> dp(positiveSum + 1, 0);
        dp[0] = 1;
        
        for (int num : nums) {
            // 从大到小遍历，避免重复使用同一元素
            for (int i = positiveSum; i >= num; i--) {
                dp[i] += dp[i - num];
            }
        }
        
        return dp[positiveSum];
    }
};
```

### 复杂度分析
- 时间复杂度：O(n * P)
- 空间复杂度：O(P)

## 工程化实践建议

1. **性能优化**：
   - 对于大规模组合数计算，使用预处理和模运算
   - 考虑使用Lucas定理处理超大n和k的情况
   - 注意整数溢出问题，适当使用长整型

2. **边界处理**：
   - 注意处理k=0、n=0等特殊情况
   - 对于可能的溢出情况进行检测

3. **测试用例**：
   - 覆盖边界情况：空数组、单元素数组、大规模输入
   - 测试异常输入：负数、超范围值等

4. **算法选择**：
   - 小n时可以使用暴力计算
   - 中等n时使用动态规划
   - 大n时使用数学公式和模运算优化

5. **跨语言实现注意事项**：
   - Java中注意int溢出问题，适当使用long
   - C++中注意数据类型选择
   - Python中注意递归深度限制

通过这些算法的学习和练习，可以深入理解组合数学的核心概念和应用场景，为解决更复杂的算法问题打下坚实的基础。
```