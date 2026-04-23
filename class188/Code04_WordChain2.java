/*
 * 词链，java版
 * 问题描述：给定n个由小写字母组成的单词，想把所有单词串成一个词链
 * 约束条件：前一个单词的结尾字符 == 后一个单词的开头字符
 * 示例词链：aloha.arachnid.dog.gopher.rat.tiger
 * 需要使用每个输入的单词恰好一次，同一单词出现k次就要用k次
 * 目标：返回字典序最小的结果，注意 . 的字典序 < 小写字母的字典序
 * 如果不存在合法词链，输出***
 * 数据范围：1 <= n <= 1000
 * 测试链接 : https://www.luogu.com.cn/problem/P1127
 * 
 * 面试要点：
 * - 欧拉路径在词链问题中的应用
 * - 图的建模：将单词的首尾字符作为节点，单词本身作为边
 * - 字典序最小的欧拉路径求解
 * - Hierholzer算法的变种应用
 * 
 * ML/DL关联：
 * - 自然语言处理中的序列建模
 * - 文本生成中的约束满足问题
 * - 图神经网络在文本处理中的应用
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;

public class Code04_WordChain2 {

    /**
     * 边比较器类，用于对边数组进行排序
     * 实现Comparator接口，按起点升序、单词字典序升序排序
     * 面试要点：自定义比较器的实现，用于保证字典序最小
     * ML/DL关联：排序算法在数据预处理中的应用
     */
    public static class EdgeCmp implements Comparator<Integer> {
        /**
         * 比较两个边的大小
         * 
         * @param i 第一个边的索引
         * @param j 第二个边的索引
         * @return 比较结果：如果str[i]小于str[j]返回负数，相等返回0，大于返回正数
         * 
         *         面试要点：
         *         - 首先比较边的起点，起点小的边排在前面
         *         - 如果起点相同，则按单词的字典序排序
         *         - 这样可以保证字典序最小的结果
         * 
         *         ML/DL关联：
         *         - 排序算法在数据预处理中的重要性
         *         - 比较函数设计对结果的影响
         */
        public int compare(Integer i, Integer j) {
            // 首先比较边的起点，如果不同则按起点排序
            if (a[i] != a[j]) {
                return a[i] - a[j];
            }
            // 如果起点相同，则按单词的字典序排序，确保字典序最小
            return str[i].compareTo(str[j]);
        }
    }

    /**
     * 最大节点数常量，对应26个小写字母加1
     * 面试要点：合理的空间预分配
     * ML/DL关联：批次大小对内存的影响
     */
    public static int MAXN = 27;
    /**
     * 最大边数常量，根据题目限制设置
     * 面试要点：数组大小预分配策略
     * ML/DL关联：批次大小(batch size)对训练效率的影响
     */
    public static int MAXM = 1002;
    /**
     * 节点数（26个小写字母）和边数（单词数）
     * 面试要点：全局变量的作用域和生命周期
     * ML/DL关联：超参数管理
     */
    public static int n = 26, m;

    /**
     * 单词数组，存储原始输入的单词信息
     * 面试要点：字符串数组的内存布局和访问效率
     * ML/DL关联：文本数据的张量表示
     */
    public static String[] str = new String[MAXM];
    /**
     * 起点数组，a[i]表示第i个单词的首字母对应的节点编号
     * 面试要点：字符到数字的映射
     * ML/DL关联：字符编码在NLP中的应用
     */
    public static int[] a = new int[MAXM];
    /**
     * 终点数组，b[i]表示第i个单词的尾字母对应的节点编号
     * 面试要点：字符到数字的映射
     * ML/DL关联：字符编码在NLP中的应用
     */
    public static int[] b = new int[MAXM];
    /**
     * 边ID数组，存储边的索引，用于排序
     * 面试要点：索引数组的使用，便于按特定顺序访问
     * ML/DL关联：索引映射在数据处理中的应用
     */
    public static Integer[] eidArr = new Integer[MAXM];

    /**
     * 邻接表头指针数组，用于链式前向星存储图
     * head[i]表示节点i的第一条出边的索引
     * 面试要点：图的多种存储方式对比（邻接矩阵vs邻接表vs链式前向星）
     * ML/DL关联：稀疏图的高效存储方法
     */
    public static int[] head = new int[MAXN];
    /**
     * 邻接表边的下一个指针数组，用于链式前向星
     * nxt[i]表示第i条边的下一条边的索引
     * 面试要点：链式前向星的实现原理
     * ML/DL关联：链表结构在图表示中的应用
     */
    public static int[] nxt = new int[MAXM];
    /**
     * 邻接表边的目标节点数组，用于链式前向星
     * to[i]表示第i条边指向的目标节点
     * 面试要点：链式前向星的空间复杂度O(M)
     * ML/DL关联：图的邻接关系表示
     */
    public static int[] to = new int[MAXM];
    /**
     * 边权重数组，存储单词字符串
     * 面试要点：边权重的概念扩展
     * ML/DL关联：图中边的属性表示
     */
    public static String[] weight = new String[MAXM];
    /**
     * 图的边计数器，用于链式前向星的边编号
     * 面试要点：边的唯一标识符管理
     * ML/DL关联：图中边的索引机制
     */
    public static int cntg;

    /**
     * 当前节点的当前边指针数组，用于Hierholzer算法
     * cur[i]表示节点i当前应该访问的下一条边
     * 面试要点：Hierholzer算法的关键数据结构，避免重复访问边
     * ML/DL关联：动态规划中的状态转移指针
     */
    public static int[] cur = new int[MAXN];
    /**
     * 节点出度数组，outDeg[i]表示节点i的出度
     * 面试要点：欧拉路径判定定理的核心数据结构
     * ML/DL关联：节点度数作为图的重要特征
     */
    public static int[] outDeg = new int[MAXN];
    /**
     * 节点入度数组，inDeg[i]表示节点i的入度
     * 面试要点：入度出度计算是图论算法的基础
     * ML/DL关联：有向图中节点的流入流出特征
     */
    public static int[] inDeg = new int[MAXN];

    /**
     * 欧拉路径结果数组，存储最终的路径单词
     * 面试要点：路径重构的数据结构
     * ML/DL关联：序列生成模型的输出结构
     */
    public static String[] path = new String[MAXM];
    /**
     * 路径节点计数器，记录路径中单词的数量
     * 面试要点：计数器在算法中的作用
     * ML/DL关联：序列长度统计
     */
    public static int cntp;

    /**
     * 添加边到链式前向星图结构中
     * 
     * @param u 起点
     * @param v 终点
     * @param w 边的权重（单词字符串）
     * 
     *          面试要点：
     *          - 链式前向星的插入操作
     *          - 时间复杂度：O(1)
     *          - 空间复杂度：O(M)
     * 
     *          ML/DL关联：
     *          - 图构建操作在GNN预处理中的应用
     *          - 邻接关系的动态更新
     */
    public static void addEdge(int u, int v, String w) {
        // 将新边插入到邻接表的头部，形成链表结构
        // nxt[++cntg]指向原来的首边，实现链表头插
        nxt[++cntg] = head[u];
        // to[cntg]记录新边的目标节点
        to[cntg] = v;
        // weight[cntg]记录新边的权重（单词）
        weight[cntg] = w;
        // head[u]更新为新边的索引
        head[u] = cntg;
    }

    /**
     * 获取字符串的起始节点（首字母对应的节点编号）
     * 
     * @param str 输入字符串
     * @return 起始节点编号（a=1, b=2, ..., z=26）
     * 
     *         面试要点：
     *         - 字符到数字的映射
     *         - ASCII码运算
     *         - 节点编号规范化
     * 
     *         ML/DL关联：
     *         - 字符编码在NLP中的应用
     *         - 特征映射方法
     */
    public static int startNode(String str) {
        // 将字符转换为数字编号，a-z对应1-26
        return str.charAt(0) - 'a' + 1;
    }

    /**
     * 获取字符串的结束节点（尾字母对应的节点编号）
     * 
     * @param str 输入字符串
     * @return 结束节点编号（a=1, b=2, ..., z=26）
     * 
     *         面试要点：
     *         - 字符到数字的映射
     *         - 字符串末尾字符获取
     *         - 节点编号规范化
     * 
     *         ML/DL关联：
     *         - 字符编码在NLP中的应用
     *         - 序列特征提取
     */
    public static int endNode(String str) {
        // 将字符转换为数字编号，a-z对应1-26
        return str.charAt(str.length() - 1) - 'a' + 1;
    }

    /**
     * 构建图结构，包括排序边、计算度数、构建邻接表
     * 
     * 面试要点：
     * - 输入预处理的重要性
     * - 多重循环的时间复杂度分析
     * - 数据结构的构建顺序
     * 
     * ML/DL关联：
     * - 数据预处理管道的设计
     * - 特征工程中的数据组织
     */
    public static void connect() {
        // 对边数组按起点、单词字典序排序，保证字典序最小
        Arrays.sort(eidArr, 1, m + 1, new EdgeCmp());
        // 遍历所有边，按起点分组处理
        for (int l = 1, r = 1; l <= m; l = ++r) {
            // 找到所有具有相同起点的边的区间
            while (r + 1 <= m && a[eidArr[l]] == a[eidArr[r + 1]]) {
                r++;
            }
            // 逆序处理相同起点的边，以保证字典序最小
            for (int i = r, u, v; i >= l; i--) {
                // 获取边的起点和终点
                u = a[eidArr[i]];
                v = b[eidArr[i]];
                // 更新节点的出入度
                outDeg[u]++;
                inDeg[v]++;
                // 将边添加到图的邻接表中
                addEdge(u, v, str[eidArr[i]]);
            }
        }
        // 初始化每个节点的当前边指针
        for (int i = 1; i <= n; i++) {
            // cur[i]初始化为head[i]，表示从第一条边开始访问
            cur[i] = head[i];
        }
    }

    /**
     * 在有向图中找到欧拉路径的起点
     * 
     * 面试要点：
     * - 欧拉路径存在条件的实现
     * - 度数差的判断逻辑
     * - 起点选择的策略
     * 
     * ML/DL关联：
     * - 图遍历的起始点选择策略
     * - 序列生成的初始状态设定
     */
    public static int directedStart() {
        // 记录起点和终点（出度比入度多1的点和入度比出度多1的点）
        int start = -1, end = -1;
        // 遍历所有节点，检查度数差
        for (int i = 1; i <= n; i++) {
            // 计算当前节点的出度减入度
            int v = outDeg[i] - inDeg[i];
            // 检查度数差是否合法（只能是-1, 0, 1）
            if (v < -1 || v > 1 || (v == 1 && start != -1) || (v == -1 && end != -1)) {
                // 如果度数差不合法，返回-1表示不存在欧拉路径
                return -1;
            }
            // 如果出度比入度多1，这是起点
            if (v == 1) {
                start = i;
            }
            // 如果入度比出度多1，这是终点
            if (v == -1) {
                end = i;
            }
        }
        // 检查起点和终点的配对情况（要么都是-1，要么都不是-1）
        if ((start == -1) ^ (end == -1)) {
            // 只有一个存在而另一个不存在，不符合欧拉路径条件
            return -1;
        }
        // 如果找到了起点（欧拉路径），返回起点
        if (start != -1) {
            return start;
        }
        // 如果没有起点和终点（欧拉回路），返回任意有出边的节点
        for (int i = 1; i <= n; i++) {
            // 如果节点有出边，它可以作为欧拉回路的起点
            if (outDeg[i] > 0) {
                return i;
            }
        }
        // 没有找到合适的起点，返回-1
        return -1;
    }

    /**
     * Hierholzer算法，用于寻找欧拉路径
     * 
     * @param u 当前节点
     * @param w 到达当前节点的边的权重（单词）
     * 
     *          面试要点：
     *          - Hierholzer算法的实现原理
     *          - 递归实现的欧拉路径查找
     *          - 路径记录方法
     * 
     *          ML/DL关联：
     *          - 图遍历算法在GNN中的应用
     *          - 序列生成的递归方法
     */
    public static void euler(int u, String w) {
        // 遍历从节点u出发的所有未访问边
        for (int e = cur[u]; e > 0; e = cur[u]) {
            // 更新当前边指针，跳过已访问的边
            cur[u] = nxt[e];
            // 递归访问下一个节点
            euler(to[e], weight[e]);
        }
        // 将当前边的权重（单词）加入路径
        path[++cntp] = w;
    }

    /**
     * 主函数：读取输入、处理数据、输出结果
     * 
     * 面试要点：
     * - 程序的整体流程设计
     * - IO优化的重要性
     * - 算法模块的组合使用
     * 
     * ML/DL关联：
     * - 训练/推理管道的构建
     * - 数据流的端到端处理
     */
    public static void main(String[] args) throws Exception {
        // 创建快速读取器，提高IO效率
        FastReader in = new FastReader(System.in);
        // 创建打印写入器，用于格式化输出
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        // 读取单词数量
        m = in.nextInt();
        // 读取所有单词并预处理
        for (int i = 1; i <= m; i++) {
            // 读取第i个单词
            str[i] = in.nextString();
            // 计算单词的起点（首字母）
            a[i] = startNode(str[i]);
            // 计算单词的终点（尾字母）
            b[i] = endNode(str[i]);
            // 初始化边ID数组
            eidArr[i] = i;
        }
        // 构建图结构
        connect();
        // 寻找欧拉路径的起点
        int start = directedStart();
        // 如果不存在欧拉路径
        if (start == -1) {
            // 输出"***"
            out.println("***");
        } else {
            // 使用Hierholzer算法求解欧拉路径
            euler(start, "");
            // 检查路径长度是否正确（应包含m+1个节点）
            if (cntp != m + 1) {
                // 如果路径长度不正确，说明图不连通
                out.println("***");
            } else {
                // 输出词链（反向输出，因为是DFS回溯时记录的）
                out.print(path[cntp - 1]);
                // 输出路径上剩余的单词，用"."连接
                for (int i = cntp - 2; i >= 1; i--) {
                    // 输出"."和路径上的单词
                    out.print("." + path[i]);
                }
                // 换行
                out.println();
            }
        }
        // 刷新输出缓冲区
        out.flush();
        // 关闭输出流
        out.close();
    }

    /**
     * 快速读取器类，用于高效读取输入
     * 通过缓冲区减少IO操作次数，提高读取速度
     * 
     * 面试要点：
     * - IO优化技术
     * - 缓冲区机制
     * - 输入解析算法
     * 
     * ML/DL关联：
     * - 高效数据加载器
     * - 批处理输入解析
     */
    static class FastReader {
        /**
         * 输入缓冲区，用于批量读取数据
         * 面试要点：缓冲区大小的选择（通常为2的幂次）
         * ML/DL关联：批次缓冲区的设计
         */
        private final byte[] buffer = new byte[1 << 16];
        /**
         * 缓冲区当前指针位置和有效数据长度
         * 面试要点：双指针技术
         * ML/DL关联：滑动窗口机制
         */
        private int ptr = 0, len = 0;
        /**
         * 输入流对象
         * 面试要点：装饰器模式的应用
         * ML/DL关联：数据流管道
         */
        private final InputStream in;

        /**
         * 构造函数，初始化输入流
         * 
         * @param in 输入流
         */
        FastReader(InputStream in) {
            // 保存输入流引用
            this.in = in;
        }

        /**
         * 读取下一个字节
         * 
         * @return 下一字节的值，如果到达流末尾返回-1
         * 
         *         面试要点：
         *         - 缓冲区管理
         *         - EOF检测
         *         - 边界条件处理
         * 
         *         ML/DL关联：
         *         - 序列数据的逐步读取
         *         - 流式数据处理
         */
        private int readByte() throws IOException {
            // 如果缓冲区已用完，重新填充
            if (ptr >= len) {
                // 从输入流读取数据到缓冲区
                len = in.read(buffer);
                // 重置指针
                ptr = 0;
                // 检查是否到达流末尾
                if (len <= 0)
                    // 返回-1表示已到达流末尾
                    return -1;
            }
            // 返回当前字节并移动指针
            return buffer[ptr++];
        }

        /**
         * 读取下一个整数
         * 
         * @return 读取的整数值
         * 
         *         面试要点：
         *         - 数字解析算法
         *         - 符号处理
         *         - 字符到数字的转换
         * 
         *         ML/DL关联：
         *         - 数据预处理中的类型转换
         *         - 特征提取中的数值解析
         */
        int nextInt() throws IOException {
            // 临时变量存储当前字符
            int c;
            // 跳过空白字符，直到找到数字或符号
            do {
                c = readByte();
            } while (c <= ' ' && c != -1);
            // 标记是否为负数
            boolean neg = false;
            // 检查符号
            if (c == '-') {
                // 设置负数标志
                neg = true;
                // 读取下一个字符
                c = readByte();
            }
            // 存储数值结果
            int val = 0;
            // 读取数字字符并转换为整数
            while (c > ' ' && c != -1) {
                // 将当前字符转换为数字并累加到结果中
                val = val * 10 + (c - '0');
                // 读取下一个字符
                c = readByte();
            }
            // 根据符号标志返回正数或负数
            return neg ? -val : val;
        }

        /**
         * 读取下一个字符串（仅包含小写字母）
         * 
         * @return 读取的字符串
         * 
         *         面试要点：
         *         - 字符串解析算法
         *         - 字符范围检查
         *         - StringBuilder的使用
         * 
         *         ML/DL关联：
         *         - 文本数据预处理
         *         - 序列数据的解析
         */
        String nextString() throws IOException {
            // 临时变量存储当前字符
            int c;
            // 跳过非小写字母字符，直到找到小写字母
            do {
                c = readByte();
                // 如果到达流末尾，返回null
                if (c == -1)
                    return null;
            } while (c < 'a' || c > 'z');
            // 使用StringBuilder构建字符串
            StringBuilder sb = new StringBuilder();
            // 读取连续的小写字母字符
            while (c >= 'a' && c <= 'z') {
                // 添加当前字符到字符串
                sb.append((char) c);
                // 读取下一个字符
                c = readByte();
                // 如果到达流末尾，跳出循环
                if (c == -1)
                    break;
            }
            // 返回构建的字符串
            return sb.toString();
        }

    }

}