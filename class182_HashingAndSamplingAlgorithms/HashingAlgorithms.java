package class_advanced_algorithms.hashing;

import java.util.*;

/**
 * 哈希算法实现
 * 
 * 包括双模哈希、三模哈希、前缀哈希等实现
 * 
 * 哈希算法在计算机科学中有着广泛的应用，包括：
 * 1. 数据结构：哈希表、布隆过滤器
 * 2. 密码学：数字签名、消息认证
 * 3. 数据完整性：校验和、数字指纹
 * 4. 数据库：索引、分区
 * 5. 网络：负载均衡、缓存
 */
public class HashingAlgorithms {
    
    // 常用的大质数，用于哈希计算
    private static final long MOD1 = 1000000007L;  // 10^9 + 7
    private static final long MOD2 = 1000000009L;  // 10^9 + 9
    private static final long MOD3 = 998244353L;   // 常用的NTT模数
    private static final long BASE = 31L;          // 哈希基数
    
    /**
     * 单模哈希
     * 使用单个大质数作为模数
     */
    public static class SingleHash {
        private long[] hash;
        private long[] pow;
        private long mod;
        
        public SingleHash(String s, long mod) {
            this.mod = mod;
            int n = s.length();
            hash = new long[n + 1];
            pow = new long[n + 1];
            
            // 预计算幂次
            pow[0] = 1;
            for (int i = 1; i <= n; i++) {
                pow[i] = (pow[i - 1] * BASE) % mod;
            }
            
            // 计算前缀哈希
            for (int i = 0; i < n; i++) {
                hash[i + 1] = (hash[i] * BASE + (s.charAt(i) - 'a' + 1)) % mod;
            }
        }
        
        /**
         * 获取子串的哈希值
         * @param l 左边界（包含）
         * @param r 右边界（包含）
         * @return 子串哈希值
         */
        public long getHash(int l, int r) {
            long result = (hash[r + 1] - hash[l] * pow[r - l + 1]) % mod;
            if (result < 0) result += mod;
            return result;
        }
    }
    
    /**
     * 双模哈希
     * 使用两个大质数作为模数，降低哈希碰撞概率
     */
    public static class DoubleHash {
        private SingleHash hash1;
        private SingleHash hash2;
        
        public DoubleHash(String s) {
            hash1 = new SingleHash(s, MOD1);
            hash2 = new SingleHash(s, MOD2);
        }
        
        /**
         * 获取子串的双模哈希值
         * @param l 左边界（包含）
         * @param r 右边界（包含）
         * @return 子串双模哈希值（两个值组成的数组）
         */
        public long[] getHash(int l, int r) {
            return new long[]{hash1.getHash(l, r), hash2.getHash(l, r)};
        }
        
        /**
         * 比较两个子串是否相等
         * @param l1 第一个子串左边界
         * @param r1 第一个子串右边界
         * @param l2 第二个子串左边界
         * @param r2 第二个子串右边界
         * @return 是否相等
         */
        public boolean equals(int l1, int r1, int l2, int r2) {
            long[] hash1 = getHash(l1, r1);
            long[] hash2 = getHash(l2, r2);
            return hash1[0] == hash2[0] && hash1[1] == hash2[1];
        }
    }
    
    /**
     * 三模哈希
     * 使用三个大质数作为模数，进一步降低哈希碰撞概率
     */
    public static class TripleHash {
        private SingleHash hash1;
        private SingleHash hash2;
        private SingleHash hash3;
        
        public TripleHash(String s) {
            hash1 = new SingleHash(s, MOD1);
            hash2 = new SingleHash(s, MOD2);
            hash3 = new SingleHash(s, MOD3);
        }
        
        /**
         * 获取子串的三模哈希值
         * @param l 左边界（包含）
         * @param r 右边界（包含）
         * @return 子串三模哈希值（三个值组成的数组）
         */
        public long[] getHash(int l, int r) {
            return new long[]{hash1.getHash(l, r), hash2.getHash(l, r), hash3.getHash(l, r)};
        }
        
        /**
         * 比较两个子串是否相等
         * @param l1 第一个子串左边界
         * @param r1 第一个子串右边界
         * @param l2 第二个子串左边界
         * @param r2 第二个子串右边界
         * @return 是否相等
         */
        public boolean equals(int l1, int r1, int l2, int r2) {
            long[] hash1 = getHash(l1, r1);
            long[] hash2 = getHash(l2, r2);
            return hash1[0] == hash2[0] && hash1[1] == hash2[1] && hash1[2] == hash2[2];
        }
    }
    
    /**
     * 计算哈希碰撞概率
     * @param mod 模数
     * @param n 字符串数量
     * @return 碰撞概率
     */
    public static double collisionProbability(long mod, long n) {
        // 使用生日悖论近似计算
        // P(碰撞) ≈ 1 - e^(-n*(n-1)/(2*mod))
        if (mod <= 0 || n <= 1) return 0.0;
        double exponent = -((double) n * (n - 1)) / (2 * mod);
        return 1.0 - Math.exp(exponent);
    }
    
    /**
     * 处理无符号整数溢出
     * @param value 可能溢出的值
     * @param mod 模数
     * @return 正确的模运算结果
     */
    public static long handleOverflow(long value, long mod) {
        value %= mod;
        if (value < 0) value += mod;
        return value;
    }
    
    /**
     * 持久化前缀哈希
     * 支持历史版本查询的前缀哈希
     */
    public static class PersistentPrefixHash {
        private List<long[]> hashes; // 每个版本的哈希值
        private List<long[]> powers; // 每个版本的幂次值
        private long mod;
        
        public PersistentPrefixHash(long mod) {
            this.mod = mod;
            this.hashes = new ArrayList<>();
            this.powers = new ArrayList<>();
            // 初始化空版本
            hashes.add(new long[]{0});
            powers.add(new long[]{1});
        }
        
        /**
         * 在指定版本后添加字符
         * @param version 版本号
         * @param c 添加的字符
         * @return 新版本号
         */
        public int append(int version, char c) {
            long[] prevHash = hashes.get(version);
            long[] prevPow = powers.get(version);
            int n = prevHash.length;
            
            // 创建新版本
            long[] newHash = new long[n + 1];
            long[] newPow = new long[n + 1];
            
            // 复制之前的内容
            System.arraycopy(prevHash, 0, newHash, 0, n);
            System.arraycopy(prevPow, 0, newPow, 0, n);
            
            // 计算新添加的字符的哈希
            newPow[n] = (newPow[n - 1] * BASE) % mod;
            newHash[n] = (newHash[n - 1] * BASE + (c - 'a' + 1)) % mod;
            
            hashes.add(newHash);
            powers.add(newPow);
            
            return hashes.size() - 1;
        }
        
        /**
         * 获取指定版本中子串的哈希值
         * @param version 版本号
         * @param l 左边界（包含）
         * @param r 右边界（包含）
         * @return 子串哈希值
         */
        public long getHash(int version, int l, int r) {
            long[] hash = hashes.get(version);
            long[] pow = powers.get(version);
            long result = (hash[r + 1] - hash[l] * pow[r - l + 1]) % mod;
            if (result < 0) result += mod;
            return result;
        }
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试字符串
        String test = "ababababab";
        System.out.println("测试字符串: " + test);
        
        // 单模哈希测试
        System.out.println("\n=== 单模哈希测试 ===");
        SingleHash singleHash = new SingleHash(test, MOD1);
        System.out.println("子串[0,1]的哈希值: " + singleHash.getHash(0, 1));
        System.out.println("子串[2,3]的哈希值: " + singleHash.getHash(2, 3));
        System.out.println("子串[4,5]的哈希值: " + singleHash.getHash(4, 5));
        
        // 双模哈希测试
        System.out.println("\n=== 双模哈希测试 ===");
        DoubleHash doubleHash = new DoubleHash(test);
        long[] hash1 = doubleHash.getHash(0, 1);
        long[] hash2 = doubleHash.getHash(2, 3);
        long[] hash3 = doubleHash.getHash(4, 5);
        System.out.println("子串[0,1]的双模哈希值: [" + hash1[0] + ", " + hash1[1] + "]");
        System.out.println("子串[2,3]的双模哈希值: [" + hash2[0] + ", " + hash2[1] + "]");
        System.out.println("子串[4,5]的双模哈希值: [" + hash3[0] + ", " + hash3[1] + "]");
        System.out.println("子串[0,1]和[2,3]是否相等: " + doubleHash.equals(0, 1, 2, 3));
        System.out.println("子串[0,1]和[4,5]是否相等: " + doubleHash.equals(0, 1, 4, 5));
        
        // 三模哈希测试
        System.out.println("\n=== 三模哈希测试 ===");
        TripleHash tripleHash = new TripleHash(test);
        long[] thash1 = tripleHash.getHash(0, 1);
        long[] thash2 = tripleHash.getHash(2, 3);
        System.out.println("子串[0,1]的三模哈希值: [" + thash1[0] + ", " + thash1[1] + ", " + thash1[2] + "]");
        System.out.println("子串[2,3]的三模哈希值: [" + thash2[0] + ", " + thash2[1] + ", " + thash2[2] + "]");
        System.out.println("子串[0,1]和[2,3]是否相等: " + tripleHash.equals(0, 1, 2, 3));
        
        // 哈希碰撞概率测试
        System.out.println("\n=== 哈希碰撞概率测试 ===");
        long n1 = 1000000; // 100万个字符串
        System.out.println("使用模数 " + MOD1 + " 时，" + n1 + " 个字符串的碰撞概率: " + 
                          String.format("%.10f", collisionProbability(MOD1, n1)));
        System.out.println("使用模数 " + MOD2 + " 时，" + n1 + " 个字符串的碰撞概率: " + 
                          String.format("%.10f", collisionProbability(MOD2, n1)));
        System.out.println("使用模数 " + MOD3 + " 时，" + n1 + " 个字符串的碰撞概率: " + 
                          String.format("%.10f", collisionProbability(MOD3, n1)));
        
        // 持久化前缀哈希测试
        System.out.println("\n=== 持久化前缀哈希测试 ===");
        PersistentPrefixHash persistentHash = new PersistentPrefixHash(MOD1);
        int version0 = 0;
        int version1 = persistentHash.append(version0, 'a');
        int version2 = persistentHash.append(version1, 'b');
        int version3 = persistentHash.append(version2, 'a');
        
        System.out.println("版本0的哈希值: " + persistentHash.getHash(version0, 0, 0)); // 空字符串
        System.out.println("版本1的前缀[0,0]哈希值: " + persistentHash.getHash(version1, 0, 0)); // "a"
        System.out.println("版本2的前缀[0,1]哈希值: " + persistentHash.getHash(version2, 0, 1)); // "ab"
        System.out.println("版本3的前缀[0,2]哈希值: " + persistentHash.getHash(version3, 0, 2)); // "aba"
    }
}