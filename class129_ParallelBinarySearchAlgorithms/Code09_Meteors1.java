package class169;

// SPOJ METEORS - Java实现
// 题目来源：https://www.spoj.com/problems/METEORS/
// 题目描述：国家收集陨石问题
// 时间复杂度：O(K * logK * logM)
// 空间复杂度：O(N + M + K)

import java.io.*;
import java.util.*;

public class Code09_Meteors1 {
    public static int MAXN = 300001;
    public static int MAXM = 300001;
    public static int MAXK = 300001;
    public static int n, m, k;
    
    // 国家信息
    public static int[] owner = new int[MAXM]; // 每个空间站属于哪个国家
    public static long[] target = new long[MAXN]; // 每个国家的目标收集量
    
    // 陨石雨信息
    public static int[] l = new int[MAXK];
    public static int[] r = new int[MAXK];
    public static int[] a = new int[MAXK];
    
    // 查询信息
    public static int[] qid = new int[MAXN];
    
    // 树状数组，支持区间修改、单点查询
    public static long[] tree = new long[MAXM << 1];
    
    // 整体二分
    public static int[] lset = new int[MAXN];
    public static int[] rset = new int[MAXN];
    
    // 查询的答案
    public static int[] ans = new int[MAXN];
    
    public static int lowbit(int i) {
        return i & -i;
    }
    
    public static void add(int i, long v) {
        int siz = m;
        while (i <= siz) {
            tree[i] += v;
            i += lowbit(i);
        }
    }
    
    // 区间加法 [l, r] += v
    public static void add(int l, int r, long v) {
        add(l, v);
        add(r + 1, -v);
    }
    
    public static long query(int i) {
        long ret = 0;
        while (i > 0) {
            ret += tree[i];
            i -= lowbit(i);
        }
        return ret;
    }
    
    public static void compute(int el, int er, int vl, int vr) {
        if (el > er) {
            return;
        }
        if (vl == vr) {
            for (int i = el; i <= er; i++) {
                ans[qid[i]] = vl;
            }
        } else {
            int mid = (vl + vr) >> 1;
            // 执行前mid次陨石雨操作
            for (int i = vl; i <= mid; i++) {
                if (l[i] <= r[i]) {
                    // 区间操作
                    add(l[i], r[i], a[i]);
                } else {
                    // 环形操作
                    add(l[i], m, a[i]);
                    add(1, r[i], a[i]);
                }
            }
            
            int lsiz = 0, rsiz = 0;
            for (int i = el; i <= er; i++) {
                int id = qid[i];
                // 计算国家id收集的陨石数量
                long collect = 0;
                // 这里需要遍历所有属于国家id的空间站
                for (int j = 1; j <= m; j++) {
                    if (owner[j] == id) {
                        collect += query(j);
                    }
                }
                
                if (collect >= target[id]) {
                    // 说明在前mid次陨石雨后就能达到目标
                    lset[++lsiz] = id;
                } else {
                    // 说明需要更多的陨石雨
                    rset[++rsiz] = id;
                }
            }
            
            // 撤销前mid次陨石雨操作
            for (int i = vl; i <= mid; i++) {
                if (l[i] <= r[i]) {
                    // 区间操作
                    add(l[i], r[i], -a[i]);
                } else {
                    // 环形操作
                    add(l[i], m, -a[i]);
                    add(1, r[i], -a[i]);
                }
            }
            
            // 重新排列查询顺序
            for (int i = 1; i <= lsiz; i++) {
                qid[el + i - 1] = lset[i];
            }
            for (int i = 1; i <= rsiz; i++) {
                qid[el + lsiz + i - 1] = rset[i];
            }
            
            // 递归处理左右两部分
            compute(el, el + lsiz - 1, vl, mid);
            compute(el + lsiz, er, mid + 1, vr);
        }
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        String[] params = br.readLine().split(" ");
        n = Integer.parseInt(params[0]);
        m = Integer.parseInt(params[1]);
        
        // 读取每个空间站属于哪个国家
        String[] owners = br.readLine().split(" ");
        for (int i = 1; i <= m; i++) {
            owner[i] = Integer.parseInt(owners[i - 1]);
        }
        
        // 读取每个国家的目标收集量
        String[] targets = br.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            target[i] = Long.parseLong(targets[i - 1]);
            qid[i] = i; // 初始化查询ID
        }
        
        params = br.readLine().split(" ");
        k = Integer.parseInt(params[0]);
        
        // 读取陨石雨信息
        for (int i = 1; i <= k; i++) {
            params = br.readLine().split(" ");
            l[i] = Integer.parseInt(params[0]);
            r[i] = Integer.parseInt(params[1]);
            a[i] = Integer.parseInt(params[2]);
        }
        
        // 整体二分求解
        compute(1, n, 1, k + 1);
        
        // 输出结果
        for (int i = 1; i <= n; i++) {
            if (ans[i] == k + 1) {
                out.println("NIE"); // 无法达到目标
            } else {
                out.println(ans[i]);
            }
        }
        
        out.flush();
        out.close();
        br.close();
    }
}