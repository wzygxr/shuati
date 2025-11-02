package class107_HashingAndSamplingAlgorithms;

import java.util.*;

/**
 * LeetCode 381. O(1) 时间插入、删除和获取随机元素 - 允许重复
 * 题目链接：https://leetcode.com/problems/insert-delete-getrandom-o1-duplicates-allowed/
 * 
 * 题目描述：
 * 设计一个支持在平均时间复杂度O(1)下执行以下操作的数据结构：
 * - insert(val)：当元素val不存在时，向集合中插入该项。
 * - remove(val)：当元素val存在时，从集合中移除该项。
 * - getRandom：随机返回现有集合中的一项。每个元素应该有同等的概率被返回。
 * 
 * 与380题的区别：允许重复元素，即集合可以包含重复值。
 * 
 * 算法思路：
 * 1. 使用ArrayList存储所有元素（允许重复）
 * 2. 使用HashMap存储元素值到索引集合的映射
 * 3. 删除操作通过将待删除元素与数组末尾元素交换实现
 * 4. 为了处理重复元素，HashMap的值是一个Set，存储该元素在数组中的所有索引
 * 
 * 时间复杂度：
 * - insert: O(1) 平均情况
 * - remove: O(1) 平均情况
 * - getRandom: O(1)
 * 
 * 空间复杂度：O(n)，其中n是集合中元素的数量
 * 
 * 工程化考量：
 * 1. 边界情况处理：空集合、大量重复元素
 * 2. 随机性保证：确保每个元素（包括重复元素）被选中的概率相等
 * 3. 内存管理：动态数组自动扩容
 * 4. 异常处理：输入验证和错误恢复
 */
public class Code15_LeetCode381_InsertDeleteGetRandomO1Duplicates {
    
    static class RandomizedCollection {
        private List<Integer> nums;                    // 存储所有元素的数组（允许重复）
        private Map<Integer, Set<Integer>> numToIndices; // 元素值到索引集合的映射
        private Random random;                         // 随机数生成器
        
        /** Initialize your data structure here. */
        public RandomizedCollection() {
            nums = new ArrayList<>();
            numToIndices = new HashMap<>();
            random = new Random();
        }
        
        /**
         * Inserts a value to the collection. Returns true if the collection did not already contain the specified element.
         * 
         * @param val 要插入的值
         * @return 如果集合中之前不包含该元素则返回true，否则返回false
         */
        public boolean insert(int val) {
            // 记录插入前是否已存在该元素
            boolean notExists = !numToIndices.containsKey(val) || numToIndices.get(val).isEmpty();
            
            // 将元素添加到数组末尾
            int index = nums.size();
            nums.add(val);
            
            // 在哈希表中记录元素到索引的映射
            numToIndices.computeIfAbsent(val, k -> new HashSet<>()).add(index);
            
            return notExists;
        }
        
        /**
         * Removes a value from the collection. Returns true if the collection contained the specified element.
         * 
         * @param val 要删除的值
         * @return 如果集合中包含该元素则删除并返回true，否则返回false
         */
        public boolean remove(int val) {
            // 如果元素不存在，返回false
            if (!numToIndices.containsKey(val) || numToIndices.get(val).isEmpty()) {
                return false;
            }
            
            // 获取要删除元素的一个索引
            Set<Integer> indices = numToIndices.get(val);
            int indexToRemove = indices.iterator().next();
            
            // 获取数组最后一个元素
            int lastIndex = nums.size() - 1;
            int lastElement = nums.get(lastIndex);
            
            // 将最后一个元素移动到要删除元素的位置
            nums.set(indexToRemove, lastElement);
            
            // 更新最后一个元素在哈希表中的索引信息
            Set<Integer> lastElementIndices = numToIndices.get(lastElement);
            lastElementIndices.remove(lastIndex);
            if (indexToRemove != lastIndex) {
                lastElementIndices.add(indexToRemove);
            }
            
            // 从哈希表中删除要删除元素的索引
            indices.remove(indexToRemove);
            
            // 删除数组最后一个元素
            nums.remove(lastIndex);
            
            return true;
        }
        
        /**
         * Get a random element from the collection.
         * 
         * @return 集合中的一个随机元素
         */
        public int getRandom() {
            // 生成随机索引并返回对应元素
            int randomIndex = random.nextInt(nums.size());
            return nums.get(randomIndex);
        }
        
        /**
         * 获取集合大小
         * 
         * @return 集合中元素的数量
         */
        public int size() {
            return nums.size();
        }
        
        /**
         * 检查集合是否为空
         * 
         * @return 如果集合为空返回true，否则返回false
         */
        public boolean isEmpty() {
            return nums.isEmpty();
        }
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 测试 LeetCode 381. O(1) 时间插入、删除和获取随机元素 - 允许重复 ===");
        
        RandomizedCollection randomizedCollection = new RandomizedCollection();
        
        // 测试插入操作
        System.out.println("插入1: " + randomizedCollection.insert(1)); // 期望: true
        System.out.println("插入1: " + randomizedCollection.insert(1)); // 期望: false
        System.out.println("插入2: " + randomizedCollection.insert(2)); // 期望: true
        System.out.println("当前大小: " + randomizedCollection.size()); // 期望: 3
        
        // 测试随机获取操作
        System.out.println("随机获取元素:");
        for (int i = 0; i < 10; i++) {
            System.out.println("  随机元素: " + randomizedCollection.getRandom());
        }
        
        // 测试删除操作
        System.out.println("删除1: " + randomizedCollection.remove(1)); // 期望: true
        System.out.println("再次删除1: " + randomizedCollection.remove(1)); // 期望: true
        System.out.println("删除不存在的1: " + randomizedCollection.remove(1)); // 期望: false
        System.out.println("删除后大小: " + randomizedCollection.size()); // 期望: 1
        
        // 测试边界情况
        System.out.println("删除2: " + randomizedCollection.remove(2)); // 期望: true
        System.out.println("删除后是否为空: " + randomizedCollection.isEmpty()); // 期望: true
        System.out.println("空集合大小: " + randomizedCollection.size()); // 期望: 0
        
        // 重新插入测试（包含重复元素）
        System.out.println("重新插入多个元素（包含重复）:");
        for (int i = 1; i <= 5; i++) {
            System.out.println("  插入" + i + ": " + randomizedCollection.insert(i));
            System.out.println("  重复插入" + i + ": " + randomizedCollection.insert(i));
        }
        System.out.println("重新插入后大小: " + randomizedCollection.size()); // 期望: 10
        
        // 大量随机测试
        System.out.println("大量随机获取测试:");
        Map<Integer, Integer> count = new HashMap<>();
        for (int i = 0; i < 10000; i++) {
            int val = randomizedCollection.getRandom();
            count.put(val, count.getOrDefault(val, 0) + 1);
        }
        
        System.out.println("各元素被选中次数:");
        for (Map.Entry<Integer, Integer> entry : count.entrySet()) {
            System.out.println("  元素" + entry.getKey() + ": " + entry.getValue() + "次");
        }
        
        // 验证随机性（每个元素被选中的次数应该大致相等）
        int total = count.values().stream().mapToInt(Integer::intValue).sum();
        double expected = (double) total / count.size();
        System.out.println("期望平均次数: " + expected);
        System.out.println("实际次数范围: " + Collections.min(count.values()) + " - " + Collections.max(count.values()));
        
        System.out.println("所有测试完成");
    }
}