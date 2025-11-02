package class092;

import java.util.Arrays;

// 分发糖果
// n 个孩子站成一排。给你一个整数数组 ratings 表示每个孩子的评分
// 你需要按照以下要求，给这些孩子分发糖果：
// 每个孩子至少分配到 1 个糖果。
// 相邻两个孩子评分更高的孩子会获得更多的糖果。
// 请你给每个孩子分发糖果，计算并返回需要准备的 最少糖果数目
// 测试链接 : https://leetcode.cn/problems/candy/
public class Code08_Candy {

    /*
     * 贪心算法解法
     * 
     * 核心思想：
     * 1. 从左到右扫描一次，确保每个评分比左边高的孩子获得的糖果比左边孩子多
     * 2. 从右到左扫描一次，确保每个评分比右边高的孩子获得的糖果比右边孩子多
     * 3. 取两次扫描结果的最大值作为最终结果
     * 
     * 时间复杂度：O(n) - 需要遍历数组两次
     * 空间复杂度：O(n) - 需要额外的数组存储每个孩子的糖果数
     * 
     * 为什么这是最优解？
     * 1. 贪心策略保证了每一步都满足局部最优条件
     * 2. 通过两次扫描分别处理左右两个方向的约束条件
     * 3. 无法在更少的时间内完成，因为至少需要遍历一遍数组
     * 
     * 工程化考虑：
     * 1. 边界条件处理：空数组、单元素数组
     * 2. 异常处理：输入参数验证
     * 3. 可读性：变量命名清晰，注释详细
     * 
     * 算法调试技巧：
     * 1. 可以通过打印每一步的left和right数组来观察糖果分配过程
     * 2. 用断言验证中间结果是否符合预期
     */

    public static int candy(int[] ratings) {
        if (ratings == null || ratings.length == 0) {
            return 0;
        }

        int n = ratings.length;
        // left[i] 表示从左到右扫描时，第i个孩子应该分得的糖果数
        int[] left = new int[n];
        Arrays.fill(left, 1); // 每个孩子至少分得1个糖果

        // 从左到右扫描
        for (int i = 1; i < n; i++) {
            // 如果当前孩子的评分比左边孩子高，则糖果数应该比左边孩子多
            if (ratings[i] > ratings[i - 1]) {
                left[i] = left[i - 1] + 1;
            }
        }

        // right 表示从右到左扫描时，当前孩子应该分得的糖果数
        int right = 1;
        int total = left[n - 1]; // 最后一个孩子的糖果数已经确定

        // 从右到左扫描
        for (int i = n - 2; i >= 0; i--) {
            // 如果当前孩子的评分比右边孩子高，则糖果数应该比右边孩子多
            if (ratings[i] > ratings[i + 1]) {
                right++;
            } else {
                right = 1; // 否则重置为1
            }
            // 取两次扫描结果的最大值作为最终结果
            total += Math.max(left[i], right);
        }

        return total;
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1: [1,0,2] -> 5
        int[] ratings1 = { 1, 0, 2 };
        System.out.println("测试用例1: " + Arrays.toString(ratings1));
        System.out.println("预期结果: 5, 实际结果: " + candy(ratings1));

        // 测试用例2: [1,2,2] -> 4
        int[] ratings2 = { 1, 2, 2 };
        System.out.println("测试用例2: " + Arrays.toString(ratings2));
        System.out.println("预期结果: 4, 实际结果: " + candy(ratings2));

        // 测试用例3: [1,3,2,2,1] -> 7
        int[] ratings3 = { 1, 3, 2, 2, 1 };
        System.out.println("测试用例3: " + Arrays.toString(ratings3));
        System.out.println("预期结果: 7, 实际结果: " + candy(ratings3));

        // 测试用例4: [1] -> 1
        int[] ratings4 = { 1 };
        System.out.println("测试用例4: " + Arrays.toString(ratings4));
        System.out.println("预期结果: 1, 实际结果: " + candy(ratings4));
    }
}