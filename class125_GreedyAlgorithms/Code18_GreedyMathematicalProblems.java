package class094;

import java.util.*;

/**
 * 贪心算法数学相关问题集合 (Greedy Algorithm Mathematical Problems Collection)
 * 包含与数学相关的贪心算法问题，如分数背包、最优分配等
 * 涵盖数论、组合数学、概率统计等数学领域的应用
 * 
 * 算法标签: 贪心算法(Greedy Algorithm)、数学应用(Mathematical Applications)、数值优化(Numerical Optimization)
 * 时间复杂度: 各题目不同，详见具体函数注释
 * 空间复杂度: 各题目不同，详见具体函数注释
 * 相关题目: 数字组合、能量管理、计算器操作、字符串构造等
 * 贪心算法专题 - 数学问题集合
 */
public class Code18_GreedyMathematicalProblems {

    /**
     * 题目1: LeetCode 179. 最大数 (Largest Number)
     * 题目描述: 给定一组非负整数，重新排列它们的顺序使之组成一个最大的整数
     * 链接: https://leetcode.cn/problems/largest-number/
     * 
     * 算法标签: 贪心算法(Greedy Algorithm)、自定义排序(Custom Sorting)、字符串处理(String Processing)
     * 时间复杂度: O(n log n) - 排序的时间复杂度
     * 空间复杂度: O(n) - 字符串数组存储
     * 是否最优解: 是
     * 
     * 算法思路详解：
     * 1. 贪心策略核心：自定义排序规则，比较两个数字拼接后的结果，
     *    对于数字a和b，比较a+b和b+a的大小来决定排列顺序
     * 2. 排序优化：通过自定义比较器实现最优排列
     * 3. 结果处理：拼接排序后的数字并处理前导零
     * 
     * 工程化最佳实践：
     * 1. 字符串转换：准确将数字转换为字符串
     * 2. 排序规则：使用正确的比较规则
     * 3. 边界处理：妥善处理全零数组情况
     * 
     * 实际应用场景：
     * 1. 数据展示：生成最大数字组合
     * 2. 密码学：数字序列优化
     * 3. 金融计算：最大收益数字组合
     */
    public static String largestNumber(int[] nums) {
        if (nums == null || nums.length == 0) {
            return "";
        }
        
        // 将数字转换为字符串数组
        String[] strNums = new String[nums.length];
        for (int i = 0; i < nums.length; i++) {
            strNums[i] = String.valueOf(nums[i]);
        }
        
        // 自定义排序：比较a+b和b+a的大小
        Arrays.sort(strNums, (a, b) -> {
            String order1 = a + b;
            String order2 = b + a;
            return order2.compareTo(order1); // 降序排列
        });
        
        // 处理前导零的情况
        if (strNums[0].equals("0")) {
            return "0";
        }
        
        // 拼接结果
        StringBuilder result = new StringBuilder();
        for (String num : strNums) {
            result.append(num);
        }
        
        return result.toString();
    }

    /**
     * 题目2: LeetCode 1665. 完成所有任务的最少初始能量 (Minimum Initial Energy to Finish Tasks)
     * 题目描述: 每个任务有实际消耗和最小初始能量要求，求完成所有任务的最小初始能量
     * 链接: https://leetcode.cn/problems/minimum-initial-energy-to-finish-tasks/
     * 
     * 算法标签: 贪心算法(Greedy Algorithm)、能量管理(Energy Management)、任务调度(Task Scheduling)
     * 时间复杂度: O(n log n) - 排序的时间复杂度
     * 空间复杂度: O(1) - 常数空间
     * 是否最优解: 是
     * 
     * 算法思路详解：
     * 1. 贪心策略核心：按(最小要求 - 实际消耗)降序排序，
     *    优先处理差值大的任务，这样可以最小化初始能量需求
     * 2. 能量维护：动态维护当前能量状态
     * 3. 补充策略：当能量不足时补充所需能量
     * 
     * 工程化最佳实践：
     * 1. 排序优化：使用高效的比较器
     * 2. 能量计算：准确计算能量补充量
     * 3. 边界处理：妥善处理空任务数组
     * 
     * 实际应用场景：
     * 1. 资源管理：有限资源下的任务执行
     * 2. 电池优化：设备电池使用优化
     * 3. 项目管理：任务资源分配优化
     */
    public static int minimumEffort(int[][] tasks) {
        if (tasks == null || tasks.length == 0) {
            return 0;
        }
        
        // 按(最小要求 - 实际消耗)降序排序
        Arrays.sort(tasks, (a, b) -> {
            int diffA = a[1] - a[0]; // 最小要求 - 实际消耗
            int diffB = b[1] - b[0];
            return diffB - diffA; // 降序排列
        });
        
        int result = 0;
        int currentEnergy = 0;
        
        for (int[] task : tasks) {
            int actual = task[0];    // 实际消耗
            int minimum = task[1];   // 最小要求
            
            if (currentEnergy < minimum) {
                // 需要补充能量
                int need = minimum - currentEnergy;
                result += need;
                currentEnergy += need;
            }
            
            // 完成任务，消耗能量
            currentEnergy -= actual;
        }
        
        return result;
    }

    /**
     * 题目3: LeetCode 991. 坏了的计算器 (Broken Calculator)
     * 题目描述: 计算器只能进行乘2和减1操作，求从X到Y的最小操作次数
     * 链接: https://leetcode.cn/problems/broken-calculator/
     * 
     * 算法标签: 贪心算法(Greedy Algorithm)、反向思维(Reverse Thinking)、数学运算(Mathematical Operations)
     * 时间复杂度: O(log Y) - 反向操作次数
     * 空间复杂度: O(1) - 常数空间
     * 是否最优解: 是
     * 
     * 算法思路详解：
     * 1. 贪心策略核心：反向思考，从Y到X，只能进行除2和加1操作，
     *    当Y > X时优先除2，当Y < X时只能加1
     * 2. 操作优化：偶数直接除2，奇数先加1变偶数
     * 3. 终止条件：当Y ≤ X时结束反向操作
     * 
     * 工程化最佳实践：
     * 1. 反向思维：将正向复杂问题转化为反向简单问题
     * 2. 操作判断：准确判断除法和加法条件
     * 3. 边界处理：妥善处理X ≥ Y的情况
     * 
     * 实际应用场景：
     * 1. 算法优化：复杂问题的反向求解
     * 2. 数学计算：特殊运算规则下的计算优化
     * 3. 游戏开发：受限操作下的最优策略
     */
    public static int brokenCalc(int X, int Y) {
        if (X >= Y) {
            return X - Y; // 只能减1操作
        }
        
        int operations = 0;
        while (Y > X) {
            operations++;
            if (Y % 2 == 0) {
                Y /= 2;
            } else {
                Y++;
            }
        }
        
        return operations + (X - Y);
    }

    /**
     * 题目4: LeetCode 910. 最小差值 II (Smallest Range II)
     * 题目描述: 对数组中的每个元素可以加上或减去K，求调整后数组的最大最小差值
     * 链接: https://leetcode.cn/problems/smallest-range-ii/
     * 
     * 算法标签: 贪心算法(Greedy Algorithm)、数组调整(Array Adjustment)、分割优化(Partition Optimization)
     * 时间复杂度: O(n log n) - 排序的时间复杂度
     * 空间复杂度: O(1) - 常数空间
     * 是否最优解: 是
     * 
     * 算法思路详解：
     * 1. 贪心策略核心：排序后寻找最佳分割点，
     *    将数组分为两部分：前一部分加K，后一部分减K
     * 2. 分割优化：尝试每个可能的分割点
     * 3. 差值计算：计算调整后的最大最小差值
     * 
     * 工程化最佳实践：
     * 1. 排序预处理：通过排序简化问题
     * 2. 分割枚举：准确枚举所有分割点
     * 3. 边界处理：妥善处理单元素数组
     * 
     * 实际应用场景：
     * 1. 数据调整：数组元素范围优化
     * 2. 信号处理：信号幅度调整
     * 3. 金融分析：价格波动范围控制
     */
    public static int smallestRangeII(int[] nums, int k) {
        if (nums == null || nums.length <= 1) {
            return 0;
        }
        
        Arrays.sort(nums);
        int n = nums.length;
        int result = nums[n - 1] - nums[0]; // 初始差值
        
        // 尝试每个可能的分割点
        for (int i = 0; i < n - 1; i++) {
            int high = Math.max(nums[n - 1] - k, nums[i] + k);
            int low = Math.min(nums[0] + k, nums[i + 1] - k);
            result = Math.min(result, high - low);
        }
        
        return result;
    }

    /**
     * 题目5: LeetCode 1526. 形成目标数组的子数组最少增加次数 (Minimum Number of Increments on Subarrays to Form a Target Array)
     * 题目描述: 通过增加连续子数组的值来形成目标数组，求最少操作次数
     * 链接: https://leetcode.cn/problems/minimum-number-of-increments-on-subarrays-to-form-a-target-array/
     * 
     * 算法标签: 贪心算法(Greedy Algorithm)、差值分析(Difference Analysis)、子数组操作(Subarray Operations)
     * 时间复杂度: O(n) - 一次遍历
     * 空间复杂度: O(1) - 常数空间
     * 是否最优解: 是
     * 
     * 算法思路详解：
     * 1. 贪心策略核心：相邻元素差值法，
     *    操作次数等于第一个元素的值加上所有正差值之和
     * 2. 差值计算：准确计算相邻元素差值
     * 3. 操作统计：统计所需最少操作次数
     * 
     * 工程化最佳实践：
     * 1. 差值处理：准确识别正差值
     * 2. 累计计算：正确累计操作次数
     * 3. 边界处理：妥善处理空数组
     * 
     * 实际应用场景：
     * 1. 数组构造：最少操作构造目标数组
     * 2. 资源分配：资源增量分配优化
     * 3. 任务规划：任务增量执行优化
     */
    public static int minNumberOperations(int[] target) {
        if (target == null || target.length == 0) {
            return 0;
        }
        
        int operations = target[0]; // 第一个元素需要的操作次数
        
        for (int i = 1; i < target.length; i++) {
            if (target[i] > target[i - 1]) {
                operations += target[i] - target[i - 1];
            }
        }
        
        return operations;
    }

    /**
     * 题目6: LeetCode 1247. 交换字符使得字符串相同 (Minimum Swaps to Make Strings Equal)
     * 题目描述: 通过交换两个字符串对应位置的字符，使得两个字符串相同
     * 链接: https://leetcode.cn/problems/minimum-swaps-to-make-strings-equal/
     * 
     * 算法标签: 贪心算法(Greedy Algorithm)、字符串匹配(String Matching)、交换优化(Swap Optimization)
     * 时间复杂度: O(n) - 一次遍历
     * 空间复杂度: O(1) - 常数空间
     * 是否最优解: 是
     * 
     * 算法思路详解：
     * 1. 贪心策略核心：统计不匹配的位置类型，
     *    有两种不匹配类型：s1[i]='x', s2[i]='y' 和 s1[i]='y', s2[i]='x'
     * 2. 交换策略：相同类型的不匹配可以一次交换解决两个，不同类型需要两次交换
     * 3. 次数计算：准确计算最少交换次数
     * 
     * 工程化最佳实践：
     * 1. 类型统计：准确统计不匹配类型
     * 2. 交换计算：正确计算交换次数
     * 3. 可行性判断：判断是否可以完成交换
     * 
     * 实际应用场景：
     * 1. 字符串处理：字符串匹配优化
     * 2. 数据同步：数据一致性维护
     * 3. 网络通信：数据包顺序调整
     */
    public static int minimumSwap(String s1, String s2) {
        if (s1 == null || s2 == null || s1.length() != s2.length()) {
            return -1;
        }
        
        int xy = 0, yx = 0; // 统计不匹配类型
        
        for (int i = 0; i < s1.length(); i++) {
            char c1 = s1.charAt(i);
            char c2 = s2.charAt(i);
            
            if (c1 == 'x' && c2 == 'y') {
                xy++;
            } else if (c1 == 'y' && c2 == 'x') {
                yx++;
            }
        }
        
        // 如果总的不匹配数是奇数，无法完成
        if ((xy + yx) % 2 != 0) {
            return -1;
        }
        
        // 计算最少交换次数
        return xy / 2 + yx / 2 + (xy % 2) * 2;
    }

    /**
     * 题目7: LeetCode 1405. 最长快乐字符串 (Longest Happy String)
     * 题目描述: 使用给定数量的a、b、c字符构造最长字符串，不能有三个连续相同字符
     * 链接: https://leetcode.cn/problems/longest-happy-string/
     * 
     * 算法标签: 贪心算法(Greedy Algorithm)、字符串构造(String Construction)、约束满足(Constraint Satisfaction)
     * 时间复杂度: O(n) - n是构造字符串长度
     * 空间复杂度: O(1) - 常数空间
     * 是否最优解: 是
     * 
     * 算法思路详解：
     * 1. 贪心策略核心：优先使用剩余数量最多的字符，
     *    如果连续使用了两个相同字符，下次使用次多的字符
     * 2. 字符选择：使用最大堆维护字符优先级
     * 3. 约束满足：确保不出现三个连续相同字符
     * 
     * 工程化最佳实践：
     * 1. 优先级维护：使用堆维护字符数量优先级
     * 2. 约束检查：准确检查连续字符约束
     * 3. 边界处理：妥善处理字符不足情况
     * 
     * 实际应用场景：
     * 1. 密码生成：满足约束的密码构造
     * 2. 字符串处理：约束字符串生成
     * 3. 游戏开发：规则字符串构造
     */
    public static String longestDiverseString(int a, int b, int c) {
        // 使用最大堆存储字符和剩余数量
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>((x, y) -> y[1] - x[1]);
        
        if (a > 0) maxHeap.offer(new int[]{'a', a});
        if (b > 0) maxHeap.offer(new int[]{'b', b});
        if (c > 0) maxHeap.offer(new int[]{'c', c});
        
        StringBuilder result = new StringBuilder();
        
        while (!maxHeap.isEmpty()) {
            int[] first = maxHeap.poll();
            char char1 = (char) first[0];
            int count1 = first[1];
            
            // 检查是否已经连续使用了两个相同字符
            int len = result.length();
            if (len >= 2 && result.charAt(len - 1) == char1 && result.charAt(len - 2) == char1) {
                // 需要换一个字符
                if (maxHeap.isEmpty()) {
                    break; // 没有其他字符可用
                }
                
                int[] second = maxHeap.poll();
                char char2 = (char) second[0];
                int count2 = second[1];
                
                result.append(char2);
                count2--;
                
                if (count2 > 0) {
                    maxHeap.offer(new int[]{char2, count2});
                }
                maxHeap.offer(first); // 把第一个字符放回去
            } else {
                // 可以使用当前字符
                result.append(char1);
                count1--;
                
                if (count1 > 0) {
                    maxHeap.offer(new int[]{char1, count1});
                }
            }
        }
        
        return result.toString();
    }

    /**
     * 题目8: LeetCode 1561. 你可以获得的最大硬币数目 (Maximum Number of Coins You Can Get)
     * 题目描述: 三人分硬币，你总是拿第二多的那堆，求最大硬币数
     * 链接: https://leetcode.cn/problems/maximum-number-of-coins-you-can-get/
     * 
     * 算法标签: 贪心算法(Greedy Algorithm)、资源分配(Resource Allocation)、排序优化(Sorting Optimization)
     * 时间复杂度: O(n log n) - 排序的时间复杂度
     * 空间复杂度: O(1) - 常数空间
     * 是否最优解: 是
     * 
     * 算法思路详解：
     * 1. 贪心策略核心：排序后每次取第二大的堆，
     *    让另外两人拿最大和最小的堆，这样可以最大化自己的收益
     * 2. 分配策略：按排序后的顺序进行最优分配
     * 3. 收益计算：累计所获得的硬币数
     * 
     * 工程化最佳实践：
     * 1. 排序优化：使用高效的排序算法
     * 2. 分配计算：准确计算分配策略
     * 3. 边界处理：妥善处理空数组
     * 
     * 实际应用场景：
     * 1. 资源分配：三人资源最优分配
     * 2. 游戏策略：公平分配下的最优策略
     * 3. 经济学：资源分配博弈
     */
    public static int maxCoins(int[] piles) {
        if (piles == null || piles.length == 0) {
            return 0;
        }
        
        Arrays.sort(piles);
        int result = 0;
        int n = piles.length;
        
        // 每次取第二大的堆（从倒数第二个开始，每隔一个取一个）
        for (int i = n - 2; i >= n / 3; i -= 2) {
            result += piles[i];
        }
        
        return result;
    }

    /**
     * 题目9: LeetCode 1689. 十-二进制数的最少数目 (Partitioning Into Minimum Number of Deci-Binary Numbers)
     * 题目描述: 用最少的十-二进制数（只包含0和1）相加得到给定数字
     * 链接: https://leetcode.cn/problems/partitioning-into-minimum-number-of-deci-binary-numbers/
     * 
     * 算法标签: 贪心算法(Greedy Algorithm)、数字分解(Number Decomposition)、位运算(Bit Operations)
     * 时间复杂度: O(n) - 一次遍历
     * 空间复杂度: O(1) - 常数空间
     * 是否最优解: 是
     * 
     * 算法思路详解：
     * 1. 贪心策略核心：最少数量等于数字中最大的数字，
     *    因为每个位置至少需要对应数量的1
     * 2. 分解策略：按位分解数字
     * 3. 数量计算：找到最大数字位
     * 
     * 工程化最佳实践：
     * 1. 数字处理：准确处理字符串数字
     * 2. 最大值查找：高效找到最大数字位
     * 3. 边界处理：妥善处理空字符串
     * 
     * 实际应用场景：
     * 1. 数字处理：数字分解优化
     * 2. 编码理论：二进制编码优化
     * 3. 密码学：数字分解应用
     */
    public static int minPartitions(String n) {
        if (n == null || n.isEmpty()) {
            return 0;
        }
        
        int maxDigit = 0;
        for (char c : n.toCharArray()) {
            maxDigit = Math.max(maxDigit, c - '0');
        }
        
        return maxDigit;
    }

    /**
     * 题目10: LeetCode 1710. 卡车上的最大单元数 (Maximum Units on a Truck)
     * 题目描述: 卡车有容量限制，选择箱子使得总单元数最大
     * 链接: https://leetcode.cn/problems/maximum-units-on-a-truck/
     * 
     * 算法标签: 贪心算法(Greedy Algorithm)、背包问题变种(Knapsack Variant)、价值优化(Value Optimization)
     * 时间复杂度: O(n log n) - 排序的时间复杂度
     * 空间复杂度: O(1) - 常数空间
     * 是否最优解: 是
     * 
     * 算法思路详解：
     * 1. 贪心策略核心：按单位容量价值（每个箱子的单元数）降序排序，
     *    优先选择单位价值高的箱子以最大化总单元数
     * 2. 容量管理：动态维护剩余容量
     * 3. 选择策略：按排序后的顺序选择箱子
     * 
     * 工程化最佳实践：
     * 1. 排序优化：使用高效的比较器
     * 2. 容量控制：准确管理剩余容量
     * 3. 边界处理：妥善处理零容量情况
     * 
     * 实际应用场景：
     * 1. 物流运输：货物装载优化
     * 2. 资源分配：有限资源下的价值最大化
     * 3. 投资组合：投资标的优选
     */
    public static int maximumUnits(int[][] boxTypes, int truckSize) {
        if (boxTypes == null || boxTypes.length == 0 || truckSize <= 0) {
            return 0;
        }
        
        // 按每个箱子的单元数降序排序
        Arrays.sort(boxTypes, (a, b) -> b[1] - a[1]);
        
        int totalUnits = 0;
        int remainingSize = truckSize;
        
        for (int[] box : boxTypes) {
            int numberOfBoxes = box[0];
            int unitsPerBox = box[1];
            
            int boxesToTake = Math.min(numberOfBoxes, remainingSize);
            totalUnits += boxesToTake * unitsPerBox;
            remainingSize -= boxesToTake;
            
            if (remainingSize == 0) {
                break;
            }
        }
        
        return totalUnits;
    }

    // 测试函数
    public static void main(String[] args) {
        // 测试最大数
        int[] nums1 = {3, 30, 34, 5, 9};
        System.out.println("最大数测试: " + largestNumber(nums1)); // 期望: "9534330"
        
        // 测试坏了的计算器
        System.out.println("坏了的计算器测试: " + brokenCalc(2, 3)); // 期望: 2
        
        // 测试最小差值II
        int[] nums2 = {1, 3, 6};
        System.out.println("最小差值II测试: " + smallestRangeII(nums2, 3)); // 期望: 3
        
        // 测试最长快乐字符串
        System.out.println("最长快乐字符串测试: " + longestDiverseString(1, 1, 7)); // 期望类似: "ccaccbcc"
        
        // 测试最大硬币数
        int[] piles = {2, 4, 1, 2, 7, 8};
        System.out.println("最大硬币数测试: " + maxCoins(piles)); // 期望: 9
    }
}