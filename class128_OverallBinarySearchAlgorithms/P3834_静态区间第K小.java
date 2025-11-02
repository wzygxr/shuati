package class168;

// P3834 【模板】可持久化线段树 2 / 静态区间第K小 - Java实现
// 题目来源：https://www.luogu.com.cn/problem/P3834
// 题目描述：给定一个长度为n的数组，有m次查询，每次查询[l,r]区间内第k小的数
// 解题思路：使用整体二分算法，将所有查询一起处理，避免对每个查询单独进行二分
// 时间复杂度：O((N+M) * logN * log(maxValue))
// 空间复杂度：O(N+M)
// 算法适用条件：
// 1. 询问的答案具有可二分性
// 2. 修改对判定答案的贡献互相独立
// 3. 修改如果对判定答案有贡献，则贡献为确定值
// 4. 贡献满足交换律、结合律，具有可加性
// 5. 题目允许离线操作

// 补充题目：POJ 2104 K-th Number
// 题目来源：http://poj.org/problem?id=2104
// 题目描述：给定一个长度为n的数组，有m次查询，每次查询[l,r]区间内第k小的数
// 解题思路：这是整体二分算法的经典应用，与P3834本质相同
// 时间复杂度：O((N+M) * logN * log(maxValue))
// 空间复杂度：O(N+M)
// 本题是静态区间第k小查询的标准问题，是整体二分算法的入门题目

import java.io.*;
import java.util.*;

public class P3834_静态区间第K小 {
    public static int MAXN = 200001;
    public static int n, m;
    
    // 原始数组，存储输入的数值
    public static int[] arr = new int[MAXN];
    
    // 离散化后的数组，用于离散化处理，将大值域映射到小下标范围
    public static int[] sorted = new int[MAXN];
    
    // 查询信息存储
    public static int[] qid = new int[MAXN];  // 查询编号
    public static int[] l = new int[MAXN];    // 查询区间左端点
    public static int[] r = new int[MAXN];    // 查询区间右端点
    public static int[] k = new int[MAXN];    // 查询第k小
    
    // 树状数组，用于维护当前值域范围内元素的个数
    public static int[] tree = new int[MAXN];
    
    // 整体二分中用于分类查询的临时存储
    public static int[] lset = new int[MAXN];  // 满足条件的查询
    public static int[] rset = new int[MAXN];  // 不满足条件的查询
    
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
    // ql, qr: 查询范围
    // vl, vr: 值域范围（离散化后的下标）
    public static void compute(int ql, int qr, int vl, int vr) {
        // 递归边界：没有查询需要处理
        if (ql > qr) {
            return;
        }
        
        // 如果值域范围只有一个值，说明找到了答案
        // 此时所有查询的答案都是sorted[vl]
        if (vl == vr) {
            for (int i = ql; i <= qr; i++) {
                ans[qid[i]] = sorted[vl];
            }
            return;
        }
        
        // 二分中点
        int mid = (vl + vr) >> 1;
        
        // 将值域小于等于mid的数加入树状数组
        // 这些数对后续的查询统计有贡献
        for (int i = vl; i <= mid; i++) {
            add(arr[i], 1);
        }
        
        // 检查每个查询，根据满足条件的元素个数划分到左右区间
        int lsiz = 0, rsiz = 0;
        for (int i = ql; i <= qr; i++) {
            int id = qid[i];
            // 查询区间[l[id], r[id]]中值小于等于sorted[mid]的元素个数
            int satisfy = query(l[id], r[id]);
            
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
        
        // 撤销对树状数组的修改，恢复到处理前的状态
        for (int i = vl; i <= mid; i++) {
            add(arr[i], -1);
        }
        
        // 递归处理左右两部分
        // 左半部分：值域在[vl, mid]范围内的查询
        compute(ql, ql + lsiz - 1, vl, mid);
        // 右半部分：值域在[mid+1, vr]范围内的查询
        compute(ql + lsiz, qr, mid + 1, vr);
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        String[] params = br.readLine().split(" ");
        n = Integer.parseInt(params[0]);
        m = Integer.parseInt(params[1]);
        
        // 读取原始数组
        String[] nums = br.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(nums[i - 1]);
            sorted[i] = arr[i];
        }
        
        // 离散化：将大值域映射到小下标范围，减少二分的值域范围
        Arrays.sort(sorted, 1, n + 1);
        int uniqueCount = 1;
        for (int i = 2; i <= n; i++) {
            if (sorted[i] != sorted[i - 1]) {
                sorted[++uniqueCount] = sorted[i];
            }
        }
        
        // 重新映射arr数组为离散化后的下标
        // 这样可以将值域从[MIN_VALUE, MAX_VALUE]映射到[1, uniqueCount]
        for (int i = 1; i <= n; i++) {
            arr[i] = Arrays.binarySearch(sorted, 1, uniqueCount + 1, arr[i]);
        }
        
        // 读取查询
        for (int i = 1; i <= m; i++) {
            String[] query = br.readLine().split(" ");
            l[i] = Integer.parseInt(query[0]);
            r[i] = Integer.parseInt(query[1]);
            k[i] = Integer.parseInt(query[2]);
            qid[i] = i; // 查询编号
        }
        
        // 整体二分求解
        // 初始查询范围[1, m]，初始值域范围[1, uniqueCount]
        compute(1, m, 1, uniqueCount);
        
        // 输出结果
        for (int i = 1; i <= m; i++) {
            out.println(ans[i]);
        }
        
        out.flush();
        out.close();
        br.close();
    }
}

// POJ 2104 K-th Number 完整实现类
// 这是整体二分算法的经典应用，与P3834本质相同
class POJ2104_KthNumber {
    private static final int MAXN = 100001; // 题目数据范围
    private int n, m;  // n:数组长度, m:查询次数
    
    // 原始数组，存储输入的数值
    private int[] arr = new int[MAXN];
    
    // 离散化后的数组，用于离散化处理，将大值域映射到小下标范围
    private int[] sorted = new int[MAXN];
    
    // 查询信息存储
    private int[] qid = new int[MAXN];  // 查询编号
    private int[] l = new int[MAXN];    // 查询区间左端点
    private int[] r = new int[MAXN];    // 查询区间右端点
    private int[] k = new int[MAXN];    // 查询第k小
    
    // 树状数组，用于维护当前值域范围内元素的个数
    private int[] tree = new int[MAXN];
    
    // 整体二分中用于分类查询的临时存储
    private int[] lset = new int[MAXN];  // 满足条件的查询
    private int[] rset = new int[MAXN];  // 不满足条件的查询
    
    // 查询的答案存储数组
    private int[] ans = new int[MAXN];
    
    // 树状数组操作
    // 计算二进制表示中最低位的1所代表的数值
    private int lowbit(int i) {
        return i & -i;
    }
    
    // 在树状数组的第i个位置加上v
    private void add(int i, int v) {
        while (i <= n) {
            tree[i] += v;
            i += lowbit(i);
        }
    }
    
    // 计算前缀和[1, i]的和
    private int sum(int i) {
        int ret = 0;
        while (i > 0) {
            ret += tree[i];
            i -= lowbit(i);
        }
        return ret;
    }
    
    // 计算区间和[l, r]的和
    private int query(int l, int r) {
        return sum(r) - sum(l - 1);
    }
    
    // 整体二分核心函数
    // ql, qr: 查询范围
    // vl, vr: 值域范围（离散化后的下标）
    private void compute(int ql, int qr, int vl, int vr) {
        // 递归边界：没有查询需要处理
        if (ql > qr) {
            return;
        }
        
        // 如果值域范围只有一个值，说明找到了答案
        // 此时所有查询的答案都是sorted[vl]
        if (vl == vr) {
            for (int i = ql; i <= qr; i++) {
                ans[qid[i]] = sorted[vl];
            }
            return;
        }
        
        // 二分中点
        int mid = (vl + vr) >> 1;
        
        // 初始化树状数组为0，避免之前查询的影响
        // 注意：对于POJ 2104的输入规模，这个操作是必要的
        // 这里我们采用另一种方式：将原始数组中小于等于mid的元素添加到树状数组
        // 但需要先将arr数组中的元素转换为原始值对应的下标
        // 这里需要重新考虑逻辑，正确的做法是根据原始数组的索引来添加
        
        // 正确的做法：遍历原始数组，如果元素值对应的离散化下标<=mid，则在对应位置+1
        for (int i = 1; i <= n; i++) {
            if (arr[i] <= mid) {
                add(i, 1);
            }
        }
        
        // 检查每个查询，根据满足条件的元素个数划分到左右区间
        int lsiz = 0, rsiz = 0;
        for (int i = ql; i <= qr; i++) {
            int id = qid[i];
            // 查询区间[l[id], r[id]]中值小于等于sorted[mid]的元素个数
            int satisfy = query(l[id], r[id]);
            
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
        
        // 撤销对树状数组的修改，恢复到处理前的状态
        for (int i = 1; i <= n; i++) {
            if (arr[i] <= mid) {
                add(i, -1);
            }
        }
        
        // 递归处理左右两部分
        // 左半部分：值域在[vl, mid]范围内的查询
        compute(ql, ql + lsiz - 1, vl, mid);
        // 右半部分：值域在[mid+1, vr]范围内的查询
        compute(ql + lsiz, qr, mid + 1, vr);
    }
    
    // 主函数，用于解决POJ 2104问题
    public void solve() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        String[] params = br.readLine().split(" ");
        n = Integer.parseInt(params[0]);
        m = Integer.parseInt(params[1]);
        
        // 读取原始数组
        String[] nums = br.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(nums[i - 1]);
            sorted[i] = arr[i];
        }
        
        // 离散化：将大值域映射到小下标范围，减少二分的值域范围
        Arrays.sort(sorted, 1, n + 1);
        int uniqueCount = 1;
        for (int i = 2; i <= n; i++) {
            if (sorted[i] != sorted[i - 1]) {
                sorted[++uniqueCount] = sorted[i];
            }
        }
        
        // 重新映射arr数组为离散化后的下标
        // 这样可以将值域从[MIN_VALUE, MAX_VALUE]映射到[1, uniqueCount]
        for (int i = 1; i <= n; i++) {
            arr[i] = Arrays.binarySearch(sorted, 1, uniqueCount + 1, arr[i]);
        }
        
        // 读取查询
        for (int i = 1; i <= m; i++) {
            String[] query = br.readLine().split(" ");
            l[i] = Integer.parseInt(query[0]);
            r[i] = Integer.parseInt(query[1]);
            k[i] = Integer.parseInt(query[2]);
            qid[i] = i; // 查询编号
        }
        
        // 初始化树状数组为0
        Arrays.fill(tree, 0);
        
        // 整体二分求解
        // 初始查询范围[1, m]，初始值域范围[1, uniqueCount]
        compute(1, m, 1, uniqueCount);
        
        // 输出结果
        for (int i = 1; i <= m; i++) {
            out.println(ans[i]);
        }
        
        out.flush();
        out.close();
        br.close();
    }
    
    // 注意：在实际提交POJ时，需要将该类作为主类，并添加main方法
    // public static void main(String[] args) throws IOException {
    //     new POJ2104_KthNumber().solve();
    // }
}