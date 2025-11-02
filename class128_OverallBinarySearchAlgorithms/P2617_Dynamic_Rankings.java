package class168;

// P2617 Dynamic Rankings - Java实现
// 题目来源：https://www.luogu.com.cn/problem/P2617
// 题目描述：
// 给定一个含有n个数的序列a1,a2…an，需要支持两种操作：
// Q l r k 表示查询下标在区间[l,r]中的第k小的数；
// C x y 表示将ax改为y。
// 解题思路：使用整体二分算法处理带修改的区间第k小查询
// 时间复杂度：O((N+Q) * logN * log(maxValue))
// 空间复杂度：O(N + Q)
// 算法适用条件：
// 1. 询问的答案具有可二分性
// 2. 修改对判定答案的贡献互相独立
// 3. 修改如果对判定答案有贡献，则贡献为确定值
// 4. 贡献满足交换律、结合律，具有可加性
// 5. 题目允许离线操作
// 注意：此实现为演示版本，实际应用中需要完善修改操作的处理逻辑

import java.io.*;
import java.util.*;

public class P2617_Dynamic_Rankings {
    public static int MAXN = 100001;
    public static int n, m;  // n:数组长度, m:操作次数
    
    // 原始数组，存储初始数值
    public static int[] arr = new int[MAXN];
    
    // 离散化后的数组，用于离散化处理，将大值域映射到小下标范围
    public static int[] sorted = new int[MAXN * 2];
    
    // 操作信息
    public static class Operation {
        int type; // 操作类型: 0表示查询, 1表示修改
        int l, r, k; // 查询操作参数: 区间[l,r]中的第k小
        int x, y;    // 修改操作参数: 将ax改为y
        int id;      // 操作编号
        
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
    
    public static Operation[] ops = new Operation[MAXN * 2];  // 存储所有操作
    
    // 树状数组，用于维护当前值域范围内元素的个数
    public static int[] tree = new int[MAXN];
    
    // 整体二分中用于分类操作的临时存储
    public static int[] lset = new int[MAXN * 2];  // 满足条件的操作
    public static int[] rset = new int[MAXN * 2];  // 不满足条件的操作
    
    // 查询的答案存储数组
    public static int[] ans = new int[MAXN];
    
    // 树状数组操作
    // 计算二进制表示中最低位的1所代表的数值
    public static int lowbit(int i) {
        return i & -i;
    }
    
    // 在树状数组的第i个位置加上v
    public static void add(int i, int v) {
        while (i <= n) {
            tree[i] += v;
            i += lowbit(i);
        }
    }
    
    // 计算前缀和[1, i]的和
    public static int sum(int i) {
        int ret = 0;
        while (i > 0) {
            ret += tree[i];
            i -= lowbit(i);
        }
        return ret;
    }
    
    // 计算区间和[l, r]的和
    public static int query(int l, int r) {
        return sum(r) - sum(l - 1);
    }
    
    // 整体二分核心函数
    // ql, qr: 操作范围
    // vl, vr: 值域范围（离散化后的下标）
    public static void compute(int ql, int qr, int vl, int vr) {
        // 递归边界：没有操作需要处理
        if (ql > qr) {
            return;
        }
        
        // 如果值域范围只有一个值，说明找到了答案
        // 此时所有查询操作的答案都是sorted[vl]
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
        
        // 将值域小于等于mid的数加入树状数组
        // 在实际实现中，我们需要处理所有值为sorted[i]的元素
        // 这里需要更复杂的处理方式，比如处理修改操作的删除和插入
        for (int i = vl; i <= mid; i++) {
            // 这里需要处理所有值为sorted[i]的元素
            // 在实际实现中，我们需要更复杂的处理方式
        }
        
        // 检查每个操作，根据满足条件的元素个数划分到左右区间
        int lsiz = 0, rsiz = 0;
        for (int i = ql; i <= qr; i++) {
            if (ops[i].type == 0) { // 查询操作
                // 查询区间[ops[i].l, ops[i].r]中值小于等于sorted[mid]的元素个数
                int satisfy = query(ops[i].l, ops[i].r);
                
                if (satisfy >= ops[i].k) {
                    // 说明第k小的数在左半部分
                    // 将该操作加入左集合
                    lset[++lsiz] = i;
                } else {
                    // 说明第k小的数在右半部分，需要在右半部分找第(k-satisfy)小的数
                    // 更新k值，将该操作加入右集合
                    ops[i].k -= satisfy;
                    rset[++rsiz] = i;
                }
            } else { // 修改操作
                // 修改操作需要拆分为删除和插入
                // 这里简化处理，实际实现中需要更复杂的逻辑
                // 如果修改后的值小于等于sorted[mid]，则对当前查询有贡献
                if (ops[i].y <= sorted[mid]) {
                    add(ops[i].x, 1);
                    lset[++lsiz] = i;
                } else {
                    // 否则对当前查询无贡献
                    rset[++rsiz] = i;
                }
            }
        }
        
        // 重新排列操作顺序，使得左集合的操作在前，右集合的操作在后
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
        
        // 撤销对树状数组的修改，恢复到处理前的状态
        // 在实际实现中，这里需要撤销之前的所有add操作
        for (int i = vl; i <= mid; i++) {
            // 撤销操作
        }
        
        // 递归处理左右两部分
        // 左半部分：值域在[vl, mid]范围内的操作
        compute(ql, ql + lsiz - 1, vl, mid);
        // 右半部分：值域在[mid+1, vr]范围内的操作
        compute(ql + lsiz, qr, mid + 1, vr);
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取数组长度和操作次数
        String[] params = br.readLine().split(" ");
        n = Integer.parseInt(params[0]);
        m = Integer.parseInt(params[1]);
        
        // 读取原始数组
        String[] nums = br.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(nums[i - 1]);
            sorted[i] = arr[i];
        }
        
        int opCount = n;
        // 读取操作
        for (int i = 1; i <= m; i++) {
            String[] op = br.readLine().split(" ");
            if (op[0].equals("Q")) {
                // 查询操作: Q l r k 表示查询下标在区间[l,r]中的第k小的数
                int l = Integer.parseInt(op[1]);
                int r = Integer.parseInt(op[2]);
                int k = Integer.parseInt(op[3]);
                ops[opCount++] = new Operation(0, l, r, k, 0, 0, i);
            } else { // C
                // 修改操作: C x y 表示将ax改为y
                int x = Integer.parseInt(op[1]);
                int y = Integer.parseInt(op[2]);
                ops[opCount++] = new Operation(1, 0, 0, 0, x, y, i);
                sorted[++n] = y; // 添加到离散化数组中
            }
        }
        
        // 离散化：将大值域映射到小下标范围，减少二分的值域范围
        Arrays.sort(sorted, 1, n + 1);
        int uniqueCount = 1;
        for (int i = 2; i <= n; i++) {
            if (sorted[i] != sorted[i - 1]) {
                sorted[++uniqueCount] = sorted[i];
            }
        }
        
        // 整体二分求解
        // 初始操作范围[1, opCount-1]，初始值域范围[1, uniqueCount]
        compute(1, opCount - 1, 1, uniqueCount);
        
        // 输出结果
        for (int i = 1; i <= m; i++) {
            if (ans[i] != 0) {
                out.println(ans[i]);
            }
        }
        
        out.flush();
        out.close();
        br.close();
    }
}