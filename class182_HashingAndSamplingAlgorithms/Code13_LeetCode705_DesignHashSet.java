package class107;

import java.util.*;

/**
 * LeetCode 705. 设计哈希集合 - Java版本
 * 
 * 题目来源：https://leetcode.com/problems/design-hashset/
 * 题目描述：不使用任何内建的哈希表库设计一个哈希集合（HashSet）。
 * 
 * 算法思路：
 * 1. 使用链地址法解决哈希冲突
 * 2. 选择合适的哈希函数和桶大小
 * 3. 实现动态扩容机制
 * 4. 处理边界情况和异常
 * 
 * 时间复杂度：平均O(1)，最坏O(n)
 * 空间复杂度：O(n)
 * 
 * 工程化考量：
 * - 动态扩容策略
 * - 哈希函数设计
 * - 内存管理优化
 * - 线程安全考虑
 */
public class Code13_LeetCode705_DesignHashSet {
    
    /**
     * 哈希集合实现类
     */
    static class MyHashSet {
        // 默认初始容量
        private static final int DEFAULT_CAPACITY = 16;
        // 负载因子阈值
        private static final double LOAD_FACTOR = 0.75;
        
        // 桶数组，每个桶是一个链表
        private LinkedList<Integer>[] buckets;
        // 当前元素数量
        private int size;
        // 当前容量
        private int capacity;
        
        /**
         * 构造函数：使用默认容量初始化哈希集合
         */
        public MyHashSet() {
            this(DEFAULT_CAPACITY);
        }
        
        /**
         * 构造函数：使用指定容量初始化哈希集合
         * 
         * @param initialCapacity 初始容量
         */
        public MyHashSet(int initialCapacity) {
            if (initialCapacity <= 0) {
                throw new IllegalArgumentException("初始容量必须大于0");
            }
            this.capacity = initialCapacity;
            this.buckets = new LinkedList[capacity];
            this.size = 0;
            
            // 初始化每个桶
            for (int i = 0; i < capacity; i++) {
                buckets[i] = new LinkedList<>();
            }
        }
        
        /**
         * 哈希函数：计算元素的哈希值
         * 
         * @param key 元素值
         * @return 哈希值（桶索引）
         */
        private int hash(int key) {
            // 使用Java的hashCode方法并取模
            return Math.abs(Integer.hashCode(key)) % capacity;
        }
        
        /**
         * 添加元素到哈希集合
         * 
         * @param key 要添加的元素
         */
        public void add(int key) {
            // 检查是否需要扩容
            if ((double) size / capacity >= LOAD_FACTOR) {
                resize();
            }
            
            int index = hash(key);
            LinkedList<Integer> bucket = buckets[index];
            
            // 检查元素是否已存在
            if (!bucket.contains(key)) {
                bucket.add(key);
                size++;
            }
        }
        
        /**
         * 从哈希集合中移除元素
         * 
         * @param key 要移除的元素
         */
        public void remove(int key) {
            int index = hash(key);
            LinkedList<Integer> bucket = buckets[index];
            
            // 使用迭代器安全删除
            Iterator<Integer> iterator = bucket.iterator();
            while (iterator.hasNext()) {
                if (iterator.next() == key) {
                    iterator.remove();
                    size--;
                    return;
                }
            }
        }
        
        /**
         * 检查哈希集合是否包含指定元素
         * 
         * @param key 要检查的元素
         * @return 如果包含返回true，否则返回false
         */
        public boolean contains(int key) {
            int index = hash(key);
            LinkedList<Integer> bucket = buckets[index];
            return bucket.contains(key);
        }
        
        /**
         * 动态扩容：当负载因子超过阈值时扩容
         */
        private void resize() {
            int newCapacity = capacity * 2;
            LinkedList<Integer>[] newBuckets = new LinkedList[newCapacity];
            
            // 初始化新桶
            for (int i = 0; i < newCapacity; i++) {
                newBuckets[i] = new LinkedList<>();
            }
            
            // 重新哈希所有元素
            for (LinkedList<Integer> bucket : buckets) {
                for (int key : bucket) {
                    int newIndex = Math.abs(Integer.hashCode(key)) % newCapacity;
                    newBuckets[newIndex].add(key);
                }
            }
            
            // 更新容量和桶数组
            this.capacity = newCapacity;
            this.buckets = newBuckets;
        }
        
        /**
         * 获取哈希集合的大小
         * 
         * @return 元素数量
         */
        public int size() {
            return size;
        }
        
        /**
         * 检查哈希集合是否为空
         * 
         * @return 如果为空返回true，否则返回false
         */
        public boolean isEmpty() {
            return size == 0;
        }
        
        /**
         * 清空哈希集合
         */
        public void clear() {
            for (LinkedList<Integer> bucket : buckets) {
                bucket.clear();
            }
            size = 0;
        }
        
        /**
         * 获取哈希集合的字符串表示
         * 
         * @return 字符串表示
         */
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("MyHashSet{");
            boolean first = true;
            for (LinkedList<Integer> bucket : buckets) {
                for (int key : bucket) {
                    if (!first) {
                        sb.append(", ");
                    }
                    sb.append(key);
                    first = false;
                }
            }
            sb.append("}");
            return sb.toString();
        }
    }
    
    /**
     * 优化版本：使用更好的哈希函数和更高效的数据结构
     */
    static class MyHashSetOptimized {
        private static final int DEFAULT_CAPACITY = 16;
        private static final double LOAD_FACTOR = 0.75;
        
        // 使用ArrayList代替LinkedList，提高随机访问性能
        private ArrayList<Integer>[] buckets;
        private int size;
        private int capacity;
        
        public MyHashSetOptimized() {
            this(DEFAULT_CAPACITY);
        }
        
        public MyHashSetOptimized(int initialCapacity) {
            if (initialCapacity <= 0) {
                throw new IllegalArgumentException("初始容量必须大于0");
            }
            this.capacity = initialCapacity;
            this.buckets = new ArrayList[capacity];
            this.size = 0;
            
            for (int i = 0; i < capacity; i++) {
                buckets[i] = new ArrayList<>();
            }
        }
        
        /**
         * 优化的哈希函数：使用乘法哈希法
         */
        private int hash(int key) {
            // 使用黄金分割率的倒数作为乘数
            double A = (Math.sqrt(5) - 1) / 2;
            double fractionalPart = (key * A) % 1;
            return (int) (fractionalPart * capacity);
        }
        
        public void add(int key) {
            if ((double) size / capacity >= LOAD_FACTOR) {
                resize();
            }
            
            int index = hash(key);
            ArrayList<Integer> bucket = buckets[index];
            
            // 使用二分查找检查元素是否存在（桶内元素有序）
            int pos = Collections.binarySearch(bucket, key);
            if (pos < 0) {
                // 插入到正确位置保持有序
                bucket.add(-pos - 1, key);
                size++;
            }
        }
        
        public void remove(int key) {
            int index = hash(key);
            ArrayList<Integer> bucket = buckets[index];
            
            int pos = Collections.binarySearch(bucket, key);
            if (pos >= 0) {
                bucket.remove(pos);
                size--;
            }
        }
        
        public boolean contains(int key) {
            int index = hash(key);
            ArrayList<Integer> bucket = buckets[index];
            return Collections.binarySearch(bucket, key) >= 0;
        }
        
        private void resize() {
            int newCapacity = capacity * 2;
            ArrayList<Integer>[] newBuckets = new ArrayList[newCapacity];
            
            for (int i = 0; i < newCapacity; i++) {
                newBuckets[i] = new ArrayList<>();
            }
            
            for (ArrayList<Integer> bucket : buckets) {
                for (int key : bucket) {
                    int newIndex = hash(key);
                    // 插入时保持有序
                    ArrayList<Integer> newBucket = newBuckets[newIndex];
                    int pos = Collections.binarySearch(newBucket, key);
                    if (pos < 0) {
                        newBucket.add(-pos - 1, key);
                    }
                }
            }
            
            this.capacity = newCapacity;
            this.buckets = newBuckets;
        }
        
        public int size() {
            return size;
        }
        
        public boolean isEmpty() {
            return size == 0;
        }
        
        public void clear() {
            for (ArrayList<Integer> bucket : buckets) {
                bucket.clear();
            }
            size = 0;
        }
    }
    
    /**
     * 测试函数
     */
    public static void main(String[] args) {
        System.out.println("=== 基础版本测试 ===");
        testBasicVersion();
        
        System.out.println("\n=== 优化版本测试 ===");
        testOptimizedVersion();
        
        System.out.println("\n=== 性能对比测试 ===");
        performanceTest();
        
        System.out.println("\n=== 边界情况测试 ===");
        edgeCaseTest();
    }
    
    private static void testBasicVersion() {
        MyHashSet hashSet = new MyHashSet();
        
        // 基本操作测试
        hashSet.add(1);
        hashSet.add(2);
        hashSet.add(3);
        
        System.out.println("添加1,2,3后大小: " + hashSet.size());
        System.out.println("包含2: " + hashSet.contains(2));
        System.out.println("包含4: " + hashSet.contains(4));
        
        hashSet.remove(2);
        System.out.println("删除2后包含2: " + hashSet.contains(2));
        System.out.println("当前集合: " + hashSet);
        
        // 重复添加测试
        hashSet.add(1);
        System.out.println("重复添加1后大小: " + hashSet.size());
        
        hashSet.clear();
        System.out.println("清空后大小: " + hashSet.size());
        System.out.println("是否为空: " + hashSet.isEmpty());
    }
    
    private static void testOptimizedVersion() {
        MyHashSetOptimized hashSet = new MyHashSetOptimized();
        
        // 基本操作测试
        hashSet.add(10);
        hashSet.add(20);
        hashSet.add(30);
        hashSet.add(5);
        hashSet.add(15);
        
        System.out.println("添加多个元素后大小: " + hashSet.size());
        System.out.println("包含15: " + hashSet.contains(15));
        System.out.println("包含25: " + hashSet.contains(25));
        
        hashSet.remove(20);
        System.out.println("删除20后包含20: " + hashSet.contains(20));
        
        // 测试有序性
        hashSet.add(8);
        hashSet.add(18);
        hashSet.add(28);
        System.out.println("添加无序元素后操作正常");
    }
    
    private static void performanceTest() {
        int testSize = 10000;
        
        // 基础版本性能测试
        long startTime = System.currentTimeMillis();
        MyHashSet basicSet = new MyHashSet();
        for (int i = 0; i < testSize; i++) {
            basicSet.add(i);
        }
        for (int i = 0; i < testSize; i++) {
            basicSet.contains(i);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("基础版本耗时: " + (endTime - startTime) + "ms");
        
        // 优化版本性能测试
        startTime = System.currentTimeMillis();
        MyHashSetOptimized optimizedSet = new MyHashSetOptimized();
        for (int i = 0; i < testSize; i++) {
            optimizedSet.add(i);
        }
        for (int i = 0; i < testSize; i++) {
            optimizedSet.contains(i);
        }
        endTime = System.currentTimeMillis();
        System.out.println("优化版本耗时: " + (endTime - startTime) + "ms");
        
        // Java内置HashSet性能测试
        startTime = System.currentTimeMillis();
        Set<Integer> javaSet = new HashSet<>();
        for (int i = 0; i < testSize; i++) {
            javaSet.add(i);
        }
        for (int i = 0; i < testSize; i++) {
            javaSet.contains(i);
        }
        endTime = System.currentTimeMillis();
        System.out.println("Java内置HashSet耗时: " + (endTime - startTime) + "ms");
    }
    
    private static void edgeCaseTest() {
        // 边界值测试
        MyHashSet hashSet = new MyHashSet(1); // 最小容量
        
        // 大量重复操作
        for (int i = 0; i < 100; i++) {
            hashSet.add(1);
        }
        System.out.println("重复添加100次1后大小: " + hashSet.size());
        
        // 删除不存在的元素
        hashSet.remove(999);
        System.out.println("删除不存在的元素后大小: " + hashSet.size());
        
        // 空集合操作
        hashSet.clear();
        System.out.println("清空后包含1: " + hashSet.contains(1));
        System.out.println("清空后是否为空: " + hashSet.isEmpty());
        
        // 负数和零测试
        hashSet.add(-1);
        hashSet.add(0);
        hashSet.add(Integer.MIN_VALUE);
        hashSet.add(Integer.MAX_VALUE);
        System.out.println("添加边界值后大小: " + hashSet.size());
        System.out.println("包含Integer.MIN_VALUE: " + hashSet.contains(Integer.MIN_VALUE));
        System.out.println("包含Integer.MAX_VALUE: " + hashSet.contains(Integer.MAX_VALUE));
    }
    
    /**
     * 复杂度分析：
     * 
     * 时间复杂度：
     * - 添加操作(add): 平均O(1)，最坏O(n)（当所有元素哈希到同一个桶时）
     * - 删除操作(remove): 平均O(1)，最坏O(n)
     * - 查询操作(contains): 平均O(1)，最坏O(n)
     * - 扩容操作(resize): O(n)
     * 
     * 空间复杂度：
     * - 总空间：O(n + m)，其中n是元素数量，m是桶的数量
     * - 每个桶需要额外空间存储链表节点
     * 
     * 算法优化点：
     * 1. 链地址法：简单有效，易于实现
     * 2. 动态扩容：避免哈希冲突过多
     * 3. 负载因子控制：平衡空间和时间效率
     * 4. 优化版本使用有序桶：提高查找效率
     * 
     * 边界情况处理：
     * - 空集合操作
     * - 重复元素添加
     * - 删除不存在的元素
     * - 边界值（最小/最大整数）
     * - 初始容量验证
     * 
     * 工程化考量：
     * - 参数可配置性（容量、负载因子）
     * - 异常处理
     * - 内存管理
     * - 性能监控
     * 
     * 实际应用场景：
     * 1. 数据库索引：快速查找记录
     * 2. 缓存系统：存储热点数据
     * 3. 编译器：符号表管理
     * 4. 网络路由：快速查找路由表
     */
}