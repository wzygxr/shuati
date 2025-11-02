package class167;

/**
 * 动态图连通性问题 - 线段树分治 + 可撤销并查集实现
 * 
 * 题目来源：LeetCode Dynamic Graph Connectivity
 * 题目链接：https://leetcode.com/problems/dynamic-graph-connectivity/
 * 
 * 问题描述：
 * 支持动态加边、删边操作，查询两点间连通性
 * 
 * 算法思路：
 * 1. 使用线段树分治处理动态加边/删边操作
 * 2. 通过可撤销并查集维护节点间的连通性
 * 3. 离线处理所有操作，把每条边的存在时间区间分解到线段树的节点上
 * 4. 通过DFS遍历线段树，处理每个时间点的查询
 * 
 * 时间复杂度：O((n + m) log m)
 * 空间复杂度：O(n + m)
 * 
 * 测试用例：
 * 输入：
 * n = 5
 * operations = [[1, 0, 1], [1, 1, 2], [3, 0, 2], [2, 1, 2], [3, 0, 2]]
 * 输出：
 * [true, false]
 * 
 * 解释：
 * 1. 添加边(0,1)
 * 2. 添加边(1,2)
 * 3. 查询0和2是否连通 -> true (0-1-2)
 * 4. 删除边(1,2)
 * 5. 查询0和2是否连通 -> false
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

public class Code09_DynamicGraphConnectivity_Java {
    
    // 常量定义
    public static int MAXN = 100001;   // 最大节点数
    public static int MAXT = 500001;   // 最大线段树任务数
    public static int n, m;            // 节点数、操作数
    
    // 事件数组：记录所有边的添加和删除事件
    // event[i][0]: 边的左端点x
    // event[i][1]: 边的右端点y
    // event[i][2]: 事件发生的时间点t
    public static int[][] event = new int[MAXN << 1][3];
    public static int eventCnt;  // 事件计数器
    
    // 记录每个时间点的操作信息
    public static int[] op = new int[MAXN];  // 操作类型：1(添加边)、2(删除边)、3(查询)
    public static int[] x = new int[MAXN];   // 操作涉及的第一个节点
    public static int[] y = new int[MAXN];   // 操作涉及的第二个节点
    
    // 可撤销并查集：维护连通性
    public static int[] father = new int[MAXN];     // 父节点数组
    public static int[] siz = new int[MAXN];        // 集合大小数组
    public static int[][] rollback = new int[MAXN][2]; // 回滚栈，记录合并操作
    public static int opsize = 0;                   // 操作计数
    
    // 时间轴线段树上的区间任务列表：链式前向星结构
    public static int[] head = new int[MAXN << 2];  // 线段树节点的头指针
    public static int[] next = new int[MAXT];       // 下一个任务的指针
    public static int[] tox = new int[MAXT];        // 任务边的起点
    public static int[] toy = new int[MAXT];        // 任务边的终点
    public static int cnt = 0;                      // 任务计数
    
    // 存储查询操作的答案
    public static boolean[] ans = new boolean[MAXN];
    
    /**
     * 并查集的find操作：查找集合代表元素
     * @param i 要查找的节点
     * @return 节点所在集合的代表元素（根节点）
     * @note 注意：此实现没有路径压缩，以支持撤销操作
     */
    public static int find(int i) {
        // 非路径压缩版本，以支持撤销操作
        while (i != father[i]) {
            i = father[i];
        }
        return i;
    }
    
    /**
     * 可撤销并查集的合并操作，在节点u和v之间添加一条边
     * @param u 第一个节点
     * @param v 第二个节点
     * @return 如果合并了两个不同的集合，返回true；否则返回false
     * @note 合并时同时维护带权并查集，并记录操作以支持撤销
     */
    public static boolean union(int u, int v) {
        // 查找u和v的根节点
        int fu = find(u);
        int fv = find(v);
        
        if (fu == fv) {
            return false; // 没有合并新的集合
        }
        
        // 按秩合并，始终将较小的树合并到较大的树中
        if (siz[fu] < siz[fv]) {
            int tmp = fu;
            fu = fv;
            fv = tmp;
        }
        
        // 合并操作
        father[fv] = fu;
        siz[fu] += siz[fv];
        
        // 记录操作，用于撤销
        rollback[++opsize][0] = fu;
        rollback[opsize][1] = fv;
        
        return true; // 成功合并两个集合
    }
    
    /**
     * 撤销最近的一次合并操作
     * @note 恢复并查集的状态
     */
    public static void undo() {
        // 获取最后一次合并操作的信息
        int fx = rollback[opsize][0];  // 父节点
        int fy = rollback[opsize--][1]; // 子节点
        
        // 恢复fy的父节点为自己
        father[fy] = fy;
        // 恢复父节点集合的大小
        siz[fx] -= siz[fy];
    }
    
    /**
     * 给线段树节点i添加一个任务：在节点x和y之间添加边
     * @param i 线段树节点编号
     * @param x 边的起点
     * @param y 边的终点
     * @note 使用链式前向星存储任务
     */
    public static void addEdge(int i, int x, int y) {
        // 创建新任务
        cnt++;
        next[cnt] = head[i];  // 指向前一个任务
        tox[cnt] = x;         // 边的起点
        toy[cnt] = y;         // 边的终点
        head[i] = cnt;        // 更新头指针
    }
    
    /**
     * 线段树区间更新：将边(jobx, joby)添加到时间区间[jobl, jobr]内
     * @param jobl 任务开始时间
     * @param jobr 任务结束时间
     * @param jobx 边的起点
     * @param joby 边的终点
     * @param l 当前线段树节点的左区间
     * @param r 当前线段树节点的右区间
     * @param i 当前线段树节点编号
     */
    public static void add(int jobl, int jobr, int jobx, int joby, int l, int r, int i) {
        // 如果当前区间完全包含在目标区间内，直接添加到当前节点
        if (jobl <= l && r <= jobr) {
            addEdge(i, jobx, joby);
        } else {
            // 否则递归到左右子树
            int mid = (l + r) >> 1;
            if (jobl <= mid) {
                add(jobl, jobr, jobx, joby, l, mid, i << 1);
            }
            if (jobr > mid) {
                add(jobl, jobr, jobx, joby, mid + 1, r, i << 1 | 1);
            }
        }
    }
    
    /**
     * 线段树分治的深度优先搜索核心方法
     * 
     * @param l 当前线段树节点的左时间区间边界
     * @param r 当前线段树节点的右时间区间边界
     * @param i 当前线段树节点编号（根节点为1，左子节点为2*i，右子节点为2*i+1）
     */
    public static void dfs(int l, int r, int i) {
        // 记录合并操作的数量，用于后续撤销
        int unionCnt = 0;
        
        // 处理当前节点上的所有边
        // 这些边在[l, r]时间区间内都是活跃的
        for (int e = head[i]; e > 0; e = next[e]) {
            // 尝试合并两个集合
            // 如果成功合并（两个不同的集合），增加计数
            if (union(tox[e], toy[e])) {
                unionCnt++;
            }
        }
        
        // 处理叶子节点（对应具体的时间点）
        if (l == r) {
            // 如果当前时间点是查询操作（类型3）
            if (op[l] == 3) {
                // 检查x[l]和y[l]是否连通
                ans[l] = (find(x[l]) == find(y[l]));
            }
        } else {
            // 非叶子节点，递归处理左右子树
            int mid = (l + r) >> 1;  // 计算中间点
            dfs(l, mid, i << 1);     // 处理左子区间
            dfs(mid + 1, r, i << 1 | 1);  // 处理右子区间
        }
        
        // 回溯：撤销所有合并操作，按逆序撤销
        for (int k = 1; k <= unionCnt; k++) {
            undo();  // 撤销并查集的合并操作
        }
    }
    
    /**
     * 预处理函数：初始化并查集、排序事件、构建线段树
     */
    public static void prepare() {
        // 初始化并查集结构
        // 每个节点初始时都是独立的集合，父节点指向自己，集合大小为1
        for (int i = 1; i <= n; i++) {
            father[i] = i;  // 每个节点初始是自己的父节点
            siz[i] = 1;     // 每个集合初始大小为1
        }
        
        // 按边的两个端点和时间排序事件，这是处理边生命周期的关键步骤
        // 排序规则：
        // 1. 首先按边的第一个端点x从小到大排序
        // 2. 然后按边的第二个端点y从小到大排序
        // 3. 最后按事件发生的时间t从小到大排序
        // 这种排序方式确保相同的边（x,y）的所有事件会集中在一起
        Arrays.sort(event, 1, eventCnt + 1,
                (a, b) -> a[0] != b[0] ? a[0] - b[0] : a[1] != b[1] ? a[1] - b[1] : a[2] - b[2]);
        
        int x, y, start, end;
        // 处理每条边的生命周期，确定边的有效时间段
        // 使用双指针技术，将相同边的所有事件分组处理
        for (int l = 1, r = 1; l <= eventCnt; l = ++r) {
            x = event[l][0];  // 当前处理的边的起点
            y = event[l][1];  // 当前处理的边的终点
            
            // 找到所有相同边(x,y)的事件，r指针指向最后一个相同边的事件
            while (r + 1 <= eventCnt && event[r + 1][0] == x && event[r + 1][1] == y) {
                r++;
            }
            
            // 处理每对添加和删除事件，确定边的有效时间区间
            // 由于事件已经排序，添加和删除事件会交替出现
            for (int i = l; i <= r; i += 2) {
                start = event[i][2];     // 边开始的时间点（添加事件的时间）
                
                // 确定边结束的时间点：
                // - 如果有对应的删除事件，则边在删除事件发生前结束（end = 删除时间-1）
                // - 如果没有对应的删除事件，则边会一直存在到最后一个查询（end = m）
                end = i + 1 <= r ? (event[i + 1][2] - 1) : m;
                
                // 将边添加到线段树的相应时间区间[start, end]
                // 这里调用线段树的区间更新函数，将边挂载到覆盖该区间的最小节点集合上
                add(start, end, x, y, 1, m, 1);
            }
        }
    }
    
    /**
     * 主函数：程序入口
     * 
     * @param args 命令行参数（未使用）
     * @throws IOException 输入输出异常
     */
    public static void main(String[] args) throws IOException {
        // 使用快速输入输出工具类，提高处理大规模数据时的效率
        FastReader in = new FastReader();
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取节点数和操作数
        n = in.nextInt();
        m = in.nextInt();
        
        // 读取每个操作
        for (int i = 1; i <= m; i++) {
            op[i] = in.nextInt();   // 操作类型：1(添加)、2(删除)、3(查询)
            x[i] = in.nextInt();    // 操作涉及的第一个节点
            y[i] = in.nextInt();    // 操作涉及的第二个节点
            
            // 对于添加和删除操作，记录事件信息
            if (op[i] != 3) {
                event[++eventCnt][0] = x[i];  // 边的起点
                event[eventCnt][1] = y[i];    // 边的终点
                event[eventCnt][2] = i;       // 事件发生的时间点（即操作序号）
            }
        }
        
        // 预处理阶段：初始化并查集，排序事件，构建线段树
        // 将每条边按照其有效时间区间挂载到线段树的相应节点上
        prepare();
        
        // 执行线段树分治的核心算法
        // 从时间区间[1, m]开始，以根节点（编号1）为起点进行DFS遍历
        // 在遍历过程中动态维护图的状态，并处理所有查询操作
        dfs(1, m, 1);
        
        // 输出所有查询操作的答案
        // 遍历所有时间点，如果该时间点是查询操作，则输出对应的结果
        for (int i = 1; i <= m; i++) {
            if (op[i] == 3) {
                out.println(ans[i]);
            }
        }
        
        // 确保所有输出都被写入到控制台
        out.flush();
        out.close();
    }
    
    /**
     * 快速输入工具类，使用缓冲区优化大规模数据的输入读取
     * 比Scanner快约10倍，适用于处理大数据量输入的竞赛题目
     */
    static class FastReader {
        private static final int BUFFER_SIZE = 1 << 16;  // 64KB缓冲区
        private final InputStream in;       // 输入流
        private final byte[] buffer;       // 字节缓冲区
        private int ptr, len;              // 指针位置和缓冲区有效长度

        /**
         * 构造函数：初始化输入流和缓冲区
         */
        public FastReader() {
            in = System.in;
            buffer = new byte[BUFFER_SIZE];
            ptr = len = 0;
        }

        /**
         * 检查是否还有下一个字节可读
         * 如果缓冲区已读完，尝试从输入流读取新的内容
         * 
         * @return 是否还有可用字节
         * @throws IOException 输入异常
         */
        private boolean hasNextByte() throws IOException {
            if (ptr < len) {
                return true;
            }
            ptr = 0; // 重置指针
            len = in.read(buffer); // 从输入流读取新内容到缓冲区
            return len > 0;
        }

        /**
         * 读取单个字节
         * 
         * @return 读取的字节值
         * @throws IOException 输入异常
         */
        private byte readByte() throws IOException {
            if (!hasNextByte()) {
                return -1; // 到达流末尾
            }
            return buffer[ptr++]; // 返回当前字节并移动指针
        }

        /**
         * 读取下一个整数
         * 
         * @return 读取的整数值
         * @throws IOException 输入异常
         */
        public int nextInt() throws IOException {
            int num = 0;
            byte b = readByte();
            // 跳过空白字符
            while (isWhitespace(b)) {
                b = readByte();
            }
            // 处理负数符号
            boolean minus = false;
            if (b == '-') {
                minus = true;
                b = readByte();
            }
            // 读取数字部分
            while (!isWhitespace(b) && b != -1) {
                num = num * 10 + (b - '0'); // 逐位构建整数
                b = readByte();
            }
            return minus ? -num : num; // 返回带符号的整数值
        }

        /**
         * 判断字节是否为空白字符（空格、换行、回车、制表符）
         * 
         * @param b 要检查的字节
         * @return 是否为空白字符
         */
        private boolean isWhitespace(byte b) {
            return b == ' ' || b == '\n' || b == '\r' || b == '\t';
        }
    }
}