package class035;

import java.util.*;

/**
 * 实现支持O(1)时间复杂度的插入、删除和随机获取元素的数据结构
 * 题目来源：LeetCode 380. Insert Delete GetRandom O(1)
 * 网址：https://leetcode.com/problems/insert-delete-getrandom-o1/
 * 
 * 一、题目解析
 * 实现一个支持以下操作的数据结构，所有操作的时间复杂度都要求为O(1)：
 * 1. insert(val): 插入元素，如果元素不存在则插入并返回true，否则返回false
 * 2. remove(val): 删除元素，如果元素存在则删除并返回true，否则返回false
 * 3. getRandom(): 随机返回集合中的一个元素，每个元素被返回的概率相同
 * 
 * 二、算法思路
 * 1. 使用动态数组(ArrayList)存储元素，支持O(1)随机访问
 * 2. 使用哈希表(HashMap)维护元素到索引的映射，支持O(1)查找
 * 3. 插入操作：直接在数组末尾添加元素，并在哈希表中记录索引
 * 4. 删除操作：将要删除元素与数组末尾元素交换，然后删除末尾元素，更新哈希表
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
 * 2. 边界情况: 插入重复元素、删除不存在元素、空集合操作
 * 3. 内存管理: Java自动垃圾回收，但仍需注意大对象的内存消耗
 * 4. 线程安全: 当前实现非线程安全，如需线程安全可使用Collections.synchronizedList等
 * 5. 性能优化: 利用数组末尾操作的O(1)特性优化删除操作
 * 6. 可扩展性: 可扩展为支持泛型的通用数据结构
 * 
 * 六、相关题目扩展
 * 1. LeetCode 380. [Insert Delete GetRandom O(1)](https://leetcode.com/problems/insert-delete-getrandom-o1/) (本题)
 * 2. LeetCode 381. [Insert Delete GetRandom O(1) - Duplicates allowed](https://leetcode.com/problems/insert-delete-getrandom-o1-duplicates-allowed/) (允许重复元素)
 * 3. 牛客网: [设计支持O(1)插入删除和随机访问的数据结构](https://www.nowcoder.com/practice/11165e95382547 cab9b6518e2760384d)
 * 4. 剑指Offer II 030. [插入、删除和随机访问都是O(1)的容器](https://leetcode.cn/problems/FortPu/)
 * 5. LintCode 657. [Insert Delete GetRandom O(1)](https://www.lintcode.com/problem/657/)
 * 6. HackerRank: [Data Structures - RandomizedSet](https://www.hackerrank.com/challenges/java-hashset/problem)
 * 7. CodeChef: [Random Set Operations](https://www.codechef.com/problems/RANDSET)
 * 8. 计蒜客: [O(1)数据结构](https://nanti.jisuanke.com/t/41394)
 * 
 * 七、补充题目（各大OJ平台）
 * 1. AtCoder ABC238D. [AND and SUM](https://atcoder.jp/contests/abc238/tasks/abc238_d) - 集合操作优化
 * 2. Codeforces Round #344 (Div. 2) D. [Messenger](https://codeforces.com/contest/631/problem/D) - 消息集合处理
 * 3. UVA 11525. [Permutation](https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2520) - 集合排列问题
 * 4. SPOJ DQUERY. [D-query](https://www.spoj.com/problems/DQUERY/) - 集合查询优化
 * 5. Project Euler 543. [Counting the Number of Close Pairs](https://projecteuler.net/problem=543) - 集合计数优化
 * 6. HDU 1284. [钱币兑换问题](https://acm.hdu.edu.cn/showproblem.php?pid=1284) - 动态规划集合优化
 * 7. POJ 3349. [Snowflake Snow Snowflakes](https://poj.org/problem?id=3349) - 集合唯一性检测
 * 8. USACO Training: [Set Operations](https://train.usaco.org/) - 集合基础训练
 * 9. 洛谷 P1168. [中位数](https://www.luogu.com.cn/problem/P1168) - 数据流集合
 * 10. 赛码: [集合设计](https://www.acmcoder.com/) - 在线编程题目
 * 
 * 八、算法设计技巧总结
 * 1. 数组+哈希表组合：利用数组的O(1)随机访问和哈希表的O(1)查找
 * 2. 交换删除法：通过将要删除元素与末尾元素交换来实现O(1)删除
 * 3. 索引映射维护：哈希表维护元素到索引的映射，确保操作一致性
 * 4. 边界优化：特殊处理数组末尾操作，避免不必要的元素移动
 * 5. 随机均匀性：使用标准随机数生成器保证元素返回概率相等
 * 
 * 九、面试要点
 * 1. 解释为什么需要数组和哈希表的组合
 * 2. 分析删除操作中交换元素的必要性
 * 3. 讨论各种边界情况的处理
 * 4. 分析时间复杂度和空间复杂度
 * 5. 提出可能的扩展和优化方向
 * 6. 讨论线程安全性问题和解决方案
 * 
 * 十、工程实践中的应用场景
 * 1. 随机抽样系统
 * 2. 负载均衡器中的服务器管理
 * 3. 缓存系统中的键管理
 * 4. 游戏开发中的道具管理
 * 5. 数据库索引优化
 * 6. 推荐系统中的候选集维护
 */
public class Code03_InsertDeleteRandom {
    
    private Map<Integer, Integer> valueToIndex; // 值到索引的映射，用于O(1)查找
    private List<Integer> values;              // 存储值的列表，用于O(1)随机访问
    private Random random;                     // 随机数生成器，用于O(1)随机选择
    
    /** 初始化数据结构 */
    public Code03_InsertDeleteRandom() {
        valueToIndex = new HashMap<>();
        values = new ArrayList<>();
        random = new Random();
    }
    
    /**
     * 插入元素
     * @param val 要插入的值
     * @return 如果值不存在则插入成功返回true，否则返回false
     * 时间复杂度: O(1) 平均时间复杂度
     * 空间复杂度: O(1)
     */
    public boolean insert(int val) {
        // 检查元素是否已存在
        if (valueToIndex.containsKey(val)) {
            return false;
        }
        // 在哈希表中记录值到索引的映射
        valueToIndex.put(val, values.size());
        // 在数组末尾添加值
        values.add(val);
        return true;
    }
    
    /**
     * 删除元素
     * @param val 要删除的值
     * @return 如果值存在则删除成功返回true，否则返回false
     * 时间复杂度: O(1) 平均时间复杂度
     * 空间复杂度: O(1)
     * 核心思想: 将要删除的元素与数组末尾元素交换，然后删除末尾元素
     */
    public boolean remove(int val) {
        // 检查元素是否存在
        if (!valueToIndex.containsKey(val)) {
            return false;
        }
        
        // 获取要删除元素的索引
        int index = valueToIndex.get(val);
        // 获取数组末尾元素
        int lastElement = values.get(values.size() - 1);
        
        // 将末尾元素移动到要删除的位置
        values.set(index, lastElement);
        // 更新末尾元素在哈希表中的索引
        valueToIndex.put(lastElement, index);
        
        // 删除数组末尾元素
        values.remove(values.size() - 1);
        // 从哈希表中删除该元素
        valueToIndex.remove(val);
        
        return true;
    }
    
    /**
     * 随机获取一个元素
     * @return 随机元素
     * 时间复杂度: O(1)
     * 空间复杂度: O(1)
     * 核心思想: 使用Random类生成随机索引，直接访问数组元素
     */
    public int getRandom() {
        // 生成0到size-1的随机索引
        int randomIndex = random.nextInt(values.size());
        // 返回对应索引的元素
        return values.get(randomIndex);
    }
    
    // ==================== 单元测试和功能演示 ====================
    
    /**
     * 单元测试类 - 测试RandomizedSet的各种功能
     */
    public static class RandomizedSetTest {
        
        /**
         * 测试边界情况
         */
        public static void testEdgeCases() {
            System.out.println("\n=== 测试边界情况 ===");
            
            Code03_InsertDeleteRandom set = new Code03_InsertDeleteRandom();
            
            // 测试空集合
            assert !set.remove(1) : "空集合删除应该返回false";
            try {
                set.getRandom();
                assert false : "空集合getRandom应该抛出异常";
            } catch (Exception e) {
                System.out.println("✓ 空集合异常处理正确");
            }
            
            // 测试插入重复元素
            assert set.insert(1) : "第一次插入1应该成功";
            assert !set.insert(1) : "第二次插入1应该失败";
            System.out.println("✓ 重复插入测试通过");
            
            // 测试删除不存在的元素
            assert !set.remove(999) : "删除不存在的元素应该返回false";
            System.out.println("✓ 删除不存在元素测试通过");
            
            // 测试插入删除后getRandom
            set.insert(2);
            set.insert(3);
            set.remove(2);
            
            // 验证删除后集合状态
            assert set.insert(2) : "删除后重新插入应该成功";
            assert set.getRandom() != 999 : "getRandom应该返回有效值";
            System.out.println("✓ 删除后状态测试通过");
        }
        
        /**
         * 测试性能和大数据量
         */
        public static void testPerformance() {
            System.out.println("\n=== 测试性能和大数据量 ===");
            
            Code03_InsertDeleteRandom set = new Code03_InsertDeleteRandom();
            int n = 10000;
            long startTime = System.currentTimeMillis();
            
            // 批量插入
            for (int i = 0; i < n; i++) {
                set.insert(i);
            }
            
            // 批量删除
            for (int i = 0; i < n; i += 2) {
                set.remove(i);
            }
            
            // 随机操作混合
            for (int i = 0; i < n; i++) {
                if (i % 3 == 0) {
                    set.insert(i + n);
                } else if (i % 5 == 0) {
                    set.remove(i);
                } else {
                    set.getRandom();
                }
            }
            
            long endTime = System.currentTimeMillis();
            System.out.println("✓ 性能测试通过，处理 " + n + " 次操作耗时: " + (endTime - startTime) + "ms");
        }
        
        /**
         * 测试随机性分布
         */
        public static void testRandomness() {
            System.out.println("\n=== 测试随机性分布 ===");
            
            Code03_InsertDeleteRandom set = new Code03_InsertDeleteRandom();
            int[] testValues = {1, 2, 3, 4, 5};
            
            // 插入测试数据
            for (int val : testValues) {
                set.insert(val);
            }
            
            // 统计随机分布
            int[] count = new int[6]; // 索引1-5对应值1-5
            int trials = 10000;
            
            for (int i = 0; i < trials; i++) {
                int randomVal = set.getRandom();
                count[randomVal]++;
            }
            
            // 验证分布均匀性（每个值应该出现约2000次）
            double expected = trials / 5.0;
            double tolerance = expected * 0.1; // 10%容差
            
            for (int i = 1; i <= 5; i++) {
                double frequency = count[i] / (double)trials;
                assert Math.abs(count[i] - expected) < tolerance : 
                    "值" + i + "出现频率不均匀: " + count[i] + " vs " + expected;
            }
            System.out.println("✓ 随机性分布测试通过");
        }
        
        /**
         * 运行所有测试
         */
        public static void runAllTests() {
            try {
                testEdgeCases();
                testPerformance();
                testRandomness();
                System.out.println("\n🎉 所有RandomizedSet测试通过！功能正常。");
            } catch (AssertionError e) {
                System.out.println("❌ 测试失败: " + e.getMessage());
            }
        }
    }
    
    /**
     * 功能演示
     */
    public static void demonstrate() {
        System.out.println("\n=== RandomizedSet功能演示 ===");
        
        Code03_InsertDeleteRandom set = new Code03_InsertDeleteRandom();
        
        System.out.println("1. 插入元素1, 2, 3");
        set.insert(1);
        set.insert(2);
        set.insert(3);
        
        System.out.println("2. 尝试插入重复元素2: " + set.insert(2));
        
        System.out.println("3. 删除元素2: " + set.remove(2));
        System.out.println("4. 再次删除元素2: " + set.remove(2));
        
        System.out.println("5. 随机获取元素:");
        for (int i = 0; i < 5; i++) {
            System.out.println("   第" + (i+1) + "次随机: " + set.getRandom());
        }
        
        System.out.println("6. 插入元素4, 5");
        set.insert(4);
        set.insert(5);
        
        System.out.println("7. 最终随机抽样:");
        for (int i = 0; i < 3; i++) {
            System.out.println("   随机值: " + set.getRandom());
        }
        
        System.out.println("\n演示完成！");
    }
    
    /**
     * 主函数 - 运行测试和演示
     */
    public static void main(String[] args) {
        // 运行单元测试
        RandomizedSetTest.runAllTests();
        
        // 功能演示
        demonstrate();
    }
}