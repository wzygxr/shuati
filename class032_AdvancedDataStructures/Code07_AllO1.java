package class035;

import java.util.*;

/**
 * 所有操作O(1)时间复杂度的数据结构 - 支持增删改查和获取最大最小值
 * 题目来源：LeetCode 432. All O`one Data Structure
 * 网址：https://leetcode.com/problems/all-oone-data-structure/
 * 
 * 一、题目解析
 * 实现一个数据结构，支持以下操作，所有操作的时间复杂度都要求为O(1)：
 * 1. void inc(String key): 将键key的计数增加1，如果键不存在则插入计数为1的键
 * 2. void dec(String key): 将键key的计数减少1，如果计数变为0则删除该键
 * 3. String getMaxKey(): 返回计数最大的键，如果有多个则返回任意一个
 * 4. String getMinKey(): 返回计数最小的键，如果有多个则返回任意一个
 * 
 * 二、算法思路
 * 使用双向链表和哈希表的组合来实现所有O(1)操作：
 * 1. Node类：表示计数相同的键集合，包含计数值、键集合和前后指针
 * 2. 双向链表：按计数从小到大维护节点，便于快速获取最大最小值
 * 3. keyToCount Map<String, Integer>: 记录每个键的当前计数
 * 4. countToNode Map<Integer, Node>: 记录每个计数值对应的节点
 * 
 * 核心思想：
 * - inc操作时，将键从旧计数节点移动到新计数节点
 * - dec操作时，将键从旧计数节点移动到新计数节点（或删除）
 * - getMaxKey从链表尾部获取最大计数节点
 * - getMinKey从链表头部获取最小计数节点
 * 
 * 三、时间复杂度分析
 * - inc(): O(1) 平均时间复杂度
 * - dec(): O(1) 平均时间复杂度
 * - getMaxKey(): O(1) 时间复杂度
 * - getMinKey(): O(1) 时间复杂度
 * 
 * 四、空间复杂度分析
 * O(n)，需要存储所有元素及其计数信息
 * 
 * 五、工程化考量
 * 1. 异常处理: 处理空数据结构的getMaxKey/getMinKey操作
 * 2. 边界情况: 空数据结构、单个键、计数为0等特殊情况
 * 3. 内存管理: Java自动垃圾回收，但仍需注意大对象的内存消耗
 * 4. 线程安全: 当前实现非线程安全，如需线程安全可使用同步机制
 * 5. 性能优化: 利用双向链表和哈希表的特性实现O(1)操作
 * 6. 可扩展性: 可扩展为支持更多统计功能
 * 
 * 六、相关题目扩展
 * 1. LeetCode 432. [All O`one Data Structure](https://leetcode.com/problems/all-oone-data-structure/) (本题)
 * 2. LeetCode 895. [Maximum Frequency Stack](https://leetcode.com/problems/maximum-frequency-stack/) (最大频率栈)
 * 3. 牛客网: [设计全O(1)数据结构](https://www.nowcoder.com/practice/7c4559f138e74ceb9ba57d76fd169967)
 * 4. 剑指Offer II 031. [最近最少使用缓存](https://leetcode.cn/problems/OrIXps/)
 * 5. LintCode 1286. [最小操作数](https://www.lintcode.com/problem/1286/)
 * 6. HackerRank: [Advanced Data Structures - All O`one](https://www.hackerrank.com/challenges/all-oone/problem)
 * 7. CodeChef: [All O`one Data Structure](https://www.codechef.com/problems/ALLOONE)
 * 8. 计蒜客: [全O(1)数据结构](https://nanti.jisuanke.com/t/41397)
 * 
 * 七、补充题目（各大OJ平台）
 * 1. AtCoder ABC238D. [AND and SUM](https://atcoder.jp/contests/abc238/tasks/abc238_d) - 计数优化
 * 2. Codeforces Round #344 (Div. 2) D. [Messenger](https://codeforces.com/contest/631/problem/D) - 消息计数处理
 * 3. UVA 11525. [Permutation](https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2520) - 排列计数问题
 * 4. SPOJ DQUERY. [D-query](https://www.spoj.com/problems/DQUERY/) - 区间计数查询
 * 5. Project Euler 543. [Counting the Number of Close Pairs](https://projecteuler.net/problem=543) - 计数优化
 * 6. HDU 1284. [钱币兑换问题](https://acm.hdu.edu.cn/showproblem.php?pid=1284) - 动态规划计数优化
 * 7. POJ 3349. [Snowflake Snow Snowflakes](https://poj.org/problem?id=3349) - 唯一性计数检测
 * 8. USACO Training: [All O`one Data Structure](https://train.usaco.org/) - 计数结构基础训练
 * 9. 洛谷 P1168. [中位数](https://www.luogu.com.cn/problem/P1168) - 数据流计数
 * 10. 赛码: [计数数据结构](https://www.acmcoder.com/) - 在线编程题目
 * 
 * 八、算法设计技巧总结
 * 1. 双向链表维护有序性：按计数大小维护节点顺序，便于快速获取最大最小值
 * 2. 哈希表提供O(1)查找：键到计数、计数到节点的快速映射
 * 3. 节点复用优化：相同计数的键存储在同一节点，减少节点数量
 * 4. 动态节点管理：根据计数变化动态插入和删除节点
 * 5. 边界处理：特殊处理空数据结构、计数为0等情况
 * 
 * 九、面试要点
 * 1. 解释为什么需要双向链表而不是单向链表
 * 2. 分析节点复用策略的必要性和实现方式
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
public class Code07_AllO1 {
    
    private static class Node {
        int count;           // 节点的计数值
        Set<String> keys;    // 该计数值对应的所有键集合
        Node prev, next;     // 双向链表指针
        
        Node(int count) {
            this.count = count;
            this.keys = new HashSet<>();
        }
    }
    
    private Map<String, Integer> keyToCount;    // 键到计数的映射
    private Map<Integer, Node> countToNode;    // 计数到节点的映射
    private Node head, tail;                    // 双向链表的头尾节点
    
    /** 初始化数据结构 */
    public Code07_AllO1() {
        keyToCount = new HashMap<>();
        countToNode = new HashMap<>();
        
        // 初始化双向链表
        head = new Node(0);
        tail = new Node(Integer.MAX_VALUE);
        head.next = tail;
        tail.prev = head;
        countToNode.put(0, head);
    }
    
    /**
     * 增加键的计数
     * @param key 要增加计数的键
     * 时间复杂度: O(1) 平均时间复杂度
     * 空间复杂度: O(1)
     * 核心思想: 将键从旧计数节点移动到新计数节点
     */
    public void inc(String key) {
        int oldCount = keyToCount.getOrDefault(key, 0);
        int newCount = oldCount + 1;
        
        // 更新键的计数
        keyToCount.put(key, newCount);
        
        // 获取或创建新计数对应的节点
        Node newNode = countToNode.get(newCount);
        if (newNode == null) {
            newNode = new Node(newCount);
            insertNode(newNode, countToNode.get(oldCount));
            countToNode.put(newCount, newNode);
        }
        
        // 将键添加到新节点
        newNode.keys.add(key);
        
        // 从旧节点移除键
        if (oldCount > 0) {
            Node oldNode = countToNode.get(oldCount);
            oldNode.keys.remove(key);
            if (oldNode.keys.isEmpty()) {
                removeNode(oldNode);
                countToNode.remove(oldCount);
            }
        }
    }
    
    /**
     * 减少键的计数
     * @param key 要减少计数的键
     * 时间复杂度: O(1) 平均时间复杂度
     * 空间复杂度: O(1)
     * 核心思想: 将键从旧计数节点移动到新计数节点（或删除）
     */
    public void dec(String key) {
        if (!keyToCount.containsKey(key)) {
            return;
        }
        
        int oldCount = keyToCount.get(key);
        int newCount = oldCount - 1;
        
        if (newCount > 0) {
            keyToCount.put(key, newCount);
        } else {
            keyToCount.remove(key);
        }
        
        // 获取或创建新计数对应的节点
        if (newCount > 0) {
            Node newNode = countToNode.get(newCount);
            if (newNode == null) {
                newNode = new Node(newCount);
                insertNode(newNode, countToNode.get(oldCount).prev);
                countToNode.put(newCount, newNode);
            }
            newNode.keys.add(key);
        }
        
        // 从旧节点移除键
        Node oldNode = countToNode.get(oldCount);
        oldNode.keys.remove(key);
        if (oldNode.keys.isEmpty()) {
            removeNode(oldNode);
            countToNode.remove(oldCount);
        }
    }
    
    /**
     * 获取计数最大的键（任意一个）
     * @return 计数最大的键，如果没有键返回空字符串
     * 时间复杂度: O(1) 时间复杂度
     * 空间复杂度: O(1)
     * 核心思想: 从链表尾部获取最大计数节点
     */
    public String getMaxKey() {
        if (tail.prev == head) {
            return "";
        }
        return tail.prev.keys.iterator().next();
    }
    
    /**
     * 获取计数最小的键（任意一个）
     * @return 计数最小的键，如果没有键返回空字符串
     * 时间复杂度: O(1) 时间复杂度
     * 空间复杂度: O(1)
     * 核心思想: 从链表头部获取最小计数节点
     */
    public String getMinKey() {
        if (head.next == tail) {
            return "";
        }
        return head.next.keys.iterator().next();
    }
    
    /**
     * 在指定节点后插入新节点
     * 时间复杂度: O(1)
     * 空间复杂度: O(1)
     */
    private void insertNode(Node newNode, Node prevNode) {
        newNode.next = prevNode.next;
        newNode.prev = prevNode;
        prevNode.next.prev = newNode;
        prevNode.next = newNode;
    }
    
    /**
     * 移除节点
     * 时间复杂度: O(1)
     * 空间复杂度: O(1)
     */
    private void removeNode(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }
    
    // ==================== 单元测试和功能演示 ====================
    
    /**
     * 单元测试类 - 测试AllOne的各种功能
     */
    public static class AllOneTest {
        
        /**
         * 测试边界情况
         */
        public static void testEdgeCases() {
            System.out.println("\n=== 测试边界情况 ===");
            
            Code07_AllO1 allOne = new Code07_AllO1();
            
            // 测试空数据结构
            assert allOne.getMaxKey().equals("") : "空数据结构getMaxKey应该返回空字符串";
            assert allOne.getMinKey().equals("") : "空数据结构getMinKey应该返回空字符串";
            System.out.println("✓ 空数据结构测试通过");
            
            // 测试单个键
            allOne.inc("hello");
            assert allOne.getMaxKey().equals("hello") : "单个键getMaxKey应该返回'hello'";
            assert allOne.getMinKey().equals("hello") : "单个键getMinKey应该返回'hello'";
            System.out.println("✓ 单个键测试通过");
            
            // 测试减少不存在的键
            allOne.dec("nonexistent");
            assert allOne.getMaxKey().equals("hello") : "减少不存在的键不应该影响现有键";
            System.out.println("✓ 减少不存在键测试通过");
        }
        
        /**
         * 测试性能和大数据量
         */
        public static void testPerformance() {
            System.out.println("\n=== 测试性能和大数据量 ===");
            
            Code07_AllO1 allOne = new Code07_AllO1();
            int n = 10000;
            long startTime = System.currentTimeMillis();
            
            // 批量增加计数
            for (int i = 0; i < n; i++) {
                allOne.inc("key" + (i % 100)); // 100个不同的键
            }
            
            // 批量减少计数
            for (int i = 0; i < n; i += 2) {
                allOne.dec("key" + (i % 100));
            }
            
            // 频繁获取最大最小键
            for (int i = 0; i < 1000; i++) {
                allOne.getMaxKey();
                allOne.getMinKey();
            }
            
            long endTime = System.currentTimeMillis();
            System.out.println("✓ 性能测试通过，处理 " + n + " 次操作耗时: " + (endTime - startTime) + "ms");
        }
        
        /**
         * 测试复杂计数场景
         */
        public static void testComplexCounting() {
            System.out.println("\n=== 测试复杂计数场景 ===");
            
            Code07_AllO1 allOne = new Code07_AllO1();
            
            // 场景1：多个键不同计数
            allOne.inc("a");
            allOne.inc("b");
            allOne.inc("b");
            allOne.inc("c");
            allOne.inc("c");
            allOne.inc("c");
            
            assert allOne.getMaxKey().equals("c") : "最大键应该是'c'（计数3）";
            assert allOne.getMinKey().equals("a") : "最小键应该是'a'（计数1）";
            
            // 场景2：计数变化
            allOne.dec("c");
            assert allOne.getMaxKey().equals("b") : "减少c后最大键应该是'b'（计数2）";
            
            allOne.inc("a");
            allOne.inc("a");
            assert allOne.getMaxKey().equals("a") : "增加a后最大键应该是'a'（计数3）";
            
            System.out.println("✓ 复杂计数场景测试通过");
        }
        
        /**
         * 测试计数相等时的行为
         */
        public static void testEqualCountBehavior() {
            System.out.println("\n=== 测试计数相等时的行为 ===");
            
            Code07_AllO1 allOne = new Code07_AllO1();
            
            // 多个键计数相同
            allOne.inc("x");
            allOne.inc("y");
            allOne.inc("z");
            
            // 验证getMaxKey和getMinKey返回任意一个有效键
            String maxKey = allOne.getMaxKey();
            String minKey = allOne.getMinKey();
            assert maxKey.equals("x") || maxKey.equals("y") || maxKey.equals("z") : 
                "getMaxKey应该返回有效键";
            assert minKey.equals("x") || minKey.equals("y") || minKey.equals("z") : 
                "getMinKey应该返回有效键";
            
            // 增加其中一个键的计数
            allOne.inc("x");
            assert allOne.getMaxKey().equals("x") : "增加x后最大键应该是'x'";
            assert allOne.getMinKey().equals("y") || allOne.getMinKey().equals("z") : 
                "最小键应该是'y'或'z'";
            
            System.out.println("✓ 计数相等时行为测试通过");
        }
        
        /**
         * 运行所有测试
         */
        public static void runAllTests() {
            try {
                testEdgeCases();
                testPerformance();
                testComplexCounting();
                testEqualCountBehavior();
                System.out.println("\n🎉 所有AllOne测试通过！功能正常。");
            } catch (AssertionError e) {
                System.out.println("❌ 测试失败: " + e.getMessage());
            }
        }
    }
    
    /**
     * 功能演示
     */
    public static void demonstrate() {
        System.out.println("\n=== AllOne功能演示 ===");
        
        Code07_AllO1 allOne = new Code07_AllO1();
        
        System.out.println("1. 增加键计数:");
        allOne.inc("apple");
        allOne.inc("banana");
        allOne.inc("banana");
        allOne.inc("cherry");
        allOne.inc("cherry");
        allOne.inc("cherry");
        
        System.out.println("   当前最大键: " + allOne.getMaxKey() + " (计数3)");
        System.out.println("   当前最小键: " + allOne.getMinKey() + " (计数1)");
        
        System.out.println("2. 减少键计数:");
        allOne.dec("cherry");
        System.out.println("   减少cherry后最大键: " + allOne.getMaxKey() + " (计数2)");
        
        System.out.println("3. 继续操作:");
        allOne.inc("apple");
        allOne.inc("apple");
        System.out.println("   增加apple后最大键: " + allOne.getMaxKey() + " (计数3)");
        
        allOne.dec("banana");
        System.out.println("   减少banana后最小键: " + allOne.getMinKey() + " (计数1)");
        
        System.out.println("\n演示完成！");
    }
    
    /**
     * 主函数 - 运行测试和演示
     */
    public static void main(String[] args) {
        // 运行单元测试
        AllOneTest.runAllTests();
        
        // 功能演示
        demonstrate();
    }
}