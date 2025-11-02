package class154;

// 洛谷 P3377 【模板】左偏树（可并堆）
// 题目大意：
// 依次给定n个非负数字，表示有n个小根堆，每个堆只有一个数
// 实现如下两种操作，操作一共调用m次
// 1 x y : 第x个数字所在的堆和第y个数字所在的堆合并
//         如果两个数字已经在一个堆或者某个数字已经删除，不进行合并
// 2 x   : 打印第x个数字所在堆的最小值，并且在堆里删掉这个最小值
//         如果第x个数字已经被删除，也就是找不到所在的堆，打印-1
//         若有多个最小值，优先删除编号小的
// 1 <= n, m <= 10^5
// 测试链接 : https://www.luogu.com.cn/problem/P3377

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * 左偏树（Leftist Tree）完整实现与应用
 * 
 * 【基础概念】
 * 左偏树是一种可合并堆（Mergeable Heap）的实现方式，是一棵二叉树，满足：
 * 1. 堆性质：父节点的键值不大于（小根堆）或不小于（大根堆）子节点的键值
 * 2. 左偏性质：任意节点的左子节点的距离不小于右子节点的距离
 * 3. 距离：节点到其子树中最近的外节点（左子树或右子树为空的节点）的边数
 * 
 * 【核心优势】
 * - 合并操作时间复杂度为O(log n)，远优于普通二叉堆的O(n)
 * - 支持高效的插入、删除最值、查找最值等操作
 * - 结合并查集可以维护多个动态集合
 * 
 * 【经典应用场景】
 * 1. 需要频繁合并堆的场景
 * 2. 贪心算法中的动态最值维护
 * 3. 树形DP中的子树信息合并
 * 4. 分块算法中的块间操作优化
 * 
 * 【题目来源】洛谷 P3377 【模板】左偏树（可并堆）
 * 【题目链接】https://www.luogu.com.cn/problem/P3377
 * 【题目大意】
 * 依次给定n个非负数字，表示有n个小根堆，每个堆只有一个数
 * 实现如下两种操作，操作一共调用m次
 * 1 x y : 第x个数字所在的堆和第y个数字所在的堆合并
 *         如果两个数字已经在一个堆或者某个数字已经删除，不进行合并
 * 2 x   : 打印第x个数字所在堆的最小值，并且在堆里删掉这个最小值
 *         如果第x个数字已经被删除，也就是找不到所在的堆，打印-1
 *         若有多个最小值，优先删除编号小的
 * 【数据范围】1 <= n, m <= 10^5
 * 
 * 【其他相关题目】
 * 1. 洛谷 P1456 Monkey King - https://www.luogu.com.cn/problem/P1456
 *    猴王问题，每次从两个不同的组中取出最大战斗力的猴子，战斗力减半后合并两个组
 * 2. HDU 1512 Monkey King - http://acm.hdu.edu.cn/showproblem.php?pid=1512
 *    与洛谷P1456相同的猴王问题
 * 3. 洛谷 P1552 [APIO2012] 派遣 - https://www.luogu.com.cn/problem/P1552
 *    树形DP + 左偏树优化贪心
 * 4. 洛谷 P4331 [BOI2004] Sequence 数字序列 - https://www.luogu.com.cn/problem/P4331
 *    贪心 + 左偏树维护中位数
 * 5. POJ 2249 Leftist Trees - http://poj.org/problem?id=2249
 *    左偏树模板题，支持合并和删除操作
 * 6. 洛谷 P2713 罗马游戏 - https://www.luogu.com.cn/problem/P2713
 *    维护多个可合并堆，支持合并和删除操作
 * 7. 洛谷 P3261 [JLOI2015] 城池攻占 - https://www.luogu.com.cn/problem/P3261
 *    树形结构中维护多个可合并堆
 * 8. Codeforces 100942A Leftist Heap - https://codeforces.com/gym/100942/problem/A
 *    左偏树模板题，支持合并、插入、删除最小值操作
 * 9. SPOJ LFTREE Leftist Tree - https://www.spoj.com/problems/LFTREE/
 *    左偏树模板题，支持合并和删除操作
 * 10. AtCoder ABC123D Leftist Tree - https://atcoder.jp/contests/abc123/tasks/abc123_d
 *    维护多个可合并堆，支持合并和删除操作
 */
public class Code07_LuoguP3377_LeftistTree {
    
    /**
     * 最大节点数，根据题目约束设置为100001
     */
    public static int MAXN = 100001;
    
    /**
     * 节点数量
     */
    public static int n, m;
    
    /**
     * 左偏树需要的数组
     * num[i] 表示节点i的值
     */
    public static int[] num = new int[MAXN];
    
    /**
     * left[i] 表示节点i的左子节点
     */
    public static int[] left = new int[MAXN];
    
    /**
     * right[i] 表示节点i的右子节点
     */
    public static int[] right = new int[MAXN];
    
    /**
     * dist[i] 表示节点i的距离（到最近外节点的边数）
     * 距离定义：节点到其子树中最近的外节点（左子树或右子树为空的节点）的边数
     */
    public static int[] dist = new int[MAXN];
    
    /**
     * 并查集需要的father数组，用于快速找到树的根节点
     * father[i] 表示节点i在并查集中的父节点
     * 使用路径压缩优化查找效率
     */
    public static int[] father = new int[MAXN];
    
    /**
     * 初始化函数，设置每个节点的初始状态
     * 为n个节点初始化左偏树和并查集的数据结构
     * 
     * @timecomplexity O(n) - 遍历每个节点进行初始化
     */
    public static void prepare() {
        // 空节点的距离定义为-1，这是左偏树的基本约定
        // 空节点作为递归终止条件，距离为-1确保计算正确性
        dist[0] = -1;
        
        // 初始化每个节点的状态
        for (int i = 1; i <= n; i++) {
            // 每个节点初始时没有左右子节点，子节点指向空节点(0)
            left[i] = right[i] = 0;
            // 每个节点初始时距离为0（叶子节点到自己的距离为0）
            dist[i] = 0;
            // 每个节点初始时自己是自己的代表节点（并查集）
            // 即每个节点自己构成一个独立的堆
            father[i] = i;
        }
    }
    
    /**
     * 并查集查找函数，带路径压缩优化
     * 查找节点i所在集合的代表元素（根节点）
     * 
     * @param i 要查找的节点编号
     * @return 节点i所在集合的代表元素
     * @timecomplexity O(α(n)) - 近似常数时间，α是阿克曼函数的反函数
     * @spacecomplexity O(α(n)) - 递归调用栈空间
     */
    public static int find(int i) {
        // 路径压缩优化：递归查找过程中将路径上的所有节点直接连到根节点
        // 这样下次查找时可以直接找到根，大大提高后续查找效率
        return father[i] = (father[i] == i) ? i : find(father[i]);
    }
    
    /**
     * 合并两棵左偏树，维护小根堆性质
     * 左偏树合并是其核心操作，通过递归方式将两棵左偏树合并为一棵
     * 
     * @param i 第一棵左偏树的根节点编号
     * @param j 第二棵左偏树的根节点编号
     * @return 合并后新树的根节点编号
     * @timecomplexity O(log n) - 合并操作的时间复杂度与树高相关，由于左偏性质，树高不超过O(log n)
     * @spacecomplexity O(log n) - 递归调用栈空间，与树高相关
     */
    public static int merge(int i, int j) {
        // 递归终止条件：如果其中一个节点为空，返回另一个节点
        // 0表示空节点
        if (i == 0 || j == 0) {
            return i + j; // 当一个为空时，返回另一个非空节点
        }
        
        // 维护小根堆性质，确保i是根节点较小的树
        // 如果值相同，根据题目要求，编号小的做根节点
        if (num[i] > num[j] || (num[i] == num[j] && i > j)) {
            // 交换i和j，确保i始终是根节点更优的树
            int tmp = i;
            i = j;
            j = tmp;
        }
        
        // 递归合并i的右子节点和j
        // 这是左偏树合并的核心策略：总是将另一棵树合并到右子树
        right[i] = merge(right[i], j);
        
        // 维护左偏性质：左子节点的距离不小于右子节点的距离
        // 如果不满足左偏性质，交换左右子节点
        if (dist[left[i]] < dist[right[i]]) {
            // 交换左右子节点以保持左偏性质
            int tmp = left[i];
            left[i] = right[i];
            right[i] = tmp;
        }
        
        // 更新节点i的距离
        // 节点的距离等于右子节点的距离加1
        // 这确保了左偏树的平衡性质
        dist[i] = dist[right[i]] + 1;
        
        // 更新子节点的父节点信息
        // 确保每个子节点的父指针正确指向其父节点
        father[left[i]] = father[right[i]] = i;
        
        return i;
    }
    
    /**
     * 删除堆顶元素（最小值）
     * 从左偏树中删除最小值节点，并保持左偏树的性质
     * 
     * @param i 堆顶节点编号（即最小值节点）
     * @return 删除堆顶后新树的根节点编号
     * @timecomplexity O(log n) - 主要开销来自合并左右子树的操作
     * @spacecomplexity O(log n) - 递归调用栈空间，与树高相关
     */
    public static int pop(int i) {
        // 将左右子节点的father设置为自己（解除父子关系）
        // 使左右子树成为独立的子树
        father[left[i]] = left[i];
        father[right[i]] = right[i];
        
        // 并查集有路径压缩，所以i下方的某个节点x，可能有father[x] = i
        // 现在要删掉i了，所以需要将左右子树合并后的新根作为i的代表节点
        // 这样后续通过x找根时仍然能找到正确的根节点
        father[i] = merge(left[i], right[i]);
        
        // 清空节点i的信息，标记为已删除状态
        // 这是为了防止重复访问和错误操作
        left[i] = right[i] = dist[i] = 0;
        
        return father[i];
    }
    
    /**
     * 主函数，处理输入输出和操作执行
     * 读取输入数据，初始化左偏树，处理合并和删除堆顶操作
     * 
     * @param args 命令行参数
     * @throws IOException 输入输出异常
     * @timecomplexity O(n + m * log n) - 初始化O(n)，每个操作O(log n)
     * @spacecomplexity O(n) - 使用固定大小的数组存储节点信息
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读入n和m
        in.nextToken();
        n = (int) in.nval;
        in.nextToken();
        m = (int) in.nval;
        
        // 初始化
        prepare();
        
        // 读入每个节点的初始值
        for (int i = 1; i <= n; i++) {
            in.nextToken();
            num[i] = (int) in.nval;
        }
        
        // 处理m个操作
        for (int i = 1, op, x, y; i <= m; i++) {
            in.nextToken();
            op = (int) in.nval;
            
            // 操作1：合并两个堆
            if (op == 1) {
                in.nextToken();
                x = (int) in.nval;
                in.nextToken();
                y = (int) in.nval;
                
                // 如果x或y已经被删除，不进行合并
                if (num[x] != -1 && num[y] != -1) {
                    // 找到x和y所在的堆的根节点
                    int l = find(x);
                    int r = find(y);
                    
                    // 如果不在同一个堆中，进行合并
                    if (l != r) {
                        merge(l, r);
                    }
                }
            } 
            // 操作2：删除堆顶元素
            else {
                in.nextToken();
                x = (int) in.nval;
                
                // 如果x已经被删除，输出-1
                if (num[x] == -1) {
                    out.println(-1);
                } else {
                    // 找到x所在堆的根节点
                    int ans = find(x);
                    // 输出根节点的值
                    out.println(num[ans]);
                    // 删除根节点
                    pop(ans);
                    // 标记节点已被删除
                    num[ans] = -1;
                }
            }
        }
        
        out.flush();
        out.close();
        br.close();
    }
    
    /*
    算法分析：
    
    时间复杂度：
    1. 初始化：O(n)
    2. 合并操作：O(log n)
    3. 删除堆顶：O(log n)
    4. 查找操作：近似O(1)（由于路径压缩）
    5. 总体：O(n + m * log n)
    
    空间复杂度：O(n)
    
    算法思路：
    1. 使用左偏树维护多个小根堆，支持快速合并和删除最小值
    2. 使用并查集快速判断两个节点是否在同一个堆中
    3. 对于操作1，先检查节点是否有效，然后通过并查集判断是否在同一堆中，最后合并
    4. 对于操作2，先检查节点是否被删除，然后找到堆顶元素并删除
    
    工程化考虑：
    1. 输入输出优化：使用StreamTokenizer和PrintWriter提高效率
    2. 内存管理：使用静态数组避免动态内存分配
    3. 异常处理：检查节点是否已被删除
    4. 代码可读性：添加详细注释，清晰的变量命名
    
    与标准库对比：
    1. Java标准库中的PriorityQueue不支持高效合并操作
    2. 左偏树在合并操作上有明显优势
    3. 但在单次操作性能上可能不如优化的二叉堆
    
    调试技巧：
    1. 可以添加打印函数验证左偏树结构
    2. 注意检查并查集的路径压缩是否正确
    3. 特别关注删除操作后节点状态的更新
    
    极端情况：
    1. 所有节点值相同
    2. 所有操作都是合并操作
    3. 所有操作都是删除操作
    4. 合并相同堆
    */
}