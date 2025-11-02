package class175.随机化与复杂度分析;

import java.util.Random;

/**
 * Fisher-Yates 洗牌算法
 * 算法思想：从数组末尾开始，将当前位置与之前的随机位置交换，确保每个元素都有相同的概率出现在任意位置
 * 时间复杂度：O(n)
 * 空间复杂度：O(1) - 原地洗牌
 * 
 * 相关题目：
 * 1. LeetCode 384. 打乱数组 - https://leetcode-cn.com/problems/shuffle-an-array/
 * 2. LintCode 1423. 随机洗牌 - https://www.lintcode.com/problem/1423/
 * 3. CodeChef - SHUFFLE - https://www.codechef.com/problems/SHUFFLE
 */
public class FisherYatesShuffle {
    private final Random random;

    public FisherYatesShuffle() {
        this.random = new Random();
    }

    /**
     * Fisher-Yates 洗牌算法实现
     * @param array 需要洗牌的数组
     */
    public void shuffle(int[] array) {
        if (array == null || array.length <= 1) {
            return;
        }

        // 从后往前遍历数组
        for (int i = array.length - 1; i > 0; i--) {
            // 生成 [0, i] 范围内的随机索引
            int j = random.nextInt(i + 1);
            // 交换 array[i] 和 array[j]
            swap(array, i, j);
        }
    }

    /**
     * 交换数组中两个元素的位置
     * @param array 数组
     * @param i 第一个元素的索引
     * @param j 第二个元素的索引
     */
    private void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        FisherYatesShuffle shuffle = new FisherYatesShuffle();
        int[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        
        System.out.println("原始数组：");
        printArray(array);
        
        shuffle.shuffle(array);
        
        System.out.println("洗牌后数组：");
        printArray(array);
    }

    /**
     * 打印数组
     */
    private static void printArray(int[] array) {
        for (int num : array) {
            System.out.print(num + " ");
        }
        System.out.println();
    }
}