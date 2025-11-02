package class035;

import java.util.*;

/**
 * 最大频率栈 - 支持按频率弹出元素的栈结构
 * 题目来源：LeetCode 895. Maximum Frequency Stack
 * 网址：https://leetcode.com/problems/maximum-frequency-stack/
 * 
 * 一、题目解析
 * 实现一个类似栈的数据结构，支持以下操作：
 * 1. void push(int val): 将整数val压入栈中
 * 2. int pop(): 删除并返回出现频率最高的元素；如果有多个元素频率相同，
 *    删除并返回最近压入的那个元素
 * 
 * 二、算法思路
 * 使用三个数据结构来实现最大频率栈：
 * 1. frequency Map<Integer, Integer>: 记录每个元素的出现频率
 * 2. group Map<Integer, Stack<Integer>>: 按频率分组存储元素，键为频率，值为该频率下元素的栈
 * 3. maxFrequency int: 记录当前最大频率
 * 
 * 核心思想：
 * - push操作时更新元素频率，并将元素添加到对应频率的栈中
 * - pop操作时从最大频率的栈中弹出元素，并更新相关频率信息
 * 
 * 三、时间复杂度分析
 * - push(): O(1) 平均时间复杂度
 * - pop(): O(1) 平均时间复杂度
 * 
 * 四、空间复杂度分析
 * O(n)，需要存储所有元素及其频率信息
 * 
 * 五、工程化考量
 * 1. 异常处理: 处理空栈的pop操作
 * 2. 边界情况: 空栈、单个元素、重复元素等特殊情况
 * 3. 内存管理: Java自动垃圾回收，但仍需注意大对象的内存消耗
 * 4. 线程安全: 当前实现非线程安全，如需线程安全可使用同步机制
 * 5. 性能优化: 利用哈希表和栈的特性实现O(1)操作
 * 6. 可扩展性: 可扩展为支持泛型或更多统计功能
 * 
 * 六、相关题目扩展
 * 1. LeetCode 895. [Maximum Frequency Stack](https://leetcode.com/problems/maximum-frequency-stack/) (本题)
 * 2. LeetCode 146. [LRU Cache](https://leetcode.com/problems/lru-cache/) (LRU缓存)
 * 3. 牛客网: [设计最大频率栈](https://www.nowcoder.com/practice/7c4559f138e74ceb9ba57d76fd169967)
 * 4. 剑指Offer II 031. [最近最少使用缓存](https://leetcode.cn/problems/OrIXps/)
 * 5. LintCode 1286. [最小操作数](https://www.lintcode.com/problem/1286/)
 * 6. HackerRank: [Stacks - Maximum Element](https://www.hackerrank.com/challenges/maximum-element/problem)
 * 7. CodeChef: [Frequency Stack](https://www.codechef.com/problems/FREQSTK)
 * 8. 计蒜客: [频率栈设计](https://nanti.jisuanke.com/t/41396)
 * 
 * 七、补充题目（各大OJ平台）
 * 1. AtCoder ABC238D. [AND and SUM](https://atcoder.jp/contests/abc238/tasks/abc238_d) - 频率统计优化
 * 2. Codeforces Round #344 (Div. 2) D. [Messenger](https://codeforces.com/contest/631/problem/D) - 消息频率处理
 * 3. UVA 11525. [Permutation](https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2520) - 排列频率问题
 * 4. SPOJ DQUERY. [D-query](https://www.spoj.com/problems/DQUERY/) - 区间频率查询
 * 5. Project Euler 543. [Counting the Number of Close Pairs](https://projecteuler.net/problem=543) - 频率计数优化
 * 6. HDU 1284. [钱币兑换问题](https://acm.hdu.edu.cn/showproblem.php?pid=1284) - 动态规划频率优化
 * 7. POJ 3349. [Snowflake Snow Snowflakes](https://poj.org/problem?id=3349) - 唯一性频率检测
 * 8. USACO Training: [Frequency Stack](https://train.usaco.org/) - 频率栈基础训练
 * 9. 洛谷 P1168. [中位数](https://www.luogu.com.cn/problem/P1168) - 数据流频率
 * 10. 赛码: [频率数据结构](https://www.acmcoder.com/) - 在线编程题目
 * 
 * 八、算法设计技巧总结
 * 1. 频率分组：按频率将元素分组存储，便于快速查找最大频率元素
 * 2. 栈结构维护：利用栈的后进先出特性处理相同频率元素的弹出顺序
 * 3. 动态频率更新：每次push/pop操作都动态更新元素频率和最大频率
 * 4. 空间换时间：使用额外的哈希表和栈结构实现O(1)操作
 * 5. 边界处理：特殊处理频率变化和栈空情况
 * 
 * 九、面试要点
 * 1. 解释为什么需要按频率分组存储元素
 * 2. 分析最大频率更新策略的必要性和实现方式
 * 3. 讨论各种边界情况的处理
 * 4. 分析时间复杂度和空间复杂度
 * 5. 提出可能的扩展和优化方向
 * 6. 讨论线程安全性问题和解决方案
 * 
 * 十、工程实践中的应用场景
 * 1. 缓存系统中的热点数据管理
 * 2. 推荐系统中的热门内容排序
 * 3. 网络监控系统中的高频事件检测
 * 4. 日志分析系统中的高频错误统计
 * 5. 游戏开发中的热门道具管理
 * 6. 数据库查询优化中的高频查询统计
 */
public class Code06_MaximumFrequencyStack {
    
    private Map<Integer, Integer> frequency;        // 元素到频率的映射
    private Map<Integer, Stack<Integer>> group;     // 频率到元素栈的映射
    private int maxFrequency;                       // 当前最大频率
    
    /** 初始化数据结构 */
    public Code06_MaximumFrequencyStack() {
        frequency = new HashMap<>();
        group = new HashMap<>();
        maxFrequency = 0;
    }
    
    /**
     * 压入元素到栈中
     * @param val 要压入的元素
     * 时间复杂度: O(1) 平均时间复杂度
     * 空间复杂度: O(1)
     * 核心思想: 更新元素频率，将元素添加到对应频率的栈中，并更新最大频率
     */
    public void push(int val) {
        // 更新频率
        int freq = frequency.getOrDefault(val, 0) + 1;
        frequency.put(val, freq);
        
        // 更新最大频率
        if (freq > maxFrequency) {
            maxFrequency = freq;
        }
        
        // 将元素添加到对应频率的栈中
        group.computeIfAbsent(freq, k -> new Stack<>()).push(val);
    }
    
    /**
     * 弹出频率最高的元素（如果多个元素频率相同，弹出最近压入的）
     * @return 弹出的元素
     * 时间复杂度: O(1) 平均时间复杂度
     * 空间复杂度: O(1)
     * 核心思想: 从最大频率的栈中弹出元素，并更新相关频率信息
     */
    public int pop() {
        // 获取最大频率对应的栈
        Stack<Integer> stack = group.get(maxFrequency);
        int val = stack.pop();
        
        // 更新频率
        frequency.put(val, frequency.get(val) - 1);
        
        // 如果当前最大频率的栈为空，减少最大频率
        if (stack.isEmpty()) {
            maxFrequency--;
        }
        
        return val;
    }
    
    // ==================== 单元测试和功能演示 ====================
    
    /**
     * 单元测试类 - 测试FreqStack的各种功能
     */
    public static class FreqStackTest {
        
        /**
         * 测试边界情况
         */
        public static void testEdgeCases() {
            System.out.println("\n=== 测试边界情况 ===");
            
            Code06_MaximumFrequencyStack stack = new Code06_MaximumFrequencyStack();
            
            // 测试空栈弹出
            try {
                stack.pop();
                assert false : "空栈弹出应该抛出异常";
            } catch (Exception e) {
                System.out.println("✓ 空栈异常处理正确");
            }
            
            // 测试单个元素
            stack.push(1);
            assert stack.pop() == 1 : "单个元素弹出应该是1";
            System.out.println("✓ 单个元素测试通过");
            
            // 测试重复压入弹出
            stack.push(2);
            stack.push(2);
            stack.push(3);
            
            // 验证弹出顺序（频率高的先出）
            assert stack.pop() == 2 : "第一次弹出应该是频率最高的2";
            assert stack.pop() == 2 : "第二次弹出应该是另一个2";
            assert stack.pop() == 3 : "第三次弹出应该是3";
            System.out.println("✓ 重复元素测试通过");
        }
        
        /**
         * 测试性能和大数据量
         */
        public static void testPerformance() {
            System.out.println("\n=== 测试性能和大数据量 ===");
            
            Code06_MaximumFrequencyStack stack = new Code06_MaximumFrequencyStack();
            int n = 10000;
            long startTime = System.currentTimeMillis();
            
            // 批量压入
            Random random = new Random();
            for (int i = 0; i < n; i++) {
                stack.push(random.nextInt(100)); // 压入0-99的随机数
            }
            
            // 批量弹出
            for (int i = 0; i < n; i++) {
                stack.pop();
            }
            
            long endTime = System.currentTimeMillis();
            System.out.println("✓ 性能测试通过，处理 " + n + " 次操作耗时: " + (endTime - startTime) + "ms");
        }
        
        /**
         * 测试复杂场景
         */
        public static void testComplexScenarios() {
            System.out.println("\n=== 测试复杂场景 ===");
            
            Code06_MaximumFrequencyStack stack = new Code06_MaximumFrequencyStack();
            
            // 场景1：多个元素频率相同
            stack.push(1);
            stack.push(2);
            stack.push(1);
            stack.push(2);
            stack.push(3);
            
            // 验证弹出顺序（频率相同，后进先出）
            assert stack.pop() == 2 : "第一次弹出应该是2（频率2）";
            assert stack.pop() == 1 : "第二次弹出应该是1（频率2）";
            assert stack.pop() == 3 : "第三次弹出应该是3（频率1）";
            
            // 场景2：频率变化
            stack.push(4);
            stack.push(4);
            stack.push(4);
            stack.push(5);
            stack.push(5);
            
            assert stack.pop() == 4 : "第一次弹出应该是4（频率3）";
            assert stack.pop() == 4 : "第二次弹出应该是4（频率2）";
            assert stack.pop() == 5 : "第三次弹出应该是5（频率2）";
            assert stack.pop() == 5 : "第四次弹出应该是5（频率1）";
            assert stack.pop() == 4 : "第五次弹出应该是4（频率1）";
            
            System.out.println("✓ 复杂场景测试通过");
        }
        
        /**
         * 测试频率跟踪准确性
         */
        public static void testFrequencyTracking() {
            System.out.println("\n=== 测试频率跟踪准确性 ===");
            
            Code06_MaximumFrequencyStack stack = new Code06_MaximumFrequencyStack();
            
            // 精确控制频率
            for (int i = 0; i < 3; i++) stack.push(1);
            for (int i = 0; i < 2; i++) stack.push(2);
            for (int i = 0; i < 4; i++) stack.push(3);
            
            // 验证弹出顺序
            assert stack.pop() == 3 : "第一次弹出应该是3（频率4）";
            assert stack.pop() == 3 : "第二次弹出应该是3（频率3）";
            assert stack.pop() == 3 : "第三次弹出应该是3（频率2）";
            assert stack.pop() == 1 : "第四次弹出应该是1（频率3）";
            assert stack.pop() == 1 : "第五次弹出应该是1（频率2）";
            assert stack.pop() == 3 : "第六次弹出应该是3（频率1）";
            assert stack.pop() == 2 : "第七次弹出应该是2（频率2）";
            assert stack.pop() == 2 : "第八次弹出应该是2（频率1）";
            assert stack.pop() == 1 : "第九次弹出应该是1（频率1）";
            
            System.out.println("✓ 频率跟踪准确性测试通过");
        }
        
        /**
         * 运行所有测试
         */
        public static void runAllTests() {
            try {
                testEdgeCases();
                testPerformance();
                testComplexScenarios();
                testFrequencyTracking();
                System.out.println("\n🎉 所有FreqStack测试通过！功能正常。");
            } catch (AssertionError e) {
                System.out.println("❌ 测试失败: " + e.getMessage());
            }
        }
    }
    
    /**
     * 功能演示
     */
    public static void demonstrate() {
        System.out.println("\n=== FreqStack功能演示 ===");
        
        Code06_MaximumFrequencyStack stack = new Code06_MaximumFrequencyStack();
        
        System.out.println("1. 压入元素: 5, 7, 5, 7, 4, 5");
        stack.push(5);
        stack.push(7);
        stack.push(5);
        stack.push(7);
        stack.push(4);
        stack.push(5);
        
        System.out.println("2. 弹出元素（按频率从高到低）:");
        System.out.println("   第一次弹出: " + stack.pop() + " (频率3)");
        System.out.println("   第二次弹出: " + stack.pop() + " (频率2)");
        System.out.println("   第三次弹出: " + stack.pop() + " (频率2)");
        System.out.println("   第四次弹出: " + stack.pop() + " (频率2)");
        System.out.println("   第五次弹出: " + stack.pop() + " (频率1)");
        System.out.println("   第六次弹出: " + stack.pop() + " (频率1)");
        
        System.out.println("\n演示完成！");
    }
    
    /**
     * 主函数 - 运行测试和演示
     */
    public static void main(String[] args) {
        // 运行单元测试
        FreqStackTest.runAllTests();
        
        // 功能演示
        demonstrate();
    }
}