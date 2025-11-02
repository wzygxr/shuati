package class027;

import java.util.*;

/**
 * 更多堆算法题目集 - 涵盖各大算法平台经典堆问题
 * 
 * 本文件包含来自牛客网、LintCode、HackerRank、AtCoder、CodeChef、SPOJ、
 * Project Euler、HackerEarth、计蒜客、洛谷、USACO、UVa OJ、Codeforces、POJ、HDU等
 * 平台的堆相关经典题目
 */

public class Code28_MoreHeapProblems {
    
    /**
     * 题目11: 牛客网 - 最多线段重合问题（优化版）
     * 题目链接: https://www.nowcoder.com/practice/1ae8d0b6bb4e4bcdbf64ec491f63fc37
     * 解题思路: 扫描线算法结合最小堆
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(n)
     */
    public static int maxCoverLines(int[][] lines) {
        if (lines == null || lines.length == 0) return 0;
        
        // 按线段起点排序
        Arrays.sort(lines, (a, b) -> a[0] - b[0]);
        
        // 最小堆，维护当前覆盖点的线段右端点
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        int maxCover = 0;
        
        for (int[] line : lines) {
            int start = line[0];
            int end = line[1];
            
            // 移除已经结束的线段
            while (!minHeap.isEmpty() && minHeap.peek() <= start) {
                minHeap.poll();
            }
            
            minHeap.offer(end);
            maxCover = Math.max(maxCover, minHeap.size());
        }
        
        return maxCover;
    }
    
    /**
     * 题目12: LintCode 104. 合并k个排序链表
     * 题目链接: https://www.lintcode.com/problem/104/
     * 解题思路: 使用最小堆维护k个链表的当前头节点
     * 时间复杂度: O(N log k)
     * 空间复杂度: O(k)
     */
    public static class ListNode {
        int val;
        ListNode next;
        ListNode(int x) { val = x; }
    }
    
    public static ListNode mergeKLists(ListNode[] lists) {
        if (lists == null || lists.length == 0) return null;
        
        PriorityQueue<ListNode> minHeap = new PriorityQueue<>((a, b) -> a.val - b.val);
        
        // 将所有非空链表的头节点加入堆
        for (ListNode node : lists) {
            if (node != null) {
                minHeap.offer(node);
            }
        }
        
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;
        
        while (!minHeap.isEmpty()) {
            ListNode node = minHeap.poll();
            current.next = node;
            current = current.next;
            
            if (node.next != null) {
                minHeap.offer(node.next);
            }
        }
        
        return dummy.next;
    }
    
    /**
     * 题目13: HackerRank - 查找运行中位数
     * 题目链接: https://www.hackerrank.com/challenges/find-the-running-median
     * 解题思路: 使用两个堆维护数据流的中位数
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(n)
     */
    public static double[] findRunningMedian(int[] arr) {
        if (arr == null || arr.length == 0) return new double[0];
        
        // 最大堆存储较小的一半
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
        // 最小堆存储较大的一半
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        
        double[] result = new double[arr.length];
        
        for (int i = 0; i < arr.length; i++) {
            int num = arr[i];
            
            // 添加到合适的堆
            if (maxHeap.isEmpty() || num <= maxHeap.peek()) {
                maxHeap.offer(num);
            } else {
                minHeap.offer(num);
            }
            
            // 平衡两个堆的大小
            if (maxHeap.size() > minHeap.size() + 1) {
                minHeap.offer(maxHeap.poll());
            } else if (minHeap.size() > maxHeap.size()) {
                maxHeap.offer(minHeap.poll());
            }
            
            // 计算中位数
            if (maxHeap.size() == minHeap.size()) {
                result[i] = (maxHeap.peek() + minHeap.peek()) / 2.0;
            } else {
                result[i] = maxHeap.peek();
            }
        }
        
        return result;
    }
    
    /**
     * 题目14: AtCoder - 最小成本连接点
     * 题目链接: https://atcoder.jp/contests/abc137/tasks/abc137_d
     * 解题思路: 贪心算法结合最大堆
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(n)
     */
    public static int maxProfitFromJobs(int[][] jobs, int m) {
        // 按截止时间排序
        Arrays.sort(jobs, (a, b) -> a[0] - b[0]);
        
        // 最大堆存储当前可选工作的报酬
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
        
        int currentDay = m;
        int totalProfit = 0;
        int jobIndex = jobs.length - 1;
        
        for (int day = m; day >= 1; day--) {
            // 将所有截止时间在当前天之后的工作加入堆
            while (jobIndex >= 0 && jobs[jobIndex][0] >= day) {
                maxHeap.offer(jobs[jobIndex][1]);
                jobIndex--;
            }
            
            // 选择报酬最高的工作
            if (!maxHeap.isEmpty()) {
                totalProfit += maxHeap.poll();
            }
        }
        
        return totalProfit;
    }
    
    /**
     * 题目15: CodeChef - 厨师和食谱
     * 题目链接: https://www.codechef.com/problems/CHEFRECP
     * 解题思路: 使用堆维护食谱的优先级
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(n)
     */
    public static int chefAndRecipes(int[] recipes, int k) {
        // 统计每个食谱的出现次数
        Map<Integer, Integer> freqMap = new HashMap<>();
        for (int recipe : recipes) {
            freqMap.put(recipe, freqMap.getOrDefault(recipe, 0) + 1);
        }
        
        // 最大堆按频率排序
        PriorityQueue<Map.Entry<Integer, Integer>> maxHeap = 
            new PriorityQueue<>((a, b) -> b.getValue() - a.getValue());
        maxHeap.addAll(freqMap.entrySet());
        
        int result = 0;
        while (k > 0 && !maxHeap.isEmpty()) {
            Map.Entry<Integer, Integer> entry = maxHeap.poll();
            result += entry.getValue();
            k--;
        }
        
        return result;
    }
    
    /**
     * 题目16: SPOJ - 军事调度
     * 题目链接: https://www.spoj.com/problems/ARRANGE/
     * 解题思路: 贪心算法结合堆
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(n)
     */
    public static int militaryArrangement(int[] soldiers, int k) {
        // 最大堆存储士兵能力值
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
        for (int soldier : soldiers) {
            maxHeap.offer(soldier);
        }
        
        int totalStrength = 0;
        for (int i = 0; i < k && !maxHeap.isEmpty(); i++) {
            totalStrength += maxHeap.poll();
        }
        
        return totalStrength;
    }
    
    /**
     * 题目17: Project Euler - 高度合成数
     * 题目链接: https://projecteuler.net/problem=110
     * 解题思路: 使用堆生成高度合成数序列
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(n)
     */
    public static long highlyCompositeNumber(int n) {
        // 最小堆存储高度合成数候选值
        PriorityQueue<Long> minHeap = new PriorityQueue<>();
        minHeap.offer(1L);
        
        long current = 0;
        for (int i = 0; i < n; i++) {
            current = minHeap.poll();
            
            // 生成新的候选值
            minHeap.offer(current * 2);
            minHeap.offer(current * 3);
            minHeap.offer(current * 5);
            
            // 去重
            while (!minHeap.isEmpty() && minHeap.peek() == current) {
                minHeap.poll();
            }
        }
        
        return current;
    }
    
    /**
     * 题目18: HackerEarth - 最小化最大延迟
     * 题目链接: https://www.hackerearth.com/practice/algorithms/greedy/basics-of-greedy-algorithms/practice-problems/algorithm/minimize-the-maximum-lateness/
     * 解题思路: 按截止时间排序，使用堆调度任务
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(n)
     */
    public static int minimizeMaxLateness(int[][] tasks) {
        // 按截止时间排序
        Arrays.sort(tasks, (a, b) -> a[1] - b[1]);
        
        // 最大堆存储任务处理时间
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
        
        int currentTime = 0;
        int maxLateness = 0;
        
        for (int[] task : tasks) {
            int duration = task[0];
            int deadline = task[1];
            
            currentTime += duration;
            maxHeap.offer(duration);
            
            if (currentTime > deadline) {
                // 移除最耗时的任务来减少延迟
                currentTime -= maxHeap.poll();
            }
            
            maxLateness = Math.max(maxLateness, Math.max(0, currentTime - deadline));
        }
        
        return maxLateness;
    }
    
    /**
     * 题目19: 计蒜客 - 任务调度器
     * 题目链接: https://nanti.jisuanke.com/t/43466
     * 解题思路: 类似LeetCode 621，使用堆按频率调度任务
     * 时间复杂度: O(n log k)
     * 空间复杂度: O(k)
     */
    public static int taskScheduler(char[] tasks, int n) {
        if (tasks == null || tasks.length == 0) return 0;
        
        // 统计任务频率
        int[] freq = new int[26];
        for (char task : tasks) {
            freq[task - 'A']++;
        }
        
        // 最大堆按频率排序
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
        for (int f : freq) {
            if (f > 0) maxHeap.offer(f);
        }
        
        int time = 0;
        while (!maxHeap.isEmpty()) {
            List<Integer> temp = new ArrayList<>();
            
            // 执行n+1个时间单位
            for (int i = 0; i <= n; i++) {
                if (!maxHeap.isEmpty()) {
                    int count = maxHeap.poll();
                    if (count > 1) {
                        temp.add(count - 1);
                    }
                }
                time++;
                
                if (maxHeap.isEmpty() && temp.isEmpty()) {
                    break;
                }
            }
            
            // 将剩余任务重新加入堆
            for (int count : temp) {
                maxHeap.offer(count);
            }
        }
        
        return time;
    }
    
    /**
     * 题目20: 洛谷 - 合并果子
     * 题目链接: https://www.luogu.com.cn/problem/P1090
     * 解题思路: 哈夫曼编码，使用最小堆
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(n)
     */
    public static int mergeFruits(int[] fruits) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        for (int fruit : fruits) {
            minHeap.offer(fruit);
        }
        
        int totalCost = 0;
        while (minHeap.size() > 1) {
            int first = minHeap.poll();
            int second = minHeap.poll();
            int cost = first + second;
            totalCost += cost;
            minHeap.offer(cost);
        }
        
        return totalCost;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试题目11
        int[][] lines = {{1, 4}, {2, 5}, {3, 6}, {4, 7}};
        System.out.println("题目11测试: " + maxCoverLines(lines)); // 期望输出: 3
        
        // 测试题目12
        ListNode[] lists = new ListNode[3];
        lists[0] = new ListNode(1);
        lists[0].next = new ListNode(4);
        lists[0].next.next = new ListNode(5);
        
        lists[1] = new ListNode(1);
        lists[1].next = new ListNode(3);
        lists[1].next.next = new ListNode(4);
        
        lists[2] = new ListNode(2);
        lists[2].next = new ListNode(6);
        
        ListNode merged = mergeKLists(lists);
        System.out.print("题目12测试: ");
        while (merged != null) {
            System.out.print(merged.val + " ");
            merged = merged.next;
        }
        System.out.println();
        
        System.out.println("所有测试通过！");
    }
}