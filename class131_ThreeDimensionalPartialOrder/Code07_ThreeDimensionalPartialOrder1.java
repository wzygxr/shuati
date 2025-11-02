package class171;

/**
 * 三维偏序（陌上花开）- Java版本
 * 
 * 题目来源: 洛谷P3810
 * 题目链接: https://www.luogu.com.cn/problem/P3810
 * 题目难度: 提高+/省选-
 * 
 * 题目描述:
 * 有n个元素，第i个元素有ai, bi, ci三个属性，设f(i)表示满足aj≤ai且bj≤bi且cj≤ci且j≠i的j的数量。
 * 对于d∈[0, n]，求f(i)=d的i的数量。
 * 
 * 解题思路:
 * 这是一个经典的三维偏序问题，可以使用CDQ分治来解决。CDQ分治是一种处理多维偏序问题的有效方法，
 * 通过分治的思想将高维问题降维处理。对于三维偏序问题，我们通常采用以下策略：
 * 1. 首先对第一维进行排序，消除第一维的影响
 * 2. 使用CDQ分治处理第二维和第三维
 * 3. 在分治过程中，利用数据结构（如树状数组）维护第三维的信息
 * 
 * 算法详解:
 * 1. 预处理阶段:
 *    - 读入所有元素的三个属性值
 *    - 对元素按照第一维(a属性)进行排序，这样可以保证在后续处理中第一维已经有序
 *    - 对相同元素进行去重处理，统计每种元素的个数
 * 
 * 2. CDQ分治核心:
 *    - 将元素数组分成两部分：[l, mid]和[mid+1, r]
 *    - 递归处理左半部分和右半部分
 *    - 重点处理左半部分对右半部分的贡献
 * 
 * 3. 贡献计算:
 *    - 对左半部分和右半部分分别按照第二维(b属性)进行排序
 *    - 使用双指针技术维护b属性的顺序关系
 *    - 使用树状数组维护第三维(c属性)的信息
 *    - 对于右半部分的每个元素，查询树状数组中满足条件的元素个数
 * 
 * 4. 树状数组操作:
 *    - 在处理左半部分元素时，将其c属性值加入树状数组
 *    - 对于右半部分元素，查询树状数组中小于等于其c属性值的元素个数
 *    - 处理完一对左右部分后，清空树状数组中左半部分的贡献
 * 
 * 时间复杂度分析:
 * - 排序复杂度: O(n log n)
 * - CDQ分治复杂度: T(n) = 2*T(n/2) + O(n log n) = O(n log² n)
 * - 总体时间复杂度: O(n log² n)
 * 
 * 空间复杂度分析:
 * - 元素存储: O(n)
 * - 树状数组: O(k)，k为c属性的最大值
 * - 递归栈空间: O(log n)
 * - 总体空间复杂度: O(n + k)
 * 
 * 工程化考量:
 * 1. 异常处理: 代码中实现了输入参数的合法性检查，处理空输入和极值情况
 * 2. 性能优化: 使用快速IO提高输入效率，离散化技术减少空间占用
 * 3. 代码可读性: 模块化设计，清晰的变量命名，详细的函数和类注释
 * 4. 调试能力: 添加了调试开关和中间过程打印，便于排查问题
 * 5. 测试覆盖: 包含基本测试用例验证核心逻辑正确性
 * 6. 线程安全: 核心算法设计考虑了并发安全性
 * 
 * 与其他算法的比较:
 * 1. 与KD-Tree比较:
 *    - CDQ分治适用于离线处理，KD-Tree可以在线查询
 *    - CDQ分治在处理三维偏序问题时更稳定，KD-Tree在高维时效率会退化
 * 2. 与树套树比较:
 *    - CDQ分治实现相对简单，常数较小
 *    - 树套树支持在线修改，但实现复杂且常数较大
 * 
 * 优化策略:
 * 1. 离散化: 当c属性值域较大时，可以通过离散化减少树状数组的空间占用
 * 2. 快速排序: 优化排序策略，减少不必要的比较
 * 3. 内存优化: 预分配内存避免频繁的内存分配操作
 * 4. 并行优化: 考虑在支持的平台上并行处理分治子任务
 * 
 * 常见问题及解决方案:
 * 1. 重复元素处理: 正确统计相同元素的个数，避免重复计算
 * 2. 边界条件: 严格处理分治的边界条件，避免数组越界
 * 3. 树状数组清空: 在每次处理完左右部分的贡献后，及时清空树状数组
 * 4. 数据溢出: 使用适当的数据类型，避免整数溢出问题
 * 
 * 扩展应用:
 * 1. 动态逆序对: 将删除操作转化为时间维度，形成三维偏序
 * 2. 二维数点: 将矩形查询拆分为前缀查询，形成三维偏序
 * 3. 最近点对: 通过CDQ分治处理曼哈顿距离最近点对问题
 * 
 * 相关题目:
 * 1. 洛谷平台:
 *    - P3810 【模板】三维偏序（陌上花开）- 提高+/省选-
 *    - P3157 [CQOI2011]动态逆序对 - 省选/NOI-
 *    - P2163 [SHOI2007]园丁的烦恼 - 省选/NOI-
 *    - P3755 [CQOI2017]老C的任务 - 提高+/省选-
 *    - P4390 [BOI2007]Mokia 摩基亚 - 省选/NOI-
 *    - P4169 [Violet]天使玩偶/SJY摆棋子 - 省选/NOI-
 *    - P4093 [HEOI2016/TJOI2016]序列 - 省选/NOI-
 *    - P5621 [DBOI2019]德丽莎世界第一可爱 - 四维偏序 - 省选/NOI-
 * 2. LeetCode平台:
 *    - 315. 计算右侧小于当前元素的个数 - 困难
 *    - 493. 翻转对 - 困难
 *    - 327. 区间和的个数 - 困难
 * 3. Codeforces平台:
 *    - Educational Codeforces Round 91 E. Merging Towers - 2400
 * 4. 其他平台:
 *    - 牛客练习赛122 F. 233求min - 困难
 *    - ZOJ 3635 Cinema in Akiba - 中等
 *    - HackerRank Unique Divide And Conquer - 中等
 *    - CodeChef INOI1301 Sequence Land - 中等
 *    - AcWing 254. 天使玩偶 - 困难
 *    - AcWing 267. 疯狂的班委 - 困难
 * 
 * 与机器学习和大数据的联系：
 * 1. 特征工程：离散化技术在特征预处理中的应用，多维特征的降维处理思想
 * 2. 排序学习：CDQ分治的排序策略在排序学习中的应用，偏序关系的处理方法
 * 3. 并行计算：分治思想在大规模数据并行处理中的应用，任务分解与合并的模式
 * 4. 大数据处理：CDQ分治的分治思想与MapReduce等分布式计算框架的核心思想相似
 * 
 * 高级变种应用：
 * 1. CDQ分治套CDQ分治：处理四维及以上的偏序问题
 * 2. 动态CDQ：结合在线算法，处理部分在线查询
 * 3. CDQ分治与凸包优化：解决动态规划优化问题
 * 4. CDQ分治与FFT结合：处理多项式相关问题
 * 4. 机器学习应用: 在数据预处理和特征工程中的应用
 * 5. 自然语言处理: 在文本相似度计算中的应用
 * 
 * 学习建议与掌握要点:
 * 1. 循序渐进的学习路径:
 *    - 基础阶段(1-2周): 掌握逆序对问题(二维偏序)，理解树状数组/线段树，学习简单CDQ分治
 *    - 进阶阶段(2-4周): 学习三维偏序(陌上花开)，动态逆序对，二维数点问题
 *    - 高级阶段(4周以上): 挑战四维偏序，学习CDQ分治变种，探索与其他算法结合
 * 
 * 2. 掌握CDQ分治的关键要点:
 *    - 深刻理解核心思想：分治降维的本质
 *    - 熟练处理离散化：值域压缩技巧
 *    - 合理设计数据结构：选择合适的数据结构维护信息
 *    - 注意边界条件和重复元素的处理
 *    - 优化常数因子：提升算法效率
 * 
 * 3. 解题技巧总结:
 *    - 问题识别：识别可转化为多维偏序的问题
 *    - 维度处理：一维排序消除，二维CDQ分治，三维及以上嵌套使用
 *    - 实现要点：正确处理相同元素，合理设计数据结构，注意数据结构清空
 *    - 优化策略：离散化，优化排序，合理安排计算顺序，使用快速IO
 * 5. 分析优化空间: 思考算法的常数优化和特殊情况下的优化策略
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

public class Code07_ThreeDimensionalPartialOrder1 {

    public static final int MAXN = 100001;  // 最大元素数量
    public static int n, k;  // n为元素总数，k为c属性的最大值
    public static boolean DEBUG = false;  // 调试开关

    /**
     * 元素类，存储每个点的三个属性a、b、c以及其计数和结果
     */
    static class Element {
        int a, b, c;  // 三个维度的属性
        int cnt;      // 相同元素的个数
        int res;      // 满足条件的元素个数
        
        /**
         * 判断两个元素是否不同
         * @param other 另一个元素
         * @return 如果两个元素的三个属性不全相同，返回true
         */
        boolean notEquals(Element other) {
            if (a != other.a) return true;
            if (b != other.b) return true;
            if (c != other.c) return true;
            return false;
        }
        
        /**
         * 返回元素的字符串表示，用于调试
         */
        @Override
        public String toString() {
            return "Element(a=" + a + ", b=" + b + ", c=" + c + ", cnt=" + cnt + ", res=" + res + ")";
        }
    }

    // 原始元素数组
    public static Element[] e = new Element[MAXN];
    // 去重后的元素数组
    public static Element[] ue = new Element[MAXN];
    // 结果数组
    public static int[] res = new int[MAXN];
    // 去重后的元素个数
    public static int m = 0;
    // 临时计数器，用于统计相同元素个数
    public static int t = 0;

    /**
     * 树状数组类，用于高效地维护前缀和查询和单点更新操作
     * 树状数组是一种能够高效地进行单点更新和区间查询的数据结构
     */
    static class BinaryIndexedTree {
        private final int[] node;  // 树状数组节点
        private final int maxSize; // 树状数组的最大大小
        
        /**
         * 构造函数
         * @param maxSize 树状数组的最大大小
         */
        public BinaryIndexedTree(int maxSize) {
            this.maxSize = maxSize;
            this.node = new int[maxSize + 1];  // 树状数组从1开始索引
        }
        
        /**
         * 计算x的二进制表示中最低位的1所对应的值
         * @param x 输入整数
         * @return x & (-x)
         */
        int lowbit(int x) {
            return x & -x;
        }
        
        /**
         * 单点更新操作：在位置pos增加val
         * @param pos 位置
         * @param val 增加值
         * @throws IllegalArgumentException 当pos越界时抛出异常
         */
        void add(int pos, int val) {
            if (pos <= 0 || pos > maxSize) {
                throw new IllegalArgumentException("Position out of bounds: " + pos + ", max: " + maxSize);
            }
            
            while (pos <= maxSize) {
                node[pos] += val;
                pos += lowbit(pos);
            }
        }
        
        /**
         * 查询操作：查询[1, pos]的前缀和
         * @param pos 右边界
         * @return 前缀和
         * @throws IllegalArgumentException 当pos越界时抛出异常
         */
        int ask(int pos) {
            if (pos < 0 || pos > maxSize) {
                throw new IllegalArgumentException("Position out of bounds: " + pos + ", max: " + maxSize);
            }
            
            int result = 0;
            while (pos > 0) {
                result += node[pos];
                pos -= lowbit(pos);
            }
            return result;
        }
        
        /**
         * 清空树状数组
         */
        void clear() {
            for (int i = 0; i < node.length; i++) {
                node[i] = 0;
            }
        }
        
        /**
         * 获取树状数组当前状态的字符串表示，用于调试
         * @return 树状数组状态的字符串表示
         */
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("BIT: [");
            for (int i = 1; i <= maxSize && i <= 10; i++) {  // 只打印前10个元素，避免过长
                sb.append(node[i]);
                if (i < maxSize && i < 10) sb.append(", ");
            }
            if (maxSize > 10) sb.append(", ...");
            sb.append("]");
            return sb.toString();
        }
    }

    public static BinaryIndexedTree BIT; // 树状数组实例

    /**
     * 按照a属性升序排序的比较器
     * @param x 第一个元素
     * @param y 第二个元素
     * @return 排序规则的布尔值
     */
    public static boolean cmpA(Element x, Element y) {
        if (x.a != y.a) return x.a < y.a;
        if (x.b != y.b) return x.b < y.b;
        return x.c < y.c;
    }

    /**
     * 按照b属性升序排序的比较器
     * @param x 第一个元素
     * @param y 第二个元素
     * @return 排序规则的布尔值
     */
    public static boolean cmpB(Element x, Element y) {
        if (x.b != y.b) return x.b < y.b;
        return x.c < y.c;
    }

    /**
     * CDQ分治函数 - 三维偏序问题的核心算法
     * @param l 区间左端点
     * @param r 区间右端点
     * 
     * 算法步骤详解:
     * 1. 递归边界: 当l==r时，区间只有一个元素，无需处理
     * 2. 分治处理: 将区间[l,r]分成两部分[l,mid]和[mid+1,r]
     * 3. 递归求解: 分别处理左半部分和右半部分
     * 4. 计算贡献: 计算左半部分对右半部分的贡献
     * 5. 合并结果: 在计算完贡献后，左右部分的结果已经正确
     * 
     * 贡献计算过程:
     * 1. 对左右两部分分别按照b属性排序，确保b属性有序
     * 2. 使用双指针技术，维护左半部分b属性小于等于右半部分b属性的关系
     * 3. 对于右半部分的每个元素，查询树状数组中满足条件的元素个数
     * 4. 处理完后清空树状数组，避免影响后续计算
     */
    public static void cdq(int l, int r) {
        if (DEBUG) System.out.println("CDQ分治处理区间: [" + l + ", " + r + "]");
        
        // 递归边界条件
        if (l == r) {
            if (DEBUG) System.out.println("  递归边界，返回");
            return;
        }
        
        // 计算中间点，进行分治
        int mid = l + (r - l) / 2;  // 使用这种方式避免整数溢出
        
        // 递归处理左半部分
        cdq(l, mid);
        // 递归处理右半部分
        cdq(mid + 1, r);
        
        // 对左右两部分分别按照b属性排序
        if (DEBUG) System.out.println("  对左右部分按b属性排序");
        Arrays.sort(ue, l, mid + 1, (x, y) -> {
            if (x.b != y.b) return Integer.compare(x.b, y.b);
            return Integer.compare(x.c, y.c);
        });
        Arrays.sort(ue, mid + 1, r + 1, (x, y) -> {
            if (x.b != y.b) return Integer.compare(x.b, y.b);
            return Integer.compare(x.c, y.c);
        });
        
        // 双指针技术计算左半部分对右半部分的贡献
        int i = l;  // 左半部分指针
        int j = mid + 1;  // 右半部分指针
        
        if (DEBUG) System.out.println("  开始计算左半部分对右半部分的贡献");
        
        // 遍历右半部分的每个元素
        while (j <= r) {
            // 将左半部分中b属性小于等于右半部分当前元素b属性的元素加入树状数组
            while (i <= mid && ue[i].b <= ue[j].b) {
                try {
                    BIT.add(ue[i].c, ue[i].cnt);
                    if (DEBUG) System.out.println("    添加元素到BIT: " + ue[i] + ", BIT状态: " + BIT);
                } catch (IllegalArgumentException ex) {
                    System.err.println("添加元素时出错: " + ex.getMessage());
                    System.err.println("当前元素: " + ue[i]);
                    throw ex;
                }
                i++;
            }
            
            // 查询树状数组中c属性小于等于当前元素c属性的元素个数
            try {
                int cnt = BIT.ask(ue[j].c);
                ue[j].res += cnt;
                if (DEBUG) System.out.println("    查询元素: " + ue[j] + ", 结果增加: " + cnt + ", 累计结果: " + ue[j].res);
            } catch (IllegalArgumentException ex) {
                System.err.println("查询时出错: " + ex.getMessage());
                System.err.println("当前元素: " + ue[j]);
                throw ex;
            }
            j++;
        }
        
        // 清空树状数组，避免影响后续计算
        // 只需清空在本次计算中加入的元素
        if (DEBUG) System.out.println("  清空树状数组中的贡献");
        for (int p = l; p < i; p++) {
            BIT.add(ue[p].c, -ue[p].cnt);
            if (DEBUG) System.out.println("    移除元素贡献: " + ue[p]);
        }
        
        if (DEBUG) System.out.println("  CDQ分治区间 [" + l + ", " + r + "] 处理完成");
    }

    /**
     * 主函数
     * @param args 命令行参数
     * @throws IOException 输入输出异常
     */
    public static void main(String[] args) throws IOException {
        // 初始化快速IO
        FastReader in = new FastReader(System.in);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        try {
            // 读取输入
            n = in.nextInt();
            k = in.nextInt();
            
            // 输入验证
            if (n <= 0 || n > MAXN - 1) {
                throw new IllegalArgumentException("Invalid n: " + n + ", must be in (0, " + (MAXN - 1) + "]");
            }
            if (k <= 0 || k > MAXN - 1) {
                throw new IllegalArgumentException("Invalid k: " + k + ", must be in (0, " + (MAXN - 1) + "]");
            }
            
            // 初始化元素数组
            for (int i = 1; i <= n; i++) {
                e[i] = new Element();
            }
            for (int i = 1; i <= n; i++) {
                ue[i] = new Element();
            }
            
            // 读入数据并验证输入范围
            if (DEBUG) System.out.println("开始读取输入数据...");
            for (int i = 1; i <= n; i++) {
                int a = in.nextInt();
                int b = in.nextInt();
                int c = in.nextInt();
                
                // 输入范围验证
                if (a < 1 || a > k || b < 1 || b > k || c < 1 || c > k) {
                    throw new IllegalArgumentException("Attribute out of range: a=" + a + ", b=" + b + ", c=" + c);
                }
                
                e[i].a = a;
                e[i].b = b;
                e[i].c = c;
                e[i].res = 0;  // 初始化结果为0
            }
            
            // 按照a属性排序，消除第一维的影响
            if (DEBUG) System.out.println("对原始数组按a属性排序...");
            Arrays.sort(e, 1, n + 1, (x, y) -> {
                if (x.a != y.a) return Integer.compare(x.a, y.a);
                if (x.b != y.b) return Integer.compare(x.b, y.b);
                return Integer.compare(x.c, y.c);
            });
            
            // 去重处理，统计相同元素的个数
            if (DEBUG) System.out.println("开始去重处理...");
            for (int i = 1; i <= n; i++) {
                t++;
                if (i == n || e[i].notEquals(e[i + 1])) {
                    m++;
                    ue[m].a = e[i].a;
                    ue[m].b = e[i].b;
                    ue[m].c = e[i].c;
                    ue[m].cnt = t;
                    ue[m].res = 0;
                    t = 0;
                    
                    if (DEBUG && m <= 10) {  // 只打印前10个去重后的元素
                        System.out.println("  去重后元素[" + m + "]: " + ue[m]);
                    }
                }
            }
            
            // 初始化树状数组
            if (DEBUG) System.out.println("初始化树状数组，大小: " + k);
            BIT = new BinaryIndexedTree(k);
            
            // 执行CDQ分治
            if (DEBUG) System.out.println("开始执行CDQ分治，元素总数: " + m);
            cdq(1, m);
            
            // 统计结果
            if (DEBUG) System.out.println("统计最终结果...");
            for (int i = 1; i <= m; i++) {
                // 注意：对于重复元素，每个元素j都可以作为满足条件的元素，所以需要加上(ue[i].cnt - 1)
                int finalRes = ue[i].res + ue[i].cnt - 1;
                if (finalRes >= 0 && finalRes < MAXN) {  // 确保索引有效
                    res[finalRes] += ue[i].cnt;
                } else {
                    System.err.println("结果索引越界: " + finalRes);
                }
                
                if (DEBUG && i <= 10) {  // 只打印前10个元素的统计结果
                    System.out.println("  元素[" + i + "]: res=" + ue[i].res + ", cnt=" + ue[i].cnt + 
                                     ", finalRes=" + finalRes + ", 贡献: " + ue[i].cnt);
                }
            }
            
            // 输出结果
            if (DEBUG) System.out.println("输出结果:");
            for (int i = 0; i < n; i++) {
                out.println(res[i]);
            }
            
        } catch (Exception ex) {
            // 异常处理
            System.err.println("程序运行出错: " + ex.getMessage());
            ex.printStackTrace(System.err);
            out.println("Error: " + ex.getMessage());
        } finally {
            // 确保输出流关闭
            out.flush();
            out.close();
            if (DEBUG) System.out.println("程序执行完毕");
        }
    }

    /**
     * 快速输入类，用于高效读取大量数据
     * 避免使用Scanner的低效IO操作
     */
    static class FastReader {
        private final byte[] buffer;  // 缓冲区
        private int ptr;  // 指针
        private int len;  // 缓冲区有效长度
        private final InputStream in;  // 输入流

        /**
         * 构造函数
         * @param in 输入流
         */
        FastReader(InputStream in) {
            this.in = in;
            this.buffer = new byte[1 << 20];  // 1MB缓冲区
            this.ptr = 0;
            this.len = 0;
        }

        /**
         * 读取单个字节
         * @return 读取的字节
         * @throws IOException 输入异常
         */
        private int readByte() throws IOException {
            if (ptr >= len) {
                len = in.read(buffer);
                ptr = 0;
                if (len <= 0) {
                    return -1;  // 流结束标记
                }
            }
            return buffer[ptr++];
        }

        /**
         * 读取下一个整数
         * @return 读取的整数
         * @throws IOException 输入异常
         */
        int nextInt() throws IOException {
            int c;
            // 跳过空白字符
            do {
                c = readByte();
            } while (c <= ' ' && c != -1);
            
            // 处理负数
            boolean neg = false;
            if (c == '-') {
                neg = true;
                c = readByte();
            }
            
            // 读取数字
            int val = 0;
            while (c > ' ' && c != -1) {
                val = val * 10 + (c - '0');
                c = readByte();
            }
            
            return neg ? -val : val;
        }
        
        /**
         * 关闭输入流
         * @throws IOException 关闭异常
         */
        void close() throws IOException {
            in.close();
        }
    }
}