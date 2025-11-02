package class170;

// P3810 【模板】三维偏序（陌上花开）
// 平台: 洛谷
// 难度: 提高+/省选-
// 标签: CDQ分治, 三维偏序
// 链接: https://www.luogu.com.cn/problem/P3810

import java.io.*;
import java.util.*;

public class P3810模板三维偏序陌上花开 {
    
    static class Point {
        int a, b, c, cnt, ans;
        
        Point(int a, int b, int c) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.cnt = 1;
            this.ans = 0;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Point point = (Point) obj;
            return a == point.a && b == point.b && c == point.c;
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(a, b, c);
        }
    }
    
    static int MAXN = 100005;
    static int MAXK = 200005;
    static Point[] points = new Point[MAXN];
    static Point[] temp = new Point[MAXN];
    static int[] tree = new int[MAXK];
    static int[] ans = new int[MAXN];
    
    public static int lowbit(int x) {
        return x & -x;
    }
    
    public static void add(int pos, int val) {
        while (pos <= MAXK - 1) {
            tree[pos] += val;
            pos += lowbit(pos);
        }
    }
    
    public static int query(int pos) {
        int res = 0;
        while (pos > 0) {
            res += tree[pos];
            pos -= lowbit(pos);
        }
        return res;
    }
    
    public static void cdq(int l, int r) {
        if (l == r) return;
        
        int mid = (l + r) >> 1;
        cdq(l, mid);
        cdq(mid + 1, r);
        
        // 按照b属性排序
        int i = l, j = mid + 1, k = l;
        while (i <= mid && j <= r) {
            if (points[i].b <= points[j].b) {
                add(points[i].c, points[i].cnt);
                temp[k++] = points[i++];
            } else {
                points[j].ans += query(points[j].c);
                temp[k++] = points[j++];
            }
        }
        
        while (i <= mid) {
            add(points[i].c, points[i].cnt);
            temp[k++] = points[i++];
        }
        
        while (j <= r) {
            points[j].ans += query(points[j].c);
            temp[k++] = points[j++];
        }
        
        // 清空树状数组
        for (int idx = l; idx <= mid; idx++) {
            add(points[idx].c, -points[idx].cnt);
        }
        
        // 复制回原数组
        System.arraycopy(temp, l, points, l, r - l + 1);
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out));
        
        String[] firstLine = br.readLine().split(" ");
        int n = Integer.parseInt(firstLine[0]);
        int k = Integer.parseInt(firstLine[1]);
        
        // 读取输入数据
        Point[] rawPoints = new Point[n];
        for (int i = 0; i < n; i++) {
            String[] line = br.readLine().split(" ");
            int a = Integer.parseInt(line[0]);
            int b = Integer.parseInt(line[1]);
            int c = Integer.parseInt(line[2]);
            rawPoints[i] = new Point(a, b, c);
        }
        
        // 去重并统计重复次数
        Arrays.sort(rawPoints, 0, n, (p1, p2) -> {
            if (p1.a != p2.a) return Integer.compare(p1.a, p2.a);
            if (p1.b != p2.b) return Integer.compare(p1.b, p2.b);
            return Integer.compare(p1.c, p2.c);
        });
        
        int m = 0;
        for (int i = 0; i < n; i++) {
            if (i > 0 && rawPoints[i].equals(rawPoints[i - 1])) {
                points[m - 1].cnt++;
            } else {
                points[m++] = rawPoints[i];
            }
        }
        
        // CDQ分治
        cdq(0, m - 1);
        
        // 统计答案
        for (int i = 0; i < m; i++) {
            ans[points[i].ans + points[i].cnt - 1] += points[i].cnt;
        }
        
        // 输出结果
        for (int i = 0; i < n; i++) {
            pw.println(ans[i]);
        }
        
        pw.flush();
        pw.close();
        br.close();
    }
}
