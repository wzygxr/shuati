package class094;

import java.util.*;

/**
 * 贪心算法补充题目集合 (Greedy Algorithm Additional Problems Collection)
 * 收集来自LeetCode、HackerRank、牛客网、洛谷、Codeforces等平台的经典贪心算法题目
 * 每个题目都提供详细的注释、复杂度分析和工程化考量
 * 
 * 算法标签: 贪心算法(Greedy Algorithm)、多平台题目集合(Multi-platform Problem Collection)
 * 时间复杂度: 各题目不同，详见具体函数注释
 * 空间复杂度: 各题目不同，详见具体函数注释
 * 相关题目: 涵盖加油站、数字处理、数组分组、序列调整、背包问题等多个领域
 * 贪心算法专题 - 综合题目集合
 */
public class Code16_GreedyAdditionalProblems {

    /**
     * 题目1: LeetCode 134. 加油站 (Gas Station)
     * 题目描述: 在一条环路上有 n 个加油站，其中第 i 个加油站有汽油 gas[i] 升。
     * 你有一辆油箱容量无限的的汽车，从第 i 个加油站开往第 i+1 个加油站需要消耗汽油 cost[i] 升。
     * 你从其中的一个加油站出发，开始时油箱为空。
     * 如果你可以绕环路行驶一周，则返回出发时加油站的编号，否则返回 -1。
     * 链接: https://leetcode.cn/problems/gas-station/
     * 
     * 算法标签: 贪心算法(Greedy Algorithm)、环路遍历(Circular Traversal)、状态维护(State Maintenance)
     * 时间复杂度: O(n) - 一次遍历
     * 空间复杂度: O(1) - 常数空间
     * 是否最优解: 是
     * 
     * 算法思路详解：
     * 1. 贪心策略核心：如果从A到B的路上没油了，那么A到B之间的任何一个站点都不能作为起点，
     *    这是因为从A到B之间任何一点出发都会经过A到B这段油量不足的路径
     * 2. 状态维护：同时维护当前剩余油量和总剩余油量两个状态
     * 3. 可行性判断：如果总剩余油量为负，说明无论从哪一点出发都无法绕行一周
     * 
     * 工程化最佳实践：
     * 1. 输入验证：严格检查输入参数的有效性
     * 2. 边界处理：妥善处理空数组和长度不匹配的情况
     * 3. 状态一致性：确保当前油量和总油量状态的一致性
     * 
     * 实际应用场景：
     * 1. 物流配送：配送车辆路线规划
     * 2. 无人机巡检：无人机续航路径规划
     * 3. 游戏开发：角色移动路径规划
     */
    public static int canCompleteCircuit(int[] gas, int[] cost) {
        if (gas == null || cost == null || gas.length != cost.length) {
            return -1;
        }
        
        int totalGas = 0;      // 总剩余油量
        int currentGas = 0;    // 当前剩余油量
        int startStation = 0;   // 起始加油站
        
        for (int i = 0; i < gas.length; i++) {
            totalGas += gas[i] - cost[i];
            currentGas += gas[i] - cost[i];
            
            // 如果当前剩余油量为负，说明从startStation到i的路径不可行
            if (currentGas < 0) {
                startStation = i + 1;  // 从下一个站点重新开始
                currentGas = 0;        // 重置当前剩余油量
            }
        }
        
        // 如果总剩余油量为负，无法绕行一周
        return totalGas >= 0 ? startStation : -1;
    }

    /**
     * 题目2: LeetCode 402. 移掉K位数字 (Remove K Digits)
     * 题目描述: 给你一个以字符串表示的非负整数 num 和一个整数 k ，
     * 移除这个数中的 k 位数字，使得剩下的数字最小。
     * 请你以字符串形式返回这个最小的数字。
     * 链接: https://leetcode.cn/problems/remove-k-digits/
     * 
     * 算法标签: 贪心算法(Greedy Algorithm)、单调栈(Monotonic Stack)、字符串处理(String Processing)
     * 时间复杂度: O(n) - n是字符串长度
     * 空间复杂度: O(n) - 栈的空间
     * 是否最优解: 是
     * 
     * 算法思路详解：
     * 1. 贪心策略核心：使用单调栈维护递增序列，
     *    当遇到更小数字时删除栈顶较大数字以获得更小结果
     * 2. 单调性维护：保持栈内元素单调递增
     * 3. 删除策略：从左到右遍历，优先删除高位的大数字
     * 
     * 工程化最佳实践：
     * 1. 字符串处理：注意前导零的处理
     * 2. 边界情况：处理删除所有数字和空结果的情况
     * 3. 内存优化：及时释放不再使用的对象
     * 
     * 实际应用场景：
     * 1. 数字优化：生成最小数字组合
     * 2. 密码学：数字序列优化
     * 3. 数据处理：数值压缩优化
     */
    public static String removeKdigits(String num, int k) {
        if (num == null || num.length() <= k) {
            return "0";
        }
        
        Deque<Character> stack = new ArrayDeque<>();
        
        for (int i = 0; i < num.length(); i++) {
            char digit = num.charAt(i);
            // 当栈不为空，当前数字小于栈顶数字，且还有删除次数时，弹出栈顶
            while (!stack.isEmpty() && k > 0 && digit < stack.peek()) {
                stack.pop();
                k--;
            }
            stack.push(digit);
        }
        
        // 处理剩余的删除次数
        while (k > 0) {
            stack.pop();
            k--;
        }
        
        // 构建结果字符串
        StringBuilder result = new StringBuilder();
        while (!stack.isEmpty()) {
            result.append(stack.pop());
        }
        result.reverse();
        
        // 去除前导零
        int start = 0;
        while (start < result.length() && result.charAt(start) == '0') {
            start++;
        }
        
        return start == result.length() ? "0" : result.substring(start);
    }

    /**
     * 题目3: LeetCode 561. 数组拆分 I (Array Partition I)
     * 题目描述: 给定长度为 2n 的整数数组 nums ，你的任务是将这些数分成 n 对，
     * 例如 (a1, b1), (a2, b2), ..., (an, bn) ，使得从 1 到 n 的 min(ai, bi) 总和最大。
     * 返回该 最大总和 。
     * 链接: https://leetcode.cn/problems/array-partition-i/
     * 
     * 算法标签: 贪心算法(Greedy Algorithm)、排序(Sorting)、配对优化(Pairing Optimization)
     * 时间复杂度: O(n log n) - 排序的时间复杂度
     * 空间复杂度: O(1) - 常数空间
     * 是否最优解: 是
     * 
     * 算法思路详解：
     * 1. 贪心策略核心：排序后相邻两个数分为一组，取较小的那个，
     *    这样能保证每组的最小值尽可能大，从而最大化总和
     * 2. 排序优化：通过排序预处理，将问题转化为确定性最优解
     * 3. 配对机制：相邻元素配对确保最小值最大化
     * 
     * 工程化最佳实践：
     * 1. 输入验证：检查数组长度是否为偶数
     * 2. 边界处理：妥善处理空数组情况
     * 3. 索引优化：使用步长为2的循环提高效率
     * 
     * 实际应用场景：
     * 1. 资源分配：最优配对资源分配
     * 2. 团队组建：能力值最优配对
     * 3. 任务分组：任务难度最优匹配
     */
    public static int arrayPairSum(int[] nums) {
        if (nums == null || nums.length % 2 != 0) {
            return 0;
        }
        
        Arrays.sort(nums);
        int sum = 0;
        
        // 每隔一个元素取一个（即每对中的第一个元素）
        for (int i = 0; i < nums.length; i += 2) {
            sum += nums[i];
        }
        
        return sum;
    }

    /**
     * 题目4: LeetCode 665. 非递减数列 (Non-decreasing Array)
     * 题目描述: 给你一个长度为 n 的整数数组 nums ，请你判断在 最多 改变 1 个元素的情况下，
     * 该数组能否变成一个非递减数列。
     * 链接: https://leetcode.cn/problems/non-decreasing-array/
     * 
     * 算法标签: 贪心算法(Greedy Algorithm)、序列调整(Sequence Adjustment)、逆序处理(Reverse Order Processing)
     * 时间复杂度: O(n) - 一次遍历
     * 空间复杂度: O(1) - 常数空间
     * 是否最优解: 是
     * 
     * 算法思路详解：
     * 1. 贪心策略核心：遇到逆序对时，优先修改前面的数字，
     *    但如果修改前面数字会影响更多后续判断，则修改后面的数字
     * 2. 修改决策：通过比较nums[i-1]和nums[i+1]决定修改哪个元素
     * 3. 次数控制：最多只能修改1个元素
     * 
     * 工程化最佳实践：
     * 1. 边界处理：妥善处理数组边界情况
     * 2. 条件判断：优化修改决策条件判断
     * 3. 计数管理：准确统计修改次数
     * 
     * 实际应用场景：
     * 1. 数据清洗：修正数据序列中的异常值
     * 2. 信号处理：平滑信号序列中的噪声
     * 3. 质量控制：调整产品参数序列
     */
    public static boolean checkPossibility(int[] nums) {
        if (nums == null || nums.length <= 1) {
            return true;
        }
        
        int count = 0;
        for (int i = 0; i < nums.length - 1; i++) {
            if (nums[i] > nums[i + 1]) {
                count++;
                if (count > 1) {
                    return false;
                }
                
                // 贪心选择：优先修改nums[i]而不是nums[i+1]
                if (i > 0 && nums[i - 1] > nums[i + 1]) {
                    nums[i + 1] = nums[i]; // 必须修改nums[i+1]
                } else {
                    nums[i] = nums[i + 1]; // 修改nums[i]
                }
            }
        }
        
        return true;
    }

    /**
     * 题目5: HackerRank - Mark and Toys
     * 题目描述: 给定一个价格数组和预算，计算最多能买多少件玩具
     * 链接: https://www.hackerrank.com/challenges/mark-and-toys
     * 
     * 算法标签: 贪心算法(Greedy Algorithm)、预算优化(Budget Optimization)、排序(Sorting)
     * 时间复杂度: O(n log n) - 排序的时间复杂度
     * 空间复杂度: O(1) - 常数空间
     * 是否最优解: 是
     * 
     * 算法思路详解：
     * 1. 贪心策略核心：优先购买价格最低的玩具，
     *    这样可以在预算内购买最多数量的玩具
     * 2. 排序优化：通过排序预处理，将问题转化为确定性最优解
     * 3. 购买策略：依次购买直到预算不足
     * 
     * 工程化最佳实践：
     * 1. 输入验证：检查价格数组和预算的有效性
     * 2. 边界处理：妥善处理空数组和零预算情况
     * 3. 成本控制：准确计算累计成本
     * 
     * 实际应用场景：
     * 1. 电商购物：预算内商品选购优化
     * 2. 投资组合：资金分配优化
     * 3. 资源采购：有限预算下的采购策略
     */
    public static int maximumToys(int[] prices, int budget) {
        if (prices == null || prices.length == 0 || budget <= 0) {
            return 0;
        }
        
        Arrays.sort(prices);
        int count = 0;
        int currentCost = 0;
        
        for (int price : prices) {
            if (currentCost + price <= budget) {
                currentCost += price;
                count++;
            } else {
                break;
            }
        }
        
        return count;
    }

    /**
     * 题目6: HackerRank - Luck Balance
     * 题目描述: 参加比赛，重要比赛输了会减少运气，不重要比赛输了会增加运气
     * 最多可以输掉k场重要比赛，求最大运气值
     * 链接: https://www.hackerrank.com/challenges/luck-balance
     * 
     * 算法标签: 贪心算法(Greedy Algorithm)、策略选择(Strategy Selection)、排序(Sorting)
     * 时间复杂度: O(n log n) - 排序的时间复杂度
     * 空间复杂度: O(n) - 存储重要比赛
     * 是否最优解: 是
     * 
     * 算法思路详解：
     * 1. 贪心策略核心：输掉所有不重要比赛（增加运气），
     *    输掉k场运气值最大的重要比赛（最大化收益）
     * 2. 分类处理：将比赛分为重要和不重要两类分别处理
     * 3. 优先级排序：重要比赛按运气值降序排序
     * 
     * 工程化最佳实践：
     * 1. 数据分类：有效分离重要和不重要比赛
     * 2. 排序优化：使用高效排序算法
     * 3. 策略执行：准确执行输赢策略
     * 
     * 实际应用场景：
     * 1. 项目管理：重要任务优先级分配
     * 2. 投资决策：风险与收益平衡
     * 3. 竞争策略：资源投入优化
     */
    public static int luckBalance(int k, int[][] contests) {
        if (contests == null || contests.length == 0) {
            return 0;
        }
        
        List<Integer> important = new ArrayList<>();
        int totalLuck = 0;
        
        for (int[] contest : contests) {
            int luck = contest[0];
            int importance = contest[1];
            
            if (importance == 1) {
                important.add(luck);
            } else {
                totalLuck += luck; // 不重要比赛直接输掉
            }
        }
        
        // 重要比赛按运气值降序排序
        important.sort(Collections.reverseOrder());
        
        // 输掉前k场重要比赛（运气值最大的k场）
        for (int i = 0; i < important.size(); i++) {
            if (i < k) {
                totalLuck += important.get(i); // 输掉比赛，增加运气
            } else {
                totalLuck -= important.get(i); // 赢得比赛，减少运气
            }
        }
        
        return totalLuck;
    }

    /**
     * 题目7: 牛客网 - 疯狂的采药 (Crazy Herbs Collection)
     * 题目描述: 完全背包问题的贪心解法变种
     * 链接: https://ac.nowcoder.com/acm/problem/16557
     * 
     * 算法标签: 贪心算法(Greedy Algorithm)、背包问题变种(Knapsack Variant)、性价比优化(Cost-Performance Optimization)
     * 时间复杂度: O(n log n) - 排序的时间复杂度
     * 空间复杂度: O(1) - 常数空间
     * 是否最优解: 对于分数背包是最优解，对于0-1背包是近似解
     * 
     * 算法思路详解：
     * 1. 贪心策略核心：优先选择性价比（价值/时间）最高的草药，
     *    这样可以在有限时间内获得最大价值
     * 2. 排序优化：按性价比降序排序
     * 3. 采摘策略：依次采摘直到时间不足
     * 
     * 工程化最佳实践：
     * 1. 性价比计算：准确计算价值与时间比
     * 2. 边界处理：处理时间不足时的部分采摘
     * 3. 精度控制：注意浮点数运算精度
     * 
     * 实际应用场景：
     * 1. 资源分配：有限资源下的收益最大化
     * 2. 投资组合：投资标的性价比优化
     * 3. 任务调度：时间约束下的任务选择
     */
    public static int crazyHerbs(int time, int[][] herbs) {
        if (herbs == null || herbs.length == 0 || time <= 0) {
            return 0;
        }
        
        // 计算每种草药的性价比
        List<double[]> herbList = new ArrayList<>();
        for (int[] herb : herbs) {
            int value = herb[0];
            int cost = herb[1];
            double ratio = (double) value / cost;
            herbList.add(new double[]{value, cost, ratio});
        }
        
        // 按性价比降序排序
        herbList.sort((a, b) -> Double.compare(b[2], a[2]));
        
        int totalValue = 0;
        int remainingTime = time;
        
        for (double[] herb : herbList) {
            int value = (int) herb[0];
            int cost = (int) herb[1];
            
            if (remainingTime >= cost) {
                // 可以采摘完整的草药
                totalValue += value;
                remainingTime -= cost;
            } else {
                // 采摘部分草药（分数背包）
                totalValue += (int) (value * ((double) remainingTime / cost));
                break;
            }
        }
        
        return totalValue;
    }

    /**
     * 题目8: Codeforces 1360B - Honest Coach
     * 题目描述: 将运动员分成两组，使得两组运动员实力值的最小差值最大
     * 链接: https://codeforces.com/problemset/problem/1360/B
     * 
     * 算法标签: 贪心算法(Greedy Algorithm)、分组优化(Grouping Optimization)、排序(Sorting)
     * 时间复杂度: O(n log n) - 排序的时间复杂度
     * 空间复杂度: O(1) - 常数空间
     * 是否最优解: 是
     * 
     * 算法思路详解：
     * 1. 贪心策略核心：排序后相邻两个运动员的实力差最小，
     *    因此最小的相邻差值就是两组实力最接近的分组
     * 2. 排序优化：通过排序预处理，将问题转化为相邻元素比较
     * 3. 差值计算：找到最小的相邻差值
     * 
     * 工程化最佳实践：
     * 1. 输入验证：检查运动员实力数组的有效性
     * 2. 边界处理：妥善处理少于2个运动员的情况
     * 3. 差值优化：使用一次遍历找到最小差值
     * 
     * 实际应用场景：
     * 1. 体育竞赛：实力均衡的队伍分组
     * 2. 教育分班：学生能力均衡分班
     * 3. 团队建设：技能均衡的团队分配
     */
    public static int honestCoach(int[] skills) {
        if (skills == null || skills.length <= 1) {
            return 0;
        }
        
        Arrays.sort(skills);
        int minDiff = Integer.MAX_VALUE;
        
        // 找到最小的相邻差值
        for (int i = 1; i < skills.length; i++) {
            minDiff = Math.min(minDiff, skills[i] - skills[i - 1]);
        }
        
        return minDiff;
    }

    /**
     * 题目9: 洛谷 P1803 - 凌乱的yyy / 线段覆盖 (Segment Coverage)
     * 题目描述: 选择最多的不重叠区间（活动选择问题）
     * 链接: https://www.luogu.com.cn/problem/P1803
     * 
     * 算法标签: 贪心算法(Greedy Algorithm)、区间调度(Interval Scheduling)、活动选择(Activity Selection)
     * 时间复杂度: O(n log n) - 排序的时间复杂度
     * 空间复杂度: O(1) - 常数空间
     * 是否最优解: 是
     * 
     * 算法思路详解：
     * 1. 贪心策略核心：按结束时间排序，优先选择结束早的活动，
     *    这样可以为后续活动留出更多时间
     * 2. 经典应用：活动选择问题的标准解法
     * 3. 选择机制：遍历排序后的活动，选择不重叠的活动
     * 
     * 工程化最佳实践：
     * 1. 排序优化：使用高效的比较器
     * 2. 边界处理：妥善处理空数组情况
     * 3. 重叠判断：准确判断区间重叠条件
     * 
     * 实际应用场景：
     * 1. 会议调度：会议室资源最优分配
     * 2. 任务安排：CPU任务调度优化
     * 3. 广告投放：广告位时间分配
     */
    public static int maxNonOverlappingIntervals(int[][] intervals) {
        if (intervals == null || intervals.length == 0) {
            return 0;
        }
        
        // 按结束时间排序
        Arrays.sort(intervals, (a, b) -> a[1] - b[1]);
        
        int count = 1;
        int end = intervals[0][1];
        
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] >= end) {
                count++;
                end = intervals[i][1];
            }
        }
        
        return count;
    }

    /**
     * 题目10: LeetCode 1005. K 次取反后最大化的数组和 (Maximize Sum After K Negations)
     * 题目描述: 给你一个整数数组 nums 和一个整数 k ，可以进行k次取反操作，求最大数组和
     * 链接: https://leetcode.cn/problems/maximize-sum-of-array-after-k-negations/
     * 
     * 算法标签: 贪心算法(Greedy Algorithm)、数组操作(Array Operations)、符号优化(Sign Optimization)
     * 时间复杂度: O(n log n) - 排序的时间复杂度
     * 空间复杂度: O(1) - 常数空间
     * 是否最优解: 是
     * 
     * 算法思路详解：
     * 1. 贪心策略核心：优先将负数变为正数以最大化数组和，
     *    如果还有剩余次数且为奇数，对最小的正数进行取反
     * 2. 分阶段处理：先处理负数，再处理剩余次数
     * 3. 符号优化：通过取反操作最大化数组元素值
     * 
     * 工程化最佳实践：
     * 1. 阶段处理：分阶段执行不同的取反策略
     * 2. 边界优化：处理剩余次数为奇数的情况
     * 3. 总和计算：准确计算数组元素总和
     * 
     * 实际应用场景：
     * 1. 数值优化：数组元素符号调整
     * 2. 金融计算：收益最大化调整
     * 3. 数据处理：数值序列优化
     */
    public static int largestSumAfterKNegations(int[] nums, int k) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        // 排序，让负数在前面
        Arrays.sort(nums);
        
        // 优先将负数变为正数
        for (int i = 0; i < nums.length && k > 0; i++) {
            if (nums[i] < 0) {
                nums[i] = -nums[i];
                k--;
            } else {
                break;
            }
        }
        
        // 如果k还有剩余且为奇数，对最小的数取反
        if (k > 0 && k % 2 == 1) {
            Arrays.sort(nums); // 重新排序找到最小的数
            nums[0] = -nums[0];
        }
        
        // 计算总和
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        
        return sum;
    }

    // 测试函数
    public static void main(String[] args) {
        // 测试加油站问题
        int[] gas = {1, 2, 3, 4, 5};
        int[] cost = {3, 4, 5, 1, 2};
        System.out.println("加油站测试: " + canCompleteCircuit(gas, cost)); // 期望: 3
        
        // 测试移掉K位数字
        System.out.println("移掉K位数字测试: " + removeKdigits("1432219", 3)); // 期望: "1219"
        
        // 测试数组拆分
        int[] nums = {1, 4, 3, 2};
        System.out.println("数组拆分测试: " + arrayPairSum(nums)); // 期望: 4
        
        // 测试非递减数列
        int[] nums2 = {4, 2, 3};
        System.out.println("非递减数列测试: " + checkPossibility(nums2)); // 期望: true
        
        // 测试疯狂的采药（近似解测试）
        int[][] herbs = {{60, 10}, {100, 20}, {120, 30}};
        System.out.println("疯狂的采药测试: " + crazyHerbs(50, herbs)); // 期望: 240
    }
}