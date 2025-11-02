package class050;

/**
 * 盛最多水的容器
 * 
 * 题目描述：
 * 给定一个长度为 n 的整数数组 height 。有 n 条垂线，第 i 条线的两个端点是 (i, 0) 和 (i, height[i]) 。
 * 找出其中的两条线，使得它们与 x 轴共同构成的容器可以容纳最多的水。
 * 返回容器可以储存的最大水量。
 * 说明：你不能倾斜容器。
 * 
 * 示例：
 * 输入：[1,8,6,2,5,4,8,3,7]
 * 输出：49
 * 解释：图中垂直线代表输入数组 [1,8,6,2,5,4,8,3,7]。在此情况下，容器能够容纳水的最大值为 49。
 * 
 * 输入：height = [1,1]
 * 输出：1
 * 
 * 解题思路：
 * 使用双指针法。左指针指向数组开始，右指针指向数组末尾。
 * 容器的容量由较短的那条线决定，所以每次移动较短的那条线的指针，尝试寻找更长的线来增大容量。
 * 这样可以在O(n)时间内找到最大容量。
 * 
 * 时间复杂度：O(n) - 双指针最多遍历一次数组
 * 空间复杂度：O(1) - 只使用了常数级别的额外空间
 * 是否最优解：是 - 基于比较的算法下界为O(n)，本算法已达到最优
 * 
 * 相关题目：
 * 1. LeetCode 11 - 盛最多水的容器（当前题目）
 * 2. LeetCode 42 - 接雨水（双指针）
 * 3. LeetCode 84 - 柱状图中最大的矩形（单调栈）
 * 
 * 工程化考虑：
 * 1. 输入验证：检查数组是否为空或长度小于2
 * 2. 异常处理：处理各种边界情况
 * 3. 边界条件：处理数组长度为2的情况
 * 
 * 语言特性差异：
 * Java: 使用Math.min和Math.max函数
 * C++: 可使用std::min和std::max函数
 * Python: 可使用min和max函数
 * 
 * 极端输入场景：
 * 1. 数组长度为2
 * 2. 数组中所有元素相等
 * 3. 数组呈递增或递减趋势
 * 4. 最大值在数组中间
 * 
 * 与机器学习等领域的联系：
 * 1. 在优化问题中，可能需要找到两个参数的最优组合
 * 2. 在特征工程中，可能需要找到两个特征的最佳配对
 */
public class Code10_ContainerWithMostWater {

    /**
     * 计算盛最多水的容器的容量
     * 
     * @param height 整数数组，表示每条垂线的高度
     * @return 容器可以储存的最大水量
     */
    public static int maxArea(int[] height) {
        // 边界条件检查
        if (height == null || height.length < 2) {
            return 0;
        }

        int maxWater = 0;
        int left = 0;              // 左指针
        int right = height.length - 1;  // 右指针

        // 当左指针小于右指针时继续循环
        while (left < right) {
            // 计算当前容器的容量
            // 容量由较短的那条线决定，乘以两条线之间的距离
            int currentWater = Math.min(height[left], height[right]) * (right - left);
            
            // 更新最大容量
            maxWater = Math.max(maxWater, currentWater);

            // 移动较短的那条线的指针，尝试寻找更长的线来增大容量
            if (height[left] <= height[right]) {
                left++;
            } else {
                right--;
            }
        }

        return maxWater;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1: [1,8,6,2,5,4,8,3,7] -> 49
        int[] height1 = {1, 8, 6, 2, 5, 4, 8, 3, 7};
        int result1 = maxArea(height1);
        System.out.println("Test 1: height=" + java.util.Arrays.toString(height1));
        System.out.println("Result: " + result1);

        // 测试用例2: [1,1] -> 1
        int[] height2 = {1, 1};
        int result2 = maxArea(height2);
        System.out.println("Test 2: height=" + java.util.Arrays.toString(height2));
        System.out.println("Result: " + result2);
    }
}