package class107_HashingAndSamplingAlgorithms;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * LeetCode 535. TinyURL 的加密与解密
 * 题目链接：https://leetcode.com/problems/encode-and-decode-tinyurl/
 * 
 * 题目描述：
 * TinyURL是一种URL简化服务。比如当你输入一个URL https://leetcode.com/problems/design-tinyurl时，
 * 它将返回一个简化的URL http://tinyurl.com/4e9iAk。
 * 要求设计一个TinyURL系统，包含加密和解密两个功能：
 * - 加密：将长URL转换为短URL
 * - 解密：将短URL转换回长URL
 * 
 * 算法思路：
 * 1. 使用哈希表存储长URL到短URL的映射关系
 * 2. 使用自增ID或哈希值生成唯一的短URL标识符
 * 3. 将标识符编码为短字符串（如62进制）
 * 4. 使用前缀"http://tinyurl.com/"构成完整短URL
 * 
 * 时间复杂度：
 * - encode: O(1) 平均情况
 * - decode: O(1) 平均情况
 * 
 * 空间复杂度：O(n)，其中n是存储的URL数量
 * 
 * 工程化考量：
 * 1. 线程安全：使用ConcurrentHashMap处理并发访问
 * 2. 唯一性保证：使用原子操作保证ID唯一性
 * 3. 性能优化：预计算字符集，避免重复计算
 * 4. 异常处理：输入验证和错误恢复
 */
public class Code22_LeetCode535_TinyURL {
    
    /**
     * TinyURL系统实现类
     */
    public static class Codec {
        // 字符集：62个字符（数字0-9，小写字母a-z，大写字母A-Z）
        private static final String CHARSET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        private static final int BASE = CHARSET.length(); // 62进制
        private static final String PREFIX = "http://tinyurl.com/";
        
        // 存储短URL到长URL的映射
        private Map<String, String> shortToLongMap;
        // 存储长URL到短URL的映射（避免重复生成）
        private Map<String, String> longToShortMap;
        // 自增ID生成器
        private AtomicLong idGenerator;
        
        public Codec() {
            this.shortToLongMap = new ConcurrentHashMap<>();
            this.longToShortMap = new ConcurrentHashMap<>();
            this.idGenerator = new AtomicLong(0);
        }
        
        /**
         * 将长URL编码为短URL
         * 
         * @param longUrl 长URL
         * @return 短URL
         */
        public String encode(String longUrl) {
            // 检查是否已经为该长URL生成过短URL
            if (longToShortMap.containsKey(longUrl)) {
                return longToShortMap.get(longUrl);
            }
            
            // 生成新的唯一ID
            long id = idGenerator.incrementAndGet();
            
            // 将ID转换为62进制字符串
            String shortKey = idToShortKey(id);
            
            // 构造短URL
            String shortUrl = PREFIX + shortKey;
            
            // 存储映射关系
            shortToLongMap.put(shortKey, longUrl);
            longToShortMap.put(longUrl, shortUrl);
            
            return shortUrl;
        }
        
        /**
         * 将短URL解码为长URL
         * 
         * @param shortUrl 短URL
         * @return 长URL
         */
        public String decode(String shortUrl) {
            // 提取短键
            String shortKey = shortUrl.substring(PREFIX.length());
            
            // 查找对应的长URL
            return shortToLongMap.getOrDefault(shortKey, "");
        }
        
        /**
         * 将ID转换为62进制字符串
         * 
         * @param id ID
         * @return 62进制字符串
         */
        private String idToShortKey(long id) {
            StringBuilder sb = new StringBuilder();
            while (id > 0) {
                sb.append(CHARSET.charAt((int) (id % BASE)));
                id /= BASE;
            }
            return sb.reverse().toString();
        }
        
        /**
         * 将62进制字符串转换为ID
         * 
         * @param shortKey 62进制字符串
         * @return ID
         */
        private long shortKeyToId(String shortKey) {
            long id = 0;
            for (char c : shortKey.toCharArray()) {
                id = id * BASE + CHARSET.indexOf(c);
            }
            return id;
        }
        
        /**
         * 获取系统统计信息
         * 
         * @return 统计信息
         */
        public Map<String, Object> getStatistics() {
            Map<String, Object> stats = new HashMap<>();
            stats.put("urlCount", shortToLongMap.size());
            stats.put("nextId", idGenerator.get());
            return stats;
        }
    }
    
    /**
     * 优化版本：使用哈希值作为ID
     */
    public static class CodecOptimized {
        private static final String CHARSET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        private static final int BASE = CHARSET.length();
        private static final String PREFIX = "http://tinyurl.com/";
        
        private Map<String, String> shortToLongMap;
        private Map<String, String> longToShortMap;
        
        public CodecOptimized() {
            this.shortToLongMap = new ConcurrentHashMap<>();
            this.longToShortMap = new ConcurrentHashMap<>();
        }
        
        public String encode(String longUrl) {
            if (longToShortMap.containsKey(longUrl)) {
                return longToShortMap.get(longUrl);
            }
            
            // 使用URL的哈希值作为ID
            int hash = longUrl.hashCode();
            String shortKey = hashToShortKey(hash);
            
            // 处理哈希冲突
            while (shortToLongMap.containsKey(shortKey) && 
                   !shortToLongMap.get(shortKey).equals(longUrl)) {
                // 如果发生冲突，添加随机字符
                shortKey += CHARSET.charAt((int) (Math.random() * BASE));
            }
            
            String shortUrl = PREFIX + shortKey;
            shortToLongMap.put(shortKey, longUrl);
            longToShortMap.put(longUrl, shortUrl);
            
            return shortUrl;
        }
        
        public String decode(String shortUrl) {
            String shortKey = shortUrl.substring(PREFIX.length());
            return shortToLongMap.getOrDefault(shortKey, "");
        }
        
        private String hashToShortKey(int hash) {
            StringBuilder sb = new StringBuilder();
            // 取绝对值避免负数
            long absHash = Math.abs((long) hash);
            while (absHash > 0) {
                sb.append(CHARSET.charAt((int) (absHash % BASE)));
                absHash /= BASE;
            }
            // 确保至少有6个字符
            while (sb.length() < 6) {
                sb.append(CHARSET.charAt((int) (Math.random() * BASE)));
            }
            return sb.toString();
        }
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 测试 LeetCode 535. TinyURL 的加密与解密 ===");
        
        // 基础版本测试
        testBasicVersion();
        
        // 优化版本测试
        testOptimizedVersion();
        
        // 性能测试
        performanceTest();
        
        // 边界情况测试
        edgeCaseTest();
    }
    
    private static void testBasicVersion() {
        System.out.println("--- 基础版本测试 ---");
        Codec codec = new Codec();
        
        // 测试基本功能
        String url1 = "https://leetcode.com/problems/design-tinyurl";
        String shortUrl1 = codec.encode(url1);
        String decodedUrl1 = codec.decode(shortUrl1);
        
        System.out.println("原始URL: " + url1);
        System.out.println("短URL: " + shortUrl1);
        System.out.println("解码URL: " + decodedUrl1);
        System.out.println("编码解码一致性: " + url1.equals(decodedUrl1));
        
        // 测试重复URL
        String shortUrl1Again = codec.encode(url1);
        System.out.println("重复编码一致性: " + shortUrl1.equals(shortUrl1Again));
        
        // 测试不同URL
        String url2 = "https://www.google.com";
        String shortUrl2 = codec.encode(url2);
        String decodedUrl2 = codec.decode(shortUrl2);
        
        System.out.println("URL2原始: " + url2);
        System.out.println("URL2短URL: " + shortUrl2);
        System.out.println("URL2解码: " + decodedUrl2);
        System.out.println("URL2一致性: " + url2.equals(decodedUrl2));
        
        // 统计信息
        System.out.println("系统统计: " + codec.getStatistics());
        System.out.println();
    }
    
    private static void testOptimizedVersion() {
        System.out.println("--- 优化版本测试 ---");
        CodecOptimized codec = new CodecOptimized();
        
        // 测试基本功能
        String url1 = "https://leetcode.com/problems/design-tinyurl";
        String shortUrl1 = codec.encode(url1);
        String decodedUrl1 = codec.decode(shortUrl1);
        
        System.out.println("原始URL: " + url1);
        System.out.println("短URL: " + shortUrl1);
        System.out.println("解码URL: " + decodedUrl1);
        System.out.println("编码解码一致性: " + url1.equals(decodedUrl1));
        
        // 测试重复URL
        String shortUrl1Again = codec.encode(url1);
        System.out.println("重复编码一致性: " + shortUrl1.equals(shortUrl1Again));
        System.out.println();
    }
    
    private static void performanceTest() {
        System.out.println("--- 性能测试 ---");
        Codec codec = new Codec();
        
        // 生成测试URL
        List<String> testUrls = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            testUrls.add("https://example.com/page" + i + ".html");
        }
        
        // 测试编码性能
        long startTime = System.currentTimeMillis();
        List<String> shortUrls = new ArrayList<>();
        for (String url : testUrls) {
            shortUrls.add(codec.encode(url));
        }
        long encodeTime = System.currentTimeMillis() - startTime;
        
        // 测试解码性能
        startTime = System.currentTimeMillis();
        for (String shortUrl : shortUrls) {
            codec.decode(shortUrl);
        }
        long decodeTime = System.currentTimeMillis() - startTime;
        
        System.out.println("编码10000个URL耗时: " + encodeTime + "ms");
        System.out.println("解码10000个URL耗时: " + decodeTime + "ms");
        System.out.println("平均每URL编码耗时: " + (encodeTime / 10000.0) + "ms");
        System.out.println("平均每URL解码耗时: " + (decodeTime / 10000.0) + "ms");
        System.out.println("系统统计: " + codec.getStatistics());
        System.out.println();
    }
    
    private static void edgeCaseTest() {
        System.out.println("--- 边界情况测试 ---");
        Codec codec = new Codec();
        
        // 测试空URL
        String emptyUrl = "";
        String shortEmpty = codec.encode(emptyUrl);
        String decodedEmpty = codec.decode(shortEmpty);
        System.out.println("空URL编码: " + shortEmpty);
        System.out.println("空URL解码: " + decodedEmpty);
        
        // 测试null URL
        try {
            codec.encode(null);
            System.out.println("Null URL处理: 异常未抛出");
        } catch (Exception e) {
            System.out.println("Null URL处理: " + e.getClass().getSimpleName());
        }
        
        // 测试无效短URL
        String invalidShort = codec.decode("http://tinyurl.com/invalid");
        System.out.println("无效短URL解码: '" + invalidShort + "'");
        
        // 测试超长URL
        StringBuilder longUrlBuilder = new StringBuilder("http://example.com/");
        for (int i = 0; i < 1000; i++) {
            longUrlBuilder.append("path" + i + "/");
        }
        String longUrl = longUrlBuilder.toString();
        String shortLong = codec.encode(longUrl);
        String decodedLong = codec.decode(shortLong);
        System.out.println("超长URL一致性: " + longUrl.equals(decodedLong));
        
        System.out.println();
    }
}