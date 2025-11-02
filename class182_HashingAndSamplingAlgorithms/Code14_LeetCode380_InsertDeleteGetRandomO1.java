package class107_HashingAndSamplingAlgorithms;

import java.util.*;

/**
 * LeetCode 380. O(1) 时间插入、删除和获取随机元素
 * 题目链接：https://leetcode.com/problems/insert-delete-getrandom-o1/
 * 
 * 题目描述：
 * 实现RandomizedSet 类：
 * - RandomizedSet() 初始化 RandomizedSet 对象
 * - bool insert(int val) 当元素 val 不存在时，向集合中插入该项，并返回 true ；否则，返回 false 。
 * - bool remove(int val) 当元素 val 存在时，从集合中移除该项，并返回 true ；否则，返回 false 。
 * - int getRandom() 随机返回现有集合中的一项（测试用例保证调用此方法时集合中至少存在一个元素）。
 *   每个元素应该有相同概率被返回。
 * 
 * 算法思路：
 * 结合使用数组和哈希表来实现所有操作的O(1)时间复杂度：
 * 1. 使用数组存储元素，支持O(1)随机访问
 * 2. 使用哈希表存储元素到数组索引的映射，支持O(1)查找
 * 3. 删除操作通过将待删除元素与数组末尾元素交换，然后删除末尾元素实现O(1)
 * 
 * 时间复杂度：
 * - insert: O(1) 平均情况
 * - remove: O(1) 平均情况
 * - getRandom: O(1)
 * 
 * 空间复杂度：O(n)，其中n是集合中元素的数量
 * 
 * 工程化考量：
 * 1. 边界情况处理：空集合、重复插入/删除
 * 2. 随机性保证：使用Java内置Random类确保等概率随机选择
 * 3. 内存管理：动态数组自动扩容
 * 4. 异常处理：输入验证和错误恢复
 */
public class Code14_LeetCode380_InsertDeleteGetRandomO1 {
    
    static class RandomizedSet {
        private List<Integer> nums;           // 存储元素的数组
        private Map<Integer, Integer> indices; // 元素到索引的映射
        private Random random;                // 随机数生成器
        
        /** Initialize your data structure here. */
        public RandomizedSet() {
            nums = new ArrayList<>();
            indices = new HashMap<>();
            random = new Random();
        }
        
        /**
         * Inserts a value to the set. Returns true if the set did not already contain the specified element.
         * 
         * @param val 要插入的值
         * @return 如果集合中不存在该元素则插入并返回true，否则返回false
         */
        public boolean insert(int val) {
            // 如果元素已存在，返回false
            if (indices.containsKey(val)) {
                return false;
            }
            
            // 将元素添加到数组末尾
            int index = nums.size();
            nums.add(val);
            // 在哈希表中记录元素到索引的映射
            indices.put(val, index);
            return true;
        }
        
        /**
         * Removes a value from the set. Returns true if the set contained the specified element.
         * 
         * @param val 要删除的值
         * @return 如果集合中存在该元素则删除并返回true，否则返回false
         */
        public boolean remove(int val) {
            // 如果元素不存在，返回false
            if (!indices.containsKey(val)) {
                return false;
            }
            
            // 获取要删除元素的索引
            int index = indices.get(val);
            // 获取数组最后一个元素
            int lastElement = nums.get(nums.size() - 1);
            
            // 将最后一个元素移动到要删除元素的位置
            nums.set(index, lastElement);
            // 更新最后一个元素在哈希表中的索引
            indices.put(lastElement, index);
            
            // 删除数组最后一个元素
            nums.remove(nums.size() - 1);
            // 从哈希表中删除该元素
            indices.remove(val);
            return true;
        }
        
        /**
         * Get a random element from the set.
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
        System.out.println("=== 测试 LeetCode 380. O(1) 时间插入、删除和获取随机元素 ===");
        
        RandomizedSet randomizedSet = new RandomizedSet();
        
        // 测试插入操作
        System.out.println("插入1: " + randomizedSet.insert(1)); // 期望: true
        System.out.println("插入2: " + randomizedSet.insert(2)); // 期望: true
        System.out.println("重复插入2: " + randomizedSet.insert(2)); // 期望: false
        System.out.println("当前大小: " + randomizedSet.size()); // 期望: 2
        
        // 测试随机获取操作
        System.out.println("随机获取元素:");
        for (int i = 0; i < 5; i++) {
            System.out.println("  随机元素: " + randomizedSet.getRandom());
        }
        
        // 测试删除操作
        System.out.println("删除2: " + randomizedSet.remove(2)); // 期望: true
        System.out.println("删除不存在的3: " + randomizedSet.remove(3)); // 期望: false
        System.out.println("删除后大小: " + randomizedSet.size()); // 期望: 1
        
        // 测试边界情况
        System.out.println("删除1: " + randomizedSet.remove(1)); // 期望: true
        System.out.println("删除后是否为空: " + randomizedSet.isEmpty()); // 期望: true
        System.out.println("空集合大小: " + randomizedSet.size()); // 期望: 0
        
        // 重新插入测试
        System.out.println("重新插入多个元素:");
        for (int i = 1; i <= 10; i++) {
            System.out.println("  插入" + i + ": " + randomizedSet.insert(i));
        }
        System.out.println("重新插入后大小: " + randomizedSet.size()); // 期望: 10
        
        // 大量随机测试
        System.out.println("大量随机获取测试:");
        Map<Integer, Integer> count = new HashMap<>();
        for (int i = 0; i < 10000; i++) {
            int val = randomizedSet.getRandom();
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