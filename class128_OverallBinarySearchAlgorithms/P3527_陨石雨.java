package class168;

// P3527 [POI2011] MET-Meteors - 陨石雨 - Java实现
// 题目来源：https://www.luogu.com.cn/problem/P3527
// 题目描述：Byteotian Interstellar Union有n个成员国，星球轨道被分为m份，第i份上有第oi个国家的太空站。
// 有k场陨石雨，每场陨石雨会向区间内的太空站提供一定数量的陨石样本。
// 每个国家希望收集pi单位的陨石样本，求每个国家在第几次陨石雨之后能收集足够的样本。
// 解题思路：使用整体二分算法，将所有国家查询一起处理
// 时间复杂度：O(K * logK * logM)
// 空间复杂度：O(N + M + K)
// 算法适用条件：
// 1. 询问的答案具有可二分性
// 2. 修改对判定答案的贡献互相独立
// 3. 修改如果对判定答案有贡献，则贡献为确定值
// 4. 贡献满足交换律、结合律，具有可加性
// 5. 题目允许离线操作

import java.io.*;
import java.util.*;

public class P3527_陨石雨 {
    public static int MAXN = 300001;
    public static int n, m, k;  // n:国家数量, m:轨道份数, k:陨石雨次数

    // 国家编号
    public static int[] qid = new int[MAXN];
    // 国家的需求
    public static int[] need = new int[MAXN];

    // 陨石雨的参数
    public static int[] rainl = new int[MAXN];  // 陨石雨左端点
    public static int[] rainr = new int[MAXN];  // 陨石雨右端点
    public static int[] num = new int[MAXN];    // 陨石雨数量

    // 国家拥有的区域列表（邻接表）
    public static int[] head = new int[MAXN];   // 邻接表头
    public static int[] next = new int[MAXN];   // 邻接表next指针
    public static int[] to = new int[MAXN];     // 邻接表边指向的点
    public static int cnt = 0;                  // 边计数器

    // 树状数组，支持范围修改、单点查询
    public static long[] tree = new long[MAXN << 1];

    // 整体二分中用于分类查询的临时存储
    public static int[] lset = new int[MAXN];  // 满足条件的国家
    public static int[] rset = new int[MAXN];  // 不满足条件的国家

    // 每个国家的答案
    public static int[] ans = new int[MAXN];

    // 添加边到邻接表中
    public static void addEdge(int i, int v) {
        next[++cnt] = head[i];
        to[cnt] = v;
        head[i] = cnt;
    }

    // 计算二进制表示中最低位的1所代表的数值
    public static int lowbit(int i) {
        return i & -i;
    }

    // 树状数组单点更新
    public static void add(int i, int v) {
        int siz = m * 2;  // 由于是环形轨道，需要扩展一倍空间
        while (i <= siz) {
            tree[i] += v;
            i += lowbit(i);
        }
    }

    // 树状数组区间更新
    public static void add(int l, int r, int v) {
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
    // ql, qr: 国家查询范围
    // vl, vr: 陨石雨范围
    public static void compute(int ql, int qr, int vl, int vr) {
        // 递归边界：没有国家需要处理
        if (ql > qr) {
            return;
        }
        
        // 如果陨石雨范围只有一个值，说明找到了答案
        if (vl == vr) {
            for (int i = ql; i <= qr; i++) {
                ans[qid[i]] = vl;
            }
        } else {
            // 二分中点
            int mid = (vl + vr) >> 1;
            
            // 将前mid场陨石雨的影响加入树状数组
            for (int i = vl; i <= mid; i++) {
                add(rainl[i], rainr[i], num[i]);
            }
            
            // 检查每个国家，根据收集到的样本数量划分到左右区间
            int lsiz = 0, rsiz = 0;
            for (int i = ql; i <= qr; i++) {
                int id = qid[i];
                long satisfy = 0;
                
                // 计算国家id在前mid场陨石雨中能收集到的样本数量
                for (int e = head[id]; e > 0; e = next[e]) {
                    // 由于是环形轨道，需要查询to[e]和to[e]+m两个位置
                    satisfy += query(to[e]) + query(to[e] + m);
                    // 如果已经满足需求，提前退出
                    if (satisfy >= need[id]) {
                        break;
                    }
                }
                
                if (satisfy >= need[id]) {
                    // 说明在前mid场陨石雨中已经满足需求
                    // 将该国家加入左集合
                    lset[++lsiz] = id;
                } else {
                    // 说明在前mid场陨石雨中不满足需求
                    // 更新还需的样本数量，将该国家加入右集合
                    need[id] -= satisfy;
                    rset[++rsiz] = id;
                }
            }
            
            // 重新排列国家顺序，使得左集合的国家在前，右集合的国家在后
            for (int i = 1; i <= lsiz; i++) {
                qid[ql + i - 1] = lset[i];
            }
            for (int i = 1; i <= rsiz; i++) {
                qid[ql + lsiz + i - 1] = rset[i];
            }
            
            // 撤销对树状数组的修改，恢复到处理前的状态
            for (int i = vl; i <= mid; i++) {
                add(rainl[i], rainr[i], -num[i]);
            }
            
            // 递归处理左右两部分
            // 左半部分：在[vl, mid]范围内满足需求的国家
            compute(ql, ql + lsiz - 1, vl, mid);
            // 右半部分：在[mid+1, vr]范围内满足需求的国家
            compute(ql + lsiz, qr, mid + 1, vr);
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取国家数量和轨道份数
        String[] params = br.readLine().split(" ");
        n = Integer.parseInt(params[0]);
        m = Integer.parseInt(params[1]);
        
        // 读取每个轨道属于哪个国家，并建立邻接表
        for (int i = 1, nation; i <= m; i++) {
            nation = Integer.parseInt(br.readLine());
            addEdge(nation, i);
        }
        
        // 读取每个国家的需求
        for (int i = 1; i <= n; i++) {
            qid[i] = i;
            need[i] = Integer.parseInt(br.readLine());
        }
        
        // 读取陨石雨信息
        k = Integer.parseInt(br.readLine());
        for (int i = 1; i <= k; i++) {
            String[] rain = br.readLine().split(" ");
            rainl[i] = Integer.parseInt(rain[0]);
            rainr[i] = Integer.parseInt(rain[1]);
            // 处理环形轨道情况：如果右端点小于左端点，说明跨越了轨道的起始点
            if (rainr[i] < rainl[i]) {
                rainr[i] += m;
            }
            num[i] = Integer.parseInt(rain[2]);
        }
        
        // 整体二分求解
        // 初始国家范围[1, n]，初始陨石雨范围[1, k+1]
        // 答案范围[1..k+1]，第k+1场陨石雨认为满足不了要求
        compute(1, n, 1, k + 1);
        
        // 输出结果
        for (int i = 1; i <= n; i++) {
            if (ans[i] == k + 1) {
                // 第k+1场陨石雨表示无法满足需求
                out.println("NIE");
            } else {
                out.println(ans[i]);
            }
        }
        
        out.flush();
        out.close();
        br.close();
    }
}