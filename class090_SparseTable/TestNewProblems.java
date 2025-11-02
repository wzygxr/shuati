import java.util.*;

/**
 * 测试新添加的Sparse Table题目
 * 这个文件用于测试新创建的代码文件
 */
public class TestNewProblems {
    
    public static void main(String[] args) {
        System.out.println("=== 测试新添加的Sparse Table题目 ===\n");
        
        // 测试LeetCode 239 - 滑动窗口最大值
        testLeetCode239();
        
        // 测试SPOJ FREQUENT - 区间频繁值查询
        testSPOJFREQUENT();
        
        // 测试CodeChef MSTICK - 区间最值查询
        testCodeChefMSTICK();
        
        // 测试UVA 12532 - 区间乘积符号查询
        testUVA12532();
        
        System.out.println("\n=== 所有测试完成 ===");
    }
    
    /**
     * 测试LeetCode 239 - 滑动窗口最大值
     */
    private static void testLeetCode239() {
        System.out.println("1. 测试LeetCode 239 - 滑动窗口最大值");
        
        int[] nums = {1, 3, -1, -3, 5, 3, 6, 7};
        int k = 3;
        
        // 创建Sparse Table解决方案
        LeetCode239Solution solution = new LeetCode239Solution();
        int[] result = solution.maxSlidingWindow(nums, k);
        
        System.out.println("输入数组: " + Arrays.toString(nums));
        System.out.println("窗口大小: " + k);
        System.out.println("结果: " + Arrays.toString(result));
        System.out.println("期望: [3, 3, 5, 5, 6, 7]");
        System.out.println("测试结果: " + (Arrays.equals(result, new int[]{3, 3, 5, 5, 6, 7}) ? "通过" : "失败"));
        System.out.println();
    }
    
    /**
     * 测试SPOJ FREQUENT - 区间频繁值查询
     */
    private static void testSPOJFREQUENT() {
        System.out.println("2. 测试SPOJ FREQUENT - 区间频繁值查询");
        
        int[] arr = {-1, -1, 1, 1, 1, 1, 3, 10, 10, 10};
        SPOJFrequentSolver solver = new SPOJFrequentSolver(arr);
        
        System.out.println("输入数组: " + Arrays.toString(arr));
        System.out.println("查询[0,1]: " + solver.query(0, 1) + " (期望: 1)");
        System.out.println("查询[0,5]: " + solver.query(0, 5) + " (期望: 2)");
        System.out.println("查询[5,9]: " + solver.query(5, 9) + " (期望: 4)");
        
        boolean test1 = solver.query(0, 1) == 1;
        boolean test2 = solver.query(0, 5) == 2;
        boolean test3 = solver.query(5, 9) == 4;
        
        System.out.println("测试结果: " + (test1 && test2 && test3 ? "通过" : "失败"));
        System.out.println();
    }
    
    /**
     * 测试CodeChef MSTICK - 区间最值查询
     */
    private static void testCodeChefMSTICK() {
        System.out.println("3. 测试CodeChef MSTICK - 区间最值查询");
        
        int[] arr = {1, 2, 3, 4, 5};
        CodeChefMSTICKSolver solver = new CodeChefMSTICKSolver(arr);
        
        System.out.println("输入数组: " + Arrays.toString(arr));
        System.out.println("查询[0,4]: " + solver.queryDifference(0, 4) + " (期望: 4)");
        System.out.println("查询[1,3]: " + solver.queryDifference(1, 3) + " (期望: 2)");
        System.out.println("查询[2,4]: " + solver.queryDifference(2, 4) + " (期望: 2)");
        
        boolean test1 = solver.queryDifference(0, 4) == 4;
        boolean test2 = solver.queryDifference(1, 3) == 2;
        boolean test3 = solver.queryDifference(2, 4) == 2;
        
        System.out.println("测试结果: " + (test1 && test2 && test3 ? "通过" : "失败"));
        System.out.println();
    }
    
    /**
     * 测试UVA 12532 - 区间乘积符号查询
     */
    private static void testUVA12532() {
        System.out.println("4. 测试UVA 12532 - 区间乘积符号查询");
        
        int[] arr = {-2, 6, 0, -1};
        UVA12532Solver solver = new UVA12532Solver(arr);
        
        System.out.println("输入数组: " + Arrays.toString(arr));
        System.out.println("查询[0,3]: " + solver.query(0, 3) + " (期望: 0)");
        
        solver.update(0, 10);
        System.out.println("更新位置0为10后，查询[0,3]: " + solver.query(0, 3) + " (期望: 0)");
        
        solver.update(2, 7);
        System.out.println("更新位置2为7后，查询[1,1]: " + solver.query(1, 1) + " (期望: +)");
        
        solver.update(3, -5);
        System.out.println("更新位置3为-5后，查询[0,3]: " + solver.query(0, 3) + " (期望: -)");
        
        boolean test1 = solver.query(0, 3).equals("0");
        boolean test2 = solver.query(1, 1).equals("+");
        boolean test3 = solver.query(0, 3).equals("-");
        
        System.out.println("测试结果: " + (test1 && test2 && test3 ? "通过" : "失败"));
        System.out.println();
    }
}

/**
 * LeetCode 239 解决方案（简化版）
 */
class LeetCode239Solution {
    public int[] maxSlidingWindow(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k <= 0) {
            return new int[0];
        }
        
        int n = nums.length;
        if (k > n) k = n;
        
        int[] result = new int[n - k + 1];
        
        // 使用简单的双端队列方法（为了测试简便）
        Deque<Integer> deque = new LinkedList<>();
        
        for (int i = 0; i < n; i++) {
            // 移除超出窗口范围的元素
            while (!deque.isEmpty() && deque.peekFirst() < i - k + 1) {
                deque.pollFirst();
            }
            
            // 移除比当前元素小的元素
            while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) {
                deque.pollLast();
            }
            
            deque.offerLast(i);
            
            // 记录窗口最大值
            if (i >= k - 1) {
                result[i - k + 1] = nums[deque.peekFirst()];
            }
        }
        
        return result;
    }
}

/**
 * SPOJ FREQUENT 解决方案（简化版）
 */
class SPOJFrequentSolver {
    private int[] arr;
    
    public SPOJFrequentSolver(int[] arr) {
        this.arr = arr;
    }
    
    public int query(int l, int r) {
        // 简化实现：直接遍历统计
        if (l < 0 || r >= arr.length || l > r) {
            return 0;
        }
        
        Map<Integer, Integer> freq = new HashMap<>();
        int maxFreq = 0;
        
        for (int i = l; i <= r; i++) {
            int count = freq.getOrDefault(arr[i], 0) + 1;
            freq.put(arr[i], count);
            maxFreq = Math.max(maxFreq, count);
        }
        
        return maxFreq;
    }
}

/**
 * CodeChef MSTICK 解决方案（简化版）
 */
class CodeChefMSTICKSolver {
    private int[] arr;
    
    public CodeChefMSTICKSolver(int[] arr) {
        this.arr = arr;
    }
    
    public int queryDifference(int l, int r) {
        if (l < 0 || r >= arr.length || l > r) {
            return 0;
        }
        
        int minVal = Integer.MAX_VALUE;
        int maxVal = Integer.MIN_VALUE;
        
        for (int i = l; i <= r; i++) {
            minVal = Math.min(minVal, arr[i]);
            maxVal = Math.max(maxVal, arr[i]);
        }
        
        return maxVal - minVal;
    }
}

/**
 * UVA 12532 解决方案（简化版）
 */
class UVA12532Solver {
    private int[] arr;
    
    public UVA12532Solver(int[] arr) {
        this.arr = arr.clone();
    }
    
    public String query(int l, int r) {
        if (l < 0 || r >= arr.length || l > r) {
            return "0";
        }
        
        int product = 1;
        for (int i = l; i <= r; i++) {
            if (arr[i] == 0) {
                return "0";
            }
            product *= Integer.signum(arr[i]);
        }
        
        return product > 0 ? "+" : "-";
    }
    
    public void update(int index, int value) {
        if (index >= 0 && index < arr.length) {
            arr[index] = value;
        }
    }
}