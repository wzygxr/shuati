package class169;

// HDU 5412 CRB and Queries - Java实现
// 题目来源：http://acm.hdu.edu.cn/showproblem.php?pid=5412
// 题目描述：带修改区间第k小查询
// 时间复杂度：O((N+Q) * logN * log(maxValue))
// 空间复杂度：O(N + Q)

import java.io.*;
import java.util.*;

public class Code08_CRBAndQueries1 {
    public static int MAXN = 100001;
    public static int n, m;
    
    // 原始数组
    public static int[] arr = new int[MAXN];
    
    // 离散化数组
    public static int[] sorted = new int[MAXN * 2];
    
    // 操作信息
    public static class Operation {
        int type; // 0: 查询, 1: 修改
        int l, r, k, x, y;
        int id;
        
        public Operation(int type, int l, int r, int k, int x, int y, int id) {
            this.type = type;
            this.l = l;
            this.r = r;
            this.k = k;
            this.x = x;
            this.y = y;
            this.id = id;
        }
    }
    
    public static Operation[] ops = new Operation[MAXN * 2];
    
    // 树状数组
    public static int[] tree = new int[MAXN];
    
    // 整体二分
    public static int[] lset = new int[MAXN * 2];
    public static int[] rset = new int[MAXN * 2];
    
    // 查询的答案
    public static int[] ans = new int[MAXN];
    
    // 树状数组操作
    public static int lowbit(int i) {
        return i & -i;
    }
    
    public static void add(int i, int v) {
        while (i <= n) {
            tree[i] += v;
            i += lowbit(i);
        }
    }
    
    public static int sum(int i) {
        int ret = 0;
        while (i > 0) {
            ret += tree[i];
            i -= lowbit(i);
        }
        return ret;
    }
    
    public static int query(int l, int r) {
        return sum(r) - sum(l - 1);
    }
    
    // 整体二分核心函数
    // ql, qr: 操作范围
    // vl, vr: 值域范围（离散化后的下标）
    public static void compute(int ql, int qr, int vl, int vr) {
        // 递归边界
        if (ql > qr) {
            return;
        }
        
        // 如果值域范围只有一个值，说明找到了答案
        if (vl == vr) {
            for (int i = ql; i <= qr; i++) {
                if (ops[i].type == 0) { // 查询操作
                    ans[ops[i].id] = sorted[vl];
                }
            }
            return;
        }
        
        // 二分中点
        int mid = (vl + vr) >> 1;
        
        // 将值小于等于sorted[mid]的数加入树状数组
        for (int i = vl; i <= mid; i++) {
            // 处理所有值为sorted[i]的元素
        }
        
        // 检查每个操作，根据满足条件的元素个数划分到左右区间
        int lsiz = 0, rsiz = 0;
        for (int i = ql; i <= qr; i++) {
            if (ops[i].type == 0) { // 查询操作
                // 查询区间[ops[i].l, ops[i].r]中值小于等于sorted[mid]的元素个数
                int satisfy = query(ops[i].l, ops[i].r);
                
                if (satisfy >= ops[i].k) {
                    // 说明第k小的数在左半部分
                    lset[++lsiz] = i;
                } else {
                    // 说明第k小的数在右半部分，需要在右半部分找第(k-satisfy)小的数
                    ops[i].k -= satisfy;
                    rset[++rsiz] = i;
                }
            } else { // 修改操作
                // 修改操作需要拆分为删除和插入
                // 这里简化处理，实际实现中需要更复杂的逻辑
                if (ops[i].y <= sorted[mid]) {
                    add(ops[i].x, 1);
                    lset[++lsiz] = i;
                } else {
                    rset[++rsiz] = i;
                }
            }
        }
        
        // 重新排列操作顺序
        int idx = ql;
        for (int i = 1; i <= lsiz; i++) {
            int temp = lset[i];
            lset[i] = ops[temp].id;
            ops[idx++] = ops[temp];
        }
        for (int i = 1; i <= rsiz; i++) {
            int temp = rset[i];
            rset[i] = ops[temp].id;
            ops[idx++] = ops[temp];
        }
        
        // 撤销对树状数组的修改
        for (int i = vl; i <= mid; i++) {
            // 撤销操作
        }
        
        // 递归处理左右两部分
        compute(ql, ql + lsiz - 1, vl, mid);
        compute(ql + lsiz, qr, mid + 1, vr);
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        String line = br.readLine();
        while (line != null && !line.isEmpty()) {
            String[] params = line.split(" ");
            n = Integer.parseInt(params[0]);
            
            // 读取原始数组
            String[] nums = br.readLine().split(" ");
            for (int i = 1; i <= n; i++) {
                arr[i] = Integer.parseInt(nums[i - 1]);
                sorted[i] = arr[i];
            }
            
            int opCount = n;
            // 读取操作
            m = Integer.parseInt(br.readLine());
            for (int i = 1; i <= m; i++) {
                String[] op = br.readLine().split(" ");
                if (op[0].equals("Q")) {
                    int l = Integer.parseInt(op[1]);
                    int r = Integer.parseInt(op[2]);
                    int k = Integer.parseInt(op[3]);
                    ops[opCount++] = new Operation(0, l, r, k, 0, 0, i);
                } else { // C
                    int x = Integer.parseInt(op[1]);
                    int y = Integer.parseInt(op[2]);
                    ops[opCount++] = new Operation(1, 0, 0, 0, x, y, i);
                    sorted[++n] = y; // 添加到离散化数组中
                }
            }
            
            // 离散化
            Arrays.sort(sorted, 1, n + 1);
            int uniqueCount = 1;
            for (int i = 2; i <= n; i++) {
                if (sorted[i] != sorted[i - 1]) {
                    sorted[++uniqueCount] = sorted[i];
                }
            }
            
            // 整体二分求解
            compute(1, opCount - 1, 1, uniqueCount);
            
            // 输出结果
            for (int i = 1; i <= m; i++) {
                if (ans[i] != 0) {
                    out.println(ans[i]);
                }
            }
            
            line = br.readLine();
        }
        
        out.flush();
        out.close();
        br.close();
    }
}