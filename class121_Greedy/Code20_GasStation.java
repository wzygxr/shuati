package class090;

// 加油站
// 题目描述：在一条环路上有 n 个加油站，其中第 i 个加油站有汽油 gas[i] 升。
// 你有一辆油箱容量无限的汽车，从第 i 个加油站开往第 i+1 个加油站需要消耗汽油 cost[i] 升。
// 你从其中一个加油站出发，开始时油箱为空。
// 给定两个整数数组 gas 和 cost，如果你可以按顺序绕环路行驶一周，则返回出发时加油站的编号，否则返回 -1。
// 如果存在解，则保证它是唯一的。
// 测试链接: https://leetcode.cn/problems/gas-station/
public class Code20_GasStation {

    /**
     * 加油站问题的贪心解法
     * 
     * 解题思路：
     * 1. 如果总的汽油量小于总的消耗量，那么无论从哪里出发，都不可能绕环路一周
     * 2. 从起点开始遍历，如果当前累计的剩余油量为负数，说明从起点到当前位置的路径不可行
     *    需要将起点更新为当前位置的下一个位置，并重新计算累计剩余油量
     * 3. 最终，如果总油量大于等于总消耗，返回找到的起点，否则返回-1
     * 
     * 贪心策略的正确性：
     * - 如果从站点A出发，在到达站点B之前就没有油了，那么从A和B之间的任何一个站点出发都不可能到达B
     * - 因为如果在A和B之间有一个站点C，从C出发能到达B，那么从A出发也能到达B（先到C，再到B）
     * - 这与假设矛盾，所以如果从A出发无法到达B，那么从A和B之间的任何站点出发都无法到达B
     * 
     * 时间复杂度：O(n)，其中n是加油站的数量，只需要遍历数组一次
     * 
     * 空间复杂度：O(1)，只使用了常数个额外变量
     * 
     * @param gas 各加油站的汽油量数组
     * @param cost 各段路程的消耗量数组
     * @return 如果能绕环路行驶一周，返回出发时加油站的编号；否则返回-1
     */
    public static int canCompleteCircuit(int[] gas, int[] cost) {
        // 边界条件处理
        if (gas == null || cost == null || gas.length != cost.length) {
            return -1; // 参数无效
        }

        int n = gas.length;
        int totalGas = 0;      // 总汽油量
        int totalCost = 0;     // 总消耗量
        int currentTank = 0;   // 当前油箱中的汽油量
        int startStation = 0;  // 起始加油站

        // 遍历所有加油站
        for (int i = 0; i < n; i++) {
            // 累加总汽油量和总消耗量
            totalGas += gas[i];
            totalCost += cost[i];

            // 计算当前的剩余油量
            currentTank += gas[i] - cost[i];

            // 如果当前剩余油量为负数，说明从startStation到i的路径不可行
            if (currentTank < 0) {
                // 将起始站点更新为i+1
                startStation = i + 1;
                // 重置当前油箱
                currentTank = 0;
            }
        }

        // 如果总汽油量小于总消耗量，无解
        if (totalGas < totalCost) {
            return -1;
        }

        // 否则，返回找到的起始站点
        return startStation;
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        // 输入: gas = [1,2,3,4,5], cost = [3,4,5,1,2]
        // 输出: 3
        // 解释:
        // 从3号加油站(索引为3处)出发，可获得4升汽油。此时油箱有 = 0 + 4 = 4升汽油
        // 开往4号加油站，消耗1升汽油，到达时油箱有 4 - 1 = 3升汽油
        // 开往0号加油站，消耗2升汽油，到达时油箱有 3 - 2 = 1升汽油
        // 开往1号加油站，消耗3升汽油，到达时油箱有 1 - 3 = -2升汽油。这不行
        // 上面的计算有误，正确的计算应该是：
        // 从3号加油站出发，获得4升汽油，油箱=4
        // 到4号加油站，消耗1升，剩余3升，加上gas[4]=5升，总8升
        // 到0号加油站，消耗2升，剩余6升，加上gas[0]=1升，总7升
        // 到1号加油站，消耗3升，剩余4升，加上gas[1]=2升，总6升
        // 到2号加油站，消耗4升，剩余2升，加上gas[2]=3升，总5升
        // 回到3号加油站，消耗5升，剩余0升，完成一圈
        int[] gas1 = {1, 2, 3, 4, 5};
        int[] cost1 = {3, 4, 5, 1, 2};
        System.out.println("测试用例1结果: " + canCompleteCircuit(gas1, cost1)); // 期望输出: 3

        // 测试用例2
        // 输入: gas = [2,3,4], cost = [3,4,3]
        // 输出: -1
        // 解释:
        // 总汽油量2+3+4=9，总消耗量3+4+3=10，总油量小于总消耗，无解
        int[] gas2 = {2, 3, 4};
        int[] cost2 = {3, 4, 3};
        System.out.println("测试用例2结果: " + canCompleteCircuit(gas2, cost2)); // 期望输出: -1

        // 测试用例3：边界情况 - 只有一个加油站
        // 输入: gas = [5], cost = [4]
        // 输出: 0
        int[] gas3 = {5};
        int[] cost3 = {4};
        System.out.println("测试用例3结果: " + canCompleteCircuit(gas3, cost3)); // 期望输出: 0

        // 测试用例4：边界情况 - 无法到达的情况
        // 输入: gas = [5,1,2,3,4], cost = [4,4,1,5,1]
        // 输出: 4
        int[] gas4 = {5, 1, 2, 3, 4};
        int[] cost4 = {4, 4, 1, 5, 1};
        System.out.println("测试用例4结果: " + canCompleteCircuit(gas4, cost4)); // 期望输出: 4

        // 测试用例5：更复杂的情况
        // 输入: gas = [3,1,1], cost = [1,2,2]
        // 输出: 0
        // 总油量5，总消耗5，满足条件
        // 从0号加油站出发：
        // 获得3升，消耗1升，剩余2升
        // 获得1升，总3升，消耗2升，剩余1升
        // 获得1升，总2升，消耗2升，剩余0升，完成一圈
        int[] gas5 = {3, 1, 1};
        int[] cost5 = {1, 2, 2};
        System.out.println("测试用例5结果: " + canCompleteCircuit(gas5, cost5)); // 期望输出: 0
    }
}