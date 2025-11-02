import java.util.*;
import java.util.stream.Collectors;

/**
 * LeetCode 45. 跳跃游戏 II (Jump Game II)
 * 
 * 题目描述:
 * 给定一个非负整数数组 nums，你最初位于数组的第一个位置。
 * 数组中的每个元素代表你在该位置可以跳跃的最大长度。
 * 你的目标是使用最少的跳跃次数到达数组的最后一个位置。
 * 假设你总是可以到达数组的最后一个位置。
 * 
 * 示例1:
 * 输入: nums = [2,3,1,1,4]
 * 输出: 2
 * 解释: 跳到最后一个位置的最小跳跃数是 2。
 *      从下标为 0 的位置跳到下标为 1 的位置，跳 1 步，然后跳 3 步到达数组的最后一个位置。
 * 
 * 示例2:
 * 输入: nums = [2,3,0,1,4]
 * 输出: 2
 * 
 * 提示:
 * 1 <= nums.length <= 10^4
 * 0 <= nums[i] <= 1000
 * 题目保证可以到达 nums[n-1]
 * 
 * 题目链接: https://leetcode.com/problems/jump-game-ii/
 * 
 * 解题思路:
 * 这道题可以使用贪心算法来解决。我们的目标是用最少的跳跃次数到达数组的最后一个位置。
 * 
 * 贪心策略：在每一步中，我们都选择能够到达的最远位置的下一步。
 * 
 * 具体来说，我们维护三个变量：
 * 1. currentEnd: 当前能够到达的最远边界
 * 2. currentFarthest: 在遍历过程中找到的从当前位置可以到达的最远位置
 * 3. jumps: 记录跳跃次数
 * 
 * 当我们遍历数组时，每当我们到达currentEnd，就意味着我们需要进行一次跳跃，此时将jumps加1，并将currentEnd更新为currentFarthest。
 * 
 * 时间复杂度: O(n)，其中n是数组的长度。我们只需要遍历数组一次。
 * 空间复杂度: O(1)，只使用了常数级别的额外空间。
 * 
 * 此外，我们还提供两种其他解法：
 * 1. 动态规划解法：时间复杂度O(n^2)，空间复杂度O(n)
 * 2. BFS解法：将问题视为图中的最短路径问题，时间复杂度O(n^2)，空间复杂度O(n)
 */

public class Code20_JumpGameII {
    
    /**
     * 解法一: 贪心算法（最优解）
     * 
     * @param nums 非负整数数组
     * @return 到达最后一个位置的最小跳跃次数
     */
    public static int jumpGreedy(int[] nums) {
        // 参数校验
        if (nums == null || nums.length <= 1) {
            return 0; // 如果数组为空或只有一个元素，不需要跳跃
        }
        
        int jumps = 0;         // 跳跃次数
        int currentEnd = 0;    // 当前能到达的最远边界
        int currentFarthest = 0; // 在遍历过程中找到的最远可达位置
        
        // 遍历数组，但不需要遍历到最后一个元素，因为一旦currentFarthest >= nums.length - 1，就已经可以到达终点
        for (int i = 0; i < nums.length - 1; i++) {
            // 更新从当前位置可达的最远位置
            currentFarthest = Math.max(currentFarthest, i + nums[i]);
            
            // 当到达当前边界时，必须进行一次跳跃
            if (i == currentEnd) {
                jumps++;
                currentEnd = currentFarthest; // 更新边界为新的最远可达位置
                
                // 如果已经可以到达或超过最后一个位置，可以提前结束
                if (currentEnd >= nums.length - 1) {
                    break;
                }
            }
        }
        
        return jumps;
    }
    
    /**
     * 解法二: 动态规划
     * 
     * @param nums 非负整数数组
     * @return 到达最后一个位置的最小跳跃次数
     */
    public static int jumpDynamicProgramming(int[] nums) {
        // 参数校验
        if (nums == null || nums.length <= 1) {
            return 0; // 如果数组为空或只有一个元素，不需要跳跃
        }
        
        int n = nums.length;
        // dp[i]表示到达位置i所需的最小跳跃次数
        int[] dp = new int[n];
        
        // 初始化所有位置为一个很大的值，表示暂时无法到达
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0; // 起始位置不需要跳跃
        
        // 计算每个位置的最小跳跃次数
        for (int i = 0; i < n; i++) {
            // 如果当前位置无法到达，跳过
            if (dp[i] == Integer.MAX_VALUE) {
                continue;
            }
            
            // 从当前位置可以跳跃到的所有位置
            int maxJump = Math.min(i + nums[i], n - 1); // 确保不超过数组边界
            for (int j = i + 1; j <= maxJump; j++) {
                // 更新到达位置j的最小跳跃次数
                if (dp[j] > dp[i] + 1) {
                    dp[j] = dp[i] + 1;
                    
                    // 如果已经到达最后一个位置，可以提前结束
                    if (j == n - 1) {
                        return dp[j];
                    }
                }
            }
        }
        
        return dp[n - 1];
    }
    
    /**
     * 解法三: BFS
     * 将问题视为图中的最短路径问题，每个位置是一个节点，从位置i可以到i+1, i+2, ..., i+nums[i]
     * 
     * @param nums 非负整数数组
     * @return 到达最后一个位置的最小跳跃次数
     */
    public static int jumpBFS(int[] nums) {
        // 参数校验
        if (nums == null || nums.length <= 1) {
            return 0; // 如果数组为空或只有一个元素，不需要跳跃
        }
        
        int n = nums.length;
        boolean[] visited = new boolean[n]; // 记录已经访问过的位置
        Queue<Integer> queue = new LinkedList<>(); // BFS队列
        
        // 初始化队列，起始位置是0，跳跃次数是0
        queue.offer(0);
        visited[0] = true;
        int jumps = 0;
        
        while (!queue.isEmpty()) {
            int size = queue.size(); // 当前层的节点数
            
            // 处理当前层的所有节点
            for (int i = 0; i < size; i++) {
                int current = queue.poll();
                
                // 如果到达最后一个位置，返回跳跃次数
                if (current == n - 1) {
                    return jumps;
                }
                
                // 将从当前位置可以到达的所有位置加入队列
                int maxJump = Math.min(current + nums[current], n - 1);
                for (int j = maxJump; j > current; j--) { // 反向遍历，优先考虑跳得更远的位置
                    if (!visited[j]) {
                        visited[j] = true;
                        queue.offer(j);
                        
                        // 如果下一层已经可以到达最后一个位置，可以提前结束当前层的处理
                        if (j == n - 1) {
                            return jumps + 1;
                        }
                    }
                }
            }
            
            jumps++; // 处理完一层，跳跃次数加1
        }
        
        return -1; // 根据题目描述，一定可以到达最后一个位置，所以不会执行到这里
    }
    
    /**
     * 打印数组
     */
    public static void printArray(int[] arr) {
        System.out.println(Arrays.stream(arr)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(", ", "[", "]")));
    }
    
    /**
     * 性能测试
     */
    public static void performanceTest(int[] nums) {
        // 测试贪心算法
        long startTime = System.currentTimeMillis();
        int result1 = jumpGreedy(nums);
        long endTime = System.currentTimeMillis();
        System.out.println("贪心算法结果: " + result1);
        System.out.println("贪心算法耗时: " + (endTime - startTime) + "ms");
        
        // 测试动态规划
        startTime = System.currentTimeMillis();
        int result2 = jumpDynamicProgramming(nums);
        endTime = System.currentTimeMillis();
        System.out.println("动态规划结果: " + result2);
        System.out.println("动态规划耗时: " + (endTime - startTime) + "ms");
        
        // 测试BFS
        startTime = System.currentTimeMillis();
        int result3 = jumpBFS(nums);
        endTime = System.currentTimeMillis();
        System.out.println("BFS结果: " + result3);
        System.out.println("BFS耗时: " + (endTime - startTime) + "ms");
    }
    
    /**
     * 生成测试用例
     */
    public static int[] generateTestCase(int n, boolean worstCase) {
        int[] nums = new int[n];
        
        if (worstCase) {
            // 最坏情况：每次只能跳1步
            Arrays.fill(nums, 1);
        } else {
            // 随机情况：生成1到5之间的随机数
            Random rand = new Random();
            for (int i = 0; i < n - 1; i++) {
                nums[i] = rand.nextInt(5) + 1; // 1到5之间的随机数
            }
            nums[n - 1] = 0; // 最后一个元素不影响
        }
        
        return nums;
    }
    
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {2, 3, 1, 1, 4};
        System.out.println("测试用例1:");
        System.out.print("nums = ");
        printArray(nums1);
        System.out.println("贪心算法结果: " + jumpGreedy(nums1)); // 预期输出: 2
        System.out.println("动态规划结果: " + jumpDynamicProgramming(nums1)); // 预期输出: 2
        System.out.println("BFS结果: " + jumpBFS(nums1)); // 预期输出: 2
        System.out.println();
        
        // 测试用例2
        int[] nums2 = {2, 3, 0, 1, 4};
        System.out.println("测试用例2:");
        System.out.print("nums = ");
        printArray(nums2);
        System.out.println("贪心算法结果: " + jumpGreedy(nums2)); // 预期输出: 2
        System.out.println("动态规划结果: " + jumpDynamicProgramming(nums2)); // 预期输出: 2
        System.out.println("BFS结果: " + jumpBFS(nums2)); // 预期输出: 2
        System.out.println();
        
        // 测试用例3 - 边界情况：只有一个元素
        int[] nums3 = {0};
        System.out.println("测试用例3（单元素数组）:");
        System.out.print("nums = ");
        printArray(nums3);
        System.out.println("贪心算法结果: " + jumpGreedy(nums3)); // 预期输出: 0
        System.out.println("动态规划结果: " + jumpDynamicProgramming(nums3)); // 预期输出: 0
        System.out.println("BFS结果: " + jumpBFS(nums3)); // 预期输出: 0
        System.out.println();
        
        // 测试用例4 - 边界情况：每次只能跳1步
        int[] nums4 = {1, 1, 1, 1, 1};
        System.out.println("测试用例4（每次只能跳1步）:");
        System.out.print("nums = ");
        printArray(nums4);
        System.out.println("贪心算法结果: " + jumpGreedy(nums4)); // 预期输出: 4
        System.out.println("动态规划结果: " + jumpDynamicProgramming(nums4)); // 预期输出: 4
        System.out.println("BFS结果: " + jumpBFS(nums4)); // 预期输出: 4
        System.out.println();
        
        // 测试用例5 - 边界情况：可以一次跳到终点
        int[] nums5 = {10, 1, 1, 1, 1};
        System.out.println("测试用例5（可以一次跳到终点）:");
        System.out.print("nums = ");
        printArray(nums5);
        System.out.println("贪心算法结果: " + jumpGreedy(nums5)); // 预期输出: 1
        System.out.println("动态规划结果: " + jumpDynamicProgramming(nums5)); // 预期输出: 1
        System.out.println("BFS结果: " + jumpBFS(nums5)); // 预期输出: 1
        System.out.println();
        
        // 性能测试 - 小规模数组
        System.out.println("小规模数组性能测试:");
        int[] smallArray = generateTestCase(100, false);
        performanceTest(smallArray);
        System.out.println();
        
        // 性能测试 - 大规模数组 - 只测试贪心算法，因为其他算法在大规模数组上会很慢
        System.out.println("大规模数组性能测试（只测试贪心算法）:");
        int[] largeArray = generateTestCase(10000, false);
        long startTime = System.currentTimeMillis();
        int result = jumpGreedy(largeArray);
        long endTime = System.currentTimeMillis();
        System.out.println("贪心算法结果: " + result);
        System.out.println("贪心算法耗时: " + (endTime - startTime) + "ms");
        
        // 最坏情况性能测试
        System.out.println("\n最坏情况性能测试:");
        int[] worstCaseArray = generateTestCase(1000, true); // 小规模的最坏情况，否则动态规划和BFS会超时
        performanceTest(worstCaseArray);
    }
}