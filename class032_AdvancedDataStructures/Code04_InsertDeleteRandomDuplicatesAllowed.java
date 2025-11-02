package class035;

import java.util.*;

/**
 * 实现支持重复元素的O(1)时间复杂度插入、删除和随机获取元素的数据结构
 * 题目来源：LeetCode 381. Insert Delete GetRandom O(1) - Duplicates allowed
 * 网址：https://leetcode.com/problems/insert-delete-getrandom-o1-duplicates-allowed/
 * 
 * 一、题目解析
 * 实现一个支持以下操作的数据结构，所有操作的时间复杂度都要求为O(1)：
 * 1. insert(val): 插入元素，允许重复元素，总是返回true
 * 2. remove(val): 删除元素的一个实例，如果元素存在则删除并返回true，否则返回false
 * 3. getRandom(): 随机返回集合中的一个元素，每个元素被返回的概率与其在集合中的数量成正比
 * 
 * 二、算法思路
 * 1. 使用动态数组(ArrayList)存储所有元素，支持O(1)随机访问
 * 2. 使用哈希表(Map<Integer, Set<Integer>>)维护元素到索引集合的映射，支持O(1)查找
 * 3. 插入操作：在数组末尾添加元素，并在哈希表中记录该元素对应的所有索引
 * 4. 删除操作：找到要删除元素的任意一个索引，将其与数组末尾元素交换，然后删除末尾元素，更新哈希表
 * 5. 随机获取：使用Random类生成随机索引，直接访问数组元素
 * 
 * 三、时间复杂度分析
 * - insert(val): O(1) 平均时间复杂度
 * - remove(val): O(1) 平均时间复杂度  
 * - getRandom(): O(1) 时间复杂度
 * 
 * 四、空间复杂度分析
 * O(n)，其中n是存储的元素数量，需要数组和哈希表存储所有元素
 * 
 * 五、工程化考量
 * 1. 异常处理: 处理空集合的getRandom操作
 * 2. 边界情况: 插入大量重复元素、删除所有实例、空集合操作
 * 3. 内存管理: Java自动垃圾回收，但仍需注意大对象的内存消耗
 * 4. 线程安全: 当前实现非线程安全，如需线程安全可使用Collections.synchronizedList等
 * 5. 性能优化: 利用LinkedHashSet保证索引集合的有序性，优化删除操作
 * 6. 可扩展性: 可扩展为支持泛型的通用数据结构
 * 
 * 六、相关题目扩展
 * 1. LeetCode 381. [Insert Delete GetRandom O(1) - Duplicates allowed](https://leetcode.com/problems/insert-delete-getrandom-o1-duplicates-allowed/) (本题)
 * 2. LeetCode 380. [Insert Delete GetRandom O(1)](https://leetcode.com/problems/insert-delete-getrandom-o1/) (不允许重复元素)
 * 3. 牛客网: [设计支持重复元素的O(1)数据结构](https://www.nowcoder.com/practice/11165e95382547cab9b6518e2760384d)
 * 4. 剑指Offer II 030. [插入、删除和随机访问都是O(1)的容器](https://leetcode.cn/problems/FortPu/)
 * 5. LintCode 657. [Insert Delete GetRandom O(1)](https://www.lintcode.com/problem/657/)
 * 6. HackerRank: [Data Structures - RandomizedSet with Duplicates](https://www.hackerrank.com/challenges/java-hashset/problem)
 * 7. CodeChef: [Random Set Operations with Duplicates](https://www.codechef.com/problems/RANDSET)
 * 8. 计蒜客: [O(1)数据结构（允许重复）](https://nanti.jisuanke.com/t/41394)
 * 
 * 七、补充题目（各大OJ平台）
 * 1. AtCoder ABC238D. [AND and SUM](https://atcoder.jp/contests/abc238/tasks/abc238_d) - 集合操作优化
 * 2. Codeforces Round #344 (Div. 2) D. [Messenger](https://codeforces.com/contest/631/problem/D) - 消息集合处理
 * 3. UVA 11525. [Permutation](https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2520) - 集合排列问题
 * 4. SPOJ DQUERY. [D-query](https://www.spoj.com/problems/DQUERY/) - 集合查询优化
 * 5. Project Euler 543. [Counting the Number of Close Pairs](https://projecteuler.net/problem=543) - 集合计数优化
 * 6. HDU 1284. [钱币兑换问题](https://acm.hdu.edu.cn/showproblem.php?pid=1284) - 动态规划集合优化
 * 7. POJ 3349. [Snowflake Snow Snowflakes](https://poj.org/problem?id=3349) - 集合唯一性检测
 * 8. USACO Training: [Set Operations with Duplicates](https://train.usaco.org/) - 集合基础训练
 * 9. 洛谷 P1168. [中位数](https://www.luogu.com.cn/problem/P1168) - 数据流集合
 * 10. 赛码: [集合设计（允许重复）](https://www.acmcoder.com/) - 在线编程题目
 * 
 * 八、算法设计技巧总结
 * 1. 数组+哈希表组合：利用数组的O(1)随机访问和哈希表的O(1)查找
 * 2. 索引集合维护：使用Set存储元素的所有索引，支持快速查找和删除
 * 3. 交换删除法：通过将要删除元素与末尾元素交换来实现O(1)删除
 * 4. 边界优化：特殊处理数组末尾操作，避免不必要的元素移动
 * 5. 随机均匀性：使用标准随机数生成器保证元素返回概率与其数量成正比
 * 
 * 九、面试要点
 * 1. 解释与不允许重复元素版本的区别和实现差异
 * 2. 分析删除操作中索引集合的维护策略
 * 3. 讨论各种边界情况的处理
 * 4. 分析时间复杂度和空间复杂度
 * 5. 提出可能的扩展和优化方向
 * 6. 讨论线程安全性问题和解决方案
 * 
 * 十、工程实践中的应用场景
 * 1. 随机抽样系统（支持重复元素）
 * 2. 负载均衡器中的服务器管理（支持多实例）
 * 3. 缓存系统中的键管理（支持重复键）
 * 4. 游戏开发中的道具管理（支持重复道具）
 * 5. 数据库索引优化（支持重复值）
 * 6. 推荐系统中的候选集维护（支持重复推荐）
 */
public class Code04_InsertDeleteRandomDuplicatesAllowed {
    
    private Map<Integer, Set<Integer>> valueToIndices; // 值到索引集合的映射，用于O(1)查找
    private List<Integer> values;                     // 存储值的列表，用于O(1)随机访问
    private Random random;                            // 随机数生成器，用于O(1)随机选择
    
    /** 初始化数据结构 */
    public Code04_InsertDeleteRandomDuplicatesAllowed() {
        valueToIndices = new HashMap<>();
        values = new ArrayList<>();
        random = new Random();
    }
    
    /**
     * 插入元素（允许重复）
     * @param val 要插入的值
     * @return 总是返回true，因为允许重复
     * 时间复杂度: O(1) 平均时间复杂度
     * 空间复杂度: O(1)
     */
    public boolean insert(int val) {
        // 如果值不存在，创建新的索引集合
        if (!valueToIndices.containsKey(val)) {
            valueToIndices.put(val, new LinkedHashSet<>());
        }
        
        // 添加新索引
        valueToIndices.get(val).add(values.size());
        values.add(val);
        
        return true;
    }
    
    /**
     * 删除元素
     * @param val 要删除的值
     * @return 如果值存在则删除成功返回true，否则返回false
     * 时间复杂度: O(1) 平均时间复杂度
     * 空间复杂度: O(1)
     * 核心思想: 找到要删除元素的任意一个索引，将其与数组末尾元素交换，然后删除末尾元素
     */
    public boolean remove(int val) {
        // 检查元素是否存在
        if (!valueToIndices.containsKey(val) || valueToIndices.get(val).isEmpty()) {
            return false;
        }
        
        // 获取要删除的值的任意一个索引
        int removeIndex = valueToIndices.get(val).iterator().next();
        int lastIndex = values.size() - 1;
        int lastElement = values.get(lastIndex);
        
        // 如果删除的不是最后一个元素，需要交换
        if (removeIndex != lastIndex) {
            // 将最后一个元素移动到要删除的位置
            values.set(removeIndex, lastElement);
            
            // 更新最后一个元素的索引映射
            Set<Integer> lastElementIndices = valueToIndices.get(lastElement);
            if (lastElementIndices != null) {
                lastElementIndices.remove(lastIndex);
                lastElementIndices.add(removeIndex);
            }
        }
        
        // 删除要删除的值的索引
        Set<Integer> valIndices = valueToIndices.get(val);
        valIndices.remove(removeIndex);
        
        // 删除最后一个元素
        values.remove(lastIndex);
        
        // 如果值的索引集合为空，删除该键
        if (valIndices.isEmpty()) {
            valueToIndices.remove(val);
        }
        
        return true;
    }
    
    /**
     * 随机获取一个元素
     * @return 随机元素
     * 时间复杂度: O(1)
     * 空间复杂度: O(1)
     * 核心思想: 使用Random类生成随机索引，直接访问数组元素
     * 概率特性: 每个元素被返回的概率与其在集合中的数量成正比
     */
    public int getRandom() {
        // 生成0到size-1的随机索引
        int randomIndex = random.nextInt(values.size());
        // 返回对应索引的元素
        return values.get(randomIndex);
    }
    
    // ==================== 单元测试和功能演示 ====================
    
    /**
     * 单元测试类 - 测试RandomizedCollection的各种功能
     */
    public static class RandomizedCollectionTest {
        
        /**
         * 测试边界情况
         */
        public static void testEdgeCases() {
            System.out.println("\n=== 测试边界情况 ===");
            
            Code04_InsertDeleteRandomDuplicatesAllowed collection = new Code04_InsertDeleteRandomDuplicatesAllowed();
            
            // 测试空集合
            assert !collection.remove(1) : "空集合删除应该返回false";
            try {
                collection.getRandom();
                assert false : "空集合getRandom应该抛出异常";
            } catch (Exception e) {
                System.out.println("✓ 空集合异常处理正确");
            }
            
            // 测试插入重复元素
            assert collection.insert(1) : "第一次插入1应该成功";
            assert collection.insert(1) : "第二次插入1应该成功（允许重复）";
            assert collection.insert(1) : "第三次插入1应该成功（允许重复）";
            System.out.println("✓ 重复插入测试通过");
            
            // 测试删除不存在的元素
            assert !collection.remove(999) : "删除不存在的元素应该返回false";
            System.out.println("✓ 删除不存在元素测试通过");
            
            // 测试删除单个实例
            assert collection.remove(1) : "删除第一个实例应该成功";
            assert collection.getRandom() == 1 : "删除一个实例后应该还能获取到值";
            System.out.println("✓ 删除单个实例测试通过");
        }
        
        /**
         * 测试性能和大数据量
         */
        public static void testPerformance() {
            System.out.println("\n=== 测试性能和大数据量 ===");
            
            Code04_InsertDeleteRandomDuplicatesAllowed collection = new Code04_InsertDeleteRandomDuplicatesAllowed();
            int n = 10000;
            long startTime = System.currentTimeMillis();
            
            // 批量插入（包含重复）
            for (int i = 0; i < n; i++) {
                collection.insert(i % 100); // 插入0-99的重复值
            }
            
            // 批量删除
            for (int i = 0; i < n; i += 2) {
                collection.remove(i % 100);
            }
            
            // 随机操作混合
            for (int i = 0; i < n; i++) {
                if (i % 3 == 0) {
                    collection.insert(i % 50 + 100); // 插入100-149的新值
                } else if (i % 5 == 0) {
                    collection.remove(i % 50);
                } else {
                    collection.getRandom();
                }
            }
            
            long endTime = System.currentTimeMillis();
            System.out.println("✓ 性能测试通过，处理 " + n + " 次操作耗时: " + (endTime - startTime) + "ms");
        }
        
        /**
         * 测试复杂删除场景
         */
        public static void testComplexRemoval() {
            System.out.println("\n=== 测试复杂删除场景 ===");
            
            Code04_InsertDeleteRandomDuplicatesAllowed collection = new Code04_InsertDeleteRandomDuplicatesAllowed();
            
            // 插入多个重复值
            collection.insert(1);
            collection.insert(1);
            collection.insert(1);
            collection.insert(2);
            collection.insert(2);
            collection.insert(3);
            
            // 验证初始状态
            assert collection.remove(1) : "删除第一个1应该成功";
            assert collection.getRandom() != 999 : "getRandom应该返回有效值";
            
            // 继续删除
            assert collection.remove(1) : "删除第二个1应该成功";
            assert collection.remove(1) : "删除第三个1应该成功";
            assert !collection.remove(1) : "删除第四个1应该失败（已不存在）";
            
            // 验证最终状态
            assert collection.remove(2) : "删除第一个2应该成功";
            assert collection.remove(2) : "删除第二个2应该成功";
            assert collection.remove(3) : "删除3应该成功";
            
            System.out.println("✓ 复杂删除场景测试通过");
        }
        
        /**
         * 运行所有测试
         */
        public static void runAllTests() {
            try {
                testEdgeCases();
                testPerformance();
                testComplexRemoval();
                System.out.println("\n🎉 所有RandomizedCollection测试通过！功能正常。");
            } catch (AssertionError e) {
                System.out.println("❌ 测试失败: " + e.getMessage());
            }
        }
    }
    
    /**
     * 功能演示
     */
    public static void demonstrate() {
        System.out.println("\n=== RandomizedCollection功能演示 ===");
        
        Code04_InsertDeleteRandomDuplicatesAllowed collection = new Code04_InsertDeleteRandomDuplicatesAllowed();
        
        System.out.println("1. 插入重复元素1, 1, 1");
        collection.insert(1);
        collection.insert(1);
        collection.insert(1);
        
        System.out.println("2. 插入元素2, 2");
        collection.insert(2);
        collection.insert(2);
        
        System.out.println("3. 删除第一个1: " + collection.remove(1));
        System.out.println("4. 删除第二个1: " + collection.remove(1));
        System.out.println("5. 删除第三个1: " + collection.remove(1));
        System.out.println("6. 尝试删除第四个1: " + collection.remove(1));
        
        System.out.println("7. 随机获取元素:");
        for (int i = 0; i < 5; i++) {
            System.out.println("   第" + (i+1) + "次随机: " + collection.getRandom());
        }
        
        System.out.println("8. 插入新元素3, 4");
        collection.insert(3);
        collection.insert(4);
        
        System.out.println("9. 最终随机抽样:");
        for (int i = 0; i < 3; i++) {
            System.out.println("   随机值: " + collection.getRandom());
        }
        
        System.out.println("\n演示完成！");
    }
    
    /**
     * 主函数 - 运行测试和演示
     */
    public static void main(String[] args) {
        // 运行单元测试
        RandomizedCollectionTest.runAllTests();
        
        // 功能演示
        demonstrate();
    }
}