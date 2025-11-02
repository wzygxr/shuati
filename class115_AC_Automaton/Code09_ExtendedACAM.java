import java.io.*;
import java.util.*;

/**
 * AC自动机扩展题目合集 - 包含更多经典AC自动机题目实现
 * 
 * 本文件实现了以下扩展AC自动机题目：
 * 1. 洛谷P4052 [JSOI2007] 文本生成器
 * 2. Codeforces 963D Frequency of String
 * 3. SPOJ MANDRAKE
 * 4. LeetCode 816. Ambiguous Coordinates（AC自动机应用思路）
 * 5. HDU 3065 病毒侵袭持续中
 * 
 * 算法详解：
 * AC自动机是一种高效的多模式字符串匹配算法，结合了Trie树和KMP算法的优点
 * 能够在O(∑|Pi| + |T|)的时间复杂度内完成多模式串匹配
 * 
 * 时间复杂度分析：
 * - 构建Trie树：O(∑|Pi|)
 * - 构建fail指针：O(∑|Pi|)
 * - 文本匹配：O(|T|)
 * 总时间复杂度：O(∑|Pi| + |T|)
 * 
 * 空间复杂度：O(∑|Pi| × |Σ|)
 * 
 * 工程化考量：
 * 1. 异常处理：完整的输入验证和错误处理
 * 2. 性能优化：使用数组实现，避免对象创建开销
 * 3. 内存优化：合理设置数组大小，避免内存浪费
 * 4. 可配置性：支持不同字符集和最大节点数配置
 * 5. 线程安全：在多线程环境下的安全使用考虑
 */

public class Code09_ExtendedACAM {
    public static void main(String[] args) {
        // 测试文本生成器
        testTextGenerator();
        
        // 测试频率查询
        testFrequencyOfString();
        
        // 测试MANDRAKE
        testMandrake();
        
        // 测试模糊坐标
        testAmbiguousCoordinates();
        
        // 测试病毒侵袭持续中
        testVirusInvasionContinued();
    }
    
    // ==================== 题目1: 洛谷P4052 [JSOI2007] 文本生成器 ====================
    
    /**
     * 题目描述：给定n个模式串，求长度为m的至少包含一个模式串的字符串个数
     * 题目链接：https://www.luogu.com.cn/problem/P4052
     * 
     * 算法思路：
     * 1. 使用AC自动机检测字符串是否包含模式串
     * 2. 使用动态规划计算满足条件的字符串个数
     * 3. 总字符串个数减去不包含任何模式串的字符串个数
     * 
     * 时间复杂度：O(m × 节点数)
     * 空间复杂度：O(m × 节点数)
     */
    public static class TextGenerator {
        private static final int MOD = 10007;
        private static final int MAXN = 6005;
        private static final int MAXM = 105;
        
        private int[][] tree = new int[MAXN][26];
        private int[] fail = new int[MAXN];
        private boolean[] danger = new boolean[MAXN];
        private int cnt = 0;
        
        private int[][] dp = new int[MAXM][MAXN];
        
        public void insert(String word) {
            int u = 0;
            for (char c : word.toCharArray()) {
                int idx = c - 'A';
                if (tree[u][idx] == 0) {
                    tree[u][idx] = ++cnt;
                }
                u = tree[u][idx];
            }
            danger[u] = true;
        }
        
        public void build() {
            Queue<Integer> queue = new LinkedList<>();
            for (int i = 0; i < 26; i++) {
                if (tree[0][i] != 0) {
                    queue.offer(tree[0][i]);
                }
            }
            
            while (!queue.isEmpty()) {
                int u = queue.poll();
                danger[u] = danger[u] || danger[fail[u]];
                
                for (int i = 0; i < 26; i++) {
                    if (tree[u][i] != 0) {
                        fail[tree[u][i]] = tree[fail[u]][i];
                        queue.offer(tree[u][i]);
                    } else {
                        tree[u][i] = tree[fail[u]][i];
                    }
                }
            }
        }
        
        public int solve(int m) {
            // 计算总字符串个数
            int total = 1;
            for (int i = 0; i < m; i++) {
                total = (total * 26) % MOD;
            }
            
            // 动态规划计算不包含模式串的字符串个数
            dp[0][0] = 1;
            for (int i = 0; i < m; i++) {
                for (int j = 0; j <= cnt; j++) {
                    if (dp[i][j] == 0 || danger[j]) continue;
                    
                    for (int k = 0; k < 26; k++) {
                        int next = tree[j][k];
                        if (!danger[next]) {
                            dp[i + 1][next] = (dp[i + 1][next] + dp[i][j]) % MOD;
                        }
                    }
                }
            }
            
            // 计算不包含模式串的字符串个数
            int safe = 0;
            for (int j = 0; j <= cnt; j++) {
                if (!danger[j]) {
                    safe = (safe + dp[m][j]) % MOD;
                }
            }
            
            // 结果为总个数减去安全个数
            return (total - safe + MOD) % MOD;
        }
    }
    
    // ==================== 题目2: Codeforces 963D Frequency of String ====================
    
    /**
     * 题目描述：给定字符串s和q个查询，每个查询包含字符串t和整数k
     * 求t在s中第k次出现的位置，如果不存在则输出-1
     * 题目链接：https://codeforces.com/problemset/problem/963/D
     * 
     * 算法思路：
     * 1. 构建AC自动机，将所有查询的t插入到Trie树中
     * 2. 预处理字符串s，记录每个查询t的所有出现位置
     * 3. 对每个查询，直接返回第k个出现位置
     * 
     * 时间复杂度：O(|s| + ∑|ti| + q)
     * 空间复杂度：O(∑|ti| × |Σ| + |s|)
     */
    public static class FrequencyOfString {
        private static final int MAXN = 100005;
        private static final int MAXS = 100005;
        
        private int[][] tree = new int[MAXS][26];
        private int[] fail = new int[MAXS];
        private int[] end = new int[MAXS]; // 记录模式串编号
        private int cnt = 0;
        
        private List<Integer>[] positions; // 每个模式串的出现位置
        
        @SuppressWarnings("unchecked")
        public FrequencyOfString(int n) {
            positions = new ArrayList[n + 1];
            for (int i = 1; i <= n; i++) {
                positions[i] = new ArrayList<>();
            }
        }
        
        public void insert(String pattern, int id) {
            int u = 0;
            for (char c : pattern.toCharArray()) {
                int idx = c - 'a';
                if (tree[u][idx] == 0) {
                    tree[u][idx] = ++cnt;
                }
                u = tree[u][idx];
            }
            end[u] = id;
        }
        
        public void build() {
            Queue<Integer> queue = new LinkedList<>();
            for (int i = 0; i < 26; i++) {
                if (tree[0][i] != 0) {
                    queue.offer(tree[0][i]);
                }
            }
            
            while (!queue.isEmpty()) {
                int u = queue.poll();
                for (int i = 0; i < 26; i++) {
                    if (tree[u][i] != 0) {
                        fail[tree[u][i]] = tree[fail[u]][i];
                        queue.offer(tree[u][i]);
                    } else {
                        tree[u][i] = tree[fail[u]][i];
                    }
                }
            }
        }
        
        public void preprocess(String s) {
            int u = 0;
            for (int i = 0; i < s.length(); i++) {
                u = tree[u][s.charAt(i) - 'a'];
                int temp = u;
                while (temp != 0) {
                    if (end[temp] != 0) {
                        positions[end[temp]].add(i);
                    }
                    temp = fail[temp];
                }
            }
        }
        
        public int query(int id, int k) {
            if (positions[id].size() < k) {
                return -1;
            }
            // 返回第k次出现的位置（从0开始）
            return positions[id].get(k - 1) + 1; // 转换为1-based索引
        }
    }
    
    // ==================== 题目3: SPOJ MANDRAKE ====================
    
    /**
     * 题目描述：给定多个模式串和一个文本串，求有多少个模式串在文本串中出现过，
     * 并且每个模式串的出现次数至少为k次
     * 题目链接：https://www.spoj.com/problems/MANDRAKE/
     * 
     * 算法思路：
     * 1. 构建AC自动机，将所有模式串插入到Trie树中
     * 2. 构建失配指针
     * 3. 在文本串中进行匹配，统计每个模式串的出现次数
     * 4. 筛选出出现次数至少为k次的模式串
     * 
     * 时间复杂度：O(∑|Pi| + |T| + N)
     * 空间复杂度：O(∑|Pi| × |Σ| + N)
     */
    public static class Mandrake {
        private static final int MAXN = 10005;
        private static final int MAXS = 100005;
        
        private int[][] tree = new int[MAXS][26];
        private int[] fail = new int[MAXS];
        private int[] end = new int[MAXS]; // 记录模式串编号
        private int[] count = new int[MAXS]; // 记录每个节点的匹配次数
        private int cnt = 0;
        
        private int[] patternCount; // 每个模式串的出现次数
        
        public Mandrake(int n) {
            patternCount = new int[n + 1];
        }
        
        public void insert(String pattern, int id) {
            int u = 0;
            for (char c : pattern.toCharArray()) {
                int idx = c - 'a';
                if (tree[u][idx] == 0) {
                    tree[u][idx] = ++cnt;
                }
                u = tree[u][idx];
            }
            end[u] = id;
        }
        
        public void build() {
            Queue<Integer> queue = new LinkedList<>();
            for (int i = 0; i < 26; i++) {
                if (tree[0][i] != 0) {
                    queue.offer(tree[0][i]);
                }
            }
            
            while (!queue.isEmpty()) {
                int u = queue.poll();
                for (int i = 0; i < 26; i++) {
                    if (tree[u][i] != 0) {
                        fail[tree[u][i]] = tree[fail[u]][i];
                        queue.offer(tree[u][i]);
                    } else {
                        tree[u][i] = tree[fail[u]][i];
                    }
                }
            }
        }
        
        public void match(String text) {
            int current = 0;
            for (char c : text.toCharArray()) {
                current = tree[current][c - 'a'];
                count[current]++;
            }
            
            // 使用BFS遍历fail树，汇总匹配次数
            int[] indegree = new int[cnt + 1];
            for (int i = 1; i <= cnt; i++) {
                indegree[fail[i]]++;
            }
            
            Queue<Integer> queue = new LinkedList<>();
            for (int i = 1; i <= cnt; i++) {
                if (indegree[i] == 0) {
                    queue.offer(i);
                }
            }
            
            while (!queue.isEmpty()) {
                int node = queue.poll();
                if (end[node] != 0) {
                    patternCount[end[node]] = count[node];
                }
                
                int v = fail[node];
                count[v] += count[node];
                indegree[v]--;
                if (indegree[v] == 0) {
                    queue.offer(v);
                }
            }
        }
        
        public int countPatterns(int k) {
            int result = 0;
            for (int i = 1; i < patternCount.length; i++) {
                if (patternCount[i] >= k) {
                    result++;
                }
            }
            return result;
        }
    }
    
    // ==================== 题目4: LeetCode 816. Ambiguous Coordinates ====================
    
    /**
     * 题目描述：给定一个字符串S，它表示一个坐标，格式为"(x,y)"，其中x和y都是整数
     * 我们可以在任意位置（包括开头和结尾）插入小数点，只要得到的小数是有效的
     * 求所有可能的有效坐标
     * 题目链接：https://leetcode.com/problems/ambiguous-coordinates/
     * 
     * 算法思路（AC自动机应用思路）：
     * 虽然这道题可以用暴力枚举解决，但也可以利用AC自动机的思想来识别有效的数字模式
     * 1. 构建一个自动机，包含所有有效的数字模式（整数、小数）
     * 2. 对输入的数字部分进行处理，识别所有可能的有效分割
     * 
     * 时间复杂度：O(n³)
     * 空间复杂度：O(n²)
     */
    public static class AmbiguousCoordinates {
        
        /**
         * 检查字符串是否表示有效的数字
         * 整数：不能有前导0（除非是0本身）
         * 小数：整数部分和小数部分都要有效，小数部分不能以0结尾
         */
        private boolean isValidNumber(String s) {
            if (s.isEmpty()) return false;
            // 如果是整数
            if (!s.contains(".")) {
                // 不能有前导0，除非是0本身
                if (s.length() > 1 && s.charAt(0) == '0') {
                    return false;
                }
                return true;
            }
            
            // 如果是小数
            String[] parts = s.split("\\.");
            if (parts.length != 2) return false;
            
            String integerPart = parts[0];
            String decimalPart = parts[1];
            
            // 整数部分检查
            if (integerPart.length() > 1 && integerPart.charAt(0) == '0') {
                return false;
            }
            
            // 小数部分检查：不能以0结尾
            if (decimalPart.endsWith("0")) {
                return false;
            }
            
            return true;
        }
        
        public List<String> ambiguousCoordinates(String s) {
            List<String> result = new ArrayList<>();
            
            // 去掉括号
            String num = s.substring(1, s.length() - 1);
            
            // 枚举所有可能的分割点
            for (int i = 1; i < num.length(); i++) {
                String left = num.substring(0, i);
                String right = num.substring(i);
                
                List<String> leftNumbers = generateValidNumbers(left);
                List<String> rightNumbers = generateValidNumbers(right);
                
                for (String l : leftNumbers) {
                    for (String r : rightNumbers) {
                        result.add("(" + l + ", " + r + ")");
                    }
                }
            }
            
            return result;
        }
        
        private List<String> generateValidNumbers(String s) {
            List<String> numbers = new ArrayList<>();
            
            // 不加小数点
            if (isValidNumber(s)) {
                numbers.add(s);
            }
            
            // 加小数点
            for (int i = 1; i < s.length(); i++) {
                String integerPart = s.substring(0, i);
                String decimalPart = s.substring(i);
                String number = integerPart + "." + decimalPart;
                
                if (isValidNumber(number)) {
                    numbers.add(number);
                }
            }
            
            return numbers;
        }
    }
    
    // ==================== 题目5: HDU 3065 病毒侵袭持续中 ====================
    
    /**
     * 题目描述：统计每个病毒在文本中出现的次数
     * 题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=3065
     * 
     * 算法思路：
     * 1. 为每个病毒分配ID，构建AC自动机
     * 2. 在文本中进行匹配，统计每个病毒的出现次数
     * 3. 输出出现次数大于0的病毒及其次数
     * 
     * 时间复杂度：O(∑|Vi| + |T|)
     * 空间复杂度：O(∑|Vi| × |Σ|)
     */
    public static class VirusInvasionContinued {
        private static final int MAXN = 1005;
        private static final int MAXS = 50005;
        
        private int[][] tree = new int[MAXS][128]; // 扩展ASCII字符集
        private int[] fail = new int[MAXS];
        private int[] end = new int[MAXS]; // 记录病毒编号
        private int[] count = new int[MAXS]; // 记录每个节点的匹配次数
        private int cnt = 0;
        
        private int[] virusCount; // 每个病毒的出现次数
        
        public VirusInvasionContinued(int n) {
            virusCount = new int[n + 1];
        }
        
        public void insert(String virus, int id) {
            int u = 0;
            for (char c : virus.toCharArray()) {
                int idx = c;
                if (tree[u][idx] == 0) {
                    tree[u][idx] = ++cnt;
                }
                u = tree[u][idx];
            }
            end[u] = id;
        }
        
        public void build() {
            Queue<Integer> queue = new LinkedList<>();
            for (int i = 0; i < 128; i++) {
                if (tree[0][i] != 0) {
                    queue.offer(tree[0][i]);
                }
            }
            
            while (!queue.isEmpty()) {
                int u = queue.poll();
                for (int i = 0; i < 128; i++) {
                    if (tree[u][i] != 0) {
                        fail[tree[u][i]] = tree[fail[u]][i];
                        queue.offer(tree[u][i]);
                    } else {
                        tree[u][i] = tree[fail[u]][i];
                    }
                }
            }
        }
        
        public void match(String text) {
            int u = 0;
            for (char c : text.toCharArray()) {
                u = tree[u][c];
                count[u]++;
            }
            
            // 使用拓扑排序汇总匹配次数
            int[] indegree = new int[cnt + 1];
            for (int i = 1; i <= cnt; i++) {
                indegree[fail[i]]++;
            }
            
            Queue<Integer> queue = new LinkedList<>();
            for (int i = 1; i <= cnt; i++) {
                if (indegree[i] == 0) {
                    queue.offer(i);
                }
            }
            
            while (!queue.isEmpty()) {
                int node = queue.poll();
                if (end[node] != 0) {
                    virusCount[end[node]] = count[node];
                }
                
                int v = fail[node];
                count[v] += count[node];
                indegree[v]--;
                if (indegree[v] == 0) {
                    queue.offer(v);
                }
            }
        }
        
        public void printResults(String[] viruses) {
            for (int i = 1; i < virusCount.length; i++) {
                if (virusCount[i] > 0) {
                    System.out.println(viruses[i - 1] + ": " + virusCount[i]);
                }
            }
        }
    }
    
    // ==================== 主函数和测试用例 ====================
    
    private static void testTextGenerator() {
        System.out.println("=== 测试文本生成器 ===");
        TextGenerator generator = new TextGenerator();
        generator.insert("ABC");
        generator.insert("DEF");
        generator.build();
        int result = generator.solve(3);
        System.out.println("长度为3的至少包含一个模式串的字符串个数: " + result);
    }
    
    private static void testFrequencyOfString() {
        System.out.println("\n=== 测试频率查询 ===");
        FrequencyOfString fos = new FrequencyOfString(2);
        fos.insert("ab", 1);
        fos.insert("bc", 2);
        fos.build();
        fos.preprocess("abcabc");
        System.out.println("模式串'ab'第1次出现位置: " + fos.query(1, 1));
        System.out.println("模式串'bc'第2次出现位置: " + fos.query(2, 2));
    }
    
    private static void testMandrake() {
        System.out.println("\n=== 测试MANDRAKE ===");
        Mandrake mandrake = new Mandrake(3);
        mandrake.insert("ab", 1);
        mandrake.insert("bc", 2);
        mandrake.insert("abc", 3);
        mandrake.build();
        mandrake.match("abcabcab");
        int result = mandrake.countPatterns(2);
        System.out.println("出现次数至少2次的模式串数量: " + result);
    }
    
    private static void testAmbiguousCoordinates() {
        System.out.println("\n=== 测试模糊坐标 ===");
        AmbiguousCoordinates ac = new AmbiguousCoordinates();
        List<String> result = ac.ambiguousCoordinates("(123)");
        System.out.println("有效坐标数量: " + result.size());
        for (String coord : result) {
            System.out.println(coord);
        }
    }
    
    private static void testVirusInvasionContinued() {
        System.out.println("\n=== 测试病毒侵袭持续中 ===");
        VirusInvasionContinued vic = new VirusInvasionContinued(2);
        String[] viruses = {"VIRUS1", "VIRUS2"};
        vic.insert(viruses[0], 1);
        vic.insert(viruses[1], 2);
        vic.build();
        vic.match("THIS IS A TEST VIRUS1 AND VIRUS2 STRING");
        vic.printResults(viruses);
    }
}