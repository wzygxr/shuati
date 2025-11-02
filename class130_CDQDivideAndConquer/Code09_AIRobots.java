package class170;

// CF1045G AI robots
// 平台: Codeforces
// 难度: 2200
// 标签: CDQ分治, 二维数点
// 链接: https://codeforces.com/problemset/problem/1045/G
// 
// 题目描述:
// 有n个机器人，每个机器人有一个位置x_i，视野范围r_i和智商q_i。
// 机器人i和机器人j能够相互交流当且仅当：
// 1. 机器人i能看到机器人j（|x_i - x_j| <= r_i）
// 2. 机器人j能看到机器人i（|x_i - x_j| <= r_j）
// 3. 他们的智商差不超过K（|q_i - q_j| <= K）
// 求有多少对机器人能够相互交流。
// 
// 解题思路:
// 使用CDQ分治解决三维偏序问题。
// 1. 第一维：按视野范围r从大到小排序
// 2. 第二维：位置x
// 3. 第三维：智商q
// 
// 由于要求相互看见，我们按视野从大到小排序后，
// 只需考虑右边（视野小的）能否被左边（视野大的）看见。
// 
// 时间复杂度：O(n log^2 n)
// 空间复杂度：O(n)

import java.util.*;

class Robot {
    int x, r, q, id;
    
    public Robot(int x, int r, int q, int id) {
        this.x = x;
        this.r = r;
        this.q = q;
        this.id = id;
    }
}

class Solution {
    private int[] bit;  // 树状数组
    private int K;  // 智商差限制
    
    // 树状数组操作
    private int lowbit(int x) {
        return x & (-x);
    }
    
    private void add(int x, int v, int n) {
        for (int i = x; i <= n; i += lowbit(i)) {
            bit[i] += v;
        }
    }
    
    private int query(int x) {
        int res = 0;
        for (int i = x; i > 0; i -= lowbit(i)) {
            res += bit[i];
        }
        return res;
    }
    
    public int solveAIRobots(int[] x, int[] r, int[] q, int K) {
        int n = x.length;
        if (n == 0) return 0;
        
        this.K = K;
        
        // 创建机器人数组并按视野范围从大到小排序
        Robot[] robots = new Robot[n];
        for (int i = 0; i < n; i++) {
            robots[i] = new Robot(x[i], r[i], q[i], i);
        }
        
        Arrays.sort(robots, (a, b) -> {
            if (a.r != b.r) return Integer.compare(b.r, a.r); // 从大到小排序
            if (a.x != b.x) return Integer.compare(a.x, b.x);
            return Integer.compare(a.q, b.q);
        });
        
        // 离散化q值
        int[] sortedQ = q.clone();
        Arrays.sort(sortedQ);
        int uniqueSize = removeDuplicates(sortedQ);
        
        bit = new int[n + 1];  // 树状数组
        
        int result = 0;
        
        // 从左到右处理每个机器人
        for (int i = 0; i < n; i++) {
            int qId = Arrays.binarySearch(sortedQ, 0, uniqueSize, robots[i].q) + 1;
            if (qId < 0) qId = -qId;
            
            // 查询在当前位置左侧，且智商在[robots[i].q-K, robots[i].q+K]范围内的机器人数量
            int lowerBound = Arrays.binarySearch(sortedQ, 0, uniqueSize, robots[i].q - K);
            if (lowerBound < 0) lowerBound = -lowerBound - 1;
            lowerBound++;
            
            int upperBound = Arrays.binarySearch(sortedQ, 0, uniqueSize, robots[i].q + K);
            if (upperBound < 0) {
                upperBound = -upperBound - 1;
                // 找到第一个大于robots[i].q+K的位置
                while (upperBound < uniqueSize && sortedQ[upperBound] <= robots[i].q + K) {
                    upperBound++;
                }
            } else {
                // 找到第一个大于robots[i].q+K的位置
                while (upperBound < uniqueSize && sortedQ[upperBound] == robots[i].q + K) {
                    upperBound++;
                }
            }
            
            // 查询范围内的机器人数量
            result += query(upperBound - 1) - query(lowerBound - 1);
            
            // 将当前机器人插入到数据结构中
            add(qId, 1, n);
        }
        
        return result;
    }
    
    // 去重函数
    private int removeDuplicates(int[] nums) {
        if (nums.length == 0) return 0;
        int uniqueSize = 1;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] != nums[uniqueSize - 1]) {
                nums[uniqueSize++] = nums[i];
            }
        }
        return uniqueSize;
    }
    
    public static void main(String[] args) {
        Solution solution = new Solution();
        
        // 测试用例
        int[] x1 = {1, 2, 3};
        int[] r1 = {3, 2, 1};
        int[] q1 = {1, 2, 3};
        int K1 = 1;
        int result1 = solution.solveAIRobots(x1, r1, q1, K1);
        
        System.out.println("输入: x = [1,2,3], r = [3,2,1], q = [1,2,3], K = 1");
        System.out.println("输出: " + result1);
    }
}