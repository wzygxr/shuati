package class092;

/**
 * LeetCode 42. 接雨水
 * 题目链接：https://leetcode.cn/problems/trapping-rain-water/
 * 难度：困难
 * 
 * 问题描述：
 * 给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子，下雨之后能接多少雨水。
 * 
 * 示例：
 * 输入：height = [0,1,0,2,1,0,1,3,2,1,2,1]
 * 输出：6
 * 解释：上面是由数组 [0,1,0,2,1,0,1,3,2,1,2,1] 表示的高度图，在这种情况下，可以接 6 个单位的雨水（蓝色部分表示雨水）。
 * 
 * 解题思路：
 * 双指针贪心算法（最优解）
 * 1. 使用左右指针分别从两端向中间移动
 * 2. 维护左右两边的最大高度
 * 3. 每次移动高度较小的指针，因为水量由较矮的一边决定
 * 4. 计算当前位置能接的雨水量：min(leftMax, rightMax) - currentHeight
 * 
 * 时间复杂度：O(n) - 只需要遍历数组一次
 * 空间复杂度：O(1) - 只使用了常数级别的额外空间
 * 
 * 最优性证明：
 * 贪心策略的正确性：每次移动高度较小的指针，因为当前位置能接的雨水量由左右两边最大高度的较小值决定。
 * 移动较矮的一边，我们能够确保当前位置的雨水量计算是正确的。
 * 
 * 工程化考量：
 * 1. 边界条件处理：数组长度小于3的情况
 * 2. 异常处理：空数组、负数高度
 * 3. 性能优化：避免重复计算，使用简洁的变量命名
 */
public class Code21_TrappingRainWater {
    
    /**
     * 计算能接的雨水量 - 双指针最优解法
     * @param height 高度数组
     * @return 能接的雨水量
     */
    public static int trap(int[] height) {
        // 边界条件处理
        if (height == null || height.length < 3) {
            return 0;
        }
        
        int left = 0; // 左指针
        int right = height.length - 1; // 右指针
        int leftMax = 0; // 左边最大高度
        int rightMax = 0; // 右边最大高度
        int water = 0; // 总雨水量
        
        // 双指针向中间移动
        while (left < right) {
            // 更新左右最大高度
            leftMax = Math.max(leftMax, height[left]);
            rightMax = Math.max(rightMax, height[right]);
            
            // 移动高度较小的指针
            if (height[left] < height[right]) {
                // 当前位置能接的雨水量 = 左边最大高度 - 当前高度
                water += leftMax - height[left];
                left++;
            } else {
                // 当前位置能接的雨水量 = 右边最大高度 - 当前高度
                water += rightMax - height[right];
                right--;
            }
        }
        
        return water;
    }
    
    /**
     * 动态规划解法（对比用）
     * 时间复杂度：O(n)，空间复杂度：O(n)
     */
    public static int trapDP(int[] height) {
        if (height == null || height.length < 3) {
            return 0;
        }
        
        int n = height.length;
        int[] leftMax = new int[n]; // 每个位置左边的最大高度
        int[] rightMax = new int[n]; // 每个位置右边的最大高度
        
        // 计算左边最大高度
        leftMax[0] = height[0];
        for (int i = 1; i < n; i++) {
            leftMax[i] = Math.max(leftMax[i - 1], height[i]);
        }
        
        // 计算右边最大高度
        rightMax[n - 1] = height[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            rightMax[i] = Math.max(rightMax[i + 1], height[i]);
        }
        
        // 计算总雨水量
        int water = 0;
        for (int i = 0; i < n; i++) {
            water += Math.min(leftMax[i], rightMax[i]) - height[i];
        }
        
        return water;
    }
    
    /**
     * 单调栈解法（对比用）
     * 时间复杂度：O(n)，空间复杂度：O(n)
     */
    public static int trapStack(int[] height) {
        if (height == null || height.length < 3) {
            return 0;
        }
        
        int water = 0;
        java.util.Stack<Integer> stack = new java.util.Stack<>();
        
        for (int i = 0; i < height.length; i++) {
            // 当栈不为空且当前高度大于栈顶高度时
            while (!stack.isEmpty() && height[i] > height[stack.peek()]) {
                int bottom = stack.pop(); // 底部位置
                if (stack.isEmpty()) {
                    break;
                }
                int left = stack.peek(); // 左边界位置
                int distance = i - left - 1; // 宽度
                int boundedHeight = Math.min(height[left], height[i]) - height[bottom]; // 高度
                water += distance * boundedHeight;
            }
            stack.push(i);
        }
        
        return water;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：标准示例
        int[] height1 = {0,1,0,2,1,0,1,3,2,1,2,1};
        System.out.println("=== 测试用例1 ===");
        System.out.println("输入: " + java.util.Arrays.toString(height1));
        int result1 = trap(height1);
        int result1DP = trapDP(height1);
        int result1Stack = trapStack(height1);
        System.out.println("双指针结果: " + result1 + "，预期: 6");
        System.out.println("动态规划结果: " + result1DP + "，预期: 6");
        System.out.println("单调栈结果: " + result1Stack + "，预期: 6");
        System.out.println("结果一致性: " + (result1 == result1DP && result1 == result1Stack));
        System.out.println();
        
        // 测试用例2：递增序列
        int[] height2 = {1,2,3,4,5};
        System.out.println("=== 测试用例2 ===");
        System.out.println("输入: " + java.util.Arrays.toString(height2));
        int result2 = trap(height2);
        System.out.println("结果: " + result2 + "，预期: 0");
        System.out.println();
        
        // 测试用例3：递减序列
        int[] height3 = {5,4,3,2,1};
        System.out.println("=== 测试用例3 ===");
        System.out.println("输入: " + java.util.Arrays.toString(height3));
        int result3 = trap(height3);
        System.out.println("结果: " + result3 + "，预期: 0");
        System.out.println();
        
        // 测试用例4：V形序列
        int[] height4 = {5,1,5};
        System.out.println("=== 测试用例4 ===");
        System.out.println("输入: " + java.util.Arrays.toString(height4));
        int result4 = trap(height4);
        System.out.println("结果: " + result4 + "，预期: 4");
        System.out.println();
        
        // 测试用例5：边界情况
        int[] height5 = {0,2,0};
        System.out.println("=== 测试用例5 ===");
        System.out.println("输入: " + java.util.Arrays.toString(height5));
        int result5 = trap(height5);
        System.out.println("结果: " + result5 + "，预期: 0");
        System.out.println();
        
        // 性能测试
        System.out.println("=== 性能测试 ===");
        int[] largeHeight = new int[10000];
        for (int i = 0; i < largeHeight.length; i++) {
            largeHeight[i] = (int) (Math.random() * 1000);
        }
        
        long startTime = System.currentTimeMillis();
        int largeResult = trap(largeHeight);
        long endTime = System.currentTimeMillis();
        System.out.println("双指针解法 - 结果: " + largeResult + "，耗时: " + (endTime - startTime) + "ms");
        
        startTime = System.currentTimeMillis();
        int largeResultDP = trapDP(largeHeight);
        endTime = System.currentTimeMillis();
        System.out.println("动态规划解法 - 结果: " + largeResultDP + "，耗时: " + (endTime - startTime) + "ms");
        
        startTime = System.currentTimeMillis();
        int largeResultStack = trapStack(largeHeight);
        endTime = System.currentTimeMillis();
        System.out.println("单调栈解法 - 结果: " + largeResultStack + "，耗时: " + (endTime - startTime) + "ms");
        
        System.out.println("结果一致性: " + (largeResult == largeResultDP && largeResult == largeResultStack));
    }
}

/*
算法深度分析：

1. 双指针解法（最优解）：
   - 核心思想：利用双指针从两端向中间移动，每次移动高度较小的指针
   - 正确性证明：对于任意位置，其水量由左右最大高度的较小值决定
   - 时间复杂度：O(n)，空间复杂度：O(1)

2. 动态规划解法：
   - 核心思想：预处理每个位置左右的最大高度
   - 优点：思路直观，易于理解
   - 缺点：需要O(n)的额外空间

3. 单调栈解法：
   - 核心思想：使用单调递减栈计算凹槽的水量
   - 适用场景：适合计算每个凹槽的独立水量
   - 缺点：空间复杂度较高，实现相对复杂

工程化深度考量：

1. 算法选择依据：
   - 空间敏感场景：选择双指针解法（最优）
   - 代码可读性：选择动态规划解法
   - 特定问题需求：选择单调栈解法

2. 边界条件处理：
   - 数组长度小于3：无法形成凹槽，直接返回0
   - 空数组和null输入：返回0或抛出异常
   - 负数高度：题目保证非负，但工程中需要验证

3. 性能优化策略：
   - 避免重复计算：缓存中间结果
   - 减少函数调用：内联简单计算
   - 使用基本类型：避免自动装箱

4. 异常场景考虑：
   - 超大数组：算法时间复杂度为O(n)，可以处理
   - 极端数据：如全0数组、单调数组等
   - 内存限制：双指针解法空间最优

5. 调试与测试：
   - 单元测试：覆盖各种边界情况
   - 性能测试：验证大规模数据表现
   - 对比测试：验证不同解法结果一致性

6. 跨语言实现差异：
   - Java：使用Math.max和基本类型
   - C++：使用std::max和指针运算
   - Python：使用max函数和列表索引

7. 工程实践建议：
   - 生产环境优先使用双指针解法
   - 添加详细的日志和监控
   - 提供多种解法便于问题诊断

8. 算法扩展性：
   - 可以扩展支持三维接雨水问题
   - 可以扩展支持动态高度变化
   - 可以扩展支持不同形状的容器

9. 与机器学习联系：
   - 双指针思想可以用于特征选择中的窗口滑动
   - 动态规划思想在序列建模中有广泛应用
   - 该问题可以看作是在约束条件下的优化问题

10. 面试技巧：
    - 能够解释每种解法的时间/空间复杂度
    - 能够证明贪心策略的正确性
    - 能够处理各种边界情况和异常输入
*/