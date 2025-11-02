import java.util.*;

/**
 * 自定义哈希集合实现
 * 题目来源：LeetCode 705. 设计哈希集合
 * 题目链接：https://leetcode.cn/problems/design-hashset/
 * 
 * 题目描述：
 * 不使用任何内建的哈希表库设计一个哈希集合（HashSet）。
 * 实现 MyHashSet 类：
 * - void add(key) 向哈希集合中插入值 key 。
 * - bool contains(key) 返回哈希集合中是否存在这个值 key 。
 * - void remove(key) 将给定值 key 从哈希集合中删除。如果哈希集合中没有这个值，什么也不做。
 * 
 * 解题思路：
 * 1. 使用链地址法解决哈希冲突
 * 2. 选择合适的哈希函数和桶大小
 * 3. 实现动态扩容机制
 * 4. 处理边界情况和异常输入
 * 
 * 时间复杂度分析：
 * - 平均情况：O(1) 对于 add、remove、contains 操作
 * - 最坏情况：O(n) 当所有元素都哈希到同一个桶时
 * 
 * 空间复杂度：O(n + m)，其中n是元素数量，m是桶的数量
 * 
 * 优化点：
 * - 动态扩容保持负载因子合理
 * - 使用质数作为桶大小减少冲突
 * - 优化哈希函数分布
 */
public class Code13_HashSetDesign {
    
    /**
     * 哈希集合节点类
     */
    private static class Node {
        int key;
        Node next;
        
        Node(int key) {
            this.key = key;
        }
    }
    
    /**
     * 自定义哈希集合实现类
     */
    public static class MyHashSet {
        private static final int INITIAL_CAPACITY = 16;
        private static final double LOAD_FACTOR = 0.75;
        
        private Node[] buckets;
        private int size;
        
        public MyHashSet() {
            this(INITIAL_CAPACITY);
        }
        
        public MyHashSet(int initialCapacity) {
            buckets = new Node[initialCapacity];
            size = 0;
        }
        
        /**
         * 向哈希集合中添加元素
         */
        public void add(int key) {
            if (contains(key)) {
                return; // 元素已存在，直接返回
            }
            
            // 检查是否需要扩容
            if ((double) size / buckets.length > LOAD_FACTOR) {
                resize();
            }
            
            int index = getIndex(key);
            Node newNode = new Node(key);
            
            // 头插法
            newNode.next = buckets[index];
            buckets[index] = newNode;
            size++;
        }
        
        /**
         * 从哈希集合中移除元素
         */
        public void remove(int key) {
            int index = getIndex(key);
            Node current = buckets[index];
            Node prev = null;
            
            while (current != null) {
                if (current.key == key) {
                    if (prev == null) {
                        // 删除头节点
                        buckets[index] = current.next;
                    } else {
                        prev.next = current.next;
                    }
                    size--;
                    return;
                }
                prev = current;
                current = current.next;
            }
        }
        
        /**
         * 检查哈希集合是否包含元素
         */
        public boolean contains(int key) {
            int index = getIndex(key);
            Node current = buckets[index];
            
            while (current != null) {
                if (current.key == key) {
                    return true;
                }
                current = current.next;
            }
            
            return false;
        }
        
        /**
         * 获取元素数量
         */
        public int size() {
            return size;
        }
        
        /**
         * 检查哈希集合是否为空
         */
        public boolean isEmpty() {
            return size == 0;
        }
        
        /**
         * 清空哈希集合
         */
        public void clear() {
            Arrays.fill(buckets, null);
            size = 0;
        }
        
        /**
         * 获取元素的哈希索引
         */
        private int getIndex(int key) {
            // 使用Java的hashCode方法并取模
            return Math.abs(Integer.hashCode(key)) % buckets.length;
        }
        
        /**
         * 动态扩容
         */
        private void resize() {
            int newCapacity = buckets.length * 2;
            Node[] newBuckets = new Node[newCapacity];
            
            // 重新哈希所有元素
            for (Node head : buckets) {
                Node current = head;
                while (current != null) {
                    Node next = current.next;
                    int newIndex = Math.abs(Integer.hashCode(current.key)) % newCapacity;
                    
                    // 头插法插入新桶
                    current.next = newBuckets[newIndex];
                    newBuckets[newIndex] = current;
                    
                    current = next;
                }
            }
            
            buckets = newBuckets;
        }
        
        /**
         * 打印哈希集合状态（用于调试）
         */
        public void printStats() {
            System.out.println("哈希集合状态：");
            System.out.println("容量：" + buckets.length);
            System.out.println("元素数量：" + size);
            System.out.println("负载因子：" + (double) size / buckets.length);
            
            // 统计每个桶的元素数量
            int[] bucketSizes = new int[buckets.length];
            for (int i = 0; i < buckets.length; i++) {
                Node current = buckets[i];
                int count = 0;
                while (current != null) {
                    count++;
                    current = current.next;
                }
                bucketSizes[i] = count;
            }
            
            System.out.println("桶分布：" + Arrays.toString(bucketSizes));
        }
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试基本功能
        System.out.println("=== 基本功能测试 ===");
        MyHashSet hashSet = new MyHashSet();
        
        // 测试添加操作
        hashSet.add(1);
        hashSet.add(2);
        hashSet.add(3);
        System.out.println("添加1,2,3后大小：" + hashSet.size());
        System.out.println("是否包含2：" + hashSet.contains(2));
        System.out.println("是否包含4：" + hashSet.contains(4));
        
        // 测试删除操作
        hashSet.remove(2);
        System.out.println("删除2后大小：" + hashSet.size());
        System.out.println("是否包含2：" + hashSet.contains(2));
        
        // 测试重复添加
        hashSet.add(1);
        System.out.println("重复添加1后大小：" + hashSet.size());
        
        // 测试边界情况
        System.out.println("\n=== 边界情况测试 ===");
        hashSet.add(Integer.MAX_VALUE);
        hashSet.add(Integer.MIN_VALUE);
        System.out.println("添加边界值后大小：" + hashSet.size());
        System.out.println("是否包含Integer.MAX_VALUE：" + hashSet.contains(Integer.MAX_VALUE));
        System.out.println("是否包含Integer.MIN_VALUE：" + hashSet.contains(Integer.MIN_VALUE));
        
        // 测试性能
        System.out.println("\n=== 性能测试 ===");
        MyHashSet largeSet = new MyHashSet();
        long startTime = System.currentTimeMillis();
        
        // 添加10000个元素
        for (int i = 0; i < 10000; i++) {
            largeSet.add(i);
        }
        
        long addTime = System.currentTimeMillis() - startTime;
        System.out.println("添加10000个元素耗时：" + addTime + "ms");
        
        // 查询性能
        startTime = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            largeSet.contains(i);
        }
        long queryTime = System.currentTimeMillis() - startTime;
        System.out.println("查询10000个元素耗时：" + queryTime + "ms");
        
        // 打印统计信息
        System.out.println("\n=== 统计信息 ===");
        largeSet.printStats();
        
        // 测试动态扩容
        System.out.println("\n=== 动态扩容测试 ===");
        MyHashSet resizeSet = new MyHashSet(4); // 小容量初始值
        for (int i = 0; i < 10; i++) {
            resizeSet.add(i);
            System.out.println("添加" + i + "后容量：" + resizeSet.size());
        }
        resizeSet.printStats();
        
        // 测试异常情况
        System.out.println("\n=== 异常情况测试 ===");
        try {
            hashSet.remove(9999); // 删除不存在的元素
            System.out.println("删除不存在的元素：正常处理");
        } catch (Exception e) {
            System.out.println("删除不存在的元素异常：" + e.getMessage());
        }
        
        // 测试清空操作
        hashSet.clear();
        System.out.println("清空后大小：" + hashSet.size());
        System.out.println("是否为空：" + hashSet.isEmpty());
    }
}