import java.util.*;

/**
 * TreeSet和TreeMap高级应用题目实现
 * 包含LeetCode 352, 363, 436, 456, 480, 683, 715, 731, 732, 855, 981, 1146, 1348, 1438等题目
 * 
 * TreeSet和TreeMap高级特性：
 * - TreeSet特点：基于红黑树实现，元素有序，支持范围查询，查找、插入、删除时间复杂度O(logN)
 * - TreeMap特点：基于红黑树实现，键值对有序，支持范围查询，查找、插入、删除时间复杂度O(logN)
 * - 高级操作：ceilingKey、floorKey、higherKey、lowerKey等
 * 
 * 时间复杂度分析：
 * - TreeSet/TreeMap基本操作：O(log n)
 * - 范围查询：O(log n + k) 其中k是结果数量
 * - 排序操作：O(n log n)
 * 
 * 空间复杂度分析：
 * - TreeSet/TreeMap存储：O(n)
 * - 额外数据结构：O(n)
 * 
 * 工程化考量：
 * 1. 异常处理：处理空输入、边界条件
 * 2. 性能优化：选择合适的数据结构，避免不必要的对象创建
 * 3. 代码可读性：添加详细注释，使用有意义的变量名
 * 4. 线程安全：非线程安全，多线程环境下需要同步
 * 
 * 相关平台题目：
 * 1. LeetCode 352. Data Stream as Disjoint Intervals (数据流变为不相交区间) - https://leetcode.com/problems/data-stream-as-disjoint-intervals/
 * 2. LeetCode 363. Max Sum of Rectangle No Larger Than K (矩形区域不超过K的最大数值和) - https://leetcode.com/problems/max-sum-of-rectangle-no-larger-than-k/
 * 3. LeetCode 436. Find Right Interval (寻找右区间) - https://leetcode.com/problems/find-right-interval/
 * 4. LeetCode 456. 132 Pattern (132模式) - https://leetcode.com/problems/132-pattern/
 * 5. LeetCode 480. Sliding Window Median (滑动窗口中位数) - https://leetcode.com/problems/sliding-window-median/
 * 6. LeetCode 683. K Empty Slots (K个空花盆) - https://leetcode.com/problems/k-empty-slots/
 * 7. LeetCode 715. Range Module (范围模块) - https://leetcode.com/problems/range-module/
 * 8. LeetCode 731. My Calendar II (我的日程安排表 II) - https://leetcode.com/problems/my-calendar-ii/
 * 9. LeetCode 732. My Calendar III (我的日程安排表 III) - https://leetcode.com/problems/my-calendar-iii/
 * 10. LeetCode 855. Exam Room (考场就座) - https://leetcode.com/problems/exam-room/
 * 11. LeetCode 981. Time Based Key-Value Store (基于时间的键值存储) - https://leetcode.com/problems/time-based-key-value-store/
 * 12. LeetCode 1146. Snapshot Array (快照数组) - https://leetcode.com/problems/snapshot-array/
 * 13. LeetCode 1348. Tweet Counts Per Frequency (推文计数) - https://leetcode.com/problems/tweet-counts-per-frequency/
 * 14. LeetCode 1438. Longest Continuous Subarray With Absolute Diff Less Than or Equal to Limit (绝对差不超过限制的最长连续子数组) - https://leetcode.com/problems/longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit/
 * 15. LintCode 613. High Five (最高分五科) - https://www.lintcode.com/problem/high-five/
 * 16. HackerEarth Monk and the Magical Candy Bags (和尚与魔法糖果袋) - https://www.hackerearth.com/practice/algorithms/greedy/basics-of-greedy-algorithms/practice-problems/algorithm/monk-and-the-magical-candy-bags/
 * 17. CodeChef FRGTNLNG (遗忘的语言) - https://www.codechef.com/problems/FRGTNLNG
 * 18. SPOJ DICT (字典) - https://www.spoj.com/problems/DICT/
 * 19. Project Euler Problem 2: Even Fibonacci numbers (偶数斐波那契数) - https://projecteuler.net/problem=2
 * 20. HackerRank Maximum Palindromes (最大回文) - https://www.hackerrank.com/challenges/maximum-palindromes
 * 21. 计蒜客 T1101: 阶乘 (阶乘) - https://www.jisuanke.com/t/T1101
 * 22. 杭电 OJ 1003: Max Sum (最大子序列和) - http://acm.hdu.edu.cn/showproblem.php?pid=1003
 * 23. 牛客网 剑指Offer 50: 第一个只出现一次的字符 (第一个只出现一次的字符) - https://www.nowcoder.com/practice/1c82e8cf713b4bbeb2a5b31cf5b0417c
 * 24. acwing 800. 数组元素的目标和 (数组元素的目标和) - https://www.acwing.com/problem/content/802/
 * 25. POJ 1003: Hangover (悬垂) - http://poj.org/problem?id=1003
 * 26. UVa OJ 101: The Blocks Problem (积木问题) - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=37
 * 27. Timus OJ 1005: Stone Pile (石子堆) - https://acm.timus.ru/problem.aspx?space=1&num=1005
 * 28. Aizu OJ ALDS1_5_A: Exhaustive Search (穷举搜索) - http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_5_A
 * 29. Comet OJ Contest #1: 热身赛 B. 简单的数学题 (简单的数学题) - https://cometoj.com/contest/1/problem/B
 * 30. MarsCode 火星编程竞赛: 数字统计 (数字统计) - https://www.marscode.cn/contest/1/problem/1002
 * 31. ZOJ 1002: Fire Net (消防网) - http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=1002
 * 32. LOJ 101: 最小生成树 (最小生成树) - https://loj.ac/p/101
 * 33. 各大高校OJ: 北京大学OJ 1000: A+B Problem (A+B问题) - http://poj.org/problem?id=1000
 * 34. Codeforces 122A. Lucky Division (幸运分割) - https://codeforces.com/problemset/problem/122/A
 * 35. AtCoder ABC 218 C - Shapes (形状) - https://atcoder.jp/contests/abc218/tasks/abc218_c
 * 36. USACO Bronze: Block Game (积木游戏) - http://www.usaco.org/index.php?page=viewproblem2&cpid=664
 * 37. 洛谷 P3366 【模板】最小生成树 (模板最小生成树) - https://www.luogu.com.cn/problem/P3366
 * 38. LeetCode 149. Max Points on a Line (直线上最多的点数) - https://leetcode.com/problems/max-points-on-a-line/
 * 39. LeetCode 215. Kth Largest Element in an Array (数组中的第K个最大元素) - https://leetcode.com/problems/kth-largest-element-in-an-array/
 * 40. LeetCode 295. Find Median from Data Stream (数据流的中位数) - https://leetcode.com/problems/find-median-from-data-stream/
 * 41. LeetCode 315. Count of Smaller Numbers After Self (计算右侧小于当前元素的个数) - https://leetcode.com/problems/count-of-smaller-numbers-after-self/
 * 42. LeetCode 327. Count of Range Sum (区间和的个数) - https://leetcode.com/problems/count-of-range-sum/
 * 43. LeetCode 350. Intersection of Two Arrays II (两个数组的交集 II) - https://leetcode.com/problems/intersection-of-two-arrays-ii/
 * 44. LeetCode 148. Sort List (排序链表) - https://leetcode.com/problems/sort-list/
 * 45. LeetCode 242. Valid Anagram (有效的字母异位词) - https://leetcode.com/problems/valid-anagram/
 * 46. LeetCode 347. Top K Frequent Elements (前 K 个高频元素) - https://leetcode.com/problems/top-k-frequent-elements/
 * 47. LeetCode 451. Sort Characters By Frequency (根据字符出现频率排序) - https://leetcode.com/problems/sort-characters-by-frequency/
 * 48. LeetCode 493. Reverse Pairs (翻转对) - https://leetcode.com/problems/reverse-pairs/
 * 49. LeetCode 539. Minimum Time Difference (最小时间差) - https://leetcode.com/problems/minimum-time-difference/
 * 50. LeetCode 791. Custom Sort String (自定义字符串排序) - https://leetcode.com/problems/custom-sort-string/

public class Code04_TreeSetTreeMapAdvanced {
    
    /**
     * LeetCode 352. Data Stream as Disjoint Intervals
     * 将数据流变为多个不相交区间
     * 网址：https://leetcode.com/problems/data-stream-as-disjoint-intervals/
     * 
     * 解题思路：
     * 1. 使用TreeMap存储区间边界，键为区间起点，值为区间终点
     * 2. 添加数字时，查找可能合并的相邻区间
     * 3. 合并重叠或相邻的区间
     * 
     * 时间复杂度：每次添加O(log n)，获取区间O(n)
     * 空间复杂度：O(n)
     */
    static class DataStreamAsDisjointIntervals {
        private TreeMap<Integer, Integer> intervals;
        
        public DataStreamAsDisjointIntervals() {
            intervals = new TreeMap<>();
        }
        
        public void addNum(int val) {
            // 查找小于等于val的区间
            Integer floorKey = intervals.floorKey(val);
            if (floorKey != null && intervals.get(floorKey) >= val) {
                // 数字已经在某个区间内，不需要处理
                return;
            }
            
            // 检查是否可以与左侧区间合并
            boolean mergeLeft = floorKey != null && intervals.get(floorKey) + 1 == val;
            
            // 检查是否可以与右侧区间合并
            Integer higherKey = intervals.higherKey(val);
            boolean mergeRight = higherKey != null && higherKey == val + 1;
            
            if (mergeLeft && mergeRight) {
                // 合并左右区间
                int newEnd = intervals.get(higherKey);
                intervals.put(floorKey, newEnd);
                intervals.remove(higherKey);
            } else if (mergeLeft) {
                // 只与左侧区间合并
                intervals.put(floorKey, val);
            } else if (mergeRight) {
                // 只与右侧区间合并
                int end = intervals.get(higherKey);
                intervals.remove(higherKey);
                intervals.put(val, end);
            } else {
                // 创建新区间
                intervals.put(val, val);
            }
        }
        
        public int[][] getIntervals() {
            int[][] result = new int[intervals.size()][2];
            int index = 0;
            for (Map.Entry<Integer, Integer> entry : intervals.entrySet()) {
                result[index][0] = entry.getKey();
                result[index][1] = entry.getValue();
                index++;
            }
            return result;
        }
    }
    
    /**
     * LeetCode 363. Max Sum of Rectangle No Larger Than K
     * 矩形区域不超过K的最大数值和
     * 网址：https://leetcode.com/problems/max-sum-of-rectangle-no-larger-than-k/
     * 
     * 解题思路：
     * 1. 固定左右边界，计算每一行的前缀和
     * 2. 使用TreeSet维护前缀和，快速查找满足条件的前缀和
     * 3. 使用ceil方法找到大于等于(target - k)的最小值
     * 
     * 时间复杂度：O(min(m,n)² * max(m,n) log max(m,n))
     * 空间复杂度：O(max(m,n))
     */
    public int maxSumSubmatrix(int[][] matrix, int k) {
        int m = matrix.length;
        int n = matrix[0].length;
        int maxSum = Integer.MIN_VALUE;
        
        // 让m是较小的维度，减少时间复杂度
        if (m > n) {
            int[][] rotated = new int[n][m];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    rotated[j][i] = matrix[i][j];
                }
            }
            return maxSumSubmatrix(rotated, k);
        }
        
        // 枚举上下边界
        for (int top = 0; top < m; top++) {
            int[] colSum = new int[n];
            for (int bottom = top; bottom < m; bottom++) {
                // 更新列前缀和
                for (int j = 0; j < n; j++) {
                    colSum[j] += matrix[bottom][j];
                }
                
                // 使用TreeSet维护前缀和
                TreeSet<Integer> set = new TreeSet<>();
                set.add(0);
                int prefixSum = 0;
                
                for (int j = 0; j < n; j++) {
                    prefixSum += colSum[j];
                    // 查找大于等于(prefixSum - k)的最小值
                    Integer ceil = set.ceiling(prefixSum - k);
                    if (ceil != null) {
                        maxSum = Math.max(maxSum, prefixSum - ceil);
                    }
                    set.add(prefixSum);
                }
            }
        }
        
        return maxSum;
    }
    
    /**
     * LeetCode 436. Find Right Interval
     * 寻找右区间
     * 网址：https://leetcode.com/problems/find-right-interval/
     * 
     * 解题思路：
     * 1. 使用TreeMap存储每个区间的起始位置和索引
     * 2. 对于每个区间，使用ceiling方法找到起始位置大于等于当前区间结束位置的最小区间
     * 3. 如果找到则返回对应索引，否则返回-1
     * 
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(n)
     */
    public int[] findRightInterval(int[][] intervals) {
        int n = intervals.length;
        int[] result = new int[n];
        
        // 使用TreeMap存储区间起始位置和索引
        TreeMap<Integer, Integer> map = new TreeMap<>();
        for (int i = 0; i < n; i++) {
            map.put(intervals[i][0], i);
        }
        
        for (int i = 0; i < n; i++) {
            int end = intervals[i][1];
            // 查找大于等于end的最小起始位置
            Integer rightStart = map.ceilingKey(end);
            if (rightStart != null) {
                result[i] = map.get(rightStart);
            } else {
                result[i] = -1;
            }
        }
        
        return result;
    }
    
    /**
     * LeetCode 456. 132 Pattern
     * 132模式
     * 网址：https://leetcode.com/problems/132-pattern/
     * 
     * 解题思路：
     * 1. 从右向左遍历数组
     * 2. 使用TreeSet维护右侧元素
     * 3. 维护一个变量记录当前最大值作为3
     * 4. 检查是否存在1小于3且3小于2的模式
     * 
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(n)
     */
    public boolean find132pattern(int[] nums) {
        int n = nums.length;
        if (n < 3) return false;
        
        // 记录左侧最小值
        int[] minLeft = new int[n];
        minLeft[0] = nums[0];
        for (int i = 1; i < n; i++) {
            minLeft[i] = Math.min(minLeft[i-1], nums[i]);
        }
        
        // 使用TreeSet维护右侧元素
        TreeSet<Integer> rightSet = new TreeSet<>();
        rightSet.add(nums[n-1]);
        
        for (int i = n-2; i > 0; i--) {
            // 当前元素作为3，左侧最小值作为1
            if (minLeft[i-1] < nums[i]) {
                // 在右侧查找大于左侧最小值且小于当前元素的数作为2
                Integer candidate = rightSet.lower(nums[i]);
                if (candidate != null && candidate > minLeft[i-1]) {
                    return true;
                }
            }
            rightSet.add(nums[i]);
        }
        
        return false;
    }
    
    /**
     * LeetCode 480. Sliding Window Median
     * 滑动窗口中位数
     * 网址：https://leetcode.com/problems/sliding-window-median/
     * 
     * 解题思路：
     * 1. 使用两个TreeSet维护窗口元素
     * 2. 左半部分存储较小的一半元素，右半部分存储较大的一半元素
     * 3. 保持两个集合大小平衡，中位数即为左半部分的最大值或两个最大值的平均值
     * 
     * 时间复杂度：O(n log k)
     * 空间复杂度：O(k)
     */
    public double[] medianSlidingWindow(int[] nums, int k) {
        int n = nums.length;
        double[] result = new double[n - k + 1];
        
        // 使用两个TreeSet维护窗口元素
        TreeSet<Integer> left = new TreeSet<>((a, b) -> 
            nums[a] != nums[b] ? Integer.compare(nums[a], nums[b]) : Integer.compare(a, b));
        TreeSet<Integer> right = new TreeSet<>((a, b) -> 
            nums[a] != nums[b] ? Integer.compare(nums[a], nums[b]) : Integer.compare(a, b));
        
        // 平衡函数，保持两个集合大小平衡
        Runnable balance = () -> {
            while (left.size() > right.size()) {
                right.add(left.pollLast());
            }
        };
        
        for (int i = 0; i < n; i++) {
            // 添加新元素
            if (left.isEmpty() || nums[i] <= nums[left.last()]) {
                left.add(i);
            } else {
                right.add(i);
            }
            balance.run();
            
            // 移除窗口外的元素
            if (i >= k) {
                if (!left.remove(i - k)) {
                    right.remove(i - k);
                }
                balance.run();
            }
            
            // 计算中位数
            if (i >= k - 1) {
                if (k % 2 == 0) {
                    result[i - k + 1] = ((double) nums[left.last()] + nums[right.first()]) / 2.0;
                } else {
                    result[i - k + 1] = (double) nums[right.first()];
                }
            }
        }
        
        return result;
    }
    
    /**
     * LeetCode 683. K Empty Slots
     * K个空花盆
     * 网址：https://leetcode.com/problems/k-empty-slots/
     * 
     * 解题思路：
     * 1. 使用TreeSet存储开花位置
     * 2. 按天数顺序添加开花位置
     * 3. 每次添加后检查新位置与相邻位置的距离是否为k+1
     * 
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(n)
     */
    public int kEmptySlots(int[] flowers, int k) {
        TreeSet<Integer> bloomed = new TreeSet<>();
        
        for (int day = 0; day < flowers.length; day++) {
            int position = flowers[day];
            bloomed.add(position);
            
            // 检查左侧相邻位置
            Integer lower = bloomed.lower(position);
            if (lower != null && position - lower - 1 == k) {
                return day + 1;
            }
            
            // 检查右侧相邻位置
            Integer higher = bloomed.higher(position);
            if (higher != null && higher - position - 1 == k) {
                return day + 1;
            }
        }
        
        return -1;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        Code04_TreeSetTreeMapAdvanced solution = new Code04_TreeSetTreeMapAdvanced();
        
        // 测试LeetCode 436
        int[][] intervals = {{1,2}, {2,3}, {0,1}, {3,4}};
        int[] result = solution.findRightInterval(intervals);
        System.out.println("LeetCode 436 Result: " + Arrays.toString(result));
        
        // 测试LeetCode 456
        int[] nums = {3,1,4,2};
        boolean has132 = solution.find132pattern(nums);
        System.out.println("LeetCode 456 Result: " + has132);
        
        // 测试LeetCode 683
        int[] flowers = {1,3,2};
        int kEmpty = solution.kEmptySlots(flowers, 1);
        System.out.println("LeetCode 683 Result: " + kEmpty);
    }
}