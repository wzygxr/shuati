import java.util.*;

/**
 * LeetCode 1499 满足不等式的最大值
 * 题目来源：LeetCode
 * 网址：https://leetcode.cn/problems/max-value-of-equation/
 * 
 * 题目描述：
 * 给你一个数组 points 和一个整数 k。数组中每个元素 points[i] 表示第 i 个点的坐标，
 * 其中 points[i][0] 是 x 坐标，points[i][1] 是 y 坐标。
 * 
 * 要求找出满足 j > i 且 |x_j - x_i| <= k 的所有点对 (i, j)，
 * 并返回其中 equation y_i + y_j + |x_i - x_j| 的最大值。
 * 
 * 解题思路：
 * 观察等式：y_i + y_j + |x_i - x_j|
 * 由于输入是按x坐标递增排序的，所以对于j > i，有x_j >= x_i，因此|x_i - x_j| = x_j - x_i
 * 等式可以简化为：(y_i - x_i) + (y_j + x_j)
 * 
 * 对于每个j，我们需要找到在i < j且x_j - x_i <= k的条件下，最大的(y_i - x_i)
 * 这可以通过单调队列来维护滑动窗口内的最大值
 * 
 * 时间复杂度：O(n)，每个元素最多被加入和弹出队列各一次
 * 空间复杂度：O(n)，最坏情况下队列中存储所有元素
 */
public class Code15_MaxValueOfEquation {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int k = scanner.nextInt();
        int[][] points = new int[n][2];
        for (int i = 0; i < n; i++) {
            points[i][0] = scanner.nextInt();
            points[i][1] = scanner.nextInt();
        }
        scanner.close();
        
        System.out.println(findMaxValueOfEquation(points, k));
    }
    
    /**
     * 找出满足条件的点对的最大等式值
     * @param points 点坐标数组
     * @param k 距离限制
     * @return 最大等式值
     */
    public static int findMaxValueOfEquation(int[][] points, int k) {
        int maxValue = Integer.MIN_VALUE;
        // 单调队列，存储的是索引，按照(y_i - x_i)单调递减排序
        Deque<Integer> deque = new LinkedList<>();
        
        for (int j = 0; j < points.length; j++) {
            int xj = points[j][0];
            int yj = points[j][1];
            
            // 移除不满足xj - xi <= k的元素
            while (!deque.isEmpty() && xj - points[deque.peekFirst()][0] > k) {
                deque.pollFirst();
            }
            
            // 如果队列不为空，计算当前的最大值
            if (!deque.isEmpty()) {
                int i = deque.peekFirst();
                maxValue = Math.max(maxValue, (yj + xj) + (points[i][1] - points[i][0]));
            }
            
            // 维护队列的单调性，确保队列中的元素按照(y_i - x_i)单调递减
            while (!deque.isEmpty() && (points[j][1] - xj) >= (points[deque.peekLast()][1] - points[deque.peekLast()][0])) {
                deque.pollLast();
            }
            
            deque.offerLast(j);
        }
        
        return maxValue;
    }
}