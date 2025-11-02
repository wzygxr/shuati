import java.io.*;
import java.util.*;

/**
 * 题目：CF911G Mass Change Queries
 * 测试链接：https://www.luogu.com.cn/problem/CF911G
 * 
 * 题目描述：
 * 给定一个长度为n的序列，支持m次操作，每次操作将区间[l, r]内所有等于x的数改为y。
 * 最后输出整个序列。
 * 
 * 解题思路：
 * 1. 使用线段树合并解决区间赋值问题
 * 2. 每个节点维护一个映射，表示当前区间内值的转换关系
 * 3. 使用懒标记优化区间修改操作
 * 4. 时间复杂度：O((n + m) log n)
 * 
 * 核心思想：
 * - 对于每个线段树节点，维护一个大小为100的数组，表示值i被映射到哪个值
 * - 区间修改时，更新对应区间的映射关系
 * - 查询时，通过懒标记下传和映射关系获取最终结果
 */
public class Code14_CF911G_MassChangeQueries {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static PrintWriter out = new PrintWriter(System.out);
    static StringTokenizer st;
    
    static int n, m;
    static int[] a;
    static final int MAX_VAL = 100; // 值的范围是1-100
    
    // 线段树节点
    static class Node {
        int l, r; // 左右子节点编号
        int[] map; // 映射关系：map[i]表示值i被映射到哪个值
        boolean lazy; // 懒标记，表示是否需要下传
        
        Node() {
            l = r = 0;
            map = new int[MAX_VAL + 1];
            // 初始化映射为恒等映射
            for (int i = 1; i <= MAX_VAL; i++) {
                map[i] = i;
            }
            lazy = false;
        }
    }
    
    static Node[] tr;
    static int cnt;
    static int root;
    
    public static void main(String[] args) throws IOException {
        n = Integer.parseInt(br.readLine());
        
        a = new int[n + 1];
        st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= n; i++) {
            a[i] = Integer.parseInt(st.nextToken());
        }
        
        // 初始化线段树
        tr = new Node[40 * n];
        for (int i = 0; i < tr.length; i++) {
            tr[i] = new Node();
        }
        cnt = 0;
        
        // 构建初始线段树
        root = build(1, n);
        
        m = Integer.parseInt(br.readLine());
        
        // 处理操作
        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int l = Integer.parseInt(st.nextToken());
            int r = Integer.parseInt(st.nextToken());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            
            if (x != y) {
                update(root, 1, n, l, r, x, y);
            }
        }
        
        // 输出最终序列
        for (int i = 1; i <= n; i++) {
            out.print(query(root, 1, n, i) + " ");
        }
        out.println();
        out.flush();
    }
    
    // 构建线段树
    static int build(int l, int r) {
        int u = ++cnt;
        if (l == r) {
            // 叶子节点不需要特殊处理，映射关系已经是恒等映射
            return u;
        }
        int mid = (l + r) >> 1;
        tr[u].l = build(l, mid);
        tr[u].r = build(mid + 1, r);
        return u;
    }
    
    // 下传懒标记
    static void pushdown(int u) {
        if (tr[u].lazy) {
            // 更新左子树
            if (tr[u].l != 0) {
                applyMapping(tr[u].l, tr[u].map);
            }
            // 更新右子树
            if (tr[u].r != 0) {
                applyMapping(tr[u].r, tr[u].map);
            }
            // 重置当前节点的映射为恒等映射
            for (int i = 1; i <= MAX_VAL; i++) {
                tr[u].map[i] = i;
            }
            tr[u].lazy = false;
        }
    }
    
    // 应用映射关系
    static void applyMapping(int u, int[] parentMap) {
        // 创建新的映射：newMap[i] = parentMap[tr[u].map[i]]
        int[] newMap = new int[MAX_VAL + 1];
        for (int i = 1; i <= MAX_VAL; i++) {
            newMap[i] = parentMap[tr[u].map[i]];
        }
        
        // 如果子节点已经有懒标记，需要合并映射
        if (tr[u].lazy) {
            int[] temp = new int[MAX_VAL + 1];
            for (int i = 1; i <= MAX_VAL; i++) {
                temp[i] = newMap[i];
            }
            newMap = temp;
        } else {
            // 否则直接设置映射
            System.arraycopy(newMap, 0, tr[u].map, 0, newMap.length);
            tr[u].lazy = true;
        }
    }
    
    // 区间更新：将区间[l, r]内所有值为x的数改为y
    static void update(int u, int l, int r, int ql, int qr, int x, int y) {
        if (ql <= l && r <= qr) {
            // 整个区间都在查询范围内
            if (!tr[u].lazy) {
                tr[u].lazy = true;
            }
            // 更新映射：将x映射到y，其他值保持不变
            for (int i = 1; i <= MAX_VAL; i++) {
                if (tr[u].map[i] == x) {
                    tr[u].map[i] = y;
                }
            }
            return;
        }
        
        pushdown(u);
        int mid = (l + r) >> 1;
        
        if (ql <= mid) {
            update(tr[u].l, l, mid, ql, qr, x, y);
        }
        if (qr > mid) {
            update(tr[u].r, mid + 1, r, ql, qr, x, y);
        }
    }
    
    // 单点查询：获取位置pos的值
    static int query(int u, int l, int r, int pos) {
        if (l == r) {
            // 叶子节点，应用映射关系后返回
            return tr[u].map[a[l]];
        }
        
        pushdown(u);
        int mid = (l + r) >> 1;
        
        if (pos <= mid) {
            return query(tr[u].l, l, mid, pos);
        } else {
            return query(tr[u].r, mid + 1, r, pos);
        }
    }
    
    // 线段树合并（可选功能，用于优化）
    static int merge(int u, int v, int l, int r) {
        if (u == 0) return v;
        if (v == 0) return u;
        
        if (l == r) {
            // 叶子节点合并：应用v的映射到u
            if (tr[v].lazy) {
                if (!tr[u].lazy) {
                    tr[u].lazy = true;
                    // 复制恒等映射
                    for (int i = 1; i <= MAX_VAL; i++) {
                        tr[u].map[i] = i;
                    }
                }
                // 合并映射
                for (int i = 1; i <= MAX_VAL; i++) {
                    tr[u].map[i] = tr[v].map[tr[u].map[i]];
                }
            }
            return u;
        }
        
        pushdown(u);
        pushdown(v);
        
        int mid = (l + r) >> 1;
        tr[u].l = merge(tr[u].l, tr[v].l, l, mid);
        tr[u].r = merge(tr[u].r, tr[v].r, mid + 1, r);
        
        return u;
    }
}

/**
 * 解题技巧总结：
 * 1. 映射关系的维护：每个节点维护一个值域大小的映射数组
 * 2. 懒标记的应用：只有当需要下传时才创建新的映射关系
 * 3. 映射合并：父节点的映射应用到子节点的映射上
 * 4. 内存优化：动态开点避免内存浪费
 * 
 * 类似题目推荐：
 * 1. P5494 【模板】线段树合并 - 线段树合并基础
 * 2. P6773 [NOI2020] 命运 - 树形DP+线段树合并
 * 3. P4556 [Vani有约会]雨天的尾巴 - 树上差分+线段树合并
 * 4. P3224 [HNOI2012]永无乡 - 并查集+线段树合并
 * 
 * 线段树合并的变种应用：
 * 1. 区间赋值：通过维护映射关系实现高效区间修改
 * 2. 颜色段合并：维护连续相同值的区间
 * 3. 历史版本管理：通过可持久化线段树支持历史查询
 * 
 * 性能优化建议：
 * 1. 使用数组而非HashMap提高访问速度
 * 2. 懒标记及时下传避免深度递归
 * 3. 合理预分配内存减少动态分配开销
 */