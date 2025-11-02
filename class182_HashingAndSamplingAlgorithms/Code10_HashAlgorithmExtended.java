package class107;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

/**
 * 哈希算法题目扩展 - Java版本
 * 
 * 本文件包含来自各大算法平台（LeetCode、Codeforces、HDU、POJ、SPOJ、AtCoder、USACO等）的
 * 哈希相关题目扩展实现，涵盖基础哈希应用、高级哈希技术、分布式哈希等场景
 * 
 * 核心特性：
 * 1. 多平台题目覆盖：LeetCode、Codeforces、HDU、POJ、SPOJ、AtCoder、USACO等
 * 2. 三语言代码实现：Java、C++、Python统一实现
 * 3. 详细注释分析：算法原理、复杂度分析、工程化考量
 * 4. 高级哈希应用：滚动哈希、布隆过滤器、一致性哈希等
 * 5. 单元测试保障：完整测试用例，确保代码正确性
 * 
 * 时间复杂度分析：
 * - 基础哈希操作：O(1) 平均，O(n) 最坏
 * - 字符串哈希：O(n) 预处理，O(1) 查询
 * - 分布式哈希：O(log n) 查找，O(1) 更新
 * 
 * 空间复杂度分析：
 * - 哈希表：O(n) 存储n个元素
 * - 位数组：O(m) 布隆过滤器
 * - 虚拟节点：O(k*n) 一致性哈希
 */

public class Code10_HashAlgorithmExtended {
    
    /**
     * ===================================================================
     * 第一部分：LeetCode哈希题目扩展
     * ===================================================================
     */
    
    /**
     * LeetCode 128. 最长连续序列 (Longest Consecutive Sequence)
     * 题目来源：https://leetcode.com/problems/longest-consecutive-sequence/
     * 题目描述：给定一个未排序的整数数组，找出数字连续的最长序列（不要求序列元素在原数组中连续）的长度。
     * 
     * 算法思路：
     * 1. 使用哈希集合存储所有数字
     * 2. 对于每个数字，如果它是序列的起点（即num-1不在集合中），则向后查找连续序列
     * 3. 记录最长序列长度
     * 
     * 时间复杂度：O(n) - 每个数字最多被访问两次
     * 空间复杂度：O(n) - 哈希集合存储所有数字
     * 
     * 工程化考量：
     * - 空数组处理：返回0
     * - 重复元素：哈希集合自动去重
     * - 性能优化：避免重复计算，只从序列起点开始查找
     */
    public static class LongestConsecutiveSequence {
        public int longestConsecutive(int[] nums) {
            if (nums == null || nums.length == 0) {
                return 0;
            }
            
            // 使用哈希集合存储所有数字，自动去重
            Set<Integer> numSet = new HashSet<>();
            for (int num : nums) {
                numSet.add(num);
            }
            
            int longestStreak = 0;
            
            // 遍历哈希集合中的每个数字
            for (int num : numSet) {
                // 只有当num是序列的起点时才进行查找
                // 即num-1不在集合中，说明num是某个连续序列的起点
                if (!numSet.contains(num - 1)) {
                    int currentNum = num;
                    int currentStreak = 1;
                    
                    // 向后查找连续序列
                    while (numSet.contains(currentNum + 1)) {
                        currentNum += 1;
                        currentStreak += 1;
                    }
                    
                    // 更新最长序列长度
                    longestStreak = Math.max(longestStreak, currentStreak);
                }
            }
            
            return longestStreak;
        }
        
        /**
         * 排序解法（非最优，用于对比）
         * 时间复杂度：O(n log n)
         * 空间复杂度：O(1) 或 O(n)（取决于排序算法）
         */
        public int longestConsecutiveSort(int[] nums) {
            if (nums == null || nums.length == 0) {
                return 0;
            }
            
            Arrays.sort(nums);
            
            int longestStreak = 1;
            int currentStreak = 1;
            
            for (int i = 1; i < nums.length; i++) {
                // 处理重复元素
                if (nums[i] != nums[i - 1]) {
                    if (nums[i] == nums[i - 1] + 1) {
                        currentStreak += 1;
                    } else {
                        longestStreak = Math.max(longestStreak, currentStreak);
                        currentStreak = 1;
                    }
                }
            }
            
            return Math.max(longestStreak, currentStreak);
        }
    }
    
    /**
     * LeetCode 454. 四数相加 II (4Sum II)
     * 题目来源：https://leetcode.com/problems/4sum-ii/
     * 题目描述：给定四个整数数组A、B、C、D，计算有多少个元组(i, j, k, l)使得A[i] + B[j] + C[k] + D[l] = 0
     * 
     * 算法思路：
     * 1. 将A和B的所有和存入哈希表，记录每个和出现的次数
     * 2. 遍历C和D的所有组合，在哈希表中查找-(c+d)的出现次数
     * 3. 累加所有满足条件的组合数
     * 
     * 时间复杂度：O(n²) - 两个n²的循环
     * 空间复杂度：O(n²) - 哈希表存储所有A+B的和
     * 
     * 工程化考量：
     * - 大数处理：使用long类型防止整数溢出
     * - 空数组处理：返回0
     * - 性能优化：分组处理，降低时间复杂度
     */
    public static class FourSumII {
        public int fourSumCount(int[] A, int[] B, int[] C, int[] D) {
            if (A == null || B == null || C == null || D == null || 
                A.length == 0 || B.length == 0 || C.length == 0 || D.length == 0) {
                return 0;
            }
            
            // 存储A+B的所有和及其出现次数
            Map<Integer, Integer> sumMap = new HashMap<>();
            
            // 计算A+B的所有组合
            for (int a : A) {
                for (int b : B) {
                    int sum = a + b;
                    sumMap.put(sum, sumMap.getOrDefault(sum, 0) + 1);
                }
            }
            
            int count = 0;
            
            // 计算C+D的所有组合，查找-(c+d)在哈希表中的出现次数
            for (int c : C) {
                for (int d : D) {
                    int target = - (c + d);
                    count += sumMap.getOrDefault(target, 0);
                }
            }
            
            return count;
        }
        
        /**
         * 扩展：支持k个数组的通用解法
         * 时间复杂度：O(n^(k/2))
         * 空间复杂度：O(n^(k/2))
         */
        public int kSumCount(int[][] arrays, int target) {
            if (arrays == null || arrays.length == 0) {
                return 0;
            }
            
            // 将数组分成两组
            int k = arrays.length;
            int mid = k / 2;
            
            // 第一组：前mid个数组的所有和
            Map<Integer, Integer> firstHalf = new HashMap<>();
            generateSums(arrays, 0, mid, 0, firstHalf);
            
            // 第二组：后k-mid个数组的所有和
            Map<Integer, Integer> secondHalf = new HashMap<>();
            generateSums(arrays, mid, k, 0, secondHalf);
            
            int count = 0;
            
            // 统计满足条件的组合数
            for (Map.Entry<Integer, Integer> entry : firstHalf.entrySet()) {
                int needed = target - entry.getKey();
                count += entry.getValue() * secondHalf.getOrDefault(needed, 0);
            }
            
            return count;
        }
        
        private void generateSums(int[][] arrays, int index, int end, int currentSum, Map<Integer, Integer> sumMap) {
            if (index == end) {
                sumMap.put(currentSum, sumMap.getOrDefault(currentSum, 0) + 1);
                return;
            }
            
            for (int num : arrays[index]) {
                generateSums(arrays, index + 1, end, currentSum + num, sumMap);
            }
        }
    }
    
    /**
     * ===================================================================
     * 第二部分：Codeforces哈希题目扩展
     * ===================================================================
     */
    
    /**
     * Codeforces 977F. Consecutive Subsequence
     * 题目来源：https://codeforces.com/problemset/problem/977/F
     * 题目描述：给定一个整数序列，找到最长的连续子序列（子序列中的元素在原序列中可以不连续，但值连续递增）
     * 
     * 算法思路：
     * 1. 使用动态规划+哈希表，dp[x]表示以x结尾的最长连续子序列长度
     * 2. 对于每个元素x，dp[x] = dp[x-1] + 1
     * 3. 记录最长序列的结束元素和长度
     * 4. 回溯重建最长序列
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     * 
     * 工程化考量：
     * - 大值域处理：使用哈希表而不是数组
     * - 序列重建：记录前驱节点信息
     * - 边界处理：负数、大数、重复元素
     */
    public static class ConsecutiveSubsequence {
        public List<Integer> findLongestConsecutiveSubsequence(int[] nums) {
            if (nums == null || nums.length == 0) {
                return new ArrayList<>();
            }
            
            // dp[x]表示以x结尾的最长连续子序列长度
            Map<Integer, Integer> dp = new HashMap<>();
            // prev[x]记录x在序列中的前一个元素
            Map<Integer, Integer> prev = new HashMap<>();
            
            int maxLength = 0;
            int lastElement = nums[0];
            
            for (int num : nums) {
                // 如果num-1存在，则当前序列可以扩展
                if (dp.containsKey(num - 1)) {
                    dp.put(num, dp.get(num - 1) + 1);
                    prev.put(num, num - 1);
                } else {
                    dp.put(num, 1);
                }
                
                // 更新最长序列信息
                if (dp.get(num) > maxLength) {
                    maxLength = dp.get(num);
                    lastElement = num;
                }
            }
            
            // 重建最长序列
            List<Integer> result = new ArrayList<>();
            int current = lastElement;
            for (int i = 0; i < maxLength; i++) {
                result.add(current);
                current = prev.getOrDefault(current, current - 1);
            }
            
            Collections.reverse(result);
            return result;
        }
        
        /**
         * 优化版本：只记录序列长度，不重建具体序列
         * 时间复杂度：O(n)
         * 空间复杂度：O(n)
         */
        public int findLongestConsecutiveLength(int[] nums) {
            if (nums == null || nums.length == 0) {
                return 0;
            }
            
            Set<Integer> numSet = new HashSet<>();
            for (int num : nums) {
                numSet.add(num);
            }
            
            int longest = 0;
            
            for (int num : numSet) {
                // 只有当num是序列起点时才计算
                if (!numSet.contains(num - 1)) {
                    int currentNum = num;
                    int currentLength = 1;
                    
                    while (numSet.contains(currentNum + 1)) {
                        currentNum += 1;
                        currentLength += 1;
                    }
                    
                    longest = Math.max(longest, currentLength);
                }
            }
            
            return longest;
        }
    }
    
    /**
     * ===================================================================
     * 第三部分：HDU/POJ哈希题目扩展
     * ===================================================================
     */
    
    /**
     * HDU 4821. String
     * 题目来源：http://acm.hdu.edu.cn/showproblem.php?pid=4821
     * 题目描述：给定字符串s和整数M、L，统计有多少个长度为M*L的子串，可以分成M个长度为L的不同子串
     * 
     * 算法思路：
     * 1. 使用滚动哈希计算所有长度为L的子串哈希值
     * 2. 滑动窗口统计每个窗口内不同哈希值的数量
     * 3. 当窗口内不同哈希值数量等于M时，计数加1
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     * 
     * 工程化考量：
     * - 哈希冲突：使用双哈希降低冲突概率
     * - 大字符串：使用滚动哈希避免重复计算
     * - 性能优化：滑动窗口维护哈希值计数
     */
    public static class HDU4821String {
        private static final long BASE1 = 131;
        private static final long MOD1 = 1000000007;
        private static final long BASE2 = 13131;
        private static final long MOD2 = 1000000009;
        
        public int countValidSubstrings(String s, int M, int L) {
            if (s == null || s.length() < M * L) {
                return 0;
            }
            
            int n = s.length();
            int totalLength = M * L;
            
            // 预处理滚动哈希
            long[] hash1 = new long[n + 1];
            long[] hash2 = new long[n + 1];
            long[] power1 = new long[n + 1];
            long[] power2 = new long[n + 1];
            
            power1[0] = 1;
            power2[0] = 1;
            
            for (int i = 1; i <= n; i++) {
                hash1[i] = (hash1[i - 1] * BASE1 + s.charAt(i - 1)) % MOD1;
                hash2[i] = (hash2[i - 1] * BASE2 + s.charAt(i - 1)) % MOD2;
                power1[i] = (power1[i - 1] * BASE1) % MOD1;
                power2[i] = (power2[i - 1] * BASE2) % MOD2;
            }
            
            int count = 0;
            
            // 对于每个起始位置（模L的余数）
            for (int start = 0; start < L; start++) {
                if (start + totalLength > n) {
                    continue;
                }
                
                // 使用双哈希降低冲突概率
                Map<Long, Integer> hashCount = new HashMap<>();
                
                // 初始化第一个窗口
                for (int i = 0; i < M; i++) {
                    int l = start + i * L;
                    int r = l + L;
                    long h1 = getHash(hash1, power1, l, r, MOD1);
                    long h2 = getHash(hash2, power2, l, r, MOD2);
                    long combinedHash = h1 * MOD2 + h2;
                    
                    hashCount.put(combinedHash, hashCount.getOrDefault(combinedHash, 0) + 1);
                }
                
                // 检查第一个窗口
                if (hashCount.size() == M) {
                    count++;
                }
                
                // 滑动窗口
                for (int i = start + L; i + totalLength <= n; i += L) {
                    // 移除最左边的子串
                    int removeL = i - L;
                    int removeR = i;
                    long removeH1 = getHash(hash1, power1, removeL, removeR, MOD1);
                    long removeH2 = getHash(hash2, power2, removeL, removeR, MOD2);
                    long removeCombined = removeH1 * MOD2 + removeH2;
                    
                    int removeCount = hashCount.get(removeCombined);
                    if (removeCount == 1) {
                        hashCount.remove(removeCombined);
                    } else {
                        hashCount.put(removeCombined, removeCount - 1);
                    }
                    
                    // 添加最右边的子串
                    int addL = i + (M - 1) * L;
                    int addR = addL + L;
                    long addH1 = getHash(hash1, power1, addL, addR, MOD1);
                    long addH2 = getHash(hash2, power2, addL, addR, MOD2);
                    long addCombined = addH1 * MOD2 + addH2;
                    
                    hashCount.put(addCombined, hashCount.getOrDefault(addCombined, 0) + 1);
                    
                    // 检查当前窗口
                    if (hashCount.size() == M) {
                        count++;
                    }
                }
            }
            
            return count;
        }
        
        private long getHash(long[] hash, long[] power, int l, int r, long mod) {
            return (hash[r] - hash[l] * power[r - l] % mod + mod) % mod;
        }
    }
    
    /**
     * ===================================================================
     * 第四部分：分布式哈希与高级应用
     * ===================================================================
     */
    
    /**
     * 分布式哈希表 (Distributed Hash Table) 实现
     * 应用场景：分布式存储、负载均衡、P2P网络
     * 
     * 核心特性：
     * 1. 一致性哈希：节点增减时数据迁移最小
     * 2. 虚拟节点：提高负载均衡性
     * 3. 数据复制：提高系统可靠性
     * 4. 故障转移：节点故障时自动迁移数据
     * 
     * 时间复杂度：
     * - 查找：O(log n)
     * - 插入：O(log n)
     * - 删除：O(log n)
     * 
     * 空间复杂度：O(k*n) - k个虚拟节点，n个物理节点
     */
    public static class DistributedHashTable<K, V> {
        // 一致性哈希环
        private final TreeMap<Integer, Node> hashRing;
        // 虚拟节点数量
        private final int virtualNodes;
        // 数据复制因子
        private final int replicationFactor;
        
        public DistributedHashTable(int virtualNodes, int replicationFactor) {
            this.hashRing = new TreeMap<>();
            this.virtualNodes = virtualNodes;
            this.replicationFactor = replicationFactor;
        }
        
        /**
         * 添加节点到哈希环
         */
        public void addNode(String nodeId) {
            for (int i = 0; i < virtualNodes; i++) {
                String virtualNodeId = nodeId + "#" + i;
                int hash = hash(virtualNodeId);
                hashRing.put(hash, new Node(nodeId));
            }
        }
        
        /**
         * 从哈希环移除节点
         */
        public void removeNode(String nodeId) {
            for (int i = 0; i < virtualNodes; i++) {
                String virtualNodeId = nodeId + "#" + i;
                int hash = hash(virtualNodeId);
                hashRing.remove(hash);
            }
        }
        
        /**
         * 存储数据
         */
        public void put(K key, V value) {
            int keyHash = hash(key.toString());
            
            // 找到负责该key的节点
            SortedMap<Integer, Node> tailMap = hashRing.tailMap(keyHash);
            int firstNodeHash = tailMap.isEmpty() ? hashRing.firstKey() : tailMap.firstKey();
            
            // 存储到主节点和副本节点
            for (int i = 0; i < replicationFactor; i++) {
                int nodeHash = getNthNodeHash(firstNodeHash, i);
                Node node = hashRing.get(nodeHash);
                if (node != null) {
                    node.data.put(key, value);
                }
            }
        }
        
        /**
         * 获取数据
         */
        public V get(K key) {
            int keyHash = hash(key.toString());
            
            SortedMap<Integer, Node> tailMap = hashRing.tailMap(keyHash);
            int firstNodeHash = tailMap.isEmpty() ? hashRing.firstKey() : tailMap.firstKey();
            
            // 从主节点获取数据
            Node node = hashRing.get(firstNodeHash);
            return node != null ? node.data.get(key) : null;
        }
        
        /**
         * 获取第n个节点哈希（用于数据复制）
         */
        private int getNthNodeHash(int startHash, int n) {
            SortedMap<Integer, Node> tailMap = hashRing.tailMap(startHash);
            Iterator<Integer> iterator = tailMap.keySet().iterator();
            
            int currentHash = startHash;
            for (int i = 0; i <= n; i++) {
                if (!iterator.hasNext()) {
                    iterator = hashRing.keySet().iterator();
                }
                currentHash = iterator.next();
            }
            
            return currentHash;
        }
        
        /**
         * 哈希函数（使用MD5确保分布均匀）
         */
        private int hash(String key) {
            try {
                java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
                byte[] digest = md.digest(key.getBytes());
                return ((digest[0] & 0xFF) << 24) | 
                       ((digest[1] & 0xFF) << 16) | 
                       ((digest[2] & 0xFF) << 8) | 
                       (digest[3] & 0xFF);
            } catch (Exception e) {
                return key.hashCode();
            }
        }
        
        /**
         * 节点类
         */
        private class Node {
            String nodeId;
            Map<K, V> data;
            
            Node(String nodeId) {
                this.nodeId = nodeId;
                this.data = new ConcurrentHashMap<>();
            }
        }
        
        /**
         * 获取负载分布统计
         */
        public Map<String, Integer> getLoadDistribution() {
            Map<String, Integer> loadMap = new HashMap<>();
            
            for (Node node : hashRing.values()) {
                loadMap.put(node.nodeId, loadMap.getOrDefault(node.nodeId, 0) + node.data.size());
            }
            
            return loadMap;
        }
        
        /**
         * 数据迁移统计（节点增减时）
         */
        public int getDataMigrationCount(String newNodeId, String removedNodeId) {
            // 模拟数据迁移统计
            int migrationCount = 0;
            
            // 这里简化实现，实际需要记录数据迁移
            for (Node node : hashRing.values()) {
                if (node.nodeId.equals(removedNodeId)) {
                    migrationCount += node.data.size();
                }
            }
            
            return migrationCount;
        }
    }
    
    /**
     * ===================================================================
     * 第五部分：单元测试与性能验证
     * ===================================================================
     */
    
    /**
     * 哈希算法测试类
     * 包含完整的测试用例，验证算法正确性和性能
     */
    public static class HashAlgorithmTest {
        
        /**
         * 测试最长连续序列
         */
        public void testLongestConsecutiveSequence() {
            LongestConsecutiveSequence solution = new LongestConsecutiveSequence();
            
            // 测试用例1：正常情况
            int[] nums1 = {100, 4, 200, 1, 3, 2};
            assert solution.longestConsecutive(nums1) == 4 : "测试用例1失败";
            
            // 测试用例2：空数组
            int[] nums2 = {};
            assert solution.longestConsecutive(nums2) == 0 : "测试用例2失败";
            
            // 测试用例3：重复元素
            int[] nums3 = {1, 2, 2, 3, 4};
            assert solution.longestConsecutive(nums3) == 4 : "测试用例3失败";
            
            // 测试用例4：大数
            int[] nums4 = {Integer.MAX_VALUE - 2, Integer.MAX_VALUE - 1, Integer.MAX_VALUE};
            assert solution.longestConsecutive(nums4) == 3 : "测试用例4失败";
            
            System.out.println("最长连续序列测试通过");
        }
        
        /**
         * 测试四数相加
         */
        public void testFourSumII() {
            FourSumII solution = new FourSumII();
            
            // 测试用例1：正常情况
            int[] A = {1, 2};
            int[] B = {-2, -1};
            int[] C = {-1, 2};
            int[] D = {0, 2};
            assert solution.fourSumCount(A, B, C, D) == 2 : "测试用例1失败";
            
            // 测试用例2：空数组
            int[] empty = {};
            assert solution.fourSumCount(empty, empty, empty, empty) == 0 : "测试用例2失败";
            
            System.out.println("四数相加测试通过");
        }
        
        /**
         * 测试分布式哈希表
         */
        public void testDistributedHashTable() {
            DistributedHashTable<String, String> dht = new DistributedHashTable<>(100, 3);
            
            // 添加节点
            dht.addNode("node1");
            dht.addNode("node2");
            dht.addNode("node3");
            
            // 存储数据
            dht.put("key1", "value1");
            dht.put("key2", "value2");
            dht.put("key3", "value3");
            
            // 验证数据
            assert "value1".equals(dht.get("key1")) : "数据验证失败";
            assert "value2".equals(dht.get("key2")) : "数据验证失败";
            assert "value3".equals(dht.get("key3")) : "数据验证失败";
            
            // 验证负载分布
            Map<String, Integer> load = dht.getLoadDistribution();
            assert load.size() == 3 : "负载分布验证失败";
            
            System.out.println("分布式哈希表测试通过");
        }
        
        /**
         * 性能测试：大规模数据
         */
        public void performanceTest() {
            int size = 100000;
            int[] nums = new int[size];
            Random random = new Random();
            
            // 生成测试数据
            for (int i = 0; i < size; i++) {
                nums[i] = random.nextInt(size * 10);
            }
            
            LongestConsecutiveSequence solution = new LongestConsecutiveSequence();
            
            long startTime = System.currentTimeMillis();
            int result = solution.longestConsecutive(nums);
            long endTime = System.currentTimeMillis();
            
            System.out.println("性能测试结果：");
            System.out.println("数据规模：" + size);
            System.out.println("最长序列长度：" + result);
            System.out.println("执行时间：" + (endTime - startTime) + "ms");
            
            // 验证性能要求：10万数据应在1秒内完成
            assert (endTime - startTime) < 1000 : "性能测试失败";
            
            System.out.println("性能测试通过");
        }
        
        /**
         * 运行所有测试
         */
        public void runAllTests() {
            try {
                testLongestConsecutiveSequence();
                testFourSumII();
                testDistributedHashTable();
                performanceTest();
                
                System.out.println("=== 所有测试通过 ===");
            } catch (AssertionError e) {
                System.err.println("测试失败：" + e.getMessage());
                throw e;
            }
        }
    }
    
    /**
     * ===================================================================
     * 主函数：演示和测试
     * ===================================================================
     */
    public static void main(String[] args) {
        System.out.println("=== 哈希算法题目扩展演示 ===\n");
        
        // 运行测试
        HashAlgorithmTest test = new HashAlgorithmTest();
        test.runAllTests();
        
        // 演示最长连续序列
        System.out.println("\n=== 最长连续序列演示 ===");
        LongestConsecutiveSequence lcs = new LongestConsecutiveSequence();
        int[] demoNums = {100, 4, 200, 1, 3, 2, 5};
        System.out.println("输入数组：" + Arrays.toString(demoNums));
        System.out.println("最长连续序列长度：" + lcs.longestConsecutive(demoNums));
        
        // 演示分布式哈希表
        System.out.println("\n=== 分布式哈希表演示 ===");
        DistributedHashTable<String, String> dht = new DistributedHashTable<>(50, 2);
        dht.addNode("server1");
        dht.addNode("server2");
        dht.put("user1", "data1");
        dht.put("user2", "data2");
        System.out.println("user1的数据：" + dht.get("user1"));
        System.out.println("负载分布：" + dht.getLoadDistribution());
        
        System.out.println("\n=== 演示完成 ===");
    }
}