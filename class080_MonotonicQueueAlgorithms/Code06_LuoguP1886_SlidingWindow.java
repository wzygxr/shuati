import java.io.*;
import java.util.*;

/**
 * 题目名称：洛谷 P1886 滑动窗口/【模板】单调队列
 * 题目来源：洛谷
 * 题目链接：https://www.luogu.com.cn/problem/P1886
 * 题目难度：中等
 * 
 * 题目描述：
 * 有一个长为 n 的序列 a，以及一个大小为 k 的窗口。
 * 现在这个窗口从左边开始向右滑动，每次滑动一个单位，求出每次滑动后窗口中的最小值和最大值。
 * 
 * 解题思路：
 * 这是单调队列的经典模板题。我们需要在O(n)时间内找到每个滑动窗口的最大值和最小值。
 * 使用两个单调队列：
 * 1. 单调递减队列：队首为窗口最大值
 * 2. 单调递增队列：队首为窗口最小值
 * 
 * 算法步骤：
 * 1. 使用双端队列维护窗口内元素的索引
 * 2. 维护一个单调递减队列求最大值
 * 3. 维持一个单调递增队列求最小值
 * 4. 每次窗口移动时更新两个队列并记录结果
 * 
 * 时间复杂度：O(n) - 每个元素最多入队和出队各两次
 * 空间复杂度：O(k) - 两个队列最多存储k个元素的索引
 * 
 * 是否为最优解：✅ 是，这是解决该问题的最优时间复杂度解法
 */

public class Code06_LuoguP1886_SlidingWindow {
    // 用于优化输入速度的缓冲区
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    
    public static int[] getMax(int[] arr, int n, int k) {
        /**
         * 单调递减队列求最大值
         * @param arr 输入数组
         * @param n 数组长度
         * @param k 窗口大小
         * @return 每个窗口的最大值数组
         */
        // 使用数组模拟双端队列，提高效率
        int[] dq = new int[n];
        int h = 0, t = 0; // 队列头指针和尾指针
        int[] result = new int[n - k + 1];
        
        for (int i = 0; i < n; i++) {
            // 移除队列中超出窗口范围的元素索引
            while (h < t && dq[h] <= i - k) {
                h++;
            }
            
            // 维护队列的单调递减性质
            while (h < t && arr[dq[t - 1]] <= arr[i]) {
                t--;
            }
            
            // 将当前元素索引入队
            dq[t++] = i;
            
            // 当窗口大小达到k时，记录窗口最大值（队首元素）
            if (i >= k - 1) {
                result[i - k + 1] = arr[dq[h]];
            }
        }
        
        return result;
    }
    
    public static int[] getMin(int[] arr, int n, int k) {
        /**
         * 单调递增队列求最小值
         * @param arr 输入数组
         * @param n 数组长度
         * @param k 窗口大小
         * @return 每个窗口的最小值数组
         */
        // 使用数组模拟双端队列，提高效率
        int[] dq = new int[n];
        int h = 0, t = 0; // 队列头指针和尾指针
        int[] result = new int[n - k + 1];
        
        for (int i = 0; i < n; i++) {
            // 移除队列中超出窗口范围的元素索引
            while (h < t && dq[h] <= i - k) {
                h++;
            }
            
            // 维护队列的单调递增性质
            while (h < t && arr[dq[t - 1]] >= arr[i]) {
                t--;
            }
            
            // 将当前元素索引入队
            dq[t++] = i;
            
            // 当窗口大小达到k时，记录窗口最小值（队首元素）
            if (i >= k - 1) {
                result[i - k + 1] = arr[dq[h]];
            }
        }
        
        return result;
    }
    
    public static void main(String[] args) throws IOException {
        // 读取输入
        String[] nk = br.readLine().split(" ");
        int n = Integer.parseInt(nk[0]);
        int k = Integer.parseInt(nk[1]);
        
        int[] arr = new int[n];
        String[] nums = br.readLine().split(" ");
        for (int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(nums[i]);
        }
        
        // 计算最大值和最小值
        int[] minValues = getMin(arr, n, k);
        int[] maxValues = getMax(arr, n, k);
        
        // 输出结果
        StringBuilder minSb = new StringBuilder();
        for (int val : minValues) {
            minSb.append(val).append(" ");
        }
        System.out.println(minSb.toString().trim());
        
        StringBuilder maxSb = new StringBuilder();
        for (int val : maxValues) {
            maxSb.append(val).append(" ");
        }
        System.out.println(maxSb.toString().trim());
        
        br.close();
    }
}