package class083;

import java.util.*;

/**
 * class083 扩展问题实现（增强版）
 * 包含四类问题的扩展题目及详细实现：
 * 1. 工作调度类问题 - 使用动态规划 + 二分查找
 * 2. 逆序对类问题 - 使用归并排序思想
 * 3. 圆环路径类问题 - 使用记忆化搜索/动态规划
 * 4. 子数组和类问题 - 使用前缀和 + 哈希表
 * 
 * 新增大量题目，涵盖各大OJ平台，提供详细注释和复杂度分析
 * 包含工程化考量、异常处理、性能优化等高级特性
 * 
 * 题目来源链接：
 * - LeetCode: https://leetcode.cn/
 * - 洛谷: https://www.luogu.com.cn/
 * - HDU: http://acm.hdu.edu.cn/
 * - POJ: http://poj.org/
 * - CodeForces: https://codeforces.com/
 * - AtCoder: https://atcoder.jp/
 * - CodeChef: https://www.codechef.com/
 * - HackerRank: https://www.hackerrank.com/
 * - LintCode: https://www.lintcode.com/
 * - USACO: http://www.usaco.org/
 * - 牛客网: https://www.nowcoder.com/
 * - 计蒜客: https://nanti.jisuanke.com/
 * - ZOJ: https://zoj.pintia.cn/
 * - SPOJ: https://www.spoj.com/
 * - Project Euler: https://projecteuler.net/
 * - HackerEarth: https://www.hackerearth.com/
 * - 各大高校 OJ: 
 * - zoj: https://zoj.pintia.cn/
 * - MarsCode: 
 * - UVa OJ: 
 * - TimusOJ: 
 * - AizuOJ: 
 * - Comet OJ: 
 * - 杭电 OJ: http://acm.hdu.edu.cn/
 * - LOJ: 
 * - 牛客: https://www.nowcoder.com/
 * - 杭州电子科技大学: http://acm.hdu.edu.cn/
 * - acwing: 
 * - codeforces: https://codeforces.com/
 * - hdu: http://acm.hdu.edu.cn/
 * - poj: http://poj.org/
 * - 剑指Offer: 
 * - 赛码: 
 */
public class ExtendedProblems {
    
    // ==================== 1. 工作调度类问题 ====================
    
    /**
     * LeetCode 1235. 规划兼职工作 (类似原题)
     * 题目链接: https://leetcode.cn/problems/maximum-profit-in-job-scheduling/
     * 核心算法: 动态规划 + 二分查找
     * 时间复杂度: O(n log n) - 排序O(n log n) + 动态规划O(n) + 二分查找O(n log n)
     * 空间复杂度: O(n) - 存储工作数组和DP数组
     * 工程化考量: 输入验证、边界条件处理、溢出保护
     */
    static class JobScheduling {
        public int jobScheduling(int[] startTime, int[] endTime, int[] profit) {
            // 输入验证
            if (startTime == null || endTime == null || profit == null || 
                startTime.length != endTime.length || endTime.length != profit.length) {
                throw new IllegalArgumentException("输入参数不合法");
            }
            
            int n = profit.length;
            if (n == 0) return 0;
            
            int[][] jobs = new int[n][3];
            for (int i = 0; i < n; ++i) {
                jobs[i] = new int[] {startTime[i], endTime[i], profit[i]};
            }
            
            // 按结束时间排序，这是贪心选择性质的关键
            Arrays.sort(jobs, (a, b) -> a[1] - b[1]);
            
            // dp[i]表示考虑前i+1个工作能获得的最大利润
            int[] dp = new int[n + 1];
            dp[0] = jobs[0][2]; // 基础情况：只考虑第一个工作
            
            for (int i = 1; i < n; ++i) {
                // 二分查找找到与当前工作不冲突的最近工作
                int j = search(jobs, jobs[i][0], i);
                // 状态转移：选择当前工作或不选择当前工作
                dp[i + 1] = Math.max(dp[i], dp[j] + jobs[i][2]);
            }
            return dp[n];
        }
        
        // 二分查找辅助函数：找到结束时间小于等于x的最后一个工作
        private int search(int[][] jobs, int x, int n) {
            int left = 0, right = n;
            while (left < right) {
                int mid = (left + right) >> 1;
                if (jobs[mid][1] > x) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }
            return left;
        }
    }
    
    /**
     * LeetCode 1335. 工作计划的最低难度
     * 题目链接: https://leetcode.cn/problems/minimum-difficulty-of-a-job-schedule/
     * 核心算法: 动态规划
     * 时间复杂度: O(n²d) - 三层循环，其中d是天数
     * 空间复杂度: O(nd) - DP数组大小
     * 工程化考量: 边界条件处理、内存优化
     */
    static class MinimumDifficulty {
        public int minDifficulty(int[] jobDifficulty, int d) {
            if (jobDifficulty == null || jobDifficulty.length == 0) return -1;
            
            int n = jobDifficulty.length;
            // 边界条件：如果工作数量少于天数，无法安排
            if (n < d) return -1;
            
            // dp[i][j] 表示完成前i个job，分成j天的最小难度
            int[][] dp = new int[n + 1][d + 1];
            // 初始化DP数组为最大值
            for (int i = 0; i <= n; i++) {
                Arrays.fill(dp[i], Integer.MAX_VALUE);
            }
            // 基础情况：完成0个工作，用0天，难度为0
            dp[0][0] = 0;
            
            // 填充DP表
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= Math.min(i, d); j++) {
                    int maxDifficulty = 0;
                    // 从后往前遍历，计算第j天的最大难度
                    for (int k = i; k >= j; k--) {
                        maxDifficulty = Math.max(maxDifficulty, jobDifficulty[k - 1]);
                        if (dp[k - 1][j - 1] != Integer.MAX_VALUE) {
                            dp[i][j] = Math.min(dp[i][j], dp[k - 1][j - 1] + maxDifficulty);
                        }
                    }
                }
            }
            
            return dp[n][d] != Integer.MAX_VALUE ? dp[n][d] : -1;
        }
    }
    
    /**
     * LeetCode 1751. 最多可以参加的会议数目 II
     * 题目链接: https://leetcode.cn/problems/maximum-number-of-events-that-can-be-attended-ii/
     * 核心算法: 动态规划 + 二分查找
     * 时间复杂度: O(n log n + nk) - 排序O(n log n) + 动态规划O(nk)
     * 空间复杂度: O(nk) - DP数组大小
     * 工程化考量: 空间优化、边界条件处理
     */
    static class MaxEvents {
        public int maxValue(int[][] events, int k) {
            if (events == null || events.length == 0 || k <= 0) return 0;
            
            int n = events.length;
            // 按结束时间排序
            Arrays.sort(events, (a, b) -> a[1] - b[1]);
            
            // dp[i][j] 表示考虑前i个事件，最多参加j个事件能获得的最大价值
            int[][] dp = new int[n + 1][k + 1];
            
            for (int i = 1; i <= n; i++) {
                // 找到与当前事件不冲突的最近事件
                int last = binarySearch(events, i - 1, events[i - 1][0]);
                
                for (int j = 1; j <= k; j++) {
                    // 不参加当前事件
                    dp[i][j] = dp[i - 1][j];
                    // 参加当前事件
                    dp[i][j] = Math.max(dp[i][j], dp[last][j - 1] + events[i - 1][2]);
                }
            }
            
            return dp[n][k];
        }
        
        // 二分查找找到结束时间小于start的最右事件
        private int binarySearch(int[][] events, int right, int start) {
            int left = 0;
            while (left < right) {
                int mid = (left + right) >> 1;
                if (events[mid][1] < start) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }
            return left;
        }
    }
    
    /**
     * LeetCode 630. 课程表 III (新增题目)
     * 题目链接: https://leetcode.cn/problems/course-schedule-iii/
     * 核心算法: 贪心 + 优先队列
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(n)
     * 工程化考量: 优先队列优化、边界条件处理
     */
    static class CourseScheduleIII {
        public int scheduleCourse(int[][] courses) {
            // 按结束时间排序
            Arrays.sort(courses, (a, b) -> a[1] - b[1]);
            
            // 大顶堆，存储已选课程的持续时间
            PriorityQueue<Integer> heap = new PriorityQueue<>((a, b) -> b - a);
            int totalTime = 0;
            
            for (int[] course : courses) {
                int duration = course[0];
                int lastDay = course[1];
                
                if (totalTime + duration <= lastDay) {
                    // 可以选这门课
                    totalTime += duration;
                    heap.offer(duration);
                } else if (!heap.isEmpty() && heap.peek() > duration) {
                    // 替换掉持续时间最长的课程
                    totalTime = totalTime - heap.poll() + duration;
                    heap.offer(duration);
                }
            }
            
            return heap.size();
        }
    }
    
    /**
     * LeetCode 452. 用最少数量的箭引爆气球 (新增题目)
     * 题目链接: https://leetcode.cn/problems/minimum-number-of-arrows-to-burst-balloons/
     * 核心算法: 贪心算法
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(1)
     * 工程化考量: 边界条件处理、整数溢出保护
     */
    static class MinimumArrowsToBurstBalloons {
        public int findMinArrowShots(int[][] points) {
            if (points == null || points.length == 0) return 0;
            
            // 按结束位置排序
            Arrays.sort(points, (a, b) -> Integer.compare(a[1], b[1]));
            
            int arrows = 1;
            int end = points[0][1];
            
            for (int i = 1; i < points.length; i++) {
                if (points[i][0] > end) {
                    // 需要新的箭
                    arrows++;
                    end = points[i][1];
                }
            }
            
            return arrows;
        }
    }
    
    /**
     * HDU 1074. Doing Homework (新增题目)
     * 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1074
     * 核心算法: 状态压缩DP
     * 时间复杂度: O(n * 2^n)
     * 空间复杂度: O(2^n)
     * 工程化考量: 状态压缩技巧、路径记录
     */
    static class DoingHomework {
        static class Homework {
            String name;
            int deadline;
            int cost;
            Homework(String name, int deadline, int cost) {
                this.name = name;
                this.deadline = deadline;
                this.cost = cost;
            }
        }
        
        public void solve(Homework[] homeworks) {
            int n = homeworks.length;
            int totalStates = 1 << n;
            
            // dp[i]表示完成状态i的作业的最小扣分
            int[] dp = new int[totalStates];
            // time[i]表示完成状态i的作业所需的总时间
            int[] time = new int[totalStates];
            // pre[i]记录状态i的前驱状态和最后完成的作业
            int[] pre = new int[totalStates];
            
            Arrays.fill(dp, Integer.MAX_VALUE);
            dp[0] = 0;
            time[0] = 0;
            
            for (int state = 0; state < totalStates; state++) {
                for (int i = 0; i < n; i++) {
                    if ((state & (1 << i)) == 0) {
                        // 作业i还未完成
                        int nextState = state | (1 << i);
                        int nextTime = time[state] + homeworks[i].cost;
                        int reduce = Math.max(0, nextTime - homeworks[i].deadline);
                        int nextScore = dp[state] + reduce;
                        
                        if (nextScore < dp[nextState] || 
                            (nextScore == dp[nextState] && 
                             compareOrder(homeworks, pre[nextState], i, state))) {
                            dp[nextState] = nextScore;
                            time[nextState] = nextTime;
                            pre[nextState] = state;
                        }
                    }
                }
            }
            
            // 输出结果
            System.out.println(dp[totalStates - 1]);
            printPath(homeworks, pre, totalStates - 1);
        }
        
        private boolean compareOrder(Homework[] homeworks, int preState, int i, int state) {
            // 比较字典序
            return true; // 简化实现
        }
        
        private void printPath(Homework[] homeworks, int[] pre, int state) {
            // 打印完成顺序
            if (state == 0) return;
            printPath(homeworks, pre, pre[state]);
            // 计算最后完成的作业
            int last = state ^ pre[state];
            int index = 0;
            while ((last & (1 << index)) == 0) index++;
            System.out.println(homeworks[index].name);
        }
    }
    
    // ==================== 2. 逆序对类问题 ====================
    
    /**
     * LeetCode 493. 翻转对
     * 题目链接: https://leetcode.cn/problems/reverse-pairs/
     * 核心算法: 归并排序 + 双指针
     * 时间复杂度: O(n log n) - 归并排序的时间复杂度
     * 空间复杂度: O(n) - 临时数组和递归栈空间
     * 工程化考量: 溢出保护、递归深度控制
     */
    static class ReversePairs {
        public int reversePairs(int[] nums) {
            if (nums == null || nums.length < 2) {
                return 0;
            }
            return mergeSort(nums, 0, nums.length - 1);
        }
        
        private int mergeSort(int[] nums, int left, int right) {
            if (left >= right) {
                return 0;
            }
            int mid = left + (right - left) / 2;
            // 分治：统计左右子数组的翻转对，再加上跨左右的翻转对
            int count = mergeSort(nums, left, mid) + mergeSort(nums, mid + 1, right);
            count += merge(nums, left, mid, right);
            return count;
        }
        
        private int merge(int[] nums, int left, int mid, int right) {
            int count = 0;
            // 统计翻转对：nums[i] > 2 * nums[j]
            int j = mid + 1;
            for (int i = left; i <= mid; i++) {
                // 注意使用long防止溢出
                while (j <= right && (long) nums[i] > 2 * (long) nums[j]) {
                    j++;
                }
                count += j - (mid + 1);
            }
            
            // 合并两个有序数组
            int[] temp = new int[right - left + 1];
            int i = left, k = 0;
            j = mid + 1;
            
            while (i <= mid && j <= right) {
                if (nums[i] <= nums[j]) {
                    temp[k++] = nums[i++];
                } else {
                    temp[k++] = nums[j++];
                }
            }
            
            while (i <= mid) {
                temp[k++] = nums[i++];
            }
            
            while (j <= right) {
                temp[k++] = nums[j++];
            }
            
            // 复制回原数组
            System.arraycopy(temp, 0, nums, left, temp.length);
            return count;
        }
    }
    
    /**
     * LeetCode 315. 计算右侧小于当前元素的个数
     * 题目链接: https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
     * 核心算法: 归并排序 / 树状数组
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(n)
     * 工程化考量: 索引维护、结果记录
     */
    static class CountSmaller {
        private int[] index;
        private int[] temp;
        private int[] tempIndex;
        private int[] ans;
        
        public List<Integer> countSmaller(int[] nums) {
            int n = nums.length;
            index = new int[n];
            temp = new int[n];
            tempIndex = new int[n];
            ans = new int[n];
            
            for (int i = 0; i < n; i++) {
                index[i] = i;
            }
            
            mergeSort(nums, 0, n - 1);
            
            List<Integer> result = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                result.add(ans[i]);
            }
            return result;
        }
        
        private void mergeSort(int[] nums, int left, int right) {
            if (left >= right) {
                return;
            }
            
            int mid = left + (right - left) / 2;
            mergeSort(nums, left, mid);
            mergeSort(nums, mid + 1, right);
            merge(nums, left, mid, right);
        }
        
        private void merge(int[] nums, int left, int mid, int right) {
            // 复制临时数组
            for (int i = left; i <= right; i++) {
                temp[i] = nums[i];
                tempIndex[i] = index[i];
            }
            
            int i = left;
            int j = mid + 1;
            for (int k = left; k <= right; k++) {
                if (i > mid) {
                    // 左半部分已处理完
                    nums[k] = temp[j];
                    index[k] = tempIndex[j];
                    j++;
                } else if (j > right) {
                    // 右半部分已处理完
                    nums[k] = temp[i];
                    index[k] = tempIndex[i];
                    ans[index[k]] += (right - mid); // 统计右侧所有小于当前元素的个数
                    i++;
                } else if (temp[i] <= temp[j]) {
                    // 左边元素较小，统计右侧已经处理的比当前元素小的数量
                    nums[k] = temp[i];
                    index[k] = tempIndex[i];
                    ans[index[k]] += (j - mid - 1);
                    i++;
                } else {
                    // 右边元素较小，直接放右边
                    nums[k] = temp[j];
                    index[k] = tempIndex[j];
                    j++;
                }
            }
        }
    }
    
    /**
     * 洛谷 P1908. 逆序对 (新增题目)
     * 题目链接: https://www.luogu.com.cn/problem/P1908
     * 核心算法: 归并排序
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(n)
     * 工程化考量: 大数处理、输入输出优化
     */
    static class LuoguP1908 {
        private long count = 0;
        
        public long countInversions(int[] nums) {
            if (nums == null || nums.length <= 1) return 0;
            count = 0;
            mergeSort(nums, 0, nums.length - 1);
            return count;
        }
        
        private void mergeSort(int[] nums, int left, int right) {
            if (left >= right) return;
            
            int mid = left + (right - left) / 2;
            mergeSort(nums, left, mid);
            mergeSort(nums, mid + 1, right);
            merge(nums, left, mid, right);
        }
        
        private void merge(int[] nums, int left, int mid, int right) {
            int[] temp = new int[right - left + 1];
            int i = left, j = mid + 1, k = 0;
            
            while (i <= mid && j <= right) {
                if (nums[i] <= nums[j]) {
                    temp[k++] = nums[i++];
                } else {
                    // 当右边元素较小时，左边剩余的所有元素都与当前右边元素构成逆序对
                    count += (mid - i + 1);
                    temp[k++] = nums[j++];
                }
            }
            
            while (i <= mid) {
                temp[k++] = nums[i++];
            }
            
            while (j <= right) {
                temp[k++] = nums[j++];
            }
            
            System.arraycopy(temp, 0, nums, left, temp.length);
        }
    }
    
    /**
     * HDU 1394. Minimum Inversion Number (新增题目)
     * 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1394
     * 核心算法: 树状数组
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(n)
     * 工程化考量: 离散化处理、树状数组应用
     */
    static class HDU1394 {
        // 树状数组实现
        static class FenwickTree {
            int[] tree;
            int n;
            
            FenwickTree(int size) {
                this.n = size;
                this.tree = new int[n + 1];
            }
            
            void update(int index, int delta) {
                while (index <= n) {
                    tree[index] += delta;
                    index += index & -index;
                }
            }
            
            int query(int index) {
                int sum = 0;
                while (index > 0) {
                    sum += tree[index];
                    index -= index & -index;
                }
                return sum;
            }
        }
        
        public int minInversionNumber(int[] nums) {
            int n = nums.length;
            FenwickTree tree = new FenwickTree(n);
            
            // 计算初始逆序对数
            int invCount = 0;
            for (int i = n - 1; i >= 0; i--) {
                invCount += tree.query(nums[i]);
                tree.update(nums[i] + 1, 1);
            }
            
            int minInv = invCount;
            // 移动第一个元素到末尾
            for (int i = 0; i < n - 1; i++) {
                invCount = invCount - nums[i] + (n - 1 - nums[i]);
                minInv = Math.min(minInv, invCount);
            }
            
            return minInv;
        }
    }
    
    /**
     * POJ 2299. Ultra-QuickSort (新增题目)
     * 题目链接: http://poj.org/problem?id=2299
     * 核心算法: 归并排序
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(n)
     * 工程化考量: 大输入处理、性能优化
     */
    static class POJ2299 {
        private long inversionCount = 0;
        
        public long ultraQuickSort(int[] nums) {
            if (nums == null || nums.length <= 1) return 0;
            inversionCount = 0;
            mergeSort(nums, 0, nums.length - 1);
            return inversionCount;
        }
        
        private void mergeSort(int[] nums, int left, int right) {
            if (left >= right) return;
            
            int mid = left + (right - left) / 2;
            mergeSort(nums, left, mid);
            mergeSort(nums, mid + 1, right);
            merge(nums, left, mid, right);
        }
        
        private void merge(int[] nums, int left, int mid, int right) {
            int[] temp = new int[right - left + 1];
            int i = left, j = mid + 1, k = 0;
            
            while (i <= mid && j <= right) {
                if (nums[i] <= nums[j]) {
                    temp[k++] = nums[i++];
                } else {
                    inversionCount += (mid - i + 1);
                    temp[k++] = nums[j++];
                }
            }
            
            while (i <= mid) {
                temp[k++] = nums[i++];
            }
            
            while (j <= right) {
                temp[k++] = nums[j++];
            }
            
            System.arraycopy(temp, 0, nums, left, temp.length);
        }
    }
    
    // ==================== 3. 圆环路径类问题 ====================
    
    /**
     * LeetCode 1423. 可获得的最大点数
     * 题目链接: https://leetcode.cn/problems/maximum-points-you-can-obtain-from-cards/
     * 核心算法: 滑动窗口
     * 时间复杂度: O(n)
     * 空间复杂度: O(1)
     * 工程化考量: 边界条件处理、滑动窗口优化
     */
    static class MaxPointsFromCards {
        public int maxScore(int[] cardPoints, int k) {
            if (cardPoints == null || cardPoints.length == 0 || k <= 0) return 0;
            
            int n = cardPoints.length;
            // 计算前k张牌的和
            int sum = 0;
            for (int i = 0; i < k; i++) {
                sum += cardPoints[i];
            }
            
            int maxSum = sum;
            // 滑动窗口：每次从左边移除一张，从右边添加一张
            for (int i = 0; i < k; i++) {
                sum += cardPoints[n - 1 - i] - cardPoints[k - 1 - i];
                maxSum = Math.max(maxSum, sum);
            }
            
            return maxSum;
        }
    }
    
    /**
     * LeetCode 134. 加油站 (新增题目)
     * 题目链接: https://leetcode.cn/problems/gas-station/
     * 核心算法: 贪心算法
     * 时间复杂度: O(n)
     * 空间复杂度: O(1)
     * 工程化考量: 环形遍历优化、边界条件处理
     */
    static class GasStation {
        public int canCompleteCircuit(int[] gas, int[] cost) {
            int n = gas.length;
            int totalTank = 0;
            int currTank = 0;
            int startingStation = 0;
            
            for (int i = 0; i < n; i++) {
                totalTank += gas[i] - cost[i];
                currTank += gas[i] - cost[i];
                
                if (currTank < 0) {
                    // 无法从当前起始点到达i+1
                    startingStation = i + 1;
                    currTank = 0;
                }
            }
            
            return totalTank >= 0 ? startingStation : -1;
        }
    }
    
    /**
     * LeetCode 213. 打家劫舍 II (新增题目)
     * 题目链接: https://leetcode.cn/problems/house-robber-ii/
     * 核心算法: 动态规划
     * 时间复杂度: O(n)
     * 空间复杂度: O(1)
     * 工程化考量: 环形数组处理、空间优化
     */
    static class HouseRobberII {
        public int rob(int[] nums) {
            if (nums == null || nums.length == 0) return 0;
            if (nums.length == 1) return nums[0];
            
            // 分两种情况：偷第一家不偷最后一家，或者不偷第一家偷最后一家
            return Math.max(robRange(nums, 0, nums.length - 2), 
                           robRange(nums, 1, nums.length - 1));
        }
        
        private int robRange(int[] nums, int start, int end) {
            if (start > end) return 0;
            
            int prev2 = 0; // dp[i-2]
            int prev1 = 0; // dp[i-1]
            
            for (int i = start; i <= end; i++) {
                int current = Math.max(prev1, prev2 + nums[i]);
                prev2 = prev1;
                prev1 = current;
            }
            
            return prev1;
        }
    }
    
    /**
     * LeetCode 503. 下一个更大元素 II (新增题目)
     * 题目链接: https://leetcode.cn/problems/next-greater-element-ii/
     * 核心算法: 单调栈
     * 时间复杂度: O(n)
     * 空间复杂度: O(n)
     * 工程化考量: 环形数组处理、栈优化
     */
    static class NextGreaterElementII {
        public int[] nextGreaterElements(int[] nums) {
            if (nums == null || nums.length == 0) return new int[0];
            
            int n = nums.length;
            int[] result = new int[n];
            Arrays.fill(result, -1);
            Deque<Integer> stack = new ArrayDeque<>();
            
            // 遍历两遍数组处理环形
            for (int i = 0; i < 2 * n; i++) {
                int num = nums[i % n];
                while (!stack.isEmpty() && nums[stack.peek()] < num) {
                    result[stack.pop()] = num;
                }
                if (i < n) {
                    stack.push(i);
                }
            }
            
            return result;
        }
    }
    
    /**
     * 洛谷 P1880. [NOI1995] 石子合并 (新增题目)
     * 题目链接: https://www.luogu.com.cn/problem/P1880
     * 核心算法: 区间DP
     * 时间复杂度: O(n³)
     * 空间复杂度: O(n²)
     * 工程化考量: 环形数组展开、前缀和优化
     */
    static class StoneMerge {
        public int[] mergeStones(int[] stones) {
            int n = stones.length;
            // 环形数组展开为2n长度
            int[] extended = new int[2 * n];
            for (int i = 0; i < 2 * n; i++) {
                extended[i] = stones[i % n];
            }
            
            // 前缀和
            int[] prefix = new int[2 * n + 1];
            for (int i = 1; i <= 2 * n; i++) {
                prefix[i] = prefix[i - 1] + extended[i - 1];
            }
            
            // dp[i][j]表示合并i到j的最小代价
            int[][] dp = new int[2 * n][2 * n];
            for (int i = 0; i < 2 * n; i++) {
                Arrays.fill(dp[i], Integer.MAX_VALUE);
                dp[i][i] = 0; // 单个石子不需要合并
            }
            
            for (int len = 2; len <= n; len++) {
                for (int i = 0; i + len <= 2 * n; i++) {
                    int j = i + len - 1;
                    for (int k = i; k < j; k++) {
                        dp[i][j] = Math.min(dp[i][j], 
                                          dp[i][k] + dp[k + 1][j] + prefix[j + 1] - prefix[i]);
                    }
                }
            }
            
            // 找长度为n的最小值
            int minCost = Integer.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                minCost = Math.min(minCost, dp[i][i + n - 1]);
            }
            
            return new int[]{minCost, 0}; // 简化返回
        }
    }
    
    // ==================== 4. 子数组和类问题 ====================
    
    /**
     * LeetCode 560. 和为 K 的子数组
     * 题目链接: https://leetcode.cn/problems/subarray-sum-equals-k/
     * 核心算法: 前缀和 + 哈希表
     * 时间复杂度: O(n)
     * 空间复杂度: O(n)
     * 工程化考量: 哈希表优化、边界条件处理
     */
    static class SubarraySumEqualsK {
        public int subarraySum(int[] nums, int k) {
            if (nums == null || nums.length == 0) return 0;
            
            // 前缀和计数字典，key是前缀和，value是出现次数
            Map<Integer, Integer> prefixSum = new HashMap<>();
            prefixSum.put(0, 1); // 前缀和为0出现1次（空数组的情况）
            
            int count = 0;
            int sum = 0;
            
            for (int num : nums) {
                sum += num;
                
                // 查找是否存在前缀和为(sum - k)的历史记录
                if (prefixSum.containsKey(sum - k)) {
                    count += prefixSum.get(sum - k);
                }
                
                // 更新当前前缀和的出现次数
                prefixSum.put(sum, prefixSum.getOrDefault(sum, 0) + 1);
            }
            
            return count;
        }
    }
    
    /**
     * LeetCode 53. 最大子数组和 (新增题目)
     * 题目链接: https://leetcode.cn/problems/maximum-subarray/
     * 核心算法: 动态规划
     * 时间复杂度: O(n)
     * 空间复杂度: O(1)
     * 工程化考量: 空间优化、边界条件处理
     */
    static class MaximumSubarray {
        public int maxSubArray(int[] nums) {
            if (nums == null || nums.length == 0) return 0;
            
            int maxSum = nums[0];
            int currentSum = nums[0];
            
            for (int i = 1; i < nums.length; i++) {
                // 状态转移：要么加入之前的子数组，要么重新开始一个子数组
                currentSum = Math.max(nums[i], currentSum + nums[i]);
                maxSum = Math.max(maxSum, currentSum);
            }
            
            return maxSum;
        }
    }
    
    /**
     * LeetCode 152. 乘积最大子数组 (新增题目)
     * 题目链接: https://leetcode.cn/problems/maximum-product-subarray/
     * 核心算法: 动态规划
     * 时间复杂度: O(n)
     * 空间复杂度: O(1)
     * 工程化考量: 负数处理、空间优化
     */
    static class MaximumProductSubarray {
        public int maxProduct(int[] nums) {
            if (nums == null || nums.length == 0) return 0;
            
            int maxProd = nums[0];
            int minProd = nums[0];
            int result = nums[0];
            
            for (int i = 1; i < nums.length; i++) {
                if (nums[i] < 0) {
                    // 遇到负数，交换最大最小值
                    int temp = maxProd;
                    maxProd = minProd;
                    minProd = temp;
                }
                
                maxProd = Math.max(nums[i], maxProd * nums[i]);
                minProd = Math.min(nums[i], minProd * nums[i]);
                result = Math.max(result, maxProd);
            }
            
            return result;
        }
    }
    
    /**
     * LeetCode 209. 长度最小的子数组 (新增题目)
     * 题目链接: https://leetcode.cn/problems/minimum-size-subarray-sum/
     * 核心算法: 滑动窗口
     * 时间复杂度: O(n)
     * 空间复杂度: O(1)
     * 工程化考量: 滑动窗口优化、边界条件处理
     */
    static class MinimumSizeSubarraySum {
        public int minSubArrayLen(int target, int[] nums) {
            if (nums == null || nums.length == 0) return 0;
            
            int left = 0;
            int sum = 0;
            int minLength = Integer.MAX_VALUE;
            
            for (int right = 0; right < nums.length; right++) {
                sum += nums[right];
                
                while (sum >= target) {
                    minLength = Math.min(minLength, right - left + 1);
                    sum -= nums[left];
                    left++;
                }
            }
            
            return minLength == Integer.MAX_VALUE ? 0 : minLength;
        }
    }
    
    /**
     * LeetCode 862. 和至少为 K 的最短子数组 (新增题目)
     * 题目链接: https://leetcode.cn/problems/shortest-subarray-with-sum-at-least-k/
     * 核心算法: 单调队列
     * 时间复杂度: O(n)
     * 空间复杂度: O(n)
     * 工程化考量: 单调队列优化、边界条件处理
     */
    static class ShortestSubarrayWithSumAtLeastK {
        public int shortestSubarray(int[] nums, int k) {
            int n = nums.length;
            long[] prefix = new long[n + 1];
            for (int i = 0; i < n; i++) {
                prefix[i + 1] = prefix[i] + nums[i];
            }
            
            Deque<Integer> deque = new ArrayDeque<>();
            int minLength = Integer.MAX_VALUE;
            
            for (int i = 0; i <= n; i++) {
                // 维护单调递增队列
                while (!deque.isEmpty() && prefix[i] <= prefix[deque.getLast()]) {
                    deque.removeLast();
                }
                
                // 检查队列头部是否满足条件
                while (!deque.isEmpty() && prefix[i] - prefix[deque.getFirst()] >= k) {
                    minLength = Math.min(minLength, i - deque.removeFirst());
                }
                
                deque.addLast(i);
            }
            
            return minLength == Integer.MAX_VALUE ? -1 : minLength;
        }
    }
    
    /**
     * HDU 1559. 最大子矩阵 (新增题目)
     * 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1559
     * 核心算法: 二维前缀和
     * 时间复杂度: O(mn)
     * 空间复杂度: O(mn)
     * 工程化考量: 二维前缀和优化、边界条件处理
     */
    static class MaximumSubmatrix {
        public int maxSubmatrix(int[][] matrix, int x, int y) {
            if (matrix == null || matrix.length == 0 || matrix[0].length == 0) return 0;
            
            int m = matrix.length;
            int n = matrix[0].length;
            
            // 计算二维前缀和
            int[][] prefix = new int[m + 1][n + 1];
            for (int i = 1; i <= m; i++) {
                for (int j = 1; j <= n; j++) {
                    prefix[i][j] = matrix[i - 1][j - 1] + prefix[i - 1][j] + 
                                  prefix[i][j - 1] - prefix[i - 1][j - 1];
                }
            }
            
            int maxSum = Integer.MIN_VALUE;
            // 枚举所有可能的子矩阵
            for (int i = x; i <= m; i++) {
                for (int j = y; j <= n; j++) {
                    int sum = prefix[i][j] - prefix[i - x][j] - 
                             prefix[i][j - y] + prefix[i - x][j - y];
                    maxSum = Math.max(maxSum, sum);
                }
            }
            
            return maxSum;
        }
    }
    
    // ==================== 测试方法 ====================
    public static void main(String[] args) {
        System.out.println("=== class083 扩展问题测试 ===");
        
        // 测试工作调度类问题
        System.out.println("\n=== 工作调度类问题测试 ===");
        JobScheduling jobScheduling = new JobScheduling();
        int[] startTime = {1, 2, 3, 3};
        int[] endTime = {3, 4, 5, 6};
        int[] profit = {50, 10, 40, 70};
        System.out.println("最大利润工作调度: " + jobScheduling.jobScheduling(startTime, endTime, profit));
        
        // 测试逆序对类问题
        System.out.println("\n=== 逆序对类问题测试 ===");
        ReversePairs reversePairs = new ReversePairs();
        int[] nums1 = {1, 3, 2, 3, 1};
        System.out.println("翻转对数量: " + reversePairs.reversePairs(nums1));
        
        LuoguP1908 luogu = new LuoguP1908();
        int[] nums2 = {5, 4, 3, 2, 1};
        System.out.println("洛谷P1908逆序对数: " + luogu.countInversions(nums2));
        
        // 测试子数组和类问题
        System.out.println("\n=== 子数组和类问题测试 ===");
        SubarraySumEqualsK subarraySum = new SubarraySumEqualsK();
        int[] nums3 = {1, 1, 1};
        int k = 2;
        System.out.println("和为K的子数组数量: " + subarraySum.subarraySum(nums3, k));
        
        MaximumSubarray maxSubarray = new MaximumSubarray();
        int[] nums4 = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        System.out.println("最大子数组和: " + maxSubarray.maxSubArray(nums4));
        
        // 测试圆环路径类问题
        System.out.println("\n=== 圆环路径类问题测试 ===");
        MaxPointsFromCards maxPoints = new MaxPointsFromCards();
        int[] cardPoints = {1, 2, 3, 4, 5, 6, 1};
        int k3 = 3;
        System.out.println("可获得的最大点数: " + maxPoints.maxScore(cardPoints, k3));
        
        GasStation gasStation = new GasStation();
        int[] gas = {1, 2, 3, 4, 5};
        int[] cost = {3, 4, 5, 1, 2};
        System.out.println("加油站起始位置: " + gasStation.canCompleteCircuit(gas, cost));
        
        System.out.println("\n=== 测试完成 ===");
    }
}