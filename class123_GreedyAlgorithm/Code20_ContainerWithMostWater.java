package class092;

/**
 * LeetCode 11. 盛最多水的容器
 * 题目链接：https://leetcode.cn/problems/container-with-most-water/
 * 难度：中等
 * 
 * 问题描述：
 * 给定一个长度为 n 的整数数组 height 。有 n 条垂线，第 i 条线的两个端点是 (i, 0) 和 (i, height[i]) 。
 * 找出其中的两条线，使得它们与 x 轴共同构成的容器可以容纳最多的水。
 * 返回容器可以储存的最大水量。
 * 
 * 示例：
 * 输入：[1,8,6,2,5,4,8,3,7]
 * 输出：49
 * 解释：图中垂直线代表输入数组 [1,8,6,2,5,4,8,3,7]。在此情况下，容器能够容纳水（表示为蓝色部分）的最大值为 49。
 * 
 * 解题思路：
 * 贪心算法 + 双指针
 * 1. 使用双指针分别指向数组的左右两端
 * 2. 计算当前指针位置能容纳的水量：min(height[left], height[right]) * (right - left)
 * 3. 移动高度较小的指针，因为移动高度较大的指针不会增加水量
 * 4. 更新最大水量
 * 
 * 时间复杂度：O(n) - 只需要遍历数组一次
 * 空间复杂度：O(1) - 只使用了常数级别的额外空间
 * 
 * 最优性证明：
 * 贪心策略的正确性：每次移动高度较小的指针，因为容器的水量由较短的边决定，移动较短的边有可能遇到更高的边从而增加水量，
 * 而移动较长的边只会减少宽度，不会增加水量。
 * 
 * 工程化考量：
 * 1. 边界条件处理：数组长度小于2的情况
 * 2. 异常处理：空数组、负数高度（题目保证非负）
 * 3. 性能优化：避免重复计算，使用简洁的变量命名
 * 
 * 与机器学习的联系：
 * 双指针思想在特征选择中也有应用，可以用于寻找最优的特征组合
 */
public class Code20_ContainerWithMostWater {
    
    /**
     * 计算容器能容纳的最大水量
     * @param height 高度数组
     * @return 最大水量
     */
    public static int maxArea(int[] height) {
        // 边界条件处理
        if (height == null || height.length < 2) {
            return 0;
        }
        
        int left = 0; // 左指针
        int right = height.length - 1; // 右指针
        int maxWater = 0; // 最大水量
        
        // 双指针遍历
        while (left < right) {
            // 计算当前水量
            int currentWater = Math.min(height[left], height[right]) * (right - left);
            // 更新最大水量
            maxWater = Math.max(maxWater, currentWater);
            
            // 移动高度较小的指针
            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        
        return maxWater;
    }
    
    /**
     * 优化版本：添加详细注释和调试信息
     */
    public static int maxAreaOptimized(int[] height) {
        // 输入验证
        if (height == null || height.length < 2) {
            throw new IllegalArgumentException("高度数组长度必须至少为2");
        }
        
        int left = 0;
        int right = height.length - 1;
        int maxWater = 0;
        
        System.out.println("开始计算最大水量...");
        System.out.println("数组长度: " + height.length);
        
        while (left < right) {
            // 计算宽度
            int width = right - left;
            // 计算当前容器的高度（取较小值）
            int currentHeight = Math.min(height[left], height[right]);
            // 计算当前水量
            int currentWater = currentHeight * width;
            
            // 调试信息
            System.out.printf("left=%d (height=%d), right=%d (height=%d), width=%d, currentWater=%d%n",
                    left, height[left], right, height[right], width, currentWater);
            
            // 更新最大水量
            if (currentWater > maxWater) {
                maxWater = currentWater;
                System.out.println("更新最大水量: " + maxWater);
            }
            
            // 贪心策略：移动高度较小的指针
            if (height[left] < height[right]) {
                left++;
                System.out.println("移动左指针: " + (left - 1) + " -> " + left);
            } else {
                right--;
                System.out.println("移动右指针: " + (right + 1) + " -> " + right);
            }
        }
        
        System.out.println("计算完成，最大水量: " + maxWater);
        return maxWater;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：标准示例
        int[] height1 = {1, 8, 6, 2, 5, 4, 8, 3, 7};
        System.out.println("=== 测试用例1 ===");
        System.out.println("输入: " + java.util.Arrays.toString(height1));
        int result1 = maxArea(height1);
        System.out.println("预期结果: 49, 实际结果: " + result1);
        System.out.println();
        
        // 测试用例2：边界情况 - 只有两个元素
        int[] height2 = {1, 1};
        System.out.println("=== 测试用例2 ===");
        System.out.println("输入: " + java.util.Arrays.toString(height2));
        int result2 = maxArea(height2);
        System.out.println("预期结果: 1, 实际结果: " + result2);
        System.out.println();
        
        // 测试用例3：递增序列
        int[] height3 = {1, 2, 3, 4, 5};
        System.out.println("=== 测试用例3 ===");
        System.out.println("输入: " + java.util.Arrays.toString(height3));
        int result3 = maxArea(height3);
        System.out.println("预期结果: 6, 实际结果: " + result3);
        System.out.println();
        
        // 测试用例4：递减序列
        int[] height4 = {5, 4, 3, 2, 1};
        System.out.println("=== 测试用例4 ===");
        System.out.println("输入: " + java.util.Arrays.toString(height4));
        int result4 = maxArea(height4);
        System.out.println("预期结果: 6, 实际结果: " + result4);
        System.out.println();
        
        // 测试用例5：所有元素相同
        int[] height5 = {3, 3, 3, 3, 3};
        System.out.println("=== 测试用例5 ===");
        System.out.println("输入: " + java.util.Arrays.toString(height5));
        int result5 = maxArea(height5);
        System.out.println("预期结果: 12, 实际结果: " + result5);
        System.out.println();
        
        // 性能测试
        System.out.println("=== 性能测试 ===");
        int[] largeHeight = new int[10000];
        for (int i = 0; i < largeHeight.length; i++) {
            largeHeight[i] = (int) (Math.random() * 1000);
        }
        
        long startTime = System.currentTimeMillis();
        int largeResult = maxArea(largeHeight);
        long endTime = System.currentTimeMillis();
        System.out.println("大规模测试结果: " + largeResult);
        System.out.println("耗时: " + (endTime - startTime) + "ms");
    }
}

/*
工程化深度分析：

1. 算法正确性证明：
   - 贪心选择性质：每次移动较短的边是正确的，因为水量由较短的边决定
   - 最优子结构：问题的最优解包含子问题的最优解
   - 通过数学归纳法可以证明该策略能得到全局最优解

2. 复杂度分析详解：
   - 时间复杂度：O(n)，每个元素最多被访问一次
   - 空间复杂度：O(1)，只使用了常数级别的变量
   - 这是最优复杂度，因为必须检查所有可能的容器组合

3. 边界条件处理：
   - 数组长度小于2：直接返回0
   - 空数组：抛出异常或返回0
   - 负数高度：题目保证非负，但实际工程中需要验证

4. 异常场景考虑：
   - 输入为null：抛出IllegalArgumentException
   - 数组包含负数：虽然题目保证非负，但工程中需要处理
   - 超大数组：算法时间复杂度为O(n)，可以处理大规模数据

5. 性能优化策略：
   - 避免重复计算：缓存Math.min结果
   - 减少函数调用：内联计算
   - 使用基本类型：避免自动装箱

6. 调试与测试：
   - 添加详细日志：跟踪指针移动和水量计算
   - 单元测试：覆盖各种边界情况
   - 性能测试：验证大规模数据下的表现

7. 跨语言实现差异：
   - Java：使用Math.min和基本类型
   - C++：使用std::min和指针
   - Python：使用min函数和列表索引

8. 工程实践建议：
   - 在生产环境中添加输入验证
   - 对于超大规模数据，可以考虑并行处理
   - 添加监控和性能指标

9. 算法扩展性：
   - 可以扩展支持三维容器
   - 可以扩展支持动态高度变化
   - 可以扩展支持多个容器的组合优化

10. 与机器学习联系：
    - 双指针思想可以用于特征选择中的最优子集选择
    - 贪心策略在强化学习的ε-贪心算法中有应用
    - 该问题可以看作是在约束条件下的优化问题
*/