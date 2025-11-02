package class181;

// 永无乡扩展版，java版
// 测试链接 : https://www.luogu.com.cn/problem/P3224
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code07_NeverLandExtended {

    public static int MAXN = 100001;
    public static int MAXT = MAXN * 40;

    public static int n, m, q;
    public static int[] pos = new int[MAXN];  // 重要度到编号的映射

    // 线段树相关数组
    public static int[] root = new int[MAXN];
    public static int[] ls = new int[MAXT];
    public static int[] rs = new int[MAXT];
    public static int[] sum = new int[MAXT];
    public static int cntt;

    // 并查集相关数组
    public static int[] father = new int[MAXN];

    // 更新节点信息
    public static void up(int i) {
        sum[i] = sum[ls[i]] + sum[rs[i]];
    }

    // 在位置jobi插入元素
    public static int add(int jobi, int l, int r, int i) {
        int rt = i;
        if (rt == 0) {
            rt = ++cntt;
        }
        if (l == r) {
            sum[rt]++;
        } else {
            int mid = (l + r) >> 1;
            if (jobi <= mid) {
                ls[rt] = add(jobi, l, mid, ls[rt]);
            } else {
                rs[rt] = add(jobi, mid + 1, r, rs[rt]);
            }
            up(rt);
        }
        return rt;
    }

    // 合并两棵线段树
    public static int merge(int l, int r, int t1, int t2) {
        if (t1 == 0 || t2 == 0) {
            return t1 + t2;
        }
        if (l == r) {
            sum[t1] += sum[t2];
        } else {
            int mid = (l + r) >> 1;
            ls[t1] = merge(l, mid, ls[t1], ls[t2]);
            rs[t1] = merge(mid + 1, r, rs[t1], rs[t2]);
            up(t1);
        }
        return t1;
    }

    // 查询第k小的元素
    public static int query(int jobk, int l, int r, int i) {
        if (i == 0 || jobk > sum[i]) {
            return -1;
        }
        if (l == r) {
            return pos[l];
        }
        int mid = (l + r) >> 1;
        if (sum[ls[i]] >= jobk) {
            return query(jobk, l, mid, ls[i]);
        } else {
            return query(jobk - sum[ls[i]], mid + 1, r, rs[i]);
        }
    }

    // 并查集查找
    public static int find(int i) {
        if (i != father[i]) {
            father[i] = find(father[i]);
        }
        return father[i];
    }

    // 并查集合并
    public static void union(int x, int y) {
        int xfa = find(x);
        int yfa = find(y);
        if (xfa != yfa) {
            father[xfa] = yfa;
            root[yfa] = merge(1, n, root[yfa], root[xfa]);
        }
    }

    public static void main(String[] args) throws Exception {
        FastReader in = new FastReader(System.in);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        n = in.nextInt();
        m = in.nextInt();
        
        // 初始化每个岛屿
        int num;
        for (int i = 1; i <= n; i++) {
            num = in.nextInt();
            pos[num] = i;  // 重要度为num的岛屿编号是i
            father[i] = i;  // 初始化并查集
            root[i] = add(num, 1, n, root[i]);  // 为每个岛屿建立线段树
        }
        
        // 处理初始连接关系
        for (int i = 1, x, y; i <= m; i++) {
            x = in.nextInt();
            y = in.nextInt();
            union(x, y);
        }
        
        // 处理查询操作
        q = in.nextInt();
        char op;
        int x, y, k;
        for (int i = 1; i <= q; i++) {
            op = in.nextChar();
            if (op == 'B') {
                x = in.nextInt();
                y = in.nextInt();
                union(x, y);
            } else {
                x = in.nextInt();
                k = in.nextInt();
                out.println(query(k, 1, n, root[find(x)]));
            }
        }
        
        out.flush();
        out.close();
    }

    // 读写工具类
    static class FastReader {
        final private int BUFFER_SIZE = 1 << 16;
        private final InputStream in;
        private final byte[] buffer;
        private int ptr, len;

        public FastReader(InputStream in) {
            this.in = in;
            buffer = new byte[BUFFER_SIZE];
            ptr = len = 0;
        }

        private boolean hasNextByte() throws IOException {
            if (ptr < len)
                return true;
            ptr = 0;
            len = in.read(buffer);
            return len > 0;
        }

        private byte readByte() throws IOException {
            if (!hasNextByte())
                return -1;
            return buffer[ptr++];
        }

        public char nextChar() throws IOException {
            byte c;
            do {
                c = readByte();
                if (c == -1)
                    return 0;
            } while (c <= ' ');
            char ans = 0;
            while (c > ' ') {
                ans = (char) c;
                c = readByte();
            }
            return ans;
        }

        public int nextInt() throws IOException {
            int num = 0;
            byte b = readByte();
            while (isWhitespace(b))
                b = readByte();
            boolean minus = false;
            if (b == '-') {
                minus = true;
                b = readByte();
            }
            while (!isWhitespace(b) && b != -1) {
                num = num * 10 + (b - '0');
                b = readByte();
            }
            return minus ? -num : num;
        }

        private boolean isWhitespace(byte b) {
            return b == ' ' || b == '\n' || b == '\r' || b == '\t';
        }
    }
}