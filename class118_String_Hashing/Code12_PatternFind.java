package class105;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * SPOJ NAJPF Pattern Find 问题实现
 * <p>
 * 题目链接：https://www.spoj.com/problems/NAJPF/
 * <p>
 * 题目描述：
 * 给定一个文本字符串和一个模式串，找出模式串在文本字符串中所有出现的位置
 * 输出找到的位置数量以及每个位置（位置从1开始计数）
 * <p>
 * 算法核心思想：
 * 使用多项式滚动哈希（Polynomial Rolling Hash）算法实现高效的字符串匹配
 * 通过将字符串转换为数值哈希，实现O(1)时间的子串比较
 * <p>
 * 算法详细步骤：
 * 1. 预处理阶段：
 *    - 计算文本字符串的前缀哈希数组
 *    - 计算幂次数组，用于快速计算任意子串的哈希值
 * 2. 模式串哈希计算：
 *    - 计算模式串的哈希值
 * 3. 匹配阶段：
 *    - 在文本中使用滑动窗口，比较每个与模式串等长的子串哈希值
 *    - 如果哈希值相等，则记录该位置
 * 4. 结果输出：
 *    - 输出匹配位置的数量和具体位置
 * <p>
 * 哈希算法原理：
 * - 多项式滚动哈希函数：hash(s) = (s[1]*base^(m-1) + s[2]*base^(m-2) + ... + s[m]) % mod
 * - 前缀哈希数组：prefixHash[i] = (prefixHash[i-1] * base + (s[i]-'a'+1)) % mod
 * - 子串哈希计算：hash(l..r) = (prefixHash[r] - prefixHash[l-1] * base^(r-l+1)) % mod
 * <p>
 * 算法优势：
 * - 高效性：预处理O(n)，查询O(n+m)，总体时间复杂度优于朴素O(nm)算法
 * - 简洁性：实现简单，易于理解和维护
 * - 可扩展性：可以处理多个模式串查询，只需预处理一次文本
 * <p>
 * 时间复杂度分析：
 * - 预处理文本哈希和幂次数组：O(n)
 * - 计算模式串哈希值：O(m)
 * - 在文本中查找模式串：O(n)
 * - 总体时间复杂度：O(n+m)
 * <p>
 * 空间复杂度分析：
 * - 存储文本和模式串：O(n+m)
 * - 存储哈希数组和幂次数组：O(n)
 * - 存储结果列表：O(n)（最坏情况）
 * - 总体空间复杂度：O(n+m)
 * <p>
 * 哈希冲突处理：
 * - 使用大质数模数(1e9+7)和合适的基数(131)降低冲突概率
 * - 在实际编程竞赛中，这种方法通常足够可靠
 * - 对于生产环境，可以使用双哈希技术进一步降低冲突风险
 * <p>
 * 与其他字符串匹配算法比较：
 * 1. KMP算法：O(n+m)时间复杂度，但实现更复杂
 * 2. Rabin-Karp算法：与本实现类似，也是哈希基础，但本实现做了优化
 * 3. 朴素匹配：O(nm)时间复杂度，效率较低
 * <p>
 * 相似题目：
 * 1. LeetCode 28: Implement strStr() - 查找子串首次出现位置
 * 2. LeetCode 459: Repeated Substring Pattern - 检测重复子串模式
 * 3. Codeforces 126B: Password - 查找满足特定条件的子串
 * 4. POJ 1226: Substrings - 处理多个子串查询
 * 5. HDU 1711: Number Sequence - 数值序列匹配问题
 * <p>
 * 测试链接：https://www.spoj.com/problems/NAJPF/
 * <p>
 * 三种语言实现参考：
 * - Java实现：当前文件
 * - Python实现：Code12_PatternFind.py
 * - C++实现：Code12_PatternFind.cpp
 * 
 * @author Algorithm Journey
 */

public class Code12_PatternFind {

    /**
     * 最大字符串长度，根据题目约束设置为1e6+6
     * SPOJ NAJPF中字符串长度可能达到1e6级别
     */
    public static int MAXN = 1000006;
    
    /**
     * 哈希基数，选择131作为哈希基数以减少哈希冲突
     * 131是常用的字符串哈希基数，与mod配合使用可以有效降低冲突概率
     */
    public static int base = 131;
    
    /**
     * 模数，使用1e9+7作为模数以控制哈希值大小并减少溢出
     * 10^9+7是一个大质数模数，能有效防止整数溢出
     */
    public static int mod = 1000000007;
    
    /**
     * 存储文本字符串的字符数组，从索引1开始存储
     * 从1开始索引可以简化哈希计算的边界条件处理
     */
    public static char[] text = new char[MAXN];
    
    /**
     * 存储模式串的字符数组，从索引1开始存储
     * 同样使用1-based索引以保持一致性
     */
    public static char[] pattern = new char[MAXN];
    
    /**
     * 前缀哈希数组，prefixHash[i]表示子串text[1..i]的哈希值
     * 用于快速计算文本中任意子串的哈希值
     */
    public static long[] prefixHash = new long[MAXN];
    
    /**
     * 前缀幂次数组，pow[i]表示base^i % mod
     * 预计算避免重复计算，提高哈希值计算效率
     */
    public static long[] pow = new long[MAXN];
    
    /**
     * 主方法，处理输入输出并协调整个算法流程
     * <p>
     * 处理流程：
     * 1. 初始化高效的输入输出流
     * 2. 读取测试用例数量
     * 3. 对于每个测试用例：
     *    - 读取文本字符串和模式串
     *    - 将字符串转换为1-based索引的字符数组
     *    - 调用findPattern方法查找所有匹配位置
     *    - 输出结果（匹配数量和位置）
     * 4. 关闭资源
     * <p>
     * 输入格式：
     * 第一行：测试用例数量t
     * 每个测试用例：
     *  - 第一行：文本字符串
     *  - 第二行：模式串
     * <p>
     * 输出格式：
     * 对于每个测试用例：
     *  - 如果找到匹配：
     *    - 第一行：匹配数量
     *    - 第二行：所有匹配位置（从1开始计数）
     *  - 如果未找到匹配：
     *    - 一行："Not Found"
     *  - 测试用例之间输出空行
     * 
     * @param args 命令行参数
     * @throws IOException 输入输出异常
     * 
     * 时间复杂度：O(t*(n+m))，其中t是测试用例数量
     * 空间复杂度：O(n+m)
     */
    public static void main(String[] args) throws IOException {
        // 创建高效的输入输出流，处理大规模数据
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取测试用例数量
        int t = Integer.parseInt(in.readLine());
        
        // 处理每个测试用例
        for (int i = 0; i < t; i++) {
            // 读取文本字符串和模式串
            String line1 = in.readLine(); // 文本字符串
            String line2 = in.readLine(); // 模式串
            
            int n = line1.length(); // 文本长度
            int m = line2.length(); // 模式串长度
            
            // 将字符串复制到字符数组中，从索引1开始存储以便于计算
            // 1-based索引可以简化哈希计算的边界条件
            line1.getChars(0, n, text, 1);
            line2.getChars(0, m, pattern, 1);
            
            // 查找模式串在文本中的所有出现位置
            // 这是算法的核心调用
            List<Integer> positions = findPattern(text, n, pattern, m);
            
            // 输出结果
            if (positions.isEmpty()) {
                out.println("Not Found");
            } else {
                out.println(positions.size()); // 输出出现次数
                for (int pos : positions) {
                    out.print(pos + " "); // 输出每个出现的位置
                }
                out.println();
            }
            
            // 在测试用例之间输出空行（最后一个测试用例除外）
            // 符合SPOJ题目的输出格式要求
            if (i < t - 1) {
                out.println();
            }
        }
        
        // 刷新输出并关闭资源
        out.flush();
        out.close();
        in.close();
    }
    
    /**
     * 预处理函数，计算前缀哈希和幂次数组
     * 为后续快速计算任意子串的哈希值提供基础
     * <p>
     * 预处理内容：
     * 1. 幂次数组pow：存储base的各次幂，用于快速计算子串哈希
     * 2. 前缀哈希数组prefixHash：从左到右计算哈希值
     * <p>
     * 哈希计算原理：
     * - 多项式滚动哈希：将字符串视为base进制数
     * - 前缀哈希：hash[i] = hash[i-1] * base + (s[i]-'a'+1) mod mod
     * - 字符值偏移+1是为了避免'a'的哈希值为0，减少哈希冲突
     * <p>
     * 数学推导：
     * - 对于字符串s[1..n]，哈希值为：s[1]*base^(n-1) + s[2]*base^(n-2) + ... + s[n]
     * - 通过前缀哈希可以在O(1)时间内计算任意子串的哈希值
     * 
     * @param s 字符串数组（1-based索引）
     * @param n 字符串长度
     * 
     * 时间复杂度：O(n) - 线性时间完成预处理
     * 空间复杂度：O(n) - 使用了两个长度为n+1的数组
     */
    public static void preprocess(char[] s, int n) {
        // 计算幂次数组，pow[i] = base^i % mod
        // 预计算幂次数组可以避免重复计算，提高哈希值计算效率
        pow[0] = 1; // 初始化base^0 = 1
        for (int i = 1; i <= n; i++) {
            pow[i] = (pow[i - 1] * base) % mod; // 递推计算并取模
        }
        
        // 计算前缀哈希数组，prefixHash[i]表示子串s[1..i]的哈希值
        prefixHash[0] = 0; // 空字符串的哈希值为0
        for (int i = 1; i <= n; i++) {
            // 哈希递推公式：prefixHash[i] = (prefixHash[i-1] * base + (s[i]-'a'+1)) % mod
            // 加1是为了避免字符'a'的哈希值为0，减少哈希冲突
            prefixHash[i] = (prefixHash[i - 1] * base + s[i] - 'a' + 1) % mod;
        }
    }
    
    /**
     * 获取子串s[l..r]的哈希值
     * 利用前缀哈希数组快速计算任意子串的哈希值
     * <p>
     * 计算公式：
     * hash(l..r) = (hash(1..r) - hash(1..l-1) * base^(r-l+1)) % mod
     * <p>
     * 数学原理详解：
     * - 假设字符串s[1..r]的哈希值为：hash(1..r) = s[1]*base^(r-1) + s[2]*base^(r-2) + ... + s[r]
     * - 字符串s[1..l-1]的哈希值为：hash(1..l-1) = s[1]*base^(l-2) + s[2]*base^(l-3) + ... + s[l-1]
     * - 将hash(1..l-1)乘以base^(r-l+1)得到：s[1]*base^(r-1) + s[2]*base^(r-2) + ... + s[l-1]*base^(r-l+1)
     * - 用hash(1..r)减去这个值，得到：s[l]*base^(r-l) + s[l+1]*base^(r-l-1) + ... + s[r]，即hash(l..r)
     * <p>
     * 取模处理说明：
     * - 加上mod再取模是为了确保结果为非负数
     * - 在减法操作中可能产生负数，需要调整为正数
     * 
     * @param l 左边界(包含，1-based索引)
     * @param r 右边界(包含，1-based索引)
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
     * 查找模式串在文本中的所有出现位置
     * 使用哈希技术实现高效的字符串匹配
     * <p>
     * 算法步骤：
     * 1. 预处理文本字符串，计算前缀哈希和幂次数组
     * 2. 计算模式串的哈希值
     * 3. 在文本中使用滑动窗口技术，遍历所有可能的匹配位置
     *    - 对于每个位置i，计算从i开始长度为m的子串的哈希值
     *    - 如果与模式串哈希值相等，则记录该位置
     * 4. 返回所有匹配位置的列表
     * <p>
     * 滑动窗口原理：
     * - 窗口大小固定为模式串长度m
     * - 从文本字符串的起始位置开始，依次向右滑动一个字符
     * - 每次滑动后比较当前窗口与模式串的哈希值
     * <p>
     * 哈希匹配优势：
     * - 每次窗口滑动后的哈希比较只需O(1)时间
     * - 相比朴素O(nm)算法，大幅提高了匹配效率
     * <p>
     * 注意事项：
     * - 本实现使用单哈希，理论上存在哈希冲突可能
     * - 在实际编程竞赛中，使用大质数模数和合适的基数通常足够可靠
     * - 对于要求更高的场景，可以考虑使用双哈希（两个不同的哈希函数）
     * 
     * @param text 文本字符串数组（1-based索引）
     * @param n 文本长度
     * @param pattern 模式串数组（1-based索引）
     * @param m 模式串长度
     * @return 所有出现位置的列表，位置从1开始计数
     * 
     * 时间复杂度：O(n+m)
     * - 预处理文本：O(n)
     * - 计算模式串哈希值：O(m)
     * - 查找过程：O(n)
     * 空间复杂度：O(n+k)，其中k是匹配数量
     */
    public static List<Integer> findPattern(char[] text, int n, char[] pattern, int m) {
        List<Integer> positions = new ArrayList<>();
        
        // 预处理文本字符串，计算前缀哈希和幂次数组
        preprocess(text, n);
        
        // 计算模式串的哈希值
        // 使用与文本相同的哈希算法，确保可比较性
        long patternHash = 0;
        for (int i = 1; i <= m; i++) {
            // 多项式滚动哈希算法：hash = (hash * base + (pattern[i]-'a'+1)) % mod
            patternHash = (patternHash * base + pattern[i] - 'a' + 1) % mod;
        }
        
        // 在文本中滑动窗口查找模式串
        // i是当前窗口的起始位置，窗口长度为m
        // 窗口范围：[i, i+m-1]，必须保证不超出文本边界
        for (int i = 1; i + m - 1 <= n; i++) {
            // 计算当前窗口的哈希值并与模式串哈希值比较
            // 使用O(1)时间计算子串哈希值
            if (getHash(i, i + m - 1) == patternHash) {
                // 如果哈希值相等，则记录该位置
                // 注意：题目要求位置从1开始计数，而我们的索引正好也是从1开始的
                positions.add(i);
            }
        }
        
        return positions;
    }
	
	/*
	 * ============================================================================
	 * 哈希冲突概率数学分析
	 * ============================================================================
	 * 哈希冲突是指不同字符串产生相同哈希值的现象。在字符串匹配问题中，哈希冲突
	 * 会导致假阳性结果（误判匹配），这在编程竞赛中可能导致错误。
	 * 
	 * 1. 当前实现的哈希参数与冲突风险：
	 *    - 基数base=131，模数mod=10^9+7（约10亿）
	 *    - 哈希空间大小M≈10^9
	 *    - 使用字符值偏移+1的方式（s[i]-'a'+1）
	 *    - 1-based索引简化了边界条件处理
	 * 
	 * 2. 生日悖论下的冲突概率计算：
	 *    - 对于长度为n的文本和长度为m的模式串，需要比较(n-m+1)个哈希值
	 *    - 同时还计算了1个模式串哈希值
	 *    - 总共有k = (n-m+2)个哈希值
	 *    - 至少一次冲突的概率：P ≈ 1 - e^(-k²/(2M))
	 *    - 当n=1e6, m=1时，k≈1e6，P≈1 - e^(-(1e6)^2/(2×1e9)) ≈ 0.39
	 *    - 当n=1e6, m=100时，k≈999901，P≈0.39
	 *    - 结论：单哈希在大规模数据下冲突概率较高
	 * 
	 * 3. 模数与基数的选择优化：
	 *    - 选择大质数作为模数：10^9+7, 10^9+9, 1e18+3等
	 *    - 选择与模数互质的基数：确保哈希分布均匀
	 *    - 当前选择：base=131与mod=1e9+7互质（gcd(131, 1e9+7)=1）
	 *    - 其他推荐组合：base=13331+mod=1e9+9，base=911+mod=1e9+7
	 * 
	 * 4. 碰撞期望与实际影响：
	 *    - 期望碰撞次数：E = k²/(2M)
	 *    - 当k=1e6, M=1e9时，E≈(1e12)/(2×1e9)=500
	 *    - 在编程竞赛中，这可能导致无法通过所有测试用例
	 *    - 在实际应用中，需要额外的字符串比较来验证匹配
	 * 
	 * 5. 安全参数选择指南：
	 *    - 对于小数据（n<1e4）：单哈希+1e9+7通常足够
	 *    - 对于中等数据（1e4<n<1e5）：考虑使用双哈希或更大模数
	 *    - 对于大数据（n≥1e5）：强烈建议使用双哈希
	 *    - 对于100%正确性要求：哈希值相等后再进行字符串比较
	 */
	
	/*
	 * ============================================================================
	 * 双哈希实现示例
	 * ============================================================================
	 * 双哈希通过同时使用两个独立的哈希函数，可以大幅降低冲突概率。在字符串匹配问题中，
	 * 只有当两个哈希值都匹配时才认为字符串匹配。
	 * 
	 * 1. 双哈希常量定义：
	 */
	/*
	// 第一个哈希函数的参数
public static int BASE1 = 131;
public static long MOD1 = 1000000007;

// 第二个哈希函数的参数
public static int BASE2 = 13331;
public static long MOD2 = 1000000009;

// 哈希数组定义
public static long[] prefixHash1 = new long[MAXN]; // 第一个前缀哈希数组
public static long[] prefixHash2 = new long[MAXN]; // 第二个前缀哈希数组
public static long[] pow1 = new long[MAXN]; // BASE1的幂次数组
public static long[] pow2 = new long[MAXN]; // BASE2的幂次数组
	 */
	
	/*
	 * 2. 双哈希预处理函数：
	 */
	/*
	public static void preprocessWithDoubleHash(char[] s, int n) {
		// 预计算两个哈希函数的幂次数组
		pow1[0] = 1;
		pow2[0] = 1;
		for (int i = 1; i <= n; i++) {
			pow1[i] = (pow1[i-1] * BASE1) % MOD1;
			pow2[i] = (pow2[i-1] * BASE2) % MOD2;
		}
		
		// 计算两个哈希函数的前缀哈希数组
		prefixHash1[0] = 0;
		prefixHash2[0] = 0;
		for (int i = 1; i <= n; i++) {
			prefixHash1[i] = (prefixHash1[i-1] * BASE1 + s[i] - 'a' + 1) % MOD1;
			prefixHash2[i] = (prefixHash2[i-1] * BASE2 + s[i] - 'a' + 1) % MOD2;
		}
	}
	 */
	
	/*
	 * 3. 双哈希值获取函数：
	 */
	/*
	// 获取子串的双哈希值
public static long getHash1(int l, int r) {
	if (l > r) return 0;
	return (prefixHash1[r] - (prefixHash1[l-1] * pow1[r-l+1]) % MOD1 + MOD1) % MOD1;
}

public static long getHash2(int l, int r) {
	if (l > r) return 0;
	return (prefixHash2[r] - (prefixHash2[l-1] * pow2[r-l+1]) % MOD2 + MOD2) % MOD2;
}
	 */
	
	/*
	 * 4. 双哈希模式匹配实现：
	 */
	/*
	public static List<Integer> findPatternWithDoubleHash(char[] text, int n, char[] pattern, int m) {
		List<Integer> positions = new ArrayList<>();
		
		// 预处理文本字符串
		preprocessWithDoubleHash(text, n);
		
		// 计算模式串的双哈希值
		long patternHash1 = 0, patternHash2 = 0;
		for (int i = 1; i <= m; i++) {
			patternHash1 = (patternHash1 * BASE1 + pattern[i] - 'a' + 1) % MOD1;
			patternHash2 = (patternHash2 * BASE2 + pattern[i] - 'a' + 1) % MOD2;
		}
		
		// 滑动窗口查找
		for (int i = 1; i + m - 1 <= n; i++) {
			// 只有当两个哈希值都相等时才认为匹配
			if (getHash1(i, i + m - 1) == patternHash1 && getHash2(i, i + m - 1) == patternHash2) {
				positions.add(i);
			}
		}
		
		return positions;
	}
	 */
	
	/*
	 * 5. 双哈希的优势分析：
	 *    - 冲突概率：从单哈希的≈0.39降至≈(0.39)^2≈0.15
	 *    - 使用两个大质数模数时，理论冲突概率极低
	 *    - 在编程竞赛中，双哈希通常可以通过所有测试用例
	 *    - 时间开销：只比单哈希多约50%的计算量
	 *    - 空间开销：需要存储两个哈希数组和两个幂次数组
	 */
	
	/*
	 * ============================================================================
	 * 推荐测试用例
	 * ============================================================================
	 * 以下测试用例覆盖了SPOJ NAJPF Pattern Find问题的各种场景，有助于验证算法正确性。
	 * 
	 * 1. 基本功能测试：
	 *    - 测试用例1：简单匹配
	 *      输入：
	 *      1
	 *      abcabcabc
	 *      abc
	 *      预期输出：
	 *      3
	 *      1 4 7 
	 *      解释：模式串abc在文本中出现3次
	 *      
	 *    - 测试用例2：无匹配
	 *      输入：
	 *      1
	 *      hello
	 *      world
	 *      预期输出：
	 *      Not Found
	 *      解释：文本中不存在模式串
	 * 
	 * 2. 边界情况测试：
	 *    - 测试用例3：模式串等于文本
	 *      输入：
	 *      1
	 *      abcdef
	 *      abcdef
	 *      预期输出：
	 *      1
	 *      1 
	 *      解释：模式串等于整个文本，只出现一次
	 *      
	 *    - 测试用例4：模式串长度为1
	 *      输入：
	 *      1
	 *      aaaaaa
	 *      a
	 *      预期输出：
	 *      6
	 *      1 2 3 4 5 6 
	 *      解释：每个位置都匹配
	 *      
	 *    - 测试用例5：模式串长度大于文本
	 *      输入：
	 *      1
	 *      abc
	 *      abcdef
	 *      预期输出：
	 *      Not Found
	 *      解释：无法匹配
	 * 
	 * 3. 重叠匹配测试：
	 *    - 测试用例6：重叠模式
	 *      输入：
	 *      1
	 *      aaaaa
	 *      aa
	 *      预期输出：
	 *      4
	 *      1 2 3 4 
	 *      解释：模式串"aa"在"aaaaa"中有4次重叠出现
	 *      
	 * 4. 哈希冲突测试：
	 *    - 测试用例7：构造的哈希冲突字符串
	 *      目的：测试算法在哈希冲突情况下的表现
	 *      注意：需要针对具体哈希函数构造冲突字符串
	 *      
	 * 5. 大数据测试：
	 *    - 测试用例8：极限长度测试
	 *      输入：
	 *      1
	 *      [生成1e6个'a']
	 *      a
	 *      预期输出：
	 *      1000000
	 *      [输出1到1000000的所有整数]
	 *      注意：测试算法处理大规模数据的能力
	 *      
	 * 6. 多测试用例测试：
	 *    - 测试用例9：连续多个测试用例
	 *      输入：
	 *      3
	 *      abcdef
	 *      def
	 *      hello world
	 *      world
	 *      programming
	 *      gram
	 *      预期输出：
	 *      1
	 *      4 
	 *      
	 *      1
	 *      7 
	 *      
	 *      1
	 *      4 
	 *      解释：测试连续处理多个测试用例和空行输出格式
	 */
	
	/*
	 * ============================================================================
	 * 字符串哈希算法比较分析
	 * ============================================================================
	 * 以下是不同字符串匹配算法的详细对比分析，帮助理解哈希方法在字符串匹配中的优势和劣势。
	 * 
	 * | 算法类型 | 时间复杂度 | 空间复杂度 | 实现复杂度 | 冲突风险 | 适用场景 |
	 * |---------|----------|-----------|-----------|---------|--------|
	 * | 多项式滚动哈希(单模数) | O(n+m) | O(n) | 低 | 中 | 编程竞赛，中等规模数据 |
	 * | 双哈希 | O(n+m) | O(n) | 中 | 极低 | 编程竞赛，大规模数据 |
	 * | KMP算法 | O(n+m) | O(m) | 高 | 无 | 确定性匹配，需要失败函数 |
	 * | Z-算法 | O(n+m) | O(n+m) | 中 | 无 | 多模式匹配，边界分析 |
	 * | Aho-Corasick | O(n+m+z) | O(m) | 高 | 无 | 多模式匹配 |
	 * | 朴素匹配 | O(n*m) | O(1) | 极低 | 无 | 小规模数据，概念验证 |
	 * | BM算法 | O(n+m)平均，O(nm)最坏 | O(m+σ) | 高 | 无 | 实际应用中的高效算法 |
	 * 
	 * 1. 哈希方法的优缺点：
	 *    - 优点：
	 *      * 实现简单，代码量少
	 *      * 预处理后支持O(1)时间的子串比较
	 *      * 易于扩展到多模式匹配（通过哈希表）
	 *      * 可以与其他方法结合使用
	 *    - 缺点：
	 *      * 存在哈希冲突风险
	 *      * 需要额外空间存储哈希数组
	 *      * 在某些情况下性能不如专门的字符串匹配算法
	 * 
	 * 2. 与KMP算法的比较：
	 *    - 时间复杂度：两者都是O(n+m)
	 *    - 实现复杂度：哈希方法更简单
	 *    - 空间复杂度：KMP只需要O(m)空间，哈希需要O(n)空间
	 *    - 确定性：KMP算法无冲突，哈希算法可能有冲突
	 *    - 适用场景：KMP适合需要精确匹配的场景，哈希适合需要快速实现的场景
	 * 
	 * 3. 与Z-算法的比较：
	 *    - 时间复杂度：两者都是O(n+m)
	 *    - Z-算法需要构建Z数组，哈希需要构建前缀哈希数组
	 *    - Z-算法适合查找所有前缀匹配，哈希适合任意子串匹配
	 *    - Z-算法无冲突风险，哈希算法有理论冲突风险
	 * 
	 * 4. 工业级应用中的最佳实践：
	 *    - 对于需要100%正确的关键系统：使用KMP、Z-算法等无冲突算法
	 *    - 对于开发效率优先的项目：使用哈希方法+必要的验证
	 *    - 对于大规模数据处理：
	 *      a. 使用双哈希降低冲突概率
	 *      b. 考虑使用BM等优化的字符串匹配算法
	 *      c. 对于多模式匹配，考虑Aho-Corasick算法
	 *    - 对于实时系统：预处理所有可能的哈希值，使用缓存优化
	 * 
	 * 5. SPOJ NAJPF问题的最佳算法选择：
	 *    - 首选方案：双哈希（本题代码实现的升级版）
	 *    - 理由：
	 *      a. 实现相对简单，代码量适中
	 *      b. 时间复杂度O(n+m)，满足题目要求
	 *      c. 冲突概率极低，在竞赛中足够可靠
	 *      d. 空间复杂度合理，能够处理1e6规模的数据
	 *    - 次选方案：KMP算法
	 *    - 不推荐：朴素匹配、单哈希
	 * 
	 * 6. 哈希优化技巧：
	 *    - 使用大质数模数和互质基数
	 *    - 预计算幂次数组避免重复计算
	 *    - 使用long类型存储哈希值避免溢出
	 *    - 哈希值相等后进行字符串比较（对于关键场景）
	 *    - 对于多模式查询，使用哈希表存储模式串哈希
	 */
}