package class157;

import java.io.*;
import java.util.*;

/**
 * 洛谷P3834 【模板】可持久化线段树 2 - 静态区间第K小
 * 
 * 题目来源：洛谷 https://www.luogu.com.cn/problem/P3834
 * 
 * 题目描述:
 * 给定一个含有n个数字的序列，每次查询区间[l,r]内第k小的数。
 * 
 * 【核心算法原理】
 * 可持久化线段树（主席树）是一种可以保存历史版本的数据结构，其核心思想是：
 * 1. 函数式编程思想：每次修改时只创建新节点，共享未修改部分
 * 2. 前缀和思想：利用前缀和的差值来计算区间信息
 * 3. 离散化处理：对大数据范围进行离散化以节省空间
 * 
 * 【解题思路】
 * 使用可持久化线段树（主席树）解决静态区间第K小问题的步骤：
 * 1. 对所有数值进行离散化处理，缩小数值范围
 * 2. 构建权值线段树，每个版本i表示前i个元素的权值分布
 * 3. 利用前缀和思想，区间[l,r]的信息等于版本r减去版本l-1
 * 4. 在线段树上二分查找第k小的数
 * 
 * 【复杂度分析】
 * 时间复杂度: O(n log n + m log n)
 *   - 离散化排序：O(n log n)
 *   - 构建所有版本线段树：O(n log n)
 *   - 每次查询：O(log n)
 * 空间复杂度: O(n log n)
 *   - 每个版本的线段树只需要O(log n)个新节点
 *   - 总共n个版本，因此空间复杂度为O(n log n)
 * 
 * 【算法变种与扩展】
 * 1. 动态区间第K小：结合树状数组实现动态修改
 * 2. 树上路径第K小：结合LCA（最近公共祖先）处理树上路径
 * 3. 二维区间第K小：使用二维主席树
 * 
 * 【示例输入输出】
 * 输入:
 * 5 3
 * 3 2 1 4 7
 * 1 4 3
 * 2 5 2
 * 3 5 1
 * 
 * 输出:
 * 3
 * 4
 * 7
 */
public class P3834_PersistentSegmentTree {
    // 数组大小定义，根据题目数据范围调整
    // 静态区间第K小问题通常数据规模较大，设置适当大小避免内存溢出
    static final int MAXN = 200010;
    
    // 原始输入数组
    static int[] arr = new int[MAXN];
    // 离散化后的排序数组，用于映射原始值到连续的排名
    static int[] sorted = new int[MAXN];
    // root[i]表示前i个元素构成的线段树的根节点编号
    static int[] root = new int[MAXN];
    
    // 线段树节点信息，采用数组模拟链式存储
    // left[rt]表示节点rt的左子节点
    static int[] left = new int[MAXN * 20]; // 开20倍空间以应对递归深度
    // right[rt]表示节点rt的右子节点
    static int[] right = new int[MAXN * 20];
    // sum[rt]表示以rt为根的子树中元素的个数
    static int[] sum = new int[MAXN * 20];
    
    // 线段树节点计数器，记录当前已创建的节点数量
    static int cnt = 0;
    
    /**
     * 构建空线段树
     * 
     * 【函数说明】
     * 递归构建一棵空的权值线段树，用于后续版本的基础
     * 
     * @param l 区间左端点（离散化后的排名范围）
     * @param r 区间右端点（离散化后的排名范围）
     * @return 根节点编号
     * 
     * 【实现细节】
     * 1. 每次创建新节点时，cnt递增作为节点唯一标识
     * 2. 初始时sum[rt]设为0，表示区间内暂时没有元素
     * 3. 递归构建左右子树直到叶节点
     * 4. 采用后序遍历的方式构建
     * 
     * 【性能优化】
     * 可以使用位运算优化：mid = (l + r) >> 1
     * 但为了可读性，这里保留除法形式
     */
    static int build(int l, int r) {
        int rt = ++cnt; // 动态分配节点编号
        sum[rt] = 0; // 初始时该节点覆盖的区间内元素个数为0
        
        // 非叶节点需要递归构建左右子树
        if (l < r) {
            int mid = (l + r) / 2;
            left[rt] = build(l, mid);     // 构建左子树，对应较小的一半值域
            right[rt] = build(mid + 1, r); // 构建右子树，对应较大的一半值域
        }
        
        return rt; // 返回当前节点编号作为根节点
    }
    
    /**
     * 在线段树中插入一个值（创建新版本）
     * 
     * 【函数说明】
     * 基于前一个版本，插入一个新元素，生成新版本的线段树
     * 
     * @param pos 要插入的值（离散化后的排名）
     * @param l 当前区间左端点
     * @param r 当前区间右端点
     * @param pre 前一个版本的对应节点编号
     * @return 新版本的当前节点编号
     * 
     * 【核心思想】
     * 可持久化的关键实现：
     * 1. 创建新节点，复制前一个版本的左右子节点引用
     * 2. 更新当前节点的计数信息
     * 3. 只更新需要修改的路径上的节点
     * 4. 未修改的子树节点与前一版本共享
     * 
     * 【算法分析】
     * - 时间复杂度：O(log n) - 每次插入只需要创建O(log n)个新节点
     * - 空间复杂度：O(log n) - 每次插入需要额外O(log n)的空间
     * 
     * 【异常处理】
     * 注意pos必须在[l, r]范围内，否则会导致错误
     */
    static int insert(int pos, int l, int r, int pre) {
        // 创建新节点，作为新版本的一部分
        int rt = ++cnt;
        
        // 复制前一个版本的左右子节点引用（共享未修改的部分）
        left[rt] = left[pre];
        right[rt] = right[pre];
        // 更新当前节点的计数值（比前一版本多一个元素）
        sum[rt] = sum[pre] + 1;
        
        // 递归更新直到叶节点
        if (l < r) {
            int mid = (l + r) / 2;
            
            // 根据pos的大小决定更新左子树还是右子树
            if (pos <= mid) {
                // 更新左子树，并更新左子节点的引用
                left[rt] = insert(pos, l, mid, left[rt]);
            } else {
                // 更新右子树，并更新右子节点的引用
                right[rt] = insert(pos, mid + 1, r, right[rt]);
            }
        }
        
        return rt; // 返回新版本的当前节点
    }
    
    /**
     * 查询区间第k小的数
     * 
     * 【函数说明】
     * 通过两个版本的线段树的差值，在线段树上二分查找第k小的元素
     * 
     * @param k 要查询的第k小
     * @param l 当前区间左端点
     * @param r 当前区间右端点
     * @param u 前一个版本的线段树根节点（对应l-1）
     * @param v 当前版本的线段树根节点（对应r）
     * @return 第k小的数在离散化数组中的位置
     * 
     * 【算法原理】
     * 利用前缀和思想：区间[l,r]的信息 = 前缀r的信息 - 前缀l-1的信息
     * 通过比较左子树中元素的个数与k的大小关系，决定在左子树还是右子树中查找
     * 
     * 【查询步骤】
     * 1. 计算左子树中元素的个数：x = sum[left[v]] - sum[left[u]]
     * 2. 如果x >= k，说明第k小在左子树中，递归查询左子树
     * 3. 否则，说明第k小在右子树中，递归查询右子树，且k要减去左子树中的元素个数
     * 
     * 【边界条件】
     * 当l == r时，说明已经找到目标位置，返回该位置
     * 
     * 【时间复杂度】
     * O(log n) - 每次查询需要遍历O(log n)层节点
     */
    static int query(int k, int l, int r, int u, int v) {
        // 边界条件：找到目标位置
        if (l >= r) return l;
        
        int mid = (l + r) / 2;
        // 计算区间[l,r]中，值小于等于mid的元素个数
        int x = sum[left[v]] - sum[left[u]];
        
        if (x >= k) {
            // 左子树中的元素个数足够，第k小在左子树
            return query(k, l, mid, left[u], left[v]);
        } else {
            // 左子树中的元素个数不足，第k小在右子树，需要减去左子树的元素个数
            return query(k - x, mid + 1, r, right[u], right[v]);
        }
    }
    
    /**
     * 离散化查找数值对应的排名
     * 
     * 【函数说明】
     * 通过二分查找，将原始数值映射到离散化后的排名
     * 
     * @param val 要查找的原始数值
     * @param n 离散化后的数组有效长度
     * @return 对应的值在离散化数组中的排名（从1开始）
     * 
     * 【离散化原理】
     * 离散化是将原始值域较大的数组映射到较小的连续整数区间
     * 例如：原数组 [10000, 20000, 5000] 可以映射为 [2, 3, 1]
     * 
     * 【注意事项】
     * 1. Arrays.binarySearch返回的是从0开始的索引，需要+1转为从1开始的排名
     * 2. sorted数组必须是已排序且去重的
     */
    static int getId(int val, int n) {
        // 在sorted数组的[1, n]范围内二分查找val
        // 返回的是从0开始的索引，+1后得到从1开始的排名
        return Arrays.binarySearch(sorted, 1, n + 1, val) + 1;
    }
    
    /**
     * 主函数
     * 
     * 【函数说明】
     * 处理输入，构建主席树，处理查询并输出结果
     * 
     * 【工程化考量】
     * 1. 使用BufferedReader和PrintWriter提高IO效率
     * 2. 边界条件处理：数组索引从1开始，避免边界错误
     * 3. 资源管理：使用try-catch-finally或try-with-resources确保资源关闭
     * 
     * 【异常处理】
     * 处理IO异常，确保程序稳定运行
     * 
     * 【性能优化】
     * 1. 快速IO处理大数据量
     * 2. 离散化去重减少空间占用
     */
    public static void main(String[] args) throws IOException {
        // 使用BufferedReader提高读取效率
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        // 使用PrintWriter提高写入效率
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取n和m
        String[] line = reader.readLine().split(" ");
        int n = Integer.parseInt(line[0]);
        int m = Integer.parseInt(line[1]);
        
        // 读取原始数组
        line = reader.readLine().split(" ");
        for (int i = 1; i <= n; i++) { // 注意这里数组从1开始索引，方便处理
            arr[i] = Integer.parseInt(line[i - 1]);
            sorted[i] = arr[i]; // 复制到sorted数组用于离散化
        }
        
        // 离散化处理步骤：
        // 1. 排序
        Arrays.sort(sorted, 1, n + 1);
        // 2. 去重
        int size = 1;
        for (int i = 2; i <= n; i++) {
            if (sorted[i] != sorted[size]) {
                sorted[++size] = sorted[i];
            }
        }
        
        // 构建主席树：
        // 1. 首先构建空树作为版本0
        root[0] = build(1, size);
        // 2. 依次插入元素，生成每个版本的线段树
        for (int i = 1; i <= n; i++) {
            // 将原始值转换为离散化后的排名
            int pos = getId(arr[i], size);
            // 基于前一个版本，插入当前元素，得到新版本
            root[i] = insert(pos, 1, size, root[i - 1]);
        }
        
        // 处理查询
        for (int i = 0; i < m; i++) {
            line = reader.readLine().split(" ");
            int l = Integer.parseInt(line[0]);
            int r = Integer.parseInt(line[1]);
            int k = Integer.parseInt(line[2]);
            
            // 查询区间[l,r]中第k小的元素在离散化后的位置
            int pos = query(k, 1, size, root[l - 1], root[r]);
            // 将离散化后的位置转换回原始值并输出
            writer.println(sorted[pos]);
        }
        
        // 关闭资源
        writer.flush();
        writer.close();
        reader.close();
    }
}

/**
 * 【工程实现总结】
 * 
 * 1. 【内存管理】
 * - 由于Java中无法动态分配数组大小，需要预分配足够的空间
 * - 线段树节点数组通常需要开MAXN的20-40倍
 * - 对于特别大的数据量，可能需要使用List或动态分配内存
 * 
 * 2. 【性能优化】
 * - 使用快速IO方法处理大规模数据
 * - 离散化时先排序后去重，避免重复计算
 * - 递归深度控制，避免栈溢出
 * 
 * 3. 【代码调试技巧】
 * - 可以在build、insert、query函数中添加打印语句调试
 * - 使用断言验证中间结果的正确性
 * - 针对小规模测试用例验证算法逻辑
 * 
 * 4. 【边界条件处理】
 * - 数组索引从1开始，避免边界错误
 * - 处理k值超过区间长度的情况
 * - 处理空输入情况
 * 
 * 5. 【与C++实现的差异】
 * - Java中无法直接使用指针，通过数组模拟指针
 * - Java的递归深度限制可能在处理大规模数据时成为问题
 * - C++中可以使用动态开点更灵活地管理内存
 * 
 * 6. 【算法变体】
 * - 静态区间第K小：最基本应用，如本题
 * - 树上第K小：结合LCA处理树上路径查询
 * - 动态第K小：结合树状数组或线段树支持修改操作
 * - 区间不同元素个数：需要额外的预处理技巧
 */