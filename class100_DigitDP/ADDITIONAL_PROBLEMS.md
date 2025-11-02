# 数位DP扩展题目大全 - 全网平台穷尽搜索

## 一、LeetCode (力扣) - 深度扩展

### 1. 233. 数字 1 的个数（数学优化版）
**题目链接**: https://leetcode.cn/problems/number-of-digit-one/
**数学分析**: 使用组合数学公式直接计算，避免DP
**时间复杂度**: O(log N)
**空间复杂度**: O(1)

```java
// 数学方法实现 - 最优解
class Solution {
    public int countDigitOne(int n) {
        if (n <= 0) return 0;
        
        long count = 0;
        long factor = 1;
        long lower = 0;
        long curr = 0;
        long higher = 0;
        
        while (n / factor != 0) {
            lower = n - (n / factor) * factor;
            curr = (n / factor) % 10;
            higher = n / (factor * 10);
            
            if (curr == 0) {
                count += higher * factor;
            } else if (curr == 1) {
                count += higher * factor + lower + 1;
            } else {
                count += (higher + 1) * factor;
            }
            
            factor *= 10;
        }
        
        return (int) count;
    }
}
```

```cpp
// C++数学实现
class Solution {
public:
    int countDigitOne(int n) {
        if (n <= 0) return 0;
        
        long count = 0;
        long factor = 1;
        long lower, curr, higher;
        
        while (n / factor != 0) {
            lower = n - (n / factor) * factor;
            curr = (n / factor) % 10;
            higher = n / (factor * 10);
            
            if (curr == 0) {
                count += higher * factor;
            } else if (curr == 1) {
                count += higher * factor + lower + 1;
            } else {
                count += (higher + 1) * factor;
            }
            
            factor *= 10;
        }
        
        return count;
    }
};
```

```python
# Python数学实现
class Solution:
    def countDigitOne(self, n: int) -> int:
        if n <= 0:
            return 0
            
        count = 0
        factor = 1
        while n // factor != 0:
            lower = n - (n // factor) * factor
            curr = (n // factor) % 10
            higher = n // (factor * 10)
            
            if curr == 0:
                count += higher * factor
            elif curr == 1:
                count += higher * factor + lower + 1
            else:
                count += (higher + 1) * factor
                
            factor *= 10
            
        return count
```

### 2. 600. 不含连续1的非负整数（斐波那契数列方法）
**数学发现**: 该问题等价于斐波那契数列问题
**时间复杂度**: O(log N)
**空间复杂度**: O(1)

```java
class Solution {
    public int findIntegers(int n) {
        // 预处理斐波那契数列（长度问题）
        int[] fib = new int[32];
        fib[0] = 1;
        fib[1] = 2;
        for (int i = 2; i < 32; i++) {
            fib[i] = fib[i-1] + fib[i-2];
        }
        
        int ans = 0;
        boolean prevBit = false;
        
        for (int i = 30; i >= 0; i--) {
            if ((n & (1 << i)) != 0) {
                ans += fib[i];
                if (prevBit) {
                    // 出现连续1，后面的数都不符合条件
                    return ans;
                }
                prevBit = true;
            } else {
                prevBit = false;
            }
        }
        
        return ans + 1; // 加上n本身
    }
}
```

### 3. 902. 最大为 N 的数字组合（字典序优化）
**优化技巧**: 利用数字集合的字典序性质
**时间复杂度**: O(L × M) 其中M是数字集合大小

```java
class Solution {
    public int atMostNGivenDigitSet(String[] digits, int n) {
        String s = String.valueOf(n);
        int len = s.length();
        int m = digits.length;
        
        int[] dp = new int[len + 1];
        dp[len] = 1;
        
        for (int i = len - 1; i >= 0; i--) {
            int si = s.charAt(i) - '0';
            for (String d : digits) {
                int num = Integer.parseInt(d);
                if (num < si) {
                    dp[i] += Math.pow(m, len - i - 1);
                } else if (num == si) {
                    dp[i] += dp[i + 1];
                }
            }
        }
        
        // 加上位数小于len的所有可能
        for (int i = 1; i < len; i++) {
            dp[0] += Math.pow(m, i);
        }
        
        return dp[0];
    }
}
```

### 4. 1015. 可被 K 整除的最小整数（数论+数位DP）
**题目链接**: https://leetcode.cn/problems/smallest-integer-divisible-by-k/
**数学技巧**: 利用模运算周期性质
**时间复杂度**: O(K)

```java
class Solution {
    public int smallestRepunitDivByK(int k) {
        if (k % 2 == 0 || k % 5 == 0) return -1;
        
        int remainder = 0;
        for (int length = 1; length <= k; length++) {
            remainder = (remainder * 10 + 1) % k;
            if (remainder == 0) {
                return length;
            }
        }
        return -1;
    }
}
```

## 二、洛谷 (Luogu) - 竞赛级题目

### 1. P2602 [ZJOI2010] 数字计数（多数字统计）
**数学优化**: 同时统计0-9所有数字的出现次数
**时间复杂度**: O(10 × log N)

```java
import java.util.*;
import java.io.*;

public class Main {
    static long[][] dp = new long[15][2];
    static int[] digits = new int[15];
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        long a = Long.parseLong(st.nextToken());
        long b = Long.parseLong(st.nextToken());
        
        long[] res1 = solve(a - 1);
        long[] res2 = solve(b);
        
        for (int i = 0; i < 10; i++) {
            System.out.print((res2[i] - res1[i]) + " ");
        }
    }
    
    static long[] solve(long n) {
        if (n < 0) return new long[10];
        int len = 0;
        long tmp = n;
        while (tmp > 0) {
            digits[++len] = (int)(tmp % 10);
            tmp /= 10;
        }
        
        long[] res = new long[10];
        for (int d = 0; d < 10; d++) {
            res[d] = dfs(len, d, true, true, 0);
        }
        return res;
    }
    
    static long dfs(int pos, int target, boolean limit, boolean lead, long cnt) {
        if (pos == 0) return cnt;
        
        if (!limit && !lead && dp[pos][0] != -1) {
            return cnt + dp[pos][0];
        }
        
        long res = 0;
        int up = limit ? digits[pos] : 9;
        
        for (int i = 0; i <= up; i++) {
            boolean newLimit = limit && (i == up);
            boolean newLead = lead && (i == 0);
            long newCnt = cnt;
            
            if (!newLead || (newLead && target == 0 && i != 0)) {
                if (i == target) newCnt++;
            }
            
            res += dfs(pos - 1, target, newLimit, newLead, newCnt);
        }
        
        if (!limit && !lead) {
            dp[pos][0] = res - cnt;
        }
        
        return res;
    }
}
```

### 2. P2657 [SCOI2009] windy 数（经典数位DP）
**状态设计**: 记录前一位数字用于判断差值
**时间复杂度**: O(10 × log N)

```java
import java.util.*;
import java.io.*;

public class Main {
    static int[][][] dp = new int[15][11][2];
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int a = Integer.parseInt(st.nextToken());
        int b = Integer.parseInt(st.nextToken());
        
        System.out.println(count(b) - count(a - 1));
    }
    
    static int count(int n) {
        if (n < 0) return 0;
        char[] s = String.valueOf(n).toCharArray();
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 11; j++) {
                Arrays.fill(dp[i][j], -1);
            }
        }
        return dfs(0, 10, true, true, s);
    }
    
    static int dfs(int pos, int last, boolean limit, boolean lead, char[] s) {
        if (pos == s.length) return lead ? 0 : 1;
        
        if (!limit && !lead && dp[pos][last][0] != -1) {
            return dp[pos][last][0];
        }
        
        int res = 0;
        int up = limit ? (s[pos] - '0') : 9;
        
        for (int i = 0; i <= up; i++) {
            if (lead) {
                if (i == 0) {
                    res += dfs(pos + 1, 10, limit && (i == up), true, s);
                } else {
                    res += dfs(pos + 1, i, limit && (i == up), false, s);
                }
            } else {
                if (Math.abs(i - last) >= 2) {
                    res += dfs(pos + 1, i, limit && (i == up), false, s);
                }
            }
        }
        
        if (!limit && !lead) {
            dp[pos][last][0] = res;
        }
        
        return res;
    }
}
```

### 3. P4124 [CQOI2016] 手机号码（复杂约束）
**多重约束**: 11位手机号码的特殊约束
**状态设计**: 多维度状态记录各种约束

```java
import java.util.*;
import java.io.*;

public class Main {
    static long[][][][][] dp = new long[12][10][10][2][2];
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        long L = Long.parseLong(st.nextToken());
        long R = Long.parseLong(st.nextToken());
        
        System.out.println(count(R) - count(L - 1));
    }
    
    static long count(long n) {
        if (n < 10000000000L) return 0;
        char[] s = String.valueOf(n).toCharArray();
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 10; j++) {
                for (int k = 0; k < 10; k++) {
                    for (int l = 0; l < 2; l++) {
                        Arrays.fill(dp[i][j][k][l], -1);
                    }
                }
            }
        }
        return dfs(0, 10, 10, 0, true, true, s);
    }
    
    static long dfs(int pos, int pre1, int pre2, int has4, boolean limit, boolean lead, char[] s) {
        if (pos == s.length) {
            // 检查约束：不能有4，要有连续3个相同数字，后4位相同，后5位是顺子
            return (has4 == 0) ? 1 : 0; // 简化版本，实际需要更复杂检查
        }
        
        // 简化实现，实际需要更复杂的状态设计
        return 0;
    }
}
```

## 三、Codeforces - 国际竞赛题目

### 1. Codeforces 55D - Beautiful Numbers（数论+数位DP）
**数学技巧**: 利用1-9的LCM是2520的性质
**状态压缩**: 模2520的余数和数字使用掩码

```java
import java.util.*;
import java.io.*;

public class CF55D {
    static long[][][] dp = new long[20][2520][1<<8];
    static int[] digits = new int[20];
    static int[] modMap = new int[2530];
    static int MOD = 2520;
    
    public static void main(String[] args) throws IOException {
        // 预处理mod映射
        int idx = 0;
        for (int i = 1; i <= MOD; i++) {
            if (MOD % i == 0) {
                modMap[i] = idx++;
            }
        }
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());
        
        while (T-- > 0) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            long L = Long.parseLong(st.nextToken());
            long R = Long.parseLong(st.nextToken());
            
            System.out.println(count(R) - count(L - 1));
        }
    }
    
    static long count(long n) {
        if (n == 0) return 0;
        int len = 0;
        while (n > 0) {
            digits[len++] = (int)(n % 10);
            n /= 10;
        }
        
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < MOD; j++) {
                Arrays.fill(dp[i][j], -1);
            }
        }
        
        return dfs(len - 1, 0, 1, true, true);
    }
    
    static long dfs(int pos, int mod, int mask, boolean limit, boolean lead) {
        if (pos < 0) {
            for (int i = 2; i <= 9; i++) {
                if ((mask & (1 << (i-2))) != 0 && mod % i != 0) {
                    return 0;
                }
            }
            return 1;
        }
        
        if (!limit && !lead && dp[pos][mod][mask] != -1) {
            return dp[pos][mod][mask];
        }
        
        long res = 0;
        int up = limit ? digits[pos] : 9;
        
        for (int i = 0; i <= up; i++) {
            boolean newLimit = limit && (i == up);
            boolean newLead = lead && (i == 0);
            int newMod = (mod * 10 + i) % MOD;
            int newMask = mask;
            
            if (!newLead && i > 1) {
                newMask |= (1 << (i-2));
            }
            
            res += dfs(pos - 1, newMod, newMask, newLimit, newLead);
        }
        
        if (!limit && !lead) {
            dp[pos][mod][mask] = res;
        }
        
        return res;
    }
}
```

### 2. Codeforces 1073E - Segment Sum（求和问题）
**特殊要求**: 同时计算个数和总和
**状态设计**: 维护(count, sum)二元组

```java
import java.util.*;
import java.io.*;

public class CF1073E {
    static class Pair {
        long count, sum;
        Pair(long c, long s) { count = c; sum = s; }
    }
    
    static Pair[][][][][] dp = new Pair[20][1<<10][2][2][2];
    static int MOD = 998244353;
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        long L = Long.parseLong(st.nextToken());
        long R = Long.parseLong(st.nextToken());
        int K = Integer.parseInt(st.nextToken());
        
        long res = (calc(R, K) - calc(L - 1, K) + MOD) % MOD;
        System.out.println(res);
    }
    
    static long calc(long n, int K) {
        if (n < 0) return 0;
        char[] s = String.valueOf(n).toCharArray();
        int len = s.length;
        
        // 初始化DP数组
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < (1<<10); j++) {
                for (int k = 0; k < 2; k++) {
                    for (int l = 0; l < 2; l++) {
                        Arrays.fill(dp[i][j][k][l], null);
                    }
                }
            }
        }
        
        Pair res = dfs(0, 0, 0, true, true, s, K);
        return res.sum;
    }
    
    static Pair dfs(int pos, int mask, int cnt, boolean limit, boolean lead, char[] s, int K) {
        if (pos == s.length) {
            if (lead) return new Pair(0, 0);
            return cnt <= K ? new Pair(1, 0) : new Pair(0, 0);
        }
        
        if (dp[pos][mask][limit?1:0][lead?1:0][cnt>K?1:0] != null) {
            return dp[pos][mask][limit?1:0][lead?1:0][cnt>K?1:0];
        }
        
        Pair res = new Pair(0, 0);
        int up = limit ? (s[pos] - '0') : 9;
        
        for (int i = 0; i <= up; i++) {
            boolean newLimit = limit && (i == up);
            boolean newLead = lead && (i == 0);
            int newMask = mask;
            int newCnt = cnt;
            
            if (!newLead) {
                if ((mask & (1 << i)) == 0) {
                    newMask |= (1 << i);
                    newCnt++;
                }
            }
            
            if (newCnt > K) continue;
            
            Pair next = dfs(pos + 1, newMask, newCnt, newLimit, newLead, s, K);
            long power = pow(10, s.length - pos - 1, MOD);
            
            res.count = (res.count + next.count) % MOD;
            res.sum = (res.sum + next.sum + i * power % MOD * next.count % MOD) % MOD;
        }
        
        dp[pos][mask][limit?1:0][lead?1:0][cnt>K?1:0] = res;
        return res;
    }
    
    static long pow(long a, long b, long mod) {
        long res = 1;
        while (b > 0) {
            if ((b & 1) == 1) res = res * a % mod;
            a = a * a % mod;
            b >>= 1;
        }
        return res;
    }
}
```

## 四、AtCoder - 日本竞赛题目

### 1. ABC135D - Digits Parade（模运算+通配符）
**特殊字符**: 包含'?'通配符
**模运算**: 模13的余数计算

```java
import java.util.*;
import java.io.*;

public class ABC135D {
    static long[][] dp = new long[100005][13];
    static int MOD = 1000000007;
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s = br.readLine();
        int n = s.length();
        
        dp[0][0] = 1;
        
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            for (int j = 0; j < 13; j++) {
                if (dp[i][j] == 0) continue;
                
                if (c == '?') {
                    for (int d = 0; d < 10; d++) {
                        int newMod = (j * 10 + d) % 13;
                        dp[i+1][newMod] = (dp[i+1][newMod] + dp[i][j]) % MOD;
                    }
                } else {
                    int d = c - '0';
                    int newMod = (j * 10 + d) % 13;
                    dp[i+1][newMod] = (dp[i+1][newMod] + dp[i][j]) % MOD;
                }
            }
        }
        
        System.out.println(dp[n][5]);
    }
}
```

## 五、HDU (杭电OJ) - 国内竞赛题目

### 1. HDU 2089 不要62（经典数位DP）
**约束条件**: 不能包含4和连续的62
**状态设计**: 记录前一位数字

```java
import java.util.*;
import java.io.*;

public class HDU2089 {
    static int[][][] dp = new int[10][10][2];
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;
        
        while ((line = br.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(line);
            int n = Integer.parseInt(st.nextToken());
            int m = Integer.parseInt(st.nextToken());
            if (n == 0 && m == 0) break;
            
            System.out.println(count(m) - count(n - 1));
        }
    }
    
    static int count(int num) {
        if (num < 0) return 0;
        char[] s = String.valueOf(num).toCharArray();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Arrays.fill(dp[i][j], -1);
            }
        }
        return dfs(0, -1, false, true, s);
    }
    
    static int dfs(int pos, int last, boolean has62, boolean limit, char[] s) {
        if (has62) return 0;
        if (pos == s.length) return 1;
        
        if (last != -1 && !limit && dp[pos][last][has62?1:0] != -1) {
            return dp[pos][last][has62?1:0];
        }
        
        int ans = 0;
        int up = limit ? (s[pos] - '0') : 9;
        
        for (int i = 0; i <= up; i++) {
            if (i == 4) continue;
            boolean newHas62 = has62 || (last == 6 && i == 2);
            ans += dfs(pos + 1, i, newHas62, limit && (i == up), s);
        }
        
        if (last != -1 && !limit) {
            dp[pos][last][has62?1:0] = ans;
        }
        
        return ans;
    }
}
```

## 六、牛客网 (Nowcoder) - 国内面试题目

### 1. 数位小孩（综合约束）
**多种约束**: 数字和约束、奇偶约束等
**状态设计**: 多维度状态记录

```java
import java.util.*;
import java.io.*;

public class NowcoderDigitChild {
    static long[][][][] dp = new long[20][200][2][2];
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        long L = Long.parseLong(st.nextToken());
        long R = Long.parseLong(st.nextToken());
        int K = Integer.parseInt(st.nextToken());
        
        System.out.println(count(R, K) - count(L - 1, K));
    }
    
    static long count(long n, int K) {
        if (n < 0) return 0;
        char[] s = String.valueOf(n).toCharArray();
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 200; j++) {
                for (int k = 0; k < 2; k++) {
                    Arrays.fill(dp[i][j][k], -1);
                }
            }
        }
        return dfs(0, 0, 0, true, true, s, K);
    }
    
    static long dfs(int pos, int sum, int odd, boolean limit, boolean lead, char[] s, int K) {
        if (pos == s.length) {
            if (lead) return 0;
            return (sum % K == 0 && odd % 2 == 0) ? 1 : 0;
        }
        
        if (!limit && !lead && dp[pos][sum][odd][0] != -1) {
            return dp[pos][sum][odd][0];
        }
        
        long ans = 0;
        int up = limit ? (s[pos] - '0') : 9;
        
        for (int i = 0; i <= up; i++) {
            boolean newLimit = limit && (i == up);
            boolean newLead = lead && (i == 0);
            int newSum = sum + i;
            int newOdd = odd + (i % 2);
            
            ans += dfs(pos + 1, newSum, newOdd, newLimit, newLead, s, K);
        }
        
        if (!limit && !lead) {
            dp[pos][sum][odd][0] = ans;
        }
        
        return ans;
    }
}
```

## 七、POJ (北大OJ) - 经典竞赛题目

### 1. POJ 3252 Round Numbers（二进制数位DP）
**二进制处理**: 统计二进制表示中0的个数不少于1的个数
**状态设计**: 记录0和1的个数差

```java
import java.util.*;
import java.io.*;

public class POJ3252 {
    static int[][][] dp = new int[35][70][2]; // 35位二进制，差值范围[-35,35] -> [0,70]
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int start = Integer.parseInt(st.nextToken());
        int end = Integer.parseInt(st.nextToken());
        
        System.out.println(count(end) - count(start - 1));
    }
    
    static int count(int n) {
        if (n <= 0) return 0;
        String binary = Integer.toBinaryString(n);
        char[] s = binary.toCharArray();
        for (int i = 0; i < 35; i++) {
            for (int j = 0; j < 70; j++) {
                Arrays.fill(dp[i][j], -1);
            }
        }
        return dfs(0, 35, true, true, s); // 基准值35对应差值0
    }
    
    static int dfs(int pos, int diff, boolean limit, boolean lead, char[] s) {
        if (pos == s.length) {
            if (lead) return 0;
            return diff >= 35 ? 1 : 0; // diff>=35表示0的个数>=1的个数
        }
        
        if (!limit && !lead && dp[pos][diff][0] != -1) {
            return dp[pos][diff][0];
        }
        
        int ans = 0;
        int up = limit ? (s[pos] - '0') : 1;
        
        for (int i = 0; i <= up; i++) {
            boolean newLimit = limit && (i == up);
            boolean newLead = lead && (i == 0);
            int newDiff = diff;
            
            if (!newLead) {
                if (i == 0) newDiff++;
                else newDiff--;
            }
            
            ans += dfs(pos + 1, newDiff, newLimit, newLead, s);
        }
        
        if (!limit && !lead) {
            dp[pos][diff][0] = ans;
        }
        
        return ans;
    }
}
```

## 八、USACO - 美国计算机竞赛

### 1. USACO 2006 November - Round Numbers（类似POJ 3252）
**USACO版本**: 更严格的约束和处理

```java
import java.util.*;
import java.io.*;

public class USACORoundNumbers {
    // 实现与POJ 3252类似，但针对USACO输入格式优化
    // 省略具体实现，参考POJ 3252
}
```

## 九、SPOJ - 国际在线评测

### 1. SPOJ INVESTIGATION - Investigation（数位和+模运算）
**复杂约束**: 数位和模数约束
**状态设计**: 模运算状态

```java
import java.util.*;
import java.io.*;

public class SPOJINVESTIGATION {
    static long[][][][] dp = new long[20][200][100][2];
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());
        
        while (T-- > 0) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            long A = Long.parseLong(st.nextToken());
            long B = Long.parseLong(st.nextToken());
            int K = Integer.parseInt(st.nextToken());
            
            System.out.println(count(B, K) - count(A - 1, K));
        }
    }
    
    static long count(long n, int K) {
        if (n < 0) return 0;
        char[] s = String.valueOf(n).toCharArray();
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 200; j++) {
                for (int k = 0; k < 100; k++) {
                    Arrays.fill(dp[i][j][k], -1);
                }
            }
        }
        return dfs(0, 0, 0, true, true, s, K);
    }
    
    static long dfs(int pos, int sum, int mod, boolean limit, boolean lead, char[] s, int K) {
        if (pos == s.length) {
            if (lead) return 0;
            return (sum > 0 && mod == 0) ? 1 : 0;
        }
        
        if (!limit && !lead && dp[pos][sum][mod][0] != -1) {
            return dp[pos][sum][mod][0];
        }
        
        long ans = 0;
        int up = limit ? (s[pos] - '0') : 9;
        
        for (int i = 0; i <= up; i++) {
            boolean newLimit = limit && (i == up);
            boolean newLead = lead && (i == 0);
            int newSum = sum + i;
            int newMod = (mod * 10 + i) % K;
            
            ans += dfs(pos + 1, newSum, newMod, newLimit, newLead, s, K);
        }
        
        if (!limit && !lead) {
            dp[pos][sum][mod][0] = ans;
        }
        
        return ans;
    }
}
```

## 十、Project Euler - 数学编程挑战

### 1. Project Euler 36 - Double-base palindromes（双进制回文）
**特殊要求**: 在十进制和二进制下都是回文数
**状态设计**: 同时处理两种进制

```java
import java.util.*;
import java.io.*;

public class Euler36 {
    public static void main(String[] args) {
        long sum = 0;
        for (int i = 1; i < 1000000; i++) {
            if (isPalindrome(i, 10) && isPalindrome(i, 2)) {
                sum += i;
            }
        }
        System.out.println(sum);
    }
    
    static boolean isPalindrome(int n, int base) {
        String s = Integer.toString(n, base);
        return new StringBuilder(s).reverse().toString().equals(s);
    }
}
```

## 总结与学习建议

### 1. 题目分类总结
- **基础计数**: LeetCode 233, 357, 600
- **状态压缩**: LeetCode 1012, 1397  
- **模运算**: Codeforces 55D, AtCoder ABC135D
- **二进制处理**: POJ 3252, LeetCode 600
- **复杂约束**: 洛谷 P4124, HDU 2089

### 2. 学习路径建议
1. **初级阶段**: 掌握基础模板和简单题目
2. **中级阶段**: 学习状态压缩和模运算技巧
3. **高级阶段**: 解决复杂约束和综合问题
4. **专家阶段**: 参与竞赛和解决原创问题

### 3. 工程实践要点
- **代码规范**: 清晰的变量命名和注释
- **性能优化**: 合理使用记忆化和状态压缩
- **测试覆盖**: 全面测试边界情况
- **跨语言实现**: 掌握Java、C++、Python三种实现

通过系统学习以上题目，您将全面掌握数位DP算法，具备解决各类复杂数字问题的能力。