package class090;

import java.util.Arrays;

// 根据身高重建队列
// 假设有打乱顺序的一群人站成一个队列，数组 people 表示队列中一些人的属性（不一定按顺序）。
// 每个 people[i] = [hi, ki] 表示第 i 个人的身高为 hi ，前面正好有 ki 个身高大于或等于 hi 的人。
// 请你重新构造并返回输入数组 people 所表示的队列。
// 返回的队列应该格式化为数组 queue ，其中 queue[j] = [hj, kj] 是队列中第 j 个人的属性（queue[0] 是排在队列前面的人）。
// 测试链接: https://leetcode.cn/problems/queue-reconstruction-by-height/
public class Code16_QueueReconstructionByHeight {

    /**
     * 根据身高重建队列问题的贪心解法
     * 
     * 解题思路：
     * 1. 将人们按身高降序排序，身高相同时按k值升序排序
     * 2. 贪心策略：先安排身高高的人，再安排身高矮的人
     * 3. 对于身高相同的人，按k值升序排列，确保k值小的先安排
     * 4. 遍历排序后的人群，将每个人插入到结果列表的指定位置
     * 
     * 贪心策略的正确性：
     * 1. 身高高的人看不到身高矮的人，所以先安排身高高的人不会影响后续安排
     * 2. 对于身高相同的人，k值小的应该排在前面
     * 3. 当我们将一个人插入到结果列表的第k个位置时，前面正好有k个人身高大于等于他
     * 
     * 时间复杂度：O(n^2)，排序需要O(n log n)，插入操作需要O(n)
     * 空间复杂度：O(n)，需要额外的结果列表空间
     * 
     * @param people 人群属性数组
     * @return 重建后的队列
     */
    public static int[][] reconstructQueue(int[][] people) {
        // 边界条件处理：如果人群数组为空，则返回空数组
        if (people == null || people.length == 0) {
            return new int[0][0];
        }

        // 1. 按身高降序排序，身高相同时按k值升序排序
        Arrays.sort(people, (a, b) -> {
            if (a[0] == b[0]) {
                return a[1] - b[1];  // 身高相同时按k值升序
            }
            return b[0] - a[0];      // 按身高降序
        });

        // 2. 初始化结果列表
        int[][] result = new int[people.length][2];

        // 3. 遍历排序后的人群
        for (int i = 0; i < people.length; i++) {
            // 4. 将当前人插入到结果列表的指定位置
            // 由于我们是按身高降序处理的，所以前面的人身高都大于等于当前人
            // 将当前人插入到第k个位置，前面正好有k个人身高大于等于他
            int pos = people[i][1];
            
            // 5. 将当前位置及之后的元素后移一位
            for (int j = i; j > pos; j--) {
                result[j] = result[j - 1];
            }
            
            // 6. 在指定位置插入当前人
            result[pos] = people[i];
        }

        // 7. 返回重建后的队列
        return result;
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        // 输入: people = [[7,0],[4,4],[7,1],[5,0],[6,1],[5,2]]
        // 输出: [[5,0],[7,0],[5,2],[6,1],[4,4],[7,1]]
        int[][] people1 = {{7, 0}, {4, 4}, {7, 1}, {5, 0}, {6, 1}, {5, 2}};
        int[][] result1 = reconstructQueue(people1);
        System.out.print("测试用例1结果: ");
        for (int[] person : result1) {
            System.out.print("[" + person[0] + "," + person[1] + "] ");
        }
        System.out.println(); // 期望输出: [5,0] [7,0] [5,2] [6,1] [4,4] [7,1]

        // 测试用例2
        // 输入: people = [[6,0],[5,0],[4,0],[3,2],[2,2],[1,4]]
        // 输出: [[4,0],[5,0],[2,2],[3,2],[1,4],[6,0]]
        int[][] people2 = {{6, 0}, {5, 0}, {4, 0}, {3, 2}, {2, 2}, {1, 4}};
        int[][] result2 = reconstructQueue(people2);
        System.out.print("测试用例2结果: ");
        for (int[] person : result2) {
            System.out.print("[" + person[0] + "," + person[1] + "] ");
        }
        System.out.println(); // 期望输出: [4,0] [5,0] [2,2] [3,2] [1,4] [6,0]

        // 测试用例3：边界情况
        // 输入: people = [[1,0]]
        // 输出: [[1,0]]
        int[][] people3 = {{1, 0}};
        int[][] result3 = reconstructQueue(people3);
        System.out.print("测试用例3结果: ");
        for (int[] person : result3) {
            System.out.print("[" + person[0] + "," + person[1] + "] ");
        }
        System.out.println(); // 期望输出: [1,0]
    }
}