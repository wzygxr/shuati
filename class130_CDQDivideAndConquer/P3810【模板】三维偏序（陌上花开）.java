package class170;

// P3810 【模板】三维偏序（陌上花开）
// 平台: 洛谷
// 难度: 提高+/省选-
// 标签: CDQ分治, 三维偏序
// 链接: https://www.luogu.com.cn/problem/P3810
// 
// 题目描述:
// 一共有n个对象，属性值范围[1, k]，每个对象有a属性、b属性、c属性
// f(i)表示，aj <= ai 且 bj <= bi 且 cj <= ci 且 j != i 的j的数量
// ans(d)表示，f(i) == d 的i的数量
// 打印所有的ans[d]，d的范围[0, n)
// 
// 示例:
// 输入:
// 10 3
// 3 3 3
// 2 3 3
// 2 3 1
// 3 1 1
// 3 1 2
// 1 3 1
// 1 1 2
// 1 3 3
// 1 1 3
// 1 3 2
// 
// 输出:
// 3
// 1
// 3
// 0
// 0
// 0
// 0
// 0
// 0
// 0
// 
// 解题思路:
// 使用CDQ分治解决三维偏序问题。这是CDQ分治的经典应用。
// 
// 1. 第一维：a属性，通过排序处理
// 2. 第二维：b属性，通过CDQ分治处理
// 3. 第三维：c属性，通过树状数组处理
// 
// 具体步骤：
// 1. 按照a属性排序，相同a的按b排序，相同b的按c排序
// 2. CDQ分治处理b属性
// 3. 在分治的合并过程中，使用双指针处理b属性的大小关系，用树状数组维护c属性的前缀和
// 
// 时间复杂度：O(n log^2 n)
// 空间复杂度：O(n)

import java.io.*;
import java.util.*;

class ObjectP3810 {
    int id, a, b, c, f;
    
    public ObjectP3810(int id, int a, int b, int c) {
        this.id = id;
        this.a = a;
        this.b = b;
        this.c = c;
        this.f = 0;
    }
}

public class P3810_模板_三维偏序_陌上花开 {
    private static int[] tree;  // 树状数组
    private static int[] ans;   // 答案数组
    private static ObjectP3810[] objects;
    
    // 树状数组操作
    private static int lowbit(int x) {
        return x & (-x);
    }
    
    private static void add(int x, int v, int k) {
        for (int i = x; i <= k; i += lowbit(i)) {
            tree[i] += v;
        }
    }
    
    private static int query(int x) {
        int res = 0;
        for (int i = x; i > 0; i -= lowbit(i)) {
            res += tree[i];
        }
        return res;
    }
    
    // CDQ分治主函数
    private static void cdq(int l, int r, int k) {
        if (l >= r) return;
        
        int mid = (l + r) >> 1;
        cdq(l, mid, k);
        cdq(mid + 1, r, k);
        
        // 合并过程，计算左半部分对右半部分的贡献
        int i = l, j = mid + 1;
        
        // 利用左、右各自b属性有序
        // 不回退的找，当前右组对象包括了几个左组的对象
        while (j <= r) {
            while (i <= mid && objects[i].b <= objects[j].b) {
                add(objects[i].c, 1, k);
                i++;
            }
            objects[j].f += query(objects[j].c);
            j++;
        }
        
        // 清空树状数组
        for (int t = l; t < i; t++) {
            add(objects[t].c, -1, k);
        }
        
        // 按b属性排序
        Arrays.sort(objects, l, r + 1, (a, b) -> a.b - b.b);
    }
    
    // 预处理函数
    private static void prepare(int n) {
        // 根据a排序，a一样根据b排序，b一样根据c排序
        // 排序后a、b、c一样的同组内，组前的下标得不到同组后面的统计量
        // 所以把这部分的贡献，提前补偿给组前的下标，然后再跑CDQ分治
        Arrays.sort(objects, 1, n + 1, (a, b) -> {
            if (a.a != b.a) return a.a - b.a;
            if (a.b != b.b) return a.b - b.b;
            return a.c - b.c;
        });
        
        for (int l = 1, r = 1; l <= n; l = ++r) {
            while (r + 1 <= n && objects[l].a == objects[r + 1].a && 
                   objects[l].b == objects[r + 1].b && 
                   objects[l].c == objects[r + 1].c) {
                r++;
            }
            for (int t = l; t <= r; t++) {
                objects[t].f = r - t;
            }
        }
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        String[] nk = reader.readLine().split(" ");
        int n = Integer.parseInt(nk[0]);
        int k = Integer.parseInt(nk[1]);
        
        objects = new ObjectP3810[n + 1];
        tree = new int[k + 1];
        ans = new int[n];
        
        for (int i = 1; i <= n; i++) {
            String[] abc = reader.readLine().split(" ");
            int a = Integer.parseInt(abc[0]);
            int b = Integer.parseInt(abc[1]);
            int c = Integer.parseInt(abc[2]);
            objects[i] = new ObjectP3810(i, a, b, c);
        }
        
        prepare(n);
        cdq(1, n, k);
        
        for (int i = 1; i <= n; i++) {
            ans[objects[i].f]++;
        }
        
        for (int i = 0; i < n; i++) {
            out.println(ans[i]);
        }
        
        out.flush();
        out.close();
    }
}