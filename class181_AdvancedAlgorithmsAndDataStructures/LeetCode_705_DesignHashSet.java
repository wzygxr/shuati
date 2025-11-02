package class008_AdvancedAlgorithmsAndDataStructures.unrolled_linked_list_problems;

import java.util.*;

/**
 * LeetCode 705. 设计哈希集合 (Design HashSet)
 * 
 * 题目来源：https://leetcode.cn/problems/design-hashset/
 * 
 * 题目描述：
 * 不使用任何内建的哈希表库设计一个哈希集合（HashSet）。
 * 实现 MyHashSet 类：
 * - void add(key) 向哈希集合中插入值 key 。
 * - bool contains(key) 返回哈希集合中是否存在这个值 key 。
 * - void remove(key) 将给定值 key 从哈希集合中删除。如果哈希集合中没有这个值，什么也不做。
 * 
 * 算法思路：
 * 这个问题可以通过以下方法解决：
 * 1. 块状链表：使用块状链表实现哈希集合
 * 2. 链地址法：使用数组+链表实现哈希表
 * 3. 开放地址法：使用线性探测或二次探测
 * 
 * 使用块状链表的方法：
 * 1. 将哈希空间分块，每块维护一个有序数组
 * 2. 对于每个添加的键，计算其哈希值并确定所属块
 * 3. 在块内维护有序性以便快速查找
 * 4. 当块满时进行分裂操作
 * 
 * 时间复杂度：
 * - 块状链表：O(√n) 平均情况
 * - 链地址法：O(n/k) 平均情况，k是桶数
 * - 开放地址法：O(1) 平均情况
 * - 空间复杂度：O(n)
 * 
 * 应用场景：
 * 1. 数据库索引：快速查找和去重
 * 2. 缓存系统：缓存键的管理
 * 3. 编译器：符号表管理
 * 
 * 相关题目：
 * 1. LeetCode 706. 设计哈希映射
 * 2. LeetCode 641. 设计循环双端队列
 * 3. LeetCode 146. LRU缓存机制
 */
public class LeetCode_705_DesignHashSet {
    
    /**
     * 块状链表节点
     */
    static class Block {
        private static final int BLOCK_SIZE = 32; // 块大小
        int[] data;  // 存储数据的数组
        int size;    // 当前块中元素的数量
        Block next;  // 指向下一个块
        
        Block() {
            data = new int[BLOCK_SIZE];
            size = 0;
            next = null;
        }
        
        /**
         * 在块中添加元素（保持有序）
         */
        boolean add(int key) {
            // 检查元素是否已存在
            int index = binarySearch(key);
            if (index >= 0) {
                return false; // 元素已存在
            }
            
            // 如果块已满，返回false
            if (size >= BLOCK_SIZE) {
                return false;
            }
            
            // 插入元素并保持有序
            int insertPos = -(index + 1);
            for (int i = size; i > insertPos; i--) {
                data[i] = data[i - 1];
            }
            data[insertPos] = key;
            size++;
            return true;
        }
        
        /**
         * 在块中删除元素
         */
        boolean remove(int key) {
            int index = binarySearch(key);
            if (index < 0) {
                return false; // 元素不存在
            }
            
            // 删除元素
            for (int i = index; i < size - 1; i++) {
                data[i] = data[i + 1];
            }
            size--;
            return true;
        }
        
        /**
         * 检查块中是否包含元素
         */
        boolean contains(int key) {
            return binarySearch(key) >= 0;
        }
        
        /**
         * 二分查找元素
         */
        private int binarySearch(int key) {
            int left = 0, right = size - 1;
            while (left <= right) {
                int mid = left + (right - left) / 2;
                if (data[mid] == key) {
                    return mid;
                } else if (data[mid] < key) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            return -(left + 1); // 返回插入位置的负值
        }
        
        /**
         * 分裂块
         */
        Block split() {
            Block newBlock = new Block();
            int splitPoint = size / 2;
            
            // 将后半部分元素移动到新块
            for (int i = splitPoint; i < size; i++) {
                newBlock.data[i - splitPoint] = data[i];
            }
            newBlock.size = size - splitPoint;
            size = splitPoint;
            
            return newBlock;
        }
    }
    
    private Block head; // 头块指针
    private int size;   // 集合中元素总数
    
    /**
     * 构造函数
     */
    public LeetCode_705_DesignHashSet() {
        head = new Block();
        size = 0;
    }
    
    /**
     * 添加元素
     * 时间复杂度：O(√n) 平均情况
     * @param key 要添加的键
     */
    public void add(int key) {
        Block current = head;
        Block prev = null;
        
        // 找到合适的块
        while (current != null && (current.next != null || current.size >= Block.BLOCK_SIZE)) {
            if (current.size > 0 && key <= current.data[current.size - 1]) {
                break;
            }
            prev = current;
            current = current.next;
        }
        
        // 如果当前块为空或需要在新块中插入
        if (current == null) {
            current = new Block();
            if (prev != null) {
                prev.next = current;
            } else {
                head = current;
            }
        }
        
        // 尝试在当前块中添加元素
        if (!current.add(key)) {
            // 如果当前块已满，需要分裂
            Block newBlock = current.split();
            newBlock.next = current.next;
            current.next = newBlock;
            
            // 确定元素应该插入哪个块
            if (key <= current.data[current.size - 1]) {
                current.add(key);
            } else {
                newBlock.add(key);
            }
        }
        
        size++;
    }
    
    /**
     * 删除元素
     * 时间复杂度：O(√n) 平均情况
     * @param key 要删除的键
     */
    public void remove(int key) {
        Block current = head;
        
        while (current != null) {
            if (current.remove(key)) {
                size--;
                return;
            }
            current = current.next;
        }
    }
    
    /**
     * 检查是否包含元素
     * 时间复杂度：O(√n) 平均情况
     * @param key 要检查的键
     * @return 是否包含该键
     */
    public boolean contains(int key) {
        Block current = head;
        
        while (current != null) {
            if (current.contains(key)) {
                return true;
            }
            current = current.next;
        }
        
        return false;
    }
    
    /**
     * 方法2：链地址法实现
     * 时间复杂度：O(n/k) 平均情况，k是桶数
     * 空间复杂度：O(n)
     */
    static class MyHashSetChaining {
        private static final int BASE = 769; // 质数作为桶数
        private LinkedList<Integer>[] buckets;
        
        public MyHashSetChaining() {
            buckets = new LinkedList[BASE];
            for (int i = 0; i < BASE; i++) {
                buckets[i] = new LinkedList<>();
            }
        }
        
        private int hash(int key) {
            return key % BASE;
        }
        
        public void add(int key) {
            int bucketIndex = hash(key);
            if (!buckets[bucketIndex].contains(key)) {
                buckets[bucketIndex].add(key);
            }
        }
        
        public void remove(int key) {
            int bucketIndex = hash(key);
            buckets[bucketIndex].remove(Integer.valueOf(key));
        }
        
        public boolean contains(int key) {
            int bucketIndex = hash(key);
            return buckets[bucketIndex].contains(key);
        }
    }
    
    /**
     * 方法3：开放地址法实现（线性探测）
     * 时间复杂度：O(1) 平均情况
     * 空间复杂度：O(n)
     */
    static class MyHashSetOpenAddressing {
        private static final int CAPACITY = 100003; // 质数容量
        private static final int EMPTY = -1;
        private static final int DELETED = -2;
        private int[] table;
        
        public MyHashSetOpenAddressing() {
            table = new int[CAPACITY];
            Arrays.fill(table, EMPTY);
        }
        
        private int hash(int key) {
            return (key % CAPACITY + CAPACITY) % CAPACITY;
        }
        
        public void add(int key) {
            int index = find(key);
            if (table[index] != key) {
                table[index] = key;
            }
        }
        
        public void remove(int key) {
            int index = find(key);
            if (table[index] == key) {
                table[index] = DELETED;
            }
        }
        
        public boolean contains(int key) {
            int index = find(key);
            return table[index] == key;
        }
        
        private int find(int key) {
            int index = hash(key);
            while (table[index] != EMPTY && table[index] != key) {
                index = (index + 1) % CAPACITY;
            }
            return index;
        }
    }
    
    /**
     * 测试函数
     */
    public static void main(String[] args) {
        System.out.println("=== 测试 LeetCode 705. 设计哈希集合 ===");
        
        // 测试用例1
        System.out.println("测试用例1:");
        LeetCode_705_DesignHashSet hashSet = new LeetCode_705_DesignHashSet();
        
        hashSet.add(1);
        hashSet.add(2);
        System.out.println("add(1), add(2)");
        
        boolean result1 = hashSet.contains(1);
        System.out.println("contains(1) = " + result1 + " (期望: true)");
        
        boolean result2 = hashSet.contains(3);
        System.out.println("contains(3) = " + result2 + " (期望: false)");
        
        hashSet.add(2);
        System.out.println("add(2)");
        
        boolean result3 = hashSet.contains(2);
        System.out.println("contains(2) = " + result3 + " (期望: true)");
        
        hashSet.remove(2);
        System.out.println("remove(2)");
        
        boolean result4 = hashSet.contains(2);
        System.out.println("contains(2) = " + result4 + " (期望: false)");
        System.out.println();
        
        // 性能测试
        System.out.println("=== 性能测试 ===");
        LeetCode_705_DesignHashSet perfHashSet = new LeetCode_705_DesignHashSet();
        Random random = new Random(42);
        
        long startTime = System.nanoTime();
        
        // 添加10000个随机数
        for (int i = 0; i < 10000; i++) {
            int key = random.nextInt(100000);
            perfHashSet.add(key);
        }
        
        // 查询10000次
        int foundCount = 0;
        for (int i = 0; i < 10000; i++) {
            int key = random.nextInt(100000);
            if (perfHashSet.contains(key)) {
                foundCount++;
            }
        }
        
        long endTime = System.nanoTime();
        System.out.println("块状链表实现: 添加和查询20000次操作时间: " + (endTime - startTime) / 1_000_000.0 + " ms, 找到元素数: " + foundCount);
        
        // 测试链地址法
        MyHashSetChaining chainingHashSet = new MyHashSetChaining();
        startTime = System.nanoTime();
        
        for (int i = 0; i < 10000; i++) {
            int key = random.nextInt(100000);
            chainingHashSet.add(key);
        }
        
        foundCount = 0;
        for (int i = 0; i < 10000; i++) {
            int key = random.nextInt(100000);
            if (chainingHashSet.contains(key)) {
                foundCount++;
            }
        }
        
        endTime = System.nanoTime();
        System.out.println("链地址法实现: 添加和查询20000次操作时间: " + (endTime - startTime) / 1_000_000.0 + " ms, 找到元素数: " + foundCount);
        
        // 测试开放地址法
        MyHashSetOpenAddressing openHashSet = new MyHashSetOpenAddressing();
        startTime = System.nanoTime();
        
        for (int i = 0; i < 10000; i++) {
            int key = random.nextInt(100000);
            openHashSet.add(key);
        }
        
        foundCount = 0;
        for (int i = 0; i < 10000; i++) {
            int key = random.nextInt(100000);
            if (openHashSet.contains(key)) {
                foundCount++;
            }
        }
        
        endTime = System.nanoTime();
        System.out.println("开放地址法实现: 添加和查询20000次操作时间: " + (endTime - startTime) / 1_000_000.0 + " ms, 找到元素数: " + foundCount);
        
        // 算法复杂度分析
        System.out.println("\n=== 算法复杂度分析 ===");
        System.out.println("块状链表实现:");
        System.out.println("  时间复杂度: O(√n) 平均情况");
        System.out.println("  空间复杂度: O(n)");
        System.out.println("链地址法实现:");
        System.out.println("  时间复杂度: O(n/k) 平均情况，k是桶数");
        System.out.println("  空间复杂度: O(n)");
        System.out.println("开放地址法实现:");
        System.out.println("  时间复杂度: O(1) 平均情况");
        System.out.println("  空间复杂度: O(n)");
    }
}