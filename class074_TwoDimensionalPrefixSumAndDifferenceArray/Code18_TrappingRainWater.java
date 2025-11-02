import java.util.*;

/**
 * LeetCode 42. 接雨水 (Trapping Rain Water)
 * 
 * 题目描述:
 * 给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子，下雨之后能接多少雨水。
 * 
 * 示例1:
 * 输入: height = [0,1,0,2,1,0,1,3,2,1,2,1]
 * 输出: 6
 * 解释: 上面是由数组 [0,1,0,2,1,0,1,3,2,1,2,1] 表示的高度图，在这种情况下，可以接 6 个单位的雨水（蓝色部分表示雨水）。
 * 
 * 示例2:
 * 输入: height = [4,2,0,3,2,5]
 * 输出: 9
 * 
 * 提示:
 * n == height.length
 * 1 <= n <= 2 * 10^4
 * 0 <= height[i] <= 10^5
 * 
 * 题目链接: https://leetcode.com/problems/trapping-rain-water/
 * 
 * 解题思路:
 * 这道题可以通过多种方法解决，包括:
 * 1. 暴力解法：计算每个位置能接的雨水量，然后求和
 * 2. 动态规划：预先计算每个位置左右两侧的最高柱子高度
 * 3. 双指针法：使用两个指针从两端向中间移动
 * 4. 单调栈：使用栈来寻找可以接水的凹槽
 * 
 * 这里实现三种解法：双指针法（最优解）、动态规划和单调栈。
 * 
 * 解法一: 双指针法
 * 时间复杂度: O(n)，其中 n 是数组的长度。只需要遍历一次数组。
 * 空间复杂度: O(1)，只使用了常数级别的额外空间。
 * 
 * 解法二: 动态规划
 * 时间复杂度: O(n)，需要两次遍历数组来填充左右最大高度数组。
 * 空间复杂度: O(n)，需要两个长度为 n 的数组来存储左右最大高度。
 * 
 * 解法三: 单调栈
 * 时间复杂度: O(n)，每个元素最多入栈和出栈一次。
 * 空间复杂度: O(n)，最坏情况下，栈的大小可能达到数组长度。
 */
public class Code18_TrappingRainWater {
    
    /**
     * 解法一: 双指针法
     * 使用两个指针从两端向中间移动，每次比较左右两侧的最大值，决定当前位置能接的雨水量。
     */
    public static int trapTwoPointers(int[] height) {
        if (height == null || height.length < 3) {
            return 0;
        }
        
        int left = 0;
        int right = height.length - 1;
        int leftMax = 0;
        int rightMax = 0;
        int waterTrapped = 0;
        
        while (left < right) {
            // 更新左右两侧的最大高度
            leftMax = Math.max(leftMax, height[left]);
            rightMax = Math.max(rightMax, height[right]);
            
            // 哪边的最大值较小，哪边可以接水
            if (leftMax < rightMax) {
                // 左侧最大值较小，计算左侧当前位置能接的水量
                waterTrapped += leftMax - height[left];
                left++;
            } else {
                // 右侧最大值较小，计算右侧当前位置能接的水量
                waterTrapped += rightMax - height[right];
                right--;
            }
        }
        
        return waterTrapped;
    }
    
    /**
     * 解法二: 动态规划
     * 预先计算每个位置左右两侧的最高柱子高度，然后计算每个位置能接的雨水量。
     */
    public static int trapDynamicProgramming(int[] height) {
        if (height == null || height.length < 3) {
            return 0;
        }
        
        int n = height.length;
        int[] leftMax = new int[n];  // 存储每个位置左侧的最高柱子高度
        int[] rightMax = new int[n]; // 存储每个位置右侧的最高柱子高度
        int waterTrapped = 0;
        
        // 计算每个位置左侧的最高柱子高度
        leftMax[0] = height[0];
        for (int i = 1; i < n; i++) {
            leftMax[i] = Math.max(leftMax[i-1], height[i]);
        }
        
        // 计算每个位置右侧的最高柱子高度
        rightMax[n-1] = height[n-1];
        for (int i = n-2; i >= 0; i--) {
            rightMax[i] = Math.max(rightMax[i+1], height[i]);
        }
        
        // 计算每个位置能接的雨水量
        for (int i = 0; i < n; i++) {
            // 当前位置能接的雨水量 = min(左侧最高柱子高度, 右侧最高柱子高度) - 当前柱子高度
            waterTrapped += Math.min(leftMax[i], rightMax[i]) - height[i];
        }
        
        return waterTrapped;
    }
    
    /**
     * 解法三: 单调栈
     * 使用栈来寻找可以接水的凹槽，栈中存储的是索引。
     */
    public static int trapMonotonicStack(int[] height) {
        if (height == null || height.length < 3) {
            return 0;
        }
        
        int n = height.length;
        int waterTrapped = 0;
        Deque<Integer> stack = new LinkedList<>(); // 存储索引
        
        for (int i = 0; i < n; i++) {
            // 当栈不为空且当前高度大于栈顶索引对应的高度时，说明找到了一个可以接水的凹槽
            while (!stack.isEmpty() && height[i] > height[stack.peek()]) {
                int bottom = stack.pop(); // 凹槽的底部索引
                
                if (stack.isEmpty()) {
                    break; // 没有左边界，无法接水
                }
                
                // 计算凹槽的宽度
                int width = i - stack.peek() - 1;
                // 计算凹槽的高度：min(左边界高度, 右边界高度) - 底部高度
                int depth = Math.min(height[stack.peek()], height[i]) - height[bottom];
                // 累加雨水量
                waterTrapped += width * depth;
            }
            
            stack.push(i); // 将当前索引入栈
        }
        
        return waterTrapped;
    }
    
    public static void printArray(int[] arr) {
        System.out.print("[");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            if (i < arr.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }
    
    public static void main(String[] args) {
        // 测试用例1
        int[] height1 = {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};
        System.out.println("测试用例1:");
        System.out.print("height = ");
        printArray(height1);
        System.out.println("双指针法结果: " + trapTwoPointers(height1)); // 预期输出: 6
        System.out.println("动态规划结果: " + trapDynamicProgramming(height1)); // 预期输出: 6
        System.out.println("单调栈结果: " + trapMonotonicStack(height1)); // 预期输出: 6
        System.out.println();
        
        // 测试用例2
        int[] height2 = {4, 2, 0, 3, 2, 5};
        System.out.println("测试用例2:");
        System.out.print("height = ");
        printArray(height2);
        System.out.println("双指针法结果: " + trapTwoPointers(height2)); // 预期输出: 9
        System.out.println("动态规划结果: " + trapDynamicProgramming(height2)); // 预期输出: 9
        System.out.println("单调栈结果: " + trapMonotonicStack(height2)); // 预期输出: 9
        System.out.println();
        
        // 测试用例3 - 边界情况：只有两根柱子
        int[] height3 = {1, 2};
        System.out.println("测试用例3:");
        System.out.print("height = ");
        printArray(height3);
        System.out.println("双指针法结果: " + trapTwoPointers(height3)); // 预期输出: 0
        System.out.println("动态规划结果: " + trapDynamicProgramming(height3)); // 预期输出: 0
        System.out.println("单调栈结果: " + trapMonotonicStack(height3)); // 预期输出: 0
        System.out.println();
        
        // 测试用例4 - 边界情况：单调递增数组
        int[] height4 = {1, 2, 3, 4, 5};
        System.out.println("测试用例4:");
        System.out.print("height = ");
        printArray(height4);
        System.out.println("双指针法结果: " + trapTwoPointers(height4)); // 预期输出: 0
        System.out.println("动态规划结果: " + trapDynamicProgramming(height4)); // 预期输出: 0
        System.out.println("单调栈结果: " + trapMonotonicStack(height4)); // 预期输出: 0
        System.out.println();
        
        // 测试用例5 - 边界情况：单调递减数组
        int[] height5 = {5, 4, 3, 2, 1};
        System.out.println("测试用例5:");
        System.out.print("height = ");
        printArray(height5);
        System.out.println("双指针法结果: " + trapTwoPointers(height5)); // 预期输出: 0
        System.out.println("动态规划结果: " + trapDynamicProgramming(height5)); // 预期输出: 0
        System.out.println("单调栈结果: " + trapMonotonicStack(height5)); // 预期输出: 0
        System.out.println();
        
        // 性能测试
        System.out.println("性能测试:");
        int n = 20000;
        int[] height6 = new int[n];
        // 生成测试数据：波峰波谷交替
        for (int i = 0; i < n; i++) {
            height6[i] = Math.min(i, n - i); // 形成一个山峰形状
        }
        
        long startTime = System.currentTimeMillis();
        int result1 = trapTwoPointers(height6);
        long endTime = System.currentTimeMillis();
        System.out.println("双指针法结果: " + result1);
        System.out.println("双指针法耗时: " + (endTime - startTime) + "ms");
        
        startTime = System.currentTimeMillis();
        int result2 = trapDynamicProgramming(height6);
        endTime = System.currentTimeMillis();
        System.out.println("动态规划结果: " + result2);
        System.out.println("动态规划耗时: " + (endTime - startTime) + "ms");
        
        startTime = System.currentTimeMillis();
        int result3 = trapMonotonicStack(height6);
        endTime = System.currentTimeMillis();
        System.out.println("单调栈结果: " + result3);
        System.out.println("单调栈耗时: " + (endTime - startTime) + "ms");
    }
}