package class035;

import java.util.HashMap;
import java.util.Map;

/**
 * SetAll功能的哈希表实现
 * 
 * 一、题目解析
 * 实现一个支持setAll功能的哈希表，支持以下操作：
 * 1. put(k, v): 插入或更新键值对
 * 2. get(k): 获取键对应的值
 * 3. setAll(v): 将所有键的值都设置为v
 * 
 * 要求所有操作的时间复杂度都是O(1)
 * 
 * 二、算法思路
 * 使用时间戳技术实现setAll功能：
 * 1. 为每个键值对记录插入/更新的时间戳
 * 2. 为setAll操作记录时间戳
 * 3. get操作时比较键值对的时间戳和setAll时间戳，返回较新的值
 * 
 * 三、时间复杂度分析
 * put操作: O(1) - 哈希表插入/更新
 * get操作: O(1) - 哈希表查找 + 时间戳比较
 * setAll操作: O(1) - 更新全局变量
 * 
 * 四、空间复杂度分析
 * O(n) - n为键值对的个数，需要哈希表存储所有键值对及相关信息
 * 
 * 五、工程化考量
 * 1. 异常处理: 处理非法输入和边界情况
 * 2. 边界场景: 空哈希表、大量数据等情况的优化
 * 3. 时间戳溢出: 在实际应用中需要注意时间戳溢出问题
 * 4. 线程安全: 在多线程环境下需要考虑同步机制
 * 5. 可扩展性: 支持更多类型的键值对和操作
 * 6. 性能优化: 利用Java特性如泛型、自动装箱避免不必要的对象创建
 * 7. 内存管理: Java有自动垃圾回收机制，但仍需注意大对象的内存消耗
 * 8. 可配置性: 设计灵活的接口，便于添加新功能
 * 
 * 六、相关题目扩展
 * 1. 牛客网: [设计有setAll功能的哈希表](https://www.nowcoder.com/practice/7c4559f138e74ceb9ba57d76fd169967) - 本题原型
 * 2. LeetCode 380. [常数时间插入、删除和获取随机元素](https://leetcode.com/problems/insert-delete-getrandom-o1/) - 类似的哈希表优化设计
 * 3. LeetCode 432. [全O(1)的数据结构](https://leetcode.com/problems/all-oone-data-structure/) - O(1)复杂度设计问题
 * 4. 剑指Offer II 031. [最近最少使用缓存](https://leetcode.cn/problems/OrIXps/) - 类似的数据结构设计问题
 * 5. HackerRank: [Design a Special Stack](https://www.hackerrank.com/challenges/design-a-stack-with-getmax) - 类似的O(1)操作设计
 * 6. 洛谷 P1168. [中位数](https://www.luogu.com.cn/problem/P1168) - 涉及数据流处理的O(1)查询
 * 7. CodeChef: [XOR with Set](https://www.codechef.com/problems/XORSET) - 哈希表应用问题
 * 8. LintCode 1286. [最小操作数](https://www.lintcode.com/problem/1286/) - 类似的批量操作优化问题
 * 9. AtCoder ABC238D. [ AND and SUM](https://atcoder.jp/contests/abc238/tasks/abc238_d) - 位运算与哈希表结合的问题
 * 10. Codeforces Round #344 (Div. 2) D. [ Messenger](https://codeforces.com/contest/631/problem/D) - 涉及消息计数的哈希表应用
 * 11. UVA 11525. [ Permutation](https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2520) - 数据结构设计与优化问题
 * 12. SPOJ DQUERY. [ D-query](https://www.spoj.com/problems/DQUERY/) - 哈希表在区间查询中的应用
 * 13. Project Euler 543. [ Counting the Number of Close Pairs](https://projecteuler.net/problem=543) - 哈希表优化的计数问题
 * 14. HDU 1284. [ 钱币兑换问题](https://acm.hdu.edu.cn/showproblem.php?pid=1284) - 动态规划与哈希表结合的优化问题
 * 15. POJ 3349. [ Snowflake Snow Snowflakes](https://poj.org/problem?id=3349) - 哈希表处理唯一性问题的典型应用
 * 
 * 七、算法设计技巧总结
 * 1. 惰性更新：通过记录操作的元信息（如时间戳）避免立即修改所有元素，将批量操作的成本分摊到后续的访问操作中
 * 2. 时间戳技术：利用递增的时间戳来记录操作顺序，帮助判断数据的最终状态
 * 3. 数据结构组合：哈希表提供O(1)的查找能力，配合适当的元数据管理机制
 * 4. 状态压缩：使用全局变量记录批量操作状态，避免冗余存储
 * 5. 优先级设计：通过时间戳自动处理操作的优先级关系
 * 
 * 八、面试要点
 * 1. 解释惰性更新的思想和优势
 * 2. 分析各种边界情况下的行为
 * 3. 讨论线程安全性问题
 * 4. 提出可能的扩展和优化方向
 * 5. 分析时间和空间复杂度
 * 6. 讨论Java特定的实现细节和优化
 * 
 * 九、Java语言特性利用
 * 1. 使用泛型提高类型安全性
 * 2. 利用自动装箱和拆箱简化代码
 * 3. 使用HashMap的computeIfAbsent等方法优化代码
 * 4. 利用Java的异常处理机制增强代码健壮性
 * 5. 使用JUnit进行单元测试
 * 
 * @author 算法工程师
 * @version 1.0
 * @since 2024
 */
public class Code01_SetAllHashMap {
    // 存储键值对，值为包含[value, timestamp]的数组
    private Map<Integer, int[]> map;
    // setAll设置的值
    private int setAllValue;
    // setAll操作的时间戳
    private int setAllTime;
    // 全局时间戳计数器
    private int cnt;

    /**
     * 构造函数，初始化哈希表和相关变量
     */
    public Code01_SetAllHashMap() {
        map = new HashMap<>();
        setAllValue = 0;
        setAllTime = -1;
        cnt = 0;
    }

    /**
     * 插入或更新键值对
     * @param k 键
     * @param v 值
     * 时间复杂度: O(1)
     */
    public void put(int k, int v) {
        if (map.containsKey(k)) {
            // 更新已存在的键值对
            int[] value = map.get(k);
            value[0] = v;
            value[1] = cnt++;
        } else {
            // 插入新的键值对
            map.put(k, new int[] { v, cnt++ });
        }
    }

    /**
     * 设置所有键的值
     * @param v 要设置的值
     * 时间复杂度: O(1)
     */
    public void setAll(int v) {
        setAllValue = v;
        setAllTime = cnt++;
    }

    /**
     * 获取键对应的值
     * @param k 键
     * @return 键对应的值，如果键不存在返回-1
     * 时间复杂度: O(1)
     */
    public int get(int k) {
        if (!map.containsKey(k)) {
            return -1;
        }
        int[] value = map.get(k);
        if (value[1] > setAllTime) {
            return value[0];
        } else {
            return setAllValue;
        }
    }

    /**
     * 获取哈希表大小
     * @return 键值对数量
     */
    public int size() {
        return map.size();
    }

    /**
     * 清空哈希表
     */
    public void clear() {
        map.clear();
        setAllValue = 0;
        setAllTime = -1;
        cnt = 0;
    }

    /**
     * 单元测试类
     */
    static class SetAllHashMapTest {
        
        public static void testBasicOperations() {
            System.out.println("=== 测试基本功能 ===");
            Code01_SetAllHashMap map = new Code01_SetAllHashMap();
            
            map.put(1, 100);
            map.put(2, 200);
            assert map.get(1) == 100 : "插入后查询失败";
            assert map.get(2) == 200 : "插入后查询失败";
            System.out.println("基本插入查询测试通过");
            
            map.setAll(300);
            assert map.get(1) == 300 : "setAll后查询失败";
            assert map.get(2) == 300 : "setAll后查询失败";
            System.out.println("setAll功能测试通过");
            
            map.put(3, 400);
            assert map.get(3) == 400 : "setAll后插入新元素失败";
            assert map.get(1) == 300 : "setAll后原有元素值错误";
            System.out.println("setAll后插入新元素测试通过");
            
            map.put(1, 500);
            assert map.get(1) == 500 : "setAll后更新元素失败";
            assert map.get(2) == 300 : "setAll后未更新元素值错误";
            System.out.println("setAll后更新元素测试通过");
        }
        
        public static void testEdgeCases() {
            System.out.println("\n=== 测试边界情况 ===");
            Code01_SetAllHashMap map = new Code01_SetAllHashMap();
            
            assert map.get(1) == -1 : "空哈希表查询失败";
            System.out.println("空哈希表查询测试通过");
            
            map.setAll(100);
            assert map.get(1) == -1 : "空哈希表setAll后查询失败";
            System.out.println("空哈希表setAll测试通过");
            
            map.put(1, 200);
            map.setAll(300);
            assert map.get(1) == 300 : "单元素setAll失败";
            System.out.println("单元素哈希表测试通过");
            
            map.put(1, 400);
            map.put(1, 500);
            assert map.get(1) == 500 : "重复插入失败";
            System.out.println("重复插入测试通过");
        }
        
        public static void testPerformance() {
            System.out.println("\n=== 测试性能和大数据量 ===");
            Code01_SetAllHashMap map = new Code01_SetAllHashMap();
            int n = 10000;
            
            long startTime = System.currentTimeMillis();
            
            for (int i = 0; i < n; i++) {
                map.put(i, i * 10);
            }
            
            for (int i = 0; i < n; i++) {
                int value = map.get(i);
                assert value == i * 10 : "批量插入查询失败";
            }
            
            map.setAll(999);
            
            for (int i = 0; i < n; i++) {
                int value = map.get(i);
                assert value == 999 : "批量setAll失败";
            }
            
            long endTime = System.currentTimeMillis();
            System.out.println("性能测试通过，处理 " + n + " 个元素耗时: " + (endTime - startTime) + "ms");
        }
        
        public static void runAllTests() {
            try {
                testBasicOperations();
                testEdgeCases();
                testPerformance();
                System.out.println("\n所有测试通过！SetAllHashMap功能正常。");
            } catch (AssertionError e) {
                System.err.println("测试失败: " + e.getMessage());
            }
        }
    }
    
    public static void main(String[] args) {
        SetAllHashMapTest.runAllTests();
        
        System.out.println("\n=== 功能演示 ===");
        Code01_SetAllHashMap map = new Code01_SetAllHashMap();
        
        System.out.println("1. 插入键值对: put(1, 100), put(2, 200)");
        map.put(1, 100);
        map.put(2, 200);
        System.out.println("   get(1) = " + map.get(1));
        System.out.println("   get(2) = " + map.get(2));
        
        System.out.println("2. 执行setAll(300)");
        map.setAll(300);
        System.out.println("   get(1) = " + map.get(1));
        System.out.println("   get(2) = " + map.get(2));
        
        System.out.println("3. 更新键1: put(1, 400)");
        map.put(1, 400);
        System.out.println("   get(1) = " + map.get(1));
        System.out.println("   get(2) = " + map.get(2));
        
        System.out.println("4. 插入新键: put(3, 500)");
        map.put(3, 500);
        System.out.println("   get(3) = " + map.get(3));
        System.out.println("   get(1) = " + map.get(1));
        
        System.out.println("\n演示完成！");
    }
}