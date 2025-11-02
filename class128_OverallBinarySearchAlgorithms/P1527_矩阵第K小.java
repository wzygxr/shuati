package class168;

// P1527 [国家集训队] 矩阵乘法 / 矩阵第K小 - Java实现
// 题目来源：https://www.luogu.com.cn/problem/P1527
// 题目描述：给定一个n×n的矩阵，有q次查询，每次查询子矩阵中第k小的数。
// 解题思路：使用整体二分算法结合二维树状数组处理矩阵第k小查询
// 时间复杂度：O(N² * logN * log(maxValue) + Q * logN * log(maxValue))
// 空间复杂度：O(N² + Q)
// 算法适用条件：
// 1. 询问的答案具有可二分性
// 2. 修改对判定答案的贡献互相独立
// 3. 修改如果对判定答案有贡献，则贡献为确定值
// 4. 贡献满足交换律、结合律，具有可加性
// 5. 题目允许离线操作

import java.io.*;
import java.util.*;

public class P1527_矩阵第K小 {
    public static int MAXN = 501;
    public static int MAXQ = 1000001;
    public static int n, q;  // n:矩阵大小, q:查询次数

    // 矩阵中的每个数字，所在行x、所在列y、数值v
    public static int[][] xyv = new int[MAXN * MAXN][3];
    // 矩阵中一共有多少个数字，cntv就是矩阵的规模
    public static int cntv = 0;

    // 查询任务的编号
    public static int[] qid = new int[MAXQ];
    // 查询范围的左上角坐标
    public static int[] a = new int[MAXQ];  // 行坐标
    public static int[] b = new int[MAXQ];  // 列坐标
    // 查询范围的右下角坐标
    public static int[] c = new int[MAXQ];  // 行坐标
    public static int[] d = new int[MAXQ];  // 列坐标
    // 查询矩阵内第k小
    public static int[] k = new int[MAXQ];

    // 二维树状数组，用于维护二维空间中元素的个数
    public static int[][] tree = new int[MAXN][MAXN];

    // 整体二分中用于分类查询的临时存储
    public static int[] lset = new int[MAXQ];  // 满足条件的查询
    public static int[] rset = new int[MAXQ];  // 不满足条件的查询

    // 每条查询的答案
    public static int[] ans = new int[MAXQ];

    // 计算二进制表示中最低位的1所代表的数值
    public static int lowbit(int i) {
        return i & -i;
    }

    // 二维树状数组单点更新: 二维空间中，(x,y)位置的词频加v
    public static void add(int x, int y, int v) {
        for (int i = x; i <= n; i += lowbit(i)) {
            for (int j = y; j <= n; j += lowbit(j)) {
                tree[i][j] += v;
            }
        }
    }

    // 二维树状数组前缀和查询: 二维空间中，左上角(1,1)到右下角(x,y)范围上的词频累加和
    public static int sum(int x, int y) {
        int ret = 0;
        for (int i = x; i > 0; i -= lowbit(i)) {
            for (int j = y; j > 0; j -= lowbit(j)) {
                ret += tree[i][j];
            }
        }
        return ret;
    }

    // 二维树状数组区间和查询: 二维空间中，左上角(a,b)到右下角(c,d)范围上的词频累加和
    public static int query(int a, int b, int c, int d) {
        return sum(c, d) - sum(a - 1, d) - sum(c, b - 1) + sum(a - 1, b - 1);
    }

    // 整体二分核心函数
    // ql, qr: 查询范围
    // vl, vr: 值域范围（排序后的下标）
    public static void compute(int ql, int qr, int vl, int vr) {
        // 递归边界：没有查询需要处理
        if (ql > qr) {
            return;
        }
        
        // 如果值域范围只有一个值，说明找到了答案
        if (vl == vr) {
            for (int i = ql; i <= qr; i++) {
                ans[qid[i]] = xyv[vl][2];  // xyv[vl][2]是排序后第vl个元素的值
            }
        } else {
            // 二分中点
            int mid = (vl + vr) >> 1;
            
            // 将值域中小于等于第mid个元素的数加入二维树状数组
            for (int i = vl; i <= mid; i++) {
                add(xyv[i][0], xyv[i][1], 1);  // 在(xyv[i][0], xyv[i][1])位置加1
            }
            
            // 检查每个查询，根据满足条件的元素个数划分到左右区间
            int lsiz = 0, rsiz = 0;
            for (int i = ql; i <= qr; i++) {
                int id = qid[i];
                // 查询子矩阵[a[id],b[id],c[id],d[id]]中值小于等于xyv[mid][2]的元素个数
                int satisfy = query(a[id], b[id], c[id], d[id]);
                
                if (satisfy >= k[id]) {
                    // 说明第k小的数在左半部分
                    // 将该查询加入左集合
                    lset[++lsiz] = id;
                } else {
                    // 说明第k小的数在右半部分，需要在右半部分找第(k-satisfy)小的数
                    // 更新k值，将该查询加入右集合
                    k[id] -= satisfy;
                    rset[++rsiz] = id;
                }
            }
            
            // 重新排列查询顺序，使得左集合的查询在前，右集合的查询在后
            for (int i = 1; i <= lsiz; i++) {
                qid[ql + i - 1] = lset[i];
            }
            for (int i = 1; i <= rsiz; i++) {
                qid[ql + lsiz + i - 1] = rset[i];
            }
            
            // 撤销对二维树状数组的修改，恢复到处理前的状态
            for (int i = vl; i <= mid; i++) {
                add(xyv[i][0], xyv[i][1], -1);  // 在(xyv[i][0], xyv[i][1])位置减1
            }
            
            // 递归处理左右两部分
            // 左半部分：值域在[vl, mid]范围内的查询
            compute(ql, ql + lsiz - 1, vl, mid);
            // 右半部分：值域在[mid+1, vr]范围内的查询
            compute(ql + lsiz, qr, mid + 1, vr);
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取矩阵大小和查询次数
        String[] params = br.readLine().split(" ");
        n = Integer.parseInt(params[0]);
        q = Integer.parseInt(params[1]);
        
        // 读取矩阵中的每个数字，记录其位置和数值
        for (int i = 1; i <= n; i++) {
            String[] row = br.readLine().split(" ");
            for (int j = 1; j <= n; j++) {
                xyv[++cntv][0] = i;           // 行坐标
                xyv[cntv][1] = j;             // 列坐标
                xyv[cntv][2] = Integer.parseInt(row[j - 1]);  // 数值
            }
        }
        
        // 读取查询
        for (int i = 1; i <= q; i++) {
            String[] query = br.readLine().split(" ");
            qid[i] = i;                       // 查询编号
            a[i] = Integer.parseInt(query[0]); // 左上角行坐标
            b[i] = Integer.parseInt(query[1]); // 左上角列坐标
            c[i] = Integer.parseInt(query[2]); // 右下角行坐标
            d[i] = Integer.parseInt(query[3]); // 右下角列坐标
            k[i] = Integer.parseInt(query[4]); // 查询第k小
        }
        
        // 按数值大小排序，这样可以通过下标进行二分
        Arrays.sort(xyv, 1, cntv + 1, (a, b) -> a[2] - b[2]);
        
        // 整体二分求解
        // 初始查询范围[1, q]，初始值域范围[1, cntv]
        compute(1, q, 1, cntv);
        
        // 输出结果
        for (int i = 1; i <= q; i++) {
            out.println(ans[i]);
        }
        
        out.flush();
        out.close();
        br.close();
    }
}