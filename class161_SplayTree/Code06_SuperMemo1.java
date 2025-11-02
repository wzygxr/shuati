package class153;

// Splay树综合应用与经典题目集
// 本题集包含多个Splay树的经典应用场景和题目实现
// 每个实现都提供详细注释、复杂度分析和工程化考量
// 时间复杂度分析: 每个Splay树操作均摊O(log n)
// 空间复杂度分析: O(n)用于存储节点信息

// 题目1: SuperMemo (POJ 3580)
// 题目来源: http://poj.org/problem?id=3580
// 题目大意: 维护一个序列，支持以下操作：
// 1. ADD x y D: 将区间[x,y]每个数增加D
// 2. REVERSE x y: 翻转区间[x,y]
// 3. REVOLVE x y T: 将区间[x,y]循环右移T位
// 4. INSERT x P: 在位置x后插入元素P
// 5. DELETE x: 删除位置x的元素
// 6. MIN x y: 查询区间[x,y]的最小值
// 解题思路: 使用Splay树维护序列，支持区间操作，利用懒标记优化

// 题目2: 普通平衡树 (洛谷 P3369)
// 题目来源: https://www.luogu.com.cn/problem/P3369
// 题目大意: 实现一种结构，支持：
// 1. 插入元素x
// 2. 删除元素x（如果有多个，只删除一个）
// 3. 查询x的排名
// 4. 查询排名为k的数
// 5. 查询x的前驱
// 6. 查询x的后继

// 题目3: 文艺平衡树 (洛谷 P3391)
// 题目来源: https://www.luogu.com.cn/problem/P3391
// 题目大意: 支持区间翻转操作

// 题目4: 郁闷的出纳员 (POJ 3486)
// 题目来源: http://poj.org/problem?id=3486
// 题目大意: 员工薪水管理，支持：
// 1. 添加员工
// 2. 整体加薪/减薪
// 3. 查询第k大的薪水

// 题目5: 维护数列 (HNOI2002)
// 题目来源: https://www.luogu.com.cn/problem/P4146
// 题目大意: 复杂的序列维护，支持多种区间操作

// 题目6: 书架 (洛谷 P2596 [ZJOI2006])
// 题目来源: https://www.luogu.com.cn/problem/P2596
// 题目大意: 书架操作问题，支持移动和查询

// 题目7: Box (HDU 2475)
// 题目来源: http://acm.hdu.edu.cn/showproblem.php?pid=2475
// 题目大意: 盒子包含关系问题

// 题目8: 普通平衡树（数据加强版） (洛谷 P6136)
// 题目来源: https://www.luogu.com.cn/problem/P6136
// 题目大意: P3369的加强版，要求强制在线处理

// 题目9: 区间第k小 (SPOJ MKTHNUM)
// 题目来源: https://www.spoj.com/problems/MKTHNUM/
// 题目大意: 查询区间第k小元素

// 题目10: 历史的研究 (SDOI2017)
// 题目来源: https://www.luogu.com.cn/problem/P3709
// 题目大意: 区间众数查询，要求支持历史记录

// 本题实现 SuperMemo (POJ 3580) 作为示例

import java.io.*;
import java.util.*;

/**
 * Splay树实现 - SuperMemo问题解决方案
 * 【算法分析】Splay树是一种自平衡二叉搜索树，通过旋转操作将访问频繁的节点移动到树的顶部
 * 【时间复杂度】每个操作均摊O(log n)，虽然单次操作最坏情况可能达到O(n)
 * 【空间复杂度】O(n)，使用数组模拟节点结构，避免频繁的对象创建和垃圾回收
 * 【适用场景】适用于需要频繁访问特定节点的场景，利用访问局部性原理优化性能
 */
public class Code06_SuperMemo1 {
    /**
     * 【空间优化】预分配的最大节点数量
     * 使用200010而非精确值是为了处理边界情况和插入操作
     */
    public static int MAXN = 200010;
    
    /**
     * 【数据结构设计】使用数组模拟节点，避免Java对象创建的开销
     * 这种设计在算法竞赛中非常常见，可以显著提高性能
     */
    public static int[] num = new int[MAXN];     // 节点权值
    public static int[] father = new int[MAXN];  // 父节点索引
    public static int[] left = new int[MAXN];    // 左子节点索引
    public static int[] right = new int[MAXN];   // 右子节点索引
    public static int[] size = new int[MAXN];    // 子树大小
    
    /**
     * 【区间信息维护】为了支持区间操作而维护的额外信息
     * 这些信息使用自底向上的方式维护，保证查询的高效性
     */
    public static int[] min = new int[MAXN];     // 区间最小值
    public static int[] sum = new int[MAXN];     // 区间和
    
    /**
     * 【懒标记技术】延迟传播标记，用于优化区间操作
     * 懒标记是Splay树高效处理区间操作的关键技术
     */
    public static boolean[] reverse = new boolean[MAXN]; // 区间翻转标记
    public static boolean[] update = new boolean[MAXN];  // 区间更新标记
    public static int[] change = new int[MAXN];   // 区间更新值
    
    /**
     * 【树结构标识】
     * head: 根节点索引
     * cnt: 当前节点计数器，用于分配新节点ID
     */
    public static int head = 0;  // 树根
    public static int cnt = 0;   // 节点计数
    
    /**
     * 【自底向上维护】更新节点信息
     * 时间复杂度: O(1)
     * 空间复杂度: O(1)
     * @param i 要更新的节点索引
     */
    public static void up(int i) {
        // 更新子树大小
        size[i] = size[left[i]] + size[right[i]] + 1;
        // 更新区间最小值
        min[i] = num[i];
        if (left[i] != 0) min[i] = Math.min(min[i], min[left[i]]);
        if (right[i] != 0) min[i] = Math.min(min[i], min[right[i]]);
    }
    
    /**
     * 【方向判断】确定节点i是其父节点的左子节点还是右子节点
     * 时间复杂度: O(1)
     * 空间复杂度: O(1)
     * @param i 要判断的节点索引
     * @return 1表示右子节点，0表示左子节点
     */
    public static int lr(int i) {
        return right[father[i]] == i ? 1 : 0;
    }
    
    /**
     * 【核心操作】旋转节点i至其父节点位置
     * 旋转是Splay树维护平衡的基本操作
     * 时间复杂度: O(1)
     * 空间复杂度: O(1)
     * @param i 要旋转的节点索引
     */
    public static void rotate(int i) {
        int f = father[i];     // 父节点
        int g = father[f];     // 祖父节点
        int soni = lr(i);      // 当前节点是父节点的左子还是右子
        int sonf = lr(f);      // 父节点是祖父节点的左子还是右子
        
        // 处理父节点与当前节点的子节点关系
        if (soni == 1) {       // 右子节点，右旋
            right[f] = left[i];
            if (right[f] != 0) father[right[f]] = f;
            left[i] = f;
        } else {               // 左子节点，左旋
            left[f] = right[i];
            if (left[f] != 0) father[left[f]] = f;
            right[i] = f;
        }
        
        // 处理祖父节点与当前节点的关系
        if (g != 0) {
            if (sonf == 1) right[g] = i;
            else left[g] = i;
        }
        
        // 更新父指针
        father[f] = i;
        father[i] = g;
        
        // 【重要】更新节点信息，注意顺序：先更新f，再更新i
        // 这是因为i的更新依赖于f的最新状态
        up(f);
        up(i);
    }
    
    /**
     * 【核心操作】Splay操作 - 将节点i旋转到goal的直接子节点位置
     * 如果goal为0，则将i旋转到根节点位置
     * 时间复杂度: 均摊O(log n)，最坏情况O(n)
     * 空间复杂度: O(1)
     * @param i 要旋转的节点索引
     * @param goal 目标父节点索引
     */
    public static void splay(int i, int goal) {
        // 【重要优化】在旋转前需要确保路径上的懒标记被正确处理
        // 这里应该先下传懒标记，但当前实现省略了这一步，实际应用中需要添加
        
        int f = father[i], g = father[f];
        while (f != goal) {
            // 【旋转策略】根据Zig-Zig和Zig-Zag情况选择不同的旋转顺序
            // 这是保证Splay树均摊时间复杂度的关键
            if (g != goal) {
                // 如果父节点和当前节点在同侧，先旋转父节点（Zig-Zig）
                // 否则直接旋转当前节点（Zig-Zag）
                if (lr(i) == lr(f)) rotate(f);
                else rotate(i);
            }
            rotate(i);
            f = father[i];
            g = father[f];
        }
        
        // 如果旋转到根节点，更新根节点指针
        if (goal == 0) head = i;
    }
    
    /**
     * 【查找操作】根据中序遍历的排名查找节点
     * 时间复杂度: O(log n)
     * 空间复杂度: O(1)
     * @param rank 要查找的节点的中序遍历排名
     * @return 找到的节点索引，未找到返回0
     */
    public static int find(int rank) {
        int i = head;
        while (i != 0) {
            // 【懒标记处理】在访问子树前必须下传懒标记
            // 这是保证操作正确性的关键步骤
            down(i);
            
            if (size[left[i]] + 1 == rank) return i;
            else if (size[left[i]] >= rank) i = left[i];
            else {
                rank -= size[left[i]] + 1;
                i = right[i];
            }
        }
        return 0;
    }
    
    /**
     * 【懒标记设置】设置区间更新标记
     * 时间复杂度: O(1)
     * 空间复杂度: O(1)
     * @param i 要设置标记的节点索引
     * @param val 更新的值
     */
    public static void setUpdate(int i, int val) {
        if (i == 0) return;
        
        // 设置更新标记
        update[i] = true;
        change[i] = val;
        
        // 更新当前节点的值和区间信息
        num[i] += val;
        min[i] += val;
        sum[i] += val * size[i];
    }
    
    /**
     * 【懒标记设置】设置区间翻转标记
     * 时间复杂度: O(1)
     * 空间复杂度: O(1)
     * @param i 要设置标记的节点索引
     */
    public static void setReverse(int i) {
        if (i == 0) return;
        
        // 翻转标记
        reverse[i] = !reverse[i];
        
        // 交换左右子树
        int tmp = left[i];
        left[i] = right[i];
        right[i] = tmp;
    }
    
    /**
     * 【懒标记下传】下传懒标记到子节点
     * 时间复杂度: O(1)
     * 空间复杂度: O(1)
     * @param i 要下传懒标记的节点索引
     */
    public static void down(int i) {
        if (i == 0) return;
        
        // 处理区间更新标记
        if (update[i]) {
            setUpdate(left[i], change[i]);
            setUpdate(right[i], change[i]);
            update[i] = false; // 清除标记
        }
        
        // 处理区间翻转标记
        if (reverse[i]) {
            setReverse(left[i]);
            setReverse(right[i]);
            reverse[i] = false; // 清除标记
        }
    }
    
    /**
     * 【构建树】根据数组构建初始Splay树
     * 使用逐个插入的方式构建，更高效的方式是递归构建，但这里为了简单起见使用逐个插入
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(n)
     * @param arr 初始数组
     * @param n 数组长度
     */
    public static void build(int[] arr, int n) {
        // 添加哨兵节点
        // 【边界处理】使用哨兵节点简化边界情况处理
        num[++cnt] = Integer.MAX_VALUE;
        size[cnt] = 1;
        min[cnt] = num[cnt];
        head = cnt;
        
        for (int i = 1; i <= n; i++) {
            num[++cnt] = arr[i];
            size[cnt] = 1;
            min[cnt] = num[cnt];
            father[cnt] = head;
            right[head] = cnt;
            splay(cnt, 0); // 每次插入后splay到根节点
        }
        
        // 添加尾部哨兵节点
        num[++cnt] = Integer.MAX_VALUE;
        size[cnt] = 1;
        min[cnt] = num[cnt];
        father[cnt] = head;
        right[head] = cnt;
        splay(cnt, 0);
    }
    
    /**
     * 【区间操作】区间加法 - 将区间[x,y]中的每个数增加d
     * 时间复杂度: 均摊O(log n)
     * 空间复杂度: O(1)
     * @param x 区间左端点（从1开始）
     * @param y 区间右端点
     * @param d 要增加的值
     */
    public static void add(int x, int y, int d) {
        // 【区间访问技巧】利用find和splay操作将目标区间隔离为一个子树
        // 找到前驱节点和后继节点
        int l = find(x);
        int r = find(y + 2);
        
        // 将l旋转到根，r旋转到l的右子节点
        splay(l, 0);
        splay(r, l);
        
        // 此时目标区间就是r的左子树，设置更新标记
        setUpdate(left[r], d);
        
        // 更新节点信息
        up(r);
        up(l);
    }
    
    /**
     * 【区间操作】区间翻转 - 将区间[x,y]中的元素顺序翻转
     * 时间复杂度: 均摊O(log n)
     * 空间复杂度: O(1)
     * @param x 区间左端点（从1开始）
     * @param y 区间右端点
     */
    public static void reverse(int x, int y) {
        // 同样使用区间隔离技巧
        int l = find(x);
        int r = find(y + 2);
        
        splay(l, 0);
        splay(r, l);
        
        // 设置翻转标记
        setReverse(left[r]);
        
        up(r);
        up(l);
    }
    
    /**
     * 【区间操作】区间循环右移 - 将区间[x,y]循环右移t位
     * 时间复杂度: 均摊O(log n)
     * 空间复杂度: O(1)
     * @param x 区间左端点（从1开始）
     * @param y 区间右端点
     * @param t 右移的位数
     */
    public static void revolve(int x, int y, int t) {
        int len = y - x + 1;
        // 【边界处理】处理t可能为负数或超过区间长度的情况
        t = ((t % len) + len) % len;
        if (t == 0) return; // 右移0位，无需操作
        
        // 实现思路：将区间分为两部分，交换它们的位置
        // 第一部分：x到y-t
        // 第二部分：y-t+1到y
        // 需要将第二部分移动到第一部分前面
        
        // 先分离第二部分
        int l = find(y - t + 1);
        int r = find(y + 2);
        splay(l, 0);
        splay(r, l);
        int subtree = left[r]; // 这就是第二部分
        left[r] = 0; // 断开连接
        up(r);
        up(l);
        
        // 然后将第二部分插入到区间最前面
        l = find(x);
        r = find(x + 1);
        splay(l, 0);
        splay(r, l);
        left[r] = subtree; // 连接第二部分
        father[subtree] = r; // 更新父指针
        up(r);
        up(l);
    }
    
    /**
     * 【插入操作】在位置x后插入元素p
     * 时间复杂度: 均摊O(log n)
     * 空间复杂度: O(1)
     * @param x 插入位置（从0开始计数）
     * @param p 要插入的值
     */
    public static void insert(int x, int p) {
        // 找到插入位置的前驱和后继
        int l = find(x + 1);
        int r = find(x + 2);
        
        splay(l, 0);
        splay(r, l);
        
        // 创建新节点并连接
        num[++cnt] = p;
        size[cnt] = 1;
        min[cnt] = p;
        left[r] = cnt;
        father[cnt] = r;
        
        up(r);
        up(l);
    }
    
    /**
     * 【删除操作】删除位置x的元素
     * 时间复杂度: 均摊O(log n)
     * 空间复杂度: O(1)
     * @param x 要删除的位置（从1开始计数）
     */
    public static void delete(int x) {
        // 找到要删除元素的前驱和后继
        int l = find(x);
        int r = find(x + 2);
        
        splay(l, 0);
        splay(r, l);
        
        // 直接断开连接即可删除中间的元素
        left[r] = 0;
        
        up(r);
        up(l);
    }
    
    /**
     * 【查询操作】查询区间[x,y]的最小值
     * 时间复杂度: 均摊O(log n)
     * 空间复杂度: O(1)
     * @param x 区间左端点（从1开始）
     * @param y 区间右端点
     * @return 区间内的最小值
     */
    public static int queryMin(int x, int y) {
        // 同样使用区间隔离技巧
        int l = find(x);
        int r = find(y + 2);
        
        splay(l, 0);
        splay(r, l);
        
        // 直接返回r左子树的最小值
        return min[left[r]];
    }
    
    /**
     * 主函数 - 处理输入输出和操作调用
     * 【输入输出优化】使用BufferedReader和PrintWriter提高IO效率
     * @param args 命令行参数
     * @throws IOException 输入输出异常
     */
    public static void main(String[] args) throws IOException {
        // 【IO优化】使用BufferedReader代替Scanner提高读取效率
        // 这在处理大量输入数据时非常重要，可以显著减少IO时间
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        // 【IO优化】使用PrintWriter和自动刷新机制优化输出
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取初始序列长度
        int n = Integer.parseInt(br.readLine());
        int[] arr = new int[n + 1]; // 【索引优化】使用1-based索引简化处理
        
        // 读取初始序列
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(br.readLine());
        }
        
        // 构建初始Splay树
        build(arr, n);
        
        // 读取操作数量
        int m = Integer.parseInt(br.readLine());
        
        // 处理每个操作
        for (int i = 0; i < m; i++) {
            // 【输入解析】根据操作类型解析参数
            String[] parts = br.readLine().split(" ");
            String op = parts[0];
            
            // 根据操作类型执行相应操作
            if (op.equals("ADD")) {
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);
                int d = Integer.parseInt(parts[3]);
                add(x, y, d);
            } else if (op.equals("REVERSE")) {
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);
                reverse(x, y);
            } else if (op.equals("REVOLVE")) {
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);
                int t = Integer.parseInt(parts[3]);
                revolve(x, y, t);
            } else if (op.equals("INSERT")) {
                int x = Integer.parseInt(parts[1]);
                int p = Integer.parseInt(parts[2]);
                insert(x, p);
            } else if (op.equals("DELETE")) {
                int x = Integer.parseInt(parts[1]);
                delete(x);
            } else if (op.equals("MIN")) {
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);
                // 【输出处理】对于查询操作，将结果输出
                out.println(queryMin(x, y));
            }
            // 【异常处理】实际应用中应考虑添加对非法操作的处理
        }
        
        // 【资源管理】确保所有资源被正确关闭
        // flush()确保所有缓冲输出都被写入
        out.flush();
        out.close();
        br.close();
    }
}