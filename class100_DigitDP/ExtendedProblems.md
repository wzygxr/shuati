# 数位DP扩展题目清单

本文件整理了与class085中数位DP问题相关的更多练习题目，来源于各大算法平台，并提供详细的解题思路和实现代码。

## 📚 按平台分类

### LeetCode (力扣)
1. **LeetCode 233. 数字 1 的个数** - https://leetcode.cn/problems/number-of-digit-one/
   - 类型：数位DP
   - 难度：困难
   - 简介：计算所有小于等于 n 的非负整数中数字 1 出现的个数。
   - **Java实现**：
   ```java
   class Solution {
       // 数位DP计算数字1的出现次数
       public int countDigitOne(int n) {
           if (n < 0) return 0;
           String s = String.valueOf(n);
           int len = s.length();
           // 预处理每一位的权值
           long[] power = new long[len];
           power[0] = 1;
           for (int i = 1; i < len; i++) {
               power[i] = power[i - 1] * 10;
           }
           
           int count = 0;
           for (int i = 0; i < len; i++) {
               int digit = s.charAt(i) - '0';
               long high = i == 0 ? 0 : Long.parseLong(s.substring(0, i));
               long low = i == len - 1 ? 0 : Long.parseLong(s.substring(i + 1));
               long posValue = power[len - i - 1];
               
               if (digit == 0) {
                   count += high * posValue;
               } else if (digit == 1) {
                   count += high * posValue + (low + 1);
               } else {
                   count += (high + 1) * posValue;
               }
           }
           return count;
       }
   }
   ```
   - **Python实现**：
   ```python
   class Solution:
       def countDigitOne(self, n: int) -> int:
           if n < 0:
               return 0
           s = str(n)
           count = 0
           for i in range(len(s)):
               high = int(s[:i]) if i > 0 else 0
               current = int(s[i])
               low = int(s[i+1:]) if i < len(s)-1 else 0
               pos = 10 ** (len(s) - i - 1)
               
               if current == 0:
                   count += high * pos
               elif current == 1:
                   count += high * pos + (low + 1)
               else:
                   count += (high + 1) * pos
           return count
   ```

2. **LeetCode 600. 不含连续1的非负整数** - https://leetcode.cn/problems/non-negative-integers-without-consecutive-ones/
   - 类型：数位DP（二进制）
   - 难度：困难
   - 简介：统计在 [0, n] 范围的非负整数中，二进制表示中不存在连续的1的整数个数。
   - **核心思路**：二进制数位DP，状态包括当前位置和前一位是否为1
   - **Java实现**：
   ```java
   class Solution {
       // 二进制数位DP，统计不含连续1的数
       public int findIntegers(int n) {
           String binary = Integer.toBinaryString(n);
           int len = binary.length();
           // dp[i][0]表示i位二进制数，最高位为0时的有效数
           // dp[i][1]表示i位二进制数，最高位为1时的有效数
           int[][] dp = new int[len][2];
           
           // 初始状态：1位二进制数
           dp[0][0] = 1;  // 0
           dp[0][1] = 1;  // 1
           
           // 填充dp数组
           for (int i = 1; i < len; i++) {
               dp[i][0] = dp[i-1][0] + dp[i-1][1];  // 最高位为0，后面可以接0或1
               dp[i][1] = dp[i-1][0];               // 最高位为1，后面只能接0
           }
           
           // 计算结果
           int result = dp[len-1][0] + dp[len-1][1];
           
           // 检查是否存在连续1的情况，需要减去不符合条件的数
           for (int i = 1; i < len; i++) {
               if (binary.charAt(i) == '1' && binary.charAt(i-1) == '1') {
                   break;  // 出现连续1，不需要调整
               }
               if (binary.charAt(i) == '0' && binary.charAt(i-1) == '1') {
                   // 调整结果
                   String suffix = binary.substring(i+1);
                   result -= Integer.parseInt(suffix, 2) + 1;
                   break;
               }
           }
           
           return result;
       }
   }
   ```

3. **LeetCode 1012. 至少有 1 位重复的数字** - https://leetcode.cn/problems/numbers-with-repeated-digits/
   - 类型：数位DP（状态压缩）
   - 难度：困难
   - 简介：返回在 [1, N] 范围内具有至少 1 位重复数字的正整数的个数。
   - **核心思路**：使用补集思想，计算不重复数字的个数，总数减去它
   - **Java实现**：
   ```java
   class Solution {
       // 使用数位DP计算不重复数字的个数，然后用总数减去它
       public int numDupDigitsAtMostN(int N) {
           return N - countUniqueDigits(N);
       }
       
       private int countUniqueDigits(int n) {
           String s = String.valueOf(n);
           int len = s.length();
           // dp[pos][mask][limit]表示在pos位，已使用数字mask，是否受限制
           int[][][] dp = new int[len][1 << 10][2];
           for (int i = 0; i < len; i++) {
               for (int j = 0; j < (1 << 10); j++) {
                   Arrays.fill(dp[i][j], -1);
               }
           }
           return dfs(0, 0, true, true, s, dp);
       }
       
       private int dfs(int pos, int mask, boolean limit, boolean lead, String s, int[][][] dp) {
           if (pos == s.length()) {
               return lead ? 0 : 1;
           }
           
           if (!lead && dp[pos][mask][limit ? 1 : 0] != -1) {
               return dp[pos][mask][limit ? 1 : 0];
           }
           
           int ans = 0;
           int upper = limit ? (s.charAt(pos) - '0') : 9;
           
           for (int i = 0; i <= upper; i++) {
               if ((mask & (1 << i)) != 0) continue;
               
               boolean newLimit = limit && (i == upper);
               boolean newLead = lead && (i == 0);
               int newMask = newLead ? 0 : (mask | (1 << i));
               
               ans += dfs(pos + 1, newMask, newLimit, newLead, s, dp);
           }
           
           if (!lead) {
               dp[pos][mask][limit ? 1 : 0] = ans;
           }
           return ans;
       }
   }
   ```

4. **LeetCode 1397. 找到所有好字符串** - https://leetcode.cn/problems/find-all-good-strings/
   - 类型：数位DP + KMP
   - 难度：困难
   - 简介：计算在给定范围内不包含特定子串的字符串数量。
   - **核心思路**：结合KMP算法和数位DP，状态包括当前位置和匹配进度
   - **Python实现**：
   ```python
   class Solution:
       def findGoodStrings(self, n: int, s1: str, s2: str, evil: str) -> int:
           MOD = 10**9 + 7
           
           # 构建KMP的next数组
           def build_kmp(pattern):
               m = len(pattern)
               next_arr = [0] * m
               for i in range(1, m):
                   j = next_arr[i-1]
                   while j > 0 and pattern[i] != pattern[j]:
                       j = next_arr[j-1]
                   if pattern[i] == pattern[j]:
                       j += 1
                   next_arr[i] = j
               return next_arr
           
           next_arr = build_kmp(evil)
           m = len(evil)
           
           # 数位DP
           @lru_cache(None)
           def dp(pos, state, lower, upper):
               if state == m:  # 包含evil，返回0
                   return 0
               if pos == n:  # 找到一个好字符串
                   return 1
               
               res = 0
               start = ord(s1[pos]) if lower else ord('a')
               end = ord(s2[pos]) if upper else ord('z')
               
               for c in range(start, end + 1):
                   ch = chr(c)
                   # 更新KMP状态
                   new_state = state
                   while new_state > 0 and ch != evil[new_state]:
                       new_state = next_arr[new_state - 1]
                   if ch == evil[new_state]:
                       new_state += 1
                   
                   # 更新边界状态
                   new_lower = lower and (c == start)
                   new_upper = upper and (c == end)
                   
                   res = (res + dp(pos + 1, new_state, new_lower, new_upper)) % MOD
               
               return res
           
           return dp(0, 0, True, True) % MOD
   ```

5. **LeetCode 1067. 范围内的数字计数** - https://leetcode.cn/problems/digit-count-in-range/
   - 类型：数位DP
   - 难度：困难
   - 简介：给定一个非负整数 d 和两个整数 low 和 high，返回在 [low, high] 范围内的所有数字中，数字 d 出现的总次数。
   - **核心思路**：前缀和思想，统计[0,high] - [0,low-1]
   - **C++实现**：
   ```cpp
   class Solution {
   public:
       int digitsCount(int d, int low, int high) {
           return countDigit(d, high) - countDigit(d, low - 1);
       }
       
   private:
       int countDigit(int d, int n) {
           if (n < 0) return 0;
           string s = to_string(n);
           int len = s.size();
           vector<vector<vector<int>>> dp(len, vector<vector<int>>(2, vector<int>(2, -1)));
           
           function<int(int, bool, bool, int)> dfs = [&](int pos, bool limit, bool lead, int cnt) -> int {
               if (pos == len) return cnt;
               if (!lead && dp[pos][limit][0] != -1) return dp[pos][limit][0];
               if (lead && d == 0 && dp[pos][limit][1] != -1) return dp[pos][limit][1];
               
               int ans = 0;
               int upper = limit ? (s[pos] - '0') : 9;
               
               for (int i = 0; i <= upper; ++i) {
                   bool new_limit = limit && (i == upper);
                   bool new_lead = lead && (i == 0);
                   int new_cnt = cnt;
                   
                   if (!new_lead || (new_lead && d == 0 && i != 0)) {
                       if (i == d) new_cnt++;
                   }
                   
                   ans += dfs(pos + 1, new_limit, new_lead, new_cnt);
               }
               
               if (!lead) dp[pos][limit][0] = ans;
               else if (d == 0) dp[pos][limit][1] = ans;
               return ans;
           };
           
           return dfs(0, true, true, 0);
       }
   };
   ```

### 洛谷 (Luogu)
1. **洛谷 P2602 [ZJOI2010] 数字计数** - https://www.luogu.com.cn/problem/P2602
   - 类型：数位DP
   - 难度：提高+/省选-
   - 简介：给定两个正整数 a 和 b，求在 [a,b] 中的所有整数中，每个数码各出现了多少次。
   - **核心思路**：分别计算[0,b]和[0,a-1]中各数字出现次数，然后相减
   - **Java实现**：
   ```java
   import java.util.*;
   
   public class Main {
       public static void main(String[] args) {
           Scanner sc = new Scanner(System.in);
           long a = sc.nextLong();
           long b = sc.nextLong();
           
           long[] countA = countDigits(a - 1);
           long[] countB = countDigits(b);
           
           for (int i = 0; i < 10; i++) {
               System.out.print((countB[i] - countA[i]) + " ");
           }
       }
       
       private static long[] countDigits(long n) {
           if (n < 0) return new long[10];
           String s = String.valueOf(n);
           int len = s.length();
           long[] res = new long[10];
           
           for (int d = 0; d < 10; d++) {
               res[d] = countSingleDigit(d, s, len);
           }
           
           return res;
       }
       
       private static long countSingleDigit(int digit, String s, int len) {
           long[][][] dp = new long[len][2][2];
           for (int i = 0; i < len; i++) {
               Arrays.fill(dp[i][0], -1);
               Arrays.fill(dp[i][1], -1);
           }
           return dfs(0, true, true, 0, digit, s.toCharArray(), dp);
       }
       
       private static long dfs(int pos, boolean limit, boolean lead, long cnt, int digit, char[] s, long[][][] dp) {
           if (pos == s.length) return cnt;
           
           if (!lead && dp[pos][limit ? 1 : 0][0] != -1) {
               return dp[pos][limit ? 1 : 0][0];
           }
           if (lead && digit == 0 && dp[pos][limit ? 1 : 0][1] != -1) {
               return dp[pos][limit ? 1 : 0][1];
           }
           
           long ans = 0;
           int upper = limit ? (s[pos] - '0') : 9;
           
           for (int i = 0; i <= upper; i++) {
               boolean newLimit = limit && (i == upper);
               boolean newLead = lead && (i == 0);
               long newCnt = cnt;
               
               if (!newLead || (newLead && digit == 0 && i != 0)) {
                   if (i == digit) newCnt++;
               }
               
               ans += dfs(pos + 1, newLimit, newLead, newCnt, digit, s, dp);
           }
           
           if (!lead) dp[pos][limit ? 1 : 0][0] = ans;
           else if (digit == 0) dp[pos][limit ? 1 : 0][1] = ans;
           return ans;
       }
   }
   ```

2. **洛谷 P2657 [SCOI2009] windy 数** - https://www.luogu.com.cn/problem/P2657
   - 类型：数位DP
   - 难度：提高+/省选-
   - 简介：不含前导零且相邻两个数字之差至少为 2 的正整数被称为 windy 数，统计范围内 windy 数的个数。
   - **Python实现**：
   ```python
   def count(n):
       if n < 0:
           return 0
       s = str(n)
       @lru_cache(None)
       def dfs(pos, last, limit, lead):
           if pos == len(s):
               return 1 if not lead else 0
           res = 0
           upper = int(s[pos]) if limit else 9
           for i in range(upper + 1):
               if lead:
                   # 前导零状态
                   if i == 0:
                       res += dfs(pos + 1, -10, limit and (i == upper), True)
                   else:
                       res += dfs(pos + 1, i, limit and (i == upper), False)
               else:
                   # 检查与前一位的差
                   if abs(i - last) >= 2:
                       res += dfs(pos + 1, i, limit and (i == upper), False)
           return res
       return dfs(0, -10, True, True)
   
   a, b = map(int, input().split())
   print(count(b) - count(a - 1))
   ```

### HDU (杭电OJ)
1. **HDU 2089 不要62** - http://acm.hdu.edu.cn/showproblem.php?pid=2089
   - 类型：数位DP
   - 难度：简单
   - 简介：统计不含数字4和连续的62的数的个数。
   - **核心思路**：简单的数位DP，状态包括当前位置和前一位数字
   - **Java实现**：
   ```java
   import java.util.*;
   
   public class Main {
       public static void main(String[] args) {
           Scanner sc = new Scanner(System.in);
           while (true) {
               int n = sc.nextInt();
               int m = sc.nextInt();
               if (n == 0 && m == 0) break;
               System.out.println(count(m) - count(n - 1));
           }
       }
       
       private static int count(int n) {
           if (n < 0) return 0;
           char[] s = String.valueOf(n).toCharArray();
           int len = s.length;
           int[][][] dp = new int[len][10][2];
           for (int i = 0; i < len; i++) {
               for (int j = 0; j < 10; j++) {
                   Arrays.fill(dp[i][j], -1);
               }
           }
           return dfs(0, -1, true, false, s, dp);
       }
       
       private static int dfs(int pos, int last, boolean limit, boolean has62, char[] s, int[][][] dp) {
           if (has62) return 0;
           if (pos == s.length) return 1;
           
           if (last != -1 && !limit && dp[pos][last][has62 ? 1 : 0] != -1) {
               return dp[pos][last][has62 ? 1 : 0];
           }
           
           int ans = 0;
           int upper = limit ? (s[pos] - '0') : 9;
           
           for (int i = 0; i <= upper; i++) {
               if (i == 4) continue;  // 不能包含4
               boolean newHas62 = has62 || (last == 6 && i == 2);  // 检查是否包含62
               ans += dfs(pos + 1, i, limit && (i == upper), newHas62, s, dp);
           }
           
           if (last != -1 && !limit) {
               dp[pos][last][has62 ? 1 : 0] = ans;
           }
           return ans;
       }
   }
   ```

2. **HDU 3555 Bomb** - http://acm.hdu.edu.cn/showproblem.php?pid=3555
   - 类型：数位DP
   - 难度：中等
   - 简介：统计包含连续的49的数的个数。
   - **核心思路**：补集思想，计算不包含49的数的个数，总数减去它

### Codeforces
1. **Codeforces 1073E Segment Sum** - https://codeforces.com/problemset/problem/1073/E
   - 类型：数位DP
   - 难度：2100
   - 简介：计算区间内最多包含k个不同数字的数的和。
   - **核心思路**：同时计算符合条件的数的个数和总和
   - **Python实现**：
   ```python
   MOD = 10**9 + 7
   
   def main():
       L, R, K = map(int, input().split())
       
       def calc(n):
           s = str(n)
           n = len(s)
           # dp[pos][mask][cnt][limit][lead] = (count, sum)
           dp = [[[[[(-1, -1) for _ in range(2)] for __ in range(2)] for ___ in range(11)] for ____ in range(1 << 10)] for _____ in range(n)]
           
           def dfs(pos, mask, cnt, limit, lead):
               if pos == n:
                   return (1, 0) if not lead else (0, 0)
               if dp[pos][mask][cnt][limit][lead] != (-1, -1):
                   return dp[pos][mask][cnt][limit][lead]
               
               res_count = 0
               res_sum = 0
               upper = int(s[pos]) if limit else 9
               
               for d in range(upper + 1):
                   new_limit = limit and (d == upper)
                   new_lead = lead and (d == 0)
                   
                   if new_lead:
                       c, s_val = dfs(pos + 1, 0, 0, new_limit, new_lead)
                       res_count = (res_count + c) % MOD
                       res_sum = (res_sum + s_val) % MOD
                   else:
                       new_mask = mask | (1 << d)
                       new_cnt = bin(new_mask).count('1')
                       if new_cnt > K:
                           continue
                       
                       c, s_val = dfs(pos + 1, new_mask, new_cnt, new_limit, new_lead)
                       # 当前位的贡献是 d * 10^(n-pos-1) * c + s_val
                       power = pow(10, n - pos - 1, MOD)
                       res_count = (res_count + c) % MOD
                       res_sum = (res_sum + d * power % MOD * c % MOD + s_val) % MOD
               
               dp[pos][mask][cnt][limit][lead] = (res_count, res_sum)
               return (res_count, res_sum)
           
           return dfs(0, 0, 0, True, True)[1]
       
       result = (calc(R) - calc(L - 1)) % MOD
       print(result)
   
   main()
   ```

## 🎯 解题思路与技巧总结

### 1. 数位DP基本思想
数位DP是一种在数位上进行的动态规划方法，主要用于解决以下几类问题：
1. 统计区间内满足特定条件的数字个数
2. 计算区间内满足特定条件的数字的某种属性总和
3. 构造满足特定条件的数字

### 2. 核心状态设计
数位DP的状态设计通常包括以下几个维度：
1. `pos`：当前处理到第几位
2. `limit`：是否受到上界限制（布尔型）
3. `lead`：是否有前导零（布尔型）
4. 其他题目相关的状态（如已使用的数字、前一位数字、匹配进度等）

### 3. 常见技巧
1. **前缀和思想**：将区间问题转化为两个前缀问题的差
2. **记忆化搜索**：使用数组或哈希表缓存已计算的状态
3. **状态压缩**：用位运算表示已使用的数字状态（对于数字不重复类问题）
4. **补集思想**：正难则反，计算不满足条件的个数，用总数减去它
5. **结合KMP**：对于字符串匹配类问题，结合KMP算法跟踪匹配状态
6. **预处理**：预处理一些固定结构的结果，避免重复计算
7. **同时维护多个值**：对于求和类问题，同时维护符合条件的数的个数和总和

### 4. 时间复杂度分析
- 基础数位DP：O(log N * 状态数)，其中log N是数字的位数
- 字符串相关数位DP：O(N * M * 状态数)，其中N是字符串长度，M是模式串长度
- 对于使用位掩码的问题：状态数通常包含2^10（最多10个不同数字）

### 5. 常见错误点
1. **前导零处理**：忽略前导零对结果的影响
2. **状态转移错误**：没有正确处理状态的转移逻辑
3. **边界条件**：处理不好边界情况导致错误
4. **溢出问题**：对于大数问题，没有考虑数据类型溢出
5. **重复计算**：没有正确使用记忆化导致重复计算

### 6. 工程化考量
1. **代码可读性**：清晰的变量命名和详细的注释
2. **模块化设计**：将数位DP的核心逻辑封装成函数
3. **异常处理**：处理各种边界情况和异常输入
4. **性能优化**：根据实际情况选择合适的记忆化方式
5. **多语言实现**：考虑不同语言的特性差异

## 🧠 数位DP常见题型

### 1. 基础计数问题
- 统计特定数字出现次数（如LeetCode 233）
- 统计满足数位条件的数字个数（如不含连续1的数字）

### 2. 状态压缩问题
- 统计各位数字不同的数字个数（如LeetCode 357、1012）
- 使用位掩码记录已使用的数字

### 3. 字符串匹配问题
- 统计不包含特定子串的字符串个数（如LeetCode 1397）
- 结合KMP等字符串算法

### 4. 二进制相关问题
- 二进制数位DP（如LeetCode 600）
- 统计二进制表示满足特定条件的数字

### 5. 求和类问题
- 计算满足条件的数的和（如Codeforces 1073E）
- 同时维护计数和总和

## 🚀 学习建议

1. **掌握基础**：先熟练掌握经典题目如LeetCode 233、600等
2. **理解模板**：理解数位DP的通用模板，能够根据不同题目调整状态设计
3. **多练习**：从简单到复杂，逐步提高解决问题的能力
4. **总结规律**：总结常见题型的解题思路和技巧
5. **工程实践**：注重代码质量、异常处理和测试覆盖
6. **多语言实现**：尝试用不同语言实现同一个问题，加深理解

## 🔍 深度剖析：数位DP的底层逻辑

### 1. 为什么数位DP高效？
数位DP的高效性来源于：
- **逐位处理**：将问题分解到每一位，降低问题的复杂度
- **记忆化搜索**：避免重复计算相同状态的子问题
- **状态压缩**：合理设计状态，减少状态空间

### 2. 状态设计的艺术
状态设计是数位DP的核心，良好的状态设计应该：
- **足够表达**：能够完整表达问题的约束条件
- **精简高效**：尽量减少状态的维度和数量
- **易于转移**：方便状态之间的转移计算

### 3. 记忆化的实现方式
不同语言实现记忆化的方式不同：
- **Java**：通常使用多维数组
- **Python**：使用lru_cache装饰器或字典
- **C++**：使用vector或map

### 4. 跨语言实现的差异
不同语言实现数位DP时需要注意：
- **数据类型范围**：Java和C++需要注意整数溢出
- **递归深度**：Python对于大数问题可能需要增加递归深度限制
- **性能优化**：C++的数组访问通常比Java快

### 5. 工程化实践建议
- **封装模板**：将数位DP的通用逻辑封装成模板类或函数
- **单元测试**：为不同类型的数位DP问题编写单元测试
- **异常处理**：处理各种边界情况和异常输入
- **性能监控**：对于大规模数据，监控算法性能

通过深入理解数位DP的底层逻辑，结合大量的练习和实践，你将能够熟练掌握这一强大的算法技术，解决各种复杂的数位相关问题。