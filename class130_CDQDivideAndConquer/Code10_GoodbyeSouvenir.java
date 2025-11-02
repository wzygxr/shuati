package class170;

// CF848C Goodbye Souvenir
// 平台: Codeforces
// 难度: 2600
// 标签: CDQ分治, 二维数点
// 链接: https://codeforces.com/problemset/problem/848/C
// 
// 题目描述:
// 给定一个长度为n的序列a，有两种操作：
// 1. 1 x y：将a_x修改为y
// 2. 2 l r：查询区间[l,r]中所有相同元素的最大跨度之和
// 最大跨度定义为：对于值为v的元素，如果它在区间中出现的位置是i1,i2,...,ik，
// 那么它的跨度是ik-i1，所有值的跨度之和就是答案。
// 
// 解题思路:
// 使用CDQ分治解决时间维度的三维偏序问题。
// 1. 第一维：时间（操作顺序）
// 2. 第二维：位置
// 3. 第三维：值
// 
// 我们将每个修改操作和查询操作都看作事件，然后使用CDQ分治来处理。
// 对于每个值，我们维护它之前出现的位置，这样可以将跨度计算转化为二维数点问题。
// 
// 时间复杂度：O(n log^2 n)
// 空间复杂度：O(n)

import java.util.*;

class Event {
    int type, time, pos, val, l, r, id; // type: 0表示修改，1表示查询
    
    public Event(int type, int time, int pos, int val, int l, int r, int id) {
        this.type = type;
        this.time = time;
        this.pos = pos;
        this.val = val;
        this.l = l;
        this.r = r;
        this.id = id;
    }
}

class Solution {
    private long[] bit;  // 树状数组
    private long[] ans;  // 答案数组
    
    // 树状数组操作
    private int lowbit(int x) {
        return x & (-x);
    }
    
    private void add(int x, long v, int n) {
        for (int i = x; i <= n; i += lowbit(i)) {
            bit[i] += v;
        }
    }
    
    private long query(int x) {
        long res = 0;
        for (int i = x; i > 0; i -= lowbit(i)) {
            res += bit[i];
        }
        return res;
    }
    
    public long[] solveGoodbyeSouvenir(int[] a, int[][] operations) {
        int n = a.length;
        int m = operations.length;
        if (m == 0) return new long[0];
        
        // 创建事件数组
        List<Event> events = new ArrayList<>();
        int time = 0;
        
        // 初始数组元素作为修改事件
        for (int i = 0; i < n; i++) {
            events.add(new Event(0, time++, i, a[i], 0, 0, 0));
        }
        
        // 处理操作
        ans = new long[m];
        for (int i = 0; i < m; i++) {
            if (operations[i][0] == 1) {
                // 修改操作
                int x = operations[i][1] - 1; // 转换为0索引
                int y = operations[i][2];
                events.add(new Event(0, time++, x, y, 0, 0, 0));
            } else {
                // 查询操作
                int l = operations[i][1] - 1; // 转换为0索引
                int r = operations[i][2] - 1; // 转换为0索引
                events.add(new Event(1, time++, 0, 0, l, r, i));
            }
        }
        
        bit = new long[n + 1];  // 树状数组
        
        // 执行CDQ分治
        cdq(events, 0, events.size() - 1);
        
        return ans;
    }
    
    // CDQ分治主函数
    private void cdq(List<Event> events, int l, int r) {
        if (l >= r) return;
        
        int mid = (l + r) >> 1;
        cdq(events, l, mid);
        cdq(events, mid + 1, r);
        
        // 合并过程，计算左半部分对右半部分的贡献
        List<Event> left = new ArrayList<>();
        List<Event> right = new ArrayList<>();
        
        for (int i = l; i <= mid; i++) {
            if (events.get(i).type == 0) { // 修改事件
                left.add(events.get(i));
            }
        }
        
        for (int i = mid + 1; i <= r; i++) {
            if (events.get(i).type == 1) { // 查询事件
                right.add(events.get(i));
            }
        }
        
        // 按位置排序
        left.sort((a, b) -> Integer.compare(a.pos, b.pos));
        right.sort((a, b) -> Integer.compare(a.l, b.l));
        
        // 处理贡献
        int j = 0;
        for (Event e : right) {
            // 将位置小于等于e.l的修改事件加入树状数组
            while (j < left.size() && left.get(j).pos <= e.l) {
                add(left.get(j).pos + 1, left.get(j).val, bit.length - 1);
                j++;
            }
            
            // 查询位置在[e.l, e.r]范围内的元素和
            ans[e.id] += query(e.r + 1) - query(e.l);
        }
        
        // 清理树状数组
        for (int i = 0; i < j; i++) {
            add(left.get(i).pos + 1, -left.get(i).val, bit.length - 1);
        }
    }
    
    public static void main(String[] args) {
        Solution solution = new Solution();
        
        // 测试用例
        int[] a1 = {1, 2, 3};
        int[][] operations1 = {{2, 1, 3}};
        long[] result1 = solution.solveGoodbyeSouvenir(a1, operations1);
        
        System.out.println("输入: a = [1,2,3], operations = [[2,1,3]]");
        System.out.print("输出: [");
        for (int i = 0; i < result1.length; i++) {
            System.out.print(result1[i]);
            if (i < result1.length - 1) System.out.print(",");
        }
        System.out.println("]");
    }
}