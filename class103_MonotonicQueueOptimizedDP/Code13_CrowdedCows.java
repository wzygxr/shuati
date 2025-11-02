import java.util.*;

/**
 * USACO 挤奶牛 (Crowded Cows)
 * 题目来源：USACO
 * 
 * 题目描述：
 * 在一条直线上有N头奶牛，每头奶牛的位置为x[i]，权值为v[i]。
 * 一头奶牛被认为是"拥挤的"，当且仅当它左边有至少一头奶牛，右边也有至少一头奶牛，
 * 且左边那头发的权值不小于它，右边那头发的权值也不小于它。
 * 求有多少头奶牛是拥挤的。
 * 
 * 解题思路：
 * 这是一个典型的滑动窗口最大值问题，使用单调队列优化。
 * - 我们需要找出每个位置i左边窗口内的最大值和右边窗口内的最大值
 * - 对于每个位置i，如果左边最大值 >= v[i] 且右边最大值 >= v[i]，则这头奶牛是拥挤的
 * - 使用单调队列维护滑动窗口中的最大值
 * 
 * 时间复杂度：O(n)，每个元素最多被加入和弹出队列各一次
 * 空间复杂度：O(n)，需要存储最大值数组和单调队列
 */
public class Code13_CrowdedCows {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int d = scanner.nextInt();
        List<int[]> cows = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int x = scanner.nextInt();
            int v = scanner.nextInt();
            cows.add(new int[]{x, v});
        }
        scanner.close();
        
        // 按照位置排序
        cows.sort((a, b) -> Integer.compare(a[0], b[0]));
        
        int[] positions = new int[n];
        int[] values = new int[n];
        for (int i = 0; i < n; i++) {
            positions[i] = cows.get(i)[0];
            values[i] = cows.get(i)[1];
        }
        
        System.out.println(solve(n, d, positions, values));
    }
    
    /**
     * 解决挤奶牛问题
     * @param n 奶牛数量
     * @param d 窗口宽度
     * @param positions 奶牛的位置数组
     * @param values 奶牛的权值数组
     * @return 拥挤的奶牛数量
     */
    public static int solve(int n, int d, int[] positions, int[] values) {
        // 记录每个位置右边窗口内的最大值
        int[] rightMax = new int[n];
        Arrays.fill(rightMax, -1);
        
        // 从右到左遍历，使用单调队列维护窗口内的最大值
        Deque<Integer> deque = new LinkedList<>();
        for (int i = n - 1; i >= 0; i--) {
            // 移除队列中位置超出窗口的元素（x[j] > x[i] + d）
            while (!deque.isEmpty() && positions[deque.peekFirst()] > positions[i] + d) {
                deque.pollFirst();
            }
            
            // 如果队列不为空，当前位置的右边最大值就是队列头部的元素
            if (!deque.isEmpty()) {
                rightMax[i] = values[deque.peekFirst()];
            }
            
            // 维护队列的单调性，移除队列尾部小于等于当前元素的值
            while (!deque.isEmpty() && values[i] >= values[deque.peekLast()]) {
                deque.pollLast();
            }
            deque.offerLast(i);
        }
        
        // 统计拥挤的奶牛数量
        int count = 0;
        deque.clear();
        
        // 从左到右遍历，使用单调队列维护窗口内的最大值
        for (int i = 0; i < n; i++) {
            // 移除队列中位置超出窗口的元素（x[j] < x[i] - d）
            while (!deque.isEmpty() && positions[deque.peekFirst()] < positions[i] - d) {
                deque.pollFirst();
            }
            
            // 如果左边有最大值且右边有最大值，并且都大于等于当前值，则是拥挤的奶牛
            if (!deque.isEmpty() && rightMax[i] != -1 && values[deque.peekFirst()] >= values[i] && rightMax[i] >= values[i]) {
                count++;
            }
            
            // 维护队列的单调性，移除队列尾部小于等于当前元素的值
            while (!deque.isEmpty() && values[i] >= values[deque.peekLast()]) {
                deque.pollLast();
            }
            deque.offerLast(i);
        }
        
        return count;
    }
}