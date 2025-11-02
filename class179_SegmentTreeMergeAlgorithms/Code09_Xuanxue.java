package class181;

// 玄学 - UOJ #46
// 测试链接 : https://uoj.ac/problem/46
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

public class Code09_Xuanxue {

    public static int MAXN = 600001;
    public static int MAXT = 20 * MAXN; // 线段树节点数

    public static int n, mod;
    public static int[] num = new int[MAXN]; // 原数组
    
    // 线段树相关
    public static int[] L = new int[5 * MAXN];  // 左边界
    public static int[] R = new int[5 * MAXN];  // 右边界
    public static int ncnt = 0;  // 节点计数器
    
    // 变换信息
    public static class Tran {
        int r, a, b;
        
        public Tran(int r, int a, int b) {
            this.r = r;
            this.a = a;
            this.b = b;
        }
        
        public Tran() {
            this.r = 0;
            this.a = 0;
            this.b = 0;
        }
    }
    
    public static Tran[] node = new Tran[MAXT];
    
    // 线段树节点
    public static int[] ls = new int[MAXT];
    public static int[] rs = new int[MAXT];
    
    static {
        for (int i = 0; i < MAXT; i++) {
            node[i] = new Tran();
        }
    }
    
    // 更新节点信息
    public static void pushUp(int u) {
        L[u] = ncnt + 1;
        int lsiz = R[ls[u]], rsiz = R[rs[u]];
        int p = L[ls[u]], q = L[rs[u]];
        
        while (p <= lsiz && q <= rsiz) {
            long a = (1L * node[p].a * node[q].a) % mod;
            long b = (1L * node[p].b * node[q].a % mod + node[q].b) % mod;
            node[++ncnt] = new Tran(Math.min(node[p].r, node[q].r), (int)a, (int)b);
            
            if (node[p].r < node[q].r) {
                p++;
            } else if (node[p].r > node[q].r) {
                q++;
            } else {
                p++;
                q++;
            }
        }
        
        while (p <= lsiz) {
            node[++ncnt] = node[p++];
        }
        
        while (q <= rsiz) {
            node[++ncnt] = node[q++];
        }
        
        R[u] = ncnt;
    }
    
    // 插入操作
    public static void insert(int u, int tL, int tR, int pL, int pR, int a, int b, int tp) {
        if (tL == tR) {
            L[u] = ncnt + 1;
            if (pL > 1) {
                node[++ncnt] = new Tran(pL - 1, 1, 0);
            }
            node[++ncnt] = new Tran(pR, a, b);
            if (pR < n) {
                node[++ncnt] = new Tran(n, 1, 0);
            }
            R[u] = ncnt;
            return;
        }
        
        int mid = (tL + tR) >> 1;
        if (tp <= mid) {
            if (ls[u] == 0) {
                ls[u] = ++ncnt;
            }
            insert(ls[u], tL, mid, pL, pR, a, b, tp);
        } else {
            if (rs[u] == 0) {
                rs[u] = ++ncnt;
            }
            insert(rs[u], mid + 1, tR, pL, pR, a, b, tp);
        }
        
        if (tp == tR) {
            pushUp(u);
        }
    }
    
    // 查询操作
    public static void query(int u, int tL, int tR, int qL, int qR, int p, int[] result) {
        if (qL <= tL && tR <= qR) {
            bs(u, p, result);
            return;
        }
        
        int mid = (tL + tR) >> 1;
        if (qL <= mid) {
            query(ls[u], tL, mid, qL, qR, p, result);
        }
        if (mid + 1 <= qR) {
            query(rs[u], mid + 1, tR, qL, qR, p, result);
        }
    }
    
    // 二分查找变换
    public static void bs(int u, int p, int[] result) {
        int l = L[u] - 1, r = R[u];
        while (l + 1 < r) {
            int mid = (l + r) >> 1;
            if (p <= node[mid].r) {
                r = mid;
            } else {
                l = mid;
            }
        }
        
        long a = (1L * result[0] * node[r].a) % mod;
        long b = (1L * result[1] * node[r].a % mod + node[r].b) % mod;
        result[0] = (int)a;
        result[1] = (int)b;
    }
    
    public static void main(String[] args) throws Exception {
        FastReader in = new FastReader(System.in);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        int kind = in.nextInt() & 1;
        int lastans = 0;
        
        n = in.nextInt();
        mod = in.nextInt();
        
        for (int i = 1; i <= n; i++) {
            num[i] = in.nextInt();
        }
        
        int q = in.nextInt();
        int qcnt = 0;
        
        for (int t = 1; t <= q; t++) {
            int opt = in.nextInt();
            if (opt == 1) {
                int i = in.nextInt();
                int j = in.nextInt();
                int a = in.nextInt();
                int b = in.nextInt();
                
                if (kind == 1) {
                    i ^= lastans;
                    j ^= lastans;
                }
                
                if (ls[1] == 0) {
                    ls[1] = ++ncnt;
                }
                insert(1, 1, q, i, j, a, b, ++qcnt);
            } else {
                int i = in.nextInt();
                int j = in.nextInt();
                int k = in.nextInt();
                
                if (kind == 1) {
                    i ^= lastans;
                    j ^= lastans;
                    k ^= lastans;
                }
                
                int[] result = {1, 0}; // A, B
                query(1, 1, q, i, j, k, result);
                lastans = (int)((1L * result[0] * num[k] % mod + result[1]) % mod);
                out.println(lastans);
            }
        }
        
        out.flush();
        out.close();
    }
    
    // 读写工具类
    static class FastReader {
        private final byte[] buffer = new byte[1 << 16];
        private int ptr = 0, len = 0;
        private final InputStream in;

        FastReader(InputStream in) {
            this.in = in;
        }

        private int readByte() throws IOException {
            if (ptr >= len) {
                len = in.read(buffer);
                ptr = 0;
                if (len <= 0)
                    return -1;
            }
            return buffer[ptr++];
        }

        int nextInt() throws IOException {
            int c;
            do {
                c = readByte();
            } while (c <= ' ' && c != -1);
            boolean neg = false;
            if (c == '-') {
                neg = true;
                c = readByte();
            }
            int val = 0;
            while (c > ' ' && c != -1) {
                val = val * 10 + (c - '0');
                c = readByte();
            }
            return neg ? -val : val;
        }
    }
}