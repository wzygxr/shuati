package class050;

/**
 * 接雨水
 * 
 * 题目描述：
 * 给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子，下雨之后能接多少雨水。
 * 
 * 示例：
 * 输入：height = [0,1,0,2,1,0,1,3,2,1,2,1]
 * 输出：6
 * 解释：上面是由数组 [0,1,0,2,1,0,1,3,2,1,2,1] 表示的高度图，在这种情况下，可以接 6 个单位的雨水（蓝色部分表示雨水）。
 * 
 * 输入：height = [4,2,0,3,2,5]
 * 输出：9
 * 
 * 解题思路：
 * 使用双指针法。我们维护两个指针 left 和 right 分别指向数组的两端，同时维护两个变量 leftMax 和 rightMax 
 * 分别记录左边和右边的最大高度。
 * 1. 如果 leftMax < rightMax，说明左边的最大值是瓶颈，可以确定 left 位置能接的雨水量为 leftMax - height[left]。
 * 2. 如果 leftMax >= rightMax，说明右边的最大值是瓶颈，可以确定 right 位置能接的雨水量为 rightMax - height[right]。
 * 3. 移动对应指针并更新最大值，直到两个指针相遇。
 * 
 * 时间复杂度：O(n) - 只需要遍历一次数组
 * 空间复杂度：O(1) - 只使用了常数级别的额外空间
 * 是否最优解：是 - 基于比较的算法下界为O(n)，本算法已达到最优
 * 
 * 相关题目：
 * 1. LeetCode 42 - 接雨水（当前题目）
 * 2. LeetCode 11 - 盛最多水的容器
 * 3. LeetCode 407 - 接雨水 II（二维版本）
 * 
 * 工程化考虑：
 * 1. 输入验证：检查数组是否为空
 * 2. 异常处理：处理各种边界情况
 * 3. 边界条件：处理数组长度小于3的情况
 * 
 * 语言特性差异：
 * Java: 使用数组索引访问
 * C++: 可使用vector
 * Python: 可使用列表
 * 
 * 极端输入场景：
 * 1. 数组长度小于3
 * 2. 数组全为0
 * 3. 数组呈递增或递减趋势
 * 4. 数组呈V字形或倒V字形
 * 
 * 与机器学习等领域的联系：
 * 1. 在图像处理中，类似的问题可以用于计算图像中特定区域的特征
 * 2. 在地理信息系统中，可以用于计算地形的积水区域
 */
public class Code14_TrappingRainWater {

    /**
     * 使用双指针法计算能接多少雨水
     * 
     * @param height 柱子的高度数组
     * @return 能接的雨水总量
     */
    public static int trap(int[] height) {
        // 边界条件检查
        if (height == null || height.length < 3) {
            return 0;
        }

        int left = 0;              // 左指针
        int right = height.length - 1;  // 右指针
        int leftMax = 0;           // 左边最大高度
        int rightMax = 0;          // 右边最大高度
        int result = 0;            // 雨水总量

        // 当左指针小于右指针时继续循环
        while (left < right) {
            // 更新左边最大高度
            leftMax = Math.max(leftMax, height[left]);
            // 更新右边最大高度
            rightMax = Math.max(rightMax, height[right]);

            // 如果左边最大高度小于右边最大高度
            if (leftMax < rightMax) {
                // 左边是瓶颈，可以确定left位置能接的雨水量
                result += leftMax - height[left];
                left++;  // 移动左指针
            } else {
                // 右边是瓶颈，可以确定right位置能接的雨水量
                result += rightMax - height[right];
                right--; // 移动右指针
            }
        }

        return result;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1: [0,1,0,2,1,0,1,3,2,1,2,1] -> 6
        int[] height1 = {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};
        int result1 = trap(height1);
        System.out.println("Test 1: height=" + java.util.Arrays.toString(height1));
        System.out.println("Result: " + result1);

        // 测试用例2: [4,2,0,3,2,5] -> 9
        int[] height2 = {4, 2, 0, 3, 2, 5};
        int result2 = trap(height2);
        System.out.println("Test 2: height=" + java.util.Arrays.toString(height2));
        System.out.println("Result: " + result2);
    }
}