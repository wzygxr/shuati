/**
 * LeetCode 706. 设计哈希映射 (Design HashMap)
 * 题目链接：https://leetcode.com/problems/design-hashmap/
 * 
 * 题目描述：
 * 不使用任何内建的哈希表库设计一个哈希映射（HashMap）。
 * 实现 MyHashMap 类：
 * - MyHashMap() 用空映射初始化对象
 * - void put(int key, int value) 向 HashMap 插入一个键值对 (key, value)。如果 key 已经存在于映射中，则更新其对应的值
 * - int get(int key) 返回特定的 key 所映射的 value；如果映射中不包含 key 的映射，返回 -1
 * - void remove(key) 如果映射中存在 key 的映射，则移除 key 和它所对应的 value
 * 
 * 算法思路：
 * 使用块状链表（分块哈希）实现哈希映射，结合了数组和链表的优点。
 * 将键值对分块存储，每个块包含固定数量的元素，使用链表连接各个块。
 * 
 * 时间复杂度：
 * - put/get/remove: O(n/b) 均摊，其中b是块大小
 * - 空间复杂度：O(n)
 * 
 * 最优解分析：
 * 块状链表在哈希冲突较多时性能优于传统链表，特别适合中等规模的数据。
 * 
 * 边界场景：
 * 1. 空映射操作
 * 2. 重复键插入
 * 3. 删除不存在的键
 * 4. 大量键值对时的性能
 * 
 * 工程化考量：
 * 1. 动态调整块大小以适应不同数据规模
 * 2. 添加负载因子监控和自动扩容
 * 3. 处理哈希冲突的优化策略
 */
package class185.unrolled_linked_list_problems;

import java.util.*;

public class LeetCode_706_DesignHashMap {
    
    // 哈希映射的块节点
    static class Block {
        private static final int DEFAULT_CAPACITY = 16;
        private int capacity;
        private int[] keys;
        private int[] values;
        private int size;
        private Block next;
        
        public Block(int capacity) {
            this.capacity = capacity;
            this.keys = new int[capacity];
            this.values = new int[capacity];
            this.size = 0;
            this.next = null;
            // 初始化数组为-1（表示空位置）
            Arrays.fill(keys, -1);
            Arrays.fill(values, -1);
        }
        
        public Block() {
            this(DEFAULT_CAPACITY);
        }
        
        public boolean isFull() {
            return size == capacity;
        }
        
        public boolean isEmpty() {
            return size == 0;
        }
        
        // 在块中查找键的位置
        public int findIndex(int key) {
            for (int i = 0; i < size; i++) {
                if (keys[i] == key) {
                    return i;
                }
            }
            return -1;
        }
        
        // 添加键值对
        public boolean put(int key, int value) {
            int index = findIndex(key);
            if (index != -1) {
                // 键已存在，更新值
                values[index] = value;
                return true;
            }
            
            if (isFull()) {
                return false; // 块已满
            }
            
            // 添加到末尾
            keys[size] = key;
            values[size] = value;
            size++;
            return true;
        }
        
        // 获取值
        public int get(int key) {
            int index = findIndex(key);
            return index != -1 ? values[index] : -1;
        }
        
        // 删除键值对
        public boolean remove(int key) {
            int index = findIndex(key);
            if (index == -1) {
                return false;
            }
            
            // 移动后续元素填补空缺
            for (int i = index; i < size - 1; i++) {
                keys[i] = keys[i + 1];
                values[i] = values[i + 1];
            }
            
            // 清空最后一个位置
            keys[size - 1] = -1;
            values[size - 1] = -1;
            size--;
            return true;
        }
    }
    
    static class MyHashMap {
        private static final int INITIAL_BLOCKS = 16;
        private static final double LOAD_FACTOR = 0.75;
        
        private Block[] blocks;
        private int size;
        private int blockCapacity;
        
        public MyHashMap() {
            this.blockCapacity = 16; // 每个块的容量
            this.blocks = new Block[INITIAL_BLOCKS];
            this.size = 0;
            
            // 初始化所有块
            for (int i = 0; i < INITIAL_BLOCKS; i++) {
                blocks[i] = new Block(blockCapacity);
            }
        }
        
        // 哈希函数
        private int hash(int key) {
            return key % blocks.length;
        }
        
        public void put(int key, int value) {
            int index = hash(key);
            Block current = blocks[index];
            
            // 在当前块链表中查找合适的块
            while (current != null) {
                if (current.put(key, value)) {
                    size++;
                    return;
                }
                if (current.next == null) {
                    // 当前块已满，创建新块
                    current.next = new Block(blockCapacity);
                }
                current = current.next;
            }
            
            // 检查是否需要扩容
            if ((double)size / (blocks.length * blockCapacity) > LOAD_FACTOR) {
                resize();
            }
        }
        
        public int get(int key) {
            int index = hash(key);
            Block current = blocks[index];
            
            while (current != null) {
                int value = current.get(key);
                if (value != -1) {
                    return value;
                }
                current = current.next;
            }
            
            return -1;
        }
        
        public void remove(int key) {
            int index = hash(key);
            Block current = blocks[index];
            
            while (current != null) {
                if (current.remove(key)) {
                    size--;
                    return;
                }
                current = current.next;
            }
        }
        
        // 扩容哈希表
        private void resize() {
            Block[] oldBlocks = blocks;
            blocks = new Block[oldBlocks.length * 2];
            
            // 初始化新块数组
            for (int i = 0; i < blocks.length; i++) {
                blocks[i] = new Block(blockCapacity);
            }
            
            // 重新哈希所有元素
            size = 0;
            for (Block oldBlock : oldBlocks) {
                Block current = oldBlock;
                while (current != null) {
                    for (int i = 0; i < current.size; i++) {
                        put(current.keys[i], current.values[i]);
                    }
                    current = current.next;
                }
            }
        }
        
        public int size() {
            return size;
        }
        
        public boolean isEmpty() {
            return size == 0;
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        System.out.println("=== 测试 LeetCode 706. 设计哈希映射 ===");
        
        MyHashMap hashMap = new MyHashMap();
        
        // 测试基本操作
        hashMap.put(1, 1);
        hashMap.put(2, 2);
        System.out.println("get(1): " + hashMap.get(1)); // 期望: 1
        System.out.println("get(3): " + hashMap.get(3)); // 期望: -1
        
        // 测试更新操作
        hashMap.put(2, 1);
        System.out.println("get(2): " + hashMap.get(2)); // 期望: 1
        
        // 测试删除操作
        hashMap.remove(2);
        System.out.println("get(2): " + hashMap.get(2)); // 期望: -1
        
        // 测试边界情况
        System.out.println("isEmpty: " + hashMap.isEmpty()); // 期望: false
        System.out.println("size: " + hashMap.size()); // 期望: 1
        
        // 测试大量数据
        for (int i = 0; i < 1000; i++) {
            hashMap.put(i, i * 2);
        }
        System.out.println("大量数据测试 - size: " + hashMap.size()); // 期望: 1000
        System.out.println("get(500): " + hashMap.get(500)); // 期望: 1000
        
        System.out.println("所有测试用例执行完成");
    }
}