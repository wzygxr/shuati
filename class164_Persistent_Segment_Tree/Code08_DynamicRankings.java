package class157;

import java.io.*;
import java.util.*;

/**
 * Luogu P2617 Dynamic Rankings
 * 
 * 题目描述:
 * 给定一个含有n个数的序列a1,a2…an，需要支持两种操作：
 * Q l r k 表示查询下标在区间[l,r]中的第k小的数；
 * C x y 表示将ax改为y。
 * 
 * 解题思路:
 * 使用树状数组套主席树解决动态区间第K小问题。
 * 1. 对所有可能出现的数值进行离散化处理
 * 2. 使用树状数组维护主席树，支持单点修改和区间查询
 * 3. 对于修改操作，先删除原值再插入新值
 * 4. 对于查询操作，利用树状数组前缀和思想，通过多个主席树的差得到区间信息
 * 5. 在线段树上二分查找第K小的数
 * 
 * 时间复杂度: O(m log^2 n)
 * 空间复杂度: O(n log^2 n)
 */
public class Code08_DynamicRankings {
    static final int MAXN = 100010;
    
    // 原始数组
    static int[] arr = new int[MAXN];
    // 离散化后的数组
    static int[] sorted = new int[MAXN * 2];
    // 树状数组套主席树
    static int[] root = new int[MAXN];
    
    // 线段树节点信息
    static int[] left = new int[MAXN * 100];
    static int[] right = new int[MAXN * 100];
    static int[] sum = new int[MAXN * 100];
    
    // 线段树节点计数器
    static int cnt = 0;
    
    // 修改操作记录
    static List<Integer> uL = new ArrayList<>();
    static List<Integer> uR = new ArrayList<>();
    static List<Integer> uV = new ArrayList<>();
    
    /**
     * lowbit操作
     * @param x 数值
     * @return 最低位的1
     */
    static int lowbit(int x) {
        return x & (-x);
    }
    
    /**
     * 构建空线段树
     * @param l 区间左端点
     * @param r 区间右端点
     * @return 根节点编号
     */
    static int build(int l, int r) {
        int rt = ++cnt;
        sum[rt] = 0;
        if (l < r) {
            int mid = (l + r) / 2;
            left[rt] = build(l, mid);
            right[rt] = build(mid + 1, r);
        }
        return rt;
    }
    
    /**
     * 在线段树中插入一个值
     * @param pos 要插入的值（离散化后的坐标）
     * @param l 区间左端点
     * @param r 区间右端点
     * @param pre 前一个版本的节点编号
     * @param val 插入的值（1表示插入，-1表示删除）
     * @return 新节点编号
     */
    static int insert(int pos, int l, int r, int pre, int val) {
        int rt = ++cnt;
        left[rt] = left[pre];
        right[rt] = right[pre];
        sum[rt] = sum[pre] + val;
        
        if (l < r) {
            int mid = (l + r) / 2;
            if (pos <= mid) {
                left[rt] = insert(pos, l, mid, left[rt], val);
            } else {
                right[rt] = insert(pos, mid + 1, r, right[rt], val);
            }
        }
        return rt;
    }
    
    /**
     * 在树状数组位置x处插入值
     * @param x 树状数组位置
     * @param pos 值的位置（离散化后）
     * @param val 插入的值（1表示插入，-1表示删除）
     * @param limit 值域上限
     */
    static void update(int x, int pos, int val, int limit) {
        for (int i = x; i <= limit; i += lowbit(i)) {
            root[i] = insert(pos, 1, cnt, root[i], val);
        }
    }
    
    /**
     * 查询区间和
     * @param x 树状数组位置
     * @return 前缀和
     */
    static int querySum(int x) {
        int ans = 0;
        for (int i = x; i > 0; i -= lowbit(i)) {
            ans += sum[root[i]];
        }
        return ans;
    }
    
    /**
     * 查询区间第k小的数
     * @param k 第k小
     * @param l 区间左端点
     * @param r 区间右端点
     * @param limit 值域上限
     * @return 第k小的数在离散化数组中的位置
     */
    static int query(int k, int l, int r, int limit) {
        // 收集查询需要的主席树根节点
        uL.clear();
        uR.clear();
        for (int i = l - 1; i > 0; i -= lowbit(i)) {
            uL.add(root[i]);
        }
        for (int i = r; i > 0; i -= lowbit(i)) {
            uR.add(root[i]);
        }
        
        int L = 1, R = limit;
        while (L < R) {
            int mid = (L + R) / 2;
            int tmp = 0;
            for (int i = 0; i < uR.size(); i++) {
                tmp += sum[left[uR.get(i)]];
            }
            for (int i = 0; i < uL.size(); i++) {
                tmp -= sum[left[uL.get(i)]];
            }
            
            if (tmp >= k) {
                for (int i = 0; i < uR.size(); i++) {
                    uR.set(i, left[uR.get(i)]);
                }
                for (int i = 0; i < uL.size(); i++) {
                    uL.set(i, left[uL.get(i)]);
                }
                R = mid;
            } else {
                for (int i = 0; i < uR.size(); i++) {
                    uR.set(i, right[uR.get(i)]);
                }
                for (int i = 0; i < uL.size(); i++) {
                    uL.set(i, right[uL.get(i)]);
                }
                L = mid + 1;
                k -= tmp;
            }
        }
        return L;
    }
    
    /**
     * 离散化查找数值对应的排名
     * @param val 要查找的值
     * @param n 数组长度
     * @return 值的排名
     */
    static int getId(int val, int n) {
        return Arrays.binarySearch(sorted, 1, n + 1, val) + 1;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        
        String[] line = reader.readLine().split(" ");
        int n = Integer.parseInt(line[0]);
        int m = Integer.parseInt(line[1]);
        
        // 读取原始数组
        line = reader.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(line[i - 1]);
            sorted[i] = arr[i];
        }
        
        // 读取操作
        int opCnt = 0;
        String[] ops = new String[m];
        int[] opX = new int[m];
        int[] opY = new int[m];
        
        for (int i = 0; i < m; i++) {
            line = reader.readLine().split(" ");
            ops[i] = line[0];
            opX[i] = Integer.parseInt(line[1]);
            opY[i] = Integer.parseInt(line[2]);
            if (line[0].equals("Q")) {
                // 查询操作不需要额外处理
            } else {
                // 修改操作需要将新值加入离散化数组
                sorted[++opCnt + n] = opY[i];
            }
        }
        
        // 离散化处理
        Arrays.sort(sorted, 1, n + opCnt + 1);
        int size = 1;
        for (int i = 2; i <= n + opCnt; i++) {
            if (sorted[i] != sorted[size]) {
                sorted[++size] = sorted[i];
            }
        }
        
        // 构建空主席树
        for (int i = 1; i <= n; i++) {
            root[i] = build(1, size);
        }
        
        // 初始化树状数组
        for (int i = 1; i <= n; i++) {
            int pos = getId(arr[i], size);
            update(i, pos, 1, n);
        }
        
        // 处理操作
        int modifyId = 0;
        for (int i = 0; i < m; i++) {
            if (ops[i].equals("Q")) {
                // 查询操作
                int l = opX[i];
                int r = opY[i];
                int k = Integer.parseInt(reader.readLine().split(" ")[2]);
                int pos = query(k, l, r, size);
                writer.println(sorted[pos]);
            } else {
                // 修改操作
                int x = opX[i];
                int y = opY[i];
                // 删除原值
                int pos1 = getId(arr[x], size);
                update(x, pos1, -1, n);
                // 更新数组
                arr[x] = y;
                // 插入新值
                int pos2 = getId(y, size);
                update(x, pos2, 1, n);
            }
        }
        
        writer.flush();
        writer.close();
        reader.close();
    }
}