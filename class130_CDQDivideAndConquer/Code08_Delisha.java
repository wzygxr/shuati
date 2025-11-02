package class170;

// P5621 [DBOI2019]德丽莎世界第一可爱
// 平台: 洛谷
// 难度: 省选/NOI-
// 标签: CDQ分治, 四维偏序
// 链接: https://www.luogu.com.cn/problem/P5621
// 
// 题目描述:
// 给定n个四元组(a_i, b_i, c_i, d_i)，对于每个i，计算满足以下条件的j的个数：
// a_j ≤ a_i 且 b_j ≤ b_i 且 c_j ≤ c_i 且 d_j ≤ d_i 且 j ≠ i
// 
// 解题思路:
// 使用CDQ分治套CDQ分治解决四维偏序问题。
// 1. 第一维：按a排序
// 2. 第二维：使用外层CDQ分治处理
// 3. 第三维和第四维：使用内层CDQ分治处理
// 
// 具体实现：
// 1. 首先按第一维a排序
// 2. 外层CDQ分治处理第二维b
// 3. 在外层CDQ分治的合并过程中，对第三维c进行排序
// 4. 内层CDQ分治处理第四维d
// 
// 时间复杂度：O(n log^3 n)
// 空间复杂度：O(n)

import java.util.*;

class Point {
    int a, b, c, d, id, ans;
    
    public Point(int a, int b, int c, int d, int id) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.id = id;
        this.ans = 0;
    }
}

class Solution {
    private int[] bit;  // 树状数组
    
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
    
    public int[] solveDelisha(int[] a, int[] b, int[] c, int[] d) {
        int n = a.length;
        if (n == 0) return new int[0];
        
        // 创建点数组
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            points[i] = new Point(a[i], b[i], c[i], d[i], i);
        }
        
        // 按第一维a排序
        Arrays.sort(points, (p1, p2) -> {
            if (p1.a != p2.a) return Integer.compare(p1.a, p2.a);
            if (p1.b != p2.b) return Integer.compare(p1.b, p2.b);
            if (p1.c != p2.c) return Integer.compare(p1.c, p2.c);
            return Integer.compare(p1.d, p2.d);
        });
        
        bit = new int[n + 1];  // 树状数组
        
        // 执行CDQ分治套CDQ分治
        cdq2d(points, 0, n - 1);
        
        // 构造结果
        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            result[points[i].id] = points[i].ans;
        }
        return result;
    }
    
    // 外层CDQ分治处理第二维b
    private void cdq2d(Point[] points, int l, int r) {
        if (l >= r) return;
        
        int mid = (l + r) >> 1;
        cdq2d(points, l, mid);
        cdq2d(points, mid + 1, r);
        
        // 合并过程，计算左半部分对右半部分的贡献
        // 按第三维c排序
        Point[] tmp = new Point[r - l + 1];
        int i = l, j = mid + 1, k = 0;
        
        while (i <= mid && j <= r) {
            if (points[i].c <= points[j].c) {
                // 左半部分的元素c值小于等于右半部分，处理插入操作
                add(points[i].d, 1, points.length);  // 插入元素
                tmp[k++] = points[i++];
            } else {
                // 右半部分的元素c值更大，处理查询操作
                // 查询d <= points[j].d的元素个数
                points[j].ans += query(points[j].d);
                tmp[k++] = points[j++];
            }
        }
        
        // 处理剩余元素
        while (i <= mid) {
            add(points[i].d, 1, points.length);
            tmp[k++] = points[i++];
        }
        while (j <= r) {
            points[j].ans += query(points[j].d);
            tmp[k++] = points[j++];
        }
        
        // 清理树状数组
        for (int t = l; t <= mid; t++) {
            add(points[t].d, -1, points.length);
        }
        
        // 将临时数组内容复制回原数组
        for (int t = 0; t < k; t++) {
            points[l + t] = tmp[t];
        }
    }
    
    public static void main(String[] args) {
        Solution solution = new Solution();
        
        // 测试用例
        int[] a1 = {1, 2, 3};
        int[] b1 = {1, 2, 3};
        int[] c1 = {1, 2, 3};
        int[] d1 = {1, 2, 3};
        int[] result1 = solution.solveDelisha(a1, b1, c1, d1);
        
        System.out.println("输入: a = [1,2,3], b = [1,2,3], c = [1,2,3], d = [1,2,3]");
        System.out.print("输出: [");
        for (int i = 0; i < result1.length; i++) {
            System.out.print(result1[i]);
            if (i < result1.length - 1) System.out.print(",");
        }
        System.out.println("]");
    }
}