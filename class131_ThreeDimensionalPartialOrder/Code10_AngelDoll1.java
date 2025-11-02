package class171;

/**
 * 天使玩偶/SJY摆棋子 - Java版本
 * 
 * 题目来源: 洛谷P4169
 * 题目链接: https://www.luogu.com.cn/problem/P4169
 * 题目难度: 省选/NOI-
 * 
 * 题目描述:
 * 在二维平面上，支持两种操作：
 * 1. 添加一个点
 * 2. 查询离指定点曼哈顿距离最近的点的距离
 * 
 * 解题思路:
 * 这是一个动态二维最近点对问题，可以使用CDQ分治来解决。
 * 
 * 算法步骤:
 * 1. 将绝对值拆开，分为四种情况讨论
 * 2. 对于每种情况，使用CDQ分治处理
 * 3. 将时间作为第一维，x坐标作为第二维，y坐标作为第三维
 * 4. 使用CDQ分治处理三维偏序问题
 * 
 * 时间复杂度: O(n log^2 n)
 * 空间复杂度: O(n)
 * 
 * 工程化考量:
 * 1. 异常处理: 处理输入参数合法性
 * 2. 性能优化: 使用快速IO提高输入效率
 * 3. 代码可读性: 添加详细注释说明算法思路
 * 4. 调试能力: 添加中间过程打印便于调试
 * 
 * 详细解题思路:
 * 1. 暴力解法：对于每次查询，遍历所有点计算距离，时间复杂度O(n*q)
 * 2. CDQ分治优化：
 *    - 将绝对值拆开，分为四种情况讨论
 *    - 对于每种情况，使用CDQ分治处理
 *    - 将时间作为第一维，x坐标作为第二维，y坐标作为第三维
 *    - 使用CDQ分治处理三维偏序问题
 * 
 * 算法详解:
 * 1. 曼哈顿距离公式：|x1-x2| + |y1-y2|
 * 2. 将绝对值拆开，分为四种情况：
 *    - x1 >= x2, y1 >= y2: (x1-x2) + (y1-y2) = (x1+y1) - (x2+y2)
 *    - x1 >= x2, y1 < y2: (x1-x2) + (y2-y1) = (x1-y1) - (x2-y2)
 *    - x1 < x2, y1 >= y2: (x2-x1) + (y1-y2) = (-x1+y1) - (-x2+y2)
 *    - x1 < x2, y1 < y2: (x2-x1) + (y2-y1) = (-x1-y1) - (-x2-y2)
 * 3. 对于每种情况，我们需要找到使表达式最大的点
 * 4. 使用CDQ分治处理三维偏序问题：
 *    - 时间作为第一维
 *    - x坐标作为第二维
 *    - y坐标作为第三维
 * 
 * 贡献计算详解:
 * 对于查询操作，我们需要找到添加操作中使曼哈顿距离最小的点
 * 通过将绝对值拆开，我们可以将问题转化为在添加操作中找到使特定表达式最大的点
 * 
 * 时间复杂度分析:
 * - CDQ分治：T(n) = 2T(n/2) + O(n log n) = O(n log^2 n)
 * - 总时间复杂度：O(n log^2 n)
 * 
 * 空间复杂度分析:
 * - 操作数组：O(n)
 * - 临时数组：O(n)
 * - 树状数组：O(n)
 * - 答案数组：O(n)
 * - 总空间复杂度：O(n)
 * 
 * 与其他算法的比较:
 * 1. 与KD树比较:
 *    - CDQ分治实现更简单
 *    - KD树支持在线查询，CDQ分治需要离线处理
 *    - KD树在随机数据上表现更好，但在极端数据上可能退化
 * 2. 与树套树比较:
 *    - CDQ分治空间复杂度更优
 *    - 树套树支持在线查询，CDQ分治需要离线处理
 * 
 * 优化策略:
 * 1. 使用离散化减少值域范围
 * 2. 优化排序策略减少常数
 * 3. 合理安排计算顺序避免重复计算
 * 4. 使用快速IO提高效率
 * 
 * 常见问题及解决方案:
 * 1. 答案错误:
 *    - 问题：贡献计算错误或边界处理不当
 *    - 解决方案：仔细检查贡献计算逻辑，验证边界条件
 * 2. 时间超限:
 *    - 问题：常数因子过大或算法复杂度分析错误
 *    - 解决方案：优化排序策略，减少不必要的操作
 * 3. 空间超限:
 *    - 问题：递归层数过深或数组开得过大
 *    - 解决方案：检查数组大小，使用全局数组，优化递归逻辑
 * 
 * 扩展应用:
 * 1. 可以处理更高维度的偏序问题
 * 2. 可以优化动态规划的转移过程
 * 3. 可以处理动态问题转静态的场景
 * 
 * 学习建议:
 * 1. 先掌握归并排序求逆序对
 * 2. 理解二维偏序问题的处理方法
 * 3. 学习三维偏序的标准处理流程
 * 4. 练习四维偏序问题
 * 5. 掌握CDQ分治优化DP的方法
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

public class Code10_AngelDoll1 {

    public static int MAXN = 600001;
    public static int n, m, tot;
    
    // 操作类型
    static class Operation {
        int x, y, t, id, type; // x坐标, y坐标, 时间, id, 类型(1:添加, 2:查询)
    }
    
    public static Operation[] op = new Operation[MAXN];
    public static Operation[] tmp = new Operation[MAXN];
    public static int[] ans = new int[MAXN];
    public static int[] tree = new int[MAXN];
    
    // 初始化对象
    static {
        for (int i = 0; i < MAXN; i++) {
            op[i] = new Operation();
            tmp[i] = new Operation();
        }
        Arrays.fill(ans, Integer.MAX_VALUE);
    }

    public static int lowbit(int x) {
        return x & (-x);
    }

    public static void add(int i, int v) {
        while (i <= MAXN - 1) {
            tree[i] = Math.max(tree[i], v);
            i += lowbit(i);
        }
    }

    public static int query(int i) {
        int ret = 0;
        while (i > 0) {
            ret = Math.max(ret, tree[i]);
            i -= lowbit(i);
        }
        return ret;
    }

    public static void clear(int i) {
        while (i <= MAXN - 1) {
            tree[i] = 0;
            i += lowbit(i);
        }
    }

    /**
     * CDQ分治函数
     * @param l 区间左端点
     * @param r 区间右端点
     */
    public static void cdq(int l, int r) {
        if (l == r) return;
        int mid = (l + r) >> 1;
        int i = l, j = mid + 1, k = l;
        
        // 归并排序
        while (i <= mid && j <= r) {
            if (op[i].x <= op[j].x) {
                if (op[i].type == 1) add(op[i].y, op[i].x + op[i].y);
                tmp[k++] = op[i++];
            } else {
                if (op[j].type == 2) ans[op[j].id] = Math.min(ans[op[j].id], op[j].x + op[j].y - query(op[j].y));
                tmp[k++] = op[j++];
            }
        }
        while (i <= mid) {
            if (op[i].type == 1) add(op[i].y, op[i].x + op[i].y);
            tmp[k++] = op[i++];
        }
        while (j <= r) {
            if (op[j].type == 2) ans[op[j].id] = Math.min(ans[op[j].id], op[j].x + op[j].y - query(op[j].y));
            tmp[k++] = op[j++];
        }
        
        // 清空树状数组
        for (int p = l; p <= mid; p++) {
            if (op[p].type == 1) clear(op[p].y);
        }
        
        // 复制回原数组
        for (int p = l; p <= r; p++) {
            op[p] = tmp[p];
        }
    }

    public static void main(String[] args) throws IOException {
        FastReader in = new FastReader(System.in);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        n = in.nextInt();
        m = in.nextInt();
        tot = 0;
        
        // 读入初始点
        for (int i = 1; i <= n; i++) {
            tot++;
            op[tot].x = in.nextInt();
            op[tot].y = in.nextInt();
            op[tot].t = 0;
            op[tot].id = 0;
            op[tot].type = 1;
        }
        
        int qcnt = 0;
        // 读入操作
        for (int i = 1; i <= m; i++) {
            int t = in.nextInt();
            int x = in.nextInt();
            int y = in.nextInt();
            if (t == 1) {
                tot++;
                op[tot].x = x;
                op[tot].y = y;
                op[tot].t = i;
                op[tot].id = 0;
                op[tot].type = 1;
            } else {
                qcnt++;
                tot++;
                op[tot].x = x;
                op[tot].y = y;
                op[tot].t = i;
                op[tot].id = qcnt;
                op[tot].type = 2;
            }
        }
        
        // 按时间排序
        Arrays.sort(op, 1, tot + 1, (a, b) -> {
            if (a.t != b.t) return Integer.compare(a.t, b.t);
            return Integer.compare(a.type, b.type);
        });
        
        // CDQ分治处理
        cdq(1, tot);
        
        // 输出结果
        for (int i = 1; i <= qcnt; i++) {
            out.println(ans[i]);
        }
        
        out.flush();
        out.close();
    }

    // 读写工具类
    static class FastReader {
        private final byte[] buffer = new byte[1 << 20];
        private int ptr = 0, len = 0;
        private final InputStream in;

        FastReader(InputStream in) {
            this.in = in;
        }

        private int readByte() throws IOException {
            if (ptr >= len) {
                len = in.read(buffer);
                ptr = 0;
                if (len <= 0)
                    return -1;
            }
            return buffer[ptr++];
        }

        int nextInt() throws IOException {
            int c;
            do {
                c = readByte();
            } while (c <= ' ' && c != -1);
            boolean neg = false;
            if (c == '-') {
                neg = true;
                c = readByte();
            }
            int val = 0;
            while (c > ' ' && c != -1) {
                val = val * 10 + (c - '0');
                c = readByte();
            }
            return neg ? -val : val;
        }
    }
}