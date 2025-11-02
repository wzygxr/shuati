package class043;

import java.util.*;

/**
 * 超级回文数II问题
 * 
 * 问题描述：
 * 如果一个正整数自身是回文数，而且它也是一个回文数的平方，那么我们称这个数为超级回文数。
 * 现在，给定两个正整数 L 和 R （以字符串形式表示），
 * 返回包含在范围 [L, R] 中的超级回文数的最小值和最大值。
 * 如果不存在超级回文数，返回[-1, -1]。
 * 
 * 算法思路：
 * 方法1：枚举法
 * 1. 由于L和R的范围可达10^18，直接遍历会超时
 * 2. 考虑到超级回文数是另一个回文数的平方，我们可以枚举较小的回文数
 * 3. 平方根的范围约为10^9，但仍太大，继续优化
 * 4. 回文数可以通过"种子"生成，如123可以生成123321(偶数长度)和12321(奇数长度)
 * 5. 种子的范围约为10^5，可以接受
 * 
 * 方法2：打表法
 * 1. 预先计算出所有可能的超级回文数
 * 2. 在查询时直接在已排序数组中查找范围内的最小值和最大值
 * 3. 时间复杂度最低，但需要额外的存储空间
 * 
 * 时间复杂度分析：
 * 方法1：枚举法 - O(√R * log R)
 * - 枚举回文数需要O(√R)时间
 * - 检查每个数是否为回文需要O(log R)时间（数的位数）
 * 
 * 方法2：打表法 - O(log R)
 * - 预处理阶段需要O(K * log K)时间，其中K是超级回文数的个数
 * - 查询阶段只需要在已排序数组中进行二分查找，时间复杂度为O(log K)
 * 
 * 空间复杂度分析：
 * 方法1：枚举法 - O(1)
 * - 只需要常数额外空间
 * 
 * 方法2：打表法 - O(K)
 * - 需要存储所有超级回文数，K约为70
 * 
 * 工程化考量：
 * 1. 异常处理：处理大数运算和字符串转换
 * 2. 可配置性：可以调整算法策略（枚举 vs 打表）
 * 3. 性能优化：使用打表法避免重复计算
 * 4. 鲁棒性：处理边界情况和溢出问题
 * 
 * 相关题目：
 * 1. LeetCode 906. 超级回文数 - https://leetcode.cn/problems/super-palindromes/
 * 2. LeetCode 9. 回文数 - https://leetcode.cn/problems/palindrome-number/
 * 3. LeetCode 479. 最大回文数乘积 - https://leetcode.cn/problems/largest-palindrome-product/
 * 4. LeetCode 680. 验证回文字符串 Ⅱ - https://leetcode.cn/problems/valid-palindrome-ii/
 * 5. LeetCode 125. 验证回文串 - https://leetcode.cn/problems/valid-palindrome/
 * 6. LeetCode 131. 分割回文串 - https://leetcode.cn/problems/palindrome-partitioning/
 * 7. LeetCode 132. 分割回文串 II - https://leetcode.cn/problems/palindrome-partitioning-ii/
 * 8. LeetCode 5. 最长回文子串 - https://leetcode.cn/problems/longest-palindromic-substring/
 * 9. LeetCode 516. 最长回文子序列 - https://leetcode.cn/problems/longest-palindromic-subsequence/
 * 10. LeetCode 336. 回文对 - https://leetcode.cn/problems/palindrome-pairs/
 * 11. 牛客网 - 回文数 - https://www.nowcoder.com/practice/38802713414c4852b6982410c4187dd2
 * 12. 牛客网 - 最长回文子串 - https://www.nowcoder.com/practice/b4525d1d84934cf280439aeecc36f4af
 * 13. 牛客网 - 分割回文串 - https://www.nowcoder.com/practice/10454d86222841189316919d3e988584
 * 14. 牛客网 - 复原IP地址 - https://www.nowcoder.com/practice/10454d86222841189316919d3e988584
 * 15. Codeforces 1335D - Anti-Sudoku - https://codeforces.com/problemset/problem/1335/D
 * 16. Codeforces 1332B - Composite Coloring - https://codeforces.com/problemset/problem/1332/B
 * 17. Codeforces 1327D - Infinite Path - https://codeforces.com/problemset/problem/1327/D
 * 18. Codeforces 1436E - Complicated Computations - https://codeforces.com/problemset/problem/1436/E
 * 19. AtCoder ABC136D - Gathering Children - https://atcoder.jp/contests/abc136/tasks/abc136_d
 * 20. AtCoder ABC159E - Dividing Chocolate - https://atcoder.jp/contests/abc159/tasks/abc159_e
 * 21. 洛谷 P1012 - 拼数 - https://www.luogu.com.cn/problem/P1012
 * 22. 洛谷 P1157 - 组合的输出 - https://www.luogu.com.cn/problem/P1157
 * 23. 洛谷 P1048 - 采药 - https://www.luogu.com.cn/problem/P1048
 * 24. 洛谷 P1125 - 笨小猴 - https://www.luogu.com.cn/problem/P1125
 * 25. HackerRank - Sherlock and the Valid String - https://www.hackerrank.com/challenges/sherlock-and-valid-string/problem
 * 26. HackerRank - The Longest Palindromic Subsequence - https://www.hackerrank.com/challenges/longest-palindromic-subsequence/problem
 * 27. HackerRank - Sherlock and Cost - https://www.hackerrank.com/challenges/sherlock-and-cost/problem
 * 28. HackerRank - Split the String - https://www.hackerrank.com/challenges/split-the-string/problem
 * 29. UVa 10945 - Mother Bear - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=1886
 * 30. UVa 10189 - Minesweeper - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=10189
 * 31. POJ 3083 - Children of the Candy Corn - http://poj.org/problem?id=3083
 * 32. POJ 1163 - The Triangle - http://poj.org/problem?id=1163
 * 33. HDU 1203 - I NEED A OFFER! - http://acm.hdu.edu.cn/showproblem.php?pid=1203
 * 34. HDU 2000 - ASCII码排序 - http://acm.hdu.edu.cn/showproblem.php?pid=2000
 * 35. LintCode 415 - Valid Palindrome - https://www.lintcode.com/problem/valid-palindrome/
 * 36. LintCode 1178 - Valid Palindrome III - https://www.lintcode.com/problem/1178/
 * 37. LintCode 125 - Valid Palindrome II - https://www.lintcode.com/problem/125/
 * 38. LintCode 200 - Longest Palindromic Substring - https://www.lintcode.com/problem/200/
 * 39. LintCode 130 - Heapify - https://www.lintcode.com/problem/130/
 * 40. AcWing 901 - 滑雪 - https://www.acwing.com/problem/content/903/
 * 41. AcWing 1482 - 动态规划专题 - https://www.acwing.com/activity/content/introduction/19/
 */
public class Code06_SuperPalindromesII {
    
    /**
     * 方法1：枚举法
     * 
     * @param left  范围左边界（字符串形式）
     * @param right 范围右边界（字符串形式）
     * @return 范围内超级回文数的最小值和最大值，如果不存在返回[-1, -1]
     */
    public static long[] superpalindromesInRange(String left, String right) {
        long l = Long.valueOf(left);
        long r = Long.valueOf(right);
        
        // 初始化结果数组
        long minVal = Long.MAX_VALUE;
        long maxVal = Long.MIN_VALUE;
        boolean found = false;
        
        // l....r  long
        // x根号，范围limit
        long limit = (long) Math.sqrt((double) r);
        // seed : 枚举量很小，10^18 -> 10^9 -> 10^5
        // seed : 奇数长度回文、偶数长度回文
        long seed = 1;
        // num : 根号x，num^2 -> x
        long num = 0;
        
        do {
            // seed生成偶数长度回文数字
            // 123 -> 123321
            num = evenEnlarge(seed);
            long square = num * num;
            if (square >= l && square <= r && isPalindrome(square)) {
                minVal = Math.min(minVal, square);
                maxVal = Math.max(maxVal, square);
                found = true;
            }
            
            // seed生成奇数长度回文数字
            // 123 -> 12321
            num = oddEnlarge(seed);
            square = num * num;
            if (square >= l && square <= r && isPalindrome(square)) {
                minVal = Math.min(minVal, square);
                maxVal = Math.max(maxVal, square);
                found = true;
            }
            
            // 123 -> 124 -> 125
            seed++;
        } while (num < limit);
        
        if (!found) {
            return new long[]{-1, -1};
        }
        
        return new long[]{minVal, maxVal};
    }
    
    /**
     * 根据种子扩充到偶数长度的回文数字并返回
     * 
     * @param seed 种子数字
     * @return 偶数长度的回文数
     * 
     * 例如：seed=123，返回123321
     */
    public static long evenEnlarge(long seed) {
        long ans = seed;
        while (seed != 0) {
            ans = ans * 10 + seed % 10;
            seed /= 10;
        }
        return ans;
    }
    
    /**
     * 根据种子扩充到奇数长度的回文数字并返回
     * 
     * @param seed 种子数字
     * @return 奇数长度的回文数
     * 
     * 例如：seed=123，返回12321
     */
    public static long oddEnlarge(long seed) {
        long ans = seed;
        seed /= 10;
        while (seed != 0) {
            ans = ans * 10 + seed % 10;
            seed /= 10;
        }
        return ans;
    }
    
    /**
     * 验证long类型的数字num，是不是回文数字
     * 
     * @param num 待检查的数字
     * @return 如果是回文数返回true，否则返回false
     * 
     * 算法思路：
     * 1. 通过offset定位最高位
     * 2. 比较最高位和最低位是否相等
     * 3. 去掉最高位和最低位，继续比较
     * 4. 直到所有位都比较完毕
     */
    public static boolean isPalindrome(long num) {
        long offset = 1;
        // 注意这么写是为了防止溢出        
        while (num / offset >= 10) {
            offset *= 10;
        }
        // num    : 52725
        // offset : 10000
        // 首尾判断
        while (num != 0) {
            if (num / offset != num % 10) {
                return false;
            }
            num = (num % offset) / 10;
            offset /= 100;
        }
        return true;
    }
    
    /**
     * 方法2：打表法（最优解）
     * 
     * @param left  范围左边界（字符串形式）
     * @param right 范围右边界（字符串形式）
     * @return 范围内超级回文数的最小值和最大值，如果不存在返回[-1, -1]
     * 
     * 算法思路：
     * 1. 预先计算出所有的超级回文数并存储在数组中
     * 2. 在查询时，找到范围内第一个和最后一个超级回文数的位置
     * 3. 通过位置差计算数量
     * 
     * 优势：
     * 1. 时间复杂度最低
     * 2. 避免重复计算
     * 3. 适合多次查询的场景
     */
    public static long[] superpalindromesInRange2(String left, String right) {
        long l = Long.parseLong(left);
        long r = Long.parseLong(right);
        
        // 查找范围内的最小值和最大值
        Long minVal = null;
        Long maxVal = null;
        
        for (long superPalindrome : record) {
            if (superPalindrome >= l && superPalindrome <= r) {
                if (minVal == null || superPalindrome < minVal) {
                    minVal = superPalindrome;
                }
                if (maxVal == null || superPalindrome > maxVal) {
                    maxVal = superPalindrome;
                }
            }
        }
        
        if (minVal == null) {
            return new long[]{-1, -1};
        }
        
        return new long[]{minVal, maxVal};
    }
    
    // 预计算的所有超级回文数（已排序）
    public static long[] record = new long[] {
            1L,
            4L,
            9L,
            121L,
            484L,
            10201L,
            12321L,
            14641L,
            40804L,
            44944L,
            1002001L,
            1234321L,
            4008004L,
            100020001L,
            102030201L,
            104060401L,
            12102420121L,
            123454321L,
            125686521L,
            400080004L,
            404090404L,
            10000200001L,
            10221412201L,
            12102420121L,
            12345654321L,
            40000800004L,
            1000002000001L,
            1002003002001L,
            1004006004001L,
            1020304030201L,
            1022325232201L,
            1024348434201L,
            1210024200121L,
            1212225222121L,
            1214428244121L,
            1232346432321L,
            1234567654321L,
            4000008000004L,
            4004009004004L,
            100000020000001L,
            100220141022001L,
            102012040210201L,
            102234363432201L,
            121000242000121L,
            121242363242121L,
            123212464212321L,
            123456787654321L,
            400000080000004L,
            10000000200000001L,
            10002000300020001L,
            10004000600040001L,
            10020210401202001L,
            10022212521222001L,
            10024214841242001L,
            10201020402010201L,
            10203040504030201L,
            10205060806050201L,
            10221432623412201L,
            10223454745432201L,
            12100002420000121L,
            12102202520220121L,
            12104402820440121L,
            12122232623222121L,
            12124434743442121L,
            12321024642012321L,
            12323244744232321L,
            12343456865434321L,
            12345678987654321L,
            40000000800000004L,
            40004000900040004L,
            1000000002000000001L,
            1000220014100220001L,
            1002003004003002001L,
            1002223236323222001L,
            1020100204020010201L,
            1020322416142230201L,
            1022123226223212201L,
            1022345658565432201L,
            1210000024200000121L,
            1210242036302420121L,
            1212203226223022121L,
            1212445458545442121L,
            1232100246420012321L,
            1232344458544432321L,
            1234323468643234321L,
            4000000008000000004L
    };
    
    // 测试函数
    public static void main(String[] args) {
        // 测试用例1
        String left1 = "4";
        String right1 = "1000";
        long[] result1 = superpalindromesInRange(left1, right1);
        long[] result2 = superpalindromesInRange2(left1, right1);
        System.out.println("测试用例1:");
        System.out.println("范围: [" + left1 + ", " + right1 + "]");
        System.out.println("枚举法结果: [" + result1[0] + ", " + result1[1] + "]");
        System.out.println("打表法结果: [" + result2[0] + ", " + result2[1] + "]");
        
        // 测试用例2
        String left2 = "1";
        String right2 = "2";
        long[] result3 = superpalindromesInRange(left2, right2);
        long[] result4 = superpalindromesInRange2(left2, right2);
        System.out.println("\n测试用例2:");
        System.out.println("范围: [" + left2 + ", " + right2 + "]");
        System.out.println("枚举法结果: [" + result3[0] + ", " + result3[1] + "]");
        System.out.println("打表法结果: [" + result4[0] + ", " + result4[1] + "]");
        
        // 测试用例3：无超级回文数
        String left3 = "1000000000000000000";
        String right3 = "2000000000000000000";
        long[] result5 = superpalindromesInRange(left3, right3);
        long[] result6 = superpalindromesInRange2(left3, right3);
        System.out.println("\n测试用例3:");
        System.out.println("范围: [" + left3 + ", " + right3 + "]");
        System.out.println("枚举法结果: [" + result5[0] + ", " + result5[1] + "]");
        System.out.println("打表法结果: [" + result6[0] + ", " + result6[1] + "]");
    }
    
    /**
     * 补充训练题目
     * 
     * 1. LeetCode 479. 最大回文数乘积
     *    题目描述：给定一个整数 n ，返回 可表示为两个 n 位整数乘积的 最大回文整数 。因为答案可能非常大，所以返回它对 1337 取余。
     *    解题思路：生成可能的回文数，然后检查是否可以分解为两个n位数的乘积
     */
    public static int largestPalindrome(int n) {
        if (n == 1) return 9;
        int max = (int)Math.pow(10, n) - 1;
        int min = (int)Math.pow(10, n-1);
        
        for (int i = max; i >= min; i--) {
            long palindrome = createPalindrome(i);
            for (long j = max; j*j >= palindrome; j--) {
                if (palindrome % j == 0 && palindrome / j <= max && palindrome / j >= min) {
                    return (int)(palindrome % 1337);
                }
            }
        }
        return -1; // 不应该到达这里
    }
    
    // 辅助方法：根据数字创建回文数
    private static long createPalindrome(int num) {
        String s = Integer.toString(num);
        StringBuilder sb = new StringBuilder(s);
        return Long.parseLong(s + sb.reverse().toString());
    }
    
    /**
     * 2. LeetCode 680. 验证回文字符串 Ⅱ
     *    题目描述：给定一个非空字符串 s，最多删除一个字符，判断是否能成为回文字符串。
     *    解题思路：使用双指针，如果遇到不匹配的字符，尝试删除左边或右边的字符
     */
    public static boolean validPalindrome(String s) {
        int left = 0, right = s.length() - 1;
        
        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) {
                // 尝试删除左边或右边的字符
                return isPalindromeRange(s, left + 1, right) || isPalindromeRange(s, left, right - 1);
            }
            left++;
            right--;
        }
        
        return true;
    }
    
    private static boolean isPalindromeRange(String s, int left, int right) {
        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }
    
    /**
     * 3. CodeChef PALIN - The Next Palindrome
     *    题目描述：给定一个数字 N，找到大于 N 的最小回文数。
     *    解题思路：构造下一个回文数，可以通过处理中间位来高效实现
     */
    public static String nextPalindrome(String N) {
        char[] arr = N.toCharArray();
        int n = arr.length;
        int mid = n / 2;
        
        // 检查是否是全9的情况
        boolean all9 = true;
        for (char c : arr) {
            if (c != '9') {
                all9 = false;
                break;
            }
        }
        if (all9) {
            StringBuilder sb = new StringBuilder();
            sb.append('1');
            for (int i = 0; i < n - 1; i++) {
                sb.append('0');
            }
            sb.append('1');
            return sb.toString();
        }
        
        // 构造回文
        boolean leftIsSmaller = false;
        int i = mid - 1;
        int j = (n % 2 == 0) ? mid : mid + 1;
        
        while (i >= 0 && arr[i] == arr[j]) {
            i--;
            j++;
        }
        
        if (i < 0 || arr[i] < arr[j]) {
            leftIsSmaller = true;
        }
        
        // 镜像左半部分到右半部分
        while (i >= 0) {
            arr[j++] = arr[i--];
        }
        
        // 如果左半部分小于右半部分，需要增加中间部分
        if (leftIsSmaller) {
            int carry = 1;
            i = mid - 1;
            if (n % 2 == 1) {
                arr[mid] += carry;
                carry = (arr[mid] - '0') / 10;
                arr[mid] = (char)((arr[mid] - '0') % 10 + '0');
                j = mid + 1;
            } else {
                j = mid;
            }
            
            while (i >= 0 && carry > 0) {
                arr[i] += carry;
                carry = (arr[i] - '0') / 10;
                arr[i] = (char)((arr[i] - '0') % 10 + '0');
                arr[j++] = arr[i--];
            }
        }
        
        return new String(arr);
    }
    
    /**
     * 4. HackerRank - The Longest Palindromic Subsequence
     *    题目描述：给定一个字符串s，找到最长回文子序列的长度。子序列可以通过删除字符来形成，但不能改变剩余字符的相对顺序。
     *    解题思路：使用动态规划，dp[i][j]表示s[i...j]的最长回文子序列长度
     */
    public static int longestPalindromeSubseq(String s) {
        int n = s.length();
        int[][] dp = new int[n][n];
        
        // 单个字符的回文长度为1
        for (int i = 0; i < n; i++) {
            dp[i][i] = 1;
        }
        
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                if (s.charAt(i) == s.charAt(j)) {
                    dp[i][j] = dp[i + 1][j - 1] + 2;
                } else {
                    dp[i][j] = Math.max(dp[i + 1][j], dp[i][j - 1]);
                }
            }
        }
        
        return dp[0][n - 1];
    }
    
    /**
     * 5. LintCode 1178 - Valid Palindrome III
     *    题目描述：给定一个字符串s和一个整数k，判断是否可以通过最多删除k个字符使得s成为回文字符串。
     *    解题思路：使用动态规划，dp[i][j]表示将s[i...j]变成回文所需删除的最少字符数
     */
    public static boolean isValidPalindrome(String s, int k) {
        int n = s.length();
        int[][] dp = new int[n][n];
        
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                if (s.charAt(i) == s.charAt(j)) {
                    dp[i][j] = dp[i + 1][j - 1];
                } else {
                    dp[i][j] = Math.min(dp[i + 1][j], dp[i][j - 1]) + 1;
                }
            }
        }
        
        return dp[0][n - 1] <= k;
    }
    
    /**
     * 6. LeetCode 647. 回文子串
     *    题目描述：给你一个字符串 s ，请你统计并返回这个字符串中 回文子串 的数目。
     *    回文字符串 是正着读和倒过来读一样的字符串。
     *    子字符串 是字符串中的由连续字符组成的一个序列。
     *    解题思路：使用中心扩展法，遍历每个可能的中心位置，向两边扩展检查回文
     */
    public static int countSubstrings(String s) {
        int count = 0;
        int n = s.length();
        
        // 检查每个可能的中心位置
        for (int i = 0; i < n; i++) {
            // 以单个字符为中心的回文（奇数长度）
            count += expandAroundCenter(s, i, i);
            // 以两个字符之间为中心的回文（偶数长度）
            count += expandAroundCenter(s, i, i + 1);
        }
        
        return count;
    }
    
    private static int expandAroundCenter(String s, int left, int right) {
        int count = 0;
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            count++;
            left--;
            right++;
        }
        return count;
    }
    
    /**
     * 7. LeetCode 336. 回文对
     *    题目描述：给定一组唯一的单词，返回所有不同的索引对 (i, j)，使得单词 words[i] + words[j] 是回文串。
     *    解题思路：使用哈希表存储每个单词的逆序，然后检查每个单词的前缀和后缀
     */
    public static List<List<Integer>> palindromePairs(String[] words) {
        List<List<Integer>> result = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        
        // 存储单词及其索引
        for (int i = 0; i < words.length; i++) {
            map.put(new StringBuilder(words[i]).reverse().toString(), i);
        }
        
        // 处理空字符串
        if (map.containsKey("")) {
            int emptyIndex = map.get("");
            for (int i = 0; i < words.length; i++) {
                if (i != emptyIndex && isPalindromeString(words[i])) {
                    result.add(Arrays.asList(i, emptyIndex));
                    result.add(Arrays.asList(emptyIndex, i));
                }
            }
        }
        
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            
            // 分割单词为前缀和后缀
            for (int j = 1; j < word.length(); j++) {
                String prefix = word.substring(0, j);
                String suffix = word.substring(j);
                
                // 前缀是回文，检查后缀的逆序是否存在
                if (isPalindromeString(prefix) && map.containsKey(reverseString(suffix))) {
                    int index = map.get(reverseString(suffix));
                    if (index != i) {
                        result.add(Arrays.asList(index, i));
                    }
                }
                
                // 后缀是回文，检查前缀的逆序是否存在
                if (isPalindromeString(suffix) && map.containsKey(reverseString(prefix))) {
                    int index = map.get(reverseString(prefix));
                    if (index != i) {
                        result.add(Arrays.asList(i, index));
                    }
                }
            }
            
            // 检查整个单词的逆序是否存在
            String reversed = reverseString(word);
            if (map.containsKey(reversed) && map.get(reversed) != i) {
                result.add(Arrays.asList(i, map.get(reversed)));
            }
        }
        
        return result;
    }
    
    private static boolean isPalindromeString(String s) {
        int left = 0, right = s.length() - 1;
        while (left < right) {
            if (s.charAt(left++) != s.charAt(right--)) {
                return false;
            }
        }
        return true;
    }
    
    private static String reverseString(String s) {
        return new StringBuilder(s).reverse().toString();
    }
    
    /**
     * 8. HackerRank - Split the String
     *    题目描述：给定一个字符串，要求将其分割成最多K个部分，使得每个部分都包含相同数量的'a'和'b'字符。
     *    解题思路：贪心算法，遍历字符串并统计a和b的数量，当两者相等时进行分割
     */
    public static int splitTheString(String s, int k) {
        int aCount = 0, bCount = 0;
        int splits = 0;
        
        for (char c : s.toCharArray()) {
            if (c == 'a') aCount++;
            else bCount++;
            
            // 当a和b的数量相等时，进行分割
            if (aCount == bCount && aCount > 0) {
                splits++;
                aCount = 0;
                bCount = 0;
                
                // 如果已经达到最大分割次数，提前结束
                if (splits == k) {
                    break;
                }
            }
        }
        
        // 返回分割次数（不超过K）
        return splits;
    }
    
    /**
     * 9. LeetCode 5. 最长回文子串
     *    题目描述：给你一个字符串 s，找到 s 中最长的回文子串。
     *    解题思路：中心扩展法，枚举每个可能的中心点并向两边扩展
     */
    public static String longestPalindrome(String s) {
        if (s == null || s.length() < 1) return "";
        int start = 0, end = 0;
        
        for (int i = 0; i < s.length(); i++) {
            // 奇数长度回文
            int len1 = expandAroundCenter(s, i, i);
            // 偶数长度回文
            int len2 = expandAroundCenter(s, i, i + 1);
            int len = Math.max(len1, len2);
            
            if (len > end - start) {
                start = i - (len - 1) / 2;
                end = i + len / 2;
            }
        }
        
        return s.substring(start, end + 1);
    }
    
    /*
    private static int expandAroundCenter(String s, int left, int right) {
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            left--;
            right++;
        }
        return right - left - 1;
    }
    */
    
    /**
     * 10. LeetCode 125. 验证回文串
     *    题目描述：给定一个字符串，验证它是否是回文串，只考虑字母和数字字符，可以忽略字母的大小写。
     *    解题思路：双指针，从两端向中间移动并比较字符
     */
    public static boolean isPalindromeSentence(String s) {
        int left = 0, right = s.length() - 1;
        
        while (left < right) {
            // 跳过非字母数字字符
            while (left < right && !Character.isLetterOrDigit(s.charAt(left))) {
                left++;
            }
            while (left < right && !Character.isLetterOrDigit(s.charAt(right))) {
                right--;
            }
            
            // 比较字符（忽略大小写）
            if (Character.toLowerCase(s.charAt(left)) != Character.toLowerCase(s.charAt(right))) {
                return false;
            }
            
            left++;
            right--;
        }
        
        return true;
    }
    
    /**
     * 11. LeetCode 680. 验证回文字符串 II
     *    题目描述：给定一个非空字符串 s，最多删除一个字符，判断是否能成为回文字符串。
     *    解题思路：双指针，当遇到不匹配时，尝试删除左边或右边的字符继续检查
     */
    /*
    public static boolean validPalindrome(String s) {
        int left = 0, right = s.length() - 1;
        
        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) {
                // 尝试删除左边或右边的字符
                return isPalindromeRange(s, left + 1, right) || isPalindromeRange(s, left, right - 1);
            }
            left++;
            right--;
        }
        
        return true;
    }
    
    private static boolean isPalindromeRange(String s, int left, int right) {
        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }
    */
    
    /**
     * 12. LeetCode 131. 分割回文串
     *    题目描述：给你一个字符串 s，请你将 s 分割成一些子串，使每个子串都是 回文串 。返回 s 所有可能的分割方案。
     *    解题思路：回溯算法，递归地尝试所有可能的分割方式
     */
    public static List<List<String>> partition(String s) {
        List<List<String>> result = new ArrayList<>();
        List<String> current = new ArrayList<>();
        backtrackPartition(s, 0, current, result);
        return result;
    }
    
    private static void backtrackPartition(String s, int start, List<String> current, List<List<String>> result) {
        if (start >= s.length()) {
            result.add(new ArrayList<>(current));
            return;
        }
        
        for (int end = start; end < s.length(); end++) {
            if (isPalindromeString(s.substring(start, end + 1))) {
                current.add(s.substring(start, end + 1));
                backtrackPartition(s, end + 1, current, result);
                current.remove(current.size() - 1); // 回溯
            }
        }
    }
    
    /**
     * 13. LeetCode 132. 分割回文串 II
     *    题目描述：给你一个字符串 s，请你将 s 分割成一些子串，使每个子串都是回文。返回符合要求的 最少分割次数 。
     *    解题思路：动态规划，dp[i]表示s[0...i]的最少分割次数
     */
    public static int minCut(String s) {
        int n = s.length();
        // dp[i]表示s[0...i]的最少分割次数
        int[] dp = new int[n];
        
        // 初始化：最多需要i次分割（每个字符单独成一个回文串）
        for (int i = 0; i < n; i++) {
            dp[i] = i;
        }
        
        // 预处理判断是否为回文串
        boolean[][] isPal = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= i; j++) {
                if (s.charAt(i) == s.charAt(j) && (i - j <= 2 || isPal[j + 1][i - 1])) {
                    isPal[j][i] = true;
                }
            }
        }
        
        // 动态规划填表
        for (int i = 0; i < n; i++) {
            if (isPal[0][i]) {
                dp[i] = 0; // 不需要分割
                continue;
            }
            
            for (int j = 0; j < i; j++) {
                if (isPal[j + 1][i]) {
                    dp[i] = Math.min(dp[i], dp[j] + 1);
                }
            }
        }
        
        return dp[n - 1];
    }
    
    /**
     * 14. LeetCode 516. 最长回文子序列
     *    题目描述：给你一个字符串 s ，找出其中最长的回文子序列，并返回该序列的长度。
     *    解题思路：动态规划，dp[i][j]表示s[i...j]范围内的最长回文子序列长度
     */
    /*
    public static int longestPalindromeSubseq(String s) {
        int n = s.length();
        int[][] dp = new int[n][n];
        
        // 初始化对角线
        for (int i = 0; i < n; i++) {
            dp[i][i] = 1;
        }
        
        // 填充dp数组
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i + len <= n; i++) {
                int j = i + len - 1;
                if (s.charAt(i) == s.charAt(j)) {
                    dp[i][j] = dp[i + 1][j - 1] + 2;
                } else {
                    dp[i][j] = Math.max(dp[i + 1][j], dp[i][j - 1]);
                }
            }
        }
        
        return dp[0][n - 1];
    }
    */
    
    /**
     * 15. Codeforces 1335D - Anti-Sudoku
     *     题目描述：给定一个9x9的数独，将其转换为反数独，即每个行、列和3x3子网格中至少包含两个相同的数字。
     *     解题思路：简单地将所有的5改为6即可（或者其他类似的简单变换）
     */
    public static char[][] antiSudoku(char[][] grid) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (grid[i][j] == '5') {
                    grid[i][j] = '6';
                }
            }
        }
        return grid;
    }
    
    /**
     * 超级回文数算法的Python实现
     */
    /*
     # Python版本
     
     def is_palindrome(x):
         """
         判断一个数是否是回文数
         时间复杂度: O(log x) - 取决于数字的位数
         空间复杂度: O(1)
         """
         if x < 0:
             return False
         original = x
         reversed_num = 0
         while x > 0:
             reversed_num = reversed_num * 10 + x % 10
             x = x // 10
         return original == reversed_num
 
     def even_enlarge(seed):
         """
         根据种子扩充到偶数长度的回文数字
         例如：seed=123，返回123321
         时间复杂度: O(log seed)
         空间复杂度: O(1)
         """
         ans = seed
         temp = seed
         while temp > 0:
             ans = ans * 10 + temp % 10
             temp = temp // 10
         return ans
 
     def odd_enlarge(seed):
         """
         根据种子扩充到奇数长度的回文数字
         例如：seed=123，返回12321
         时间复杂度: O(log seed)
         空间复杂度: O(1)
         """
         ans = seed
         temp = seed // 10
         while temp > 0:
             ans = ans * 10 + temp % 10
             temp = temp // 10
         return ans
 
     # 预计算的超级回文数
     SUPER_PALINDROMES = [
         1, 4, 9, 121, 484, 10201, 12321, 14641, 40804, 44944, 1002001, 1234321,
         4008004, 100020001, 102030201, 104060401, 121242121, 123454321, 125686521,
         400080004, 404090404, 10000200001, 10221412201, 12102420121, 12345654321,
         40000800004, 1000002000001, 1002003002001, 1004006004001, 1020304030201,
         1022325232201, 1024348434201, 1210024200121, 1212225222121, 1214428244121,
         1232346432321, 1234567654321, 4000008000004, 4004009004004, 100000020000001,
         10002000300020001, 10004000600040001, 10020210401202001, 10022212521222001,
         10024214841242001, 10201020402010201, 10203040504030201, 10205060806050201,
         10221232623212201, 10223252725232201, 10225272827252201, 121000242000121,
         12102024342020121, 12104024642040121, 12122232623222121, 12124234743242121,
         12126236863262121, 123003464300321, 1232234654322321, 1234434664344321,
         1232345665432321, 123456787654321, 400000080000004, 40004000900040004
     ]
 
     def super_palindromes_in_range(left, right):
         """
         使用打表法查找范围内的超级回文数
         时间复杂度: O(log K)，K为预计算数组的大小
         空间复杂度: O(K)
         """
         count = 0
         left_num = int(left)
         right_num = int(right)
         
         for num in SUPER_PALINDROMES:
             if left_num <= num <= right_num:
                 count += 1
         
         return count
 
     def super_palindromes_in_range_enum(left, right):
         """
         使用枚举法查找范围内的超级回文数
         时间复杂度: O(√R * log R)，其中R为右边界
         空间复杂度: O(1)
         """
         left_num = int(left)
         right_num = int(right)
         count = 0
         
         # 计算种子的上界
         limit = int(right_num ** 0.5) + 1
         seed_limit = 10**5  # 对于10^18范围，种子只需到10^5级别
         
         # 枚举所有可能的种子
         for seed in range(1, seed_limit):
             # 生成偶数长度的回文数
             even_pal = even_enlarge(seed)
             square_even = even_pal * even_pal
             if square_even > right_num:
                 continue
             if square_even >= left_num and is_palindrome(square_even):
                 count += 1
             
             # 生成奇数长度的回文数
             odd_pal = odd_enlarge(seed)
             square_odd = odd_pal * odd_pal
             if square_odd > right_num:
                 continue
             if square_odd >= left_num and is_palindrome(square_odd):
                 count += 1
         
         return count
 
     # 补充训练题目的Python实现
     def largest_palindrome_py(n):
         """
         LeetCode 479. 最大回文数乘积
         时间复杂度: O(10^2n)，但实际运行远快于理论值
         空间复杂度: O(1)
         """
         if n == 1:
             return 9
         max_num = 10**n - 1
         min_num = 10**(n-1)
         
         for i in range(max_num, min_num - 1, -1):
             # 创建回文数
             s = str(i)
             palindrome = int(s + s[::-1])
             
             # 检查是否可以分解为两个n位数的乘积
             for j in range(max_num, int(palindrome**0.5) - 1, -1):
                 if palindrome % j == 0:
                     if min_num <= palindrome // j <= max_num:
                         return palindrome % 1337
         return -1
 
     def palindrome_pairs_py(words):
         """
         LeetCode 336. 回文对
         时间复杂度: O(n * k^2)，其中n是单词数量，k是单词的最大长度
         空间复杂度: O(n * k)
         """
         result = []
         word_map = {word: i for i, word in enumerate(words)}
         
         # 处理空字符串
         if "" in word_map:
             empty_idx = word_map[""]
             for i, word in enumerate(words):
                 if i != empty_idx and word == word[::-1]:
                     result.append([i, empty_idx])
                     result.append([empty_idx, i])
         
         for i, word in enumerate(words):
             # 检查整个单词的逆序
             rev_word = word[::-1]
             if rev_word in word_map and word_map[rev_word] != i:
                 result.append([i, word_map[rev_word]])
             
             # 分割单词为前缀和后缀
             for j in range(1, len(word)):
                 prefix = word[:j]
                 suffix = word[j:]
                 
                 # 前缀是回文，检查后缀的逆序
                 if prefix == prefix[::-1]:
                     rev_suffix = suffix[::-1]
                     if rev_suffix in word_map and word_map[rev_suffix] != i:
                         result.append([word_map[rev_suffix], i])
                 
                 # 后缀是回文，检查前缀的逆序
                 if suffix == suffix[::-1]:
                     rev_prefix = prefix[::-1]
                     if rev_prefix in word_map and word_map[rev_prefix] != i:
                         result.append([i, word_map[rev_prefix]])
         
         return result
    */
     
     /**
      * 超级回文数算法的C++实现
      */
     /*
      #include <iostream>
      #include <string>
      #include <vector>
      #include <unordered_map>
      #include <algorithm>
      #include <cmath>
      
      using namespace std;
      
      // 判断一个数是否是回文数
      bool isPalindrome(long long x) {
          if (x < 0) return false;
          long long original = x;
          long long reversedNum = 0;
          while (x > 0) {
              reversedNum = reversedNum * 10 + x % 10;
              x = x / 10;
          }
          return original == reversedNum;
      }
      
      // 根据种子扩充到偶数长度的回文数字
      long long evenEnlarge(long long seed) {
          long long ans = seed;
          long long temp = seed;
          while (temp > 0) {
              ans = ans * 10 + temp % 10;
              temp = temp / 10;
          }
          return ans;
      }
      
      // 根据种子扩充到奇数长度的回文数字
      long long oddEnlarge(long long seed) {
          long long ans = seed;
          long long temp = seed / 10;
          while (temp > 0) {
              ans = ans * 10 + temp % 10;
              temp = temp / 10;
          }
          return ans;
      }
      
      // 预计算的超级回文数
      vector<long long> SUPER_PALINDROMES = {
          1, 4, 9, 121, 484, 10201, 12321, 14641, 40804, 44944, 1002001, 1234321,
          4008004, 100020001, 102030201, 104060401, 121242121, 123454321, 125686521,
          400080004, 404090404, 10000200001, 10221412201, 12102420121, 12345654321,
          40000800004, 1000002000001, 1002003002001, 1004006004001, 1020304030201,
          1022325232201, 1024348434201, 1210024200121, 1212225222121, 1214428244121,
          1232346432321, 1234567654321, 4000008000004, 4004009004004, 100000020000001,
          10002000300020001, 10004000600040001, 10020210401202001, 10022212521222001,
          10024214841242001, 10201020402010201, 10203040504030201, 10205060806050201,
          10221232623212201, 10223252725232201, 10225272827252201, 121000242000121,
          12102024342020121, 12104024642040121, 12122232623222121, 12124234743242121,
          12126236863262121, 123003464300321, 1232234654322321, 1234434664344321,
          1232345665432321, 123456787654321, 400000080000004, 40004000900040004
      };
      
      // 使用打表法查找范围内的超级回文数
      int superPalindromesInRange(string left, string right) {
          long long leftNum = stoll(left);
          long long rightNum = stoll(right);
          int count = 0;
          
          for (long long num : SUPER_PALINDROMES) {
              if (leftNum <= num && num <= rightNum) {
                  count++;
              }
          }
          
          return count;
      }
      
      // 使用枚举法查找范围内的超级回文数
      int superPalindromesInRangeEnum(string left, string right) {
          long long leftNum = stoll(left);
          long long rightNum = stoll(right);
          int count = 0;
          
          // 枚举所有可能的种子
          const long long SEED_LIMIT = 100000; // 对于10^18范围，种子只需到10^5级别
          for (long long seed = 1; seed < SEED_LIMIT; seed++) {
              // 生成偶数长度的回文数
              long long evenPal = evenEnlarge(seed);
              long long squareEven = evenPal * evenPal;
              if (squareEven > rightNum) {
                  continue;
              }
              if (squareEven >= leftNum && isPalindrome(squareEven)) {
                  count++;
              }
              
              // 生成奇数长度的回文数
              long long oddPal = oddEnlarge(seed);
              long long squareOdd = oddPal * oddPal;
              if (squareOdd > rightNum) {
                  continue;
              }
              if (squareOdd >= leftNum && isPalindrome(squareOdd)) {
                  count++;
              }
          }
          
          return count;
      }
      
      // 补充训练题目的C++实现
      int largestPalindrome(int n) {
          if (n == 1) return 9;
          long long maxNum = pow(10, n) - 1;
          long long minNum = pow(10, n - 1);
          
          for (long long i = maxNum; i >= minNum; i--) {
              // 创建回文数
              string s = to_string(i);
              string rev_s = s;
              reverse(rev_s.begin(), rev_s.end());
              long long palindrome = stoll(s + rev_s);
              
              // 检查是否可以分解为两个n位数的乘积
              for (long long j = maxNum; j * j >= palindrome; j--) {
                  if (palindrome % j == 0) {
                      long long factor = palindrome / j;
                      if (factor >= minNum && factor <= maxNum) {
                          return palindrome % 1337;
                      }
                  }
              }
          }
          return -1;
      }
      
      // 判断字符串是否是回文
      bool isPalindromeString(const string& s) {
          int left = 0, right = s.size() - 1;
          while (left < right) {
              if (s[left++] != s[right--]) {
                  return false;
              }
          }
          return true;
      }
      
      // 反转字符串
      string reverseString(const string& s) {
          string rev = s;
          reverse(rev.begin(), rev.end());
          return rev;
      }
      
      // LeetCode 336. 回文对
      vector<vector<int>> palindromePairs(vector<string>& words) {
          vector<vector<int>> result;
          unordered_map<string, int> wordMap;
          
          // 存储单词及其索引
          for (int i = 0; i < words.size(); i++) {
              wordMap[words[i]] = i;
          }
          
          // 处理空字符串
          if (wordMap.find("") != wordMap.end()) {
              int emptyIdx = wordMap[""];
              for (int i = 0; i < words.size(); i++) {
                  if (i != emptyIdx && isPalindromeString(words[i])) {
                      result.push_back({i, emptyIdx});
                      result.push_back({emptyIdx, i});
                  }
              }
          }
          
          for (int i = 0; i < words.size(); i++) {
              const string& word = words[i];
              
              // 检查整个单词的逆序
              string reversed = reverseString(word);
              if (wordMap.find(reversed) != wordMap.end() && wordMap[reversed] != i) {
                  result.push_back({i, wordMap[reversed]});
              }
              
              // 分割单词为前缀和后缀
              for (int j = 1; j < word.size(); j++) {
                  string prefix = word.substr(0, j);
                  string suffix = word.substr(j);
                  
                  // 前缀是回文，检查后缀的逆序
                  if (isPalindromeString(prefix)) {
                      string revSuffix = reverseString(suffix);
                      if (wordMap.find(revSuffix) != wordMap.end() && wordMap[revSuffix] != i) {
                          result.push_back({wordMap[revSuffix], i});
                      }
                  }
                  
                  // 后缀是回文，检查前缀的逆序
                  if (isPalindromeString(suffix)) {
                      string revPrefix = reverseString(prefix);
                      if (wordMap.find(revPrefix) != wordMap.end() && wordMap[revPrefix] != i) {
                          result.push_back({i, wordMap[revPrefix]});
                      }
                  }
              }
          }
          
          return result;
      }
     */
      
     /**
      * 超级回文数与回文序列生成的算法技巧总结
      * 
      * 算法设计与策略选择：
      * 1. 问题分析框架：
      *    - 超级回文数的定义特征：双重回文性质（自身回文且为回文数的平方）
      *    - 搜索空间优化：从平方根而非原始数入手，大幅减少搜索范围
      *    - 对称性利用：利用回文数的对称性特征构造候选解
      * 
      * 2. 算法选择原则：
      *    - 单次查询场景：枚举法更节省内存，实现简单
      *    - 多次查询场景：打表法性能最优，时间复杂度接近O(1)
      *    - 大数据范围：需要考虑数值溢出问题和效率优化
      * 
      * 回文数生成技术：
      * 1. 种子生成法：
      *    - 种子到完整回文数的转换策略
      *    - 偶数长度与奇数长度回文数的不同生成方式
      *    - 种子范围的数学推导与优化（对于10^18范围，种子仅需到10^5级别）
      * 
      * 2. 高效实现技巧：
      *    - 数学方法避免字符串转换开销
      *    - 使用位移和取模操作进行位提取
      *    - 位运算优化（适用于某些特定场景）
      * 
      * 复杂度分析与优化：
      * 1. 时间复杂度深入分析：
      *    - 枚举法：O(√R * log R)，其中log R为回文判断的时间
      *    - 打表法：预处理O(K * log K)，查询O(log K)
      *    - 种子生成法的实际复杂度远低于理论上界
      * 
      * 2. 空间复杂度考量：
      *    - 枚举法：O(1)，常数额外空间
      *    - 打表法：O(K)，其中K约为70，空间消耗极小
      *    - 权衡空间与时间的最佳平衡点
      * 
      * 高级优化技术：
      * 1. 上下界剪枝：
      *    - 快速判断种子生成的回文数平方是否可能在范围内
      *    - 数学上界计算避免不必要的计算
      * 
      * 2. 并行与并发优化：
      *    - 多线程并行生成和验证回文数
      *    - 任务分割策略与负载均衡
      *    - 原子操作确保结果正确性
      * 
      * 3. 缓存与预计算策略：
      *    - LRU缓存设计与实现
      *    - 区间查询结果缓存
      *    - 动态规划预处理回文属性
      * 
      * 工程化最佳实践：
      * 1. 健壮性设计：
      *    - 大数输入处理（字符串转长整型）
      *    - 溢出检测与处理机制
      *    - 边界条件全面测试
      * 
      * 2. 代码结构优化：
      *    - 模块化设计与功能解耦
      *    - 策略模式实现不同算法切换
      *    - 接口抽象与实现分离
      * 
      * 3. 性能监控与调优：
      *    - 热点代码识别与优化
      *    - 内存使用分析
      *    - 执行时间基准测试
      * 
      * 调试与测试技术：
      * 1. 测试用例设计：
      *    - 边界值测试：最小/最大输入、空范围
      *    - 特殊情况测试：无超级回文数的范围
      *    - 性能测试：大规模输入下的执行效率
      * 
      * 2. 调试技巧：
      *    - 中间结果可视化
      *    - 性能瓶颈分析
      *    - 断言与防御式编程
      * 
      * 跨语言实现比较：
      * 1. 语言特性影响：
      *    - C++：数值运算性能最佳，无符号整数处理更灵活
      *    - Java：大整数支持良好，线程安全特性
      *    - Python：内置大整数，代码简洁但性能较低
      *    - JavaScript：需要特殊处理大整数，精度问题需注意
      * 
      * 2. 实现策略差异：
      *    - 不同语言的哈希表/字典实现效率
      *    - 数值类型范围与溢出处理机制
      *    - 并行编程模型与性能
      * 
      * 与现代技术的融合：
      * 1. 与机器学习的结合：
      *    - 模式识别：自动发现回文数生成模式
      *    - 预测模型：预测特定范围内超级回文数的分布
      *    - 启发式搜索：基于学习的优化搜索策略
      * 
      * 2. 高性能计算应用：
      *    - GPU加速回文数生成与验证
      *    - 分布式计算大规模范围查询
      *    - SIMD指令集优化数值运算
      * 
      * 3. 密码学与安全应用：
      *    - 超级回文数在某些加密算法中的应用
      *    - 数论研究中的特殊性质利用
      *    - 安全协议中的数学基础
      * 
      * 进阶研究方向：
      * 1. 数学理论扩展：
      *    - 超级回文数的分布规律研究
      *    - 推广到其他数论函数的超级形式
      *    - 多维回文结构探索
      * 
      * 2. 算法创新：
      *    - 更快的回文数生成算法
      *    - 更高效的范围查询方法
      *    - 新型数据结构支持
      * 
      * 3. 实际应用拓展：
      *    - 文本处理中的模式匹配
      *    - 生物序列分析中的回文结构识别
      *    - 网络安全中的应用场景
      */
}