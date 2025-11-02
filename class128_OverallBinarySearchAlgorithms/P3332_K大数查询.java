package class168;

// P3332 [ZJOI2013] K大数查询 - Java实现
// 题目来源：https://www.luogu.com.cn/problem/P3332
// 题目描述：维护n个可重整数集，支持区间加数和区间查询第k大。
// 解题思路：使用整体二分算法处理区间加数和区间查询第k大的操作
// 时间复杂度：O(M * logN * log(maxValue))
// 空间复杂度：O(N + M)
// 算法适用条件：
// 1. 询问的答案具有可二分性
// 2. 修改对判定答案的贡献互相独立
// 3. 修改如果对判定答案有贡献，则贡献为确定值
// 4. 贡献满足交换律、结合律，具有可加性
// 5. 题目允许离线操作

import java.io.*;
import java.util.*;

public class P3332_K大数查询 {
    public static int MAXN = 50001;
    public static int MAXM = 50001;
    public static int n, m;  // n:集合个数, m:操作次数

    // 事件编号组成的数组
    public static int[] eid = new int[MAXM << 1];
    // 事件类型: op == 1代表修改事件(区间[L,R]增加值val), op == 2代表查询事件([L,R]范围上查询第k大)
    public static int[] op = new int[MAXM << 1];
    public static int[] L = new int[MAXM << 1];    // 事件区间左端点
    public static int[] R = new int[MAXM << 1];    // 事件区间右端点
    public static int[] val = new int[MAXM << 1];  // 修改事件的值
    public static int[] k = new int[MAXM << 1];    // 查询事件的第k大
    public static int[] q = new int[MAXM << 1];    // 查询事件的编号
    public static int cnte = 0;                    // 事件计数器

    // 树状数组，支持区间修改、单点查询
    public static long[] tree = new long[MAXN << 1];

    // 整体二分中用于分类事件的临时存储
    public static int[] lset = new int[MAXM << 1];  // 满足条件的事件
    public static int[] rset = new int[MAXM << 1];  // 不满足条件的事件

    // 查询的答案存储数组
    public static long[] ans = new long[MAXN];

    // 计算二进制表示中最低位的1所代表的数值
    public static int lowbit(int i) {
        return i & -i;
    }

    // 树状数组单点更新
    public static void add(int i, long v) {
        int siz = n;
        while (i <= siz) {
            tree[i] += v;
            i += lowbit(i);
        }
    }

    // 树状数组区间更新: 区间加法 [l, r] += v
    public static void add(int l, int r, long v) {
        add(l, v);
        add(r + 1, -v);
    }

    // 树状数组单点查询
    public static long query(int i) {
        long ret = 0;
        while (i > 0) {
            ret += tree[i];
            i -= lowbit(i);
        }
        return ret;
    }

    // 整体二分核心函数
    // el, er: 事件范围
    // vl, vr: 值域范围
    public static void compute(int el, int er, long vl, long vr) {
        // 递归边界：没有事件需要处理
        if (el > er) {
            return;
        }
        
        // 如果值域范围只有一个值，说明找到了答案
        if (vl == vr) {
            for (int i = el; i <= er; i++) {
                if (op[eid[i]] == 2) { // 查询事件
                    ans[q[eid[i]]] = vl;
                }
            }
        } else {
            // 二分中点
            long mid = (vl + vr) >> 1;
            
            // 检查每个事件，根据事件类型和值域范围划分到左右区间
            int lsiz = 0, rsiz = 0;
            for (int i = el; i <= er; i++) {
                int id = eid[i];
                if (op[id] == 1) {
                    // 修改事件
                    if (val[id] <= mid) {
                        // 对左半区间有贡献，执行修改
                        add(L[id], R[id], 1);
                        // 将该事件加入左集合
                        lset[++lsiz] = id;
                    } else {
                        // 对左半区间无贡献，放入右半区间
                        // 将该事件加入右集合
                        rset[++rsiz] = id;
                    }
                } else {
                    // 查询事件
                    // 查询区间[L[id], R[id]]中大于mid的元素个数
                    long satisfy = query(R[id]) - query(L[id] - 1);
                    if (satisfy >= k[id]) {
                        // 说明第k大的数在左半部分(值大于mid)
                        // 将该事件加入左集合
                        lset[++lsiz] = id;
                    } else {
                        // 说明第k大的数在右半部分(值小于等于mid)
                        // 更新k值，将该事件加入右集合
                        k[id] -= satisfy;
                        rset[++rsiz] = id;
                    }
                }
            }
            
            // 撤销对树状数组的修改，恢复到处理前的状态
            for (int i = 1; i <= lsiz; i++) {
                int id = lset[i];
                if (op[id] == 1 && val[id] <= (vl + vr) >> 1) {
                    add(L[id], R[id], -1);
                }
            }
            
            // 重新排列事件顺序，使得左集合的事件在前，右集合的事件在后
            for (int i = 1; i <= lsiz; i++) {
                eid[el + i - 1] = lset[i];
            }
            for (int i = 1; i <= rsiz; i++) {
                eid[el + lsiz + i - 1] = rset[i];
            }
            
            // 递归处理左右两部分
            // 左半部分：值域在[vl, mid]范围内的事件
            compute(el, el + lsiz - 1, vl, mid);
            // 右半部分：值域在[mid+1, vr]范围内的事件
            compute(el + lsiz, er, mid + 1, vr);
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取集合个数和操作次数
        String[] params = br.readLine().split(" ");
        n = Integer.parseInt(params[0]);
        m = Integer.parseInt(params[1]);
        
        // 记录值域范围
        long minVal = Long.MAX_VALUE;
        long maxVal = Long.MIN_VALUE;
        
        // 读取所有事件
        for (int i = 1; i <= m; i++) {
            String[] event = br.readLine().split(" ");
            eid[i] = i;  // 事件编号
            op[i] = Integer.parseInt(event[0]);  // 事件类型
            L[i] = Integer.parseInt(event[1]);   // 区间左端点
            R[i] = Integer.parseInt(event[2]);   // 区间右端点
            
            if (op[i] == 1) {
                // 修改操作: 1 L R val 表示将val加入到编号在[L,R]内的集合中
                val[i] = Integer.parseInt(event[3]);
                minVal = Math.min(minVal, val[i]);
                maxVal = Math.max(maxVal, val[i]);
            } else {
                // 查询操作: 2 L R k 表示查询编号在[L,R]内的集合的并集中，第k大的数
                k[i] = Integer.parseInt(event[3]);
                q[i] = i;  // 查询编号
            }
        }
        
        // 整体二分求解
        // 初始事件范围[1, m]，初始值域范围[minVal, maxVal]
        compute(1, m, minVal, maxVal);
        
        // 输出结果
        for (int i = 1; i <= m; i++) {
            if (op[i] == 2) {  // 查询操作的结果
                out.println(ans[i]);
            }
        }
        
        out.flush();
        out.close();
        br.close();
    }
}