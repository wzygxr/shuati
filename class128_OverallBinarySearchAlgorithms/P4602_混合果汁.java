package class168;

// P4602 [CTSC2018] 混合果汁 - Java实现
// 题目来源：https://www.luogu.com.cn/problem/P4602
// 题目描述：商店里有n种果汁，每种有美味度、价格和添加上限。m个小朋友希望用不超过g元钱制作至少L升果汁，
// 且希望混合果汁的美味度尽可能高（美味度等于所有参与混合的果汁的美味度的最小值）。
// 解题思路：使用整体二分算法，二分美味度，贪心选择满足条件的果汁
// 时间复杂度：O((N+M) * logN * log(maxD))
// 空间复杂度：O(N + M)
// 算法适用条件：
// 1. 询问的答案具有可二分性
// 2. 修改对判定答案的贡献互相独立
// 3. 修改如果对判定答案有贡献，则贡献为确定值
// 4. 贡献满足交换律、结合律，具有可加性
// 5. 题目允许离线操作

import java.util.*;
import java.io.*;

public class P4602_混合果汁 {
    public static int MAXN = 100001;
    
    public static int n, m;  // n:果汁种数, m:小朋友数量
    
    // 果汁信息
    public static int[] d = new int[MAXN];  // 美味度
    public static int[] p = new int[MAXN];  // 价格
    public static int[] l = new int[MAXN];  // 添加上限
    
    // 小朋友信息
    public static long[] g = new long[MAXN];  // 最大支付价格
    public static long[] L = new long[MAXN];  // 最小体积需求
    public static int[] qid = new int[MAXN];  // 查询编号
    
    // 整体二分中用于分类查询的临时存储
    public static int[] lset = new int[MAXN];  // 满足条件的查询
    public static int[] rset = new int[MAXN];  // 不满足条件的查询
    public static int[] ans = new int[MAXN];   // 查询的答案
    
    // 离散化
    public static int[] sorted = new int[MAXN];  // 离散化后的美味度数组
    public static int cntv = 0;                  // 离散化后的元素个数
    
    // 果汁信息结构体
    static class Juice implements Comparable<Juice> {
        int d, p, l;  // d:美味度, p:价格, l:添加上限
        
        public Juice(int d, int p, int l) {
            this.d = d;
            this.p = p;
            this.l = l;
        }
        
        @Override
        public int compareTo(Juice other) {
            return Integer.compare(this.d, other.d);  // 按美味度升序排序
        }
    }
    
    public static Juice[] juices = new Juice[MAXN];
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取果汁种数和小朋友数量
        String[] line = br.readLine().split(" ");
        n = Integer.parseInt(line[0]);
        m = Integer.parseInt(line[1]);
        
        // 读取果汁信息
        for (int i = 1; i <= n; i++) {
            line = br.readLine().split(" ");
            int di = Integer.parseInt(line[0]);  // 美味度
            int pi = Integer.parseInt(line[1]);  // 价格
            int li = Integer.parseInt(line[2]);  // 添加上限
            juices[i] = new Juice(di, pi, li);
            sorted[++cntv] = di;  // 用于离散化
        }
        
        // 处理小朋友需求
        for (int i = 1; i <= m; i++) {
            line = br.readLine().split(" ");
            g[i] = Long.parseLong(line[0]);  // 最大支付价格
            L[i] = Long.parseLong(line[1]);  // 最小体积需求
            qid[i] = i;                      // 查询编号
        }
        
        // 离散化：将美味度值域映射到小下标范围
        Arrays.sort(sorted, 1, cntv + 1);
        cntv = unique(sorted, cntv);  // 去重
        
        // 按美味度排序果汁
        Arrays.sort(juices, 1, n + 1);
        
        // 整体二分求解
        // 初始查询范围[1, m]，初始值域范围[1, cntv]
        compute(1, m, 1, cntv);
        
        // 输出结果
        for (int i = 1; i <= m; i++) {
            if (ans[i] != 0) {
                // 输出第i个小朋友能喝到的最美味的混合果汁的美味度
                out.println(sorted[ans[i]]);
            } else {
                // 无法满足需求
                out.println(-1);
            }
        }
        out.flush();
    }
    
    // 去重函数：对已排序数组进行去重，返回去重后的长度
    public static int unique(int[] arr, int len) {
        if (len <= 1) return len;
        int i = 1, j = 2;
        while (j <= len) {
            if (arr[j] != arr[i]) {
                arr[++i] = arr[j];
            }
            j++;
        }
        return i;
    }
    
    // 整体二分核心函数
    // ql, qr: 查询范围
    // vl, vr: 值域范围（离散化后的下标）
    public static void compute(int ql, int qr, int vl, int vr) {
        // 递归边界：没有查询需要处理
        if (ql > qr) {
            return;
        }
        
        // 如果值域范围只有一个值，说明找到了答案
        if (vl == vr) {
            for (int i = ql; i <= qr; i++) {
                ans[qid[i]] = vl;
            }
            return;
        }
        
        // 二分中点
        int mid = (vl + vr) >> 1;
        
        // 检查每个查询是否能满足条件
        int lsiz = 0, rsiz = 0;
        for (int i = ql; i <= qr; i++) {
            int id = qid[i];
            // 检查美味度大于等于sorted[mid]的果汁是否能满足需求
            if (check(mid, g[id], L[id])) {
                // 满足条件，说明答案可能更大(美味度更高)，分到左区间
                // 将该查询加入左集合
                lset[++lsiz] = id;
            } else {
                // 不满足条件，说明答案更小(美味度更低)，分到右区间
                // 将该查询加入右集合
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
        
        // 递归处理左右区间
        // 左半部分：美味度在[mid+1, vr]范围内的查询
        compute(ql, ql + lsiz - 1, mid + 1, vr);
        // 右半部分：美味度在[vl, mid]范围内的查询
        compute(ql + lsiz, qr, vl, mid);
    }
    
    // 检查美味度大于等于sorted[mid]的果汁是否能满足在maxPrice价格内制作出minVolume体积的果汁
    public static boolean check(int mid, long maxPrice, long minVolume) {
        long totalVolume = 0;   // 累计体积
        long totalPrice = 0;    // 累计价格
        
        // 贪心策略：从美味度高的果汁开始选择，以获得最高的美味度
        for (int i = n; i >= 1; i--) {
            // 只考虑美味度大于等于sorted[mid]的果汁
            if (juices[i].d < sorted[mid]) break;
            
            // 计算能使用的体积：取添加上限和还需体积的较小值
            long useVolume = Math.min(juices[i].l, minVolume - totalVolume);
            if (useVolume <= 0) continue;  // 如果不需要更多体积，跳过
            
            // 累加体积和价格
            totalVolume += useVolume;
            totalPrice += useVolume * juices[i].p;
            
            // 如果已经超过价格限制，直接返回false
            if (totalPrice > maxPrice) {
                return false;
            }
            
            // 如果已经满足体积需求，跳出循环
            if (totalVolume >= minVolume) {
                break;
            }
        }
        
        // 检查是否满足体积需求且价格不超限
        return totalVolume >= minVolume && totalPrice <= maxPrice;
    }
}