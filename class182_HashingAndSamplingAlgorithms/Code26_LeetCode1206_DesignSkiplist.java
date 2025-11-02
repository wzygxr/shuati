package class107_HashingAndSamplingAlgorithms;

import java.util.*;

/**
 * LeetCode 1206. 设计跳表 (Design Skiplist)
 * 题目链接：https://leetcode.com/problems/design-skiplist/
 * 
 * 题目描述：
 * 不使用任何库函数，设计一个跳表。
 * 跳表是一种可以在O(log(n))时间内完成增加、删除、搜索操作的数据结构。
 * 跳表相比树堆与红黑树，其功能与性能相当，而且代码更短，设计思想与链表相似。
 * 
 * 实现 Skiplist 类：
 * - Skiplist() 初始化跳表对象
 * - bool search(int target) 返回target是否存在于跳表中
 * - void add(int num) 插入一个元素到跳表
 * - bool erase(int num) 在跳表中删除一个值，如果 num 不存在，直接返回false
 * 
 * 算法思路：
 * 1. 跳表是一种多层链表结构，每一层都是一个有序链表
 * 2. 每个节点可能出现在多个层中，通过随机化决定节点出现在哪些层
 * 3. 查找时从最高层开始，逐层向下查找，减少比较次数
 * 4. 插入和删除时需要维护各层链表的正确性
 * 
 * 时间复杂度：
 * - search: O(log n) 平均情况
 * - add: O(log n) 平均情况
 * - erase: O(log n) 平均情况
 * 
 * 空间复杂度：O(n) 平均情况
 * 
 * 工程化考量：
 * 1. 随机化：使用随机数决定节点层数，保证性能
 * 2. 内存优化：合理设置最大层数，避免过多内存消耗
 * 3. 异常处理：处理空值和边界情况
 * 4. 线程安全：在多线程环境下需要加锁保护
 */
public class Code26_LeetCode1206_DesignSkiplist {
    
    // 跳表节点类
    private static class Node {
        int value;
        Node[] next;
        
        Node(int value, int level) {
            this.value = value;
            this.next = new Node[level];
        }
    }
    
    // 最大层数
    private static final int MAX_LEVEL = 16;
    
    // 随机数生成器
    private final Random random;
    
    // 头节点
    private final Node head;
    
    // 当前最高层数
    private int currentLevel;
    
    /**
     * 构造函数
     */
    public Code26_LeetCode1206_DesignSkiplist() {
        this.random = new Random();
        this.head = new Node(-1, MAX_LEVEL);
        this.currentLevel = 0;
    }
    
    /**
     * 搜索目标值是否存在
     * @param target 目标值
     * @return 如果存在返回true，否则返回false
     */
    public boolean search(int target) {
        Node current = head;
        
        // 从最高层开始向下搜索
        for (int i = currentLevel - 1; i >= 0; i--) {
            // 在当前层找到小于target的最大节点
            while (current.next[i] != null && current.next[i].value < target) {
                current = current.next[i];
            }
        }
        
        // 检查下一个节点是否为目标值
        current = current.next[0];
        return current != null && current.value == target;
    }
    
    /**
     * 添加元素
     * @param num 要添加的元素
     */
    public void add(int num) {
        // 更新数组，记录每一层需要更新的节点
        Node[] update = new Node[MAX_LEVEL];
        Node current = head;
        
        // 从最高层开始向下搜索插入位置
        for (int i = currentLevel - 1; i >= 0; i--) {
            // 在当前层找到小于num的最大节点
            while (current.next[i] != null && current.next[i].value < num) {
                current = current.next[i];
            }
            update[i] = current;
        }
        
        // 移动到下一层
        current = current.next[0];
        
        // 如果元素已存在，直接返回
        if (current != null && current.value == num) {
            return;
        }
        
        // 随机生成新节点的层数
        int newLevel = randomLevel();
        
        // 如果新层数大于当前最高层数，更新update数组
        if (newLevel > currentLevel) {
            for (int i = currentLevel; i < newLevel; i++) {
                update[i] = head;
            }
            currentLevel = newLevel;
        }
        
        // 创建新节点
        Node newNode = new Node(num, newLevel);
        
        // 更新各层的指针
        for (int i = 0; i < newLevel; i++) {
            newNode.next[i] = update[i].next[i];
            update[i].next[i] = newNode;
        }
    }
    
    /**
     * 删除元素
     * @param num 要删除的元素
     * @return 如果删除成功返回true，否则返回false
     */
    public boolean erase(int num) {
        // 更新数组，记录每一层需要更新的节点
        Node[] update = new Node[MAX_LEVEL];
        Node current = head;
        
        // 从最高层开始向下搜索删除位置
        for (int i = currentLevel - 1; i >= 0; i--) {
            // 在当前层找到小于num的最大节点
            while (current.next[i] != null && current.next[i].value < num) {
                current = current.next[i];
            }
            update[i] = current;
        }
        
        // 移动到下一层
        current = current.next[0];
        
        // 如果元素不存在，直接返回false
        if (current == null || current.value != num) {
            return false;
        }
        
        // 更新各层的指针
        for (int i = 0; i < currentLevel; i++) {
            if (update[i].next[i] != current) {
                break;
            }
            update[i].next[i] = current.next[i];
        }
        
        // 更新当前最高层数
        while (currentLevel > 1 && head.next[currentLevel - 1] == null) {
            currentLevel--;
        }
        
        return true;
    }
    
    /**
     * 随机生成节点层数
     * @return 节点层数
     */
    private int randomLevel() {
        int level = 1;
        // 随机生成层数，每层概率为1/2
        while (random.nextInt() % 2 == 0 && level < MAX_LEVEL) {
            level++;
        }
        return level;
    }
    
    /**
     * 获取跳表统计信息
     * @return 统计信息映射
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("currentLevel", currentLevel);
        stats.put("maxLevel", MAX_LEVEL);
        
        // 计算节点数量
        int count = 0;
        Node current = head.next[0];
        while (current != null) {
            count++;
            current = current.next[0];
        }
        stats.put("nodeCount", count);
        
        return stats;
    }
    
    /**
     * 性能测试类
     */
    public static class PerformanceTest {
        
        /**
         * 测试跳表的性能
         * @param skiplist 跳表实例
         * @param operationCount 操作数量
         */
        public static void testSkiplistPerformance(Code26_LeetCode1206_DesignSkiplist skiplist, 
                                                   int operationCount) {
            System.out.println("=== 跳表性能测试 ===");
            System.out.printf("操作数量: %d\n", operationCount);
            
            Random random = new Random(42);
            
            // 测试插入性能
            long startTime = System.nanoTime();
            
            for (int i = 0; i < operationCount / 2; i++) {
                int value = random.nextInt(operationCount);
                skiplist.add(value);
            }
            
            long addTime = System.nanoTime() - startTime;
            
            // 测试查询性能
            startTime = System.nanoTime();
            
            int hitCount = 0;
            int missCount = 0;
            
            for (int i = 0; i < operationCount / 2; i++) {
                int value = random.nextInt(operationCount * 2);
                boolean found = skiplist.search(value);
                if (found) {
                    hitCount++;
                } else {
                    missCount++;
                }
            }
            
            long searchTime = System.nanoTime() - startTime;
            
            // 测试删除性能
            startTime = System.nanoTime();
            
            int deleteSuccess = 0;
            int deleteFailed = 0;
            
            for (int i = 0; i < operationCount / 4; i++) {
                int value = random.nextInt(operationCount);
                boolean deleted = skiplist.erase(value);
                if (deleted) {
                    deleteSuccess++;
                } else {
                    deleteFailed++;
                }
            }
            
            long eraseTime = System.nanoTime() - startTime;
            
            System.out.printf("插入平均时间: %.2f ns\n", (double) addTime / (operationCount / 2));
            System.out.printf("查询平均时间: %.2f ns\n", (double) searchTime / (operationCount / 2));
            System.out.printf("删除平均时间: %.2f ns\n", (double) eraseTime / (operationCount / 4));
            System.out.printf("查询命中率: %.2f%%\n", (double) hitCount / (hitCount + missCount) * 100);
            
            // 显示统计信息
            System.out.println("跳表统计信息: " + skiplist.getStatistics());
        }
    }
    
    /**
     * 单元测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 跳表设计测试 ===\n");
        
        // 基本功能测试
        System.out.println("1. 基本功能测试:");
        
        Code26_LeetCode1206_DesignSkiplist skiplist = new Code26_LeetCode1206_DesignSkiplist();
        
        // 添加元素
        skiplist.add(1);
        skiplist.add(2);
        skiplist.add(3);
        System.out.println("添加元素 1, 2, 3");
        
        // 查询元素
        boolean found = skiplist.search(0);
        System.out.println("查询元素 0: " + found);
        
        found = skiplist.search(1);
        System.out.println("查询元素 1: " + found);
        
        found = skiplist.search(4);
        System.out.println("查询元素 4: " + found);
        
        // 删除元素
        boolean deleted = skiplist.erase(0);
        System.out.println("删除元素 0: " + deleted);
        
        deleted = skiplist.erase(1);
        System.out.println("删除元素 1: " + deleted);
        
        found = skiplist.search(1);
        System.out.println("查询元素 1: " + found);
        
        // 复杂场景测试
        System.out.println("\n2. 复杂场景测试:");
        
        Code26_LeetCode1206_DesignSkiplist skiplist2 = new Code26_LeetCode1206_DesignSkiplist();
        
        // 添加多个元素
        for (int i = 1; i <= 10; i++) {
            skiplist2.add(i * 10);
        }
        
        // 查询所有元素
        for (int i = 1; i <= 10; i++) {
            found = skiplist2.search(i * 10);
            System.out.printf("查询元素 %d: %s\n", i * 10, found);
        }
        
        // 删除部分元素
        for (int i = 1; i <= 5; i++) {
            deleted = skiplist2.erase(i * 10);
            System.out.printf("删除元素 %d: %s\n", i * 10, deleted);
        }
        
        // 再次查询所有元素
        for (int i = 1; i <= 10; i++) {
            found = skiplist2.search(i * 10);
            System.out.printf("查询元素 %d: %s\n", i * 10, found);
        }
        
        // 性能测试
        System.out.println("\n3. 性能测试:");
        Code26_LeetCode1206_DesignSkiplist skiplist3 = new Code26_LeetCode1206_DesignSkiplist();
        PerformanceTest.testSkiplistPerformance(skiplist3, 10000);
        
        System.out.println("\n=== 算法复杂度分析 ===");
        System.out.println("时间复杂度:");
        System.out.println("- search: O(log n) 平均情况");
        System.out.println("- add: O(log n) 平均情况");
        System.out.println("- erase: O(log n) 平均情况");
        System.out.println("空间复杂度: O(n) 平均情况");
        
        System.out.println("\n=== 工程化应用场景 ===");
        System.out.println("1. Redis有序集合: 使用跳表实现有序集合");
        System.out.println("2. 数据库索引: 某些数据库使用跳表作为索引结构");
        System.out.println("3. 内存数据库: 高性能内存数据结构");
        System.out.println("4. 实时系统: 需要快速查找和更新的场景");
        
        System.out.println("\n=== 设计要点 ===");
        System.out.println("1. 多层链表: 通过多层链表减少查找时间");
        System.out.println("2. 随机化: 使用随机数决定节点层数，保证性能");
        System.out.println("3. 指针维护: 插入和删除时正确维护各层指针");
        System.out.println("4. 内存优化: 合理设置最大层数，避免过多内存消耗");
    }
}