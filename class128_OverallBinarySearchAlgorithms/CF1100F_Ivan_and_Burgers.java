package class168;

// CF1100F Ivan and Burgers - Java实现
// 题目来源：https://codeforces.com/problemset/problem/1100/F
// 题目描述：
// 给定一个长度为n的数组，有q次查询，每次查询[l,r]区间内元素异或的最大值。
// 解题思路：使用整体二分算法结合线性基，将所有查询一起处理
// 时间复杂度：O((N+Q) * logN * 32)
// 空间复杂度：O(N * 32)
// 算法适用条件：
// 1. 询问的答案具有可二分性
// 2. 修改对判定答案的贡献互相独立
// 3. 修改如果对判定答案有贡献，则贡献为确定值
// 4. 贡献满足交换律、结合律，具有可加性
// 5. 题目允许离线操作

import java.io.*;
import java.util.*;

public class CF1100F_Ivan_and_Burgers {
    public static int MAXN = 500001;
    public static int MAXB = 32;  // 线性基的位数，32位整数
    public static int n, m;       // n:数组长度, m:查询次数
    
    // 原始数组，存储输入的数值
    public static int[] arr = new int[MAXN];
    
    // 查询信息
    public static class Query {
        int l, r, id;  // l:查询区间左端点, r:查询区间右端点, id:查询编号
        
        public Query(int l, int r, int id) {
            this.l = l;
            this.r = r;
            this.id = id;
        }
    }
    
    public static Query[] queries = new Query[MAXN];  // 存储所有查询
    
    // 线性基
    // 用于维护一组线性无关的向量，支持插入向量和查询异或最大值
    public static class LinearBasis {
        int[] a = new int[MAXB];  // 线性基数组，a[i]表示最高位为i的基向量
        
        // 插入一个向量到线性基中
        public void insert(int x) {
            for (int i = MAXB - 1; i >= 0; i--) {
                // 如果x的第i位为0，跳过
                if (((x >> i) & 1) == 0) continue;
                // 如果a[i]为空，直接插入
                if (a[i] == 0) {
                    a[i] = x;
                    break;
                }
                // 否则用a[i]消去x的第i位
                x ^= a[i];
            }
        }
        
        // 查询线性基能表示出的最大异或值
        public int queryMax() {
            int res = 0;
            for (int i = MAXB - 1; i >= 0; i--) {
                // 贪心策略：如果异或a[i]能使结果更大，则异或
                if (((res >> i) & 1) == 0) {
                    res ^= a[i];
                }
            }
            return res;
        }
        
        // 合并另一个线性基
        public void merge(LinearBasis other) {
            for (int i = 0; i < MAXB; i++) {
                if (other.a[i] != 0) {
                    insert(other.a[i]);
                }
            }
        }
    }
    
    // 整体二分中用于分类查询的临时存储
    public static int[] lset = new int[MAXN];  // 左半部分查询
    public static int[] rset = new int[MAXN];  // 右半部分查询
    
    // 查询的答案存储数组
    public static int[] ans = new int[MAXN];
    
    // 线性基数组，basis[i]表示前i个元素构成的线性基
    public static LinearBasis[] basis = new LinearBasis[MAXN];
    
    // 整体二分核心函数
    // ql, qr: 查询范围
    // vl, vr: 区间范围
    public static void compute(int ql, int qr, int vl, int vr) {
        // 递归边界：没有查询需要处理
        if (ql > qr) {
            return;
        }
        
        // 如果区间范围只有一个位置，说明找到了答案
        // 此时所有查询的答案都可以通过basis[vl]直接得到
        if (vl == vr) {
            for (int i = ql; i <= qr; i++) {
                ans[queries[i].id] = basis[vl].queryMax();
            }
            return;
        }
        
        // 二分中点
        int mid = (vl + vr) >> 1;
        
        // 构建左半部分的线性基
        // 从后往前插入元素，构建[arr[mid], arr[mid-1], ..., arr[vl]]的线性基
        LinearBasis leftBasis = new LinearBasis();
        for (int i = mid; i >= vl; i--) {
            leftBasis.insert(arr[i]);
        }
        
        // 构建右半部分的线性基
        // 从前往后插入元素，构建[arr[mid+1], arr[mid+2], ..., arr[vr]]的线性基
        LinearBasis rightBasis = new LinearBasis();
        for (int i = mid + 1; i <= vr; i++) {
            rightBasis.insert(arr[i]);
        }
        
        // 检查每个查询，根据区间位置划分到左右区间
        int lsiz = 0, rsiz = 0;
        for (int i = ql; i <= qr; i++) {
            int l = queries[i].l;
            int r = queries[i].r;
            
            if (r <= mid) {
                // 查询区间完全在左半部分
                // 将该查询加入左集合
                lset[++lsiz] = i;
            } else if (l > mid) {
                // 查询区间完全在右半部分
                // 将该查询加入右集合
                rset[++rsiz] = i;
            } else {
                // 查询区间跨越中点
                // 需要合并左右两部分的线性基来计算答案
                LinearBasis temp = new LinearBasis();
                temp.merge(leftBasis);
                temp.merge(rightBasis);
                ans[queries[i].id] = temp.queryMax();
            }
        }
        
        // 重新排列查询顺序，使得左集合的查询在前，右集合的查询在后
        int idx = ql;
        for (int i = 1; i <= lsiz; i++) {
            queries[idx++] = queries[lset[i]];
        }
        for (int i = 1; i <= rsiz; i++) {
            queries[idx++] = queries[rset[i]];
        }
        
        // 递归处理左右两部分
        // 左半部分：区间在[vl, mid]范围内的查询
        compute(ql, ql + lsiz - 1, vl, mid);
        // 右半部分：区间在[mid+1, vr]范围内的查询
        compute(ql + lsiz, qr, mid + 1, vr);
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        n = Integer.parseInt(br.readLine());
        
        // 读取原始数组
        String[] nums = br.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(nums[i - 1]);
        }
        
        m = Integer.parseInt(br.readLine());
        
        // 读取查询
        for (int i = 1; i <= m; i++) {
            String[] query = br.readLine().split(" ");
            int l = Integer.parseInt(query[0]);
            int r = Integer.parseInt(query[1]);
            queries[i] = new Query(l, r, i);
        }
        
        // 初始化线性基数组
        for (int i = 0; i <= n; i++) {
            basis[i] = new LinearBasis();
        }
        
        // 整体二分求解
        // 初始查询范围[1, m]，初始区间范围[1, n]
        compute(1, m, 1, n);
        
        // 输出结果
        for (int i = 1; i <= m; i++) {
            out.println(ans[i]);
        }
        
        out.flush();
        out.close();
        br.close();
    }
}