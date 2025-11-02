package class047;

/**
 * LeetCode 1094. 拼车 (Car Pooling)
 * 
 * 题目描述:
 * 假设你是一位顺风车司机，车上最初有 capacity 个空座位可以用来载客。
 * 由于道路拥堵，你只能向一个方向行驶。
 * 给定一个数组 trips，其中 trips[i] = [num_passengers, start, end]
 * 表示第 i 次旅行有 num_passengers 位乘客，接他们和放他们的位置分别是 start 和 end。
 * 这些位置是从你的初始位置向东的公里数。
 * 当且仅当你可以在所有给定的行程中接送所有乘客时，返回 true，否则返回 false。
 * 
 * 示例:
 * 输入: trips = [[2,1,5],[3,3,7]], capacity = 4
 * 输出: false
 * 
 * 输入: trips = [[2,1,5],[3,3,7]], capacity = 5
 * 输出: true
 * 
 * 提示:
 * 1 <= trips.length <= 1000
 * trips[i].length == 3
 * 1 <= num_passengersi <= 100
 * 0 <= fromi < toi <= 1000
 * 1 <= capacity <= 10^5
 * 
 * 题目链接: https://leetcode.com/problems/car-pooling/
 * 
 * 解题思路:
 * 使用差分数组技巧来处理区间更新操作。
 * 1. 创建一个差分数组diff，大小为1001（题目提示toi <= 1000）
 * 2. 对于每次旅行[passengers, start, end]，在差分数组中执行diff[start] += passengers和diff[end] -= passengers
 * 3. 对差分数组计算前缀和，得到每个位置的实际乘客数
 * 4. 检查是否有任何位置的乘客数超过capacity
 * 
 * 时间复杂度: O(n + m) - n是trips长度，m是位置范围(1001)
 * 空间复杂度: O(m) - 需要额外的差分数组空间
 * 
 * 这是最优解，因为需要处理所有行程，而且位置范围固定。
 */
public class Code06_CarPooling {

    /**
     * 判断是否可以完成所有行程
     * 
     * @param trips 行程数组，每个行程包含[乘客数, 起点, 终点]
     * @param capacity 车辆容量
     * @return 是否可以完成所有行程
     */
    public static boolean carPooling(int[][] trips, int capacity) {
        // 边界情况处理
        if (trips == null || trips.length == 0) {
            return true;
        }
        
        // 位置范围是0-1000，共1001个位置
        final int MAX_POSITION = 1001;
        
        // 创建差分数组
        int[] diff = new int[MAX_POSITION];
        
        // 处理每次行程
        for (int[] trip : trips) {
            int passengers = trip[0];  // 乘客数
            int start = trip[1];       // 起点
            int end = trip[2];         // 终点
            
            // 在起点增加乘客
            diff[start] += passengers;
            
            // 在终点减少乘客（乘客在此下车）
            diff[end] -= passengers;
        }
        
        // 通过计算差分数组的前缀和得到每个位置的实际乘客数
        int currentPassengers = 0;
        for (int i = 0; i < MAX_POSITION; i++) {
            currentPassengers += diff[i];
            
            // 如果任何位置的乘客数超过容量，返回false
            if (currentPassengers > capacity) {
                return false;
            }
        }
        
        return true;
    }

    /**
     * 测试用例
     */
    public static void main(String[] args) {
        // 测试用例1
        int[][] trips1 = {{2, 1, 5}, {3, 3, 7}};
        int capacity1 = 4;
        boolean result1 = carPooling(trips1, capacity1);
        // 预期输出: false
        System.out.println("测试用例1: " + result1);

        // 测试用例2
        int[][] trips2 = {{2, 1, 5}, {3, 3, 7}};
        int capacity2 = 5;
        boolean result2 = carPooling(trips2, capacity2);
        // 预期输出: true
        System.out.println("测试用例2: " + result2);
        
        // 测试用例3
        int[][] trips3 = {{2, 1, 5}, {3, 5, 7}};
        int capacity3 = 3;
        boolean result3 = carPooling(trips3, capacity3);
        // 预期输出: true
        System.out.println("测试用例3: " + result3);
        
        // 测试用例4
        int[][] trips4 = {{3, 2, 7}, {3, 7, 9}, {8, 3, 9}};
        int capacity4 = 11;
        boolean result4 = carPooling(trips4, capacity4);
        // 预期输出: true
        System.out.println("测试用例4: " + result4);
    }
}