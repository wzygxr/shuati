package class091;

import java.util.HashMap;
import java.util.Map;

// 森林中的兔子
// 森林中有未知数量的兔子。提问其中若干只兔子 "还有多少只兔子与你（指被提问的兔子）颜色相同?" ，
// 收集回答，将答案存入数组 answers 中。
// 给你数组 answers ，返回森林中兔子的最少数量。
// 测试链接 : https://leetcode.cn/problems/rabbits-in-forest/
public class Code26_RabbitsInForest {

    /**
     * 森林中的兔子问题
     * 
     * 算法思路：
     * 使用贪心策略结合哈希表：
     * 1. 对于每个回答x，意味着该兔子所在的组至少有x+1只兔子（包括自己）
     * 2. 如果有多个兔子回答相同的x，我们可以假设它们可能属于同一组，但最多只能有x+1只兔子属于同一组
     * 3. 对于回答x的cnt只兔子，需要的组数为：Math.ceil(cnt / (x + 1))
     * 4. 每组需要x+1只兔子，所以总数量为：组数 * (x + 1)
     * 
     * 正确性分析：
     * 1. 为了最小化兔子的数量，我们应该尽可能让回答相同x的兔子属于同一组
     * 2. 但是每组最多只能有x+1只兔子回答相同的x，否则必然有不同颜色的兔子
     * 3. 通过向上取整计算组数，可以保证同一组内的兔子不会超过x+1只
     * 
     * 时间复杂度：O(n) - 其中n是数组answers的长度，需要遍历数组统计每个回答的出现次数
     * 空间复杂度：O(n) - 最坏情况下，每个回答都不同，需要存储所有不同的回答
     * 
     * @param answers 兔子的回答数组
     * @return 森林中兔子的最少数量
     */
    public static int numRabbits(int[] answers) {
        // 边界检查
        if (answers == null || answers.length == 0) {
            return 0;
        }
        
        // 使用哈希表统计每个回答的出现次数
        Map<Integer, Integer> countMap = new HashMap<>();
        for (int answer : answers) {
            countMap.put(answer, countMap.getOrDefault(answer, 0) + 1);
        }
        
        int minRabbits = 0;  // 兔子的最少数量
        
        // 计算每组需要的兔子数量
        for (Map.Entry<Integer, Integer> entry : countMap.entrySet()) {
            int x = entry.getKey();      // 回答的数值
            int cnt = entry.getValue();  // 回答x的兔子数量
            
            // 计算需要的组数：向上取整(cnt / (x + 1))
            int groups = (cnt + x) / (x + 1);
            
            // 每组需要x+1只兔子
            minRabbits += groups * (x + 1);
        }
        
        return minRabbits;
    }

    // 测试用例
    public static void main(String[] args) {
        // 测试用例1: answers = [1,1,2] -> 输出: 5
        // 解释:
        // - 两只回答1的兔子可能属于同一颜色，需要2只兔子
        // - 一只回答2的兔子需要3只兔子（包括自己）
        // 总共: 2 + 3 = 5
        int[] answers1 = {1, 1, 2};
        System.out.println("测试用例1:");
        System.out.print("回答数组: ");
        for (int ans : answers1) {
            System.out.print(ans + " ");
        }
        System.out.println();
        System.out.println("最少兔子数量: " + numRabbits(answers1)); // 期望输出: 5
        
        // 测试用例2: answers = [10,10,10] -> 输出: 11
        // 解释: 三只回答10的兔子可能属于同一颜色，需要11只兔子
        int[] answers2 = {10, 10, 10};
        System.out.println("\n测试用例2:");
        System.out.print("回答数组: ");
        for (int ans : answers2) {
            System.out.print(ans + " ");
        }
        System.out.println();
        System.out.println("最少兔子数量: " + numRabbits(answers2)); // 期望输出: 11
        
        // 测试用例3: answers = [] -> 输出: 0
        int[] answers3 = {};
        System.out.println("\n测试用例3:");
        System.out.print("回答数组: []");
        System.out.println();
        System.out.println("最少兔子数量: " + numRabbits(answers3)); // 期望输出: 0
    }
}