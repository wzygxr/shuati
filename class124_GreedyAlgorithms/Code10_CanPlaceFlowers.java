package class093;

/**
 * 种花问题（Can Place Flowers）
 * 题目来源：LeetCode 605
 * 题目链接：https://leetcode.cn/problems/can-place-flowers/
 * 
 * 问题描述：
 * 假设有一个很长的花坛，一部分地块种植了花，另一部分却没有。
 * 可是，花不能种植在相邻的地块上，它们会争夺水源，两者都会死去。
 * 给你一个整数数组 flowerbed 表示花坛，由若干 0 和 1 组成，
 * 其中 0 表示没种植花，1 表示种植了花。
 * 另有一个数 n ，能否在不打破种植规则的情况下种入 n 朵花？
 * 能则返回 true ，不能则返回 false 。
 * 
 * 算法思路：
 * 使用贪心策略，尽可能多地种花，只要当前位置可以种花（当前位置是0，且左右都是0或边界），就种花。
 * 具体步骤：
 * 1. 遍历花坛数组
 * 2. 对于每个位置，检查是否满足种花条件：
 *    - 当前位置是0
 *    - 前一个位置是0或当前是第一个位置
 *    - 后一个位置是0或当前是最后一个位置
 * 3. 如果满足条件，就在当前位置种花（将0改为1），并减少需要种的花数量
 * 4. 如果需要种的花数量减为0，返回true
 * 5. 遍历结束后，如果需要种的花数量已减为0，返回true，否则返回false
 * 
 * 时间复杂度：O(n)，其中n是花坛数组的长度，只需遍历数组一次
 * 空间复杂度：O(1)，只使用了常数额外空间
 * 
 * 是否最优解：是。贪心策略在此问题中能得到最优解。
 * 
 * 适用场景：
 * 1. 间隔种植问题
 * 2. 资源分配问题，需要满足相邻资源不能同时使用
 * 
 * 异常处理：
 * 1. 处理空数组情况
 * 2. 处理n为0的边界情况（不需要种花，直接返回true）
 * 
 * 工程化考量：
 * 1. 输入验证：检查数组是否为空，检查n是否为非负数
 * 2. 边界条件：处理边界位置的种植判断
 * 3. 性能优化：一旦确认可以种植n朵花，立即返回结果
 */
public class Code10_CanPlaceFlowers {
    
    /**
     * 判断是否能在不打破种植规则的情况下种入n朵花
     * 
     * @param flowerbed 表示花坛的数组，0表示没种植花，1表示种植了花
     * @param n 需要种入的花数量
     * @return 如果能种入n朵花返回true，否则返回false
     */
    public static boolean canPlaceFlowers(int[] flowerbed, int n) {
        // 边界条件检查
        if (flowerbed == null || flowerbed.length == 0) {
            return n == 0; // 空花坛只能种0朵花
        }
        
        if (n <= 0) {
            return true; // 不需要种花，直接返回true
        }
        
        int length = flowerbed.length;
        
        // 遍历花坛数组
        for (int i = 0; i < length; i++) {
            // 检查当前位置是否可以种花
            if (flowerbed[i] == 0) {
                // 检查左侧是否为空或边界
                boolean leftEmpty = (i == 0) || (flowerbed[i - 1] == 0);
                // 检查右侧是否为空或边界
                boolean rightEmpty = (i == length - 1) || (flowerbed[i + 1] == 0);
                
                if (leftEmpty && rightEmpty) {
                    // 可以种花
                    flowerbed[i] = 1; // 标记为已种花
                    n--; // 减少需要种的花数量
                    
                    // 如果已经种完所有需要的花，返回true
                    if (n == 0) {
                        return true;
                    }
                }
            }
        }
        
        // 遍历结束后，检查是否种完了所有需要的花
        return n == 0;
    }
    
    /**
     * 测试函数，验证算法正确性
     */
    public static void main(String[] args) {
        // 测试用例1: 基本情况 - 可以种花
        int[] flowerbed1 = {1, 0, 0, 0, 1};
        int n1 = 1;
        boolean result1 = canPlaceFlowers(flowerbed1, n1);
        System.out.println("测试用例1:");
        System.out.print("花坛: [");
        for (int i = 0; i < flowerbed1.length; i++) {
            System.out.print(flowerbed1[i]);
            if (i < flowerbed1.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
        System.out.println("需要种花数量: " + n1);
        System.out.println("能否种植: " + result1);
        System.out.println("期望输出: true");
        System.out.println();
        
        // 测试用例2: 基本情况 - 不能种花
        int[] flowerbed2 = {1, 0, 0, 0, 1};
        int n2 = 2;
        boolean result2 = canPlaceFlowers(flowerbed2, n2);
        System.out.println("测试用例2:");
        System.out.print("花坛: [");
        for (int i = 0; i < flowerbed2.length; i++) {
            System.out.print(flowerbed2[i]);
            if (i < flowerbed2.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
        System.out.println("需要种花数量: " + n2);
        System.out.println("能否种植: " + result2);
        System.out.println("期望输出: false");
        System.out.println();
        
        // 测试用例3: 边界情况 - n为0
        int[] flowerbed3 = {1, 0, 0, 0, 1};
        int n3 = 0;
        boolean result3 = canPlaceFlowers(flowerbed3, n3);
        System.out.println("测试用例3:");
        System.out.print("花坛: [");
        for (int i = 0; i < flowerbed3.length; i++) {
            System.out.print(flowerbed3[i]);
            if (i < flowerbed3.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
        System.out.println("需要种花数量: " + n3);
        System.out.println("能否种植: " + result3);
        System.out.println("期望输出: true");
        System.out.println();
        
        // 测试用例4: 边界情况 - 全为0的花坛
        int[] flowerbed4 = {0, 0, 0, 0};
        int n4 = 2;
        boolean result4 = canPlaceFlowers(flowerbed4, n4);
        System.out.println("测试用例4:");
        System.out.print("花坛: [");
        for (int i = 0; i < flowerbed4.length; i++) {
            System.out.print(flowerbed4[i]);
            if (i < flowerbed4.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
        System.out.println("需要种花数量: " + n4);
        System.out.println("能否种植: " + result4);
        System.out.println("期望输出: true");
        System.out.println();
        
        // 测试用例5: 边界情况 - 单元素花坛
        int[] flowerbed5 = {0};
        int n5 = 1;
        boolean result5 = canPlaceFlowers(flowerbed5, n5);
        System.out.println("测试用例5:");
        System.out.print("花坛: [");
        System.out.print(flowerbed5[0]);
        System.out.println("]");
        System.out.println("需要种花数量: " + n5);
        System.out.println("能否种植: " + result5);
        System.out.println("期望输出: true");
    }
}