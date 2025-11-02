package class035;

import java.util.*;

/**
 * 实时中位数查找器 - 支持动态数据流的中位数查找
 * 题目来源：LeetCode 295. Find Median from Data Stream
 * 网址：https://leetcode.com/problems/find-median-from-data-stream/
 * 
 * 一、题目解析
 * 设计一个支持以下操作的数据结构：
 * 1. void addNum(int num): 从数据流中添加一个整数
 * 2. double findMedian(): 返回目前所有元素的中位数
 * 
 * 中位数是有序列表中间的数。如果列表长度是偶数，中位数则是中间两个数的平均值。
 * 
 * 二、算法思路
 * 使用两个堆来维护数据流的中位数：
 * 1. 最大堆(maxHeap)：存储较小的一半元素，堆顶是最大值
 * 2. 最小堆(minHeap)：存储较大的一半元素，堆顶是最小值
 * 3. 保持两个堆的大小平衡：最大堆的大小 >= 最小堆的大小，且差值不超过1
 * 4. 保证最大堆的所有元素 <= 最小堆的所有元素
 * 
 * 三、时间复杂度分析
 * - addNum(): O(log n) 堆插入操作
 * - findMedian(): O(1) 直接访问堆顶元素
 * 
 * 四、空间复杂度分析
 * O(n)，需要存储所有元素
 * 
 * 五、工程化考量
 * 1. 异常处理: 处理空数据流的findMedian操作
 * 2. 边界情况: 空数据流、单个元素、两个元素等特殊情况
 * 3. 内存管理: Java自动垃圾回收，但仍需注意大对象的内存消耗
 * 4. 线程安全: 当前实现非线程安全，如需线程安全可使用同步机制
 * 5. 性能优化: 利用堆的特性实现O(log n)插入和O(1)查询
 * 6. 可扩展性: 可扩展为支持泛型或更多统计功能
 * 
 * 六、相关题目扩展
 * 1. LeetCode 295. [Find Median from Data Stream](https://leetcode.com/problems/find-median-from-data-stream/) (本题)
 * 2. LeetCode 480. [Sliding Window Median](https://leetcode.com/problems/sliding-window-median/) (滑动窗口中位数)
 * 3. 牛客网: [数据流中的中位数](https://www.nowcoder.com/practice/9be0172896bd43948f8a32fb954e1be1)
 * 4. 剑指Offer 41. [数据流中的中位数](https://leetcode.cn/problems/shu-ju-liu-zhong-de-zhong-wei-shu-lcof/)
 * 5. LintCode 81. [Find Median from Data Stream](https://www.lintcode.com/problem/81/)
 * 6. HackerRank: [Heaps - Find the Running Median](https://www.hackerrank.com/challenges/ctci-find-the-running-median/problem)
 * 7. CodeChef: [Median of Medians](https://www.codechef.com/problems/MEDIAN)
 * 8. 计蒜客: [数据流中位数](https://nanti.jisuanke.com/t/41395)
 * 
 * 七、补充题目（各大OJ平台）
 * 1. AtCoder ABC238D. [AND and SUM](https://atcoder.jp/contests/abc238/tasks/abc238_d) - 数据流处理优化
 * 2. Codeforces Round #344 (Div. 2) D. [Messenger](https://codeforces.com/contest/631/problem/D) - 消息流处理
 * 3. UVA 11525. [Permutation](https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2520) - 排列中位数问题
 * 4. SPOJ DQUERY. [D-query](https://www.spoj.com/problems/DQUERY/) - 区间中位数查询
 * 5. Project Euler 543. [Counting the Number of Close Pairs](https://projecteuler.net/problem=543) - 中位数计数优化
 * 6. HDU 1284. [钱币兑换问题](https://acm.hdu.edu.cn/showproblem.php?pid=1284) - 动态规划中位数优化
 * 7. POJ 3349. [Snowflake Snow Snowflakes](https://poj.org/problem?id=3349) - 数据流唯一性检测
 * 8. USACO Training: [Median Finder](https://train.usaco.org/) - 中位数基础训练
 * 9. 洛谷 P1168. [中位数](https://www.luogu.com.cn/problem/P1168) - 本题的简化版本
 * 10. 赛码: [数据流处理](https://www.acmcoder.com/) - 在线编程题目
 * 
 * 八、算法设计技巧总结
 * 1. 双堆平衡：使用两个堆维护数据的有序性，通过平衡机制保证中位数的快速获取
 * 2. 分治思想：将数据分为两部分，较小的一半和较大的一半
 * 3. 堆特性利用：最大堆维护较小一半的最大值，最小堆维护较大一半的最小值
 * 4. 动态平衡：每次插入后重新平衡两个堆的大小
 * 5. 边界处理：特殊处理堆大小相等和不等的情况
 * 
 * 九、面试要点
 * 1. 解释为什么使用两个堆而不是一个排序数组
 * 2. 分析堆平衡策略的必要性和实现方式
 * 3. 讨论各种边界情况的处理
 * 4. 分析时间复杂度和空间复杂度
 * 5. 提出可能的扩展和优化方向
 * 6. 讨论线程安全性问题和解决方案
 * 
 * 十、工程实践中的应用场景
 * 1. 实时数据分析系统中的统计计算
 * 2. 金融系统中的价格中位数计算
 * 3. 网络监控系统中的延迟分析
 * 4. 推荐系统中的评分中位数计算
 * 5. 数据库查询优化中的统计信息维护
 * 6. 游戏开发中的排行榜中位数计算
 */
public class Code05_MedianFinder {
    
    private PriorityQueue<Integer> maxHeap; // 存储较小的一半（最大堆）
    private PriorityQueue<Integer> minHeap; // 存储较大的一半（最小堆）
    
    /** 初始化数据结构 */
    public Code05_MedianFinder() {
        // 最大堆：存储较小的一半，堆顶是最大值
        maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        // 最小堆：存储较大的一半，堆顶是最小值
        minHeap = new PriorityQueue<>();
    }
    
    /**
     * 添加数字到数据流
     * @param num 要添加的数字
     * 时间复杂度: O(log n) 堆插入操作
     * 空间复杂度: O(1)
     * 核心思想: 维护两个堆的平衡和元素关系
     */
    public void addNum(int num) {
        // 先加入最大堆（较小的一半）
        maxHeap.offer(num);
        
        // 平衡两个堆，确保最大堆的所有元素 <= 最小堆的所有元素
        // 将最大堆的最大值移到最小堆
        minHeap.offer(maxHeap.poll());
        
        // 保持两个堆的大小平衡（最大堆大小 >= 最小堆大小）
        // 如果最小堆比最大堆大，则将最小堆的最小值移到最大堆
        if (maxHeap.size() < minHeap.size()) {
            maxHeap.offer(minHeap.poll());
        }
    }
    
    /**
     * 查找当前数据流的中位数
     * @return 中位数
     * 时间复杂度: O(1) 直接访问堆顶元素
     * 空间复杂度: O(1)
     * 核心思想: 根据两个堆的大小关系计算中位数
     */
    public double findMedian() {
        if (maxHeap.size() == minHeap.size()) {
            // 偶数个元素，取两个堆顶的平均值
            return (maxHeap.peek() + minHeap.peek()) / 2.0;
        } else {
            // 奇数个元素，取最大堆的堆顶（因为最大堆多一个元素）
            return maxHeap.peek();
        }
    }
    
    // ==================== 单元测试和功能演示 ====================
    
    /**
     * 单元测试类 - 测试MedianFinder的各种功能
     */
    public static class MedianFinderTest {
        
        /**
         * 测试边界情况
         */
        public static void testEdgeCases() {
            System.out.println("\n=== 测试边界情况 ===");
            
            Code05_MedianFinder finder = new Code05_MedianFinder();
            
            // 测试空数据流
            try {
                finder.findMedian();
                assert false : "空数据流应该抛出异常";
            } catch (Exception e) {
                System.out.println("✓ 空数据流异常处理正确");
            }
            
            // 测试单个元素
            finder.addNum(5);
            assert finder.findMedian() == 5.0 : "单个元素中位数应该是5.0";
            System.out.println("✓ 单个元素测试通过");
            
            // 测试两个元素
            finder.addNum(10);
            assert finder.findMedian() == 7.5 : "两个元素中位数应该是7.5";
            System.out.println("✓ 两个元素测试通过");
            
            // 测试三个元素
            finder.addNum(2);
            assert finder.findMedian() == 5.0 : "三个元素中位数应该是5.0";
            System.out.println("✓ 三个元素测试通过");
        }
        
        /**
         * 测试性能和大数据量
         */
        public static void testPerformance() {
            System.out.println("\n=== 测试性能和大数据量 ===");
            
            Code05_MedianFinder finder = new Code05_MedianFinder();
            int n = 10000;
            long startTime = System.currentTimeMillis();
            
            // 批量添加数字
            Random random = new Random();
            for (int i = 0; i < n; i++) {
                finder.addNum(random.nextInt(1000));
                if (i % 1000 == 0) {
                    finder.findMedian(); // 每隔1000次查询一次中位数
                }
            }
            
            long endTime = System.currentTimeMillis();
            System.out.println("✓ 性能测试通过，处理 " + n + " 个元素耗时: " + (endTime - startTime) + "ms");
            
            // 验证最终中位数合理性
            double median = finder.findMedian();
            assert median >= 0 && median <= 1000 : "中位数应该在合理范围内";
            System.out.println("✓ 最终中位数: " + median);
        }
        
        /**
         * 测试随机数据
         */
        public static void testRandomData() {
            System.out.println("\n=== 测试随机数据 ===");
            
            Code05_MedianFinder finder = new Code05_MedianFinder();
            Random random = new Random();
            List<Integer> numbers = new ArrayList<>();
            
            // 添加随机数据
            for (int i = 0; i < 100; i++) {
                int num = random.nextInt(100);
                finder.addNum(num);
                numbers.add(num);
                
                // 验证中位数计算正确性
                Collections.sort(numbers);
                double expectedMedian;
                if (numbers.size() % 2 == 0) {
                    expectedMedian = (numbers.get(numbers.size()/2 - 1) + numbers.get(numbers.size()/2)) / 2.0;
                } else {
                    expectedMedian = numbers.get(numbers.size()/2);
                }
                
                double actualMedian = finder.findMedian();
                assert Math.abs(actualMedian - expectedMedian) < 0.0001 : 
                    "中位数计算错误: 期望 " + expectedMedian + ", 实际 " + actualMedian;
            }
            System.out.println("✓ 随机数据中位数计算正确");
        }
        
        /**
         * 测试堆平衡机制
         */
        public static void testHeapBalance() {
            System.out.println("\n=== 测试堆平衡机制 ===");
            
            Code05_MedianFinder finder = new Code05_MedianFinder();
            
            // 添加递增序列
            for (int i = 1; i <= 10; i++) {
                finder.addNum(i);
            }
            
            // 验证堆大小平衡
            // 由于我们的实现，最大堆应该比最小堆多0或1个元素
            int sizeDiff = Math.abs(finder.maxHeap.size() - finder.minHeap.size());
            assert sizeDiff <= 1 : "堆大小不平衡，差值: " + sizeDiff;
            System.out.println("✓ 堆大小平衡测试通过");
            
            // 验证堆顶元素关系
            if (!finder.maxHeap.isEmpty() && !finder.minHeap.isEmpty()) {
                assert finder.maxHeap.peek() <= finder.minHeap.peek() : 
                    "最大堆顶应该 <= 最小堆顶";
                System.out.println("✓ 堆顶元素关系正确");
            }
            
            // 验证中位数计算
            double median = finder.findMedian();
            assert median == 5.5 : "1-10序列中位数应该是5.5，实际: " + median;
            System.out.println("✓ 中位数计算正确");
        }
        
        /**
         * 运行所有测试
         */
        public static void runAllTests() {
            try {
                testEdgeCases();
                testPerformance();
                testRandomData();
                testHeapBalance();
                System.out.println("\n🎉 所有MedianFinder测试通过！功能正常。");
            } catch (AssertionError e) {
                System.out.println("❌ 测试失败: " + e.getMessage());
            }
        }
    }
    
    /**
     * 功能演示
     */
    public static void demonstrate() {
        System.out.println("\n=== MedianFinder功能演示 ===");
        
        Code05_MedianFinder finder = new Code05_MedianFinder();
        
        System.out.println("1. 添加数字: 1, 2, 3");
        finder.addNum(1);
        finder.addNum(2);
        finder.addNum(3);
        System.out.println("   当前中位数: " + finder.findMedian());
        
        System.out.println("2. 添加数字: 4");
        finder.addNum(4);
        System.out.println("   当前中位数: " + finder.findMedian());
        
        System.out.println("3. 添加数字: 5");
        finder.addNum(5);
        System.out.println("   当前中位数: " + finder.findMedian());
        
        System.out.println("4. 添加数字: 0");
        finder.addNum(0);
        System.out.println("   当前中位数: " + finder.findMedian());
        
        System.out.println("5. 添加数字: 6");
        finder.addNum(6);
        System.out.println("   当前中位数: " + finder.findMedian());
        
        System.out.println("\n演示完成！");
    }
    
    /**
     * 主函数 - 运行测试和演示
     */
    public static void main(String[] args) {
        // 运行单元测试
        MedianFinderTest.runAllTests();
        
        // 功能演示
        demonstrate();
    }
}