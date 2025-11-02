package class169.supplementary_solutions;

import java.io.*;
import java.util.*;

/**
 * HDU 2665 Kth Number - Java实现
 * 题目来源：http://acm.hdu.edu.cn/showproblem.php?pid=2665
 * 题目描述：静态区间第k小查询
 * 
 * 问题描述：
 * 给定一个长度为n的数组，有m个查询，每个查询要求在指定区间内找到第k小的数。
 * 
 * 解题思路：
 * 使用整体二分处理静态区间第k小问题。将所有查询一起处理，二分答案的值域，
 * 利用树状数组维护区间内小于等于mid的元素个数。
 * 
 * 时间复杂度：O((N+Q) * logN * log(maxValue))
 * 空间复杂度：O(N + Q)
 */
public class HDU2665_KthNumber {
    static final int MAXN = 100001;
    static int n, m;
    
    // 原始数组
    static int[] arr = new int[MAXN];
    
    // 离散化数组
    static int[] sorted = new int[MAXN];
    
    // 查询信息
    static int[] queryL = new int[MAXN];
    static int[] queryR = new int[MAXN];
    static int[] queryK = new int[MAXN];
    static int[] queryId = new int[MAXN];
    
    // 树状数组
    static int[] tree = new int[MAXN];
    
    // 整体二分
    static int[] lset = new int[MAXN];
    static int[] rset = new int[MAXN];
    
    // 查询的答案
    static int[] ans = new int[MAXN];
    
    /**
     * 计算一个数的lowbit值
     * @param i 输入的数
     * @return lowbit值
     */
    static int lowbit(int i) {
        return i & -i;
    }
    
    /**
     * 在树状数组中给位置i增加v
     * @param i 位置
     * @param v 增加的值
     */
    static void add(int i, int v) {
        while (i <= n) {
            tree[i] += v;
            i += lowbit(i);
        }
    }
    
    /**
     * 计算前缀和[1..i]
     * @param i 位置
     * @return 前缀和
     */
    static int sum(int i) {
        int ret = 0;
        while (i > 0) {
            ret += tree[i];
            i -= lowbit(i);
        }
        return ret;
    }
    
    /**
     * 计算区间和[l..r]
     * @param l 左端点
     * @param r 右端点
     * @return 区间和
     */
    static int query(int l, int r) {
        return sum(r) - sum(l - 1);
    }
    
    /**
     * 整体二分核心函数
     * @param ql 查询范围的左端点
     * @param qr 查询范围的右端点
     * @param vl 值域范围的左端点（离散化后的下标）
     * @param vr 值域范围的右端点（离散化后的下标）
     */
    static void compute(int ql, int qr, int vl, int vr) {
        // 递归边界
        if (ql > qr) {
            return;
        }
        
        // 如果值域范围只有一个值，说明找到了答案
        if (vl == vr) {
            for (int i = ql; i <= qr; i++) {
                ans[queryId[i]] = sorted[vl];
            }
            return;
        }
        
        // 二分中点
        int mid = (vl + vr) >> 1;
        
        // 将值小于等于sorted[mid]的数加入树状数组
        for (int i = vl; i <= mid; i++) {
            // 遍历所有值为sorted[i]的元素，将其加入树状数组
            for (int j = 1; j <= n; j++) {
                if (arr[j] == sorted[i]) {
                    add(j, 1);
                }
            }
        }
        
        // 检查每个查询，根据满足条件的元素个数划分到左右区间
        int lsiz = 0, rsiz = 0;
        for (int i = ql; i <= qr; i++) {
            // 查询区间[queryL[i], queryR[i]]中值小于等于sorted[mid]的元素个数
            int satisfy = query(queryL[i], queryR[i]);
            
            if (satisfy >= queryK[i]) {
                // 说明第k小的数在左半部分
                lset[++lsiz] = i;
            } else {
                // 说明第k小的数在右半部分，需要在右半部分找第(k-satisfy)小的数
                queryK[i] -= satisfy;
                rset[++rsiz] = i;
            }
        }
        
        // 重新排列查询顺序
        int idx = ql;
        for (int i = 1; i <= lsiz; i++) {
            int temp = lset[i];
            lset[i] = queryId[temp];
            queryId[idx++] = temp;
        }
        for (int i = 1; i <= rsiz; i++) {
            int temp = rset[i];
            rset[i] = queryId[temp];
            queryId[idx++] = temp;
        }
        
        // 撤销对树状数组的修改
        for (int i = vl; i <= mid; i++) {
            // 遍历所有值为sorted[i]的元素，将其从树状数组中删除
            for (int j = 1; j <= n; j++) {
                if (arr[j] == sorted[i]) {
                    add(j, -1);
                }
            }
        }
        
        // 递归处理左右两部分
        compute(ql, ql + lsiz - 1, vl, mid);
        compute(ql + lsiz, qr, mid + 1, vr);
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        int t = Integer.parseInt(br.readLine());
        while (t-- > 0) {
            String[] params = br.readLine().split(" ");
            n = Integer.parseInt(params[0]);
            m = Integer.parseInt(params[1]);
            
            // 读取原始数组
            String[] nums = br.readLine().split(" ");
            for (int i = 1; i <= n; i++) {
                arr[i] = Integer.parseInt(nums[i - 1]);
                sorted[i] = arr[i];
            }
            
            // 读取查询
            for (int i = 1; i <= m; i++) {
                String[] query = br.readLine().split(" ");
                queryL[i] = Integer.parseInt(query[0]);
                queryR[i] = Integer.parseInt(query[1]);
                queryK[i] = Integer.parseInt(query[2]);
                queryId[i] = i;
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
            compute(1, m, 1, uniqueCount);
            
            // 输出结果
            for (int i = 1; i <= m; i++) {
                out.println(ans[i]);
            }
        }
        
        out.flush();
        out.close();
        br.close();
    }
}