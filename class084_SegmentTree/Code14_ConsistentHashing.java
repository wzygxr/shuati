package class111;

// 一致性哈希算法实现 (Java版本)
// 题目来源: 分布式系统设计面试题
// 应用场景: 负载均衡、分布式缓存、分布式存储系统
// 题目描述: 实现一致性哈希算法，支持节点的动态增删和虚拟节点技术
// 
// 解题思路:
// 1. 使用哈希环存储节点和虚拟节点
// 2. 使用虚拟节点技术解决数据分布不均问题
// 3. 支持节点的动态添加和删除
// 4. 实现高效的数据查找和节点定位
// 
// 时间复杂度分析:
// - 添加节点: O(k)，其中k是虚拟节点数量
// - 删除节点: O(k)
// - 查找节点: O(log n)，其中n是节点总数
// 
// 空间复杂度: O(n * k)，其中n是物理节点数，k是每个节点的虚拟节点数
// 
// 工程化考量:
// 1. 异常处理: 验证节点和数据的有效性
// 2. 性能优化: 使用TreeMap实现高效的区间查找
// 3. 负载均衡: 虚拟节点技术确保数据均匀分布
// 4. 容错性: 支持节点的动态增删，最小化数据迁移

import java.util.*;

public class Code14_ConsistentHashing {
    
    // 哈希环，存储虚拟节点到物理节点的映射
    private TreeMap<Integer, String> hashRing;
    
    // 物理节点列表
    private Set<String> physicalNodes;
    
    // 每个物理节点的虚拟节点数量
    private int virtualNodeCount;
    
    /**
     * 构造函数
     * @param virtualNodeCount 每个物理节点的虚拟节点数量
     */
    public Code14_ConsistentHashing(int virtualNodeCount) {
        this.hashRing = new TreeMap<>();
        this.physicalNodes = new HashSet<>();
        this.virtualNodeCount = virtualNodeCount;
    }
    
    /**
     * 添加物理节点
     * @param node 物理节点名称
     * @throws IllegalArgumentException 如果节点名为空或已存在
     */
    public void addNode(String node) {
        if (node == null || node.trim().isEmpty()) {
            throw new IllegalArgumentException("节点名不能为空");
        }
        if (physicalNodes.contains(node)) {
            throw new IllegalArgumentException("节点 " + node + " 已存在");
        }
        
        physicalNodes.add(node);
        
        // 为物理节点创建虚拟节点
        for (int i = 0; i < virtualNodeCount; i++) {
            String virtualNode = node + "#" + i;
            int hash = hash(virtualNode);
            hashRing.put(hash, node);
        }
        
        System.out.println("添加节点: " + node + "，虚拟节点数: " + virtualNodeCount);
    }
    
    /**
     * 删除物理节点
     * @param node 物理节点名称
     * @throws IllegalArgumentException 如果节点不存在
     */
    public void removeNode(String node) {
        if (!physicalNodes.contains(node)) {
            throw new IllegalArgumentException("节点 " + node + " 不存在");
        }
        
        physicalNodes.remove(node);
        
        // 删除该物理节点的所有虚拟节点
        Iterator<Map.Entry<Integer, String>> iterator = hashRing.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, String> entry = iterator.next();
            if (entry.getValue().equals(node)) {
                iterator.remove();
            }
        }
        
        System.out.println("删除节点: " + node);
    }
    
    /**
     * 根据键查找对应的物理节点
     * @param key 数据键
     * @return 负责该键的物理节点
     * @throws IllegalArgumentException 如果键为空或哈希环为空
     */
    public String getNode(String key) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("键不能为空");
        }
        if (hashRing.isEmpty()) {
            throw new IllegalArgumentException("哈希环为空，请先添加节点");
        }
        
        int hash = hash(key);
        
        // 在哈希环上顺时针查找第一个大于等于该哈希值的节点
        Map.Entry<Integer, String> entry = hashRing.ceilingEntry(hash);
        
        // 如果没找到，则返回环上的第一个节点（环状结构）
        if (entry == null) {
            entry = hashRing.firstEntry();
        }
        
        return entry.getValue();
    }
    
    /**
     * 哈希函数 - 使用FNV-1a算法
     * @param str 输入字符串
     * @return 哈希值
     */
    private int hash(String str) {
        final int FNV_OFFSET_BASIS = 0x811C9DC5;
        final int FNV_PRIME = 0x01000193;
        
        int hash = FNV_OFFSET_BASIS;
        for (byte b : str.getBytes()) {
            hash ^= (b & 0xff);
            hash *= FNV_PRIME;
        }
        
        // 确保哈希值为正数
        return hash & 0x7fffffff;
    }
    
    /**
     * 获取哈希环的状态信息
     * @return 哈希环状态字符串
     */
    public String getStatus() {
        StringBuilder sb = new StringBuilder();
        sb.append("一致性哈希环状态:\n");
        sb.append("物理节点数: ").append(physicalNodes.size()).append("\n");
        sb.append("虚拟节点数: ").append(hashRing.size()).append("\n");
        sb.append("物理节点列表: ").append(physicalNodes).append("\n");
        
        // 统计每个物理节点的虚拟节点分布
        Map<String, Integer> nodeDistribution = new HashMap<>();
        for (String node : hashRing.values()) {
            nodeDistribution.put(node, nodeDistribution.getOrDefault(node, 0) + 1);
        }
        
        sb.append("虚拟节点分布: ").append(nodeDistribution).append("\n");
        
        return sb.toString();
    }
    
    /**
     * 负载均衡测试
     * 模拟大量数据分布，检查负载均衡性
     * @param dataCount 测试数据数量
     */
    public void loadBalanceTest(int dataCount) {
        if (physicalNodes.isEmpty()) {
            System.out.println("请先添加节点再进行负载均衡测试");
            return;
        }
        
        Map<String, Integer> distribution = new HashMap<>();
        
        // 初始化分布统计
        for (String node : physicalNodes) {
            distribution.put(node, 0);
        }
        
        // 模拟数据分布
        Random random = new Random();
        for (int i = 0; i < dataCount; i++) {
            String key = "key" + random.nextInt(1000000);
            String node = ch.getNode(key);
            distribution.put(node, distribution.get(node) + 1);
        }
        
        // 计算负载均衡指标
        int total = dataCount;
        double average = (double) total / physicalNodes.size();
        double variance = 0.0;
        
        System.out.println("负载均衡测试结果 (数据量: " + dataCount + "):");
        for (Map.Entry<String, Integer> entry : distribution.entrySet()) {
            double deviation = Math.abs(entry.getValue() - average);
            variance += deviation * deviation;
            System.out.printf("节点 %s: %d 数据 (%.2f%%)\n", 
                entry.getKey(), entry.getValue(), 
                (double) entry.getValue() / total * 100);
        }
        
        double stdDev = Math.sqrt(variance / physicalNodes.size());
        System.out.printf("标准差: %.2f, 相对标准差: %.2f%%\n", 
            stdDev, stdDev / average * 100);
    }
    
    /**
     * 单元测试方法
     */
    public static void test() {
        System.out.println("=== 一致性哈希算法单元测试 ===");
        
        // 创建一致性哈希实例，每个物理节点有3个虚拟节点
        Code14_ConsistentHashing ch = new Code14_ConsistentHashing(3);
        
        // 测试1: 添加节点
        ch.addNode("Node-A");
        ch.addNode("Node-B");
        ch.addNode("Node-C");
        
        System.out.println(ch.getStatus());
        
        // 测试2: 数据分布测试
        Map<String, Integer> testDistribution = new HashMap<>();
        String[] testKeys = {"user1", "user2", "user3", "data1", "data2", "file1"};
        
        for (String key : testKeys) {
            String node = ch.getNode(key);
            testDistribution.put(node, testDistribution.getOrDefault(node, 0) + 1);
            System.out.println("键 '" + key + "' 分配到节点: " + node);
        }
        
        System.out.println("测试数据分布: " + testDistribution);
        
        // 测试3: 负载均衡测试
        ch.loadBalanceTest(1000);
        
        // 测试4: 节点删除测试
        System.out.println("\n=== 节点删除测试 ===");
        ch.removeNode("Node-B");
        System.out.println(ch.getStatus());
        
        // 重新测试数据分布
        testDistribution.clear();
        for (String key : testKeys) {
            String node = ch.getNode(key);
            testDistribution.put(node, testDistribution.getOrDefault(node, 0) + 1);
            System.out.println("键 '" + key + "' 重新分配到节点: " + node);
        }
        
        System.out.println("删除节点后数据分布: " + testDistribution);
        
        System.out.println("=== 单元测试完成 ===");
    }
    
    public static void main(String[] args) {
        if (args.length > 0 && "test".equals(args[0])) {
            test();
            return;
        }
        
        // 演示示例
        Code14_ConsistentHashing ch = new Code14_ConsistentHashing(5);
        
        // 添加节点
        ch.addNode("Server-1");
        ch.addNode("Server-2");
        ch.addNode("Server-3");
        
        System.out.println(ch.getStatus());
        
        // 演示数据分布
        String[] demoKeys = {"user:1001", "order:2001", "product:3001", "cache:4001"};
        for (String key : demoKeys) {
            System.out.println("数据 '" + key + "' 分配到: " + ch.getNode(key));
        }
        
        // 负载均衡测试
        ch.loadBalanceTest(10000);
    }
}