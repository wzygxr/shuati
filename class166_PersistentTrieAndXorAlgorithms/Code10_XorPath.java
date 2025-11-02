package class159;

// 树上异或路径最大值
// 给定一棵n个节点的树，每个节点有点权
// 有m次查询，每次查询格式为：
// 1 x y : 在以x为根的子树中找一个点，使其点权与y的异或值最大
// 2 x y z : 在x到y的路径上找一个点，使其点权与z的异或值最大
// 测试链接 : https://www.luogu.com.cn/problem/P4592

// 补充题目1: 树上子树异或最大值查询
// 在树结构中，每个节点有权值，查询以某节点为根的子树中与给定值异或的最大值
// 测试链接: https://www.luogu.com.cn/problem/P4592

// 补充题目2: 树上路径异或最大值查询
// 在树结构中，每个节点有权值，查询两点间路径上与给定值异或的最大值
// 测试链接: https://www.luogu.com.cn/problem/P4592

// 补充题目3: DFS序应用
// 利用DFS序将树上子树问题转化为区间问题
// 测试链接: https://www.luogu.com.cn/problem/P4592

// 补充题目4: LCA应用
// 利用最近公共祖先算法解决树上路径查询问题
// 测试链接: https://www.luogu.com.cn/problem/P4592

import java.io.*;
import java.util.*;

public class Code10_XorPath {
    // 最大节点数
    public static int MAXN = 100001;
    
    // Trie树最大节点数
    public static int MAXT = MAXN * 62;
    
    // 倍增数组最大高度
    public static int MAXH = 16;
    
    // 位数，由于数字范围是1 <= 点权、z < 2^30，所以最多需要30位
    public static int BIT = 29;
    
    // 节点数和查询数
    public static int n, m;
    
    // 每个节点的点权
    public static int[] arr = new int[MAXN];
    
    // 链式前向星需要的数组（用于存储树的边）
    
    // head[i]表示节点i的第一条边的编号
    public static int[] head = new int[MAXN];
    
    // next[i]表示第i条边的下一条边的编号
    public static int[] next = new int[MAXN << 1];
    
    // to[i]表示第i条边指向的节点
    public static int[] to = new int[MAXN << 1];
    
    // 链式前向星的边的计数器
    public static int cntg = 0;
    
    // 树上dfs需要的数据结构
    
    // deep[i]表示节点i的深度
    public static int[] deep = new int[MAXN];
    
    // size[i]表示以节点i为根的子树大小
    public static int[] size = new int[MAXN];
    
    // stjump[i][j]表示节点i向上跳2^j步到达的节点（用于LCA计算）
    public static int[][] stjump = new int[MAXN][MAXH];
    
    // dfn[i]表示节点i的DFS序号（用于将子树问题转化为区间问题）
    public static int[] dfn = new int[MAXN];
    
    // dfn序号计数器
    public static int cntd = 0;
    
    // 可持久化Trie相关数据结构
    
    // root1[i]表示基于dfn序的可持久化Trie根节点编号（用于子树查询）
    public static int[] root1 = new int[MAXN];
    
    // root2[i]表示基于父节点的可持久化Trie根节点编号（用于路径查询）
    public static int[] root2 = new int[MAXN];
    
    // tree[i][0/1]表示Trie树节点i的左右子节点编号
    public static int[][] tree = new int[MAXT][2];
    
    // pass[i]表示经过Trie树节点i的数字个数（用于区间查询）
    public static int[] pass = new int[MAXT];
    
    // Trie树节点计数器
    public static int cntt = 0;
    
    /**
     * 添加一条无向边到链式前向星
     * 
     * @param u 起点
     * @param v 终点
     */
    public static void addEdge(int u, int v) {
        // 创建新边
        next[++cntg] = head[u];
        to[cntg] = v;
        head[u] = cntg;
    }
    
    /**
     * 在可持久化Trie树中插入一个数字
     * 实现可持久化的核心：只创建被修改的节点，其余节点继承历史版本
     * 
     * @param num 要插入的数字（节点点权）
     * @param i 前一个版本的根节点编号
     * @return 新版本的根节点编号
     */
    public static int insert(int num, int i) {
        // 创建新根节点
        int rt = ++cntt;
        
        // 复用前一个版本的左右子树（可持久化的核心）
        tree[rt][0] = tree[i][0];
        tree[rt][1] = tree[i][1];
        
        // 经过该节点的数字个数加1
        pass[rt] = pass[i] + 1;
        
        // 从高位到低位处理数字的每一位（Trie树的构建过程）
        for (int b = BIT, path, pre = rt, cur; b >= 0; b--, pre = cur) {
            // 提取第b位的值（0或1）
            path = (num >> b) & 1;
            
            // 获取前一个版本中对应子节点
            i = tree[i][path];
            
            // 创建新节点（只创建需要改变的节点）
            cur = ++cntt;
            
            // 复用前一个版本的子节点信息
            tree[cur][0] = tree[i][0];
            tree[cur][1] = tree[i][1];
            
            // 更新经过该节点的数字个数
            pass[cur] = pass[i] + 1;
            
            // 连接父子节点
            tree[pre][path] = cur;
        }
        return rt;
    }
    
    /**
     * 在可持久化Trie树中查询区间[u,v]与num异或的最大值
     * 利用pass数组实现区间查询：通过比较两个版本中节点pass值的差来判断区间内是否存在该路径
     * 
     * @param num 查询的目标数字
     * @param u 区间左边界对应版本的根节点编号
     * @param v 区间右边界对应版本的根节点编号
     * @return 区间内与num异或的最大值
     */
    public static int query(int num, int u, int v) {
        int ans = 0;
        
        // 从高位到低位贪心选择使异或结果最大的路径
        for (int b = BIT, path, best; b >= 0; b--) {
            // 提取第b位的值
            path = (num >> b) & 1;
            
            // 贪心策略：尽量选择与当前位相反的路径（使异或结果最大）
            best = path ^ 1;
            
            // 区间查询的关键：通过pass值差判断区间内是否存在best路径
            // 如果在区间[u,v]中存在best路径，则选择该路径
            if (pass[tree[v][best]] > pass[tree[u][best]]) {
                // 将第b位置为1（异或结果为1）
                ans += 1 << b;
                
                // 移动到best子节点
                u = tree[u][best];
                v = tree[v][best];
            } else {
                // 否则只能选择相同路径
                u = tree[u][path];
                v = tree[v][path];
            }
        }
        return ans;
    }
    
    /**
     * 第一次DFS遍历树，计算节点深度、子树大小、dfn序等信息
     * 
     * @param u 当前节点
     * @param fa 父节点
     */
    public static void dfs1(int u, int fa) {
        // 计算节点深度
        deep[u] = deep[fa] + 1;
        
        // 初始化子树大小
        size[u] = 1;
        
        // 设置直接父节点
        stjump[u][0] = fa;
        
        // 记录DFS序号（将树上问题转化为序列问题的关键）
        dfn[u] = ++cntd;
        
        // 预处理倍增数组（用于LCA计算）
        for (int p = 1; p < MAXH; p++) {
            stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
        }
        
        // 遍历子节点
        for (int ei = head[u], v; ei > 0; ei = next[ei]) {
            v = to[ei];
            if (v != fa) {
                // 递归处理子节点
                dfs1(v, u);
                
                // 累加子树大小
                size[u] += size[v];
            }
        }
    }
    
    /**
     * 第二次DFS遍历树，构建两种版本的可持久化Trie
     * 
     * @param u 当前节点
     * @param fa 父节点
     */
    public static void dfs2(int u, int fa) {
        // 基于dfn序构建Trie（用于子树查询）
        // 由于DFS序的性质，子树在序列中是连续的区间
        root1[dfn[u]] = insert(arr[u], root1[dfn[u] - 1]);
        
        // 基于父节点构建Trie（用于路径查询）
        // 通过维护父子关系来支持路径查询
        root2[u] = insert(arr[u], root2[fa]);
        
        // 遍历子节点
        for (int ei = head[u]; ei > 0; ei = next[ei]) {
            if (to[ei] != fa) {
                // 递归处理子节点
                dfs2(to[ei], u);
            }
        }
    }
    
    /**
     * 计算两个节点的最近公共祖先(LCA)
     * 使用倍增算法实现
     * 
     * @param a 节点a
     * @param b 节点b
     * @return 最近公共祖先节点编号
     */
    public static int lca(int a, int b) {
        // 确保a节点深度不小于b节点
        if (deep[a] < deep[b]) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        
        // 先将a调整到与b同一深度
        for (int p = MAXH - 1; p >= 0; p--) {
            if (deep[stjump[a][p]] >= deep[b]) {
                a = stjump[a][p];
            }
        }
        
        // 如果a和b在同一节点，直接返回
        if (a == b) {
            return a;
        }
        
        // 同时向上跳，直到相遇
        for (int p = MAXH - 1; p >= 0; p--) {
            if (stjump[a][p] != stjump[b][p]) {
                a = stjump[a][p];
                b = stjump[b][p];
            }
        }
        
        // 返回最近公共祖先的父节点
        return stjump[a][0];
    }
    
    public static void main(String[] args) throws IOException {
        // 使用BufferedReader和PrintWriter提高IO效率
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取第一行：n（节点数）和m（查询数）
        String[] parts = br.readLine().split(" ");
        n = Integer.parseInt(parts[0]);
        m = Integer.parseInt(parts[1]);
        
        // 读取每个节点的点权
        parts = br.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(parts[i - 1]);
        }
        
        // 读取树的边信息
        for (int i = 1, u, v; i < n; i++) {
            parts = br.readLine().split(" ");
            u = Integer.parseInt(parts[0]);
            v = Integer.parseInt(parts[1]);
            
            // 添加无向边
            addEdge(u, v);
            addEdge(v, u);
        }
        
        // 预处理阶段
        // 第一次DFS：计算树的基本信息
        dfs1(1, 0);
        
        // 第二次DFS：构建可持久化Trie
        dfs2(1, 0);
        
        // 处理查询
        for (int i = 1, op, x, y, z; i <= m; i++) {
            parts = br.readLine().split(" ");
            op = Integer.parseInt(parts[0]);  // 操作类型
            x = Integer.parseInt(parts[1]);   // 第一个参数
            y = Integer.parseInt(parts[2]);   // 第二个参数
            
            if (op == 1) {
                // 子树查询：在以x为根的子树中找一个点，使其点权与y的异或值最大
                // 利用DFS序的性质，子树在序列中是连续区间[dfn[x], dfn[x]+size[x]-1]
                out.println(query(y, root1[dfn[x] - 1], root1[dfn[x] + size[x] - 1]));
            } else {
                // 路径查询：在x到y的路径上找一个点，使其点权与z的异或值最大
                z = Integer.parseInt(parts[3]);  // 第三个参数
                
                // 计算x和y的最近公共祖先
                int lcaNode = lca(x, y);
                
                // 获取LCA的父节点（用于容斥计算）
                int lcaFa = stjump[lcaNode][0];
                
                // 利用容斥原理计算路径上与z异或的最大值：
                // 路径x->y上的点 = (路径root->x上的点) ∪ (路径root->y上的点) - (路径root->lca上的点) - (路径root->lca_fa上的点)
                int ans = Math.max(
                    query(z, root2[lcaFa], root2[x]),  // x到LCA路径上的点
                    query(z, root2[lcaFa], root2[y])   // y到LCA路径上的点
                );
                
                out.println(ans);
            }
        }
        
        // 刷新输出缓冲区并关闭
        out.flush();
        out.close();
    }
    
    /*
     * 算法分析:
     * 时间复杂度: O((n + m) * log M)
     *   - n是节点数，m是查询数
     *   - log M是数字的位数（这里M=2^30，所以log M=30）
     *   - 每次插入和查询操作都需要遍历数字的所有位
     *   - LCA计算的时间复杂度为O(log n)
     * 空间复杂度: O(n * log M)
     *   - 需要存储两个版本的可持久化Trie
     *   - 每个版本的Trie最多有log M个节点
     *   - 总共有n个版本
     * 
     * 算法思路:
     * 1. 使用两次dfs预处理树的信息：
     *    - 第一次计算深度、子树大小、dfn序、倍增数组
     *    - 第二次构建可持久化Trie
     * 2. 构建两种版本的可持久化Trie：
     *    - root1: 基于dfn序，用于子树查询
     *    - root2: 基于父节点，用于路径查询
     * 3. 对于子树查询，在dfn序的区间中查找
     * 4. 对于路径查询，利用LCA将路径分为两段分别查询
     * 
     * 关键点:
     * 1. 树上DFS的两次遍历技巧
     * 2. 可持久化Trie的两种构建方式
     * 3. LCA算法的倍增实现
     * 4. 树上路径的拆分技巧
     * 
     * 数学原理:
     * 1. DFS序性质：子树在DFS序中是连续的区间
     * 2. 容斥原理：树上路径x->y的点集 = (root->x) ∪ (root->y) - (root->lca) - (root->lca_fa)
     * 3. 倍增LCA：通过预处理跳转表快速计算LCA
     * 
     * 工程化考量:
     * 1. IO优化：使用BufferedReader和PrintWriter提高读写效率
     * 2. 内存管理：合理设置数组大小，避免内存浪费
     * 3. 边界处理：正确处理根节点、叶子节点等特殊情况
     * 4. 性能优化：使用位运算提高计算效率
     * 
     * 跨语言实现差异:
     * 1. Java有自动垃圾回收，不需要手动释放内存
     * 2. Java中的数组访问需要边界检查，可能比C++稍慢
     * 3. Java中的IO操作可以通过BufferedReader/PrintWriter优化
     * 
     * 算法在工程中的应用:
     * 1. 社交网络分析：在社交网络树结构中查找具有特定属性的用户
     * 2. 文件系统：在目录树中查找满足特定条件的文件
     * 3. 网络路由：在网络拓扑树中查找最优路径
     * 4. 数据库索引：树形索引结构中的范围查询优化
     * 
     * 调试技巧:
     * 1. 打印中间变量：在关键步骤打印DFS序、深度、Trie节点状态
     * 2. 小例子测试：用简单的树结构验证算法逻辑的正确性
     * 3. 边界测试：测试单节点树、链式树等特殊情况
     * 4. 性能分析：对于大数据量输入，使用性能分析工具监控时间和内存占用
     * 
     * 算法优化建议:
     * 1. 对于稀疏数据，可以使用压缩Trie减少空间占用
     * 2. 对于频繁查询的场景，可以增加缓存机制
     * 3. 可以使用位运算的优化技巧，如预计算位掩码
     * 4. 对于多线程环境，可以考虑使用无锁数据结构
     */
}