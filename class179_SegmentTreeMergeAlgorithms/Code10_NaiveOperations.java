package class181;

// Naive Operations - HDU 6315
// 测试链接 : https://acm.hdu.edu.cn/showproblem.php?pid=6315
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

public class Code10_NaiveOperations {

    public static int MAXN = 100005;
    public static int MAXT = MAXN << 2; // 线段树节点数

    public static int n, m;
    
    // 线段树相关
    public static int[] tree = new int[MAXT];  // 区间和
    public static int[] cnt = new int[MAXT];   // 区间最小值
    public static int[] col = new int[MAXT];   // 延迟标记
    public static int[] b = new int[MAXN];     // b数组
    
    // 线段树更新
    public static void pushUp(int rt) {
        cnt[rt] = Math.min(cnt[rt << 1], cnt[rt << 1 | 1]);
        tree[rt] = tree[rt << 1] + tree[rt << 1 | 1];
    }
    
    public static void pushDown(int rt) {
        if (col[rt] != 0) {
            cnt[rt << 1] -= col[rt];
            cnt[rt << 1 | 1] -= col[rt];
            col[rt << 1] += col[rt];
            col[rt << 1 | 1] += col[rt];
            col[rt] = 0;
        }
    }
    
    public static void build(int l, int r, int rt) {
        if (l == r) {
            cnt[rt] = b[l];
            tree[rt] = col[rt] = 0;
            return;
        }
        
        int mid = (l + r) >> 1;
        build(l, mid, rt << 1);
        build(mid + 1, r, rt << 1 | 1);
        pushUp(rt);
    }
    
    public static void update(int L, int R, int temp, int l, int r, int rt) {
        if (temp == 1) {
            if (l == r) {
                cnt[rt] = b[l];
                tree[rt] += 1;
                return;
            }
            
            pushDown(rt);
            int mid = (l + r) >> 1;
            if (L <= mid) update(L, R, cnt[rt << 1] == 1 ? 1 : 0, l, mid, rt << 1);
            if (R > mid) update(L, R, cnt[rt << 1 | 1] == 1 ? 1 : 0, mid + 1, r, rt << 1 | 1);
        } else {
            if (L <= l && r <= R) {
                cnt[rt] -= 1;
                col[rt] += 1;
                return;
            }
            
            pushDown(rt);
            int mid = (l + r) >> 1;
            if (L <= mid) update(L, R, 0, l, mid, rt << 1);
            if (R > mid) update(L, R, 0, mid + 1, r, rt << 1 | 1);
        }
        pushUp(rt);
    }
    
    public static int query(int L, int R, int l, int r, int rt) {
        if (L <= l && r <= R) {
            return tree[rt];
        }
        
        pushDown(rt);
        int mid = (l + r) >> 1;
        int ret = 0;
        if (L <= mid) ret += query(L, R, l, mid, rt << 1);
        if (R > mid) ret += query(L, R, mid + 1, r, rt << 1 | 1);
        return ret;
    }
    
    public static void main(String[] args) throws Exception {
        FastReader in = new FastReader(System.in);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        while (in.hasNext()) {
            n = in.nextInt();
            m = in.nextInt();
            
            Arrays.fill(cnt, 0x3f3f3f3f);
            Arrays.fill(tree, 0);
            Arrays.fill(col, 0);
            
            for (int i = 1; i <= n; i++) {
                b[i] = in.nextInt();
            }
            
            build(1, n, 1);
            
            for (int i = 0; i < m; i++) {
                String op = in.next();
                int l = in.nextInt();
                int r = in.nextInt();
                
                if (op.charAt(0) == 'a') {
                    update(l, r, cnt[1] == 1 ? 1 : 0, 1, n, 1);
                } else {
                    out.println(query(l, r, 1, n, 1));
                }
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

        boolean hasNext() throws IOException {
            return ptr < len || len != -1;
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
        
        String next() throws IOException {
            int c;
            do {
                c = readByte();
            } while (c <= ' ' && c != -1);
            StringBuilder sb = new StringBuilder();
            while (c > ' ' && c != -1) {
                sb.append((char) c);
                c = readByte();
            }
            return sb.toString();
        }
    }
}