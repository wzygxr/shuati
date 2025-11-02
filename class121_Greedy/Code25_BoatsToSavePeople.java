package class090;

import java.util.Arrays;

// 救生艇
// 给定数组 people 。people[i]表示第 i 个人的体重 ，船的数量不限，每艘船可以承载的最大重量为 limit。
// 每艘船最多可同时载两人，但条件是这些人的重量之和最多为 limit。
// 返回 承载所有人所需的最小船数 。
// 测试链接: https://leetcode.cn/problems/boats-to-save-people/
public class Code25_BoatsToSavePeople {

    /**
     * 救生艇问题的贪心解法
     * 
     * 解题思路：
     * 1. 将人的体重按升序排序
     * 2. 使用双指针，一个指向最轻的人，一个指向最重的人
     * 3. 如果最轻和最重的人可以一起坐船，则两人一起上船
     * 4. 否则，让最重的人单独坐船
     * 
     * 贪心策略的正确性：
     * 局部最优：让最重的人尽量和最轻的人配对，这样可以充分利用船的载重能力
     * 全局最优：使用最少的船只数
     * 
     * 时间复杂度：O(n log n)，主要消耗在排序上
     * 空间复杂度：O(1)，只使用了常数个额外变量
     * 
     * @param people 人的体重数组
     * @param limit 船的载重限制
     * @return 所需的最小船数
     */
    public static int numRescueBoats(int[] people, int limit) {
        // 边界条件处理
        if (people == null || people.length == 0) return 0;
        
        // 1. 对人的体重进行升序排序
        Arrays.sort(people);
        
        // 2. 初始化双指针
        int left = 0;           // 指向最轻的人
        int right = people.length - 1; // 指向最重的人
        int boats = 0;           // 船的数量
        
        // 3. 双指针遍历
        while (left <= right) {
            // 4. 如果最轻和最重的人可以一起坐船
            if (people[left] + people[right] <= limit) {
                left++;   // 最轻的人上船
                right--;  // 最重的人上船
            } else {
                // 5. 否则，让最重的人单独坐船
                right--;
            }
            boats++; // 使用一艘船
        }
        
        return boats;
    }

    /**
     * 救生艇问题的另一种解法（更详细）
     * 
     * 解题思路：
     * 1. 排序后使用贪心策略
     * 2. 详细记录每一步的操作
     * 
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(1)
     */
    public static int numRescueBoats2(int[] people, int limit) {
        if (people == null || people.length == 0) return 0;
        
        Arrays.sort(people);
        int boats = 0;
        int i = 0, j = people.length - 1;
        
        while (i <= j) {
            // 如果当前最重的人可以单独坐船
            if (people[j] > limit) {
                // 这种情况不应该发生，因为题目保证每个人的体重不超过limit
                j--;
                boats++;
                continue;
            }
            
            // 如果最轻和最重的人可以一起坐船
            if (i < j && people[i] + people[j] <= limit) {
                i++;
                j--;
            } else {
                // 最重的人单独坐船
                j--;
            }
            boats++;
        }
        
        return boats;
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        // 输入: people = [1,2], limit = 3
        // 输出: 1
        // 解释: 1 和 2 可以一起坐船
        int[] people1 = {1, 2};
        System.out.println("测试用例1结果: " + numRescueBoats(people1, 3)); // 期望输出: 1
        
        // 测试用例2
        // 输入: people = [3,2,2,1], limit = 3
        // 输出: 3
        // 解释: 3艘船分别载 (1, 2), (2) 和 (3)
        int[] people2 = {3, 2, 2, 1};
        System.out.println("测试用例2结果: " + numRescueBoats(people2, 3)); // 期望输出: 3
        
        // 测试用例3
        // 输入: people = [3,5,3,4], limit = 5
        // 输出: 4
        // 解释: 4艘船分别载 (3), (3), (4), (5)
        int[] people3 = {3, 5, 3, 4};
        System.out.println("测试用例3结果: " + numRescueBoats(people3, 5)); // 期望输出: 4
        
        // 测试用例4：边界情况
        // 输入: people = [1], limit = 1
        // 输出: 1
        int[] people4 = {1};
        System.out.println("测试用例4结果: " + numRescueBoats(people4, 1)); // 期望输出: 1
        
        // 测试用例5：复杂情况
        // 输入: people = [1,2,3,4,5], limit = 5
        // 输出: 3
        // 解释: 3艘船分别载 (1,4), (2,3), (5)
        int[] people5 = {1, 2, 3, 4, 5};
        System.out.println("测试用例5结果: " + numRescueBoats(people5, 5)); // 期望输出: 3
        
        // 测试用例6：极限情况
        // 输入: people = [3,2,3,2,2], limit = 6
        // 输出: 3
        // 解释: 3艘船分别载 (2,2), (2,3), (3)
        int[] people6 = {3, 2, 3, 2, 2};
        System.out.println("测试用例6结果: " + numRescueBoats(people6, 6)); // 期望输出: 3
    }
}