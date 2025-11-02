package class090;

// 种花问题
// 假设有一个很长的花坛，一部分地块种植了花，另一部分却没有。
// 可是，花不能种植在相邻的地块上，它们会争夺水源，两者都会死去。
// 给你一个整数数组 flowerbed 表示花坛，由若干 0 和 1 组成，
// 其中 0 表示没种植花，1 表示种植了花。
// 另有一个数 n ，能否在不打破种植规则的情况下种入 n 朵花？
// 能则返回 true ，不能则返回 false。
// 测试链接: https://leetcode.cn/problems/can-place-flowers/
public class Code14_CanPlaceFlowers {

    /**
     * 种花问题的贪心解法
     * 
     * 解题思路：
     * 1. 贪心策略：从左到右遍历花坛，在可以种花的位置就种一朵
     * 2. 判断位置是否可以种花的条件：
     *    - 当前位置必须是空地（值为0）
     *    - 前一个位置是空地或者边界
     *    - 后一个位置是空地或者边界
     * 3. 种花后更新花坛状态，统计种花数量
     * 
     * 贪心策略的正确性：
     * 能种就种是一种贪心的思想，因为每个位置种花都不会影响之前已经判断过的位置，
     * 而且能种花的位置如果不种，也不会得到更优解。
     * 
     * 时间复杂度：O(n)，只需要遍历数组一次
     * 空间复杂度：O(1)，只使用了常数个额外变量
     * 
     * @param flowerbed 花坛数组，0表示空地，1表示已种花
     * @param n 需要种花的数量
     * @return 是否能种下n朵花
     */
    public static boolean canPlaceFlowers(int[] flowerbed, int n) {
        // 边界条件处理：如果需要种花数量为0，则一定可以种下
        if (n == 0) {
            return true;
        }

        // 1. 初始化已种花数量
        int count = 0;

        // 2. 遍历花坛数组
        for (int i = 0; i < flowerbed.length; i++) {
            // 3. 判断当前位置是否可以种花
            // 条件：当前位置是空地，且前一个位置是空地或边界，且后一个位置是空地或边界
            if (flowerbed[i] == 0 
                && (i == 0 || flowerbed[i - 1] == 0) 
                && (i == flowerbed.length - 1 || flowerbed[i + 1] == 0)) {
                
                // 4. 在当前位置种花
                flowerbed[i] = 1;
                count++;

                // 5. 如果已经种下所需数量的花，直接返回true
                if (count >= n) {
                    return true;
                }
            }
        }

        // 6. 返回是否种下了足够的花
        return count >= n;
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        // 输入: flowerbed = [1,0,0,0,1], n = 1
        // 输出: true
        int[] flowerbed1 = {1, 0, 0, 0, 1};
        System.out.println("测试用例1结果: " + canPlaceFlowers(flowerbed1, 1)); // 期望输出: true

        // 测试用例2
        // 输入: flowerbed = [1,0,0,0,1], n = 2
        // 输出: false
        int[] flowerbed2 = {1, 0, 0, 0, 1};
        System.out.println("测试用例2结果: " + canPlaceFlowers(flowerbed2, 2)); // 期望输出: false

        // 测试用例3
        // 输入: flowerbed = [0,0,0,0,0], n = 3
        // 输出: true
        int[] flowerbed3 = {0, 0, 0, 0, 0};
        System.out.println("测试用例3结果: " + canPlaceFlowers(flowerbed3, 3)); // 期望输出: true

        // 测试用例4：边界情况
        // 输入: flowerbed = [1], n = 0
        // 输出: true
        int[] flowerbed4 = {1};
        System.out.println("测试用例4结果: " + canPlaceFlowers(flowerbed4, 0)); // 期望输出: true

        // 测试用例5：复杂情况
        // 输入: flowerbed = [0,0,1,0,0], n = 1
        // 输出: true
        int[] flowerbed5 = {0, 0, 1, 0, 0};
        System.out.println("测试用例5结果: " + canPlaceFlowers(flowerbed5, 1)); // 期望输出: true
    }
}