package class107_HashingAndSamplingAlgorithms;

import java.util.*;

/**
 * Codeforces 514C. Watto and Mechanism
 * 题目链接：https://codeforces.com/problemset/problem/514/C
 * 
 * 题目描述：
 * Watto有一个包含n个字符串的数据库，每个字符串只包含字符'a', 'b', 'c'。
 * 然后有m个查询，每个查询给出一个字符串，问是否存在数据库中的某个字符串，
 * 使得两个字符串长度相同且恰好有一个位置上的字符不同。
 * 
 * 算法思路：
 * 1. 使用字符串哈希技术预处理所有数据库中的字符串
 * 2. 对于每个查询字符串，枚举每个位置，尝试将该位置的字符替换为另外两个字符
 * 3. 计算替换后的字符串哈希值，在数据库中查找是否存在匹配的哈希值
 * 4. 为了减少哈希冲突，使用双哈希技术
 * 
 * 时间复杂度：
 * - 预处理：O(n * L)，其中n是数据库中字符串数量，L是字符串平均长度
 * - 查询：O(m * L)，其中m是查询数量
 * 
 * 空间复杂度：O(n)，存储所有字符串的哈希值
 * 
 * 工程化考量：
 * 1. 哈希冲突处理：使用双哈希减少冲突概率
 * 2. 性能优化：预计算幂数组避免重复计算
 * 3. 边界情况：空字符串、单字符字符串
 * 4. 内存管理：使用HashSet快速查找
 */
public class Code16_Codeforces514C_WattoAndMechanism {
    
    // 双哈希参数
    private static final long BASE1 = 131;
    private static final long BASE2 = 13131;
    private static final long MOD1 = 1000000007;
    private static final long MOD2 = 1000000009;
    
    static class WattoMechanism {
        private Set<Long> hashes;  // 存储数据库中所有字符串的双哈希值
        private long[] pow1;       // 预计算的BASE1的幂
        private long[] pow2;       // 预计算的BASE2的幂
        private int maxLength;     // 数据库中字符串的最大长度
        
        /**
         * 初始化机制
         * 
         * @param strings 数据库中的字符串数组
         */
        public WattoMechanism(String[] strings) {
            hashes = new HashSet<>();
            maxLength = 0;
            
            // 计算最大长度
            for (String s : strings) {
                maxLength = Math.max(maxLength, s.length());
            }
            
            // 预计算幂数组
            pow1 = new long[maxLength + 1];
            pow2 = new long[maxLength + 1];
            pow1[0] = 1;
            pow2[0] = 1;
            
            for (int i = 1; i <= maxLength; i++) {
                pow1[i] = (pow1[i - 1] * BASE1) % MOD1;
                pow2[i] = (pow2[i - 1] * BASE2) % MOD2;
            }
            
            // 计算并存储所有字符串的哈希值
            for (String s : strings) {
                long hash = computeHash(s);
                hashes.add(hash);
            }
        }
        
        /**
         * 计算字符串的双哈希值
         * 
         * @param s 输入字符串
         * @return 双哈希值
         */
        private long computeHash(String s) {
            long hash1 = 0, hash2 = 0;
            for (int i = 0; i < s.length(); i++) {
                hash1 = (hash1 * BASE1 + s.charAt(i)) % MOD1;
                hash2 = (hash2 * BASE2 + s.charAt(i)) % MOD2;
            }
            return hash1 * MOD2 + hash2;  // 组合两个哈希值
        }
        
        /**
         * 查询是否存在满足条件的字符串
         * 
         * @param query 查询字符串
         * @return 如果存在满足条件的字符串返回true，否则返回false
         */
        public boolean query(String query) {
            int n = query.length();
            
            // 预计算查询字符串的前缀哈希值
            long[] prefixHash1 = new long[n + 1];
            long[] prefixHash2 = new long[n + 1];
            
            for (int i = 1; i <= n; i++) {
                prefixHash1[i] = (prefixHash1[i - 1] * BASE1 + query.charAt(i - 1)) % MOD1;
                prefixHash2[i] = (prefixHash2[i - 1] * BASE2 + query.charAt(i - 1)) % MOD2;
            }
            
            // 枚举每个位置，尝试替换字符
            for (int i = 0; i < n; i++) {
                char originalChar = query.charAt(i);
                
                // 尝试替换为其他两个字符
                for (char c = 'a'; c <= 'c'; c++) {
                    if (c == originalChar) continue;
                    
                    // 计算替换后的字符串哈希值
                    long newHash1 = (prefixHash1[i] * BASE1 + c) % MOD1;
                    long newHash2 = (prefixHash2[i] * BASE2 + c) % MOD2;
                    
                    // 加上后缀部分
                    if (i + 1 < n) {
                        newHash1 = (newHash1 * pow1[n - i - 1] + (prefixHash1[n] - prefixHash1[i + 1] * pow1[n - i - 1] % MOD1 + MOD1) % MOD1) % MOD1;
                        newHash2 = (newHash2 * pow2[n - i - 1] + (prefixHash2[n] - prefixHash2[i + 1] * pow2[n - i - 1] % MOD2 + MOD2) % MOD2) % MOD2;
                    }
                    
                    long combinedHash = newHash1 * MOD2 + newHash2;
                    
                    // 检查哈希值是否存在于数据库中
                    if (hashes.contains(combinedHash)) {
                        return true;
                    }
                }
            }
            
            return false;
        }
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 测试 Codeforces 514C. Watto and Mechanism ===");
        
        // 测试用例1
        String[] database1 = {"abc", "bcd", "cde"};
        WattoMechanism wm1 = new WattoMechanism(database1);
        
        System.out.println("数据库: " + Arrays.toString(database1));
        System.out.println("查询 'aac': " + wm1.query("aac")); // 期望: true (与"abc"差一个字符)
        System.out.println("查询 'def': " + wm1.query("def")); // 期望: true (与"cde"差一个字符)
        System.out.println("查询 'xyz': " + wm1.query("xyz")); // 期望: false (与所有字符串差超过一个字符)
        System.out.println();
        
        // 测试用例2
        String[] database2 = {"aaaa", "bbbb", "cccc"};
        WattoMechanism wm2 = new WattoMechanism(database2);
        
        System.out.println("数据库: " + Arrays.toString(database2));
        System.out.println("查询 'aaab': " + wm2.query("aaab")); // 期望: true (与"aaaa"差一个字符)
        System.out.println("查询 'bbbc': " + wm2.query("bbbc")); // 期望: true (与"bbbb"差一个字符)
        System.out.println("查询 'ccca': " + wm2.query("ccca")); // 期望: true (与"cccc"差一个字符)
        System.out.println("查询 'abcd': " + wm2.query("abcd")); // 期望: false (与所有字符串差超过一个字符)
        System.out.println();
        
        // 边界情况测试
        String[] database3 = {"a", "b", "c"};
        WattoMechanism wm3 = new WattoMechanism(database3);
        
        System.out.println("数据库: " + Arrays.toString(database3));
        System.out.println("查询 'b': " + wm3.query("b")); // 期望: false (完全相同)
        System.out.println("查询 'd': " + wm3.query("d")); // 期望: true (与"c"差一个字符)
        System.out.println();
        
        // 性能测试
        System.out.println("=== 性能测试 ===");
        List<String> largeDatabase = new ArrayList<>();
        Random random = new Random();
        
        // 生成大量随机字符串
        for (int i = 0; i < 10000; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < 10; j++) {
                sb.append((char) ('a' + random.nextInt(3))); // 只使用a,b,c
            }
            largeDatabase.add(sb.toString());
        }
        
        String[] largeArray = largeDatabase.toArray(new String[0]);
        long startTime = System.currentTimeMillis();
        WattoMechanism wmLarge = new WattoMechanism(largeArray);
        long initTime = System.currentTimeMillis() - startTime;
        
        // 执行大量查询
        startTime = System.currentTimeMillis();
        int queryCount = 0;
        for (int i = 0; i < 1000; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < 10; j++) {
                sb.append((char) ('a' + random.nextInt(3)));
            }
            if (wmLarge.query(sb.toString())) {
                queryCount++;
            }
        }
        long queryTime = System.currentTimeMillis() - startTime;
        
        System.out.println("10000个字符串初始化耗时: " + initTime + "ms");
        System.out.println("1000次查询耗时: " + queryTime + "ms");
        System.out.println("找到匹配的查询数量: " + queryCount);
        
        System.out.println("所有测试完成");
    }
}