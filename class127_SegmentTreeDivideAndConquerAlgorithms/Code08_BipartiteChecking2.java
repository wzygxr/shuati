package class167;

/**
 * 线段树分治 + 扩展域并查集 解决动态二分图检测问题
 * 题目来源: Codeforces 813F - Bipartite Checking
 * <p>
 * 算法核心思想详解：
 * 1. 线段树分治：将时间轴划分为区间，每条边挂载到其有效时间段对应的线段树节点
 * 2. 扩展域并查集：维护二分图的双色性质，每个节点i拆分为i和i+n两个表示
 * 3. 可撤销操作：在DFS遍历线段树时，应用边并记录状态，回溯时撤销操作
 * 4. 离线处理：提前收集所有操作，按时间区间处理
 * <p>
 * 典型应用场景：
 * - 动态图维护：需要频繁添加和删除边的场景
 * - 二分图判定：在图的动态变化过程中判断是否保持二分性
 * - 时间敏感问题：边的有效性随时间变化的问题
 * <p>
 * 算法优势：
 * - 时间复杂度：O(q log q α(n))，其中α(n)是阿克曼函数的反函数，近似为常数
 * - 空间复杂度：O(n + q log q)
 * - 能够处理大规模动态图问题，支持高达10^5量级的节点和操作
 * <p>
 * 实现要点：
 * - 使用链式前向星存储线段树节点上的边
 * - 不使用路径压缩的并查集，以支持操作回滚
 * - 按秩合并策略，保持并查集的树高度较小
 * - 剪枝优化：一旦发现不是二分图，标记该区间内所有时间点
 */

import java.io.*;
import java.util.*;

public class Code08_BipartiteChecking2 {

    // 常量定义
    public static final int MAXN = 100001;    // 最大节点数
    public static final int MAXQ = 100001;    // 最大操作数
    public static final int MAXT = 500001;    // 最大线段树节点数
    
    // 输入输出优化相关变量
    private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    private static PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
    
    // 输入数据
    public static int n, q;
    public static int[] op = new int[MAXQ + 1]; // 操作类型
    public static int[] x = new int[MAXQ + 1]; // 操作涉及的节点x
    public static int[] y = new int[MAXQ + 1]; // 操作涉及的节点y
    
    // 事件数组：用于记录边的有效时间段
    public static int[][] event = new int[(MAXQ << 1) + 1][3]; // [x, y, time]
    public static int eventCnt = 0;
    
    // 扩展域并查集
    public static int[] father = new int[(MAXN << 1) + 1]; // 父节点数组，扩展为2n
    public static int[] size = new int[(MAXN << 1) + 1];   // 集合大小
    public static int[][] rollback = new int[MAXT + 1][2]; // 回滚栈
    public static int opsize = 0; // 操作计数
    
    // 线段树相关：链式前向星结构存储每个线段树节点上的边
    public static int[] head = new int[(MAXQ << 2) + 1]; // 头指针
    public static int[] next = new int[MAXT + 1];       // 下一条边
    public static int[] tox = new int[MAXT + 1];        // 边的x节点
    public static int[] toy = new int[MAXT + 1];        // 边的y节点
    public static int cnt = 0; // 边计数
    
    // 答案数组
    public static boolean[] ans = new boolean[MAXQ + 1];
    
    /**
     * 并查集查找操作 - 查找根节点（非路径压缩版本）
     * <p>
     * 实现原理：
     * - 从节点i开始，沿着父指针向上遍历，直到找到根节点（即父节点等于自身的节点）
     * - 使用迭代方式实现，避免递归的栈开销
     * <p>
     * 为什么不使用路径压缩：
     * - 路径压缩会改变树的结构，使得回滚操作变得复杂或不可行
     * - 在可撤销并查集中，需要保持操作的可逆性，因此只能采用原始的查找方式
     * - 虽然没有路径压缩，但配合按秩合并策略，时间复杂度仍能保持在O(α(n))
     * <p>
     * 与路径压缩版本的对比：
     * - 标准并查集：使用路径压缩，时间复杂度O(α(n))，但无法回滚
     * - 可撤销并查集：不使用路径压缩，时间复杂度O(log n)到O(α(n))之间，支持回滚
     * <p>
     * 时间复杂度：
     * - 平均情况：O(α(n))，其中α(n)是阿克曼函数的反函数，近似为常数
     * - 最坏情况：O(log n)，但在按秩合并的优化下，这种情况很少发生
     * <p>
     * 空间复杂度：O(1)
     * 
     * @param i 要查找的节点，范围为1~2n（扩展域）
     * @return 节点所在集合的根节点
     */
    public static int find(int i) {
        // 迭代查找根节点
        // 从节点i开始，沿着父指针向上遍历
        while (i != father[i]) {
            i = father[i]; // 移动到父节点
        }
        return i; // 返回根节点
    }
    
    /**
     * 扩展域并查集的合并操作 - 处理二分图约束条件
     * <p>
     * 算法原理：
     * - 扩展域并查集通过将每个节点拆分为两个表示（i和i+n）来维护二分图性质
     * - i表示节点i在集合A，i+n表示节点i在集合B
     * - 对于边(x,y)，在二分图中x和y必须属于不同的集合
     * - 因此需要合并x与y+n，同时合并y与x+n
     * <p>
     * 实现步骤：
     * 1. 检查x和y是否已经在同一集合（即x和y在二分图的同一侧）
     * 2. 如果在同一集合，说明添加这条边会导致奇环，不是二分图
     * 3. 否则，合并x与y+n，同时合并y与x+n
     * 4. 记录合并操作，以便后续回滚
     * <p>
     * 按秩合并优化：
     * - 将较小的集合合并到较大的集合中
     * - 保持树的高度较小，提高查找效率
     * - 记录合并的父节点和子节点，用于撤销操作
     * <p>
     * 时间复杂度：O(α(n))，主要由find操作决定
     * 空间复杂度：O(1)，但会使用rollback数组存储回滚信息
     * 
     * @param x 边的第一个节点
     * @param y 边的第二个节点
     * @return 合并是否成功。如果返回false，说明添加这条边后图不是二分图
     */
    public static boolean union(int x, int y) {
        // 查找x的根节点（x在集合A中的表示）
        int fx1 = find(x);
        // 查找y的根节点（y在集合A中的表示）
        int fy1 = find(y);
        
        // 关键检测：如果x和y已经在同一集合，说明添加边(x,y)会形成奇环
        // 这意味着图不再是二分图
        if (fx1 == fy1) {
            return false; // 合并失败，不是二分图
        }
        
        // 查找x在集合B中的根节点（x+n）
        int fx2 = find(x + n);
        // 查找y在集合B中的根节点（y+n）
        int fy2 = find(y + n);
        
        // 合并操作1：将x的集合A与y的集合B合并
        // 这表示：如果x在集合A，那么y必须在集合B
        if (fx1 != fy2) {
            // 按秩合并：总是将较小的集合合并到较大的集合中
            if (size[fx1] < size[fy2]) {
                // 交换fx1和fy2，确保fx1是较大集合的根
                int temp = fx1;
                fx1 = fy2;
                fy2 = temp;
            }
            // 执行合并：将较小集合的根指向较大集合的根
            father[fy2] = fx1;
            // 更新较大集合的大小
            size[fx1] += size[fy2];
            // 记录合并操作，用于回滚
            opsize++;
            rollback[opsize][0] = fx1;
            rollback[opsize][1] = fy2;
        }
        
        // 合并操作2：将y的集合A与x的集合B合并
        // 这表示：如果y在集合A，那么x必须在集合B
        if (fx2 != fy1) {
            // 同样使用按秩合并策略
            if (size[fx2] < size[fy1]) {
                int temp = fx2;
                fx2 = fy1;
                fy1 = temp;
            }
            father[fy1] = fx2;
            size[fx2] += size[fy1];
            opsize++;
            rollback[opsize][0] = fx2;
            rollback[opsize][1] = fy1;
        }
        
        // 合并成功，图仍然是二分图
        return true;
    }
    
    /**
     * 撤销合并操作 - 恢复并查集到合并前的状态
     * <p>
     * 算法原理：
     * - 线段树分治需要在DFS遍历过程中应用边，并在回溯时撤销这些操作
     * - 撤销操作是合并操作的逆过程，恢复被合并的节点到独立状态
     * - 利用rollback数组记录每次合并的父节点和子节点信息
     * <p>
     * 实现步骤：
     * 1. 获取最后一次合并操作的信息（从rollback数组中）
     * 2. 将子节点的父指针重新指向自己（恢复独立状态）
     * 3. 从父节点的大小中减去子节点的大小
     * 4. 减少操作计数器，指向下一个待撤销的操作
     * <p>
     * 关键点：
     * - 回滚必须按照与合并相反的顺序进行
     * - 由于合并操作记录了父节点和子节点的关系，撤销操作可以准确恢复状态
     * - 不使用路径压缩确保了回滚的正确性
     * <p>
     * 在线段树分治中的作用：
     * - 允许在不同的时间区间独立处理边的添加和删除
     * - 避免了为每个时间点维护独立的并查集状态
     * - 大大节省了空间复杂度，同时保持了时间效率
     * <p>
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    public static void undo() {
        // 获取最后一次合并操作的父节点fx和子节点fy
        int fx = rollback[opsize][0]; // 父节点（合并后的根）
        int fy = rollback[opsize][1]; // 子节点（被合并的根）
        
        // 减少操作计数器，指向下一个待撤销的操作
        opsize--;
        
        // 撤销操作1：将子节点的父指针重新指向自己，恢复独立状态
        father[fy] = fy;
        
        // 撤销操作2：从父节点的大小中减去子节点的大小，恢复正确的集合大小
        size[fx] -= size[fy];
    }
    
    /**
     * 向线段树节点添加边 - 使用链式前向星存储结构
     * <p>
     * 数据结构说明：
     * - 链式前向星是一种高效的邻接表实现，用于存储图的边
     * - 在线段树分治中，用于存储挂载在各个线段树节点上的边
     * <p>
     * 实现原理：
     * 1. 创建一个新的边节点，存储边的信息（tox, toy）
     * 2. 采用头插法，将新节点插入到对应线段树节点的边链表头部
     * 3. 通过next数组维护链表的连接关系
     * <p>
     * 优势：
     * - 时间复杂度：添加边的操作时间复杂度为O(1)
     * - 空间复杂度：总体O(q log q)，每条边被挂载到O(log q)个线段树节点上
     * - 遍历效率高：可以快速访问挂载在特定线段树节点上的所有边
     * <p>
     * 与其他存储方式对比：
     * - 相比使用List数组，链式前向星在频繁插入时性能更优
     * - 相比使用vector（C++），内存布局更紧凑，缓存命中率更高
     * 
     * @param i 线段树节点编号
     * @param x 边的第一个节点
     * @param y 边的第二个节点
     */
    public static void addEdge(int i, int x, int y) {
        next[++cnt] = head[i];
        tox[cnt] = x;
        toy[cnt] = y;
        head[i] = cnt;
    }
    
    /**
     * 线段树分治的核心操作：将边添加到对应时间段的线段树节点
     * <p>
     * 算法原理：
     * - 将时间轴视为线段树，每条边对应时间轴上的一个区间[jobl, jobr]
     * - 使用线段树区间更新的思想，将边挂载到覆盖该区间的线段树节点上
     * - 叶子节点对应单个时间点，非叶子节点对应时间区间
     * <p>
     * 实现步骤：
     * 1. 如果当前线段树节点完全包含在目标区间内，直接将边挂载到该节点
     * 2. 否则，递归地将边挂载到左子树和/或右子树
     * 3. 每条边最终会被挂载到O(log q)个线段树节点上
     * <p>
     * 时间复杂度：
     * - 单条边的添加操作：O(log q)
     * - 所有边的总时间：O(q log q)
     * <p>
     * 空间复杂度：
     * - 每条边被挂载到O(log q)个节点，总空间O(q log q)
     * <p>
     * 优化点：
     * - 使用链式前向星存储边，提高插入和遍历效率
     * - 线段树采用隐式存储，避免显式构建树结构
     * 
     * @param jobl 边的有效区间左端点
     * @param jobr 边的有效区间右端点
     * @param jobx 边的第一个节点
     * @param joby 边的第二个节点
     * @param l 当前线段树节点表示的区间左端点
     * @param r 当前线段树节点表示的区间右端点
     * @param i 当前线段树节点的编号
     */
    public static void add(int jobl, int jobr, int jobx, int joby, int l, int r, int i) {
        if (jobl <= l && r <= jobr) {
            addEdge(i, jobx, joby);
            return;
        }
        int mid = (l + r) >> 1;
        if (jobl <= mid) {
            add(jobl, jobr, jobx, joby, l, mid, i << 1);
        }
        if (jobr > mid) {
            add(jobl, jobr, jobx, joby, mid + 1, r, i << 1 | 1);
        }
    }
    
    /**
     * 线段树分治的深度优先遍历 - 核心算法实现
     * <p>
     * 算法原理：
     * - 从根节点开始深度优先遍历线段树
     * - 在进入节点时，应用该节点上挂载的所有边
     * - 递归处理子节点
     * - 回溯时撤销所有应用过的边，恢复并查集状态
     * <p>
     * 实现步骤：
     * 1. 应用当前线段树节点上的所有边，记录应用的操作数
     * 2. 检查应用过程中是否发现非二分图的情况
     * 3. 如果到达叶子节点（单个时间点），记录该时间点的结果
     * 4. 否则，如果仍然是二分图，递归处理左右子节点
     * 5. 如果不是二分图，标记该区间内所有时间点都不是二分图（剪枝优化）
     * 6. 回溯：撤销所有应用过的操作，恢复并查集状态
     * <p>
     * 核心思想：
     * - 时间旅行：通过回溯机制，模拟边的添加和删除
     * - 离线处理：提前将所有边挂载到时间线段树
     * - 剪枝优化：一旦发现非二分图，可以直接标记整个区间
     * <p>
     * 时间复杂度分析：
     * - 每条边被挂载到O(log q)个线段树节点上
     * - 每条边在每个挂载点被处理一次
     * - 处理每条边的时间为O(α(n))
     * - 总时间复杂度：O(q log q α(n))
     * <p>
     * 空间复杂度分析：
     * - 递归调用栈深度：O(log q)
     * - 回滚数组：O(q log q)
     * 
     * @param l 当前线段树节点表示的区间左端点
     * @param r 当前线段树节点表示的区间右端点
     * @param i 当前线段树节点的编号
     */
    public static void dfs(int l, int r, int i) {
        // 记录本次DFS应用的合并操作数量，用于回溯时撤销
        int unionCnt = 0;
        // 标记当前子图是否为二分图
        boolean isBipartite = true;
        
        // 步骤1：应用当前线段树节点上挂载的所有边
        // 遍历当前节点的边链表（链式前向星结构）
        for (int e = head[i]; e > 0 && isBipartite; e = next[e]) {
            // 尝试合并边的两个端点，使用扩展域并查集
            if (union(tox[e], toy[e])) {
                // 如果合并成功，记录操作计数（每次union最多执行2次合并）
                unionCnt += 2;
            } else {
                // 如果合并失败，说明添加该边后图不再是二分图
                isBipartite = false;
            }
        }
        
        // 步骤2：根据当前是否是叶子节点和是否是二分图来处理
        if (l == r) {
            // 情况1：叶子节点，对应单个时间点
            // 记录该时间点是否是二分图
            ans[l] = isBipartite;
        } else {
            // 情况2：非叶子节点，对应时间区间
            int mid = (l + r) >> 1;
            
            if (isBipartite) {
                // 子问题1：如果当前子图仍是二分图，递归处理左右子节点
                // 继续深度优先遍历左子树
                dfs(l, mid, i << 1);
                // 继续深度优先遍历右子树
                dfs(mid + 1, r, i << 1 | 1);
            } else {
                // 子问题2：如果当前子图不是二分图，进行剪枝优化
                // 标记该区间内的所有时间点都不是二分图
                // 避免不必要的递归，提高效率
                for (int k = l; k <= mid; k++) {
                    ans[k] = false;
                }
                for (int k = mid + 1; k <= r; k++) {
                    ans[k] = false;
                }
            }
        }
        
        // 步骤3：回溯操作 - 撤销本次DFS应用的所有合并操作
        // 按照与应用相反的顺序撤销
        for (int k = 1; k <= unionCnt; k++) {
            undo();
        }
    }
    
    /**
     * 准备阶段：初始化数据结构、处理事件、构建时间线段树
     * <p>
     * 功能概述：
     * - 初始化并查集，设置每个节点为自己的父节点
     * - 对事件进行排序，方便合并相同边的添加和删除操作
     * - 确定每条边的有效时间区间
     * - 将边添加到对应的线段树节点
     * <p>
     * 实现步骤详解：
     * 1. 初始化并查集：将每个节点（包括扩展域）的父节点设为自身，大小设为1
     * 2. 排序事件：按照边的两个端点和时间戳排序，便于后续处理
     * 3. 处理事件：遍历排序后的事件，确定每条边的有效时间区间
     * 4. 构建线段树：将边添加到其有效时间区间对应的线段树节点
     * <p>
     * 关键算法点：
     * - 事件合并：将相同边的添加和删除操作配对处理
     * - 时间区间计算：对于添加事件，找到对应的删除事件（如果有）来确定有效区间
     * - 离线处理：提前处理所有操作，将问题转化为时间线段树上的边处理问题
     * <p>
     * 时间复杂度分析：
     * - 并查集初始化：O(n)
     * - 事件排序：O(q log q)
     * - 事件处理和线段树构建：O(q log q)
     * - 总体时间复杂度：O(q log q)
     * <p>
     * 空间复杂度分析：
     * - 并查集数组：O(n)
     * - 线段树和边存储：O(q log q)
     */
    public static void prepare() {
        // 步骤1：初始化并查集
        // 每个节点i有两个表示：i（集合A）和i+n（集合B）
        for (int i = 1; i <= (n << 1); i++) {
            father[i] = i;  // 初始时，每个节点的父节点是自身
            size[i] = 1;    // 初始时，每个集合的大小为1
        }
        
        // 步骤2：对事件进行排序
        // 排序规则：先按边的x节点，再按y节点，最后按时间戳
        // 这样可以将相同边的添加和删除操作集中在一起处理
        Arrays.sort(event, 1, eventCnt + 1, (a, b) -> {
            if (a[0] != b[0]) return a[0] - b[0];      // 优先按x节点排序
            if (a[1] != b[1]) return a[1] - b[1];      // 其次按y节点排序
            return a[2] - b[2];                        // 最后按时间戳排序
        });
        
        // 步骤3：处理事件，确定每条边的有效时间区间
        int x, y, start, end;
        // 双指针法遍历排序后的事件
        for (int l = 1, r = 1; l <= eventCnt; l = ++r) {
            x = event[l][0];  // 边的第一个节点
            y = event[l][1];  // 边的第二个节点
            
            // 合并相同的边：找到所有相同边的事件
            while (r + 1 <= eventCnt && event[r + 1][0] == x && event[r + 1][1] == y) {
                r++;  // 移动右指针，扩大相同边事件的范围
            }
            
            // 处理每条边的添加和删除事件对
            // 每次跳过2个事件：添加事件和对应的删除事件
            for (int i = l; i <= r; i += 2) {
                start = event[i][2];  // 边的开始时间（添加操作的时间戳）
                // 边的结束时间：如果有对应的删除操作，则为删除操作时间减1；否则持续到最后
                end = (i + 1 <= r) ? (event[i + 1][2] - 1) : q;
                
                // 步骤4：将边添加到线段树中对应的时间区间
                add(start, end, x, y, 0, q, 1);
            }
        }
    }
    
    /**
     * 主函数：程序入口，协调整个算法流程
     * <p>
     * 功能概述：
     * - 读取输入数据（节点数、操作数和具体操作）
     * - 预处理操作，转换为事件格式
     * - 调用算法核心函数处理
     * - 输出结果
     * <p>
     * 实现步骤详解：
     * 1. 读取节点数n和操作数q
     * 2. 读取每个操作，记录为事件
     * 3. 预处理：构建时间线段树，准备离线数据
     * 4. 执行线段树分治算法，检测每个时间点的二分图性质
     * 5. 输出每个操作后的结果（该时刻图是否为二分图）
     * <p>
     * 输入处理细节：
     * - 操作类型：1表示添加边，0表示删除边
     * - 对每条边，确保x <= y，避免重复存储
     * - 将每个操作记录为事件，存储边的两个端点和时间戳
     * <p>
     * 输入输出优化：
     * - 使用BufferedReader和BufferedWriter提高IO效率
     * - 使用StringTokenizer解析输入数据
     * <p>
     * 数据规模处理：
     * - 支持n和q最大为10^5的大规模数据
     * - 内存预分配以适应大数据量
     * 
     * @param args 命令行参数（未使用）
     * @throws IOException 输入输出异常
     */
    public static void main(String[] args) throws IOException {
        // 步骤1：读取输入数据
        StringTokenizer st = new StringTokenizer(in.readLine());
        n = Integer.parseInt(st.nextToken());  // 节点数
        q = Integer.parseInt(st.nextToken());  // 操作数
        
        // 步骤2：读取每个操作，记录为事件
        for (int i = 1; i <= q; i++) {
            st = new StringTokenizer(in.readLine());
            op[i] = Integer.parseInt(st.nextToken());  // 操作类型（1添加边，0删除边）
            x[i] = Integer.parseInt(st.nextToken());   // 边的第一个节点
            y[i] = Integer.parseInt(st.nextToken());   // 边的第二个节点
            
            // 优化处理：确保x <= y，避免重复存储相同的边
            if (x[i] > y[i]) {
                int temp = x[i];
                x[i] = y[i];
                y[i] = temp;
            }
            
            // 记录事件：将每个操作转换为事件格式
            // event[事件索引][0] = x坐标
            // event[事件索引][1] = y坐标
            // event[事件索引][2] = 时间戳（操作序号）
            event[++eventCnt][0] = x[i];
            event[eventCnt][1] = y[i];
            event[eventCnt][2] = i;
        }
        
        // 步骤3：准备阶段 - 构建时间线段树，处理事件
        prepare();
        
        // 步骤4：执行线段树分治算法
        // 从根节点(1)开始深度优先遍历，处理整个时间区间[0, q]
        dfs(0, q, 1);
        
        // 步骤5：输出结果
        // ans[i]表示第i个操作后图是否为二分图
        for (int i = 1; i <= q; i++) {
            out.println(ans[i] ? "YES" : "NO");
        }
        
        // 关闭IO流，释放资源
        out.flush();
        in.close();
        out.close();
    }
    
    /*
     * 算法时间复杂度分析：
     * - 排序事件: O(q log q)
     * - 线段树分治: O(q log q α(n))，其中α(n)是阿克曼函数的反函数，近似为常数
     * - 整体时间复杂度: O(q log q α(n))
     * 
     * 空间复杂度分析：
     * - 并查集数组: O(n)
     * - 线段树和事件数组: O(q log q)
     * - 整体空间复杂度: O(n + q log q)
     */
}

//#include <bits/stdc++.h>
//
//using namespace std;
//
//const int MAXN = 100001;
//const int MAXQ = 100001;
//const int MAXT = 500001;
//int n, q;
//
//int event[MAXN << 1][3];
//int eventCnt;
//
//int op[MAXQ];
//int x[MAXQ];
//int y[MAXQ];
//
//int father[MAXN << 1];
//int siz[MAXN << 1];
//int rollback[MAXN][2];
//int opsize = 0;
//
//int head[MAXQ << 2];
//int next[MAXT];
//int tox[MAXT];
//int toy[MAXT];
//int cnt = 0;
//
//bool ans[MAXQ];
//
//int find(int i) {
//    while (i != father[i]) {
//        i = father[i];
//    }
//    return i;
//}
//
//bool union(int x, int y) {
//    int fx1 = find(x);
//    int fy1 = find(y);
//    // 如果x的左侧和y的左侧已经在同一集合，说明不是二分图
//    if (fx1 == fy1) {
//        return false;
//    }
//    int fx2 = find(x + n);
//    int fy2 = find(y + n);
//    // 合并x的左侧与y的右侧
//    if (fx1 != fy2) {
//        if (siz[fx1] < siz[fy2]) {
//            swap(fx1, fy2);
//        }
//        father[fy2] = fx1;
//        siz[fx1] += siz[fy2];
//        opsize++;
//        rollback[opsize][0] = fx1;
//        rollback[opsize][1] = fy2;
//    }
//    // 合并y的左侧与x的右侧
//    if (fx2 != fy1) {
//        if (siz[fx2] < siz[fy1]) {
//            swap(fx2, fy1);
//        }
//        father[fy1] = fx2;
//        siz[fx2] += siz[fy1];
//        opsize++;
//        rollback[opsize][0] = fx2;
//        rollback[opsize][1] = fy1;
//    }
//    return true;
//}
//
//void undo() {
//    int fx = rollback[opsize][0];
//    int fy = rollback[opsize][1];
//    opsize--;
//    father[fy] = fy;
//    siz[fx] -= siz[fy];
//}
//
//void addEdge(int i, int x, int y) {
//    next[++cnt] = head[i];
//    tox[cnt] = x;
//    toy[cnt] = y;
//    head[i] = cnt;
//}
//
//void add(int jobl, int jobr, int jobx, int joby, int l, int r, int i) {
//    if (jobl <= l && r <= jobr) {
//        addEdge(i, jobx, joby);
//    } else {
//        int mid = (l + r) >> 1;
//        if (jobl <= mid) {
//            add(jobl, jobr, jobx, joby, l, mid, i << 1);
//        }
//        if (jobr > mid) {
//            add(jobl, jobr, jobx, joby, mid + 1, r, i << 1 | 1);
//        }
//    }
//}
//
//void dfs(int l, int r, int i) {
//    int unionCnt = 0;
//    bool isBipartite = true;
//    for (int e = head[i]; e > 0 && isBipartite; e = next[e]) {
//        if (union(tox[e], toy[e])) {
//            unionCnt += 2;
//        } else {
//            isBipartite = false;
//        }
//    }
//    if (l == r) {
//        ans[l] = isBipartite;
//    } else {
//        int mid = (l + r) >> 1;
//        if (isBipartite) {
//            dfs(l, mid, i << 1);
//            dfs(mid + 1, r, i << 1 | 1);
//        } else {
//            // 如果当前区间不是二分图，那么所有子区间都不是二分图
//            for (int k = l; k <= mid; k++) {
//                ans[k] = false;
//            }
//            for (int k = mid + 1; k <= r; k++) {
//                ans[k] = false;
//            }
//        }
//    }
//    // 撤销操作
//    for (int k = 1; k <= unionCnt; k++) {
//        undo();
//    }
//}
//
//void prepare() {
//    for (int i = 1; i <= (n << 1); i++) {
//        father[i] = i;
//        siz[i] = 1;
//    }
//    sort(event + 1, event + eventCnt + 1, [](int* a, int* b) {
//        if (a[0] != b[0]) return a[0] < b[0];
//        if (a[1] != b[1]) return a[1] < b[1];
//        return a[2] < b[2];
//    });
//    int x, y, start, end;
//    for (int l = 1, r = 1; l <= eventCnt; l = ++r) {
//        x = event[l][0];
//        y = event[l][1];
//        while (r + 1 <= eventCnt && event[r + 1][0] == x && event[r + 1][1] == y) {
//            r++;
//        }
//        for (int i = l; i <= r; i += 2) {
//            start = event[i][2];
//            end = i + 1 <= r ? (event[i + 1][2] - 1) : q;
//            add(start, end, x, y, 0, q, 1);
//        }
//    }
//}
//
//int main() {
//    ios::sync_with_stdio(false);
//    cin.tie(nullptr);
//    cin >> n >> q;
//    eventCnt = 0;
//    for (int i = 1; i <= q; i++) {
//        cin >> op[i] >> x[i] >> y[i];
//        if (x[i] > y[i]) {
//            swap(x[i], y[i]);
//        }
//        event[++eventCnt][0] = x[i];
//        event[eventCnt][1] = y[i];
//        event[eventCnt][2] = i;
//    }
//    prepare();
//    dfs(0, q, 1);
//    for (int i = 1; i <= q; i++) {
//        cout << (ans[i] ? "YES" : "NO") << '\n';
//    }
//    return 0;
//}