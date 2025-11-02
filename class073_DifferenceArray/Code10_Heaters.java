package class047;

import java.util.*;

/**
 * LeetCode 475. 供暖器 (Heaters)
 * 
 * 题目描述:
 * 冬季已经来临。你的任务是设计一个有固定加热半径的供暖器，使得所有房屋都可以被供暖。
 * 现在，给出位于一条水平线上的房屋和供暖器的位置，找到可以覆盖所有房屋的最小加热半径。
 * 所以，你的输入将会是房屋和供暖器的位置。你将输出供暖器的最小加热半径。
 * 
 * 示例1:
 * 输入: houses = [1,2,3], heaters = [2]
 * 输出: 1
 * 解释: 仅在位置2上有一个供暖器。如果我们将加热半径设为1，那么所有房屋就都能得到供暖。
 * 
 * 示例2:
 * 输入: houses = [1,2,3,4], heaters = [1,4]
 * 输出: 1
 * 解释: 在位置1和4上有两个供暖器。我们需要将加热半径设为1，这样房屋2和3就都能得到供暖。
 * 
 * 示例3:
 * 输入: houses = [1,5], heaters = [2]
 * 输出: 3
 * 解释: 供暖器在位置2，需要半径3才能覆盖房屋1和房屋5。
 * 
 * 提示:
 * 1. 给出的房屋和供暖器的数目是非负数且不会超过 25000。
 * 2. 给出的房屋和供暖器的位置均是非负数且不会超过 10^9。
 * 3. 只要房屋位于供暖器的半径内（包括在边缘上），它就可以得到供暖。
 * 4. 所有供暖器都遵循你的半径标准，加热的半径也一样。
 * 
 * 题目链接: https://leetcode.com/problems/heaters/
 * 
 * 解题思路:
 * 这个问题可以使用二分查找来解决：
 * 1. 首先对供暖器的位置进行排序，以便使用二分查找
 * 2. 对于每个房屋，找到离它最近的供暖器
 * 3. 计算房屋到最近供暖器的距离，并更新最大距离
 * 4. 最终的最大距离就是所需的最小加热半径
 * 
 * 具体步骤：
 * 1. 对供暖器数组进行排序
 * 2. 遍历每个房屋位置
 * 3. 对每个房屋位置，使用二分查找找到其左右两侧最近的供暖器
 * 4. 计算房屋到这两个供暖器的距离，取较小值
 * 5. 更新全局最大距离
 * 
 * 时间复杂度: O(n log n + m log n) - n是供暖器数量，m是房屋数量，排序需要O(n log n)，每个房屋的二分查找需要O(log n)
 * 空间复杂度: O(1) - 只需要常数级的额外空间
 * 
 * 这是最优解，因为我们需要遍历每个房屋并为每个房屋进行二分查找，这已经是理论上的最优复杂度。
 */
public class Code10_Heaters {

    /**
     * 计算供暖器的最小加热半径
     * 
     * @param houses 房屋位置数组
     * @param heaters 供暖器位置数组
     * @return 最小加热半径
     */
    public static int findRadius(int[] houses, int[] heaters) {
        if (houses == null || houses.length == 0) {
            return 0;
        }
        if (heaters == null || heaters.length == 0) {
            // 没有供暖器，无法供暖，但根据题意，供暖器数量不会为0
            return -1;
        }
        
        // 对供暖器位置进行排序，以便使用二分查找
        Arrays.sort(heaters);
        
        int maxRadius = 0;
        
        // 遍历每个房屋
        for (int house : houses) {
            // 找到离当前房屋最近的供暖器
            int closestHeaterDistance = findClosestHeater(house, heaters);
            
            // 更新最大半径
            maxRadius = Math.max(maxRadius, closestHeaterDistance);
        }
        
        return maxRadius;
    }
    
    /**
     * 使用二分查找找到离指定房屋最近的供暖器，并返回距离
     * 
     * @param house 房屋位置
     * @param heaters 已排序的供暖器位置数组
     * @return 房屋到最近供暖器的距离
     */
    private static int findClosestHeater(int house, int[] heaters) {
        int left = 0;
        int right = heaters.length - 1;
        
        // 处理边界情况：房屋在所有供暖器的左侧
        if (house <= heaters[0]) {
            return heaters[0] - house;
        }
        // 处理边界情况：房屋在所有供暖器的右侧
        if (house >= heaters[right]) {
            return house - heaters[right];
        }
        
        // 二分查找
        while (left < right - 1) {
            int mid = left + (right - left) / 2;
            if (heaters[mid] == house) {
                return 0; // 房屋正好在供暖器位置
            } else if (heaters[mid] < house) {
                left = mid;
            } else {
                right = mid;
            }
        }
        
        // 此时，heaters[left] < house < heaters[right]，计算到两者的距离，取较小值
        return Math.min(house - heaters[left], heaters[right] - house);
    }

    /**
     * 另一种实现方式，使用Java内置的二分查找方法
     */
    public static int findRadiusAlternative(int[] houses, int[] heaters) {
        if (houses == null || houses.length == 0) {
            return 0;
        }
        if (heaters == null || heaters.length == 0) {
            return -1;
        }
        
        Arrays.sort(heaters);
        int maxRadius = 0;
        
        for (int house : houses) {
            // 使用Java内置的二分查找找到插入位置
            int index = Arrays.binarySearch(heaters, house);
            
            // 如果找到供暖器
            if (index >= 0) {
                continue; // 距离为0，不影响结果
            }
            
            // 如果没找到，index是-(插入点)-1
            int insertPos = -index - 1;
            int closestDistance = Integer.MAX_VALUE;
            
            // 检查左侧供暖器
            if (insertPos > 0) {
                closestDistance = Math.min(closestDistance, house - heaters[insertPos - 1]);
            }
            
            // 检查右侧供暖器
            if (insertPos < heaters.length) {
                closestDistance = Math.min(closestDistance, heaters[insertPos] - house);
            }
            
            maxRadius = Math.max(maxRadius, closestDistance);
        }
        
        return maxRadius;
    }

    /**
     * 测试用例
     */
    public static void main(String[] args) {
        // 测试用例1
        int[] houses1 = {1, 2, 3};
        int[] heaters1 = {2};
        System.out.println("测试用例1 结果: " + findRadius(houses1, heaters1)); // 预期输出: 1
        System.out.println("测试用例1 (替代方法) 结果: " + findRadiusAlternative(houses1, heaters1)); // 预期输出: 1

        // 测试用例2
        int[] houses2 = {1, 2, 3, 4};
        int[] heaters2 = {1, 4};
        System.out.println("测试用例2 结果: " + findRadius(houses2, heaters2)); // 预期输出: 1
        System.out.println("测试用例2 (替代方法) 结果: " + findRadiusAlternative(houses2, heaters2)); // 预期输出: 1

        // 测试用例3
        int[] houses3 = {1, 5};
        int[] heaters3 = {2};
        System.out.println("测试用例3 结果: " + findRadius(houses3, heaters3)); // 预期输出: 3
        System.out.println("测试用例3 (替代方法) 结果: " + findRadiusAlternative(houses3, heaters3)); // 预期输出: 3
        
        // 测试用例4 - 空输入
        int[] houses4 = {};
        int[] heaters4 = {1, 2, 3};
        System.out.println("测试用例4 (空房屋) 结果: " + findRadius(houses4, heaters4)); // 预期输出: 0
        
        // 测试用例5 - 供暖器和房屋重叠
        int[] houses5 = {1, 1, 1, 1};
        int[] heaters5 = {1};
        System.out.println("测试用例5 (重叠位置) 结果: " + findRadius(houses5, heaters5)); // 预期输出: 0
        
        // 测试用例6 - 大规模数据
        int[] houses6 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int[] heaters6 = {3, 7};
        System.out.println("测试用例6 (大规模数据) 结果: " + findRadius(houses6, heaters6)); // 预期输出: 3
    }
}