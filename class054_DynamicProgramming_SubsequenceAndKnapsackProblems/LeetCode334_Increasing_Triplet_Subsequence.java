package class086;

// LeetCode 334. 递增的三元子序列
// 给你一个整数数组 nums ，判断这个数组中是否存在长度为 3 的递增子序列。
// 如果存在这样的三元组下标 (i, j, k) 且满足 i < j < k ，使得 nums[i] < nums[j] < nums[k] ，返回 true ；否则，返回 false 。
// 测试链接 : https://leetcode.cn/problems/increasing-triplet-subsequence/

/**
 * 算法详解：递增的三元子序列（LeetCode 334）
 * 
 * 问题描述：
 * 给定一个整数数组 nums，判断是否存在长度为3的递增子序列。
 * 即是否存在下标 i < j < k，使得 nums[i] < nums[j] < nums[k]。
 * 
 * 算法思路：
 * 1. 使用贪心思想，维护两个变量：first和second
 * 2. first表示当前找到的最小值，second表示比first大的最小值
 * 3. 遍历数组，如果找到比second大的数，说明存在递增三元组
 * 
 * 时间复杂度分析：
 * 1. 遍历数组一次：O(n)
 * 2. 总体时间复杂度：O(n)
 * 
 * 空间复杂度分析：
 * 1. 只使用常数个变量：O(1)
 * 2. 总体空间复杂度：O(1)
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入数组是否为空
 * 2. 边界处理：处理数组长度小于3的情况
 * 3. 性能优化：提前终止遍历
 * 4. 代码简洁性：使用清晰的变量命名
 * 
 * 极端场景验证：
 * 1. 输入数组为空的情况
 * 2. 数组长度小于3的情况
 * 3. 严格递增数组的情况
 * 4. 严格递减数组的情况
 * 5. 包含重复元素的数组
 * 6. 大规模数组的性能测试
 */
public class LeetCode334_Increasing_Triplet_Subsequence {
    
    /**
     * 贪心算法解法
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     * 
     * 算法思想：
     * 维护两个变量first和second，分别表示当前找到的最小值和次小值。
     * 遍历数组时，如果当前数比second大，说明存在递增三元组。
     * 如果当前数比first小，更新first；如果比first大但比second小，更新second。
     * 
     * 这种方法的巧妙之处在于：
     * 1. 不需要记录完整的三元组，只需要判断是否存在
     * 2. 通过维护first和second，可以处理各种情况
     * 3. 算法具有最优子结构性质
     */
    public static boolean increasingTriplet(int[] nums) {
        // 异常处理：检查输入数组是否为空
        if (nums == null || nums.length < 3) {
            return false;
        }
        
        int n = nums.length;
        
        // 特殊情况：数组长度小于3，直接返回false
        if (n < 3) {
            return false;
        }
        
        // 初始化first和second
        // first表示当前找到的最小值
        // second表示比first大的最小值
        int first = Integer.MAX_VALUE;
        int second = Integer.MAX_VALUE;
        
        // 遍历数组
        for (int i = 0; i < n; i++) {
            int num = nums[i];
            
            if (num <= first) {
                // 如果当前数小于等于first，更新first
                // 注意：这里使用<=而不是<，是为了处理重复元素
                first = num;
            } else if (num <= second) {
                // 如果当前数大于first但小于等于second，更新second
                second = num;
            } else {
                // 如果当前数大于second，说明找到递增三元组
                return true;
            }
        }
        
        // 遍历完成仍未找到，返回false
        return false;
    }
    
    /**
     * 动态规划解法（通用解法，可扩展到k元组）
     * 时间复杂度：O(n²)
     * 空间复杂度：O(n)
     * 
     * 算法思想：
     * 使用dp数组，dp[i]表示以nums[i]结尾的最长递增子序列长度。
     * 如果存在dp[i] >= 3，则说明存在递增三元组。
     * 
     * 优点：可以扩展到任意长度的递增子序列判断
     * 缺点：时间复杂度较高，不适合大规模数据
     */
    public static boolean increasingTripletDP(int[] nums) {
        if (nums == null || nums.length < 3) {
            return false;
        }
        
        int n = nums.length;
        
        // dp[i]表示以nums[i]结尾的最长递增子序列长度
        int[] dp = new int[n];
        
        // 初始化：每个元素自身构成长度为1的子序列
        for (int i = 0; i < n; i++) {
            dp[i] = 1;
        }
        
        // 填充dp数组
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                    
                    // 如果找到长度>=3的递增子序列，直接返回true
                    if (dp[i] >= 3) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    /**
     * 二分查找解法（LIS思想）
     * 时间复杂度：O(n log k)，其中k是递增子序列的最大长度
     * 空间复杂度：O(k)
     * 
     * 算法思想：
     * 维护一个tails数组，tails[i]表示长度为i+1的递增子序列的最小末尾元素。
     * 如果tails数组的长度达到3，说明存在递增三元组。
     */
    public static boolean increasingTripletBinarySearch(int[] nums) {
        if (nums == null || nums.length < 3) {
            return false;
        }
        
        int n = nums.length;
        
        // tails数组：tails[i]表示长度为i+1的递增子序列的最小末尾元素
        int[] tails = new int[3]; // 我们只关心长度是否达到3
        int len = 0; // 当前最长递增子序列的长度
        
        for (int num : nums) {
            // 使用二分查找找到num在tails数组中的位置
            int left = 0, right = len;
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (tails[mid] < num) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }
            
            // 更新tails数组
            tails[left] = num;
            
            // 如果left等于len，说明需要扩展tails数组
            if (left == len) {
                len++;
                
                // 如果长度达到3，返回true
                if (len >= 3) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * 单元测试方法
     * 验证算法的正确性和各种边界情况
     */
    public static void main(String[] args) {
        System.out.println("=== LeetCode 334 递增的三元子序列测试 ===\n");
        
        // 测试用例1：存在递增三元组
        int[] nums1 = {1, 2, 3, 4, 5};
        testCase("测试用例1 - 严格递增", nums1, true);
        
        // 测试用例2：不存在递增三元组
        int[] nums2 = {5, 4, 3, 2, 1};
        testCase("测试用例2 - 严格递减", nums2, false);
        
        // 测试用例3：存在递增三元组（非连续）
        int[] nums3 = {2, 1, 5, 0, 4, 6};
        testCase("测试用例3 - 非连续递增", nums3, true);
        
        // 测试用例4：边界情况（长度小于3）
        int[] nums4 = {1, 2};
        testCase("测试用例4 - 长度小于3", nums4, false);
        
        // 测试用例5：空数组
        int[] nums5 = {};
        testCase("测试用例5 - 空数组", nums5, false);
        
        // 测试用例6：包含重复元素
        int[] nums6 = {1, 1, 1, 1, 1};
        testCase("测试用例6 - 全部重复", nums6, false);
        
        // 测试用例7：LeetCode官方示例
        int[] nums7 = {1, 2, 3, 4, 5};
        testCase("测试用例7 - LeetCode示例", nums7, true);
        
        // 测试用例8：复杂情况
        int[] nums8 = {5, 1, 6, 2, 7, 3, 8};
        testCase("测试用例8 - 复杂情况", nums8, true);
        
        // 测试用例9：刚好存在三元组
        int[] nums9 = {1, 2, 0, 3};
        testCase("测试用例9 - 刚好存在", nums9, true);
        
        // 性能测试：大规模数据
        performanceTest();
    }
    
    /**
     * 测试用例辅助方法
     */
    private static void testCase(String description, int[] nums, boolean expected) {
        System.out.println(description);
        System.out.println("输入数组: " + java.util.Arrays.toString(nums));
        
        boolean result1 = increasingTriplet(nums);
        boolean result2 = increasingTripletDP(nums);
        boolean result3 = increasingTripletBinarySearch(nums);
        
        System.out.println("贪心算法: " + result1 + " " + (result1 == expected ? "✓" : "✗"));
        System.out.println("动态规划: " + result2 + " " + (result2 == expected ? "✓" : "✗"));
        System.out.println("二分查找: " + result3 + " " + (result3 == expected ? "✓" : "✗"));
        System.out.println("期望结果: " + expected);
        
        // 验证所有方法结果一致
        if (result1 == result2 && result2 == result3 && result1 == expected) {
            System.out.println("测试通过 ✓\n");
        } else {
            System.out.println("测试失败 ✗\n");
        }
    }
    
    /**
     * 性能测试方法
     * 测试算法在大规模数据下的表现
     */
    private static void performanceTest() {
        System.out.println("=== 性能测试 ===");
        
        // 生成测试数据：大规模随机数组
        int n = 10000;
        int[] nums = new int[n];
        java.util.Random random = new java.util.Random();
        
        // 生成随机数组（大概率存在递增三元组）
        for (int i = 0; i < n; i++) {
            nums[i] = random.nextInt(1000000);
        }
        
        // 测试贪心算法
        long startTime = System.currentTimeMillis();
        boolean result1 = increasingTriplet(nums);
        long endTime = System.currentTimeMillis();
        System.out.println("贪心算法:");
        System.out.println("  结果: " + result1);
        System.out.println("  耗时: " + (endTime - startTime) + "ms");
        
        // 测试二分查找算法
        startTime = System.currentTimeMillis();
        boolean result3 = increasingTripletBinarySearch(nums);
        endTime = System.currentTimeMillis();
        System.out.println("二分查找:");
        System.out.println("  结果: " + result3);
        System.out.println("  耗时: " + (endTime - startTime) + "ms");
        
        // 注意：动态规划算法在大规模数据下性能较差，这里不测试
        System.out.println("动态规划算法在大规模数据下性能较差，跳过测试");
        
        // 验证结果一致性
        if (result1 == result3) {
            System.out.println("结果一致性验证: 通过 ✓");
        } else {
            System.out.println("结果一致性验证: 失败 ✗");
        }
    }
    
    /**
     * 复杂度分析详细计算：
     * 
     * 贪心算法：
     * - 时间：单次遍历数组，每次操作O(1) → O(n)
     * - 空间：使用常数个变量 → O(1)
     * - 最优解：是，因为必须遍历整个数组才能确定结果
     * 
     * 动态规划：
     * - 时间：外层循环n次，内层循环n次 → O(n²)
     * - 空间：dp数组大小n → O(n)
     * - 最优解：否，时间复杂度较高
     * 
     * 二分查找：
     * - 时间：遍历数组n次，每次二分查找O(log k) → O(n log k)，其中k≤3 → O(n)
     * - 空间：tails数组大小3 → O(1)
     * - 最优解：是，但贪心算法更简洁
     * 
     * 工程选择依据：
     * 1. 对于只需要判断是否存在三元组的情况，选择贪心算法
     * 2. 如果需要找到具体的三元组或判断更长的子序列，选择动态规划
     * 3. 二分查找方法在理论上有优势，但实际中贪心算法更实用
     * 
     * 算法调试技巧：
     * 1. 打印first和second的实时变化，观察算法执行过程
     * 2. 使用小规模测试用例验证边界情况
     * 3. 对于复杂情况，可以手动计算预期结果进行对比
     */
}