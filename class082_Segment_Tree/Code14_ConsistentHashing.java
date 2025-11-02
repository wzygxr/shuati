import java.util.*;

/**
 * 一致性哈希算法实现
 * 
 * 题目来源：分布式系统设计面试题
 * 应用场景：负载均衡、分布式缓存、分布式存储系统
 * 
 * 核心思想：
 * 1. 将哈希空间组织成一个虚拟的圆环（0 ~ 2^32-1）
 * 2. 服务器节点通过哈希函数映射到环上
 * 3. 数据通过哈希函数映射到环上，顺时针找到最近的服务器节点
 * 4. 虚拟节点技术解决数据分布不均问题
 * 
 * 时间复杂度：
 * - 添加节点：O(k) k为虚拟节点数
 * - 删除节点：O(k)
 * - 查找节点：O(log n) n为环上节点总数
 * 
 * 空间复杂度：O(n*k) n为物理节点数，k为虚拟节点数
 * 
 * 工程化考量：
 * 1. 虚拟节点解决数据倾斜问题
 * 2. 支持节点的动态增删
 * 3. 数据迁移最小化
 * 4. 容错性和可扩展性
 */
public class Code14_ConsistentHashing {
    
    // 哈希环，存储虚拟节点到物理节点的映射
    private TreeMap<Integer, String> ring = new TreeMap<>();
    
    // 虚拟节点数量
    private int virtualNodes;
    
    // 物理节点集合
    private Set<String> physicalNodes = new HashSet<>();
    
    public Code14_ConsistentHashing(int virtualNodes) {
        this.virtualNodes = virtualNodes;
    }
    
    /**
     * 添加物理节点
     * @param node 物理节点标识
     */
    public void addNode(String node) {
        if (physicalNodes.contains(node)) {
            return; // 节点已存在
        }
        
        physicalNodes.add(node);
        
        // 为每个物理节点创建虚拟节点
        for (int i = 0; i < virtualNodes; i++) {
            String virtualNode = node + "#" + i;
            int hash = getHash(virtualNode);
            ring.put(hash, node);
        }
    }
    
    /**
     * 删除物理节点
     * @param node 物理节点标识
     */
    public void removeNode(String node) {
        if (!physicalNodes.contains(node)) {
            return; // 节点不存在
        }
        
        physicalNodes.remove(node);
        
        // 删除该节点的所有虚拟节点
        for (int i = 0; i < virtualNodes; i++) {
            String virtualNode = node + "#" + i;
            int hash = getHash(virtualNode);
            ring.remove(hash);
        }
    }
    
    /**
     * 根据key查找对应的物理节点
     * @param key 数据key
     * @return 物理节点标识
     */
    public String getNode(String key) {
        if (ring.isEmpty()) {
            return null;
        }
        
        int hash = getHash(key);
        
        // 在环上查找大于等于该hash的第一个节点
        Map.Entry<Integer, String> entry = ring.ceilingEntry(hash);
        
        // 如果没找到，则返回环的第一个节点（环形结构）
        if (entry == null) {
            entry = ring.firstEntry();
        }
        
        return entry.getValue();
    }
    
    /**
     * 哈希函数：使用MD5哈希然后取模
     * @param key 输入字符串
     * @return 哈希值（0 ~ 2^32-1）
     */
    private int getHash(String key) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(key.getBytes());
            // 取前4个字节作为哈希值
            int hash = 0;
            for (int i = 0; i < 4; i++) {
                hash = (hash << 8) | (digest[i] & 0xFF);
            }
            return hash & 0x7FFFFFFF; // 确保为正数
        } catch (Exception e) {
            // 如果MD5不可用，使用简单的哈希函数
            return key.hashCode() & 0x7FFFFFFF;
        }
    }
    
    /**
     * 获取环上节点分布情况（用于调试）
     */
    public void printRing() {
        System.out.println("一致性哈希环状态：");
        for (Map.Entry<Integer, String> entry : ring.entrySet()) {
            System.out.println("位置 " + entry.getKey() + " -> " + entry.getValue());
        }
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 创建一致性哈希环，每个物理节点有3个虚拟节点
        Code14_ConsistentHashing ch = new Code14_ConsistentHashing(3);
        
        // 添加物理节点
        ch.addNode("Server-A");
        ch.addNode("Server-B");
        ch.addNode("Server-C");
        
        // 测试数据分布
        String[] testKeys = {"user1", "user2", "user3", "data1", "data2", "data3"};
        
        System.out.println("=== 初始节点分布测试 ===");
        for (String key : testKeys) {
            String node = ch.getNode(key);
            System.out.println("Key: " + key + " -> Node: " + node);
        }
        
        // 测试节点删除
        System.out.println("\n=== 删除Server-B后测试 ===");
        ch.removeNode("Server-B");
        
        for (String key : testKeys) {
            String node = ch.getNode(key);
            System.out.println("Key: " + key + " -> Node: " + node);
        }
        
        // 测试节点添加
        System.out.println("\n=== 添加Server-D后测试 ===");
        ch.addNode("Server-D");
        
        for (String key : testKeys) {
            String node = ch.getNode(key);
            System.out.println("Key: " + key + " -> Node: " + node);
        }
        
        // 性能测试
        System.out.println("\n=== 性能测试 ===");
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            ch.getNode("test" + i);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("10000次查找耗时: " + (endTime - startTime) + "ms");
        
        // 打印环状态（调试用）
        ch.printRing();
    }
}