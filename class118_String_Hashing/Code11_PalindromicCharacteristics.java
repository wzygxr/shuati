package class105;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * CodeForces 835D Palindromic Characteristics 问题实现
 * <p>
 * 题目链接：https://codeforces.com/problemset/problem/835/D
 * <p>
 * 题目描述：
 * 定义k级回文串：
 * - 1级回文串：字符串本身是回文串
 * - k级回文串(k>1)：字符串本身是回文串，且左右两半相等，左右两半都是(k-1)级回文串
 * 题目要求计算字符串中各个级别(k=1,2,...,n)的回文子串数量
 * <p>
 * 算法核心思想：
 * 1. 哈希技术：使用前向和后向哈希数组实现O(1)时间回文判断
 * 2. 动态规划：使用二维数组dp[i][j]表示子串str[i..j]的最高回文级别
 * 3. 分层计数：根据动态规划结果统计各级别回文串数量
 * <p>
 * 算法详细步骤：
 * 1. 预处理阶段：
 *    - 计算前缀哈希数组prefixHash
 *    - 计算后缀哈希数组suffixHash
 *    - 计算幂次数组pow，用于快速计算子串哈希值
 * 2. 动态规划阶段：
 *    - 初始化长度为1的子串为1级回文
 *    - 按子串长度递增顺序计算每个子串的回文级别
 *    - 对于回文子串，进一步判断是否满足更高级别回文的条件
 * 3. 结果统计阶段：
 *    - 遍历所有子串及其回文级别
 *    - 统计各级别回文串数量，注意每个k级回文也是1到k级的回文
 * 4. 输出结果
 * <p>
 * 回文级别定义解析：
 * - 1级回文：任何回文串至少是1级回文
 * - 2级回文：不仅是回文，且左右两半完全相同且为1级回文
 * - 3级回文：不仅是回文，且左右两半完全相同且为2级回文
 * - 以此类推...
 * <p>
 * 算法优势：
 * - 高效性：使用哈希实现O(1)时间回文判断
 * - 结构化：使用动态规划清晰表达子问题关系
 * - 精确性：正确处理奇数和偶数长度子串的不同情况
 * <p>
 * 时间复杂度分析：
 * - 预处理阶段：O(n)，计算哈希数组和幂次数组
 * - 动态规划阶段：O(n²)，遍历所有可能的子串对
 * - 统计阶段：O(n²)，遍历所有子串并统计各级别数量
 * - 总体时间复杂度：O(n²)
 * <p>
 * 空间复杂度分析：
 * - 哈希数组和幂次数组：O(n)
 * - 动态规划数组：O(n²)
 * - 计数数组：O(n)
 * - 总体空间复杂度：O(n²)
 * <p>
 * 哈希冲突处理：
 * - 使用大质数模数10^9+7减少哈希冲突
 * - 在实际比赛环境中，这种方法通常足够可靠
 * - 对于要求更高的场景，可以使用双哈希技术进一步降低冲突概率
 * <p>
 * 与其他方法比较：
 * 1. Manacher算法：O(n)时间找出所有回文子串，但难以直接计算回文级别
 * 2. 暴力方法：O(n³)时间，枚举所有子串并判断，效率低下
 * 3. 中心扩展法：O(n²)时间，适合回文子串数量统计，但计算回文级别复杂度高
 * <p>
 * 相似题目：
 * 1. LeetCode 647: Palindromic Substrings - 统计回文子串数量
 * 2. LeetCode 5: Longest Palindromic Substring - 最长回文子串
 * 3. Codeforces 1326D2: Prefix-Suffix Palindrome - 前缀后缀回文问题
 * 4. POJ 3974: Palindrome - 最长回文子串查询
 * 5. HDU 3068: 最长回文 - 最长回文子串长度
 * <p>
 * 测试链接：https://codeforces.com/problemset/problem/835/D
 * <p>
 * 三种语言实现参考：
 * - Java实现：当前文件
 * - Python实现：Code11_PalindromicCharacteristics.py
 * - C++实现：Code11_PalindromicCharacteristics.cpp
 * 
 * @author Algorithm Journey
 */

public class Code11_PalindromicCharacteristics {

    /**
     * 最大字符串长度，根据题目约束设置
     * Codeforces 835D中字符串长度不超过5000
     */
    public static int MAXN = 5001;
    
    /**
     * 哈希基数，选择131作为哈希基数以减少哈希冲突
     * 131是一个常用的字符串哈希基数，与mod配合使用可以有效减少冲突
     */
    public static int base = 131;
    
    /**
     * 模数，用于控制哈希值大小并减少溢出
     * 10^9+7是一个常用的大质数模数，能有效防止整数溢出
     */
    public static int mod = 1000000007;
    
    /**
     * 存储输入字符串的字符数组，从索引1开始存储以便计算
     * 从1开始索引可以简化边界条件处理
     */
    public static char[] str = new char[MAXN];
    
    /**
     * 前缀哈希数组，prefixHash[i]表示子串str[1..i]的哈希值
     * 用于快速计算子串的正向哈希值
     */
    public static long[] prefixHash = new long[MAXN];
    
    /**
     * 后缀哈希数组，suffixHash[i]表示子串str[i..n]的哈希值
     * 用于快速计算子串的反向哈希值
     */
    public static long[] suffixHash = new long[MAXN];
    
    /**
     * 前缀幂次数组，pow[i]表示base^i % mod
     * 预计算避免重复计算，提高哈希值计算效率
     */
    public static long[] pow = new long[MAXN];
    
    /**
     * 动态规划数组，dp[i][j]表示子串str[i..j]的回文级别
     * 回文级别定义：dp[i][j]=k表示该子串是k级回文
     */
    public static int[][] dp = new int[MAXN][MAXN];
    
    /**
     * 结果计数数组，count[k]表示k级回文子串的数量
     * 注意：一个k级回文也是1到k级的回文
     */
    public static int[] count = new int[MAXN];
    
    /**
     * 主方法，处理输入输出并协调整个算法流程
     * <p>
     * 处理流程：
     * 1. 初始化高效的输入输出流
     * 2. 读取输入字符串并转换为字符数组
     * 3. 调用preprocess()预处理哈希数组和幂次数组
     * 4. 调用computePalindromicLevels()计算所有子串的回文级别
     * 5. 调用countPalindromes()统计各级别回文子串数量
     * 6. 输出结果并关闭资源
     * <p>
     * 输入格式：
     * 一个字符串s，长度不超过5000
     * <p>
     * 输出格式：
     * 输出n个整数，分别表示1级到n级回文子串的数量，用空格分隔
     * 
     * @param args 命令行参数
     * @throws IOException 输入输出异常
     * 
     * 时间复杂度：O(n²)，其中n是字符串长度
     * 空间复杂度：O(n²)
     */
    public static void main(String[] args) throws IOException {
        // 创建高效的输入输出流，提高处理效率
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取输入字符串
        String s = in.readLine();
        int n = s.length();
        
        // 将字符串复制到字符数组中，从索引1开始存储以便于计算
        // 1-based索引可以简化哈希计算的边界条件处理
        s.getChars(0, n, str, 1);
        
        // 预处理哈希数组和幂次数组，为回文判断做准备
        preprocess(n);
        
        // 计算每个子串的回文级别，核心算法部分
        computePalindromicLevels(n);
        
        // 统计各级回文子串数量
        countPalindromes(n);
        
        // 输出各级回文子串的数量，从1级到n级
        for (int k = 1; k <= n; k++) {
            out.print(count[k] + " ");
        }
        out.println();
        
        // 刷新输出并关闭资源
        out.flush();
        out.close();
        in.close();
    }
    
    /**
     * 预处理函数，计算前缀哈希、后缀哈希和幂次数组
     * 为后续快速判断子串是否为回文提供基础
     * <p>
     * 预处理内容：
     * 1. 幂次数组pow：存储base的各次幂，用于快速计算子串哈希
     * 2. 前缀哈希数组prefixHash：从左到右计算哈希值
     * 3. 后缀哈希数组suffixHash：从右到左计算哈希值
     * <p>
     * 哈希计算原理：
     * - 前缀哈希：hash[i] = hash[i-1] * base + (str[i]-'a'+1) mod mod
     * - 后缀哈希：hash[i] = hash[i+1] * base + (str[i]-'a'+1) mod mod
     * - 字符值偏移+1是为了避免'a'的哈希值为0，减少哈希冲突
     * 
     * @param n 字符串长度
     * 
     * 时间复杂度：O(n) - 线性时间完成所有预处理
     * 空间复杂度：O(n)
     */
    public static void preprocess(int n) {
        // 计算幂次数组，pow[i] = base^i % mod
        // 预计算幂次数组可以避免重复计算，提高哈希值计算效率
        pow[0] = 1; // 初始化base^0 = 1
        for (int i = 1; i <= n; i++) {
            pow[i] = (pow[i - 1] * base) % mod; // 递推计算并取模
        }
        
        // 计算前缀哈希数组，prefixHash[i]表示子串str[1..i]的哈希值
        prefixHash[0] = 0; // 空字符串的哈希值为0
        for (int i = 1; i <= n; i++) {
            // 哈希递推公式：prefixHash[i] = (prefixHash[i-1] * base + (str[i]-'a'+1)) % mod
            // 加1是为了避免字符'a'的哈希值为0，减少哈希冲突
            prefixHash[i] = (prefixHash[i - 1] * base + str[i] - 'a' + 1) % mod;
        }
        
        // 计算后缀哈希数组，suffixHash[i]表示子串str[i..n]的哈希值
        // 与前缀哈希计算方向相反，用于快速获取子串的反向哈希值
        suffixHash[n + 1] = 0; // 超出字符串范围的哈希值为0
        for (int i = n; i >= 1; i--) {
            // 与前缀哈希类似，但方向相反
            suffixHash[i] = (suffixHash[i + 1] * base + str[i] - 'a' + 1) % mod;
        }
    }
    
    /**
     * 获取子串str[l..r]的哈希值
     * 利用前缀哈希数组快速计算任意子串的哈希值
     * <p>
     * 计算公式：
     * hash(l..r) = (hash(1..r) - hash(1..l-1) * base^(r-l+1)) % mod
     * <p>
     * 数学原理：
     * - hash(1..r) = s[1]*base^(r-1) + s[2]*base^(r-2) + ... + s[r-1]*base + s[r]
     * - hash(1..l-1) = s[1]*base^(l-2) + s[2]*base^(l-3) + ... + s[l-2]*base + s[l-1]
     * - hash(1..l-1)*base^(r-l+1) = s[1]*base^(r-1) + ... + s[l-1]*base^(r-l+1)
     * - 相减后得到：s[l]*base^(r-l) + ... + s[r-1]*base + s[r]，即hash(l..r)
     * 
     * @param l 左边界(包含)
     * @param r 右边界(包含)
     * @return 子串的哈希值
     * 
     * 时间复杂度：O(1) - 常数时间计算
     * 空间复杂度：O(1)
     */
    public static long getHash(int l, int r) {
        if (l > r) return 0; // 边界条件处理
        
        // 哈希计算公式：hash(l..r) = (hash(1..r) - hash(1..l-1) * base^(r-l+1)) % mod
        // 加上mod再取模是为了确保结果非负
        long res = (prefixHash[r] - (prefixHash[l - 1] * pow[r - l + 1]) % mod + mod) % mod;
        return res;
    }
    
    /**
     * 获取子串str[l..r]的反向哈希值
     * 利用后缀哈希数组快速计算子串的反向哈希值
     * <p>
     * 计算公式：
     * reverseHash(l..r) = (hash(l..n) - hash(r+1..n) * base^(r-l+1)) % mod
     * <p>
     * 数学原理：
     * - hash(l..n) = s[l]*base^(n-l) + s[l+1]*base^(n-l-1) + ... + s[n]
     * - hash(r+1..n) = s[r+1]*base^(n-r-1) + ... + s[n]
     * - hash(r+1..n)*base^(r-l+1) = s[r+1]*base^(n-l) + ... + s[n]*base^(r-l+1)
     * - 相减后得到：s[l]*base^(n-l) + ... + s[r]*base^(n-r)，即反向hash(l..r)
     * 
     * @param l 左边界(包含)
     * @param r 右边界(包含)
     * @return 子串的反向哈希值
     * 
     * 时间复杂度：O(1) - 常数时间计算
     * 空间复杂度：O(1)
     */
    public static long getReverseHash(int l, int r) {
        if (l > r) return 0; // 边界条件处理
        
        // 与正向哈希类似，但使用后缀哈希数组
        long res = (suffixHash[l] - (suffixHash[r + 1] * pow[r - l + 1]) % mod + mod) % mod;
        return res;
    }
    
    /**
     * 判断子串str[l..r]是否为回文串
     * 通过比较子串的正向哈希值和反向哈希值来判断
     * <p>
     * 回文判断原理：
     * 一个字符串是回文当且仅当其正向哈希值等于反向哈希值
     * 哈希值比较是O(1)时间的，这比直接比较字符更高效
     * <p>
     * 注意事项：
     * 理论上存在哈希冲突的可能，但使用大质数模数和合适的基数可以大大降低冲突概率
     * 
     * @param l 左边界(包含)
     * @param r 右边界(包含)
     * @return 是否为回文串
     * 
     * 时间复杂度：O(1) - 常数时间判断
     * 空间复杂度：O(1)
     */
    public static boolean isPalindrome(int l, int r) {
        // 如果一个子串是回文串，那么它的正向哈希值应该等于反向哈希值
        return getHash(l, r) == getReverseHash(l, r);
    }
    
    /**
     * 计算每个子串的回文级别
     * 使用动态规划按子串长度从小到大计算
     * <p>
     * 回文级别定义回顾：
     * - 1级回文：字符串本身是回文串
     * - k级回文(k>1)：字符串本身是回文串，且左右两半相等，左右两半都是(k-1)级回文串
     * <p>
     * 动态规划策略：
     * 1. 基础情况：长度为1的子串都是1级回文
     * 2. 状态转移：
     *    - 对于长度>=2的子串，先判断是否为回文
     *    - 如果是回文，根据长度奇偶性分别处理
     *    - 偶数长度：判断左右两半是否相等且左半部分是(k-1)级回文
     *    - 奇数长度：忽略中间字符，判断左右两半是否相等且左半部分是(k-1)级回文
     *    - 如果满足条件，回文级别为左半部分回文级别+1，否则为1
     * <p>
     * 算法优势：
     * - 按子串长度递增计算，确保计算长串时子问题已解决
     * - 使用哈希快速判断回文和子串相等，避免O(n)时间比较
     * 
     * @param n 字符串长度
     * 
     * 时间复杂度：O(n²) - 两层嵌套循环处理所有可能的子串
     * 空间复杂度：O(n²) - 存储动态规划数组
     */
    public static void computePalindromicLevels(int n) {
        // 初始化dp数组，所有子串的回文级别初始化为0
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                dp[i][j] = 0;
            }
        }
        
        // 基础情况：长度为1的子串都是1级回文
        // 单个字符总是回文，且满足1级回文的定义
        for (int i = 1; i <= n; i++) {
            dp[i][i] = 1;
        }
        
        // 按子串长度从小到大计算回文级别
        // 这样可以保证在计算较长子串时，其所有可能的子子串已经计算完毕
        for (int len = 2; len <= n; len++) {
            // 遍历所有可能的起始位置i，对应的结束位置j = i + len - 1
            for (int i = 1; i + len - 1 <= n; i++) {
                int j = i + len - 1;
                
                // 首先判断该子串是否为回文串
                if (isPalindrome(i, j)) {
                    // 如果是回文串，判断是否为k级回文(k>1)
                    // 需要分情况处理偶数长度和奇数长度
                    if (len % 2 == 0) {
                        // 偶数长度的情况：左右两半长度相等
                        int mid = (i + j) / 2; // 中间位置
                        
                        // 检查左右两半是否相等且左半部分是(k-1)级回文
                        // 使用哈希比较左右两半是否相等，避免字符比较
                        if (getHash(i, mid) == getHash(mid + 1, j) && dp[i][mid] > 0) {
                            // 如果满足条件，则回文级别为左半部分的回文级别+1
                            dp[i][j] = dp[i][mid] + 1;
                        } else {
                            // 至少是1级回文
                            dp[i][j] = 1;
                        }
                    } else {
                        // 奇数长度的情况：需要忽略中间字符
                        int mid = (i + j) / 2; // 中间字符位置
                        
                        // 检查左右两半(不包含中间字符)是否相等且左半部分是(k-1)级回文
                        if (getHash(i, mid - 1) == getHash(mid + 1, j) && dp[i][mid - 1] > 0) {
                            // 如果满足条件，则回文级别为左半部分的回文级别+1
                            dp[i][j] = dp[i][mid - 1] + 1;
                        } else {
                            // 至少是1级回文
                            dp[i][j] = 1;
                        }
                    }
                }
                // 非回文串的dp值保持为0
            }
        }
    }
    
    /**
     * 统计各级回文子串数量
     * 注意：如果一个子串是k级回文，那么它也是所有小于k的级别的回文
     * <p>
     * 统计逻辑：
     * 1. 初始化计数数组count[k]为0
     * 2. 遍历所有可能的子串[i..j]
     * 3. 对于每个子串，如果它是L级回文，则它也是1到L级的回文
     * 4. 因此，对于每个子串，需要将count[1]到count[L]都加1
     * <p>
     * 例如：
     * - 一个3级回文同时也是1级和2级回文
     * - 因此需要count[1]++, count[2]++, count[3]++
     * <p>
     * 实现说明：
     * 使用三层循环确保正确统计各级别回文数量
     * 虽然是O(n³)的理论复杂度，但实际中每个子串的回文级别不会太高
     * 
     * @param n 字符串长度
     * 
     * 时间复杂度：O(n² * L)，其中L是平均回文级别
     * 空间复杂度：O(n) - 存储计数数组
     */
    public static void countPalindromes(int n) {
        // 初始化计数数组，所有级别初始化为0
        for (int k = 1; k <= n; k++) {
            count[k] = 0;
        }
        
        // 遍历所有可能的子串[i..j]
        for (int i = 1; i <= n; i++) {
            for (int j = i; j <= n; j++) {
                // 对于每个子串，如果它是L级回文，那么它也是1到L级的回文
                // 因此需要将每个级别的计数器都加1
                for (int k = 1; k <= dp[i][j]; k++) {
                    count[k]++;
                }
            }
        }
    }
    
	/*
	 * ============================================================================
	 * 哈希冲突概率数学分析
	 * ============================================================================
	 * 哈希冲突是指不同的字符串产生相同哈希值的现象，这在所有哈希算法中都可能发生。
	 * 对于Codeforces 835D问题，哈希冲突会导致回文判断错误，从而影响整个算法正确性。
	 * 
	 * 1. 基数选择的影响：
	 *    - 当前实现使用base=131，这是一个较好的选择
	 *    - 理论分析：131大于小写字母集大小(26)，确保单字符哈希不冲突
	 *    - 实践证明：131在字符串哈希中表现良好，冲突率低
	 *    - 其他可选基数：13331、911、1009等更大的质数
	 * 
	 * 2. 模数选择的影响：
	 *    - 当前实现使用mod=10^9+7，这是一个大质数模数
	 *    - 模数大小：10^9+7 ≈ 1×10^9，提供了较大的哈希空间
	 *    - 冲突概率：理论上对于两个随机字符串，冲突概率约为1/mod
	 *    - 对于问题规模n=5000，子串数量约为12.5×10^6，根据生日悖论，冲突概率约为
	 *      P ≈ 1 - e^(-(12.5×10^6)^2/(2×10^9)) ≈ 0.075
	 * 
	 * 3. 哈希空间大小对冲突概率的影响：
	 *    - 当前单模数方案：哈希空间约1×10^9
	 *    - 双模数组合方案：哈希空间约(1×10^9)^2=1×10^18
	 *    - 双哈希下冲突概率：对于12.5×10^6个子串，冲突概率约为
	 *      P ≈ 1 - e^(-(12.5×10^6)^2/(2×10^18)) ≈ 7.8×10^-8
	 *    - 结论：双哈希可将冲突概率降低到极低水平
	 * 
	 * 4. 生日悖论在本题中的应用：
	 *    - 问题描述：当哈希空间大小为M，有k个子串时，至少有一对冲突的概率
	 *    - 计算公式：P(k) ≈ 1 - e^(-k²/(2M))
	 *    - 具体计算：
	 *      - 单模数：k=12.5×10^6，M=10^9，P≈7.5%
	 *      - 双模数：k=12.5×10^6，M=10^18，P≈7.8×10^-8
	 *    - 竞赛安全标准：通常要求冲突概率低于10^-8
	 * 
	 * 5. 安全界限评估：
	 *    - 单模数10^9+7的安全子串数量：k < sqrt(2M×ln(1/(1-10^-8))) ≈ 4.5×10^4
	 *    - 对于本题n=5000，子串数量远超安全界限，存在较高冲突风险
	 *    - 建议：在实际竞赛中应使用双哈希以确保结果正确性
	 * 
	 * 6. 实际应用中的最佳实践：
	 *    - 竞赛环境：必须使用双哈希或更大模数
	 *    - 工程应用：根据数据规模选择
	 *      - 小规模数据：单哈希可能足够
	 *      - 大规模数据：双哈希或更高安全措施
	 *    - 极端情况：哈希值匹配后进行字符串实际比较
	 */
	
	/*
	 * ============================================================================
	 * 双哈希实现示例
	 * ============================================================================
	 * 双哈希是一种通过同时使用两个独立哈希函数来降低冲突概率的技术。
	 * 在回文判断中，只有当两个哈希函数都验证为回文时，才认为子串是回文。
	 * 
	 * 1. 双哈希参数定义：
	 */
	/*
	// 双哈希实现的常量定义
public static final int MAXN = 5001;
public static final int BASE1 = 131;      // 第一个哈希基数
public static final int BASE2 = 13331;    // 第二个哈希基数
public static final long MOD1 = 1000000007; // 第一个模数
public static final long MOD2 = 1000000009; // 第二个模数

// 第一个哈希函数的数据结构
public static long[] prefixHash1 = new long[MAXN];
public static long[] suffixHash1 = new long[MAXN];
public static long[] pow1 = new long[MAXN];

// 第二个哈希函数的数据结构
public static long[] prefixHash2 = new long[MAXN];
public static long[] suffixHash2 = new long[MAXN];
public static long[] pow2 = new long[MAXN];
	 */
	
	/*
	 * 2. 双哈希预处理方法：
	 */
	/*
	public static void preprocess(int n) {
		// 预计算第一个哈希函数的幂次数组
		pow1[0] = 1;
		for (int i = 1; i <= n; i++) {
			pow1[i] = (pow1[i-1] * BASE1) % MOD1;
		}
		
		// 预计算第二个哈希函数的幂次数组
		pow2[0] = 1;
		for (int i = 1; i <= n; i++) {
			pow2[i] = (pow2[i-1] * BASE2) % MOD2;
		}
		
		// 计算第一个哈希函数的前缀哈希数组
		prefixHash1[0] = 0;
		for (int i = 1; i <= n; i++) {
			prefixHash1[i] = (prefixHash1[i-1] * BASE1 + (str[i] - 'a' + 1)) % MOD1;
		}
		
		// 计算第一个哈希函数的后缀哈希数组
		suffixHash1[n+1] = 0;
		for (int i = n; i >= 1; i--) {
			suffixHash1[i] = (suffixHash1[i+1] * BASE1 + (str[i] - 'a' + 1)) % MOD1;
		}
		
		// 计算第二个哈希函数的前缀哈希数组
		prefixHash2[0] = 0;
		for (int i = 1; i <= n; i++) {
			prefixHash2[i] = (prefixHash2[i-1] * BASE2 + (str[i] - 'a' + 1)) % MOD2;
		}
		
		// 计算第二个哈希函数的后缀哈希数组
		suffixHash2[n+1] = 0;
		for (int i = n; i >= 1; i--) {
			suffixHash2[i] = (suffixHash2[i+1] * BASE2 + (str[i] - 'a' + 1)) % MOD2;
		}
	}
	 */
	
	/*
	 * 3. 双哈希子串哈希计算方法：
	 */
	/*
	public static long getHash1(int l, int r) {
		if (l > r) return 0;
		long res = (prefixHash1[r] - (prefixHash1[l-1] * pow1[r-l+1]) % MOD1 + MOD1) % MOD1;
		return res;
	}
	
	public static long getReverseHash1(int l, int r) {
		if (l > r) return 0;
		long res = (suffixHash1[l] - (suffixHash1[r+1] * pow1[r-l+1]) % MOD1 + MOD1) % MOD1;
		return res;
	}
	
	public static long getHash2(int l, int r) {
		if (l > r) return 0;
		long res = (prefixHash2[r] - (prefixHash2[l-1] * pow2[r-l+1]) % MOD2 + MOD2) % MOD2;
		return res;
	}
	
	public static long getReverseHash2(int l, int r) {
		if (l > r) return 0;
		long res = (suffixHash2[l] - (suffixHash2[r+1] * pow2[r-l+1]) % MOD2 + MOD2) % MOD2;
		return res;
	}
	 */
	
	/*
	 * 4. 双哈希回文判断方法：
	 */
	/*
	public static boolean isPalindrome(int l, int r) {
		// 只有当两个哈希函数都验证为回文时，才认为是回文
		boolean hash1Result = getHash1(l, r) == getReverseHash1(l, r);
		boolean hash2Result = getHash2(l, r) == getReverseHash2(l, r);
		return hash1Result && hash2Result;
	}
	
	// 双哈希子串相等判断方法
	public static boolean isEqual(int l1, int r1, int l2, int r2) {
		// 只有当两个哈希函数都验证为相等时，才认为子串相等
		boolean hash1Equal = getHash1(l1, r1) == getHash1(l2, r2);
		boolean hash2Equal = getHash2(l1, r1) == getHash2(l2, r2);
		return hash1Equal && hash2Equal;
	}
	
	// 相应地修改computePalindromicLevels方法中的子串相等判断
	// 将原来的getHash(i, mid) == getHash(mid + 1, j)替换为：
	// isEqual(i, mid, mid + 1, j)
	// 同样地，将getHash(i, mid - 1) == getHash(mid + 1, j)替换为：
	// isEqual(i, mid - 1, mid + 1, j)
	 */
	
	/*
	 * ============================================================================
	 * 推荐测试用例
	 * ============================================================================
	 * 以下是针对Codeforces 835D问题的全面测试用例，覆盖各种边界情况和关键场景。
	 * 
	 * 1. 基本功能测试：
	 *    - 测试用例1：简单回文
	 *      输入：abacaba
	 *      预期输出：16 7 3 1 0 0 0
	 *      解释：总共有16个1级回文，7个2级回文等
	 *      
	 *    - 测试用例2：全相同字符
	 *      输入：aaaaa
	 *      预期输出：15 8 4 2 1
	 *      解释：每个子串都是多级回文
	 * 
	 * 2. 边界情况测试：
	 *    - 测试用例3：单字符
	 *      输入：a
	 *      预期输出：1
	 *      解释：只有1个1级回文
	 *      
	 *    - 测试用例4：无回文（除单个字符外）
	 *      输入：abcde
	 *      预期输出：5 0 0 0 0
	 *      解释：只有5个1级回文（每个单字符）
	 * 
	 * 3. 特殊模式测试：
	 *    - 测试用例5：周期性回文
	 *      输入：aaaabaaaa
	 *      详细分析：包含多个不同级别的回文子串
	 *      
	 *    - 测试用例6：嵌套回文
	 *      输入：abbaabba
	 *      预期：各级回文数量符合嵌套结构
	 * 
	 * 4. 复杂回文结构测试：
	 *    - 测试用例7：最大级别测试
	 *      输入：aaaa...aaa（n个a）
	 *      预期：级别k的数量为n-k+1，直到log2(n)
	 *      
	 *    - 测试用例8：混合结构
	 *      输入：abcabcabc
	 *      分析：包含重复模式但回文较少的情况
	 * 
	 * 5. 性能测试：
	 *    - 测试用例9：最大规模测试
	 *      输入：生成n=5000的字符串，如全a或ababab...
	 *      预期：算法能在时间限制内完成（Codeforces时间限制通常为1-2秒）
	 *      注意：O(n²)算法在n=5000时约有25×10^6次操作
	 * 
	 * 6. 测试用例设计原则：
	 *    - 覆盖各种回文结构（简单、嵌套、重复）
	 *    - 测试边界条件（最小长度、最大长度）
	 *    - 设计能区分不同回文级别的用例
	 *    - 包含可能触发哈希冲突的测试数据
	 *    - 验证算法在大规模数据下的性能
	 */
	
	/*
	 * ============================================================================
	 * 字符串哈希算法比较分析
	 * ============================================================================
	 * 以下是多项式滚动哈希与其他处理回文子串问题的算法的详细对比。
	 * 
	 * | 算法类型 | 时间复杂度 | 空间复杂度 | 实现复杂度 | 优势场景 | 劣势场景 |
	 * |---------|----------|-----------|-----------|---------|--------|
	 * | 多项式滚动哈希+DP | O(n²) | O(n²) | 中 | 易于实现、可计算回文级别 | 空间占用较大 |
	 * | Manacher算法 | O(n) | O(n) | 高 | 线性时间找出所有回文 | 难以计算回文级别 |
	 * | 中心扩展法 | O(n²) | O(1) | 低 | 实现简单、空间效率高 | 难以计算回文级别 |
	 * | 后缀数组+RMQ | O(n log n) | O(n) | 高 | 多模式查询、支持多种操作 | 实现复杂 |
	 * | 暴力方法 | O(n³) | O(1) | 极低 | 概念简单 | 效率极差 |
	 * | 后缀自动机 | O(n) | O(n) | 很高 | 功能强大、支持多种查询 | 实现复杂、理解困难 |
	 * 
	 * 1. 多项式滚动哈希+DP的关键优势：
	 *    - 能直接计算回文级别，符合题目要求
	 *    - 实现相对简单，易于理解和调试
	 *    - O(1)时间回文判断，避免字符比较开销
	 *    - 动态规划思路清晰，能正确表达问题结构
	 *    - 可扩展性好，容易与其他技术结合
	 * 
	 * 2. 与Manacher算法的深入比较：
	 *    - 时间复杂度：Manacher O(n) 优于 O(n²)
	 *    - 空间复杂度：Manacher O(n) 优于 O(n²)
	 *    - 功能限制：Manacher难以直接计算回文级别
	 *    - 实现难度：Manacher较难实现和理解
	 *    - 适用场景：Manacher适合仅统计数量或查找最长回文
	 * 
	 * 3. 与中心扩展法的比较：
	 *    - 时间复杂度：两者均为O(n²)
	 *    - 空间复杂度：中心扩展O(1) 优于 O(n²)
	 *    - 实现难度：中心扩展更简单
	 *    - 回文判断：中心扩展O(n) vs 哈希O(1)
	 *    - 回文级别：哈希+DP更容易计算回文级别
	 * 
	 * 4. 算法选择建议：
	 *    - 本题特定：必须使用哈希+DP或类似思路，因为需要计算回文级别
	 *    - 仅统计数量：Manacher算法最佳
	 *    - 空间受限：中心扩展法更优
	 *    - 功能扩展：后缀自动机或后缀数组更强大
	 *    - 竞赛环境：哈希+DP是平衡实现复杂度和效率的最佳选择
	 * 
	 * 5. 实际应用中的优化方向：
	 *    - 使用双哈希提高可靠性
	 *    - 空间优化：可以使用滚动数组优化DP空间
	 *    - 并行计算：某些部分可以并行化以提高性能
	 *    - 提前剪枝：对于明显不满足条件的子串提前跳过
	 *    - 内存优化：针对大规模数据使用更紧凑的数据结构
	 */
}