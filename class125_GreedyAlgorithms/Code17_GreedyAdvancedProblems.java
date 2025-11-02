package class094;

import java.util.*;

/**
 * 贪心算法高级题目集合 (Greedy Algorithm Advanced Problems Collection)
 * 包含更复杂的贪心算法问题和优化技巧
 * 涵盖区间调度、资源分配、路径优化等高级应用
 * 
 * 算法标签: 贪心算法(Greedy Algorithm)、高级应用(Advanced Applications)、多领域问题(Multi-domain Problems)
 * 时间复杂度: 各题目不同，详见具体函数注释
 * 空间复杂度: 各题目不同，详见具体函数注释
 * 相关题目: 课程调度、区间交集、会议安排、建筑攀爬、字符串处理等
 * 贪心算法专题 - 高级题目集合
 */
public class Code17_GreedyAdvancedProblems {

    /**
     * 题目1: LeetCode 630. 课程表 III (Course Schedule III)
     * 题目描述: 有n门课程，每门课程有持续时间duration和截止时间lastDay
     * 选择课程使得在截止时间前完成，求最多能选多少门课程
     * 链接: https://leetcode.cn/problems/course-schedule-iii/
     * 
     * 算法标签: 贪心算法(Greedy Algorithm)、优先队列(Priority Queue)、课程调度(Course Scheduling)
     * 时间复杂度: O(n log n) - 排序和堆操作
     * 空间复杂度: O(n) - 最大堆存储已选课程
     * 是否最优解: 是
     * 
     * 算法思路详解：
     * 1. 贪心策略核心：按截止时间排序，使用最大堆维护已选课程的持续时间，
     *    这样可以确保在时间不够时替换掉持续时间最长的课程
     * 2. 选择机制：如果当前课程可以完成则直接加入，否则替换最长课程
     * 3. 堆维护：使用最大堆快速获取已选课程中最长的持续时间
     * 
     * 工程化最佳实践：
     * 1. 排序优化：使用高效的比较器进行截止时间排序
     * 2. 堆操作：合理维护堆中元素的一致性
     * 3. 边界处理：妥善处理空数组和无课程情况
     * 
     * 实际应用场景：
     * 1. 教育管理：课程安排优化
     * 2. 项目管理：任务调度优化
     * 3. 资源分配：时间资源最优利用
     */
    public static int scheduleCourse(int[][] courses) {
        if (courses == null || courses.length == 0) {
            return 0;
        }
        
        // 按截止时间排序
        Arrays.sort(courses, (a, b) -> a[1] - b[1]);
        
        // 最大堆，存储已选课程的持续时间
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
        int currentTime = 0;
        
        for (int[] course : courses) {
            int duration = course[0];
            int lastDay = course[1];
            
            if (currentTime + duration <= lastDay) {
                // 可以完成当前课程
                currentTime += duration;
                maxHeap.offer(duration);
            } else if (!maxHeap.isEmpty() && maxHeap.peek() > duration) {
                // 替换掉持续时间最长的课程
                currentTime = currentTime - maxHeap.poll() + duration;
                maxHeap.offer(duration);
            }
        }
        
        return maxHeap.size();
    }

    /**
     * 题目2: LeetCode 757. 设置交集大小至少为2 (Set Intersection Size At Least Two)
     * 题目描述: 给定一组区间，选择最少的点使得每个区间至少包含2个点
     * 链接: https://leetcode.cn/problems/set-intersection-size-at-least-two/
     * 
     * 算法标签: 贪心算法(Greedy Algorithm)、区间覆盖(Interval Covering)、点选择(Point Selection)
     * 时间复杂度: O(n log n) - 排序的时间复杂度
     * 空间复杂度: O(1) - 常数空间维护两个点
     * 是否最优解: 是
     * 
     * 算法思路详解：
     * 1. 贪心策略核心：按结束位置排序，从后往前选择点，
     *    这样可以最大化点的复用率，最小化点的数量
     * 2. 点维护：维护两个最大的点，确保每个区间包含至少两个点
     * 3. 选择策略：根据当前区间与已选点的关系决定是否添加新点
     * 
     * 工程化最佳实践：
     * 1. 排序策略：结束位置升序，相同时开始位置降序
     * 2. 状态维护：准确维护两个最大点的位置
     * 3. 条件判断：优化点添加条件判断
     * 
     * 实际应用场景：
     * 1. 传感器部署：最少传感器覆盖所有区域
     * 2. 监控系统：最少摄像头覆盖所有通道
     * 3. 网络部署：最少基站覆盖所有区域
     */
    public static int intersectionSizeTwo(int[][] intervals) {
        if (intervals == null || intervals.length == 0) {
            return 0;
        }
        
        // 按结束位置升序排序，结束位置相同时按开始位置降序排序
        Arrays.sort(intervals, (a, b) -> {
            if (a[1] != b[1]) {
                return a[1] - b[1];
            } else {
                return b[0] - a[0];
            }
        });
        
        int result = 0;
        int first = -1, second = -1; // 维护两个最大的点
        
        for (int[] interval : intervals) {
            int start = interval[0];
            int end = interval[1];
            
            if (start > second) {
                // 需要添加两个新点
                result += 2;
                first = end - 1;
                second = end;
            } else if (start > first) {
                // 需要添加一个新点
                result += 1;
                first = second;
                second = end;
            }
            // 否则，当前区间已经包含至少两个点
        }
        
        return result;
    }

    /**
     * 题目3: LeetCode 1353. 最多可以参加的会议数目 (Maximum Number of Events That Can Be Attended)
     * 题目描述: 给定会议的开始和结束时间，每天只能参加一个会议，求最多能参加多少会议
     * 链接: https://leetcode.cn/problems/maximum-number-of-events-that-can-be-attended/
     * 
     * 算法标签: 贪心算法(Greedy Algorithm)、时间调度(Time Scheduling)、优先队列(Priority Queue)
     * 时间复杂度: O(n log n) - 排序和堆操作
     * 空间复杂度: O(n) - 最小堆存储可参加会议
     * 是否最优解: 是
     * 
     * 算法思路详解：
     * 1. 贪心策略核心：按开始时间排序，使用最小堆维护当前可参加的会议，
     *    每天选择结束时间最早的会议参加，这样可以为后续会议留出更多时间
     * 2. 时间模拟：逐天模拟会议参加过程
     * 3. 堆维护：动态维护当前可参加会议的结束时间
     * 
     * 工程化最佳实践：
     * 1. 时间管理：准确模拟每天的会议安排
     * 2. 堆操作：及时添加和移除过期会议
     * 3. 边界处理：妥善处理无会议和会议冲突情况
     * 
     * 实际应用场景：
     * 1. 会议安排：最优会议参与策略
     * 2. 任务调度：时间资源最优分配
     * 3. 活动规划：活动参与优化
     */
    public static int maxEvents(int[][] events) {
        if (events == null || events.length == 0) {
            return 0;
        }
        
        // 按开始时间排序
        Arrays.sort(events, (a, b) -> a[0] - b[0]);
        
        // 最小堆，存储当前可参加的会议的结束时间
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        int result = 0;
        int day = 1;
        int index = 0;
        int n = events.length;
        
        while (index < n || !minHeap.isEmpty()) {
            // 将今天开始的会议加入堆中
            while (index < n && events[index][0] == day) {
                minHeap.offer(events[index][1]);
                index++;
            }
            
            // 移除已经过期的会议
            while (!minHeap.isEmpty() && minHeap.peek() < day) {
                minHeap.poll();
            }
            
            // 参加结束时间最早的会议
            if (!minHeap.isEmpty()) {
                minHeap.poll();
                result++;
            }
            
            day++;
        }
        
        return result;
    }

    /**
     * 题目4: LeetCode 1642. 可以到达的最远建筑 (Furthest Building You Can Reach)
     * 题目描述: 使用梯子和砖块爬建筑，求能到达的最远建筑
     * 链接: https://leetcode.cn/problems/furthest-building-you-can-reach/
     * 
     * 算法标签: 贪心算法(Greedy Algorithm)、资源分配(Resource Allocation)、优先队列(Priority Queue)
     * 时间复杂度: O(n log k) - k是梯子数量
     * 空间复杂度: O(k) - 最小堆存储梯子使用
     * 是否最优解: 是
     * 
     * 算法思路详解：
     * 1. 贪心策略核心：使用最小堆维护已使用的梯子，
     *    优先使用梯子处理大的高度差，当梯子不够时用砖块替换最小的梯子使用
     * 2. 资源管理：合理分配梯子和砖块两种资源
     * 3. 替换策略：用砖块替换最小梯子使用以节省梯子
     * 
     * 工程化最佳实践：
     * 1. 资源优化：最大化梯子的使用价值
     * 2. 堆维护：准确维护梯子使用情况
     * 3. 边界处理：妥善处理无高度差和资源不足情况
     * 
     * 实际应用场景：
     * 1. 资源规划：有限资源下的最优分配
     * 2. 项目管理：工具和人力的最优使用
     * 3. 物流运输：运输工具的最优调度
     */
    public static int furthestBuilding(int[] heights, int bricks, int ladders) {
        if (heights == null || heights.length <= 1) {
            return 0;
        }
        
        // 最小堆，存储已使用的梯子对应的高度差
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        
        for (int i = 1; i < heights.length; i++) {
            int diff = heights[i] - heights[i - 1];
            
            if (diff > 0) {
                // 需要爬升
                minHeap.offer(diff);
                
                // 如果梯子不够用，用砖块替换最小的梯子使用
                if (minHeap.size() > ladders) {
                    bricks -= minHeap.poll();
                    if (bricks < 0) {
                        return i - 1; // 无法到达当前建筑
                    }
                }
            }
        }
        
        return heights.length - 1;
    }

    /**
     * 题目5: LeetCode 321. 拼接最大数 (Create Maximum Number)
     * 题目描述: 从两个数组中保持相对顺序地取数字，拼接成最大的k位数
     * 链接: https://leetcode.cn/problems/create-maximum-number/
     * 
     * 算法标签: 贪心算法(Greedy Algorithm)、序列合并(Sequence Merging)、单调栈(Monotonic Stack)
     * 时间复杂度: O(k * (m + n)) - m和n分别是两个数组长度
     * 空间复杂度: O(k) - 存储结果数组
     * 是否最优解: 是
     * 
     * 算法思路详解：
     * 1. 贪心策略核心：分别从两个数组中取指定长度的最大子序列，
     *    然后合并两个子序列得到最大结果
     * 2. 子序列获取：使用单调栈获取指定长度的最大子序列
     * 3. 序列合并：合并两个子序列时保持相对顺序
     * 
     * 工程化最佳实践：
     * 1. 枚举优化：枚举合理的子序列长度组合
     * 2. 序列比较：准确比较两个序列的字典序
     * 3. 内存管理：及时释放临时数组
     * 
     * 实际应用场景：
     * 1. 数据处理：最优数据组合生成
     * 2. 密码学：最大数字序列生成
     * 3. 金融计算：最优数字组合
     */
    public static int[] maxNumber(int[] nums1, int[] nums2, int k) {
        int m = nums1.length, n = nums2.length;
        int[] result = new int[k];
        
        // 枚举从nums1中取i个数字，从nums2中取k-i个数字
        for (int i = Math.max(0, k - n); i <= Math.min(k, m); i++) {
            int[] seq1 = getMaxSubsequence(nums1, i);
            int[] seq2 = getMaxSubsequence(nums2, k - i);
            int[] merged = merge(seq1, seq2);
            
            if (compare(merged, 0, result, 0) > 0) {
                result = merged;
            }
        }
        
        return result;
    }
    
    // 获取长度为k的最大子序列（单调栈）
    private static int[] getMaxSubsequence(int[] nums, int k) {
        if (k == 0) return new int[0];
        if (k == nums.length) return nums.clone();
        
        int[] stack = new int[k];
        int top = -1;
        int remain = nums.length - k; // 可以删除的数字数量
        
        for (int num : nums) {
            while (top >= 0 && stack[top] < num && remain > 0) {
                top--;
                remain--;
            }
            if (top < k - 1) {
                stack[++top] = num;
            } else {
                remain--;
            }
        }
        
        return stack;
    }
    
    // 合并两个子序列
    private static int[] merge(int[] nums1, int[] nums2) {
        int m = nums1.length, n = nums2.length;
        if (m == 0) return nums2;
        if (n == 0) return nums1;
        
        int[] result = new int[m + n];
        int i = 0, j = 0, idx = 0;
        
        while (i < m && j < n) {
            if (compare(nums1, i, nums2, j) > 0) {
                result[idx++] = nums1[i++];
            } else {
                result[idx++] = nums2[j++];
            }
        }
        
        while (i < m) result[idx++] = nums1[i++];
        while (j < n) result[idx++] = nums2[j++];
        
        return result;
    }
    
    // 比较两个序列的大小
    private static int compare(int[] nums1, int i, int[] nums2, int j) {
        while (i < nums1.length && j < nums2.length) {
            if (nums1[i] != nums2[j]) {
                return nums1[i] - nums2[j];
            }
            i++;
            j++;
        }
        return (nums1.length - i) - (nums2.length - j);
    }

    /**
     * 题目6: LeetCode 316. 去除重复字母 (Remove Duplicate Letters)
     * 题目描述: 去除字符串中的重复字母，使得每个字母只出现一次，并且结果字典序最小
     * 链接: https://leetcode.cn/problems/remove-duplicate-letters/
     * 
     * 算法标签: 贪心算法(Greedy Algorithm)、单调栈(Monotonic Stack)、字符串处理(String Processing)
     * 时间复杂度: O(n) - 一次遍历
     * 空间复杂度: O(n) - 栈和辅助数组空间
     * 是否最优解: 是
     * 
     * 算法思路详解：
     * 1. 贪心策略核心：使用单调栈维护结果字符串，
     *    记录每个字符的最后出现位置，保持栈内字符单调递增
     * 2. 栈维护：维护单调递增的字符序列
     * 3. 字符选择：当栈顶字符大于当前字符且后面还会出现时，弹出栈顶
     * 
     * 工程化最佳实践：
     * 1. 位置记录：准确记录每个字符的最后出现位置
     * 2. 栈操作：合理维护栈中字符的访问状态
     * 3. 结果构建：正确构建最终结果字符串
     * 
     * 实际应用场景：
     * 1. 字符串优化：生成字典序最小的字符串
     * 2. 密码处理：字符去重和优化
     * 3. 数据清洗：重复数据去除
     */
    public static String removeDuplicateLetters(String s) {
        if (s == null || s.isEmpty()) {
            return "";
        }
        
        // 记录每个字符的最后出现位置
        int[] lastPos = new int[26];
        for (int i = 0; i < s.length(); i++) {
            lastPos[s.charAt(i) - 'a'] = i;
        }
        
        boolean[] visited = new boolean[26];
        Deque<Character> stack = new ArrayDeque<>();
        
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            
            // 如果字符已经在栈中，跳过
            if (visited[c - 'a']) {
                continue;
            }
            
            // 维护单调栈：当栈顶字符大于当前字符且后面还会出现时，弹出栈顶
            while (!stack.isEmpty() && stack.peek() > c && lastPos[stack.peek() - 'a'] > i) {
                visited[stack.pop() - 'a'] = false;
            }
            
            stack.push(c);
            visited[c - 'a'] = true;
        }
        
        // 构建结果字符串
        StringBuilder result = new StringBuilder();
        while (!stack.isEmpty()) {
            result.append(stack.pop());
        }
        
        return result.reverse().toString();
    }

    /**
     * 题目7: LeetCode 768. 最多能完成排序的块 II (Max Chunks To Make Sorted II)
     * 题目描述: 将数组分成最多的块，使得每个块排序后连接起来的结果与整个数组排序后的结果相同
     * 链接: https://leetcode.cn/problems/max-chunks-to-make-sorted-ii/
     * 
     * 算法标签: 贪心算法(Greedy Algorithm)、数组分块(Array Chunking)、前缀处理(Prefix Processing)
     * 时间复杂度: O(n) - 一次遍历
     * 空间复杂度: O(n) - 前缀最大值和后缀最小值数组
     * 是否最优解: 是
     * 
     * 算法思路详解：
     * 1. 贪心策略核心：维护当前块的最大值和前缀最大值，
     *    当当前块的最大值小于等于后面所有数的最小值时，可以分块
     * 2. 分块条件：前缀最大值≤后缀最小值
     * 3. 块数统计：准确统计可分块数量
     * 
     * 工程化最佳实践：
     * 1. 前缀处理：准确计算前缀最大值和后缀最小值
     * 2. 分块判断：优化分块条件判断
     * 3. 边界处理：妥善处理数组边界情况
     * 
     * 实际应用场景：
     * 1. 数据分片：数组最优分片策略
     * 2. 并行处理：数据块并行处理优化
     * 3. 内存管理：大数组分块处理
     */
    public static int maxChunksToSorted(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        int n = arr.length;
        int[] maxLeft = new int[n];
        int[] minRight = new int[n];
        
        // 计算从左到右的最大值
        maxLeft[0] = arr[0];
        for (int i = 1; i < n; i++) {
            maxLeft[i] = Math.max(maxLeft[i - 1], arr[i]);
        }
        
        // 计算从右到左的最小值
        minRight[n - 1] = arr[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            minRight[i] = Math.min(minRight[i + 1], arr[i]);
        }
        
        int chunks = 0;
        for (int i = 0; i < n - 1; i++) {
            if (maxLeft[i] <= minRight[i + 1]) {
                chunks++;
            }
        }
        
        return chunks + 1; // 最后一块
    }

    /**
     * 题目8: LeetCode 1326. 灌溉花园的最少水龙头数目 (Minimum Number of Taps to Open to Water a Garden)
     * 题目描述: 水龙头可以灌溉一定范围的花园，求最少需要多少个水龙头
     * 链接: https://leetcode.cn/problems/minimum-number-of-taps-to-open-to-water-a-garden/
     * 
     * 算法标签: 贪心算法(Greedy Algorithm)、区间覆盖(Interval Covering)、资源优化(Resource Optimization)
     * 时间复杂度: O(n) - 一次遍历
     * 空间复杂度: O(n) - 区间数组存储
     * 是否最优解: 是
     * 
     * 算法思路详解：
     * 1. 贪心策略核心：区间覆盖问题，将水龙头转换为区间，
     *    使用贪心选择覆盖整个花园的最少水龙头
     * 2. 覆盖策略：找到能覆盖当前结束位置的最远水龙头
     * 3. 数量统计：准确统计所需水龙头数量
     * 
     * 工程化最佳实践：
     * 1. 区间转换：准确将水龙头转换为灌溉区间
     * 2. 覆盖判断：优化区间覆盖条件判断
     * 3. 异常处理：妥善处理无法覆盖情况
     * 
     * 实际应用场景：
     * 1. 设施部署：最少设施覆盖所有区域
     * 2. 网络建设：最少基站覆盖所有用户
     * 3. 监控部署：最少摄像头覆盖所有区域
     */
    public static int minTaps(int n, int[] ranges) {
        if (ranges == null || ranges.length != n + 1) {
            return -1;
        }
        
        // 创建区间数组
        int[][] intervals = new int[n + 1][2];
        for (int i = 0; i <= n; i++) {
            int left = Math.max(0, i - ranges[i]);
            int right = Math.min(n, i + ranges[i]);
            intervals[i] = new int[]{left, right};
        }
        
        // 按左端点排序
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
        
        int taps = 0;
        int currentEnd = 0;
        int farthest = 0;
        int i = 0;
        
        while (currentEnd < n) {
            // 找到能覆盖当前结束位置的最远水龙头
            while (i <= n && intervals[i][0] <= currentEnd) {
                farthest = Math.max(farthest, intervals[i][1]);
                i++;
            }
            
            if (farthest <= currentEnd) {
                return -1; // 无法覆盖
            }
            
            taps++;
            currentEnd = farthest;
            
            if (currentEnd >= n) {
                break;
            }
        }
        
        return taps;
    }

    // 测试函数
    public static void main(String[] args) {
        // 测试课程表III
        int[][] courses = {{100, 200}, {200, 1300}, {1000, 1250}, {2000, 3200}};
        System.out.println("课程表III测试: " + scheduleCourse(courses)); // 期望: 3
        
        // 测试去除重复字母
        System.out.println("去除重复字母测试: " + removeDuplicateLetters("bcabc")); // 期望: "abc"
        
        // 测试最多可以参加的会议数目
        int[][] events = {{1, 2}, {2, 3}, {3, 4}, {1, 2}};
        System.out.println("最多会议测试: " + maxEvents(events)); // 期望: 4
    }
}